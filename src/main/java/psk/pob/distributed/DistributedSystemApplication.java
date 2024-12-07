package psk.pob.distributed;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import psk.pob.distributed.communication.CommunicationService;
import psk.pob.distributed.communication.DistributedSystemManager;
import psk.pob.distributed.communication.algorithms.CommunicationAlgorithm;
import psk.pob.distributed.communication.algorithms.CommunicationAlgorithmFactory;
import psk.pob.distributed.models.Message;
import psk.pob.distributed.models.MessageType;
import psk.pob.distributed.models.Node;
import psk.pob.distributed.models.NodeRegistry;

//@Slf4j
@SpringBootApplication
public class DistributedSystemApplication {
  private static final Logger log = LoggerFactory.getLogger(DistributedSystemApplication.class);

  public static void main(String[] args) {
    File logDir = new File("./logs");
    if (!logDir.exists()) {
      logDir.mkdirs();
    }

    ApplicationContext context = SpringApplication.run(DistributedSystemApplication.class, args);
    CommunicationService communicationService = context.getBean(CommunicationService.class);

    NodeRegistry nodeRegistry = context.getBean(NodeRegistry.class);
    String LOCAL_HOST = "127.0.0.1";

    nodeRegistry.registerNode(new Node("1", LOCAL_HOST, 8081));
    nodeRegistry.registerNode(new Node("2", LOCAL_HOST, 8082));
    nodeRegistry.registerNode(new Node("3", LOCAL_HOST, 8083));

    CommunicationAlgorithmFactory factory = context.getBean(CommunicationAlgorithmFactory.class);
    CommunicationAlgorithm algorithm = factory.createAlgorithm("leader");

    DistributedSystemManager manager = context.getBean(DistributedSystemManager.class);
    manager.setCommunicationAlgorithm(algorithm);
    manager.initializeNodes(nodeRegistry.getAllNodes());

    // Start servers for each node
    for (Node node : nodeRegistry.getAllNodes()) {
      new Thread(() -> startServer(node.getPort(), nodeRegistry, communicationService)).start();
    }

    // Send a test message
    Node sourceNode = nodeRegistry.getAllNodes().get(0);
    Message message = new Message("1", "Receiver", "Payload", MessageType.REQUEST);
    manager.sendMessage(sourceNode, message);
  }

  private static void startServer(int port, NodeRegistry nodeRegistry, CommunicationService communicationService) {
    try (ServerSocket serverSocket = new ServerSocket(port)) {
      log.info("Server started on port {}", port);
      while (true) {
        Socket clientSocket = serverSocket.accept();
        log.info("Connection received on port {}", port);

        new Thread(() -> {
          try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {
            Message message = (Message) in.readObject();
            log.info("Message received on port {}: {}", port, message);

            processMessage(message, port, nodeRegistry, communicationService);
          } catch (IOException | ClassNotFoundException e) {
            log.error("Error while reading message: {}", e.getMessage());
          }
        }).start();
      }
    } catch (IOException e) {
      log.error("Error while starting server on port {}: {}", port, e.getMessage());
    }
  }

  private static void processMessage(Message message, int port, NodeRegistry nodeRegistry, CommunicationService communicationService) {
    log.info("Processing message on port {}: {}", port, message);
    Node senderNode = nodeRegistry.getNode(message.getSenderId());
    if (senderNode != null) {
      senderNode.setLastHeartbeatTime(System.currentTimeMillis());
      senderNode.setHealthy(true);
      log.info("Updated state of node {}}: Healthy=true, LastHeartbeatTime={}%n",
          senderNode.getId(), senderNode.getLastHeartbeatTime());
    } else {
      log.error("Unknown sender node: {}", message.getSenderId());
    }

    if (message.getType() == MessageType.REQUEST) {
      Message response = new Message(
          "Server on port " + port,
          message.getSenderId(),
          "Acknowledged",
          MessageType.RESPONSE
      );
      sendResponse(response, senderNode, communicationService);
    }

    if (message.getType() == MessageType.REQUEST) {
      forwardMessage(message, port, nodeRegistry, communicationService);
    }
  }

  private static void sendResponse(Message response, Node targetNode,
      CommunicationService communicationService) {
    try {
      if (targetNode != null) {
        communicationService.sendMessage(
            new Node("Server", "127.0.0.1", targetNode.getPort()),
            targetNode,
            response
        );
        log.info("Sent acknowledgment to {}", targetNode.getId());
      }
    } catch (Exception e) {
      log.error("Failed to send response to node {}: {}%n", targetNode.getId(),
          e.getMessage());
    }
  }

  private static void forwardMessage(Message message, int port, NodeRegistry nodeRegistry,
      CommunicationService communicationService) {
    List<Node> allNodes = nodeRegistry.getAllNodes();
    for (Node node : allNodes) {
      if (node.getPort() != port) { // Don't forward back to the original sender
        try {
          log.info("Forwarding message to node {}", node.getId());
          communicationService.sendMessage(
              new Node("Server", "127.0.0.1", port),
              node,
              message
          );
        } catch (Exception e) {
          log.error("Failed to forward message to node {}: {}%n", node.getId(),
              e.getMessage());
        }
      }
    }
  }
}
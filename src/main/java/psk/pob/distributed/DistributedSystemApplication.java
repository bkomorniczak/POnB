package psk.pob.distributed;

import static psk.pob.distributed.models.MessageType.REQUEST;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import psk.pob.distributed.communication.DistributedSystemManager;
import psk.pob.distributed.communication.algorithms.CommunicationAlgorithm;
import psk.pob.distributed.communication.algorithms.CommunicationAlgorithmFactory;
import psk.pob.distributed.models.Message;
import psk.pob.distributed.models.MessageType;
import psk.pob.distributed.models.Node;
import psk.pob.distributed.models.NodeRegistry;

@Slf4j
@SpringBootApplication
public class DistributedSystemApplication {

  public static void main(String[] args) {
    ApplicationContext context = SpringApplication.run(DistributedSystemApplication.class, args);

    NodeRegistry nodeRegistry = context.getBean(NodeRegistry.class);
    String LOCAL_HOST = "127.0.0.1";

    nodeRegistry.registerNode(new Node("1", LOCAL_HOST, 8081));
    nodeRegistry.registerNode(new Node("2", LOCAL_HOST, 8082));
    nodeRegistry.registerNode(new Node("3", LOCAL_HOST, 8083));

    CommunicationAlgorithmFactory factory = context.getBean(CommunicationAlgorithmFactory.class);
    CommunicationAlgorithm algorithm = factory.createAlgorithm("broadcast");

    DistributedSystemManager manager = context.getBean(DistributedSystemManager.class);
    manager.setCommunicationAlgorithm(algorithm);
    manager.initializeNodes(nodeRegistry.getAllNodes());

    // Start servers for each node
    for (Node node : nodeRegistry.getAllNodes()) {
      new Thread(() -> startServer(node.getPort())).start();
    }

    // Send a test message
    Node sourceNode = nodeRegistry.getAllNodes().get(0);
    Message message = new Message("Sender", "Receiver", "Payload", MessageType.REQUEST);
    manager.sendMessage(sourceNode, message);
  }

  private static void startServer(int port) {
    try (ServerSocket serverSocket = new ServerSocket(port)) {
      System.out.println("Server started on port " + port);
      while (true) {
        Socket clientSocket = serverSocket.accept();
        System.out.println("Connection received on port " + port);

        // Handle incoming messages
        new Thread(() -> {
          try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {
            Message message = (Message) in.readObject();
            System.out.println("Message received on port " + port + ": " + message);

            // Process the message (this could include updating state, forwarding, etc.)
            processMessage(message, port);
          } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error while reading message: " + e.getMessage());
            e.printStackTrace();
          }
        }).start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void processMessage(Message message, int port) {
    System.out.printf("Processing message on port %d: %s%n", port, message);
    // Add logic for processing the message here
    // e.g., updating node states, replying to sender, etc.
  }


}


package psk.pob.distributed.tcp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import psk.pob.distributed.models.Message;
import psk.pob.distributed.models.Node;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;


@Slf4j
public class ThreadedTCPServer {

  public static void main(String[] args) {
    int port = 9090;
    String identifier;
    String configFile = "nodes.json";

    if (args.length > 0) {
      identifier = args[0];
    } else {
      identifier = "Server1";
    }
    if (args.length > 1) {
      port = Integer.parseInt(args[1]);
    }
    if (args.length > 2) {
      configFile = args[2];
    }

    List<Node> nodes = loadNodes();
    log.info("Loaded nodes: {}", nodes);

    try (ServerSocket serverSocket = new ServerSocket(port)) {
      log.info("Server {} is running on port {}", identifier, port);

      while (true) {
        Socket clientSocket = serverSocket.accept();
        log.info("New client connected: {}", clientSocket.getInetAddress());

        // Handle the client in a new thread
        new Thread(() -> handleClient(clientSocket, nodes, identifier)).start();
      }
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
  }

  private static List<Node> loadNodes() {
    ObjectMapper objectMapper = new ObjectMapper();
    try (InputStream inputStream = ThreadedTCPServer.class.getClassLoader().getResourceAsStream("nodes.json")) {
      if (inputStream == null) {
        throw new RuntimeException("nodes.json not found in classpath");
      }
      List<Node> nodes = objectMapper.readValue(inputStream, new TypeReference<List<Node>>() {});
      System.out.println("Loaded nodes: " + nodes);
      return nodes;
    } catch (IOException e) {
      throw new RuntimeException("Failed to load node configurations from nodes.json", e);
    }
  }

  private static void handleClient(Socket clientSocket, List<Node> nodes, String currentIdentifier) {
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(clientSocket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(
            new OutputStreamWriter(clientSocket.getOutputStream()))) {

      ObjectMapper objectMapper = new ObjectMapper();

      String clientMessage;
      while ((clientMessage = reader.readLine()) != null) {
        System.out.println("Received from client: " + clientMessage);

        try {
          Message message = objectMapper.readValue(clientMessage, Message.class);

          if ("error".equals(message.getType())) {
            System.out.println("Injecting error as requested by the client.");
            writer.write("ERROR: Simulated server error response");
            writer.newLine();
            writer.flush();
            continue;
          }

          Message responseMessage = new Message("Server", message.getSenderId(),
              "Acknowledged: " + message.getPayload(), message.getType());
          String jsonResponse = objectMapper.writeValueAsString(responseMessage);

          writer.write(jsonResponse);
          writer.newLine();
          writer.flush();

          broadcastMessage(message, nodes, currentIdentifier);

        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
          System.err.println("Malformed message received: " + clientMessage);
          writer.write("ERROR: Malformed message");
          writer.newLine();
          writer.flush();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        clientSocket.close();
        System.out.println("Client disconnected.");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }



  private static void broadcastMessage(Message message, List<Node> nodes,
      String currentIdentifier) {
    for (Node node : nodes) {
      if (node.getId().equals(currentIdentifier)) {
        continue;
      }

      try (Socket socket = new Socket(node.getHost(), node.getPort());
          BufferedWriter writer = new BufferedWriter(
              new OutputStreamWriter(socket.getOutputStream()))) {

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonMessage = objectMapper.writeValueAsString(message);

        writer.write(jsonMessage);
        writer.newLine();
        writer.flush();

        System.out.println("Broadcasted to node: " + node.getId());
      } catch (IOException e) {
        System.err.println(
            "Failed to send message to node " + node.getId() + ": " + e.getMessage());
      }
    }
  }
}
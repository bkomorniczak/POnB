package psk.pob.distributed.tcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import psk.pob.distributed.models.Message;

import java.io.*;
import java.net.Socket;
import psk.pob.distributed.models.MessageType;

public class TCPClient {
  public static void main(String[] args) {
    String host = "localhost";
    int port = 9090;

    ObjectMapper objectMapper = new ObjectMapper();

    try (Socket socket = new Socket(host, port);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

      sendNormalMessage(writer, reader, objectMapper);

      sendMalformedMessage(writer);
      sendMessageToInvalidRecipient(writer, reader, objectMapper);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void sendNormalMessage(BufferedWriter writer, BufferedReader reader, ObjectMapper objectMapper) throws IOException {
    Message message = new Message("Client", "Server1", "Hello from client", MessageType.REQUEST);
    String jsonMessage = objectMapper.writeValueAsString(message);

    writer.write(jsonMessage);
    writer.newLine();
    writer.flush();
    System.out.println("Sent: " + jsonMessage);

    String response = reader.readLine();
    if (response != null) {
      System.out.println("Received: " + response);
    } else {
      System.out.println("No response received.");
    }
  }

  private static void sendMalformedMessage(BufferedWriter writer) throws IOException {
    if (Math.random() < 0.3) { // 30% chance to inject a malformed message
      System.out.println("Sending malformed message...");
      writer.write("{invalid-json");
      writer.newLine();
      writer.flush();
      System.out.println("Malformed message sent.");
    }
  }

  private static void sendMessageToInvalidRecipient(BufferedWriter writer, BufferedReader reader, ObjectMapper objectMapper) throws IOException {
    if (Math.random() < 0.3) { // 30% chance to send to an invalid recipient
      Message invalidMessage = new Message("Client", "NonExistentServer", "Message to nowhere", MessageType.ERROR);
      String jsonMessage = objectMapper.writeValueAsString(invalidMessage);

      writer.write(jsonMessage);
      writer.newLine();
      writer.flush();
      System.out.println("Sent to invalid recipient: " + jsonMessage);

      String response = reader.readLine();
      if (response != null) {
        System.out.println("Received: " + response);
      } else {
        System.out.println("No response received for invalid recipient.");
      }
    }
  }
}

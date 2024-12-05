package psk.pob.distributed.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import psk.pob.distributed.models.Node;

public class CommunicationManager {

  public void sendMessage(Node node, String message) {
    try (Socket socket = new Socket(node.getIpAddress(), node.getPort());
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
      out.println(message);
    } catch (IOException e) {
      System.err.println("Failed to send message to node: " + node.getId());
    }
  }

  public String receiveMessage(Node node) {
    try (Socket socket = new Socket(node.getIpAddress(), node.getPort());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
      return in.readLine();
    } catch (IOException e) {
      System.err.println("Failed to receive message from node: " + node.getId());
      return null;
    }
  }
}


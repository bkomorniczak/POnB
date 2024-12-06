package psk.pob.distributed.communication;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import psk.pob.distributed.models.Node;

//Manages low-level communication details
@Slf4j
@Component
public class CommunicationManager {

  public void sendMessage(Node node, String message) {
    try (Socket socket = new Socket(node.getIpAddress(), node.getPort());
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
      out.println(message);
    } catch (IOException e) {
      log.error("Failed to send message to node: {}", node.getId());
    }
  }

  public void receiveMessage(String message, Node sender) {
    if ("HEARTBEAT_RESPONSE".equals(message)) {
      sender.setHealthy(true);
      sender.setLastHeartbeatTime(System.currentTimeMillis());
    } else {
      log.info("Received message: {} from node: {}", message, sender);
    }
  }
  public void handleIncomingMessages() {
    while (true) {
      String incomingMessage = listenForMessages();
      Node sender = identifySender(incomingMessage);
      receiveMessage(incomingMessage, sender);
    }
  }
}


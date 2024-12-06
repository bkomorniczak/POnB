package psk.pob.distributed.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import psk.pob.distributed.models.Message;

@Component
public class ServerHandler implements Runnable {

  private final int port;
  private final CommunicationService communicationService;

  public ServerHandler(@Value("${server.port}") int port, CommunicationService communicationService) {
    this.port = port;
    this.communicationService = communicationService;
  }

  @Override
  public void run() {
    try (ServerSocket serverSocket = new ServerSocket(port)) {
      while (true) {
        Socket clientSocket = serverSocket.accept();
        new Thread(() -> handleClient(clientSocket)).start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void handleClient(Socket clientSocket) {
    try {
      Message message = communicationService.receiveMessage(clientSocket);
      processMessage(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void processMessage(Message message) {
    switch (message.getType()) {
      case REQUEST:
        System.out.println("Processing request: " + message.getPayload());
        break;
      case RESPONSE:
        System.out.println("Processing response: " + message.getPayload());
        break;
      case ERROR:
        System.err.println("Error received: " + message.getPayload());
        break;
      default:
        System.out.println("Unknown message type: " + message.getType());
        break;
    }
  }
}



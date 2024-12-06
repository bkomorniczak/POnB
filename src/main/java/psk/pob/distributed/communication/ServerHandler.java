package psk.pob.distributed.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import psk.pob.distributed.models.Message;

@Slf4j
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
      log.error(e.getMessage(), e);
    }
  }

  private void handleClient(Socket clientSocket) {
    try {
      Message message = communicationService.receiveMessage(clientSocket);
      processMessage(message);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
  }

  private void processMessage(Message message) {
    switch (message.getType()) {
      case REQUEST:
        log.info("Processing request: {}", message.getPayload());
        break;
      case RESPONSE:
        log.info("Processing response: {}", message.getPayload());
        break;
      case ERROR:
        log.error("Error received: {}", message.getPayload());
        break;
      default:
        log.warn("Unknown message type: {}", message.getType());
        break;
    }
  }
}



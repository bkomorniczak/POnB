package psk.pob.distributed.communication;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import psk.pob.distributed.controller.MetricsService;
import psk.pob.distributed.models.Message;
import psk.pob.distributed.models.Node;

//provides higher level communication functionalities
@Slf4j
@Component
public class CommunicationService {

  private final Random random = new Random();

  private final MetricsService metricsService;

  public CommunicationService(MetricsService metricsService) {
    this.metricsService = metricsService;
  }

  public void sendMessage(Node source, Node target, Message message) {
    if ( random.nextDouble() <0.2 ) {
      log.info("Dropping message from {} to {}", source.getId(), target.getId());
      return;
    }
    try (Socket socket = new Socket(target.getHost(), target.getPort());
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
      log.info("Sending message from {} to {}", source.getId(), target.getId());
      out.writeObject(message);
      metricsService.incrementMessagesSent();
    } catch (IOException e) {
      log.error("Failed to send message from {} to {}: {}", source.getId(), target.getId(), e.getMessage());
    }
  }

  public Message receiveMessage(Socket clientSocket) throws IOException {
    try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
      String jsonMessage = in.readLine();
      metricsService.incrementMessagesReceived();
      return deserializeMessage(jsonMessage);
    }
  }

  private Message deserializeMessage(String jsonMessage) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.readValue(jsonMessage, Message.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to parse message", e);
    }
  }
}



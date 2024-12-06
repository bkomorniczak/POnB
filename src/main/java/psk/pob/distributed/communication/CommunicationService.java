package psk.pob.distributed.communication;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import psk.pob.distributed.models.Message;
import psk.pob.distributed.models.Node;

//provides higher level communication functionalities
@Slf4j
@Component
public class CommunicationService {

  public CommunicationService() {
  }

  public void sendMessage(Node source, Node target, Message message) {
    try (Socket socket = new Socket(target.getHost(), target.getPort());
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
      log.info("Sending message from {} to {}", source.getId(), target.getId());
      out.writeObject(message);
    } catch (IOException e) {
      log.error("Failed to send message from {} to {}: {}", source.getId(), target.getId(), e.getMessage());
    }
  }

  public Message receiveMessage(Socket clientSocket) throws IOException {
    try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
      String jsonMessage = in.readLine();
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



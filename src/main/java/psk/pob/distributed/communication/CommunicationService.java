package psk.pob.distributed.communication;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import org.springframework.stereotype.Service;
import psk.pob.distributed.models.Message;
import psk.pob.distributed.models.Node;

//provides higher level communication functionalities
@Service
public class CommunicationService {
  private final CommunicationManager communicationManager;

  public CommunicationService(CommunicationManager communicationManager) {
    this.communicationManager = communicationManager;
  }

  public boolean sendHeartbeat(Node node) {
    try {
      communicationManager.sendMessage(node, "HEARTBEAT");
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public Message receiveMessage(Socket clientSocket) throws IOException {
    try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
      String jsonMessage = in.readLine(); // Assuming one line per message
      // Deserialize JSON to a Message object
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



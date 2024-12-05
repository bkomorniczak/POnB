package psk.pob.distributed.communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.springframework.stereotype.Service;
import psk.pob.distributed.models.Message;
import psk.pob.distributed.models.Node;

@Service
public class CommunicationService {

  private final ObjectMapper objectMapper = new ObjectMapper();

  public void sendMessage(Node node, Message message) throws IOException {
    try (Socket socket = new Socket(node.getHost(), node.getPort());
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
      String jsonMessage = objectMapper.writeValueAsString(message);
      out.println(jsonMessage);
    }
  }

  public Message receiveMessage(Socket socket) throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    String jsonMessage = in.readLine();
    return objectMapper.readValue(jsonMessage, Message.class);
  }
}

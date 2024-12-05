package psk.pob.distributed.communication;


import org.springframework.stereotype.Service;
import psk.pob.distributed.models.Node;

@Service
public class CommunicationService {

  private final CommunicationManager communicationManager;

  public CommunicationService(CommunicationManager communicationManager) {
    this.communicationManager = communicationManager;
  }

  public void sendMessage(Node node, String message) {
    communicationManager.sendMessage(node, message);
  }

  public void listenForMessages() {
    communicationManager.handleIncomingMessages();
  }

}


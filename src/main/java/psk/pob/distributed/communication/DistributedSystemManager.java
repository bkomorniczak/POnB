package psk.pob.distributed.communication;

import java.util.List;
import org.springframework.stereotype.Component;
import psk.pob.distributed.communication.algorithms.CommunicationAlgorithm;
import psk.pob.distributed.models.Message;
import psk.pob.distributed.models.Node;
import psk.pob.distributed.models.NodeRegistry;


@Component
public class DistributedSystemManager {
  private CommunicationAlgorithm communicationAlgorithm;

  public DistributedSystemManager(CommunicationAlgorithm algorithm) {
    this.communicationAlgorithm = algorithm;
  }

  public void initializeNodes(List<Node> nodes) {
    communicationAlgorithm.initialize(nodes);
  }

  public void sendMessage(Node source, Message message) {
    List<Node> allNodes = NodeRegistry.getInstance().getAllNodes();
    communicationAlgorithm.communicate(source, allNodes, message);
  }

  public void setCommunicationAlgorithm(CommunicationAlgorithm algorithm) {
    this.communicationAlgorithm = algorithm;
  }
}


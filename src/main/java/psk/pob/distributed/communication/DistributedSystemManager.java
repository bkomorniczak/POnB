package psk.pob.distributed.communication;

import java.util.List;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import psk.pob.distributed.communication.algorithms.CommunicationAlgorithm;
import psk.pob.distributed.models.Message;
import psk.pob.distributed.models.Node;
import psk.pob.distributed.models.NodeRegistry;


@Component
public class DistributedSystemManager {
  private final NodeRegistry nodeRegistry;
  private final CommunicationService communicationService;
  private CommunicationAlgorithm communicationAlgorithm;

  @Autowired
  public DistributedSystemManager(
      NodeRegistry nodeRegistry,
      CommunicationService communicationService,
      @Qualifier("broadcastAlgorithm") CommunicationAlgorithm communicationAlgorithm
  ) {
    this.nodeRegistry = nodeRegistry;
    this.communicationService = communicationService;
    this.communicationAlgorithm = communicationAlgorithm;
  }

  public void setCommunicationAlgorithm(CommunicationAlgorithm communicationAlgorithm) {
    this.communicationAlgorithm = communicationAlgorithm;
  }

  public void initializeNodes(List<Node> nodes) {
    nodeRegistry.registerNodes(nodes);
    communicationAlgorithm.initialize(nodes);
  }

  public void sendMessage(Node source, Message message) {
    List<Node> allNodes = nodeRegistry.getAllNodes();
    communicationAlgorithm.communicate(source, allNodes, message);
  }
}

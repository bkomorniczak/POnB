package psk.pob.distributed.communication;

import java.util.List;
import org.springframework.stereotype.Service;
import psk.pob.distributed.models.Message;
import psk.pob.distributed.models.MessageType;
import psk.pob.distributed.models.Node;

@Service
public class NodeHealthService {

  private final CommunicationService communicationService;
  private final DistributedSystemManager systemManager;

  public NodeHealthService(CommunicationService communicationService, DistributedSystemManager systemManager) {
    this.communicationService = communicationService;
    this.systemManager = systemManager;
  }

  public void startMonitoring(List<Node> nodes) {
    nodes.forEach(node -> {
      try {
        Message heartbeat = new Message("selfId", node.getId(), "heartbeat", MessageType.REQUEST);
        communicationService.sendMessage(node, String.valueOf(heartbeat));
        systemManager.updateNodeStatus(node, true);
      } catch (Exception e) {
        systemManager.updateNodeStatus(node, false);
      }
    });
  }
}

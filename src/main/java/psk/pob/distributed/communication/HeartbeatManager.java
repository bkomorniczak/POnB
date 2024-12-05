package psk.pob.distributed.communication;

import psk.pob.distributed.models.Node;
import psk.pob.distributed.models.NodeRegistry;

public class HeartbeatManager {
  private final NodeRegistry nodeRegistry;

  public HeartbeatManager(NodeRegistry nodeRegistry) {
    this.nodeRegistry = nodeRegistry;
  }

  public void sendHeartbeat(Node node) {
    try {
      CommunicationManager communicationManager = new CommunicationManager();
      communicationManager.sendMessage(node, "HEARTBEAT");
      node.updateHeartbeatTime(); // Update timestamp on successful send
    } catch (Exception e) {
      nodeRegistry.updateNodeHealth(node.getId(), false);
    }
  }

  public void checkNodeHealth() {
    long currentTime = System.currentTimeMillis();
    for (Node node : nodeRegistry.getAllNodes()) {
      if (currentTime - node.getLastHeartbeatTime() > 5000) {
        nodeRegistry.updateNodeHealth(node.getId(), false);
      }
    }
  }
}

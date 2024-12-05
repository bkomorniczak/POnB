package psk.pob.distributed.communication;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import psk.pob.distributed.models.Node;
import psk.pob.distributed.models.NodeRegistry;

public class HeartbeatManager {
  private final NodeRegistry nodeRegistry;
  private final CommunicationService communicationService;

  public HeartbeatManager(NodeRegistry nodeRegistry, CommunicationService communicationService) {
    this.nodeRegistry = nodeRegistry;
    this.communicationService = communicationService;
  }

  public void startHeartbeat() {
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    scheduler.scheduleAtFixedRate(() -> {
      for (Node node : nodeRegistry.getAllNodes()) {
        boolean isHealthy = sendHeartbeat(node);
        node.setHealthy(isHealthy);
        node.setLastHeartbeatTime(System.currentTimeMillis());
      }
    }, 0, 5, TimeUnit.SECONDS);
  }

  private boolean sendHeartbeat(Node node) {
    try {
      communicationService.sendMessage(node, "HEARTBEAT");
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}

package psk.pob.distributed.communication;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import psk.pob.distributed.models.Node;
import psk.pob.distributed.models.NodeRegistry;

// Handles periodic health checks od nodes
public class HeartbeatManager {
  private final NodeRegistry nodeRegistry;
  private final CommunicationService communicationService;
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  public HeartbeatManager(NodeRegistry nodeRegistry, CommunicationService communicationService) {
    this.nodeRegistry = nodeRegistry;
    this.communicationService = communicationService;
  }

  public void start() {
    scheduler.scheduleAtFixedRate(this::checkNodesHealth, 0, 5, TimeUnit.SECONDS);
  }

  private void checkNodesHealth() {
    for (Node node : nodeRegistry.getAllNodes()) {
      boolean isHealthy = communicationService.sendHeartbeat(node);
      node.setHealthy(isHealthy);
      node.setLastHeartbeatTime(System.currentTimeMillis());
    }
  }
}

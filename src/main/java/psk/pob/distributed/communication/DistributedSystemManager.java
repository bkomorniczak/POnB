package psk.pob.distributed.communication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import psk.pob.distributed.models.Message;
import psk.pob.distributed.models.MessageType;
import psk.pob.distributed.models.Node;

@Service
public class DistributedSystemManager {

  private final LoadBalancerService loadBalancerService;
  private final NodeHealthService nodeHealthService;
  private final CommunicationService communicationService;
  private final List<Node> nodes;

  public DistributedSystemManager(LoadBalancerService loadBalancerService,
      NodeHealthService nodeHealthService,
      CommunicationService communicationService) {
    this.loadBalancerService = loadBalancerService;
    this.nodeHealthService = nodeHealthService;
    this.communicationService = communicationService;
    this.nodes = new ArrayList<>();
  }

  public void initialize() {
    nodes.add(new Node("Node1", "127.0.0.1", 8080));
    nodes.add(new Node("Node2", "127.0.0.1", 8081));
    nodeHealthService.startMonitoring(nodes);
  }

  public void processRequest(String request) {
    try {
      Node nextNode = loadBalancerService.getNextNode(getHealthyNodes());
      Message message = new Message("selfId", nextNode.getId(), request, MessageType.REQUEST);
      communicationService.sendMessage(nextNode, message);
    } catch (IllegalStateException e) {
      System.err.println("No healthy nodes available: " + e.getMessage());
    } catch (IOException e) {
      System.err.println("Failed to send message: " + e.getMessage());
    }
  }

  public List<Node> getHealthyNodes() {
    return nodes.stream()
        .filter(Node::isHealthy)
        .toList();
  }

  public void updateNodeStatus(Node node, boolean isHealthy) {
    node.setHealthy(isHealthy);
  }
}

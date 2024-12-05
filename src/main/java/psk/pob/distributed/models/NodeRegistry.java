package psk.pob.distributed.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeRegistry {
  private final Map<String, Node> nodes = new HashMap<>();

  public void registerNode(String id, String host, int port) {
    Node node = new Node(id, host, port);
    nodes.put(id, node);
  }

  public Node getNode(String id) {
    return nodes.get(id);
  }

  public List<Node> getAllNodes() {
    return new ArrayList<>(nodes.values());
  }

  public void updateNodeHealth(String id, boolean isHealthy) {
    Node node = nodes.get(id);
    if (node != null) {
      node.setHealthy(isHealthy);
    }
  }

  public void addNewNode(Node node) {
    nodes.registerNode(node);
    System.out.println("Node registered: " + node);
  }

  public Node findNodeById(String nodeId) {
    return nodes.getNode(nodeId);
  }

}

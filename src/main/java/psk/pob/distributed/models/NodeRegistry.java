package psk.pob.distributed.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

//maintains registry of active nodes
@Service
public class NodeRegistry {
  private final Map<String, Node> nodes = new HashMap<>();

  public void registerNode(Node node) {
    nodes.put(node.getId(), node);
  }

  public List<Node> getAllNodes() {
    return new ArrayList<>(nodes.values());
  }

  public Node getNode(String nodeId) {
    return nodes.get(nodeId);
  }

  public void registerNodes(List<Node> multipleNodes) {
    for (Node node : multipleNodes) {
      nodes.put(node.getId(), node);
    }
  }
}

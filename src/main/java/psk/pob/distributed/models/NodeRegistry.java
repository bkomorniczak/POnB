package psk.pob.distributed.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//maintains registry of active nodes
public class NodeRegistry {
  private final Map<String, Node> nodes = new ConcurrentHashMap<>();

  public void registerNode(Node node) {
    nodes.put(node.getId(), node);
  }

  public Node getNode(String nodeId) {
    return nodes.get(nodeId);
  }

  public Collection<Node> getAllNodes() {
    return nodes.values();
  }
}


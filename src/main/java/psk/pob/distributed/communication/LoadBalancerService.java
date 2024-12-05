package psk.pob.distributed.communication;

import java.util.List;
import org.springframework.stereotype.Service;
import psk.pob.distributed.models.Node;

@Service
public class LoadBalancerService {

  private int nextServerIndex = 0;

  public Node getNextNode(List<Node> nodes) {
    if (nodes.isEmpty()) {
      throw new IllegalStateException("No available nodes");
    }
    Node selectedNode = nodes.get(nextServerIndex);
    nextServerIndex = (nextServerIndex + 1) % nodes.size();
    return selectedNode;
  }
}


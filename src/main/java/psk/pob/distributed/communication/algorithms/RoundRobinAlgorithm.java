package psk.pob.distributed.communication.algorithms;

import java.util.List;
import psk.pob.distributed.communication.CommunicationService;
import psk.pob.distributed.models.Message;
import psk.pob.distributed.models.Node;

public class RoundRobinAlgorithm implements CommunicationAlgorithm {
  private int currentIndex = 0;

  @Override
  public void communicate(Node source, List<Node> targets, Message message) {
    if (targets.isEmpty()) return;

    Node target = targets.get(currentIndex);
    System.out.println("Sending message from " + source.getId() + " to " + target.getId());
    CommunicationService.getInstance().sendMessage(source, target, message);

    currentIndex = (currentIndex + 1) % targets.size();
  }

  @Override
  public void initialize(List<Node> nodes) {
    // No specific setup needed for round-robin
  }
}

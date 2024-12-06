package psk.pob.distributed.communication.algorithms;

import java.util.List;
import psk.pob.distributed.communication.CommunicationService;
import psk.pob.distributed.models.Message;
import psk.pob.distributed.models.Node;

public class LeaderBasedAlgorithm implements CommunicationAlgorithm {
  private Node leader;

  @Override
  public void communicate(Node source, List<Node> targets, Message message) {
    if (!source.equals(leader)) {
      System.out.println("Forwarding message from " + source.getId() + " to leader " + leader.getId());
      CommunicationService.getInstance().sendMessage(source, leader, message);
    }

    for (Node target : targets) {
      if (!target.equals(leader)) {
        System.out.println("Leader " + leader.getId() + " forwarding message to " + target.getId());
        CommunicationService.getInstance().sendMessage(leader, target, message);
      }
    }
  }

  @Override
  public void initialize(List<Node> nodes) {
    this.leader = nodes.get(0); // Static leader selection; can be dynamic
    System.out.println("Leader selected: " + leader.getId());
  }
}

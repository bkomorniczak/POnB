package psk.pob.distributed.communication.algorithms;

import java.util.List;
import psk.pob.distributed.communication.CommunicationService;
import psk.pob.distributed.models.Message;
import psk.pob.distributed.models.Node;

public class BroadcastAlgorithm implements CommunicationAlgorithm {

  @Override
  public void communicate(Node source, List<Node> targets, Message message) {
    for (Node target : targets) {
      if (!target.equals(source)) {
        System.out.println("Broadcasting message from " + source.getId() + " to " + target.getId());
        CommunicationService.getInstance().sendMessage(source, target, message);
      }
    }
  }

  @Override
  public void initialize(List<Node> nodes) {
    // No specific setup needed for broadcast
  }
}



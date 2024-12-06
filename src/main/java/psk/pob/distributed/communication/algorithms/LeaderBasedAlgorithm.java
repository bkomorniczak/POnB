package psk.pob.distributed.communication.algorithms;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import psk.pob.distributed.communication.CommunicationService;
import psk.pob.distributed.models.Message;
import psk.pob.distributed.models.Node;

@Slf4j
@Component
public class LeaderBasedAlgorithm implements CommunicationAlgorithm {

  private final CommunicationService communicationService;

  @Autowired
  public LeaderBasedAlgorithm(CommunicationService communicationService) {
    this.communicationService = communicationService;
  }

  @Override
  public void communicate(Node source, List<Node> targets, Message message) {
    if (targets.isEmpty()) {
      log.warn("No targets available for communication.");
      return;
    }

    Node leader = targets.get(0);

    if (!source.equals(leader)) {
      log.info("Node {} is forwarding message to leader {}", source.getId(), leader.getId());
      communicationService.sendMessage(source, leader, message);
    } else {
      for (Node target : targets) {
        if (!target.equals(source)) {
          log.info("Leader {} forwarding message to {}", leader.getId(), target.getId());
          communicationService.sendMessage(leader, target, message);
        }
      }
    }
  }

  @Override
  public void initialize(List<Node> nodes) {
    // No specific setup needed for leader-based algorithm
  }
}

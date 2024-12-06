package psk.pob.distributed.communication.algorithms;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import psk.pob.distributed.communication.CommunicationService;
import psk.pob.distributed.models.Message;
import psk.pob.distributed.models.Node;

@Slf4j
public class LeaderBasedAlgorithm implements CommunicationAlgorithm {
  private Node leader;

  CommunicationService communicationService;
  @Override
  public void communicate(Node source, List<Node> targets, Message message) {
    if (!source.equals(leader)) {
      log.info("Forwarding message from {} to leader {}", source.getId(), leader.getId());
      communicationService.sendMessage(source, leader, message);
    }

    for (Node target : targets) {
      if (!target.equals(leader)) {
        log.info("Leader {} forwarding message to {}", leader.getId(), target.getId());
        communicationService.sendMessage(leader, target, message);
      }
    }
  }

  @Override
  public void initialize(List<Node> nodes) {
    this.leader = nodes.get(0); // Static leader selection; can be dynamic
    log.info("Leader selected: {}", leader.getId());
  }
}

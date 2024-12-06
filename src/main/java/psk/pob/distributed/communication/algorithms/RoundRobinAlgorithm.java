package psk.pob.distributed.communication.algorithms;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import psk.pob.distributed.communication.CommunicationService;
import psk.pob.distributed.models.Message;
import psk.pob.distributed.models.Node;

@Slf4j
public class RoundRobinAlgorithm implements CommunicationAlgorithm {
  private int currentIndex = 0;

  CommunicationService communicationService;
  @Override
  public void communicate(Node source, List<Node> targets, Message message) {
    if (targets.isEmpty()) return;

    Node target = targets.get(currentIndex);
    log.info("Sending message from {} to {}", source.getId(), target.getId());
    communicationService.sendMessage(source, target, message);

    currentIndex = (currentIndex + 1) % targets.size();
  }

  @Override
  public void initialize(List<Node> nodes) {
    // No specific setup needed for round-robin
  }
}

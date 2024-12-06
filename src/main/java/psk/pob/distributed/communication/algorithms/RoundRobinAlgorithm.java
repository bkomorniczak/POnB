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
public class RoundRobinAlgorithm implements CommunicationAlgorithm {

  private final CommunicationService communicationService;
  private int currentIndex = 0;

  @Autowired
  public RoundRobinAlgorithm(CommunicationService communicationService) {
    this.communicationService = communicationService;
  }

  @Override
  public void communicate(Node source, List<Node> targets, Message message) {
    if (targets.isEmpty()) {
      log.warn("No targets available for communication.");
      return;
    }

    // Select the next node in a round-robin fashion
    currentIndex = (currentIndex + 1) % targets.size();
    Node target = targets.get(currentIndex);

    if (!target.equals(source)) {
      log.info("Sending message from {} to {} using Round-Robin", source.getId(), target.getId());
      communicationService.sendMessage(source, target, message);
    }
  }

  @Override
  public void initialize(List<Node> nodes) {
    // No specific setup needed for round-robin
  }
}

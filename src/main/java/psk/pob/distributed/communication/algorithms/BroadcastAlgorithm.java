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
public class BroadcastAlgorithm implements CommunicationAlgorithm {

  private final CommunicationService communicationService;

  @Autowired
  public BroadcastAlgorithm(CommunicationService communicationService) {
    this.communicationService = communicationService;
  }

  @Override
  public void communicate(Node source, List<Node> targets, Message message) {
    for (Node target : targets) {
      if (!target.equals(source)) {
        log.info("Broadcasting message from {} to {}", source.getId(), target.getId());
        communicationService.sendMessage(source, target, message);
      }
    }
  }

  @Override
  public void initialize(List<Node> nodes) {
    // No specific setup needed for broadcast
  }
}

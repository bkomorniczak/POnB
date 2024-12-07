package psk.pob.distributed.communication.algorithms;

import java.util.List;
import java.util.Random;
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
  private final Random random = new Random();
  private static final int DELAY_MIN = 0;
  private static final int DELAY_MAX = 0;
  @Autowired
  public BroadcastAlgorithm(CommunicationService communicationService) {
    this.communicationService = communicationService;
  }

  @Override
  public void communicate(Node source, List<Node> targets, Message message) {
    for (Node target : targets) {
      simulateDelay();
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


  public void simulateDelay() {
    try {
      long delay = random.nextInt(DELAY_MIN, DELAY_MAX);
      Thread.sleep(delay);
      log.info("Simulated delay of {}ms", delay);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.error("Delay simulation interrupted");
    }
  }
}

package psk.pob.distributed.communication.algorithms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import psk.pob.distributed.communication.CommunicationService;

@Component
public class CommunicationAlgorithmFactory {

  private final CommunicationService communicationService;

  @Autowired
  public CommunicationAlgorithmFactory(CommunicationService communicationService) {
    this.communicationService = communicationService;
  }

  public CommunicationAlgorithm createAlgorithm(String type) {
    return switch (type.toLowerCase()) {
      case "broadcast" -> new BroadcastAlgorithm(communicationService);
      case "leader" -> new LeaderBasedAlgorithm();
      case "round-robin" -> new RoundRobinAlgorithm();
      default -> throw new IllegalArgumentException("Unknown algorithm type: " + type);
    };
  }
}

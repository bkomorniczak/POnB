package psk.pob.distributed.communication.algorithms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommunicationAlgorithmFactory {

  private final RoundRobinAlgorithm roundRobinAlgorithm;
  private final BroadcastAlgorithm broadcastAlgorithm;
  private final LeaderBasedAlgorithm leaderBasedAlgorithm;

  @Autowired
  public CommunicationAlgorithmFactory(
      RoundRobinAlgorithm roundRobinAlgorithm,
      BroadcastAlgorithm broadcastAlgorithm,
      LeaderBasedAlgorithm leaderBasedAlgorithm) {
    this.roundRobinAlgorithm = roundRobinAlgorithm;
    this.broadcastAlgorithm = broadcastAlgorithm;
    this.leaderBasedAlgorithm = leaderBasedAlgorithm;
  }

  public CommunicationAlgorithm createAlgorithm(String type) {
    return switch (type.toLowerCase()) {
      case "broadcast" -> broadcastAlgorithm;
      case "leader" -> leaderBasedAlgorithm;
      case "round-robin" -> roundRobinAlgorithm;
      default -> throw new IllegalArgumentException("Unknown algorithm type: " + type);
    };
  }
}


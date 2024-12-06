package psk.pob.distributed.communication.algorithms;

public class CommunicationAlgorithmFactory {

  private CommunicationAlgorithmFactory() {
    //intentionally left blank
  }

  public static CommunicationAlgorithm createAlgorithm(String type) {
    return switch (type.toLowerCase()) {
      case "broadcast" -> new BroadcastAlgorithm();
      case "leader" -> new LeaderBasedAlgorithm();
      case "round-robin" -> new RoundRobinAlgorithm();
      default -> throw new IllegalArgumentException("Unknown algorithm type: " + type);
    };
  }
}

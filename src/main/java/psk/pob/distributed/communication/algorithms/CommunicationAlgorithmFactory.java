package psk.pob.distributed.communication.algorithms;

public class CommunicationAlgorithmFactory {

  private CommunicationAlgorithmFactory() {
    //intentionally left blank
  }

  public static CommunicationAlgorithm createAlgorithm(String type) {
    switch (type.toLowerCase()) {
      case "broadcast":
        return new BroadcastAlgorithm();
      case "leader":
        return new LeaderBasedAlgorithm();
      case "round-robin":
        return new RoundRobinAlgorithm();
      default:
        throw new IllegalArgumentException("Unknown algorithm type: " + type);
    }
  }
}

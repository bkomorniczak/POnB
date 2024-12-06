package psk.pob.distributed.communication.algorithms;

import java.util.List;
import psk.pob.distributed.models.Message;
import psk.pob.distributed.models.Node;

public interface CommunicationAlgorithm {
  void communicate(Node source, List<Node> targets, Message message);
  void initialize(List<Node> nodes);
}

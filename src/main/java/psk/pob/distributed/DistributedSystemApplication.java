package psk.pob.distributed;

import static psk.pob.distributed.models.MessageType.REQUEST;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import psk.pob.distributed.communication.DistributedSystemManager;
import psk.pob.distributed.communication.algorithms.CommunicationAlgorithm;
import psk.pob.distributed.communication.algorithms.CommunicationAlgorithmFactory;
import psk.pob.distributed.models.Message;
import psk.pob.distributed.models.Node;
import psk.pob.distributed.models.NodeRegistry;

@SpringBootApplication
public class DistributedSystemApplication {

  public class Main {
    public static void main(String[] args) {
      NodeRegistry nodeRegistry = NodeRegistry.getInstance();
      nodeRegistry.registerNode(new Node("1", "127.0.0.1", 8081));
      nodeRegistry.registerNode(new Node("2", "127.0.0.1", 8082));
      nodeRegistry.registerNode(new Node("3", "127.0.0.1", 8083));

      String algorithmType = System.getenv("ALGORITHM_TYPE"); // Example: leader, broadcast, round-robin
      if (algorithmType == null) {
        algorithmType = "broadcast"; // Default to broadcast
      }

      CommunicationAlgorithm algorithm = CommunicationAlgorithmFactory.createAlgorithm(algorithmType);
      DistributedSystemManager manager = new DistributedSystemManager(algorithm);
      manager.initializeNodes(nodeRegistry.getAllNodes());

      Node sourceNode = nodeRegistry.getAllNodes().get(0);
      Message message = new Message("Sender", "Receiver", "Payload", REQUEST);
      manager.sendMessage(sourceNode, message);
    }
  }

}

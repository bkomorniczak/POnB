package psk.pob.distributed;

import static psk.pob.distributed.models.MessageType.REQUEST;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import psk.pob.distributed.communication.CommunicationManager;
import psk.pob.distributed.communication.CommunicationService;
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
      NodeRegistry nodeRegistry = new NodeRegistry();
      String LOCAL_HOST = "127.0.0.1";
      nodeRegistry.registerNode(new Node("1", LOCAL_HOST, 8081));
      nodeRegistry.registerNode(new Node("2", LOCAL_HOST, 8082));
      nodeRegistry.registerNode(new Node("3", LOCAL_HOST, 8083));

      String algorithmType = System.getenv("ALGORITHM_TYPE"); // Example: leader, broadcast, round-robin
      if (algorithmType == null) {
        algorithmType = "broadcast"; // Default to broadcast
      }

      CommunicationAlgorithm algorithm = CommunicationAlgorithmFactory.createAlgorithm(algorithmType);
      CommunicationManager communicationManager = new CommunicationManager();
      CommunicationService communicationService = new CommunicationService(communicationManager);

      DistributedSystemManager manager = new DistributedSystemManager(nodeRegistry, communicationService,algorithm);
      manager.initializeNodes(nodeRegistry.getAllNodes());

      Node sourceNode = nodeRegistry.getAllNodes().get(0);
      Message message = new Message("Sender", "Receiver", "Payload", REQUEST);
      manager.sendMessage(sourceNode, message);
    }
  }

}

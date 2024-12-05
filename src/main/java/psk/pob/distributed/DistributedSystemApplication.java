package psk.pob.distributed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import psk.pob.distributed.communication.CommunicationManager;
import psk.pob.distributed.communication.CommunicationService;
import psk.pob.distributed.communication.DistributedSystemManager;
import psk.pob.distributed.communication.HeartbeatManager;
import psk.pob.distributed.models.Node;
import psk.pob.distributed.models.NodeRegistry;

@SpringBootApplication
public class DistributedSystemApplication {

  public static void main(String[] args) {
    NodeRegistry nodeRegistry = new NodeRegistry();
    CommunicationManager communicationManager = new CommunicationManager();
    CommunicationService communicationService = new CommunicationService(communicationManager);
    HeartbeatManager heartbeatManager = new HeartbeatManager(nodeRegistry, communicationService);

    // Register initial nodes
    Node node1 = new Node("1", "127.0.0.1", 8081);
    nodeRegistry.registerNode(node1);

    // Start heartbeat monitoring
    heartbeatManager.startHeartbeat();

    // Start listening for messages
    communicationService.listenForMessages();
  }
}

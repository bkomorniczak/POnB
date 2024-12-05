package psk.pob.distributed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import psk.pob.distributed.communication.DistributedSystemManager;

@SpringBootApplication
public class DistributedSystemApplication {

  private final DistributedSystemManager systemManager;

  public DistributedSystemApplication(DistributedSystemManager systemManager) {
    this.systemManager = systemManager;
  }

  public static void main(String[] args) {
    SpringApplication.run(DistributedSystemApplication.class, args);
  }

  @EventListener(ApplicationReadyEvent.class)
  public void runAfterStartup() {
    systemManager.initialize();
  }
}

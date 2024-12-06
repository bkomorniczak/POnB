package psk.pob.distributed.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class MetricsService {

  private final Counter messagesSentCounter;
  private final Counter messagesReceivedCounter;

  public MetricsService(MeterRegistry meterRegistry) {
    this.messagesSentCounter = Counter.builder("messages_sent_total")
        .description("Total number of messages sent")
        .register(meterRegistry);
    this.messagesReceivedCounter = Counter.builder("messages_received_total")
        .description("Total number of messages received")
        .register(meterRegistry);
  }

  public void incrementMessagesSent() {
    messagesSentCounter.increment();
  }

  public void incrementMessagesReceived() {
    messagesReceivedCounter.increment();
  }
}


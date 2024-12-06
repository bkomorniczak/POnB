package psk.pob.distributed.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {

  private final Counter messagesSent;
  private final Counter messagesReceived;

  public MetricsService(MeterRegistry meterRegistry) {
    this.messagesSent = meterRegistry.counter("messages_sent_total");
    this.messagesReceived = meterRegistry.counter("messages_received_total");
  }

  public void incrementMessagesSent() {
    messagesSent.increment();
  }

  public void incrementMessagesReceived() {
    messagesReceived.increment();
  }
}


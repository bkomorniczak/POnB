package psk.pob.distributed.controller;

import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ConcurrentHashMap;
import psk.pob.distributed.models.Message;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

  private final ConcurrentHashMap<String, String> messageLog = new ConcurrentHashMap<>();

  @PostMapping
  public Message handleMessage(@RequestBody Message request) {
    messageLog.put(request.getSenderId() + "->" + request.getReceiverId(), request.getPayload());

    return new Message( request.getSenderId(), request.getReceiverId(),request.getPayload(), request.getType());
  }

  @GetMapping
  public ConcurrentHashMap<String, String> getLogs() {
    return messageLog;
  }
}
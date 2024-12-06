package psk.pob.distributed.models;

import lombok.Getter;

@Getter
public class Message {
  private final String senderId;
  private final String receiverId;
  private final String payload; // Message content
  private final MessageType type;

  public Message(String senderId, String receiverId, String payload, MessageType type) {
    this.senderId = senderId;
    this.receiverId = receiverId;
    this.payload = payload;
    this.type = type;
  }

}


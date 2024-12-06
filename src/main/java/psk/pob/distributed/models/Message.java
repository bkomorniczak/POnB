package psk.pob.distributed.models;


import java.io.Serializable;

public class Message implements Serializable {
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

  public String getSenderId() {
    return senderId;
  }

  public String getReceiverId() {
    return receiverId;
  }

  public String getPayload() {
    return payload;
  }

  public MessageType getType() {
    return type;
  }

  @Override
  public String toString() {
    return "Message{" +
        "senderId='" + senderId + '\'' +
        ", receiverId='" + receiverId + '\'' +
        ", payload='" + payload + '\'' +
        ", type=" + type +
        '}';
  }
}


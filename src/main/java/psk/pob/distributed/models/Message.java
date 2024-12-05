package psk.pob.distributed.models;

public class Message {
  private String senderId;
  private String receiverId;
  private String payload; // Message content
  private MessageType type;

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
}


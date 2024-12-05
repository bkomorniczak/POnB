package psk.pob.distributed.models;

public class Node {
  private String id;
  private String host;
  private int port;
  private String ipAddress;
  private boolean healthy;


  private long lastHeartbeatTime;

  public Node(String id, String host, int port) {
    this.id = id;
    this.host = host;
    this.port = port;
    this.ipAddress = resolveIpAddress(host, port);
    this.healthy = true;
    this.lastHeartbeatTime = System.currentTimeMillis();
  }

  public Node() {
  }

  private String resolveIpAddress(String host, int port) {
//    try {
//      InetAddress address = InetAddress.getByName(host);
//      return address.getHostAddress();
//    } catch (UnknownHostException e) {
//      throw new IllegalArgumentException("Unable to resolve IP address for host: " + host, e);
//    }
    return host + ":" + port;
  }

  public String getId() {
    return id;
  }

  public String getHost() {
    return host;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public int getPort() {
    return port;
  }

  public boolean isHealthy() {
    return healthy;
  }

  public long getLastHeartbeatTime() {
    return lastHeartbeatTime;
  }

  public void setHealthy(boolean healthy) {
    this.healthy = healthy;
  }

  public void updateHeartbeatTime() {
    this.lastHeartbeatTime = System.currentTimeMillis();
  }

  @Override
  public String toString() {
    return "Node{" +
        "id='" + id + '\'' +
        ", host='" + host + '\'' +
        ", ipAddress='" + ipAddress + '\'' +
        ", port=" + port +
        ", healthy=" + healthy +
        ", lastHeartbeatTime=" + lastHeartbeatTime +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Node node = (Node) o;
    return port == node.port &&
        id.equals(node.id) &&
        host.equals(node.host);
  }

  @Override
  public int hashCode() {
    return id.hashCode() + host.hashCode() + port;
  }

  public void setLastHeartbeatTime(long lastHeartbeatTime) {
    this.lastHeartbeatTime = lastHeartbeatTime;
  }
}

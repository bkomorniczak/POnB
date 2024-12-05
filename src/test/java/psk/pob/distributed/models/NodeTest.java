package psk.pob.distributed.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


class NodeTest {

  @Test
  void testNodeInitialization() {
    Node node = new Node("node1", "localhost", 8080);

    assertEquals("node1", node.getId());
    assertEquals("localhost", node.getHost());
    assertNotNull(node.getIpAddress());
    assertEquals(8080, node.getPort());
    assertTrue(node.isHealthy());
  }

  @Test
  void testHeartbeatUpdate() {
    Node node = new Node("node1", "localhost", 8080);

    long initialHeartbeatTime = node.getLastHeartbeatTime();
    try {
      Thread.sleep(10);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    node.updateHeartbeatTime();
    assertTrue(node.getLastHeartbeatTime() > initialHeartbeatTime);
  }

  @Test
  void testHealthStatus() {
    Node node = new Node("node1", "localhost", 8080);

    assertTrue(node.isHealthy());

    node.setHealthy(false);
    assertFalse(node.isHealthy());
  }

  @Test
  void testEqualsAndHashCode() {
    Node node1 = new Node("node1", "localhost", 8080);
    Node node2 = new Node("node1", "localhost", 8080);
    Node node3 = new Node("node2", "localhost", 8081);

    assertEquals(node1, node2);
    assertNotEquals(node1, node3);

    assertEquals(node1.hashCode(), node2.hashCode());
    assertNotEquals(node1.hashCode(), node3.hashCode());
  }

  @Test
  void testToString() {
    Node node = new Node("node1", "localhost", 8080);
    String nodeString = node.toString();

    assertTrue(nodeString.contains("id='node1'"));
    assertTrue(nodeString.contains("host='localhost'"));
    assertTrue(nodeString.contains("port=8080"));
  }
}

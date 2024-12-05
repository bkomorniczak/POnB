package psk.pob.distributed.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;

@Configuration
public class ActuatorConfig {
  public ActuatorConfig(WebEndpointProperties webEndpointProperties) {
    webEndpointProperties.setBasePath("/actuator");
  }
}


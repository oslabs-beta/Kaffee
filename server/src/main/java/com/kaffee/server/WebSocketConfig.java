package com.kaffee.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableScheduling
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  /**
   * Configures the WebSocket connection.
   */

  // I was going to use this to validate CORS issues
  private String client = "http://localhost:6060/";

  @Override
  public void configureMessageBroker(final MessageBrokerRegistry config) {
    // this is the prefix to add a message topic, in our case a new metric
    config.enableSimpleBroker("/metric");

    // this is the prefix to handle messages bound for methods
    // with @MessageMapping
    config.setApplicationDestinationPrefixes("/app");
  }

  @Override
  public void registerStompEndpoints(final StompEndpointRegistry registry) {
    // this is endpoint for websocket connections
    registry.addEndpoint("/socket").setAllowedOrigins(client);
    registry.addEndpoint("/socket").setAllowedOrigins(client).withSockJS();
  }

  public String getClient() {
    return this.client;
  }
}

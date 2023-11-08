package com.kaffee.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configures the WebSocket connection.
 */
@Configuration
@EnableScheduling
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  /** The location of the client. */
  private String client = "http://localhost:6060/";

  /**
   * Overriding the configureMessageBroker.
   *
   * Taken from: https://spring.io/guides/gs/messaging-stomp-websocket/
   */
  @Override
  public void configureMessageBroker(final MessageBrokerRegistry config) {
    // this is the prefix to add a message topic, in our case a new metric
    config.enableSimpleBroker("/metric");

    // this is the prefix to handle messages bound for methods
    // with @MessageMapping
    config.setApplicationDestinationPrefixes("/app");
  }

  /**
   * Overriding the registerStompEndpoints.
   *
   * Taken from: https://spring.io/guides/gs/messaging-stomp-websocket/
   */
  @Override
  public void registerStompEndpoints(final StompEndpointRegistry registry) {
    // this is endpoint for websocket connections
    registry.addEndpoint("/socket").setAllowedOrigins("*");
    registry.addEndpoint("/socket").setAllowedOrigins().withSockJS();
  }
}

package com.kaffee.server;

import org.springframework.test.context.ContextConfiguration;

// we need to know how to test websocket configuration
@ContextConfiguration(classes = ServerApplication.class)
public class WebSocketConfigTests {
  // the configuration settings for the MessageBrokerRegistry are all private
  // I'm not sure if there's a good way to generalize testing for valid routes
}

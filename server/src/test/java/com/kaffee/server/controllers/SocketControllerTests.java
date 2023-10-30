package com.kaffee.server.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

import com.kaffee.server.ServerApplication;

@ContextConfiguration(classes=ServerApplication.class)
public class SocketControllerTests {    
  @Test
  @DisplayName("Can subscribe to a valid metric")
  void subscribeReturnsMessage() throws Exception {
    // this is based upon an example get response. This should be modified as /subscribe
    // has different behavior and is sent across the websocket
    // this.mockMvc.perform(post("/subscribe"))
    //   .andDo(print())
    //   .andExpect(status().isOk())
    //   .andExpect(content().string(containsString("Test")));
  }
  
  @Test
  @DisplayName("Attempting to subscribe to invalid metric does nothing")
  void subscribeReturnsErrorWithInvalidMetric() throws Exception {

  }

  // verify subscribing to a metric twice does nothing
  
  // verify "/unsubscribe" route

  // verify "/unsubscribe" for metric does not work
}

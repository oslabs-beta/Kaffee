package com.kaffee.server;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = ServerApplication.class)
@ContextConfiguration
@AutoConfigureMockMvc
class ServerApplicationTests {

  @Autowired
  private MockMvc mockMvc;

  // verify that each REST endpoint works:
  // actually it appears these are tested in the controllers they are used
 
  // verify "/getSettings"
  
  // verify "/setSettings"
  
  // verify "/getLogFiles"
  
  // verify "/addData" --> perhaps this needs a more descriptive endpoint
  
  /////////////////////////////////

  // verify that each WebSocket endpoint works:
  
  // verify "/subscribe"
  @Test
  void subscribeReturnsMessage() throws Exception {
    // this is based upon an example get response. This should be modified as /subscribe
    // has different behavior and is sent across the websocket
    // this.mockMvc.perform(post("/subscribe"))
    //   .andDo(print())
    //   .andExpect(status().isOk())
    //   .andExpect(content().string(containsString("Test")));
  }
  
  // verify "/subscribe" returns error if the metric isn't in the list
  @Test
  void subscribeReturnsErrorWithInvalidMetric() throws Exception {

  }
  
  // verify "/unsubscribe"

  // verify "/metric/{exampleMetric}"
}

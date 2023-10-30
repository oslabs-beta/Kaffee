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

}

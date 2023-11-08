package com.kaffee.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests for the Server Application.
 */
@SpringBootTest(classes = ServerApplication.class)
@ContextConfiguration
@AutoConfigureMockMvc
class ServerApplicationTests {

  /**
   * Autowiring the mockMvc.
   */
  @Autowired
  private MockMvc mockMvc;

  // created for tests, but as of 11/6 I'm not sure what there is to test here
}

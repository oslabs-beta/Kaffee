package com.kaffee.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.kaffee.server.ServerApplication;

@ContextConfiguration(classes=ServerApplication.class)
@WebMvcTest(ServerMetricController.class)
public class ProducerMetricControllerTests {
        
  @Autowired
  private MockMvc mockMvc;

  // This file isn't currently used for anything

  // This would be used to connect to a kafka producer and read it's JMX metric settings
  // similar to the server metrics

  // verify we can get a list of available producer metrics from an endpoint
  
  // verify that the available producer metrics are in a JSON format

  // verify that we can subscribe to producer metrics

  // verify that we can unsubscribe to producer metrics
}

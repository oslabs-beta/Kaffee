package com.kaffee.server.controllers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaffee.server.ServerApplication;

@ContextConfiguration(classes=ServerApplication.class)
@WebMvcTest(ServerMetricController.class)
public class ServerMetricControllerTests {
    
  @Autowired
  private MockMvc mockMvc;

  // verify "/availableServerMetrics"
  @Test
  @DisplayName("The path /availableServerMetrics should return a valid JSON object")
  void availableServerMetricsReturnsObject() throws Exception {
    // get the JSON object, test that it contains "bytes-in"
    // do we want to verify it has each expected element, or just that it has the correct format?
    MvcResult result = mockMvc.perform(get("/availableServerMetrics")
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasItem("bytes-in")))
      .andReturn();

    // convert to Map object
    String content = result.getResponse().getContentAsString();
    Map<String, String> resultMap = new ObjectMapper().readValue(content, new TypeReference<>() {});

    // do something with the resultMap?
  }

  @Test
  void testFormattedMetrics() {

  }

  @Test
  void testMetricAttributes() {

  }

  @Test
  void testPostJMXPort() {

  }

  @Test
  void testPostKafkaPort() {

  }

  @Test
  void testPostKafkaUrl() {

  }
}

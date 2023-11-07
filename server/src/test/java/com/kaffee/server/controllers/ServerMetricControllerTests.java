package com.kaffee.server.controllers;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ObjectFactoryCreatingFactoryBean;
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
import com.kaffee.server.models.MetricSubscriptions;

@ContextConfiguration(classes=ServerApplication.class)
@WebMvcTest(ServerMetricController.class)
public class ServerMetricControllerTests {
  private JSONObject originalSettings;
  private JSONObject newSettings;

  @Autowired
  private MockMvc mockMvc;

  @BeforeAll
  void createObjects() {
    this.originalSettings = new JSONObject();
    originalSettings.put("metric-count", "40")
    {"metric-count":"40","JMX_PORT":9999,"consumers":"2","log-filepath":"Desktop","KAFKA_PORT":9094,"KAFKA_URL":"68.96.78.126","producers":1}
  }

  @Test
  @DisplayName("The path /available-server-metrics should return a valid JSON object")
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

  // verify that we can set the JMX port
  // should we be doing this here or in DataAndLog
  @Test
  @DisplayName("The JMX port can be set using /setJMX_PORT")
  void canChangeJmxPort() throws Exception {
    MetricSubscriptions ms = new MetricSubscriptions();

    Integer originalJmxPort = ms.getJmxPort();

    mockMvc.perform(get("/setJMX_PORT")
    .contentType(MediaType.APPLICATION_JSON))
    .andExpect(status().isOk())
    .andReturn();

    Integer newJmxPort = ms.getJmxPort();

    assertNotEquals(originalJmxPort, newJmxPort);
    // verify that the new settings gets converted
    // assertEquals()
  }

  // verify that we can set the Kafka url
  // should we be doing this here or in DataAndLog

  // verify that we can set the Kafka port
  // should we be doing this here or in DataAndLog

  // verify we can get a list of the metrics attributes
  // do we really care about exposing this as an API endpoint? Does the user need this?

  // verify that we can get a formatted metric
  // do we still need this, might this be useful for testing?
}

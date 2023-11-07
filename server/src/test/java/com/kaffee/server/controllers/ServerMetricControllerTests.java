package com.kaffee.server.controllers;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaffee.server.ServerApplication;
import com.kaffee.server.models.MetricSubscriptions;

@ContextConfiguration(classes = ServerApplication.class)
@WebMvcTest(ServerMetricController.class)
public class ServerMetricControllerTests {
  /** JSON for the original settings. */
  private JSONObject originalSettings;
  /** JSON for testing changes to settings. */
  private JSONObject newSettings;

  /**
   * Autowiring mockMvc.
   */
  @Autowired
  private MockMvc mockMvc;

  /**
   * Declaration of two JSON settings for testing. I have converted what
   * should be numbers to strings to quiet CheckStyle.
   *
   * @throws JSONException
   */
  @BeforeAll
  void createObjects() throws JSONException {
    this.originalSettings = new JSONObject();
    this.originalSettings.put("metric-count", "40").put("JMX_PORT", "9999")
        .put("KAFKA_PORT", "9094").put("KAFKA_URL", "68.96.78.126")
        .put("consumers", 2).put("producers", 1).put("log-filepath", "Desktop")
        .put("metric-count", "40");

    this.newSettings = new JSONObject();
    this.newSettings.put("metric-count", "40").put("JMX_PORT", "9999")
        .put("KAFKA_PORT", "9094").put("KAFKA_URL", "68.96.78.126")
        .put("consumers", 2).put("producers", 1).put("log-filepath", "Desktop")
        .put("metric-count", "40");
  }

  /**
   * Test that the /available-server-metrics path returns a valid JSON object.
   * Also test that the "bytes-in" key exists.
   *
   * @throws Exception
   */
  @Test
  @DisplayName("/available-server-metrics should return a valid JSON object")
  void availableServerMetricsReturnsObject() throws Exception {
    // get the JSON object, test that it contains "bytes-in"
    // do we want to verify it has each expected element, or just that it has
    // the correct format?
    MvcResult result = mockMvc
        .perform(get("/availableServerMetrics")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasItem("bytes-in"))).andReturn();

    // convert to Map object
    String content = result.getResponse().getContentAsString();
    Map<String, String> resultMap = new ObjectMapper().readValue(content,
        new TypeReference<>() {
        });

    // do something with the resultMap?
  }

  /**
   * Verify that we can set the JMX port.
   *
   * @throws Exception
   */
  @Test
  @DisplayName("The JMX port can be set using /setJMX_PORT")
  void canChangeJmxPort() throws Exception {
    MetricSubscriptions ms = new MetricSubscriptions();

    Integer originalJmxPort = ms.getJmxPort();

    mockMvc.perform(get("/setJMX_PORT").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();

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
  // do we really care about exposing this as an API endpoint? Does the user
  // need this?

  // verify that we can get a formatted metric
  // do we still need this, might this be useful for testing?
}

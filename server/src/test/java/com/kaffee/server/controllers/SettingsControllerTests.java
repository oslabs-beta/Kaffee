package com.kaffee.server.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * Tests for the SettingsController class.
 */
@ContextConfiguration(classes = SettingsController.class)
public class SettingsControllerTests {
  /**
   * Test for locating and reading from file.
   */
  @Test
  @DisplayName("Can read JMX Port from file")
  void readSettings() {
    try {
      String testString = "{ \"JMX_PORT\": 9094 }";

      StringReader testReader = new StringReader(testString);
      BufferedReader testBufferedReader = new BufferedReader(testReader);

      SettingsController sc = new SettingsController();
      Object kafkaPort = sc.getSettingFromReader("JMX_PORT", testBufferedReader)
          .toString();

      assertEquals(kafkaPort, "9094");
    } catch (IOException e) {
      fail("KAFKA_PORT key should exist");
    }
  }
}

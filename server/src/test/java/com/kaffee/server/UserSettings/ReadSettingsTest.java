package com.kaffee.server.UserSettings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the ReadSettings class.
 */
public class ReadSettingsTest {
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
      Object kafkaPort = ReadSettings.getSetting("JMX_PORT", testBufferedReader)
          .toString();

      assertEquals(kafkaPort, "9094");
    } catch (IOException e) {
      fail("KAFKA_PORT key should exist");
    }
  }
}

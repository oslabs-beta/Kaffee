package com.kaffee.server.UserSettings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

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
      Object kafkaPort = ReadSettings.main("KAFKA_PORT");

      assertEquals(kafkaPort, "9094");
    } catch (IOException e) {
      fail("KAFKA_PORT key should exist");
    }
  }
}

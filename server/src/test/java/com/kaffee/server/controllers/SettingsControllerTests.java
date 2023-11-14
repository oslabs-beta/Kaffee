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
  }
}

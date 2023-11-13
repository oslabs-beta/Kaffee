package com.kaffee.server.models;

import java.util.Arrays;
import java.util.List;

/**
 * All user settings to be used throughout the application.
 */
public class UserSettings {
  /** JMX_PORT: port of JMX endpoint. */
  private Integer jmxPort;
  /** KAFKA_URL: URL of the Kafka broker. */
  private String kafkaUrl;
  /** KAFKA_PORT: port of the Kafka broker. */
  private Integer kafkaPort;
  /** metric-count: the number of metrics to display on screen. */
  private Integer metricCount;
  /** consumers: the number of consumers to run when testing. */
  private Integer consumers;
  /** producers: the number of producers to run when testing. */
  private Integer producers;
  /** log-filepath: fully defined location to store logs. */
  private String logFilepath;

  /**
   * Constructor for UserSettings with no arguments.
   */
  public void UserSettings() {

  }

  /**
   * Load settings from file.
   */

  /**
   * Save settings to file.
   */

  /**
   * Setters and Getters
   */

  /**
   * Set JMX port value.
   *
   * @param port Integer in the range of 1025 - 65535, with exceptions.
   * @throws IllegalArgumentException
   */
  public void setJmxPort(final Integer port) throws IllegalArgumentException {
    if (!this.isValidPort(port)) {
      throw new IllegalArgumentException("Invalid JMX Port value");
    }

    this.jmxPort = port;
  }

  /**
   * Get the current JMX port value.
   *
   * @return the current JMX port.
   */
  public Integer getJmxPort() {
    return this.jmxPort;
  }

  /**
   * Ensures that a port is valid for setting validation.
   *
   * @param port The numeric value of a port for validation.
   * @return Boolean whether the provided port is a valid port.
   */
  private boolean isValidPort(final Integer port) {
    List<Integer> commonPorts = Arrays.asList(1433, 1434, 3306, 3389, 8080,
        8443);
    return (1024 < port && port < 65535 && !commonPorts.contains(port));
  }
}

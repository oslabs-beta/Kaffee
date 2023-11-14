package com.kaffee.server.models;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.validator.routines.UrlValidator;
import org.json.JSONObject;

public class UserSettings {
  /** The lowest allowed port. */
  private static final Integer MIN_PORT = 1025;
  /** The highest allowed port. */
  private static final Integer MAX_PORT = 65535;
  /** List of reserved ports we should block from assignment. */
  private static final List<Integer> RESERVED_PORTS = Arrays.asList(1433, 1434,
      3306, 3389, 8080, 8443);
  /** Maximum number of producers and consumers to allow. */
  private static final Integer MAX_AGENTS = 1000;

  /** The port for the JMX endpoint. */
  private Integer jmxPort;
  /** The URL for the Kafka broker. */
  private String kafkaUrl;
  /** The port for the Kafka broker. */
  private Integer kafkaPort;
  /** The number of metric points to display on a chart. */
  private Integer metricCount;
  /** The number of consumers to run during testing. */
  private Integer consumers;
  /** The number of producers to run during testing. */
  private Integer producers;
  /** The string representation of where to save log files. */
  private String logFilePath;

  /** The singleton of this UserSettings. */
  private static UserSettings us = null;

  /**
   * Default UserSettings record constructor with verification.
   *
   * @param jmxPort     The exposed JMX port
   * @param kafkaUrl    The URL of the Kafka broker
   * @param kafkaPort   The port of the Kafka broker
   * @param metricCount The number of metric points to display on charts
   * @param consumers   The number of consumers to use in testing
   * @param producers   The number of consumers to use in testing
   * @param logFilePath The location for the log file
   */
  private UserSettings(final Integer jmxPort, final String kafkaUrl,
      final Integer kafkaPort, final Integer metricCount,
      final Integer consumers, final Integer producers,
      final String logFilePath) throws IllegalArgumentException {
    this.setJmxPort(jmxPort);
    this.setKafkaUrl(kafkaUrl);
    this.setKafkaPort(kafkaPort);
    this.setMetricCount(metricCount);
    this.setConsumers(consumers);
    this.setProducers(producers);
    this.setLogFilePath(logFilePath);
  }

  /**
   * Return the synchronized UserSettings, instantiating if uninstantiated.
   *
   * @param jmxPort     The exposed JMX port
   * @param kafkaUrl    The URL of the Kafka broker
   * @param kafkaPort   The port of the Kafka broker
   * @param metricCount The number of metric points to display on charts
   * @param consumers   The number of consumers to use in testing
   * @param producers   The number of consumers to use in testing
   * @param logFilePath The location for the log file
   *
   * @return current UserSettings instance
   */
  public static synchronized UserSettings getInstance(final Integer jmxPort,
      final String kafkaUrl, final Integer kafkaPort, final Integer metricCount,
      final Integer consumers, final Integer producers,
      final String logFilePath) {
    if (us == null) {
      us = new UserSettings(jmxPort, kafkaUrl, kafkaPort, metricCount,
          consumers, producers, logFilePath);
    }
    return us;
  }

  /**
   * Setter for jmxPort.
   *
   * @param jmxPort
   * @throws IllegalArgumentException
   */
  public void setJmxPort(final Integer jmxPort)
      throws IllegalArgumentException {
    if (!isValidPort(jmxPort)) {
      String errorMessage = """
          Ports must be between %d and %d, and not include
          the following reserved ports: %s
          """;
      throw new IllegalArgumentException(String.format(errorMessage, MIN_PORT,
          MAX_PORT, RESERVED_PORTS.toString()));
    }

    this.jmxPort = jmxPort;
  }

  /**
   * Getter for jmxPort.
   *
   * @return current JMX port value
   */
  public Integer getJmxPort() {
    return this.jmxPort;
  }

  /**
   * Setter for kafkaPort.
   *
   * @param kafkaPort
   * @throws IllegalArgumentException
   */
  public void setKafkaPort(final Integer kafkaPort)
      throws IllegalArgumentException {
    if (!isValidPort(kafkaPort)) {
      String errorMessage = """
          Ports must be between %d and %d, and not include
          the following reserved ports: %s
          """;
      throw new IllegalArgumentException(String.format(errorMessage, MIN_PORT,
          MAX_PORT, RESERVED_PORTS.toString()));
    }

    this.kafkaPort = kafkaPort;
  }

  /**
   * Getter for kafkaPort.
   *
   * @return current Kafka port value
   */
  public Integer getKafkaPort() {
    return this.kafkaPort;
  }

  /**
   * Setter for kafkaUrl.
   *
   * @param kafkaUrl
   * @throws IllegalArgumentException
   */
  public void setKafkaUrl(final String kafkaUrl)
      throws IllegalArgumentException {
    // I believe this is failing because we are using KafkaUrl without a
    // protocol for insertion in our JMX endpoin
    // if (!isValidUrl(kafkaUrl)) {
    // throw new IllegalArgumentException("Provided KafkaUrl is not valid");
    // }

    this.kafkaUrl = kafkaUrl;
  }

  /**
   * Getter for kafkaUrl.
   *
   * @return the current Kafka URL value
   */
  public String getKafkaUrl() {
    return this.kafkaUrl;
  }

  /**
   * Setter for consumers.
   *
   * @param consumers
   * @throws IllegalArgumentException
   */
  public void setConsumers(final Integer consumers)
      throws IllegalArgumentException {
    if (!isValidAgentCount(consumers)) {
      throw new IllegalArgumentException(String
          .format("Consumers must be between 1 and %d", MAX_AGENTS.toString()));
    } else {
      this.consumers = consumers;
    }
  }

  /**
   * Getter for consumers.
   *
   * @return the current number of consumers to run during testing
   */
  public Integer getConsumers() {
    return this.consumers;
  }

  /**
   * Setter for producers.
   *
   * @param producers
   * @throws IllegalArgumentException
   */
  public void setProducers(final Integer producers)
      throws IllegalArgumentException {
    if (!isValidAgentCount(producers)) {
      throw new IllegalArgumentException(String
          .format("Producers must be between 1 and %d", MAX_AGENTS.toString()));
    }

    this.producers = producers;
  }

  /**
   * Getter for producers.
   *
   * @return the current number of producers to run during testing
   */
  public Integer getProducers() {
    return this.producers;
  }

  /**
   * Setter for metricCount.
   *
   * @param metricCount the number of metric points to display on the charts
   */
  public void setMetricCount(final Integer metricCount) {
    this.metricCount = metricCount;
  }

  /**
   * Getter for metricCount.
   *
   * @return the current number of metric points to display on the charts
   */
  public Integer getMetricCount() {
    return this.metricCount;
  }

  /**
   * Setter for logFilePath.
   *
   * @param logFilePath The string representing the location of the log file
   * @throws IllegalArgumentException
   */
  public void setLogFilePath(final String logFilePath)
      throws IllegalArgumentException {
    // if (!Files.exists(Path.of(logFilePath))) {
    // throw new IllegalArgumentException("File path could not be resolved");
    // }

    this.logFilePath = logFilePath;
  }

  /**
   * Getter for logFilePath.
   *
   * @return the current string matching the path to the log file
   */
  public String getLogFilePath() {
    return this.logFilePath;
  }

  /**
   * Ensures that a port is valid for setting validation.
   *
   * @param port The numeric value of a port for validation.
   * @return Boolean whether the provided port is a valid port.
   */
  private boolean isValidPort(final Integer port) {
    return (MIN_PORT <= port && port < MAX_PORT
        && !RESERVED_PORTS.contains(port));
  }

  /**
   * Set limits on the consumers and producers. Make sure the value is
   * positive and less than defined maximum.
   *
   * @param count The numeric value of consumers or producers
   * @return Boolean whether count is greater than 0 and less than maxAgents
   */
  private boolean isValidAgentCount(final Integer count) {
    return (0 < count && count < MAX_AGENTS);
  }

  /**
   * Ensure that a URL is valid for setting validation.
   *
   * @param url The string value of the URL
   * @return Boolean whether UrlValidator can validate the given URL
   */
  private boolean isValidUrl(final String url) {
    UrlValidator uv = new UrlValidator();
    return uv.isValid(url);
  }

  /**
   * Method to convert the UserSetting record to JSONObject.
   *
   * @return the current UserSetting converted to JSONObject
   */
  public JSONObject convertToJson() {
    return new JSONObject(this);
  }
}

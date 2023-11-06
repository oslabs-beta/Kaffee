package com.kaffee.server.models;

import java.util.Map;

/**
 * Creates the MessageData object which is used by the websocket connection.
 */
public class MessageData {
  /**
   * metric: The name of the metric.
   */
  private String metric;
  /**
   * The timestamp of the MessageData, stored in milliseconds. There may be
   * value in storing this in a timestamp and then converting the value to a
   * string before sending it in the JSON across the websocket
   */
  private Number time;
  /**
   * A map of metrics attributes and their values.
   */
  private Map<String, String> snapshot;

  /**
   * Creates an empty MessageData with no assigned values.
   *
   * I believe that this may be able to be removed.
   */
  public MessageData() {

  }

  /**
   * Create a MessageData using the current system timestamp.
   *
   * @param metric   The name of the metric.
   * @param snapshot The map of metric attributes and their values.
   */
  public MessageData(final String metric, final Map<String, String> snapshot) {
    this.setMetric(metric);
    this.setSnapshot(snapshot);

    this.setTime(System.currentTimeMillis());
  }

  /**
   * Create a MessageData with assigned timestamp.
   *
   * @param metric   The name of the metric.
   * @param time     The time in milliseconds.
   * @param snapshot The map of metric attributes and their values.
   */
  public MessageData(String metric, Number time, Map<String, String> snapshot) {
    this.setMetric(metric);
    this.setSnapshot(snapshot);
    this.setTime(time);
  }

  /**
   * Get the name of the metric.
   *
   * @return the name of the metric
   */
  final String getMetric() {
    return this.metric;
  }

  /**
   * Get the time of MessageData.
   *
   * @return the timestamp of MessageData
   */
  public Number getTime() {
    return this.time;
  }

  public Map<String, String> getSnapshot() {
    return this.snapshot;
  }

  public void setMetric(String metric) {
    this.metric = metric;
  }

  /**
   * Set the time to an integer.
   *
   * @param time The current time in milliseconds.
   */
  private void setTime(final Number time) {
    this.time = time;
  }

  private void setSnapshot(Map<String, String> snapshot) {
    this.snapshot = snapshot;
  }
}

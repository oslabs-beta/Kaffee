package com.kaffee.server.models;

import java.util.Map;
import java.util.regex.Pattern;

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
  public MessageData(final String metric, final Number time,
      final Map<String, String> snapshot) {
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

  /**
   * Get the MessageData snapshot mapping.
   *
   * @return The map of metric attributes and their values.
   */
  public Map<String, String> getSnapshot() {
    return this.snapshot;
  }

  /**
   * Set the MessageData metric name.
   *
   * @param metric The name of the MessageData metric
   */
  public void setMetric(final String metric) {
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

  /**
   * Set the MessageData snapshot map. The keys should never contain
   * characters that are unsafe for URI strings.
   *
   * @param snapshot The metrics attributes and their values.
   * @throws IllegalArgumentException the key values of the snapshot should be
   *                                  URI safe.
   */
  private void setSnapshot(final Map<String, String> snapshot)
      throws IllegalArgumentException {
    // Only letters, numbers, dashes and underscores allowed. I think this may
    // be unnecessary here, but is necessary in the metrics list.
    Pattern uriSafe = Pattern.compile("^[a-zA-Z0-9-_]$");
    for (String key : snapshot.keySet()) {
      if (!uriSafe.matcher(key).matches()) {
        String errorMessage = """
            Snapshot keys may only contain letters,
            numbers, hyphens, and underscores.""";
        throw new IllegalArgumentException(errorMessage);
      }
    }

    this.snapshot = snapshot;
  }
}

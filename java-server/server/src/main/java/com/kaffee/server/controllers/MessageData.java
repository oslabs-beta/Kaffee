package com.kaffee.server.controllers;

import java.util.Map;
import org.json.JSONObject;

public class MessageData {
  private String metric;
  private Number time;
  private Map<String, String> snapshot;


  public MessageData() {

  }

// MessageData bytesIn = new MessageData("bytesIn", 123352435, SnapshotMap)
  public MessageData(String metric, Number time, Map<String, String> snapshot) {
    this.metric = metric;
    this.time = time;
    this.snapshot = snapshot;
  }

  public String getMetric() {
    return this.metric;
  }

  public Number getTime() {
    return this.time;
  }

  public Map<String, String> getSnapshot() {
    return this.snapshot;
  }

  public void setMetric(String metric) {
    this.metric = metric;
  }

  public void setTime(Number time) {
    this.time = time;
  }

  public void setSnapshot(Map<String, String> snapshot) {
    this.snapshot = snapshot;
  }
}

package com.kaffee.server.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class ProducerMetricController {
  /**
   * a map of all the available producer metrics and their JMX endpoints.
   */
  private Map<String, String> producerMetricsMap;

  /**
   * Create a ProducerMetricsController.
   */
  public ProducerMetricController() {
    this.producerMetricsMap = getProducerMetricMap();
  }

  /**
   * The keys in this map to provide clean labels for use in the front end,
   * and a corresponding JMX endpoint for that metric.
   *
   * @return The map between a label and the JMX endpoints
   */
  private Map<String, String> getProducerMetricMap() {
    return new HashMap<String, String>() {
      {
        put("compression-rate",
            "kafka.producer:type=producer-metrics,client-id=([-.w]+)");
        put("response-rate",
            "kafka.producer:type=producer-metrics,client-id=([-.w]+)");
        put("request-rate",
            "kafka.producer:type=producer-metrics,client-id=([-.w]+)");
        put("request-latency-avg", "");
        put("outgoing-byte-rate", "");
        put("io-wait-time-ns-avg", "");
        put("batch-size-avg", "");
      }
    };
  }
}

package com.kaffee.server.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class ProducerMetricController {
  private Map<String, String> producerMetricsMap;

  public ProducerMetricController() {
    this.producerMetricsMap = getProducerMetricMap();
  }

  private Map<String, String> getProducerMetricMap() {
    return new HashMap<String, String>() {{
      put("compression-rate", "kafka.producer:type=producer-metrics,client-id=([-.w]+)");
      put("response-rate", "kafka.producer:type=producer-metrics,client-id=([-.w]+)");
      put("request-rate", "kafka.producer:type=producer-metrics,client-id=([-.w]+)");
      put("request-latency-avg", "");
      put("outgoing-byte-rate", "");
      put("io-wait-time-ns-avg", "");
      put("batch-size-avg", "");
    }};
  }
}

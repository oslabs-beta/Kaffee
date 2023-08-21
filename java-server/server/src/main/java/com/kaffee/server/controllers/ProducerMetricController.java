package com.kaffee.server.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("producer/")
public class ProducerMetricController {
  private Map<String, String> producerMetricsMap = getProducerMetricMap();

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


  @GetMapping("/{metric}")
  public double getByteRate(@PathVariable("metric")String metric) {
    return 0.9;
  }
}

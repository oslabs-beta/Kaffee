package com.kaffee.server.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("producer/")
public class ProducerMetricController {
  @GetMapping("byte-rate")
  public double getByteRate() {
    double currentRate = 0.9;

    return currentRate;
  }
}

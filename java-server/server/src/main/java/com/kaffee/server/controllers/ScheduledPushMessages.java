package com.kaffee.server.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties.RSocket.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.github.javafaker.ChuckNorris;
import com.github.javafaker.Faker;

@Service
public class ScheduledPushMessages {
  private final ServerMetricController smc;

  public ScheduledPushMessages(ServerMetricController smc) {
    this.smc = smc;
  }

  @Autowired
  SimpMessagingTemplate simpMessagingTemplate;

  @Autowired
  MessageData messageData;

  @Scheduled(fixedRate = 100)
  public void sendMessage() throws Exception {

    Number time = System.currentTimeMillis();
    String metric = "bytes-in";
    Map<String, String> data = smc.getFormattedMetrics(smc.jmxServerMetrics.get(metric));

    MessageData message = new MessageData(metric, time, data);
    // System.out.println(message);
    simpMessagingTemplate.convertAndSend("/metric/bytes-in", message);
    message = null;
  }
  
  // @Scheduled(fixedRate = 500)
  // public void sendMetrics() {
    
  // }
}

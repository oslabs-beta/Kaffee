package com.kaffee.server.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.github.javafaker.ChuckNorris;
import com.github.javafaker.Faker;

@Service
public class ScheduledPushMessages {
  @Autowired
  SimpMessagingTemplate simpMessagingTemplate;

  @Autowired
  MessageData messageData;

  @Scheduled(fixedRate = 10)
  public void sendMessage() throws Exception {
    ServerMetricController serverMetrics = new ServerMetricController();

    Number time = System.currentTimeMillis();
    String metric = "bytes-in";
    Map<String, String> data = serverMetrics.getFormattedMetrics(serverMetrics.jmxServerMetrics.get(metric));

    MessageData message = new MessageData(metric, time, data);
    // System.out.println(message);
    simpMessagingTemplate.convertAndSend("/metric/bytes-in", message);
  }
  
  @Scheduled(fixedRate = 500)
  public void sendMetrics() {
    
  }
}

package com.kaffee.server.controllers;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.kaffee.server.models.MessageData;
import com.kaffee.server.models.MetricSubscriptions;

@Service
public class ScheduledMessagesController {
  private final MetricSubscriptions ms;
  private final ServerMetricController smc;

  public ScheduledMessagesController(MetricSubscriptions ms, ServerMetricController smc) {
    this.ms = ms;
    this.smc = smc;
  }

  @Autowired
  SimpMessagingTemplate simpMessagingTemplate;

  @Autowired
  MessageData messageData;

  @Scheduled(fixedRate = 1000)
  public void sendMessage() throws Exception {
    Set<String> metrics = ms.subscribedServerMetrics;

    for (String metric : metrics) {
      Map<String, String> data = smc.getFormattedMetrics(smc.jmxServerMetrics.get(metric));

      MessageData message = new MessageData(metric, data);
      String outputPath = "/metric/" + metric;
      
      simpMessagingTemplate.convertAndSend(outputPath, message);
      message = null;
    }
  }
  
  @Scheduled(fixedRate = 5000)
  public void sendSubscriptions() throws NullPointerException {
    try {
      simpMessagingTemplate.convertAndSend("/metric/subscriptions", ms.subscribedServerMetrics);
    } catch (NullPointerException npe) {
      System.out.println("No subscibed metrics.");
    }
  }
}

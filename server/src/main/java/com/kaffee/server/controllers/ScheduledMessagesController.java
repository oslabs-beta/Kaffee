package com.kaffee.server.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.kaffee.server.models.MessageData;
import com.kaffee.server.models.MetricSubscriptions;

/**
 * Sending regularly scheduled messages over the established WebSocket.
 */
@Service
public class ScheduledMessagesController {
  /** The MetricSubscriptions class. */
  private final MetricSubscriptions ms;
  /** The ServerMetricController class. */
  private final ServerMetricController smc;

  /**
   * Create a ScheduledMessageController with connections to
   * MetricSubscriptions and ServerMetricController.
   *
   * @param ms  The MessageSubscriptions class
   * @param smc The ServerMetricController class
   */
  public ScheduledMessagesController(final MetricSubscriptions ms,
      final ServerMetricController smc) {
    this.ms = ms;
    this.smc = smc;
  }

  /**
   * Wiring the SimpMessagingTemplate.
   */
  @Autowired
  private SimpMessagingTemplate simpMessagingTemplate;

  /**
   * Wiring the MessageData for use in sending messages.
   */
  @Autowired
  private MessageData messageData;

  /**
   * Send broker messages to "/metric/<metric_name>".
   *
   * @throws Exception
   */
  @Scheduled(fixedRate = 1000)
  public void sendMessage() throws Exception {
    Map<String, String> metrics = ms.subscribedServerMetrics;

    for (Map.Entry<String, String> metric : metrics.entrySet()) {
      if (metric != null) {
        Map<String, String> data = smc.getFormattedMetrics(metric.getValue());

        MessageData message = new MessageData(metric.getKey(), data);
        String outputPath = "/metric/" + metric.getKey();
        simpMessagingTemplate.convertAndSend(outputPath, message);
        message = null;
      }
    }
  }

  /**
   * Send a list of subscribed mestrics to "/metric/subscriptions".
   *
   * @throws NullPointerException
   */
  @Scheduled(fixedRate = 5000)
  public void sendSubscriptions() throws NullPointerException {
    try {
      simpMessagingTemplate.convertAndSend("/metric/subscriptions",
          ms.subscribedServerMetrics);
    } catch (NullPointerException npe) {
      System.out.println("No subscibed metrics.");
    }
  }
}

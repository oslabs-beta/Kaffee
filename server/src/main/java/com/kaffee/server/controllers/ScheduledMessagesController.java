package com.kaffee.server.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;
import javax.management.IntrospectionException;

import org.apache.commons.beanutils.ConversionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.kaffee.server.models.MessageData;
import com.kaffee.server.models.MetricSubscriptions;

import com.kaffee.server.models.ApiError;
import com.kaffee.server.models.FileHandler;

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
   * @param fh The FileHandler class instance
   */
  public ScheduledMessagesController(final MetricSubscriptions ms,
      final ServerMetricController smc, final FileHandler fh)
      throws IOException {
    this.ms = ms;
    this.smc = smc;
    this.fh = FileHandler.getInstance();
  }

  /**
   * Wiring the SimpMessagingTemplate.
   */
  @Autowired
  private SimpMessagingTemplate simpMessagingTemplate;

  /** The FileHandler class. */
  @Autowired
  private FileHandler fh;

  /**
   * Send broker messages to "/metric/<metric_name>". This needs improved
   * error handling to return a better response to the front end.
   *
   * @throws Exception
   */
  @Scheduled(fixedRate = 1000)
  public void sendMessage()
      throws IOException, ConversionException, MalformedObjectNameException,
      InstanceNotFoundException, ReflectionException, MBeanException,
      AttributeNotFoundException, IntrospectionException, InterruptedException {
    Map<String, String> metrics = ms.getSubscriptions();

    for (Map.Entry<String, String> metric : metrics.entrySet()) {
      String outputPath = "/metric/" + metric.getKey();
      try {
        Map<String, String> data = smc.getFormattedMetrics(metric.getValue());

        MessageData message = new MessageData(metric.getKey(), data);

        simpMessagingTemplate.convertAndSend(outputPath, message);

        this.fh.saveToLog(message);
      } catch (IOException ioException) {
        System.out
            .println("Failed to connect to Kafka broker. Check settings.");
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,
            "Failed to connect to broker.", ioException);
        simpMessagingTemplate.convertAndSend(outputPath, apiError);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  /**
   * Send a list of subscribed mestrics to "/metric/subscriptions".
   *
   * @throws NullPointerException
   */
  // @Scheduled(fixedRate = 5000)
  public void sendSubscriptions() throws NullPointerException {
    try {
      // simpMessagingTemplate.convertAndSend("/metric/subscriptions",
      //     ms.getSubscriptions());
    } catch (NullPointerException npe) {
      System.out.println("No subscibed metrics.");
    }
  }
}

package com.kaffee.server.controllers;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;

import java.io.IOException;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaffee.server.models.MetricSubscriptions;

// Here we have the message mapping.
// From our WebSocketConfig.java file, these methods are prefixed with '/app'
@Controller
public class SocketController {
  /** The MetricSubscriptions variable for CI. */
  private MetricSubscriptions ms;

  /**
   * Constructor for SocketController class.
   * @param ms - The MetricSubscriptions object.
   */
  public SocketController(final MetricSubscriptions ms) {
    this.ms = ms;
  }

  /**
   * Extracts a string value from a JSON string given a field name.
   * @param json - The JSON string.
   * @param field - The field name.
   * @throws {JsonMappingException} If there is an error mapping the JSON.
   * @throws {JsonProcessingException} If there is an error processing the JSON.
   * @return The text value of the specified field.
   */
  private String getStringFromJson(final String json, final String field)
      throws JsonMappingException, JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode messageObj = mapper.readTree(json);
    return messageObj.get(field).textValue();
  }

  /**
   * Handles subscription messages.
   * @param metric - The metric to subscribe to.
   * @throws {IOException} If there is an I/O error.
   * @throws {MalformedObjectNameException} If the object name is malformed.
   * @throws {AttributeNotFoundException} If the attribute is not found.
   * @throws {MBeanException} If there is an MBean exception.
   * @throws {ReflectionException} If there is a reflection exception.
   * @throws {InstanceNotFoundException} If the instance is not found.
   * @throws {InterruptedException} If the thread is interrupted.
   */
  @MessageMapping("/subscribe")
  public void subscribeToMetric(final String metric) throws IOException,
      MalformedObjectNameException, AttributeNotFoundException, MBeanException,
      ReflectionException, InstanceNotFoundException, InterruptedException {
    try {
      this.ms.addSubscription(getStringFromJson(metric, "metric"));
    } catch (NullPointerException npe) {
      System.out.println("Error in socket:subscribeToMetric, metric was null");
    }
  }

  /**
   * Handles unsubscription messages.
   * @param metric - The metric to unsubscribe from.
   * @throws {IOException} If there is an I/O error.
   * @throws {MalformedObjectNameException} If the object name is malformed.
   * @throws {AttributeNotFoundException} If the attribute is not found.
   * @throws {MBeanException} If there is an MBean exception.
   * @throws {ReflectionException} If there is a reflection exception.
   * @throws {InstanceNotFoundException} If the instance is not found.
   * @throws {InterruptedException} If the thread is interrupted.
   */
  @MessageMapping("/unsubscribe")
  public void unsubscribeFromMetric(final String metric) throws IOException,
      MalformedObjectNameException, AttributeNotFoundException, MBeanException,
      ReflectionException, InstanceNotFoundException, InterruptedException {
    try {
      this.ms.removeSubscription(getStringFromJson(metric, "metric"));
    } catch (NullPointerException npe) {
      System.out
          .println("Error in socket:unsubscribeFromMetric, metric was null");
    }
  }
}

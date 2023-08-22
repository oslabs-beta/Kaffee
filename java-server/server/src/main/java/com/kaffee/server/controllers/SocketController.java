package com.kaffee.server.controllers;

import java.util.Date;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.handler.annotation.SendTo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.github.javafaker.ChuckNorris;
import com.github.javafaker.Faker;

// Here we have the message mapping.
// From our WebSocketConfig.java file, these methods are prefixed with '/app'
@Controller
public class SocketController {


  @MessageMapping("app/subscribe/")
  public void subscribeToMetric(String metric)  throws IOException, MalformedObjectNameException, AttributeNotFoundException,
    MBeanException, ReflectionException, InstanceNotFoundException, InterruptedException {
      ServerMetricController serverMetric = new ServerMetricController();
      serverMetric.addSubscription(metric);
    }

  @SendTo("/metric/test2")
  public MessageData sendData() {
    Faker faker = new Faker();
    ChuckNorris chuckNorris = faker.chuckNorris();

    MessageData message = new MessageData(chuckNorris.fact(), new SimpleDateFormat("HH:mm").format(new Date()));

    return message;
  }

  @MessageMapping("app/test")
  @SendTo("/metric/facts")
  public MessageData sendFact() {
    Faker faker = new Faker();
    ChuckNorris chuckNorris = faker.chuckNorris();
    String time = new SimpleDateFormat("HH:mm").format(new Date());
    
    return new MessageData(chuckNorris.fact(), time);
  }


  @MessageMapping("/sendTest")
  @SendTo("/metric/messages")
  public MessageData getData() {
    System.out.println("In the getData function");
    String time = new SimpleDateFormat("HH:mm").format(new Date());
    return new MessageData("Hello, World!", time);
  }
}

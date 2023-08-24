package com.kaffee.server.controllers;

import java.util.Date;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;

import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.handler.annotation.SendTo;

import org.springframework.stereotype.Controller;

// Here we have the message mapping.
// From our WebSocketConfig.java file, these methods are prefixed with '/app'
@Controller
public class SocketController {

  private final ServerMetricController smc;

  public SocketController(ServerMetricController smc) {
    this.smc = smc;
  }

  @MessageMapping("app/subscribe/")
  public void subscribeToMetric(String metric)  throws IOException, MalformedObjectNameException, AttributeNotFoundException,
    MBeanException, ReflectionException, InstanceNotFoundException, InterruptedException {
      this.smc.addSubscription(metric);
    }

  // @SendTo("/metric/test2")
  // public MessageData sendData() {
  //   Faker faker = new Faker();
  //   ChuckNorris chuckNorris = faker.chuckNorris();

  //   // MessageData message = new MessageData(chuckNorris.fact(), new SimpleDateFormat("HH:mm").format(new Date()));

  //   // return message;
  //   return new MessageData();
  // }

  // @MessageMapping("app/test")
  // @SendTo("/metric/facts")
  // public MessageData sendFact() {
  //   Faker faker = new Faker();
  //   ChuckNorris chuckNorris = faker.chuckNorris();
  //   String time = new SimpleDateFormat("HH:mm").format(new Date());
    
  //   // return new MessageData(chuckNorris.fact(), time);
  //   return new MessageData();
  // }


  // @MessageMapping("/sendTest")
  // @SendTo("/metric/messages")
  // public MessageData getData() {
  //   System.out.println("In the getData function");
  //   String time = new SimpleDateFormat("HH:mm").format(new Date());
  //   // return new MessageData("Hello, World!", time);
  // }
}

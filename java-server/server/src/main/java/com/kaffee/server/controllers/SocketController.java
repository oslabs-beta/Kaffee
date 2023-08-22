package com.kaffee.server.controllers;

import java.util.Date;
import java.text.SimpleDateFormat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.handler.annotation.SendTo;

import org.springframework.stereotype.Controller;

import com.github.javafaker.ChuckNorris;
import com.github.javafaker.Faker;

// Here we have the message mapping.
// From our WebSocketConfig.java file, these methods are prefixed with '/app'
@Controller
public class SocketController {

  @SendTo("/metric/test2")
  public MessageData sendData() throws Exception {
    Faker faker = new Faker();
    ChuckNorris chuckNorris = faker.chuckNorris();

    MessageData message = new MessageData(chuckNorris.fact(), new SimpleDateFormat("HH:mm").format(new Date()));

    return message;
  }

  @MessageMapping("app/test")
  @SendTo("/metric/facts")
  public MessageData sendFact() throws Exception {
    Faker faker = new Faker();
    ChuckNorris chuckNorris = faker.chuckNorris();
    String time = new SimpleDateFormat("HH:mm").format(new Date());
    
    return new MessageData(chuckNorris.fact(), time);
  }


  @MessageMapping("/sendTest")
  @SendTo("/metric/messages")
  public MessageData getData() throws Exception {
    System.out.println("In the getData function");
    String time = new SimpleDateFormat("HH:mm").format(new Date());
    return new MessageData("Hello, World!", time);
  }
}

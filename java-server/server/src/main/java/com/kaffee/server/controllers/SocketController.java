package com.kaffee.server.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

// Here we have the message mapping.
// From our WebSocketConfig.java file, these methods are prefixed with '/app'
@Controller
public class SocketController {

  @MessageMapping("/test")
  @SendTo("/app/messages")
  public MessageData sendData() throws Exception {
    Thread.sleep(1000);
    System.out.println("In the sendData function");
    return new MessageData("Data");
  }
}

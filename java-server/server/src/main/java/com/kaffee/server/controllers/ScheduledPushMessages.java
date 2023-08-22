package com.kaffee.server.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

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

  @Bean
  public ChuckNorris chuckNorris() {
    return (new Faker()).chuckNorris();
  }

  // @Scheduled(fixedRate = 50)
  // public void sendMessage() {
  //   String time = new SimpleDateFormat("HH:mm").format(new Date());
  //   String fact = chuckNorris().fact();
  //   System.out.println(time + ": " + fact);
  //   simpMessagingTemplate.convertAndSend("/metric/messages", new MessageData(fact, time));
  // }
}

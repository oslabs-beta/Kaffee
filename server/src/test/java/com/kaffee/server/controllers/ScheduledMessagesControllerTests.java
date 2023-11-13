package com.kaffee.server.controllers;

import org.springframework.test.context.ContextConfiguration;

import com.kaffee.server.ServerApplication;

@ContextConfiguration(classes=ServerApplication.class)
public class ScheduledMessagesControllerTests {
  
  // verify that we are sending server metrics to the correct path, in 1.0 this was '/metric/<METRIC KEY>'
  // if we are planning to be able to point at a producer or a consumer, we may want to have separate paths

}

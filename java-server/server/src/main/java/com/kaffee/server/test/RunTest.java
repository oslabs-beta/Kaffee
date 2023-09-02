package com.kaffee.server.test;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.HashMap;

import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ReflectionException;

import org.springframework.boot.autoconfigure.integration.IntegrationProperties.RSocket.Server;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kaffee.server.controllers.ServerMetricController;
import com.kaffee.server.UserSettings.ReadSettings;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

@RestController
@RequestMapping("/test")
public class RunTest{
  public static boolean run = false;
  private final ServerMetricController smc;
  public RunTest(ServerMetricController smc) {
    this.smc = smc;
  }
  
  @GetMapping("/runTest")
  public HashMap<String, AttributeList> multipleTests() throws IOException, MalformedObjectNameException, AttributeNotFoundException,
    MBeanException, ReflectionException, InstanceNotFoundException, IntrospectionException, InterruptedException, javax.management.IntrospectionException{
    run = true;
    int producers = java.lang.Integer.parseInt(ReadSettings.main("producers").toString());
    int consumers = java.lang.Integer.parseInt(ReadSettings.main("consumers").toString());

    for(int i = 0; i < producers; i++){
      String testNum = Integer.toString(i);
      String name = "test".concat(testNum);
      TestProducer newProducer = new TestProducer(name);
      newProducer.start();
    }
    for(int i = 0; i < consumers; i++){
      TestConsumer consumer = new TestConsumer();
      consumer.start();
    }
   
    return smc.getBytesInOut();
  }

  @GetMapping("/stopTest")
  public HashMap<String, AttributeList> stopTest() throws IOException, MalformedObjectNameException, AttributeNotFoundException,
    MBeanException, ReflectionException, InstanceNotFoundException, IntrospectionException, InterruptedException, javax.management.IntrospectionException{
      run = false;
      System.out.println(run);
      return smc.getBytesInOut();
    }
}
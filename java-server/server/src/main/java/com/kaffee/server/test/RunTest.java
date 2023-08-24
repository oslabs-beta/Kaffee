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

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kaffee.server.controllers.ServerMetricController;


@RestController
@RequestMapping("/test")
public class RunTest{
  
  @GetMapping("/runTest")
  public HashMap<String, AttributeList> multipleTests() throws IOException, MalformedObjectNameException, AttributeNotFoundException,
    MBeanException, ReflectionException, InstanceNotFoundException, IntrospectionException, InterruptedException, javax.management.IntrospectionException{

    for(int i = 0; i <= 4; i++){
      String testNum = Integer.toString(i);
      String name = "test".concat(testNum);
      TestProducer newProducer = new TestProducer(name);
      newProducer.start();
    }
    ServerMetricController bytes = new ServerMetricController();
    return bytes.getBytesInOut();
  }
}
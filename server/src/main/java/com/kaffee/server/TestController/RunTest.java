package com.kaffee.server.TestController;

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
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;

import com.kaffee.server.controllers.ServerMetricController;
import com.kaffee.server.controllers.SettingsController;
import com.kaffee.server.models.UserSettings;

@RestController
@RequestMapping("/test")
public class RunTest {
  public static boolean run = false;
  private final ServerMetricController smc;
  private final SettingsController sc;

  public RunTest(final ServerMetricController smc,
      final SettingsController sc) {
    this.smc = smc;
    this.sc = sc;
  }

  @GetMapping("/runTest")
  public HashMap<String, AttributeList> multipleTests() throws IOException,
      MalformedObjectNameException, AttributeNotFoundException, MBeanException,
      ReflectionException, InstanceNotFoundException, IntrospectionException,
      InterruptedException, javax.management.IntrospectionException {
    run = true;
    UserSettings currSettings = this.sc.getUserSettings();
    int producers = currSettings.getProducers();
    int consumers = currSettings.getConsumers();

    for (int i = 0; i < producers; i++) {
      String testNum = Integer.toString(i);
      String name = "test".concat(testNum);
      TestProducer newProducer = new TestProducer(name);
      newProducer.start();
    }
    for (int i = 0; i < consumers; i++) {
      TestConsumer consumer = new TestConsumer();
      consumer.start();
    }

    return smc.getBytesInOut();
  }

  @GetMapping("/stopTest")
  public HashMap<String, AttributeList> stopTest() throws IOException,
      MalformedObjectNameException, AttributeNotFoundException, MBeanException,
      ReflectionException, InstanceNotFoundException, IntrospectionException,
      InterruptedException, javax.management.IntrospectionException {
    run = false;
    System.out.println(run);
    return smc.getBytesInOut();
  }
}
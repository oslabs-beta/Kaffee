package com.kaffee.server;

import java.io.IOException;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/get-metric")
public class MetricController {


  @GetMapping("/bytes-in")
  public void getBytesIn() throws IOException, MalformedObjectNameException, AttributeNotFoundException,
      MBeanException, ReflectionException, InstanceNotFoundException, InterruptedException {
    JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi");
    JMXConnector connector = JMXConnectorFactory.connect(url);
    MBeanServerConnection mbsc = connector.getMBeanServerConnection();

    ObjectName bytesInMetric = new ObjectName("kafka.server:type=BrokerTopicMetrics,name=BytesInPerSec");

    double bytesIn = (double) mbsc.getAttribute(bytesInMetric, "OneMinuteRate");
    System.out.println("BytesInPerSec:" + bytesIn);
    System.out.println("\n");

    connector.close();
  }

  @GetMapping("/bytes-out")
  public void getBytesOut() throws IOException, MalformedObjectNameException, AttributeNotFoundException,
      MBeanException, ReflectionException, InstanceNotFoundException, InterruptedException {
    JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi");
    JMXConnector connector = JMXConnectorFactory.connect(url);
    MBeanServerConnection mbsc = connector.getMBeanServerConnection();

    ObjectName bytesOutMetric = new ObjectName("kafka.server:type=BrokerTopicMetrics,name=BytesOutPerSec");

    double bytesOut = (double) mbsc.getAttribute(bytesOutMetric, "OneMinuteRate");
    System.out.println("BytesOutPerSec:" + bytesOut);
    System.out.println("\n");
    connector.close();
  }


  @GetMapping("/under-replicated-partitions")
  public void getUnderReplicatedPartitions() throws IOException, MalformedObjectNameException, AttributeNotFoundException,
      MBeanException, ReflectionException, InstanceNotFoundException, InterruptedException {
    JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi");
    JMXConnector connector = JMXConnectorFactory.connect(url);
    MBeanServerConnection mbsc = connector.getMBeanServerConnection();

    ObjectName underReplicatedPartitionMetric = new ObjectName("kafka.server:type=ReplicaManager,name=UnderReplicatedPartitions");

    double underReplicatedPartitions = (double) mbsc.getAttribute(underReplicatedPartitionMetric, "OneMinuteRate");
    System.out.println("underReplicatedPartitions:" + underReplicatedPartitions);
    System.out.println("\n");

    connector.close();
  }


}

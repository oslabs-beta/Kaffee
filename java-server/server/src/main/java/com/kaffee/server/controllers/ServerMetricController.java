package com.kaffee.server.controllers;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.AttributeList;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ServerMetricController {
  private int JMX_PORT;
  private String SERVER_JMX_STRING;
  private Map<String, String> jmxServerMetrics;

  ServerMetricController() {
    JMX_PORT = 9092;
    SERVER_JMX_STRING = "service:jmx:rmi:///jndi/rmi://localhost:%d/jmxrmi";
    try {
    jmxServerMetrics = this.getServerMetricsStrings();
    getServerStrings();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private Map<String, String> getServerMetricsStrings() {
    return new HashMap<String, String>() {{
      put("bytes-in", "kafka.server:type=BrokerTopicMetrics,name=BytesInPerSec");
      put("bytes-out", "kafka.server:type=BrokerTopicMetrics,name=BytesOutPerSec");
      put("under-replicated-partitions", "kafka.server:type=ReplicaManager,name=UnderReplicatedPartitions");
    }};
  }

  private JMXConnector connectToJMX() throws IOException {
    JMXServiceURL url = new JMXServiceURL(String.format(SERVER_JMX_STRING, JMX_PORT));
    return JMXConnectorFactory.connect(url);
  }

  private Map<String, String> getServerStrings() throws IOException, MalformedObjectNameException, InstanceNotFoundException, ReflectionException,javax.management.IntrospectionException, IntrospectionException {
    JMXConnector connector = this.connectToJMX();
    MBeanServerConnection mbsc = connector.getMBeanServerConnection();

    Set<ObjectName> metricSet = mbsc.queryNames(new ObjectName("kafka.server:type=BrokerTopicMetrics,*"), null);
    Map<String, String> mapNamesToStrings = new HashMap<>();

    Iterator<ObjectName> it = metricSet.iterator();
    while (it.hasNext()) {
      ObjectName name = (ObjectName) it.next();

      Hashtable<String, String> keyProperties = name.getKeyPropertyList();
      System.out.println(keyProperties.toString());
      mapNamesToStrings.put(keyProperties.get("name"), name.toString());
      System.out.println(keyProperties.get("name"));
    }

    return mapNamesToStrings;
  }


  private String[] getMetricAttributes(String metric) throws IOException, MalformedObjectNameException, InstanceNotFoundException, ReflectionException,javax.management.IntrospectionException {
    JMXConnector connector = this.connectToJMX();
    MBeanServerConnection mbsc = connector.getMBeanServerConnection();

    Set<ObjectName> attributeSet = mbsc.queryNames(new ObjectName(String.format("kafka.server:type=BrokerTopicMetrics,name=%s", metric)), null);
    Set<String> metricAttributes = new HashSet<String>();

    ObjectName metricName = (ObjectName) attributeSet.iterator().next();
    MBeanAttributeInfo[] attributes = mbsc.getMBeanInfo(metricName).getAttributes();
    for (int i = 0; i < attributes.length; i++) {
      metricAttributes.add(attributes[i].getName());
    }

    String[] metricArray = new String[metricAttributes.size()];
    metricArray = metricAttributes.toArray(metricArray);
    return metricArray;
  }

  @GetMapping("/available-server-metrics")
  public Map<String, String> getServerMetrics() throws IOException, MalformedObjectNameException, AttributeNotFoundException,
    MBeanException, ReflectionException, InstanceNotFoundException, InterruptedException {
      return jmxServerMetrics;
  }

  // Server Metrics
  @GetMapping("/bytes")
  public HashMap<String, AttributeList> getBytesInOut() throws IOException, MalformedObjectNameException, AttributeNotFoundException,
    MBeanException, ReflectionException, InstanceNotFoundException, InterruptedException, javax.management.IntrospectionException {
    HashMap<String, AttributeList> bytesHash = new HashMap<>();

    JMXConnector connector = this.connectToJMX();
    MBeanServerConnection mbsc = connector.getMBeanServerConnection();

    ObjectName bytesInMetric = new ObjectName(jmxServerMetrics.get("bytes-in"));
    ObjectName bytesOutMetric = new ObjectName(jmxServerMetrics.get("bytes-out"));
    AttributeList bytesIn = mbsc.getAttributes(bytesInMetric, getMetricAttributes("BytesInPerSec"));
    AttributeList bytesOut = mbsc.getAttributes(bytesOutMetric, getMetricAttributes("BytesOutPerSec"));

    connector.close();
    bytesHash.put("bytes-in",bytesIn);
    bytesHash.put("bytes-out", bytesOut);

    return bytesHash;
  }

  @GetMapping("/metric-attributes")
  public String[] getBytesIn() throws IOException, MalformedObjectNameException, InstanceNotFoundException, ReflectionException, IntrospectionException, javax.management.IntrospectionException {
    return getMetricAttributes("BytesInPerSec");
  }


  @GetMapping("/under-replicated-partitions")
  public Integer getUnderReplicatedPartitions() throws IOException, MalformedObjectNameException, AttributeNotFoundException,
      MBeanException, ReflectionException, InstanceNotFoundException, InterruptedException {
    JMXConnector connector = this.connectToJMX();
    MBeanServerConnection mbsc = connector.getMBeanServerConnection();

    ObjectName underReplicatedPartitionMetric = new ObjectName(jmxServerMetrics.get("under-replicated-partitions"));

    Integer underReplicatedPartitions = (Integer) mbsc.getAttribute(underReplicatedPartitionMetric, "Value");

    connector.close();
    return underReplicatedPartitions;
  }
}

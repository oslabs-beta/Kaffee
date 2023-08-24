package com.kaffee.server.controllers;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.List;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.AttributeList;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kaffee.server.models.MetricSubscriptions;

import ch.qos.logback.classic.Logger;

@Controller
@RestController
@RequestMapping("/")
public class ServerMetricController {
  private MetricSubscriptions ms;
  public Map<String, String> jmxServerMetrics;
  public Set<String> subscribedMetrics;

  // Set defaults in the constructor
  public ServerMetricController(MetricSubscriptions ms) {
    this.ms = ms;
    try {
      jmxServerMetrics = ms.getServerMetricsStrings();

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }


  // the idea here was to programatically get all metrics within a given type. This should establish a good framework for how we might go through all metric types
  // and therefore we can use it to search for everything with a given "topic" key
  // this was adapted from here: http://www.dba-oracle.com/t_weblogic_list_mbeans_jmx.htm
  private Map<String, String> getServerStrings() throws IOException, MalformedObjectNameException, InstanceNotFoundException, ReflectionException,javax.management.IntrospectionException, IntrospectionException {
    // connect to the JMX port
    JMXConnector connector = ms.connectToJMX();

    // create a connector to the MBeans exposed at the port
    MBeanServerConnection mbsc = connector.getMBeanServerConnection();

    // create a set of the 
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

  // This returns a list of metrics for a given attribute
  private List<String> getMetricAttributes(String metric) throws IOException, MalformedObjectNameException, InstanceNotFoundException, ReflectionException,javax.management.IntrospectionException {
    JMXConnector connector = ms.connectToJMX();
    MBeanServerConnection mbsc = connector.getMBeanServerConnection();

    Set<ObjectName> attributeSet = mbsc.queryNames(new ObjectName(String.format(metric)), null);
    List<String> metricAttributes = new ArrayList<String>();

    ObjectName metricName = (ObjectName) attributeSet.iterator().next();
    MBeanAttributeInfo[] attributes = mbsc.getMBeanInfo(metricName).getAttributes();
    for (int i = 0; i < attributes.length; i++) {
      metricAttributes.add(attributes[i].getName());
    }

    return metricAttributes;
  }

  public Map<String, String> getFormattedMetrics(String metric) throws IOException, MalformedObjectNameException, InstanceNotFoundException, ReflectionException,javax.management.IntrospectionException, MBeanException, AttributeNotFoundException {
    // private JSONObject getFormattedMetrics(String metric) throws IOException, MalformedObjectNameException, InstanceNotFoundException, ReflectionException,javax.management.IntrospectionException, MBeanException, AttributeNotFoundException {
    JMXConnector connector = ms.connectToJMX();
    MBeanServerConnection mbsc = connector.getMBeanServerConnection();

    // JSONObject metricsObject = new JSONObject();
    Map<String, String> metricsObject = new HashMap<String, String>();
 
    Set<ObjectName> attributeSet = mbsc.queryNames(new ObjectName(metric), null);

    ObjectName metricName = (ObjectName) attributeSet.iterator().next();
    MBeanAttributeInfo[] attributes = mbsc.getMBeanInfo(metricName).getAttributes();
    for (int i = 0; i < attributes.length; i++) {
      metricsObject.put(attributes[i].getName(), mbsc.getAttribute(metricName, attributes[i].getName()).toString());
      // metricsObject.put(attributes[i].getName(), mbsc.getAttribute(metricName, attributes[i].getName()));
    }

    return metricsObject;
  }


  @GetMapping("/available-server-metrics")
  public Set<String> getServerMetrics() throws IOException, MalformedObjectNameException, AttributeNotFoundException,
    MBeanException, ReflectionException, InstanceNotFoundException, InterruptedException {
      return jmxServerMetrics.keySet();
  }

  private String[] metricsList(String metricString) throws IOException, InstanceNotFoundException, MalformedObjectNameException, ReflectionException, IntrospectionException, javax.management.IntrospectionException {

    List<String> metricAttributesList = getMetricAttributes(metricString);
    int listSize = metricAttributesList.size();
    String[] listArray = new String[listSize];
    listArray = metricAttributesList.toArray(listArray);

    return listArray;
  }

  // Server Metrics
  @GetMapping("/bytes")
  public HashMap<String, AttributeList> getBytesInOut() throws IOException, MalformedObjectNameException, AttributeNotFoundException,
    MBeanException, ReflectionException, InstanceNotFoundException, InterruptedException, javax.management.IntrospectionException, IntrospectionException {
    HashMap<String, AttributeList> bytesHash = new HashMap<>();

    JMXConnector connector = ms.connectToJMX();
    MBeanServerConnection mbsc = connector.getMBeanServerConnection();

    String bytesInString = this.jmxServerMetrics.get("bytes-in");
    String bytesOutString = this.jmxServerMetrics.get("bytes-out");

    ObjectName bytesInMetric = new ObjectName(bytesInString);
    ObjectName bytesOutMetric = new ObjectName(bytesOutString);


    AttributeList bytesIn = mbsc.getAttributes(bytesInMetric, metricsList(bytesInString));
    AttributeList bytesOut = mbsc.getAttributes(bytesOutMetric, metricsList(bytesOutString));

    connector.close();
    bytesHash.put("bytes-in",bytesIn);
    bytesHash.put("bytes-out", bytesOut);

    return bytesHash;
  }

  @GetMapping("/metric-attributes/{metric}")
  public List<String> metricAttributes(@PathVariable("metric")String metric) throws IOException, MalformedObjectNameException, InstanceNotFoundException, ReflectionException, IntrospectionException, javax.management.IntrospectionException {
    List<String> metrics = new ArrayList<String>();
    try {
      String metricString = this.jmxServerMetrics.get(metric);

      metrics = getMetricAttributes(metricString);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      return metrics;
    }
  }

  @GetMapping("/metrics/{metric}")
  public String formattedMetrics(@PathVariable("metric")String metric) throws IOException, MalformedObjectNameException, MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException, IntrospectionException, javax.management.IntrospectionException {
    String metricString = this.jmxServerMetrics.get(metric);
    System.out.println(metricString);
    return getFormattedMetrics(metricString).toString();
  }

  @GetMapping("/under-replicated-partitions")
  public Integer getUnderReplicatedPartitions() throws IOException, MalformedObjectNameException, AttributeNotFoundException,
      MBeanException, ReflectionException, InstanceNotFoundException, InterruptedException {
    JMXConnector connector = ms.connectToJMX();
    MBeanServerConnection mbsc = connector.getMBeanServerConnection();

    ObjectName underReplicatedPartitionMetric = new ObjectName(jmxServerMetrics.get("under-replicated-partitions"));

    Integer underReplicatedPartitions = (Integer) mbsc.getAttribute(underReplicatedPartitionMetric, "Value");

    connector.close();
    return underReplicatedPartitions;
  }
}

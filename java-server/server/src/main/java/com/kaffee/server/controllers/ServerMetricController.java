package com.kaffee.server.controllers;

import java.lang.Integer;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.classic.Logger;

@RestController
@RequestMapping("/")
public class ServerMetricController {
  private int JMX_PORT;
  private String SERVER_JMX_STRING;
  public Map<String, String> jmxServerMetrics;
  public Set<String> subscribedMetrics;

  // Set defaults in the constructor
  public ServerMetricController() {
    JMX_PORT = 9092;
    SERVER_JMX_STRING = "service:jmx:rmi:///jndi/rmi://localhost:%d/jmxrmi";
    try {
      jmxServerMetrics = getServerMetricsStrings();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  //settings receiver and setter
  @PostMapping("/setJMXPort")
  public int postController
  (@RequestBody String body) {
    Integer PORT = java.lang.Integer.parseInt(body);
    System.out.println("Before: " + JMX_PORT);
    JMX_PORT = PORT;
    System.out.println("After: " + JMX_PORT);
    return JMX_PORT;
}

  // A list of server strings so we can expose only a few endpoints
  private Map<String, String> getServerMetricsStrings() {
    return new HashMap<String, String>() {{
      put("under-replicated-partitions", "kafka.server:type=ReplicaManager,name=UnderReplicatedPartitions");
      put("isr-shrinks", "kafka.server:type=ReplicaManager,name=IsrShrinksPerSec");
      put("isr-expands", "kafka.server:type=ReplicaManager,name=IsrExpandsPerSec");
      put("active-controller-count", "kafka.controller:type=KafkaController,name=ActiveControllerCount");
      put("offline-partitions-count", "kafka.controller:type=KafkaController,name=OfflinePartitionsCount");
      put("leader-election-rate", "	kafka.controller:type=ControllerStats,name=LeaderElectionRateAndTimeMs");
      put("unclean-leader-selection", "kafka.controller:type=ControllerStats,name=UncleanLeaderElectionsPerSec");

      // The following won't work as listed, we may need to break this into multiple parts for each element within {}
      // put("total-time-ms", "kafka.network:type=RequestMetrics,name=TotalTimeMs,request={Produce|FetchConsumer|FetchFollower}");

      // The following won't work as listed, we may need to break this into multiple parts for each element within {}
      // put("purgatory-size", "kafka.server:type=DelayedOperationPurgatory,name=PurgatorySize,delayedOperation={Produce|Fetch}");

      put("bytes-in", "kafka.server:type=BrokerTopicMetrics,name=BytesInPerSec");
      put("bytes-out", "kafka.server:type=BrokerTopicMetrics,name=BytesOutPerSec");

      // The following won't work as listed, we may need to break this into multiple parts for each element within {}
      // put("requests-per-second", "kafka.network:type=RequestMetrics,name=RequestsPerSec,request={Produce|FetchConsumer|FetchFollower},version={0|1|2|3|\u2026}");

      // on JConsole these don't seem to match my system settings. I'm not sure if this is because kafka was started in WSL
      // put("cpu-load", "java.lang:type=OperatingSystem,name=CpuLoad");

      // you can do this using java.io.file: https://mkyong.com/java/how-to-get-free-disk-space-in-java/
      // put("disk-usage", "");

      // put("network-bytes-sent", "");
      // put("network-bytes-received", "");

      // from https://www.datadoghq.com/blog/monitoring-kafka-performance-metrics/
    //   put("young-garbaget", "java.lang:type=GarbageCollector,name=G1 Young Generation,");
    //   put("old-garbage", "java.lang:type=GarbageCollector,name=G1 Old Generation");
    }};
  }

  // a generic connector, connecting to the exposed JMX endpoints
  // ideally we should set the server string and port number programatically,
  // and perhaps even make this take in arguments so we can handle multiple connections
  private JMXConnector connectToJMX() throws IOException {
    JMXServiceURL url = new JMXServiceURL(String.format(SERVER_JMX_STRING, JMX_PORT));
    return JMXConnectorFactory.connect(url);
  }

  // the idea here was to programatically get all metrics within a given type. This should establish a good framework for how we might go through all metric types
  // and therefore we can use it to search for everything with a given "topic" key
  // this was adapted from here: http://www.dba-oracle.com/t_weblogic_list_mbeans_jmx.htm
  private Map<String, String> getServerStrings() throws IOException, MalformedObjectNameException, InstanceNotFoundException, ReflectionException,javax.management.IntrospectionException, IntrospectionException {
    // connect to the JMX port
    JMXConnector connector = this.connectToJMX();

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
    JMXConnector connector = this.connectToJMX();
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
    JMXConnector connector = this.connectToJMX();
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

  public void addSubscription(String metric) {
    String metricString = jmxServerMetrics.get(metric);

    subscribedMetrics.add(metricString);
  }

  public void removeSubscription(String metric) {
    String metricString = jmxServerMetrics.get(metric);

    subscribedMetrics.remove(metricString);
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

    JMXConnector connector = this.connectToJMX();
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
    JMXConnector connector = this.connectToJMX();
    MBeanServerConnection mbsc = connector.getMBeanServerConnection();

    ObjectName underReplicatedPartitionMetric = new ObjectName(jmxServerMetrics.get("under-replicated-partitions"));

    Integer underReplicatedPartitions = (Integer) mbsc.getAttribute(underReplicatedPartitionMetric, "Value");

    connector.close();
    return underReplicatedPartitions;
  }
}

package com.kaffee.server.controllers;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    jmxServerMetrics = this.getServerMetricsStrings();
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

  @GetMapping("/available-server-metrics")
  public Set<String> getServerMetrics() throws IOException, MalformedObjectNameException, AttributeNotFoundException,
    MBeanException, ReflectionException, InstanceNotFoundException, InterruptedException {
      return getServerMetricsStrings().keySet();
  }

  // Server Metrics
  @GetMapping("/bytes")
  public HashMap<String, Double> getBytesInOut() throws IOException, MalformedObjectNameException, AttributeNotFoundException,
    MBeanException, ReflectionException, InstanceNotFoundException, InterruptedException {
    HashMap<String, Double> bytesHash = new HashMap<>();

    JMXConnector connector = this.connectToJMX();
    MBeanServerConnection mbsc = connector.getMBeanServerConnection();

    ObjectName bytesInMetric = new ObjectName(jmxServerMetrics.get("bytes-in"));
    ObjectName bytesOutMetric = new ObjectName(jmxServerMetrics.get("bytes-out"));
    double bytesIn = (double) mbsc.getAttribute(bytesInMetric, "OneMinuteRate");
    double bytesOut = (double) mbsc.getAttribute(bytesOutMetric, "OneMinuteRate");
    bytesHash.put("bytesIn", bytesIn);
    bytesHash.put("bytesOut", bytesOut);
    connector.close();
    return bytesHash;
  }

  @GetMapping("/bytes-in")
  public Object getBytesIn() throws IOException, MalformedObjectNameException, InstanceNotFoundException, ReflectionException, IntrospectionException, javax.management.IntrospectionException {
    JMXConnector connector = this.connectToJMX();
    MBeanServerConnection mbsc = connector.getMBeanServerConnection();

    ObjectName bytesInMetric = new ObjectName(jmxServerMetrics.get("bytes-in"));
    System.out.println(mbsc.getMBeanInfo(bytesInMetric));
    return mbsc;
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
//DOES NOT EXIST
// Producer Metrics - Global Request Metrics
//   @GetMapping("/producer-metrics/{clientId}")
//     public Map<String, Object> getProducerMetrics(
//             @PathVariable String clientId
//     ) throws IOException, MalformedObjectNameException, AttributeNotFoundException,
//             MBeanException, ReflectionException, InstanceNotFoundException, InterruptedException {
        
//         JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi");
//         JMXConnector connector = JMXConnectorFactory.connect(url);
//         MBeanServerConnection mbsc = connector.getMBeanServerConnection();

//         String objectNameStr = "kafka.producer:type=producer-metrics,client-id=" + clientId;
//         System.out.println(objectNameStr);
//         ObjectName producerNodeMetric = new ObjectName(objectNameStr);
//         Map<String, Object> metrics = new HashMap<>();
//         try {
//             double metricValue = (double) mbsc.getAttribute(producerNodeMetric, "incoming-byte-rate");
//             metrics.put("MetricName", metricValue);
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//         connector.close();

//         return metrics;
//     }

    // Consumer Metrics - Fetch Metrics
    @GetMapping("/consumer-fetch-manager-metrics/{clientId}")
    public Map<String, Object> getConsumerFetchManagerMetrics(
            @PathVariable String clientId
    ) throws IOException, MalformedObjectNameException, AttributeNotFoundException,
            MBeanException, ReflectionException, InstanceNotFoundException, InterruptedException {

        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi");
        JMXConnector connector = JMXConnectorFactory.connect(url);
        MBeanServerConnection mbsc = connector.getMBeanServerConnection();

        String objectNameStr = "kafka.consumer:type=consumer-fetch-manager-metrics,client-id=" + clientId;
        ObjectName consumerFetchManagerMetric = new ObjectName(objectNameStr);

        Map<String, Object> metrics = new HashMap<>();
        try {
            double metricValue = (double) mbsc.getAttribute(consumerFetchManagerMetric, "MetricName");
            metrics.put("MetricName", metricValue);
        } catch (Exception e) {
            e.printStackTrace();
        }

        connector.close();

        return metrics;
    }

    // Consumer Metrics - Global Request Metrics
    @GetMapping("/consumer-metrics/{clientId}")
    public Map<String, Object> getConsumerMetrics(
            @PathVariable String clientId
    ) throws IOException, MalformedObjectNameException, AttributeNotFoundException,
            MBeanException, ReflectionException, InstanceNotFoundException, InterruptedException {

        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi");
        JMXConnector connector = JMXConnectorFactory.connect(url);
        MBeanServerConnection mbsc = connector.getMBeanServerConnection();

        String objectNameStr = "kafka.consumer:type=consumer-metrics,client-id=" + clientId;
        ObjectName consumerMetrics = new ObjectName(objectNameStr);

        Map<String, Object> metrics = new HashMap<>();
        try {
            double metricValue = (double) mbsc.getAttribute(consumerMetrics, "MetricName");
            metrics.put("MetricName", metricValue);
        } catch (Exception e) {
            e.printStackTrace();
        }

        connector.close();

        return metrics;
    }
}

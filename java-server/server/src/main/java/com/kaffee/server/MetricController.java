package com.kaffee.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
@RequestMapping("/get-metric")
public class MetricController {


  // Server Metrics
  @GetMapping("/bytes")
  public Object getBytesInOut() throws IOException, MalformedObjectNameException, AttributeNotFoundException,
    MBeanException, ReflectionException, InstanceNotFoundException, InterruptedException {
    Map<String, Object>object = new HashMap<>();
    JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi");
    JMXConnector connector = JMXConnectorFactory.connect(url);
    MBeanServerConnection mbsc = connector.getMBeanServerConnection();

    ObjectName bytesInMetric = new ObjectName("kafka.server:type=BrokerTopicMetrics,name=BytesInPerSec");
    ObjectName bytesOutMetric = new ObjectName("kafka.server:type=BrokerTopicMetrics,name=BytesOutPerSec");
    double bytesIn = (double) mbsc.getAttribute(bytesInMetric, "OneMinuteRate");
    double bytesOut = (double) mbsc.getAttribute(bytesOutMetric, "OneMinuteRate");
    object.put("bytesIn", bytesIn);
    object.put("bytesOut", bytesOut);
    connector.close();
    return object;
  }


  @GetMapping("/under-replicated-partitions")
  public Integer getUnderReplicatedPartitions() throws IOException, MalformedObjectNameException, AttributeNotFoundException,
      MBeanException, ReflectionException, InstanceNotFoundException, InterruptedException {
    JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi");
    JMXConnector connector = JMXConnectorFactory.connect(url);
    MBeanServerConnection mbsc = connector.getMBeanServerConnection();

    ObjectName underReplicatedPartitionMetric = new ObjectName("kafka.server:type=ReplicaManager,name=UnderReplicatedPartitions");

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

package com.kaffee.server.models;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class MetricSubscriptions {
  private int SERVER_JMX_PORT;
  private int PRODUCER_JMX_PORT;
  private int CONSUMER_JMX_PORT;
  private String KAFKA_URL;
  private String RESOLVED_URL;
  public Map<String, String> subscribedServerMetrics;
  public Map<String, String> serverMetrics;

  public MetricSubscriptions() {
    SERVER_JMX_PORT = 9999;
    KAFKA_URL = "localhost";

    String baseUrl = "service:jmx:rmi:///jndi/rmi://%s:%d/jmxrmi";
    RESOLVED_URL = String.format(baseUrl, KAFKA_URL, SERVER_JMX_PORT);
    serverMetrics = getServerMetricsStrings();

    subscribedServerMetrics = new HashMap<>();
  }

  public Map<String, String> getServerMetricsStrings() {
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

  public JMXConnector connectToJMX() throws IOException {
    JMXServiceURL url = new JMXServiceURL(RESOLVED_URL);
    return JMXConnectorFactory.connect(url);
  }

  
  public void addSubscription(String metric) throws NullPointerException {
    try {    
      String metricString = serverMetrics.get(metric);
      if (metricString != null) {
        subscribedServerMetrics.put(metric, metricString);
      }
    } catch (NullPointerException npe) {
      System.out.println(String.format("No metric found for string: %s", metric));
    }
  }

  public void removeSubscription(String metric) {
    subscribedServerMetrics.remove(metric);
    System.out.println("Unsubscribed from " + metric);
  }

  public int getJmxPort() {
    return this.SERVER_JMX_PORT;
  }

  public void setJmxPort(int port) {
    this.SERVER_JMX_PORT = port;
  }
}

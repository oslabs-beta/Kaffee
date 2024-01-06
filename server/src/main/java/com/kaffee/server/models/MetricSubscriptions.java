package com.kaffee.server.models;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Subscription to valid server metrics.
 *
 * This may need a class rename since producers and consumer metrics will
 * probably need to be handled separately.
 */
@ConfigurationProperties
public class MetricSubscriptions {
  /** The map of subscribed metrics. */
  private Map<String, String> subscribedServerMetrics = new HashMap<>();
  /**
   * The map of server metrics. Perhaps this doesn't need to be saved at the
   * class level and can just be returned from the getServerMetricsStrings
   * method.
   */
  private Map<String, String> serverMetrics;

  /** MetricSubscriptions instance. */
  private static MetricSubscriptions ms;

  /**
   * Constructor for MetricSubscriptions.
   *
   * Sets JMX Port, Kafka URL, and then resolved them into a valid JMX
   * endpoint.
   *
   * @throws IOException
   */
  private MetricSubscriptions() throws IOException {
    this.serverMetrics = getServerMetricsStrings();
  }

  /**
   * Return the instance of MetricSubscriptions, instantiating if needed.
   *
   * @return the current MetricSubscriptions instance
   */
  public static synchronized MetricSubscriptions getInstance()
      throws IOException {
    if (ms == null) {
      ms = new MetricSubscriptions();
    }

    return ms;
  }

  private String getResolvedUrl(final UserSettings us) {
    String kafkaUrl = us.getKafkaUrl();
    Integer jmxPort = us.getJmxPort();
    String baseUrl = "service:jmx:rmi:///jndi/rmi://%s:%d/jmxrmi";

    return String.format(baseUrl, kafkaUrl, jmxPort);
  }

  /**
   * Mapping of HTML element friendly name with a JMX endpoint.
   *
   * @return Map of the server metrics with a user-friendly name mapped to the
   *         corresponding JMX endpoint
   */
  @SuppressWarnings("checkstyle:LineLength")
  public Map<String, String> getServerMetricsStrings() {
    return new HashMap<String, String>() {
      {
        put("under-replicated-partitions",
            "kafka.server:type=ReplicaManager,name=UnderReplicatedPartitions");
        put("isr-shrinks",
            "kafka.server:type=ReplicaManager,name=IsrShrinksPerSec");
        put("isr-expands",
            "kafka.server:type=ReplicaManager,name=IsrExpandsPerSec");
        put("active-controller-count",
            "kafka.controller:type=KafkaController,name=ActiveControllerCount");
        put("offline-partitions-count",
            "kafka.controller:type=KafkaController,name=OfflinePartitionsCount");
        // put("leader-election-rate", "
        // kafka.controller:type=ControllerStats,name=LeaderElectionRateAndTimeMs");
        put("unclean-leader-selection",
            "kafka.controller:type=ControllerStats,name=UncleanLeaderElectionsPerSec");

        // The following won't work as listed, we may need to break this into
        // multiple parts for each element within {}
        // put("total-time-ms",
        // "kafka.network:type=RequestMetrics,name=TotalTimeMs,request={Produce|FetchConsumer|FetchFollower}");

        // The following won't work as listed, we may need to break this into
        // multiple parts for each element within {}
        // put("purgatory-size",
        // "kafka.server:type=DelayedOperationPurgatory,name=PurgatorySize,delayedOperation={Produce|Fetch}");

        put("bytes-in",
            "kafka.server:type=BrokerTopicMetrics,name=BytesInPerSec");
        put("bytes-out",
            "kafka.server:type=BrokerTopicMetrics,name=BytesOutPerSec");

        // The following won't work as listed, we may need to break this into
        // multiple parts for each element within {}
        // put("requests-per-second",
        // "kafka.network:type=RequestMetrics,name=RequestsPerSec,request={Produce|FetchConsumer|FetchFollower},version={0|1|2|3|\u2026}");

        // on JConsole these don't seem to match my system settings. I'm not
        // sure if this is because kafka was started in WSL
        // put("cpu-load", "java.lang:type=OperatingSystem,name=CpuLoad");

        // you can do this using java.io.file:
        // https://mkyong.com/java/how-to-get-free-disk-space-in-java/
        // put("disk-usage", "");

        // put("network-bytes-sent", "");
        // put("network-bytes-received", "");

        // from
        // https://www.datadoghq.com/blog/monitoring-kafka-performance-metrics/
        // put("young-garbaget", "java.lang:type=GarbageCollector,name=G1
        // Young Generation,");
        // put("old-garbage", "java.lang:type=GarbageCollector,name=G1 Old
        // Generation");
      }
    };
    // CHECKSTYLE: ON
  }

  /**
   * Connect to the JMX port we resolved upon construction. We should probably
   * resolve this outside of construction so we can re-resolve the service URL
   * when changing parts of the resolved url.
   *
   * @param us
   * @return JMXConnectorFactory connection
   * @throws IOException
   */
  public JMXConnector connectToJMX(final UserSettings us) throws IOException {
    String resolvedUrl = this.getResolvedUrl(us);
    JMXServiceURL url = new JMXServiceURL(resolvedUrl);
    return JMXConnectorFactory.connect(url);
  }

  /**
   * Add a metric to the list of subscribed metrics.
   *
   * @param metric
   * @throws NullPointerException thrown if metric isn't a key in the list of
   *                              valid metrics
   */
  public void addSubscription(final String metric) throws NullPointerException {
    try {
      String metricString = serverMetrics.get(metric);
      if (metricString != null) {
        this.subscribedServerMetrics.put(metric, metricString);
        System.out.println("Subscribed to " + metric);
      }
    } catch (NullPointerException npe) {
      System.out
          .println(String.format("No metric found for string: %s", metric));
    }
  }

  /**
   * Remove a metric subscription from the subscription list. Can we output
   * the message to a log, or maybe only output when debug is enabled?
   *
   * @param metric metric to unsubscribe from.
   */
  public void removeSubscription(final String metric) {
    this.subscribedServerMetrics.remove(metric);
    System.out.println("Unsubscribed from " + metric);
  }

  /**
   * Get the list of subscribed metrics.
   *
   * @return the current list of metrics for reporting
   */
  public Map<String, String> getSubscriptions() {
    return this.subscribedServerMetrics;
  }
}

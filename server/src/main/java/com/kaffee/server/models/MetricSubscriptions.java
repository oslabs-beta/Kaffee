package com.kaffee.server.models;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.kaffee.server.UserSettings.ReadSettings;

/**
 * Subscription to valid server metrics.
 *
 * This may need a class rename since producers and consumer metrics will
 * probably need to be handled separately.
 */
@ConfigurationProperties
public class MetricSubscriptions {
  /** The JMX port of the Kafka server. */
  private int serverJmxPort;
  /** The port of the Kafka server. */
  private int kafkaPort;
  /** The url of the Kafka server. */
  private String kafkaUrl;
  /**
   * The resolved URL. This should be done within a private method and
   * resolved when we need to use it. This way we can change either kafkaPort
   * or kafkaUrl and always get a new, valid URL.
   */
  private String resolvedUrl;
  /** The map of subscribed metrics. */
  public Map<String, String> subscribedServerMetrics = new HashMap<>();
  /**
   * The map of server metrics. Perhaps this doesn't need to be saved at the
   * class level and can just be returned from the getServerMetricsStrings
   * method.
   */
  private Map<String, String> serverMetrics;

  /**
   * Constructor for MetricSubscriptions.
   *
   * Sets JMX Port, Kafka URL, and then resolved them into a valid JMX
   * endpoint.
   *
   * @throws IOException
   */
  public MetricSubscriptions() throws IOException {
    this.setJmxPort();
    this.setKafkaUrl();
    // this.KAFKA_URL = "host.docker.internal";

    String baseUrl = "service:jmx:rmi:///jndi/rmi://%s:%d/jmxrmi";
    this.resolvedUrl = String.format(baseUrl, this.kafkaUrl, serverJmxPort);
    this.serverMetrics = getServerMetricsStrings();
  }

  /**
   * Mapping of HTML element friendly name with a JMX endpoint.
   *
   * @return Map of the server metrics with a user-friendly name mapped to the
   *         corresponding JMX endpoint
   */
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
  }

  /**
   * Connect to the JMX port we resolved upon construction. We should probably
   * resolve this outside of construction so we can re-resolve the service URL
   * when changing parts of the resolved url.
   *
   * @return JMXConnectorFactory connection
   * @throws IOException
   */
  public JMXConnector connectToJMX() throws IOException {
    JMXServiceURL url = new JMXServiceURL(this.resolvedUrl);
    return JMXConnectorFactory.connect(url);
  }

  public void reInitialize() throws IOException {
    setJmxPort();
    setKafkaUrl();
    String baseUrl = "service:jmx:rmi:///jndi/rmi://%s:%d/jmxrmi";
    resolvedUrl = String.format(baseUrl, this.kafkaUrl, serverJmxPort);
    serverMetrics = getServerMetricsStrings();
  }

  public void addSubscription(String metric) throws NullPointerException {
    try {
      String metricString = serverMetrics.get(metric);
      if (metricString != null) {
        subscribedServerMetrics.put(metric, metricString);
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
    subscribedServerMetrics.remove(metric);
    System.out.println("Unsubscribed from " + metric);
  }

  /**
   * Get the current value of the server's JMX port.
   *
   * @return the current server JMX port value.
   */
  public int getJmxPort() {
    return serverJmxPort;
  }

  /**
   * Set JMX Port by reading from the settings json.path/ This should be
   * abstracted into two different setter methods: Set it with a value given,
   * or set it without argument and read from a settings JSON model.
   *
   * @throws IOException
   * @throws IllegalArgumentException
   */
  public void setJmxPort() throws IOException, IllegalArgumentException {
    Integer newPort = Integer
        .parseInt(ReadSettings.main("JMX_PORT").toString());

    if (!this.isValidPort(newPort)) {
      throw new IllegalArgumentException("Invalid JMX Port value");
    }
    this.serverJmxPort = newPort;
  }

  /**
   * Get the current value of the Kafka URL.
   *
   * @return The current Kafka URL value
   */
  public String getKafkaUrl() {
    return this.kafkaUrl;
  }

  /**
   * Set Kafka URL by reading from the settings json.path/ This should be
   * abstracted into two different setter methods: Set it with a value given,
   * or set it without argument and read from a settings JSON model.
   *
   * Currently pattern matches for IP addresses. This may need to be changed
   * if we match to IRI. I have included the IRI regex in case we need to use
   * it. The Regex for this comes from the following StackOverflow answer:
   * https://stackoverflow.com/a/190405/22776634
   *
   * @throws IOException
   * @throws IllegalArgumentException
   */
  public void setKafkaUrl() throws IOException, IllegalArgumentException {
    String url = ReadSettings.main("KAFKA_URL").toString();
    String jmxPort = ReadSettings.main("JMX_PORT").toString();
    String baseUrl = "service:jmx:rmi:///jndi/rmi://" + url + ":" + jmxPort + "/jmxrmi";
    // UrlValidator uv = new UrlValidator();
    // if (!uv.isValid(baseUrl)) {
    //   throw new IllegalArgumentException("Kafka URL provided is invalid.");
    // }
    this.kafkaUrl = url;
  }

  /**
   * Get the current value of the Kafka port.
   *
   * @return the current Kafka port value.
   */
  public int getKafkaPort() {
    return this.kafkaPort;
  }

  /**
   * Set Kafka URL by reading from the settings json.path/ This should be
   * abstracted into two different setter methods: Set it with a value given,
   * or set it without argument and read from a settings JSON model.
   *
   * @throws IOException
   * @throws IllegalArgumentException
   */
  public void setKafkaPort() throws IOException, IllegalArgumentException {
    Integer port = Integer.parseInt(ReadSettings.main("KAFKA_PORT").toString());
    if (!this.isValidPort(port)) {
      throw new IllegalArgumentException("Invalid Kafka Port value");
    }
    this.kafkaPort = port;
  }

  /**
   * Ensures that a port is valid for setting validation.
   *
   * @param port The numeric value of a port for validation.
   * @return Boolean whether the provided port is a valid port.
   */
  private boolean isValidPort(final Integer port) {
    List<Integer> commonPorts = Arrays.asList(1433, 1434, 3306, 3389, 8080,
    8443);

    return (port > 1024 && port < 65535 && !commonPorts.contains(port));
  }
}

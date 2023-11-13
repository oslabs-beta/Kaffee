package com.kaffee.server.TestController;

import com.kaffee.server.UserSettings.ReadSettings;

import java.io.IOException;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * Configuration of the Kafka Consumer.
 */
public class KafkaConsumerConfig {
  /**
   * get the Kafka Consumer Properties.
   *
   * @return Properties: BOOTSTRAP_SERVERS_CONFIG,
   *         KEY_DESERIALIZER_CLASS_CONFIG, VALUE_DESERIALIZER_CLASS_CONFIG,
   *         _GROUP_ID_CONFIG
   * @throws IOException
   */
  public static Properties getConsumerProp() throws IOException {
    ReadSettings rs = new ReadSettings();

    String url = rs.getSetting("KAFKA_URL").toString();
    String port = rs.getSetting("KAFKA_PORT").toString();
    Properties property = new Properties();
    String socket = url + ":" + port;
    property.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, socket); // Kafka
                                                                   // broker
                                                                   // addresses
    property.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
        StringDeserializer.class.getName());
    property.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
        StringDeserializer.class.getName());
    property.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
    return property;
  }

}

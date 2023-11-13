package com.kaffee.server.TestController;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import com.kaffee.server.UserSettings.ReadSettings;

import java.io.IOException;
import java.util.Properties;

public class KafkaProducerConfig {
  public static Properties getProducerProperties() throws IOException {
    ReadSettings rs = new ReadSettings();

    String url = rs.getSetting("KAFKA_URL").toString();
    String port = rs.getSetting("KAFKA_PORT").toString();
    System.out.println(url);
    Properties properties = new Properties();
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, url + ":" + port); // Kafka
                                                                               // broker
                                                                               // addresses
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        StringSerializer.class.getName());
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        StringSerializer.class.getName());
    // Additional configurations
    System.out.println("PROPERTIES");
    System.out.println(properties);
    return properties;
  }
}
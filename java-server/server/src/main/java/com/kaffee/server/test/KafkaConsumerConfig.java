package com.kaffee.server.test;

import com.kaffee.server.UserSettings.ReadSettings;
import com.kaffee.server.controllers.ServerMetricController;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Consumer;

public class KafkaConsumerConfig {
  public static Properties getConsumerProp(){
    String url = ReadSettings.main("KAFKA_URL").toString();
    String port = ReadSettings.main("KAFKA_PORT").toString();
    Properties property = new Properties();
        property.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, url + ":" + port); // Kafka broker addresses
        property.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        property.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        property.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
    return property;
  }

  
}

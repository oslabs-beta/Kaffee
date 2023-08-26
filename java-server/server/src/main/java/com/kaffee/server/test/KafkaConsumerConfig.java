package com.kaffee.server.test;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Consumer;

public class KafkaConsumerConfig {
  public static Properties getConsumerProp(){
    Properties property = new Properties();
        property.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9094"); // Kafka broker addresses
        property.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        property.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        property.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
    return property;
  }

  
}

package com.kaffee.server.test;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import com.kaffee.server.UserSettings.ReadSettings;

import java.util.Properties;

public class KafkaProducerConfig {
    public static Properties getProducerProperties() {
    String url = ReadSettings.main("KAFKA_URL").toString();
    String port = ReadSettings.main("KAFKA_PORT").toString();
    System.out.println(url + ":" + port);
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, url + ":" + port); // Kafka broker addresses
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        
        // Additional configurations
        
        return properties;
    }
}
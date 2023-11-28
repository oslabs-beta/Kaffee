package com.kaffee.server.TestController;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import com.kaffee.server.controllers.SettingsController;
import com.kaffee.server.models.UserSettings;

import java.io.IOException;
import java.util.Properties;

public class KafkaProducerConfig {
  /** SettingsController link. */
  private static SettingsController sc;

  private static KafkaProducerConfig kpc = null;

  /**
   * Constructor for KafkaConsumerConfig.
   *
   * @param sc
   */
  public KafkaProducerConfig(final SettingsController sc) {
    this.sc = sc;
  }

  public static KafkaProducerConfig getInstance() {
    if (kpc == null) {
      kpc = new KafkaProducerConfig(sc);
    }

    return kpc;
  }

  /**
   * get the Kafka Producers Properties.
   *
   * @return Properties: BOOTSTRAP_SERVERS_CONFIG,
   *         KEY_DESERIALIZER_CLASS_CONFIG, VALUE_DESERIALIZER_CLASS_CONFIG
   * @throws IOException
   */
  public Properties getProducerProperties() throws IOException {
    UserSettings currentSettings = this.sc.getUserSettings();

    String url = currentSettings.getKafkaUrl();
    String port = currentSettings.getKafkaPort().toString();
    Properties properties = new Properties();
    String socket = url + ":" + port;

    // Kafka broker addresses
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, socket);
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

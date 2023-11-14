package com.kaffee.server.TestController;

import java.io.IOException;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.kaffee.server.controllers.SettingsController;
import com.kaffee.server.models.UserSettings;

/**
 * Configuration of the Kafka Consumer.
 */
public class KafkaConsumerConfig {
  /** SettingsController link. */
  private SettingsController sc;

  /**
   * Constructor for KafkaConsumerConfig.
   *
   * @param sc
   */
  public KafkaConsumerConfig(final SettingsController sc) {
    this.sc = sc;
  }

  /**
   * get the Kafka Consumer Properties.
   *
   * @return Properties: BOOTSTRAP_SERVERS_CONFIG,
   *         KEY_DESERIALIZER_CLASS_CONFIG, VALUE_DESERIALIZER_CLASS_CONFIG,
   *         _GROUP_ID_CONFIG
   * @throws IOException
   */
  public Properties getConsumerProp() throws IOException {
    UserSettings currentSettings = this.sc.getUserSettings();

    String url = currentSettings.getKafkaUrl();
    String port = currentSettings.getKafkaPort().toString();
    Properties property = new Properties();
    String socket = url + ":" + port;

    // Kafka broker addresses
    property.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, socket);
    property.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
        StringDeserializer.class.getName());
    property.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
        StringDeserializer.class.getName());
    property.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
    return property;
  }

}

package com.kaffee.server;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.kaffee.server.controllers.SettingsController;
import com.kaffee.server.models.MessageData;
import com.kaffee.server.models.MetricSubscriptions;

/**
 * Configuration of the entrypoint for the server.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
@Import({ WebSocketConfig.class })
public class ServerApplication {

  /**
   * Main method to run the web server.
   *
   * @param args arguments to run the server
   */
  public static void main(final String[] args) {
    SpringApplication.run(ServerApplication.class, args);
  }

  /**
   * Create the Bean for MessageData.
   *
   * @return new MessageData
   */
  @Bean
  public MessageData messageData() {
    return new MessageData();
  }

  /**
   * Create the Bean for SettingsController.
   *
   * @return new SettingsContoller
   */
  @Bean
  public SettingsController settingsController() throws IOException {
    return new SettingsController();
  }

  /**
   * Create the Bean for MetricSubscriptions.
   *
   * @return new MetricSubscriptions
   * @throws IOException
   */
  @Bean
  public MetricSubscriptions metricSubscriptions() throws IOException {
    return new MetricSubscriptions();
  }

}

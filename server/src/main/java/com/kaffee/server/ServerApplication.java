package com.kaffee.server;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.plaf.BorderUIResource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;

import com.kaffee.server.controllers.SettingsController;
import com.kaffee.server.models.ApiError;
import com.kaffee.server.models.MessageData;
import com.kaffee.server.models.MetricSubscriptions;
import com.kaffee.server.models.UserSettings;

/**
 * Configuration of the entrypoint for the server.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
@Import({ WebSocketConfig.class })
public class ServerApplication {

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
   * Create the Bean for UserSettings.
   *
   * @return new UserSettings
   */
  @Bean
  public UserSettings userSettings() throws IOException {
    return new SettingsController().getUserSettingsFromFile();
  }

  /**
   * Create the Bean for MetricSubscriptions.
   *
   * @return new MetricSubscriptions
   * @throws IOException
   */
  @Bean
  public MetricSubscriptions metricSubscriptions() throws IOException {
    return MetricSubscriptions.getInstance();
  }

  /**
   * Main method to run the web server.
   *
   * @param args arguments to run the server
   */
  public static void main(final String[] args) {
    SpringApplication.run(ServerApplication.class, args);
    // new SpringApplicationBuilder(ServerApplication.class).headless(false)
    // .run(args);
  }

}

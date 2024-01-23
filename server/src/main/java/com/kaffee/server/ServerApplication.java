package com.kaffee.server;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.kaffee.server.controllers.DataAndLogController;
import com.kaffee.server.controllers.SettingsController;
import com.kaffee.server.models.FileHandler;
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
   * @return MetricSubscriptions instance
   * @throws IOException
   */
  @Bean
  public MetricSubscriptions metricSubscriptions() throws IOException {
    return MetricSubscriptions.getInstance();
  }

  /**
   * Create the Bean for fileHandler.
   *
   * @return FileHandler instance.
   * @throws IOException
   */
  @Bean
  public FileHandler fileHandler() throws IOException {
    return FileHandler.getInstance();
  }

  /**
   * Main method to run the web server.
   *
   * @param args arguments to run the server
   */
  public static void main(final String[] args) throws IOException {
    FileHandler fh = null;
    try {
      fh = FileHandler.getInstance();
      SpringApplication.run(ServerApplication.class, args);
    } catch (IOException ioex) {
      ioex.printStackTrace();
    } finally {
      try {
        if (fh != null) {
          fh.close();
        }
      } catch (IOException ioex) {
        ioex.printStackTrace();
      }
    }
  }

}

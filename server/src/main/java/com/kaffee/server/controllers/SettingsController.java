package com.kaffee.server.controllers;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kaffee.server.models.UserSettings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Class to read settings from a settings.json file.
 */
@Controller
@RestController
@RequestMapping("/")
public class SettingsController {
  /** The UserSettings record. */
  private UserSettings us;

  /** Location to read the settings. */
  private String fileLocation;

  /** default string for where to find the settings file. */

  // private String final DEFAULT_PATH =
  // "src/main/java/com/kaffee/server/settings.json";
  private static final String DEFAULT_PATH = "src/main/resources/settings.json";

  /**
   * Constructor for SettingsController, using the default file path.
   */
  public SettingsController() throws IllegalArgumentException, IOException {
    this.setFileLocation(DEFAULT_PATH);
    this.us = this.getUserSettingsFromFile();
  }

  /**
   * Constructor for SettingsController, using a given filepath.
   *
   * @param location String location of where the file is located
   * @throws IOException
   */
  public SettingsController(final String location)
      throws IllegalArgumentException, IOException {
    this.setFileLocation(location);
    this.us = this.getUserSettingsFromFile();
  }

  /**
   * Set the class's fileLocation property.
   *
   * @param location The location of the settings file.
   */
  public void setFileLocation(final String location) {
    this.fileLocation = Paths.get(location).toString();
  }

  /**
   * Get the user settings from the initialized settings file.
   *
   * @return UserSettings object with matching values for settings
   * @throws IllegalArgumentException if stored values are out of range
   * @throws IOException
   */
  public UserSettings getUserSettingsFromFile()
      throws IllegalArgumentException, IOException {
    JSONObject settingsJson = this.getSettingsJson();

    Integer jmxPort = this.convertObjToInteger(settingsJson.get("JMX_PORT"));
    String kafkaUrl = settingsJson.get("KAFKA_URL").toString();
    Integer kafkaPort = this
        .convertObjToInteger(settingsJson.get("KAFKA_PORT"));
    Integer metricCount = this
        .convertObjToInteger(settingsJson.get("metric-count"));
    Integer consumers = this.convertObjToInteger(settingsJson.get("consumers"));
    Integer producers = this.convertObjToInteger(settingsJson.get("producers"));

    String logFilePath = settingsJson.get("log-filepath").toString();

    return new UserSettings(jmxPort, kafkaUrl, kafkaPort, metricCount,
        consumers, producers, logFilePath);
  }

  /**
   * Get the current UserSettings.
   *
   * @return the current UserSettings object.
   */
  public UserSettings getUserSettings() {
    return this.us;
  }

  private Integer convertObjToInteger(final Object obj) {
    return Integer.parseInt(obj.toString());
  }

  /**
   * Method for validating data and saving it to the correct location.
   *
   * @param setting Setting to update
   * @param value   New value for setting
   * @return The current settings of the UserSettings, updated if there are no
   *         exceptions, unmodified if there are exceptions.
   * @throws IllegalArgumentException
   * @throws IOException
   */
  public UserSettings saveUserSettings(final String setting, final Object value)
      throws IllegalArgumentException, IOException {
    try {
      switch (setting) {
      case "metric-count":
        this.us.setMetricCount(this.convertObjToInteger(value));
        break;
      case "JMX_PORT":
        this.us.setJmxPort(this.convertObjToInteger(value));
        break;
      case "KAFKA_PORT":
        this.us.setKafkaPort(this.convertObjToInteger(value));
        break;
      case "consumers":
        this.us.setConsumers(this.convertObjToInteger(value));
        break;
      case "producers":
        this.us.setProducers(this.convertObjToInteger(value));
        break;
      case "KAFKA_URL":
        this.us.setKafkaUrl(value.toString());
        break;
      case "log-filepath":
        this.us.setLogFilePath(value.toString());
        break;
      default:
        break;
      }

      // save user settings
      BufferedWriter writer = new BufferedWriter(
          new FileWriter(this.fileLocation));
      String newSettingsStringified = this.us.convertToJson().toString();
      writer.write(newSettingsStringified);
      writer.close();
    } catch (Exception e) {
      System.out
          .println("Caught exception in SettingsController.saveUserSettings");
      System.out.println(e);
    }

    return this.us;
  }

  /**
   * Retrieve the settings from the designated fileLocation.
   *
   * @return JSONObject holding the settings contents
   * @throws IOException
   */
  private JSONObject getSettingsJson() throws IOException {
    BufferedReader settingsReader = this.openFile(Path.of(this.fileLocation));
    String settingsContents = settingsReader.lines()
        .collect(Collectors.joining(System.lineSeparator()));
    // close the buffer now that we have read it.
    settingsReader.close();

    return new JSONObject(settingsContents);
  }

  /**
   * Open a file given a string location. This is separated for testing.
   *
   * @param location The relative path to the resource
   * @return the BufferedReader of opening the file
   * @throws IOException
   */
  private BufferedReader openFile(final Path location) throws IOException {
    BufferedReader settingsReader = Files.newBufferedReader(location);

    return settingsReader;
  }

  /**
   * Retrieve a particular key out of the default settings.json file.
   *
   * @param key A key whose value to read
   * @return The value equal to the key given
   * @throws IOException
   */
  public Object getSettingFromFile(final String key) throws IOException {
    JSONObject settings = this.getSettingsJson();
    return settings.get(key);
  }
}

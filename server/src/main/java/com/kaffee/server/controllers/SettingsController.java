package com.kaffee.server.controllers;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kaffee.server.models.FileHandler;
import com.kaffee.server.models.UserSettings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class to read settings from a settings.json file.
 */
@Controller
@RestController
@RequestMapping("/")
public class SettingsController {
  /** The UserSettings object. */
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

    UserSettings loadedSettings = UserSettings.getInstance(jmxPort, kafkaUrl,
        kafkaPort, metricCount, consumers, producers, logFilePath);
    return loadedSettings;
  }

  /**
   * Get the current UserSettings.
   *
   * @return the current UserSettings object.
   */
  public UserSettings getUserSettings() {
    return this.us;
  }

  /**
   * Format the UserSettings object to the JSON format we expect for storage.
   *
   * @return UserSettings converted to JSON and keys switched to match
   *         settings.json
   */
  public JSONObject getUserSettingsFormattedJson() {
    JSONObject currentSettings = this.us.convertToJson();
    return this.convertJavaKeyToJSON(currentSettings);
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
  public UserSettings saveUserSetting(final String setting, final Object value)
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
        FileHandler fh = FileHandler.getInstance();
        fh.setDirectory(value.toString());

        this.us.setLogFilePath(value.toString());
        break;
      default:
        break;
      }

      // save user settings
      BufferedWriter writer = new BufferedWriter(
          new FileWriter(this.fileLocation));
      JSONObject convertedObject = this
          .convertJavaKeyToJSON(this.us.convertToJson());
      String newSettingsStringified = convertedObject.toString();
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

  /**
   * Method for remapping UserSettings properties to JSON keys. We have to do
   * this because some of the JSON keys are not valid Java variable names.
   *
   * @param userSettings UserSettings object converted to JSONObject
   * @return JSONObject with keys re-assigned.
   */
  private JSONObject convertJavaKeyToJSON(final JSONObject userSettings) {
    JSONObject returnObject = userSettings;

    Map<String, String> keyMapping = new HashMap<String, String>() {
      {
        put("metricCount", "metric-count");
        put("kafkaPort", "KAFKA_PORT");
        put("jmxPort", "JMX_PORT");
        put("kafkaUrl", "KAFKA_URL");
        put("logFilePath", "log-filepath");
      }
    };

    for (Map.Entry<String, String> entry : keyMapping.entrySet()) {
      String currentKey = entry.getKey();
      Object keyValue = returnObject.get(currentKey);
      returnObject.put(entry.getValue(), keyValue);
      returnObject.remove(currentKey);
    }

    return returnObject;
  }

  /**
   * GET routing for getting the current settings.
   *
   * @return ResponseEntity with OK status and the contents of settings.json
   *         stringified
   * @throws IOException
   */
  @GetMapping("/getSettings")
  private ResponseEntity<String> getSettings() throws IOException {
    /*
     * The following is the old method // Declare path to settings.json String
     * resourceName = "src/main/java/com/kaffee/server/settings.json"; // Read
     * all bytes as bytes[], then stringify using new String(); String
     * stringified = new String( Files.readAllBytes(Paths.get(resourceName)));
     */

    try {
      JSONObject currentSettings = this.getUserSettingsFormattedJson();
      String stringified = currentSettings.toString();
      // Return ResponseEntity with status 200 & body containing settings.json
      return ResponseEntity.ok(stringified);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toString());
    }
  }

  /**
   * set Post route to /updateSettings.
   *
   * @param body The ResponseBody string
   * @return ResponseEntity with status OK and the string "Updated!"
   */
  @PostMapping("/updateSettings")
  // declare argument using annotation @RequestBody to get body from request
  public ResponseEntity<String> updateSettings(@RequestBody final String body)
      throws IOException {
    // Convert body to JSON
    JSONObject reqBody = new JSONObject(body);
    // Get settingName and newValue from reqbody as String
    String settingName = reqBody.getString("settingName");
    Object newValue = reqBody.get("newValue");

    UserSettings newSettings = this.saveUserSetting(settingName, newValue);

    return ResponseEntity.ok(newSettings.convertToJson().toString());
  }
}

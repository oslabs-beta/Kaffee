/**
 * Management of user settings.
 */
package com.kaffee.server.UserSettings;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Class to read settings from a settings.json file. Shouldn't this be in a
 * model, connected through a separate controller?
 */
public record ReadSettings() {
  /** Location to read the settings. */
  private static String fileLocation;

  /**
   * Main method to read settings from the JSON file.
   *
   * @param key key within the settings.json file to read.
   * @return the value of the indicated key within the file.
   * @throws IOException
   */
  public void main() throws IOException {
    // declare String resource name and set value to settings file path
    // I believe we should be storing this file in server/resources so that it
    // is properly located after complilation.
    // String resourceName = "src/main/java/com/kaffee/server/settings.json";

    this.setFileLocation("src/resources/settings.json");
  }

  /**
   * Main method to read settings from the JSON file.
   *
   * @param key key within the settings.json file to read.
   * @return the value of the indicated key within the file.
   * @throws IOException
   */
  public void main(final String location) throws IOException {
    this.setFileLocation(location);
  }

  /**
   * Set the class's fileLocation property.
   *
   * @param location The location of the settings file.
   */
  public void setFileLocation(final String location) {
    this.fileLocation = location;
  }

  /**
   * Open a file given a string location.
   *
   * @param location The relative path to the resource
   * @return the BufferedReader of opening the file
   * @throws IOException
   */
  private BufferedReader openFile(final String location) throws IOException {
    Path settingsPath = Paths.get(location);
    BufferedReader settingsReader = Files.newBufferedReader(settingsPath);

    return settingsReader;
  }

  /**
   * Retrieve a particular key out of the default settings.json file.
   *
   * @param key A key whose value to read
   * @return The value equal to the key given
   * @throws IOException
   */
  public Object getSetting(final String key) throws IOException {

    BufferedReader settingsReader = this.openFile(this.fileLocation);
    String settingsContents = settingsReader.lines()
        .collect(Collectors.joining(System.lineSeparator()));
    // close the buffer now that we have read it.
    settingsReader.close();

    JSONObject settings = new JSONObject(settingsContents);
    System.out.println("KEY: " + key);
    System.out.println("VALUE: " + settings.get(key));
    return settings.get(key);
  }

  /**
   * Retrieve a particular key out of the settings.json file, with given
   * BufferedReader.
   *
   * @param key            A key whose value to read
   * @param settingsReader The BufferedReader containing the JSON
   * @return The value equal to the key given
   * @throws IOException
   */
  public static Object getSetting(final String key,
      final BufferedReader settingsReader) throws IOException {

    String settingsContents = settingsReader.lines()
        .collect(Collectors.joining(System.lineSeparator()));
    // close the buffer now that we have read it.
    settingsReader.close();

    JSONObject settings = new JSONObject(settingsContents);
    System.out.println("KEY: " + key);
    System.out.println("VALUE: " + settings.get(key));
    return settings.get(key);
  }
}

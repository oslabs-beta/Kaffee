package com.kaffee.server.UserSettings;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class to read settings from a settings.json file. Shouldn't this be in a
 * model, connected through a separate controller?
 */
public record ReadSettings() {

  /**
   * Main method to read settings from the JSON file.
   *
   * @param key key within the settings.json file to read.
   * @return the value of the indicated key within the file.
   * @throws IOException
   */
  public static Object main(final String key) throws IOException {
    // declare String resource name and set value to settings file path
    // I believe we should be storing this file in server/resources so that it
    // is properly located after complilation.
    String resourceName = "classes/com/kaffee/server/settings.json";
    // pass path to Files.readAllBytes, convert byte array to String using new
    // String();
    String stringified = new String(
        Files.readAllBytes(Paths.get(resourceName)));
    // convert stringified json to object
    JSONObject object = new JSONObject(stringified);
    // debugging system console logs
    System.out.println("KEY: " + key);
    System.out.println("VALUE: " + object.get(key));
    // return updated value
    return object.get(key);
  }
}

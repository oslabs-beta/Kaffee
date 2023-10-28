package com.kaffee.server.UserSettings;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public record ReadSettings(){
  public static Object main(String key) throws IOException {
    //declare String resource name and set value to settings file path
    String resourceName = "src/main/java/com/kaffee/server/settings.json";
    //pass path to Files.readAllBytes, convert byte array to String using new String();
    String stringified = new String(Files.readAllBytes(Paths.get(resourceName)));
    //convert stringified json to object
    JSONObject object = new JSONObject(stringified);
    //debugging system console logs
    System.out.println("KEY: " + key);
    System.out.println("VALUE: " + object.get(key));
    //return updated value
    return object.get(key);
  }
}
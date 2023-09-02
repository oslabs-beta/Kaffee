package com.kaffee.server.UserSettings;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.Map;


public record ReadSettings(){
  public static Object main(String key) {
    String resourceName = "../../../../settings.json";
    InputStream is = ReadSettings.class.getResourceAsStream(resourceName);
    if (is == null) {
        throw new NullPointerException("Cannot find resource file " + resourceName);
    }
    JSONTokener tokener = new JSONTokener(is);
    JSONObject object = new JSONObject(tokener);
    return object.get(key);
  }
}
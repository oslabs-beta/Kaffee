package com.kaffee.server.UserSettings;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;

public record ReadSettings(){
  public static void main(String[] args) {
    String resourceName = "./settings.json";
    InputStream is = ReadSettings.class.getResourceAsStream(resourceName);
    if (is == null) {
        throw new NullPointerException("Cannot find resource file " + resourceName);
    }
    JSONTokener tokener = new JSONTokener(is);
    JSONObject object = new JSONObject(tokener);
    System.out.println("object: " + object);
  }
}
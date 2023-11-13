package com.kaffee.server.controllers;

import com.kaffee.server.UserSettings.ReadSettings;
import com.kaffee.server.models.MetricSubscriptions;

import org.springframework.web.bind.annotation.RestController;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/")
public class KaffeeSettingsController {
  // Get Settings Route and Handler

  /**
   * Autowiring the ApplicationContext.
   */
  @Autowired
  ApplicationContext context;

  /**
   * GET routing for getting the current settings.
   *
   * @return ResponseEntity with OK status and the contents of settings.json
   *         stringified
   * @throws IOException
   */
  @GetMapping("/getSettings")
  private ResponseEntity<String> getSettings() throws IOException {
    // Declare path to settings.json
    String resourceName = "src/main/java/com/kaffee/server/settings.json";
    // Read all bytes as bytes[], then stringify using new String();
    String stringified = new String(
        Files.readAllBytes(Paths.get(resourceName)));
    // Return ResponseEntity with status 200 & body containing settings.json
    return ResponseEntity.ok(stringified);
  }

  /**
   * set Post route to /updateSettings.
   *
   * @return ResponseEntity with status OK and the string "Updated!"
   */
  @PostMapping("/updateSettings")
  // declare argument using annotation @RequestBody to get body from request
  public ResponseEntity<String> updateSettings(@RequestBody String body)
      throws IOException {
    // Convert body to JSON
    JSONObject reqBody = new JSONObject(body);
    // Get settingName and newValue from reqbody as String
    String settingName = reqBody.getString("settingName");
    String newValue = reqBody.getString("newValue");
    // declare path to settings.json
    String resourceName = "src/main/java/com/kaffee/server/settings.json";
    // Read all bytes as bytes[], then stringify using new String()
    String stringified = new String(
        Files.readAllBytes(Paths.get(resourceName)));
    // Convert Stringified JSON to JSONObject using new JSONObject()
    JSONObject json = new JSONObject(stringified);
    // Update requested setting using .put()
    json.put(settingName, newValue);
    // convert updated settings json to string
    String reString = json.toString();
    // convert stringified settings json to an array of bytes (byte[])
    byte[] jsonToBytes = reString.getBytes();
    // overwrite old settings file with the updated settings file
    Files.write(Paths.get(resourceName), jsonToBytes);
    ReadSettings rs = new ReadSettings();
    rs.getSetting(settingName);
    // refresh Connection to use new Settings
    MetricSubscriptions ms = context.getBean("metricSubscriptions",
        MetricSubscriptions.class);
    ms.reInitialize();
    // MetricSubscriptions newMs = ms.getBean("metricSubscriptions",
    // MetricSubscriptions.class);
    // newMs.reInitialize();

    // this should really return the JSON of the updated file,
    // which could aid in testing and also user feedback: e.g. if they
    // click save and the settings reverted, this would indicated that
    // there was an error.
    return ResponseEntity.ok("Updated!");
  }
}

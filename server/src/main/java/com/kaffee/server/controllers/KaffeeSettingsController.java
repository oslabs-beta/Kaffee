package com.kaffee.server.controllers;

import com.kaffee.server.models.UserSettings;

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

@RestController
@RequestMapping("/")
public class KaffeeSettingsController {
  // Get Settings Route and Handler

  /** The SettingsContoller class. */
  private final SettingsController sc;

  /**
   * Create KaffeeSettingsController with a connection to UserSettings.
   *
   * @param sc
   */
  public KaffeeSettingsController(final SettingsController sc) {
    this.sc = sc;
  }

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
    /*
     * The following is the old method // Declare path to settings.json String
     * resourceName = "src/main/java/com/kaffee/server/settings.json"; // Read
     * all bytes as bytes[], then stringify using new String(); String
     * stringified = new String( Files.readAllBytes(Paths.get(resourceName)));
     */

    UserSettings us = this.sc.getUserSettings();
    String stringified = us.convertToJson().toString();
    // Return ResponseEntity with status 200 & body containing settings.json
    return ResponseEntity.ok(stringified);
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
    String newValue = reqBody.getString("newValue");

    UserSettings newSettings = this.sc.saveUserSettings(settingName, newValue);

    return ResponseEntity.ok(newSettings.convertToJson().toString());
  }
}

package com.kaffee.server.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.kaffee.server.UserSettings.ReadSettings;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


@RestController
@RequestMapping("/")
public class KaffeeSettingsController {
//Get Settings
  @GetMapping("/getSettings")
  private ResponseEntity getSettings() throws IOException{
    String resourceName = "src/main/java/com/kaffee/server/settings.json";
    String stringified = new String(Files.readAllBytes(Paths.get(resourceName)));
    System.out.println(stringified);
    return ResponseEntity.ok(stringified);
  }
}
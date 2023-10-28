package com.kaffee.server.controllers;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


@RestController
@RequestMapping("/")
public class KaffeeSettingsController {
//Get Settings Route and Handler
  @GetMapping("/getSettings")
  private ResponseEntity<String> getSettings() throws IOException{
    //Declare path to settings.json
    String resourceName = "src/main/java/com/kaffee/server/settings.json";
    //Read all bytes as bytes[], then stringify using new String();
    String stringified = new String(Files.readAllBytes(Paths.get(resourceName)));
    //Return ResponseEntity with status 200 & body containing settings.json
    return ResponseEntity.ok(stringified);
  }
}
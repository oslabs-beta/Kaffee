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
public class DataAndLogController{
//Get Historical Log Route
@GetMapping("/getLogFiles")
  private ResponseEntity<String> getLogFiles() throws IOException{
    String resourceName = "/Users/lapduke/Desktop/Kaffee1.1/Kaffee/Historical_Logs/History.json";
    String stringified = new String(Files.readAllBytes(Paths.get(resourceName)));
    return ResponseEntity.ok(stringified);
  }
}
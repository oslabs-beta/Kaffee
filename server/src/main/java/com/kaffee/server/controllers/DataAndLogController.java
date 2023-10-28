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
  private ResponseEntity<String> getLogFiles(){
    
    return ResponseEntity.ok()
  }
}
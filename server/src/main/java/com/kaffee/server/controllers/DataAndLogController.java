package com.kaffee.server.controllers;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;



@RestController
@RequestMapping("/")
public class DataAndLogController{
//Get Historical Log Route
@GetMapping("/getLogFiles")
  private ResponseEntity<String[]> getLogFiles() throws IOException{
    //directory path
    String resourceName = "/Users/lapduke/Desktop/Kaffee1.1/Kaffee/Historical_Logs/";
    //declare a new File at the directory path
    File directory = new File(resourceName);
    //declare a variable files which is an array of string of file names contained in the directory.
    String files[] = directory.list();
    //iterate through the files and list their name & their content using Files.readString
    for(int i = 0; i<files.length; i++){
      Path filePath = Paths.get("/Users/lapduke/Desktop/Kaffee1.1/Kaffee/Historical_Logs/",files[i]);
      System.out.println(files[i] + ": " + "\n" + (Files.readString(filePath)));
    }
    //return the array of files
    return ResponseEntity.ok(files);
  }
}
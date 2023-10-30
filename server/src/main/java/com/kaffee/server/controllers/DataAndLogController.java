package com.kaffee.server.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.apache.kafka.common.protocol.types.Field.Bool;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.io.File;



@RestController
@RequestMapping("/")
public class DataAndLogController{
  //Get Historical Log Route
  @GetMapping("/getLogFiles")
  private ResponseEntity<String[]> getLogFiles() throws IOException{
    //directory path
    String resourceName = "Historical_Logs/";
    //declare a new File at the directory path
    File directory = new File(resourceName);
    //declare a variable files which is an array of string of file names contained in the directory.
    String files[] = directory.list();
    //iterate through the files and list their name & their content using Files.readString
    if(files != null){
      for(int i = 0; i<files.length; i++){
        Path filePath = Paths.get("Historical_Logs/",files[i]);
        System.out.println(files[i] + ": " + "\n" + (Files.readString(filePath)));
      }
    }

    //return the array of files
    return ResponseEntity.ok(files);
  }
  @PostMapping("/addData")
  private ResponseEntity<String> addData(@RequestBody String body) throws IOException{
    //declare filename and path
    String filename = "Historical_Logs/" + LocalDate.now().toString() + "_log.json";
    JSONObject data = new JSONObject(body);
    Iterator<String> dataKeys = data.keys();
    String metricName = dataKeys.next();
    String metricValue = data.getString(metricName);
    try {
      //instantiate a new File passing in the path and filename as an arg
      File newLog = new File(filename);
      //.createNewFile(); returns a boolean if the file exists or not. If it doesn't exist, creates the file, if it does exist, create the file
      Boolean check = newLog.createNewFile();
      newLog.createNewFile();
      if(check){
        String emptyObj = "{labels: [], datasets: []}";
        Files.write(Paths.get(filename), emptyObj.getBytes());
      }
      String stringified = new String(Files.readAllBytes(Paths.get(filename)));
      JSONObject jsonFile = new JSONObject(stringified);
      String labelsObject = jsonFile.get("labels").toString();
      String reString = jsonFile.toString();
      Files.write(Paths.get(filename), reString.getBytes());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Not Created: " + e);
    }
    return ResponseEntity.ok("Created!");
  }
}
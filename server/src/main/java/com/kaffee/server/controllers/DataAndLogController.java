package com.kaffee.server.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;
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
  @GetMapping("/getData")
  private ResponseEntity<String> getData(@RequestParam String filename) throws IOException{
    try {
      //find file with the requested name
      Path filePath = Paths.get("Historical_Logs/",filename);
      String stringifiedFile = Files.readString(filePath);
      return ResponseEntity.ok(stringifiedFile);
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Not Created: " + e);
    }

  }
  @PostMapping("/addData")
  private ResponseEntity<String> addData(@RequestBody String body) throws IOException{

    //declare filename and path
    String filename = "Historical_Logs/" + LocalDate.now().toString() + "_log.json";
    JSONObject data = new JSONObject(body);
    //get the metric name from request body
    String metricName = data.keys().next();
    //get the metric data from request body
    JSONObject metricValues = data.getJSONObject(metricName);
    JSONArray metricTimeLabels = metricValues.getJSONArray("labels");
    JSONArray datasets = metricValues.getJSONArray("datasets");
    try {
      //instantiate a new File passing in the path and filename as an arg
      File newLog = new File(filename);
      //.createNewFile(); returns a boolean if the file exists or not. If it doesn't exist, creates the file, if it does exist, create the file
      Boolean check = newLog.createNewFile();
      newLog.createNewFile();
      if(check){
        String emptyObj = "{}";
        Files.write(Paths.get(filename), emptyObj.getBytes());
      }
      //get the file
      String stringified = new String(Files.readAllBytes(Paths.get(filename)));
      //parse the file into a json object
      JSONObject jsonFile = new JSONObject(stringified);
      //if metric name exists, skip creation of key
      //else create metric name key
      if(!jsonFile.has(metricName)){
        jsonFile.put(metricName, "{labels: [], datasets: [{label: 'One Minute Rate', data: []},{label: 'Fifteen Minute Rate', data: []},{label: 'Five Minute Rate', data: []},{label: 'Mean Rate', data: []}]}");
      }
      //push new timestamp labels to labels
      JSONObject curMetrics = new JSONObject(jsonFile.getString(metricName));
      JSONArray timestamps = curMetrics.getJSONArray("labels");
      timestamps.putAll(metricTimeLabels);
      curMetrics.put("labels", timestamps);
      System.out.println(curMetrics);
      //push new data to correct dataset with corresponding matching label
      for(int i = 0; i < datasets.length(); i++){
        //get the new dataset
        JSONArray newDataSet = datasets.getJSONObject(i).getJSONArray("data");
        //get the files dataset
        JSONArray curDataSet = curMetrics.getJSONArray("datasets").getJSONObject(i).getJSONArray("data");
        //push new dataset to curdata set
        curDataSet.putAll(newDataSet);
      }
      //stringify jsonfile
      jsonFile.put(metricName, curMetrics);
      String reString = jsonFile.toString();
      //write updated log file to path
      Files.write(Paths.get(filename), reString.getBytes());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Not Created: " + e);
    }
    return ResponseEntity.ok("Created!");
  }
}
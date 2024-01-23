package com.kaffee.server.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.kaffee.server.models.FileHandler;
import com.kaffee.server.models.MessageData;

import org.apache.tomcat.util.collections.CaseInsensitiveKeyMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Controller for logging the metric data read.
 */
@RestController
@RequestMapping("/")
public class DataAndLogController {
  /** Store the FileHandler instance. */
  private FileHandler fh;

  /**
   * Create a DataAndLogController.
   */
  public DataAndLogController(final FileHandler fh) throws IOException {
    this.fh = fh;
  }

  /**
   * API Endpoint for returning log liles.
   *
   * @return Response entity OK (200) and the array of files in the directory
   * @throws IOException
   */
  @GetMapping("/getLogFiles")
  private ResponseEntity<String[]> getLogFiles() throws IOException {
    // directory path
    String logDirectory = fh.getDirectory();
    // declare a new File at the directory path
    File directory = new File(logDirectory);
    // declare a variable files which is an array of string of file names
    // contained in the directory.
    String[] files = directory.list();
    // iterate through the files and list their name & their content using
    // Files.readString
    // for (int i = 0; i < files.length; i++) {
    // Path filePath = Paths.get(logDirectory, files[i]);
    // System.out.println(files[i] + ": " + "\n" +
    // (Files.readString(filePath)));
    // }
    // return the array of files
    return ResponseEntity.ok(files);
  }

  @GetMapping("/getData")
  private ResponseEntity<String> getData(@RequestParam String filename)
      throws IOException {
    try {
      // find file with the requested name
      Path filePath = Paths.get(fh.getDirectory(), filename);
      System.out.println(filePath.toString());

      JSONObject fileContents = fh.readFromLog(filePath.toString());
      // String fileContents = "ok";
      return ResponseEntity.ok(fileContents.toString());
    } catch (Exception e) {
      return ResponseEntity.status(500).body("File not found: " + e);
    }
  }

  /*
  // Ideally we shouldn't send data from the back to the front and back to the
  // back unless the user has modified data somehow.
  // This should be replaced with adding data as it's being read.
  // NOTE: Should we log all data as it's read, or only data that we display?
  @PostMapping("/addData")
  private ResponseEntity<String> addData(@RequestBody String body)
      throws IOException {
    // declare filename and path
    String filename = "history/" + LocalDate.now().toString() + "_log.json";
    JSONObject data = new JSONObject(body);
    // get the metric name from request body
    String metricName = data.keys().next();
    // get the metric data from request body
    JSONObject metricValues = data.getJSONObject(metricName);
    JSONArray metricTimeLabels = metricValues.getJSONArray("labels");
    JSONArray datasets = metricValues.getJSONArray("datasets");
    try {
      // instantiate a new File passing in the path and filename as an arg
      File newLog = new File(filename);
      // .createNewFile(); returns a boolean if the file exists or not. If it
      // doesn't exist, creates the file, if it does exist, create the file
      Boolean check = newLog.createNewFile();
      newLog.createNewFile();
      if (check) {
        String emptyObj = "{}";
        Files.write(Paths.get(filename), emptyObj.getBytes());
      }
      // get the file
      String stringified = new String(Files.readAllBytes(Paths.get(filename)));
      // parse the file into a json object
      JSONObject jsonFile = new JSONObject(stringified);
      // if metric name exists, skip creation of key
      // else create metric name key
      if (!jsonFile.has(metricName)) {
        jsonFile.put(metricName, new JSONObject(
            "{labels: [], datasets: [{label: 'One Minute Rate', data: []},{label: 'Count', data: []},{label: 'Fifteen Minute Rate', data: []},{label: 'Five Minute Rate', data: []},{label: 'Mean Rate', data: []}]}"));
      }
      // push new timestamp labels to labels
      JSONObject curMetrics = jsonFile.getJSONObject(metricName);
      JSONArray timestamps = curMetrics.getJSONArray("labels");
      timestamps.putAll(metricTimeLabels);
      curMetrics.put("labels", timestamps);
      // push new data to correct dataset with corresponding matching label
      for (int i = 0; i < datasets.length(); i++) {
        // get the new dataset
        JSONObject newDataSet = datasets.getJSONObject(i);
        // get the current label
        String curLabel = newDataSet.getString("label");
        // .getJSONArray("data");
        for (int j = 0; j < datasets.length(); j++) {
          // get the files dataset
          JSONObject curDataSet = curMetrics.getJSONArray("datasets")
              .getJSONObject(j);
          if (curDataSet.getString("label").equals(curLabel)) {
            // push new dataset to curdata set
            curDataSet.getJSONArray("data")
                .putAll(newDataSet.getJSONArray("data"));
          }
        }
      }
      // stringify jsonfile
      jsonFile.remove(metricName);
      jsonFile.put(metricName, curMetrics);
      String reString = jsonFile.toString();
      // write updated log file to path
      Files.write(Paths.get(filename), reString.getBytes());
    } catch (Exception e) {
      System.out.println(e);
      return ResponseEntity.status(500).body("Not Created: " + e);
    }
    return ResponseEntity.status(200).body("Created!");
  }
  */

}

package com.kaffee.server.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.kaffee.server.models.MessageData;

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
public class DataAndLogController implements AutoCloseable {
  /** String with path to log file location. */
  private String directoryLocation;

  /** File that is currently being written. */
  private File logFile;

  /** Buffered writer for writing to logFile. */
  private BufferedWriter fileWriter;

  /**
   * Create a DataAndLogController at the default directory. Default directory
   * is "history"
   */
  public DataAndLogController() throws IOException {
    new DataAndLogController("history");
  }

  /**
   * Create a DataAndLogController at a given directory.
   *
   * @param dirLocation The relative path of the logs directory
   */
  public DataAndLogController(final String dirLocation) throws IOException {
    this.setDirectory(dirLocation);

    this.setCurrentFile();
  }

  @Override
  public void close() {
    try {
      fileWriter.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
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
    String logDirectory = this.getDirectory();
    // declare a new File at the directory path
    File directory = new File(logDirectory);
    // declare a variable files which is an array of string of file names
    // contained in the directory.
    String[] files = directory.list();
    // iterate through the files and list their name & their content using
    // Files.readString
    for (int i = 0; i < files.length; i++) {
      Path filePath = Paths.get(logDirectory, files[i]);
      System.out.println(files[i] + ": " + "\n" + (Files.readString(filePath)));
    }
    // return the array of files
    return ResponseEntity.ok(files);
  }

  @GetMapping("/getData")
  private ResponseEntity<String> getData(@RequestParam String filename)
      throws IOException {
    try {
      // find file with the requested name
      Path filePath = Paths.get(this.getDirectory(), filename);
      String stringifiedFile = Files.readString(filePath);
      return ResponseEntity.ok(stringifiedFile);
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Not Created: " + e);
    }
  }

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

  /**
   * Set the directory of the log files.
   *
   * @param dirLocation A string location of the directory
   */
  private void setDirectory(final String dirLocation) {
    // Previous to this implementation, we used
    // "/Users/lapduke/Desktop/Kaffee1.1/Kaffee/history"
    Path path = Paths.get(System.getProperty("user.dir"));

    this.directoryLocation = path.resolve(dirLocation).toString();
  }

  /**
   * Get the currently set directory of the log files.
   *
   * @return the resolved full path of the log files directory.
   */
  private String getDirectory() {
    return this.directoryLocation;
  }

  /**
   * Generate filename for saving file.
   *
   * @return the filename with correctly formatted date.
   */
  private String generateFileName() {
    String datePrefix = this.formatDate();
    String fileSuffix = "_log.json";

    return datePrefix + fileSuffix;
  }

  /**
   * Generate the string of today's date for use with generateFileName.
   *
   * @return String of today's date formatted as 'YYYY-MM-DD'
   */
  private String formatDate() {
    return LocalDate.now().toString();
  }

  /**
   * Set the current logFile and fileWriter, closing the old writer if it was
   * set and exists. This should handle cases where the program runs over
   * multiple days.
   */
  private void setCurrentFile() throws IOException {
    String directory = this.directoryLocation;
    String currentFileName = this.generateFileName();

    String currentFilePath = directory + currentFileName;
    this.logFile = new File(currentFilePath);

    if (this.fileWriter != null) {
      try {
        this.fileWriter.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }

    try {
      this.fileWriter = new BufferedWriter(new FileWriter(this.logFile, true));
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  private Boolean checkDate() throws FileNameException {
    String today = this.formatDate();

    Pattern datePattern = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2})");
    Matcher matcher = datePattern.matcher(this.logFile.toString());

    if (!matcher.matches()) {
      throw new FileNameException(
          "File name does not contain correctly formatted date.");
    }
    String logDate = matcher.group(1);

    return today.equals(logDate);
  }

  /**
   * Write to data to current log file.
   *
   * @param metricData
   */
  public void saveToLog(final MessageData metricData)
      throws FileNameException, IOException {
    // If the log file hasn't been initialized, otherwise make sure the date
    // is the same as current.
    if (this.logFile == null || !this.checkDate()) {
      this.setCurrentFile();
    }

    this.fileWriter.append(this.formatMetricDataForLog(metricData));
  }

  private JSONObject readFromLog() throws FileNotFoundException, IOException {
    JSONObject logData;

    try {
      BufferedReader reader = new BufferedReader(new FileReader(this.logFile));
      String line = reader.readLine();
      while (line != null) {

        logData = this.formatLogDataForChart(line, logData);
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return logData;
  }

  /**
   * Append data from a logfile line to existing JSON object, formatted for
   * sending to the front end and display in Chart component.
   *
   * Each log file line is formatted like: "<TIMESTAMP>: <METRIC NAME>:
   * <VALUES>"
   *
   * ChartJS will want a JSON object like: "{<METRIC NAME>: {"labels": {
   * [<TIMESTAMPS>], "datasets": [<VALUES>] } }"
   *
   * @param logData
   * @param existingData
   * @return returns modified JSON object with new data added
   */
  private JSONObject formatLogDataForChart(final String logData,
      final JSONObject existingData) throws ParseException {
    // ChartJS does funky things when the timeline is out of order. I'm not
    // sure if we need
    // to handle potentially out of date timestamps.

    Pattern logPattern = Pattern.compile(
        "^(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}): ([a-z]*): (.*)$");
    Matcher matcher = logPattern.matcher(logData);

    SimpleDateFormat dateFormat = new SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss.SSS");
    Date parsedTime = dateFormat.parse(matcher.group(1));
    Long time = parsedTime.getTime();

    String metricName = matcher.group(2);

    JSONObject values = new JSONObject(matcher.group(3));
    JSONArray labels;
    JSONArray datasets;

    if (existingData.has(metricName)) {
      JSONObject existingMetric = existingData.getJSONObject(metricName);
      labels = existingMetric.getJSONArray("labels");
      datasets = existingMetric.getJSONArray("datasets");

      labels.put(time);
      datasets.put(values);
    } else {
      JSONObject chartJsonObject = new JSONObject();
      chartJsonObject.put("labels", new JSONArray());
      chartJsonObject.put("datasets", new JSONArray());
      existingData.put(metricName, chartJsonObject);
    }
    // For readability, saved as: "<TIMESTAMP>: <METRIC NAME>: <VALUES>"
    // "yyyy-MM-dd'T'HH:mm:ss.SSS");

    // we need to return data formatted like this:
    /**
     * { "bytes-in": { labels: [time1, time2, time3 ] datasets: [ { label:
     * OneMinuteRate, data[data1, data2, data3] } ] } "isr-shrinks": { labels:
     * [time1, time2, time3 ] datasets: [ { label: OneMinuteRate, data[data1,
     * data2, data3] } ] } }
     */
    return existingData;
  }

  /**
   * Format MessageData object to string for writing to logFile.
   *
   * @param data Single metric read to be stored in the log.
   * @return String formatted in the style "<TIMESTAMP>: <METRIC NAME>:
   *         <VALUES>".
   */
  private String formatMetricDataForLog(final MessageData data) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss.SSS");
    Date timestamp = new Date(data.getTime());
    String time = dateFormat.format(timestamp);

    String metricName = data.getMetric();

    JSONObject jsonValues = new JSONObject(data.getSnapshot());
    String values = jsonValues.toString();

    return time + ": " + metricName + ": " + values;
  }
}


class FileNameException extends Exception {
  /**
   * Exception to handle incorrectly formatted filename.
   *
   * @param errorMessage
   */
  FileNameException(final String errorMessage) {
    super(errorMessage);
  }
}

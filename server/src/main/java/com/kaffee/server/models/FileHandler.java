package com.kaffee.server.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;

public final class FileHandler {
  /** FileHandler instance for singleton implementation. */
  private static FileHandler fh;

  /** String with path to log file location. */
  private String directoryLocation;

  /** File that is currently being written. */
  private File logFile;

  /** Current date to speed up checking if date has rolled over. */
  private LocalDate currDate;

  /** Buffered writer for writing to logFile. */
  private BufferedWriter bufferWriter;

  /** The queue of logs to write to the logFile. */
  private final BlockingQueue<MessageData> logQueue = new LinkedBlockingQueue<>();

  private FileHandler() throws IOException {
    this.setDirectory("history");

    this.setCurrentFile();
    this.startLoggingThread();
  }

  /**
   * Return the instmance of FileHandler, instantiating if needed.
   *
   * @return the current FileHandler instance
   * @throws IOException
   */
  public static synchronized FileHandler getInstance() throws IOException {
    if (fh == null) {
      fh = new FileHandler();
    }

    return fh;
  }

  public void close() {
    MessageData terminationSignal = MessageData.createTerminationSignal();
    logQueue.add(terminationSignal);
  }

  /**
  * Set the directory of the log files.
  *
  * @param dirLocation A string location of the directory
  */
  public void setDirectory(final String dirLocation) {
    // Previous to this implementation, we used
    // "/Users/lapduke/Desktop/Kaffee1.1/Kaffee/history"
    Path path = Paths.get(System.getProperty("user.dir"), "resources");

    directoryLocation = path.resolve(dirLocation).toString();
  }

  /**
  * Get the currently set directory of the log files.
  *
  * @return the resolved full path of the log files directory.
  */
  public String getDirectory() {
    return this.directoryLocation;
  }

  /**
   * Generate filename for saving file.
   *
   * @return the filename with correctly formatted date.
   */
  private String generateFileName() {
    if (currDate == null) {
      this.setCurrentDate();
    }

    String datePrefix = this.formatDate(currDate);
    String fileSuffix = "_log.json";

    return datePrefix + fileSuffix;
  }

  /**
   * Generate the string of today's date for use with generateFileName.
   * This is a separate method in case we wish to modify the
   * formatted version (like for a locale).
   *
   * @param date The LocalDate object to format
   * @return String of today's date formatted as 'YYYY-MM-DD'
   */
  private String formatDate(final LocalDate date) {
    return date.toString();
  }

  /**
   * Check if the current date matches the date of the open file.
   *
   * @return whether the date matches the date of the open file.
   */
  private Boolean checkDate() throws IllegalStateException {
    LocalDate today = LocalDate.now();

    return today == currDate;
  }

  /**
  * Set the current logFile and fileWriter, closing the old writer if it was
  * set and exists. This should handle cases where the program runs over
  * multiple days.
  */
  private void setCurrentFile() throws IOException {
    if (bufferWriter != null) {
      try {
        bufferWriter.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }

    this.setCurrentDate();

    String currentFileName = this.generateFileName();

    Path filePath = Paths.get(directoryLocation, currentFileName);
    this.logFile = new File(filePath.toString());

    try {
      FileWriter fileWriter = new FileWriter(this.logFile, true);
      bufferWriter = new BufferedWriter(fileWriter);

    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  private void setCurrentDate() {
    currDate = LocalDate.now();
  }

  @Scheduled(cron = "0 0 0 * * ?")
  private void changeFileAtDayStart() throws IOException {
    try {
      this.setCurrentFile();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
  * Add Message data to the queue to log.
  *
  * @param metricData
  */
  public synchronized void saveToLog(final MessageData metricData) {
    this.logQueue.add(metricData);
    // System.out.println("Function called.");
  }

  /**
   * Begin the thread that will pull items from the logQueue and log them.
   */
  public void startLoggingThread() {
    Thread loggingThread = new Thread(() -> {
      try {
        while (true) {
          //   // Take is a blocking dequeue operation.
          MessageData logEntry = this.logQueue.take();

          if (logEntry == null || logEntry.isTerminationSignal()) {
            // Terminate if the termination signal is added to the queue.
            break;
          }

          String dataToLog = this.formatMetricDataForLog(logEntry);
          System.out.println(dataToLog);
          bufferWriter.write(dataToLog);
          bufferWriter.newLine();
          bufferWriter.flush();
        }
      } catch (InterruptedException | IOException ex) {
        ex.printStackTrace();
      }
    });

    // Mark the thread as a daemon so it won't prevent application shutdown
    loggingThread.setDaemon(true);
    loggingThread.start();
  }

  /**
  * Read a given log file and format it for display in ChartJS.
  *
  * @param filename The String representing the path to the file
  * @return JSON formatted for ChartJS display
  * @throws FileNotFoundException
  * @throws IOException
  */
  public JSONObject readFromLog(final String filename)
      throws FileNotFoundException, IOException {
    JSONObject logData = new JSONObject();

    try {
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      String line = reader.readLine();
      while (line != null) {
        logData = this.formatLogDataForChart(line, logData);
      }

      reader.close();
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
        "^(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}): ([a-z]*): (.*)$",
        Pattern.CASE_INSENSITIVE);
    Matcher matcher = logPattern.matcher(logData);
    System.out.println(logData);
    matcher.find();

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

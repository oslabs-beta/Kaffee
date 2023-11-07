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

/**
 * Controller for logging the metric data read.
 */
@RestController
@RequestMapping("/")
public class DataAndLogController {
  /** String with path to log file location. */
  private String directoryLocation;

  /**
   * Create a DataAndLogController at the default directory. Default directory
   * is "Historical_Logs"
   */
  public DataAndLogController() {
    this.setDirectory("Historical_Logs");
  }

  /**
   * Create a DataAndLogController at a given directory.
   *
   * @param dirLocation The relative path of the logs directory
   */
  public DataAndLogController(final String dirLocation) {
    this.setDirectory(dirLocation);
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

  /**
   * Set the directory of the log files.
   *
   * @param dirLocation A string location of the directory
   */
  private void setDirectory(final String dirLocation) {
    // Previous to this implementation, we used
    // "/Users/lapduke/Desktop/Kaffee1.1/Kaffee/Historical_Logs"
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
   * Generate filename function. Created so that DataAndLogControllerTest can
   * compile. In reality filename generation should be private, and
   * untestable. A work around is create a file through some public method,
   * and then verify the newly created file passes any tests.
   *
   * @return A dummy string.
   */
  public static String generateFileName() {
    return "filename";
  }
}

package com.kaffee.server.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.kaffee.server.ServerApplication;

// @ContextConfiguration(classes = ServerApplication.class)
@SpringBootTest(classes = DataAndLogController.class)
// @WebMvcTest(DataAndLogController.class)
public class DataAndLogControllerTests {
  /**
   * Autowiring MockMvc.
   */
  // @Autowired
  // private MockMvc mvc;

  /**
   * Generating the MockBean for dataAndLog.
   */
  @MockBean
  private DataAndLogController dataAndLog;

  /**
   * Test that filenames have the format YYYY-MM-DD_log.json.
   */
  @Test
  @DisplayName("Check that new log files have the format YYYY-MM-DD_log.json")
  void validFileNameFormat() {
    String filename = dataAndLog.generateFileName();
    System.out.println(filename);

    Pattern fnPattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}_log\\.json");

    assertTrue(fnPattern.matcher(filename).matches());
  }

  /**
   * Check that new files have today's date.
   */
  @Test
  @DisplayName("Check that new log files are created using the current date")
  void validFileNameDate() {
    DataAndLogController dal = new DataAndLogController();
    String filename = dataAndLog.generateFileName();

    Date date = Calendar.getInstance().getTime();
    String formatString = "yyyy-mm-dd";
    DateFormat dateFormat = new SimpleDateFormat(formatString);
    String strDate = dateFormat.format(date);

    assertEquals(filename.substring(0, formatString.length()), strDate);
  }

  // Test that data can be written to a file

  // Test that data is correctly appended to a given file

  // Test that the file directory can be read

  // Test that data can be read from an indicated file

  // Test that the JSON sent to the front end for display is correctly
  // formatted

  // Test that a file can be deleted from the GUI
  // This was never implemented, but we shouldn't create a log file if we
  // can't
  // also allow a user to remove the indicated file.
}

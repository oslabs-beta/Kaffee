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
  // @MockBean

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

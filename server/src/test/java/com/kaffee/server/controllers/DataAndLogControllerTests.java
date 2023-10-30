package com.kaffee.server.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

// JUnit 5 doesn't require @RunWith
@WebMvcTest(DataAndLogController.class)
public class DataAndLogControllerTests {
  @Autowired
  private MockMvc mvc;

  @MockBean
  private DataAndLogController dataAndLog;

  // Test that the log file name are correctly formatted
  @Test
  void validFileName() {
  }

  // Test that data can be written to a file

  // Test that data is correctly appended to a given file

  // Test that the file directory can be read
  
  // Test that data can be read from an indicated file

  // Test that the JSON sent to the front end for display is correctly formatted

  // Test that a file can be deleted from the GUI
  // This was never implemented, but we shouldn't create a log file if we can't
  // also allow a user to remove the indicated file.
}

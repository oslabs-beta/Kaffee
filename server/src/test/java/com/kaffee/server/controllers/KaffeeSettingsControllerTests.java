package com.kaffee.server.controllers;

import com.kaffee.server.ServerApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@ContextConfiguration(classes=ServerApplication.class)
@WebMvcTest(ServerMetricController.class)
public class KaffeeSettingsControllerTests {
      
  @Autowired
  private MockMvc mockMvc;

  // Verify that we can get the settings

  // verify that each setting exists when loaded
  // kafka url
  // kafka port
  // jmx port
  // filepath for logs <-- since we can't create a web interface to save this, do we want to set this?
  // producers
  // consumers
  // tick-count <-- I called this metric-count, but this was confusing. Can we workshop a better name?

  // verify that we can update the settings as a batch

  // verify that we can update each setting individually
  // This is if we want to do individual updates for some reason, otherwise omit
}

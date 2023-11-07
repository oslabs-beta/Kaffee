package com.kaffee.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration for the WebMvc.
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

  /**
   * Add cors mappings to the WebMvc.
   */
  @Override
  public void addCorsMappings(final CorsRegistry registry) {
    registry.addMapping("/**");
  }
}

package com.kaffee.server;

import java.io.IOError;
import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.kaffee.server.models.MessageData;
import com.kaffee.server.models.MetricSubscriptions;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@Import({ WebSocketConfig.class })
public class ServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

  @Bean
  public MessageData messageData() {
    return new MessageData();
  }

  @Bean
  public MetricSubscriptions metricSubscriptions() throws IOException{
    return new MetricSubscriptions();
  }
}

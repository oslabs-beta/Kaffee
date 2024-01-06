package com.kaffee.server.models;

import org.springframework.http.HttpStatus;

import lombok.Data;

/**
 * Error handling in order to give a friendly response to API Errors.
 */
@Data
public class ApiError {
  private HttpStatus status;
  private Integer statusCode;
  private String message;
  private String debugMessage;

  /**
   * Private constructor.
   */
  public ApiError() {

  }

  /**
   * Default constructor with only error code.
   * 
   * @param status HTTP Status Code
   */
  public ApiError(HttpStatus status) {
    this();
    this.status = status;
    this.statusCode = status.value();

  }

  /**
   * Overload for status and thrown error.
   * 
   * @param status HTTP Status Code
   * @param ex     Thrown error
   */
  public ApiError(HttpStatus status, Throwable ex) {
    this();
    this.status = status;
    this.statusCode = status.value();
    this.message = "Unexpected Error";
    this.debugMessage = ex.getLocalizedMessage();
  }

  /**
   * Overload with status, thrown error, and custom message.
   * 
   * @param status  HTTP Status Code
   * @param message Custom message
   * @param ex      Thrown error
   */
  public ApiError(HttpStatus status, String message, Throwable ex) {
    this();
    this.status = status;
    this.statusCode = status.value();
    this.message = message;
    this.debugMessage = ex.getLocalizedMessage();
  }
}

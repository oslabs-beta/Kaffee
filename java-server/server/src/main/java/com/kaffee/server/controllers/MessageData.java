package com.kaffee.server.controllers;

public class MessageData {
  private String content;
  private String time;

  public MessageData() {

  }

  public MessageData(String content, String time) {
    this.content = content;
    this.time = time;
  }

  public String getContent() {
    return content;
  }
}

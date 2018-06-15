package com.hapramp.steemconnect4j;

public class SteemConnectException extends Exception {
  public String name;
  public String error;
  public String description;

  /**
  * constructor for SteemConnectException.
  * @param name             name of exception
  * @param error            error
  * @param description      description about exception
  */
  public SteemConnectException(String name, String error, String description) {
    this.name = name;
    this.error = error;
    this.description = description;
  }

  @Override
  public String toString() {
    return "SteemConnectException{"
            + "name='"
            + name
            + '\''
            + ", error='"
            + error
            + '\''
            + ", description='"
            + description
            + '\''
            + '}';
  }
}

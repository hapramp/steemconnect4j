package com.hapramp.steemconnect4j;

public class Beneficiary {
  private String account;
  private int weight;

  public Beneficiary(String account, int weight) {
    this.account = account;
    this.weight = weight;
  }

  @Override
  public String toString() {
    return "{\"account\":\"" + account + "\""
      + ",\"weight\":" + weight + "}";
  }
}

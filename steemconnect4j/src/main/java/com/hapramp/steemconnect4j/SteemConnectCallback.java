package com.hapramp.steemconnect4j;

public interface SteemConnectCallback {
  void onResponse(String response);
  void onError(SteemConnectException e);
}

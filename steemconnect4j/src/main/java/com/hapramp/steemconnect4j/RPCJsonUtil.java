package com.hapramp.steemconnect4j;

public class RPCJsonUtil {

  /**
    * formats key value pairs.
    * @param key    key
    * @param value  value
    * @return       "key":"value"
  */
  public static String getKeyValuePair(String key, String value) {
    return "\"" + key + "\":\"" + value + "\"";
  }

  /**
    * formats key value pairs.
    * @param key    key
    * @param value  value
    * @return       {"key":"value"}
  */
  public static String getObjectString(String key , String value){
    return "{\"" + key + "\":\"" + value + "\"}";
  }

  /**
    * formats key value pairs.
    * @param key    key
    * @param value  value
    * @return       ["key":"value"]
  */
  public static String getArrayString(String key , String value){
    return "[\"" + key + "\":\"" + value + "\"]";
  }
}

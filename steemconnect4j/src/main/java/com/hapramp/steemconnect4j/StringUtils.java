package com.hapramp.steemconnect4j;

public class StringUtils {
  /**
    * formats the string array to a comman separated string.
    * @param strings    a string array
    * @return           a ,(comman) separated string
  */
  public static String getCommaSeparatedString(String[] strings) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < strings.length; i++) {
      stringBuilder.append(strings[i]);
      if (i != strings.length - 1) {
        stringBuilder.append(",");
      }
    }
    return stringBuilder.toString();
  }

  /**
    * formats strings to a comman separated string. Adds '{' before and '}'
    * after the string.
    *
    * @param strings   a variable number of strings
    * @return          a string like {"a","b"}
    *
  */
  public static String getCommanSeparatedObjectString(String... strings) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("{");
    for (int i = 0; i < strings.length; i++) {
      stringBuilder.append(strings[i]);
      if (i != strings.length - 1) {
        stringBuilder.append(",");
      }
    }
    stringBuilder.append("}");
    return stringBuilder.toString();
  }

  /**
    * formats strings to a comman separated string. Adds '[' before and ']'
    * after the string.
    *
    * @param strings  variable number of strings
    * @return         a string like ["a","b"]
  */
  public static String getCommanSeparatedArrayString(String... strings) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[");
    for (int i = 0; i < strings.length; i++) {
      stringBuilder.append(strings[i]);
      if (i != strings.length - 1) {
        stringBuilder.append(",");
      }
    }
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
}

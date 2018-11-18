package com.hapramp.steemconnect4j;

import java.util.ArrayList;
import java.util.Map;

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

  /**
  * formats values of map to url query parameters.
  * @param map  map of key value pairs of query
  * @return     formatted string for query parameters
  */
  public static String getQueryParamsFromMap(Map<String, String> map) {
    StringBuilder queryBuilder = new StringBuilder();
    for (Map.Entry<String, String> entry : map.entrySet()) {
      queryBuilder.append(entry.getKey())
        .append("=")
        .append(entry.getValue())
        .append("&");
    }
    return queryBuilder.toString();
  }

  /**
  * formats string of operations string.
  * @param value    value for operations key
  * @return         object form of operations string
  */
  public static String getOperationsString(String value) {
    return "{\"operations\":" + value + "}";
  }

  /**
   * Formats string for `comment_options` operation.
   * @param author              author
   * @param permlink            perlink
   * @param percentSteemDollars percentSteemDollars
   * @param beneficiaries       List of beneficiaries
   * @return                    Formatted string
   */
  public static String getCommentOptionStringFormat(String author,
                                                    String permlink,
                                                    int percentSteemDollars,
                                                    ArrayList<Beneficiary>
                                                      beneficiaries) {
    return "{" + "\"author\": \"" + author + "\","
      + "\"permlink\":" + " \"" + permlink + "\","
      + "\"max_accepted_payout\": \"1000000.000 SBD\","
      + "\"percent_steem_dollars\": " + percentSteemDollars + ","
      + "\"allow_votes\": true,"
      + "\"allow_curation_rewards\": true,"
      + "\"extensions\": ["
      + "[0, {"
      + "\"beneficiaries\": " + beneficiaries.toString() + "}]"
      + "]"
      + "}";
  }
}

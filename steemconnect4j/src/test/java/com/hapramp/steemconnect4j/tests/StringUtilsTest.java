package com.hapramp.steemconnect4j.tests;

import static org.junit.Assert.assertEquals;

import com.hapramp.steemconnect4j.StringUtils;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;




public class StringUtilsTest {
  @Test
  public void test_getCommaSeparatedString() {
    String[] testStrings = {"first","second"};
    String res = StringUtils.getCommaSeparatedString(testStrings);
    assertEquals("first,second",res);
  }

  @Test
  public void test_getCommanSeparatedObjectString() {
    String res = StringUtils
        .getCommanSeparatedObjectString("\"voter\"","\"permlink\"","\"weight\"");
    assertEquals("{\"voter\",\"permlink\",\"weight\"}",res);
  }

  @Test
  public void test_getCommanSeparatedArrayString() {
    String csos = StringUtils.getCommanSeparatedObjectString("\"voter\"","\"permlink\"");
    String csas = StringUtils.getCommanSeparatedArrayString("\"vote\"",csos);
    String exp = "[\"vote\",{\"voter\",\"permlink\"}]";
    print(exp,csas);
    assertEquals(exp,csas);
  }

  @Test
  public void test_singleItemInArrayString() {
    String csas = StringUtils.getCommanSeparatedArrayString("vote");
    String exp = "[vote]";
    print(exp,csas);
    assertEquals(exp,csas);
  }

  @Test
  public void test_QueryParameterBuilder() {
    String redirectUri = "http://google.com";
    Map<String,String> map = new HashMap<>();
    map.put("auth","allowed");
    map.put("user","bxute");
    map.put("id","10");
    String url = String.format("%s/sign/%s?%s%s", "https://hapramp.com",
        "follow", StringUtils.getQueryParamsFromMap(map),
        "redirectUri=" + (redirectUri != null && (redirectUri.length() > 0) ? redirectUri : ""));
    String exp = "https://hapramp.com/sign/follow?auth=allowed&id=10&user=bxute&redirectUri=http://google.com";
    assertEquals(exp,url);
  }

  private void print(String exp,String actual) {
    System.out.println("Expected: " + exp);
    System.out.println("Actual: " + actual);
  }
}

package com.hapramp.steemconnect4j.tests;

import static org.junit.Assert.assertEquals;

import com.hapramp.steemconnect4j.StringUtils;
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

  private void print(String exp,String actual) {
    System.out.println("Expected: " + exp);
    System.out.println("Actual: " + actual);
  }
}

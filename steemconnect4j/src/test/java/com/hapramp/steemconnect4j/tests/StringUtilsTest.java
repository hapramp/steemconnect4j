package com.hapramp.steemconnect4j.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.hapramp.steemconnect4j.HttpMethod;
import com.hapramp.steemconnect4j.NetworkUtils;
import com.hapramp.steemconnect4j.Route;
import com.hapramp.steemconnect4j.RpcJsonUtil;
import com.hapramp.steemconnect4j.SteemConnectCallback;
import com.hapramp.steemconnect4j.SteemConnectException;
import com.hapramp.steemconnect4j.SteemConnectOptions;
import com.hapramp.steemconnect4j.StringUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.junit.Test;



public class StringUtilsTest {
  @Test
  public void testGetCommaSeparatedString() {
    String[] testStrings = {"first","second"};
    String res = StringUtils.getCommaSeparatedString(testStrings);
    assertEquals("first,second",res);
  }

  @Test
  public void testGetCommanSeparatedObjectString() {
    String res = StringUtils
        .getCommanSeparatedObjectString("\"voter\"","\"permlink\"","\"weight\"");
    assertEquals("{\"voter\",\"permlink\",\"weight\"}",res);
  }

  @Test
  public void testGetCommanSeparatedArrayString() {
    String csos = StringUtils.getCommanSeparatedObjectString("\"voter\"","\"permlink\"");
    String csas = StringUtils.getCommanSeparatedArrayString("\"vote\"",csos);
    String exp = "[\"vote\",{\"voter\",\"permlink\"}]";
    print(exp,csas);
    assertEquals(exp,csas);
  }

  @Test
  public void testSingleItemInArrayString() {
    String csas = StringUtils.getCommanSeparatedArrayString("vote");
    String exp = "[vote]";
    print(exp,csas);
    assertEquals(exp,csas);
  }

  @Test
  public void testQueryParameterBuilder() {
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

  @Test
  public void testHttpMethodClass() {
    assertEquals("GET", HttpMethod.GET);
    assertEquals("POST",HttpMethod.POST);
    assertEquals("PUT",HttpMethod.PUT);
  }

  @Test
  public void testNetworkUtils() {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    try {
      NetworkUtils.request("http://httpbin.org/get", "GET", "", "", new SteemConnectCallback() {
        @Override
        public void onResponse(String response) {
          print(response,response);
          assertTrue(response.length() > 0);
          countDownLatch.countDown();
        }

        @Override
        public void onError(SteemConnectException e) {
          System.out.print(e.description);
          assertFalse(e.error.length() > 0);
          countDownLatch.countDown();
        }
      });
      countDownLatch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testRouteClass() {
    assertEquals("broadcast", Route.BROADCAST);
    assertEquals("me",Route.ME);
    assertEquals("oauth2/token/revoke",Route.REVOKE_TOKEN);
  }

  @Test
  public void testGetKeyValuePair() {
    String kv = RpcJsonUtil.getKeyValuePair("a","b");
    String exp = "\"a\":b";
    assertEquals(exp,kv);
  }

  @Test
  public void testGetObjectString() {
    String os = RpcJsonUtil.getObjectString("a","b");
    String exp = "{a:b}";
    assertEquals(exp,os);
  }

  @Test
  public void testGetArrayString() {
    String as = RpcJsonUtil.getArrayString("a","b");
    String exp = "[a:b]";
    assertEquals(exp,as);
  }

  @Test
  public void testSteemConnectOptionsClass() {
    SteemConnectOptions steemConnectOptions = new SteemConnectOptions();
    steemConnectOptions.setAccessToken("token");
    steemConnectOptions.setApp("app");
    steemConnectOptions.setBaseUrl("baseUrl");
    steemConnectOptions.setScope(new String[]{"scope1","scope2"});
    steemConnectOptions.setState("loggedin");
    steemConnectOptions.setCallback("callback");
    assertEquals("token", steemConnectOptions.getAccessToken());
    assertEquals("app", steemConnectOptions.getApp());
    assertEquals("callback", steemConnectOptions.getCallback());
    assertTrue(steemConnectOptions.getScope().length == 2);
    assertEquals("loggedin", steemConnectOptions.getState());
    assertEquals("baseUrl",steemConnectOptions.getBaseUrl());
  }

  private void print(String exp,String actual) {
    System.out.println("Expected: " + exp);
    System.out.println("Actual: " + actual);
  }
}

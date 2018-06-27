package com.hapramp.steemconnect4j;

public class SteemConnectOptions {
  public static final String BASE_URL = "https://v2.steemconnect.com";
  private String baseUrl;
  private String app;
  private String state;
  private String callback;
  private String[] scope;
  private String clientSecret;
  private String accessToken;

  /**
  * SteemConnectOptions class constructor.
  */
  public SteemConnectOptions() {
    this.baseUrl = BASE_URL;
  }

  /**
  * returns base url.
  * @return baseUrl
  */
  public String getBaseUrl() {
    return baseUrl;
  }

  /**
  * sets base url.
  * @param baseUrl base url
  */
  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  /**
  * helper method to get app name.
  * @return app name
  */
  public String getApp() {
    return app;
  }

  /**
  * sets app name.
  * @param app   app name
  */
  public void setApp(String app) {
    this.app = app;
  }

  /**
  * helper method to get callback url.
  * @return  callback url
  */
  public String getCallback() {
    return callback;
  }

  /**
   * sets callback url.
  * @param callback callback url
  */
  public void setCallback(String callback) {
    this.callback = callback;
  }

  /**
  * helper method to get scopes for oauth.
  * @return  array of scopes
  */
  public String[] getScope() {
    return scope;
  }

  /**
  * sets scopes for oauth.
  * @param scope array of scopes
  */
  public void setScope(String[] scope) {
    this.scope = scope;
  }

  /**
  * helper method to get state.
  * @return  state
  */
  public String getState() {
    return state;
  }

  /**
  * sets state.
  * @param state state
  */
  public void setState(String state) {
    this.state = state;
  }

  /**
  * helper method to get accessToken for oauth.
  * @return accessToken
  */
  public String getAccessToken() {
    return accessToken;
  }

  /**
  * helper method to get client secret.
  * @return  client secret
  */
  public String getClientSecret() {
    return clientSecret;
  }

  /**
   * helper method to set client secret.
   * @param clientSecret client secret
   */
  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  /**
  * sets accessToken.
  * @param accessToken       accessToken
  */
  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }
}

package com.hapramp.steemconnect4j;

import com.sun.jndi.toolkit.url.UrlUtil;
import java.io.UnsupportedEncodingException;

public class SteemConnect {
  private SteemConnectOptions steemConnectOptions;

	/**
	 * constructor for SteemConnect.
	 * @param options  options for configuration
	 */
  public SteemConnect(SteemConnectOptions options) {
    this.steemConnectOptions = options;
  }

	/**
	 * build login url.
	 * @return	login url for oauth.
	 */
  public String getLoginUrl() {
    StringBuilder loginUrlBuilder = new StringBuilder();
    try {
      loginUrlBuilder
      .append(steemConnectOptions.getBaseUrl())
      .append("/oauth2/authorize?client_id=").append(steemConnectOptions.getApp())
      .append("&redirect_uri=").append(UrlUtil.encode(steemConnectOptions.getCallback(), "UTF-8"));
      if (steemConnectOptions.getScope().length > 0) {
        loginUrlBuilder.append("&scope=").append(StringUtils.getCommaSeparatedString(steemConnectOptions.getScope()));
      }
      if (steemConnectOptions.getState() != null) {
        loginUrlBuilder.append("&state=").append(UrlUtil.encode(steemConnectOptions.getState(), "UTF-8"));
      }
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return loginUrlBuilder.toString();
  }

	/**
	 * performs a send request.
	 * @param route					route for operation
	 * @param method					http method type
	 * @param body					body of request
	 * @param steemConnectCallback	callback for response
	 */
  public void send(String route, String method, String body, SteemConnectCallback steemConnectCallback) {
    String url = steemConnectOptions.getBaseUrl() + "/api/" + route;
    NetworkUtils.request(url, method, steemConnectOptions.getAccessToken(), body, steemConnectCallback);
  }

	/**
	 * broadcasts a operation.
	 * @param operations				operation to be broadcasted
	 * @param steemConnectCallback	callback for response
	 */
  public void broadcast(String operations, SteemConnectCallback steemConnectCallback) {
    this.send(Route.BROADCAST, HttpMethod.POST, operations, steemConnectCallback);
  }

	/**
	 * performs vote.
	 * @param voter					voter
	 * @param author					author of the content
	 * @param permlink				permlink of the content
	 * @param weight					weight of vote
	 * @param steemConnectCallback	callback for the response
	 */
  public void vote(String voter, String author, String permlink, String weight, SteemConnectCallback steemConnectCallback) {
  }

  public static class InstanceBuilder {
    SteemConnectOptions steemConnectOptions;

	  /**
	   * Constructor for InstanceBuilder
	   */
    public InstanceBuilder() {
      steemConnectOptions = new SteemConnectOptions();
    }

	  /**
	   * sets base url to steemconnectOptions object.
	   * @param baseUrl	base url
	   * @return  		instance of InstanceBuilder
	   */
    public InstanceBuilder setBaseUrl(String baseUrl) {
      steemConnectOptions.setBaseUrl(baseUrl);
      return this;
    }

	  /**
	   * sets app name to steemconnectOptions object.
	   * @param app 	app name
	   * @return		instance of InstanceBuilder
	   */
    public InstanceBuilder setApp(String app) {
      steemConnectOptions.setApp(app);
      return this;
    }

	  /**
	   * sets scopes to steemconnectOptions object.
	   * @param scopes	scopes of oauth
	   * @return			instance of InstanceBuilder
	   */
    public InstanceBuilder setScope(String[] scopes) {
      steemConnectOptions.setScope(scopes);
      return this;
    }

	  /**
	   * sets callback to steemconnectOptions object.
	   * @param url	callback url of oauth
	   * @return		instance of InstanceBuilder
	   */
    public InstanceBuilder setCallbackUrl(String url) {
      steemConnectOptions.setCallback(url);
      return this;
    }

	  /**
	   * instantiate SteemConnect class object.
	   * @return SteemConnect object
	   */
    public SteemConnect build() {
      return new SteemConnect(steemConnectOptions);
    }
  }
}

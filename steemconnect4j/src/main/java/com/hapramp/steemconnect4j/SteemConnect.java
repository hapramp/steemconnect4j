package com.hapramp.steemconnect4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

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
  * @return  login url for oauth.
  */
  public String getLoginUrl(boolean wantCode) throws SteemConnectException {
    StringBuilder loginUrlBuilder = new StringBuilder();
    try {
      loginUrlBuilder
      .append(steemConnectOptions.getBaseUrl())
          .append("/oauth2/authorize?client_id=").append(steemConnectOptions.getApp())
          .append("&redirect_uri=")
          .append(URLEncoder.encode(steemConnectOptions.getCallback(), "UTF-8"));
      if (wantCode) {
        loginUrlBuilder
          .append("&response_type=code");
      }
      if (steemConnectOptions.getScope().length > 0) {
        loginUrlBuilder
            .append("&scope=")
            .append(StringUtils.getCommaSeparatedString(
            steemConnectOptions.getScope()));
      }
      if (steemConnectOptions.getState() != null) {
        loginUrlBuilder.append("&state=")
          .append(URLEncoder.encode(steemConnectOptions.getState(), "UTF-8"));
      }
    } catch (UnsupportedEncodingException e) {
      throw new SteemConnectException("LoginUrl", "EncodingException", e.toString());
    }
    return loginUrlBuilder.toString();
  }

  /**
  * method to request refresh token given a code of user.
  * @param code                      code of logged in user.
  * @param steemConnectCallback      callback for response.
  * @throws SteemConnectException    exception for unexpected errors.
  */
  public void requestRefreshToken(String code, SteemConnectCallback steemConnectCallback)
      throws SteemConnectException {
    //check client secret
    if (steemConnectOptions.getClientSecret() != null
        && steemConnectOptions.getClientSecret().length() > 0) {
      String body = StringUtils.getCommanSeparatedObjectString(
          RpcJsonUtil.getKeyValuePair("response_type", "\"refresh\""),
          RpcJsonUtil.getKeyValuePair("code", "\""
          + code
          + "\""),
          RpcJsonUtil.getKeyValuePair("client_id", "\""
          + steemConnectOptions.getApp()
          + "\""),
          RpcJsonUtil.getKeyValuePair("client_secret", "\""
          + steemConnectOptions.getClientSecret()
          + "\""),
          RpcJsonUtil.getKeyValuePair("scope", "\""
          + StringUtils.getCommaSeparatedString(steemConnectOptions.getScope())
          + "\"")
      );
      send(Route.REFRESH_TOKEN, HttpMethod.POST, body, steemConnectCallback);
    } else {
      throw new SteemConnectException("RefreshToken",
        "ArguementException", "You must set a valid client secret to SteemConnectOptions!");
    }
  }

  /**
  * method to get access token from refresh token.
  * @param refreshToken             refreshToken of logged in user.
  * @param steemConnectCallback     callback for response
  * @throws SteemConnectException   exception for unexpected errors.
  */
  public void requestAccessToken(String refreshToken, SteemConnectCallback steemConnectCallback)
      throws SteemConnectException {
    //check refresh token
    if (refreshToken != null && refreshToken.length() > 0) {
      //check client secret
      if (steemConnectOptions.getClientSecret() != null
          && steemConnectOptions.getClientSecret().length() > 0) {
        //perform request.
        String body = StringUtils.getCommanSeparatedObjectString(
            RpcJsonUtil.getKeyValuePair("refresh_token", "\""
            + refreshToken
            + "\""),
            RpcJsonUtil.getKeyValuePair("client_id", "\""
            + steemConnectOptions.getApp()
            + "\""),
            RpcJsonUtil.getKeyValuePair("client_secret", "\""
            + steemConnectOptions.getClientSecret()
            + "\""),
            RpcJsonUtil.getKeyValuePair("scope", "\""
            + StringUtils.getCommaSeparatedString(steemConnectOptions.getScope())
            + "\"")
        );
        send(Route.REFRESH_TOKEN, HttpMethod.POST, body, steemConnectCallback);
      } else {
        //throw exception
        throw new SteemConnectException("AccessToken",
          "ArguementException", "You must set a valid client secret to SteemConnectOptions!");
      }
    } else {
      throw new SteemConnectException("AccessToken",
        "ArguementException", "You must pass a valid refreshToken!");
    }
  }

  /**
  * performs a send request.
  * @param route                   route for operation
  * @param method                  http method type
  * @param body                    body of request
  * @param steemConnectCallback    callback for response
  */
  private void send(String route,  String method, String body, SteemConnectCallback
      steemConnectCallback) {
    String url = steemConnectOptions.getBaseUrl() + "/api/" + route;
    NetworkUtils.request(url, method, steemConnectOptions.getAccessToken(), body,
        steemConnectCallback);
  }

  /**
  * broadcasts a operation.
  * @param operations             operation to be broadcasted
  * @param steemConnectCallback   callback for response
  */
  private void broadcast(String operations, SteemConnectCallback steemConnectCallback) {
    this.send(Route.BROADCAST, HttpMethod.POST, operations, steemConnectCallback);
  }

  /**
  * fetches user information of logged in user.
  * @param steemConnectCallback  callback for `me` endpoint
  */
  public void me(SteemConnectCallback steemConnectCallback) {
    this.send(Route.ME, HttpMethod.POST, StringUtils.getCommanSeparatedObjectString(""),
        steemConnectCallback);
  }

  /**
  * performs vote.
  * @param voter                   voter
  * @param author                  author of the content
  * @param permlink                permlink of the content
  * @param weight                  weight of vote
  * @param steemConnectCallback    callback for the response
  */
  public void vote(String voter, String author, String permlink, String weight,
                   SteemConnectCallback steemConnectCallback) {
    String params = StringUtils.getCommanSeparatedObjectString(
    		    RpcJsonUtil.getKeyValuePair("voter", "\"" + voter + "\""),
		    RpcJsonUtil.getKeyValuePair("author", "\"" + author + "\""),
		    RpcJsonUtil.getKeyValuePair("permlink", "\"" + permlink + "\""),
		    RpcJsonUtil.getKeyValuePair("weight", weight));
    String operation = StringUtils.getOperationsString(
    		StringUtils.getCommanSeparatedArrayString(
    				StringUtils.getCommanSeparatedArrayString("\"vote\"", params)));
    this.broadcast(operation, steemConnectCallback);
  }

  /**
  * create comment on a post.
  * @param parentAuthor           author of post
  * @param parentPermlink         parent permlink of post
  * @param author                 author of comment
  * @param permlink               permlink of post
  * @param title                  title of comment
  * @param body                   body of comment
  * @param jsonMetaData           jsonmetadata of comment
  * @param steemConnectCallback   callback for response
  */
  public void comment(String parentAuthor, String parentPermlink, String author, String permlink,
      String title, String body, String jsonMetaData, SteemConnectCallback steemConnectCallback) {
    String params = StringUtils.getCommanSeparatedObjectString(
        RpcJsonUtil.getKeyValuePair("parent_author", "\"" + parentAuthor + "\""),
        RpcJsonUtil.getKeyValuePair("parent_permlink", "\"" + parentPermlink + "\""),
        RpcJsonUtil.getKeyValuePair("author", "\"" + author + "\""),
        RpcJsonUtil.getKeyValuePair("permlink", "\"" + permlink + "\""),
        RpcJsonUtil.getKeyValuePair("title", "\"" + title + "\""),
        RpcJsonUtil.getKeyValuePair("body", "\"" + body + "\""),
        RpcJsonUtil.getKeyValuePair("json_metadata", "\"" + jsonMetaData + "\"")
    );
    String operations = StringUtils.getOperationsString(
    		StringUtils.getCommanSeparatedArrayString(
    				StringUtils.getCommanSeparatedArrayString("\"comment\"", params)));
    this.broadcast(operations, steemConnectCallback);
  }

  /**
  * reblogs any content.
  * @param account                account of author
  * @param author                 author of reblog
  * @param permlink               permlink of post
  * @param steemConnectCallback   callback for response
  */
  public void reblog(String account, String author, String permlink,
      SteemConnectCallback steemConnectCallback) {
    String params = StringUtils.getCommanSeparatedObjectString(
        RpcJsonUtil.getKeyValuePair("required_auths", "[]"),
        RpcJsonUtil.getKeyValuePair("required_posting_auths", "[\"" + account + "\"]"),
        RpcJsonUtil.getKeyValuePair("id", "\"follow\""),
        RpcJsonUtil.getKeyValuePair("json",  StringUtils.getCommanSeparatedArrayString("\"reblog\"",
            StringUtils.getCommanSeparatedObjectString(account, author, permlink)))
    );

    String operation = StringUtils.getOperationsString(
    		StringUtils.getCommanSeparatedArrayString(
    		StringUtils.getCommanSeparatedArrayString("\"custom_json\"", params)));
    this.broadcast(operation, steemConnectCallback);
  }

  /**
  * follow a user.
  * @param follower               follower
  * @param following              user to be followed
  * @param steemConnectCallback   callback for response
  */
  public void follow(String follower, String following, SteemConnectCallback steemConnectCallback) {
    String params = StringUtils.getCommanSeparatedObjectString(
        RpcJsonUtil.getKeyValuePair("required_auths", "[]"),
        RpcJsonUtil.getKeyValuePair("required_posting_auths", "\"" + follower + "\"]"),
        RpcJsonUtil.getKeyValuePair("id", "\"follow\""),
        RpcJsonUtil.getKeyValuePair("json",  StringUtils.getCommanSeparatedArrayString("\"follow\"",
                     StringUtils.getCommanSeparatedObjectString(
                     		RpcJsonUtil.getKeyValuePair("follower", "\"" + follower + "\""),
		                 RpcJsonUtil.getKeyValuePair("following", "\"" + following + "\""),
                     RpcJsonUtil.getKeyValuePair("what", "[\"blog\"]"))))
    );

    String operations = StringUtils.getOperationsString(
    		StringUtils.getCommanSeparatedArrayString(
    		StringUtils.getCommanSeparatedArrayString("\"custom_json\"", params)));
    this.broadcast(operations, steemConnectCallback);
  }

  /**
  * unfollow a user.
  * @param unfollower               unfollower
  * @param unfollowing              user to be unfollowed
  * @param steemConnectCallback   callback for response
  */
  public void unfollow(String unfollower, String unfollowing,
                       SteemConnectCallback steemConnectCallback) {
    String params = StringUtils.getCommanSeparatedObjectString(
        RpcJsonUtil.getKeyValuePair("required_auths", "[]"),
        RpcJsonUtil.getKeyValuePair("required_posting_auths", "[\"" + unfollower + "\"]"),
        RpcJsonUtil.getKeyValuePair("id", "\"follow\""),
        RpcJsonUtil.getKeyValuePair("json",  StringUtils.getCommanSeparatedArrayString("\"follow\"",
            StringUtils.getCommanSeparatedObjectString(
                RpcJsonUtil.getKeyValuePair("follower", "\"" + unfollower + "\""),
                RpcJsonUtil.getKeyValuePair("following", "\"" + unfollowing + "\""),
                RpcJsonUtil.getKeyValuePair("what", "[]"))))
    );

    String operations = StringUtils.getOperationsString(
    		StringUtils.getCommanSeparatedArrayString(
    		StringUtils.getCommanSeparatedArrayString("\"custom_json\"", params)));
    this.broadcast(operations, steemConnectCallback);
  }

  /**
  * ignore a user.
  * @param follower               follower
  * @param following              user to be followed
  * @param steemConnectCallback   callback for response
  */
  public void ignore(String follower, String following, SteemConnectCallback steemConnectCallback) {
    String params = StringUtils.getCommanSeparatedObjectString(
        RpcJsonUtil.getKeyValuePair("required_auths", "[]"),
        RpcJsonUtil.getKeyValuePair("required_posting_auths", "[\"" + follower + "\"]"),
        RpcJsonUtil.getKeyValuePair("id", "\"follow\""),
        RpcJsonUtil.getKeyValuePair("json",
		        StringUtils.getCommanSeparatedArrayString("\"follow\"",
        StringUtils.getCommanSeparatedObjectString(follower, following,
            RpcJsonUtil.getKeyValuePair("what", "[\"ignore\"]"))))
    );

    String operations = StringUtils.getOperationsString(
    		StringUtils.getCommanSeparatedArrayString(
    		StringUtils.getCommanSeparatedArrayString("\"custom_json\"", params)));
    this.broadcast(operations, steemConnectCallback);
  }

  /**
  * claim reward of user.
  * @param account                 account name
  * @param rewardSteem             rewarded Steem
  * @param rewardSbd               rewarded SBD
  * @param rewardVests             rewarded vests
  * @param steemConnectCallback    callback for response
  */
  public void claimRewardBalance(String account, String rewardSteem, String rewardSbd,
                                 String rewardVests, SteemConnectCallback steemConnectCallback) {
    String params = StringUtils.getCommanSeparatedObjectString(
    		RpcJsonUtil.getKeyValuePair("account", "\"" + account + "\""),
        RpcJsonUtil.getKeyValuePair("reward_steem", "\"" + rewardSteem + "\""),
        RpcJsonUtil.getKeyValuePair("reward_sbd",  "\"" + rewardSbd + "\""),
        RpcJsonUtil.getKeyValuePair("reward_vests",  "\"" + rewardVests + "\""));

    String operation = StringUtils.getOperationsString(
    		StringUtils.getCommanSeparatedArrayString(
    		StringUtils.getCommanSeparatedArrayString("\"claim_reward_balance\"", params)));
    this.broadcast(operation, steemConnectCallback);
  }

  /**
  * removes accessToken.
  * @param steemConnectCallback  callback for response.
  */
  public void revokeToken(SteemConnectCallback steemConnectCallback) {
    String body = RpcJsonUtil.getObjectString("token", this.steemConnectOptions.getAccessToken());
    this.send(Route.REVOKE_TOKEN, HttpMethod.POST, body, steemConnectCallback);
    this.removeAccessToken();
  }

  /**
  * updates user metadata.
  * @param metadata                 updated metadata of user.
  * @param steemConnectCallback     callback for response
  */
  public void updateUserMetaData(String metadata, SteemConnectCallback steemConnectCallback) {
    this.send(Route.ME,  HttpMethod.PUT, RpcJsonUtil.getObjectString("user_metadata", metadata),
        steemConnectCallback);
  }

  /**
  * format signing url.
  * @param name            name of signing method
  * @param params          map of query parameters
  * @param redirectUri     redirect url after sigining
  * @return                signing url
  */
  public String sign(String name, Map<String, String> params, String redirectUri) {
    String url = String.format("%s/sign/%s?%s%s", this.steemConnectOptions.getBaseUrl(),
            name, StringUtils.getQueryParamsFromMap(params),
            redirectUri != null && redirectUri.length() > 0 ? "redirect_uri=" + redirectUri : "");
    return url;
  }

  public static class InstanceBuilder {
    SteemConnectOptions steemConnectOptions;

    /**
    * Constructor for InstanceBuilder.
    */
    public InstanceBuilder() {
      steemConnectOptions = new SteemConnectOptions();
    }

    /**
    * sets base url to steemconnectOptions object.
    * @param baseUrl  base url
    * @return       instance of InstanceBuilder
    */
    public InstanceBuilder setBaseUrl(String baseUrl) {
      steemConnectOptions.setBaseUrl(baseUrl);
      return this;
    }

    /**
    * sets app name to steemconnectOptions object.
    * @param app   app name
    * @return        instance of InstanceBuilder
    */
    public InstanceBuilder setApp(String app) {
      steemConnectOptions.setApp(app);
      return this;
    }

    /**
    * set state of app.
    * @param state state of the app
    * @return      instance of InstanceBuilder
    */
    public InstanceBuilder setState(String state) {
      steemConnectOptions.setState(state);
      return this;
    }

    /**
    * set access Token for user.
    * @param acessToken  accessToken of current user
    * @return            instance of InstanceBuilder
    */
    public InstanceBuilder setAcessToken(String acessToken) {
      steemConnectOptions.setAccessToken(acessToken);
      return this;
    }

    /**
    * sets scopes to steemconnectOptions object.
    * @param scopes  scopes of oauth
    * @return        instance of InstanceBuilder
    */
    public InstanceBuilder setScope(String[] scopes) {
      steemConnectOptions.setScope(scopes);
      return this;
    }

    /**
    * sets callback to steemconnectOptions object.
    * @param url    callback url of oauth
    * @return       instance of InstanceBuilder
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

  /**
  * removes accessToken.
  */
  private void removeAccessToken() {
    this.steemConnectOptions.setAccessToken(null);
  }

}

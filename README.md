# SteemConnect4j  [![](https://jitpack.io/v/hapramp/steemconnect4j.svg)](https://jitpack.io/#hapramp/steemconnect4j)  [![Build Status](https://travis-ci.org/hapramp/steemconnect4j.svg?branch=master)](https://travis-ci.org/hapramp/steemconnect4j) [![codecov](https://codecov.io/gh/hapramp/steemconnect4j/branch/master/graph/badge.svg)](https://codecov.io/gh/hapramp/steemconnect4j)

A SteemConnect SDK for Java.

## Example Implementation

If you are wondering how to use this SDK in Android projects, we've made an example for you :smile:.  
Check out https://github.com/hapramp/SteemConnectSampleApp.

## Using steemconnect4j with `gradle`

1. Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
   repositories {
    maven { url 'https://jitpack.io' }
   }
}
```

2. Add the dependency

```groovy
dependencies {
  implementation 'com.github.hapramp:steemconnect4j:v1.3.1'
}
```

---

## Initialize `SteemConnect`

```java

//init Builder
SteemConnect.InstanceBuilder instanceBuilder = new SteemConnect.InstanceBuilder();

//set parameters
instanceBuilder
            .setApp("<your-app-name>")
            .setBaseUrl("base-url")
            .setAccessToken("accessToken")
            .setState("state")
            .setCallbackUrl("<callback-url>")
            .setScope(new String[]{"comment", "vote"});  //scopes of your application
            
// build the SteemConnect object.			
SteemConnect steemConnect = instanceBuilder.build();

```

**Parameters**  
 - **app:** This is the name of the app that was registered in the SteemConnect V2 dashboard.
 - **callbackUrl:** This is the URL that users will be redirected to after interacting with SC2. It must be listed in the "Redirect URI(s)" list in the app settings EXACTLY the same as it is specified here.
 - **accessToken:** If you have an oauth2 access token for this user already you can specify it here, otherwise you can leave it and set it later.
 - **scope:** This is a list of operations the app will be able to access on the user's account. For a complete list of scopes see: https://github.com/steemit/steemconnect/wiki/OAuth-2#scopes.
 - **state:** Data that will be passed to the callbackURL for your app after the user has logged in.

Alternatively, you can build `SteemConnect` object using `SteemConnectOptions`.

```java
//configure `SteemConnectOptions`
SteemConnectOptions steemConnectOptions = new SteemConnectOptions();
steemConnectOptions.setAccessToken("accessToken");
steemConnectOptions.setState("state");
steemConnectOptions.setCallback("");
steemConnectOptions.setScope(new String[]{"scope 1", "scope 2"});
steemConnectOptions.setBaseUrl("base-url");
steemConnectOptions.setApp("app");

//initialize `SteemConnect`
SteemConnect steemConnect = new SteemConnect(steemConnectOptions);

```

---

## Getting Login Url

1. Build `SteemConnect` object with atleast
`app`, `callbackUrl`, `baseUrl` and `scope`.
These fields are mandatory.

2. Get login url
```java
String loginUrl = steemConnect.getLoginUrl(true);  //steemConnect is an object created before.
```

> Prototype of method: `public void getLoginUrl(boolean wantCode);`

There are 2 type of loginUrl, featuring different results in redirected url.
 - Redirection with `access_token`
 - Redirection with `code` (recommended, more secure)

Using the first one which provides direct access_token is less secure since we will need to persist in local database or temprory file for future use.

Using `code` we can request `access_token` whenever we need.

So let us see the differences,

```java
String loginUrl = steemConnect.getLoginUrl(false);  //wantCode: false
```
**Login URL:** `https://v2.steemconnect.com/oauth2/authorize?client_id=&redirect_uri=%2F&scope=`

**Redirected URL:** `https://<callback-url>?access_token=eyJhb....0o&expires_in=604800&username=<some-user-name>`

**Feature**
 - loginUrl do not contains `response_type=code` query parameter.
 - redirected URL has `access_token` as query parameter

#### And 


```java
String loginUrl = steemConnect.getLoginUrl(true);  //wantCode: true
```
**Login URL:** `https://v2.steemconnect.com/oauth2/authorize?client_id=&response_type=code&redirect_uri=%2F&scope=`

**Redirected URL:** `https://<callback-url>?code=eyJhb....0o`

**Feature**
 - loginUrl contains `response_type=code` query parameter.
 - redirected URL has `code` as query parameter instead of `access_token`


3. Now use this `loginUrl` to present authentication page to the user.

---
## Flow of getting Access Token

##### `Code` --> `RefreshToken` --> `Access Token`

---
## What is Refresh Token ?

Refresh tokens given from https://v2.steemconnect.com/api/oauth2/authorize when scope contains offline. The purpose is for headless operations to have access. Refresh tokens are not the same as access tokens. Access tokens allow access, but have an expiration of about 7 days. Refresh tokens do not expire, but instead of access they can be used to continuously retrieve access tokens.

---
## Getting Refresh Token from `code`

```
//prepare steemconnect options
SteemConnectOptions steemConnectOptions = new SteemConnectOptions();
steemConnectOptions.setClientSecret("your_client_secret");
steemConnectOptions.setScope(new String[]{"vote","comment","offline"}); //`offline` is must.
steemConnectOptions.setApp("your-app");
//build steemconnect
SteemConnect steemConnect = new SteemConnect(steemConnectOptions);
//call method
try {
  steemConnect.requestRefreshToken("code-of-user", new SteemConnectCallback() {
  @Override
  public void onResponse(String response) {
          
  }
  
  @Override
  public void onError(SteemConnectException e) {
  
  }
  });
 }
  catch (SteemConnectException e) {
   e.printStackTrace();
}
```

On Successfull Request, it responds with :
```
{
  access_token: "ey09..02",
  username: <user-name>,
  refresh_token: "some token"
}

```

---
## Getting AccessToken from `Refresh Token`

```
SteemConnectOptions steemConnectOptions = new SteemConnectOptions();
steemConnectOptions.setClientSecret("your_client_secret");
steemConnectOptions.setScope(new String[]{"vote","comment","offline"}); //offline is must
steemConnectOptions.setApp("your-app");
SteemConnect steemConnect = new SteemConnect(steemConnectOptions);
try {
 steemConnect.requestAccessToken("refresh-token-of-user", new SteemConnectCallback() {
   @Override
   public void onResponse(String response) {

   }
   
   @Override
   public void onError(SteemConnectException e) {

   }
  });
 }
 catch (SteemConnectException e) {
   e.printStackTrace();
}
```

---

## Setting AccessToken

```java
SteemConnectOptions steemConnectOptions = new SteemConnectOptions();
steemConnectOptions.setAccessToken(token);
...
...
steemConnect = new SteemConnect(steemConnectOptions);
```

---

## Getting User Profile

```java
steemConnect.me(steemConnectCallback);
// steemConnectCallback is type of interface,Hence you need to provide its implementation.
// response will be sent via callback methods.
```

On successfull call, JSON will be returned in
```java
void onResponse(String response);  //method of steemConnectCallback
```

---

## Vote

```java
steemConnect.vote("voter", "author", "permlink", "weight", new SteemConnectCallback() {
   @Override
   public void onResponse(String response) {
		    
   }

   @Override
   public void onError(SteemConnectException e) {

   }
});
```

---

## Comment

```java
steemConnect.comment("parentAuthor",
		    "parentPermlink",
		    "author",
		    "permlink",
		    "title",
		    "body",
		    "jsonmetadata",
		    new SteemConnectCallback() {
   @Override
   public void onResponse(String response) {

   }

   @Override
   public void onError(SteemConnectException e) {

});
```
---

## Delete

```java
steemConnect.delete("author",
		    "permlink",
		    new SteemConnectCallback() {
   @Override
   public void onResponse(String response) {

   }

   @Override
   public void onError(SteemConnectException e) {

});
```

---

## Generate hot signing link

The `sign()` method creates a URL to which your app can redirect the user to perform a signed transaction on the blockchain such as a transfer or delegation:

```java
Map<String, String> map = new HashMap<>();
map.put("to", "bxute");
map.put("amount", "1.0000 STEEM");
map.put("memo", "done!");
String uri = steemConnect.sign("transfer", map, "<redirectUri>");

// output : https://steemconnect.com/sign/transfer?to=bxute&amount=1.000%20STEEM&memo=done!&redirect_uri=<redirectUri>
```

---

## Logout

The `revokeToken()` method will log the current user out of your application by revoking the access token provided to your app for that user:

```java
steemConnect.revokeToken(new SteemConnectCallback() {
   @Override
   public void onResponse(String response) {
		    
   }

   @Override
   public void onError(SteemConnectException e) {

   }
});
```

---

## Reblog

```java
steemConnect.reblog("accout", "author", "permlink", new SteemConnectCallback() {
   @Override
   public void onResponse(String response) {
   
   }

   @Override
   public void onError(SteemConnectException e) {

   }
});
```

---

## Follow 

```java
steemConnect.follow("follower", "following", new SteemConnectCallback() {
   @Override
   public void onResponse(String response) {

   }

   @Override
   public void onError(SteemConnectException e) {
   
   }
});
```

---

## Unfollow

```java
steemConnect.unfollow("unfollower", "unfollowing", new SteemConnectCallback() {
   @Override
   public void onResponse(String response) {

   }

   @Override
   public void onError(SteemConnectException e) {
   
   }
});
```

---

## Ignore

```java
steemConnect.ignore("follower", "following", new SteemConnectCallback() {
   @Override
   public void onResponse(String response) {

   }

   @Override
   public void onError(SteemConnectException e) {
   
   }
});
```

---

## Claim Reward Balance

```java
steemConnect.claimRewardBalance("account",
      "rewardSteem",
      "rewardSBD",
      "rewardVests", new SteemConnectCallback() {
	  @Override
	  public void onResponse(String response) {
		    
	  }

	   @Override
	   public void onError(SteemConnectException e) {

	  }
});
```

---

## Update User Metadata

```java
steemConnect.updateUserMetaData("metadata", new SteemConnectCallback() {
   @Override
   public void onResponse(String response) {
		    
   }
   @Override
   public void onError(SteemConnectException e) {

   }
});
```

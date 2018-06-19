## SteemConnect4j  [![](https://jitpack.io/v/hapramp/steemconnect4j.svg)](https://jitpack.io/#hapramp/steemconnect4j)  [![Build Status](https://travis-ci.org/hapramp/steemconnect4j.svg?branch=master)](https://travis-ci.org/hapramp/steemconnect4j) [![codecov](https://codecov.io/gh/hapramp/steemconnect4j/branch/master/graph/badge.svg)](https://codecov.io/gh/hapramp/steemconnect4j)
A SteemConnect SDK for Java.
### How to use steemconnect4j ?
**For Gradle**
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
  implementation 'com.github.hapramp:steemconnect4j:v0.2'
}
```


#### How to initialize `SteemConnect`

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
            .setScope(new String[]{"comment", "vote"}); //scopes of your application
            
// build the SteemConnect object.			
SteemConnect steemConnect = instanceBuilder.build();

```

Parameters
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
steemConnectOptions.setScope(new String[]{"scope 1","scope 2"});
steemConnectOptions.setBaseUrl("base-url");
steemConnectOptions.setApp("app");

//initialize `SteemConnect`
SteemConnect steemConnect = new SteemConnect(steemConnectOptions);

```

### Getting Login Url
1. Build `SteemConnect` object with atleast
`app` , `callbackUrl` , `baseUrl` and `scope`.
These fields are mandatory.

2. Get login url
```java
String loginUrl = steemConnect.getLoginUrl(); //steemConnect is a object created before.
```

3. Now use this `loginUrl` to present authentication page to the user.

4. If user provides correct credentials, steemconnect redirects to callback url with 
`access_token` , `expires` and `username` as query parameters.
Something like:

`https://<callback-url>?access_token=eyJhb....0o&expires_in=604800&username=<some-user-name>`

5. Store these values temporarily in your app since you will need them for further steps.

### Setting AccessToken

```java
SteemConnectOptions steemConnectOptions = new SteemConnectOptions();
steemConnectOptions.setAccessToken(token);
...
...
steemConnect = new SteemConnect(steemConnectOptions);
```

### Getting User Profile

```java
steemConnect.me(steemConnectCallback);
// steemConnectCallback is type of interface,Hence you need to provide its implementation.
// response will be sent via callback methods.
```

On successfull call, JSON will be returned in
```java
void onResponse(String response); //method of steemConnectCallback
```



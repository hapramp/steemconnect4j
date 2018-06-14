package com.hapramp.steemconnect4j;

public class SteemConnectOptions {
	public static final String BASE_URL = "https://steemconnect.com";
	private String baseUrl;
	private String app;
	private String state;
	private String callback;
	private String[] scope;
	private String accessToken;

	public SteemConnectOptions() {
		this.baseUrl = BASE_URL;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public String[] getScope() {
		return scope;
	}

	public void setScope(String[] scope) {
		this.scope = scope;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}

package com.hapramp.steemconnect4j;

import com.sun.jndi.toolkit.url.UrlUtil;
import java.io.UnsupportedEncodingException;

public class SteemConnect {
	private SteemConnectOptions steemConnectOptions;

	public SteemConnect(SteemConnectOptions options) {
		this.steemConnectOptions = options;
	}

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

	public void send(String route, String method, String body, SteemConnectCallback steemConnectCallback) {
		String url = steemConnectOptions.getBaseUrl() + "/api/" + route;
		NetworkUtils.request(url, method, steemConnectOptions.getAccessToken(), body, steemConnectCallback);
	}

	public void broadcast(String operations, SteemConnectCallback steemConnectCallback) {
		this.send(Route.BROADCAST, HttpMethod.POST, operations, steemConnectCallback);
	}

	public void me(SteemConnectCallback steemConnectCallback) {
		this.send(Route.ME, HttpMethod.POST, "{}", steemConnectCallback);
	}

	public void vote(String voter, String author, String permlink, String weight, SteemConnectCallback steemConnectCallback) {

	}

	public static class InstanceBuilder {
		SteemConnectOptions steemConnectOptions;

		public InstanceBuilder() {
			steemConnectOptions = new SteemConnectOptions();
		}

		public InstanceBuilder setBaseUrl(String baseUrl) {
			steemConnectOptions.setBaseUrl(baseUrl);
			return this;
		}

		public InstanceBuilder setApp(String app) {
			steemConnectOptions.setApp(app);
			return this;
		}

		public InstanceBuilder setScope(String[] scopes) {
			steemConnectOptions.setScope(scopes);
			return this;
		}

		public InstanceBuilder setCallbackUrl(String url) {
			steemConnectOptions.setCallback(url);
			return this;
		}

		public SteemConnect build() {
			return new SteemConnect(steemConnectOptions);
		}
	}
}

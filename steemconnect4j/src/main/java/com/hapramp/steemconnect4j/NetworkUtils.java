package com.hapramp.steemconnect4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {
  private static final String USER_AGENT = "Mozilla/5.0";

	/**
	 * performs a http request.
	 * @param url					url to be requested on
	 * @param method     				http method type
	 * @param token					access toke of steemconnect
	 * @param body					body of the request
	 * @param steemConnectCallback	callback for response(error/success)
	 */
  public static void request(String url , String method,String token,String body, SteemConnectCallback steemConnectCallback) {
    URL obj = null;
    try {
        obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(method);
        con.setRequestProperty("Accept","application/json, text/plain, */*");
        con.setRequestProperty("Content-Type","application/json");
        con.setRequestProperty("Authorization",token);
        con.setRequestProperty("User-Agent", USER_AGENT);
        OutputStream os = con.getOutputStream();
        os.write(body.getBytes("UTF-8"));
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
          BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
          String inputLine;
          StringBuffer response = new StringBuffer();
          while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
          }
          in.close();
          if(steemConnectCallback!=null){
            steemConnectCallback.onResponse(response.toString());
          }
          System.out.println(response.toString());
        } else {
        if(steemConnectCallback!=null){
          steemConnectCallback.onError(new SteemConnectException("SteemConnectException","Connection Failed","Response code "+responseCode));
        }
        }
      }
    catch (IOException e) {
        e.printStackTrace();
        if(steemConnectCallback!=null){
          steemConnectCallback.onError(new SteemConnectException("SteemConnectException","IOException",e.toString()));
        }
    }
  }
}

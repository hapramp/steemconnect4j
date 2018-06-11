package com.hapramp.steemconnect4j;

public class SteemConnectException extends Exception{
    public String name;
    public String error;
    public String description;

    public SteemConnectException(String s, String name) {
        super(s);
        this.name = name;
    }

    @Override
    public String toString() {
        return "SteemConnectException{" +
                "name='" + name + '\'' +
                ", error='" + error + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

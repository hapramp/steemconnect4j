package com.hapramp.steemconnect4j.tests;

import com.hapramp.steemconnect4j.SteemConnect;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ExampleTest {
    @Test
    public void testMe() {
        assertTrue(SteemConnect.getLoginUrl().length() > 0);
    }

    @Test
    public void testSteemConnect() {

    }
}

package com.hapramp.steemconnect4j.tests;

import static org.junit.Assert.assertNotNull;

import com.hapramp.steemconnect4j.SteemConnect;
import org.junit.Test;

public class InstanceBuilderTest {

  @Test
  public void testInstanceBuilder() {
    SteemConnect.InstanceBuilder instanceBuilder = new SteemConnect.InstanceBuilder();
    instanceBuilder.setScope(new String[]{"vote", "comment"});
    instanceBuilder.setCallbackUrl("url");
    instanceBuilder.setApp("app");
    instanceBuilder.setBaseUrl("baseUrl");
    instanceBuilder.setAcessToken("act");
    instanceBuilder.setState("offline");
    SteemConnect steemConnect = instanceBuilder.build();
    assertNotNull(steemConnect);
  }
}

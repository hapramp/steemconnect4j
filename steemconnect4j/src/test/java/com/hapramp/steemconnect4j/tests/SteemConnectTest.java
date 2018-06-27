package com.hapramp.steemconnect4j.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.hapramp.steemconnect4j.SteemConnect;
import com.hapramp.steemconnect4j.SteemConnectCallback;
import com.hapramp.steemconnect4j.SteemConnectException;
import com.hapramp.steemconnect4j.SteemConnectOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.junit.Before;
import org.junit.Test;

public class SteemConnectTest {
  SteemConnect steemConnect;

  /**
   * prepares required object before test runs.
   */
  @Before
  public void prepare() {
    SteemConnectOptions steemConnectOptions = new SteemConnectOptions();
    steemConnectOptions.setClientSecret("cs");
    steemConnectOptions.setApp("app");
    steemConnectOptions.setScope(new String[]{"vote", "comment"});
    steemConnectOptions.setAccessToken("at");
    steemConnectOptions.setCallback("cb");
    steemConnectOptions.setState("offline");
    steemConnect = new SteemConnect(steemConnectOptions);
  }

  @Test
  public void testGetLoginUrl() {
    String expectedWithoutCode  = "https://v2.steemconnect.com/oauth2/authorize?"
        + "client_id=app&redirect_uri=cb&scope=vote,comment&state=offline";
    String expectedWithCode  = "https://v2.steemconnect.com/oauth2/authorize?"
        + "client_id=app&redirect_uri=cb&response_type=code&scope=vote,comment&state=offline";
    try {
      String resWithCode = steemConnect.getLoginUrl(true);
      String resWithoutCode = steemConnect.getLoginUrl(false);
      System.out.print(resWithoutCode);
      assertEquals(expectedWithCode, resWithCode);
      assertEquals(expectedWithoutCode, resWithoutCode);
    } catch (SteemConnectException e) {
      //fail the test
      assertTrue(false);
    }
  }

  @Test
  public void testRequestRefreshTokenException() {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    SteemConnectOptions steemConnectOptions = new SteemConnectOptions();
    steemConnectOptions.setApp("app");
    steemConnectOptions.setScope(new String[]{"vote", "comment"});
    steemConnect = new SteemConnect(steemConnectOptions);
    try {
      steemConnect.requestRefreshToken("code", new SteemConnectCallback() {
        @Override
        public void onResponse(String response) {
          assertFalse(response.length() > 0);
          countDownLatch.countDown();
        }

        @Override
        public void onError(SteemConnectException e) {
          assertFalse(e.toString().length() > 0);
          countDownLatch.countDown();
        }
      });
      countDownLatch.await();
    } catch (SteemConnectException e) {
      System.out.print(e.toString());
      assertTrue(e.toString().length() > 0);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testRequestRefreshTokenWithoutException() {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    SteemConnectOptions steemConnectOptions = new SteemConnectOptions();
    steemConnectOptions.setApp("app");
    steemConnectOptions.setClientSecret("cs");
    steemConnectOptions.setScope(new String[]{"vote", "comment"});
    steemConnect = new SteemConnect(steemConnectOptions);
    try {
      steemConnect.requestRefreshToken("code", new SteemConnectCallback() {
        @Override
        public void onResponse(String response) {
          System.out.print(response);
          assertTrue(response.length() > 0);
          countDownLatch.countDown();
        }

        @Override
        public void onError(SteemConnectException e) {
          System.out.print(e.toString());
          assertTrue(e.toString().length() > 0);
          countDownLatch.countDown();
        }
      });
      countDownLatch.await();
    } catch (SteemConnectException e) {
      System.out.print(e.toString());
      assertFalse(e.toString().length() > 0);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testRequestAccessToken() {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    SteemConnectOptions steemConnectOptions = new SteemConnectOptions();
    steemConnectOptions.setApp("app");
    steemConnectOptions.setClientSecret("cs");
    steemConnectOptions.setScope(new String[]{"vote", "comment"});
    steemConnect = new SteemConnect(steemConnectOptions);
    try {
      steemConnect.requestAccessToken("token", new SteemConnectCallback() {
        @Override
        public void onResponse(String response) {
          System.out.print(response);
          assertTrue(response.length() > 0);
          countDownLatch.countDown();
        }

        @Override
        public void onError(SteemConnectException e) {
          System.out.print(e.toString());
          assertTrue(e.toString().length() > 0);
          countDownLatch.countDown();
        }
      });
      countDownLatch.await();
    } catch (SteemConnectException e) {
      System.out.print(e.toString());
      assertFalse(e.toString().length() > 0);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testRequestAccessTokenExceptionDueToNullRefreshToken() {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    SteemConnectOptions steemConnectOptions = new SteemConnectOptions();
    steemConnectOptions.setApp("app");
    steemConnectOptions.setClientSecret("cs");
    steemConnectOptions.setScope(new String[]{"vote", "comment"});
    steemConnect = new SteemConnect(steemConnectOptions);
    try {
      steemConnect.requestAccessToken(null, new SteemConnectCallback() {
        @Override
        public void onResponse(String response) {
          System.out.print(response);
          assertFalse(response.length() > 0);
          countDownLatch.countDown();
        }

        @Override
        public void onError(SteemConnectException e) {
          System.out.print(e.toString());
          assertFalse(e.toString().length() > 0);
          countDownLatch.countDown();
        }
      });
      countDownLatch.await();
    } catch (SteemConnectException e) {
      System.out.print(e.toString());
      assertTrue(e.toString().length() > 0);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testRequestAccessTokenExceptionDueToNullClientSecret() {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    SteemConnectOptions steemConnectOptions = new SteemConnectOptions();
    steemConnectOptions.setApp("app");
    steemConnectOptions.setClientSecret(null);
    steemConnectOptions.setScope(new String[]{"vote", "comment"});
    steemConnect = new SteemConnect(steemConnectOptions);
    try {
      steemConnect.requestAccessToken("refresh_token", new SteemConnectCallback() {
        @Override
        public void onResponse(String response) {
          System.out.print(response);
          assertFalse(response.length() > 0);
          countDownLatch.countDown();
        }

        @Override
        public void onError(SteemConnectException e) {
          System.out.print(e.toString());
          assertFalse(e.toString().length() > 0);
          countDownLatch.countDown();
        }
      });
      countDownLatch.await();
    } catch (SteemConnectException e) {
      System.out.print(e.toString());
      assertTrue(e.toString().length() > 0);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testMe() {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    try {
      steemConnect.me(new SteemConnectCallback() {
        @Override
        public void onResponse(String response) {
          assertTrue(response.length() > 0);
          countDownLatch.countDown();
        }

        @Override
        public void onError(SteemConnectException e) {
          assertTrue(e.toString().length() > 0);
          countDownLatch.countDown();
        }
      });
      countDownLatch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testVote() {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    try {
      steemConnect.vote("voter", "author", "permlink", "1020", new SteemConnectCallback() {
        @Override
        public void onResponse(String response) {
          assertTrue(response.length() > 0);
          countDownLatch.countDown();
        }

        @Override
        public void onError(SteemConnectException e) {
          assertTrue(e.toString().length() > 0);
          countDownLatch.countDown();
        }
      });
      countDownLatch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testComment() {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    try {
      steemConnect.comment("parent_author", "parent_permlink",
          "title", "body", "json",
          "author", "permlink", new SteemConnectCallback() {
            @Override
            public void onResponse(String response) {
              assertTrue(response.length() > 0);
              countDownLatch.countDown();
            }

            @Override
            public void onError(SteemConnectException e) {
              assertTrue(e.toString().length() > 0);
              countDownLatch.countDown();
            }
          });
      countDownLatch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testReblog() {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    try {
      steemConnect.reblog("account", "author", "permlink",
          new SteemConnectCallback() {
          @Override
          public void onResponse(String response) {
            assertTrue(response.length() > 0);
            countDownLatch.countDown();
          }

          @Override
          public void onError(SteemConnectException e) {
            assertTrue(e.toString().length() > 0);
            countDownLatch.countDown();
          }
        });
      countDownLatch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testFollow() {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    try {
      steemConnect.follow("follower", "following",  new SteemConnectCallback() {
        @Override
        public void onResponse(String response) {
          assertTrue(response.length() > 0);
          countDownLatch.countDown();
        }

        @Override
        public void onError(SteemConnectException e) {
          assertTrue(e.toString().length() > 0);
          countDownLatch.countDown();
        }
      });
      countDownLatch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testUnFollow() {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    try {
      steemConnect.unfollow("follower", "unfollowing",  new SteemConnectCallback() {
        @Override
        public void onResponse(String response) {
          assertTrue(response.length() > 0);
          countDownLatch.countDown();
        }

        @Override
        public void onError(SteemConnectException e) {
          assertTrue(e.toString().length() > 0);
          countDownLatch.countDown();
        }
      });
      countDownLatch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testIgnore() {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    try {
      steemConnect.ignore("follower", "following", new SteemConnectCallback() {
        @Override
        public void onResponse(String response) {
          assertTrue(response.length() > 0);
          countDownLatch.countDown();
        }

        @Override
        public void onError(SteemConnectException e) {
          assertTrue(e.toString().length() > 0);
          countDownLatch.countDown();
        }
      });
      countDownLatch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testClaimRewardBalance() {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    try {
      steemConnect.claimRewardBalance("account", "10STEEM", "5 SBD",
          "1020", new SteemConnectCallback() {
            @Override
            public void onResponse(String response) {
              assertTrue(response.length() > 0);
              countDownLatch.countDown();
            }

            @Override
            public void onError(SteemConnectException e) {
              assertTrue(e.toString().length() > 0);
              countDownLatch.countDown();
            }
          });
      countDownLatch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testRevokeToken() {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    try {
      steemConnect.revokeToken(new SteemConnectCallback() {
        @Override
        public void onResponse(String response) {
          assertTrue(response.length() > 0);
          countDownLatch.countDown();
        }

        @Override
        public void onError(SteemConnectException e) {
          assertTrue(e.toString().length() > 0);
          countDownLatch.countDown();
        }
      });
      countDownLatch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testUpdateMetadata() {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    try {
      steemConnect.updateUserMetaData("metadata",  new SteemConnectCallback() {
        @Override
        public void onResponse(String response) {
          assertTrue(response.length() > 0);
          countDownLatch.countDown();
        }

        @Override
        public void onError(SteemConnectException e) {
          assertTrue(e.toString().length() > 0);
          countDownLatch.countDown();
        }
      });
      countDownLatch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testSignMethod() {
    Map<String, String> map = new HashMap<>();
    map.put("to", "bxute");
    map.put("amount", "1.000 STEEM");
    map.put("memo", "done!");
    String real = steemConnect.sign("transfer", map, "<redirectUri>");
    String signExpected = "https://v2.steemconnect.com/sign/transfer?amount=1.000 STEEM&memo=done!&to=bxute&<redirectUri>";
    assertEquals(signExpected, real);
  }
}

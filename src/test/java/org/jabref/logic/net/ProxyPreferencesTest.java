package org.jabref.logic.net;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;

import org.jabref.logic.net.ProxyPreferences;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ProxyPreferencesTest {

  final ProxyPreferences proxy = new ProxyPreferences(true, "hostname", "port", true, "username", "password");
  final ProxyPreferences otherProxy = new ProxyPreferences(false, "hostname", "port", true, "username", "password");
  final ProxyPreferences nullProxy = new ProxyPreferences(null, null, null, null, null, null);

  String falseObject = "falseObject";

  /**
  *Tests the getters in ProxyPreferences
  *@author Anton Hedkrans
  **/
  @Test
  public void getTest() {
    assertEquals(proxy.isUseProxy(), true);
    assertEquals(proxy.getHostname(), "hostname");
    assertEquals(proxy.getPort(), "port");
    assertEquals(proxy.isUseAuthentication(), true);
    assertEquals(proxy.getUsername(), "username");
    assertEquals(proxy.getPassword(), "password");
  }
  /**
  *Test the equals function in ProxyPreferences
  *@author Anton Hedkrans
  **/
  @Test
  public void testEquals() {
    assertTrue(proxy.equals(proxy));
    assertFalse(proxy.equals(falseObject));
    assertFalse(proxy.equals(otherProxy));
    assertFalse(proxy.equals(nullProxy));
  }
}

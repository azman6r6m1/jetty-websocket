package dev.ayam.sample.websocket;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class Common {
  private static final Path KEYSTORE_PATH = Path.of("server.p12");
  private static final char[] KEYSTORE_PASSWORD = "abcd#1234".toCharArray();
  private static final String PROTOCOL = "TLSv1.2";

  private Common() {
  }

  public static SSLContext createSSLContext() throws Exception {
    KeyStore keyStore = KeyStore.getInstance("PKCS12");
    try (InputStream is = Files.newInputStream(KEYSTORE_PATH)) {
      keyStore.load(is, KEYSTORE_PASSWORD);
    }
    
    KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    keyManagerFactory.init(keyStore, KEYSTORE_PASSWORD);
    KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();

    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init(keyStore);
    TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

    SSLContext sslContext = SSLContext.getInstance(PROTOCOL);
    sslContext.init(keyManagers, trustManagers, null);

    return sslContext;
  }

}

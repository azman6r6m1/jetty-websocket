package dev.ayam.sample.websocket;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.jsr356.ClientContainer;

public class JettyClient {
  public static void main(String[] args) throws Exception {
    URI uri = URI.create("wss://wss.ayam.dev:22222/test");

    SslContextFactory ssl = new SslContextFactory.Client();
    ssl.setProtocol("TLSv1.2");
    ssl.setKeyStorePath("server.p12");
    ssl.setKeyStorePassword("abcd#1234");

    HttpClient httpClient = new HttpClient(ssl);
    ClientContainer clientContainer = new ClientContainer(httpClient);
    clientContainer.getClient().addManaged(httpClient);
    clientContainer.start();

    WebSocketContainer cpntainer = clientContainer;

    ClientEndpointConfig clientEndpointConfig = ClientEndpointConfig.Builder.create().configurator(new ClientEndpointConfig.Configurator() {

      @Override
      public void beforeRequest(Map<String, List<String>> headers) {
        headers.put("Origin", Collections.singletonList("https://wss.ayam.dev:22222"));
        super.beforeRequest(headers);
      }
    }).build();

    Session session = cpntainer.connectToServer(JettyClientEndpoint.class, clientEndpointConfig, uri);

    try (Scanner scanner = new Scanner(System.in)) {
      while (true) {
        String message = scanner.nextLine();

        if ("!q".equalsIgnoreCase(message))
          break;

        session.getBasicRemote().sendText(message);

      }
    }

    session.close();

  }
}

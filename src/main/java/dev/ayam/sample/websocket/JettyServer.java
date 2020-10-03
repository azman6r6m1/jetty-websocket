package dev.ayam.sample.websocket;

import javax.websocket.server.ServerEndpointConfig;

import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

public class JettyServer {
  public static void main(String[] args) throws Exception {

    SslContextFactory ssl = new SslContextFactory.Server();
    ssl.setProtocol("TLSv1.2");
    ssl.setKeyStorePath("server.p12");
    ssl.setKeyStorePassword("abcd#1234");

    HttpConfiguration https = new HttpConfiguration();
    https.addCustomizer(new SecureRequestCustomizer());

    Server server = new Server();
    ServerConnector connector = new ServerConnector(server, ssl, new HttpConnectionFactory(https));
    connector.setHost("wss.ayam.dev");
    connector.setPort(22222);
    server.addConnector(connector);

    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");

    WebSocketServerContainerInitializer.configure(context, (servletContext, serverContainer) -> {
      serverContainer.addEndpoint(ServerEndpointConfig.Builder.create(JettyServerEndpoint.class, "/test").build());
    });
    server.setHandler(context);

    server.start();
    server.dump(System.err);
    server.join();
  }
}

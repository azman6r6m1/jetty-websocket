package dev.ayam.sample.websocket;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/test")
public class JettyServerEndpoint extends Endpoint implements MessageHandler.Whole<String> {

  private Session session;

  @OnOpen
  @Override
  public void onOpen(Session session, EndpointConfig config) {
    this.session = session;
    this.session.addMessageHandler(this);
    try {
      System.out.println("Client OK");
      session.getBasicRemote().sendText("Hi...");
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  @OnMessage
  @Override
  public void onMessage(String message) {
    try {
      System.out.println("Client: " + message);
      session.getBasicRemote().sendText(String.join("~", new StringBuilder(message).reverse().toString().split("")));
    }
    catch (IOException e) {
      e.printStackTrace();
    }

  }

  @OnClose
  @Override
  public void onClose(Session session, CloseReason closeReason) {
    this.session = null;
  }

  @OnError
  @Override
  public void onError(Session session, Throwable thr) {
    thr.printStackTrace();
  }
}

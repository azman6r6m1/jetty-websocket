package dev.ayam.sample.websocket;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ClientEndpoint
public class JettyClientEndpoint extends Endpoint implements MessageHandler.Whole<String> {

  private Session session;

  @OnOpen
  @Override
  public void onOpen(Session session, EndpointConfig config) {
    this.session = session;
    this.session.addMessageHandler(this);
    System.out.println("Server OK.");
  }

  @OnMessage
  @Override
  public void onMessage(String message) {
    System.out.println("Server: " + message);
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

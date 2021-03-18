package org.java_websocket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.java_websocket.util.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractWebSocket extends WebSocketAdapter {
  private static final Logger log = LoggerFactory.getLogger(AbstractWebSocket.class);
  
  private ScheduledFuture connectionLostCheckerFuture;
  
  private ScheduledExecutorService connectionLostCheckerService;
  
  private long connectionLostTimeout = TimeUnit.SECONDS.toNanos(60L);
  
  private boolean reuseAddr;
  
  private final Object syncConnectionLost = new Object();
  
  private boolean tcpNoDelay;
  
  private boolean websocketRunning = false;
  
  private void cancelConnectionLostTimer() {
    ScheduledExecutorService scheduledExecutorService = this.connectionLostCheckerService;
    if (scheduledExecutorService != null) {
      scheduledExecutorService.shutdownNow();
      this.connectionLostCheckerService = null;
    } 
    ScheduledFuture scheduledFuture = this.connectionLostCheckerFuture;
    if (scheduledFuture != null) {
      scheduledFuture.cancel(false);
      this.connectionLostCheckerFuture = null;
    } 
  }
  
  private void executeConnectionLostDetection(WebSocket paramWebSocket, long paramLong) {
    if (!(paramWebSocket instanceof WebSocketImpl))
      return; 
    paramWebSocket = paramWebSocket;
    if (paramWebSocket.getLastPong() < paramLong) {
      log.trace("Closing connection due to no pong received: {}", paramWebSocket);
      paramWebSocket.closeConnection(1006, "The connection was closed because the other endpoint did not respond with a pong in time. For more information check: https://github.com/TooTallNate/Java-WebSocket/wiki/Lost-connection-detection");
      return;
    } 
    if (paramWebSocket.isOpen()) {
      paramWebSocket.sendPing();
      return;
    } 
    log.trace("Trying to ping a non open connection: {}", paramWebSocket);
  }
  
  private void restartConnectionLostTimer() {
    cancelConnectionLostTimer();
    this.connectionLostCheckerService = Executors.newSingleThreadScheduledExecutor((ThreadFactory)new NamedThreadFactory("connectionLostChecker"));
    Runnable runnable = new Runnable() {
        private ArrayList<WebSocket> connections = new ArrayList<WebSocket>();
        
        public void run() {
          this.connections.clear();
          try {
            this.connections.addAll(AbstractWebSocket.this.getConnections());
            long l = (long)(System.nanoTime() - AbstractWebSocket.this.connectionLostTimeout * 1.5D);
            for (WebSocket webSocket : this.connections)
              AbstractWebSocket.this.executeConnectionLostDetection(webSocket, l); 
          } catch (Exception exception) {}
          this.connections.clear();
        }
      };
    ScheduledExecutorService scheduledExecutorService = this.connectionLostCheckerService;
    long l = this.connectionLostTimeout;
    this.connectionLostCheckerFuture = scheduledExecutorService.scheduleAtFixedRate(runnable, l, l, TimeUnit.NANOSECONDS);
  }
  
  public int getConnectionLostTimeout() {
    synchronized (this.syncConnectionLost) {
      return (int)TimeUnit.NANOSECONDS.toSeconds(this.connectionLostTimeout);
    } 
  }
  
  protected abstract Collection<WebSocket> getConnections();
  
  public boolean isReuseAddr() {
    return this.reuseAddr;
  }
  
  public boolean isTcpNoDelay() {
    return this.tcpNoDelay;
  }
  
  public void setConnectionLostTimeout(int paramInt) {
    synchronized (this.syncConnectionLost) {
      long l = TimeUnit.SECONDS.toNanos(paramInt);
      this.connectionLostTimeout = l;
      if (l <= 0L) {
        log.trace("Connection lost timer stopped");
        cancelConnectionLostTimer();
        return;
      } 
      if (this.websocketRunning) {
        log.trace("Connection lost timer restarted");
        try {
          for (WebSocket webSocket : new ArrayList(getConnections())) {
            if (webSocket instanceof WebSocketImpl)
              ((WebSocketImpl)webSocket).updateLastPong(); 
          } 
        } catch (Exception exception) {
          log.error("Exception during connection lost restart", exception);
        } 
        restartConnectionLostTimer();
      } 
      return;
    } 
  }
  
  public void setReuseAddr(boolean paramBoolean) {
    this.reuseAddr = paramBoolean;
  }
  
  public void setTcpNoDelay(boolean paramBoolean) {
    this.tcpNoDelay = paramBoolean;
  }
  
  protected void startConnectionLostTimer() {
    synchronized (this.syncConnectionLost) {
      if (this.connectionLostTimeout <= 0L) {
        log.trace("Connection lost timer deactivated");
        return;
      } 
      log.trace("Connection lost timer started");
      this.websocketRunning = true;
      restartConnectionLostTimer();
      return;
    } 
  }
  
  protected void stopConnectionLostTimer() {
    synchronized (this.syncConnectionLost) {
      if (this.connectionLostCheckerService != null || this.connectionLostCheckerFuture != null) {
        this.websocketRunning = false;
        log.trace("Connection lost timer stopped");
        cancelConnectionLostTimer();
      } 
      return;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\AbstractWebSocket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
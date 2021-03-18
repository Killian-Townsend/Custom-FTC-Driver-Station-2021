package org.java_websocket.server;

import java.io.IOException;
import java.nio.channels.ByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import org.java_websocket.SSLSocketChannel2;

public class CustomSSLWebSocketServerFactory extends DefaultSSLWebSocketServerFactory {
  private final String[] enabledCiphersuites;
  
  private final String[] enabledProtocols;
  
  public CustomSSLWebSocketServerFactory(SSLContext paramSSLContext, ExecutorService paramExecutorService, String[] paramArrayOfString1, String[] paramArrayOfString2) {
    super(paramSSLContext, paramExecutorService);
    this.enabledProtocols = paramArrayOfString1;
    this.enabledCiphersuites = paramArrayOfString2;
  }
  
  public CustomSSLWebSocketServerFactory(SSLContext paramSSLContext, String[] paramArrayOfString1, String[] paramArrayOfString2) {
    this(paramSSLContext, Executors.newSingleThreadScheduledExecutor(), paramArrayOfString1, paramArrayOfString2);
  }
  
  public ByteChannel wrapChannel(SocketChannel paramSocketChannel, SelectionKey paramSelectionKey) throws IOException {
    SSLEngine sSLEngine = this.sslcontext.createSSLEngine();
    String[] arrayOfString = this.enabledProtocols;
    if (arrayOfString != null)
      sSLEngine.setEnabledProtocols(arrayOfString); 
    arrayOfString = this.enabledCiphersuites;
    if (arrayOfString != null)
      sSLEngine.setEnabledCipherSuites(arrayOfString); 
    sSLEngine.setUseClientMode(false);
    return (ByteChannel)new SSLSocketChannel2(paramSocketChannel, sSLEngine, this.exec, paramSelectionKey);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\server\CustomSSLWebSocketServerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
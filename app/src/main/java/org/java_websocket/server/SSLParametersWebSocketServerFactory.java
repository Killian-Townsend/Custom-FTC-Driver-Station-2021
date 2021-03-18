package org.java_websocket.server;

import java.io.IOException;
import java.nio.channels.ByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import org.java_websocket.SSLSocketChannel2;

public class SSLParametersWebSocketServerFactory extends DefaultSSLWebSocketServerFactory {
  private final SSLParameters sslParameters;
  
  public SSLParametersWebSocketServerFactory(SSLContext paramSSLContext, ExecutorService paramExecutorService, SSLParameters paramSSLParameters) {
    super(paramSSLContext, paramExecutorService);
    if (paramSSLParameters != null) {
      this.sslParameters = paramSSLParameters;
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  public SSLParametersWebSocketServerFactory(SSLContext paramSSLContext, SSLParameters paramSSLParameters) {
    this(paramSSLContext, Executors.newSingleThreadScheduledExecutor(), paramSSLParameters);
  }
  
  public ByteChannel wrapChannel(SocketChannel paramSocketChannel, SelectionKey paramSelectionKey) throws IOException {
    SSLEngine sSLEngine = this.sslcontext.createSSLEngine();
    sSLEngine.setUseClientMode(false);
    sSLEngine.setSSLParameters(this.sslParameters);
    return (ByteChannel)new SSLSocketChannel2(paramSocketChannel, sSLEngine, this.exec, paramSelectionKey);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\server\SSLParametersWebSocketServerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
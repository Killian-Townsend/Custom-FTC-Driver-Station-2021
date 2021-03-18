package org.firstinspires.ftc.robotserver.internal.webserver;

import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.WebHandlerManager;
import com.qualcomm.robotcore.util.WebServer;
import com.qualcomm.robotcore.wifi.NetworkConnection;
import com.qualcomm.robotcore.wifi.NetworkConnectionFactory;
import com.qualcomm.robotcore.wifi.NetworkType;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant;
import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.firstinspires.ftc.robotcore.internal.webserver.RobotControllerWebInfo;
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.WebSocketManager;
import org.firstinspires.ftc.robotserver.internal.webserver.tempfile.UploadedTempFileManagerFactory;
import org.firstinspires.ftc.robotserver.internal.webserver.websockets.FtcWebSocketServer;
import org.firstinspires.ftc.robotserver.internal.webserver.websockets.tootallnate.TooTallWebSocketServer;

public class CoreRobotWebServer implements WebServer {
  private static final boolean DBG = false;
  
  private static final int DEFAULT_PORT = 8080;
  
  public static final String TAG = CoreRobotWebServer.class.getSimpleName();
  
  private InetAddress connectionOwnerAddress;
  
  private final Object lock;
  
  private final NanoHTTPD nanoHttpd;
  
  private NetworkConnection networkConnection;
  
  private String networkName;
  
  private final NetworkType networkType;
  
  private int port;
  
  private boolean serverIsAlive;
  
  private final Object startStopLock;
  
  private long timeServerStartedMillis;
  
  private final RobotWebHandlerManager webHandlerManager;
  
  private final FtcWebSocketServer webSocketServer;
  
  public CoreRobotWebServer(int paramInt, NetworkType paramNetworkType) {
    this.port = paramInt;
    this.networkType = paramNetworkType;
    NanoHTTPD nanoHTTPD = createNanoHttpd(paramInt);
    this.nanoHttpd = nanoHTTPD;
    nanoHTTPD.setTempFileManagerFactory((NanoHTTPD.TempFileManagerFactory)new UploadedTempFileManagerFactory());
    this.webSocketServer = (FtcWebSocketServer)new TooTallWebSocketServer(new InetSocketAddress(paramInt + 1));
    this.webHandlerManager = new RobotWebHandlerManager(this);
    this.lock = new Object();
    this.startStopLock = new Object();
  }
  
  public CoreRobotWebServer(NetworkType paramNetworkType) {
    this(8080, paramNetworkType);
  }
  
  private NanoHTTPD createNanoHttpd(int paramInt) {
    RobotLog.vv(TAG, "creating NanoHTTPD(%d)", new Object[] { Integer.valueOf(paramInt) });
    return new NanoHTTPD(paramInt) {
        public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession param1IHTTPSession) {
          NanoHTTPD.Method method = param1IHTTPSession.getMethod();
          return (NanoHTTPD.Method.GET == method || NanoHTTPD.Method.PUT == method || NanoHTTPD.Method.POST == method) ? CoreRobotWebServer.this.webHandlerManager.serve(param1IHTTPSession) : newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.NOT_FOUND, "text/plain", "");
        }
      };
  }
  
  private static void logError(String paramString) {
    RobotLog.ee(TAG, paramString);
  }
  
  public static void logSession(NanoHTTPD.IHTTPSession paramIHTTPSession, boolean paramBoolean) {
    String str = paramIHTTPSession.getUri();
    if (!paramBoolean && str.equals("/ping"))
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("\n   ");
    stringBuilder.append(String.format("uri='%s'", new Object[] { paramIHTTPSession.getUri() }));
    stringBuilder.append("\n   ");
    stringBuilder.append(String.format("method='%s'", new Object[] { paramIHTTPSession.getMethod() }));
    if (paramIHTTPSession.getQueryParameterString() != null && paramIHTTPSession.getQueryParameterString().length() > 0) {
      stringBuilder.append("\n   ");
      stringBuilder.append(String.format("query='%s'", new Object[] { paramIHTTPSession.getQueryParameterString() }));
    } 
    for (Map.Entry entry : paramIHTTPSession.getParameters().entrySet()) {
      stringBuilder.append("\n   ");
      stringBuilder.append(String.format("param('%s')=[", new Object[] { entry.getKey() }));
      Iterator<String> iterator = ((List)entry.getValue()).iterator();
      for (boolean bool = true; iterator.hasNext(); bool = false) {
        String str1 = iterator.next();
        if (!bool)
          stringBuilder.append(", "); 
        stringBuilder.append(String.format("'%s'", new Object[] { str1 }));
      } 
      stringBuilder.append("]");
    } 
    for (Map.Entry entry : paramIHTTPSession.getHeaders().entrySet()) {
      stringBuilder.append("\n   ");
      stringBuilder.append(String.format("header %s=%s", new Object[] { entry.getKey(), entry.getValue() }));
    } 
    RobotLog.dd(TAG, "session(0x%08x)=%s", new Object[] { Integer.valueOf(paramIHTTPSession.hashCode()), stringBuilder.toString() });
  }
  
  public RobotControllerWebInfo getConnectionInformation() {
    // Byte code:
    //   0: aload_0
    //   1: getfield lock : Ljava/lang/Object;
    //   4: astore #4
    //   6: aload #4
    //   8: monitorenter
    //   9: ldc '(unavailable)'
    //   11: astore_3
    //   12: aload_3
    //   13: astore_2
    //   14: aload_0
    //   15: getfield connectionOwnerAddress : Ljava/net/InetAddress;
    //   18: ifnull -> 81
    //   21: aload_0
    //   22: getfield nanoHttpd : Lfi/iki/elonen/NanoHTTPD;
    //   25: invokevirtual getListeningPort : ()I
    //   28: istore_1
    //   29: aload_3
    //   30: astore_2
    //   31: iload_1
    //   32: iconst_m1
    //   33: if_icmpeq -> 81
    //   36: new java/lang/StringBuilder
    //   39: dup
    //   40: invokespecial <init> : ()V
    //   43: astore_2
    //   44: aload_2
    //   45: ldc 'http://'
    //   47: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   50: pop
    //   51: aload_2
    //   52: aload_0
    //   53: getfield connectionOwnerAddress : Ljava/net/InetAddress;
    //   56: invokevirtual getHostAddress : ()Ljava/lang/String;
    //   59: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   62: pop
    //   63: aload_2
    //   64: ldc ':'
    //   66: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   69: pop
    //   70: aload_2
    //   71: iload_1
    //   72: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   75: pop
    //   76: aload_2
    //   77: invokevirtual toString : ()Ljava/lang/String;
    //   80: astore_2
    //   81: aload_0
    //   82: getfield networkName : Ljava/lang/String;
    //   85: astore #5
    //   87: aload_0
    //   88: getfield networkConnection : Lcom/qualcomm/robotcore/wifi/NetworkConnection;
    //   91: ifnull -> 105
    //   94: aload_0
    //   95: getfield networkConnection : Lcom/qualcomm/robotcore/wifi/NetworkConnection;
    //   98: invokevirtual getPassphrase : ()Ljava/lang/String;
    //   101: astore_3
    //   102: goto -> 115
    //   105: invokestatic getDefContext : ()Landroid/content/Context;
    //   108: getstatic org/firstinspires/ftc/robotcore/internal/webserver/R$string.manage_page_no_network : I
    //   111: invokevirtual getString : (I)Ljava/lang/String;
    //   114: astore_3
    //   115: new org/firstinspires/ftc/robotcore/internal/webserver/RobotControllerWebInfo
    //   118: dup
    //   119: aload #5
    //   121: aload_3
    //   122: aload_2
    //   123: aload_0
    //   124: getfield serverIsAlive : Z
    //   127: aload_0
    //   128: getfield timeServerStartedMillis : J
    //   131: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZJ)V
    //   134: astore_2
    //   135: aload #4
    //   137: monitorexit
    //   138: aload_2
    //   139: areturn
    //   140: astore_2
    //   141: aload #4
    //   143: monitorexit
    //   144: aload_2
    //   145: athrow
    // Exception table:
    //   from	to	target	type
    //   14	29	140	finally
    //   36	81	140	finally
    //   81	102	140	finally
    //   105	115	140	finally
    //   115	138	140	finally
    //   141	144	140	finally
  }
  
  public WebHandlerManager getWebHandlerManager() {
    return this.webHandlerManager;
  }
  
  public WebSocketManager getWebSocketManager() {
    return this.webSocketServer.getWebSocketManager();
  }
  
  public void start() {
    Object object = this.startStopLock;
    /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
    try {
      if (wasStarted()) {
        RobotLog.vv(TAG, "Asked an already running WebServer to start");
        /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
        return;
      } 
      RobotLog.vv(TAG, "starting port=%d", new Object[] { Integer.valueOf(this.port) });
      this.nanoHttpd.start();
      this.webSocketServer.start();
      synchronized (this.lock) {
        this.timeServerStartedMillis = System.currentTimeMillis();
        NetworkConnection networkConnection = NetworkConnectionFactory.getNetworkConnection(this.networkType, null);
        this.networkConnection = networkConnection;
        if (networkConnection == null) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Cannot start Network Connection of type: ");
          stringBuilder.append(this.networkType);
          logError(stringBuilder.toString());
        } 
        this.networkConnection.enable();
        if (this.networkConnection instanceof WifiDirectAssistant) {
          this.networkName = ((WifiDirectAssistant)this.networkConnection).getGroupNetworkName();
        } else {
          this.networkName = null;
        } 
        this.connectionOwnerAddress = this.networkConnection.getConnectionOwnerAddress();
        this.serverIsAlive = this.nanoHttpd.isAlive();
        RobotLog.vv(TAG, "started port=%d", new Object[] { Integer.valueOf(this.port) });
      } 
    } catch (IOException iOException) {
      logError(iOException.getMessage());
    } finally {
      Exception exception;
    } 
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
  }
  
  public void stop() {
    Object object = this.startStopLock;
    /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
    try {
      this.webSocketServer.stop(5000);
    } catch (InterruptedException interruptedException) {
      RobotLog.ee(TAG, interruptedException, "Error stopping WebSocket server");
    } finally {
      Exception exception;
    } 
    RobotLog.vv(TAG, "stopping port=%d", new Object[] { Integer.valueOf(this.port) });
    this.nanoHttpd.stop();
    synchronized (this.lock) {
      if (this.networkConnection != null) {
        this.networkConnection.disable();
        this.networkConnection = null;
      } 
      RobotLog.vv(TAG, "stopped port=%d", new Object[] { Integer.valueOf(this.port) });
      /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
      return;
    } 
  }
  
  public boolean wasStarted() {
    synchronized (this.startStopLock) {
      return this.nanoHttpd.wasStarted();
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\webserver\CoreRobotWebServer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
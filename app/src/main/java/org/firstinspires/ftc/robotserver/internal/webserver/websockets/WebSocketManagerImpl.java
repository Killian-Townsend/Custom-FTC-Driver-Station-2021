package org.firstinspires.ftc.robotserver.internal.webserver.websockets;

import com.qualcomm.robotcore.util.RobotLog;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.FtcWebSocket;
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.FtcWebSocketMessage;
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.WebSocketManager;
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.WebSocketNamespaceHandler;

public final class WebSocketManagerImpl implements WebSocketManager {
  private static final String NOTIFY_CURRENT_TIME_MESSAGE_TYPE = "notifyCurrentTime";
  
  private static final String REQUEST_CURRENT_TIME_MESSAGE_TYPE = "requestCurrentTime";
  
  private static final String SUBSCRIBE_TO_NAMESPACE_MESSAGE_TYPE = "subscribeToNamespace";
  
  private static final String TAG = "WebSocketManager";
  
  private static final String UNSUBSCRIBE_FROM_NAMESPACE_MESSAGE_TYPE = "unsubscribeFromNamespace";
  
  private final ConcurrentMap<String, WebSocketNamespaceHandler> namespaceHandlerMap = new ConcurrentHashMap<String, WebSocketNamespaceHandler>();
  
  private final ConcurrentMap<String, Set<FtcWebSocket>> namespaceSubscribersMap = new ConcurrentHashMap<String, Set<FtcWebSocket>>();
  
  public WebSocketManagerImpl() {
    AppUtil.getInstance().setWebSocketManager(this);
  }
  
  private void handleNotifyCurrentTimeMessage(FtcWebSocketMessage paramFtcWebSocketMessage) {
    TimePayload timePayload = TimePayload.fromJson(paramFtcWebSocketMessage.getPayload());
    AppUtil.getInstance().setWallClockIfCurrentlyInsane(timePayload.timeMs, timePayload.timezone);
  }
  
  private void handleSystemNamespace(FtcWebSocketMessage paramFtcWebSocketMessage, FtcWebSocketImpl paramFtcWebSocketImpl) {
    if (paramFtcWebSocketMessage.getType().equals("subscribeToNamespace")) {
      subscribeWebSocketToNamespace(paramFtcWebSocketImpl, paramFtcWebSocketMessage.getPayload());
      return;
    } 
    if (paramFtcWebSocketMessage.getType().equals("unsubscribeFromNamespace")) {
      unsubscribeWebSocketFromNamespace(paramFtcWebSocketImpl, paramFtcWebSocketMessage.getPayload());
      return;
    } 
    if (paramFtcWebSocketMessage.getType().equals("notifyCurrentTime"))
      handleNotifyCurrentTimeMessage(paramFtcWebSocketMessage); 
  }
  
  private void internalRegisterNamespaceHandler(WebSocketNamespaceHandler paramWebSocketNamespaceHandler) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokevirtual getNamespace : ()Ljava/lang/String;
    //   6: astore_2
    //   7: aload_2
    //   8: invokestatic isGoodString : (Ljava/lang/String;)Z
    //   11: ifeq -> 138
    //   14: aload_2
    //   15: ldc 'system'
    //   17: invokevirtual equals : (Ljava/lang/Object;)Z
    //   20: ifne -> 128
    //   23: aload_0
    //   24: getfield namespaceHandlerMap : Ljava/util/concurrent/ConcurrentMap;
    //   27: aload_2
    //   28: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   33: ifeq -> 95
    //   36: aload_0
    //   37: getfield namespaceHandlerMap : Ljava/util/concurrent/ConcurrentMap;
    //   40: aload_2
    //   41: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   46: instanceof org/firstinspires/ftc/robotserver/internal/webserver/websockets/WebSocketManagerImpl$BroadcastOnlyNamespaceHandler
    //   49: ifeq -> 55
    //   52: goto -> 95
    //   55: new java/lang/StringBuilder
    //   58: dup
    //   59: invokespecial <init> : ()V
    //   62: astore_1
    //   63: aload_1
    //   64: ldc 'namespace '
    //   66: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   69: pop
    //   70: aload_1
    //   71: aload_2
    //   72: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   75: pop
    //   76: aload_1
    //   77: ldc ' is already registered with a handler'
    //   79: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   82: pop
    //   83: new java/lang/IllegalArgumentException
    //   86: dup
    //   87: aload_1
    //   88: invokevirtual toString : ()Ljava/lang/String;
    //   91: invokespecial <init> : (Ljava/lang/String;)V
    //   94: athrow
    //   95: aload_0
    //   96: getfield namespaceHandlerMap : Ljava/util/concurrent/ConcurrentMap;
    //   99: aload_2
    //   100: aload_1
    //   101: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   106: pop
    //   107: aload_0
    //   108: getfield namespaceSubscribersMap : Ljava/util/concurrent/ConcurrentMap;
    //   111: aload_2
    //   112: new java/util/concurrent/CopyOnWriteArraySet
    //   115: dup
    //   116: invokespecial <init> : ()V
    //   119: invokeinterface putIfAbsent : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   124: pop
    //   125: aload_0
    //   126: monitorexit
    //   127: return
    //   128: new java/lang/IllegalArgumentException
    //   131: dup
    //   132: ldc 'namespace system is reserved.'
    //   134: invokespecial <init> : (Ljava/lang/String;)V
    //   137: athrow
    //   138: new java/lang/IllegalArgumentException
    //   141: dup
    //   142: ldc 'namespace must not be null, empty, or in need of trimming'
    //   144: invokespecial <init> : (Ljava/lang/String;)V
    //   147: athrow
    //   148: astore_1
    //   149: aload_0
    //   150: monitorexit
    //   151: aload_1
    //   152: athrow
    // Exception table:
    //   from	to	target	type
    //   2	52	148	finally
    //   55	95	148	finally
    //   95	125	148	finally
    //   128	138	148	finally
    //   138	148	148	finally
  }
  
  private void subscribeWebSocketToNamespace(FtcWebSocketImpl paramFtcWebSocketImpl, String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield namespaceHandlerMap : Ljava/util/concurrent/ConcurrentMap;
    //   6: aload_2
    //   7: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   12: ifne -> 37
    //   15: ldc 'WebSocketManager'
    //   17: ldc 'Cannot subscribe %s to namespace (%s) because there is no corresponding namespace handler registered'
    //   19: iconst_2
    //   20: anewarray java/lang/Object
    //   23: dup
    //   24: iconst_0
    //   25: aload_1
    //   26: aastore
    //   27: dup
    //   28: iconst_1
    //   29: aload_2
    //   30: aastore
    //   31: invokestatic ee : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   34: aload_0
    //   35: monitorexit
    //   36: return
    //   37: aload_0
    //   38: getfield namespaceSubscribersMap : Ljava/util/concurrent/ConcurrentMap;
    //   41: aload_2
    //   42: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   47: checkcast java/util/Set
    //   50: aload_1
    //   51: invokeinterface add : (Ljava/lang/Object;)Z
    //   56: ifeq -> 95
    //   59: aload_0
    //   60: getfield namespaceHandlerMap : Ljava/util/concurrent/ConcurrentMap;
    //   63: aload_2
    //   64: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   69: checkcast org/firstinspires/ftc/robotcore/internal/webserver/websockets/WebSocketNamespaceHandler
    //   72: aload_1
    //   73: invokevirtual onSubscribe : (Lorg/firstinspires/ftc/robotcore/internal/webserver/websockets/FtcWebSocket;)V
    //   76: ldc 'WebSocketManager'
    //   78: ldc 'Subscribed %s to namespace (%s)'
    //   80: iconst_2
    //   81: anewarray java/lang/Object
    //   84: dup
    //   85: iconst_0
    //   86: aload_1
    //   87: aastore
    //   88: dup
    //   89: iconst_1
    //   90: aload_2
    //   91: aastore
    //   92: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   95: aload_0
    //   96: monitorexit
    //   97: return
    //   98: astore_1
    //   99: aload_0
    //   100: monitorexit
    //   101: aload_1
    //   102: athrow
    // Exception table:
    //   from	to	target	type
    //   2	34	98	finally
    //   37	95	98	finally
  }
  
  private void unsubscribeWebSocketFromNamespace(FtcWebSocket paramFtcWebSocket, String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield namespaceHandlerMap : Ljava/util/concurrent/ConcurrentMap;
    //   6: aload_2
    //   7: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   12: ifne -> 37
    //   15: ldc 'WebSocketManager'
    //   17: ldc 'Cannot unsubscribe %s from namespace (%s) because there is no corresponding namespace handler registered'
    //   19: iconst_2
    //   20: anewarray java/lang/Object
    //   23: dup
    //   24: iconst_0
    //   25: aload_1
    //   26: aastore
    //   27: dup
    //   28: iconst_1
    //   29: aload_2
    //   30: aastore
    //   31: invokestatic ee : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   34: aload_0
    //   35: monitorexit
    //   36: return
    //   37: aload_0
    //   38: getfield namespaceSubscribersMap : Ljava/util/concurrent/ConcurrentMap;
    //   41: aload_2
    //   42: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   47: checkcast java/util/Set
    //   50: aload_1
    //   51: invokeinterface remove : (Ljava/lang/Object;)Z
    //   56: ifeq -> 95
    //   59: aload_0
    //   60: getfield namespaceHandlerMap : Ljava/util/concurrent/ConcurrentMap;
    //   63: aload_2
    //   64: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   69: checkcast org/firstinspires/ftc/robotcore/internal/webserver/websockets/WebSocketNamespaceHandler
    //   72: aload_1
    //   73: invokevirtual onUnsubscribe : (Lorg/firstinspires/ftc/robotcore/internal/webserver/websockets/FtcWebSocket;)V
    //   76: ldc 'WebSocketManager'
    //   78: ldc 'Unsubscribed %s from namespace (%s)'
    //   80: iconst_2
    //   81: anewarray java/lang/Object
    //   84: dup
    //   85: iconst_0
    //   86: aload_1
    //   87: aastore
    //   88: dup
    //   89: iconst_1
    //   90: aload_2
    //   91: aastore
    //   92: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   95: aload_0
    //   96: monitorexit
    //   97: return
    //   98: astore_1
    //   99: aload_0
    //   100: monitorexit
    //   101: aload_1
    //   102: athrow
    // Exception table:
    //   from	to	target	type
    //   2	34	98	finally
    //   37	95	98	finally
  }
  
  public int broadcastToNamespace(String paramString, FtcWebSocketMessage paramFtcWebSocketMessage) {
    if (paramString.equals(paramFtcWebSocketMessage.getNamespace())) {
      if (this.namespaceSubscribersMap.containsKey(paramString)) {
        int i = 0;
        Iterator<FtcWebSocket> iterator = ((Set)this.namespaceSubscribersMap.get(paramString)).iterator();
        while (iterator.hasNext()) {
          ((FtcWebSocket)iterator.next()).send(paramFtcWebSocketMessage);
          i++;
        } 
        return i;
      } 
      throw new IllegalStateException("You must register a namespace before broadcasting to it.");
    } 
    throw new IllegalArgumentException("Cannot broadcast to a different namespace than is listed in the message");
  }
  
  void onWebSocketClose(FtcWebSocket paramFtcWebSocket) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield namespaceSubscribersMap : Ljava/util/concurrent/ConcurrentMap;
    //   6: invokeinterface keySet : ()Ljava/util/Set;
    //   11: invokeinterface iterator : ()Ljava/util/Iterator;
    //   16: astore_2
    //   17: aload_2
    //   18: invokeinterface hasNext : ()Z
    //   23: ifeq -> 43
    //   26: aload_0
    //   27: aload_1
    //   28: aload_2
    //   29: invokeinterface next : ()Ljava/lang/Object;
    //   34: checkcast java/lang/String
    //   37: invokespecial unsubscribeWebSocketFromNamespace : (Lorg/firstinspires/ftc/robotcore/internal/webserver/websockets/FtcWebSocket;Ljava/lang/String;)V
    //   40: goto -> 17
    //   43: aload_0
    //   44: monitorexit
    //   45: return
    //   46: astore_1
    //   47: aload_0
    //   48: monitorexit
    //   49: aload_1
    //   50: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	46	finally
    //   17	40	46	finally
  }
  
  void onWebSocketConnected(FtcWebSocket paramFtcWebSocket) {
    if (!AppUtil.getInstance().isSaneWalkClockTime(AppUtil.getInstance().getWallClockTime()))
      ((FtcWebSocketImpl)paramFtcWebSocket).internalSend(new FtcWebSocketMessage("system", "requestCurrentTime")); 
  }
  
  void onWebSocketMessage(FtcWebSocketMessage paramFtcWebSocketMessage, FtcWebSocketImpl paramFtcWebSocketImpl) {
    if (paramFtcWebSocketMessage.getNamespace().equals("system")) {
      handleSystemNamespace(paramFtcWebSocketMessage, paramFtcWebSocketImpl);
      return;
    } 
    if (!this.namespaceHandlerMap.containsKey(paramFtcWebSocketMessage.getNamespace())) {
      RobotLog.ww("WebSocketManager", "Received message to unregistered namespace %s", new Object[] { paramFtcWebSocketMessage.getNamespace() });
      return;
    } 
    ((WebSocketNamespaceHandler)this.namespaceHandlerMap.get(paramFtcWebSocketMessage.getNamespace())).onMessage(paramFtcWebSocketMessage, paramFtcWebSocketImpl);
  }
  
  public void registerNamespaceAsBroadcastOnly(String paramString) {
    internalRegisterNamespaceHandler(new BroadcastOnlyNamespaceHandler(paramString));
    RobotLog.vv("WebSocketManager", "Registered broadcast-only namespace %s", new Object[] { paramString });
  }
  
  public void registerNamespaceHandler(WebSocketNamespaceHandler paramWebSocketNamespaceHandler) {
    internalRegisterNamespaceHandler(paramWebSocketNamespaceHandler);
    RobotLog.vv("WebSocketManager", "Registered handler for namespace %s", new Object[] { paramWebSocketNamespaceHandler.getNamespace() });
  }
  
  private static final class BroadcastOnlyNamespaceHandler extends WebSocketNamespaceHandler {
    private BroadcastOnlyNamespaceHandler(String param1String) {
      super(param1String);
    }
    
    private String getTag() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("BroadcastOnlyNamespace-");
      stringBuilder.append(getNamespace());
      return stringBuilder.toString();
    }
    
    public void onMessage(FtcWebSocketMessage param1FtcWebSocketMessage, FtcWebSocket param1FtcWebSocket) {
      super.onMessage(param1FtcWebSocketMessage, param1FtcWebSocket);
      String str = getTag();
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Message received on broadcast-only namespace ");
      stringBuilder.append(getNamespace());
      stringBuilder.append(":");
      RobotLog.ww(str, stringBuilder.toString());
      RobotLog.ww(getTag(), param1FtcWebSocketMessage.toString());
    }
  }
  
  private static final class TimePayload {
    long timeMs;
    
    String timezone;
    
    private static TimePayload fromJson(String param1String) {
      return (TimePayload)SimpleGson.getInstance().fromJson(param1String, TimePayload.class);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\webserver\websockets\WebSocketManagerImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
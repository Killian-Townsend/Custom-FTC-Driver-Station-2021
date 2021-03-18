package org.firstinspires.ftc.robotserver.internal.webserver.websockets;

import java.util.ArrayList;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.WebSocketNamespaceHandler;

public final class WebSocketNamespaceHandlerRegistry {
  private static WebSocketManagerImpl manager;
  
  private static List<WebSocketNamespaceHandler> namespaceHandlersToBeRegistered = new ArrayList<WebSocketNamespaceHandler>();
  
  public static void onWebSocketServerCreation(WebSocketManagerImpl paramWebSocketManagerImpl) {
    // Byte code:
    //   0: ldc org/firstinspires/ftc/robotserver/internal/webserver/websockets/WebSocketNamespaceHandlerRegistry
    //   2: monitorenter
    //   3: aload_0
    //   4: putstatic org/firstinspires/ftc/robotserver/internal/webserver/websockets/WebSocketNamespaceHandlerRegistry.manager : Lorg/firstinspires/ftc/robotserver/internal/webserver/websockets/WebSocketManagerImpl;
    //   7: getstatic org/firstinspires/ftc/robotserver/internal/webserver/websockets/WebSocketNamespaceHandlerRegistry.namespaceHandlersToBeRegistered : Ljava/util/List;
    //   10: invokeinterface iterator : ()Ljava/util/Iterator;
    //   15: astore_1
    //   16: aload_1
    //   17: invokeinterface hasNext : ()Z
    //   22: ifeq -> 41
    //   25: aload_0
    //   26: aload_1
    //   27: invokeinterface next : ()Ljava/lang/Object;
    //   32: checkcast org/firstinspires/ftc/robotcore/internal/webserver/websockets/WebSocketNamespaceHandler
    //   35: invokevirtual registerNamespaceHandler : (Lorg/firstinspires/ftc/robotcore/internal/webserver/websockets/WebSocketNamespaceHandler;)V
    //   38: goto -> 16
    //   41: ldc org/firstinspires/ftc/robotserver/internal/webserver/websockets/WebSocketNamespaceHandlerRegistry
    //   43: monitorexit
    //   44: return
    //   45: astore_0
    //   46: ldc org/firstinspires/ftc/robotserver/internal/webserver/websockets/WebSocketNamespaceHandlerRegistry
    //   48: monitorexit
    //   49: aload_0
    //   50: athrow
    // Exception table:
    //   from	to	target	type
    //   3	16	45	finally
    //   16	38	45	finally
  }
  
  public static void registerNamespaceHandler(WebSocketNamespaceHandler paramWebSocketNamespaceHandler) {
    // Byte code:
    //   0: ldc org/firstinspires/ftc/robotserver/internal/webserver/websockets/WebSocketNamespaceHandlerRegistry
    //   2: monitorenter
    //   3: getstatic org/firstinspires/ftc/robotserver/internal/webserver/websockets/WebSocketNamespaceHandlerRegistry.manager : Lorg/firstinspires/ftc/robotserver/internal/webserver/websockets/WebSocketManagerImpl;
    //   6: ifnull -> 19
    //   9: getstatic org/firstinspires/ftc/robotserver/internal/webserver/websockets/WebSocketNamespaceHandlerRegistry.manager : Lorg/firstinspires/ftc/robotserver/internal/webserver/websockets/WebSocketManagerImpl;
    //   12: aload_0
    //   13: invokevirtual registerNamespaceHandler : (Lorg/firstinspires/ftc/robotcore/internal/webserver/websockets/WebSocketNamespaceHandler;)V
    //   16: goto -> 29
    //   19: getstatic org/firstinspires/ftc/robotserver/internal/webserver/websockets/WebSocketNamespaceHandlerRegistry.namespaceHandlersToBeRegistered : Ljava/util/List;
    //   22: aload_0
    //   23: invokeinterface add : (Ljava/lang/Object;)Z
    //   28: pop
    //   29: ldc org/firstinspires/ftc/robotserver/internal/webserver/websockets/WebSocketNamespaceHandlerRegistry
    //   31: monitorexit
    //   32: return
    //   33: astore_0
    //   34: ldc org/firstinspires/ftc/robotserver/internal/webserver/websockets/WebSocketNamespaceHandlerRegistry
    //   36: monitorexit
    //   37: aload_0
    //   38: athrow
    // Exception table:
    //   from	to	target	type
    //   3	16	33	finally
    //   19	29	33	finally
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\webserver\websockets\WebSocketNamespaceHandlerRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
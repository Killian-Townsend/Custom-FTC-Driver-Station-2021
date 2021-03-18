package org.firstinspires.ftc.robotserver.internal.webserver.controlhubupdater;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.os.ResultReceiver;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ThreadPool;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.network.PeerStatusCallback;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.ui.UILocation;
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.FtcWebSocket;
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.FtcWebSocketMessage;
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.WebSocketManager;
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.WebSocketMessageTypeHandler;
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.WebSocketNamespaceHandler;
import org.firstinspires.ftc.robotserver.internal.webserver.controlhubupdater.result.Result;

public final class ChUpdaterCommManager extends WebSocketNamespaceHandler {
  private static final String BIND_MESSAGE_TYPE = "bind";
  
  private static final String CONFIRM_DANGEROUS_ACTION_MESSAGE_TYPE = "confirmDangerousAction";
  
  private static final String DELETE_UPLOADED_FILE_MESSAGE_TYPE = "deleteUploadedFile";
  
  private static final String NOTIFICATION_MESSAGE_TYPE = "notification";
  
  private static final String TAG = "ChUpdaterCommManager";
  
  public static final String WS_NAMESPACE = "ControlHubUpdater";
  
  private final Queue<String> toastQueue;
  
  private final Object toastQueueLock;
  
  private final Map<UUID, ChUpdaterResultReceiver> uuidResultReceiverMap = new ConcurrentHashMap<UUID, ChUpdaterResultReceiver>(4);
  
  private final WebSocketManager webSocketManager;
  
  public ChUpdaterCommManager(WebSocketManager paramWebSocketManager) {
    super("ControlHubUpdater");
    this.webSocketManager = paramWebSocketManager;
    synchronized (ChUpdaterBroadcastReceiver.outerClassReferenceLock) {
      ChUpdaterBroadcastReceiver.access$102(this);
      this.toastQueue = ChUpdaterBroadcastReceiver.toastQueue;
      this.toastQueueLock = ChUpdaterBroadcastReceiver.toastQueueLock;
      null = new QueuedToastSender();
      if (NetworkConnectionHandler.getInstance().registerPeerStatusCallback((PeerStatusCallback)null))
        null.onPeerConnected(); 
      return;
    } 
  }
  
  private static FtcWebSocketMessage createWsMessageFromResult(Result paramResult, boolean paramBoolean, String paramString) {
    String str2 = paramString;
    if (paramString == null)
      str2 = paramResult.getMessage(); 
    paramString = paramResult.getDetailMessage();
    Result.DetailMessageType detailMessageType = paramResult.getDetailMessageType();
    Result.PresentationType presentationType2 = paramResult.getPresentationType();
    String str1 = str2;
    if (detailMessageType == Result.DetailMessageType.DISPLAYED) {
      str1 = str2;
      if (paramString != null) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str2);
        stringBuilder.append("\n\n");
        stringBuilder.append(paramString);
        str1 = stringBuilder.toString();
      } 
    } 
    Result.PresentationType presentationType1 = presentationType2;
    if (paramBoolean) {
      presentationType1 = presentationType2;
      if (presentationType2 == Result.PresentationType.PROMPT)
        presentationType1 = Result.PresentationType.ERROR; 
    } 
    return new FtcWebSocketMessage("ControlHubUpdater", "notification", (new NotificationPayload(str1, presentationType1)).toJson());
  }
  
  private static boolean isResultEligibleForToast(Result paramResult) {
    return (paramResult.getPresentationType() == Result.PresentationType.SUCCESS || paramResult.getPresentationType() == Result.PresentationType.ERROR);
  }
  
  private static void logResult(Result paramResult) {
    byte b;
    String str = paramResult.getMessage();
    if (paramResult.getPresentationType() == Result.PresentationType.ERROR) {
      b = 6;
    } else {
      b = 2;
    } 
    RobotLog.internalLog(b, "ChUpdaterCommManager", paramResult.getCause(), String.format("%s result received: %s", new Object[] { paramResult.getPresentationType().name(), str }));
    if (paramResult.getDetailMessage() != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("detail message: ");
      stringBuilder.append(paramResult.getDetailMessage());
      RobotLog.internalLog(b, "ChUpdaterCommManager", null, stringBuilder.toString());
    } 
  }
  
  private static boolean sendResultAsToastIfPossible(Result paramResult) {
    if (isResultEligibleForToast(paramResult) && NetworkConnectionHandler.getInstance().isPeerConnected()) {
      AppUtil.getInstance().showToast(UILocation.BOTH, paramResult.getMessage(), 1);
      return true;
    } 
    return false;
  }
  
  public void onSubscribe(FtcWebSocket paramFtcWebSocket) {
    synchronized (ChUpdaterBroadcastReceiver.messagesForNewWsConnectionsLock) {
      Iterator<FtcWebSocketMessage> iterator = ChUpdaterBroadcastReceiver.messagesForNewWsConnections.iterator();
      while (iterator.hasNext()) {
        FtcWebSocketMessage ftcWebSocketMessage = iterator.next();
        paramFtcWebSocket.send(ftcWebSocketMessage);
        if (ChUpdaterBroadcastReceiver.withinReconnectionAllowancePeriod) {
          ChUpdaterBroadcastReceiver.seenMessageSet.add(ftcWebSocketMessage);
          continue;
        } 
        iterator.remove();
      } 
      return;
    } 
  }
  
  protected void registerMessageTypeHandlers(Map<String, WebSocketMessageTypeHandler> paramMap) {
    paramMap.put("bind", new BindMessageHandler());
    paramMap.put("confirmDangerousAction", new ConfirmDangerousActionMessageHandler());
    paramMap.put("deleteUploadedFile", new DeleteUploadedFileMessageHandler());
  }
  
  public UUID startUpdate(UpdateType paramUpdateType, File paramFile) {
    String str;
    ChUpdaterResultReceiver chUpdaterResultReceiver = new ChUpdaterResultReceiver(paramFile, new Handler(Looper.getMainLooper()));
    UUID uUID = UUID.randomUUID();
    this.uuidResultReceiverMap.put(uUID, chUpdaterResultReceiver);
    int i = null.$SwitchMap$org$firstinspires$ftc$robotserver$internal$webserver$controlhubupdater$ChUpdaterCommManager$UpdateType[paramUpdateType.ordinal()];
    if (i != 1) {
      if (i != 2) {
        paramUpdateType = null;
      } else {
        str = "com.revrobotics.controlhubupdater.action.APPLY_OTA_UPDATE";
      } 
    } else {
      str = "com.revrobotics.controlhubupdater.action.UPDATE_FTC_APP";
    } 
    Intent intent = new Intent();
    intent.setComponent(new ComponentName("com.revrobotics.controlhubupdater", "com.revrobotics.controlhubupdater.UpdateService"));
    intent.setAction(str);
    intent.putExtra("com.revrobotics.controlhubupdater.extra.UPDATE_FILE_PATH", paramFile.getAbsolutePath());
    intent.putExtra("com.revrobotics.controlhubupdater.extra.RESULT_RECEIVER", (Parcelable)AppUtil.wrapResultReceiverForIpc(chUpdaterResultReceiver));
    chUpdaterResultReceiver.setSentIntent(intent);
    AppUtil.getDefContext().startService(intent);
    return uUID;
  }
  
  private class BindMessageHandler extends UpdaterMessageTypeHandler {
    private BindMessageHandler() {}
    
    void handleUpdaterMessage(FtcWebSocket param1FtcWebSocket, UUID param1UUID, ChUpdaterCommManager.ChUpdaterResultReceiver param1ChUpdaterResultReceiver) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Binding WebSocket ");
      stringBuilder.append(param1FtcWebSocket);
      stringBuilder.append(" to UUID ");
      stringBuilder.append(param1UUID);
      RobotLog.dd("ChUpdaterCommManager", stringBuilder.toString());
      param1ChUpdaterResultReceiver.setWebSocket(param1FtcWebSocket);
    }
  }
  
  public static class ChUpdaterBroadcastReceiver extends BroadcastReceiver {
    private static final int RECONNECTION_ALLOWANCE_SECONDS = 10;
    
    private static final List<FtcWebSocketMessage> messagesForNewWsConnections = new ArrayList<FtcWebSocketMessage>();
    
    private static final Object messagesForNewWsConnectionsLock;
    
    private static ChUpdaterCommManager outerClassReference;
    
    private static final Object outerClassReferenceLock = new Object();
    
    private static Set<FtcWebSocketMessage> seenMessageSet = new HashSet<FtcWebSocketMessage>();
    
    private static final Queue<String> toastQueue;
    
    private static final Object toastQueueLock;
    
    private static boolean withinReconnectionAllowancePeriod = true;
    
    static {
      messagesForNewWsConnectionsLock = new Object();
      toastQueue = new LinkedBlockingQueue<String>();
      toastQueueLock = new Object();
      ThreadPool.getDefaultScheduler().schedule(new AllowancePeriodExpiredRunnable(), 10L, TimeUnit.SECONDS);
    }
    
    public void onReceive(Context param1Context, Intent param1Intent) {
      // Byte code:
      //   0: aload_2
      //   1: ldc 'com.revrobotics.controlhubupdater.broadcast.extra.BUNDLE'
      //   3: invokevirtual getBundleExtra : (Ljava/lang/String;)Landroid/os/Bundle;
      //   6: invokestatic fromBundle : (Landroid/os/Bundle;)Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/Result;
      //   9: astore_2
      //   10: aload_2
      //   11: invokestatic access$1400 : (Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/Result;)V
      //   14: aload_2
      //   15: invokestatic access$1500 : (Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/Result;)Z
      //   18: istore #4
      //   20: aload_2
      //   21: invokestatic access$1600 : (Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/Result;)Z
      //   24: istore #5
      //   26: iload #4
      //   28: ifeq -> 65
      //   31: iload #5
      //   33: ifne -> 65
      //   36: getstatic org/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/ChUpdaterCommManager$ChUpdaterBroadcastReceiver.toastQueueLock : Ljava/lang/Object;
      //   39: astore_1
      //   40: aload_1
      //   41: monitorenter
      //   42: getstatic org/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/ChUpdaterCommManager$ChUpdaterBroadcastReceiver.toastQueue : Ljava/util/Queue;
      //   45: aload_2
      //   46: invokevirtual getMessage : ()Ljava/lang/String;
      //   49: invokeinterface add : (Ljava/lang/Object;)Z
      //   54: pop
      //   55: aload_1
      //   56: monitorexit
      //   57: goto -> 65
      //   60: astore_2
      //   61: aload_1
      //   62: monitorexit
      //   63: aload_2
      //   64: athrow
      //   65: aload_2
      //   66: iconst_1
      //   67: aconst_null
      //   68: invokestatic access$1700 : (Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/Result;ZLjava/lang/String;)Lorg/firstinspires/ftc/robotcore/internal/webserver/websockets/FtcWebSocketMessage;
      //   71: astore_1
      //   72: iconst_0
      //   73: istore_3
      //   74: getstatic org/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/ChUpdaterCommManager$ChUpdaterBroadcastReceiver.outerClassReferenceLock : Ljava/lang/Object;
      //   77: astore_2
      //   78: aload_2
      //   79: monitorenter
      //   80: getstatic org/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/ChUpdaterCommManager$ChUpdaterBroadcastReceiver.outerClassReference : Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/ChUpdaterCommManager;
      //   83: ifnull -> 101
      //   86: getstatic org/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/ChUpdaterCommManager$ChUpdaterBroadcastReceiver.outerClassReference : Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/ChUpdaterCommManager;
      //   89: invokestatic access$1800 : (Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/ChUpdaterCommManager;)Lorg/firstinspires/ftc/robotcore/internal/webserver/websockets/WebSocketManager;
      //   92: ldc 'ControlHubUpdater'
      //   94: aload_1
      //   95: invokeinterface broadcastToNamespace : (Ljava/lang/String;Lorg/firstinspires/ftc/robotcore/internal/webserver/websockets/FtcWebSocketMessage;)I
      //   100: istore_3
      //   101: aload_2
      //   102: monitorexit
      //   103: getstatic org/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/ChUpdaterCommManager$ChUpdaterBroadcastReceiver.messagesForNewWsConnectionsLock : Ljava/lang/Object;
      //   106: astore_2
      //   107: aload_2
      //   108: monitorenter
      //   109: iload_3
      //   110: ifeq -> 119
      //   113: getstatic org/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/ChUpdaterCommManager$ChUpdaterBroadcastReceiver.withinReconnectionAllowancePeriod : Z
      //   116: ifeq -> 143
      //   119: getstatic org/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/ChUpdaterCommManager$ChUpdaterBroadcastReceiver.messagesForNewWsConnections : Ljava/util/List;
      //   122: aload_1
      //   123: invokeinterface add : (Ljava/lang/Object;)Z
      //   128: pop
      //   129: iload_3
      //   130: ifle -> 143
      //   133: getstatic org/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/ChUpdaterCommManager$ChUpdaterBroadcastReceiver.seenMessageSet : Ljava/util/Set;
      //   136: aload_1
      //   137: invokeinterface add : (Ljava/lang/Object;)Z
      //   142: pop
      //   143: aload_2
      //   144: monitorexit
      //   145: return
      //   146: astore_1
      //   147: aload_2
      //   148: monitorexit
      //   149: aload_1
      //   150: athrow
      //   151: astore_1
      //   152: aload_2
      //   153: monitorexit
      //   154: aload_1
      //   155: athrow
      // Exception table:
      //   from	to	target	type
      //   42	57	60	finally
      //   61	63	60	finally
      //   80	101	151	finally
      //   101	103	151	finally
      //   113	119	146	finally
      //   119	129	146	finally
      //   133	143	146	finally
      //   143	145	146	finally
      //   147	149	146	finally
      //   152	154	151	finally
    }
    
    private static class AllowancePeriodExpiredRunnable implements Runnable {
      private AllowancePeriodExpiredRunnable() {}
      
      public void run() {
        synchronized (ChUpdaterCommManager.ChUpdaterBroadcastReceiver.messagesForNewWsConnectionsLock) {
          ChUpdaterCommManager.ChUpdaterBroadcastReceiver.access$702(false);
          for (FtcWebSocketMessage ftcWebSocketMessage : ChUpdaterCommManager.ChUpdaterBroadcastReceiver.seenMessageSet)
            ChUpdaterCommManager.ChUpdaterBroadcastReceiver.messagesForNewWsConnections.remove(ftcWebSocketMessage); 
          ChUpdaterCommManager.ChUpdaterBroadcastReceiver.access$802(null);
          return;
        } 
      }
    }
  }
  
  private static class AllowancePeriodExpiredRunnable implements Runnable {
    private AllowancePeriodExpiredRunnable() {}
    
    public void run() {
      synchronized (ChUpdaterCommManager.ChUpdaterBroadcastReceiver.messagesForNewWsConnectionsLock) {
        ChUpdaterCommManager.ChUpdaterBroadcastReceiver.access$702(false);
        for (FtcWebSocketMessage ftcWebSocketMessage : ChUpdaterCommManager.ChUpdaterBroadcastReceiver.seenMessageSet)
          ChUpdaterCommManager.ChUpdaterBroadcastReceiver.messagesForNewWsConnections.remove(ftcWebSocketMessage); 
        ChUpdaterCommManager.ChUpdaterBroadcastReceiver.access$802(null);
        return;
      } 
    }
  }
  
  private class ChUpdaterResultReceiver extends ResultReceiver {
    private Result lastResult;
    
    private Intent sentIntent;
    
    private final File uploadedFile;
    
    private FtcWebSocket webSocket;
    
    ChUpdaterResultReceiver(File param1File, Handler param1Handler) {
      super(param1Handler);
      this.uploadedFile = param1File;
    }
    
    private void confirmDangerousAction() {
      this.sentIntent.putExtra("com.revrobotics.controlhubupdater.extra.DANGEROUS_ACTION_CONFIRMED", true);
      AppUtil.getDefContext().startService(this.sentIntent);
    }
    
    private void deleteAssociatedUpload() {
      this.uploadedFile.delete();
    }
    
    private void setSentIntent(Intent param1Intent) {
      this.sentIntent = param1Intent;
    }
    
    private void setWebSocket(FtcWebSocket param1FtcWebSocket) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: aload_1
      //   4: putfield webSocket : Lorg/firstinspires/ftc/robotcore/internal/webserver/websockets/FtcWebSocket;
      //   7: aload_0
      //   8: getfield lastResult : Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/Result;
      //   11: ifnull -> 29
      //   14: aload_1
      //   15: aload_0
      //   16: getfield lastResult : Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/Result;
      //   19: iconst_0
      //   20: aconst_null
      //   21: invokestatic access$1700 : (Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/Result;ZLjava/lang/String;)Lorg/firstinspires/ftc/robotcore/internal/webserver/websockets/FtcWebSocketMessage;
      //   24: invokeinterface send : (Lorg/firstinspires/ftc/robotcore/internal/webserver/websockets/FtcWebSocketMessage;)V
      //   29: aload_0
      //   30: monitorexit
      //   31: return
      //   32: astore_1
      //   33: aload_0
      //   34: monitorexit
      //   35: aload_1
      //   36: athrow
      // Exception table:
      //   from	to	target	type
      //   2	29	32	finally
    }
    
    protected void onReceiveResult(int param1Int, Bundle param1Bundle) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_2
      //   3: invokestatic fromBundle : (Landroid/os/Bundle;)Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/Result;
      //   6: astore #7
      //   8: aload #7
      //   10: invokestatic access$1400 : (Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/Result;)V
      //   13: aload #7
      //   15: invokestatic access$1500 : (Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/Result;)Z
      //   18: istore_3
      //   19: aload #7
      //   21: invokestatic access$1600 : (Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/Result;)Z
      //   24: istore #4
      //   26: aload_0
      //   27: getfield this$0 : Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/ChUpdaterCommManager;
      //   30: invokestatic access$2400 : (Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/ChUpdaterCommManager;)Ljava/lang/Object;
      //   33: astore #6
      //   35: aload #6
      //   37: monitorenter
      //   38: aload_0
      //   39: getfield webSocket : Lorg/firstinspires/ftc/robotcore/internal/webserver/websockets/FtcWebSocket;
      //   42: ifnull -> 123
      //   45: aload_0
      //   46: getfield webSocket : Lorg/firstinspires/ftc/robotcore/internal/webserver/websockets/FtcWebSocket;
      //   49: invokeinterface isOpen : ()Z
      //   54: ifeq -> 123
      //   57: aconst_null
      //   58: astore #5
      //   60: aload #5
      //   62: astore_2
      //   63: aload #7
      //   65: invokevirtual getResultType : ()Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/ResultType;
      //   68: getstatic org/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/OtaResultType.VERIFICATION_SUCCEEDED : Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/OtaResultType;
      //   71: if_acmpne -> 102
      //   74: aload #5
      //   76: astore_2
      //   77: aload_0
      //   78: getfield webSocket : Lorg/firstinspires/ftc/robotcore/internal/webserver/websockets/FtcWebSocket;
      //   81: invokeinterface getRemoteIpAddress : ()Ljava/net/InetAddress;
      //   86: invokevirtual isLoopbackAddress : ()Z
      //   89: ifeq -> 102
      //   92: invokestatic getDefContext : ()Landroid/content/Context;
      //   95: getstatic org/firstinspires/ftc/robotcore/internal/webserver/R$string.ota_result_type_verification_succeeded_localhost : I
      //   98: invokevirtual getString : (I)Ljava/lang/String;
      //   101: astore_2
      //   102: aload #7
      //   104: iconst_0
      //   105: aload_2
      //   106: invokestatic access$1700 : (Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/Result;ZLjava/lang/String;)Lorg/firstinspires/ftc/robotcore/internal/webserver/websockets/FtcWebSocketMessage;
      //   109: astore_2
      //   110: aload_0
      //   111: getfield webSocket : Lorg/firstinspires/ftc/robotcore/internal/webserver/websockets/FtcWebSocket;
      //   114: aload_2
      //   115: invokeinterface send : (Lorg/firstinspires/ftc/robotcore/internal/webserver/websockets/FtcWebSocketMessage;)V
      //   120: goto -> 156
      //   123: aload_0
      //   124: aload #7
      //   126: putfield lastResult : Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/result/Result;
      //   129: iload #4
      //   131: ifne -> 156
      //   134: iload_3
      //   135: ifeq -> 156
      //   138: aload_0
      //   139: getfield this$0 : Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/ChUpdaterCommManager;
      //   142: invokestatic access$2500 : (Lorg/firstinspires/ftc/robotserver/internal/webserver/controlhubupdater/ChUpdaterCommManager;)Ljava/util/Queue;
      //   145: aload #7
      //   147: invokevirtual getMessage : ()Ljava/lang/String;
      //   150: invokeinterface add : (Ljava/lang/Object;)Z
      //   155: pop
      //   156: aload #6
      //   158: monitorexit
      //   159: aload_0
      //   160: monitorexit
      //   161: return
      //   162: astore_2
      //   163: aload #6
      //   165: monitorexit
      //   166: aload_2
      //   167: athrow
      //   168: astore_2
      //   169: aload_0
      //   170: monitorexit
      //   171: aload_2
      //   172: athrow
      // Exception table:
      //   from	to	target	type
      //   2	38	168	finally
      //   38	57	162	finally
      //   63	74	162	finally
      //   77	102	162	finally
      //   102	120	162	finally
      //   123	129	162	finally
      //   138	156	162	finally
      //   156	159	162	finally
      //   163	166	162	finally
      //   166	168	168	finally
    }
  }
  
  private class ConfirmDangerousActionMessageHandler extends UpdaterMessageTypeHandler {
    private ConfirmDangerousActionMessageHandler() {}
    
    void handleUpdaterMessage(FtcWebSocket param1FtcWebSocket, UUID param1UUID, ChUpdaterCommManager.ChUpdaterResultReceiver param1ChUpdaterResultReceiver) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Confirming dangerous action for UUID ");
      stringBuilder.append(param1UUID);
      RobotLog.ww("ChUpdaterCommManager", stringBuilder.toString());
      param1ChUpdaterResultReceiver.confirmDangerousAction();
    }
  }
  
  private class DeleteUploadedFileMessageHandler extends UpdaterMessageTypeHandler {
    private DeleteUploadedFileMessageHandler() {}
    
    void handleUpdaterMessage(FtcWebSocket param1FtcWebSocket, UUID param1UUID, ChUpdaterCommManager.ChUpdaterResultReceiver param1ChUpdaterResultReceiver) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Deleting uploaded file associated with UUID ");
      stringBuilder.append(param1UUID);
      RobotLog.ww("ChUpdaterCommManager", stringBuilder.toString());
      param1ChUpdaterResultReceiver.deleteAssociatedUpload();
    }
  }
  
  private static class NotificationPayload {
    final String message;
    
    final Result.PresentationType presentationType;
    
    NotificationPayload(String param1String, Result.PresentationType param1PresentationType) {
      this.message = param1String;
      this.presentationType = param1PresentationType;
    }
    
    String toJson() {
      return SimpleGson.getInstance().toJson(this);
    }
  }
  
  private class QueuedToastSender implements PeerStatusCallback {
    private QueuedToastSender() {}
    
    public void onPeerConnected() {
      synchronized (ChUpdaterCommManager.this.toastQueueLock) {
        for (String str = ChUpdaterCommManager.this.toastQueue.poll(); str != null; str = ChUpdaterCommManager.this.toastQueue.poll())
          AppUtil.getInstance().showToast(UILocation.BOTH, str, 1); 
        return;
      } 
    }
    
    public void onPeerDisconnected() {}
  }
  
  public enum UpdateType {
    APP, OTA;
    
    static {
      UpdateType updateType = new UpdateType("APP", 1);
      APP = updateType;
      $VALUES = new UpdateType[] { OTA, updateType };
    }
  }
  
  private abstract class UpdaterMessageTypeHandler implements WebSocketMessageTypeHandler {
    private UpdaterMessageTypeHandler() {}
    
    public final void handleMessage(FtcWebSocketMessage param1FtcWebSocketMessage, FtcWebSocket param1FtcWebSocket) {
      UUID uUID = UUID.fromString(param1FtcWebSocketMessage.getPayload());
      ChUpdaterCommManager.ChUpdaterResultReceiver chUpdaterResultReceiver = (ChUpdaterCommManager.ChUpdaterResultReceiver)ChUpdaterCommManager.this.uuidResultReceiverMap.get(uUID);
      if (chUpdaterResultReceiver != null) {
        handleUpdaterMessage(param1FtcWebSocket, uUID, chUpdaterResultReceiver);
        return;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Received message with unknown UUID ");
      stringBuilder.append(uUID);
      RobotLog.ww("ChUpdaterCommManager", stringBuilder.toString());
    }
    
    abstract void handleUpdaterMessage(FtcWebSocket param1FtcWebSocket, UUID param1UUID, ChUpdaterCommManager.ChUpdaterResultReceiver param1ChUpdaterResultReceiver);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\webserver\controlhubupdater\ChUpdaterCommManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.network;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.robocol.RobocolDatagram;
import com.qualcomm.robotcore.robocol.RobocolDatagramSocket;
import com.qualcomm.robotcore.robocol.RobocolParsable;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ThreadPool;
import java.util.concurrent.LinkedBlockingDeque;

public class RecvLoopRunnable implements Runnable {
  private static final int BANDWIDTH_SAMPLE_PERIOD = 500;
  
  public static boolean DEBUG = false;
  
  private static boolean DO_TRAFFIC_DATA = false;
  
  public static final String TAG = "Robocol";
  
  private static ElapsedTime bandwidthSampleTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
  
  private double bytesPerMilli = 0.0D;
  
  protected RecvLoopCallback callback;
  
  protected ElapsedTime commandProcessingTimer;
  
  protected LinkedBlockingDeque<Command> commandsToProcess = new LinkedBlockingDeque<Command>();
  
  protected ElapsedTime lastRecvPacket;
  
  protected ElapsedTime packetProcessingTimer;
  
  protected double sProcessingTimerReportingThreshold;
  
  protected RobocolDatagramSocket socket;
  
  public RecvLoopRunnable(RecvLoopCallback paramRecvLoopCallback, RobocolDatagramSocket paramRobocolDatagramSocket, ElapsedTime paramElapsedTime) {
    this.callback = paramRecvLoopCallback;
    this.socket = paramRobocolDatagramSocket;
    this.lastRecvPacket = paramElapsedTime;
    this.packetProcessingTimer = new ElapsedTime();
    this.commandProcessingTimer = new ElapsedTime();
    this.sProcessingTimerReportingThreshold = 0.5D;
    this.socket.gatherTrafficData(DO_TRAFFIC_DATA);
    RobotLog.vv("Robocol", "RecvLoopRunnable created");
  }
  
  protected void calculateBytesPerMilli() {
    if (bandwidthSampleTimer.time() >= 500.0D) {
      this.bytesPerMilli = (this.socket.getRxDataSample() + this.socket.getTxDataSample()) / bandwidthSampleTimer.time();
      bandwidthSampleTimer.reset();
      this.socket.resetDataSample();
    } 
  }
  
  public long getBytesPerSecond() {
    return (long)(this.bytesPerMilli * 1000.0D);
  }
  
  public void injectReceivedCommand(Command paramCommand) {
    this.commandsToProcess.addLast(paramCommand);
  }
  
  public void run() {
    ThreadPool.logThreadLifeCycle("RecvLoopRunnable.run()", new Runnable() {
          public void run() {
            // Byte code:
            //   0: invokestatic access$000 : ()Lcom/qualcomm/robotcore/util/ElapsedTime;
            //   3: invokevirtual reset : ()V
            //   6: invokestatic getInstance : ()Lorg/firstinspires/ftc/robotcore/internal/system/AppUtil;
            //   9: astore #8
            //   11: invokestatic currentThread : ()Ljava/lang/Thread;
            //   14: invokevirtual isInterrupted : ()Z
            //   17: ifne -> 583
            //   20: aload_0
            //   21: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/network/RecvLoopRunnable;
            //   24: getfield socket : Lcom/qualcomm/robotcore/robocol/RobocolDatagramSocket;
            //   27: invokevirtual recv : ()Lcom/qualcomm/robotcore/robocol/RobocolDatagram;
            //   30: astore #7
            //   32: aload #8
            //   34: invokevirtual getWallClockTime : ()J
            //   37: lstore_3
            //   38: invokestatic currentThread : ()Ljava/lang/Thread;
            //   41: invokevirtual isInterrupted : ()Z
            //   44: ifeq -> 48
            //   47: return
            //   48: aload #7
            //   50: ifnonnull -> 93
            //   53: aload_0
            //   54: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/network/RecvLoopRunnable;
            //   57: getfield socket : Lcom/qualcomm/robotcore/robocol/RobocolDatagramSocket;
            //   60: invokevirtual isClosed : ()Z
            //   63: ifeq -> 87
            //   66: ldc 'Robocol'
            //   68: ldc 'socket closed; %s returning'
            //   70: iconst_1
            //   71: anewarray java/lang/Object
            //   74: dup
            //   75: iconst_0
            //   76: invokestatic currentThread : ()Ljava/lang/Thread;
            //   79: invokevirtual getName : ()Ljava/lang/String;
            //   82: aastore
            //   83: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
            //   86: return
            //   87: invokestatic yield : ()V
            //   90: goto -> 11
            //   93: aload #7
            //   95: invokevirtual getAddress : ()Ljava/net/InetAddress;
            //   98: invokestatic getInstance : ()Lorg/firstinspires/ftc/robotcore/internal/network/NetworkConnectionHandler;
            //   101: invokevirtual getCurrentPeerAddr : ()Ljava/net/InetAddress;
            //   104: invokevirtual equals : (Ljava/lang/Object;)Z
            //   107: istore #5
            //   109: iload #5
            //   111: ifne -> 128
            //   114: aload #7
            //   116: invokevirtual getMsgType : ()Lcom/qualcomm/robotcore/robocol/RobocolParsable$MsgType;
            //   119: getstatic com/qualcomm/robotcore/robocol/RobocolParsable$MsgType.PEER_DISCOVERY : Lcom/qualcomm/robotcore/robocol/RobocolParsable$MsgType;
            //   122: if_acmpeq -> 128
            //   125: goto -> 11
            //   128: iload #5
            //   130: ifeq -> 153
            //   133: aload_0
            //   134: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/network/RecvLoopRunnable;
            //   137: getfield lastRecvPacket : Lcom/qualcomm/robotcore/util/ElapsedTime;
            //   140: ifnull -> 153
            //   143: aload_0
            //   144: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/network/RecvLoopRunnable;
            //   147: getfield lastRecvPacket : Lcom/qualcomm/robotcore/util/ElapsedTime;
            //   150: invokevirtual reset : ()V
            //   153: aload_0
            //   154: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/network/RecvLoopRunnable;
            //   157: getfield packetProcessingTimer : Lcom/qualcomm/robotcore/util/ElapsedTime;
            //   160: invokevirtual reset : ()V
            //   163: aload_0
            //   164: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/network/RecvLoopRunnable;
            //   167: getfield callback : Lorg/firstinspires/ftc/robotcore/internal/network/RecvLoopRunnable$RecvLoopCallback;
            //   170: aload #7
            //   172: invokeinterface packetReceived : (Lcom/qualcomm/robotcore/robocol/RobocolDatagram;)Lorg/firstinspires/ftc/robotcore/internal/network/CallbackResult;
            //   177: getstatic org/firstinspires/ftc/robotcore/internal/network/CallbackResult.HANDLED : Lorg/firstinspires/ftc/robotcore/internal/network/CallbackResult;
            //   180: if_acmpeq -> 446
            //   183: getstatic org/firstinspires/ftc/robotcore/internal/network/RecvLoopRunnable$2.$SwitchMap$com$qualcomm$robotcore$robocol$RobocolParsable$MsgType : [I
            //   186: aload #7
            //   188: invokevirtual getMsgType : ()Lcom/qualcomm/robotcore/robocol/RobocolParsable$MsgType;
            //   191: invokevirtual ordinal : ()I
            //   194: iaload
            //   195: tableswitch default -> 605, 1 -> 387, 2 -> 368, 3 -> 290, 4 -> 272, 5 -> 254, 6 -> 236, 7 -> 446
            //   236: aload_0
            //   237: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/network/RecvLoopRunnable;
            //   240: getfield callback : Lorg/firstinspires/ftc/robotcore/internal/network/RecvLoopRunnable$RecvLoopCallback;
            //   243: aload #7
            //   245: invokeinterface emptyEvent : (Lcom/qualcomm/robotcore/robocol/RobocolDatagram;)Lorg/firstinspires/ftc/robotcore/internal/network/CallbackResult;
            //   250: pop
            //   251: goto -> 446
            //   254: aload_0
            //   255: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/network/RecvLoopRunnable;
            //   258: getfield callback : Lorg/firstinspires/ftc/robotcore/internal/network/RecvLoopRunnable$RecvLoopCallback;
            //   261: aload #7
            //   263: invokeinterface gamepadEvent : (Lcom/qualcomm/robotcore/robocol/RobocolDatagram;)Lorg/firstinspires/ftc/robotcore/internal/network/CallbackResult;
            //   268: pop
            //   269: goto -> 446
            //   272: aload_0
            //   273: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/network/RecvLoopRunnable;
            //   276: getfield callback : Lorg/firstinspires/ftc/robotcore/internal/network/RecvLoopRunnable$RecvLoopCallback;
            //   279: aload #7
            //   281: invokeinterface telemetryEvent : (Lcom/qualcomm/robotcore/robocol/RobocolDatagram;)Lorg/firstinspires/ftc/robotcore/internal/network/CallbackResult;
            //   286: pop
            //   287: goto -> 446
            //   290: new com/qualcomm/robotcore/robocol/Command
            //   293: dup
            //   294: aload #7
            //   296: invokespecial <init> : (Lcom/qualcomm/robotcore/robocol/RobocolDatagram;)V
            //   299: astore #6
            //   301: invokestatic getInstance : ()Lorg/firstinspires/ftc/robotcore/internal/network/NetworkConnectionHandler;
            //   304: aload #6
            //   306: invokevirtual processAcknowledgments : (Lcom/qualcomm/robotcore/robocol/Command;)Lorg/firstinspires/ftc/robotcore/internal/network/CallbackResult;
            //   309: invokevirtual isHandled : ()Z
            //   312: ifne -> 446
            //   315: ldc 'Robocol'
            //   317: ldc 'received command: %s(%d) %s'
            //   319: iconst_3
            //   320: anewarray java/lang/Object
            //   323: dup
            //   324: iconst_0
            //   325: aload #6
            //   327: invokevirtual getName : ()Ljava/lang/String;
            //   330: aastore
            //   331: dup
            //   332: iconst_1
            //   333: aload #6
            //   335: invokevirtual getSequenceNumber : ()I
            //   338: invokestatic valueOf : (I)Ljava/lang/Integer;
            //   341: aastore
            //   342: dup
            //   343: iconst_2
            //   344: aload #6
            //   346: invokevirtual getExtra : ()Ljava/lang/String;
            //   349: aastore
            //   350: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
            //   353: aload_0
            //   354: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/network/RecvLoopRunnable;
            //   357: getfield commandsToProcess : Ljava/util/concurrent/LinkedBlockingDeque;
            //   360: aload #6
            //   362: invokevirtual addLast : (Ljava/lang/Object;)V
            //   365: goto -> 446
            //   368: aload_0
            //   369: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/network/RecvLoopRunnable;
            //   372: getfield callback : Lorg/firstinspires/ftc/robotcore/internal/network/RecvLoopRunnable$RecvLoopCallback;
            //   375: aload #7
            //   377: lload_3
            //   378: invokeinterface heartbeatEvent : (Lcom/qualcomm/robotcore/robocol/RobocolDatagram;J)Lorg/firstinspires/ftc/robotcore/internal/network/CallbackResult;
            //   383: pop
            //   384: goto -> 446
            //   387: aload_0
            //   388: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/network/RecvLoopRunnable;
            //   391: getfield callback : Lorg/firstinspires/ftc/robotcore/internal/network/RecvLoopRunnable$RecvLoopCallback;
            //   394: aload #7
            //   396: invokeinterface peerDiscoveryEvent : (Lcom/qualcomm/robotcore/robocol/RobocolDatagram;)Lorg/firstinspires/ftc/robotcore/internal/network/CallbackResult;
            //   401: pop
            //   402: goto -> 446
            //   405: new java/lang/StringBuilder
            //   408: dup
            //   409: invokespecial <init> : ()V
            //   412: astore #6
            //   414: aload #6
            //   416: ldc 'Unhandled message type: '
            //   418: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   421: pop
            //   422: aload #6
            //   424: aload #7
            //   426: invokevirtual getMsgType : ()Lcom/qualcomm/robotcore/robocol/RobocolParsable$MsgType;
            //   429: invokevirtual name : ()Ljava/lang/String;
            //   432: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   435: pop
            //   436: ldc 'Robocol'
            //   438: aload #6
            //   440: invokevirtual toString : ()Ljava/lang/String;
            //   443: invokestatic ee : (Ljava/lang/String;Ljava/lang/String;)V
            //   446: aload_0
            //   447: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/network/RecvLoopRunnable;
            //   450: getfield packetProcessingTimer : Lcom/qualcomm/robotcore/util/ElapsedTime;
            //   453: invokevirtual seconds : ()D
            //   456: dstore_1
            //   457: dload_1
            //   458: aload_0
            //   459: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/network/RecvLoopRunnable;
            //   462: getfield sProcessingTimerReportingThreshold : D
            //   465: dcmpl
            //   466: ifle -> 554
            //   469: ldc 'Robocol'
            //   471: ldc 'packet processing took %.3f s: type=%s'
            //   473: iconst_2
            //   474: anewarray java/lang/Object
            //   477: dup
            //   478: iconst_0
            //   479: dload_1
            //   480: invokestatic valueOf : (D)Ljava/lang/Double;
            //   483: aastore
            //   484: dup
            //   485: iconst_1
            //   486: aload #7
            //   488: invokevirtual getMsgType : ()Lcom/qualcomm/robotcore/robocol/RobocolParsable$MsgType;
            //   491: invokevirtual toString : ()Ljava/lang/String;
            //   494: aastore
            //   495: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
            //   498: goto -> 554
            //   501: astore #6
            //   503: goto -> 575
            //   506: astore #6
            //   508: goto -> 513
            //   511: astore #6
            //   513: ldc 'Robocol'
            //   515: aload #6
            //   517: ldc 'exception in %s'
            //   519: iconst_1
            //   520: anewarray java/lang/Object
            //   523: dup
            //   524: iconst_0
            //   525: invokestatic currentThread : ()Ljava/lang/Thread;
            //   528: invokevirtual getName : ()Ljava/lang/String;
            //   531: aastore
            //   532: invokestatic ee : (Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V
            //   535: aload_0
            //   536: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/network/RecvLoopRunnable;
            //   539: getfield callback : Lorg/firstinspires/ftc/robotcore/internal/network/RecvLoopRunnable$RecvLoopCallback;
            //   542: aload #6
            //   544: invokevirtual getMessage : ()Ljava/lang/String;
            //   547: iconst_0
            //   548: invokeinterface reportGlobalError : (Ljava/lang/String;Z)Lorg/firstinspires/ftc/robotcore/internal/network/CallbackResult;
            //   553: pop
            //   554: aload #7
            //   556: invokevirtual close : ()V
            //   559: invokestatic access$100 : ()Z
            //   562: ifeq -> 11
            //   565: aload_0
            //   566: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/network/RecvLoopRunnable;
            //   569: invokevirtual calculateBytesPerMilli : ()V
            //   572: goto -> 11
            //   575: aload #7
            //   577: invokevirtual close : ()V
            //   580: aload #6
            //   582: athrow
            //   583: ldc 'Robocol'
            //   585: ldc_w 'interrupted; %s returning'
            //   588: iconst_1
            //   589: anewarray java/lang/Object
            //   592: dup
            //   593: iconst_0
            //   594: invokestatic currentThread : ()Ljava/lang/Thread;
            //   597: invokevirtual getName : ()Ljava/lang/String;
            //   600: aastore
            //   601: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
            //   604: return
            //   605: goto -> 405
            // Exception table:
            //   from	to	target	type
            //   153	236	511	com/qualcomm/robotcore/exception/RobotCoreException
            //   153	236	506	java/lang/RuntimeException
            //   153	236	501	finally
            //   236	251	511	com/qualcomm/robotcore/exception/RobotCoreException
            //   236	251	506	java/lang/RuntimeException
            //   236	251	501	finally
            //   254	269	511	com/qualcomm/robotcore/exception/RobotCoreException
            //   254	269	506	java/lang/RuntimeException
            //   254	269	501	finally
            //   272	287	511	com/qualcomm/robotcore/exception/RobotCoreException
            //   272	287	506	java/lang/RuntimeException
            //   272	287	501	finally
            //   290	365	511	com/qualcomm/robotcore/exception/RobotCoreException
            //   290	365	506	java/lang/RuntimeException
            //   290	365	501	finally
            //   368	384	511	com/qualcomm/robotcore/exception/RobotCoreException
            //   368	384	506	java/lang/RuntimeException
            //   368	384	501	finally
            //   387	402	511	com/qualcomm/robotcore/exception/RobotCoreException
            //   387	402	506	java/lang/RuntimeException
            //   387	402	501	finally
            //   405	446	511	com/qualcomm/robotcore/exception/RobotCoreException
            //   405	446	506	java/lang/RuntimeException
            //   405	446	501	finally
            //   446	498	511	com/qualcomm/robotcore/exception/RobotCoreException
            //   446	498	506	java/lang/RuntimeException
            //   446	498	501	finally
            //   513	554	501	finally
          }
        });
  }
  
  public void setCallback(RecvLoopCallback paramRecvLoopCallback) {
    this.callback = paramRecvLoopCallback;
  }
  
  public class CommandProcessor implements Runnable {
    public void run() {
      while (true) {
        if (!Thread.currentThread().isInterrupted())
          try {
            Command command = RecvLoopRunnable.this.commandsToProcess.takeFirst();
            RecvLoopRunnable.this.commandProcessingTimer.reset();
            if (RecvLoopRunnable.DEBUG)
              RobotLog.vv("Robocol", "command=%s...", new Object[] { command.getName() }); 
            RecvLoopRunnable.this.callback.commandEvent(command);
            if (RecvLoopRunnable.DEBUG)
              RobotLog.vv("Robocol", "...command=%s", new Object[] { command.getName() }); 
            double d = RecvLoopRunnable.this.commandProcessingTimer.seconds();
            if (d > RecvLoopRunnable.this.sProcessingTimerReportingThreshold)
              RobotLog.ee("Robocol", "command processing took %.3f s: command=%s", new Object[] { Double.valueOf(d), command.getName() }); 
            continue;
          } catch (InterruptedException interruptedException) {
            break;
          } catch (RobotCoreException robotCoreException) {
            RobotLog.ee("Robocol", (Throwable)robotCoreException, "exception in %s", new Object[] { Thread.currentThread().getName() });
            RecvLoopRunnable.this.callback.reportGlobalError(robotCoreException.getMessage(), false);
            continue;
          } catch (RuntimeException runtimeException) {
            RobotLog.ee("Robocol", runtimeException, "exception in %s", new Object[] { Thread.currentThread().getName() });
            RecvLoopRunnable.this.callback.reportGlobalError(runtimeException.getMessage(), false);
            continue;
          }  
        return;
      } 
    }
  }
  
  public static class DegenerateCallback implements RecvLoopCallback {
    public CallbackResult commandEvent(Command param1Command) throws RobotCoreException {
      return CallbackResult.NOT_HANDLED;
    }
    
    public CallbackResult emptyEvent(RobocolDatagram param1RobocolDatagram) throws RobotCoreException {
      return CallbackResult.NOT_HANDLED;
    }
    
    public CallbackResult gamepadEvent(RobocolDatagram param1RobocolDatagram) throws RobotCoreException {
      return CallbackResult.NOT_HANDLED;
    }
    
    public CallbackResult heartbeatEvent(RobocolDatagram param1RobocolDatagram, long param1Long) throws RobotCoreException {
      return CallbackResult.NOT_HANDLED;
    }
    
    public CallbackResult packetReceived(RobocolDatagram param1RobocolDatagram) throws RobotCoreException {
      return CallbackResult.NOT_HANDLED;
    }
    
    public CallbackResult peerDiscoveryEvent(RobocolDatagram param1RobocolDatagram) throws RobotCoreException {
      return CallbackResult.NOT_HANDLED;
    }
    
    public CallbackResult reportGlobalError(String param1String, boolean param1Boolean) {
      return CallbackResult.NOT_HANDLED;
    }
    
    public CallbackResult telemetryEvent(RobocolDatagram param1RobocolDatagram) throws RobotCoreException {
      return CallbackResult.NOT_HANDLED;
    }
  }
  
  public static interface RecvLoopCallback {
    CallbackResult commandEvent(Command param1Command) throws RobotCoreException;
    
    CallbackResult emptyEvent(RobocolDatagram param1RobocolDatagram) throws RobotCoreException;
    
    CallbackResult gamepadEvent(RobocolDatagram param1RobocolDatagram) throws RobotCoreException;
    
    CallbackResult heartbeatEvent(RobocolDatagram param1RobocolDatagram, long param1Long) throws RobotCoreException;
    
    CallbackResult packetReceived(RobocolDatagram param1RobocolDatagram) throws RobotCoreException;
    
    CallbackResult peerDiscoveryEvent(RobocolDatagram param1RobocolDatagram) throws RobotCoreException;
    
    CallbackResult reportGlobalError(String param1String, boolean param1Boolean);
    
    CallbackResult telemetryEvent(RobocolDatagram param1RobocolDatagram) throws RobotCoreException;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\RecvLoopRunnable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
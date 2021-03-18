package org.firstinspires.ftc.ftccommon.external;

import android.content.Context;
import com.qualcomm.ftccommon.R;
import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.robot.RobotState;
import com.qualcomm.robotcore.robot.RobotStatus;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.concurrent.atomic.AtomicInteger;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.internal.network.NetworkStatus;
import org.firstinspires.ftc.robotcore.internal.network.PeerStatus;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class SoundPlayingRobotMonitor implements RobotStateMonitor {
  public static boolean DEBUG = false;
  
  public static int soundConnect = R.raw.ss_r2d2_up;
  
  public static int soundDisconnect = R.raw.ss_bb8_down;
  
  public static int soundError;
  
  public static int soundRunning = R.raw.ss_light_speed;
  
  public static int soundWarning = R.raw.ss_mine;
  
  protected Context context;
  
  protected String errorMessage = null;
  
  protected Sound lastSoundPlayed = Sound.None;
  
  protected NetworkStatus networkStatus = NetworkStatus.UNKNOWN;
  
  protected PeerStatus peerStatus = PeerStatus.UNKNOWN;
  
  protected RobotState robotState = RobotState.UNKNOWN;
  
  protected RobotStatus robotStatus = RobotStatus.UNKNOWN;
  
  protected AtomicInteger runningsInFlight = new AtomicInteger(0);
  
  protected String warningMessage = null;
  
  static {
    soundError = R.raw.ss_mf_fail;
  }
  
  public SoundPlayingRobotMonitor() {
    this((Context)AppUtil.getInstance().getApplication());
  }
  
  public SoundPlayingRobotMonitor(Context paramContext) {
    this.context = paramContext;
  }
  
  public static void prefillSoundCache() {
    SoundPlayer.getInstance().prefillSoundCache(new int[] { soundConnect, soundDisconnect, soundRunning, soundWarning, soundError });
  }
  
  protected void playConnect() {
    if (!SoundPlayer.getInstance().isLocalSoundOn() && this.lastSoundPlayed == Sound.Running && this.runningsInFlight.get() == 0) {
      RobotLog.vv("SoundPlayer", "playing running again");
      playRunning();
    } 
    playSound(Sound.Connect, soundConnect);
  }
  
  protected void playDisconnect() {
    playSound(Sound.Disconnect, soundDisconnect);
  }
  
  protected void playError() {
    playSound(Sound.Error, soundError);
  }
  
  protected void playRunning() {
    this.runningsInFlight.getAndIncrement();
    playSound(Sound.Running, soundRunning, new Consumer<Integer>() {
          public void accept(Integer param1Integer) {
            SoundPlayingRobotMonitor.this.runningsInFlight.decrementAndGet();
          }
        },  null);
  }
  
  protected void playSound(Sound paramSound, int paramInt) {
    playSound(paramSound, paramInt, null, null);
  }
  
  protected void playSound(Sound paramSound, int paramInt, Consumer<Integer> paramConsumer, Runnable paramRunnable) {
    this.lastSoundPlayed = paramSound;
    SoundPlayer.getInstance().startPlaying(this.context, paramInt, new SoundPlayer.PlaySoundParams(true), paramConsumer, paramRunnable);
  }
  
  protected void playWarning() {
    playSound(Sound.Warning, soundWarning);
  }
  
  public void updateErrorMessage(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: ifnull -> 34
    //   6: aload_1
    //   7: aload_0
    //   8: getfield errorMessage : Ljava/lang/String;
    //   11: invokevirtual equals : (Ljava/lang/Object;)Z
    //   14: ifne -> 34
    //   17: getstatic org/firstinspires/ftc/ftccommon/external/SoundPlayingRobotMonitor.DEBUG : Z
    //   20: ifeq -> 30
    //   23: ldc 'SoundPlayer'
    //   25: ldc 'updateErrorMessage()'
    //   27: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
    //   30: aload_0
    //   31: invokevirtual playError : ()V
    //   34: aload_0
    //   35: aload_1
    //   36: putfield errorMessage : Ljava/lang/String;
    //   39: aload_0
    //   40: monitorexit
    //   41: return
    //   42: astore_1
    //   43: aload_0
    //   44: monitorexit
    //   45: aload_1
    //   46: athrow
    // Exception table:
    //   from	to	target	type
    //   6	30	42	finally
    //   30	34	42	finally
    //   34	39	42	finally
  }
  
  public void updateNetworkStatus(NetworkStatus paramNetworkStatus, String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: aload_0
    //   4: getfield networkStatus : Lorg/firstinspires/ftc/robotcore/internal/network/NetworkStatus;
    //   7: if_acmpeq -> 43
    //   10: getstatic org/firstinspires/ftc/ftccommon/external/SoundPlayingRobotMonitor.DEBUG : Z
    //   13: ifeq -> 34
    //   16: ldc 'SoundPlayer'
    //   18: ldc 'updateNetworkStatus(%s)'
    //   20: iconst_1
    //   21: anewarray java/lang/Object
    //   24: dup
    //   25: iconst_0
    //   26: aload_1
    //   27: invokevirtual toString : ()Ljava/lang/String;
    //   30: aastore
    //   31: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   34: getstatic org/firstinspires/ftc/ftccommon/external/SoundPlayingRobotMonitor$2.$SwitchMap$org$firstinspires$ftc$robotcore$internal$network$NetworkStatus : [I
    //   37: aload_1
    //   38: invokevirtual ordinal : ()I
    //   41: iaload
    //   42: istore_3
    //   43: aload_0
    //   44: aload_1
    //   45: putfield networkStatus : Lorg/firstinspires/ftc/robotcore/internal/network/NetworkStatus;
    //   48: aload_0
    //   49: monitorexit
    //   50: return
    //   51: astore_1
    //   52: aload_0
    //   53: monitorexit
    //   54: aload_1
    //   55: athrow
    // Exception table:
    //   from	to	target	type
    //   2	34	51	finally
    //   34	43	51	finally
    //   43	48	51	finally
  }
  
  public void updatePeerStatus(PeerStatus paramPeerStatus) {
    if (paramPeerStatus != this.peerStatus) {
      if (DEBUG)
        RobotLog.vv("SoundPlayer", "updatePeerStatus(%s)", new Object[] { paramPeerStatus.toString() }); 
      int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$internal$network$PeerStatus[paramPeerStatus.ordinal()];
      if (i != 2) {
        if (i == 3 && this.peerStatus != PeerStatus.DISCONNECTED)
          playDisconnect(); 
      } else if (this.peerStatus != PeerStatus.CONNECTED) {
        playConnect();
      } 
    } 
    this.peerStatus = paramPeerStatus;
  }
  
  public void updateRobotState(RobotState paramRobotState) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: aload_0
    //   4: getfield robotState : Lcom/qualcomm/robotcore/robot/RobotState;
    //   7: if_acmpeq -> 54
    //   10: getstatic org/firstinspires/ftc/ftccommon/external/SoundPlayingRobotMonitor.DEBUG : Z
    //   13: ifeq -> 35
    //   16: ldc 'SoundPlayer'
    //   18: ldc_w 'updateRobotState(%s)'
    //   21: iconst_1
    //   22: anewarray java/lang/Object
    //   25: dup
    //   26: iconst_0
    //   27: aload_1
    //   28: invokevirtual toString : ()Ljava/lang/String;
    //   31: aastore
    //   32: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   35: getstatic org/firstinspires/ftc/ftccommon/external/SoundPlayingRobotMonitor$2.$SwitchMap$com$qualcomm$robotcore$robot$RobotState : [I
    //   38: aload_1
    //   39: invokevirtual ordinal : ()I
    //   42: iaload
    //   43: iconst_5
    //   44: if_icmpeq -> 50
    //   47: goto -> 54
    //   50: aload_0
    //   51: invokevirtual playRunning : ()V
    //   54: aload_0
    //   55: aload_1
    //   56: putfield robotState : Lcom/qualcomm/robotcore/robot/RobotState;
    //   59: aload_0
    //   60: monitorexit
    //   61: return
    //   62: astore_1
    //   63: aload_0
    //   64: monitorexit
    //   65: aload_1
    //   66: athrow
    // Exception table:
    //   from	to	target	type
    //   2	35	62	finally
    //   35	47	62	finally
    //   50	54	62	finally
    //   54	59	62	finally
  }
  
  public void updateRobotStatus(RobotStatus paramRobotStatus) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: aload_0
    //   4: getfield robotStatus : Lcom/qualcomm/robotcore/robot/RobotStatus;
    //   7: if_acmpeq -> 44
    //   10: getstatic org/firstinspires/ftc/ftccommon/external/SoundPlayingRobotMonitor.DEBUG : Z
    //   13: ifeq -> 35
    //   16: ldc 'SoundPlayer'
    //   18: ldc_w 'updateRobotStatus(%s)'
    //   21: iconst_1
    //   22: anewarray java/lang/Object
    //   25: dup
    //   26: iconst_0
    //   27: aload_1
    //   28: invokevirtual toString : ()Ljava/lang/String;
    //   31: aastore
    //   32: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   35: getstatic org/firstinspires/ftc/ftccommon/external/SoundPlayingRobotMonitor$2.$SwitchMap$com$qualcomm$robotcore$robot$RobotStatus : [I
    //   38: aload_1
    //   39: invokevirtual ordinal : ()I
    //   42: iaload
    //   43: istore_2
    //   44: aload_0
    //   45: aload_1
    //   46: putfield robotStatus : Lcom/qualcomm/robotcore/robot/RobotStatus;
    //   49: aload_0
    //   50: monitorexit
    //   51: return
    //   52: astore_1
    //   53: aload_0
    //   54: monitorexit
    //   55: aload_1
    //   56: athrow
    // Exception table:
    //   from	to	target	type
    //   2	35	52	finally
    //   35	44	52	finally
    //   44	49	52	finally
  }
  
  public void updateWarningMessage(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: ifnull -> 35
    //   6: aload_1
    //   7: aload_0
    //   8: getfield warningMessage : Ljava/lang/String;
    //   11: invokevirtual equals : (Ljava/lang/Object;)Z
    //   14: ifne -> 35
    //   17: getstatic org/firstinspires/ftc/ftccommon/external/SoundPlayingRobotMonitor.DEBUG : Z
    //   20: ifeq -> 31
    //   23: ldc 'SoundPlayer'
    //   25: ldc_w 'updateWarningMessage()'
    //   28: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
    //   31: aload_0
    //   32: invokevirtual playWarning : ()V
    //   35: aload_0
    //   36: aload_1
    //   37: putfield warningMessage : Ljava/lang/String;
    //   40: aload_0
    //   41: monitorexit
    //   42: return
    //   43: astore_1
    //   44: aload_0
    //   45: monitorexit
    //   46: aload_1
    //   47: athrow
    // Exception table:
    //   from	to	target	type
    //   6	31	43	finally
    //   31	35	43	finally
    //   35	40	43	finally
  }
  
  protected enum Sound {
    Connect, Disconnect, Error, None, Running, Warning;
    
    static {
      Sound sound = new Sound("Error", 5);
      Error = sound;
      $VALUES = new Sound[] { None, Connect, Disconnect, Running, Warning, sound };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\ftccommon\external\SoundPlayingRobotMonitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
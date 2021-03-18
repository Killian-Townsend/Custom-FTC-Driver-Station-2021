package org.firstinspires.ftc.robotcore.external.stream;

import android.graphics.Bitmap;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier;
import com.qualcomm.robotcore.robocol.Command;
import java.io.ByteArrayOutputStream;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.network.RobotCoreCommandList;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;

public class CameraStreamServer implements OpModeManagerNotifier.Notifications {
  public static final int CHUNK_SIZE = 4096;
  
  private static final int DEFAULT_JPEG_QUALITY = 75;
  
  private static final CameraStreamServer INSTANCE = new CameraStreamServer();
  
  private static int frameNum;
  
  private int jpegQuality = 75;
  
  private OpModeManagerImpl opModeManager;
  
  private CameraStreamSource source;
  
  public static CameraStreamServer getInstance() {
    return INSTANCE;
  }
  
  private void sendFrame(Bitmap paramBitmap) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    paramBitmap.compress(Bitmap.CompressFormat.JPEG, this.jpegQuality, byteArrayOutputStream);
    byte[] arrayOfByte = byteArrayOutputStream.toByteArray();
    NetworkConnectionHandler networkConnectionHandler = NetworkConnectionHandler.getInstance();
    networkConnectionHandler.sendCommand(new Command("CMD_RECEIVE_FRAME_BEGIN", (new RobotCoreCommandList.CmdReceiveFrameBegin(frameNum, arrayOfByte.length)).serialize()));
    for (int i = 0; i < Math.ceil(arrayOfByte.length / 4096.0D); i++) {
      int j = i * 4096;
      int k = Math.min(4096, arrayOfByte.length - j);
      networkConnectionHandler.sendCommand(new Command("CMD_RECEIVE_FRAME_CHUNK", (new RobotCoreCommandList.CmdReceiveFrameChunk(frameNum, i, arrayOfByte, j, k)).serialize()));
    } 
    frameNum++;
  }
  
  public int getJpegQuality() {
    return this.jpegQuality;
  }
  
  public CallbackResult handleRequestFrame() {
    // Byte code:
    //   0: aload_0
    //   1: getfield source : Lorg/firstinspires/ftc/robotcore/external/stream/CameraStreamSource;
    //   4: ifnull -> 39
    //   7: aload_0
    //   8: monitorenter
    //   9: aload_0
    //   10: getfield source : Lorg/firstinspires/ftc/robotcore/external/stream/CameraStreamSource;
    //   13: new org/firstinspires/ftc/robotcore/external/stream/CameraStreamServer$1
    //   16: dup
    //   17: aload_0
    //   18: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/external/stream/CameraStreamServer;)V
    //   21: invokestatic createTrivial : (Ljava/lang/Object;)Lorg/firstinspires/ftc/robotcore/external/function/Continuation;
    //   24: invokeinterface getFrameBitmap : (Lorg/firstinspires/ftc/robotcore/external/function/Continuation;)V
    //   29: aload_0
    //   30: monitorexit
    //   31: goto -> 39
    //   34: astore_1
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_1
    //   38: athrow
    //   39: getstatic org/firstinspires/ftc/robotcore/internal/network/CallbackResult.HANDLED : Lorg/firstinspires/ftc/robotcore/internal/network/CallbackResult;
    //   42: areturn
    // Exception table:
    //   from	to	target	type
    //   9	31	34	finally
    //   35	37	34	finally
  }
  
  public void onOpModePostStop(OpMode paramOpMode) {
    setSource(null);
    OpModeManagerImpl opModeManagerImpl = this.opModeManager;
    if (opModeManagerImpl != null) {
      opModeManagerImpl.unregisterListener(this);
      this.opModeManager = null;
    } 
  }
  
  public void onOpModePreInit(OpMode paramOpMode) {}
  
  public void onOpModePreStart(OpMode paramOpMode) {}
  
  public void setJpegQuality(int paramInt) {
    this.jpegQuality = paramInt;
  }
  
  public void setSource(CameraStreamSource paramCameraStreamSource) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield source : Lorg/firstinspires/ftc/robotcore/external/stream/CameraStreamSource;
    //   7: new org/firstinspires/ftc/robotcore/internal/network/RobotCoreCommandList$CmdStreamChange
    //   10: dup
    //   11: invokespecial <init> : ()V
    //   14: astore_3
    //   15: aload_1
    //   16: ifnull -> 85
    //   19: iconst_1
    //   20: istore_2
    //   21: goto -> 24
    //   24: aload_3
    //   25: iload_2
    //   26: putfield available : Z
    //   29: invokestatic getInstance : ()Lorg/firstinspires/ftc/robotcore/internal/network/NetworkConnectionHandler;
    //   32: new com/qualcomm/robotcore/robocol/Command
    //   35: dup
    //   36: ldc 'CMD_STREAM_CHANGE'
    //   38: aload_3
    //   39: invokevirtual serialize : ()Ljava/lang/String;
    //   42: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;)V
    //   45: invokevirtual sendCommand : (Lcom/qualcomm/robotcore/robocol/Command;)V
    //   48: aload_1
    //   49: ifnull -> 77
    //   52: invokestatic getInstance : ()Lorg/firstinspires/ftc/robotcore/internal/system/AppUtil;
    //   55: invokevirtual getActivity : ()Landroid/app/Activity;
    //   58: invokestatic getOpModeManagerOfActivity : (Landroid/app/Activity;)Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl;
    //   61: astore_1
    //   62: aload_0
    //   63: aload_1
    //   64: putfield opModeManager : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl;
    //   67: aload_1
    //   68: ifnull -> 77
    //   71: aload_1
    //   72: aload_0
    //   73: invokevirtual registerListener : (Lcom/qualcomm/robotcore/eventloop/opmode/OpModeManagerNotifier$Notifications;)Lcom/qualcomm/robotcore/eventloop/opmode/OpMode;
    //   76: pop
    //   77: aload_0
    //   78: monitorexit
    //   79: return
    //   80: astore_1
    //   81: aload_0
    //   82: monitorexit
    //   83: aload_1
    //   84: athrow
    //   85: iconst_0
    //   86: istore_2
    //   87: goto -> 24
    // Exception table:
    //   from	to	target	type
    //   2	15	80	finally
    //   24	48	80	finally
    //   52	67	80	finally
    //   71	77	80	finally
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\stream\CameraStreamServer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
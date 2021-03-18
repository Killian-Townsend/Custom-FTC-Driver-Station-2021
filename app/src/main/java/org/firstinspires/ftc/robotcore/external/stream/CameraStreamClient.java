package org.firstinspires.ftc.robotcore.external.stream;

import android.graphics.Bitmap;
import java.util.SortedMap;
import java.util.TreeMap;
import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
import org.firstinspires.ftc.robotcore.internal.network.RobotCoreCommandList;

public class CameraStreamClient {
  private static final CameraStreamClient INSTANCE = new CameraStreamClient();
  
  private static final int MAX_CONCURRENT_FRAMES = 5;
  
  private boolean available;
  
  private Listener listener;
  
  private SortedMap<Integer, PartialFrame> partialFrames = new TreeMap<Integer, PartialFrame>();
  
  public static CameraStreamClient getInstance() {
    return INSTANCE;
  }
  
  public CallbackResult handleReceiveFrameBegin(String paramString) {
    RobotCoreCommandList.CmdReceiveFrameBegin cmdReceiveFrameBegin = RobotCoreCommandList.CmdReceiveFrameBegin.deserialize(paramString);
    PartialFrame partialFrame = new PartialFrame();
    PartialFrame.access$102(partialFrame, cmdReceiveFrameBegin.getLength());
    PartialFrame.access$202(partialFrame, new byte[cmdReceiveFrameBegin.getLength()]);
    this.partialFrames.put(Integer.valueOf(cmdReceiveFrameBegin.getFrameNum()), partialFrame);
    if (this.partialFrames.size() > 5) {
      SortedMap<Integer, PartialFrame> sortedMap = this.partialFrames;
      sortedMap.remove(sortedMap.firstKey());
    } 
    return CallbackResult.HANDLED;
  }
  
  public CallbackResult handleReceiveFrameChunk(String paramString) {
    // Byte code:
    //   0: aload_1
    //   1: invokestatic deserialize : (Ljava/lang/String;)Lorg/firstinspires/ftc/robotcore/internal/network/RobotCoreCommandList$CmdReceiveFrameChunk;
    //   4: astore_1
    //   5: aload_0
    //   6: getfield partialFrames : Ljava/util/SortedMap;
    //   9: aload_1
    //   10: invokevirtual getFrameNum : ()I
    //   13: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   16: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   21: ifne -> 28
    //   24: getstatic org/firstinspires/ftc/robotcore/internal/network/CallbackResult.HANDLED : Lorg/firstinspires/ftc/robotcore/internal/network/CallbackResult;
    //   27: areturn
    //   28: aload_0
    //   29: getfield partialFrames : Ljava/util/SortedMap;
    //   32: aload_1
    //   33: invokevirtual getFrameNum : ()I
    //   36: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   39: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   44: checkcast org/firstinspires/ftc/robotcore/external/stream/CameraStreamClient$PartialFrame
    //   47: astore_3
    //   48: aload_1
    //   49: invokevirtual getData : ()[B
    //   52: arraylength
    //   53: istore_2
    //   54: aload_1
    //   55: invokevirtual getData : ()[B
    //   58: iconst_0
    //   59: aload_3
    //   60: invokestatic access$200 : (Lorg/firstinspires/ftc/robotcore/external/stream/CameraStreamClient$PartialFrame;)[B
    //   63: aload_1
    //   64: invokevirtual getChunkNum : ()I
    //   67: sipush #4096
    //   70: imul
    //   71: iload_2
    //   72: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   75: aload_3
    //   76: aload_3
    //   77: invokestatic access$300 : (Lorg/firstinspires/ftc/robotcore/external/stream/CameraStreamClient$PartialFrame;)I
    //   80: iload_2
    //   81: iadd
    //   82: invokestatic access$302 : (Lorg/firstinspires/ftc/robotcore/external/stream/CameraStreamClient$PartialFrame;I)I
    //   85: pop
    //   86: aload_3
    //   87: invokestatic access$300 : (Lorg/firstinspires/ftc/robotcore/external/stream/CameraStreamClient$PartialFrame;)I
    //   90: aload_3
    //   91: invokestatic access$100 : (Lorg/firstinspires/ftc/robotcore/external/stream/CameraStreamClient$PartialFrame;)I
    //   94: if_icmpne -> 195
    //   97: aload_0
    //   98: getfield partialFrames : Ljava/util/SortedMap;
    //   101: aload_1
    //   102: invokevirtual getFrameNum : ()I
    //   105: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   108: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   113: pop
    //   114: aload_0
    //   115: getfield partialFrames : Ljava/util/SortedMap;
    //   118: aload_1
    //   119: invokevirtual getFrameNum : ()I
    //   122: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   125: invokeinterface headMap : (Ljava/lang/Object;)Ljava/util/SortedMap;
    //   130: invokeinterface clear : ()V
    //   135: aload_3
    //   136: invokestatic access$200 : (Lorg/firstinspires/ftc/robotcore/external/stream/CameraStreamClient$PartialFrame;)[B
    //   139: iconst_0
    //   140: aload_3
    //   141: invokestatic access$100 : (Lorg/firstinspires/ftc/robotcore/external/stream/CameraStreamClient$PartialFrame;)I
    //   144: invokestatic decodeByteArray : ([BII)Landroid/graphics/Bitmap;
    //   147: astore_1
    //   148: aload_1
    //   149: ifnonnull -> 161
    //   152: ldc 'Received invalid frame bitmap'
    //   154: invokestatic e : (Ljava/lang/String;)V
    //   157: getstatic org/firstinspires/ftc/robotcore/internal/network/CallbackResult.HANDLED : Lorg/firstinspires/ftc/robotcore/internal/network/CallbackResult;
    //   160: areturn
    //   161: aload_0
    //   162: monitorenter
    //   163: aload_0
    //   164: getfield listener : Lorg/firstinspires/ftc/robotcore/external/stream/CameraStreamClient$Listener;
    //   167: ifnull -> 180
    //   170: aload_0
    //   171: getfield listener : Lorg/firstinspires/ftc/robotcore/external/stream/CameraStreamClient$Listener;
    //   174: aload_1
    //   175: invokeinterface onFrameBitmap : (Landroid/graphics/Bitmap;)V
    //   180: aload_0
    //   181: monitorexit
    //   182: goto -> 195
    //   185: astore_1
    //   186: aload_0
    //   187: monitorexit
    //   188: aload_1
    //   189: athrow
    //   190: ldc 'Received too many frame bytes'
    //   192: invokestatic e : (Ljava/lang/String;)V
    //   195: getstatic org/firstinspires/ftc/robotcore/internal/network/CallbackResult.HANDLED : Lorg/firstinspires/ftc/robotcore/internal/network/CallbackResult;
    //   198: areturn
    //   199: astore_1
    //   200: goto -> 190
    // Exception table:
    //   from	to	target	type
    //   0	28	199	java/lang/ArrayIndexOutOfBoundsException
    //   28	148	199	java/lang/ArrayIndexOutOfBoundsException
    //   152	161	199	java/lang/ArrayIndexOutOfBoundsException
    //   161	163	199	java/lang/ArrayIndexOutOfBoundsException
    //   163	180	185	finally
    //   180	182	185	finally
    //   186	188	185	finally
    //   188	190	199	java/lang/ArrayIndexOutOfBoundsException
  }
  
  public CallbackResult handleStreamChange(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokestatic deserialize : (Ljava/lang/String;)Lorg/firstinspires/ftc/robotcore/internal/network/RobotCoreCommandList$CmdStreamChange;
    //   5: getfield available : Z
    //   8: putfield available : Z
    //   11: aload_0
    //   12: monitorenter
    //   13: aload_0
    //   14: getfield listener : Lorg/firstinspires/ftc/robotcore/external/stream/CameraStreamClient$Listener;
    //   17: ifnull -> 33
    //   20: aload_0
    //   21: getfield listener : Lorg/firstinspires/ftc/robotcore/external/stream/CameraStreamClient$Listener;
    //   24: aload_0
    //   25: getfield available : Z
    //   28: invokeinterface onStreamAvailableChange : (Z)V
    //   33: aload_0
    //   34: monitorexit
    //   35: getstatic org/firstinspires/ftc/robotcore/internal/network/CallbackResult.HANDLED : Lorg/firstinspires/ftc/robotcore/internal/network/CallbackResult;
    //   38: areturn
    //   39: astore_1
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_1
    //   43: athrow
    // Exception table:
    //   from	to	target	type
    //   13	33	39	finally
    //   33	35	39	finally
    //   40	42	39	finally
  }
  
  public boolean isStreamAvailable() {
    return this.available;
  }
  
  public void setListener(Listener paramListener) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield listener : Lorg/firstinspires/ftc/robotcore/external/stream/CameraStreamClient$Listener;
    //   7: aload_0
    //   8: monitorexit
    //   9: return
    //   10: astore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: aload_1
    //   14: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	10	finally
  }
  
  public static interface Listener {
    void onFrameBitmap(Bitmap param1Bitmap);
    
    void onStreamAvailableChange(boolean param1Boolean);
  }
  
  private static class PartialFrame {
    private int bytesRead;
    
    private byte[] data;
    
    private int length;
    
    private PartialFrame() {}
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\stream\CameraStreamClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
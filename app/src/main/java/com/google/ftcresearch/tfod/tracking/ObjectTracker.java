package com.google.ftcresearch.tfod.tracking;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;
import com.google.ftcresearch.tfod.util.Size;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.microedition.khronos.opengles.GL10;

public class ObjectTracker {
  private static final int DOWNSAMPLE_FACTOR = 2;
  
  private static final boolean DRAW_TEXT = false;
  
  private static final int MAX_DEBUG_HISTORY_SIZE = 100;
  
  private static final int MAX_FRAME_HISTORY_SIZE = 200;
  
  private static final String TAG = "ObjectTracker";
  
  protected static ObjectTracker instance;
  
  private static boolean libraryFound = false;
  
  protected final boolean alwaysTrack;
  
  private final Vector<PointF> debugHistory;
  
  private final byte[] downsampledFrame;
  
  private long downsampledTimestamp;
  
  protected final int frameHeight;
  
  protected final int frameWidth;
  
  private FrameChange lastKeypoints;
  
  private long lastTimestamp;
  
  private final float[] matrixValues = new float[9];
  
  private long nativeObjectTracker;
  
  private final int rowStride;
  
  private final LinkedList<TimestampedDeltas> timestampedDeltas;
  
  private final Map<String, TrackedObject> trackedObjects;
  
  static {
    try {
      System.loadLibrary("object_tracking");
      libraryFound = true;
      return;
    } catch (UnsatisfiedLinkError unsatisfiedLinkError) {
      Log.e("ObjectTracker", "libtensorflow_demo.so not found, tracking unavailable");
      return;
    } 
  }
  
  protected ObjectTracker(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {
    this.frameWidth = paramInt1;
    this.frameHeight = paramInt2;
    this.rowStride = paramInt3;
    this.alwaysTrack = paramBoolean;
    this.timestampedDeltas = new LinkedList<TimestampedDeltas>();
    this.trackedObjects = new HashMap<String, TrackedObject>();
    this.debugHistory = new Vector<PointF>(100);
    paramInt1 = paramInt1 + 2 - 1;
    this.downsampledFrame = new byte[paramInt1 / 2 * paramInt1 / 2];
  }
  
  public static void clearInstance() {
    // Byte code:
    //   0: ldc com/google/ftcresearch/tfod/tracking/ObjectTracker
    //   2: monitorenter
    //   3: getstatic com/google/ftcresearch/tfod/tracking/ObjectTracker.instance : Lcom/google/ftcresearch/tfod/tracking/ObjectTracker;
    //   6: ifnull -> 15
    //   9: getstatic com/google/ftcresearch/tfod/tracking/ObjectTracker.instance : Lcom/google/ftcresearch/tfod/tracking/ObjectTracker;
    //   12: invokevirtual release : ()V
    //   15: ldc com/google/ftcresearch/tfod/tracking/ObjectTracker
    //   17: monitorexit
    //   18: return
    //   19: astore_0
    //   20: ldc com/google/ftcresearch/tfod/tracking/ObjectTracker
    //   22: monitorexit
    //   23: aload_0
    //   24: athrow
    // Exception table:
    //   from	to	target	type
    //   3	15	19	finally
  }
  
  protected static native void downsampleImageNative(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfbyte1, int paramInt4, byte[] paramArrayOfbyte2);
  
  private RectF downscaleRect(RectF paramRectF) {
    return new RectF(paramRectF.left / 2.0F, paramRectF.top / 2.0F, paramRectF.right / 2.0F, paramRectF.bottom / 2.0F);
  }
  
  private void drawHistoryDebug(Canvas paramCanvas) {
    drawHistoryPoint(paramCanvas, (this.frameWidth / 2 * 2 / 2), (this.frameHeight / 2 * 2 / 2));
  }
  
  private void drawHistoryPoint(Canvas paramCanvas, float paramFloat1, float paramFloat2) {
    Paint paint = new Paint();
    paint.setAntiAlias(false);
    paint.setTypeface(Typeface.SERIF);
    paint.setColor(-65536);
    paint.setStrokeWidth(2.0F);
    paint.setColor(-16711936);
    paramCanvas.drawCircle(paramFloat1, paramFloat2, 3.0F, paint);
    paint.setColor(-65536);
    synchronized (this.debugHistory) {
      int j = this.debugHistory.size();
      int i = 0;
      while (i < j) {
        PointF pointF = this.debugHistory.get(j - i - 1);
        float f1 = paramFloat1 + pointF.x;
        float f2 = pointF.y + paramFloat2;
        paramCanvas.drawLine(paramFloat1, paramFloat2, f1, f2, paint);
        i++;
        paramFloat2 = f2;
        paramFloat1 = f1;
      } 
      return;
    } 
  }
  
  private void drawKeypointsDebug(Canvas paramCanvas) {
    Paint paint = new Paint();
    FrameChange frameChange = this.lastKeypoints;
    if (frameChange == null)
      return; 
    float f1 = frameChange.minScore;
    float f2 = this.lastKeypoints.maxScore;
    for (PointChange pointChange : this.lastKeypoints.pointDeltas) {
      if (pointChange.wasFound) {
        float f3 = pointChange.keypointA.score;
        float f4 = f2 - f1;
        paint.setColor(floatToChar((f3 - f1) / f4) << 16 | 0xFF000000 | floatToChar(1.0F - (pointChange.keypointA.score - f1) / f4));
        float[] arrayOfFloat1 = new float[4];
        arrayOfFloat1[0] = pointChange.keypointA.x;
        arrayOfFloat1[1] = pointChange.keypointA.y;
        arrayOfFloat1[2] = pointChange.keypointB.x;
        arrayOfFloat1[3] = pointChange.keypointB.y;
        paramCanvas.drawRect(arrayOfFloat1[2] - 3.0F, arrayOfFloat1[3] - 3.0F, arrayOfFloat1[2] + 3.0F, arrayOfFloat1[3] + 3.0F, paint);
        paint.setColor(-16711681);
        paramCanvas.drawLine(arrayOfFloat1[2], arrayOfFloat1[3], arrayOfFloat1[0], arrayOfFloat1[1], paint);
        continue;
      } 
      paint.setColor(-256);
      float[] arrayOfFloat = new float[2];
      arrayOfFloat[0] = pointChange.keypointA.x;
      arrayOfFloat[1] = pointChange.keypointA.y;
      paramCanvas.drawCircle(arrayOfFloat[0], arrayOfFloat[1], 5.0F, paint);
    } 
  }
  
  private static int floatToChar(float paramFloat) {
    return Math.max(0, Math.min((int)(paramFloat * 255.999F), 255));
  }
  
  private PointF getAccumulatedDelta(long paramLong, float paramFloat1, float paramFloat2, float paramFloat3) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: lload_1
    //   4: new android/graphics/RectF
    //   7: dup
    //   8: fload_3
    //   9: fload #5
    //   11: fsub
    //   12: fload #4
    //   14: fload #5
    //   16: fsub
    //   17: fload_3
    //   18: fload #5
    //   20: fadd
    //   21: fload #5
    //   23: fload #4
    //   25: fadd
    //   26: invokespecial <init> : (FFFF)V
    //   29: invokespecial getCurrentPosition : (JLandroid/graphics/RectF;)Landroid/graphics/RectF;
    //   32: astore #6
    //   34: new android/graphics/PointF
    //   37: dup
    //   38: aload #6
    //   40: invokevirtual centerX : ()F
    //   43: fload_3
    //   44: fsub
    //   45: aload #6
    //   47: invokevirtual centerY : ()F
    //   50: fload #4
    //   52: fsub
    //   53: invokespecial <init> : (FF)V
    //   56: astore #6
    //   58: aload_0
    //   59: monitorexit
    //   60: aload #6
    //   62: areturn
    //   63: astore #6
    //   65: aload_0
    //   66: monitorexit
    //   67: aload #6
    //   69: athrow
    // Exception table:
    //   from	to	target	type
    //   2	58	63	finally
  }
  
  private RectF getCurrentPosition(long paramLong, RectF paramRectF) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_3
    //   4: invokespecial downscaleRect : (Landroid/graphics/RectF;)Landroid/graphics/RectF;
    //   7: astore_3
    //   8: iconst_4
    //   9: newarray float
    //   11: astore #4
    //   13: aload_0
    //   14: lload_1
    //   15: aload_3
    //   16: getfield left : F
    //   19: aload_3
    //   20: getfield top : F
    //   23: aload_3
    //   24: getfield right : F
    //   27: aload_3
    //   28: getfield bottom : F
    //   31: aload #4
    //   33: invokevirtual getCurrentPositionNative : (JFFFF[F)V
    //   36: aload_0
    //   37: new android/graphics/RectF
    //   40: dup
    //   41: aload #4
    //   43: iconst_0
    //   44: faload
    //   45: aload #4
    //   47: iconst_1
    //   48: faload
    //   49: aload #4
    //   51: iconst_2
    //   52: faload
    //   53: aload #4
    //   55: iconst_3
    //   56: faload
    //   57: invokespecial <init> : (FFFF)V
    //   60: invokespecial upscaleRect : (Landroid/graphics/RectF;)Landroid/graphics/RectF;
    //   63: astore_3
    //   64: aload_0
    //   65: monitorexit
    //   66: aload_3
    //   67: areturn
    //   68: astore_3
    //   69: aload_0
    //   70: monitorexit
    //   71: aload_3
    //   72: athrow
    // Exception table:
    //   from	to	target	type
    //   2	64	68	finally
  }
  
  public static ObjectTracker getInstance(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {
    // Byte code:
    //   0: ldc com/google/ftcresearch/tfod/tracking/ObjectTracker
    //   2: monitorenter
    //   3: getstatic com/google/ftcresearch/tfod/tracking/ObjectTracker.libraryFound : Z
    //   6: ifne -> 23
    //   9: ldc 'ObjectTracker'
    //   11: ldc_w 'Native object tracking support not found. See tensorflow/examples/android/README.md for details.'
    //   14: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   17: pop
    //   18: ldc com/google/ftcresearch/tfod/tracking/ObjectTracker
    //   20: monitorexit
    //   21: aconst_null
    //   22: areturn
    //   23: getstatic com/google/ftcresearch/tfod/tracking/ObjectTracker.instance : Lcom/google/ftcresearch/tfod/tracking/ObjectTracker;
    //   26: ifnonnull -> 63
    //   29: new com/google/ftcresearch/tfod/tracking/ObjectTracker
    //   32: dup
    //   33: iload_0
    //   34: iload_1
    //   35: iload_2
    //   36: iload_3
    //   37: invokespecial <init> : (IIIZ)V
    //   40: astore #4
    //   42: aload #4
    //   44: putstatic com/google/ftcresearch/tfod/tracking/ObjectTracker.instance : Lcom/google/ftcresearch/tfod/tracking/ObjectTracker;
    //   47: aload #4
    //   49: invokevirtual init : ()V
    //   52: getstatic com/google/ftcresearch/tfod/tracking/ObjectTracker.instance : Lcom/google/ftcresearch/tfod/tracking/ObjectTracker;
    //   55: astore #4
    //   57: ldc com/google/ftcresearch/tfod/tracking/ObjectTracker
    //   59: monitorexit
    //   60: aload #4
    //   62: areturn
    //   63: new java/lang/RuntimeException
    //   66: dup
    //   67: ldc_w 'Tried to create a new objectracker before releasing the old one!'
    //   70: invokespecial <init> : (Ljava/lang/String;)V
    //   73: athrow
    //   74: astore #4
    //   76: ldc com/google/ftcresearch/tfod/tracking/ObjectTracker
    //   78: monitorexit
    //   79: aload #4
    //   81: athrow
    // Exception table:
    //   from	to	target	type
    //   3	18	74	finally
    //   23	57	74	finally
    //   63	74	74	finally
  }
  
  private native void initNative(int paramInt1, int paramInt2, boolean paramBoolean);
  
  private void updateDebugHistory() {
    this.lastKeypoints = new FrameChange(getKeypointsNative(false));
    long l = this.lastTimestamp;
    if (l == 0L)
      return; 
    null = getAccumulatedDelta(l, (this.frameWidth / 2), (this.frameHeight / 2), 100.0F);
    synchronized (this.debugHistory) {
      this.debugHistory.add(null);
      while (this.debugHistory.size() > 100)
        this.debugHistory.remove(0); 
      return;
    } 
  }
  
  private RectF upscaleRect(RectF paramRectF) {
    return new RectF(paramRectF.left * 2.0F, paramRectF.top * 2.0F, paramRectF.right * 2.0F, paramRectF.bottom * 2.0F);
  }
  
  public void drawDebug(Canvas paramCanvas, Matrix paramMatrix) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokevirtual save : ()I
    //   6: pop
    //   7: aload_1
    //   8: aload_2
    //   9: invokevirtual setMatrix : (Landroid/graphics/Matrix;)V
    //   12: aload_0
    //   13: aload_1
    //   14: invokespecial drawHistoryDebug : (Landroid/graphics/Canvas;)V
    //   17: aload_0
    //   18: aload_1
    //   19: invokespecial drawKeypointsDebug : (Landroid/graphics/Canvas;)V
    //   22: aload_1
    //   23: invokevirtual restore : ()V
    //   26: aload_0
    //   27: monitorexit
    //   28: return
    //   29: astore_1
    //   30: aload_0
    //   31: monitorexit
    //   32: aload_1
    //   33: athrow
    // Exception table:
    //   from	to	target	type
    //   2	26	29	finally
  }
  
  protected native void drawNative(int paramInt1, int paramInt2, float[] paramArrayOffloat);
  
  public void drawOverlay(GL10 paramGL10, Size paramSize, Matrix paramMatrix) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new android/graphics/Matrix
    //   5: dup
    //   6: aload_3
    //   7: invokespecial <init> : (Landroid/graphics/Matrix;)V
    //   10: astore_1
    //   11: aload_1
    //   12: fconst_2
    //   13: fconst_2
    //   14: invokevirtual preScale : (FF)Z
    //   17: pop
    //   18: aload_1
    //   19: aload_0
    //   20: getfield matrixValues : [F
    //   23: invokevirtual getValues : ([F)V
    //   26: aload_0
    //   27: aload_2
    //   28: getfield width : I
    //   31: aload_2
    //   32: getfield height : I
    //   35: aload_0
    //   36: getfield matrixValues : [F
    //   39: invokevirtual drawNative : (II[F)V
    //   42: aload_0
    //   43: monitorexit
    //   44: return
    //   45: astore_1
    //   46: aload_0
    //   47: monitorexit
    //   48: aload_1
    //   49: athrow
    // Exception table:
    //   from	to	target	type
    //   2	42	45	finally
  }
  
  protected native void forgetNative(String paramString);
  
  protected native float getCurrentCorrelation(String paramString);
  
  protected native void getCurrentPositionNative(long paramLong, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float[] paramArrayOffloat);
  
  public Vector<String> getDebugText() {
    Vector<String> vector = new Vector();
    if (this.lastKeypoints != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Num keypoints ");
      stringBuilder.append(this.lastKeypoints.pointDeltas.size());
      vector.add(stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("Min score: ");
      stringBuilder.append(this.lastKeypoints.minScore);
      vector.add(stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("Max score: ");
      stringBuilder.append(this.lastKeypoints.maxScore);
      vector.add(stringBuilder.toString());
    } 
    return vector;
  }
  
  protected native float[] getKeypointsNative(boolean paramBoolean);
  
  protected native byte[] getKeypointsPacked(float paramFloat);
  
  protected native float getMatchScore(String paramString);
  
  protected native String getModelIdNative(String paramString);
  
  protected native void getTrackedPositionNative(String paramString, float[] paramArrayOffloat);
  
  protected native boolean haveObject(String paramString);
  
  protected void init() {
    initNative(this.frameWidth / 2, this.frameHeight / 2, this.alwaysTrack);
  }
  
  protected native boolean isObjectVisible(String paramString);
  
  public void nextFrame(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, long paramLong, float[] paramArrayOffloat, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield downsampledTimestamp : J
    //   6: lload_3
    //   7: lcmp
    //   8: ifeq -> 37
    //   11: aload_0
    //   12: getfield frameWidth : I
    //   15: aload_0
    //   16: getfield frameHeight : I
    //   19: aload_0
    //   20: getfield rowStride : I
    //   23: aload_1
    //   24: iconst_2
    //   25: aload_0
    //   26: getfield downsampledFrame : [B
    //   29: invokestatic downsampleImageNative : (III[BI[B)V
    //   32: aload_0
    //   33: lload_3
    //   34: putfield downsampledTimestamp : J
    //   37: aload_0
    //   38: aload_0
    //   39: getfield downsampledFrame : [B
    //   42: aload_2
    //   43: lload_3
    //   44: aload #5
    //   46: invokevirtual nextFrameNative : ([B[BJ[F)V
    //   49: aload_0
    //   50: getfield timestampedDeltas : Ljava/util/LinkedList;
    //   53: new com/google/ftcresearch/tfod/tracking/ObjectTracker$TimestampedDeltas
    //   56: dup
    //   57: lload_3
    //   58: aload_0
    //   59: fconst_2
    //   60: invokevirtual getKeypointsPacked : (F)[B
    //   63: invokespecial <init> : (J[B)V
    //   66: invokevirtual add : (Ljava/lang/Object;)Z
    //   69: pop
    //   70: aload_0
    //   71: getfield timestampedDeltas : Ljava/util/LinkedList;
    //   74: invokevirtual size : ()I
    //   77: sipush #200
    //   80: if_icmple -> 94
    //   83: aload_0
    //   84: getfield timestampedDeltas : Ljava/util/LinkedList;
    //   87: invokevirtual removeFirst : ()Ljava/lang/Object;
    //   90: pop
    //   91: goto -> 70
    //   94: aload_0
    //   95: getfield trackedObjects : Ljava/util/Map;
    //   98: invokeinterface values : ()Ljava/util/Collection;
    //   103: invokeinterface iterator : ()Ljava/util/Iterator;
    //   108: astore_1
    //   109: aload_1
    //   110: invokeinterface hasNext : ()Z
    //   115: ifeq -> 133
    //   118: aload_1
    //   119: invokeinterface next : ()Ljava/lang/Object;
    //   124: checkcast com/google/ftcresearch/tfod/tracking/ObjectTracker$TrackedObject
    //   127: invokestatic access$000 : (Lcom/google/ftcresearch/tfod/tracking/ObjectTracker$TrackedObject;)V
    //   130: goto -> 109
    //   133: iload #6
    //   135: ifeq -> 142
    //   138: aload_0
    //   139: invokespecial updateDebugHistory : ()V
    //   142: aload_0
    //   143: lload_3
    //   144: putfield lastTimestamp : J
    //   147: aload_0
    //   148: monitorexit
    //   149: return
    //   150: astore_1
    //   151: aload_0
    //   152: monitorexit
    //   153: aload_1
    //   154: athrow
    // Exception table:
    //   from	to	target	type
    //   2	37	150	finally
    //   37	70	150	finally
    //   70	91	150	finally
    //   94	109	150	finally
    //   109	130	150	finally
    //   138	142	150	finally
    //   142	147	150	finally
  }
  
  protected native void nextFrameNative(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, long paramLong, float[] paramArrayOffloat);
  
  public List<byte[]> pollAccumulatedFlowData(long paramLong) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/ArrayList
    //   5: dup
    //   6: invokespecial <init> : ()V
    //   9: astore_3
    //   10: aload_0
    //   11: getfield timestampedDeltas : Ljava/util/LinkedList;
    //   14: invokevirtual size : ()I
    //   17: ifle -> 65
    //   20: aload_0
    //   21: getfield timestampedDeltas : Ljava/util/LinkedList;
    //   24: invokevirtual peek : ()Ljava/lang/Object;
    //   27: checkcast com/google/ftcresearch/tfod/tracking/ObjectTracker$TimestampedDeltas
    //   30: astore #4
    //   32: aload #4
    //   34: getfield timestamp : J
    //   37: lload_1
    //   38: lcmp
    //   39: ifgt -> 65
    //   42: aload_3
    //   43: aload #4
    //   45: getfield deltas : [B
    //   48: invokeinterface add : (Ljava/lang/Object;)Z
    //   53: pop
    //   54: aload_0
    //   55: getfield timestampedDeltas : Ljava/util/LinkedList;
    //   58: invokevirtual removeFirst : ()Ljava/lang/Object;
    //   61: pop
    //   62: goto -> 10
    //   65: aload_0
    //   66: monitorexit
    //   67: aload_3
    //   68: areturn
    //   69: astore_3
    //   70: aload_0
    //   71: monitorexit
    //   72: aload_3
    //   73: athrow
    // Exception table:
    //   from	to	target	type
    //   2	10	69	finally
    //   10	62	69	finally
  }
  
  protected native void registerNewObjectWithAppearanceNative(String paramString, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, byte[] paramArrayOfbyte);
  
  public void release() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual releaseMemoryNative : ()V
    //   6: ldc com/google/ftcresearch/tfod/tracking/ObjectTracker
    //   8: monitorenter
    //   9: aconst_null
    //   10: putstatic com/google/ftcresearch/tfod/tracking/ObjectTracker.instance : Lcom/google/ftcresearch/tfod/tracking/ObjectTracker;
    //   13: ldc com/google/ftcresearch/tfod/tracking/ObjectTracker
    //   15: monitorexit
    //   16: aload_0
    //   17: monitorexit
    //   18: return
    //   19: astore_1
    //   20: ldc com/google/ftcresearch/tfod/tracking/ObjectTracker
    //   22: monitorexit
    //   23: aload_1
    //   24: athrow
    //   25: astore_1
    //   26: aload_0
    //   27: monitorexit
    //   28: aload_1
    //   29: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	25	finally
    //   9	16	19	finally
    //   20	23	19	finally
    //   23	25	25	finally
  }
  
  protected native void releaseMemoryNative();
  
  protected native void setCurrentPositionNative(String paramString, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
  
  protected native void setPreviousPositionNative(String paramString, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong);
  
  public TrackedObject trackObject(RectF paramRectF, long paramLong, byte[] paramArrayOfbyte) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield downsampledTimestamp : J
    //   6: lload_2
    //   7: lcmp
    //   8: ifeq -> 38
    //   11: aload_0
    //   12: getfield frameWidth : I
    //   15: aload_0
    //   16: getfield frameHeight : I
    //   19: aload_0
    //   20: getfield rowStride : I
    //   23: aload #4
    //   25: iconst_2
    //   26: aload_0
    //   27: getfield downsampledFrame : [B
    //   30: invokestatic downsampleImageNative : (III[BI[B)V
    //   33: aload_0
    //   34: lload_2
    //   35: putfield downsampledTimestamp : J
    //   38: new com/google/ftcresearch/tfod/tracking/ObjectTracker$TrackedObject
    //   41: dup
    //   42: aload_0
    //   43: aload_1
    //   44: lload_2
    //   45: aload_0
    //   46: getfield downsampledFrame : [B
    //   49: invokespecial <init> : (Lcom/google/ftcresearch/tfod/tracking/ObjectTracker;Landroid/graphics/RectF;J[B)V
    //   52: astore_1
    //   53: aload_0
    //   54: monitorexit
    //   55: aload_1
    //   56: areturn
    //   57: astore_1
    //   58: aload_0
    //   59: monitorexit
    //   60: aload_1
    //   61: athrow
    // Exception table:
    //   from	to	target	type
    //   2	38	57	finally
    //   38	53	57	finally
  }
  
  public TrackedObject trackObject(RectF paramRectF, byte[] paramArrayOfbyte) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new com/google/ftcresearch/tfod/tracking/ObjectTracker$TrackedObject
    //   5: dup
    //   6: aload_0
    //   7: aload_1
    //   8: aload_0
    //   9: getfield lastTimestamp : J
    //   12: aload_2
    //   13: invokespecial <init> : (Lcom/google/ftcresearch/tfod/tracking/ObjectTracker;Landroid/graphics/RectF;J[B)V
    //   16: astore_1
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_1
    //   20: areturn
    //   21: astore_1
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_1
    //   25: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	21	finally
  }
  
  public static class FrameChange {
    public static final int KEYPOINT_STEP = 7;
    
    private final float maxScore;
    
    private final float minScore;
    
    public final Vector<ObjectTracker.PointChange> pointDeltas;
    
    public FrameChange(float[] param1ArrayOffloat) {
      this.pointDeltas = new Vector<ObjectTracker.PointChange>(param1ArrayOffloat.length / 7);
      float f2 = 100.0F;
      float f1 = -100.0F;
      int i;
      for (i = 0; i < param1ArrayOffloat.length; i += 7) {
        boolean bool;
        float f3 = param1ArrayOffloat[i + 0];
        float f4 = param1ArrayOffloat[i + 1];
        if (param1ArrayOffloat[i + 2] > 0.0F) {
          bool = true;
        } else {
          bool = false;
        } 
        float f5 = param1ArrayOffloat[i + 3];
        float f6 = param1ArrayOffloat[i + 4];
        float f7 = param1ArrayOffloat[i + 5];
        int j = (int)param1ArrayOffloat[i + 6];
        f2 = Math.min(f2, f7);
        f1 = Math.max(f1, f7);
        this.pointDeltas.add(new ObjectTracker.PointChange(f3 * 2.0F, f4 * 2.0F, f5 * 2.0F, f6 * 2.0F, f7, j, bool));
      } 
      this.minScore = f2;
      this.maxScore = f1;
    }
  }
  
  public static class Keypoint {
    public final float score;
    
    public final int type;
    
    public final float x;
    
    public final float y;
    
    public Keypoint(float param1Float1, float param1Float2) {
      this.x = param1Float1;
      this.y = param1Float2;
      this.score = 0.0F;
      this.type = -1;
    }
    
    public Keypoint(float param1Float1, float param1Float2, float param1Float3, int param1Int) {
      this.x = param1Float1;
      this.y = param1Float2;
      this.score = param1Float3;
      this.type = param1Int;
    }
    
    Keypoint delta(Keypoint param1Keypoint) {
      return new Keypoint(this.x - param1Keypoint.x, this.y - param1Keypoint.y);
    }
  }
  
  public static class PointChange {
    public final ObjectTracker.Keypoint keypointA;
    
    public final ObjectTracker.Keypoint keypointB;
    
    ObjectTracker.Keypoint pointDelta;
    
    private final boolean wasFound;
    
    public PointChange(float param1Float1, float param1Float2, float param1Float3, float param1Float4, float param1Float5, int param1Int, boolean param1Boolean) {
      this.wasFound = param1Boolean;
      this.keypointA = new ObjectTracker.Keypoint(param1Float1, param1Float2, param1Float5, param1Int);
      this.keypointB = new ObjectTracker.Keypoint(param1Float3, param1Float4);
    }
    
    public ObjectTracker.Keypoint getDelta() {
      if (this.pointDelta == null)
        this.pointDelta = this.keypointB.delta(this.keypointA); 
      return this.pointDelta;
    }
  }
  
  private static class TimestampedDeltas {
    final byte[] deltas;
    
    final long timestamp;
    
    public TimestampedDeltas(long param1Long, byte[] param1ArrayOfbyte) {
      this.timestamp = param1Long;
      this.deltas = param1ArrayOfbyte;
    }
  }
  
  public class TrackedObject {
    private final String id;
    
    private boolean isDead;
    
    private long lastExternalPositionTime;
    
    private RectF lastTrackedPosition;
    
    private boolean visibleInLastFrame;
    
    TrackedObject(ObjectTracker this$0, RectF param1RectF, long param1Long, byte[] param1ArrayOfbyte) {
      // Byte code:
      //   0: aload_0
      //   1: aload_1
      //   2: putfield this$0 : Lcom/google/ftcresearch/tfod/tracking/ObjectTracker;
      //   5: aload_0
      //   6: invokespecial <init> : ()V
      //   9: aload_0
      //   10: iconst_0
      //   11: putfield isDead : Z
      //   14: aload_0
      //   15: aload_0
      //   16: invokevirtual hashCode : ()I
      //   19: invokestatic toString : (I)Ljava/lang/String;
      //   22: putfield id : Ljava/lang/String;
      //   25: aload_0
      //   26: lload_3
      //   27: putfield lastExternalPositionTime : J
      //   30: aload_1
      //   31: monitorenter
      //   32: aload_0
      //   33: aload_2
      //   34: aload #5
      //   36: invokevirtual registerInitialAppearance : (Landroid/graphics/RectF;[B)V
      //   39: aload_0
      //   40: aload_2
      //   41: lload_3
      //   42: invokevirtual setPreviousPosition : (Landroid/graphics/RectF;J)V
      //   45: aload_1
      //   46: invokestatic access$400 : (Lcom/google/ftcresearch/tfod/tracking/ObjectTracker;)Ljava/util/Map;
      //   49: aload_0
      //   50: getfield id : Ljava/lang/String;
      //   53: aload_0
      //   54: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
      //   59: pop
      //   60: aload_1
      //   61: monitorexit
      //   62: return
      //   63: astore_2
      //   64: aload_1
      //   65: monitorexit
      //   66: aload_2
      //   67: athrow
      // Exception table:
      //   from	to	target	type
      //   32	62	63	finally
      //   64	66	63	finally
    }
    
    private void checkValidObject() {
      if (!this.isDead) {
        if (this.this$0 == ObjectTracker.instance)
          return; 
        throw new RuntimeException("TrackedObject created with another ObjectTracker!");
      } 
      throw new RuntimeException("TrackedObject already removed from tracking!");
    }
    
    private void updateTrackedPosition() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: invokespecial checkValidObject : ()V
      //   6: iconst_4
      //   7: newarray float
      //   9: astore_1
      //   10: aload_0
      //   11: getfield this$0 : Lcom/google/ftcresearch/tfod/tracking/ObjectTracker;
      //   14: aload_0
      //   15: getfield id : Ljava/lang/String;
      //   18: aload_1
      //   19: invokevirtual getTrackedPositionNative : (Ljava/lang/String;[F)V
      //   22: aload_0
      //   23: new android/graphics/RectF
      //   26: dup
      //   27: aload_1
      //   28: iconst_0
      //   29: faload
      //   30: aload_1
      //   31: iconst_1
      //   32: faload
      //   33: aload_1
      //   34: iconst_2
      //   35: faload
      //   36: aload_1
      //   37: iconst_3
      //   38: faload
      //   39: invokespecial <init> : (FFFF)V
      //   42: putfield lastTrackedPosition : Landroid/graphics/RectF;
      //   45: aload_0
      //   46: aload_0
      //   47: getfield this$0 : Lcom/google/ftcresearch/tfod/tracking/ObjectTracker;
      //   50: aload_0
      //   51: getfield id : Ljava/lang/String;
      //   54: invokevirtual isObjectVisible : (Ljava/lang/String;)Z
      //   57: putfield visibleInLastFrame : Z
      //   60: aload_0
      //   61: monitorexit
      //   62: return
      //   63: astore_1
      //   64: aload_0
      //   65: monitorexit
      //   66: aload_1
      //   67: athrow
      // Exception table:
      //   from	to	target	type
      //   2	60	63	finally
    }
    
    public float getCurrentCorrelation() {
      checkValidObject();
      return this.this$0.getCurrentCorrelation(this.id);
    }
    
    long getLastExternalPositionTime() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield lastExternalPositionTime : J
      //   6: lstore_1
      //   7: aload_0
      //   8: monitorexit
      //   9: lload_1
      //   10: lreturn
      //   11: astore_3
      //   12: aload_0
      //   13: monitorexit
      //   14: aload_3
      //   15: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	11	finally
    }
    
    public RectF getTrackedPositionInPreviewFrame() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: invokespecial checkValidObject : ()V
      //   6: aload_0
      //   7: getfield lastTrackedPosition : Landroid/graphics/RectF;
      //   10: astore_1
      //   11: aload_1
      //   12: ifnonnull -> 19
      //   15: aload_0
      //   16: monitorexit
      //   17: aconst_null
      //   18: areturn
      //   19: aload_0
      //   20: getfield this$0 : Lcom/google/ftcresearch/tfod/tracking/ObjectTracker;
      //   23: aload_0
      //   24: getfield lastTrackedPosition : Landroid/graphics/RectF;
      //   27: invokestatic access$600 : (Lcom/google/ftcresearch/tfod/tracking/ObjectTracker;Landroid/graphics/RectF;)Landroid/graphics/RectF;
      //   30: astore_1
      //   31: aload_0
      //   32: monitorexit
      //   33: aload_1
      //   34: areturn
      //   35: astore_1
      //   36: aload_0
      //   37: monitorexit
      //   38: aload_1
      //   39: athrow
      // Exception table:
      //   from	to	target	type
      //   2	11	35	finally
      //   19	31	35	finally
    }
    
    void registerInitialAppearance(RectF param1RectF, byte[] param1ArrayOfbyte) {
      param1RectF = this.this$0.downscaleRect(param1RectF);
      this.this$0.registerNewObjectWithAppearanceNative(this.id, param1RectF.left, param1RectF.top, param1RectF.right, param1RectF.bottom, param1ArrayOfbyte);
    }
    
    void setCurrentPosition(RectF param1RectF) {
      checkValidObject();
      null = this.this$0.downscaleRect(param1RectF);
      synchronized (this.this$0) {
        this.this$0.setCurrentPositionNative(this.id, null.left, null.top, null.right, null.bottom);
        return;
      } 
    }
    
    void setPreviousPosition(RectF param1RectF, long param1Long) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: invokespecial checkValidObject : ()V
      //   6: aload_0
      //   7: getfield this$0 : Lcom/google/ftcresearch/tfod/tracking/ObjectTracker;
      //   10: astore #4
      //   12: aload #4
      //   14: monitorenter
      //   15: aload_0
      //   16: getfield lastExternalPositionTime : J
      //   19: lload_2
      //   20: lcmp
      //   21: ifle -> 38
      //   24: ldc 'ObjectTracker'
      //   26: ldc 'Tried to use older position time!'
      //   28: invokestatic w : (Ljava/lang/String;Ljava/lang/String;)I
      //   31: pop
      //   32: aload #4
      //   34: monitorexit
      //   35: aload_0
      //   36: monitorexit
      //   37: return
      //   38: aload_0
      //   39: getfield this$0 : Lcom/google/ftcresearch/tfod/tracking/ObjectTracker;
      //   42: aload_1
      //   43: invokestatic access$500 : (Lcom/google/ftcresearch/tfod/tracking/ObjectTracker;Landroid/graphics/RectF;)Landroid/graphics/RectF;
      //   46: astore_1
      //   47: aload_0
      //   48: lload_2
      //   49: putfield lastExternalPositionTime : J
      //   52: aload_0
      //   53: getfield this$0 : Lcom/google/ftcresearch/tfod/tracking/ObjectTracker;
      //   56: aload_0
      //   57: getfield id : Ljava/lang/String;
      //   60: aload_1
      //   61: getfield left : F
      //   64: aload_1
      //   65: getfield top : F
      //   68: aload_1
      //   69: getfield right : F
      //   72: aload_1
      //   73: getfield bottom : F
      //   76: aload_0
      //   77: getfield lastExternalPositionTime : J
      //   80: invokevirtual setPreviousPositionNative : (Ljava/lang/String;FFFFJ)V
      //   83: aload_0
      //   84: invokespecial updateTrackedPosition : ()V
      //   87: aload #4
      //   89: monitorexit
      //   90: aload_0
      //   91: monitorexit
      //   92: return
      //   93: astore_1
      //   94: aload #4
      //   96: monitorexit
      //   97: aload_1
      //   98: athrow
      //   99: astore_1
      //   100: aload_0
      //   101: monitorexit
      //   102: aload_1
      //   103: athrow
      // Exception table:
      //   from	to	target	type
      //   2	15	99	finally
      //   15	35	93	finally
      //   38	90	93	finally
      //   94	97	93	finally
      //   97	99	99	finally
    }
    
    public void stopTracking() {
      checkValidObject();
      synchronized (this.this$0) {
        this.isDead = true;
        this.this$0.forgetNative(this.id);
        this.this$0.trackedObjects.remove(this.id);
        return;
      } 
    }
    
    public boolean visibleInLastPreviewFrame() {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_0
      //   3: getfield visibleInLastFrame : Z
      //   6: istore_1
      //   7: aload_0
      //   8: monitorexit
      //   9: iload_1
      //   10: ireturn
      //   11: astore_2
      //   12: aload_0
      //   13: monitorexit
      //   14: aload_2
      //   15: athrow
      // Exception table:
      //   from	to	target	type
      //   2	7	11	finally
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\ftcresearch\tfod\tracking\ObjectTracker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.tfod;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.util.Pair;
import com.google.ftcresearch.tfod.tracking.ObjectTracker;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

public class MultiBoxTracker {
  private static final int[] COLORS = new int[] { 
      -16776961, -65536, -16711936, -256, -16711681, -65281, -1, Color.parseColor("#55FF55"), Color.parseColor("#FFA500"), Color.parseColor("#FF8888"), 
      Color.parseColor("#AAAAFF"), Color.parseColor("#FFFFAA"), Color.parseColor("#55AAAA"), Color.parseColor("#AA33AA"), Color.parseColor("#0D0068") };
  
  private static final String TAG = "MultiBoxTracker";
  
  private static final float TEXT_SIZE_DIP = 18.0F;
  
  private final Queue<Integer> availableColors = new LinkedList<Integer>();
  
  private final BorderedText borderedText;
  
  private final Paint boxPaint = new Paint();
  
  private CameraInformation cameraInformation;
  
  private int frameHeight;
  
  private Matrix frameToCanvasMatrix;
  
  private int frameWidth;
  
  private boolean initialized;
  
  private ObjectTracker objectTracker;
  
  private final TfodParameters params;
  
  final List<Pair<Float, RectF>> screenRects = new LinkedList<Pair<Float, RectF>>();
  
  private int sensorOrientation;
  
  private final float textSizePx;
  
  private final List<TrackedRecognition> trackedObjects = new LinkedList<TrackedRecognition>();
  
  public MultiBoxTracker(TfodParameters paramTfodParameters) {
    int i = 0;
    this.initialized = false;
    this.params = paramTfodParameters;
    int[] arrayOfInt = COLORS;
    int j = arrayOfInt.length;
    while (i < j) {
      int k = arrayOfInt[i];
      this.availableColors.add(Integer.valueOf(k));
      i++;
    } 
    this.boxPaint.setColor(-65536);
    this.boxPaint.setStyle(Paint.Style.STROKE);
    this.boxPaint.setStrokeWidth(12.0F);
    this.boxPaint.setStrokeCap(Paint.Cap.ROUND);
    this.boxPaint.setStrokeJoin(Paint.Join.ROUND);
    this.boxPaint.setStrokeMiter(100.0F);
    this.textSizePx = 20.0F;
    this.borderedText = new BorderedText(this.textSizePx);
  }
  
  private Matrix getFrameToCanvasMatrix() {
    return this.frameToCanvasMatrix;
  }
  
  private void handleDetection(byte[] paramArrayOfbyte, long paramLong, Pair<Float, RecognitionImpl> paramPair) {
    int i;
    ObjectTracker.TrackedObject trackedObject = this.objectTracker.trackObject(((RecognitionImpl)paramPair.second).getLocation(), paramLong, paramArrayOfbyte);
    if (trackedObject.getCurrentCorrelation() < this.params.trackerMarginalCorrelation) {
      trackedObject.stopTracking();
      return;
    } 
    LinkedList<TrackedRecognition> linkedList = new LinkedList();
    float f = 0.0F;
    Iterator<TrackedRecognition> iterator = this.trackedObjects.iterator();
    paramArrayOfbyte = null;
    while (iterator.hasNext()) {
      TrackedRecognition trackedRecognition = iterator.next();
      RectF rectF1 = trackedRecognition.trackedObject.getTrackedPositionInPreviewFrame();
      RectF rectF2 = trackedObject.getTrackedPositionInPreviewFrame();
      RectF rectF3 = new RectF();
      boolean bool = rectF3.setIntersect(rectF1, rectF2);
      float f1 = rectF3.width() * rectF3.height();
      f1 /= rectF1.width() * rectF1.height() + rectF2.width() * rectF2.height() - f1;
      if (bool && f1 > this.params.trackerMaxOverlap) {
        if (((Float)paramPair.first).floatValue() < trackedRecognition.detectionConfidence && trackedRecognition.trackedObject.getCurrentCorrelation() > this.params.trackerMarginalCorrelation) {
          trackedObject.stopTracking();
          return;
        } 
        linkedList.add(trackedRecognition);
        if (f1 > f) {
          trackedRecognition1 = trackedRecognition;
          f = f1;
        } 
      } 
    } 
    trackedRecognition2 = trackedRecognition1;
    if (this.availableColors.isEmpty()) {
      trackedRecognition2 = trackedRecognition1;
      if (linkedList.isEmpty()) {
        for (TrackedRecognition trackedRecognition2 : this.trackedObjects) {
          if (trackedRecognition2.detectionConfidence < ((Float)paramPair.first).floatValue() && (trackedRecognition1 == null || trackedRecognition2.detectionConfidence < trackedRecognition1.detectionConfidence))
            trackedRecognition1 = trackedRecognition2; 
        } 
        trackedRecognition2 = trackedRecognition1;
        if (trackedRecognition1 != null) {
          linkedList.add(trackedRecognition1);
          trackedRecognition2 = trackedRecognition1;
        } 
      } 
    } 
    for (TrackedRecognition trackedRecognition : linkedList) {
      trackedRecognition.trackedObject.stopTracking();
      this.trackedObjects.remove(trackedRecognition);
      if (trackedRecognition != trackedRecognition2)
        this.availableColors.add(Integer.valueOf(trackedRecognition.color)); 
    } 
    if (trackedRecognition2 == null && this.availableColors.isEmpty()) {
      Log.e("MultiBoxTracker", "No room to track this object, aborting.");
      trackedObject.stopTracking();
      return;
    } 
    TrackedRecognition trackedRecognition1 = new TrackedRecognition();
    trackedRecognition1.detectionConfidence = ((Float)paramPair.first).floatValue();
    trackedRecognition1.trackedObject = trackedObject;
    trackedRecognition1.title = ((RecognitionImpl)paramPair.second).getLabel();
    if (trackedRecognition2 != null) {
      i = trackedRecognition2.color;
    } else {
      i = ((Integer)this.availableColors.poll()).intValue();
    } 
    trackedRecognition1.color = i;
    this.trackedObjects.add(trackedRecognition1);
  }
  
  private void processResults(long paramLong, List<Recognition> paramList, byte[] paramArrayOfbyte) {
    TrackedRecognition trackedRecognition;
    LinkedList<Pair> linkedList = new LinkedList();
    this.screenRects.clear();
    Matrix matrix = new Matrix(getFrameToCanvasMatrix());
    for (Recognition recognition : paramList) {
      StringBuilder stringBuilder;
      RecognitionImpl recognitionImpl = (RecognitionImpl)recognition;
      if (recognitionImpl.getLocation() == null)
        continue; 
      RectF rectF1 = new RectF(recognitionImpl.getLocation());
      RectF rectF2 = new RectF();
      matrix.mapRect(rectF2, rectF1);
      this.screenRects.add(new Pair(Float.valueOf(recognition.getConfidence()), rectF2));
      if (rectF1.width() < this.params.trackerMinSize || rectF1.height() < this.params.trackerMinSize) {
        stringBuilder = new StringBuilder();
        stringBuilder.append("Degenerate rectangle! ");
        stringBuilder.append(rectF1);
        Log.w("MultiBoxTracker", stringBuilder.toString());
        continue;
      } 
      linkedList.add(new Pair(Float.valueOf(stringBuilder.getConfidence()), recognitionImpl));
    } 
    if (linkedList.isEmpty())
      return; 
    if (this.objectTracker == null) {
      this.trackedObjects.clear();
      for (Pair pair : linkedList) {
        trackedRecognition = new TrackedRecognition();
        trackedRecognition.detectionConfidence = ((Float)pair.first).floatValue();
        trackedRecognition.location = new RectF(((RecognitionImpl)pair.second).getLocation());
        trackedRecognition.trackedObject = null;
        trackedRecognition.title = ((RecognitionImpl)pair.second).getLabel();
        trackedRecognition.color = COLORS[this.trackedObjects.size()];
        this.trackedObjects.add(trackedRecognition);
        if (this.trackedObjects.size() >= COLORS.length)
          break; 
      } 
      return;
    } 
    Iterator<Pair<Float, RecognitionImpl>> iterator = trackedRecognition.iterator();
    while (iterator.hasNext())
      handleDetection((byte[])pair, paramLong, iterator.next()); 
  }
  
  public void draw(Canvas paramCanvas) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield sensorOrientation : I
    //   6: sipush #180
    //   9: irem
    //   10: bipush #90
    //   12: if_icmpne -> 393
    //   15: iconst_1
    //   16: istore #4
    //   18: goto -> 21
    //   21: aload_1
    //   22: invokevirtual getHeight : ()I
    //   25: i2f
    //   26: fstore_2
    //   27: iload #4
    //   29: ifeq -> 41
    //   32: aload_0
    //   33: getfield frameWidth : I
    //   36: istore #5
    //   38: goto -> 47
    //   41: aload_0
    //   42: getfield frameHeight : I
    //   45: istore #5
    //   47: fload_2
    //   48: iload #5
    //   50: i2f
    //   51: fdiv
    //   52: fstore_2
    //   53: aload_1
    //   54: invokevirtual getWidth : ()I
    //   57: i2f
    //   58: fstore_3
    //   59: iload #4
    //   61: ifeq -> 73
    //   64: aload_0
    //   65: getfield frameHeight : I
    //   68: istore #5
    //   70: goto -> 79
    //   73: aload_0
    //   74: getfield frameWidth : I
    //   77: istore #5
    //   79: fload_2
    //   80: fload_3
    //   81: iload #5
    //   83: i2f
    //   84: fdiv
    //   85: invokestatic min : (FF)F
    //   88: fstore_2
    //   89: aload_0
    //   90: getfield frameWidth : I
    //   93: istore #6
    //   95: aload_0
    //   96: getfield frameHeight : I
    //   99: istore #7
    //   101: iload #4
    //   103: ifeq -> 115
    //   106: aload_0
    //   107: getfield frameHeight : I
    //   110: istore #5
    //   112: goto -> 121
    //   115: aload_0
    //   116: getfield frameWidth : I
    //   119: istore #5
    //   121: iload #5
    //   123: i2f
    //   124: fload_2
    //   125: fmul
    //   126: f2i
    //   127: istore #5
    //   129: iload #4
    //   131: ifeq -> 143
    //   134: aload_0
    //   135: getfield frameWidth : I
    //   138: istore #4
    //   140: goto -> 149
    //   143: aload_0
    //   144: getfield frameHeight : I
    //   147: istore #4
    //   149: aload_0
    //   150: iload #6
    //   152: iload #7
    //   154: iload #5
    //   156: fload_2
    //   157: iload #4
    //   159: i2f
    //   160: fmul
    //   161: f2i
    //   162: aload_0
    //   163: getfield sensorOrientation : I
    //   166: iconst_0
    //   167: invokestatic getTransformationMatrix : (IIIIIZ)Landroid/graphics/Matrix;
    //   170: putfield frameToCanvasMatrix : Landroid/graphics/Matrix;
    //   173: aload_0
    //   174: getfield trackedObjects : Ljava/util/List;
    //   177: invokeinterface iterator : ()Ljava/util/Iterator;
    //   182: astore #10
    //   184: aload #10
    //   186: invokeinterface hasNext : ()Z
    //   191: ifeq -> 385
    //   194: aload #10
    //   196: invokeinterface next : ()Ljava/lang/Object;
    //   201: checkcast org/firstinspires/ftc/robotcore/internal/tfod/MultiBoxTracker$TrackedRecognition
    //   204: astore #9
    //   206: aload_0
    //   207: getfield objectTracker : Lcom/google/ftcresearch/tfod/tracking/ObjectTracker;
    //   210: ifnull -> 226
    //   213: aload #9
    //   215: getfield trackedObject : Lcom/google/ftcresearch/tfod/tracking/ObjectTracker$TrackedObject;
    //   218: invokevirtual getTrackedPositionInPreviewFrame : ()Landroid/graphics/RectF;
    //   221: astore #8
    //   223: goto -> 240
    //   226: new android/graphics/RectF
    //   229: dup
    //   230: aload #9
    //   232: getfield location : Landroid/graphics/RectF;
    //   235: invokespecial <init> : (Landroid/graphics/RectF;)V
    //   238: astore #8
    //   240: aload_0
    //   241: invokespecial getFrameToCanvasMatrix : ()Landroid/graphics/Matrix;
    //   244: aload #8
    //   246: invokevirtual mapRect : (Landroid/graphics/RectF;)Z
    //   249: pop
    //   250: aload_0
    //   251: getfield boxPaint : Landroid/graphics/Paint;
    //   254: aload #9
    //   256: getfield color : I
    //   259: invokevirtual setColor : (I)V
    //   262: aload #8
    //   264: invokevirtual width : ()F
    //   267: aload #8
    //   269: invokevirtual height : ()F
    //   272: invokestatic min : (FF)F
    //   275: ldc_w 8.0
    //   278: fdiv
    //   279: fstore_2
    //   280: aload_1
    //   281: aload #8
    //   283: fload_2
    //   284: fload_2
    //   285: aload_0
    //   286: getfield boxPaint : Landroid/graphics/Paint;
    //   289: invokevirtual drawRoundRect : (Landroid/graphics/RectF;FFLandroid/graphics/Paint;)V
    //   292: aload #9
    //   294: getfield title : Ljava/lang/String;
    //   297: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   300: ifne -> 337
    //   303: ldc_w '%s %.2f'
    //   306: iconst_2
    //   307: anewarray java/lang/Object
    //   310: dup
    //   311: iconst_0
    //   312: aload #9
    //   314: getfield title : Ljava/lang/String;
    //   317: aastore
    //   318: dup
    //   319: iconst_1
    //   320: aload #9
    //   322: getfield detectionConfidence : F
    //   325: invokestatic valueOf : (F)Ljava/lang/Float;
    //   328: aastore
    //   329: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   332: astore #9
    //   334: goto -> 360
    //   337: ldc_w '%.2f'
    //   340: iconst_1
    //   341: anewarray java/lang/Object
    //   344: dup
    //   345: iconst_0
    //   346: aload #9
    //   348: getfield detectionConfidence : F
    //   351: invokestatic valueOf : (F)Ljava/lang/Float;
    //   354: aastore
    //   355: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   358: astore #9
    //   360: aload_0
    //   361: getfield borderedText : Lorg/firstinspires/ftc/robotcore/internal/tfod/BorderedText;
    //   364: aload_1
    //   365: aload #8
    //   367: getfield left : F
    //   370: fload_2
    //   371: fadd
    //   372: aload #8
    //   374: getfield bottom : F
    //   377: aload #9
    //   379: invokevirtual drawText : (Landroid/graphics/Canvas;FFLjava/lang/String;)V
    //   382: goto -> 184
    //   385: aload_0
    //   386: monitorexit
    //   387: return
    //   388: astore_1
    //   389: aload_0
    //   390: monitorexit
    //   391: aload_1
    //   392: athrow
    //   393: iconst_0
    //   394: istore #4
    //   396: goto -> 21
    // Exception table:
    //   from	to	target	type
    //   2	15	388	finally
    //   21	27	388	finally
    //   32	38	388	finally
    //   41	47	388	finally
    //   47	59	388	finally
    //   64	70	388	finally
    //   73	79	388	finally
    //   79	101	388	finally
    //   106	112	388	finally
    //   115	121	388	finally
    //   134	140	388	finally
    //   143	149	388	finally
    //   149	184	388	finally
    //   184	223	388	finally
    //   226	240	388	finally
    //   240	334	388	finally
    //   337	360	388	finally
    //   360	382	388	finally
  }
  
  public void drawDebug(Canvas paramCanvas) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new android/graphics/Paint
    //   5: dup
    //   6: invokespecial <init> : ()V
    //   9: astore #4
    //   11: aload #4
    //   13: iconst_m1
    //   14: invokevirtual setColor : (I)V
    //   17: aload #4
    //   19: ldc_w 60.0
    //   22: invokevirtual setTextSize : (F)V
    //   25: new android/graphics/Paint
    //   28: dup
    //   29: invokespecial <init> : ()V
    //   32: astore #5
    //   34: aload #5
    //   36: ldc -65281
    //   38: invokevirtual setColor : (I)V
    //   41: aload #5
    //   43: ldc_w 4.0
    //   46: invokevirtual setStrokeWidth : (F)V
    //   49: aload #5
    //   51: sipush #200
    //   54: invokevirtual setAlpha : (I)V
    //   57: aload #5
    //   59: getstatic android/graphics/Paint$Style.STROKE : Landroid/graphics/Paint$Style;
    //   62: invokevirtual setStyle : (Landroid/graphics/Paint$Style;)V
    //   65: aload_0
    //   66: getfield screenRects : Ljava/util/List;
    //   69: invokeinterface iterator : ()Ljava/util/Iterator;
    //   74: astore #6
    //   76: aload #6
    //   78: invokeinterface hasNext : ()Z
    //   83: ifeq -> 229
    //   86: aload #6
    //   88: invokeinterface next : ()Ljava/lang/Object;
    //   93: checkcast android/util/Pair
    //   96: astore #7
    //   98: aload #7
    //   100: getfield second : Ljava/lang/Object;
    //   103: checkcast android/graphics/RectF
    //   106: astore #8
    //   108: aload_1
    //   109: aload #8
    //   111: aload #5
    //   113: invokevirtual drawRect : (Landroid/graphics/RectF;Landroid/graphics/Paint;)V
    //   116: new java/lang/StringBuilder
    //   119: dup
    //   120: invokespecial <init> : ()V
    //   123: astore #9
    //   125: aload #9
    //   127: ldc_w ''
    //   130: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   133: pop
    //   134: aload #9
    //   136: aload #7
    //   138: getfield first : Ljava/lang/Object;
    //   141: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   144: pop
    //   145: aload_1
    //   146: aload #9
    //   148: invokevirtual toString : ()Ljava/lang/String;
    //   151: aload #8
    //   153: getfield left : F
    //   156: aload #8
    //   158: getfield top : F
    //   161: aload #4
    //   163: invokevirtual drawText : (Ljava/lang/String;FFLandroid/graphics/Paint;)V
    //   166: aload_0
    //   167: getfield borderedText : Lorg/firstinspires/ftc/robotcore/internal/tfod/BorderedText;
    //   170: astore #9
    //   172: aload #8
    //   174: invokevirtual centerX : ()F
    //   177: fstore_2
    //   178: aload #8
    //   180: invokevirtual centerY : ()F
    //   183: fstore_3
    //   184: new java/lang/StringBuilder
    //   187: dup
    //   188: invokespecial <init> : ()V
    //   191: astore #8
    //   193: aload #8
    //   195: ldc_w ''
    //   198: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   201: pop
    //   202: aload #8
    //   204: aload #7
    //   206: getfield first : Ljava/lang/Object;
    //   209: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   212: pop
    //   213: aload #9
    //   215: aload_1
    //   216: fload_2
    //   217: fload_3
    //   218: aload #8
    //   220: invokevirtual toString : ()Ljava/lang/String;
    //   223: invokevirtual drawText : (Landroid/graphics/Canvas;FFLjava/lang/String;)V
    //   226: goto -> 76
    //   229: aload_0
    //   230: getfield objectTracker : Lcom/google/ftcresearch/tfod/tracking/ObjectTracker;
    //   233: ifnonnull -> 248
    //   236: ldc 'MultiBoxTracker'
    //   238: ldc_w 'skipping drawing debug because object tracker is null'
    //   241: invokestatic w : (Ljava/lang/String;Ljava/lang/String;)I
    //   244: pop
    //   245: aload_0
    //   246: monitorexit
    //   247: return
    //   248: aload_0
    //   249: invokespecial getFrameToCanvasMatrix : ()Landroid/graphics/Matrix;
    //   252: astore #5
    //   254: aload #5
    //   256: astore #4
    //   258: aload #5
    //   260: ifnonnull -> 281
    //   263: ldc 'MultiBoxTracker'
    //   265: ldc_w 'Should be returning here, making identity instead'
    //   268: invokestatic w : (Ljava/lang/String;Ljava/lang/String;)I
    //   271: pop
    //   272: new android/graphics/Matrix
    //   275: dup
    //   276: invokespecial <init> : ()V
    //   279: astore #4
    //   281: aload_0
    //   282: getfield trackedObjects : Ljava/util/List;
    //   285: invokeinterface iterator : ()Ljava/util/Iterator;
    //   290: astore #5
    //   292: aload #5
    //   294: invokeinterface hasNext : ()Z
    //   299: ifeq -> 380
    //   302: aload #5
    //   304: invokeinterface next : ()Ljava/lang/Object;
    //   309: checkcast org/firstinspires/ftc/robotcore/internal/tfod/MultiBoxTracker$TrackedRecognition
    //   312: getfield trackedObject : Lcom/google/ftcresearch/tfod/tracking/ObjectTracker$TrackedObject;
    //   315: astore #7
    //   317: aload #7
    //   319: invokevirtual getTrackedPositionInPreviewFrame : ()Landroid/graphics/RectF;
    //   322: astore #6
    //   324: aload #4
    //   326: aload #6
    //   328: invokevirtual mapRect : (Landroid/graphics/RectF;)Z
    //   331: ifeq -> 292
    //   334: ldc_w '%.2f'
    //   337: iconst_1
    //   338: anewarray java/lang/Object
    //   341: dup
    //   342: iconst_0
    //   343: aload #7
    //   345: invokevirtual getCurrentCorrelation : ()F
    //   348: invokestatic valueOf : (F)Ljava/lang/Float;
    //   351: aastore
    //   352: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   355: astore #7
    //   357: aload_0
    //   358: getfield borderedText : Lorg/firstinspires/ftc/robotcore/internal/tfod/BorderedText;
    //   361: aload_1
    //   362: aload #6
    //   364: getfield right : F
    //   367: aload #6
    //   369: getfield bottom : F
    //   372: aload #7
    //   374: invokevirtual drawText : (Landroid/graphics/Canvas;FFLjava/lang/String;)V
    //   377: goto -> 292
    //   380: aload_0
    //   381: getfield objectTracker : Lcom/google/ftcresearch/tfod/tracking/ObjectTracker;
    //   384: aload_1
    //   385: aload #4
    //   387: invokevirtual drawDebug : (Landroid/graphics/Canvas;Landroid/graphics/Matrix;)V
    //   390: aload_0
    //   391: monitorexit
    //   392: return
    //   393: astore_1
    //   394: aload_0
    //   395: monitorexit
    //   396: aload_1
    //   397: athrow
    // Exception table:
    //   from	to	target	type
    //   2	76	393	finally
    //   76	226	393	finally
    //   229	245	393	finally
    //   248	254	393	finally
    //   263	281	393	finally
    //   281	292	393	finally
    //   292	377	393	finally
    //   380	390	393	finally
  }
  
  public List<Recognition> getRecognitions() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/ArrayList
    //   5: dup
    //   6: invokespecial <init> : ()V
    //   9: astore_2
    //   10: aload_0
    //   11: getfield trackedObjects : Ljava/util/List;
    //   14: invokeinterface iterator : ()Ljava/util/Iterator;
    //   19: astore_3
    //   20: aload_3
    //   21: invokeinterface hasNext : ()Z
    //   26: ifeq -> 104
    //   29: aload_3
    //   30: invokeinterface next : ()Ljava/lang/Object;
    //   35: checkcast org/firstinspires/ftc/robotcore/internal/tfod/MultiBoxTracker$TrackedRecognition
    //   38: astore #4
    //   40: aload_0
    //   41: getfield objectTracker : Lcom/google/ftcresearch/tfod/tracking/ObjectTracker;
    //   44: ifnull -> 59
    //   47: aload #4
    //   49: getfield trackedObject : Lcom/google/ftcresearch/tfod/tracking/ObjectTracker$TrackedObject;
    //   52: invokevirtual getTrackedPositionInPreviewFrame : ()Landroid/graphics/RectF;
    //   55: astore_1
    //   56: goto -> 72
    //   59: new android/graphics/RectF
    //   62: dup
    //   63: aload #4
    //   65: getfield location : Landroid/graphics/RectF;
    //   68: invokespecial <init> : (Landroid/graphics/RectF;)V
    //   71: astore_1
    //   72: aload_2
    //   73: new org/firstinspires/ftc/robotcore/internal/tfod/RecognitionImpl
    //   76: dup
    //   77: aload_0
    //   78: getfield cameraInformation : Lorg/firstinspires/ftc/robotcore/internal/tfod/CameraInformation;
    //   81: aload #4
    //   83: getfield title : Ljava/lang/String;
    //   86: aload #4
    //   88: getfield detectionConfidence : F
    //   91: aload_1
    //   92: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/tfod/CameraInformation;Ljava/lang/String;FLandroid/graphics/RectF;)V
    //   95: invokeinterface add : (Ljava/lang/Object;)Z
    //   100: pop
    //   101: goto -> 20
    //   104: aload_0
    //   105: monitorexit
    //   106: aload_2
    //   107: areturn
    //   108: astore_1
    //   109: aload_0
    //   110: monitorexit
    //   111: aload_1
    //   112: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	108	finally
    //   20	56	108	finally
    //   59	72	108	finally
    //   72	101	108	finally
  }
  
  public void onFrame(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfbyte, long paramLong, CameraInformation paramCameraInformation) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield objectTracker : Lcom/google/ftcresearch/tfod/tracking/ObjectTracker;
    //   6: ifnonnull -> 75
    //   9: aload_0
    //   10: getfield initialized : Z
    //   13: ifne -> 75
    //   16: invokestatic clearInstance : ()V
    //   19: iload_1
    //   20: iload_2
    //   21: iload_3
    //   22: iconst_1
    //   23: invokestatic getInstance : (IIIZ)Lcom/google/ftcresearch/tfod/tracking/ObjectTracker;
    //   26: astore #9
    //   28: aload_0
    //   29: aload #9
    //   31: putfield objectTracker : Lcom/google/ftcresearch/tfod/tracking/ObjectTracker;
    //   34: aload_0
    //   35: iload_1
    //   36: putfield frameWidth : I
    //   39: aload_0
    //   40: iload_2
    //   41: putfield frameHeight : I
    //   44: aload_0
    //   45: iload #4
    //   47: putfield sensorOrientation : I
    //   50: aload_0
    //   51: aload #8
    //   53: putfield cameraInformation : Lorg/firstinspires/ftc/robotcore/internal/tfod/CameraInformation;
    //   56: aload_0
    //   57: iconst_1
    //   58: putfield initialized : Z
    //   61: aload #9
    //   63: ifnonnull -> 75
    //   66: ldc 'MultiBoxTracker'
    //   68: ldc_w 'Object tracking support not found. See tensorflow/examples/android/README.md for details.'
    //   71: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   74: pop
    //   75: aload_0
    //   76: getfield objectTracker : Lcom/google/ftcresearch/tfod/tracking/ObjectTracker;
    //   79: astore #8
    //   81: aload #8
    //   83: ifnonnull -> 89
    //   86: aload_0
    //   87: monitorexit
    //   88: return
    //   89: aload_0
    //   90: getfield objectTracker : Lcom/google/ftcresearch/tfod/tracking/ObjectTracker;
    //   93: aload #5
    //   95: aconst_null
    //   96: lload #6
    //   98: aconst_null
    //   99: iconst_1
    //   100: invokevirtual nextFrame : ([B[BJ[FZ)V
    //   103: new java/util/LinkedList
    //   106: dup
    //   107: aload_0
    //   108: getfield trackedObjects : Ljava/util/List;
    //   111: invokespecial <init> : (Ljava/util/Collection;)V
    //   114: invokevirtual iterator : ()Ljava/util/Iterator;
    //   117: astore #5
    //   119: aload #5
    //   121: invokeinterface hasNext : ()Z
    //   126: ifeq -> 202
    //   129: aload #5
    //   131: invokeinterface next : ()Ljava/lang/Object;
    //   136: checkcast org/firstinspires/ftc/robotcore/internal/tfod/MultiBoxTracker$TrackedRecognition
    //   139: astore #8
    //   141: aload #8
    //   143: getfield trackedObject : Lcom/google/ftcresearch/tfod/tracking/ObjectTracker$TrackedObject;
    //   146: astore #9
    //   148: aload #9
    //   150: invokevirtual getCurrentCorrelation : ()F
    //   153: aload_0
    //   154: getfield params : Lorg/firstinspires/ftc/robotcore/internal/tfod/TfodParameters;
    //   157: getfield trackerMinCorrelation : F
    //   160: fcmpg
    //   161: ifge -> 119
    //   164: aload #9
    //   166: invokevirtual stopTracking : ()V
    //   169: aload_0
    //   170: getfield trackedObjects : Ljava/util/List;
    //   173: aload #8
    //   175: invokeinterface remove : (Ljava/lang/Object;)Z
    //   180: pop
    //   181: aload_0
    //   182: getfield availableColors : Ljava/util/Queue;
    //   185: aload #8
    //   187: getfield color : I
    //   190: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   193: invokeinterface add : (Ljava/lang/Object;)Z
    //   198: pop
    //   199: goto -> 119
    //   202: aload_0
    //   203: monitorexit
    //   204: return
    //   205: astore #5
    //   207: aload_0
    //   208: monitorexit
    //   209: aload #5
    //   211: athrow
    // Exception table:
    //   from	to	target	type
    //   2	61	205	finally
    //   66	75	205	finally
    //   75	81	205	finally
    //   89	119	205	finally
    //   119	199	205	finally
  }
  
  public void printResults() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield trackedObjects : Ljava/util/List;
    //   6: invokeinterface iterator : ()Ljava/util/Iterator;
    //   11: astore_1
    //   12: aload_1
    //   13: invokeinterface hasNext : ()Z
    //   18: ifeq -> 64
    //   21: aload_1
    //   22: invokeinterface next : ()Ljava/lang/Object;
    //   27: checkcast org/firstinspires/ftc/robotcore/internal/tfod/MultiBoxTracker$TrackedRecognition
    //   30: astore_2
    //   31: aload_0
    //   32: getfield objectTracker : Lcom/google/ftcresearch/tfod/tracking/ObjectTracker;
    //   35: ifnull -> 49
    //   38: aload_2
    //   39: getfield trackedObject : Lcom/google/ftcresearch/tfod/tracking/ObjectTracker$TrackedObject;
    //   42: invokevirtual getTrackedPositionInPreviewFrame : ()Landroid/graphics/RectF;
    //   45: pop
    //   46: goto -> 12
    //   49: new android/graphics/RectF
    //   52: dup
    //   53: aload_2
    //   54: getfield location : Landroid/graphics/RectF;
    //   57: invokespecial <init> : (Landroid/graphics/RectF;)V
    //   60: pop
    //   61: goto -> 12
    //   64: aload_0
    //   65: monitorexit
    //   66: return
    //   67: astore_1
    //   68: aload_0
    //   69: monitorexit
    //   70: aload_1
    //   71: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	67	finally
    //   12	46	67	finally
    //   49	61	67	finally
  }
  
  public void trackResults(List<Recognition> paramList, byte[] paramArrayOfbyte, long paramLong) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: lload_3
    //   4: aload_1
    //   5: aload_2
    //   6: invokespecial processResults : (JLjava/util/List;[B)V
    //   9: aload_0
    //   10: monitorexit
    //   11: return
    //   12: astore_1
    //   13: aload_0
    //   14: monitorexit
    //   15: aload_1
    //   16: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	12	finally
  }
  
  private static class TrackedRecognition {
    int color;
    
    float detectionConfidence;
    
    RectF location;
    
    String title;
    
    ObjectTracker.TrackedObject trackedObject;
    
    private TrackedRecognition() {}
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\tfod\MultiBoxTracker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
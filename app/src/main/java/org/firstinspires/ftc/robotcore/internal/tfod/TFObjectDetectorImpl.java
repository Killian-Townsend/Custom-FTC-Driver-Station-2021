package org.firstinspires.ftc.robotcore.internal.tfod;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.function.ContinuationResult;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamServer;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.tensorflow.lite.Interpreter;

public class TFObjectDetectorImpl implements TFObjectDetector {
  private static final String TAG = "TFObjectDetector";
  
  private AnnotatedYuvRgbFrame annotatedFrame;
  
  private final Object annotatedFrameLock;
  
  private final AppUtil appUtil;
  
  private Continuation<? extends Consumer<Bitmap>> bitmapContinuation;
  
  private final Object bitmapFrameLock;
  
  private final ClippingMargins clippingMargins;
  
  private FrameGenerator frameGenerator;
  
  private TfodFrameManager frameManager;
  
  private Thread frameManagerThread;
  
  private ImageView imageView;
  
  private FrameLayout.LayoutParams imageViewLayoutParams;
  
  private ViewGroup imageViewParent;
  
  private final List<Interpreter> interpreters;
  
  private final List<String> labels;
  
  private long lastReturnedFrameTime;
  
  private TfodParameters params;
  
  private Rate rate;
  
  private final int rotation;
  
  private VuforiaLocalizer vuforiaLocalizer;
  
  private final Zoom zoom;
  
  public TFObjectDetectorImpl(TFObjectDetector.Parameters paramParameters, VuforiaLocalizer paramVuforiaLocalizer) {
    Activity activity;
    this.appUtil = AppUtil.getInstance();
    this.interpreters = new ArrayList<Interpreter>();
    this.labels = new ArrayList<String>();
    this.clippingMargins = new ClippingMargins();
    this.zoom = new Zoom(1.0D, 1.7777777777777777D);
    this.annotatedFrameLock = new Object();
    this.lastReturnedFrameTime = 0L;
    this.bitmapFrameLock = new Object();
    this.params = makeTfodParameters(paramParameters);
    this.vuforiaLocalizer = paramVuforiaLocalizer;
    this.rate = new Rate(this.params.maxFrameRate);
    if (paramParameters.activity != null) {
      activity = paramParameters.activity;
    } else {
      activity = this.appUtil.getRootActivity();
    } 
    this.rotation = getRotation(activity, paramVuforiaLocalizer.getCameraName());
    this.frameGenerator = new VuforiaFrameGenerator(paramVuforiaLocalizer, this.rotation, this.clippingMargins);
    createImageViewIfRequested(activity, paramParameters);
    int i = 10;
    while (true) {
      if (i >= 0)
        try {
          this.annotatedFrame = new AnnotatedYuvRgbFrame(this.frameGenerator.getFrame(), new ArrayList<Recognition>());
          if (this.imageView != null)
            updateImageView(this.annotatedFrame); 
          CameraStreamServer.getInstance().setSource((CameraStreamSource)this);
          return;
        } catch (IllegalStateException illegalStateException) {
          Log.e("TFObjectDetector", "TFObjectDetectorImpl.<init> - could not get image from frame generator");
          if (i != 0) {
            i--;
            continue;
          } 
          throw illegalStateException;
        } catch (InterruptedException interruptedException) {
          throw new RuntimeException("TFObjectDetector constructor interrupted while getting first frame!");
        }  
      if (this.imageView != null)
        updateImageView(this.annotatedFrame); 
      CameraStreamServer.getInstance().setSource((CameraStreamSource)this);
      return;
    } 
  }
  
  private void createImageViewIfRequested(final Activity activity, TFObjectDetector.Parameters paramParameters) {
    if (paramParameters.tfodMonitorViewParent != null) {
      this.imageViewParent = paramParameters.tfodMonitorViewParent;
    } else if (paramParameters.tfodMonitorViewIdParent != 0) {
      this.imageViewParent = (ViewGroup)activity.findViewById(paramParameters.tfodMonitorViewIdParent);
    } 
    if (this.imageViewParent != null)
      this.appUtil.synchronousRunOnUiThread(new Runnable() {
            public void run() {
              TFObjectDetectorImpl.access$002(TFObjectDetectorImpl.this, new ImageView((Context)activity));
              TFObjectDetectorImpl.this.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
              TFObjectDetectorImpl.this.imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
              TFObjectDetectorImpl.access$102(TFObjectDetectorImpl.this, null);
              if (TFObjectDetectorImpl.this.rotation != 0)
                TFObjectDetectorImpl.this.imageView.setRotation((360 - TFObjectDetectorImpl.this.rotation)); 
              TFObjectDetectorImpl.this.imageViewParent.addView((View)TFObjectDetectorImpl.this.imageView);
              TFObjectDetectorImpl.this.imageViewParent.setVisibility(0);
            }
          }); 
  }
  
  private AnnotatedYuvRgbFrame getAnnotatedFrame() {
    synchronized (this.annotatedFrameLock) {
      return this.annotatedFrame;
    } 
  }
  
  private AnnotatedYuvRgbFrame getAnnotatedFrameAtRate() {
    this.rate.sleep();
    synchronized (this.annotatedFrameLock) {
      return this.annotatedFrame;
    } 
  }
  
  private static int getRotation(Activity paramActivity, CameraName paramCameraName) {
    // Byte code:
    //   0: aload_1
    //   1: instanceof org/firstinspires/ftc/robotcore/external/hardware/camera/BuiltinCameraName
    //   4: istore #6
    //   6: iconst_0
    //   7: istore #5
    //   9: iload #5
    //   11: istore_3
    //   12: iload #6
    //   14: ifeq -> 170
    //   17: aload_0
    //   18: invokevirtual getWindowManager : ()Landroid/view/WindowManager;
    //   21: invokeinterface getDefaultDisplay : ()Landroid/view/Display;
    //   26: invokevirtual getRotation : ()I
    //   29: istore_2
    //   30: iload_2
    //   31: ifeq -> 49
    //   34: iload_2
    //   35: iconst_1
    //   36: if_icmpeq -> 68
    //   39: iload_2
    //   40: iconst_2
    //   41: if_icmpeq -> 61
    //   44: iload_2
    //   45: iconst_3
    //   46: if_icmpeq -> 54
    //   49: iconst_0
    //   50: istore_2
    //   51: goto -> 71
    //   54: sipush #270
    //   57: istore_2
    //   58: goto -> 71
    //   61: sipush #180
    //   64: istore_2
    //   65: goto -> 71
    //   68: bipush #90
    //   70: istore_2
    //   71: aload_1
    //   72: checkcast org/firstinspires/ftc/robotcore/external/hardware/camera/BuiltinCameraName
    //   75: invokeinterface getCameraDirection : ()Lorg/firstinspires/ftc/robotcore/external/navigation/VuforiaLocalizer$CameraDirection;
    //   80: astore_0
    //   81: iconst_0
    //   82: istore #4
    //   84: iload #5
    //   86: istore_3
    //   87: iload #4
    //   89: invokestatic getNumberOfCameras : ()I
    //   92: if_icmpge -> 170
    //   95: new android/hardware/Camera$CameraInfo
    //   98: dup
    //   99: invokespecial <init> : ()V
    //   102: astore_1
    //   103: iload #4
    //   105: aload_1
    //   106: invokestatic getCameraInfo : (ILandroid/hardware/Camera$CameraInfo;)V
    //   109: aload_1
    //   110: getfield facing : I
    //   113: iconst_1
    //   114: if_icmpne -> 139
    //   117: aload_0
    //   118: getstatic org/firstinspires/ftc/robotcore/external/navigation/VuforiaLocalizer$CameraDirection.FRONT : Lorg/firstinspires/ftc/robotcore/external/navigation/VuforiaLocalizer$CameraDirection;
    //   121: if_acmpne -> 139
    //   124: iload_2
    //   125: ineg
    //   126: istore_2
    //   127: aload_1
    //   128: getfield orientation : I
    //   131: istore_3
    //   132: iload_2
    //   133: iload_3
    //   134: isub
    //   135: istore_3
    //   136: goto -> 170
    //   139: aload_1
    //   140: getfield facing : I
    //   143: ifne -> 161
    //   146: aload_0
    //   147: getstatic org/firstinspires/ftc/robotcore/external/navigation/VuforiaLocalizer$CameraDirection.BACK : Lorg/firstinspires/ftc/robotcore/external/navigation/VuforiaLocalizer$CameraDirection;
    //   150: if_acmpne -> 161
    //   153: aload_1
    //   154: getfield orientation : I
    //   157: istore_3
    //   158: goto -> 132
    //   161: iload #4
    //   163: iconst_1
    //   164: iadd
    //   165: istore #4
    //   167: goto -> 84
    //   170: iload_3
    //   171: ifge -> 183
    //   174: iload_3
    //   175: sipush #360
    //   178: iadd
    //   179: istore_3
    //   180: goto -> 170
    //   183: iload_3
    //   184: sipush #360
    //   187: irem
    //   188: ireturn
  }
  
  private AnnotatedYuvRgbFrame getUpdatedAnnotatedFrame() {
    synchronized (this.annotatedFrameLock) {
      if (this.annotatedFrame.getFrameTimeNanos() > this.lastReturnedFrameTime) {
        this.lastReturnedFrameTime = this.annotatedFrame.getFrameTimeNanos();
        return this.annotatedFrame;
      } 
      return null;
    } 
  }
  
  private void initialize(FileInputStream paramFileInputStream, long paramLong1, long paramLong2, String... paramVarArgs) throws IOException {
    int j;
    MappedByteBuffer mappedByteBuffer = paramFileInputStream.getChannel().map(FileChannel.MapMode.READ_ONLY, paramLong1, paramLong2);
    int k = paramVarArgs.length;
    byte b = 0;
    int i = 0;
    while (true) {
      j = b;
      if (i < k) {
        String str = paramVarArgs[i];
        this.labels.add(str);
        i++;
        continue;
      } 
      break;
    } 
    while (j < this.params.numExecutorThreads) {
      this.interpreters.add(new Interpreter(mappedByteBuffer, this.params.numInterpreterThreads));
      j++;
    } 
    this.frameManager = new TfodFrameManager(this.frameGenerator, this.interpreters, this.labels, this.params, this.zoom, new AnnotatedFrameCallback() {
          public void onResult(AnnotatedYuvRgbFrame param1AnnotatedYuvRgbFrame) {
            synchronized (TFObjectDetectorImpl.this.annotatedFrameLock) {
              TFObjectDetectorImpl.access$502(TFObjectDetectorImpl.this, param1AnnotatedYuvRgbFrame);
              if (TFObjectDetectorImpl.this.imageView != null)
                TFObjectDetectorImpl.this.updateImageView(param1AnnotatedYuvRgbFrame); 
              return;
            } 
          }
        });
    Thread thread = new Thread(this.frameManager, "FrameManager");
    this.frameManagerThread = thread;
    thread.start();
  }
  
  private static List<Recognition> makeRecognitionsList(AnnotatedYuvRgbFrame paramAnnotatedYuvRgbFrame) {
    return new ArrayList<Recognition>(paramAnnotatedYuvRgbFrame.getRecognitions());
  }
  
  private static TfodParameters makeTfodParameters(TFObjectDetector.Parameters paramParameters) {
    return (new TfodParameters.Builder(paramParameters.isModelQuantized, paramParameters.inputSize)).trackerDisable(paramParameters.useObjectTracker ^ true).numInterpreterThreads(paramParameters.numInterpreterThreads).numExecutorThreads(paramParameters.numExecutorThreads).maxNumDetections(paramParameters.maxNumDetections).timingBufferSize(paramParameters.timingBufferSize).maxFrameRate(paramParameters.maxFrameRate).minResultConfidence(paramParameters.minResultConfidence).trackerMaxOverlap(paramParameters.trackerMaxOverlap).trackerMinSize(paramParameters.trackerMinSize).trackerMarginalCorrelation(paramParameters.trackerMarginalCorrelation).trackerMinCorrelation(paramParameters.trackerMinCorrelation).build();
  }
  
  private void updateImageView(AnnotatedYuvRgbFrame paramAnnotatedYuvRgbFrame) {
    null = paramAnnotatedYuvRgbFrame.getFrame().getCopiedBitmap();
    Canvas canvas = new Canvas(null);
    TfodFrameManager tfodFrameManager = this.frameManager;
    if (tfodFrameManager != null)
      tfodFrameManager.drawDebug(canvas); 
    synchronized (this.bitmapFrameLock) {
      if (this.bitmapContinuation != null) {
        this.bitmapContinuation.dispatch(new ContinuationResult<Consumer<Bitmap>>() {
              public void handle(Consumer<Bitmap> param1Consumer) {
                param1Consumer.accept(bitmap);
              }
            });
        this.bitmapContinuation = null;
      } 
      this.appUtil.synchronousRunOnUiThread(new Runnable() {
            public void run() {
              if (TFObjectDetectorImpl.this.imageView != null) {
                if (TFObjectDetectorImpl.this.imageViewLayoutParams == null) {
                  double d2 = bitmap.getWidth();
                  double d4 = bitmap.getHeight();
                  double d3 = d2;
                  double d1 = d4;
                  if (TFObjectDetectorImpl.this.rotation % 180 != 0) {
                    d3 = d4;
                    d1 = d2;
                  } 
                  d4 = Math.min(TFObjectDetectorImpl.this.imageView.getWidth() / d3, TFObjectDetectorImpl.this.imageView.getHeight() / d1);
                  d2 = d3 * d4;
                  d3 = d1 * d4;
                  d4 = d2;
                  d1 = d3;
                  if (TFObjectDetectorImpl.this.rotation % 180 != 0) {
                    d1 = d2;
                    d4 = d3;
                  } 
                  TFObjectDetectorImpl.access$102(TFObjectDetectorImpl.this, new FrameLayout.LayoutParams((int)d4, (int)d1, 17));
                  TFObjectDetectorImpl.this.imageView.setLayoutParams((ViewGroup.LayoutParams)TFObjectDetectorImpl.this.imageViewLayoutParams);
                } 
                TFObjectDetectorImpl.this.imageView.setImageBitmap(bitmap);
                TFObjectDetectorImpl.this.imageView.invalidate();
              } 
            }
          });
      return;
    } 
  }
  
  public void activate() {
    TfodFrameManager tfodFrameManager = this.frameManager;
    if (tfodFrameManager != null)
      tfodFrameManager.activate(); 
  }
  
  public void deactivate() {
    TfodFrameManager tfodFrameManager = this.frameManager;
    if (tfodFrameManager != null)
      tfodFrameManager.deactivate(); 
  }
  
  public void getFrameBitmap(Continuation<? extends Consumer<Bitmap>> paramContinuation) {
    synchronized (this.bitmapFrameLock) {
      this.bitmapContinuation = paramContinuation;
      return;
    } 
  }
  
  public List<Recognition> getRecognitions() {
    return makeRecognitionsList(getAnnotatedFrame());
  }
  
  public List<Recognition> getUpdatedRecognitions() {
    AnnotatedYuvRgbFrame annotatedYuvRgbFrame = getUpdatedAnnotatedFrame();
    return (annotatedYuvRgbFrame == null) ? null : makeRecognitionsList(annotatedYuvRgbFrame);
  }
  
  public void loadModelFromAsset(String paramString, String... paramVarArgs) {
    try {
      AssetFileDescriptor assetFileDescriptor = AppUtil.getDefContext().getAssets().openFd(paramString);
      FileInputStream fileInputStream = assetFileDescriptor.createInputStream();
      try {
        initialize(fileInputStream, assetFileDescriptor.getStartOffset(), assetFileDescriptor.getDeclaredLength(), paramVarArgs);
        return;
      } finally {
        paramVarArgs = null;
      } 
    } catch (IOException iOException) {
      throw new RuntimeException("TFObjectDetector loadModelFromAsset failed", iOException);
    } 
  }
  
  public void loadModelFromFile(String paramString, String... paramVarArgs) {
    try {
      File file = new File(paramString);
      FileInputStream fileInputStream = new FileInputStream(paramString);
      try {
        initialize(fileInputStream, 0L, file.length(), paramVarArgs);
        return;
      } finally {
        paramVarArgs = null;
      } 
    } catch (IOException iOException) {
      throw new RuntimeException("TFObjectDetector loadModelFromFile failed", iOException);
    } 
  }
  
  public void setClippingMargins(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    synchronized (this.clippingMargins) {
      int i = this.rotation;
      if (i != 0) {
        if (i != 90) {
          if (i != 180) {
            if (i == 270) {
              this.clippingMargins.left = paramInt2;
              this.clippingMargins.top = paramInt3;
              this.clippingMargins.right = paramInt4;
              this.clippingMargins.bottom = paramInt1;
            } else {
              throw new IllegalStateException("rotation must be 0, 90, 180, or 270.");
            } 
          } else {
            this.clippingMargins.left = paramInt3;
            this.clippingMargins.top = paramInt4;
            this.clippingMargins.right = paramInt1;
            this.clippingMargins.bottom = paramInt2;
          } 
        } else {
          this.clippingMargins.left = paramInt4;
          this.clippingMargins.top = paramInt1;
          this.clippingMargins.right = paramInt2;
          this.clippingMargins.bottom = paramInt3;
        } 
      } else {
        this.clippingMargins.left = paramInt1;
        this.clippingMargins.top = paramInt2;
        this.clippingMargins.right = paramInt3;
        this.clippingMargins.bottom = paramInt4;
      } 
      return;
    } 
  }
  
  public void setZoom(double paramDouble1, double paramDouble2) {
    Zoom.validateArguments(paramDouble1, paramDouble2);
    synchronized (this.zoom) {
      this.zoom.magnification = paramDouble1;
      this.zoom.aspectRatio = paramDouble2;
      return;
    } 
  }
  
  public void shutdown() {
    Thread thread = Thread.currentThread();
    boolean bool = Thread.interrupted();
    deactivate();
    this.frameManagerThread.interrupt();
    try {
      this.frameManagerThread.join();
    } catch (InterruptedException interruptedException) {
      bool = true;
    } 
    if (this.imageView != null)
      this.appUtil.synchronousRunOnUiThread(new Runnable() {
            public void run() {
              TFObjectDetectorImpl.this.imageViewParent.removeView((View)TFObjectDetectorImpl.this.imageView);
              TFObjectDetectorImpl.this.imageViewParent.setVisibility(8);
            }
          }); 
    this.frameGenerator.shutdown();
    if (bool)
      thread.interrupt(); 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\tfod\TFObjectDetectorImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
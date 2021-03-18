package org.firstinspires.ftc.robotcore.internal.tfod;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import com.google.ftcresearch.tfod.util.ImageUtils;
import com.google.ftcresearch.tfod.util.Size;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.tensorflow.lite.Interpreter;

class TfodFrameManager implements Runnable {
  private static final String TAG = "TfodFrameManager";
  
  private static final Paint paint = new Paint();
  
  private static final Paint zoomPaint = new Paint();
  
  private volatile boolean active;
  
  private final Queue<Integer> availableIds;
  
  private final RollingAverage averageInferenceTime;
  
  private final CameraInformation cameraInformation;
  
  private final ExecutorService executor;
  
  private final FrameGenerator frameGenerator;
  
  private final List<Interpreter> interpreters;
  
  private final List<String> labels;
  
  private volatile AnnotatedYuvRgbFrame lastRecognizedFrame;
  
  private final Object lastRecognizedFrameLock;
  
  private long lastSubmittedFrameTimeNanos;
  
  private final List<float[]> numDetections;
  
  private final Matrix originalToTrackerTransform;
  
  private final List<float[][]> outputClasses;
  
  private final List<float[][][]> outputLocations;
  
  private final List<float[][]> outputScores;
  
  private final TfodParameters params;
  
  private final AnnotatedFrameCallback tfodCallback;
  
  private final MultiBoxTracker tracker;
  
  private final Size trackerFrameSize;
  
  private final Matrix trackerToOriginalTransform;
  
  private final Zoom zoom;
  
  static {
    paint.setColor(-65536);
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(10.0F);
    zoomPaint.setColor(-1);
    zoomPaint.setAlpha(128);
    zoomPaint.setStyle(Paint.Style.FILL);
  }
  
  TfodFrameManager(FrameGenerator paramFrameGenerator, List<Interpreter> paramList, List<String> paramList1, TfodParameters paramTfodParameters, Zoom paramZoom, AnnotatedFrameCallback paramAnnotatedFrameCallback) {
    MultiBoxTracker multiBoxTracker;
    int j;
    this.outputLocations = (List)new ArrayList<float>();
    this.outputClasses = (List)new ArrayList<float>();
    this.outputScores = (List)new ArrayList<float>();
    this.numDetections = (List)new ArrayList<float>();
    this.availableIds = new ConcurrentLinkedQueue<Integer>();
    this.lastRecognizedFrameLock = new Object();
    this.frameGenerator = paramFrameGenerator;
    this.cameraInformation = paramFrameGenerator.getCameraInformation();
    this.interpreters = paramList;
    this.labels = paramList1;
    this.params = paramTfodParameters;
    this.zoom = paramZoom;
    this.tfodCallback = paramAnnotatedFrameCallback;
    this.trackerFrameSize = calculateTrackerFrameSize(paramTfodParameters.inputSize, this.cameraInformation.size);
    this.originalToTrackerTransform = ImageUtils.transformBetweenImageSizes(this.cameraInformation.size, this.trackerFrameSize);
    this.trackerToOriginalTransform = ImageUtils.transformBetweenImageSizes(this.trackerFrameSize, this.cameraInformation.size);
    this.executor = Executors.newFixedThreadPool(paramTfodParameters.numExecutorThreads);
    this.averageInferenceTime = new RollingAverage(paramTfodParameters.timingBufferSize);
    if (paramTfodParameters.trackerDisable) {
      paramFrameGenerator = null;
    } else {
      multiBoxTracker = new MultiBoxTracker(paramTfodParameters);
    } 
    this.tracker = multiBoxTracker;
    byte b = 0;
    int i = 0;
    while (true) {
      j = b;
      if (i < paramTfodParameters.numExecutorThreads) {
        this.outputLocations.add((float[][][])Array.newInstance(float.class, new int[] { 1, paramTfodParameters.maxNumDetections, 4 }));
        this.outputClasses.add((float[][])Array.newInstance(float.class, new int[] { 1, paramTfodParameters.maxNumDetections }));
        this.outputScores.add((float[][])Array.newInstance(float.class, new int[] { 1, paramTfodParameters.maxNumDetections }));
        this.numDetections.add(new float[1]);
        i++;
        continue;
      } 
      break;
    } 
    while (j < paramTfodParameters.numExecutorThreads) {
      this.availableIds.add(Integer.valueOf(j));
      j++;
    } 
  }
  
  private static Size calculateTrackerFrameSize(int paramInt, Size paramSize) {
    int i = Math.min(paramSize.width, paramSize.height);
    int j = Math.max(paramSize.width, paramSize.height);
    long l1;
    for (l1 = (paramInt + 1);; l1++) {
      l2 = i;
      if (l1 >= l2 || j * l1 % l2 == 0L)
        break; 
    } 
    long l2 = j * l1 / l2;
    return (i == paramSize.width) ? new Size((int)l1, (int)l2) : new Size((int)l2, (int)l1);
  }
  
  private boolean enoughInterFrameTimeElapsed(long paramLong) {
    return (paramLong - this.lastSubmittedFrameTimeNanos >= (long)this.averageInferenceTime.get() / this.params.numExecutorThreads);
  }
  
  private void receiveNewRecognitions(AnnotatedYuvRgbFrame paramAnnotatedYuvRgbFrame) {
    synchronized (this.lastRecognizedFrameLock) {
      if (this.lastRecognizedFrame == null || paramAnnotatedYuvRgbFrame.getFrameTimeNanos() > this.lastRecognizedFrame.getFrameTimeNanos()) {
        this.lastRecognizedFrame = paramAnnotatedYuvRgbFrame;
        if (this.params.trackerDisable) {
          this.tfodCallback.onResult(paramAnnotatedYuvRgbFrame);
          return;
        } 
        null = new Timer(paramAnnotatedYuvRgbFrame.getTag());
        null.start("TfodFrameManager.receiveNewRecognitions - preprocessing for tracker update in receive");
        byte[] arrayOfByte = paramAnnotatedYuvRgbFrame.getFrame().getLuminosityArray(this.trackerFrameSize);
        List<Recognition> list = transformRecognitionLocations(paramAnnotatedYuvRgbFrame.getRecognitions(), this.originalToTrackerTransform);
        null.end();
        this.tracker.trackResults(list, arrayOfByte, paramAnnotatedYuvRgbFrame.getFrameTimeNanos());
        return;
      } 
      Log.w("TfodFrameManager", "Received an out of order recognition. Something is likely wrong!");
      return;
    } 
  }
  
  private void submitFrameToTracker(AnnotatedYuvRgbFrame paramAnnotatedYuvRgbFrame) {
    byte[] arrayOfByte = paramAnnotatedYuvRgbFrame.getFrame().getLuminosityArray(this.trackerFrameSize);
    long l = paramAnnotatedYuvRgbFrame.getFrameTimeNanos();
    this.tracker.onFrame(this.trackerFrameSize.width, this.trackerFrameSize.height, this.trackerFrameSize.width, 0, arrayOfByte, l, this.cameraInformation);
  }
  
  private void submitRecognitionTask(final AnnotatedYuvRgbFrame annotatedFrame) {
    final Integer interpreterId = this.availableIds.poll();
    if (integer != null) {
      Zoom zoom;
      RecognizeImageRunnable recognizeImageRunnable;
      synchronized (this.zoom) {
        double d1 = this.zoom.magnification;
        double d2 = this.zoom.aspectRatio;
        recognizeImageRunnable = new RecognizeImageRunnable(annotatedFrame, this.cameraInformation, this.interpreters.get(integer.intValue()), this.params, d1, d2, this.labels, this.outputLocations.get(integer.intValue()), this.outputClasses.get(integer.intValue()), this.outputScores.get(integer.intValue()), this.numDetections.get(integer.intValue()), new AnnotatedFrameCallback() {
              public void onResult(AnnotatedYuvRgbFrame param1AnnotatedYuvRgbFrame) {
                long l = System.nanoTime() - annotatedFrame.getFrameTimeNanos();
                TimeUnit.MILLISECONDS.convert(l, TimeUnit.NANOSECONDS);
                TfodFrameManager.this.averageInferenceTime.add(l);
                TfodFrameManager.this.receiveNewRecognitions(annotatedFrame);
                TfodFrameManager.this.availableIds.add(interpreterId);
              }
            });
        this.lastSubmittedFrameTimeNanos = annotatedFrame.getFrameTimeNanos();
        this.executor.submit(recognizeImageRunnable);
        return;
      } 
    } 
  }
  
  private List<Recognition> transformRecognitionLocations(List<Recognition> paramList, Matrix paramMatrix) {
    ArrayList<RecognitionImpl> arrayList = new ArrayList();
    for (RecognitionImpl recognitionImpl : paramList) {
      RectF rectF = recognitionImpl.getLocation();
      paramMatrix.mapRect(rectF);
      arrayList.add(new RecognitionImpl(recognitionImpl, rectF));
    } 
    return (List)arrayList;
  }
  
  void activate() {
    this.active = true;
  }
  
  void deactivate() {
    this.active = false;
  }
  
  void draw(Canvas paramCanvas) {
    if (!this.active)
      return; 
    if (this.zoom.isZoomed()) {
      Rect rect = this.zoom.getZoomArea(paramCanvas.getWidth(), paramCanvas.getHeight());
      paramCanvas.drawRect(0.0F, 0.0F, rect.right, rect.top, zoomPaint);
      paramCanvas.drawRect(rect.right, 0.0F, paramCanvas.getWidth(), rect.bottom, zoomPaint);
      paramCanvas.drawRect(rect.left, rect.bottom, paramCanvas.getWidth(), paramCanvas.getHeight(), zoomPaint);
      paramCanvas.drawRect(0.0F, rect.top, rect.left, paramCanvas.getHeight(), zoomPaint);
    } 
    if (!this.params.trackerDisable) {
      this.tracker.draw(paramCanvas);
      return;
    } 
    AnnotatedYuvRgbFrame annotatedYuvRgbFrame = this.lastRecognizedFrame;
    if (annotatedYuvRgbFrame != null) {
      Iterator<Recognition> iterator = annotatedYuvRgbFrame.getRecognitions().iterator();
      while (iterator.hasNext())
        paramCanvas.drawRect(((RecognitionImpl)iterator.next()).getLocation(), paint); 
    } 
  }
  
  void drawDebug(Canvas paramCanvas) {
    if (!this.active)
      return; 
    draw(paramCanvas);
    if (!this.params.trackerDisable)
      this.tracker.drawDebug(paramCanvas); 
  }
  
  public void run() {
    Timer timer = new Timer("TfodFrameManager");
    while (true) {
      if (!Thread.currentThread().isInterrupted()) {
        timer.start("TfodFrameManager.run - Waiting for frame");
        try {
          YuvRgbFrame yuvRgbFrame = this.frameGenerator.getFrame();
          timer.end();
          AnnotatedYuvRgbFrame annotatedYuvRgbFrame = new AnnotatedYuvRgbFrame(yuvRgbFrame, new ArrayList<Recognition>());
          if (!this.active) {
            this.tfodCallback.onResult(annotatedYuvRgbFrame);
            continue;
          } 
          if (enoughInterFrameTimeElapsed(yuvRgbFrame.getFrameTimeNanos())) {
            Timer timer1 = new Timer(yuvRgbFrame.getTag());
            timer1.start("TfodFrameManager.run - submitting recognition task");
            submitRecognitionTask(annotatedYuvRgbFrame);
            timer1.end();
          } 
          if (!this.params.trackerDisable) {
            submitFrameToTracker(annotatedYuvRgbFrame);
            this.tracker.printResults();
            Timer timer1 = new Timer(annotatedYuvRgbFrame.getTag());
            timer1.start("TfodFrameManager.run - preprocessing for tracker in main loop");
            List<Recognition> list = transformRecognitionLocations(this.tracker.getRecognitions(), this.trackerToOriginalTransform);
            timer1.end();
            this.tfodCallback.onResult(new AnnotatedYuvRgbFrame(yuvRgbFrame, list));
          } 
        } catch (IllegalStateException illegalStateException) {
          Log.e("TfodFrameManager", "TfodFrameManager.run - could not get image from frame generator");
        } catch (InterruptedException interruptedException) {
          Thread.currentThread().interrupt();
          return;
        } 
        continue;
      } 
      if (!this.executor.isShutdown()) {
        this.executor.shutdown();
        try {
          this.executor.awaitTermination(100L, TimeUnit.MILLISECONDS);
          return;
        } catch (InterruptedException interruptedException) {
          Thread.currentThread().interrupt();
        } 
      } 
      return;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\tfod\TfodFrameManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
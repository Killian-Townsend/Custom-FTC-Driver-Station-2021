package org.firstinspires.ftc.robotcore.internal.tfod;

import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.tensorflow.lite.Interpreter;

class RecognizeImageRunnable implements Runnable {
  private static final float IMAGE_MEAN = 128.0F;
  
  private static final float IMAGE_STD = 128.0F;
  
  private static final String TAG = "RecognizeImageRunnable";
  
  private final AnnotatedYuvRgbFrame annotatedFrame;
  
  private final AnnotatedFrameCallback callback;
  
  private final CameraInformation cameraInformation;
  
  private final Interpreter interpreter;
  
  private final List<String> labels;
  
  private final float[] numDetections;
  
  private final float[][] outputClasses;
  
  private final float[][][] outputLocations;
  
  private final float[][] outputScores;
  
  private final TfodParameters params;
  
  private final double zoomAspectRatio;
  
  private final double zoomMagnification;
  
  RecognizeImageRunnable(AnnotatedYuvRgbFrame paramAnnotatedYuvRgbFrame, CameraInformation paramCameraInformation, Interpreter paramInterpreter, TfodParameters paramTfodParameters, double paramDouble1, double paramDouble2, List<String> paramList, float[][][] paramArrayOffloat, float[][] paramArrayOffloat1, float[][] paramArrayOffloat2, float[] paramArrayOffloat3, AnnotatedFrameCallback paramAnnotatedFrameCallback) {
    this.annotatedFrame = paramAnnotatedYuvRgbFrame;
    this.cameraInformation = paramCameraInformation;
    this.interpreter = paramInterpreter;
    this.params = paramTfodParameters;
    this.zoomMagnification = paramDouble1;
    this.zoomAspectRatio = paramDouble2;
    this.labels = paramList;
    this.outputLocations = paramArrayOffloat;
    this.outputClasses = paramArrayOffloat1;
    this.outputScores = paramArrayOffloat2;
    this.numDetections = paramArrayOffloat3;
    this.callback = paramAnnotatedFrameCallback;
  }
  
  private RectF convertOutputLocationToDetectionBox(float[] paramArrayOffloat) {
    int i = this.annotatedFrame.getFrame().getWidth();
    int j = this.annotatedFrame.getFrame().getHeight();
    if (Zoom.isZoomed(this.zoomMagnification)) {
      Rect rect = Zoom.getZoomArea(this.zoomMagnification, this.zoomAspectRatio, i, j);
      return new RectF(paramArrayOffloat[1] * rect.width() + rect.left, paramArrayOffloat[0] * rect.height() + rect.top, paramArrayOffloat[3] * rect.width() + rect.left, paramArrayOffloat[2] * rect.height() + rect.top);
    } 
    float f1 = paramArrayOffloat[1];
    float f2 = i;
    float f3 = paramArrayOffloat[0];
    float f4 = j;
    return new RectF(f1 * f2, f3 * f4, paramArrayOffloat[3] * f2, paramArrayOffloat[2] * f4);
  }
  
  private void postprocessDetections() {
    for (int i = 0; i < this.params.maxNumDetections; i++) {
      float f = this.outputScores[0][i];
      if (f >= this.params.minResultConfidence) {
        int j = (int)this.outputClasses[0][i];
        if (j >= this.labels.size() || j < 0) {
          Log.w(this.annotatedFrame.getTag(), "RecognizeImageRunnable.postprocessDetections - got a recognition with an invalid / background label");
        } else {
          RectF rectF = convertOutputLocationToDetectionBox(this.outputLocations[0][i]);
          this.annotatedFrame.getRecognitions().add(new RecognitionImpl(this.cameraInformation, this.labels.get(j), f, rectF));
        } 
      } 
    } 
    Collections.sort(this.annotatedFrame.getRecognitions(), new Comparator<Recognition>() {
          public int compare(Recognition param1Recognition1, Recognition param1Recognition2) {
            return Float.compare(param1Recognition2.getConfidence(), param1Recognition1.getConfidence());
          }
        });
  }
  
  private ByteBuffer preprocessFrame() {
    int[] arrayOfInt = this.annotatedFrame.getFrame().getArgb8888Array(this.params.inputSize, this.zoomMagnification, this.zoomAspectRatio);
    if (this.params.isModelQuantized) {
      i = 1;
    } else {
      i = 4;
    } 
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(this.params.inputSize * this.params.inputSize * 3 * i);
    byteBuffer.order(ByteOrder.nativeOrder());
    byteBuffer.rewind();
    for (int i = 0; i < this.params.inputSize; i++) {
      for (int j = 0; j < this.params.inputSize; j++) {
        int k = arrayOfInt[this.params.inputSize * i + j];
        if (this.params.isModelQuantized) {
          byteBuffer.put((byte)(k >> 16 & 0xFF));
          byteBuffer.put((byte)(k >> 8 & 0xFF));
          byteBuffer.put((byte)(k & 0xFF));
        } else {
          byteBuffer.putFloat(((k >> 16 & 0xFF) - 128.0F) / 128.0F);
          byteBuffer.putFloat(((k >> 8 & 0xFF) - 128.0F) / 128.0F);
          byteBuffer.putFloat(((k & 0xFF) - 128.0F) / 128.0F);
        } 
      } 
    } 
    return byteBuffer;
  }
  
  public void run() {
    Timer timer = new Timer(this.annotatedFrame.getTag());
    timer.start("RecognizeImageRunnable.preprocessFrame");
    ByteBuffer byteBuffer = preprocessFrame();
    timer.end();
    timer.start("Interpreter.runForMultipleInputsOutputs");
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    hashMap.put(Integer.valueOf(0), this.outputLocations);
    hashMap.put(Integer.valueOf(1), this.outputClasses);
    hashMap.put(Integer.valueOf(2), this.outputScores);
    hashMap.put(Integer.valueOf(3), this.numDetections);
    this.interpreter.runForMultipleInputsOutputs(new Object[] { byteBuffer }, hashMap);
    timer.end();
    timer.start("RecognizeImageRunnable.postprocessDetections");
    postprocessDetections();
    timer.end();
    this.callback.onResult(this.annotatedFrame);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\tfod\RecognizeImageRunnable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
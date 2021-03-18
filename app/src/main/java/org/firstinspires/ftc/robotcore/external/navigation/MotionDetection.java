package org.firstinspires.ftc.robotcore.external.navigation;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;

public class MotionDetection implements SensorEventListener {
  private static final boolean DEBUG = false;
  
  private static final String TAG = "MotionDetection";
  
  private final double DEFAULT_DETECTION_THRESHOLD = 2.0D;
  
  private final int DEFAULT_RATE_LIMIT_SECONDS = 1;
  
  private double detectionThreshold = 2.0D;
  
  protected Vector gravity = new Vector();
  
  private CopyOnWriteArrayList<MotionDetectionListener> listeners = new CopyOnWriteArrayList<MotionDetectionListener>();
  
  private boolean listening = false;
  
  private int rateLimitSeconds;
  
  private Deadline rateLimiter = new Deadline(1L, TimeUnit.SECONDS);
  
  public MotionDetection() {}
  
  public MotionDetection(double paramDouble, int paramInt) {}
  
  protected Vector filter(SensorEvent paramSensorEvent) {
    Vector vector1 = new Vector();
    Vector vector2 = this.gravity;
    vector2.x = vector2.x * 0.8D + paramSensorEvent.values[0] * 0.19999999999999996D;
    vector2 = this.gravity;
    vector2.y = vector2.y * 0.8D + paramSensorEvent.values[1] * 0.19999999999999996D;
    vector2 = this.gravity;
    vector2.z = vector2.z * 0.8D + paramSensorEvent.values[2] * 0.19999999999999996D;
    vector1.x = paramSensorEvent.values[0] - this.gravity.x;
    vector1.y = paramSensorEvent.values[1] - this.gravity.y;
    vector1.z = paramSensorEvent.values[2] - this.gravity.z;
    return vector1;
  }
  
  public boolean isAvailable() {
    return ((SensorManager)AppUtil.getInstance().getRootActivity().getSystemService("sensor")).getSensorList(1).isEmpty() ^ true;
  }
  
  protected void notifyListeners(double paramDouble) {
    Iterator<MotionDetectionListener> iterator = this.listeners.iterator();
    while (iterator.hasNext())
      ((MotionDetectionListener)iterator.next()).onMotionDetected(paramDouble); 
  }
  
  public void onAccuracyChanged(Sensor paramSensor, int paramInt) {}
  
  public void onSensorChanged(SensorEvent paramSensorEvent) {
    double d = filter(paramSensorEvent).magnitude();
    if (d >= this.detectionThreshold && this.rateLimiter.hasExpired()) {
      this.rateLimiter.reset();
      notifyListeners(d);
    } 
  }
  
  public void purgeListeners() {
    this.listeners.clear();
  }
  
  public void registerListener(MotionDetectionListener paramMotionDetectionListener) {
    this.listeners.add(paramMotionDetectionListener);
  }
  
  public void startListening() {
    if (!this.listening) {
      SensorManager sensorManager = (SensorManager)AppUtil.getInstance().getRootActivity().getSystemService("sensor");
      sensorManager.registerListener(this, sensorManager.getDefaultSensor(1), 1);
      this.listening = true;
    } 
  }
  
  public void stopListening() {
    if (this.listening) {
      ((SensorManager)AppUtil.getInstance().getRootActivity().getSystemService("sensor")).unregisterListener(this);
      this.listening = false;
    } 
  }
  
  public static interface MotionDetectionListener {
    void onMotionDetected(double param1Double);
  }
  
  public class Vector {
    double x;
    
    double y;
    
    double z;
    
    public double magnitude() {
      double d1 = this.x;
      double d2 = this.y;
      double d3 = this.z;
      return Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\MotionDetection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
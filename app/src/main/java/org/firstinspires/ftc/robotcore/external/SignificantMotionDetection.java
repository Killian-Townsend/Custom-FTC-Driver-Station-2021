package org.firstinspires.ftc.robotcore.external;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class SignificantMotionDetection {
  private final TriggerListener listener;
  
  private CopyOnWriteArrayList<SignificantMotionDetectionListener> listeners = new CopyOnWriteArrayList<SignificantMotionDetectionListener>();
  
  private boolean listening;
  
  private final Sensor motion;
  
  private final SensorManager sensorManager;
  
  public SignificantMotionDetection() {
    SensorManager sensorManager = (SensorManager)AppUtil.getInstance().getRootActivity().getSystemService("sensor");
    this.sensorManager = sensorManager;
    this.motion = sensorManager.getDefaultSensor(17);
    this.listener = new TriggerListener();
  }
  
  protected void notifyListeners() {
    Iterator<SignificantMotionDetectionListener> iterator = this.listeners.iterator();
    while (iterator.hasNext())
      ((SignificantMotionDetectionListener)iterator.next()).onSignificantMotion(); 
  }
  
  public void registerListener(SignificantMotionDetectionListener paramSignificantMotionDetectionListener) {
    this.listeners.add(paramSignificantMotionDetectionListener);
  }
  
  public void startListening() {
    if (!this.listening) {
      this.sensorManager.requestTriggerSensor(this.listener, this.motion);
      this.listening = true;
    } 
  }
  
  public void stopListening() {
    if (this.listening) {
      this.sensorManager.cancelTriggerSensor(this.listener, this.motion);
      this.listening = false;
    } 
  }
  
  public static interface SignificantMotionDetectionListener {
    void onSignificantMotion();
  }
  
  private class TriggerListener extends TriggerEventListener {
    private TriggerListener() {}
    
    public void onTrigger(TriggerEvent param1TriggerEvent) {
      SignificantMotionDetection.this.notifyListeners();
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\SignificantMotionDetection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
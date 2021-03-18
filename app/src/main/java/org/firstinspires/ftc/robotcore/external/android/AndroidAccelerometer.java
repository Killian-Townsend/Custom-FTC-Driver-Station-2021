package org.firstinspires.ftc.robotcore.external.android;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class AndroidAccelerometer implements SensorEventListener {
  private volatile DistanceUnit distanceUnit = DistanceUnit.METER;
  
  private volatile boolean listening;
  
  private volatile long timestamp;
  
  private volatile float x;
  
  private volatile float y;
  
  private volatile float z;
  
  public Acceleration getAcceleration() {
    return (this.timestamp != 0L) ? (new Acceleration(DistanceUnit.METER, this.x, this.y, this.z, this.timestamp)).toUnit(this.distanceUnit) : null;
  }
  
  public DistanceUnit getDistanceUnit() {
    return this.distanceUnit;
  }
  
  public double getX() {
    return (this.timestamp != 0L) ? this.distanceUnit.fromMeters(this.x) : 0.0D;
  }
  
  public double getY() {
    return (this.timestamp != 0L) ? this.distanceUnit.fromMeters(this.y) : 0.0D;
  }
  
  public double getZ() {
    return (this.timestamp != 0L) ? this.distanceUnit.fromMeters(this.z) : 0.0D;
  }
  
  public boolean isAvailable() {
    return ((SensorManager)AppUtil.getInstance().getRootActivity().getSystemService("sensor")).getSensorList(1).isEmpty() ^ true;
  }
  
  public void onAccuracyChanged(Sensor paramSensor, int paramInt) {}
  
  public void onSensorChanged(SensorEvent paramSensorEvent) {
    this.timestamp = paramSensorEvent.timestamp;
    this.x = paramSensorEvent.values[0];
    this.y = paramSensorEvent.values[1];
    this.z = paramSensorEvent.values[2];
  }
  
  public void setDistanceUnit(DistanceUnit paramDistanceUnit) {
    if (paramDistanceUnit != null)
      this.distanceUnit = paramDistanceUnit; 
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
      this.timestamp = 0L;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\android\AndroidAccelerometer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.external.android;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class AndroidGyroscope implements SensorEventListener {
  private volatile AngleUnit angleUnit = AngleUnit.RADIANS;
  
  private volatile boolean listening;
  
  private volatile long timestamp;
  
  private volatile float x;
  
  private volatile float y;
  
  private volatile float z;
  
  public AngleUnit getAngleUnit() {
    return this.angleUnit;
  }
  
  public AngularVelocity getAngularVelocity() {
    return (this.timestamp != 0L) ? (new AngularVelocity(AngleUnit.RADIANS, this.x, this.y, this.z, this.timestamp)).toAngleUnit(this.angleUnit) : null;
  }
  
  public float getX() {
    return (this.timestamp != 0L) ? this.angleUnit.fromRadians(this.x) : 0.0F;
  }
  
  public float getY() {
    return (this.timestamp != 0L) ? this.angleUnit.fromRadians(this.y) : 0.0F;
  }
  
  public float getZ() {
    return (this.timestamp != 0L) ? this.angleUnit.fromRadians(this.z) : 0.0F;
  }
  
  public boolean isAvailable() {
    return ((SensorManager)AppUtil.getInstance().getRootActivity().getSystemService("sensor")).getSensorList(4).isEmpty() ^ true;
  }
  
  public void onAccuracyChanged(Sensor paramSensor, int paramInt) {}
  
  public void onSensorChanged(SensorEvent paramSensorEvent) {
    this.timestamp = paramSensorEvent.timestamp;
    this.x = paramSensorEvent.values[0];
    this.y = paramSensorEvent.values[1];
    this.z = paramSensorEvent.values[2];
  }
  
  public void setAngleUnit(AngleUnit paramAngleUnit) {
    if (paramAngleUnit != null)
      this.angleUnit = paramAngleUnit; 
  }
  
  public void startListening() {
    if (!this.listening) {
      SensorManager sensorManager = (SensorManager)AppUtil.getInstance().getRootActivity().getSystemService("sensor");
      sensorManager.registerListener(this, sensorManager.getDefaultSensor(4), 0);
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


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\android\AndroidGyroscope.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
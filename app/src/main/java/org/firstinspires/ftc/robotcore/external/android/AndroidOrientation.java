package org.firstinspires.ftc.robotcore.external.android;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.WindowManager;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class AndroidOrientation implements SensorEventListener {
  private final float[] acceleration = new float[3];
  
  private volatile AngleUnit angleUnit = AngleUnit.RADIANS;
  
  private volatile double azimuth;
  
  private final float[] inclinationMatrix = new float[9];
  
  private volatile boolean listening;
  
  private final float[] magneticField = new float[3];
  
  private final float[] orientation = new float[3];
  
  private volatile double pitch;
  
  private volatile double roll;
  
  private final float[] rotationMatrix = new float[9];
  
  private volatile long timestampAcceleration;
  
  private volatile long timestampMagneticField;
  
  private static double mod(double paramDouble1, double paramDouble2) {
    double d2 = paramDouble1 % paramDouble2;
    double d1 = d2;
    if (d2 != 0.0D) {
      if (Math.signum(paramDouble1) == Math.signum(paramDouble2))
        return d2; 
      d1 = d2 + paramDouble2;
    } 
    return d1;
  }
  
  private static double normalizeAzimuth(double paramDouble) {
    return mod(paramDouble, 6.283185307179586D);
  }
  
  private static double normalizePitch(double paramDouble) {
    return mod(paramDouble + Math.PI, 6.283185307179586D) - Math.PI;
  }
  
  private static double normalizeRoll(double paramDouble) {
    paramDouble = Math.max(Math.min(paramDouble, Math.PI), -3.141592653589793D);
    if (paramDouble >= -1.5707963267948966D && paramDouble <= 1.5707963267948966D)
      return paramDouble; 
    double d = Math.PI - paramDouble;
    paramDouble = d;
    if (d >= 4.71238898038469D)
      paramDouble = d - 6.283185307179586D; 
    return paramDouble;
  }
  
  public double getAngle() {
    if (this.timestampAcceleration != 0L && this.timestampMagneticField != 0L) {
      double d = Math.atan2(this.pitch, -this.roll);
      return this.angleUnit.fromRadians(d);
    } 
    return 0.0D;
  }
  
  public AngleUnit getAngleUnit() {
    return this.angleUnit;
  }
  
  public double getAzimuth() {
    return (this.timestampAcceleration != 0L && this.timestampMagneticField != 0L) ? this.angleUnit.fromRadians(this.azimuth) : 0.0D;
  }
  
  public double getMagnitude() {
    if (this.timestampAcceleration != 0L && this.timestampMagneticField != 0L) {
      double d1 = Math.min(1.5707963267948966D, Math.abs(this.pitch));
      double d2 = Math.min(1.5707963267948966D, Math.abs(this.roll));
      return 1.0D - Math.cos(d1) * Math.cos(d2);
    } 
    return 0.0D;
  }
  
  public double getPitch() {
    return (this.timestampAcceleration != 0L && this.timestampMagneticField != 0L) ? this.angleUnit.fromRadians(this.pitch) : 0.0D;
  }
  
  public double getRoll() {
    return (this.timestampAcceleration != 0L && this.timestampMagneticField != 0L) ? this.angleUnit.fromRadians(this.roll) : 0.0D;
  }
  
  public boolean isAvailable() {
    SensorManager sensorManager = (SensorManager)AppUtil.getInstance().getRootActivity().getSystemService("sensor");
    return (!sensorManager.getSensorList(1).isEmpty() && !sensorManager.getSensorList(2).isEmpty());
  }
  
  public void onAccuracyChanged(Sensor paramSensor, int paramInt) {}
  
  public void onSensorChanged(SensorEvent paramSensorEvent) {
    int i = paramSensorEvent.sensor.getType();
    if (i == 1) {
      this.timestampAcceleration = paramSensorEvent.timestamp;
      this.acceleration[0] = paramSensorEvent.values[0];
      this.acceleration[1] = paramSensorEvent.values[1];
      this.acceleration[2] = paramSensorEvent.values[2];
    } else if (i == 2) {
      this.timestampMagneticField = paramSensorEvent.timestamp;
      this.magneticField[0] = paramSensorEvent.values[0];
      this.magneticField[1] = paramSensorEvent.values[1];
      this.magneticField[2] = paramSensorEvent.values[2];
    } 
    if (this.timestampAcceleration != 0L && this.timestampMagneticField != 0L) {
      SensorManager.getRotationMatrix(this.rotationMatrix, this.inclinationMatrix, this.acceleration, this.magneticField);
      SensorManager.getOrientation(this.rotationMatrix, this.orientation);
      this.azimuth = normalizeAzimuth(this.orientation[0]);
      this.pitch = normalizePitch(this.orientation[1]);
      this.roll = normalizeRoll(-this.orientation[2]);
      i = ((WindowManager)AppUtil.getInstance().getRootActivity().getSystemService("window")).getDefaultDisplay().getRotation();
      if (i == 1) {
        double d = -this.pitch;
        this.pitch = -this.roll;
        this.roll = d;
        return;
      } 
      if (i == 2) {
        this.roll = -this.roll;
        return;
      } 
      if (i == 3) {
        double d = this.pitch;
        this.pitch = this.roll;
        this.roll = d;
      } 
    } 
  }
  
  public void setAngleUnit(AngleUnit paramAngleUnit) {
    if (paramAngleUnit != null)
      this.angleUnit = paramAngleUnit; 
  }
  
  public void startListening() {
    if (!this.listening) {
      SensorManager sensorManager = (SensorManager)AppUtil.getInstance().getRootActivity().getSystemService("sensor");
      sensorManager.registerListener(this, sensorManager.getDefaultSensor(1), 3);
      sensorManager.registerListener(this, sensorManager.getDefaultSensor(2), 3);
      this.listening = true;
    } 
  }
  
  public void stopListening() {
    if (this.listening) {
      ((SensorManager)AppUtil.getInstance().getRootActivity().getSystemService("sensor")).unregisterListener(this);
      this.listening = false;
      this.timestampAcceleration = 0L;
      this.timestampMagneticField = 0L;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\android\AndroidOrientation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
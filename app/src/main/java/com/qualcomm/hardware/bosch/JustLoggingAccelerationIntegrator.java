package com.qualcomm.hardware.bosch;

import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

public class JustLoggingAccelerationIntegrator implements BNO055IMU.AccelerationIntegrator {
  Acceleration acceleration;
  
  BNO055IMU.Parameters parameters;
  
  public Acceleration getAcceleration() {
    Acceleration acceleration2 = this.acceleration;
    Acceleration acceleration1 = acceleration2;
    if (acceleration2 == null)
      acceleration1 = new Acceleration(); 
    return acceleration1;
  }
  
  public Position getPosition() {
    return new Position();
  }
  
  public Velocity getVelocity() {
    return new Velocity();
  }
  
  public void initialize(BNO055IMU.Parameters paramParameters, Position paramPosition, Velocity paramVelocity) {
    this.parameters = paramParameters;
  }
  
  public void update(Acceleration paramAcceleration) {
    if (paramAcceleration.acquisitionTime != 0L) {
      Acceleration acceleration = this.acceleration;
      if (acceleration != null) {
        this.acceleration = paramAcceleration;
        if (this.parameters.loggingEnabled) {
          RobotLog.vv(this.parameters.loggingTag, "dt=%.3fs accel=%s", new Object[] { Double.valueOf((this.acceleration.acquisitionTime - acceleration.acquisitionTime) * 1.0E-9D), this.acceleration });
          return;
        } 
      } else {
        this.acceleration = paramAcceleration;
      } 
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\bosch\JustLoggingAccelerationIntegrator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
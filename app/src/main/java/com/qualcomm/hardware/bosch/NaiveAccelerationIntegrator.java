package com.qualcomm.hardware.bosch;

import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.NavUtil;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

public class NaiveAccelerationIntegrator implements BNO055IMU.AccelerationIntegrator {
  Acceleration acceleration = null;
  
  BNO055IMU.Parameters parameters = null;
  
  Position position = new Position();
  
  Velocity velocity = new Velocity();
  
  public Acceleration getAcceleration() {
    return this.acceleration;
  }
  
  public Position getPosition() {
    return this.position;
  }
  
  public Velocity getVelocity() {
    return this.velocity;
  }
  
  public void initialize(BNO055IMU.Parameters paramParameters, Position paramPosition, Velocity paramVelocity) {
    this.parameters = paramParameters;
    if (paramPosition == null)
      paramPosition = this.position; 
    this.position = paramPosition;
    if (paramVelocity == null)
      paramVelocity = this.velocity; 
    this.velocity = paramVelocity;
    this.acceleration = null;
  }
  
  public void update(Acceleration paramAcceleration) {
    if (paramAcceleration.acquisitionTime != 0L) {
      BNO055IMU.Parameters parameters;
      Acceleration acceleration = this.acceleration;
      if (acceleration != null) {
        Velocity velocity = this.velocity;
        this.acceleration = paramAcceleration;
        if (acceleration.acquisitionTime != 0L) {
          Velocity velocity1 = NavUtil.meanIntegrate(this.acceleration, acceleration);
          this.velocity = NavUtil.plus(this.velocity, velocity1);
        } 
        if (velocity.acquisitionTime != 0L) {
          Position position = NavUtil.meanIntegrate(this.velocity, velocity);
          this.position = NavUtil.plus(this.position, position);
        } 
        parameters = this.parameters;
        if (parameters != null && parameters.loggingEnabled) {
          RobotLog.vv(this.parameters.loggingTag, "dt=%.3fs accel=%s vel=%s pos=%s", new Object[] { Double.valueOf((this.acceleration.acquisitionTime - acceleration.acquisitionTime) * 1.0E-9D), this.acceleration, this.velocity, this.position });
          return;
        } 
      } else {
        this.acceleration = (Acceleration)parameters;
      } 
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\bosch\NaiveAccelerationIntegrator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
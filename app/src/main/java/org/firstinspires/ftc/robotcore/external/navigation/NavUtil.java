package org.firstinspires.ftc.robotcore.external.navigation;

public class NavUtil {
  public static Position integrate(Velocity paramVelocity, double paramDouble) {
    return new Position(paramVelocity.unit, paramVelocity.xVeloc * paramDouble, paramVelocity.yVeloc * paramDouble, paramVelocity.zVeloc * paramDouble, paramVelocity.acquisitionTime);
  }
  
  public static Velocity integrate(Acceleration paramAcceleration, double paramDouble) {
    return new Velocity(paramAcceleration.unit, paramAcceleration.xAccel * paramDouble, paramAcceleration.yAccel * paramDouble, paramAcceleration.zAccel * paramDouble, paramAcceleration.acquisitionTime);
  }
  
  public static Position meanIntegrate(Velocity paramVelocity1, Velocity paramVelocity2) {
    double d = (paramVelocity1.acquisitionTime - paramVelocity2.acquisitionTime);
    return integrate(scale(plus(paramVelocity1, paramVelocity2), 0.5D), d * 1.0E-9D);
  }
  
  public static Velocity meanIntegrate(Acceleration paramAcceleration1, Acceleration paramAcceleration2) {
    double d = (paramAcceleration1.acquisitionTime - paramAcceleration2.acquisitionTime);
    return integrate(scale(plus(paramAcceleration1, paramAcceleration2), 0.5D), d * 1.0E-9D);
  }
  
  public static Acceleration minus(Acceleration paramAcceleration1, Acceleration paramAcceleration2) {
    return new Acceleration(paramAcceleration1.unit, paramAcceleration1.xAccel - paramAcceleration1.unit.fromUnit(paramAcceleration2.unit, paramAcceleration2.xAccel), paramAcceleration1.yAccel - paramAcceleration1.unit.fromUnit(paramAcceleration2.unit, paramAcceleration2.yAccel), paramAcceleration1.zAccel - paramAcceleration1.unit.fromUnit(paramAcceleration2.unit, paramAcceleration2.zAccel), Math.max(paramAcceleration1.acquisitionTime, paramAcceleration2.acquisitionTime));
  }
  
  public static Position minus(Position paramPosition1, Position paramPosition2) {
    return new Position(paramPosition1.unit, paramPosition1.x - paramPosition1.unit.fromUnit(paramPosition2.unit, paramPosition2.x), paramPosition1.y - paramPosition1.unit.fromUnit(paramPosition2.unit, paramPosition2.y), paramPosition1.z - paramPosition1.unit.fromUnit(paramPosition2.unit, paramPosition2.z), Math.max(paramPosition1.acquisitionTime, paramPosition2.acquisitionTime));
  }
  
  public static Velocity minus(Velocity paramVelocity1, Velocity paramVelocity2) {
    return new Velocity(paramVelocity1.unit, paramVelocity1.xVeloc - paramVelocity1.unit.fromUnit(paramVelocity2.unit, paramVelocity2.xVeloc), paramVelocity1.yVeloc - paramVelocity1.unit.fromUnit(paramVelocity2.unit, paramVelocity2.yVeloc), paramVelocity1.zVeloc - paramVelocity1.unit.fromUnit(paramVelocity2.unit, paramVelocity2.zVeloc), Math.max(paramVelocity1.acquisitionTime, paramVelocity2.acquisitionTime));
  }
  
  public static Acceleration plus(Acceleration paramAcceleration1, Acceleration paramAcceleration2) {
    return new Acceleration(paramAcceleration1.unit, paramAcceleration1.xAccel + paramAcceleration1.unit.fromUnit(paramAcceleration2.unit, paramAcceleration2.xAccel), paramAcceleration1.yAccel + paramAcceleration1.unit.fromUnit(paramAcceleration2.unit, paramAcceleration2.yAccel), paramAcceleration1.zAccel + paramAcceleration1.unit.fromUnit(paramAcceleration2.unit, paramAcceleration2.zAccel), Math.max(paramAcceleration1.acquisitionTime, paramAcceleration2.acquisitionTime));
  }
  
  public static Position plus(Position paramPosition1, Position paramPosition2) {
    return new Position(paramPosition1.unit, paramPosition1.x + paramPosition1.unit.fromUnit(paramPosition2.unit, paramPosition2.x), paramPosition1.y + paramPosition1.unit.fromUnit(paramPosition2.unit, paramPosition2.y), paramPosition1.z + paramPosition1.unit.fromUnit(paramPosition2.unit, paramPosition2.z), Math.max(paramPosition1.acquisitionTime, paramPosition2.acquisitionTime));
  }
  
  public static Velocity plus(Velocity paramVelocity1, Velocity paramVelocity2) {
    return new Velocity(paramVelocity1.unit, paramVelocity1.xVeloc + paramVelocity1.unit.fromUnit(paramVelocity2.unit, paramVelocity2.xVeloc), paramVelocity1.yVeloc + paramVelocity1.unit.fromUnit(paramVelocity2.unit, paramVelocity2.yVeloc), paramVelocity1.zVeloc + paramVelocity1.unit.fromUnit(paramVelocity2.unit, paramVelocity2.zVeloc), Math.max(paramVelocity1.acquisitionTime, paramVelocity2.acquisitionTime));
  }
  
  public static Acceleration scale(Acceleration paramAcceleration, double paramDouble) {
    return new Acceleration(paramAcceleration.unit, paramAcceleration.xAccel * paramDouble, paramAcceleration.yAccel * paramDouble, paramAcceleration.zAccel * paramDouble, paramAcceleration.acquisitionTime);
  }
  
  public static Position scale(Position paramPosition, double paramDouble) {
    return new Position(paramPosition.unit, paramPosition.x * paramDouble, paramPosition.y * paramDouble, paramPosition.z * paramDouble, paramPosition.acquisitionTime);
  }
  
  public static Velocity scale(Velocity paramVelocity, double paramDouble) {
    return new Velocity(paramVelocity.unit, paramVelocity.xVeloc * paramDouble, paramVelocity.yVeloc * paramDouble, paramVelocity.zVeloc * paramDouble, paramVelocity.acquisitionTime);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\NavUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
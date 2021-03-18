package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareItem;
import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.BNO055IMUImpl;
import com.qualcomm.hardware.lynx.LynxEmbeddedIMU;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.util.ReadWriteFile;
import java.util.Iterator;
import java.util.Set;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Axis;
import org.firstinspires.ftc.robotcore.external.navigation.MagneticFlux;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
import org.firstinspires.ftc.robotcore.external.navigation.Temperature;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

class BNO055IMUAccess extends HardwareAccess<BNO055IMUImpl> {
  private final BNO055IMUImpl imu = this.hardwareDevice;
  
  BNO055IMUAccess(BlocksOpMode paramBlocksOpMode, HardwareItem paramHardwareItem, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramHardwareItem, paramHardwareMap, BNO055IMUImpl.class);
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"getAcceleration"})
  public Acceleration getAcceleration() {
    startBlockExecution(BlockType.GETTER, ".Acceleration");
    return this.imu.getAcceleration();
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"getAngularOrientation"})
  public Orientation getAngularOrientation() {
    startBlockExecution(BlockType.GETTER, ".AngularOrientation");
    return this.imu.getAngularOrientation();
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"getAngularOrientation"})
  public Orientation getAngularOrientation(String paramString1, String paramString2, String paramString3) {
    startBlockExecution(BlockType.FUNCTION, ".getAngularOrientation");
    AxesReference axesReference = checkAxesReference(paramString1);
    AxesOrder axesOrder = checkAxesOrder(paramString2);
    AngleUnit angleUnit = checkAngleUnit(paramString3);
    return (axesReference != null && axesOrder != null && angleUnit != null) ? this.imu.getAngularOrientation(axesReference, axesOrder, angleUnit) : null;
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"getAngularOrientationAxes"})
  public String getAngularOrientationAxes() {
    startBlockExecution(BlockType.GETTER, ".AngularOrientationAxes");
    Set set = this.imu.getAngularOrientationAxes();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[");
    Iterator<Axis> iterator = set.iterator();
    for (String str = ""; iterator.hasNext(); str = ",") {
      Axis axis = iterator.next();
      stringBuilder.append(str);
      stringBuilder.append("\"");
      stringBuilder.append(axis.toString());
      stringBuilder.append("\"");
    } 
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"getAngularVelocity"})
  public AngularVelocity getAngularVelocity() {
    startBlockExecution(BlockType.GETTER, ".AngularVelocity");
    return this.imu.getAngularVelocity();
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"getAngularVelocity"})
  public AngularVelocity getAngularVelocity(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".getAngularVelocity");
    AngleUnit angleUnit = checkAngleUnit(paramString);
    return (angleUnit != null) ? this.imu.getAngularVelocity(angleUnit) : null;
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"getAngularVelocityAxes"})
  public String getAngularVelocityAxes() {
    startBlockExecution(BlockType.GETTER, ".AngularVelocityAxes");
    Set set = this.imu.getAngularVelocityAxes();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[");
    Iterator<Axis> iterator = set.iterator();
    for (String str = ""; iterator.hasNext(); str = ",") {
      Axis axis = iterator.next();
      stringBuilder.append(str);
      stringBuilder.append("\"");
      stringBuilder.append(axis.toString());
      stringBuilder.append("\"");
    } 
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"getCalibrationStatus"})
  public String getCalibrationStatus() {
    startBlockExecution(BlockType.GETTER, ".CalibrationStatus");
    BNO055IMU.CalibrationStatus calibrationStatus = this.imu.getCalibrationStatus();
    return (calibrationStatus != null) ? calibrationStatus.toString() : "";
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"getGravity"})
  public Acceleration getGravity() {
    startBlockExecution(BlockType.GETTER, ".Gravity");
    return this.imu.getGravity();
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"getI2cAddress"})
  public int getI2cAddress7Bit() {
    startBlockExecution(BlockType.GETTER, ".I2cAddress7Bit");
    I2cAddr i2cAddr = this.imu.getI2cAddress();
    return (i2cAddr != null) ? i2cAddr.get7Bit() : 0;
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"getI2cAddress"})
  public int getI2cAddress8Bit() {
    startBlockExecution(BlockType.GETTER, ".I2cAddress8Bit");
    I2cAddr i2cAddr = this.imu.getI2cAddress();
    return (i2cAddr != null) ? i2cAddr.get8Bit() : 0;
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"getLinearAcceleration"})
  public Acceleration getLinearAcceleration() {
    startBlockExecution(BlockType.GETTER, ".LinearAcceleration");
    return this.imu.getLinearAcceleration();
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"getMagneticFieldStrength"})
  public MagneticFlux getMagneticFieldStrength() {
    startBlockExecution(BlockType.GETTER, ".MagneticFieldStrength");
    return this.imu.getMagneticFieldStrength();
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"getOverallAcceleration"})
  public Acceleration getOverallAcceleration() {
    startBlockExecution(BlockType.GETTER, ".OverallAcceleration");
    return this.imu.getOverallAcceleration();
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"getParameters"})
  public BNO055IMU.Parameters getParameters() {
    startBlockExecution(BlockType.GETTER, ".Parameters");
    return (BNO055IMU.Parameters)this.imu.getParameters();
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"getPosition"})
  public Position getPosition() {
    startBlockExecution(BlockType.GETTER, ".Position");
    return this.imu.getPosition();
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"getQuaternionOrientation"})
  public Quaternion getQuaternionOrientation() {
    startBlockExecution(BlockType.GETTER, ".QuaternionOrientation");
    return this.imu.getQuaternionOrientation();
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"getSystemError"})
  public String getSystemError() {
    startBlockExecution(BlockType.GETTER, ".SystemError");
    BNO055IMU.SystemError systemError = this.imu.getSystemError();
    return (systemError != null) ? systemError.toString() : "";
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"getSystemStatus"})
  public String getSystemStatus() {
    startBlockExecution(BlockType.GETTER, ".SystemStatus");
    BNO055IMU.SystemStatus systemStatus = this.imu.getSystemStatus();
    return (systemStatus != null) ? systemStatus.toString() : "";
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"getTemperature"})
  public Temperature getTemperature() {
    startBlockExecution(BlockType.GETTER, ".Temperature");
    return this.imu.getTemperature();
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"getVelocity"})
  public Velocity getVelocity() {
    startBlockExecution(BlockType.GETTER, ".Velocity");
    return this.imu.getVelocity();
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"initialize"})
  public void initialize(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".initialize");
    paramObject = checkBNO055IMUParameters(paramObject);
    if (paramObject != null)
      this.imu.initialize(paramObject); 
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"isAccelerometerCalibrated"})
  public boolean isAccelerometerCalibrated() {
    startBlockExecution(BlockType.FUNCTION, ".isAccelerometerCalibrated");
    return this.imu.isAccelerometerCalibrated();
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"isGyroCalibrated"})
  public boolean isGyroCalibrated() {
    startBlockExecution(BlockType.FUNCTION, ".isGyroCalibrated");
    return this.imu.isGyroCalibrated();
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"isMagnetometerCalibrated"})
  public boolean isMagnetometerCalibrated() {
    startBlockExecution(BlockType.FUNCTION, ".isMagnetometerCalibrated");
    return this.imu.isMagnetometerCalibrated();
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"isSystemCalibrated"})
  public boolean isSystemCalibrated() {
    startBlockExecution(BlockType.FUNCTION, ".isSystemCalibrated");
    return this.imu.isSystemCalibrated();
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"readCalibrationData"})
  public void saveCalibrationData(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".saveCalibrationData");
    ReadWriteFile.writeFile(AppUtil.getInstance().getSettingsFile(paramString), this.imu.readCalibrationData().serialize());
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"setI2cAddress"})
  public void setI2cAddress7Bit(int paramInt) {
    startBlockExecution(BlockType.SETTER, ".I2cAddress7Bit");
    this.imu.setI2cAddress(I2cAddr.create7bit(paramInt));
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"setI2cAddress"})
  public void setI2cAddress8Bit(int paramInt) {
    startBlockExecution(BlockType.SETTER, ".I2cAddress8Bit");
    this.imu.setI2cAddress(I2cAddr.create8bit(paramInt));
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"startAccelerationIntegration"})
  public void startAccelerationIntegration_with1(int paramInt) {
    startBlockExecution(BlockType.FUNCTION, ".startAccelerationIntegration");
    this.imu.startAccelerationIntegration(null, null, paramInt);
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"startAccelerationIntegration"})
  public void startAccelerationIntegration_with3(Object paramObject1, Object paramObject2, int paramInt) {
    startBlockExecution(BlockType.FUNCTION, ".startAccelerationIntegration");
    paramObject1 = checkArg(paramObject1, Position.class, "initialPosition");
    paramObject2 = checkArg(paramObject2, Velocity.class, "initialVelocity");
    if (paramObject1 != null && paramObject2 != null)
      this.imu.startAccelerationIntegration((Position)paramObject1, (Velocity)paramObject2, paramInt); 
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitBNO055IMU.class, LynxEmbeddedIMU.class}, methodName = {"stopAccelerationIntegration"})
  public void stopAccelerationIntegration() {
    startBlockExecution(BlockType.FUNCTION, ".stopAccelerationIntegration");
    this.imu.stopAccelerationIntegration();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\BNO055IMUAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
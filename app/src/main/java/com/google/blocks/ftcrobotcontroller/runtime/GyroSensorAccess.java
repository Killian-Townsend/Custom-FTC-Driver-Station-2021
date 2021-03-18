package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareItem;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtGyroSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.OrientationSensor;
import java.util.Iterator;
import java.util.Set;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Axis;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

class GyroSensorAccess extends HardwareAccess<GyroSensor> {
  private final GyroSensor gyroSensor = this.hardwareDevice;
  
  GyroSensorAccess(BlocksOpMode paramBlocksOpMode, HardwareItem paramHardwareItem, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramHardwareItem, paramHardwareMap, GyroSensor.class);
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtGyroSensor.class, ModernRoboticsI2cGyro.class}, methodName = {"calibrate"})
  public void calibrate() {
    startBlockExecution(BlockType.FUNCTION, ".calibrate");
    this.gyroSensor.calibrate();
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cGyro.class}, methodName = {"getAngularOrientation"})
  public Orientation getAngularOrientation(String paramString1, String paramString2, String paramString3) {
    startBlockExecution(BlockType.FUNCTION, ".getAngularOrientation");
    AxesReference axesReference = checkAxesReference(paramString1);
    AxesOrder axesOrder = checkAxesOrder(paramString2);
    AngleUnit angleUnit = checkAngleUnit(paramString3);
    if (axesReference != null && axesOrder != null && angleUnit != null) {
      GyroSensor gyroSensor = this.gyroSensor;
      if (gyroSensor instanceof OrientationSensor)
        return ((OrientationSensor)gyroSensor).getAngularOrientation(axesReference, axesOrder, angleUnit); 
      reportWarning("This GyroSensor is not a OrientationSensor.");
      return new Orientation(axesReference, axesOrder, angleUnit, 0.0F, 0.0F, 0.0F, 0L);
    } 
    return null;
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cGyro.class}, methodName = {"getAngularOrientationAxes"})
  public String getAngularOrientationAxes() {
    startBlockExecution(BlockType.GETTER, ".AngularOrientationAxes");
    GyroSensor gyroSensor = this.gyroSensor;
    if (gyroSensor instanceof OrientationSensor) {
      Set set = ((OrientationSensor)gyroSensor).getAngularOrientationAxes();
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
    reportWarning("This GyroSensor is not a OrientationSensor.");
    return "[]";
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtGyroSensor.class, ModernRoboticsI2cGyro.class}, methodName = {"getAngularVelocity"})
  public AngularVelocity getAngularVelocity(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".getAngularVelocity");
    AngleUnit angleUnit = checkAngleUnit(paramString);
    if (angleUnit != null) {
      GyroSensor gyroSensor = this.gyroSensor;
      if (gyroSensor instanceof Gyroscope)
        return ((Gyroscope)gyroSensor).getAngularVelocity(angleUnit); 
      reportWarning("This GyroSensor is not a Gyroscope.");
      return new AngularVelocity(angleUnit, 0.0F, 0.0F, 0.0F, 0L);
    } 
    return null;
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtGyroSensor.class, ModernRoboticsI2cGyro.class}, methodName = {"getAngularVelocityAxes"})
  public String getAngularVelocityAxes() {
    startBlockExecution(BlockType.GETTER, ".AngularVelocityAxes");
    GyroSensor gyroSensor = this.gyroSensor;
    if (gyroSensor instanceof Gyroscope) {
      Set set = ((Gyroscope)gyroSensor).getAngularVelocityAxes();
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
    reportWarning("This GyroSensor is not a Gyroscope.");
    return "[]";
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtGyroSensor.class, ModernRoboticsI2cGyro.class}, methodName = {"getHeading"})
  public int getHeading() {
    startBlockExecution(BlockType.GETTER, ".Heading");
    return this.gyroSensor.getHeading();
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cGyro.class}, methodName = {"getHeadingMode"})
  public String getHeadingMode() {
    startBlockExecution(BlockType.GETTER, ".HeadingMode");
    GyroSensor gyroSensor = this.gyroSensor;
    if (gyroSensor instanceof ModernRoboticsI2cGyro) {
      ModernRoboticsI2cGyro.HeadingMode headingMode = ((ModernRoboticsI2cGyro)gyroSensor).getHeadingMode();
      if (headingMode != null)
        return headingMode.toString(); 
    } else {
      reportWarning("This GyroSensor is not a ModernRoboticsI2cGyro.");
    } 
    return "";
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cGyro.class}, methodName = {"getI2cAddress"})
  public int getI2cAddress7Bit() {
    startBlockExecution(BlockType.GETTER, ".I2cAddress7Bit");
    GyroSensor gyroSensor = this.gyroSensor;
    if (gyroSensor instanceof ModernRoboticsI2cGyro) {
      I2cAddr i2cAddr = ((ModernRoboticsI2cGyro)gyroSensor).getI2cAddress();
      if (i2cAddr != null)
        return i2cAddr.get7Bit(); 
    } else {
      reportWarning("This GyroSensor is not a ModernRoboticsI2cGyro.");
    } 
    return 0;
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cGyro.class}, methodName = {"getI2cAddress"})
  public int getI2cAddress8Bit() {
    startBlockExecution(BlockType.GETTER, ".I2cAddress8Bit");
    GyroSensor gyroSensor = this.gyroSensor;
    if (gyroSensor instanceof ModernRoboticsI2cGyro) {
      I2cAddr i2cAddr = ((ModernRoboticsI2cGyro)gyroSensor).getI2cAddress();
      if (i2cAddr != null)
        return i2cAddr.get8Bit(); 
    } else {
      reportWarning("This GyroSensor is not a ModernRoboticsI2cGyro.");
    } 
    return 0;
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cGyro.class}, methodName = {"getIntegratedZValue"})
  public int getIntegratedZValue() {
    startBlockExecution(BlockType.GETTER, ".IntegratedZValue");
    GyroSensor gyroSensor = this.gyroSensor;
    if (gyroSensor instanceof ModernRoboticsI2cGyro)
      return ((ModernRoboticsI2cGyro)gyroSensor).getIntegratedZValue(); 
    reportWarning("This GyroSensor is not a ModernRoboticsI2cGyro.");
    return 0;
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtGyroSensor.class, ModernRoboticsI2cGyro.class}, methodName = {"rawX"})
  public int getRawX() {
    startBlockExecution(BlockType.GETTER, ".RawX");
    return this.gyroSensor.rawX();
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtGyroSensor.class, ModernRoboticsI2cGyro.class}, methodName = {"rawY"})
  public int getRawY() {
    startBlockExecution(BlockType.GETTER, ".RawY");
    return this.gyroSensor.rawY();
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtGyroSensor.class, ModernRoboticsI2cGyro.class}, methodName = {"rawZ"})
  public int getRawZ() {
    startBlockExecution(BlockType.GETTER, ".RawZ");
    return this.gyroSensor.rawZ();
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtGyroSensor.class}, methodName = {"getRotationFraction"})
  public double getRotationFraction() {
    startBlockExecution(BlockType.GETTER, ".RotationFraction");
    return this.gyroSensor.getRotationFraction();
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtGyroSensor.class, ModernRoboticsI2cGyro.class}, methodName = {"isCalibrating"})
  public boolean isCalibrating() {
    startBlockExecution(BlockType.FUNCTION, ".isCalibrating");
    return this.gyroSensor.isCalibrating();
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtGyroSensor.class, ModernRoboticsI2cGyro.class}, methodName = {"resetZAxisIntegrator"})
  public void resetZAxisIntegrator() {
    startBlockExecution(BlockType.FUNCTION, ".resetZAxisIntegrator");
    this.gyroSensor.resetZAxisIntegrator();
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cGyro.class}, methodName = {"setHeadingMode"})
  public void setHeadingMode(String paramString) {
    startBlockExecution(BlockType.SETTER, ".HeadingMode");
    ModernRoboticsI2cGyro.HeadingMode headingMode = (ModernRoboticsI2cGyro.HeadingMode)checkArg(paramString, ModernRoboticsI2cGyro.HeadingMode.class, "");
    if (headingMode != null) {
      GyroSensor gyroSensor = this.gyroSensor;
      if (gyroSensor instanceof ModernRoboticsI2cGyro) {
        ((ModernRoboticsI2cGyro)gyroSensor).setHeadingMode(headingMode);
        return;
      } 
      reportWarning("This GyroSensor is not a ModernRoboticsI2cGyro.");
    } 
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cGyro.class}, methodName = {"setI2cAddress"})
  public void setI2cAddress7Bit(int paramInt) {
    startBlockExecution(BlockType.SETTER, ".I2cAddress7Bit");
    GyroSensor gyroSensor = this.gyroSensor;
    if (gyroSensor instanceof ModernRoboticsI2cGyro) {
      ((ModernRoboticsI2cGyro)gyroSensor).setI2cAddress(I2cAddr.create7bit(paramInt));
      return;
    } 
    reportWarning("This GyroSensor is not a ModernRoboticsI2cGyro.");
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cGyro.class}, methodName = {"setI2cAddress"})
  public void setI2cAddress8Bit(int paramInt) {
    startBlockExecution(BlockType.SETTER, ".I2cAddress8Bit");
    GyroSensor gyroSensor = this.gyroSensor;
    if (gyroSensor instanceof ModernRoboticsI2cGyro) {
      ((ModernRoboticsI2cGyro)gyroSensor).setI2cAddress(I2cAddr.create8bit(paramInt));
      return;
    } 
    reportWarning("This GyroSensor is not a ModernRoboticsI2cGyro.");
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\GyroSensorAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
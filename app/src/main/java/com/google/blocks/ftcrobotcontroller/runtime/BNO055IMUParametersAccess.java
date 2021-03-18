package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.I2cAddr;

class BNO055IMUParametersAccess extends Access {
  BNO055IMUParametersAccess(BlocksOpMode paramBlocksOpMode, String paramString) {
    super(paramBlocksOpMode, paramString, "IMU-BNO055.Parameters");
  }
  
  @JavascriptInterface
  @Block(classes = {BNO055IMU.Parameters.class}, constructor = true)
  public BNO055IMU.Parameters create() {
    startBlockExecution(BlockType.CREATE, "");
    return new BNO055IMU.Parameters();
  }
  
  @JavascriptInterface
  @Block(classes = {BNO055IMU.Parameters.class}, fieldName = {"accelUnit"})
  public String getAccelUnit(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".AccelUnit");
    paramObject = checkBNO055IMUParameters(paramObject);
    if (paramObject != null) {
      paramObject = ((BNO055IMU.Parameters)paramObject).accelUnit;
      if (paramObject != null)
        return paramObject.toString(); 
    } 
    return "";
  }
  
  @JavascriptInterface
  @Block(classes = {BNO055IMU.Parameters.class}, fieldName = {"accelerationIntegrationAlgorithm"})
  public String getAccelerationIntegrationAlgorithm(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".AccelerationIntegrationAlgorithm");
    paramObject = checkBNO055IMUParameters(paramObject);
    if (paramObject != null) {
      paramObject = ((BNO055IMU.Parameters)paramObject).accelerationIntegrationAlgorithm;
      if (paramObject == null || paramObject instanceof com.qualcomm.hardware.bosch.NaiveAccelerationIntegrator)
        return "NAIVE"; 
      if (paramObject instanceof JustLoggingAccelerationIntegrator)
        return "JUST_LOGGING"; 
    } 
    return "";
  }
  
  @JavascriptInterface
  @Block(classes = {BNO055IMU.Parameters.class}, fieldName = {"angleUnit"})
  public String getAngleUnit(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".AngleUnit");
    paramObject = checkBNO055IMUParameters(paramObject);
    if (paramObject != null) {
      paramObject = ((BNO055IMU.Parameters)paramObject).angleUnit;
      if (paramObject != null)
        return paramObject.toString(); 
    } 
    return "";
  }
  
  @JavascriptInterface
  @Block(classes = {BNO055IMU.Parameters.class}, fieldName = {"calibrationDataFile"})
  public String getCalibrationDataFile(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".CalibrationDataFile");
    paramObject = checkBNO055IMUParameters(paramObject);
    if (paramObject != null) {
      paramObject = ((BNO055IMU.Parameters)paramObject).calibrationDataFile;
      if (paramObject != null)
        return (String)paramObject; 
    } 
    return "";
  }
  
  @JavascriptInterface
  @Block(classes = {BNO055IMU.Parameters.class}, fieldName = {"i2cAddr"})
  public int getI2cAddress7Bit(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".I2cAddress7Bit");
    paramObject = checkBNO055IMUParameters(paramObject);
    if (paramObject != null) {
      paramObject = ((BNO055IMU.Parameters)paramObject).i2cAddr;
      if (paramObject != null)
        return paramObject.get7Bit(); 
    } 
    return 0;
  }
  
  @JavascriptInterface
  @Block(classes = {BNO055IMU.Parameters.class}, fieldName = {"i2cAddr"})
  public int getI2cAddress8Bit(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".I2cAddress8Bit");
    paramObject = checkBNO055IMUParameters(paramObject);
    if (paramObject != null) {
      paramObject = ((BNO055IMU.Parameters)paramObject).i2cAddr;
      if (paramObject != null)
        return paramObject.get8Bit(); 
    } 
    return 0;
  }
  
  @JavascriptInterface
  @Block(classes = {BNO055IMU.Parameters.class}, fieldName = {"loggingEnabled"})
  public boolean getLoggingEnabled(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".LoggingEnabled");
    paramObject = checkBNO055IMUParameters(paramObject);
    return (paramObject != null) ? ((BNO055IMU.Parameters)paramObject).loggingEnabled : false;
  }
  
  @JavascriptInterface
  @Block(classes = {BNO055IMU.Parameters.class}, fieldName = {"loggingTag"})
  public String getLoggingTag(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".LoggingTag");
    paramObject = checkBNO055IMUParameters(paramObject);
    if (paramObject != null) {
      paramObject = ((BNO055IMU.Parameters)paramObject).loggingTag;
      if (paramObject != null)
        return (String)paramObject; 
    } 
    return "";
  }
  
  @JavascriptInterface
  @Block(classes = {BNO055IMU.Parameters.class}, fieldName = {"mode"})
  public String getSensorMode(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".SensorMode");
    paramObject = checkBNO055IMUParameters(paramObject);
    if (paramObject != null) {
      paramObject = ((BNO055IMU.Parameters)paramObject).mode;
      if (paramObject != null)
        return paramObject.toString(); 
    } 
    return "";
  }
  
  @JavascriptInterface
  @Block(classes = {BNO055IMU.Parameters.class}, fieldName = {"temperatureUnit"})
  public String getTempUnit(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".TempUnit");
    paramObject = checkBNO055IMUParameters(paramObject);
    if (paramObject != null) {
      paramObject = ((BNO055IMU.Parameters)paramObject).temperatureUnit;
      if (paramObject != null)
        return paramObject.toString(); 
    } 
    return "";
  }
  
  @JavascriptInterface
  @Block(classes = {BNO055IMU.Parameters.class}, fieldName = {"accelUnit"})
  public void setAccelUnit(Object paramObject, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".setAccelUnit");
    paramObject = checkBNO055IMUParameters(paramObject);
    BNO055IMU.AccelUnit accelUnit = checkArg(paramString, BNO055IMU.AccelUnit.class, "accelUnit");
    if (paramObject != null && accelUnit != null)
      ((BNO055IMU.Parameters)paramObject).accelUnit = accelUnit; 
  }
  
  @JavascriptInterface
  @Block(classes = {BNO055IMU.Parameters.class}, fieldName = {"accelerationIntegrationAlgorithm"})
  public void setAccelerationIntegrationAlgorithm(Object paramObject, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".setAccelerationIntegrationAlgorithm");
    paramObject = checkBNO055IMUParameters(paramObject);
    Algorithm algorithm = checkArg(paramString, Algorithm.class, "accelerationIntegrationAlgorithm");
    if (paramObject != null && algorithm != null) {
      int i = null.$SwitchMap$com$google$blocks$ftcrobotcontroller$runtime$BNO055IMUParametersAccess$Algorithm[algorithm.ordinal()];
      if (i != 1) {
        if (i != 2)
          return; 
        ((BNO055IMU.Parameters)paramObject).accelerationIntegrationAlgorithm = (BNO055IMU.AccelerationIntegrator)new JustLoggingAccelerationIntegrator();
        return;
      } 
      ((BNO055IMU.Parameters)paramObject).accelerationIntegrationAlgorithm = null;
    } 
  }
  
  @JavascriptInterface
  @Block(classes = {BNO055IMU.Parameters.class}, fieldName = {"angleUnit"})
  public void setAngleUnit(Object paramObject, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".setAngleUnit");
    paramObject = checkBNO055IMUParameters(paramObject);
    BNO055IMU.AngleUnit angleUnit = checkArg(paramString, BNO055IMU.AngleUnit.class, "angleUnit");
    if (paramObject != null && angleUnit != null)
      ((BNO055IMU.Parameters)paramObject).angleUnit = angleUnit; 
  }
  
  @JavascriptInterface
  @Block(classes = {BNO055IMU.Parameters.class}, fieldName = {"calibrationDataFile"})
  public void setCalibrationDataFile(Object paramObject, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".setCalibrationDataFile");
    paramObject = checkBNO055IMUParameters(paramObject);
    if (paramObject != null)
      ((BNO055IMU.Parameters)paramObject).calibrationDataFile = paramString; 
  }
  
  @JavascriptInterface
  @Block(classes = {BNO055IMU.Parameters.class}, fieldName = {"i2cAddr"})
  public void setI2cAddress7Bit(Object paramObject, int paramInt) {
    startBlockExecution(BlockType.FUNCTION, ".setI2cAddress7Bit");
    paramObject = checkBNO055IMUParameters(paramObject);
    if (paramObject != null)
      ((BNO055IMU.Parameters)paramObject).i2cAddr = I2cAddr.create7bit(paramInt); 
  }
  
  @JavascriptInterface
  @Block(classes = {BNO055IMU.Parameters.class}, fieldName = {"i2cAddr"})
  public void setI2cAddress8Bit(Object paramObject, int paramInt) {
    startBlockExecution(BlockType.FUNCTION, ".setI2cAddress8Bit");
    paramObject = checkBNO055IMUParameters(paramObject);
    if (paramObject != null)
      ((BNO055IMU.Parameters)paramObject).i2cAddr = I2cAddr.create8bit(paramInt); 
  }
  
  @JavascriptInterface
  @Block(classes = {BNO055IMU.Parameters.class}, fieldName = {"loggingEnabled"})
  public void setLoggingEnabled(Object paramObject, boolean paramBoolean) {
    startBlockExecution(BlockType.FUNCTION, ".setLoggingEnabled");
    paramObject = checkBNO055IMUParameters(paramObject);
    if (paramObject != null)
      ((BNO055IMU.Parameters)paramObject).loggingEnabled = paramBoolean; 
  }
  
  @JavascriptInterface
  @Block(classes = {BNO055IMU.Parameters.class}, fieldName = {"loggingTag"})
  public void setLoggingTag(Object paramObject, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".setLoggingTag");
    paramObject = checkBNO055IMUParameters(paramObject);
    if (paramObject != null)
      ((BNO055IMU.Parameters)paramObject).loggingTag = paramString; 
  }
  
  @JavascriptInterface
  @Block(classes = {BNO055IMU.Parameters.class}, fieldName = {"mode"})
  public void setSensorMode(Object paramObject, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".setSensorMode");
    paramObject = checkBNO055IMUParameters(paramObject);
    BNO055IMU.SensorMode sensorMode = checkArg(paramString, BNO055IMU.SensorMode.class, "sensorMode");
    if (paramObject != null && sensorMode != null)
      ((BNO055IMU.Parameters)paramObject).mode = sensorMode; 
  }
  
  @JavascriptInterface
  @Block(classes = {BNO055IMU.Parameters.class}, fieldName = {"temperatureUnit"})
  public void setTempUnit(Object paramObject, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".setTempUnit");
    paramObject = checkBNO055IMUParameters(paramObject);
    BNO055IMU.TempUnit tempUnit = checkArg(paramString, BNO055IMU.TempUnit.class, "tempUnit");
    if (paramObject != null && tempUnit != null)
      ((BNO055IMU.Parameters)paramObject).temperatureUnit = tempUnit; 
  }
  
  enum Algorithm {
    JUST_LOGGING, NAIVE;
    
    static {
      Algorithm algorithm = new Algorithm("JUST_LOGGING", 1);
      JUST_LOGGING = algorithm;
      $VALUES = new Algorithm[] { NAIVE, algorithm };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\BNO055IMUParametersAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
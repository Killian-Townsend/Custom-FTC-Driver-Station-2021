package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareItem;
import com.qualcomm.robotcore.exception.TargetPositionNotSetException;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

class DcMotorAccess extends HardwareAccess<DcMotor> {
  private final DcMotor dcMotor = this.hardwareDevice;
  
  DcMotorAccess(BlocksOpMode paramBlocksOpMode, HardwareItem paramHardwareItem, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramHardwareItem, paramHardwareMap, DcMotor.class);
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotorEx.class}, methodName = {"getCurrent"})
  public double getCurrent(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".getCurrent");
    CurrentUnit currentUnit = (CurrentUnit)checkArg(paramString, CurrentUnit.class, "");
    if (currentUnit != null) {
      DcMotor dcMotor = this.dcMotor;
      if (dcMotor instanceof DcMotorEx)
        return ((DcMotorEx)dcMotor).getCurrent(currentUnit); 
      reportWarning("This DcMotor is not a DcMotorEx.");
    } 
    return 0.0D;
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotorEx.class}, methodName = {"getCurrentAlert"})
  public double getCurrentAlert(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".getCurrentAlert");
    CurrentUnit currentUnit = (CurrentUnit)checkArg(paramString, CurrentUnit.class, "");
    if (currentUnit != null) {
      DcMotor dcMotor = this.dcMotor;
      if (dcMotor instanceof DcMotorEx)
        return ((DcMotorEx)dcMotor).getCurrentAlert(currentUnit); 
      reportWarning("This DcMotor is not a DcMotorEx.");
    } 
    return 0.0D;
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotor.class, DcMotorEx.class}, methodName = {"getCurrentPosition"})
  public int getCurrentPosition() {
    startBlockExecution(BlockType.GETTER, ".CurrentPosition");
    return this.dcMotor.getCurrentPosition();
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotor.class, DcMotorEx.class}, methodName = {"getDirection"})
  public String getDirection() {
    startBlockExecution(BlockType.GETTER, ".Direction");
    DcMotorSimple.Direction direction = this.dcMotor.getDirection();
    return (direction != null) ? direction.toString() : "";
  }
  
  @JavascriptInterface
  @Deprecated
  public int getMaxSpeed() {
    startBlockExecution(BlockType.GETTER, ".MaxSpeed");
    return 0;
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotor.class, DcMotorEx.class}, methodName = {"getMode"})
  public String getMode() {
    startBlockExecution(BlockType.GETTER, ".Mode");
    DcMotor.RunMode runMode = this.dcMotor.getMode();
    return (runMode != null) ? runMode.toString() : "";
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotorEx.class}, methodName = {"getPIDFCoefficients"})
  public PIDFCoefficients getPIDFCoefficients(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".getPIDFCoefficients");
    DcMotor.RunMode runMode = (DcMotor.RunMode)checkArg(paramString, DcMotor.RunMode.class, "");
    if (runMode != null) {
      DcMotor dcMotor = this.dcMotor;
      if (dcMotor instanceof DcMotorEx)
        return ((DcMotorEx)dcMotor).getPIDFCoefficients(runMode); 
      reportWarning("This DcMotor is not a DcMotorEx.");
    } 
    return null;
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotor.class, DcMotorEx.class}, methodName = {"getPower"})
  public double getPower() {
    startBlockExecution(BlockType.GETTER, ".Power");
    return this.dcMotor.getPower();
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotor.class, DcMotorEx.class}, methodName = {"getPowerFloat"})
  public boolean getPowerFloat() {
    startBlockExecution(BlockType.GETTER, ".PowerFloat");
    return this.dcMotor.getPowerFloat();
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotor.class, DcMotorEx.class}, methodName = {"getTargetPosition"})
  public int getTargetPosition() {
    startBlockExecution(BlockType.GETTER, ".TargetPosition");
    return this.dcMotor.getTargetPosition();
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotorEx.class}, methodName = {"getTargetPositionTolerance"})
  public int getTargetPositionTolerance() {
    startBlockExecution(BlockType.GETTER, ".TargetPositionTolerance");
    DcMotor dcMotor = this.dcMotor;
    if (dcMotor instanceof DcMotorEx)
      return ((DcMotorEx)dcMotor).getTargetPositionTolerance(); 
    reportWarning("This DcMotor is not a DcMotorEx.");
    return 0;
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotorEx.class}, methodName = {"getVelocity"})
  public double getVelocity() {
    startBlockExecution(BlockType.FUNCTION, ".getVelocity");
    DcMotor dcMotor = this.dcMotor;
    if (dcMotor instanceof DcMotorEx)
      return ((DcMotorEx)dcMotor).getVelocity(); 
    reportWarning("This DcMotor is not a DcMotorEx.");
    return 0.0D;
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotorEx.class}, methodName = {"getVelocity"})
  public double getVelocity_withAngleUnit(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".getVelocity");
    AngleUnit angleUnit = (AngleUnit)checkArg(paramString, AngleUnit.class, "");
    if (angleUnit != null) {
      DcMotor dcMotor = this.dcMotor;
      if (dcMotor instanceof DcMotorEx)
        return ((DcMotorEx)dcMotor).getVelocity(angleUnit); 
      reportWarning("This DcMotor is not a DcMotorEx.");
    } 
    return 0.0D;
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotor.class, DcMotorEx.class}, methodName = {"getZeroPowerBehavior"})
  public String getZeroPowerBehavior() {
    startBlockExecution(BlockType.GETTER, ".ZeroPowerBehavior");
    DcMotor.ZeroPowerBehavior zeroPowerBehavior = this.dcMotor.getZeroPowerBehavior();
    return (zeroPowerBehavior != null) ? zeroPowerBehavior.toString() : "";
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotor.class, DcMotorEx.class}, methodName = {"isBusy"})
  public boolean isBusy() {
    startBlockExecution(BlockType.FUNCTION, ".isBusy");
    return this.dcMotor.isBusy();
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotorEx.class}, methodName = {"isMotorEnabled"})
  public boolean isMotorEnabled() {
    startBlockExecution(BlockType.FUNCTION, ".isMotorEnabled");
    DcMotor dcMotor = this.dcMotor;
    if (dcMotor instanceof DcMotorEx)
      return ((DcMotorEx)dcMotor).isMotorEnabled(); 
    reportWarning("This DcMotor is not a DcMotorEx.");
    return false;
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotorEx.class}, methodName = {"isOverCurrent"})
  public boolean isOverCurrent() {
    startBlockExecution(BlockType.FUNCTION, ".isOverCurrent");
    DcMotor dcMotor = this.dcMotor;
    if (dcMotor instanceof DcMotorEx)
      return ((DcMotorEx)dcMotor).isOverCurrent(); 
    reportWarning("This DcMotor is not a DcMotorEx.");
    return false;
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotorEx.class}, methodName = {"setCurrentAlert"})
  public void setCurrentAlert(double paramDouble, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".setCurrentAlert");
    CurrentUnit currentUnit = (CurrentUnit)checkArg(paramString, CurrentUnit.class, "");
    if (currentUnit != null) {
      DcMotor dcMotor = this.dcMotor;
      if (dcMotor instanceof DcMotorEx) {
        ((DcMotorEx)dcMotor).setCurrentAlert(paramDouble, currentUnit);
        return;
      } 
      reportWarning("This DcMotor is not a DcMotorEx.");
    } 
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotor.class, DcMotorEx.class}, methodName = {"setDirection"})
  public void setDirection(String paramString) {
    startBlockExecution(BlockType.SETTER, ".Direction");
    DcMotorSimple.Direction direction = (DcMotorSimple.Direction)checkArg(paramString, DcMotorSimple.Direction.class, "");
    if (direction != null)
      this.dcMotor.setDirection(direction); 
  }
  
  @JavascriptInterface
  @Deprecated
  public void setDualMaxSpeed(double paramDouble1, Object paramObject, double paramDouble2) {
    startBlockExecution(BlockType.SETTER, ".MaxSpeed");
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotor.class, DcMotorEx.class}, methodName = {"setMode"})
  public void setDualMode(String paramString1, Object paramObject, String paramString2) {
    startBlockExecution(BlockType.SETTER, ".Mode");
    DcMotor.RunMode runMode1 = (DcMotor.RunMode)checkArg(paramString1, DcMotor.RunMode.class, "first");
    DcMotor.RunMode runMode2 = (DcMotor.RunMode)checkArg(paramString2, DcMotor.RunMode.class, "second");
    if (runMode1 != null && runMode2 != null && paramObject instanceof DcMotorAccess) {
      paramObject = paramObject;
      this.dcMotor.setMode(runMode1);
      ((DcMotorAccess)paramObject).dcMotor.setMode(runMode2);
    } 
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotor.class, DcMotorEx.class}, methodName = {"setPower"})
  public void setDualPower(double paramDouble1, Object paramObject, double paramDouble2) {
    startBlockExecution(BlockType.SETTER, ".Power");
    if (paramObject instanceof DcMotorAccess) {
      paramObject = paramObject;
      this.dcMotor.setPower(paramDouble1);
      ((DcMotorAccess)paramObject).dcMotor.setPower(paramDouble2);
    } 
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotor.class, DcMotorEx.class}, methodName = {"setTargetPosition"})
  public void setDualTargetPosition(double paramDouble1, Object paramObject, double paramDouble2) {
    startBlockExecution(BlockType.SETTER, ".TargetPosition");
    if (paramObject instanceof DcMotorAccess) {
      paramObject = paramObject;
      this.dcMotor.setTargetPosition((int)paramDouble1);
      ((DcMotorAccess)paramObject).dcMotor.setTargetPosition((int)paramDouble2);
    } 
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotorEx.class}, methodName = {"setTargetPositionTolerance"})
  public void setDualTargetPositionTolerance(double paramDouble1, Object paramObject, double paramDouble2) {
    startBlockExecution(BlockType.SETTER, ".TargetPositionTolerance");
    if (paramObject instanceof DcMotorAccess) {
      paramObject = paramObject;
      DcMotor dcMotor = this.dcMotor;
      if (dcMotor instanceof DcMotorEx)
        ((DcMotorEx)dcMotor).setTargetPositionTolerance((int)paramDouble1); 
      paramObject = ((DcMotorAccess)paramObject).dcMotor;
      if (paramObject instanceof DcMotorEx)
        ((DcMotorEx)paramObject).setTargetPositionTolerance((int)paramDouble2); 
    } 
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotorEx.class}, methodName = {"setVelocity"})
  public void setDualVelocity(double paramDouble1, Object paramObject, double paramDouble2) {
    startBlockExecution(BlockType.SETTER, ".Velocity");
    if (paramObject instanceof DcMotorAccess) {
      paramObject = paramObject;
      DcMotor dcMotor = this.dcMotor;
      if (dcMotor instanceof DcMotorEx)
        ((DcMotorEx)dcMotor).setVelocity(paramDouble1); 
      paramObject = ((DcMotorAccess)paramObject).dcMotor;
      if (paramObject instanceof DcMotorEx)
        ((DcMotorEx)paramObject).setVelocity(paramDouble2); 
    } 
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotor.class, DcMotorEx.class}, methodName = {"setZeroPowerBehavior"})
  public void setDualZeroPowerBehavior(String paramString1, Object paramObject, String paramString2) {
    startBlockExecution(BlockType.SETTER, ".ZeroPowerBehavior");
    DcMotor.ZeroPowerBehavior zeroPowerBehavior1 = (DcMotor.ZeroPowerBehavior)checkArg(paramString1, DcMotor.ZeroPowerBehavior.class, "first");
    DcMotor.ZeroPowerBehavior zeroPowerBehavior2 = (DcMotor.ZeroPowerBehavior)checkArg(paramString2, DcMotor.ZeroPowerBehavior.class, "second");
    if (zeroPowerBehavior1 != null && zeroPowerBehavior2 != null && paramObject instanceof DcMotorAccess) {
      paramObject = paramObject;
      this.dcMotor.setZeroPowerBehavior(zeroPowerBehavior1);
      ((DcMotorAccess)paramObject).dcMotor.setZeroPowerBehavior(zeroPowerBehavior2);
    } 
  }
  
  @JavascriptInterface
  @Deprecated
  public void setMaxSpeed(double paramDouble) {
    startBlockExecution(BlockType.SETTER, ".MaxSpeed");
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotor.class, DcMotorEx.class}, methodName = {"setMode"})
  public void setMode(String paramString) {
    startBlockExecution(BlockType.SETTER, ".Mode");
    DcMotor.RunMode runMode = (DcMotor.RunMode)checkArg(paramString, DcMotor.RunMode.class, "");
    if (runMode != null)
      try {
        this.dcMotor.setMode(runMode);
        return;
      } catch (TargetPositionNotSetException targetPositionNotSetException) {
        RobotLog.setGlobalErrorMsg(targetPositionNotSetException.getMessage());
        throw new IllegalStateException();
      }  
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotorEx.class}, methodName = {"setMotorDisable"})
  public void setMotorDisable() {
    startBlockExecution(BlockType.FUNCTION, ".setMotorDisable");
    DcMotor dcMotor = this.dcMotor;
    if (dcMotor instanceof DcMotorEx) {
      ((DcMotorEx)dcMotor).setMotorDisable();
      return;
    } 
    reportWarning("This DcMotor is not a DcMotorEx.");
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotorEx.class}, methodName = {"setMotorEnable"})
  public void setMotorEnable() {
    startBlockExecution(BlockType.FUNCTION, ".setMotorEnable");
    DcMotor dcMotor = this.dcMotor;
    if (dcMotor instanceof DcMotorEx) {
      ((DcMotorEx)dcMotor).setMotorEnable();
      return;
    } 
    reportWarning("This DcMotor is not a DcMotorEx.");
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotorEx.class}, methodName = {"setPIDFCoefficients"})
  public void setPIDFCoefficients(String paramString, Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".setPIDFCoefficients");
    DcMotor.RunMode runMode = (DcMotor.RunMode)checkArg(paramString, DcMotor.RunMode.class, "");
    paramObject = checkArg(paramObject, PIDFCoefficients.class, "");
    if (runMode != null && paramObject != null) {
      DcMotor dcMotor = this.dcMotor;
      if (dcMotor instanceof DcMotorEx) {
        ((DcMotorEx)dcMotor).setPIDFCoefficients(runMode, (PIDFCoefficients)paramObject);
        return;
      } 
      reportWarning("This DcMotor is not a DcMotorEx.");
    } 
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotorEx.class}, methodName = {"setPositionPIDFCoefficients"})
  public void setPositionPIDFCoefficients(double paramDouble) {
    startBlockExecution(BlockType.FUNCTION, ".setPositionPIDFCoefficients");
    DcMotor dcMotor = this.dcMotor;
    if (dcMotor instanceof DcMotorEx) {
      ((DcMotorEx)dcMotor).setPositionPIDFCoefficients(paramDouble);
      return;
    } 
    reportWarning("This DcMotor is not a DcMotorEx.");
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotor.class, DcMotorEx.class}, methodName = {"setPower"})
  public void setPower(double paramDouble) {
    startBlockExecution(BlockType.SETTER, ".Power");
    this.dcMotor.setPower(paramDouble);
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotor.class, DcMotorEx.class}, methodName = {"setTargetPosition"})
  public void setTargetPosition(double paramDouble) {
    startBlockExecution(BlockType.SETTER, ".TargetPosition");
    this.dcMotor.setTargetPosition((int)paramDouble);
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotorEx.class}, methodName = {"setTargetPositionTolerance"})
  public void setTargetPositionTolerance(int paramInt) {
    startBlockExecution(BlockType.SETTER, ".TargetPositionTolerance");
    DcMotor dcMotor = this.dcMotor;
    if (dcMotor instanceof DcMotorEx) {
      ((DcMotorEx)dcMotor).setTargetPositionTolerance(paramInt);
      return;
    } 
    reportWarning("This DcMotor is not a DcMotorEx.");
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotorEx.class}, methodName = {"setVelocity"})
  public void setVelocity(double paramDouble) {
    startBlockExecution(BlockType.FUNCTION, ".setVelocity");
    DcMotor dcMotor = this.dcMotor;
    if (dcMotor instanceof DcMotorEx) {
      ((DcMotorEx)dcMotor).setVelocity(paramDouble);
      return;
    } 
    reportWarning("This DcMotor is not a DcMotorEx.");
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotorEx.class}, methodName = {"setVelocityPIDFCoefficients"})
  public void setVelocityPIDFCoefficients(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
    startBlockExecution(BlockType.FUNCTION, ".setVelocityPIDFCoefficients");
    DcMotor dcMotor = this.dcMotor;
    if (dcMotor instanceof DcMotorEx) {
      ((DcMotorEx)dcMotor).setVelocityPIDFCoefficients(paramDouble1, paramDouble2, paramDouble3, paramDouble4);
      return;
    } 
    reportWarning("This DcMotor is not a DcMotorEx.");
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotorEx.class}, methodName = {"setVelocity"})
  public void setVelocity_withAngleUnit(double paramDouble, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".setVelocity");
    AngleUnit angleUnit = (AngleUnit)checkArg(paramString, AngleUnit.class, "");
    if (angleUnit != null) {
      DcMotor dcMotor = this.dcMotor;
      if (dcMotor instanceof DcMotorEx) {
        ((DcMotorEx)dcMotor).setVelocity(paramDouble, angleUnit);
        return;
      } 
      reportWarning("This DcMotor is not a DcMotorEx.");
    } 
  }
  
  @JavascriptInterface
  @Block(classes = {DcMotor.class, DcMotorEx.class}, methodName = {"setZeroPowerBehavior"})
  public void setZeroPowerBehavior(String paramString) {
    startBlockExecution(BlockType.SETTER, ".ZeroPowerBehavior");
    DcMotor.ZeroPowerBehavior zeroPowerBehavior = (DcMotor.ZeroPowerBehavior)checkArg(paramString, DcMotor.ZeroPowerBehavior.class, "");
    if (zeroPowerBehavior != null)
      this.dcMotor.setZeroPowerBehavior(zeroPowerBehavior); 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\DcMotorAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
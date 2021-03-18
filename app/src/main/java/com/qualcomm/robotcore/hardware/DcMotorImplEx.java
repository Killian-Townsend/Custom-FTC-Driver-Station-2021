package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class DcMotorImplEx extends DcMotorImpl implements DcMotorEx {
  DcMotorControllerEx controllerEx;
  
  int targetPositionTolerance = 5;
  
  public DcMotorImplEx(DcMotorController paramDcMotorController, int paramInt) {
    this(paramDcMotorController, paramInt, DcMotorSimple.Direction.FORWARD);
  }
  
  public DcMotorImplEx(DcMotorController paramDcMotorController, int paramInt, DcMotorSimple.Direction paramDirection) {
    this(paramDcMotorController, paramInt, paramDirection, MotorConfigurationType.getUnspecifiedMotorType());
  }
  
  public DcMotorImplEx(DcMotorController paramDcMotorController, int paramInt, DcMotorSimple.Direction paramDirection, MotorConfigurationType paramMotorConfigurationType) {
    super(paramDcMotorController, paramInt, paramDirection, paramMotorConfigurationType);
    this.controllerEx = (DcMotorControllerEx)paramDcMotorController;
  }
  
  protected double adjustAngularRate(double paramDouble) {
    double d = paramDouble;
    if (getOperationalDirection() == DcMotorSimple.Direction.REVERSE)
      d = -paramDouble; 
    return d;
  }
  
  public double getCurrent(CurrentUnit paramCurrentUnit) {
    return this.controllerEx.getMotorCurrent(this.portNumber, paramCurrentUnit);
  }
  
  public double getCurrentAlert(CurrentUnit paramCurrentUnit) {
    return this.controllerEx.getMotorCurrentAlert(this.portNumber, paramCurrentUnit);
  }
  
  public PIDCoefficients getPIDCoefficients(DcMotor.RunMode paramRunMode) {
    return this.controllerEx.getPIDCoefficients(getPortNumber(), paramRunMode);
  }
  
  public PIDFCoefficients getPIDFCoefficients(DcMotor.RunMode paramRunMode) {
    return this.controllerEx.getPIDFCoefficients(getPortNumber(), paramRunMode);
  }
  
  public int getTargetPositionTolerance() {
    return this.targetPositionTolerance;
  }
  
  public double getVelocity() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield controllerEx : Lcom/qualcomm/robotcore/hardware/DcMotorControllerEx;
    //   7: aload_0
    //   8: invokevirtual getPortNumber : ()I
    //   11: invokeinterface getMotorVelocity : (I)D
    //   16: invokevirtual adjustAngularRate : (D)D
    //   19: dstore_1
    //   20: aload_0
    //   21: monitorexit
    //   22: dload_1
    //   23: dreturn
    //   24: astore_3
    //   25: aload_0
    //   26: monitorexit
    //   27: aload_3
    //   28: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	24	finally
  }
  
  public double getVelocity(AngleUnit paramAngleUnit) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield controllerEx : Lcom/qualcomm/robotcore/hardware/DcMotorControllerEx;
    //   7: aload_0
    //   8: invokevirtual getPortNumber : ()I
    //   11: aload_1
    //   12: invokeinterface getMotorVelocity : (ILorg/firstinspires/ftc/robotcore/external/navigation/AngleUnit;)D
    //   17: invokevirtual adjustAngularRate : (D)D
    //   20: dstore_2
    //   21: aload_0
    //   22: monitorexit
    //   23: dload_2
    //   24: dreturn
    //   25: astore_1
    //   26: aload_0
    //   27: monitorexit
    //   28: aload_1
    //   29: athrow
    // Exception table:
    //   from	to	target	type
    //   2	21	25	finally
  }
  
  protected void internalSetTargetPosition(int paramInt) {
    this.controllerEx.setMotorTargetPosition(this.portNumber, paramInt, this.targetPositionTolerance);
  }
  
  public boolean isMotorEnabled() {
    return this.controllerEx.isMotorEnabled(getPortNumber());
  }
  
  public boolean isOverCurrent() {
    return this.controllerEx.isMotorOverCurrent(this.portNumber);
  }
  
  public void setCurrentAlert(double paramDouble, CurrentUnit paramCurrentUnit) {
    this.controllerEx.setMotorCurrentAlert(this.portNumber, paramDouble, paramCurrentUnit);
  }
  
  public void setMotorDisable() {
    this.controllerEx.setMotorDisable(getPortNumber());
  }
  
  public void setMotorEnable() {
    this.controllerEx.setMotorEnable(getPortNumber());
  }
  
  public void setPIDCoefficients(DcMotor.RunMode paramRunMode, PIDCoefficients paramPIDCoefficients) {
    this.controllerEx.setPIDCoefficients(getPortNumber(), paramRunMode, paramPIDCoefficients);
  }
  
  public void setPIDFCoefficients(DcMotor.RunMode paramRunMode, PIDFCoefficients paramPIDFCoefficients) {
    this.controllerEx.setPIDFCoefficients(getPortNumber(), paramRunMode, paramPIDFCoefficients);
  }
  
  public void setPositionPIDFCoefficients(double paramDouble) {
    setPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION, new PIDFCoefficients(paramDouble, 0.0D, 0.0D, 0.0D, MotorControlAlgorithm.PIDF));
  }
  
  public void setTargetPositionTolerance(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: putfield targetPositionTolerance : I
    //   7: aload_0
    //   8: monitorexit
    //   9: return
    //   10: astore_2
    //   11: aload_0
    //   12: monitorexit
    //   13: aload_2
    //   14: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	10	finally
  }
  
  public void setVelocity(double paramDouble) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: dload_1
    //   4: invokevirtual adjustAngularRate : (D)D
    //   7: dstore_1
    //   8: aload_0
    //   9: getfield controllerEx : Lcom/qualcomm/robotcore/hardware/DcMotorControllerEx;
    //   12: aload_0
    //   13: invokevirtual getPortNumber : ()I
    //   16: dload_1
    //   17: invokeinterface setMotorVelocity : (ID)V
    //   22: aload_0
    //   23: monitorexit
    //   24: return
    //   25: astore_3
    //   26: aload_0
    //   27: monitorexit
    //   28: aload_3
    //   29: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	25	finally
  }
  
  public void setVelocity(double paramDouble, AngleUnit paramAngleUnit) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: dload_1
    //   4: invokevirtual adjustAngularRate : (D)D
    //   7: dstore_1
    //   8: aload_0
    //   9: getfield controllerEx : Lcom/qualcomm/robotcore/hardware/DcMotorControllerEx;
    //   12: aload_0
    //   13: invokevirtual getPortNumber : ()I
    //   16: dload_1
    //   17: aload_3
    //   18: invokeinterface setMotorVelocity : (IDLorg/firstinspires/ftc/robotcore/external/navigation/AngleUnit;)V
    //   23: aload_0
    //   24: monitorexit
    //   25: return
    //   26: astore_3
    //   27: aload_0
    //   28: monitorexit
    //   29: aload_3
    //   30: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	26	finally
  }
  
  public void setVelocityPIDFCoefficients(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
    setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(paramDouble1, paramDouble2, paramDouble3, paramDouble4, MotorControlAlgorithm.PIDF));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\DcMotorImplEx.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
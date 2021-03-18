package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.external.navigation.Rotation;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class DcMotorImpl implements DcMotor {
  protected DcMotorController controller = null;
  
  protected DcMotorSimple.Direction direction = DcMotorSimple.Direction.FORWARD;
  
  protected MotorConfigurationType motorType = null;
  
  protected int portNumber = -1;
  
  public DcMotorImpl(DcMotorController paramDcMotorController, int paramInt) {
    this(paramDcMotorController, paramInt, DcMotorSimple.Direction.FORWARD);
  }
  
  public DcMotorImpl(DcMotorController paramDcMotorController, int paramInt, DcMotorSimple.Direction paramDirection) {
    this(paramDcMotorController, paramInt, paramDirection, MotorConfigurationType.getUnspecifiedMotorType());
  }
  
  public DcMotorImpl(DcMotorController paramDcMotorController, int paramInt, DcMotorSimple.Direction paramDirection, MotorConfigurationType paramMotorConfigurationType) {
    this.controller = paramDcMotorController;
    this.portNumber = paramInt;
    this.direction = paramDirection;
    this.motorType = paramMotorConfigurationType;
    RobotLog.v("DcMotorImpl(type=%s)", new Object[] { paramMotorConfigurationType.getXmlTag() });
    paramDcMotorController.setMotorType(paramInt, paramMotorConfigurationType.clone());
  }
  
  protected int adjustPosition(int paramInt) {
    int i = paramInt;
    if (getOperationalDirection() == DcMotorSimple.Direction.REVERSE)
      i = -paramInt; 
    return i;
  }
  
  protected double adjustPower(double paramDouble) {
    double d = paramDouble;
    if (getOperationalDirection() == DcMotorSimple.Direction.REVERSE)
      d = -paramDouble; 
    return d;
  }
  
  public void close() {
    setPowerFloat();
  }
  
  public String getConnectionInfo() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.controller.getConnectionInfo());
    stringBuilder.append("; port ");
    stringBuilder.append(this.portNumber);
    return stringBuilder.toString();
  }
  
  public DcMotorController getController() {
    return this.controller;
  }
  
  public int getCurrentPosition() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield controller : Lcom/qualcomm/robotcore/hardware/DcMotorController;
    //   7: aload_0
    //   8: getfield portNumber : I
    //   11: invokeinterface getMotorCurrentPosition : (I)I
    //   16: invokevirtual adjustPosition : (I)I
    //   19: istore_1
    //   20: aload_0
    //   21: monitorexit
    //   22: iload_1
    //   23: ireturn
    //   24: astore_2
    //   25: aload_0
    //   26: monitorexit
    //   27: aload_2
    //   28: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	24	finally
  }
  
  public String getDeviceName() {
    return AppUtil.getDefContext().getString(R.string.configTypeMotor);
  }
  
  public DcMotorSimple.Direction getDirection() {
    return this.direction;
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return this.controller.getManufacturer();
  }
  
  public DcMotor.RunMode getMode() {
    return this.controller.getMotorMode(this.portNumber);
  }
  
  public MotorConfigurationType getMotorType() {
    return this.controller.getMotorType(this.portNumber);
  }
  
  protected DcMotorSimple.Direction getOperationalDirection() {
    return (this.motorType.getOrientation() == Rotation.CCW) ? this.direction.inverted() : this.direction;
  }
  
  public int getPortNumber() {
    return this.portNumber;
  }
  
  public double getPower() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield controller : Lcom/qualcomm/robotcore/hardware/DcMotorController;
    //   6: aload_0
    //   7: getfield portNumber : I
    //   10: invokeinterface getMotorPower : (I)D
    //   15: dstore_1
    //   16: aload_0
    //   17: invokevirtual getMode : ()Lcom/qualcomm/robotcore/hardware/DcMotor$RunMode;
    //   20: getstatic com/qualcomm/robotcore/hardware/DcMotor$RunMode.RUN_TO_POSITION : Lcom/qualcomm/robotcore/hardware/DcMotor$RunMode;
    //   23: if_acmpne -> 34
    //   26: dload_1
    //   27: invokestatic abs : (D)D
    //   30: dstore_1
    //   31: goto -> 40
    //   34: aload_0
    //   35: dload_1
    //   36: invokevirtual adjustPower : (D)D
    //   39: dstore_1
    //   40: aload_0
    //   41: monitorexit
    //   42: dload_1
    //   43: dreturn
    //   44: astore_3
    //   45: aload_0
    //   46: monitorexit
    //   47: aload_3
    //   48: athrow
    // Exception table:
    //   from	to	target	type
    //   2	31	44	finally
    //   34	40	44	finally
  }
  
  public boolean getPowerFloat() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual getZeroPowerBehavior : ()Lcom/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior;
    //   6: getstatic com/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior.FLOAT : Lcom/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior;
    //   9: if_acmpne -> 28
    //   12: aload_0
    //   13: invokevirtual getPower : ()D
    //   16: dstore_1
    //   17: dload_1
    //   18: dconst_0
    //   19: dcmpl
    //   20: ifne -> 28
    //   23: iconst_1
    //   24: istore_3
    //   25: goto -> 30
    //   28: iconst_0
    //   29: istore_3
    //   30: aload_0
    //   31: monitorexit
    //   32: iload_3
    //   33: ireturn
    //   34: astore #4
    //   36: aload_0
    //   37: monitorexit
    //   38: aload #4
    //   40: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	34	finally
  }
  
  public int getTargetPosition() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield controller : Lcom/qualcomm/robotcore/hardware/DcMotorController;
    //   7: aload_0
    //   8: getfield portNumber : I
    //   11: invokeinterface getMotorTargetPosition : (I)I
    //   16: invokevirtual adjustPosition : (I)I
    //   19: istore_1
    //   20: aload_0
    //   21: monitorexit
    //   22: iload_1
    //   23: ireturn
    //   24: astore_2
    //   25: aload_0
    //   26: monitorexit
    //   27: aload_2
    //   28: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	24	finally
  }
  
  public int getVersion() {
    return 1;
  }
  
  public DcMotor.ZeroPowerBehavior getZeroPowerBehavior() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield controller : Lcom/qualcomm/robotcore/hardware/DcMotorController;
    //   6: aload_0
    //   7: getfield portNumber : I
    //   10: invokeinterface getMotorZeroPowerBehavior : (I)Lcom/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior;
    //   15: astore_1
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_1
    //   19: areturn
    //   20: astore_1
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_1
    //   24: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	20	finally
  }
  
  protected void internalSetMode(DcMotor.RunMode paramRunMode) {
    this.controller.setMotorMode(this.portNumber, paramRunMode);
  }
  
  protected void internalSetPower(double paramDouble) {
    this.controller.setMotorPower(this.portNumber, paramDouble);
  }
  
  protected void internalSetTargetPosition(int paramInt) {
    this.controller.setMotorTargetPosition(this.portNumber, paramInt);
  }
  
  public boolean isBusy() {
    return this.controller.isBusy(this.portNumber);
  }
  
  public void resetDeviceConfigurationForOpMode() {
    setDirection(DcMotorSimple.Direction.FORWARD);
    this.controller.resetDeviceConfigurationForOpMode(this.portNumber);
  }
  
  public void setDirection(DcMotorSimple.Direction paramDirection) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield direction : Lcom/qualcomm/robotcore/hardware/DcMotorSimple$Direction;
    //   7: aload_0
    //   8: monitorexit
    //   9: return
    //   10: astore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: aload_1
    //   14: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	10	finally
  }
  
  public void setMode(DcMotor.RunMode paramRunMode) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: invokevirtual migrate : ()Lcom/qualcomm/robotcore/hardware/DcMotor$RunMode;
    //   7: invokevirtual internalSetMode : (Lcom/qualcomm/robotcore/hardware/DcMotor$RunMode;)V
    //   10: aload_0
    //   11: monitorexit
    //   12: return
    //   13: astore_1
    //   14: aload_0
    //   15: monitorexit
    //   16: aload_1
    //   17: athrow
    // Exception table:
    //   from	to	target	type
    //   2	10	13	finally
  }
  
  public void setMotorType(MotorConfigurationType paramMotorConfigurationType) {
    this.controller.setMotorType(this.portNumber, paramMotorConfigurationType);
  }
  
  public void setPower(double paramDouble) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual getMode : ()Lcom/qualcomm/robotcore/hardware/DcMotor$RunMode;
    //   6: getstatic com/qualcomm/robotcore/hardware/DcMotor$RunMode.RUN_TO_POSITION : Lcom/qualcomm/robotcore/hardware/DcMotor$RunMode;
    //   9: if_acmpne -> 20
    //   12: dload_1
    //   13: invokestatic abs : (D)D
    //   16: dstore_1
    //   17: goto -> 26
    //   20: aload_0
    //   21: dload_1
    //   22: invokevirtual adjustPower : (D)D
    //   25: dstore_1
    //   26: aload_0
    //   27: dload_1
    //   28: invokevirtual internalSetPower : (D)V
    //   31: aload_0
    //   32: monitorexit
    //   33: return
    //   34: astore_3
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_3
    //   38: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	34	finally
    //   20	26	34	finally
    //   26	31	34	finally
  }
  
  @Deprecated
  public void setPowerFloat() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getstatic com/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior.FLOAT : Lcom/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior;
    //   6: invokevirtual setZeroPowerBehavior : (Lcom/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior;)V
    //   9: aload_0
    //   10: dconst_0
    //   11: invokevirtual setPower : (D)V
    //   14: aload_0
    //   15: monitorexit
    //   16: return
    //   17: astore_1
    //   18: aload_0
    //   19: monitorexit
    //   20: aload_1
    //   21: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	17	finally
  }
  
  public void setTargetPosition(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: iload_1
    //   5: invokevirtual adjustPosition : (I)I
    //   8: invokevirtual internalSetTargetPosition : (I)V
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: astore_2
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_2
    //   18: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	14	finally
  }
  
  public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior paramZeroPowerBehavior) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield controller : Lcom/qualcomm/robotcore/hardware/DcMotorController;
    //   6: aload_0
    //   7: getfield portNumber : I
    //   10: aload_1
    //   11: invokeinterface setMotorZeroPowerBehavior : (ILcom/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior;)V
    //   16: aload_0
    //   17: monitorexit
    //   18: return
    //   19: astore_1
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_1
    //   23: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	19	finally
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\DcMotorImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
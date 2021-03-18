package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.R;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class ServoImpl implements Servo {
  protected ServoController controller = null;
  
  protected Servo.Direction direction = Servo.Direction.FORWARD;
  
  protected double limitPositionMax = 1.0D;
  
  protected double limitPositionMin = 0.0D;
  
  protected int portNumber = -1;
  
  public ServoImpl(ServoController paramServoController, int paramInt) {
    this(paramServoController, paramInt, Servo.Direction.FORWARD);
  }
  
  public ServoImpl(ServoController paramServoController, int paramInt, Servo.Direction paramDirection) {
    this.direction = paramDirection;
    this.controller = paramServoController;
    this.portNumber = paramInt;
  }
  
  private double reverse(double paramDouble) {
    return 1.0D - paramDouble + 0.0D;
  }
  
  public void close() {}
  
  public String getConnectionInfo() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.controller.getConnectionInfo());
    stringBuilder.append("; port ");
    stringBuilder.append(this.portNumber);
    return stringBuilder.toString();
  }
  
  public ServoController getController() {
    return this.controller;
  }
  
  public String getDeviceName() {
    return AppUtil.getDefContext().getString(R.string.configTypeServo);
  }
  
  public Servo.Direction getDirection() {
    return this.direction;
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return this.controller.getManufacturer();
  }
  
  public int getPortNumber() {
    return this.portNumber;
  }
  
  public double getPosition() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield controller : Lcom/qualcomm/robotcore/hardware/ServoController;
    //   6: aload_0
    //   7: getfield portNumber : I
    //   10: invokeinterface getServoPosition : (I)D
    //   15: dstore_3
    //   16: dload_3
    //   17: dstore_1
    //   18: aload_0
    //   19: getfield direction : Lcom/qualcomm/robotcore/hardware/Servo$Direction;
    //   22: getstatic com/qualcomm/robotcore/hardware/Servo$Direction.REVERSE : Lcom/qualcomm/robotcore/hardware/Servo$Direction;
    //   25: if_acmpne -> 34
    //   28: aload_0
    //   29: dload_3
    //   30: invokespecial reverse : (D)D
    //   33: dstore_1
    //   34: dload_1
    //   35: aload_0
    //   36: getfield limitPositionMin : D
    //   39: aload_0
    //   40: getfield limitPositionMax : D
    //   43: dconst_0
    //   44: dconst_1
    //   45: invokestatic scale : (DDDDD)D
    //   48: dconst_0
    //   49: dconst_1
    //   50: invokestatic clip : (DDD)D
    //   53: dstore_1
    //   54: aload_0
    //   55: monitorexit
    //   56: dload_1
    //   57: dreturn
    //   58: astore #5
    //   60: aload_0
    //   61: monitorexit
    //   62: aload #5
    //   64: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	58	finally
    //   18	34	58	finally
    //   34	54	58	finally
  }
  
  public int getVersion() {
    return 1;
  }
  
  protected void internalSetPosition(double paramDouble) {
    this.controller.setServoPosition(this.portNumber, paramDouble);
  }
  
  public void resetDeviceConfigurationForOpMode() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: dconst_0
    //   4: putfield limitPositionMin : D
    //   7: aload_0
    //   8: dconst_1
    //   9: putfield limitPositionMax : D
    //   12: aload_0
    //   13: getstatic com/qualcomm/robotcore/hardware/Servo$Direction.FORWARD : Lcom/qualcomm/robotcore/hardware/Servo$Direction;
    //   16: putfield direction : Lcom/qualcomm/robotcore/hardware/Servo$Direction;
    //   19: aload_0
    //   20: monitorexit
    //   21: return
    //   22: astore_1
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_1
    //   26: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	22	finally
  }
  
  public void scaleRange(double paramDouble1, double paramDouble2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: dload_1
    //   3: dconst_0
    //   4: dconst_1
    //   5: invokestatic clip : (DDD)D
    //   8: dstore_1
    //   9: dload_3
    //   10: dconst_0
    //   11: dconst_1
    //   12: invokestatic clip : (DDD)D
    //   15: dstore_3
    //   16: dload_1
    //   17: dload_3
    //   18: dcmpl
    //   19: ifge -> 35
    //   22: aload_0
    //   23: dload_1
    //   24: putfield limitPositionMin : D
    //   27: aload_0
    //   28: dload_3
    //   29: putfield limitPositionMax : D
    //   32: aload_0
    //   33: monitorexit
    //   34: return
    //   35: new java/lang/IllegalArgumentException
    //   38: dup
    //   39: ldc 'min must be less than max'
    //   41: invokespecial <init> : (Ljava/lang/String;)V
    //   44: athrow
    //   45: astore #5
    //   47: aload_0
    //   48: monitorexit
    //   49: aload #5
    //   51: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	45	finally
    //   22	32	45	finally
    //   35	45	45	finally
  }
  
  public void setDirection(Servo.Direction paramDirection) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield direction : Lcom/qualcomm/robotcore/hardware/Servo$Direction;
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
  
  public void setPosition(double paramDouble) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: dload_1
    //   3: dconst_0
    //   4: dconst_1
    //   5: invokestatic clip : (DDD)D
    //   8: dstore_3
    //   9: dload_3
    //   10: dstore_1
    //   11: aload_0
    //   12: getfield direction : Lcom/qualcomm/robotcore/hardware/Servo$Direction;
    //   15: getstatic com/qualcomm/robotcore/hardware/Servo$Direction.REVERSE : Lcom/qualcomm/robotcore/hardware/Servo$Direction;
    //   18: if_acmpne -> 27
    //   21: aload_0
    //   22: dload_3
    //   23: invokespecial reverse : (D)D
    //   26: dstore_1
    //   27: aload_0
    //   28: dload_1
    //   29: dconst_0
    //   30: dconst_1
    //   31: aload_0
    //   32: getfield limitPositionMin : D
    //   35: aload_0
    //   36: getfield limitPositionMax : D
    //   39: invokestatic scale : (DDDDD)D
    //   42: invokevirtual internalSetPosition : (D)V
    //   45: aload_0
    //   46: monitorexit
    //   47: return
    //   48: astore #5
    //   50: aload_0
    //   51: monitorexit
    //   52: aload #5
    //   54: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	48	finally
    //   11	27	48	finally
    //   27	45	48	finally
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\ServoImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
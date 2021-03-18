package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class CRServoImpl implements CRServo {
  protected static final double apiPowerMax = 1.0D;
  
  protected static final double apiPowerMin = -1.0D;
  
  protected static final double apiServoPositionMax = 1.0D;
  
  protected static final double apiServoPositionMin = 0.0D;
  
  protected ServoController controller = null;
  
  protected DcMotorSimple.Direction direction = DcMotorSimple.Direction.FORWARD;
  
  protected int portNumber = -1;
  
  public CRServoImpl(ServoController paramServoController, int paramInt) {
    this(paramServoController, paramInt, DcMotorSimple.Direction.FORWARD);
  }
  
  public CRServoImpl(ServoController paramServoController, int paramInt, DcMotorSimple.Direction paramDirection) {
    this.direction = paramDirection;
    this.controller = paramServoController;
    this.portNumber = paramInt;
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
    return AppUtil.getDefContext().getString(R.string.configTypeContinuousRotationServo);
  }
  
  public DcMotorSimple.Direction getDirection() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield direction : Lcom/qualcomm/robotcore/hardware/DcMotorSimple$Direction;
    //   6: astore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_1
    //   10: areturn
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return this.controller.getManufacturer();
  }
  
  public int getPortNumber() {
    return this.portNumber;
  }
  
  public double getPower() {
    double d2 = Range.scale(this.controller.getServoPosition(this.portNumber), 0.0D, 1.0D, -1.0D, 1.0D);
    double d1 = d2;
    if (this.direction == DcMotorSimple.Direction.REVERSE)
      d1 = -d2; 
    return d1;
  }
  
  public int getVersion() {
    return 1;
  }
  
  public void resetDeviceConfigurationForOpMode() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getstatic com/qualcomm/robotcore/hardware/DcMotorSimple$Direction.FORWARD : Lcom/qualcomm/robotcore/hardware/DcMotorSimple$Direction;
    //   6: putfield direction : Lcom/qualcomm/robotcore/hardware/DcMotorSimple$Direction;
    //   9: aload_0
    //   10: monitorexit
    //   11: return
    //   12: astore_1
    //   13: aload_0
    //   14: monitorexit
    //   15: aload_1
    //   16: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	12	finally
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
  
  public void setPower(double paramDouble) {
    if (this.direction == DcMotorSimple.Direction.REVERSE)
      paramDouble = -paramDouble; 
    paramDouble = Range.scale(Range.clip(paramDouble, -1.0D, 1.0D), -1.0D, 1.0D, 0.0D, 1.0D);
    this.controller.setServoPosition(this.portNumber, paramDouble);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\CRServoImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
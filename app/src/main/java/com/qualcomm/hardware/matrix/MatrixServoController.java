package com.qualcomm.hardware.matrix;

import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.TypeConversion;
import java.util.Arrays;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class MatrixServoController implements ServoController {
  private static final byte I2C_DATA_OFFSET = 4;
  
  private static final byte MAX_SERVOS = 4;
  
  private static final byte SERVO_DISABLE_ALL = 0;
  
  private static final byte SERVO_ENABLE_ALL = 15;
  
  public static final int SERVO_POSITION_MAX = 240;
  
  private MatrixMasterController master;
  
  protected ServoController.PwmStatus pwmStatus;
  
  protected double[] servoCache = new double[4];
  
  public MatrixServoController(MatrixMasterController paramMatrixMasterController) {
    this.master = paramMatrixMasterController;
    this.pwmStatus = ServoController.PwmStatus.DISABLED;
    Arrays.fill(this.servoCache, 0.0D);
    paramMatrixMasterController.registerServoController(this);
  }
  
  private void throwIfChannelIsInvalid(int paramInt) {
    if (paramInt >= 1 && paramInt <= 4)
      return; 
    throw new IllegalArgumentException(String.format("Channel %d is invalid; valid channels are %d..%d", new Object[] { Integer.valueOf(paramInt), Integer.valueOf(1), Byte.valueOf((byte)4) }));
  }
  
  public void close() {
    pwmDisable();
  }
  
  public String getConnectionInfo() {
    return this.master.getConnectionInfo();
  }
  
  public String getDeviceName() {
    return AppUtil.getDefContext().getString(R.string.displayNameMatrixServoController);
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.Matrix;
  }
  
  public ServoController.PwmStatus getPwmStatus() {
    return this.pwmStatus;
  }
  
  public double getServoPosition(int paramInt) {
    MatrixI2cTransaction matrixI2cTransaction = new MatrixI2cTransaction((byte)paramInt, MatrixI2cTransaction.I2cTransactionProperty.PROPERTY_SERVO);
    if (this.master.queueTransaction(matrixI2cTransaction))
      this.master.waitOnRead(); 
    return this.servoCache[paramInt] / 240.0D;
  }
  
  public int getVersion() {
    return 1;
  }
  
  public void handleReadServo(MatrixI2cTransaction paramMatrixI2cTransaction, byte[] paramArrayOfbyte) {
    this.servoCache[paramMatrixI2cTransaction.servo] = TypeConversion.unsignedByteToInt(paramArrayOfbyte[4]);
  }
  
  public void pwmDisable() {
    MatrixI2cTransaction matrixI2cTransaction = new MatrixI2cTransaction((byte)0, MatrixI2cTransaction.I2cTransactionProperty.PROPERTY_SERVO_ENABLE, 0);
    this.master.queueTransaction(matrixI2cTransaction);
    this.pwmStatus = ServoController.PwmStatus.DISABLED;
  }
  
  public void pwmEnable() {
    MatrixI2cTransaction matrixI2cTransaction = new MatrixI2cTransaction((byte)0, MatrixI2cTransaction.I2cTransactionProperty.PROPERTY_SERVO_ENABLE, 15);
    this.master.queueTransaction(matrixI2cTransaction);
    this.pwmStatus = ServoController.PwmStatus.ENABLED;
  }
  
  public void resetDeviceConfigurationForOpMode() {
    pwmDisable();
  }
  
  public void setServoPosition(int paramInt, double paramDouble) {
    throwIfChannelIsInvalid(paramInt);
    Range.throwIfRangeIsInvalid(paramDouble, 0.0D, 1.0D);
    byte b = (byte)(int)(paramDouble * 240.0D);
    MatrixI2cTransaction matrixI2cTransaction = new MatrixI2cTransaction((byte)paramInt, b, (byte)0);
    this.master.queueTransaction(matrixI2cTransaction);
  }
  
  public void setServoPosition(int paramInt, double paramDouble, byte paramByte) {
    throwIfChannelIsInvalid(paramInt);
    Range.throwIfRangeIsInvalid(paramDouble, 0.0D, 1.0D);
    byte b = (byte)(int)(paramDouble * 240.0D);
    MatrixI2cTransaction matrixI2cTransaction = new MatrixI2cTransaction((byte)paramInt, b, paramByte);
    this.master.queueTransaction(matrixI2cTransaction);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\matrix\MatrixServoController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
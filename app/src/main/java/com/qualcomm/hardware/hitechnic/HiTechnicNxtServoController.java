package com.qualcomm.hardware.hitechnic;

import android.content.Context;
import com.qualcomm.hardware.R;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.LastKnown;

public final class HiTechnicNxtServoController extends HiTechnicNxtController implements ServoController {
  protected static final byte[] ADDRESS_CHANNEL_MAP;
  
  protected static final int ADDRESS_PWM = 72;
  
  protected static final I2cAddr I2C_ADDRESS = I2cAddr.create8bit(2);
  
  protected static final byte PWM_DISABLE = -1;
  
  protected static final byte PWM_ENABLE = 0;
  
  protected static final byte PWM_ENABLE_WITHOUT_TIMEOUT = -86;
  
  protected static final int SERVO_FIRST = 1;
  
  protected static final int SERVO_LAST = 6;
  
  protected static final double apiPositionMax = 1.0D;
  
  protected static final double apiPositionMin = 0.0D;
  
  protected static final int iRegWindowFirst = 64;
  
  protected static final int iRegWindowMax = 73;
  
  protected static final double servoPositionMax = 255.0D;
  
  protected static final double servoPositionMin = 0.0D;
  
  protected LastKnown<Double>[] commandedServoPositions = (LastKnown<Double>[])LastKnown.createArray(ADDRESS_CHANNEL_MAP.length);
  
  protected LastKnown<Boolean> lastKnownPwmEnabled = new LastKnown();
  
  static {
    ADDRESS_CHANNEL_MAP = new byte[] { -1, 66, 67, 68, 69, 70, 71 };
  }
  
  public HiTechnicNxtServoController(Context paramContext, I2cController paramI2cController, int paramInt) {
    super(paramContext, paramI2cController, paramInt, I2C_ADDRESS);
    I2cDeviceSynch.HeartbeatAction heartbeatAction = new I2cDeviceSynch.HeartbeatAction(true, true, new I2cDeviceSynch.ReadWindow(ADDRESS_CHANNEL_MAP[1], 1, I2cDeviceSynch.ReadMode.ONLY_ONCE));
    this.i2cDeviceSynch.setHeartbeatAction(heartbeatAction);
    this.i2cDeviceSynch.setHeartbeatInterval(9000);
    this.i2cDeviceSynch.enableWriteCoalescing(true);
    this.i2cDeviceSynch.setReadWindow(new I2cDeviceSynch.ReadWindow(64, 9, I2cDeviceSynch.ReadMode.BALANCED));
    finishConstruction();
  }
  
  private void validateApiPosition(double paramDouble) {
    if (0.0D <= paramDouble && paramDouble <= 1.0D)
      return; 
    throw new IllegalArgumentException(String.format("illegal servo position %f; must be in interval [%f,%f]", new Object[] { Double.valueOf(paramDouble), Double.valueOf(0.0D), Double.valueOf(1.0D) }));
  }
  
  private void validateServo(int paramInt) {
    if (paramInt >= 1 && paramInt <= 6)
      return; 
    throw new IllegalArgumentException(String.format("Servo %d is invalid; valid servos are %d..%d", new Object[] { Integer.valueOf(paramInt), Integer.valueOf(1), Integer.valueOf(6) }));
  }
  
  protected void controllerNowArmedOrPretending() {
    adjustHookingToMatchEngagement();
  }
  
  protected void doHook() {
    this.i2cDeviceSynch.engage();
  }
  
  protected void doUnhook() {
    this.i2cDeviceSynch.disengage();
  }
  
  protected void floatHardware() {
    pwmDisable();
  }
  
  public String getConnectionInfo() {
    return String.format(this.context.getString(R.string.controllerPortConnectionInfoFormat), new Object[] { this.controller.getConnectionInfo(), Integer.valueOf(this.physicalPort) });
  }
  
  public String getDeviceName() {
    return this.context.getString(R.string.nxtServoControllerName);
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.HiTechnic;
  }
  
  public ServoController.PwmStatus getPwmStatus() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: bipush #72
    //   5: invokevirtual read8 : (I)B
    //   8: iconst_m1
    //   9: if_icmpne -> 19
    //   12: getstatic com/qualcomm/robotcore/hardware/ServoController$PwmStatus.DISABLED : Lcom/qualcomm/robotcore/hardware/ServoController$PwmStatus;
    //   15: astore_1
    //   16: goto -> 23
    //   19: getstatic com/qualcomm/robotcore/hardware/ServoController$PwmStatus.ENABLED : Lcom/qualcomm/robotcore/hardware/ServoController$PwmStatus;
    //   22: astore_1
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_1
    //   26: areturn
    //   27: astore_1
    //   28: aload_0
    //   29: monitorexit
    //   30: aload_1
    //   31: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	27	finally
    //   19	23	27	finally
  }
  
  public double getServoPosition(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateServo : (I)V
    //   7: aload_0
    //   8: getfield commandedServoPositions : [Lcom/qualcomm/robotcore/util/LastKnown;
    //   11: iload_1
    //   12: aaload
    //   13: invokevirtual getRawValue : ()Ljava/lang/Object;
    //   16: checkcast java/lang/Double
    //   19: astore #4
    //   21: aload #4
    //   23: ifnonnull -> 33
    //   26: ldc2_w NaN
    //   29: dstore_2
    //   30: goto -> 39
    //   33: aload #4
    //   35: invokevirtual doubleValue : ()D
    //   38: dstore_2
    //   39: aload_0
    //   40: monitorexit
    //   41: dload_2
    //   42: dreturn
    //   43: astore #4
    //   45: aload_0
    //   46: monitorexit
    //   47: aload #4
    //   49: athrow
    // Exception table:
    //   from	to	target	type
    //   2	21	43	finally
    //   33	39	43	finally
  }
  
  public int getVersion() {
    return 2;
  }
  
  public void initializeHardware() {
    pwmDisable();
  }
  
  public void pwmDisable() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield lastKnownPwmEnabled : Lcom/qualcomm/robotcore/util/LastKnown;
    //   6: astore_3
    //   7: iconst_0
    //   8: istore_1
    //   9: aload_3
    //   10: iconst_0
    //   11: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   14: invokevirtual updateValue : (Ljava/lang/Object;)Z
    //   17: ifeq -> 53
    //   20: aload_0
    //   21: bipush #72
    //   23: iconst_m1
    //   24: invokevirtual write8 : (IB)V
    //   27: aload_0
    //   28: getfield commandedServoPositions : [Lcom/qualcomm/robotcore/util/LastKnown;
    //   31: astore_3
    //   32: aload_3
    //   33: arraylength
    //   34: istore_2
    //   35: iload_1
    //   36: iload_2
    //   37: if_icmpge -> 53
    //   40: aload_3
    //   41: iload_1
    //   42: aaload
    //   43: invokevirtual invalidate : ()V
    //   46: iload_1
    //   47: iconst_1
    //   48: iadd
    //   49: istore_1
    //   50: goto -> 35
    //   53: aload_0
    //   54: monitorexit
    //   55: return
    //   56: astore_3
    //   57: aload_0
    //   58: monitorexit
    //   59: aload_3
    //   60: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	56	finally
    //   9	35	56	finally
    //   40	46	56	finally
  }
  
  public void pwmEnable() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield lastKnownPwmEnabled : Lcom/qualcomm/robotcore/util/LastKnown;
    //   6: iconst_1
    //   7: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   10: invokevirtual updateValue : (Ljava/lang/Object;)Z
    //   13: ifeq -> 23
    //   16: aload_0
    //   17: bipush #72
    //   19: iconst_0
    //   20: invokevirtual write8 : (IB)V
    //   23: aload_0
    //   24: monitorexit
    //   25: return
    //   26: astore_1
    //   27: aload_0
    //   28: monitorexit
    //   29: aload_1
    //   30: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	26	finally
  }
  
  public void resetDeviceConfigurationForOpMode() {
    floatHardware();
  }
  
  public void setServoPosition(int paramInt, double paramDouble) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateServo : (I)V
    //   7: dload_2
    //   8: dconst_0
    //   9: dconst_1
    //   10: invokestatic clip : (DDD)D
    //   13: dstore_2
    //   14: aload_0
    //   15: dload_2
    //   16: invokespecial validateApiPosition : (D)V
    //   19: aload_0
    //   20: getfield commandedServoPositions : [Lcom/qualcomm/robotcore/util/LastKnown;
    //   23: iload_1
    //   24: aaload
    //   25: dload_2
    //   26: invokestatic valueOf : (D)Ljava/lang/Double;
    //   29: invokevirtual updateValue : (Ljava/lang/Object;)Z
    //   32: ifeq -> 64
    //   35: dload_2
    //   36: dconst_0
    //   37: dconst_1
    //   38: dconst_0
    //   39: ldc2_w 255.0
    //   42: invokestatic scale : (DDDDD)D
    //   45: d2i
    //   46: i2b
    //   47: istore #4
    //   49: aload_0
    //   50: getstatic com/qualcomm/hardware/hitechnic/HiTechnicNxtServoController.ADDRESS_CHANNEL_MAP : [B
    //   53: iload_1
    //   54: baload
    //   55: iload #4
    //   57: invokevirtual write8 : (IB)V
    //   60: aload_0
    //   61: invokevirtual pwmEnable : ()V
    //   64: aload_0
    //   65: monitorexit
    //   66: return
    //   67: astore #5
    //   69: aload_0
    //   70: monitorexit
    //   71: aload #5
    //   73: athrow
    // Exception table:
    //   from	to	target	type
    //   2	64	67	finally
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\hitechnic\HiTechnicNxtServoController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
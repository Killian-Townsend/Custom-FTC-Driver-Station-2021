package com.qualcomm.hardware.modernrobotics;

import android.content.Context;
import com.qualcomm.hardware.HardwareFactory;
import com.qualcomm.hardware.R;
import com.qualcomm.hardware.modernrobotics.comm.ReadWriteRunnable;
import com.qualcomm.hardware.modernrobotics.comm.ReadWriteRunnableStandard;
import com.qualcomm.robotcore.eventloop.SyncdDevice;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.LastKnown;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import org.firstinspires.ftc.robotcore.internal.hardware.usb.ArmableUsbDevice;

public final class ModernRoboticsUsbServoController extends ModernRoboticsUsbController implements ServoController {
  public static final int ADDRESS_CHANNEL1 = 66;
  
  public static final int ADDRESS_CHANNEL2 = 67;
  
  public static final int ADDRESS_CHANNEL3 = 68;
  
  public static final int ADDRESS_CHANNEL4 = 69;
  
  public static final int ADDRESS_CHANNEL5 = 70;
  
  public static final int ADDRESS_CHANNEL6 = 71;
  
  public static final byte[] ADDRESS_CHANNEL_MAP = new byte[] { -1, 66, 67, 68, 69, 70, 71 };
  
  public static final int ADDRESS_PWM = 72;
  
  public static final int ADDRESS_UNUSED = -1;
  
  public static final boolean DEBUG_LOGGING = false;
  
  public static final int MONITOR_LENGTH = 9;
  
  public static final byte PWM_DISABLE = -1;
  
  public static final byte PWM_ENABLE = 0;
  
  public static final byte PWM_ENABLE_WITHOUT_TIMEOUT = -86;
  
  protected static final int SERVO_FIRST = 1;
  
  protected static final int SERVO_LAST = 6;
  
  public static final byte START_ADDRESS = 64;
  
  public static final String TAG = "MRServoController";
  
  protected static final double apiPositionMax = 1.0D;
  
  protected static final double apiPositionMin = 0.0D;
  
  protected static final double servoPositionMax = 255.0D;
  
  protected static final double servoPositionMin = 0.0D;
  
  protected LastKnown<Double>[] commandedServoPositions = (LastKnown<Double>[])LastKnown.createArray(ADDRESS_CHANNEL_MAP.length);
  
  protected LastKnown<Boolean> lastKnownPwmEnabled = new LastKnown();
  
  public ModernRoboticsUsbServoController(Context paramContext, SerialNumber paramSerialNumber, ArmableUsbDevice.OpenRobotUsbDevice paramOpenRobotUsbDevice, SyncdDevice.Manager paramManager) throws RobotCoreException, InterruptedException {
    super(paramContext, paramSerialNumber, paramManager, paramOpenRobotUsbDevice, new ModernRoboticsUsbDevice.CreateReadWriteRunnable(paramContext, paramSerialNumber) {
          public ReadWriteRunnable create(RobotUsbDevice param1RobotUsbDevice) {
            return (ReadWriteRunnable)new ReadWriteRunnableStandard(context, serialNumber, param1RobotUsbDevice, 9, 64, false);
          }
        });
  }
  
  private void doArmOrPretend(boolean paramBoolean) throws RobotCoreException, InterruptedException {
    String str1;
    String str2 = HardwareFactory.getDeviceDisplayName(this.context, this.serialNumber);
    if (paramBoolean) {
      str1 = "";
    } else {
      str1 = " (pretend)";
    } 
    RobotLog.d("arming modern servo controller %s%s...", new Object[] { str2, str1 });
    if (paramBoolean) {
      armDevice();
    } else {
      pretendDevice();
    } 
    RobotLog.d("...arming modern servo controller %s complete", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, this.serialNumber) });
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
  
  protected void doArm() throws RobotCoreException, InterruptedException {
    doArmOrPretend(true);
  }
  
  protected void doCloseFromArmed() {
    floatHardware();
    doCloseFromOther();
  }
  
  protected void doCloseFromOther() {
    try {
      doDisarm();
      return;
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return;
    } catch (RobotCoreException robotCoreException) {
      return;
    } 
  }
  
  protected void doDisarm() throws RobotCoreException, InterruptedException {
    RobotLog.d("disarming modern servo controller \"%s\"...", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, this.serialNumber) });
    disarmDevice();
    RobotLog.d("...disarming modern servo controller %s complete", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, this.serialNumber) });
  }
  
  protected void doPretend() throws RobotCoreException, InterruptedException {
    doArmOrPretend(false);
  }
  
  protected void floatHardware() {
    pwmDisable();
  }
  
  public String getConnectionInfo() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("USB ");
    stringBuilder.append(getSerialNumber());
    return stringBuilder.toString();
  }
  
  public String getDeviceName() {
    return String.format("%s %s", new Object[] { this.context.getString(R.string.moduleDisplayNameServoController), this.robotUsbDevice.getFirmwareVersion() });
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.ModernRobotics;
  }
  
  public ServoController.PwmStatus getPwmStatus() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: bipush #72
    //   5: iconst_1
    //   6: invokevirtual read : (II)[B
    //   9: iconst_0
    //   10: baload
    //   11: iconst_m1
    //   12: if_icmpne -> 23
    //   15: getstatic com/qualcomm/robotcore/hardware/ServoController$PwmStatus.DISABLED : Lcom/qualcomm/robotcore/hardware/ServoController$PwmStatus;
    //   18: astore_1
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_1
    //   22: areturn
    //   23: getstatic com/qualcomm/robotcore/hardware/ServoController$PwmStatus.ENABLED : Lcom/qualcomm/robotcore/hardware/ServoController$PwmStatus;
    //   26: astore_1
    //   27: aload_0
    //   28: monitorexit
    //   29: aload_1
    //   30: areturn
    //   31: astore_1
    //   32: aload_0
    //   33: monitorexit
    //   34: aload_1
    //   35: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	31	finally
    //   23	27	31	finally
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
  
  protected String getTag() {
    return "MRServoController";
  }
  
  public void initializeHardware() {
    floatHardware();
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
    //   50: getstatic com/qualcomm/hardware/modernrobotics/ModernRoboticsUsbServoController.ADDRESS_CHANNEL_MAP : [B
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


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\modernrobotics\ModernRoboticsUsbServoController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
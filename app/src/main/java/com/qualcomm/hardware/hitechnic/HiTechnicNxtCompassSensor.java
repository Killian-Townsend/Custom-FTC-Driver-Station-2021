package com.qualcomm.hardware.hitechnic;

import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cControllerPortDeviceImpl;
import java.util.concurrent.locks.Lock;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class HiTechnicNxtCompassSensor extends I2cControllerPortDeviceImpl implements CompassSensor, I2cController.I2cPortReadyCallback {
  public static final byte CALIBRATION = 67;
  
  public static final byte CALIBRATION_FAILURE = 70;
  
  public static final int COMPASS_BUFFER = 65;
  
  public static final int COMPASS_BUFFER_SIZE = 5;
  
  public static final byte DIRECTION_END = 9;
  
  public static final byte DIRECTION_START = 7;
  
  public static final byte HEADING_IN_TWO_DEGREE_INCREMENTS = 66;
  
  public static final int HEADING_WORD_LENGTH = 2;
  
  public static final I2cAddr I2C_ADDRESS = I2cAddr.create8bit(2);
  
  public static final double INVALID_DIRECTION = -1.0D;
  
  public static final byte MEASUREMENT = 0;
  
  public static final byte MODE_CONTROL_ADDRESS = 65;
  
  public static final byte ONE_DEGREE_HEADING_ADDER = 67;
  
  private boolean calibrationFailed = false;
  
  private double direction;
  
  private CompassSensor.CompassMode mode = CompassSensor.CompassMode.MEASUREMENT_MODE;
  
  private byte[] readBuffer;
  
  private Lock readBufferLock;
  
  private boolean switchingModes = false;
  
  private byte[] writeBuffer;
  
  private Lock writeBufferLock;
  
  public HiTechnicNxtCompassSensor(I2cController paramI2cController, int paramInt) {
    super(paramI2cController, paramInt);
    finishConstruction();
  }
  
  private void readModeSwitch() {
    if (this.mode == CompassSensor.CompassMode.MEASUREMENT_MODE)
      this.controller.enableI2cReadMode(this.physicalPort, I2C_ADDRESS, 65, 5); 
    this.switchingModes = false;
  }
  
  private void writeModeSwitch() {
    boolean bool;
    this.switchingModes = true;
    if (this.mode == CompassSensor.CompassMode.CALIBRATION_MODE) {
      bool = true;
    } else {
      bool = false;
    } 
    this.controller.enableI2cWriteMode(this.physicalPort, I2C_ADDRESS, 65, 1);
    try {
      this.writeBufferLock.lock();
      this.writeBuffer[3] = bool;
      return;
    } finally {
      this.writeBufferLock.unlock();
    } 
  }
  
  public boolean calibrationFailed() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mode : Lcom/qualcomm/robotcore/hardware/CompassSensor$CompassMode;
    //   6: astore #4
    //   8: getstatic com/qualcomm/robotcore/hardware/CompassSensor$CompassMode.CALIBRATION_MODE : Lcom/qualcomm/robotcore/hardware/CompassSensor$CompassMode;
    //   11: astore #5
    //   13: iconst_0
    //   14: istore_2
    //   15: aload #4
    //   17: aload #5
    //   19: if_acmpeq -> 85
    //   22: aload_0
    //   23: getfield switchingModes : Z
    //   26: istore_3
    //   27: iload_3
    //   28: ifeq -> 34
    //   31: goto -> 85
    //   34: aload_0
    //   35: getfield readBufferLock : Ljava/util/concurrent/locks/Lock;
    //   38: invokeinterface lock : ()V
    //   43: aload_0
    //   44: getfield readBuffer : [B
    //   47: iconst_3
    //   48: baload
    //   49: istore_1
    //   50: iload_1
    //   51: bipush #70
    //   53: if_icmpne -> 58
    //   56: iconst_1
    //   57: istore_2
    //   58: aload_0
    //   59: getfield readBufferLock : Ljava/util/concurrent/locks/Lock;
    //   62: invokeinterface unlock : ()V
    //   67: aload_0
    //   68: monitorexit
    //   69: iload_2
    //   70: ireturn
    //   71: astore #4
    //   73: aload_0
    //   74: getfield readBufferLock : Ljava/util/concurrent/locks/Lock;
    //   77: invokeinterface unlock : ()V
    //   82: aload #4
    //   84: athrow
    //   85: aload_0
    //   86: monitorexit
    //   87: iconst_0
    //   88: ireturn
    //   89: astore #4
    //   91: aload_0
    //   92: monitorexit
    //   93: aload #4
    //   95: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	89	finally
    //   22	27	89	finally
    //   34	50	71	finally
    //   58	67	89	finally
    //   73	85	89	finally
  }
  
  public void close() {}
  
  protected void controllerNowArmedOrPretending() {
    this.controller.enableI2cReadMode(this.physicalPort, I2C_ADDRESS, 65, 5);
    this.readBuffer = this.controller.getI2cReadCache(this.physicalPort);
    this.readBufferLock = this.controller.getI2cReadCacheLock(this.physicalPort);
    this.writeBuffer = this.controller.getI2cWriteCache(this.physicalPort);
    this.writeBufferLock = this.controller.getI2cWriteCacheLock(this.physicalPort);
    this.controller.registerForI2cPortReadyCallback(this, this.physicalPort);
  }
  
  public String getConnectionInfo() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.controller.getConnectionInfo());
    stringBuilder.append("; port ");
    stringBuilder.append(this.physicalPort);
    return stringBuilder.toString();
  }
  
  public String getDeviceName() {
    return AppUtil.getDefContext().getString(R.string.configTypeHTCompass);
  }
  
  public double getDirection() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield switchingModes : Z
    //   6: istore #4
    //   8: iload #4
    //   10: ifeq -> 19
    //   13: aload_0
    //   14: monitorexit
    //   15: ldc2_w -1.0
    //   18: dreturn
    //   19: aload_0
    //   20: getfield mode : Lcom/qualcomm/robotcore/hardware/CompassSensor$CompassMode;
    //   23: astore #5
    //   25: getstatic com/qualcomm/robotcore/hardware/CompassSensor$CompassMode.CALIBRATION_MODE : Lcom/qualcomm/robotcore/hardware/CompassSensor$CompassMode;
    //   28: astore #6
    //   30: aload #5
    //   32: aload #6
    //   34: if_acmpne -> 43
    //   37: aload_0
    //   38: monitorexit
    //   39: ldc2_w -1.0
    //   42: dreturn
    //   43: aload_0
    //   44: getfield readBufferLock : Ljava/util/concurrent/locks/Lock;
    //   47: invokeinterface lock : ()V
    //   52: aload_0
    //   53: getfield readBuffer : [B
    //   56: bipush #7
    //   58: bipush #9
    //   60: invokestatic copyOfRange : ([BII)[B
    //   63: astore #5
    //   65: aload_0
    //   66: getfield readBufferLock : Ljava/util/concurrent/locks/Lock;
    //   69: invokeinterface unlock : ()V
    //   74: aload #5
    //   76: getstatic java/nio/ByteOrder.LITTLE_ENDIAN : Ljava/nio/ByteOrder;
    //   79: invokestatic byteArrayToShort : ([BLjava/nio/ByteOrder;)S
    //   82: istore_3
    //   83: iload_3
    //   84: i2d
    //   85: dstore_1
    //   86: aload_0
    //   87: monitorexit
    //   88: dload_1
    //   89: dreturn
    //   90: astore #5
    //   92: aload_0
    //   93: getfield readBufferLock : Ljava/util/concurrent/locks/Lock;
    //   96: invokeinterface unlock : ()V
    //   101: aload #5
    //   103: athrow
    //   104: astore #5
    //   106: aload_0
    //   107: monitorexit
    //   108: aload #5
    //   110: athrow
    // Exception table:
    //   from	to	target	type
    //   2	8	104	finally
    //   19	30	104	finally
    //   43	65	90	finally
    //   65	83	104	finally
    //   92	104	104	finally
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.HiTechnic;
  }
  
  public int getVersion() {
    return 1;
  }
  
  public void portIsReady(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield controller : Lcom/qualcomm/robotcore/hardware/I2cController;
    //   6: aload_0
    //   7: getfield physicalPort : I
    //   10: invokeinterface setI2cPortActionFlag : (I)V
    //   15: aload_0
    //   16: getfield controller : Lcom/qualcomm/robotcore/hardware/I2cController;
    //   19: aload_0
    //   20: getfield physicalPort : I
    //   23: invokeinterface readI2cCacheFromController : (I)V
    //   28: aload_0
    //   29: getfield switchingModes : Z
    //   32: ifeq -> 55
    //   35: aload_0
    //   36: invokespecial readModeSwitch : ()V
    //   39: aload_0
    //   40: getfield controller : Lcom/qualcomm/robotcore/hardware/I2cController;
    //   43: aload_0
    //   44: getfield physicalPort : I
    //   47: invokeinterface writeI2cCacheToController : (I)V
    //   52: goto -> 68
    //   55: aload_0
    //   56: getfield controller : Lcom/qualcomm/robotcore/hardware/I2cController;
    //   59: aload_0
    //   60: getfield physicalPort : I
    //   63: invokeinterface writeI2cPortFlagOnlyToController : (I)V
    //   68: aload_0
    //   69: monitorexit
    //   70: return
    //   71: astore_2
    //   72: aload_0
    //   73: monitorexit
    //   74: aload_2
    //   75: athrow
    // Exception table:
    //   from	to	target	type
    //   2	52	71	finally
    //   55	68	71	finally
  }
  
  public void resetDeviceConfigurationForOpMode() {}
  
  public void setMode(CompassSensor.CompassMode paramCompassMode) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mode : Lcom/qualcomm/robotcore/hardware/CompassSensor$CompassMode;
    //   6: astore_2
    //   7: aload_2
    //   8: aload_1
    //   9: if_acmpne -> 15
    //   12: aload_0
    //   13: monitorexit
    //   14: return
    //   15: aload_0
    //   16: aload_1
    //   17: putfield mode : Lcom/qualcomm/robotcore/hardware/CompassSensor$CompassMode;
    //   20: aload_0
    //   21: invokespecial writeModeSwitch : ()V
    //   24: aload_0
    //   25: monitorexit
    //   26: return
    //   27: astore_1
    //   28: aload_0
    //   29: monitorexit
    //   30: aload_1
    //   31: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	27	finally
    //   15	24	27	finally
  }
  
  public String status() {
    return String.format("NXT Compass Sensor, connected via device %s, port %d", new Object[] { this.controller.getSerialNumber(), Integer.valueOf(this.physicalPort) });
  }
  
  public String toString() {
    return String.format("Compass: %3.1f", new Object[] { Double.valueOf(getDirection()) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\hitechnic\HiTechnicNxtCompassSensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.qualcomm.hardware.hitechnic;

import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cAddressableDevice;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cControllerPortDeviceImpl;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.util.TypeConversion;
import java.util.concurrent.locks.Lock;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class HiTechnicNxtIrSeekerSensor extends I2cControllerPortDeviceImpl implements IrSeekerSensor, I2cAddressableDevice, I2cController.I2cPortReadyCallback {
  public static final double DEFAULT_SIGNAL_DETECTED_THRESHOLD = 0.00390625D;
  
  public static final byte DIRECTION = 4;
  
  public static final double[] DIRECTION_TO_ANGLE;
  
  public static final I2cAddr I2C_ADDRESS = I2cAddr.create8bit(16);
  
  public static final byte INVALID_ANGLE = 0;
  
  public static final byte MAX_ANGLE = 9;
  
  public static final double MAX_SENSOR_STRENGTH = 256.0D;
  
  public static final int MEM_AC_START_ADDRESS = 73;
  
  public static final int MEM_DC_START_ADDRESS = 66;
  
  public static final int MEM_MODE_ADDRESS = 65;
  
  public static final int MEM_READ_LENGTH = 6;
  
  public static final byte MIN_ANGLE = 1;
  
  public static final byte MODE_AC = 0;
  
  public static final byte MODE_DC = 2;
  
  public static final byte SENSOR_COUNT = 9;
  
  public static final byte SENSOR_FIRST = 5;
  
  private IrSeekerSensor.Mode mode = IrSeekerSensor.Mode.MODE_1200HZ;
  
  private byte[] readBuffer;
  
  private Lock readBufferLock;
  
  private double signalDetectedThreshold = 0.00390625D;
  
  private volatile boolean switchingModes;
  
  private byte[] writeBuffer;
  
  private Lock writeBufferLock;
  
  static {
    DIRECTION_TO_ANGLE = new double[] { 0.0D, -120.0D, -90.0D, -60.0D, -30.0D, 0.0D, 30.0D, 60.0D, 90.0D, 120.0D };
  }
  
  public HiTechnicNxtIrSeekerSensor(I2cController paramI2cController, int paramInt) {
    super(paramI2cController, paramInt);
    finishConstruction();
  }
  
  private double getSensorStrength(byte[] paramArrayOfbyte, int paramInt) {
    return TypeConversion.unsignedByteToDouble(paramArrayOfbyte[paramInt + 5]) / 256.0D;
  }
  
  private void writeModeSwitch() {
    boolean bool;
    this.switchingModes = true;
    if (this.mode == IrSeekerSensor.Mode.MODE_600HZ) {
      bool = true;
    } else {
      bool = false;
    } 
    this.controller.enableI2cWriteMode(this.physicalPort, I2C_ADDRESS, 65, 1);
    try {
      this.writeBufferLock.lock();
      this.writeBuffer[4] = bool;
      return;
    } finally {
      this.writeBufferLock.unlock();
    } 
  }
  
  public void close() {}
  
  protected void controllerNowArmedOrPretending() {
    this.readBuffer = this.controller.getI2cReadCache(this.physicalPort);
    this.readBufferLock = this.controller.getI2cReadCacheLock(this.physicalPort);
    this.writeBuffer = this.controller.getI2cWriteCache(this.physicalPort);
    this.writeBufferLock = this.controller.getI2cWriteCacheLock(this.physicalPort);
    this.controller.registerForI2cPortReadyCallback(this, this.physicalPort);
    this.switchingModes = true;
  }
  
  public double getAngle() {
    boolean bool = this.switchingModes;
    double d = 0.0D;
    if (bool)
      return 0.0D; 
    try {
      this.readBufferLock.lock();
      double d1 = d;
      if (this.readBuffer[4] >= 1)
        if (this.readBuffer[4] > 9) {
          d1 = d;
        } else {
          d1 = DIRECTION_TO_ANGLE[this.readBuffer[4]];
        }  
      return d1;
    } finally {
      this.readBufferLock.unlock();
    } 
  }
  
  public String getConnectionInfo() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.controller.getConnectionInfo());
    stringBuilder.append("; port ");
    stringBuilder.append(this.physicalPort);
    return stringBuilder.toString();
  }
  
  public String getDeviceName() {
    return AppUtil.getDefContext().getString(R.string.configTypeHTIrSeeker);
  }
  
  public I2cAddr getI2cAddress() {
    return I2C_ADDRESS;
  }
  
  public IrSeekerSensor.IrSeekerIndividualSensor[] getIndividualSensors() {
    null = new IrSeekerSensor.IrSeekerIndividualSensor[9];
    if (this.switchingModes)
      return null; 
    try {
      this.readBufferLock.lock();
      for (int i = 0; i < 9; i++)
        null[i] = new IrSeekerSensor.IrSeekerIndividualSensor(DIRECTION_TO_ANGLE[i * 2 + 1], getSensorStrength(this.readBuffer, i)); 
      return null;
    } finally {
      this.readBufferLock.unlock();
    } 
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.HiTechnic;
  }
  
  public IrSeekerSensor.Mode getMode() {
    return this.mode;
  }
  
  public double getSignalDetectedThreshold() {
    return this.signalDetectedThreshold;
  }
  
  public double getStrength() {
    boolean bool = this.switchingModes;
    double d = 0.0D;
    if (bool)
      return 0.0D; 
    try {
      this.readBufferLock.lock();
      for (int i = 0; i < 9; i++)
        d = Math.max(d, getSensorStrength(this.readBuffer, i)); 
      return d;
    } finally {
      this.readBufferLock.unlock();
    } 
  }
  
  public int getVersion() {
    return 2;
  }
  
  public void portIsReady(int paramInt) {
    this.controller.setI2cPortActionFlag(this.physicalPort);
    this.controller.readI2cCacheFromController(this.physicalPort);
    if (this.switchingModes) {
      if (this.mode == IrSeekerSensor.Mode.MODE_600HZ) {
        this.controller.enableI2cReadMode(this.physicalPort, I2C_ADDRESS, 66, 6);
      } else {
        this.controller.enableI2cReadMode(this.physicalPort, I2C_ADDRESS, 73, 6);
      } 
      this.controller.writeI2cCacheToController(this.physicalPort);
      this.switchingModes = false;
      return;
    } 
    this.controller.writeI2cPortFlagOnlyToController(this.physicalPort);
  }
  
  public void resetDeviceConfigurationForOpMode() {}
  
  public void setI2cAddress(I2cAddr paramI2cAddr) {
    throw new UnsupportedOperationException("This method is not supported.");
  }
  
  public void setMode(IrSeekerSensor.Mode paramMode) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mode : Lcom/qualcomm/robotcore/hardware/IrSeekerSensor$Mode;
    //   6: astore_2
    //   7: aload_2
    //   8: aload_1
    //   9: if_acmpne -> 15
    //   12: aload_0
    //   13: monitorexit
    //   14: return
    //   15: aload_0
    //   16: aload_1
    //   17: putfield mode : Lcom/qualcomm/robotcore/hardware/IrSeekerSensor$Mode;
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
  
  public void setSignalDetectedThreshold(double paramDouble) {
    this.signalDetectedThreshold = paramDouble;
  }
  
  public boolean signalDetected() {
    boolean bool = this.switchingModes;
    boolean bool1 = false;
    if (bool)
      return false; 
    try {
      this.readBufferLock.lock();
      byte b = this.readBuffer[4];
      if (b != 0) {
        b = 1;
      } else {
        b = 0;
      } 
      this.readBufferLock.unlock();
      bool = bool1;
      return bool;
    } finally {
      this.readBufferLock.unlock();
    } 
  }
  
  public String toString() {
    return signalDetected() ? String.format("IR Seeker: %3.0f%% signal at %6.1f degrees", new Object[] { Double.valueOf(getStrength() * 100.0D), Double.valueOf(getAngle()) }) : "IR Seeker:  --% signal at  ---.- degrees";
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\hitechnic\HiTechnicNxtIrSeekerSensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.qualcomm.hardware.modernrobotics;

import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cAddrConfig;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteOrder;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class ModernRoboticsI2cIrSeekerSensorV3 extends I2cDeviceSynchDevice<I2cDeviceSynch> implements IrSeekerSensor, I2cAddrConfig {
  public static final I2cAddr ADDRESS_I2C_DEFAULT = I2cAddr.create8bit(56);
  
  public static final double MAX_SENSOR_STRENGTH = 255.0D;
  
  protected IrSeekerSensor.Mode mode = IrSeekerSensor.Mode.MODE_1200HZ;
  
  protected double signalDetectedThreshold;
  
  public ModernRoboticsI2cIrSeekerSensorV3(I2cDeviceSynch paramI2cDeviceSynch) {
    super((I2cDeviceSynchSimple)paramI2cDeviceSynch, true);
    setOptimalReadWindow();
    ((I2cDeviceSynch)this.deviceClient).setI2cAddress(ADDRESS_I2C_DEFAULT);
    registerArmingStateCallback(false);
    ((I2cDeviceSynch)this.deviceClient).engage();
  }
  
  protected boolean doInitialize() {
    setMode(IrSeekerSensor.Mode.MODE_1200HZ);
    this.signalDetectedThreshold = 0.00392156862745098D;
    return true;
  }
  
  public double getAngle() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual getMode : ()Lcom/qualcomm/robotcore/hardware/IrSeekerSensor$Mode;
    //   6: getstatic com/qualcomm/robotcore/hardware/IrSeekerSensor$Mode.MODE_1200HZ : Lcom/qualcomm/robotcore/hardware/IrSeekerSensor$Mode;
    //   9: if_acmpne -> 20
    //   12: getstatic com/qualcomm/hardware/modernrobotics/ModernRoboticsI2cIrSeekerSensorV3$Register.DIR_DATA_1200 : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsI2cIrSeekerSensorV3$Register;
    //   15: astore #4
    //   17: goto -> 25
    //   20: getstatic com/qualcomm/hardware/modernrobotics/ModernRoboticsI2cIrSeekerSensorV3$Register.DIR_DATA_600 : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsI2cIrSeekerSensorV3$Register;
    //   23: astore #4
    //   25: aload_0
    //   26: aload #4
    //   28: invokevirtual read8 : (Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsI2cIrSeekerSensorV3$Register;)B
    //   31: istore_3
    //   32: iload_3
    //   33: i2d
    //   34: dstore_1
    //   35: aload_0
    //   36: monitorexit
    //   37: dload_1
    //   38: dreturn
    //   39: astore #4
    //   41: aload_0
    //   42: monitorexit
    //   43: aload #4
    //   45: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	39	finally
    //   20	25	39	finally
    //   25	32	39	finally
  }
  
  public String getDeviceName() {
    return String.format("%s %s", new Object[] { AppUtil.getDefContext().getString(R.string.configTypeIrSeekerV3), new RobotUsbDevice.FirmwareVersion(read8(Register.FIRMWARE_REV)) });
  }
  
  public I2cAddr getI2cAddress() {
    return ((I2cDeviceSynch)this.deviceClient).getI2cAddress();
  }
  
  public IrSeekerSensor.IrSeekerIndividualSensor[] getIndividualSensors() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual getMode : ()Lcom/qualcomm/robotcore/hardware/IrSeekerSensor$Mode;
    //   6: getstatic com/qualcomm/robotcore/hardware/IrSeekerSensor$Mode.MODE_1200HZ : Lcom/qualcomm/robotcore/hardware/IrSeekerSensor$Mode;
    //   9: if_acmpne -> 19
    //   12: getstatic com/qualcomm/hardware/modernrobotics/ModernRoboticsI2cIrSeekerSensorV3$Register.LEFT_SIDE_DATA_1200 : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsI2cIrSeekerSensorV3$Register;
    //   15: astore_1
    //   16: goto -> 23
    //   19: getstatic com/qualcomm/hardware/modernrobotics/ModernRoboticsI2cIrSeekerSensorV3$Register.LEFT_SIDE_DATA_600 : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsI2cIrSeekerSensorV3$Register;
    //   22: astore_1
    //   23: new com/qualcomm/robotcore/hardware/IrSeekerSensor$IrSeekerIndividualSensor
    //   26: dup
    //   27: ldc2_w -1.0
    //   30: aload_0
    //   31: aload_1
    //   32: invokevirtual readShort : (Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsI2cIrSeekerSensorV3$Register;)S
    //   35: i2d
    //   36: ldc2_w 255.0
    //   39: ddiv
    //   40: invokespecial <init> : (DD)V
    //   43: astore_2
    //   44: aload_0
    //   45: invokevirtual getMode : ()Lcom/qualcomm/robotcore/hardware/IrSeekerSensor$Mode;
    //   48: getstatic com/qualcomm/robotcore/hardware/IrSeekerSensor$Mode.MODE_1200HZ : Lcom/qualcomm/robotcore/hardware/IrSeekerSensor$Mode;
    //   51: if_acmpne -> 61
    //   54: getstatic com/qualcomm/hardware/modernrobotics/ModernRoboticsI2cIrSeekerSensorV3$Register.RIGHT_SIDE_DATA_1200 : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsI2cIrSeekerSensorV3$Register;
    //   57: astore_1
    //   58: goto -> 65
    //   61: getstatic com/qualcomm/hardware/modernrobotics/ModernRoboticsI2cIrSeekerSensorV3$Register.RIGHT_SIDE_DATA_600 : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsI2cIrSeekerSensorV3$Register;
    //   64: astore_1
    //   65: new com/qualcomm/robotcore/hardware/IrSeekerSensor$IrSeekerIndividualSensor
    //   68: dup
    //   69: dconst_1
    //   70: aload_0
    //   71: aload_1
    //   72: invokevirtual readShort : (Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsI2cIrSeekerSensorV3$Register;)S
    //   75: i2d
    //   76: ldc2_w 255.0
    //   79: ddiv
    //   80: invokespecial <init> : (DD)V
    //   83: astore_1
    //   84: aload_0
    //   85: monitorexit
    //   86: iconst_2
    //   87: anewarray com/qualcomm/robotcore/hardware/IrSeekerSensor$IrSeekerIndividualSensor
    //   90: dup
    //   91: iconst_0
    //   92: aload_2
    //   93: aastore
    //   94: dup
    //   95: iconst_1
    //   96: aload_1
    //   97: aastore
    //   98: areturn
    //   99: astore_1
    //   100: aload_0
    //   101: monitorexit
    //   102: aload_1
    //   103: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	99	finally
    //   19	23	99	finally
    //   23	58	99	finally
    //   61	65	99	finally
    //   65	84	99	finally
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.ModernRobotics;
  }
  
  public IrSeekerSensor.Mode getMode() {
    return this.mode;
  }
  
  public double getSignalDetectedThreshold() {
    return this.signalDetectedThreshold;
  }
  
  public double getStrength() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual getMode : ()Lcom/qualcomm/robotcore/hardware/IrSeekerSensor$Mode;
    //   6: getstatic com/qualcomm/robotcore/hardware/IrSeekerSensor$Mode.MODE_1200HZ : Lcom/qualcomm/robotcore/hardware/IrSeekerSensor$Mode;
    //   9: if_acmpne -> 19
    //   12: getstatic com/qualcomm/hardware/modernrobotics/ModernRoboticsI2cIrSeekerSensorV3$Register.SIGNAL_STRENTH_1200 : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsI2cIrSeekerSensorV3$Register;
    //   15: astore_3
    //   16: goto -> 23
    //   19: getstatic com/qualcomm/hardware/modernrobotics/ModernRoboticsI2cIrSeekerSensorV3$Register.SIGNAL_STRENTH_600 : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsI2cIrSeekerSensorV3$Register;
    //   22: astore_3
    //   23: aload_0
    //   24: aload_3
    //   25: invokevirtual read8 : (Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsI2cIrSeekerSensorV3$Register;)B
    //   28: invokestatic unsignedByteToDouble : (B)D
    //   31: dstore_1
    //   32: dload_1
    //   33: ldc2_w 255.0
    //   36: ddiv
    //   37: dstore_1
    //   38: aload_0
    //   39: monitorexit
    //   40: dload_1
    //   41: dreturn
    //   42: astore_3
    //   43: aload_0
    //   44: monitorexit
    //   45: aload_3
    //   46: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	42	finally
    //   19	23	42	finally
    //   23	32	42	finally
  }
  
  public int getVersion() {
    return 3;
  }
  
  public byte read8(Register paramRegister) {
    return ((I2cDeviceSynch)this.deviceClient).read8(paramRegister.bVal);
  }
  
  protected short readShort(Register paramRegister) {
    return TypeConversion.byteArrayToShort(((I2cDeviceSynch)this.deviceClient).read(paramRegister.bVal, 2), ByteOrder.LITTLE_ENDIAN);
  }
  
  public void setI2cAddress(I2cAddr paramI2cAddr) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield deviceClient : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchSimple;
    //   6: checkcast com/qualcomm/robotcore/hardware/I2cDeviceSynch
    //   9: aload_1
    //   10: invokeinterface setI2cAddress : (Lcom/qualcomm/robotcore/hardware/I2cAddr;)V
    //   15: aload_0
    //   16: monitorexit
    //   17: return
    //   18: astore_1
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_1
    //   22: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	18	finally
  }
  
  public void setMode(IrSeekerSensor.Mode paramMode) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield mode : Lcom/qualcomm/robotcore/hardware/IrSeekerSensor$Mode;
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
  
  protected void setOptimalReadWindow() {
    I2cDeviceSynch.ReadWindow readWindow = new I2cDeviceSynch.ReadWindow(Register.READ_WINDOW_FIRST.bVal, Register.READ_WINDOW_LAST.bVal - Register.READ_WINDOW_FIRST.bVal + 1, I2cDeviceSynch.ReadMode.REPEAT);
    ((I2cDeviceSynch)this.deviceClient).setReadWindow(readWindow);
  }
  
  public void setSignalDetectedThreshold(double paramDouble) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: dload_1
    //   4: putfield signalDetectedThreshold : D
    //   7: aload_0
    //   8: monitorexit
    //   9: return
    //   10: astore_3
    //   11: aload_0
    //   12: monitorexit
    //   13: aload_3
    //   14: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	10	finally
  }
  
  public boolean signalDetected() {
    return (getStrength() > this.signalDetectedThreshold);
  }
  
  public String toString() {
    return signalDetected() ? String.format("IR Seeker: %3.0f%% signal at %6.1f degrees", new Object[] { Double.valueOf(getStrength() * 100.0D), Double.valueOf(getAngle()) }) : "IR Seeker:  --% signal at  ---.- degrees";
  }
  
  public void write8(Register paramRegister, byte paramByte) {
    ((I2cDeviceSynch)this.deviceClient).write8(paramRegister.bVal, paramByte);
  }
  
  public enum Register {
    DIR_DATA_1200,
    DIR_DATA_600,
    FIRMWARE_REV,
    LEFT_SIDE_DATA_1200,
    LEFT_SIDE_DATA_600,
    MANUFACTURE_CODE,
    READ_WINDOW_FIRST(0),
    READ_WINDOW_LAST(0),
    RIGHT_SIDE_DATA_1200(0),
    RIGHT_SIDE_DATA_600(0),
    SENSOR_ID(0),
    SIGNAL_STRENTH_1200(0),
    SIGNAL_STRENTH_600(0),
    UNKNOWN(0),
    UNUSED(0);
    
    public byte bVal;
    
    static {
      DIR_DATA_1200 = new Register("DIR_DATA_1200", 5, 4);
      SIGNAL_STRENTH_1200 = new Register("SIGNAL_STRENTH_1200", 6, 5);
      DIR_DATA_600 = new Register("DIR_DATA_600", 7, 6);
      SIGNAL_STRENTH_600 = new Register("SIGNAL_STRENTH_600", 8, 7);
      LEFT_SIDE_DATA_1200 = new Register("LEFT_SIDE_DATA_1200", 9, 8);
      RIGHT_SIDE_DATA_1200 = new Register("RIGHT_SIDE_DATA_1200", 10, 10);
      LEFT_SIDE_DATA_600 = new Register("LEFT_SIDE_DATA_600", 11, 12);
      RIGHT_SIDE_DATA_600 = new Register("RIGHT_SIDE_DATA_600", 12, 14);
      READ_WINDOW_LAST = new Register("READ_WINDOW_LAST", 13, RIGHT_SIDE_DATA_600.bVal + 1);
      Register register = new Register("UNKNOWN", 14, -1);
      UNKNOWN = register;
      $VALUES = new Register[] { 
          READ_WINDOW_FIRST, FIRMWARE_REV, MANUFACTURE_CODE, SENSOR_ID, UNUSED, DIR_DATA_1200, SIGNAL_STRENTH_1200, DIR_DATA_600, SIGNAL_STRENTH_600, LEFT_SIDE_DATA_1200, 
          RIGHT_SIDE_DATA_1200, LEFT_SIDE_DATA_600, RIGHT_SIDE_DATA_600, READ_WINDOW_LAST, register };
    }
    
    Register(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\modernrobotics\ModernRoboticsI2cIrSeekerSensorV3.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
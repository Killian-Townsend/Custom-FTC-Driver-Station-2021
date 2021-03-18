package com.qualcomm.hardware.modernrobotics;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cAddrConfig;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.I2cWaitControl;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.TypeConversion;

public class ModernRoboticsI2cColorSensor extends I2cDeviceSynchDevice<I2cDeviceSynch> implements ColorSensor, NormalizedColorSensor, SwitchableLight, I2cAddrConfig {
  public static final I2cAddr ADDRESS_I2C_DEFAULT = I2cAddr.create8bit(60);
  
  protected final float colorNormalizationFactor = 1.5258789E-5F;
  
  protected boolean isLightOn = false;
  
  private float softwareGain = 1.0F;
  
  public ModernRoboticsI2cColorSensor(I2cDeviceSynch paramI2cDeviceSynch) {
    super((I2cDeviceSynchSimple)paramI2cDeviceSynch, true);
    I2cDeviceSynch.ReadWindow readWindow = new I2cDeviceSynch.ReadWindow(Register.READ_WINDOW_FIRST.bVal, Register.READ_WINDOW_LAST.bVal - Register.READ_WINDOW_FIRST.bVal + 1, I2cDeviceSynch.ReadMode.REPEAT);
    ((I2cDeviceSynch)this.deviceClient).setReadWindow(readWindow);
    ((I2cDeviceSynch)this.deviceClient).setI2cAddress(ADDRESS_I2C_DEFAULT);
    registerArmingStateCallback(false);
    ((I2cDeviceSynch)this.deviceClient).engage();
  }
  
  public int alpha() {
    return readUnsignedByte(Register.ALPHA);
  }
  
  public int argb() {
    return getNormalizedColors().toColor();
  }
  
  public int blue() {
    return readUnsignedByte(Register.BLUE);
  }
  
  protected boolean doInitialize() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iconst_1
    //   4: invokevirtual enableLed : (Z)V
    //   7: aload_0
    //   8: monitorexit
    //   9: iconst_1
    //   10: ireturn
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public void enableLed(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iload_1
    //   3: ifeq -> 13
    //   6: getstatic com/qualcomm/hardware/modernrobotics/ModernRoboticsI2cColorSensor$Command.ACTIVE_LED : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsI2cColorSensor$Command;
    //   9: astore_2
    //   10: goto -> 17
    //   13: getstatic com/qualcomm/hardware/modernrobotics/ModernRoboticsI2cColorSensor$Command.PASSIVE_LED : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsI2cColorSensor$Command;
    //   16: astore_2
    //   17: aload_0
    //   18: aload_2
    //   19: invokevirtual writeCommand : (Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsI2cColorSensor$Command;)V
    //   22: aload_0
    //   23: iload_1
    //   24: putfield isLightOn : Z
    //   27: aload_0
    //   28: monitorexit
    //   29: return
    //   30: astore_2
    //   31: aload_0
    //   32: monitorexit
    //   33: aload_2
    //   34: athrow
    // Exception table:
    //   from	to	target	type
    //   6	10	30	finally
    //   13	17	30	finally
    //   17	27	30	finally
  }
  
  public void enableLight(boolean paramBoolean) {
    enableLed(paramBoolean);
  }
  
  public String getDeviceName() {
    return String.format("Modern Robotics I2C Color Sensor %s", new Object[] { new RobotUsbDevice.FirmwareVersion(read8(Register.FIRMWARE_REV)) });
  }
  
  public float getGain() {
    return this.softwareGain;
  }
  
  public I2cAddr getI2cAddress() {
    return ((I2cDeviceSynch)this.deviceClient).getI2cAddress();
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.ModernRobotics;
  }
  
  public NormalizedRGBA getNormalizedColors() {
    NormalizedRGBA normalizedRGBA = new NormalizedRGBA();
    normalizedRGBA.red = Range.clip(this.softwareGain * readUnsignedShort(Register.NORMALIZED_RED_READING) * 1.5258789E-5F, 0.0F, 1.0F);
    normalizedRGBA.green = Range.clip(this.softwareGain * readUnsignedShort(Register.NORMALIZED_GREEN_READING) * 1.5258789E-5F, 0.0F, 1.0F);
    normalizedRGBA.blue = Range.clip(this.softwareGain * readUnsignedShort(Register.NORMALIZED_BLUE_READING) * 1.5258789E-5F, 0.0F, 1.0F);
    normalizedRGBA.alpha = Range.clip(this.softwareGain * readUnsignedShort(Register.NORMALIZED_ALPHA_READING) * 1.5258789E-5F, 0.0F, 1.0F);
    return normalizedRGBA;
  }
  
  public int green() {
    return readUnsignedByte(Register.GREEN);
  }
  
  public boolean isLightOn() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isLightOn : Z
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public byte read8(Register paramRegister) {
    return ((I2cDeviceSynch)this.deviceClient).read8(paramRegister.bVal);
  }
  
  public int readUnsignedByte(Register paramRegister) {
    return TypeConversion.unsignedByteToInt(read8(paramRegister));
  }
  
  public int readUnsignedShort(Register paramRegister) {
    return TypeConversion.unsignedShortToInt(TypeConversion.byteArrayToShort(((I2cDeviceSynch)this.deviceClient).read(paramRegister.bVal, 2)));
  }
  
  public int red() {
    return readUnsignedByte(Register.RED);
  }
  
  public void resetDeviceConfigurationForOpMode() {
    super.resetDeviceConfigurationForOpMode();
    this.softwareGain = 1.0F;
  }
  
  public void setGain(float paramFloat) {
    this.softwareGain = paramFloat;
  }
  
  public void setI2cAddress(I2cAddr paramI2cAddr) {
    ((I2cDeviceSynch)this.deviceClient).setI2cAddress(paramI2cAddr);
  }
  
  public String toString() {
    return String.format("argb: 0x%08x", new Object[] { Integer.valueOf(argb()) });
  }
  
  public void write8(Register paramRegister, byte paramByte) {
    ((I2cDeviceSynch)this.deviceClient).write8(paramRegister.bVal, paramByte);
  }
  
  public void writeCommand(Command paramCommand) {
    ((I2cDeviceSynch)this.deviceClient).waitForWriteCompletions(I2cWaitControl.ATOMIC);
    write8(Register.COMMAND, paramCommand.bVal);
  }
  
  public enum Command {
    ACTIVE_LED(0),
    CALIBRATE_BLACK(0),
    CALIBRATE_WHITE(0),
    HZ50(0),
    HZ60(0),
    PASSIVE_LED(1);
    
    public byte bVal;
    
    static {
      CALIBRATE_BLACK = new Command("CALIBRATE_BLACK", 4, 66);
      Command command = new Command("CALIBRATE_WHITE", 5, 67);
      CALIBRATE_WHITE = command;
      $VALUES = new Command[] { ACTIVE_LED, PASSIVE_LED, HZ50, HZ60, CALIBRATE_BLACK, command };
    }
    
    Command(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
  }
  
  public enum Register {
    FIRMWARE_REV(0),
    GREEN(0),
    GREEN_INDEX(0),
    GREEN_READING(0),
    ALPHA(1),
    ALPHA_READING(1),
    BLUE(1),
    BLUE_INDEX(1),
    BLUE_READING(1),
    COLOR_INDEX(1),
    COLOR_NUMBER(1),
    COMMAND(1),
    MANUFACTURE_CODE(1),
    NORMALIZED_ALPHA_READING(1),
    NORMALIZED_BLUE_READING(1),
    NORMALIZED_GREEN_READING(1),
    NORMALIZED_RED_READING(1),
    READ_WINDOW_FIRST(1),
    READ_WINDOW_LAST(1),
    RED(1),
    RED_INDEX(1),
    RED_READING(1),
    SENSOR_ID(2);
    
    public byte bVal;
    
    static {
      COLOR_NUMBER = new Register("COLOR_NUMBER", 4, 4);
      RED = new Register("RED", 5, 5);
      GREEN = new Register("GREEN", 6, 6);
      BLUE = new Register("BLUE", 7, 7);
      ALPHA = new Register("ALPHA", 8, 8);
      COLOR_INDEX = new Register("COLOR_INDEX", 9, 9);
      RED_INDEX = new Register("RED_INDEX", 10, 10);
      GREEN_INDEX = new Register("GREEN_INDEX", 11, 11);
      BLUE_INDEX = new Register("BLUE_INDEX", 12, 12);
      RED_READING = new Register("RED_READING", 13, 14);
      GREEN_READING = new Register("GREEN_READING", 14, 16);
      BLUE_READING = new Register("BLUE_READING", 15, 18);
      ALPHA_READING = new Register("ALPHA_READING", 16, 20);
      NORMALIZED_RED_READING = new Register("NORMALIZED_RED_READING", 17, 22);
      NORMALIZED_GREEN_READING = new Register("NORMALIZED_GREEN_READING", 18, 24);
      NORMALIZED_BLUE_READING = new Register("NORMALIZED_BLUE_READING", 19, 26);
      NORMALIZED_ALPHA_READING = new Register("NORMALIZED_ALPHA_READING", 20, 28);
      READ_WINDOW_FIRST = new Register("READ_WINDOW_FIRST", 21, RED.bVal);
      Register register = new Register("READ_WINDOW_LAST", 22, NORMALIZED_ALPHA_READING.bVal + 1);
      READ_WINDOW_LAST = register;
      $VALUES = new Register[] { 
          FIRMWARE_REV, MANUFACTURE_CODE, SENSOR_ID, COMMAND, COLOR_NUMBER, RED, GREEN, BLUE, ALPHA, COLOR_INDEX, 
          RED_INDEX, GREEN_INDEX, BLUE_INDEX, RED_READING, GREEN_READING, BLUE_READING, ALPHA_READING, NORMALIZED_RED_READING, NORMALIZED_GREEN_READING, NORMALIZED_BLUE_READING, 
          NORMALIZED_ALPHA_READING, READ_WINDOW_FIRST, register };
    }
    
    Register(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\modernrobotics\ModernRoboticsI2cColorSensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
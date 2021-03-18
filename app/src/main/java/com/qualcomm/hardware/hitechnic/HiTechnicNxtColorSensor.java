package com.qualcomm.hardware.hitechnic;

import android.graphics.Color;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cAddressableDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.I2cWaitControl;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.util.TypeConversion;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class HiTechnicNxtColorSensor extends I2cDeviceSynchDevice<I2cDeviceSynch> implements ColorSensor, NormalizedColorSensor, SwitchableLight, I2cAddressableDevice {
  public static final I2cAddr ADDRESS_I2C = I2cAddr.create8bit(2);
  
  protected final float colorNormalizationFactor = 0.00390625F;
  
  protected boolean isLightOn = false;
  
  public HiTechnicNxtColorSensor(I2cDeviceSynch paramI2cDeviceSynch) {
    super((I2cDeviceSynchSimple)paramI2cDeviceSynch, true);
    I2cDeviceSynch.ReadWindow readWindow = new I2cDeviceSynch.ReadWindow(Register.READ_WINDOW_FIRST.bVal, Register.READ_WINDOW_LAST.bVal - Register.READ_WINDOW_FIRST.bVal + 1, I2cDeviceSynch.ReadMode.REPEAT);
    ((I2cDeviceSynch)this.deviceClient).setReadWindow(readWindow);
    ((I2cDeviceSynch)this.deviceClient).setI2cAddress(ADDRESS_I2C);
    ((I2cDeviceSynch)this.deviceClient).engage();
    registerArmingStateCallback(false);
  }
  
  public int alpha() {
    return 0;
  }
  
  public int argb() {
    return Color.argb(alpha(), red(), green(), blue());
  }
  
  public int blue() {
    return readColorByte(Register.BLUE);
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
    //   6: getstatic com/qualcomm/hardware/hitechnic/HiTechnicNxtColorSensor$Command.ACTIVE_LED : Lcom/qualcomm/hardware/hitechnic/HiTechnicNxtColorSensor$Command;
    //   9: astore_2
    //   10: goto -> 17
    //   13: getstatic com/qualcomm/hardware/hitechnic/HiTechnicNxtColorSensor$Command.PASSIVE_LED : Lcom/qualcomm/hardware/hitechnic/HiTechnicNxtColorSensor$Command;
    //   16: astore_2
    //   17: aload_0
    //   18: aload_2
    //   19: invokevirtual writeCommand : (Lcom/qualcomm/hardware/hitechnic/HiTechnicNxtColorSensor$Command;)V
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
    return AppUtil.getDefContext().getString(R.string.configTypeHTColorSensor);
  }
  
  public float getGain() {
    return 1.0F;
  }
  
  public I2cAddr getI2cAddress() {
    return ADDRESS_I2C;
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.HiTechnic;
  }
  
  public NormalizedRGBA getNormalizedColors() {
    NormalizedRGBA normalizedRGBA = new NormalizedRGBA();
    normalizedRGBA.red = red() * 0.00390625F;
    normalizedRGBA.green = green() * 0.00390625F;
    normalizedRGBA.blue = blue() * 0.00390625F;
    normalizedRGBA.alpha = Math.max(Math.max(normalizedRGBA.red, normalizedRGBA.green), normalizedRGBA.blue);
    return normalizedRGBA;
  }
  
  public int getVersion() {
    return 2;
  }
  
  public int green() {
    return readColorByte(Register.GREEN);
  }
  
  public boolean isLightOn() {
    return this.isLightOn;
  }
  
  public byte read8(Register paramRegister) {
    return ((I2cDeviceSynch)this.deviceClient).read8(paramRegister.bVal);
  }
  
  public int readColorByte(Register paramRegister) {
    return TypeConversion.unsignedByteToInt(read8(paramRegister));
  }
  
  public int red() {
    return readColorByte(Register.RED);
  }
  
  public void setGain(float paramFloat) {}
  
  public void setI2cAddress(I2cAddr paramI2cAddr) {
    throw new UnsupportedOperationException("setI2cAddress is not supported.");
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
    PASSIVE_LED(0);
    
    public byte bVal;
    
    static {
      Command command = new Command("PASSIVE_LED", 1, 1);
      PASSIVE_LED = command;
      $VALUES = new Command[] { ACTIVE_LED, command };
    }
    
    Command(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
  }
  
  public enum Register {
    BLUE(0),
    COLOR_NUMBER(0),
    COMMAND(65),
    GREEN(65),
    READ_WINDOW_FIRST(65),
    READ_WINDOW_LAST(65),
    RED(65);
    
    public byte bVal;
    
    static {
      GREEN = new Register("GREEN", 3, 68);
      BLUE = new Register("BLUE", 4, 69);
      READ_WINDOW_FIRST = new Register("READ_WINDOW_FIRST", 5, RED.bVal);
      Register register = new Register("READ_WINDOW_LAST", 6, BLUE.bVal);
      READ_WINDOW_LAST = register;
      $VALUES = new Register[] { COMMAND, COLOR_NUMBER, RED, GREEN, BLUE, READ_WINDOW_FIRST, register };
    }
    
    Register(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\hitechnic\HiTechnicNxtColorSensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
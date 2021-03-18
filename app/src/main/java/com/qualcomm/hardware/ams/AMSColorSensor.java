package com.qualcomm.hardware.ams;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

public interface AMSColorSensor extends ColorSensor, NormalizedColorSensor {
  public static final int AMS_COLOR_COMMAND_BIT = 128;
  
  public static final int AMS_COLOR_COMMAND_TYPE_AUTO_INCREMENT = 32;
  
  public static final int AMS_COLOR_COMMAND_TYPE_REPEATED_BYTE = 0;
  
  public static final int AMS_COLOR_COMMAND_TYPE_RESERVED = 512;
  
  public static final int AMS_COLOR_COMMAND_TYPE_SPECIAL = 544;
  
  public static final I2cAddr AMS_TCS34725_ADDRESS = I2cAddr.create7bit(41);
  
  public static final byte AMS_TCS34725_ID = 68;
  
  public static final I2cAddr AMS_TMD37821_ADDRESS = I2cAddr.create7bit(57);
  
  public static final byte AMS_TMD37821_ID = 96;
  
  public static final byte AMS_TMD37823_ID = 105;
  
  byte getDeviceID();
  
  Parameters getParameters();
  
  boolean initialize(Parameters paramParameters);
  
  byte[] read(Register paramRegister, int paramInt);
  
  byte read8(Register paramRegister);
  
  void write(Register paramRegister, byte[] paramArrayOfbyte);
  
  void write8(Register paramRegister, int paramInt);
  
  public enum Config {
    LONG_WAIT,
    NORMAL(0);
    
    public final byte bVal;
    
    static {
      Config config = new Config("LONG_WAIT", 1, 2);
      LONG_WAIT = config;
      $VALUES = new Config[] { NORMAL, config };
    }
    
    Config(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
  }
  
  public enum Enable {
    AEN(0),
    AIEN(0),
    OFF(0),
    PEN(0),
    PIEN(0),
    PON(0),
    RES6(0),
    RES7(128),
    UNKNOWN(128),
    WEN(128);
    
    public final byte bVal;
    
    static {
      PIEN = new Enable("PIEN", 2, 32);
      AIEN = new Enable("AIEN", 3, 16);
      WEN = new Enable("WEN", 4, 8);
      PEN = new Enable("PEN", 5, 4);
      AEN = new Enable("AEN", 6, 2);
      PON = new Enable("PON", 7, 1);
      OFF = new Enable("OFF", 8, 0);
      Enable enable = new Enable("UNKNOWN", 9, -1);
      UNKNOWN = enable;
      $VALUES = new Enable[] { RES7, RES6, PIEN, AIEN, WEN, PEN, AEN, PON, OFF, enable };
    }
    
    Enable(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
    
    public byte bitOr(byte param1Byte) {
      return (byte)(param1Byte | this.bVal);
    }
    
    public byte bitOr(Enable param1Enable) {
      byte b = this.bVal;
      return (byte)(param1Enable.bVal | b);
    }
  }
  
  public enum Gain {
    GAIN_1(128),
    GAIN_16(128),
    GAIN_4(128),
    GAIN_64(128),
    MASK(128),
    UNKNOWN(-1);
    
    public final byte bVal;
    
    static {
      GAIN_16 = new Gain("GAIN_16", 3, 2);
      GAIN_64 = new Gain("GAIN_64", 4, 3);
      Gain gain = new Gain("MASK", 5, 3);
      MASK = gain;
      $VALUES = new Gain[] { UNKNOWN, GAIN_1, GAIN_4, GAIN_16, GAIN_64, gain };
    }
    
    Gain(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
    
    public static Gain fromByte(byte param1Byte) {
      for (Gain gain : values()) {
        if (gain.bVal == param1Byte)
          return gain; 
      } 
      return UNKNOWN;
    }
  }
  
  public enum LEDDrive {
    MASK(-1),
    Percent100(0),
    Percent12_5(0),
    Percent25(0),
    Percent50(64);
    
    public final byte bVal;
    
    static {
      Percent12_5 = new LEDDrive("Percent12_5", 3, 1088);
      LEDDrive lEDDrive = new LEDDrive("MASK", 4, 192);
      MASK = lEDDrive;
      $VALUES = new LEDDrive[] { Percent100, Percent50, Percent25, Percent12_5, lEDDrive };
    }
    
    LEDDrive(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
  }
  
  public static class Parameters implements Cloneable {
    public int atime = atimeFromMs(24.0F);
    
    public int deviceId;
    
    public AMSColorSensor.Gain gain = AMSColorSensor.Gain.GAIN_4;
    
    public I2cAddr i2cAddr;
    
    public AMSColorSensor.LEDDrive ledDrive = AMSColorSensor.LEDDrive.Percent12_5;
    
    public boolean loggingEnabled = false;
    
    public String loggingTag = "AMSColorSensor";
    
    public int proximityPulseCount = 8;
    
    public int proximitySaturation = 1023;
    
    public I2cDeviceSynch.ReadWindow readWindow = new I2cDeviceSynch.ReadWindow(AMSColorSensor.Register.READ_WINDOW_FIRST.bVal, AMSColorSensor.Register.READ_WINDOW_LAST.bVal - AMSColorSensor.Register.READ_WINDOW_FIRST.bVal + 1, I2cDeviceSynch.ReadMode.REPEAT);
    
    public boolean useProximityIfAvailable = true;
    
    public Parameters(I2cAddr param1I2cAddr, int param1Int) {
      this.i2cAddr = param1I2cAddr;
      this.deviceId = param1Int;
    }
    
    public static int atimeFromMs(float param1Float) {
      return Math.max(0, 256 - (int)Math.ceil((param1Float / 2.4F)));
    }
    
    public static Parameters createForTCS34725() {
      return new Parameters(AMSColorSensor.AMS_TCS34725_ADDRESS, 68);
    }
    
    public static Parameters createForTMD37821() {
      return new Parameters(AMSColorSensor.AMS_TMD37821_ADDRESS, 96);
    }
    
    public Parameters clone() {
      try {
        return (Parameters)super.clone();
      } catch (CloneNotSupportedException cloneNotSupportedException) {
        throw new RuntimeException("internal error: Parameters not cloneable");
      } 
    }
    
    public int getMaximumReading() {
      return Math.min(65535, integrationCycles() * 1024);
    }
    
    public int integrationCycles() {
      return 256 - this.atime;
    }
    
    public float msAccumulationInterval() {
      return integrationCycles() * 2.4F;
    }
  }
  
  public enum Pers {
    CYCLE_NONE(0),
    UNKNOWN(0),
    CYCLE_1(64),
    CYCLE_10(64),
    CYCLE_15(64),
    CYCLE_2(64),
    CYCLE_20(64),
    CYCLE_25(64),
    CYCLE_3(64),
    CYCLE_30(64),
    CYCLE_35(64),
    CYCLE_40(64),
    CYCLE_45(64),
    CYCLE_5(64),
    CYCLE_50(64),
    CYCLE_55(64),
    CYCLE_60(64);
    
    public final byte bVal;
    
    static {
      CYCLE_10 = new Pers("CYCLE_10", 5, 5);
      CYCLE_15 = new Pers("CYCLE_15", 6, 6);
      CYCLE_20 = new Pers("CYCLE_20", 7, 7);
      CYCLE_25 = new Pers("CYCLE_25", 8, 8);
      CYCLE_30 = new Pers("CYCLE_30", 9, 9);
      CYCLE_35 = new Pers("CYCLE_35", 10, 10);
      CYCLE_40 = new Pers("CYCLE_40", 11, 11);
      CYCLE_45 = new Pers("CYCLE_45", 12, 12);
      CYCLE_50 = new Pers("CYCLE_50", 13, 13);
      CYCLE_55 = new Pers("CYCLE_55", 14, 14);
      CYCLE_60 = new Pers("CYCLE_60", 15, 15);
      Pers pers = new Pers("UNKNOWN", 16, -1);
      UNKNOWN = pers;
      $VALUES = new Pers[] { 
          CYCLE_NONE, CYCLE_1, CYCLE_2, CYCLE_3, CYCLE_5, CYCLE_10, CYCLE_15, CYCLE_20, CYCLE_25, CYCLE_30, 
          CYCLE_35, CYCLE_40, CYCLE_45, CYCLE_50, CYCLE_55, CYCLE_60, pers };
    }
    
    Pers(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
  }
  
  public enum Register {
    AIHT(0),
    AILT(0),
    ALPHA(0),
    ATIME(0),
    BLUE(0),
    CONFIGURATION(0),
    CONTROL(0),
    DEVICE_ID(0),
    ENABLE(0),
    GREEN(0),
    PDATA(0),
    PERS(0),
    PPLUSE(0),
    READ_WINDOW_FIRST(0),
    READ_WINDOW_LAST(0),
    RED(0),
    REGISTER2(0),
    STATUS(0),
    WTIME(0);
    
    public final byte bVal;
    
    static {
      AILT = new Register("AILT", 4, 4);
      AIHT = new Register("AIHT", 5, 6);
      PERS = new Register("PERS", 6, 12);
      CONFIGURATION = new Register("CONFIGURATION", 7, 13);
      PPLUSE = new Register("PPLUSE", 8, 14);
      CONTROL = new Register("CONTROL", 9, 15);
      DEVICE_ID = new Register("DEVICE_ID", 10, 18);
      STATUS = new Register("STATUS", 11, 19);
      ALPHA = new Register("ALPHA", 12, 20);
      RED = new Register("RED", 13, 22);
      GREEN = new Register("GREEN", 14, 24);
      BLUE = new Register("BLUE", 15, 26);
      PDATA = new Register("PDATA", 16, 28);
      READ_WINDOW_FIRST = new Register("READ_WINDOW_FIRST", 17, WTIME.bVal);
      Register register = new Register("READ_WINDOW_LAST", 18, BLUE.bVal + 1);
      READ_WINDOW_LAST = register;
      $VALUES = new Register[] { 
          ENABLE, ATIME, REGISTER2, WTIME, AILT, AIHT, PERS, CONFIGURATION, PPLUSE, CONTROL, 
          DEVICE_ID, STATUS, ALPHA, RED, GREEN, BLUE, PDATA, READ_WINDOW_FIRST, register };
    }
    
    Register(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
  }
  
  public enum Status {
    AINT(0),
    AVALID(0),
    PINT(32),
    PVALID(32);
    
    public final byte bVal;
    
    static {
      Status status = new Status("AVALID", 3, 1);
      AVALID = status;
      $VALUES = new Status[] { PINT, AINT, PVALID, status };
    }
    
    Status(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
  }
  
  public enum Wait {
    MS_204(32),
    MS_2_4(255),
    MS_614(255),
    UNKNOWN(255);
    
    public final byte bVal;
    
    static {
      Wait wait = new Wait("UNKNOWN", 3, -2);
      UNKNOWN = wait;
      $VALUES = new Wait[] { MS_2_4, MS_204, MS_614, wait };
    }
    
    Wait(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\ams\AMSColorSensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
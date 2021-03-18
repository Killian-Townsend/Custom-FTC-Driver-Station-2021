package com.qualcomm.hardware.broadcom;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

public interface BroadcomColorSensor extends ColorSensor, NormalizedColorSensor {
  public static final I2cAddr BROADCOM_APDS9151_ADDRESS = I2cAddr.create7bit(82);
  
  public static final byte BROADCOM_APDS9151_ID = -62;
  
  byte getDeviceID();
  
  Parameters getParameters();
  
  boolean initialize(Parameters paramParameters);
  
  byte[] read(Register paramRegister, int paramInt);
  
  byte read8(Register paramRegister);
  
  void write(Register paramRegister, byte[] paramArrayOfbyte);
  
  void write8(Register paramRegister, int paramInt);
  
  public enum Gain {
    GAIN_1,
    GAIN_18,
    GAIN_3,
    GAIN_6,
    GAIN_9,
    UNKNOWN(-1);
    
    public final byte bVal;
    
    static {
      Gain gain = new Gain("GAIN_18", 5, 4);
      GAIN_18 = gain;
      $VALUES = new Gain[] { UNKNOWN, GAIN_1, GAIN_3, GAIN_6, GAIN_9, gain };
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
  
  public enum LEDCurrent {
    CURRENT_100mA(-1),
    CURRENT_10mA(-1),
    CURRENT_125mA(-1),
    CURRENT_25mA(-1),
    CURRENT_2_5mA(0),
    CURRENT_50mA(0),
    CURRENT_5mA(1),
    CURRENT_75mA(1);
    
    public final byte bVal;
    
    static {
      CURRENT_100mA = new LEDCurrent("CURRENT_100mA", 6, 6);
      LEDCurrent lEDCurrent = new LEDCurrent("CURRENT_125mA", 7, 7);
      CURRENT_125mA = lEDCurrent;
      $VALUES = new LEDCurrent[] { CURRENT_2_5mA, CURRENT_5mA, CURRENT_10mA, CURRENT_25mA, CURRENT_50mA, CURRENT_75mA, CURRENT_100mA, lEDCurrent };
    }
    
    LEDCurrent(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
    
    public byte bitOr(byte param1Byte) {
      return (byte)(param1Byte | this.bVal);
    }
    
    public byte bitOr(LEDCurrent param1LEDCurrent) {
      byte b = this.bVal;
      return (byte)(param1LEDCurrent.bVal | b);
    }
  }
  
  public enum LEDPulseModulation {
    RES0(0),
    LED_PULSE_100kHz(1),
    LED_PULSE_60kHz(1),
    LED_PULSE_70kHz(1),
    LED_PULSE_80kHz(1),
    LED_PULSE_90kHz(1),
    RES1(1),
    RES2(2);
    
    public final byte bVal;
    
    static {
      LEDPulseModulation lEDPulseModulation = new LEDPulseModulation("LED_PULSE_100kHz", 7, 7);
      LED_PULSE_100kHz = lEDPulseModulation;
      $VALUES = new LEDPulseModulation[] { RES0, RES1, RES2, LED_PULSE_60kHz, LED_PULSE_70kHz, LED_PULSE_80kHz, LED_PULSE_90kHz, lEDPulseModulation };
    }
    
    LEDPulseModulation(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
    
    public byte bitOr(byte param1Byte) {
      return (byte)(param1Byte | this.bVal);
    }
    
    public byte bitOr(LEDPulseModulation param1LEDPulseModulation) {
      byte b = this.bVal;
      return (byte)(param1LEDPulseModulation.bVal | b);
    }
  }
  
  public enum LSMeasurementRate {
    R25ms(0),
    R500ms(0),
    R50ms(1),
    R1000ms(2),
    R100ms(2),
    R2000ms_1(2),
    R2000ms_2(2),
    R200ms(2);
    
    public final byte bVal;
    
    static {
      R1000ms = new LSMeasurementRate("R1000ms", 5, 5);
      R2000ms_1 = new LSMeasurementRate("R2000ms_1", 6, 6);
      LSMeasurementRate lSMeasurementRate = new LSMeasurementRate("R2000ms_2", 7, 7);
      R2000ms_2 = lSMeasurementRate;
      $VALUES = new LSMeasurementRate[] { R25ms, R50ms, R100ms, R200ms, R500ms, R1000ms, R2000ms_1, lSMeasurementRate };
    }
    
    LSMeasurementRate(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
    
    public byte bitOr(byte param1Byte) {
      return (byte)(param1Byte | this.bVal);
    }
    
    public byte bitOr(BroadcomColorSensor.PSMeasurementRate param1PSMeasurementRate) {
      byte b = this.bVal;
      return (byte)(param1PSMeasurementRate.bVal | b);
    }
  }
  
  public enum LSResolution {
    R20BIT(0),
    RES_1(0),
    RES_2(0),
    R13BIT(1),
    R16BIT(1),
    R17BIT(1),
    R18BIT(1),
    R19BIT(1);
    
    public final byte bVal;
    
    static {
      R18BIT = new LSResolution("R18BIT", 2, 2);
      R17BIT = new LSResolution("R17BIT", 3, 3);
      R16BIT = new LSResolution("R16BIT", 4, 4);
      R13BIT = new LSResolution("R13BIT", 5, 5);
      RES_1 = new LSResolution("RES_1", 6, 6);
      LSResolution lSResolution = new LSResolution("RES_2", 7, 7);
      RES_2 = lSResolution;
      $VALUES = new LSResolution[] { R20BIT, R19BIT, R18BIT, R17BIT, R16BIT, R13BIT, RES_1, lSResolution };
    }
    
    LSResolution(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
    
    public byte bitOr(byte param1Byte) {
      return (byte)(param1Byte | this.bVal);
    }
    
    public byte bitOr(LSResolution param1LSResolution) {
      byte b = this.bVal;
      return (byte)(param1LSResolution.bVal | b);
    }
  }
  
  public enum MainControl {
    LS_EN(0),
    OFF(0),
    PS_EN(0),
    RES3(0),
    RES7(128),
    RGB_MODE(128),
    SAI_LS(128),
    SAI_PS(64),
    SW_RESET(64);
    
    public final byte bVal;
    
    static {
      RES3 = new MainControl("RES3", 4, 8);
      RGB_MODE = new MainControl("RGB_MODE", 5, 4);
      LS_EN = new MainControl("LS_EN", 6, 2);
      PS_EN = new MainControl("PS_EN", 7, 1);
      MainControl mainControl = new MainControl("OFF", 8, 0);
      OFF = mainControl;
      $VALUES = new MainControl[] { RES7, SAI_PS, SAI_LS, SW_RESET, RES3, RGB_MODE, LS_EN, PS_EN, mainControl };
    }
    
    MainControl(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
    
    public byte bitOr(byte param1Byte) {
      return (byte)(param1Byte | this.bVal);
    }
    
    public byte bitOr(MainControl param1MainControl) {
      byte b = this.bVal;
      return (byte)(param1MainControl.bVal | b);
    }
  }
  
  public enum MainStatus {
    POWER_ON_STATUS(32),
    PS_DATA_STAT(32),
    PS_INT_STAT(32),
    PS_LOGIC_SIG_STAT(32),
    LS_DATA_STATUS(64),
    LS_INT_STAT(64);
    
    public final byte bVal;
    
    static {
      LS_DATA_STATUS = new MainStatus("LS_DATA_STATUS", 2, 8);
      PS_LOGIC_SIG_STAT = new MainStatus("PS_LOGIC_SIG_STAT", 3, 4);
      PS_INT_STAT = new MainStatus("PS_INT_STAT", 4, 2);
      MainStatus mainStatus = new MainStatus("PS_DATA_STAT", 5, 1);
      PS_DATA_STAT = mainStatus;
      $VALUES = new MainStatus[] { POWER_ON_STATUS, LS_INT_STAT, LS_DATA_STATUS, PS_LOGIC_SIG_STAT, PS_INT_STAT, mainStatus };
    }
    
    MainStatus(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
    
    public byte bitOr(byte param1Byte) {
      return (byte)(param1Byte | this.bVal);
    }
    
    public byte bitOr(MainStatus param1MainStatus) {
      byte b = this.bVal;
      return (byte)(param1MainStatus.bVal | b);
    }
  }
  
  public enum PSMeasurementRate {
    R100ms(32),
    R12_5ms(32),
    R200ms(32),
    R25ms(32),
    R400ms(32),
    R50ms(32),
    R6_25ms(32),
    RES(0);
    
    public final byte bVal;
    
    static {
      R12_5ms = new PSMeasurementRate("R12_5ms", 2, 2);
      R25ms = new PSMeasurementRate("R25ms", 3, 3);
      R50ms = new PSMeasurementRate("R50ms", 4, 4);
      R100ms = new PSMeasurementRate("R100ms", 5, 5);
      R200ms = new PSMeasurementRate("R200ms", 6, 6);
      PSMeasurementRate pSMeasurementRate = new PSMeasurementRate("R400ms", 7, 7);
      R400ms = pSMeasurementRate;
      $VALUES = new PSMeasurementRate[] { RES, R6_25ms, R12_5ms, R25ms, R50ms, R100ms, R200ms, pSMeasurementRate };
    }
    
    PSMeasurementRate(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
    
    public byte bitOr(byte param1Byte) {
      return (byte)(param1Byte | this.bVal);
    }
    
    public byte bitOr(PSMeasurementRate param1PSMeasurementRate) {
      byte b = this.bVal;
      return (byte)(param1PSMeasurementRate.bVal | b);
    }
  }
  
  public enum PSResolution {
    R10BIT(0),
    R11BIT(0),
    R8BIT(0),
    R9BIT(1);
    
    public final byte bVal;
    
    static {
      PSResolution pSResolution = new PSResolution("R11BIT", 3, 3);
      R11BIT = pSResolution;
      $VALUES = new PSResolution[] { R8BIT, R9BIT, R10BIT, pSResolution };
    }
    
    PSResolution(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
    
    public byte bitOr(byte param1Byte) {
      return (byte)(param1Byte | this.bVal);
    }
    
    public byte bitOr(PSResolution param1PSResolution) {
      byte b = this.bVal;
      return (byte)(param1PSResolution.bVal | b);
    }
  }
  
  public static class Parameters implements Cloneable {
    public static BroadcomColorSensor.LSResolution lightSensorResolution = BroadcomColorSensor.LSResolution.R16BIT;
    
    public static BroadcomColorSensor.PSResolution proximityResolution = BroadcomColorSensor.PSResolution.R11BIT;
    
    public int colorSaturation = 65535;
    
    public int deviceId;
    
    public BroadcomColorSensor.Gain gain = BroadcomColorSensor.Gain.GAIN_3;
    
    public I2cAddr i2cAddr;
    
    public BroadcomColorSensor.LEDCurrent ledCurrent = BroadcomColorSensor.LEDCurrent.CURRENT_125mA;
    
    public BroadcomColorSensor.LSMeasurementRate lightSensorMeasRate = BroadcomColorSensor.LSMeasurementRate.R100ms;
    
    public boolean loggingEnabled = false;
    
    public String loggingTag = "BroadcomColorSensor";
    
    public BroadcomColorSensor.PSMeasurementRate proximityMeasRate = BroadcomColorSensor.PSMeasurementRate.R100ms;
    
    public int proximityPulseCount = 32;
    
    public int proximitySaturation = 2047;
    
    public BroadcomColorSensor.LEDPulseModulation pulseModulation = BroadcomColorSensor.LEDPulseModulation.LED_PULSE_60kHz;
    
    public I2cDeviceSynch.ReadWindow readWindow = new I2cDeviceSynch.ReadWindow(BroadcomColorSensor.Register.READ_WINDOW_FIRST.bVal, BroadcomColorSensor.Register.READ_WINDOW_LAST.bVal - BroadcomColorSensor.Register.READ_WINDOW_FIRST.bVal + 1, I2cDeviceSynch.ReadMode.REPEAT);
    
    static {
    
    }
    
    public Parameters(I2cAddr param1I2cAddr, int param1Int) {
      this.i2cAddr = param1I2cAddr;
      this.deviceId = param1Int;
    }
    
    public static Parameters createForAPDS9151() {
      return new Parameters(BroadcomColorSensor.BROADCOM_APDS9151_ADDRESS, -62);
    }
    
    public Parameters clone() {
      try {
        return (Parameters)super.clone();
      } catch (CloneNotSupportedException cloneNotSupportedException) {
        throw new RuntimeException("internal error: Parameters not cloneable");
      } 
    }
  }
  
  public enum Register {
    MAIN_CTRL(0),
    MAIN_STATUS(0),
    PART_ID(0),
    PS_CAN(0),
    PS_DATA(0),
    INT_CFG(1),
    INT_PST(1),
    LS_DATA_BLUE(1),
    LS_DATA_GREEN(1),
    LS_DATA_IR(1),
    LS_DATA_RED(1),
    LS_GAIN(1),
    LS_MEAS_RATE(1),
    LS_THRES_LOW(1),
    LS_THRES_UP(1),
    LS_THRES_VAR(1),
    PS_LED(1),
    PS_MEAS_RATE(1),
    PS_PULSES(2),
    PS_THRES_LOW(2),
    PS_THRES_UP(2),
    READ_WINDOW_FIRST(2),
    READ_WINDOW_LAST(2);
    
    public final byte bVal;
    
    static {
      LS_MEAS_RATE = new Register("LS_MEAS_RATE", 4, 4);
      LS_GAIN = new Register("LS_GAIN", 5, 5);
      PART_ID = new Register("PART_ID", 6, 6);
      MAIN_STATUS = new Register("MAIN_STATUS", 7, 7);
      PS_DATA = new Register("PS_DATA", 8, 8);
      LS_DATA_IR = new Register("LS_DATA_IR", 9, 10);
      LS_DATA_GREEN = new Register("LS_DATA_GREEN", 10, 13);
      LS_DATA_BLUE = new Register("LS_DATA_BLUE", 11, 16);
      LS_DATA_RED = new Register("LS_DATA_RED", 12, 19);
      INT_CFG = new Register("INT_CFG", 13, 25);
      INT_PST = new Register("INT_PST", 14, 26);
      PS_THRES_UP = new Register("PS_THRES_UP", 15, 27);
      PS_THRES_LOW = new Register("PS_THRES_LOW", 16, 30);
      PS_CAN = new Register("PS_CAN", 17, 31);
      LS_THRES_UP = new Register("LS_THRES_UP", 18, 33);
      LS_THRES_LOW = new Register("LS_THRES_LOW", 19, 36);
      LS_THRES_VAR = new Register("LS_THRES_VAR", 20, 39);
      READ_WINDOW_FIRST = new Register("READ_WINDOW_FIRST", 21, PS_DATA.bVal);
      Register register = new Register("READ_WINDOW_LAST", 22, LS_DATA_RED.bVal + 1);
      READ_WINDOW_LAST = register;
      $VALUES = new Register[] { 
          MAIN_CTRL, PS_LED, PS_PULSES, PS_MEAS_RATE, LS_MEAS_RATE, LS_GAIN, PART_ID, MAIN_STATUS, PS_DATA, LS_DATA_IR, 
          LS_DATA_GREEN, LS_DATA_BLUE, LS_DATA_RED, INT_CFG, INT_PST, PS_THRES_UP, PS_THRES_LOW, PS_CAN, LS_THRES_UP, LS_THRES_LOW, 
          LS_THRES_VAR, READ_WINDOW_FIRST, register };
    }
    
    Register(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\broadcom\BroadcomColorSensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.ftdi.eeprom;

import org.firstinspires.ftc.robotcore.internal.ftdi.FtDevice;
import org.firstinspires.ftc.robotcore.internal.ftdi.FtDeviceIOException;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbException;

public class FT_EE_232H_Ctrl extends FT_EE_Ctrl {
  private static final int AL_DRIVE_CURRENT = 3;
  
  private static final int AL_FAST_SLEW = 4;
  
  private static final int AL_SCHMITT_INPUT = 8;
  
  private static final int BL_DRIVE_CURRENT = 768;
  
  private static final int BL_FAST_SLEW = 1024;
  
  private static final int BL_SCHMITT_INPUT = 2048;
  
  private static final String DEFAULT_PID = "6014";
  
  private static final byte EEPROM_SIZE_LOCATION = 15;
  
  private static FtDevice ft_device;
  
  public FT_EE_232H_Ctrl(FtDevice paramFtDevice) throws FtDeviceIOException, RobotUsbException {
    super(paramFtDevice);
    getEepromSize((byte)15);
  }
  
  public int getUserSize() throws RobotUsbException {
    int j = readWord((short)9);
    int i = (j & 0xFF) / 2;
    j = ((j & 0xFF00) >> 8) / 2;
    return (this.mEepromSize - i + 1 - 1 - j + 1) * 2;
  }
  
  public short programEeprom(FT_EEPROM paramFT_EEPROM) {
    int[] arrayOfInt = new int[this.mEepromSize];
    if (paramFT_EEPROM.getClass() != FT_EEPROM_232H.class)
      return 1; 
    FT_EEPROM_232H fT_EEPROM_232H = (FT_EEPROM_232H)paramFT_EEPROM;
    try {
      if (fT_EEPROM_232H.FIFO) {
        arrayOfInt[0] = arrayOfInt[0] | 0x1;
      } else if (fT_EEPROM_232H.FIFOTarget) {
        arrayOfInt[0] = arrayOfInt[0] | 0x2;
      } else if (fT_EEPROM_232H.FastSerial) {
        arrayOfInt[0] = arrayOfInt[0] | 0x4;
      } 
      if (fT_EEPROM_232H.FT1248)
        arrayOfInt[0] = arrayOfInt[0] | 0x8; 
      if (fT_EEPROM_232H.LoadVCP)
        arrayOfInt[0] = arrayOfInt[0] | 0x10; 
      if (fT_EEPROM_232H.FT1248ClockPolarity)
        arrayOfInt[0] = arrayOfInt[0] | 0x100; 
      if (fT_EEPROM_232H.FT1248LSB)
        arrayOfInt[0] = arrayOfInt[0] | 0x200; 
      if (fT_EEPROM_232H.FT1248FlowControl)
        arrayOfInt[0] = arrayOfInt[0] | 0x400; 
      if (fT_EEPROM_232H.PowerSaveEnable)
        arrayOfInt[0] = arrayOfInt[0] | 0x8000; 
      arrayOfInt[1] = fT_EEPROM_232H.VendorId;
      arrayOfInt[2] = fT_EEPROM_232H.ProductId;
      arrayOfInt[3] = 2304;
      arrayOfInt[4] = setUSBConfig(paramFT_EEPROM);
      arrayOfInt[5] = setDeviceControl(paramFT_EEPROM);
      byte b1 = fT_EEPROM_232H.AL_DriveCurrent;
      int i = b1;
      if (b1 == -1)
        i = 0; 
      arrayOfInt[6] = i | arrayOfInt[6];
      if (fT_EEPROM_232H.AL_SlowSlew)
        arrayOfInt[6] = arrayOfInt[6] | 0x4; 
      if (fT_EEPROM_232H.AL_SchmittInput)
        arrayOfInt[6] = arrayOfInt[6] | 0x8; 
      b1 = fT_EEPROM_232H.BL_DriveCurrent;
      i = b1;
      if (b1 == -1)
        i = 0; 
      int j = arrayOfInt[6];
      arrayOfInt[6] = (short)(i << 8) | j;
      if (fT_EEPROM_232H.BL_SlowSlew)
        arrayOfInt[6] = arrayOfInt[6] | 0x400; 
      if (fT_EEPROM_232H.BL_SchmittInput)
        arrayOfInt[6] = arrayOfInt[6] | 0x800; 
      i = setStringDescriptor(fT_EEPROM_232H.Manufacturer, arrayOfInt, 80, 7, false);
      i = setStringDescriptor(fT_EEPROM_232H.Product, arrayOfInt, i, 8, false);
      if (fT_EEPROM_232H.SerNumEnable)
        setStringDescriptor(fT_EEPROM_232H.SerialNumber, arrayOfInt, i, 9, false); 
      arrayOfInt[10] = 0;
      arrayOfInt[11] = 0;
      arrayOfInt[12] = 0;
      arrayOfInt[12] = fT_EEPROM_232H.CBus0 | fT_EEPROM_232H.CBus1 << 4 | fT_EEPROM_232H.CBus2 << 8 | fT_EEPROM_232H.CBus3 << 12;
      arrayOfInt[13] = 0;
      i = fT_EEPROM_232H.CBus4;
      j = fT_EEPROM_232H.CBus5;
      byte b2 = fT_EEPROM_232H.CBus6;
      arrayOfInt[13] = fT_EEPROM_232H.CBus7 << 12 | i | j << 4 | b2 << 8;
      arrayOfInt[14] = 0;
      arrayOfInt[14] = fT_EEPROM_232H.CBus8 | fT_EEPROM_232H.CBus9 << 4;
      arrayOfInt[15] = this.mEepromType;
      arrayOfInt[69] = 72;
      if (this.mEepromType == 70)
        return 1; 
      if (arrayOfInt[1] != 0 && arrayOfInt[2] != 0) {
        boolean bool = programEeprom(arrayOfInt, this.mEepromSize - 1);
        return (short)(bool ^ true);
      } 
      return 2;
    } catch (Exception exception) {
      exception.printStackTrace();
      return 0;
    } 
  }
  
  public FT_EEPROM readEeprom() {
    FT_EEPROM_232H fT_EEPROM_232H = new FT_EEPROM_232H();
    int[] arrayOfInt = new int[this.mEepromSize];
    if (this.mEepromBlank)
      return fT_EEPROM_232H; 
    short s = 0;
    try {
      while (s < this.mEepromSize) {
        arrayOfInt[s] = readWord(s);
        s = (short)(s + 1);
      } 
      fT_EEPROM_232H.UART = false;
      int i = arrayOfInt[0] & 0xF;
      if (i != 0) {
        if (i != 1) {
          if (i != 2) {
            if (i != 4) {
              if (i != 8) {
                fT_EEPROM_232H.UART = true;
              } else {
                fT_EEPROM_232H.FT1248 = true;
              } 
            } else {
              fT_EEPROM_232H.FastSerial = true;
            } 
          } else {
            fT_EEPROM_232H.FIFOTarget = true;
          } 
        } else {
          fT_EEPROM_232H.FIFO = true;
        } 
      } else {
        fT_EEPROM_232H.UART = true;
      } 
      if ((arrayOfInt[0] & 0x10) > 0) {
        fT_EEPROM_232H.LoadVCP = true;
        fT_EEPROM_232H.LoadD2XX = false;
      } else {
        fT_EEPROM_232H.LoadVCP = false;
        fT_EEPROM_232H.LoadD2XX = true;
      } 
      if ((arrayOfInt[0] & 0x100) > 0) {
        fT_EEPROM_232H.FT1248ClockPolarity = true;
      } else {
        fT_EEPROM_232H.FT1248ClockPolarity = false;
      } 
      if ((arrayOfInt[0] & 0x200) > 0) {
        fT_EEPROM_232H.FT1248LSB = true;
      } else {
        fT_EEPROM_232H.FT1248LSB = false;
      } 
      if ((arrayOfInt[0] & 0x400) > 0) {
        fT_EEPROM_232H.FT1248FlowControl = true;
      } else {
        fT_EEPROM_232H.FT1248FlowControl = false;
      } 
      if ((arrayOfInt[0] & 0x8000) > 0)
        fT_EEPROM_232H.PowerSaveEnable = true; 
      fT_EEPROM_232H.VendorId = (short)arrayOfInt[1];
      fT_EEPROM_232H.ProductId = (short)arrayOfInt[2];
      getUSBConfig(fT_EEPROM_232H, arrayOfInt[4]);
      getDeviceControl(fT_EEPROM_232H, arrayOfInt[5]);
      i = arrayOfInt[6] & 0x3;
      if (i != 0) {
        if (i != 1) {
          if (i != 2) {
            if (i == 3)
              fT_EEPROM_232H.AL_DriveCurrent = 3; 
          } else {
            fT_EEPROM_232H.AL_DriveCurrent = 2;
          } 
        } else {
          fT_EEPROM_232H.AL_DriveCurrent = 1;
        } 
      } else {
        fT_EEPROM_232H.AL_DriveCurrent = 0;
      } 
      if ((arrayOfInt[6] & 0x4) > 0) {
        fT_EEPROM_232H.AL_SlowSlew = true;
      } else {
        fT_EEPROM_232H.AL_SlowSlew = false;
      } 
      if ((arrayOfInt[6] & 0x8) > 0) {
        fT_EEPROM_232H.AL_SchmittInput = true;
      } else {
        fT_EEPROM_232H.AL_SchmittInput = false;
      } 
    } catch (Exception exception) {
      return null;
    } 
    short s1 = (short)((arrayOfInt[6] & 0x300) >> 8);
    if (s1 != 0) {
      if (s1 != 1) {
        if (s1 != 2) {
          if (s1 == 3)
            ((FT_EEPROM_232H)exception).BL_DriveCurrent = 3; 
        } else {
          ((FT_EEPROM_232H)exception).BL_DriveCurrent = 2;
        } 
      } else {
        ((FT_EEPROM_232H)exception).BL_DriveCurrent = 1;
      } 
    } else {
      ((FT_EEPROM_232H)exception).BL_DriveCurrent = 0;
    } 
    if ((arrayOfInt[6] & 0x400) > 0) {
      ((FT_EEPROM_232H)exception).BL_SlowSlew = true;
    } else {
      ((FT_EEPROM_232H)exception).BL_SlowSlew = false;
    } 
    if ((arrayOfInt[6] & 0x800) > 0) {
      ((FT_EEPROM_232H)exception).BL_SchmittInput = true;
    } else {
      ((FT_EEPROM_232H)exception).BL_SchmittInput = false;
    } 
    ((FT_EEPROM_232H)exception).CBus0 = (byte)(short)(arrayOfInt[12] >> 0 & 0xF);
    ((FT_EEPROM_232H)exception).CBus1 = (byte)(short)(arrayOfInt[12] >> 4 & 0xF);
    ((FT_EEPROM_232H)exception).CBus2 = (byte)(short)(arrayOfInt[12] >> 8 & 0xF);
    ((FT_EEPROM_232H)exception).CBus3 = (byte)(short)(arrayOfInt[12] >> 12 & 0xF);
    ((FT_EEPROM_232H)exception).CBus4 = (byte)(short)(arrayOfInt[13] >> 0 & 0xF);
    ((FT_EEPROM_232H)exception).CBus5 = (byte)(short)(arrayOfInt[13] >> 4 & 0xF);
    ((FT_EEPROM_232H)exception).CBus6 = (byte)(short)(arrayOfInt[13] >> 8 & 0xF);
    ((FT_EEPROM_232H)exception).CBus7 = (byte)(short)(arrayOfInt[13] >> 12 & 0xF);
    ((FT_EEPROM_232H)exception).CBus8 = (byte)(short)(arrayOfInt[14] >> 0 & 0xF);
    ((FT_EEPROM_232H)exception).CBus9 = (byte)(short)(arrayOfInt[14] >> 4 & 0xF);
    ((FT_EEPROM_232H)exception).Manufacturer = getStringDescriptor((arrayOfInt[7] & 0xFF) / 2, arrayOfInt);
    ((FT_EEPROM_232H)exception).Product = getStringDescriptor((arrayOfInt[8] & 0xFF) / 2, arrayOfInt);
    ((FT_EEPROM_232H)exception).SerialNumber = getStringDescriptor((arrayOfInt[9] & 0xFF) / 2, arrayOfInt);
    return (FT_EEPROM)exception;
  }
  
  public byte[] readUserData(int paramInt) throws RobotUsbException {
    byte[] arrayOfByte = new byte[paramInt];
    if (paramInt != 0 && paramInt <= getUserSize()) {
      short s = (short)(this.mEepromSize - getUserSize() / 2 - 1 - 1);
      int i = 0;
      while (i < paramInt) {
        short s1 = (short)(s + 1);
        int j = readWord(s);
        int k = i + 1;
        if (k < paramInt)
          arrayOfByte[k] = (byte)(j & 0xFF); 
        arrayOfByte[i] = (byte)((j & 0xFF00) >> 8);
        i += 2;
        s = s1;
      } 
      return arrayOfByte;
    } 
    return null;
  }
  
  public int writeUserData(byte[] paramArrayOfbyte) throws RobotUsbException {
    if (paramArrayOfbyte.length > getUserSize())
      return 0; 
    int[] arrayOfInt = new int[this.mEepromSize];
    for (short s = 0; s < this.mEepromSize; s = (short)(s + 1))
      arrayOfInt[s] = readWord(s); 
    short s1 = (short)(this.mEepromSize - getUserSize() / 2 - 1 - 1);
    int i = 0;
    while (i < paramArrayOfbyte.length) {
      int j = i + 1;
      if (j < paramArrayOfbyte.length) {
        j = paramArrayOfbyte[j] & 0xFF;
      } else {
        j = 0;
      } 
      byte b = paramArrayOfbyte[i];
      short s2 = (short)(s1 + 1);
      arrayOfInt[s1] = j << 8 | b & 0xFF;
      i += 2;
      s1 = s2;
    } 
    return (arrayOfInt[1] != 0 && arrayOfInt[2] != 0) ? (!programEeprom(arrayOfInt, this.mEepromSize - 1) ? 0 : paramArrayOfbyte.length) : 0;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\ftdi\eeprom\FT_EE_232H_Ctrl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
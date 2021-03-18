package org.firstinspires.ftc.robotcore.internal.ftdi.eeprom;

import org.firstinspires.ftc.robotcore.internal.ftdi.FtDevice;
import org.firstinspires.ftc.robotcore.internal.ftdi.FtDeviceIOException;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbException;

public class FT_EE_2232H_Ctrl extends FT_EE_Ctrl {
  private static final int AH_DRIVE_CURRENT = 48;
  
  private static final int AH_FAST_SLEW = 64;
  
  private static final int AH_SCHMITT_INPUT = 128;
  
  private static final int AL_DRIVE_CURRENT = 3;
  
  private static final int AL_FAST_SLEW = 4;
  
  private static final int AL_SCHMITT_INPUT = 8;
  
  private static final int A_245_FIFO = 1;
  
  private static final int A_245_FIFO_TARGET = 2;
  
  private static final int A_FAST_SERIAL = 4;
  
  private static final int A_LOAD_VCP_DRIVER = 8;
  
  private static final int A_UART_RS232 = 0;
  
  private static final int BH_DRIVE_CURRENT = 12288;
  
  private static final int BH_FAST_SLEW = 16384;
  
  private static final int BH_SCHMITT_INPUT = 32768;
  
  private static final int BL_DRIVE_CURRENT = 768;
  
  private static final int BL_FAST_SLEW = 1024;
  
  private static final int BL_SCHMITT_INPUT = 2048;
  
  private static final String DEFAULT_PID = "6010";
  
  private static final byte EEPROM_SIZE_LOCATION = 12;
  
  private static final int INVERT_CTS = 2048;
  
  private static final int INVERT_DCD = 16384;
  
  private static final int INVERT_DSR = 8192;
  
  private static final int INVERT_DTR = 4096;
  
  private static final int INVERT_RI = 32768;
  
  private static final int INVERT_RTS = 1024;
  
  private static final int INVERT_RXD = 512;
  
  private static final int INVERT_TXD = 256;
  
  private static final int TPRDRV = 24;
  
  public FT_EE_2232H_Ctrl(FtDevice paramFtDevice) throws FtDeviceIOException, RobotUsbException {
    super(paramFtDevice);
    getEepromSize((byte)12);
  }
  
  public int getUserSize() throws RobotUsbException {
    int j = readWord((short)9);
    int i = (j & 0xFF) / 2;
    j = ((j & 0xFF00) >> 8) / 2;
    return (this.mEepromSize - 1 - 1 - i + j + 1) * 2;
  }
  
  public short programEeprom(FT_EEPROM paramFT_EEPROM) {
    int[] arrayOfInt = new int[this.mEepromSize];
    if (paramFT_EEPROM.getClass() != FT_EEPROM_2232H.class)
      return 1; 
    FT_EEPROM_2232H fT_EEPROM_2232H = (FT_EEPROM_2232H)paramFT_EEPROM;
    try {
      boolean bool;
      if (!fT_EEPROM_2232H.A_UART)
        if (fT_EEPROM_2232H.A_FIFO) {
          arrayOfInt[0] = arrayOfInt[0] | 0x1;
        } else if (fT_EEPROM_2232H.A_FIFOTarget) {
          arrayOfInt[0] = arrayOfInt[0] | 0x2;
        } else {
          arrayOfInt[0] = arrayOfInt[0] | 0x4;
        }  
      if (fT_EEPROM_2232H.A_LoadVCP)
        arrayOfInt[0] = arrayOfInt[0] | 0x8; 
      if (!fT_EEPROM_2232H.B_UART)
        if (fT_EEPROM_2232H.B_FIFO) {
          arrayOfInt[0] = arrayOfInt[0] | 0x100;
        } else if (fT_EEPROM_2232H.B_FIFOTarget) {
          arrayOfInt[0] = arrayOfInt[0] | 0x200;
        } else {
          arrayOfInt[0] = arrayOfInt[0] | 0x400;
        }  
      if (fT_EEPROM_2232H.B_LoadVCP)
        arrayOfInt[0] = arrayOfInt[0] | 0x800; 
      if (fT_EEPROM_2232H.PowerSaveEnable)
        arrayOfInt[0] = arrayOfInt[0] | 0x8000; 
      arrayOfInt[1] = fT_EEPROM_2232H.VendorId;
      arrayOfInt[2] = fT_EEPROM_2232H.ProductId;
      arrayOfInt[3] = 1792;
      arrayOfInt[4] = setUSBConfig(paramFT_EEPROM);
      arrayOfInt[5] = setDeviceControl(paramFT_EEPROM);
      arrayOfInt[6] = 0;
      byte b = fT_EEPROM_2232H.AL_DriveCurrent;
      int i = b;
      if (b == -1)
        i = 0; 
      arrayOfInt[6] = i | arrayOfInt[6];
      if (fT_EEPROM_2232H.AL_SlowSlew)
        arrayOfInt[6] = arrayOfInt[6] | 0x4; 
      if (fT_EEPROM_2232H.AL_SchmittInput)
        arrayOfInt[6] = arrayOfInt[6] | 0x8; 
      b = fT_EEPROM_2232H.AH_DriveCurrent;
      i = b;
      if (b == -1)
        i = 0; 
      arrayOfInt[6] = (short)(i << 4) | arrayOfInt[6];
      if (fT_EEPROM_2232H.AH_SlowSlew)
        arrayOfInt[6] = arrayOfInt[6] | 0x40; 
      if (fT_EEPROM_2232H.AH_SchmittInput)
        arrayOfInt[6] = arrayOfInt[6] | 0x80; 
      b = fT_EEPROM_2232H.BL_DriveCurrent;
      i = b;
      if (b == -1)
        i = 0; 
      arrayOfInt[6] = (short)(i << 8) | arrayOfInt[6];
      if (fT_EEPROM_2232H.BL_SlowSlew)
        arrayOfInt[6] = arrayOfInt[6] | 0x400; 
      if (fT_EEPROM_2232H.BL_SchmittInput)
        arrayOfInt[6] = arrayOfInt[6] | 0x800; 
      arrayOfInt[6] = (short)(fT_EEPROM_2232H.BH_DriveCurrent << 12) | arrayOfInt[6];
      if (fT_EEPROM_2232H.BH_SlowSlew)
        arrayOfInt[6] = arrayOfInt[6] | 0x4000; 
      if (fT_EEPROM_2232H.BH_SchmittInput)
        arrayOfInt[6] = arrayOfInt[6] | 0x8000; 
      if (this.mEepromType == 70) {
        i = 13;
        bool = true;
      } else {
        i = 77;
        bool = false;
      } 
      i = setStringDescriptor(fT_EEPROM_2232H.Manufacturer, arrayOfInt, i, 7, bool);
      i = setStringDescriptor(fT_EEPROM_2232H.Product, arrayOfInt, i, 8, bool);
      if (fT_EEPROM_2232H.SerNumEnable)
        setStringDescriptor(fT_EEPROM_2232H.SerialNumber, arrayOfInt, i, 9, bool); 
      i = fT_EEPROM_2232H.TPRDRV;
      if (i != 0) {
        if (i != 1) {
          if (i != 2) {
            if (i != 3) {
              arrayOfInt[11] = 0;
            } else {
              arrayOfInt[11] = 24;
            } 
          } else {
            arrayOfInt[11] = 16;
          } 
        } else {
          arrayOfInt[11] = 8;
        } 
      } else {
        arrayOfInt[11] = 0;
      } 
      arrayOfInt[12] = this.mEepromType;
      if (arrayOfInt[1] != 0 && arrayOfInt[2] != 0) {
        bool = programEeprom(arrayOfInt, this.mEepromSize - 1);
        return (short)(bool ^ true);
      } 
      return 2;
    } catch (Exception exception) {
      exception.printStackTrace();
      return 0;
    } 
  }
  
  public FT_EEPROM readEeprom() {
    FT_EEPROM_2232H fT_EEPROM_2232H = new FT_EEPROM_2232H();
    int[] arrayOfInt = new int[this.mEepromSize];
    if (this.mEepromBlank)
      return fT_EEPROM_2232H; 
    short s = 0;
    try {
      while (s < this.mEepromSize) {
        arrayOfInt[s] = readWord(s);
        s = (short)(s + 1);
      } 
      int k = arrayOfInt[0];
      short s3 = (short)(k & 0x7);
      if (s3 != 0) {
        if (s3 != 1) {
          if (s3 != 2) {
            if (s3 != 4) {
              fT_EEPROM_2232H.A_UART = true;
            } else {
              fT_EEPROM_2232H.A_FastSerial = true;
            } 
          } else {
            fT_EEPROM_2232H.A_FIFOTarget = true;
          } 
        } else {
          fT_EEPROM_2232H.A_FIFO = true;
        } 
      } else {
        fT_EEPROM_2232H.A_UART = true;
      } 
      if ((short)((k & 0x8) >> 3) == 1) {
        fT_EEPROM_2232H.A_LoadVCP = true;
        fT_EEPROM_2232H.A_LoadD2XX = false;
      } else {
        fT_EEPROM_2232H.A_LoadVCP = false;
        fT_EEPROM_2232H.A_LoadD2XX = true;
      } 
      s3 = (short)((k & 0x700) >> 8);
      if (s3 != 0) {
        if (s3 != 1) {
          if (s3 != 2) {
            if (s3 != 4) {
              fT_EEPROM_2232H.B_UART = true;
            } else {
              fT_EEPROM_2232H.B_FastSerial = true;
            } 
          } else {
            fT_EEPROM_2232H.B_FIFOTarget = true;
          } 
        } else {
          fT_EEPROM_2232H.B_FIFO = true;
        } 
      } else {
        fT_EEPROM_2232H.B_UART = true;
      } 
      if ((short)((k & 0x800) >> 11) == 1) {
        fT_EEPROM_2232H.B_LoadVCP = true;
        fT_EEPROM_2232H.B_LoadD2XX = false;
      } else {
        fT_EEPROM_2232H.B_LoadVCP = false;
        fT_EEPROM_2232H.B_LoadD2XX = true;
      } 
      if ((short)((k & 0x8000) >> 15) == 1) {
        fT_EEPROM_2232H.PowerSaveEnable = true;
      } else {
        fT_EEPROM_2232H.PowerSaveEnable = false;
      } 
      fT_EEPROM_2232H.VendorId = (short)arrayOfInt[1];
      fT_EEPROM_2232H.ProductId = (short)arrayOfInt[2];
      getUSBConfig(fT_EEPROM_2232H, arrayOfInt[4]);
      getDeviceControl(fT_EEPROM_2232H, arrayOfInt[5]);
      k = (short)(arrayOfInt[6] & 0x3);
      if (k != 0) {
        if (k != 1) {
          if (k != 2) {
            if (k == 3)
              fT_EEPROM_2232H.AL_DriveCurrent = 3; 
          } else {
            fT_EEPROM_2232H.AL_DriveCurrent = 2;
          } 
        } else {
          fT_EEPROM_2232H.AL_DriveCurrent = 1;
        } 
      } else {
        fT_EEPROM_2232H.AL_DriveCurrent = 0;
      } 
      if ((short)(arrayOfInt[6] & 0x4) == 4) {
        fT_EEPROM_2232H.AL_SlowSlew = true;
      } else {
        fT_EEPROM_2232H.AL_SlowSlew = false;
      } 
      if ((short)(arrayOfInt[6] & 0x8) == 8) {
        fT_EEPROM_2232H.AL_SchmittInput = true;
      } else {
        fT_EEPROM_2232H.AL_SchmittInput = false;
      } 
    } catch (Exception exception) {
      return null;
    } 
    short s1 = (short)((arrayOfInt[6] & 0x30) >> 4);
    if (s1 != 0) {
      if (s1 != 1) {
        if (s1 != 2) {
          if (s1 == 3)
            ((FT_EEPROM_2232H)exception).AH_DriveCurrent = 3; 
        } else {
          ((FT_EEPROM_2232H)exception).AH_DriveCurrent = 2;
        } 
      } else {
        ((FT_EEPROM_2232H)exception).AH_DriveCurrent = 1;
      } 
    } else {
      ((FT_EEPROM_2232H)exception).AH_DriveCurrent = 0;
    } 
    if ((short)(arrayOfInt[6] & 0x40) == 64) {
      ((FT_EEPROM_2232H)exception).AH_SlowSlew = true;
    } else {
      ((FT_EEPROM_2232H)exception).AH_SlowSlew = false;
    } 
    s1 = (short)(arrayOfInt[6] & 0x80);
    if (s1 == 128) {
      ((FT_EEPROM_2232H)exception).AH_SchmittInput = true;
    } else {
      ((FT_EEPROM_2232H)exception).AH_SchmittInput = false;
    } 
    short s2 = (short)((arrayOfInt[6] & 0x300) >> 8);
    if (s2 != 0) {
      if (s2 != 1) {
        if (s2 != 2) {
          if (s2 == 3)
            ((FT_EEPROM_2232H)exception).BL_DriveCurrent = 3; 
        } else {
          ((FT_EEPROM_2232H)exception).BL_DriveCurrent = 2;
        } 
      } else {
        ((FT_EEPROM_2232H)exception).BL_DriveCurrent = 1;
      } 
    } else {
      ((FT_EEPROM_2232H)exception).BL_DriveCurrent = 0;
    } 
    if ((short)(arrayOfInt[6] & 0x400) == 1024) {
      ((FT_EEPROM_2232H)exception).BL_SlowSlew = true;
    } else {
      ((FT_EEPROM_2232H)exception).BL_SlowSlew = false;
    } 
    int j = arrayOfInt[6];
    if (s1 == 2048) {
      ((FT_EEPROM_2232H)exception).BL_SchmittInput = true;
    } else {
      ((FT_EEPROM_2232H)exception).BL_SchmittInput = false;
    } 
    s1 = (short)((arrayOfInt[6] & 0x3000) >> 12);
    if (s1 != 0) {
      if (s1 != 1) {
        if (s1 != 2) {
          if (s1 == 3)
            ((FT_EEPROM_2232H)exception).BH_DriveCurrent = 3; 
        } else {
          ((FT_EEPROM_2232H)exception).BH_DriveCurrent = 2;
        } 
      } else {
        ((FT_EEPROM_2232H)exception).BH_DriveCurrent = 1;
      } 
    } else {
      ((FT_EEPROM_2232H)exception).BH_DriveCurrent = 0;
    } 
    if ((short)(arrayOfInt[6] & 0x4000) == 16384) {
      ((FT_EEPROM_2232H)exception).BH_SlowSlew = true;
    } else {
      ((FT_EEPROM_2232H)exception).BH_SlowSlew = false;
    } 
    if ((short)(arrayOfInt[6] & 0x8000) == 32768) {
      ((FT_EEPROM_2232H)exception).BH_SchmittInput = true;
    } else {
      ((FT_EEPROM_2232H)exception).BH_SchmittInput = false;
    } 
    s1 = (short)((arrayOfInt[11] & 0x18) >> 3);
    if (s1 < 4) {
      ((FT_EEPROM_2232H)exception).TPRDRV = s1;
    } else {
      ((FT_EEPROM_2232H)exception).TPRDRV = 0;
    } 
    int i = arrayOfInt[7] & 0xFF;
    if (this.mEepromType == 70) {
      ((FT_EEPROM_2232H)exception).Manufacturer = getStringDescriptor((i - 128) / 2, arrayOfInt);
      ((FT_EEPROM_2232H)exception).Product = getStringDescriptor(((arrayOfInt[8] & 0xFF) - 128) / 2, arrayOfInt);
      ((FT_EEPROM_2232H)exception).SerialNumber = getStringDescriptor(((arrayOfInt[9] & 0xFF) - 128) / 2, arrayOfInt);
      return (FT_EEPROM)exception;
    } 
    ((FT_EEPROM_2232H)exception).Manufacturer = getStringDescriptor(i / 2, arrayOfInt);
    ((FT_EEPROM_2232H)exception).Product = getStringDescriptor((arrayOfInt[8] & 0xFF) / 2, arrayOfInt);
    ((FT_EEPROM_2232H)exception).SerialNumber = getStringDescriptor((arrayOfInt[9] & 0xFF) / 2, arrayOfInt);
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


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\ftdi\eeprom\FT_EE_2232H_Ctrl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
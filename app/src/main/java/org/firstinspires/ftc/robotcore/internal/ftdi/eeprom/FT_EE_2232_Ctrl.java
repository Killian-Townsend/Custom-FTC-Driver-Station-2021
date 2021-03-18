package org.firstinspires.ftc.robotcore.internal.ftdi.eeprom;

import org.firstinspires.ftc.robotcore.internal.ftdi.FtDevice;
import org.firstinspires.ftc.robotcore.internal.ftdi.FtDeviceIOException;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbException;

public class FT_EE_2232_Ctrl extends FT_EE_Ctrl {
  private static final short CHECKSUM_LOCATION = 63;
  
  private static final String DEFAULT_PID = "6010";
  
  private static final byte EEPROM_SIZE_LOCATION = 10;
  
  public FT_EE_2232_Ctrl(FtDevice paramFtDevice) throws FtDeviceIOException, RobotUsbException {
    super(paramFtDevice);
    getEepromSize((byte)10);
  }
  
  public int getUserSize() throws RobotUsbException {
    int i = readWord((short)9);
    int j = ((i & 0xFF00) >> 8) / 2;
    return (this.mEepromSize - 1 - 1 - (i & 0xFF) + j) * 2;
  }
  
  public short programEeprom(FT_EEPROM paramFT_EEPROM) {
    int[] arrayOfInt = new int[this.mEepromSize];
    if (paramFT_EEPROM.getClass() != FT_EEPROM_2232D.class)
      return 1; 
    FT_EEPROM_2232D fT_EEPROM_2232D = (FT_EEPROM_2232D)paramFT_EEPROM;
    arrayOfInt[0] = 0;
    try {
      boolean bool;
      if (fT_EEPROM_2232D.A_FIFO) {
        arrayOfInt[0] = arrayOfInt[0] | 0x1;
      } else if (fT_EEPROM_2232D.A_FIFOTarget) {
        arrayOfInt[0] = arrayOfInt[0] | 0x2;
      } else {
        arrayOfInt[0] = arrayOfInt[0] | 0x4;
      } 
      if (fT_EEPROM_2232D.A_HighIO)
        arrayOfInt[0] = arrayOfInt[0] | 0x10; 
      if (fT_EEPROM_2232D.A_LoadVCP) {
        arrayOfInt[0] = arrayOfInt[0] | 0x8;
      } else if (fT_EEPROM_2232D.B_FIFO) {
        arrayOfInt[0] = arrayOfInt[0] | 0x100;
      } else if (fT_EEPROM_2232D.B_FIFOTarget) {
        arrayOfInt[0] = arrayOfInt[0] | 0x200;
      } else {
        arrayOfInt[0] = arrayOfInt[0] | 0x400;
      } 
      if (fT_EEPROM_2232D.B_HighIO)
        arrayOfInt[0] = arrayOfInt[0] | 0x1000; 
      if (fT_EEPROM_2232D.B_LoadVCP)
        arrayOfInt[0] = arrayOfInt[0] | 0x800; 
      arrayOfInt[1] = fT_EEPROM_2232D.VendorId;
      arrayOfInt[2] = fT_EEPROM_2232D.ProductId;
      arrayOfInt[3] = 1280;
      arrayOfInt[4] = setUSBConfig(paramFT_EEPROM);
      arrayOfInt[4] = setDeviceControl(paramFT_EEPROM);
      if (this.mEepromType == 70) {
        i = 11;
        bool = true;
      } else {
        i = 75;
        bool = false;
      } 
      int i = setStringDescriptor(fT_EEPROM_2232D.Manufacturer, arrayOfInt, i, 7, bool);
      i = setStringDescriptor(fT_EEPROM_2232D.Product, arrayOfInt, i, 8, bool);
      if (fT_EEPROM_2232D.SerNumEnable)
        setStringDescriptor(fT_EEPROM_2232D.SerialNumber, arrayOfInt, i, 9, bool); 
      arrayOfInt[10] = this.mEepromType;
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
    FT_EEPROM_2232D fT_EEPROM_2232D = new FT_EEPROM_2232D();
    int[] arrayOfInt = new int[this.mEepromSize];
    int i = 0;
    try {
      while (i < this.mEepromSize) {
        arrayOfInt[i] = readWord((short)i);
        i++;
      } 
    } catch (Exception exception) {
      return null;
    } 
    i = (short)(arrayOfInt[0] & 0x7);
    if (i != 0) {
      if (i != 1) {
        if (i != 2) {
          if (i == 4)
            ((FT_EEPROM_2232D)exception).A_FastSerial = true; 
        } else {
          ((FT_EEPROM_2232D)exception).A_FIFOTarget = true;
        } 
      } else {
        ((FT_EEPROM_2232D)exception).A_FIFO = true;
      } 
    } else {
      ((FT_EEPROM_2232D)exception).A_UART = true;
    } 
    if ((short)((arrayOfInt[0] & 0x8) >> 3) == 1) {
      ((FT_EEPROM_2232D)exception).A_LoadVCP = true;
    } else {
      ((FT_EEPROM_2232D)exception).A_HighIO = true;
    } 
    if ((short)((arrayOfInt[0] & 0x10) >> 4) == 1)
      ((FT_EEPROM_2232D)exception).A_HighIO = true; 
    i = (short)((arrayOfInt[0] & 0x700) >> 8);
    if (i != 0) {
      if (i != 1) {
        if (i != 2) {
          if (i == 4)
            ((FT_EEPROM_2232D)exception).B_FastSerial = true; 
        } else {
          ((FT_EEPROM_2232D)exception).B_FIFOTarget = true;
        } 
      } else {
        ((FT_EEPROM_2232D)exception).B_FIFO = true;
      } 
    } else {
      ((FT_EEPROM_2232D)exception).B_UART = true;
    } 
    if ((short)((arrayOfInt[0] & 0x800) >> 11) == 1) {
      ((FT_EEPROM_2232D)exception).B_LoadVCP = true;
    } else {
      ((FT_EEPROM_2232D)exception).B_LoadD2XX = true;
    } 
    if ((short)((arrayOfInt[0] & 0x1000) >> 12) == 1)
      ((FT_EEPROM_2232D)exception).B_HighIO = true; 
    ((FT_EEPROM_2232D)exception).VendorId = (short)arrayOfInt[1];
    ((FT_EEPROM_2232D)exception).ProductId = (short)arrayOfInt[2];
    getUSBConfig((FT_EEPROM)exception, arrayOfInt[4]);
    i = arrayOfInt[7] & 0xFF;
    if (this.mEepromType == 70) {
      ((FT_EEPROM_2232D)exception).Manufacturer = getStringDescriptor((i - 128) / 2, arrayOfInt);
      ((FT_EEPROM_2232D)exception).Product = getStringDescriptor(((arrayOfInt[8] & 0xFF) - 128) / 2, arrayOfInt);
      ((FT_EEPROM_2232D)exception).SerialNumber = getStringDescriptor(((arrayOfInt[9] & 0xFF) - 128) / 2, arrayOfInt);
      return (FT_EEPROM)exception;
    } 
    ((FT_EEPROM_2232D)exception).Manufacturer = getStringDescriptor(i / 2, arrayOfInt);
    ((FT_EEPROM_2232D)exception).Product = getStringDescriptor((arrayOfInt[8] & 0xFF) / 2, arrayOfInt);
    ((FT_EEPROM_2232D)exception).SerialNumber = getStringDescriptor((arrayOfInt[9] & 0xFF) / 2, arrayOfInt);
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


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\ftdi\eeprom\FT_EE_2232_Ctrl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
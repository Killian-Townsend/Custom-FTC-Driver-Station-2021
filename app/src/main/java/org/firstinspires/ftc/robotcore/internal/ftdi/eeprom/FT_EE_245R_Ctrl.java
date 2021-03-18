package org.firstinspires.ftc.robotcore.internal.ftdi.eeprom;

import org.firstinspires.ftc.robotcore.internal.ftdi.FtDevice;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbException;

public class FT_EE_245R_Ctrl extends FT_EE_Ctrl {
  private static final short EEPROM_SIZE = 80;
  
  private static final short EE_MAX_SIZE = 1024;
  
  private static final short ENDOFUSERLOCATION = 63;
  
  private static final int EXTERNAL_OSCILLATOR = 2;
  
  private static final int HIGH_CURRENT_IO = 4;
  
  private static final int INVERT_CTS = 2048;
  
  private static final int INVERT_DCD = 16384;
  
  private static final int INVERT_DSR = 8192;
  
  private static final int INVERT_DTR = 4096;
  
  private static final int INVERT_RI = 32768;
  
  private static final int INVERT_RTS = 1024;
  
  private static final int INVERT_RXD = 512;
  
  private static final int INVERT_TXD = 256;
  
  private static final int LOAD_D2XX_DRIVER = 8;
  
  private FtDevice ftDevice;
  
  public FT_EE_245R_Ctrl(FtDevice paramFtDevice) {
    super(paramFtDevice);
    this.ftDevice = paramFtDevice;
  }
  
  public int getUserSize() throws RobotUsbException {
    return (63 - ((readWord((short)7) & 0xFF00) >> 8) / 2 + 12 + ((readWord((short)8) & 0xFF00) >> 8) / 2 + 1 - ((0xFF00 & readWord((short)9)) >> 8) / 2 - 1) * 2;
  }
  
  public short programEeprom(FT_EEPROM paramFT_EEPROM) {
    int[] arrayOfInt = new int[80];
    if (paramFT_EEPROM.getClass() != FT_EEPROM_245R.class)
      return 1; 
    FT_EEPROM_245R fT_EEPROM_245R = (FT_EEPROM_245R)paramFT_EEPROM;
    short s = 0;
    while (true) {
      if (s < 80) {
        try {
          arrayOfInt[s] = readWord(s);
          s = (short)(s + 1);
        } catch (Exception exception) {
          exception.printStackTrace();
          return 0;
        } 
        continue;
      } 
      int j = arrayOfInt[0] & 0xFF00 | 0x0;
      int i = j;
      if (fT_EEPROM_245R.HighIO)
        i = j | 0x4; 
      j = i;
      if (fT_EEPROM_245R.LoadVCP)
        j = i | 0x8; 
      if (fT_EEPROM_245R.ExternalOscillator) {
        i = j | 0x2;
      } else {
        i = j & 0xFFFD;
      } 
      arrayOfInt[0] = i;
      arrayOfInt[1] = fT_EEPROM_245R.VendorId;
      arrayOfInt[2] = fT_EEPROM_245R.ProductId;
      arrayOfInt[3] = 1536;
      arrayOfInt[4] = setUSBConfig(exception);
      j = setDeviceControl(exception);
      i = j;
      if (fT_EEPROM_245R.InvertTXD)
        i = j | 0x100; 
      j = i;
      if (fT_EEPROM_245R.InvertRXD)
        j = i | 0x200; 
      i = j;
      if (fT_EEPROM_245R.InvertRTS)
        i = j | 0x400; 
      j = i;
      if (fT_EEPROM_245R.InvertCTS)
        j = i | 0x800; 
      i = j;
      if (fT_EEPROM_245R.InvertDTR)
        i = j | 0x1000; 
      j = i;
      if (fT_EEPROM_245R.InvertDSR)
        j = i | 0x2000; 
      i = j;
      if (fT_EEPROM_245R.InvertDCD)
        i = j | 0x4000; 
      j = i;
      if (fT_EEPROM_245R.InvertRI)
        j = i | 0x8000; 
      arrayOfInt[5] = j;
      arrayOfInt[10] = fT_EEPROM_245R.CBus0 | fT_EEPROM_245R.CBus1 << 4 | fT_EEPROM_245R.CBus2 << 8 | fT_EEPROM_245R.CBus3 << 12;
      arrayOfInt[11] = fT_EEPROM_245R.CBus4;
      i = setStringDescriptor(fT_EEPROM_245R.Manufacturer, arrayOfInt, 12, 7, true);
      i = setStringDescriptor(fT_EEPROM_245R.Product, arrayOfInt, i, 8, true);
      if (fT_EEPROM_245R.SerNumEnable)
        setStringDescriptor(fT_EEPROM_245R.SerialNumber, arrayOfInt, i, 9, true); 
      if (arrayOfInt[1] != 0 && arrayOfInt[2] != 0) {
        byte b = this.ftDevice.getLatencyTimer();
        this.ftDevice.setLatencyTimer((byte)119);
        try {
          boolean bool = programEeprom(arrayOfInt, 80);
          return (short)(bool ^ true);
        } finally {
          this.ftDevice.setLatencyTimer(b);
        } 
      } 
      return 2;
    } 
  }
  
  public FT_EEPROM readEeprom() {
    FT_EEPROM_245R fT_EEPROM_245R = new FT_EEPROM_245R();
    int[] arrayOfInt = new int[80];
    int i = 0;
    while (true) {
      if (i < 80) {
        short s = (short)i;
        try {
          arrayOfInt[i] = readWord(s);
          i++;
        } catch (Exception exception) {
          return null;
        } 
        continue;
      } 
      if ((arrayOfInt[0] & 0x4) == 4) {
        ((FT_EEPROM_245R)exception).HighIO = true;
      } else {
        ((FT_EEPROM_245R)exception).HighIO = false;
      } 
      if ((arrayOfInt[0] & 0x8) == 8) {
        ((FT_EEPROM_245R)exception).LoadVCP = true;
      } else {
        ((FT_EEPROM_245R)exception).LoadVCP = false;
      } 
      if ((arrayOfInt[0] & 0x2) == 2) {
        ((FT_EEPROM_245R)exception).ExternalOscillator = true;
      } else {
        ((FT_EEPROM_245R)exception).ExternalOscillator = false;
      } 
      ((FT_EEPROM_245R)exception).VendorId = (short)arrayOfInt[1];
      ((FT_EEPROM_245R)exception).ProductId = (short)arrayOfInt[2];
      getUSBConfig((FT_EEPROM)exception, arrayOfInt[4]);
      getDeviceControl(exception, arrayOfInt[5]);
      if ((arrayOfInt[5] & 0x100) == 256) {
        ((FT_EEPROM_245R)exception).InvertTXD = true;
      } else {
        ((FT_EEPROM_245R)exception).InvertTXD = false;
      } 
      if ((arrayOfInt[5] & 0x200) == 512) {
        ((FT_EEPROM_245R)exception).InvertRXD = true;
      } else {
        ((FT_EEPROM_245R)exception).InvertRXD = false;
      } 
      if ((arrayOfInt[5] & 0x400) == 1024) {
        ((FT_EEPROM_245R)exception).InvertRTS = true;
      } else {
        ((FT_EEPROM_245R)exception).InvertRTS = false;
      } 
      if ((arrayOfInt[5] & 0x800) == 2048) {
        ((FT_EEPROM_245R)exception).InvertCTS = true;
      } else {
        ((FT_EEPROM_245R)exception).InvertCTS = false;
      } 
      if ((arrayOfInt[5] & 0x1000) == 4096) {
        ((FT_EEPROM_245R)exception).InvertDTR = true;
      } else {
        ((FT_EEPROM_245R)exception).InvertDTR = false;
      } 
      if ((arrayOfInt[5] & 0x2000) == 8192) {
        ((FT_EEPROM_245R)exception).InvertDSR = true;
      } else {
        ((FT_EEPROM_245R)exception).InvertDSR = false;
      } 
      if ((arrayOfInt[5] & 0x4000) == 16384) {
        ((FT_EEPROM_245R)exception).InvertDCD = true;
      } else {
        ((FT_EEPROM_245R)exception).InvertDCD = false;
      } 
      if ((arrayOfInt[5] & 0x8000) == 32768) {
        ((FT_EEPROM_245R)exception).InvertRI = true;
      } else {
        ((FT_EEPROM_245R)exception).InvertRI = false;
      } 
      i = arrayOfInt[10];
      ((FT_EEPROM_245R)exception).CBus0 = (byte)(i & 0xF);
      ((FT_EEPROM_245R)exception).CBus1 = (byte)((i & 0xF0) >> 4);
      ((FT_EEPROM_245R)exception).CBus2 = (byte)((i & 0xF00) >> 8);
      ((FT_EEPROM_245R)exception).CBus3 = (byte)((i & 0xF000) >> 12);
      ((FT_EEPROM_245R)exception).CBus4 = (byte)(arrayOfInt[11] & 0xFF);
      ((FT_EEPROM_245R)exception).Manufacturer = getStringDescriptor(((arrayOfInt[7] & 0xFF) - 128) / 2, arrayOfInt);
      ((FT_EEPROM_245R)exception).Product = getStringDescriptor(((arrayOfInt[8] & 0xFF) - 128) / 2, arrayOfInt);
      ((FT_EEPROM_245R)exception).SerialNumber = getStringDescriptor(((arrayOfInt[9] & 0xFF) - 128) / 2, arrayOfInt);
      return (FT_EEPROM)exception;
    } 
  }
  
  public byte[] readUserData(int paramInt) throws RobotUsbException {
    byte[] arrayOfByte = new byte[paramInt];
    if (paramInt != 0 && paramInt <= getUserSize()) {
      short s = (short)(63 - getUserSize() / 2 - 1);
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
    int[] arrayOfInt = new int[80];
    for (short s = 0; s < 80; s = (short)(s + 1))
      arrayOfInt[s] = readWord(s); 
    short s1 = (short)((short)(63 - getUserSize() / 2 - 1) & 0xFFFF);
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
    if (arrayOfInt[1] != 0 && arrayOfInt[2] != 0) {
      byte b = this.ftDevice.getLatencyTimer();
      this.ftDevice.setLatencyTimer((byte)119);
      try {
        boolean bool = programEeprom(arrayOfInt, 63);
        return !bool ? 0 : paramArrayOfbyte.length;
      } finally {
        this.ftDevice.setLatencyTimer(b);
      } 
    } 
    return 0;
  }
  
  public void writeWord(short paramShort1, short paramShort2) throws RobotUsbException {
    if (paramShort1 < 1024) {
      byte b = this.ftDevice.getLatencyTimer();
      this.ftDevice.setLatencyTimer((byte)119);
      try {
        FtDevice.throwIfStatus(this.ftDevice.getConnection().controlTransfer(64, 145, paramShort2, paramShort1, (byte[])null, 0, 0), "writeWord");
        return;
      } finally {
        this.ftDevice.setLatencyTimer(b);
      } 
    } 
    throw new IllegalArgumentException(String.format("offset >= 1024: %d", new Object[] { Short.valueOf(paramShort1) }));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\ftdi\eeprom\FT_EE_245R_Ctrl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
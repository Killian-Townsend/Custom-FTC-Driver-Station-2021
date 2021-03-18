package org.firstinspires.ftc.robotcore.internal.ftdi.eeprom;

import android.util.Log;
import org.firstinspires.ftc.robotcore.internal.ftdi.FtDevice;
import org.firstinspires.ftc.robotcore.internal.ftdi.FtDeviceIOException;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbException;

public class FT_EE_Ctrl {
  private static final int BUS_POWERED = 128;
  
  private static final short EE_MAX_SIZE = 1024;
  
  private static final int ENABLE_SERIAL_NUMBER = 8;
  
  private static final int PULL_DOWN_IN_USB_SUSPEND = 4;
  
  private static final int SELF_POWERED = 64;
  
  private static final int USB_REMOTE_WAKEUP = 32;
  
  private FtDevice mDevice;
  
  boolean mEepromBlank;
  
  int mEepromSize;
  
  short mEepromType;
  
  public FT_EE_Ctrl(FtDevice paramFtDevice) {
    this.mDevice = paramFtDevice;
  }
  
  void clearUserDataArea(int paramInt1, int paramInt2, int[] paramArrayOfint) {
    while (paramInt1 < paramInt2) {
      paramArrayOfint[paramInt1] = 0;
      paramInt1++;
    } 
  }
  
  public int eraseEeprom() throws RobotUsbException {
    return this.mDevice.getConnection().controlTransfer(64, 146, 0, 0, (byte[])null, 0, 0);
  }
  
  void getDeviceControl(Object paramObject, int paramInt) {
    paramObject = paramObject;
    if ((paramInt & 0x4) > 0) {
      ((FT_EEPROM)paramObject).PullDownEnable = true;
    } else {
      ((FT_EEPROM)paramObject).PullDownEnable = false;
    } 
    if ((paramInt & 0x8) > 0) {
      ((FT_EEPROM)paramObject).SerNumEnable = true;
      return;
    } 
    ((FT_EEPROM)paramObject).SerNumEnable = false;
  }
  
  int getEepromSize(byte paramByte) throws FtDeviceIOException, RobotUsbException {
    int i = readWord((short)(paramByte & 0xFFFFFFFF));
    if (i != 65535) {
      if (i != 70) {
        if (i != 82) {
          if (i != 86) {
            if (i != 102)
              return 0; 
            this.mEepromType = 102;
            this.mEepromSize = 128;
            this.mEepromBlank = false;
            return 256;
          } 
          this.mEepromType = 86;
          this.mEepromSize = 128;
          this.mEepromBlank = false;
          return 128;
        } 
        this.mEepromType = 82;
        this.mEepromSize = 1024;
        this.mEepromBlank = false;
        return 1024;
      } 
      this.mEepromType = 70;
      this.mEepromSize = 64;
      this.mEepromBlank = false;
      return 64;
    } 
    writeWord((short)192, (short)192);
    readWord((short)192);
    readWord((short)64);
    readWord((short)0);
    this.mEepromBlank = true;
    if ((readWord((short)0) & 0xFF) == 192) {
      eraseEeprom();
      this.mEepromType = 70;
      this.mEepromSize = 64;
      return 64;
    } 
    if ((readWord((short)64) & 0xFF) == 192) {
      eraseEeprom();
      this.mEepromType = 86;
      this.mEepromSize = 128;
      return 128;
    } 
    if ((readWord((short)192) & 0xFF) == 192) {
      eraseEeprom();
      this.mEepromType = 102;
      this.mEepromSize = 128;
      return 256;
    } 
    eraseEeprom();
    return 0;
  }
  
  String getStringDescriptor(int paramInt, int[] paramArrayOfint) {
    int j = (paramArrayOfint[paramInt] & 0xFF) / 2;
    int i = paramInt + 1;
    String str = "";
    for (paramInt = i; paramInt < j - 1 + i; paramInt++) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str);
      stringBuilder.append((char)paramArrayOfint[paramInt]);
      str = stringBuilder.toString();
    } 
    return str;
  }
  
  void getUSBConfig(FT_EEPROM paramFT_EEPROM, int paramInt) {
    paramFT_EEPROM.MaxPower = (short)((byte)(paramInt >> 8) * 2);
    paramInt = (byte)paramInt;
    if ((paramInt & 0x40) == 64 && (paramInt & 0x80) == 128) {
      paramFT_EEPROM.SelfPowered = true;
    } else {
      paramFT_EEPROM.SelfPowered = false;
    } 
    if ((paramInt & 0x20) == 32) {
      paramFT_EEPROM.RemoteWakeup = true;
      return;
    } 
    paramFT_EEPROM.RemoteWakeup = false;
  }
  
  public int getUserSize() throws RobotUsbException {
    return 0;
  }
  
  public short programEeprom(FT_EEPROM paramFT_EEPROM) throws RobotUsbException {
    return 1;
  }
  
  boolean programEeprom(int[] paramArrayOfint, int paramInt) throws RobotUsbException {
    int j = 43690;
    int i = 0;
    while (i < paramInt) {
      writeWord((short)i, (short)paramArrayOfint[i]);
      j = (j ^ paramArrayOfint[i]) & 0xFFFF;
      short s = (short)(j << 1 & 0xFFFF);
      j = ((short)(j >> 15 & 0xFFFF) | s) & 0xFFFF;
      i++;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Entered WriteWord Checksum : ");
      stringBuilder.append(j);
      Log.d("FT_EE_Ctrl", stringBuilder.toString());
    } 
    writeWord((short)paramInt, (short)j);
    return true;
  }
  
  public FT_EEPROM readEeprom() {
    return null;
  }
  
  public byte[] readUserData(int paramInt) throws RobotUsbException {
    return null;
  }
  
  public int readWord(short paramShort) throws RobotUsbException {
    byte[] arrayOfByte = new byte[2];
    if (paramShort >= 1024)
      return -1; 
    this.mDevice.getConnection().controlTransfer(-64, 144, 0, paramShort, arrayOfByte, 2, 0);
    return (arrayOfByte[1] & 0xFF) << 8 | arrayOfByte[0] & 0xFF;
  }
  
  int setDeviceControl(Object paramObject) {
    boolean bool;
    paramObject = paramObject;
    if (((FT_EEPROM)paramObject).PullDownEnable) {
      bool = true;
    } else {
      bool = false;
    } 
    return ((FT_EEPROM)paramObject).SerNumEnable ? (bool | 0x8) : (bool & 0xF7);
  }
  
  int setStringDescriptor(String paramString, int[] paramArrayOfint, int paramInt1, int paramInt2, boolean paramBoolean) {
    int i = paramString.length() * 2 + 2;
    paramArrayOfint[paramInt2] = i << 8 | paramInt1 * 2;
    if (paramBoolean)
      paramArrayOfint[paramInt2] = paramArrayOfint[paramInt2] + 128; 
    char[] arrayOfChar = paramString.toCharArray();
    paramInt2 = paramInt1 + 1;
    paramArrayOfint[paramInt1] = i | 0x300;
    int j = (i - 2) / 2;
    paramInt1 = 0;
    while (true) {
      i = paramInt2 + 1;
      paramArrayOfint[paramInt2] = arrayOfChar[paramInt1];
      if (++paramInt1 >= j)
        return i; 
      paramInt2 = i;
    } 
  }
  
  int setUSBConfig(Object paramObject) {
    char c;
    paramObject = paramObject;
    if (((FT_EEPROM)paramObject).RemoteWakeup) {
      c = ' ';
    } else {
      c = '';
    } 
    int i = c;
    if (((FT_EEPROM)paramObject).SelfPowered)
      i = c | 0x40; 
    return ((FT_EEPROM)paramObject).MaxPower / 2 << 8 | i;
  }
  
  public int writeUserData(byte[] paramArrayOfbyte) throws RobotUsbException {
    return 0;
  }
  
  public void writeWord(short paramShort1, short paramShort2) throws RobotUsbException {
    if (paramShort1 < 1024) {
      FtDevice.throwIfStatus(this.mDevice.getConnection().controlTransfer(64, 145, paramShort2, paramShort1, (byte[])null, 0, 0), "writeWord");
      return;
    } 
    throw new IllegalArgumentException(String.format("offset >= 1024: %d", new Object[] { Short.valueOf(paramShort1) }));
  }
  
  static final class EepromType {
    static final short INVALID = 255;
    
    static final short TYPE_46 = 70;
    
    static final short TYPE_52 = 82;
    
    static final short TYPE_56 = 86;
    
    static final short TYPE_66 = 102;
    
    static final short TYPE_MTP = 1;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\ftdi\eeprom\FT_EE_Ctrl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
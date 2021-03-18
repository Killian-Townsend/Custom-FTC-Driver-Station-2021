package org.firstinspires.ftc.robotcore.internal.ftdi.eeprom;

import org.firstinspires.ftc.robotcore.internal.ftdi.FtDevice;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbException;

public class FT_EE_232B_Ctrl extends FT_EE_Ctrl {
  private static final short CHECKSUM_LOCATION = 63;
  
  private static final short EEPROM_SIZE = 64;
  
  private static FtDevice ft_device;
  
  public FT_EE_232B_Ctrl(FtDevice paramFtDevice) {
    super(paramFtDevice);
    ft_device = paramFtDevice;
  }
  
  public int getUserSize() throws RobotUsbException {
    return (63 - ((readWord((short)7) & 0xFF00) >> 8) / 2 + 10 + ((readWord((short)8) & 0xFF00) >> 8) / 2 + 1 - 1 - ((0xFF00 & readWord((short)9)) >> 8) / 2) * 2;
  }
  
  public short programEeprom(FT_EEPROM paramFT_EEPROM) {
    int[] arrayOfInt = new int[64];
    if (paramFT_EEPROM.getClass() != FT_EEPROM.class)
      return 1; 
    short s = 0;
    while (true) {
      if (s < 64) {
        try {
          arrayOfInt[s] = readWord(s);
          s = (short)(s + 1);
        } catch (Exception exception) {
          exception.printStackTrace();
          return 0;
        } 
        continue;
      } 
      arrayOfInt[1] = ((FT_EEPROM)exception).VendorId;
      arrayOfInt[2] = ((FT_EEPROM)exception).ProductId;
      arrayOfInt[3] = (ft_device.getDeviceInfo()).bcdDevice;
      arrayOfInt[4] = setUSBConfig(exception);
      int i = setStringDescriptor(((FT_EEPROM)exception).Manufacturer, arrayOfInt, 10, 7, true);
      i = setStringDescriptor(((FT_EEPROM)exception).Product, arrayOfInt, i, 8, true);
      if (((FT_EEPROM)exception).SerNumEnable)
        setStringDescriptor(((FT_EEPROM)exception).SerialNumber, arrayOfInt, i, 9, true); 
      if (arrayOfInt[1] != 0 && arrayOfInt[2] != 0) {
        boolean bool = programEeprom(arrayOfInt, 63);
        return (short)(bool ^ true);
      } 
      return 2;
    } 
  }
  
  public FT_EEPROM readEeprom() {
    FT_EEPROM fT_EEPROM = new FT_EEPROM();
    int[] arrayOfInt = new int[64];
    int i = 0;
    while (true) {
      if (i < 64) {
        short s = (short)i;
        try {
          arrayOfInt[i] = readWord(s);
          i++;
        } catch (Exception exception) {
          return null;
        } 
        continue;
      } 
      ((FT_EEPROM)exception).VendorId = (short)arrayOfInt[1];
      ((FT_EEPROM)exception).ProductId = (short)arrayOfInt[2];
      getUSBConfig((FT_EEPROM)exception, arrayOfInt[4]);
      ((FT_EEPROM)exception).Manufacturer = getStringDescriptor(10, arrayOfInt);
      i = ((FT_EEPROM)exception).Manufacturer.length() + 10 + 1;
      ((FT_EEPROM)exception).Product = getStringDescriptor(i, arrayOfInt);
      ((FT_EEPROM)exception).SerialNumber = getStringDescriptor(i + ((FT_EEPROM)exception).Product.length() + 1, arrayOfInt);
      return (FT_EEPROM)exception;
    } 
  }
  
  public byte[] readUserData(int paramInt) throws RobotUsbException {
    byte[] arrayOfByte = new byte[paramInt];
    if (paramInt != 0 && paramInt <= getUserSize()) {
      short s = (short)((short)(63 - getUserSize() / 2 - 1) & 0xFFFF);
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
    int[] arrayOfInt = new int[64];
    for (short s = 0; s < 64; s = (short)(s + 1))
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
    return (arrayOfInt[1] != 0 && arrayOfInt[2] != 0) ? (!programEeprom(arrayOfInt, 63) ? 0 : paramArrayOfbyte.length) : 0;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\ftdi\eeprom\FT_EE_232B_Ctrl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
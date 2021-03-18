package org.firstinspires.ftc.robotcore.internal.usb;

import android.text.TextUtils;
import com.qualcomm.robotcore.util.SerialNumber;

public class UsbSerialNumber extends SerialNumber {
  public UsbSerialNumber(String paramString) {
    super(paramString);
  }
  
  public static boolean isValidUsbSerialNumber(String paramString) {
    if (!TextUtils.isEmpty(paramString)) {
      byte b = -1;
      if (paramString.hashCode() == 730933386 && paramString.equals("200901010001"))
        b = 0; 
      if (b != 0)
        return true; 
    } 
    return false;
  }
  
  public boolean isUsb() {
    return true;
  }
  
  public String toString() {
    return this.serialNumberString;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\interna\\usb\UsbSerialNumber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
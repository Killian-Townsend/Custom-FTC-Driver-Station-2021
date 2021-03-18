package org.firstinspires.ftc.robotcore.internal.usb;

import com.qualcomm.robotcore.util.SerialNumber;
import java.util.regex.Pattern;
import org.firstinspires.ftc.robotcore.internal.system.Misc;

public class LynxModuleSerialNumber extends SerialNumber {
  public static final String TAG = "LynxModuleSerialNumber";
  
  protected static final String regexSeparator = Pattern.quote("|");
  
  protected static final String separator = "|";
  
  protected final int moduleAddress;
  
  protected final SerialNumber parentSerialNumber;
  
  public LynxModuleSerialNumber(SerialNumber paramSerialNumber, int paramInt) {
    this(Misc.formatInvariant("%sparent=%s|mod#=%d", new Object[] { "ExpHub:", Misc.encodeEntity(paramSerialNumber.getString(), "|"), Integer.valueOf(paramInt) }));
  }
  
  public LynxModuleSerialNumber(String paramString) {
    super(paramString.trim());
    String[] arrayOfString = this.serialNumberString.split(regexSeparator);
    this.parentSerialNumber = SerialNumber.fromString(Misc.decodeEntity(arrayOfString[0].split("=")[1]));
    this.moduleAddress = Integer.decode(arrayOfString[1].split("=")[1]).intValue();
  }
  
  public SerialNumber getScannableDeviceSerialNumber() {
    return this.parentSerialNumber.getScannableDeviceSerialNumber();
  }
  
  public String toString() {
    return Misc.formatForUser("%s(%s,mod#=%d)", new Object[] { "LynxModuleSerialNumber", this.parentSerialNumber, Integer.valueOf(this.moduleAddress) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\interna\\usb\LynxModuleSerialNumber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
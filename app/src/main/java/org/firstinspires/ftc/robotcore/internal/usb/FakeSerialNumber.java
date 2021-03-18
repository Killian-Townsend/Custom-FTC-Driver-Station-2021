package org.firstinspires.ftc.robotcore.internal.usb;

import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.util.SerialNumber;
import org.firstinspires.ftc.robotcore.internal.system.Misc;

public class FakeSerialNumber extends SerialNumber {
  public FakeSerialNumber() {
    this(stringBuilder.toString());
  }
  
  public FakeSerialNumber(String paramString) {
    super(paramString.trim());
  }
  
  public static boolean isLegacyFake(String paramString) {
    return (paramString == null || paramString.equals("-1") || paramString.equalsIgnoreCase("N/A") || paramString.trim().isEmpty());
  }
  
  public boolean isFake() {
    return true;
  }
  
  public String toString() {
    return Misc.formatForUser(R.string.noSerialNumber);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\interna\\usb\FakeSerialNumber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
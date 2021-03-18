package org.firstinspires.ftc.robotcore.internal.usb;

import com.qualcomm.robotcore.util.SerialNumber;

public class EmbeddedSerialNumber extends SerialNumber {
  public EmbeddedSerialNumber() {
    super("(embedded)");
  }
  
  public boolean isEmbedded() {
    return true;
  }
  
  public String toString() {
    return this.serialNumberString;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\interna\\usb\EmbeddedSerialNumber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.usb;

import android.text.TextUtils;
import com.qualcomm.robotcore.util.SerialNumber;
import org.firstinspires.ftc.robotcore.internal.system.Misc;

public class VendorProductSerialNumber extends SerialNumber {
  protected final String connectionPath;
  
  protected final int productId;
  
  protected final int vendorId;
  
  public VendorProductSerialNumber(int paramInt1, int paramInt2, String paramString) {
    this(Misc.formatInvariant("%svendor=0x%04x|product=0x%04x|connection=%s", new Object[] { "VendorProduct:", Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), str }));
  }
  
  public VendorProductSerialNumber(String paramString) {
    super(paramString.trim());
    String str;
    String[] arrayOfString = this.serialNumberString.split("\\|");
    this.vendorId = Integer.decode(arrayOfString[0].split("=")[1]).intValue();
    this.productId = Integer.decode(arrayOfString[1].split("=")[1]).intValue();
    arrayOfString = arrayOfString[2].split("=");
    if (arrayOfString.length > 1) {
      str = arrayOfString[1];
    } else {
      str = "";
    } 
    this.connectionPath = str;
  }
  
  public String getConnectionPath() {
    return this.connectionPath;
  }
  
  public int getProductId() {
    return this.productId;
  }
  
  public int getVendorId() {
    return this.vendorId;
  }
  
  public boolean isVendorProduct() {
    return true;
  }
  
  public boolean matches(Object paramObject) {
    boolean bool1 = paramObject instanceof VendorProductSerialNumber;
    boolean bool = false;
    null = bool;
    if (bool1) {
      paramObject = paramObject;
      null = bool;
      if (this.vendorId == ((VendorProductSerialNumber)paramObject).vendorId) {
        null = bool;
        if (this.productId == ((VendorProductSerialNumber)paramObject).productId) {
          if (!TextUtils.isEmpty(((VendorProductSerialNumber)paramObject).connectionPath)) {
            null = bool;
            return this.connectionPath.equals(((VendorProductSerialNumber)paramObject).connectionPath) ? true : null;
          } 
        } else {
          return null;
        } 
      } else {
        return null;
      } 
    } else {
      return null;
    } 
    return true;
  }
  
  public String toString() {
    String str;
    int i = this.vendorId;
    int j = this.productId;
    if (TextUtils.isEmpty(this.connectionPath)) {
      str = "";
    } else {
      str = ":";
    } 
    return Misc.formatForUser("%d:%d%s%s", new Object[] { Integer.valueOf(i), Integer.valueOf(j), str, this.connectionPath });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\interna\\usb\VendorProductSerialNumber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
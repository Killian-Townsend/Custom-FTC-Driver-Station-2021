package org.firstinspires.ftc.robotcore.internal.ftdi;

import android.hardware.usb.UsbDevice;

public class VendorAndProductIds {
  private int productId = 0;
  
  private int vendorId = 0;
  
  public VendorAndProductIds() {}
  
  public VendorAndProductIds(int paramInt1, int paramInt2) {}
  
  public static VendorAndProductIds from(UsbDevice paramUsbDevice) {
    return (paramUsbDevice == null) ? new VendorAndProductIds() : new VendorAndProductIds(paramUsbDevice.getVendorId(), paramUsbDevice.getProductId());
  }
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (!(paramObject instanceof VendorAndProductIds))
      return false; 
    paramObject = paramObject;
    return (this.vendorId == ((VendorAndProductIds)paramObject).vendorId && this.productId == ((VendorAndProductIds)paramObject).productId);
  }
  
  public int getProductId() {
    return this.productId;
  }
  
  public int getVendorId() {
    return this.vendorId;
  }
  
  public int hashCode() {
    return this.vendorId ^ this.productId;
  }
  
  public void setProductId(int paramInt) {
    this.productId = paramInt;
  }
  
  public void setVendorId(int paramInt) {
    this.vendorId = paramInt;
  }
  
  public String toString() {
    return String.format("vid=0x%04x pid=0x%04x", new Object[] { Integer.valueOf(this.vendorId), Integer.valueOf(this.productId) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\ftdi\VendorAndProductIds.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
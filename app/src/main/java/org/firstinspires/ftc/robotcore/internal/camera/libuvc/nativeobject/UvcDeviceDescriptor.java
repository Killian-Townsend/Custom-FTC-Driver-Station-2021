package org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject;

import org.firstinspires.ftc.robotcore.internal.system.NativeObject;

public class UvcDeviceDescriptor extends NativeObject {
  protected static int[] fieldOffsets = nativeGetFieldOffsets((Fields.values()).length);
  
  public UvcDeviceDescriptor(long paramLong) {
    super(paramLong, NativeObject.MemoryAllocator.EXTERNAL);
  }
  
  protected static native void nativeFreeDeviceDescriptor(long paramLong);
  
  protected static native int[] nativeGetFieldOffsets(int paramInt);
  
  protected void destructor() {
    if (this.pointer != 0L) {
      nativeFreeDeviceDescriptor(this.pointer);
      clearPointer();
    } 
    super.destructor();
  }
  
  public int getBcdUvc() {
    return getUShort(Fields.bcdUVC.offset());
  }
  
  public String getManufacturer() {
    return getString(Fields.manufacturer.offset());
  }
  
  public String getProduct() {
    return getString(Fields.product.offset());
  }
  
  public int getProductId() {
    return getUShort(Fields.idProduct.offset());
  }
  
  public String getSerialNumber() {
    return getString(Fields.serialNumber.offset());
  }
  
  protected int getStructSize() {
    return fieldOffsets[Fields.sizeof.ordinal()];
  }
  
  public int getVendorId() {
    return getUShort(Fields.idVendor.offset());
  }
  
  protected enum Fields {
    bcdUVC, idProduct, idVendor, manufacturer, product, serialNumber, sizeof;
    
    static {
      idProduct = new Fields("idProduct", 2);
      bcdUVC = new Fields("bcdUVC", 3);
      serialNumber = new Fields("serialNumber", 4);
      manufacturer = new Fields("manufacturer", 5);
      Fields fields = new Fields("product", 6);
      product = fields;
      $VALUES = new Fields[] { sizeof, idVendor, idProduct, bcdUVC, serialNumber, manufacturer, fields };
    }
    
    public int offset() {
      return UvcDeviceDescriptor.fieldOffsets[ordinal()];
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\nativeobject\UvcDeviceDescriptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
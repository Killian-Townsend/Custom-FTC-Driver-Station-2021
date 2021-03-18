package org.firstinspires.ftc.robotcore.internal.ftdi;

import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.firstinspires.ftc.robotcore.internal.system.Assert;

public class UsbDeviceDescriptor extends UsbDescriptorHeader {
  public int bDeviceClass;
  
  public int bDeviceProtocol;
  
  public int bDeviceSubClass;
  
  public int bMaxPacketSize0;
  
  public int bNumConfigurations;
  
  public int bcdDevice;
  
  public int bcdUSB;
  
  public int iManufacturer;
  
  public int iProduct;
  
  public int iSerialNumber;
  
  public int idProduct;
  
  public int idVendor;
  
  public UsbDeviceDescriptor(ByteBuffer paramByteBuffer) {
    super(paramByteBuffer);
    boolean bool;
    if (this.bLength >= 18) {
      bool = true;
    } else {
      bool = false;
    } 
    Assert.assertTrue(bool);
    this.bcdUSB = TypeConversion.unsignedShortToInt(paramByteBuffer.getShort());
    this.bDeviceClass = TypeConversion.unsignedByteToInt(paramByteBuffer.get());
    this.bDeviceSubClass = TypeConversion.unsignedByteToInt(paramByteBuffer.get());
    this.bDeviceProtocol = TypeConversion.unsignedByteToInt(paramByteBuffer.get());
    this.bMaxPacketSize0 = TypeConversion.unsignedByteToInt(paramByteBuffer.get());
    this.idVendor = TypeConversion.unsignedShortToInt(paramByteBuffer.getShort());
    this.idProduct = TypeConversion.unsignedShortToInt(paramByteBuffer.getShort());
    this.bcdDevice = TypeConversion.unsignedShortToInt(paramByteBuffer.getShort());
    this.iManufacturer = TypeConversion.unsignedByteToInt(paramByteBuffer.get());
    this.iProduct = TypeConversion.unsignedByteToInt(paramByteBuffer.get());
    this.iSerialNumber = TypeConversion.unsignedByteToInt(paramByteBuffer.get());
    this.bNumConfigurations = TypeConversion.unsignedByteToInt(paramByteBuffer.get());
  }
  
  public UsbDeviceDescriptor(byte[] paramArrayOfbyte) {
    this(ByteBuffer.wrap(paramArrayOfbyte).order(ByteOrder.LITTLE_ENDIAN));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\ftdi\UsbDeviceDescriptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
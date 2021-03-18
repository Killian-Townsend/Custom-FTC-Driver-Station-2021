package org.firstinspires.ftc.robotcore.internal.ftdi;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class UsbStringDescriptor extends UsbDescriptorHeader {
  public String string;
  
  public UsbStringDescriptor(ByteBuffer paramByteBuffer) {
    super(paramByteBuffer);
    byte[] arrayOfByte = new byte[this.bLength - cbOverhead()];
    paramByteBuffer.get(arrayOfByte);
    this.string = new String(arrayOfByte, Charset.forName("UTF-16LE"));
  }
  
  public UsbStringDescriptor(byte[] paramArrayOfbyte) {
    this(ByteBuffer.wrap(paramArrayOfbyte).order(ByteOrder.LITTLE_ENDIAN));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\ftdi\UsbStringDescriptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
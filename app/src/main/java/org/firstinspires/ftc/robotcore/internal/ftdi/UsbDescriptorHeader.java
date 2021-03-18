package org.firstinspires.ftc.robotcore.internal.ftdi;

import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class UsbDescriptorHeader {
  public int bDescriptorType;
  
  public int bLength;
  
  public UsbDescriptorHeader(ByteBuffer paramByteBuffer) {
    this.bLength = TypeConversion.unsignedByteToInt(paramByteBuffer.get());
    this.bDescriptorType = TypeConversion.unsignedByteToInt(paramByteBuffer.get());
  }
  
  public UsbDescriptorHeader(byte[] paramArrayOfbyte) {
    this(ByteBuffer.wrap(paramArrayOfbyte).order(ByteOrder.LITTLE_ENDIAN));
  }
  
  protected int cbOverhead() {
    return 2;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\ftdi\UsbDescriptorHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
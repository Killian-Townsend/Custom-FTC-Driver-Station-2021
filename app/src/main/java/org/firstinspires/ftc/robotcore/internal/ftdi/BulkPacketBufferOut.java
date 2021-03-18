package org.firstinspires.ftc.robotcore.internal.ftdi;

public class BulkPacketBufferOut extends BulkPacketBuffer {
  public BulkPacketBufferOut(int paramInt) {
    super(paramInt);
  }
  
  public void copyFrom(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    this.byteBuffer.clear();
    this.byteBuffer.put(paramArrayOfbyte, paramInt1, paramInt2);
    setCurrentLength(paramInt2);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\ftdi\BulkPacketBufferOut.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
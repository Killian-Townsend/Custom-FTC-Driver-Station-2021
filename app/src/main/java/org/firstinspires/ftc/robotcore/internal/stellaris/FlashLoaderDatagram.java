package org.firstinspires.ftc.robotcore.internal.stellaris;

public class FlashLoaderDatagram {
  public static final byte ACK = -52;
  
  public static final int CB_HEADER = 2;
  
  public static final int IB_LENGTH = 0;
  
  public static final int IB_PAYLOAD = 2;
  
  public static final int IB_XSUM = 1;
  
  public static final byte NAK = 51;
  
  public byte[] data;
  
  public FlashLoaderDatagram(int paramInt) {
    paramInt += 2;
    byte[] arrayOfByte = new byte[paramInt];
    this.data = arrayOfByte;
    arrayOfByte[0] = (byte)paramInt;
  }
  
  public FlashLoaderDatagram(byte[] paramArrayOfbyte) {
    this(paramArrayOfbyte.length);
    System.arraycopy(paramArrayOfbyte, 0, this.data, 2, paramArrayOfbyte.length);
    computeChecksum();
  }
  
  protected byte computeChecksum() {
    int j = 0;
    int i = 2;
    while (true) {
      byte[] arrayOfByte = this.data;
      if (i < arrayOfByte.length) {
        j += arrayOfByte[i];
        i++;
        continue;
      } 
      return (byte)j;
    } 
  }
  
  protected boolean isChecksumValid() {
    return (this.data[1] == computeChecksum());
  }
  
  protected void updateChecksum() {
    this.data[1] = computeChecksum();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\stellaris\FlashLoaderDatagram.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
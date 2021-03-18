package org.firstinspires.ftc.robotcore.internal.android.dex;

import org.firstinspires.ftc.robotcore.internal.android.dex.util.ByteArrayByteInput;
import org.firstinspires.ftc.robotcore.internal.android.dex.util.ByteInput;

public final class EncodedValue implements Comparable<EncodedValue> {
  private final byte[] data;
  
  public EncodedValue(byte[] paramArrayOfbyte) {
    this.data = paramArrayOfbyte;
  }
  
  public ByteInput asByteInput() {
    return (ByteInput)new ByteArrayByteInput(this.data);
  }
  
  public int compareTo(EncodedValue paramEncodedValue) {
    int j = Math.min(this.data.length, paramEncodedValue.data.length);
    for (int i = 0; i < j; i++) {
      byte[] arrayOfByte1 = this.data;
      byte b = arrayOfByte1[i];
      byte[] arrayOfByte2 = paramEncodedValue.data;
      if (b != arrayOfByte2[i])
        return (arrayOfByte1[i] & 0xFF) - (arrayOfByte2[i] & 0xFF); 
    } 
    return this.data.length - paramEncodedValue.data.length;
  }
  
  public byte[] getBytes() {
    return this.data;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(Integer.toHexString(this.data[0] & 0xFF));
    stringBuilder.append("...(");
    stringBuilder.append(this.data.length);
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
  
  public void writeTo(Dex.Section paramSection) {
    paramSection.write(this.data);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dex\EncodedValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
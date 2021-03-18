package org.firstinspires.ftc.robotcore.internal.android.dex.util;

public final class ByteArrayByteInput implements ByteInput {
  private final byte[] bytes;
  
  private int position;
  
  public ByteArrayByteInput(byte... paramVarArgs) {
    this.bytes = paramVarArgs;
  }
  
  public byte readByte() {
    byte[] arrayOfByte = this.bytes;
    int i = this.position;
    this.position = i + 1;
    return arrayOfByte[i];
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\de\\util\ByteArrayByteInput.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
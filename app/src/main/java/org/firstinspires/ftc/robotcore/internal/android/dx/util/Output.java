package org.firstinspires.ftc.robotcore.internal.android.dx.util;

import org.firstinspires.ftc.robotcore.internal.android.dex.util.ByteOutput;

public interface Output extends ByteOutput {
  void alignTo(int paramInt);
  
  void assertCursor(int paramInt);
  
  int getCursor();
  
  void write(ByteArray paramByteArray);
  
  void write(byte[] paramArrayOfbyte);
  
  void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
  
  void writeByte(int paramInt);
  
  void writeInt(int paramInt);
  
  void writeLong(long paramLong);
  
  void writeShort(int paramInt);
  
  int writeSleb128(int paramInt);
  
  int writeUleb128(int paramInt);
  
  void writeZeroes(int paramInt);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\d\\util\Output.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
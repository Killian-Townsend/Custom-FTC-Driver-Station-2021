package org.firstinspires.ftc.robotcore.internal.android.dx.io.instructions;

public interface CodeOutput extends CodeCursor {
  void write(short paramShort);
  
  void write(short paramShort1, short paramShort2);
  
  void write(short paramShort1, short paramShort2, short paramShort3);
  
  void write(short paramShort1, short paramShort2, short paramShort3, short paramShort4);
  
  void write(short paramShort1, short paramShort2, short paramShort3, short paramShort4, short paramShort5);
  
  void write(byte[] paramArrayOfbyte);
  
  void write(int[] paramArrayOfint);
  
  void write(long[] paramArrayOflong);
  
  void write(short[] paramArrayOfshort);
  
  void writeInt(int paramInt);
  
  void writeLong(long paramLong);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\io\instructions\CodeOutput.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
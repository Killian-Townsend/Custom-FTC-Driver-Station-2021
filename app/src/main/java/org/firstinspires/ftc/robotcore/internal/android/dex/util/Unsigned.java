package org.firstinspires.ftc.robotcore.internal.android.dex.util;

public final class Unsigned {
  public static int compare(int paramInt1, int paramInt2) {
    return (paramInt1 == paramInt2) ? 0 : (((paramInt1 & 0xFFFFFFFFL) < (paramInt2 & 0xFFFFFFFFL)) ? -1 : 1);
  }
  
  public static int compare(short paramShort1, short paramShort2) {
    return (paramShort1 == paramShort2) ? 0 : (((paramShort1 & 0xFFFF) < (paramShort2 & 0xFFFF)) ? -1 : 1);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\de\\util\Unsigned.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
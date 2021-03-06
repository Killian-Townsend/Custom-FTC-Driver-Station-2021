package androidx.core.os;

import android.os.Build;

public class BuildCompat {
  @Deprecated
  public static boolean isAtLeastN() {
    return (Build.VERSION.SDK_INT >= 24);
  }
  
  @Deprecated
  public static boolean isAtLeastNMR1() {
    return (Build.VERSION.SDK_INT >= 25);
  }
  
  @Deprecated
  public static boolean isAtLeastO() {
    return (Build.VERSION.SDK_INT >= 26);
  }
  
  @Deprecated
  public static boolean isAtLeastOMR1() {
    return (Build.VERSION.SDK_INT >= 27);
  }
  
  @Deprecated
  public static boolean isAtLeastP() {
    return (Build.VERSION.SDK_INT >= 28);
  }
  
  @Deprecated
  public static boolean isAtLeastQ() {
    return (Build.VERSION.SDK_INT >= 29);
  }
  
  public static boolean isAtLeastR() {
    return (Build.VERSION.CODENAME.length() == 1 && Build.VERSION.CODENAME.charAt(0) >= 'R' && Build.VERSION.CODENAME.charAt(0) <= 'Z');
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\core\os\BuildCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.qualcomm.robotcore.util;

import android.os.Build;
import android.view.InputDevice;
import java.util.HashSet;
import java.util.Set;

public class Hardware {
  private static boolean mIsIFC = CheckIfIFC();
  
  public static boolean CheckIfIFC() {
    String str1 = Build.BOARD;
    String str2 = Build.BRAND;
    String str3 = Build.DEVICE;
    String str4 = Build.HARDWARE;
    String str5 = Build.MANUFACTURER;
    String str6 = Build.MODEL;
    String str7 = Build.PRODUCT;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Platform information: board = ");
    stringBuilder.append(str1);
    stringBuilder.append(" brand = ");
    stringBuilder.append(str2);
    stringBuilder.append(" device = ");
    stringBuilder.append(str3);
    stringBuilder.append(" hardware = ");
    stringBuilder.append(str4);
    stringBuilder.append(" manufacturer = ");
    stringBuilder.append(str5);
    stringBuilder.append(" model = ");
    stringBuilder.append(str6);
    stringBuilder.append(" product = ");
    stringBuilder.append(str7);
    RobotLog.d(stringBuilder.toString());
    if (str1.equals("MSM8960") == true && str2.equals("qcom") == true && str3.equals("msm8960") == true && str4.equals("qcom") == true && str5.equals("unknown") == true && str6.equals("msm8960") == true && str7.equals("msm8960") == true) {
      RobotLog.d("Detected IFC6410 Device!");
      return true;
    } 
    RobotLog.d("Detected regular SmartPhone Device!");
    return false;
  }
  
  public static boolean IsIFC() {
    return mIsIFC;
  }
  
  public static Set<Integer> getGameControllerIds() {
    HashSet<Integer> hashSet = new HashSet();
    for (int i : InputDevice.getDeviceIds()) {
      int j = InputDevice.getDevice(i).getSources();
      if ((j & 0x401) == 1025 || (j & 0x1000010) == 16777232)
        hashSet.add(Integer.valueOf(i)); 
    } 
    return hashSet;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcor\\util\Hardware.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.system;

public interface PermissionListener {
  void onPermissionDenied(String paramString);
  
  void onPermissionGranted(String paramString);
  
  void onPermissionPermanentlyDenied(String paramString);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\PermissionListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
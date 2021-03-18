package org.firstinspires.ftc.robotcore.internal.network;

public interface PasswordManager {
  String getPassword();
  
  boolean isDefault();
  
  String resetPassword(boolean paramBoolean);
  
  void setPassword(String paramString, boolean paramBoolean) throws InvalidNetworkSettingException;
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\PasswordManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
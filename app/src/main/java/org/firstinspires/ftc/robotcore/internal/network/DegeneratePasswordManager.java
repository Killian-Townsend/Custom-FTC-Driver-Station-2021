package org.firstinspires.ftc.robotcore.internal.network;

public class DegeneratePasswordManager implements PasswordManager {
  public String getPassword() {
    return null;
  }
  
  public boolean isDefault() {
    return false;
  }
  
  public String resetPassword(boolean paramBoolean) {
    return "";
  }
  
  public void setPassword(String paramString, boolean paramBoolean) {}
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\DegeneratePasswordManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
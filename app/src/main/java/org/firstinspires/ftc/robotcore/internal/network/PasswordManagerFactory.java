package org.firstinspires.ftc.robotcore.internal.network;

import com.qualcomm.robotcore.util.Device;

public class PasswordManagerFactory {
  protected static PasswordManager passwordManager;
  
  public static PasswordManager getInstance() {
    if (passwordManager == null)
      if (Device.isRevControlHub() == true) {
        passwordManager = new ControlHubPasswordManager();
      } else {
        passwordManager = new DegeneratePasswordManager();
      }  
    return passwordManager;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\PasswordManagerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
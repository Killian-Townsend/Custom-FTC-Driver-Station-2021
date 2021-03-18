package com.qualcomm.ftccommon.configuration;

public class EditLegacyServoControllerActivity extends EditServoControllerActivity {
  public String getTag() {
    return getClass().getSimpleName();
  }
  
  protected void refreshSerialNumber() {
    this.textViewSerialNumber.setVisibility(8);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\EditLegacyServoControllerActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
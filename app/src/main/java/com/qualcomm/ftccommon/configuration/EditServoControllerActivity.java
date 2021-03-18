package com.qualcomm.ftccommon.configuration;

import com.qualcomm.robotcore.hardware.configuration.ServoControllerConfiguration;

public class EditServoControllerActivity extends EditServoListActivity {
  public static final RequestCode requestCode = RequestCode.EDIT_SERVO_CONTROLLER;
  
  public void finishOk() {
    ((ServoControllerConfiguration)this.controllerConfiguration).setServos(this.itemList);
    super.finishOk();
  }
  
  public String getTag() {
    return getClass().getSimpleName();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\EditServoControllerActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
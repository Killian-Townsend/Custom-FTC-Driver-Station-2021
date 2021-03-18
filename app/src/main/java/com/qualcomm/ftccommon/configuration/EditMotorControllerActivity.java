package com.qualcomm.ftccommon.configuration;

import com.qualcomm.robotcore.hardware.configuration.MotorControllerConfiguration;

public class EditMotorControllerActivity extends EditMotorListActivity {
  public static final RequestCode requestCode = RequestCode.EDIT_MOTOR_CONTROLLER;
  
  public void finishOk() {
    ((MotorControllerConfiguration)this.controllerConfiguration).setMotors(this.itemList);
    super.finishOk();
  }
  
  public String getTag() {
    return getClass().getSimpleName();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\EditMotorControllerActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
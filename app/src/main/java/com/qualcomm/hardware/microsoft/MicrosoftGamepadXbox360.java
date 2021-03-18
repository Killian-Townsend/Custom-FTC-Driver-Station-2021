package com.qualcomm.hardware.microsoft;

import com.qualcomm.robotcore.hardware.Gamepad;

public class MicrosoftGamepadXbox360 extends Gamepad {
  public MicrosoftGamepadXbox360() {
    this(null);
  }
  
  public MicrosoftGamepadXbox360(Gamepad.GamepadCallback paramGamepadCallback) {
    super(paramGamepadCallback);
  }
  
  public static boolean matchesVidPid(int paramInt1, int paramInt2) {
    return (paramInt1 == 1118 && paramInt2 == 654);
  }
  
  public Gamepad.Type type() {
    return Gamepad.Type.XBOX_360;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\microsoft\MicrosoftGamepadXbox360.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
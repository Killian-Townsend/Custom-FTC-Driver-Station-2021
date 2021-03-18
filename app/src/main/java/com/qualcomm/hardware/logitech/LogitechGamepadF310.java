package com.qualcomm.hardware.logitech;

import android.view.MotionEvent;
import com.qualcomm.robotcore.hardware.Gamepad;

public class LogitechGamepadF310 extends Gamepad {
  public LogitechGamepadF310() {
    this((Gamepad.GamepadCallback)null);
  }
  
  public LogitechGamepadF310(Gamepad.GamepadCallback paramGamepadCallback) {
    super(paramGamepadCallback);
  }
  
  public static boolean matchesVidPid(int paramInt1, int paramInt2) {
    return (paramInt1 == 1133 && paramInt2 == 49693);
  }
  
  public Gamepad.Type type() {
    return Gamepad.Type.LOGITECH_F310;
  }
  
  public void update(MotionEvent paramMotionEvent) {
    setGamepadId(paramMotionEvent.getDeviceId());
    setTimestamp(paramMotionEvent.getEventTime());
    boolean bool2 = false;
    this.left_stick_x = cleanMotionValues(paramMotionEvent.getAxisValue(0));
    this.left_stick_y = cleanMotionValues(paramMotionEvent.getAxisValue(1));
    this.right_stick_x = cleanMotionValues(paramMotionEvent.getAxisValue(11));
    this.right_stick_y = cleanMotionValues(paramMotionEvent.getAxisValue(14));
    this.left_trigger = paramMotionEvent.getAxisValue(23);
    this.right_trigger = paramMotionEvent.getAxisValue(22);
    if (paramMotionEvent.getAxisValue(16) > this.dpadThreshold) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    this.dpad_down = bool1;
    if (paramMotionEvent.getAxisValue(16) < -this.dpadThreshold) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    this.dpad_up = bool1;
    if (paramMotionEvent.getAxisValue(15) > this.dpadThreshold) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    this.dpad_right = bool1;
    boolean bool1 = bool2;
    if (paramMotionEvent.getAxisValue(15) < -this.dpadThreshold)
      bool1 = true; 
    this.dpad_left = bool1;
    callCallback();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\logitech\LogitechGamepadF310.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
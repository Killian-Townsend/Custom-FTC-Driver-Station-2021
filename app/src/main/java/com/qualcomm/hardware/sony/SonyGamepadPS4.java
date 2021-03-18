package com.qualcomm.hardware.sony;

import android.view.KeyEvent;
import android.view.MotionEvent;
import com.qualcomm.robotcore.hardware.Gamepad;

public class SonyGamepadPS4 extends Gamepad {
  protected boolean TriggerFixLeft = false;
  
  protected boolean TriggerFixRight = false;
  
  public SonyGamepadPS4() {
    this((Gamepad.GamepadCallback)null);
  }
  
  public SonyGamepadPS4(Gamepad.GamepadCallback paramGamepadCallback) {
    super(paramGamepadCallback);
  }
  
  public static boolean matchesVidPid(int paramInt1, int paramInt2) {
    int i;
    boolean bool = true;
    if (paramInt1 == 1356 && paramInt2 == 2508) {
      i = 1;
    } else {
      i = 0;
    } 
    if (paramInt1 == 30021 && paramInt2 == 260) {
      paramInt1 = bool;
    } else {
      paramInt1 = 0;
    } 
    return i | paramInt1;
  }
  
  public String toString() {
    return ps4ToString();
  }
  
  public Gamepad.Type type() {
    return Gamepad.Type.SONY_PS4;
  }
  
  public void update(KeyEvent paramKeyEvent) {
    setGamepadId(paramKeyEvent.getDeviceId());
    setTimestamp(paramKeyEvent.getEventTime());
    int i = paramKeyEvent.getKeyCode();
    if (i == 97) {
      this.a = pressed(paramKeyEvent);
    } else if (i == 98) {
      this.b = pressed(paramKeyEvent);
    } else if (i == 96) {
      this.x = pressed(paramKeyEvent);
    } else if (i == 99) {
      this.y = pressed(paramKeyEvent);
    } else if (i == 110) {
      this.guide = pressed(paramKeyEvent);
    } else if (i == 105) {
      this.start = pressed(paramKeyEvent);
    } else if (i == 104) {
      this.back = pressed(paramKeyEvent);
    } else if (i == 106) {
      this.touchpad = pressed(paramKeyEvent);
    } else if (i == 101) {
      this.right_bumper = pressed(paramKeyEvent);
    } else if (i == 100) {
      this.left_bumper = pressed(paramKeyEvent);
    } else if (i == 109) {
      this.left_stick_button = pressed(paramKeyEvent);
    } else if (i == 108) {
      this.right_stick_button = pressed(paramKeyEvent);
    } 
    updateButtonAliases();
    callCallback();
  }
  
  public void update(MotionEvent paramMotionEvent) {
    setGamepadId(paramMotionEvent.getDeviceId());
    setTimestamp(paramMotionEvent.getEventTime());
    boolean bool2 = false;
    this.left_stick_x = cleanMotionValues(paramMotionEvent.getAxisValue(0));
    this.left_stick_y = cleanMotionValues(paramMotionEvent.getAxisValue(1));
    this.right_stick_x = cleanMotionValues(paramMotionEvent.getAxisValue(11));
    this.right_stick_y = cleanMotionValues(paramMotionEvent.getAxisValue(14));
    float f = paramMotionEvent.getAxisValue(12) * 0.5F + 0.5F;
    if (this.TriggerFixLeft) {
      this.left_trigger = f;
    } else {
      this.left_trigger = 0.0F;
      if (f != 0.5F) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      this.TriggerFixLeft = bool1;
    } 
    f = paramMotionEvent.getAxisValue(13) * 0.5F + 0.5F;
    if (this.TriggerFixRight) {
      this.right_trigger = f;
    } else {
      this.right_trigger = 0.0F;
      if (f != 0.5F) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      this.TriggerFixRight = bool1;
    } 
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
    updateButtonAliases();
    callCallback();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\sony\SonyGamepadPS4.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.hardware.Gamepad;

class GamepadAccess extends Access {
  private final Gamepad gamepad;
  
  GamepadAccess(BlocksOpMode paramBlocksOpMode, String paramString, Gamepad paramGamepad) {
    super(paramBlocksOpMode, paramString, paramString);
    this.gamepad = paramGamepad;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"a"})
  public boolean getA() {
    startBlockExecution(BlockType.GETTER, ".A");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.a : false;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, methodName = {"atRest"})
  public boolean getAtRest() {
    startBlockExecution(BlockType.GETTER, ".AtRest");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.atRest() : false;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"b"})
  public boolean getB() {
    startBlockExecution(BlockType.GETTER, ".B");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.b : false;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"back"})
  public boolean getBack() {
    startBlockExecution(BlockType.GETTER, ".Back");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.back : false;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"circle"})
  public boolean getCircle() {
    startBlockExecution(BlockType.GETTER, ".Circle");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.circle : false;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"cross"})
  public boolean getCross() {
    startBlockExecution(BlockType.GETTER, ".Cross");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.cross : false;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"dpad_down"})
  public boolean getDpadDown() {
    startBlockExecution(BlockType.GETTER, ".DpadDown");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.dpad_down : false;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"dpad_left"})
  public boolean getDpadLeft() {
    startBlockExecution(BlockType.GETTER, ".DpadLeft");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.dpad_left : false;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"dpad_right"})
  public boolean getDpadRight() {
    startBlockExecution(BlockType.GETTER, ".DpadRight");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.dpad_right : false;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"dpad_up"})
  public boolean getDpadUp() {
    startBlockExecution(BlockType.GETTER, ".DpadUp");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.dpad_up : false;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"guide"})
  public boolean getGuide() {
    startBlockExecution(BlockType.GETTER, ".Guide");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.guide : false;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"left_bumper"})
  public boolean getLeftBumper() {
    startBlockExecution(BlockType.GETTER, ".LeftBumper");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.left_bumper : false;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"left_stick_button"})
  public boolean getLeftStickButton() {
    startBlockExecution(BlockType.GETTER, ".LeftStickButton");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.left_stick_button : false;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"left_stick_x"})
  public float getLeftStickX() {
    startBlockExecution(BlockType.GETTER, ".LeftStickX");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.left_stick_x : 0.0F;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"left_stick_y"})
  public float getLeftStickY() {
    startBlockExecution(BlockType.GETTER, ".LeftStickY");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.left_stick_y : 0.0F;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"left_trigger"})
  public float getLeftTrigger() {
    startBlockExecution(BlockType.GETTER, ".LeftTrigger");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.left_trigger : 0.0F;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"options"})
  public boolean getOptions() {
    startBlockExecution(BlockType.GETTER, ".Options");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.options : false;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"ps"})
  public boolean getPS() {
    startBlockExecution(BlockType.GETTER, ".PS");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.ps : false;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"right_bumper"})
  public boolean getRightBumper() {
    startBlockExecution(BlockType.GETTER, ".RightBumper");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.right_bumper : false;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"right_stick_button"})
  public boolean getRightStickButton() {
    startBlockExecution(BlockType.GETTER, ".RightStickButton");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.right_stick_button : false;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"right_stick_x"})
  public float getRightStickX() {
    startBlockExecution(BlockType.GETTER, ".RightStickX");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.right_stick_x : 0.0F;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"right_stick_y"})
  public float getRightStickY() {
    startBlockExecution(BlockType.GETTER, ".RightStickY");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.right_stick_y : 0.0F;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"right_trigger"})
  public float getRightTrigger() {
    startBlockExecution(BlockType.GETTER, ".RightTrigger");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.right_trigger : 0.0F;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"share"})
  public boolean getShare() {
    startBlockExecution(BlockType.GETTER, ".Share");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.share : false;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"square"})
  public boolean getSquare() {
    startBlockExecution(BlockType.GETTER, ".Square");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.square : false;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"start"})
  public boolean getStart() {
    startBlockExecution(BlockType.GETTER, ".Start");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.start : false;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"touchpad"})
  public boolean getTouchpad() {
    startBlockExecution(BlockType.GETTER, ".Touchpad");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.touchpad : false;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"triangle"})
  public boolean getTriangle() {
    startBlockExecution(BlockType.GETTER, ".Triangle");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.triangle : false;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"x"})
  public boolean getX() {
    startBlockExecution(BlockType.GETTER, ".X");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.x : false;
  }
  
  @JavascriptInterface
  @Block(classes = {Gamepad.class}, fieldName = {"y"})
  public boolean getY() {
    startBlockExecution(BlockType.GETTER, ".Y");
    Gamepad gamepad = this.gamepad;
    return (gamepad != null) ? gamepad.y : false;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\GamepadAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
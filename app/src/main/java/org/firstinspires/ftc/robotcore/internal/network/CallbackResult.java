package org.firstinspires.ftc.robotcore.internal.network;

public enum CallbackResult {
  HANDLED, HANDLED_CONTINUE, NOT_HANDLED;
  
  static {
    HANDLED = new CallbackResult("HANDLED", 1);
    CallbackResult callbackResult = new CallbackResult("HANDLED_CONTINUE", 2);
    HANDLED_CONTINUE = callbackResult;
    $VALUES = new CallbackResult[] { NOT_HANDLED, HANDLED, callbackResult };
  }
  
  public boolean isHandled() {
    return (this != NOT_HANDLED);
  }
  
  public boolean stopDispatch() {
    return (this == HANDLED);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\CallbackResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
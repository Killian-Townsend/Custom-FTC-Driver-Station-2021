package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.util.RobotLog;

class DbgLogAccess extends Access {
  public static final String TAG = "DbgLog";
  
  DbgLogAccess(BlocksOpMode paramBlocksOpMode, String paramString) {
    super(paramBlocksOpMode, paramString, "DbgLog");
  }
  
  @JavascriptInterface
  public void error(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".error");
    RobotLog.ee("DbgLog", paramString);
  }
  
  @JavascriptInterface
  public void msg(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".msg");
    RobotLog.ii("DbgLog", paramString);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\DbgLogAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.vuforia;

import org.firstinspires.ftc.robotcore.internal.system.Misc;

public class VuforiaException extends RuntimeException {
  public VuforiaException(Exception paramException, String paramString, Object... paramVarArgs) {
    super(Misc.formatForUser(paramString, paramVarArgs), paramException);
  }
  
  public VuforiaException(String paramString, Object... paramVarArgs) {
    super(Misc.formatForUser(paramString, paramVarArgs));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\vuforia\VuforiaException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
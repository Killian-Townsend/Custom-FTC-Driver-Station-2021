package org.firstinspires.ftc.robotcore.internal.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class OpModeMetaAndClass {
  public final Class<OpMode> clazz;
  
  public final OpModeMeta meta;
  
  public OpModeMetaAndClass(OpModeMeta paramOpModeMeta, Class<OpMode> paramClass) {
    this.meta = paramOpModeMeta;
    this.clazz = paramClass;
  }
  
  public boolean isOnBotJava() {
    return OnBotJavaDeterminer.isOnBotJava(this.clazz);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opmode\OpModeMetaAndClass.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
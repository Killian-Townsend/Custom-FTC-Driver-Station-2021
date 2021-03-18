package org.firstinspires.ftc.robotcore.internal.android.dx.dex;

import org.firstinspires.ftc.robotcore.internal.android.dex.DexFormat;

public class DexOptions {
  public static final boolean ALIGN_64BIT_REGS_SUPPORT = true;
  
  public boolean ALIGN_64BIT_REGS_IN_OUTPUT_FINISHER = true;
  
  public boolean forceJumbo = false;
  
  public int targetApiLevel = 13;
  
  public String getMagic() {
    return DexFormat.apiToMagic(this.targetApiLevel);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\DexOptions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
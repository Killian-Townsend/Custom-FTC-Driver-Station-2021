package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import java.io.PrintWriter;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.ToHuman;

public abstract class EncodedMember implements ToHuman {
  private final int accessFlags;
  
  public EncodedMember(int paramInt) {
    this.accessFlags = paramInt;
  }
  
  public abstract void addContents(DexFile paramDexFile);
  
  public abstract void debugPrint(PrintWriter paramPrintWriter, boolean paramBoolean);
  
  public abstract int encode(DexFile paramDexFile, AnnotatedOutput paramAnnotatedOutput, int paramInt1, int paramInt2);
  
  public final int getAccessFlags() {
    return this.accessFlags;
  }
  
  public abstract CstString getName();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\EncodedMember.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
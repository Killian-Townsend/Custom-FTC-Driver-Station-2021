package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code;

import java.util.HashSet;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;

public interface CatchBuilder {
  CatchTable build();
  
  HashSet<Type> getCatchTypes();
  
  boolean hasAnyCatches();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\CatchBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
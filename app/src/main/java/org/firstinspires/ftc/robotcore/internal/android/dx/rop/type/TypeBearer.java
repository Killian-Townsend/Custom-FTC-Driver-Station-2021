package org.firstinspires.ftc.robotcore.internal.android.dx.rop.type;

import org.firstinspires.ftc.robotcore.internal.android.dx.util.ToHuman;

public interface TypeBearer extends ToHuman {
  int getBasicFrameType();
  
  int getBasicType();
  
  TypeBearer getFrameType();
  
  Type getType();
  
  boolean isConstant();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\type\TypeBearer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
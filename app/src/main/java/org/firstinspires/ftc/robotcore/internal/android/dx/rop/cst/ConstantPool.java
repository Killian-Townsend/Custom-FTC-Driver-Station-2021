package org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst;

public interface ConstantPool {
  Constant get(int paramInt);
  
  Constant get0Ok(int paramInt);
  
  Constant[] getEntries();
  
  Constant getOrNull(int paramInt);
  
  int size();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\cst\ConstantPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
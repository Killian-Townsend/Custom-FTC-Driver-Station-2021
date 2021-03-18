package org.firstinspires.ftc.robotcore.internal.android.dx.rop.type;

public interface TypeList {
  Type getType(int paramInt);
  
  int getWordCount();
  
  boolean isMutable();
  
  int size();
  
  TypeList withAddedType(Type paramType);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\type\TypeList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
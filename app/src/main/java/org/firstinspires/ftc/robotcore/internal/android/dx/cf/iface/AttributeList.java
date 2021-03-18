package org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface;

public interface AttributeList {
  int byteLength();
  
  Attribute findFirst(String paramString);
  
  Attribute findNext(Attribute paramAttribute);
  
  Attribute get(int paramInt);
  
  boolean isMutable();
  
  int size();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\iface\AttributeList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
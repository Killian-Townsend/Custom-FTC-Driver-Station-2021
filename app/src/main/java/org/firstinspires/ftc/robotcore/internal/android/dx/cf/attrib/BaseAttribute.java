package org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib;

import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.Attribute;

public abstract class BaseAttribute implements Attribute {
  private final String name;
  
  public BaseAttribute(String paramString) {
    if (paramString != null) {
      this.name = paramString;
      return;
    } 
    throw new NullPointerException("name == null");
  }
  
  public String getName() {
    return this.name;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\attrib\BaseAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
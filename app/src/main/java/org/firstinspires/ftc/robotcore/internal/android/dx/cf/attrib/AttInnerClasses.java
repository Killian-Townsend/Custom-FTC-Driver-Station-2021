package org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib;

import org.firstinspires.ftc.robotcore.internal.android.dx.util.MutabilityException;

public final class AttInnerClasses extends BaseAttribute {
  public static final String ATTRIBUTE_NAME = "InnerClasses";
  
  private final InnerClassList innerClasses;
  
  public AttInnerClasses(InnerClassList paramInnerClassList) {
    super("InnerClasses");
    try {
      boolean bool = paramInnerClassList.isMutable();
      if (!bool) {
        this.innerClasses = paramInnerClassList;
        return;
      } 
      throw new MutabilityException("innerClasses.isMutable()");
    } catch (NullPointerException nullPointerException) {
      throw new NullPointerException("innerClasses == null");
    } 
  }
  
  public int byteLength() {
    return this.innerClasses.size() * 8 + 8;
  }
  
  public InnerClassList getInnerClasses() {
    return this.innerClasses;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\attrib\AttInnerClasses.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.annotation.Annotations;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.MutabilityException;

public abstract class BaseAnnotations extends BaseAttribute {
  private final Annotations annotations;
  
  private final int byteLength;
  
  public BaseAnnotations(String paramString, Annotations paramAnnotations, int paramInt) {
    super(paramString);
    try {
      boolean bool = paramAnnotations.isMutable();
      if (!bool) {
        this.annotations = paramAnnotations;
        this.byteLength = paramInt;
        return;
      } 
      throw new MutabilityException("annotations.isMutable()");
    } catch (NullPointerException nullPointerException) {
      throw new NullPointerException("annotations == null");
    } 
  }
  
  public final int byteLength() {
    return this.byteLength + 6;
  }
  
  public final Annotations getAnnotations() {
    return this.annotations;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\attrib\BaseAnnotations.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
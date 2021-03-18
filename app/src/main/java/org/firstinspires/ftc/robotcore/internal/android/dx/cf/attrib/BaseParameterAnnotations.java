package org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.annotation.AnnotationsList;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.MutabilityException;

public abstract class BaseParameterAnnotations extends BaseAttribute {
  private final int byteLength;
  
  private final AnnotationsList parameterAnnotations;
  
  public BaseParameterAnnotations(String paramString, AnnotationsList paramAnnotationsList, int paramInt) {
    super(paramString);
    try {
      boolean bool = paramAnnotationsList.isMutable();
      if (!bool) {
        this.parameterAnnotations = paramAnnotationsList;
        this.byteLength = paramInt;
        return;
      } 
      throw new MutabilityException("parameterAnnotations.isMutable()");
    } catch (NullPointerException nullPointerException) {
      throw new NullPointerException("parameterAnnotations == null");
    } 
  }
  
  public final int byteLength() {
    return this.byteLength + 6;
  }
  
  public final AnnotationsList getParameterAnnotations() {
    return this.parameterAnnotations;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\attrib\BaseParameterAnnotations.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
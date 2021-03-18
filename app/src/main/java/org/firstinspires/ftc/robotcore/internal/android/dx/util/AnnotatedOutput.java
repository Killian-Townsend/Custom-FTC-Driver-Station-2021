package org.firstinspires.ftc.robotcore.internal.android.dx.util;

public interface AnnotatedOutput extends Output {
  void annotate(int paramInt, String paramString);
  
  void annotate(String paramString);
  
  boolean annotates();
  
  void endAnnotation();
  
  int getAnnotationWidth();
  
  boolean isVerbose();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\d\\util\AnnotatedOutput.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
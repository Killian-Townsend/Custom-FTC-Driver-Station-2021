package org.firstinspires.ftc.robotcore.internal.android.dx.rop.annotation;

import org.firstinspires.ftc.robotcore.internal.android.dx.util.ToHuman;

public enum AnnotationVisibility implements ToHuman {
  BUILD,
  EMBEDDED,
  RUNTIME("runtime"),
  SYSTEM("runtime");
  
  private final String human;
  
  static {
    BUILD = new AnnotationVisibility("BUILD", 1, "build");
    SYSTEM = new AnnotationVisibility("SYSTEM", 2, "system");
    AnnotationVisibility annotationVisibility = new AnnotationVisibility("EMBEDDED", 3, "embedded");
    EMBEDDED = annotationVisibility;
    $VALUES = new AnnotationVisibility[] { RUNTIME, BUILD, SYSTEM, annotationVisibility };
  }
  
  AnnotationVisibility(String paramString1) {
    this.human = paramString1;
  }
  
  public String toHuman() {
    return this.human;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\annotation\AnnotationVisibility.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
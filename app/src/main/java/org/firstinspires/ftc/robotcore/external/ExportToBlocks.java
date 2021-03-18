package org.firstinspires.ftc.robotcore.external;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ExportToBlocks {
  String comment() default "";
  
  String[] parameterLabels() default {};
  
  String tooltip() default "";
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\ExportToBlocks.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
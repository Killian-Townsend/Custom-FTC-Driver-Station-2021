package com.qualcomm.robotcore.hardware.configuration.annotations;

import com.qualcomm.robotcore.hardware.configuration.ServoFlavor;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ServoType {
  ServoFlavor flavor();
  
  double usPulseFrameRate() default 20000.0D;
  
  double usPulseLower() default 600.0D;
  
  double usPulseUpper() default 2400.0D;
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\annotations\ServoType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
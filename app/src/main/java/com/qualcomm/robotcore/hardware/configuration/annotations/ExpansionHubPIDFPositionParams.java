package com.qualcomm.robotcore.hardware.configuration.annotations;

import com.qualcomm.robotcore.hardware.MotorControlAlgorithm;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ExpansionHubPIDFPositionParams {
  double P();
  
  MotorControlAlgorithm algorithm() default MotorControlAlgorithm.PIDF;
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\annotations\ExpansionHubPIDFPositionParams.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
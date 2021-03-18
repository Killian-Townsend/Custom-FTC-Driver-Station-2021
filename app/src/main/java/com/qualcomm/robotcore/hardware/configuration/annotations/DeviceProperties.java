package com.qualcomm.robotcore.hardware.configuration.annotations;

import com.qualcomm.robotcore.hardware.ControlSystem;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface DeviceProperties {
  boolean builtIn() default false;
  
  ControlSystem[] compatibleControlSystems() default {ControlSystem.MODERN_ROBOTICS, ControlSystem.REV_HUB};
  
  String description() default "";
  
  String name();
  
  String xmlTag();
  
  String[] xmlTagAliases() default {};
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\annotations\DeviceProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
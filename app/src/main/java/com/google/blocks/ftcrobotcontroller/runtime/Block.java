package com.google.blocks.ftcrobotcontroller.runtime;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Block {
  Class[] classes() default {};
  
  boolean constructor() default false;
  
  boolean exclusiveToBlocks() default false;
  
  String[] fieldName() default {};
  
  String[] methodName() default {};
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\Block.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
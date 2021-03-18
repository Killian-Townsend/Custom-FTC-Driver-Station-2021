package com.qualcomm.robotcore.hardware.configuration;

import java.lang.reflect.Constructor;

public class ConstructorPrototype {
  Class<?>[] prototypeParameterTypes;
  
  public ConstructorPrototype(Class<?>... paramVarArgs) {
    this.prototypeParameterTypes = paramVarArgs;
  }
  
  public boolean matches(Constructor paramConstructor) {
    Class[] arrayOfClass = paramConstructor.getParameterTypes();
    if (arrayOfClass.length == this.prototypeParameterTypes.length) {
      int i = 0;
      while (true) {
        Class<?>[] arrayOfClass1 = this.prototypeParameterTypes;
        if (i < arrayOfClass1.length) {
          if (!arrayOfClass[i].equals(arrayOfClass1[i]))
            return false; 
          i++;
          continue;
        } 
        return true;
      } 
    } 
    return false;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\ConstructorPrototype.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
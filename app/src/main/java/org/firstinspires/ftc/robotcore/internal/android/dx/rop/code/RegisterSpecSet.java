package org.firstinspires.ftc.robotcore.internal.android.dx.rop.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.util.MutabilityControl;

public final class RegisterSpecSet extends MutabilityControl {
  public static final RegisterSpecSet EMPTY = new RegisterSpecSet(0);
  
  private int size;
  
  private final RegisterSpec[] specs;
  
  public RegisterSpecSet(int paramInt) {
    super(bool);
    boolean bool;
    this.specs = new RegisterSpec[paramInt];
    this.size = 0;
  }
  
  public boolean equals(Object paramObject) {
    if (!(paramObject instanceof RegisterSpecSet))
      return false; 
    RegisterSpecSet registerSpecSet = (RegisterSpecSet)paramObject;
    paramObject = registerSpecSet.specs;
    int i = this.specs.length;
    if (i == paramObject.length) {
      if (size() != registerSpecSet.size())
        return false; 
      for (int j = 0; j < i; j++) {
        RegisterSpec registerSpec = this.specs[j];
        Object object = paramObject[j];
        if (registerSpec != object)
          if (registerSpec != null) {
            if (!registerSpec.equals(object))
              return false; 
          } else {
            return false;
          }  
      } 
      return true;
    } 
    return false;
  }
  
  public RegisterSpec findMatchingLocal(RegisterSpec paramRegisterSpec) {
    int j = this.specs.length;
    for (int i = 0; i < j; i++) {
      RegisterSpec registerSpec = this.specs[i];
      if (registerSpec != null && paramRegisterSpec.matchesVariable(registerSpec))
        return registerSpec; 
    } 
    return null;
  }
  
  public RegisterSpec get(int paramInt) {
    try {
      return this.specs[paramInt];
    } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
      throw new IllegalArgumentException("bogus reg");
    } 
  }
  
  public RegisterSpec get(RegisterSpec paramRegisterSpec) {
    return get(paramRegisterSpec.getReg());
  }
  
  public int getMaxSize() {
    return this.specs.length;
  }
  
  public int hashCode() {
    int k = this.specs.length;
    int i = 0;
    int j = i;
    while (i < k) {
      int m;
      RegisterSpec registerSpec = this.specs[i];
      if (registerSpec == null) {
        m = 0;
      } else {
        m = registerSpec.hashCode();
      } 
      j = j * 31 + m;
      i++;
    } 
    return j;
  }
  
  public void intersect(RegisterSpecSet paramRegisterSpecSet, boolean paramBoolean) {
    int j;
    throwIfImmutable();
    RegisterSpec[] arrayOfRegisterSpec = paramRegisterSpecSet.specs;
    int m = this.specs.length;
    int k = Math.min(m, arrayOfRegisterSpec.length);
    this.size = -1;
    int i = 0;
    while (true) {
      j = k;
      if (i < k) {
        RegisterSpec registerSpec = this.specs[i];
        if (registerSpec != null) {
          RegisterSpec registerSpec1 = registerSpec.intersect(arrayOfRegisterSpec[i], paramBoolean);
          if (registerSpec1 != registerSpec)
            this.specs[i] = registerSpec1; 
        } 
        i++;
        continue;
      } 
      break;
    } 
    while (j < m) {
      this.specs[j] = null;
      j++;
    } 
  }
  
  public RegisterSpec localItemToSpec(LocalItem paramLocalItem) {
    int j = this.specs.length;
    for (int i = 0; i < j; i++) {
      RegisterSpec registerSpec = this.specs[i];
      if (registerSpec != null && paramLocalItem.equals(registerSpec.getLocalItem()))
        return registerSpec; 
    } 
    return null;
  }
  
  public RegisterSpecSet mutableCopy() {
    int j = this.specs.length;
    RegisterSpecSet registerSpecSet = new RegisterSpecSet(j);
    for (int i = 0; i < j; i++) {
      RegisterSpec registerSpec = this.specs[i];
      if (registerSpec != null)
        registerSpecSet.put(registerSpec); 
    } 
    registerSpecSet.size = this.size;
    return registerSpecSet;
  }
  
  public void put(RegisterSpec paramRegisterSpec) {
    throwIfImmutable();
    if (paramRegisterSpec != null) {
      this.size = -1;
      try {
        int i = paramRegisterSpec.getReg();
        this.specs[i] = paramRegisterSpec;
        if (i > 0) {
          int j = i - 1;
          RegisterSpec registerSpec = this.specs[j];
          if (registerSpec != null && registerSpec.getCategory() == 2)
            this.specs[j] = null; 
        } 
        if (paramRegisterSpec.getCategory() == 2)
          this.specs[i + 1] = null; 
        return;
      } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
        throw new IllegalArgumentException("spec.getReg() out of range");
      } 
    } 
    throw new NullPointerException("spec == null");
  }
  
  public void putAll(RegisterSpecSet paramRegisterSpecSet) {
    int j = paramRegisterSpecSet.getMaxSize();
    for (int i = 0; i < j; i++) {
      RegisterSpec registerSpec = paramRegisterSpecSet.get(i);
      if (registerSpec != null)
        put(registerSpec); 
    } 
  }
  
  public void remove(RegisterSpec paramRegisterSpec) {
    try {
      this.specs[paramRegisterSpec.getReg()] = null;
      this.size = -1;
      return;
    } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
      throw new IllegalArgumentException("bogus reg");
    } 
  }
  
  public int size() {
    int j = this.size;
    int i = j;
    if (j < 0) {
      int k = this.specs.length;
      i = 0;
      j = 0;
      while (j < k) {
        int m = i;
        if (this.specs[j] != null)
          m = i + 1; 
        j++;
        i = m;
      } 
      this.size = i;
    } 
    return i;
  }
  
  public String toString() {
    int j = this.specs.length;
    StringBuffer stringBuffer = new StringBuffer(j * 25);
    stringBuffer.append('{');
    int i = 0;
    for (boolean bool = false; i < j; bool = bool1) {
      RegisterSpec registerSpec = this.specs[i];
      boolean bool1 = bool;
      if (registerSpec != null) {
        if (bool) {
          stringBuffer.append(", ");
        } else {
          bool = true;
        } 
        stringBuffer.append(registerSpec);
        bool1 = bool;
      } 
      i++;
    } 
    stringBuffer.append('}');
    return stringBuffer.toString();
  }
  
  public RegisterSpecSet withOffset(int paramInt) {
    int j = this.specs.length;
    RegisterSpecSet registerSpecSet = new RegisterSpecSet(j + paramInt);
    for (int i = 0; i < j; i++) {
      RegisterSpec registerSpec = this.specs[i];
      if (registerSpec != null)
        registerSpecSet.put(registerSpec.withOffset(paramInt)); 
    } 
    registerSpecSet.size = this.size;
    if (isImmutable())
      registerSpecSet.setImmutable(); 
    return registerSpecSet;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\code\RegisterSpecSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
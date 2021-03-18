package org.firstinspires.ftc.robotcore.internal.android.dx.rop.type;

import java.util.HashMap;

public final class Prototype implements Comparable<Prototype> {
  private static final HashMap<String, Prototype> internTable = new HashMap<String, Prototype>(500);
  
  private final String descriptor;
  
  private StdTypeList parameterFrameTypes;
  
  private final StdTypeList parameterTypes;
  
  private final Type returnType;
  
  private Prototype(String paramString, Type paramType, StdTypeList paramStdTypeList) {
    if (paramString != null) {
      if (paramType != null) {
        if (paramStdTypeList != null) {
          this.descriptor = paramString;
          this.returnType = paramType;
          this.parameterTypes = paramStdTypeList;
          this.parameterFrameTypes = null;
          return;
        } 
        throw new NullPointerException("parameterTypes == null");
      } 
      throw new NullPointerException("returnType == null");
    } 
    throw new NullPointerException("descriptor == null");
  }
  
  public static Prototype intern(String paramString) {
    if (paramString != null) {
      HashMap<String, Prototype> hashMap;
      Type[] arrayOfType;
      synchronized (internTable) {
        Prototype prototype = internTable.get(paramString);
        if (prototype != null)
          return prototype; 
        arrayOfType = makeParameterArray(paramString);
        byte b = 0;
        int j = 0;
        for (int i = 1;; i = k) {
          int k = paramString.charAt(i);
          if (k == 41) {
            Type type = Type.internReturnType(paramString.substring(i + 1));
            StdTypeList stdTypeList = new StdTypeList(j);
            for (i = b; i < j; i++)
              stdTypeList.set(i, arrayOfType[i]); 
            return putIntern(new Prototype(paramString, type, stdTypeList));
          } 
          int m = i;
          while (k == 91)
            k = paramString.charAt(++m); 
          if (k == 76) {
            int n = paramString.indexOf(';', m);
            if (n != -1) {
              n++;
            } else {
              throw new IllegalArgumentException("bad descriptor");
            } 
          } else {
            k = m + 1;
          } 
          arrayOfType[j] = Type.intern(paramString.substring(i, k));
          j++;
        } 
      } 
    } 
    throw new NullPointerException("descriptor == null");
  }
  
  public static Prototype intern(String paramString, Type paramType, boolean paramBoolean1, boolean paramBoolean2) {
    Prototype prototype = intern(paramString);
    if (paramBoolean1)
      return prototype; 
    Type type = paramType;
    if (paramBoolean2)
      type = paramType.asUninitialized(2147483647); 
    return prototype.withFirstParameter(type);
  }
  
  public static Prototype internInts(Type paramType, int paramInt) {
    StringBuffer stringBuffer = new StringBuffer(100);
    stringBuffer.append('(');
    for (int i = 0; i < paramInt; i++)
      stringBuffer.append('I'); 
    stringBuffer.append(')');
    stringBuffer.append(paramType.getDescriptor());
    return intern(stringBuffer.toString());
  }
  
  private static Type[] makeParameterArray(String paramString) {
    int i = paramString.length();
    byte b = 0;
    if (paramString.charAt(0) == '(') {
      int m;
      int k = 0;
      int j = 1;
      while (true) {
        m = b;
        if (j < i) {
          char c = paramString.charAt(j);
          if (c == ')') {
            m = j;
            break;
          } 
          m = k;
          if (c >= 'A') {
            m = k;
            if (c <= 'Z')
              m = k + 1; 
          } 
          j++;
          k = m;
          continue;
        } 
        break;
      } 
      if (m != 0 && m != i - 1) {
        if (paramString.indexOf(')', m + 1) == -1)
          return new Type[k]; 
        throw new IllegalArgumentException("bad descriptor");
      } 
      throw new IllegalArgumentException("bad descriptor");
    } 
    throw new IllegalArgumentException("bad descriptor");
  }
  
  private static Prototype putIntern(Prototype paramPrototype) {
    synchronized (internTable) {
      String str = paramPrototype.getDescriptor();
      Prototype prototype = internTable.get(str);
      if (prototype != null)
        return prototype; 
      internTable.put(str, paramPrototype);
      return paramPrototype;
    } 
  }
  
  public int compareTo(Prototype paramPrototype) {
    if (this == paramPrototype)
      return 0; 
    int i = this.returnType.compareTo(paramPrototype.returnType);
    if (i != 0)
      return i; 
    int j = this.parameterTypes.size();
    int k = paramPrototype.parameterTypes.size();
    int m = Math.min(j, k);
    for (i = 0; i < m; i++) {
      int n = this.parameterTypes.get(i).compareTo(paramPrototype.parameterTypes.get(i));
      if (n != 0)
        return n; 
    } 
    return (j < k) ? -1 : ((j > k) ? 1 : 0);
  }
  
  public boolean equals(Object paramObject) {
    return (this == paramObject) ? true : (!(paramObject instanceof Prototype) ? false : this.descriptor.equals(((Prototype)paramObject).descriptor));
  }
  
  public String getDescriptor() {
    return this.descriptor;
  }
  
  public StdTypeList getParameterFrameTypes() {
    if (this.parameterFrameTypes == null) {
      StdTypeList stdTypeList1;
      int j = this.parameterTypes.size();
      StdTypeList stdTypeList2 = new StdTypeList(j);
      int i = 0;
      boolean bool = false;
      while (i < j) {
        Type type2 = this.parameterTypes.get(i);
        Type type1 = type2;
        if (type2.isIntlike()) {
          type1 = Type.INT;
          bool = true;
        } 
        stdTypeList2.set(i, type1);
        i++;
      } 
      if (bool) {
        stdTypeList1 = stdTypeList2;
      } else {
        stdTypeList1 = this.parameterTypes;
      } 
      this.parameterFrameTypes = stdTypeList1;
    } 
    return this.parameterFrameTypes;
  }
  
  public StdTypeList getParameterTypes() {
    return this.parameterTypes;
  }
  
  public Type getReturnType() {
    return this.returnType;
  }
  
  public int hashCode() {
    return this.descriptor.hashCode();
  }
  
  public String toString() {
    return this.descriptor;
  }
  
  public Prototype withFirstParameter(Type paramType) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("(");
    stringBuilder.append(paramType.getDescriptor());
    stringBuilder.append(this.descriptor.substring(1));
    String str = stringBuilder.toString();
    StdTypeList stdTypeList = this.parameterTypes.withFirst(paramType);
    stdTypeList.setImmutable();
    return putIntern(new Prototype(str, this.returnType, stdTypeList));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\type\Prototype.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
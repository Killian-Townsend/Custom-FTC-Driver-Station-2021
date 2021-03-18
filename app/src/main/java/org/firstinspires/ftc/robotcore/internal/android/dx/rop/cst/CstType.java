package org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst;

import java.util.HashMap;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;

public final class CstType extends TypedConstant {
  public static final CstType BOOLEAN;
  
  public static final CstType BOOLEAN_ARRAY;
  
  public static final CstType BYTE;
  
  public static final CstType BYTE_ARRAY;
  
  public static final CstType CHARACTER;
  
  public static final CstType CHAR_ARRAY;
  
  public static final CstType DOUBLE;
  
  public static final CstType DOUBLE_ARRAY;
  
  public static final CstType FLOAT;
  
  public static final CstType FLOAT_ARRAY;
  
  public static final CstType INTEGER;
  
  public static final CstType INT_ARRAY;
  
  public static final CstType LONG;
  
  public static final CstType LONG_ARRAY;
  
  public static final CstType OBJECT;
  
  public static final CstType SHORT;
  
  public static final CstType SHORT_ARRAY;
  
  public static final CstType VOID;
  
  private static final HashMap<Type, CstType> interns = new HashMap<Type, CstType>(100);
  
  private CstString descriptor;
  
  private final Type type;
  
  static {
    OBJECT = intern(Type.OBJECT);
    BOOLEAN = intern(Type.BOOLEAN_CLASS);
    BYTE = intern(Type.BYTE_CLASS);
    CHARACTER = intern(Type.CHARACTER_CLASS);
    DOUBLE = intern(Type.DOUBLE_CLASS);
    FLOAT = intern(Type.FLOAT_CLASS);
    LONG = intern(Type.LONG_CLASS);
    INTEGER = intern(Type.INTEGER_CLASS);
    SHORT = intern(Type.SHORT_CLASS);
    VOID = intern(Type.VOID_CLASS);
    BOOLEAN_ARRAY = intern(Type.BOOLEAN_ARRAY);
    BYTE_ARRAY = intern(Type.BYTE_ARRAY);
    CHAR_ARRAY = intern(Type.CHAR_ARRAY);
    DOUBLE_ARRAY = intern(Type.DOUBLE_ARRAY);
    FLOAT_ARRAY = intern(Type.FLOAT_ARRAY);
    LONG_ARRAY = intern(Type.LONG_ARRAY);
    INT_ARRAY = intern(Type.INT_ARRAY);
    SHORT_ARRAY = intern(Type.SHORT_ARRAY);
  }
  
  public CstType(Type paramType) {
    if (paramType != null) {
      if (paramType != Type.KNOWN_NULL) {
        this.type = paramType;
        this.descriptor = null;
        return;
      } 
      throw new UnsupportedOperationException("KNOWN_NULL is not representable");
    } 
    throw new NullPointerException("type == null");
  }
  
  public static CstType forBoxedPrimitiveType(Type paramType) {
    StringBuilder stringBuilder;
    switch (paramType.getBasicType()) {
      default:
        stringBuilder = new StringBuilder();
        stringBuilder.append("not primitive: ");
        stringBuilder.append(paramType);
        throw new IllegalArgumentException(stringBuilder.toString());
      case 8:
        return SHORT;
      case 7:
        return LONG;
      case 6:
        return INTEGER;
      case 5:
        return FLOAT;
      case 4:
        return DOUBLE;
      case 3:
        return CHARACTER;
      case 2:
        return BYTE;
      case 1:
        return BOOLEAN;
      case 0:
        break;
    } 
    return VOID;
  }
  
  public static CstType intern(Type paramType) {
    synchronized (interns) {
      CstType cstType2 = interns.get(paramType);
      CstType cstType1 = cstType2;
      if (cstType2 == null) {
        cstType1 = new CstType(paramType);
        interns.put(paramType, cstType1);
      } 
      return cstType1;
    } 
  }
  
  protected int compareTo0(Constant paramConstant) {
    return this.type.getDescriptor().compareTo(((CstType)paramConstant).type.getDescriptor());
  }
  
  public boolean equals(Object paramObject) {
    boolean bool1 = paramObject instanceof CstType;
    boolean bool = false;
    if (!bool1)
      return false; 
    if (this.type == ((CstType)paramObject).type)
      bool = true; 
    return bool;
  }
  
  public Type getClassType() {
    return this.type;
  }
  
  public CstString getDescriptor() {
    if (this.descriptor == null)
      this.descriptor = new CstString(this.type.getDescriptor()); 
    return this.descriptor;
  }
  
  public String getPackageName() {
    String str = getDescriptor().getString();
    int i = str.lastIndexOf('/');
    int j = str.lastIndexOf('[');
    return (i == -1) ? "default" : str.substring(j + 2, i).replace('/', '.');
  }
  
  public Type getType() {
    return Type.CLASS;
  }
  
  public int hashCode() {
    return this.type.hashCode();
  }
  
  public boolean isCategory2() {
    return false;
  }
  
  public String toHuman() {
    return this.type.toHuman();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("type{");
    stringBuilder.append(toHuman());
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
  
  public String typeName() {
    return "type";
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\cst\CstType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
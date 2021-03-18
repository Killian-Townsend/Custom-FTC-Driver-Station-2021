package org.firstinspires.ftc.robotcore.internal.android.dx.rop.code;

import java.util.HashMap;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeBearer;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.ToHuman;

public final class RegisterSpec implements TypeBearer, ToHuman, Comparable<RegisterSpec> {
  public static final String PREFIX = "v";
  
  private static final ForComparison theInterningItem;
  
  private static final HashMap<Object, RegisterSpec> theInterns = new HashMap<Object, RegisterSpec>(1000);
  
  private final LocalItem local;
  
  private final int reg;
  
  private final TypeBearer type;
  
  static {
    theInterningItem = new ForComparison();
  }
  
  private RegisterSpec(int paramInt, TypeBearer paramTypeBearer, LocalItem paramLocalItem) {
    if (paramInt >= 0) {
      if (paramTypeBearer != null) {
        this.reg = paramInt;
        this.type = paramTypeBearer;
        this.local = paramLocalItem;
        return;
      } 
      throw new NullPointerException("type == null");
    } 
    throw new IllegalArgumentException("reg < 0");
  }
  
  private boolean equals(int paramInt, TypeBearer paramTypeBearer, LocalItem paramLocalItem) {
    if (this.reg == paramInt && this.type.equals(paramTypeBearer)) {
      LocalItem localItem = this.local;
      if (localItem == paramLocalItem || (localItem != null && localItem.equals(paramLocalItem)))
        return true; 
    } 
    return false;
  }
  
  private static int hashCodeOf(int paramInt, TypeBearer paramTypeBearer, LocalItem paramLocalItem) {
    byte b;
    if (paramLocalItem != null) {
      b = paramLocalItem.hashCode();
    } else {
      b = 0;
    } 
    return (b * 31 + paramTypeBearer.hashCode()) * 31 + paramInt;
  }
  
  private static RegisterSpec intern(int paramInt, TypeBearer paramTypeBearer, LocalItem paramLocalItem) {
    synchronized (theInterns) {
      theInterningItem.set(paramInt, paramTypeBearer, paramLocalItem);
      paramTypeBearer = theInterns.get(theInterningItem);
      if (paramTypeBearer != null)
        return (RegisterSpec)paramTypeBearer; 
      paramTypeBearer = theInterningItem.toRegisterSpec();
      theInterns.put(paramTypeBearer, paramTypeBearer);
      return (RegisterSpec)paramTypeBearer;
    } 
  }
  
  public static RegisterSpec make(int paramInt, TypeBearer paramTypeBearer) {
    return intern(paramInt, paramTypeBearer, null);
  }
  
  public static RegisterSpec make(int paramInt, TypeBearer paramTypeBearer, LocalItem paramLocalItem) {
    if (paramLocalItem != null)
      return intern(paramInt, paramTypeBearer, paramLocalItem); 
    throw new NullPointerException("local  == null");
  }
  
  public static RegisterSpec makeLocalOptional(int paramInt, TypeBearer paramTypeBearer, LocalItem paramLocalItem) {
    return intern(paramInt, paramTypeBearer, paramLocalItem);
  }
  
  public static String regString(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("v");
    stringBuilder.append(paramInt);
    return stringBuilder.toString();
  }
  
  private String toString0(boolean paramBoolean) {
    StringBuffer stringBuffer = new StringBuffer(40);
    stringBuffer.append(regString());
    stringBuffer.append(":");
    LocalItem localItem = this.local;
    if (localItem != null)
      stringBuffer.append(localItem.toString()); 
    Type type = this.type.getType();
    stringBuffer.append(type);
    if (type != this.type) {
      stringBuffer.append("=");
      if (paramBoolean) {
        TypeBearer typeBearer = this.type;
        if (typeBearer instanceof CstString) {
          stringBuffer.append(((CstString)typeBearer).toQuoted());
          return stringBuffer.toString();
        } 
      } 
      if (paramBoolean) {
        TypeBearer typeBearer = this.type;
        if (typeBearer instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant) {
          stringBuffer.append(typeBearer.toHuman());
          return stringBuffer.toString();
        } 
      } 
      stringBuffer.append(this.type);
    } 
    return stringBuffer.toString();
  }
  
  public int compareTo(RegisterSpec paramRegisterSpec) {
    int i = this.reg;
    int j = paramRegisterSpec.reg;
    byte b = -1;
    if (i < j)
      return -1; 
    if (i > j)
      return 1; 
    i = this.type.getType().compareTo(paramRegisterSpec.type.getType());
    if (i != 0)
      return i; 
    LocalItem localItem2 = this.local;
    if (localItem2 == null) {
      if (paramRegisterSpec.local == null)
        b = 0; 
      return b;
    } 
    LocalItem localItem1 = paramRegisterSpec.local;
    return (localItem1 == null) ? 1 : localItem2.compareTo(localItem1);
  }
  
  public boolean equals(Object paramObject) {
    if (!(paramObject instanceof RegisterSpec)) {
      if (paramObject instanceof ForComparison) {
        paramObject = paramObject;
        return equals(((ForComparison)paramObject).reg, ((ForComparison)paramObject).type, ((ForComparison)paramObject).local);
      } 
      return false;
    } 
    paramObject = paramObject;
    return equals(((RegisterSpec)paramObject).reg, ((RegisterSpec)paramObject).type, ((RegisterSpec)paramObject).local);
  }
  
  public boolean equalsUsingSimpleType(RegisterSpec paramRegisterSpec) {
    boolean bool1 = matchesVariable(paramRegisterSpec);
    boolean bool = false;
    if (!bool1)
      return false; 
    if (this.reg == paramRegisterSpec.reg)
      bool = true; 
    return bool;
  }
  
  public final int getBasicFrameType() {
    return this.type.getBasicFrameType();
  }
  
  public final int getBasicType() {
    return this.type.getBasicType();
  }
  
  public int getCategory() {
    return this.type.getType().getCategory();
  }
  
  public TypeBearer getFrameType() {
    return this.type.getFrameType();
  }
  
  public LocalItem getLocalItem() {
    return this.local;
  }
  
  public int getNextReg() {
    return this.reg + getCategory();
  }
  
  public int getReg() {
    return this.reg;
  }
  
  public Type getType() {
    return this.type.getType();
  }
  
  public TypeBearer getTypeBearer() {
    return this.type;
  }
  
  public int hashCode() {
    return hashCodeOf(this.reg, this.type, this.local);
  }
  
  public RegisterSpec intersect(RegisterSpec paramRegisterSpec, boolean paramBoolean) {
    if (this == paramRegisterSpec)
      return this; 
    if (paramRegisterSpec != null) {
      TypeBearer typeBearer;
      if (this.reg != paramRegisterSpec.getReg())
        return null; 
      LocalItem localItem = this.local;
      if (localItem == null || !localItem.equals(paramRegisterSpec.getLocalItem())) {
        localItem = null;
      } else {
        localItem = this.local;
      } 
      if (localItem == this.local) {
        i = 1;
      } else {
        i = 0;
      } 
      if (paramBoolean && !i)
        return null; 
      Type type = getType();
      if (type != paramRegisterSpec.getType())
        return null; 
      if (this.type.equals(paramRegisterSpec.getTypeBearer()))
        typeBearer = this.type; 
      if (typeBearer == this.type && i)
        return this; 
      int i = this.reg;
      return (localItem == null) ? make(i, typeBearer) : make(i, typeBearer, localItem);
    } 
    return null;
  }
  
  public boolean isCategory1() {
    return this.type.getType().isCategory1();
  }
  
  public boolean isCategory2() {
    return this.type.getType().isCategory2();
  }
  
  public final boolean isConstant() {
    return false;
  }
  
  public boolean isEvenRegister() {
    return ((getReg() & 0x1) == 0);
  }
  
  public boolean matchesVariable(RegisterSpec paramRegisterSpec) {
    boolean bool = false;
    if (paramRegisterSpec == null)
      return false; 
    null = bool;
    if (this.type.getType().equals(paramRegisterSpec.type.getType())) {
      LocalItem localItem2 = this.local;
      LocalItem localItem1 = paramRegisterSpec.local;
      if (localItem2 != localItem1) {
        null = bool;
        if (localItem2 != null) {
          null = bool;
          if (localItem2.equals(localItem1))
            return true; 
        } 
        return null;
      } 
    } else {
      return null;
    } 
    return true;
  }
  
  public String regString() {
    return regString(this.reg);
  }
  
  public String toHuman() {
    return toString0(true);
  }
  
  public String toString() {
    return toString0(false);
  }
  
  public RegisterSpec withLocalItem(LocalItem paramLocalItem) {
    LocalItem localItem = this.local;
    return (localItem != paramLocalItem) ? ((localItem != null && localItem.equals(paramLocalItem)) ? this : makeLocalOptional(this.reg, this.type, paramLocalItem)) : this;
  }
  
  public RegisterSpec withOffset(int paramInt) {
    return (paramInt == 0) ? this : withReg(this.reg + paramInt);
  }
  
  public RegisterSpec withReg(int paramInt) {
    return (this.reg == paramInt) ? this : makeLocalOptional(paramInt, this.type, this.local);
  }
  
  public RegisterSpec withSimpleType() {
    Type type1;
    TypeBearer typeBearer = this.type;
    if (typeBearer instanceof Type) {
      type1 = (Type)typeBearer;
    } else {
      type1 = typeBearer.getType();
    } 
    Type type2 = type1;
    if (type1.isUninitialized())
      type2 = type1.getInitializedType(); 
    return (type2 == typeBearer) ? this : makeLocalOptional(this.reg, (TypeBearer)type2, this.local);
  }
  
  public RegisterSpec withType(TypeBearer paramTypeBearer) {
    return makeLocalOptional(this.reg, paramTypeBearer, this.local);
  }
  
  private static class ForComparison {
    private LocalItem local;
    
    private int reg;
    
    private TypeBearer type;
    
    private ForComparison() {}
    
    public boolean equals(Object param1Object) {
      return !(param1Object instanceof RegisterSpec) ? false : ((RegisterSpec)param1Object).equals(this.reg, this.type, this.local);
    }
    
    public int hashCode() {
      return RegisterSpec.hashCodeOf(this.reg, this.type, this.local);
    }
    
    public void set(int param1Int, TypeBearer param1TypeBearer, LocalItem param1LocalItem) {
      this.reg = param1Int;
      this.type = param1TypeBearer;
      this.local = param1LocalItem;
    }
    
    public RegisterSpec toRegisterSpec() {
      return new RegisterSpec(this.reg, this.type, this.local);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\code\RegisterSpec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
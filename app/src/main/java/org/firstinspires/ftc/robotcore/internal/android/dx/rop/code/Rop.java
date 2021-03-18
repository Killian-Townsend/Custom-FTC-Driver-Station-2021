package org.firstinspires.ftc.robotcore.internal.android.dx.rop.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.StdTypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public final class Rop {
  public static final int BRANCH_GOTO = 3;
  
  public static final int BRANCH_IF = 4;
  
  public static final int BRANCH_MAX = 6;
  
  public static final int BRANCH_MIN = 1;
  
  public static final int BRANCH_NONE = 1;
  
  public static final int BRANCH_RETURN = 2;
  
  public static final int BRANCH_SWITCH = 5;
  
  public static final int BRANCH_THROW = 6;
  
  private final int branchingness;
  
  private final TypeList exceptions;
  
  private final boolean isCallLike;
  
  private final String nickname;
  
  private final int opcode;
  
  private final Type result;
  
  private final TypeList sources;
  
  public Rop(int paramInt1, Type paramType, TypeList paramTypeList, int paramInt2, String paramString) {
    this(paramInt1, paramType, paramTypeList, (TypeList)StdTypeList.EMPTY, paramInt2, false, paramString);
  }
  
  public Rop(int paramInt, Type paramType, TypeList paramTypeList, String paramString) {
    this(paramInt, paramType, paramTypeList, (TypeList)StdTypeList.EMPTY, 1, false, paramString);
  }
  
  public Rop(int paramInt1, Type paramType, TypeList paramTypeList1, TypeList paramTypeList2, int paramInt2, String paramString) {
    this(paramInt1, paramType, paramTypeList1, paramTypeList2, paramInt2, false, paramString);
  }
  
  public Rop(int paramInt1, Type paramType, TypeList paramTypeList1, TypeList paramTypeList2, int paramInt2, boolean paramBoolean, String paramString) {
    if (paramType != null) {
      if (paramTypeList1 != null) {
        if (paramTypeList2 != null) {
          if (paramInt2 >= 1 && paramInt2 <= 6) {
            if (paramTypeList2.size() == 0 || paramInt2 == 6) {
              this.opcode = paramInt1;
              this.result = paramType;
              this.sources = paramTypeList1;
              this.exceptions = paramTypeList2;
              this.branchingness = paramInt2;
              this.isCallLike = paramBoolean;
              this.nickname = paramString;
              return;
            } 
            throw new IllegalArgumentException("exceptions / branchingness mismatch");
          } 
          throw new IllegalArgumentException("bogus branchingness");
        } 
        throw new NullPointerException("exceptions == null");
      } 
      throw new NullPointerException("sources == null");
    } 
    throw new NullPointerException("result == null");
  }
  
  public Rop(int paramInt, Type paramType, TypeList paramTypeList1, TypeList paramTypeList2, String paramString) {
    this(paramInt, paramType, paramTypeList1, paramTypeList2, 6, false, paramString);
  }
  
  public Rop(int paramInt, TypeList paramTypeList1, TypeList paramTypeList2) {
    this(paramInt, Type.VOID, paramTypeList1, paramTypeList2, 6, true, null);
  }
  
  public final boolean canThrow() {
    return (this.exceptions.size() != 0);
  }
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (!(paramObject instanceof Rop))
      return false; 
    paramObject = paramObject;
    return (this.opcode == ((Rop)paramObject).opcode && this.branchingness == ((Rop)paramObject).branchingness && this.result == ((Rop)paramObject).result && this.sources.equals(((Rop)paramObject).sources) && this.exceptions.equals(((Rop)paramObject).exceptions));
  }
  
  public int getBranchingness() {
    return this.branchingness;
  }
  
  public TypeList getExceptions() {
    return this.exceptions;
  }
  
  public String getNickname() {
    String str = this.nickname;
    return (str != null) ? str : toString();
  }
  
  public int getOpcode() {
    return this.opcode;
  }
  
  public Type getResult() {
    return this.result;
  }
  
  public TypeList getSources() {
    return this.sources;
  }
  
  public int hashCode() {
    return (((this.opcode * 31 + this.branchingness) * 31 + this.result.hashCode()) * 31 + this.sources.hashCode()) * 31 + this.exceptions.hashCode();
  }
  
  public boolean isCallLike() {
    return this.isCallLike;
  }
  
  public boolean isCommutative() {
    int i = this.opcode;
    if (i != 14 && i != 16)
      switch (i) {
        default:
          return false;
        case 20:
        case 21:
        case 22:
          break;
      }  
    return true;
  }
  
  public String toString() {
    StringBuffer stringBuffer = new StringBuffer(40);
    stringBuffer.append("Rop{");
    stringBuffer.append(RegOps.opName(this.opcode));
    if (this.result != Type.VOID) {
      stringBuffer.append(" ");
      stringBuffer.append(this.result);
    } else {
      stringBuffer.append(" .");
    } 
    stringBuffer.append(" <-");
    int i = this.sources.size();
    byte b = 0;
    if (i == 0) {
      stringBuffer.append(" .");
    } else {
      for (int j = 0; j < i; j++) {
        stringBuffer.append(' ');
        stringBuffer.append(this.sources.getType(j));
      } 
    } 
    if (this.isCallLike)
      stringBuffer.append(" call"); 
    i = this.exceptions.size();
    if (i != 0) {
      stringBuffer.append(" throws");
      for (int j = b; j < i; j++) {
        stringBuffer.append(' ');
        if (this.exceptions.getType(j) == Type.THROWABLE) {
          stringBuffer.append("<any>");
        } else {
          stringBuffer.append(this.exceptions.getType(j));
        } 
      } 
    } else {
      int j = this.branchingness;
      if (j != 1) {
        if (j != 2) {
          if (j != 3) {
            if (j != 4) {
              if (j != 5) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(" ");
                stringBuilder.append(Hex.u1(this.branchingness));
                stringBuffer.append(stringBuilder.toString());
              } else {
                stringBuffer.append(" switches");
              } 
            } else {
              stringBuffer.append(" ifs");
            } 
          } else {
            stringBuffer.append(" gotos");
          } 
        } else {
          stringBuffer.append(" returns");
        } 
      } else {
        stringBuffer.append(" flows");
      } 
    } 
    stringBuffer.append('}');
    return stringBuffer.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\code\Rop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
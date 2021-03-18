package org.firstinspires.ftc.robotcore.internal.android.dx.cf.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeBearer;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public final class Merger {
  public static boolean isPossiblyAssignableFrom(TypeBearer paramTypeBearer1, TypeBearer paramTypeBearer2) {
    Type type1 = paramTypeBearer1.getType();
    Type type2 = paramTypeBearer2.getType();
    boolean bool = type1.equals(type2);
    boolean bool1 = true;
    if (bool)
      return true; 
    int j = type1.getBasicType();
    int k = type2.getBasicType();
    int i = j;
    if (j == 10) {
      type1 = Type.OBJECT;
      i = 9;
    } 
    j = k;
    if (k == 10) {
      type2 = Type.OBJECT;
      j = 9;
    } 
    if (i != 9 || j != 9)
      return (type1.isIntlike() && type2.isIntlike()); 
    if (type1 == Type.KNOWN_NULL)
      return false; 
    if (type2 == Type.KNOWN_NULL)
      return true; 
    if (type1 == Type.OBJECT)
      return true; 
    if (type1.isArray()) {
      Type type4;
      Type type3 = type2;
      if (!type2.isArray())
        return false; 
      while (true) {
        type2 = type1.getComponentType();
        type4 = type3.getComponentType();
        if (type2.isArray()) {
          type1 = type2;
          type3 = type4;
          if (!type4.isArray())
            break; 
          continue;
        } 
        break;
      } 
      return isPossiblyAssignableFrom((TypeBearer)type2, (TypeBearer)type4);
    } 
    bool = bool1;
    if (type2.isArray()) {
      bool = bool1;
      if (type1 != Type.SERIALIZABLE) {
        if (type1 == Type.CLONEABLE)
          return true; 
        bool = false;
      } 
    } 
    return bool;
  }
  
  public static OneLocalsArray mergeLocals(OneLocalsArray paramOneLocalsArray1, OneLocalsArray paramOneLocalsArray2) {
    if (paramOneLocalsArray1 == paramOneLocalsArray2)
      return paramOneLocalsArray1; 
    int i = paramOneLocalsArray1.getMaxLocals();
    OneLocalsArray oneLocalsArray = null;
    if (paramOneLocalsArray2.getMaxLocals() == i) {
      int j = 0;
      while (j < i) {
        TypeBearer typeBearer2 = paramOneLocalsArray1.getOrNull(j);
        TypeBearer typeBearer1 = mergeType(typeBearer2, paramOneLocalsArray2.getOrNull(j));
        OneLocalsArray oneLocalsArray1 = oneLocalsArray;
        if (typeBearer1 != typeBearer2) {
          oneLocalsArray1 = oneLocalsArray;
          if (oneLocalsArray == null)
            oneLocalsArray1 = paramOneLocalsArray1.copy(); 
          if (typeBearer1 == null) {
            oneLocalsArray1.invalidate(j);
          } else {
            oneLocalsArray1.set(j, typeBearer1);
          } 
        } 
        j++;
        oneLocalsArray = oneLocalsArray1;
      } 
      if (oneLocalsArray == null)
        return paramOneLocalsArray1; 
      oneLocalsArray.setImmutable();
      return oneLocalsArray;
    } 
    throw new SimException("mismatched maxLocals values");
  }
  
  public static ExecutionStack mergeStack(ExecutionStack paramExecutionStack1, ExecutionStack paramExecutionStack2) {
    if (paramExecutionStack1 == paramExecutionStack2)
      return paramExecutionStack1; 
    int i = paramExecutionStack1.size();
    ExecutionStack executionStack = null;
    if (paramExecutionStack2.size() == i) {
      int j = 0;
      while (true) {
        StringBuilder stringBuilder;
        ExecutionStack executionStack1;
        if (j < i) {
          TypeBearer typeBearer1 = paramExecutionStack1.peek(j);
          TypeBearer typeBearer2 = paramExecutionStack2.peek(j);
          TypeBearer typeBearer3 = mergeType(typeBearer1, typeBearer2);
          executionStack1 = executionStack;
          if (typeBearer3 != typeBearer1) {
            executionStack1 = executionStack;
            if (executionStack == null)
              executionStack1 = paramExecutionStack1.copy(); 
            if (typeBearer3 != null) {
              try {
                executionStack1.change(j, typeBearer3);
                j++;
                executionStack = executionStack1;
              } catch (SimException simException) {
                StringBuilder stringBuilder1 = new StringBuilder();
                stringBuilder1.append("...while merging stack[");
                stringBuilder1.append(Hex.u2(j));
                stringBuilder1.append("]");
                simException.addContext(stringBuilder1.toString());
                throw simException;
              } 
              continue;
            } 
            stringBuilder = new StringBuilder();
            stringBuilder.append("incompatible: ");
            stringBuilder.append(typeBearer1);
            stringBuilder.append(", ");
            stringBuilder.append(typeBearer2);
            throw new SimException(stringBuilder.toString());
          } 
        } else {
          if (executionStack == null)
            return (ExecutionStack)stringBuilder; 
          executionStack.setImmutable();
          return executionStack;
        } 
        j++;
        executionStack = executionStack1;
      } 
    } 
    throw new SimException("mismatched stack depths");
  }
  
  public static TypeBearer mergeType(TypeBearer paramTypeBearer1, TypeBearer paramTypeBearer2) {
    TypeBearer typeBearer;
    if (paramTypeBearer1 != null) {
      if (paramTypeBearer1.equals(paramTypeBearer2))
        return paramTypeBearer1; 
      if (paramTypeBearer2 == null)
        return null; 
      Type type1 = paramTypeBearer1.getType();
      Type type2 = paramTypeBearer2.getType();
      if (type1 == type2)
        return (TypeBearer)type1; 
      if (type1.isReference() && type2.isReference()) {
        if (type1 == Type.KNOWN_NULL)
          return (TypeBearer)type2; 
        if (type2 == Type.KNOWN_NULL)
          return (TypeBearer)type1; 
        if (type1.isArray() && type2.isArray()) {
          typeBearer = mergeType((TypeBearer)type1.getComponentType(), (TypeBearer)type2.getComponentType());
          return (TypeBearer)((typeBearer == null) ? Type.OBJECT : ((Type)typeBearer).getArrayType());
        } 
        return (TypeBearer)Type.OBJECT;
      } 
      return (TypeBearer)((typeBearer.isIntlike() && type2.isIntlike()) ? Type.INT : null);
    } 
    return typeBearer;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\code\Merger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
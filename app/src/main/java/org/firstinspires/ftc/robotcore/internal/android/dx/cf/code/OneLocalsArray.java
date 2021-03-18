package org.firstinspires.ftc.robotcore.internal.android.dx.cf.code;

import org.firstinspires.ftc.robotcore.internal.android.dex.util.ExceptionWithContext;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeBearer;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public class OneLocalsArray extends LocalsArray {
  private final TypeBearer[] locals;
  
  public OneLocalsArray(int paramInt) {
    super(bool);
    boolean bool;
    this.locals = new TypeBearer[paramInt];
  }
  
  private static TypeBearer throwSimException(int paramInt, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("local ");
    stringBuilder.append(Hex.u2(paramInt));
    stringBuilder.append(": ");
    stringBuilder.append(paramString);
    throw new SimException(stringBuilder.toString());
  }
  
  public void annotate(ExceptionWithContext paramExceptionWithContext) {
    int i = 0;
    while (true) {
      TypeBearer[] arrayOfTypeBearer = this.locals;
      if (i < arrayOfTypeBearer.length) {
        String str;
        TypeBearer typeBearer = arrayOfTypeBearer[i];
        if (typeBearer == null) {
          str = "<invalid>";
        } else {
          str = str.toString();
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("locals[");
        stringBuilder.append(Hex.u2(i));
        stringBuilder.append("]: ");
        stringBuilder.append(str);
        paramExceptionWithContext.addContext(stringBuilder.toString());
        i++;
        continue;
      } 
      break;
    } 
  }
  
  public OneLocalsArray copy() {
    OneLocalsArray oneLocalsArray = new OneLocalsArray(this.locals.length);
    TypeBearer[] arrayOfTypeBearer = this.locals;
    System.arraycopy(arrayOfTypeBearer, 0, oneLocalsArray.locals, 0, arrayOfTypeBearer.length);
    return oneLocalsArray;
  }
  
  public TypeBearer get(int paramInt) {
    TypeBearer typeBearer = this.locals[paramInt];
    return (typeBearer == null) ? throwSimException(paramInt, "invalid") : typeBearer;
  }
  
  public TypeBearer getCategory1(int paramInt) {
    TypeBearer typeBearer = get(paramInt);
    Type type = typeBearer.getType();
    return type.isUninitialized() ? throwSimException(paramInt, "uninitialized instance") : (type.isCategory2() ? throwSimException(paramInt, "category-2") : typeBearer);
  }
  
  public TypeBearer getCategory2(int paramInt) {
    TypeBearer typeBearer = get(paramInt);
    return typeBearer.getType().isCategory1() ? throwSimException(paramInt, "category-1") : typeBearer;
  }
  
  public int getMaxLocals() {
    return this.locals.length;
  }
  
  public TypeBearer getOrNull(int paramInt) {
    return this.locals[paramInt];
  }
  
  protected OneLocalsArray getPrimary() {
    return this;
  }
  
  public void invalidate(int paramInt) {
    throwIfImmutable();
    this.locals[paramInt] = null;
  }
  
  public void makeInitialized(Type paramType) {
    int j = this.locals.length;
    if (j == 0)
      return; 
    throwIfImmutable();
    Type type = paramType.getInitializedType();
    for (int i = 0; i < j; i++) {
      TypeBearer[] arrayOfTypeBearer = this.locals;
      if (arrayOfTypeBearer[i] == paramType)
        arrayOfTypeBearer[i] = (TypeBearer)type; 
    } 
  }
  
  public LocalsArray merge(LocalsArray paramLocalsArray) {
    return (paramLocalsArray instanceof OneLocalsArray) ? merge((OneLocalsArray)paramLocalsArray) : paramLocalsArray.merge(this);
  }
  
  public OneLocalsArray merge(OneLocalsArray paramOneLocalsArray) {
    try {
      return Merger.mergeLocals(this, paramOneLocalsArray);
    } catch (SimException simException) {
      simException.addContext("underlay locals:");
      annotate(simException);
      simException.addContext("overlay locals:");
      paramOneLocalsArray.annotate(simException);
      throw simException;
    } 
  }
  
  public LocalsArraySet mergeWithSubroutineCaller(LocalsArray paramLocalsArray, int paramInt) {
    return (new LocalsArraySet(getMaxLocals())).mergeWithSubroutineCaller(paramLocalsArray, paramInt);
  }
  
  public void set(int paramInt, TypeBearer paramTypeBearer) {
    throwIfImmutable();
    try {
      paramTypeBearer = paramTypeBearer.getFrameType();
      if (paramInt >= 0) {
        if (paramTypeBearer.getType().isCategory2())
          this.locals[paramInt + 1] = null; 
        TypeBearer[] arrayOfTypeBearer = this.locals;
        arrayOfTypeBearer[paramInt] = paramTypeBearer;
        if (paramInt != 0) {
          paramTypeBearer = arrayOfTypeBearer[--paramInt];
          if (paramTypeBearer != null && paramTypeBearer.getType().isCategory2())
            this.locals[paramInt] = null; 
        } 
        return;
      } 
      throw new IndexOutOfBoundsException("idx < 0");
    } catch (NullPointerException nullPointerException) {
      throw new NullPointerException("type == null");
    } 
  }
  
  public void set(RegisterSpec paramRegisterSpec) {
    set(paramRegisterSpec.getReg(), (TypeBearer)paramRegisterSpec);
  }
  
  public String toHuman() {
    StringBuilder stringBuilder = new StringBuilder();
    int i = 0;
    while (true) {
      TypeBearer[] arrayOfTypeBearer = this.locals;
      if (i < arrayOfTypeBearer.length) {
        String str;
        TypeBearer typeBearer = arrayOfTypeBearer[i];
        if (typeBearer == null) {
          str = "<invalid>";
        } else {
          str = str.toString();
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("locals[");
        stringBuilder1.append(Hex.u2(i));
        stringBuilder1.append("]: ");
        stringBuilder1.append(str);
        stringBuilder1.append("\n");
        stringBuilder.append(stringBuilder1.toString());
        i++;
        continue;
      } 
      return stringBuilder.toString();
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\code\OneLocalsArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
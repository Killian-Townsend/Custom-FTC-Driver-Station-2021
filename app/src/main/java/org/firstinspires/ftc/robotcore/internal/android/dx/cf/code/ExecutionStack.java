package org.firstinspires.ftc.robotcore.internal.android.dx.cf.code;

import org.firstinspires.ftc.robotcore.internal.android.dex.util.ExceptionWithContext;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeBearer;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.MutabilityControl;

public final class ExecutionStack extends MutabilityControl {
  private final boolean[] local;
  
  private final TypeBearer[] stack;
  
  private int stackPtr;
  
  public ExecutionStack(int paramInt) {
    super(bool);
    boolean bool;
    this.stack = new TypeBearer[paramInt];
    this.local = new boolean[paramInt];
    this.stackPtr = 0;
  }
  
  private static String stackElementString(TypeBearer paramTypeBearer) {
    return (paramTypeBearer == null) ? "<invalid>" : paramTypeBearer.toString();
  }
  
  private static TypeBearer throwSimException(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("stack: ");
    stringBuilder.append(paramString);
    throw new SimException(stringBuilder.toString());
  }
  
  public void annotate(ExceptionWithContext paramExceptionWithContext) {
    int j = this.stackPtr - 1;
    for (int i = 0; i <= j; i++) {
      String str;
      if (i == j) {
        str = "top0";
      } else {
        str = Hex.u2(j - i);
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("stack[");
      stringBuilder.append(str);
      stringBuilder.append("]: ");
      stringBuilder.append(stackElementString(this.stack[i]));
      paramExceptionWithContext.addContext(stringBuilder.toString());
    } 
  }
  
  public void change(int paramInt, TypeBearer paramTypeBearer) {
    throwIfImmutable();
    try {
      paramTypeBearer = paramTypeBearer.getFrameType();
      paramInt = this.stackPtr - paramInt - 1;
      TypeBearer typeBearer = this.stack[paramInt];
      if (typeBearer == null || typeBearer.getType().getCategory() != paramTypeBearer.getType().getCategory()) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("incompatible substitution: ");
        stringBuilder.append(stackElementString(typeBearer));
        stringBuilder.append(" -> ");
        stringBuilder.append(stackElementString(paramTypeBearer));
        throwSimException(stringBuilder.toString());
      } 
      this.stack[paramInt] = paramTypeBearer;
      return;
    } catch (NullPointerException nullPointerException) {
      throw new NullPointerException("type == null");
    } 
  }
  
  public void clear() {
    throwIfImmutable();
    for (int i = 0; i < this.stackPtr; i++) {
      this.stack[i] = null;
      this.local[i] = false;
    } 
    this.stackPtr = 0;
  }
  
  public ExecutionStack copy() {
    ExecutionStack executionStack = new ExecutionStack(this.stack.length);
    TypeBearer[] arrayOfTypeBearer = this.stack;
    System.arraycopy(arrayOfTypeBearer, 0, executionStack.stack, 0, arrayOfTypeBearer.length);
    boolean[] arrayOfBoolean = this.local;
    System.arraycopy(arrayOfBoolean, 0, executionStack.local, 0, arrayOfBoolean.length);
    executionStack.stackPtr = this.stackPtr;
    return executionStack;
  }
  
  public int getMaxStack() {
    return this.stack.length;
  }
  
  public void makeInitialized(Type paramType) {
    if (this.stackPtr == 0)
      return; 
    throwIfImmutable();
    Type type = paramType.getInitializedType();
    for (int i = 0; i < this.stackPtr; i++) {
      TypeBearer[] arrayOfTypeBearer = this.stack;
      if (arrayOfTypeBearer[i] == paramType)
        arrayOfTypeBearer[i] = (TypeBearer)type; 
    } 
  }
  
  public ExecutionStack merge(ExecutionStack paramExecutionStack) {
    try {
      return Merger.mergeStack(this, paramExecutionStack);
    } catch (SimException simException) {
      simException.addContext("underlay stack:");
      annotate(simException);
      simException.addContext("overlay stack:");
      paramExecutionStack.annotate(simException);
      throw simException;
    } 
  }
  
  public TypeBearer peek(int paramInt) {
    if (paramInt >= 0) {
      int i = this.stackPtr;
      return (paramInt >= i) ? throwSimException("underflow") : this.stack[i - paramInt - 1];
    } 
    throw new IllegalArgumentException("n < 0");
  }
  
  public boolean peekLocal(int paramInt) {
    if (paramInt >= 0) {
      int i = this.stackPtr;
      if (paramInt < i)
        return this.local[i - paramInt - 1]; 
      throw new SimException("stack: underflow");
    } 
    throw new IllegalArgumentException("n < 0");
  }
  
  public Type peekType(int paramInt) {
    return peek(paramInt).getType();
  }
  
  public TypeBearer pop() {
    throwIfImmutable();
    TypeBearer typeBearer = peek(0);
    TypeBearer[] arrayOfTypeBearer = this.stack;
    int i = this.stackPtr;
    arrayOfTypeBearer[i - 1] = null;
    this.local[i - 1] = false;
    this.stackPtr = i - typeBearer.getType().getCategory();
    return typeBearer;
  }
  
  public void push(TypeBearer paramTypeBearer) {
    throwIfImmutable();
    try {
      paramTypeBearer = paramTypeBearer.getFrameType();
      int i = paramTypeBearer.getType().getCategory();
      int j = this.stackPtr;
      TypeBearer[] arrayOfTypeBearer = this.stack;
      if (j + i > arrayOfTypeBearer.length) {
        throwSimException("overflow");
        return;
      } 
      if (i == 2) {
        arrayOfTypeBearer[j] = null;
        this.stackPtr = j + 1;
      } 
      arrayOfTypeBearer = this.stack;
      i = this.stackPtr;
      arrayOfTypeBearer[i] = paramTypeBearer;
      this.stackPtr = i + 1;
      return;
    } catch (NullPointerException nullPointerException) {
      throw new NullPointerException("type == null");
    } 
  }
  
  public void setLocal() {
    throwIfImmutable();
    this.local[this.stackPtr] = true;
  }
  
  public int size() {
    return this.stackPtr;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\code\ExecutionStack.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
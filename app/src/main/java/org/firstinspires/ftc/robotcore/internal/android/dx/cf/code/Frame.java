package org.firstinspires.ftc.robotcore.internal.android.dx.cf.code;

import org.firstinspires.ftc.robotcore.internal.android.dex.util.ExceptionWithContext;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.StdTypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeBearer;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IntList;

public final class Frame {
  private final LocalsArray locals;
  
  private final ExecutionStack stack;
  
  private final IntList subroutines;
  
  public Frame(int paramInt1, int paramInt2) {
    this(new OneLocalsArray(paramInt1), new ExecutionStack(paramInt2));
  }
  
  private Frame(LocalsArray paramLocalsArray, ExecutionStack paramExecutionStack) {
    this(paramLocalsArray, paramExecutionStack, IntList.EMPTY);
  }
  
  private Frame(LocalsArray paramLocalsArray, ExecutionStack paramExecutionStack, IntList paramIntList) {
    if (paramLocalsArray != null) {
      if (paramExecutionStack != null) {
        paramIntList.throwIfMutable();
        this.locals = paramLocalsArray;
        this.stack = paramExecutionStack;
        this.subroutines = paramIntList;
        return;
      } 
      throw new NullPointerException("stack == null");
    } 
    throw new NullPointerException("locals == null");
  }
  
  private static LocalsArray adjustLocalsForSubroutines(LocalsArray paramLocalsArray, IntList paramIntList) {
    if (!(paramLocalsArray instanceof LocalsArraySet))
      return paramLocalsArray; 
    LocalsArraySet localsArraySet = (LocalsArraySet)paramLocalsArray;
    paramLocalsArray = localsArraySet;
    if (paramIntList.size() == 0)
      paramLocalsArray = localsArraySet.getPrimary(); 
    return paramLocalsArray;
  }
  
  private IntList mergeSubroutineLists(IntList paramIntList) {
    if (this.subroutines.equals(paramIntList))
      return this.subroutines; 
    IntList intList = new IntList();
    int j = this.subroutines.size();
    int k = paramIntList.size();
    for (int i = 0; i < j && i < k && this.subroutines.get(i) == paramIntList.get(i); i++)
      intList.add(i); 
    intList.setImmutable();
    return intList;
  }
  
  public void annotate(ExceptionWithContext paramExceptionWithContext) {
    this.locals.annotate(paramExceptionWithContext);
    this.stack.annotate(paramExceptionWithContext);
  }
  
  public Frame copy() {
    return new Frame(this.locals.copy(), this.stack.copy(), this.subroutines);
  }
  
  public LocalsArray getLocals() {
    return this.locals;
  }
  
  public ExecutionStack getStack() {
    return this.stack;
  }
  
  public IntList getSubroutines() {
    return this.subroutines;
  }
  
  public void initializeWithParameters(StdTypeList paramStdTypeList) {
    int k = paramStdTypeList.size();
    int i = 0;
    int j = 0;
    while (i < k) {
      Type type = paramStdTypeList.get(i);
      this.locals.set(j, (TypeBearer)type);
      j += type.getCategory();
      i++;
    } 
  }
  
  public Frame makeExceptionHandlerStartFrame(CstType paramCstType) {
    ExecutionStack executionStack = getStack().copy();
    executionStack.clear();
    executionStack.push((TypeBearer)paramCstType);
    return new Frame(getLocals(), executionStack, this.subroutines);
  }
  
  public void makeInitialized(Type paramType) {
    this.locals.makeInitialized(paramType);
    this.stack.makeInitialized(paramType);
  }
  
  public Frame makeNewSubroutineStartFrame(int paramInt1, int paramInt2) {
    this.subroutines.mutableCopy().add(paramInt1);
    return (new Frame(this.locals.getPrimary(), this.stack, IntList.makeImmutable(paramInt1))).mergeWithSubroutineCaller(this, paramInt1, paramInt2);
  }
  
  public Frame mergeWith(Frame paramFrame) {
    LocalsArray localsArray = getLocals().merge(paramFrame.getLocals());
    ExecutionStack executionStack = getStack().merge(paramFrame.getStack());
    IntList intList = mergeSubroutineLists(paramFrame.subroutines);
    localsArray = adjustLocalsForSubroutines(localsArray, intList);
    return (localsArray == getLocals() && executionStack == getStack() && this.subroutines == intList) ? this : new Frame(localsArray, executionStack, intList);
  }
  
  public Frame mergeWithSubroutineCaller(Frame paramFrame, int paramInt1, int paramInt2) {
    IntList intList2;
    LocalsArraySet localsArraySet = getLocals().mergeWithSubroutineCaller(paramFrame.getLocals(), paramInt2);
    ExecutionStack executionStack = getStack().merge(paramFrame.getStack());
    IntList intList1 = paramFrame.subroutines.mutableCopy();
    intList1.add(paramInt1);
    intList1.setImmutable();
    if (localsArraySet == getLocals() && executionStack == getStack() && this.subroutines.equals(intList1))
      return this; 
    if (this.subroutines.equals(intList1)) {
      intList2 = this.subroutines;
    } else {
      IntList intList;
      if (this.subroutines.size() > intList1.size()) {
        intList2 = this.subroutines;
        intList = intList1;
        intList1 = intList2;
      } else {
        intList = this.subroutines;
      } 
      paramInt2 = intList1.size();
      int i = intList.size();
      paramInt1 = i - 1;
      while (true) {
        intList2 = intList1;
        if (paramInt1 >= 0) {
          if (intList.get(paramInt1) == intList1.get(paramInt2 - i + paramInt1)) {
            paramInt1--;
            continue;
          } 
          throw new RuntimeException("Incompatible merged subroutines");
        } 
        break;
      } 
    } 
    return new Frame(localsArraySet, executionStack, intList2);
  }
  
  public void setImmutable() {
    this.locals.setImmutable();
    this.stack.setImmutable();
  }
  
  public Frame subFrameForLabel(int paramInt1, int paramInt2) {
    LocalsArray localsArray = this.locals;
    if (localsArray instanceof LocalsArraySet) {
      localsArray = ((LocalsArraySet)localsArray).subArrayForLabel(paramInt2);
    } else {
      localsArray = null;
    } 
    try {
      IntList intList = this.subroutines.mutableCopy();
      if (intList.pop() == paramInt1) {
        intList.setImmutable();
        return (localsArray == null) ? null : new Frame(localsArray, this.stack, intList);
      } 
      throw new RuntimeException("returning from invalid subroutine");
    } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
      throw new RuntimeException("returning from invalid subroutine");
    } catch (NullPointerException nullPointerException) {
      throw new NullPointerException("can't return from non-subroutine");
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\code\Frame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
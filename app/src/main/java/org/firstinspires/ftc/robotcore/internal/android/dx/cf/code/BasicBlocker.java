package org.firstinspires.ftc.robotcore.internal.android.dx.cf.code;

import java.util.ArrayList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Bits;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IntList;

public final class BasicBlocker implements BytecodeArray.Visitor {
  private final int[] blockSet;
  
  private final ByteCatchList[] catchLists;
  
  private final int[] liveSet;
  
  private final ConcreteMethod method;
  
  private int previousOffset;
  
  private final IntList[] targetLists;
  
  private final int[] workSet;
  
  private BasicBlocker(ConcreteMethod paramConcreteMethod) {
    if (paramConcreteMethod != null) {
      this.method = paramConcreteMethod;
      int i = paramConcreteMethod.getCode().size() + 1;
      this.workSet = Bits.makeBitSet(i);
      this.liveSet = Bits.makeBitSet(i);
      this.blockSet = Bits.makeBitSet(i);
      this.targetLists = new IntList[i];
      this.catchLists = new ByteCatchList[i];
      this.previousOffset = -1;
      return;
    } 
    throw new NullPointerException("method == null");
  }
  
  private void addWorkIfNecessary(int paramInt, boolean paramBoolean) {
    if (!Bits.get(this.liveSet, paramInt))
      Bits.set(this.workSet, paramInt); 
    if (paramBoolean)
      Bits.set(this.blockSet, paramInt); 
  }
  
  private void doit() {
    BytecodeArray bytecodeArray = this.method.getCode();
    ByteCatchList byteCatchList = this.method.getCatches();
    int i = byteCatchList.size();
    Bits.set(this.workSet, 0);
    Bits.set(this.blockSet, 0);
    while (!Bits.isEmpty(this.workSet)) {
      try {
        bytecodeArray.processWorkSet(this.workSet, this);
        for (int j = 0; j < i; j++) {
          ByteCatchList.Item item = byteCatchList.get(j);
          int k = item.getStartPc();
          int m = item.getEndPc();
          if (Bits.anyInRange(this.liveSet, k, m)) {
            Bits.set(this.blockSet, k);
            Bits.set(this.blockSet, m);
            addWorkIfNecessary(item.getHandlerPc(), true);
          } 
        } 
      } catch (IllegalArgumentException illegalArgumentException) {
        throw new SimException("flow of control falls off end of method", illegalArgumentException);
      } 
    } 
  }
  
  private ByteBlockList getBlockList() {
    // Byte code:
    //   0: aload_0
    //   1: getfield method : Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/ConcreteMethod;
    //   4: invokevirtual getCode : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/BytecodeArray;
    //   7: invokevirtual size : ()I
    //   10: anewarray org/firstinspires/ftc/robotcore/internal/android/dx/cf/code/ByteBlock
    //   13: astore #10
    //   15: iconst_0
    //   16: istore #4
    //   18: iconst_0
    //   19: istore_3
    //   20: iload_3
    //   21: istore_2
    //   22: aload_0
    //   23: getfield blockSet : [I
    //   26: iload_3
    //   27: iconst_1
    //   28: iadd
    //   29: invokestatic findFirst : ([II)I
    //   32: istore #5
    //   34: iload #5
    //   36: ifge -> 77
    //   39: new org/firstinspires/ftc/robotcore/internal/android/dx/cf/code/ByteBlockList
    //   42: dup
    //   43: iload_2
    //   44: invokespecial <init> : (I)V
    //   47: astore #6
    //   49: iload #4
    //   51: istore_1
    //   52: iload_1
    //   53: iload_2
    //   54: if_icmpge -> 74
    //   57: aload #6
    //   59: iload_1
    //   60: aload #10
    //   62: iload_1
    //   63: aaload
    //   64: invokevirtual set : (ILorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/ByteBlock;)V
    //   67: iload_1
    //   68: iconst_1
    //   69: iadd
    //   70: istore_1
    //   71: goto -> 52
    //   74: aload #6
    //   76: areturn
    //   77: iload_2
    //   78: istore_1
    //   79: aload_0
    //   80: getfield liveSet : [I
    //   83: iload_3
    //   84: invokestatic get : ([II)Z
    //   87: ifeq -> 201
    //   90: aconst_null
    //   91: astore #6
    //   93: iload #5
    //   95: iconst_1
    //   96: isub
    //   97: istore_1
    //   98: iload_1
    //   99: iload_3
    //   100: if_icmplt -> 126
    //   103: aload_0
    //   104: getfield targetLists : [Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/IntList;
    //   107: iload_1
    //   108: aaload
    //   109: astore #6
    //   111: aload #6
    //   113: ifnull -> 119
    //   116: goto -> 128
    //   119: iload_1
    //   120: iconst_1
    //   121: isub
    //   122: istore_1
    //   123: goto -> 98
    //   126: iconst_m1
    //   127: istore_1
    //   128: aload #6
    //   130: ifnonnull -> 148
    //   133: iload #5
    //   135: invokestatic makeImmutable : (I)Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/IntList;
    //   138: astore #8
    //   140: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/cf/code/ByteCatchList.EMPTY : Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/ByteCatchList;
    //   143: astore #7
    //   145: goto -> 178
    //   148: aload_0
    //   149: getfield catchLists : [Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/ByteCatchList;
    //   152: iload_1
    //   153: aaload
    //   154: astore #9
    //   156: aload #6
    //   158: astore #8
    //   160: aload #9
    //   162: astore #7
    //   164: aload #9
    //   166: ifnonnull -> 178
    //   169: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/cf/code/ByteCatchList.EMPTY : Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/ByteCatchList;
    //   172: astore #7
    //   174: aload #6
    //   176: astore #8
    //   178: aload #10
    //   180: iload_2
    //   181: new org/firstinspires/ftc/robotcore/internal/android/dx/cf/code/ByteBlock
    //   184: dup
    //   185: iload_3
    //   186: iload_3
    //   187: iload #5
    //   189: aload #8
    //   191: aload #7
    //   193: invokespecial <init> : (IIILorg/firstinspires/ftc/robotcore/internal/android/dx/util/IntList;Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/ByteCatchList;)V
    //   196: aastore
    //   197: iload_2
    //   198: iconst_1
    //   199: iadd
    //   200: istore_1
    //   201: iload #5
    //   203: istore_3
    //   204: iload_1
    //   205: istore_2
    //   206: goto -> 22
  }
  
  public static ByteBlockList identifyBlocks(ConcreteMethod paramConcreteMethod) {
    BasicBlocker basicBlocker = new BasicBlocker(paramConcreteMethod);
    basicBlocker.doit();
    return basicBlocker.getBlockList();
  }
  
  private void visitCommon(int paramInt1, int paramInt2, boolean paramBoolean) {
    Bits.set(this.liveSet, paramInt1);
    if (paramBoolean) {
      addWorkIfNecessary(paramInt1 + paramInt2, false);
      return;
    } 
    Bits.set(this.blockSet, paramInt1 + paramInt2);
  }
  
  private void visitThrowing(int paramInt1, int paramInt2, boolean paramBoolean) {
    paramInt2 += paramInt1;
    if (paramBoolean)
      addWorkIfNecessary(paramInt2, true); 
    ByteCatchList byteCatchList = this.method.getCatches().listFor(paramInt1);
    this.catchLists[paramInt1] = byteCatchList;
    IntList[] arrayOfIntList = this.targetLists;
    if (!paramBoolean)
      paramInt2 = -1; 
    arrayOfIntList[paramInt1] = byteCatchList.toTargetList(paramInt2);
  }
  
  public int getPreviousOffset() {
    return this.previousOffset;
  }
  
  public void setPreviousOffset(int paramInt) {
    this.previousOffset = paramInt;
  }
  
  public void visitBranch(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (paramInt1 != 167) {
      if (paramInt1 == 168)
        addWorkIfNecessary(paramInt2, true); 
      paramInt1 = paramInt2 + paramInt3;
      visitCommon(paramInt2, paramInt3, true);
      addWorkIfNecessary(paramInt1, true);
      this.targetLists[paramInt2] = IntList.makeImmutable(paramInt1, paramInt4);
    } else {
      visitCommon(paramInt2, paramInt3, false);
      this.targetLists[paramInt2] = IntList.makeImmutable(paramInt4);
    } 
    addWorkIfNecessary(paramInt4, true);
  }
  
  public void visitConstant(int paramInt1, int paramInt2, int paramInt3, Constant paramConstant, int paramInt4) {
    visitCommon(paramInt2, paramInt3, true);
    if (paramConstant instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstMemberRef || paramConstant instanceof CstType || paramConstant instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString)
      visitThrowing(paramInt2, paramInt3, true); 
  }
  
  public void visitInvalid(int paramInt1, int paramInt2, int paramInt3) {
    visitCommon(paramInt2, paramInt3, true);
  }
  
  public void visitLocal(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Type paramType, int paramInt5) {
    if (paramInt1 == 169) {
      visitCommon(paramInt2, paramInt3, false);
      this.targetLists[paramInt2] = IntList.EMPTY;
      return;
    } 
    visitCommon(paramInt2, paramInt3, true);
  }
  
  public void visitNewarray(int paramInt1, int paramInt2, CstType paramCstType, ArrayList<Constant> paramArrayList) {
    visitCommon(paramInt1, paramInt2, true);
    visitThrowing(paramInt1, paramInt2, true);
  }
  
  public void visitNoArgs(int paramInt1, int paramInt2, int paramInt3, Type paramType) {
    if (paramInt1 != 108 && paramInt1 != 112) {
      if (paramInt1 != 172 && paramInt1 != 177) {
        if (paramInt1 != 190)
          if (paramInt1 != 191) {
            if (paramInt1 != 194 && paramInt1 != 195)
              switch (paramInt1) {
                default:
                  switch (paramInt1) {
                    default:
                      visitCommon(paramInt2, paramInt3, true);
                      return;
                    case 79:
                    case 80:
                    case 81:
                    case 82:
                    case 83:
                    case 84:
                    case 85:
                    case 86:
                      break;
                  } 
                  break;
                case 46:
                case 47:
                case 48:
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                  break;
              }  
          } else {
            visitCommon(paramInt2, paramInt3, false);
            visitThrowing(paramInt2, paramInt3, false);
            return;
          }  
        visitCommon(paramInt2, paramInt3, true);
        visitThrowing(paramInt2, paramInt3, true);
        return;
      } 
      visitCommon(paramInt2, paramInt3, false);
      this.targetLists[paramInt2] = IntList.EMPTY;
      return;
    } 
    visitCommon(paramInt2, paramInt3, true);
    if (paramType == Type.INT || paramType == Type.LONG)
      visitThrowing(paramInt2, paramInt3, true); 
  }
  
  public void visitSwitch(int paramInt1, int paramInt2, int paramInt3, SwitchList paramSwitchList, int paramInt4) {
    paramInt1 = 0;
    visitCommon(paramInt2, paramInt3, false);
    addWorkIfNecessary(paramSwitchList.getDefaultTarget(), true);
    paramInt3 = paramSwitchList.size();
    while (paramInt1 < paramInt3) {
      addWorkIfNecessary(paramSwitchList.getTarget(paramInt1), true);
      paramInt1++;
    } 
    this.targetLists[paramInt2] = paramSwitchList.getTargets();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\code\BasicBlocker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
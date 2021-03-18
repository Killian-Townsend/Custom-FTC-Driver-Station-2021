package org.firstinspires.ftc.robotcore.internal.android.dx.cf.code;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.MethodList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.BasicBlock;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.BasicBlockList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.Insn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.InsnList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.PlainInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.Rop;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RopMethod;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.Rops;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.SourcePosition;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.ThrowingInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.TranslationAdvice;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Prototype;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.StdTypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeBearer;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Bits;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IntList;

public final class Ropper {
  private static final int PARAM_ASSIGNMENT = -1;
  
  private static final int RETURN = -2;
  
  private static final int SPECIAL_LABEL_COUNT = 7;
  
  private static final int SYNCH_CATCH_1 = -6;
  
  private static final int SYNCH_CATCH_2 = -7;
  
  private static final int SYNCH_RETURN = -3;
  
  private static final int SYNCH_SETUP_1 = -4;
  
  private static final int SYNCH_SETUP_2 = -5;
  
  private final ByteBlockList blocks;
  
  private final CatchInfo[] catchInfos;
  
  private final ExceptionSetupLabelAllocator exceptionSetupLabelAllocator;
  
  private boolean hasSubroutines;
  
  private final RopperMachine machine;
  
  private final int maxLabel;
  
  private final int maxLocals;
  
  private final ConcreteMethod method;
  
  private final ArrayList<BasicBlock> result;
  
  private final ArrayList<IntList> resultSubroutines;
  
  private final Simulator sim;
  
  private final Frame[] startFrames;
  
  private final Subroutine[] subroutines;
  
  private boolean synchNeedsExceptionHandler;
  
  private Ropper(ConcreteMethod paramConcreteMethod, TranslationAdvice paramTranslationAdvice, MethodList paramMethodList) {
    if (paramConcreteMethod != null) {
      if (paramTranslationAdvice != null) {
        this.method = paramConcreteMethod;
        ByteBlockList byteBlockList = BasicBlocker.identifyBlocks(paramConcreteMethod);
        this.blocks = byteBlockList;
        this.maxLabel = byteBlockList.getMaxLabel();
        this.maxLocals = paramConcreteMethod.getMaxLocals();
        this.machine = new RopperMachine(this, paramConcreteMethod, paramTranslationAdvice, paramMethodList);
        this.sim = new Simulator(this.machine, paramConcreteMethod);
        int i = this.maxLabel;
        this.startFrames = new Frame[i];
        this.subroutines = new Subroutine[i];
        this.result = new ArrayList<BasicBlock>(this.blocks.size() * 2 + 10);
        this.resultSubroutines = new ArrayList<IntList>(this.blocks.size() * 2 + 10);
        this.catchInfos = new CatchInfo[this.maxLabel];
        this.synchNeedsExceptionHandler = false;
        this.startFrames[0] = new Frame(this.maxLocals, paramConcreteMethod.getMaxStack());
        this.exceptionSetupLabelAllocator = new ExceptionSetupLabelAllocator();
        return;
      } 
      throw new NullPointerException("advice == null");
    } 
    throw new NullPointerException("method == null");
  }
  
  private void addBlock(BasicBlock paramBasicBlock, IntList paramIntList) {
    if (paramBasicBlock != null) {
      this.result.add(paramBasicBlock);
      paramIntList.throwIfMutable();
      this.resultSubroutines.add(paramIntList);
      return;
    } 
    throw new NullPointerException("block == null");
  }
  
  private void addExceptionSetupBlocks() {
    int j = this.catchInfos.length;
    for (int i = 0; i < j; i++) {
      CatchInfo catchInfo = this.catchInfos[i];
      if (catchInfo != null)
        for (ExceptionHandlerSetup exceptionHandlerSetup : catchInfo.getSetups()) {
          SourcePosition sourcePosition = labelToBlock(i).getFirstInsn().getPosition();
          InsnList insnList = new InsnList(2);
          insnList.set(0, (Insn)new PlainInsn(Rops.opMoveException((TypeBearer)exceptionHandlerSetup.getCaughtType()), sourcePosition, RegisterSpec.make(this.maxLocals, (TypeBearer)exceptionHandlerSetup.getCaughtType()), RegisterSpecList.EMPTY));
          insnList.set(1, (Insn)new PlainInsn(Rops.GOTO, sourcePosition, null, RegisterSpecList.EMPTY));
          insnList.setImmutable();
          addBlock(new BasicBlock(exceptionHandlerSetup.getLabel(), insnList, IntList.makeImmutable(i), i), this.startFrames[i].getSubroutines());
        }  
    } 
  }
  
  private boolean addOrReplaceBlock(BasicBlock paramBasicBlock, IntList paramIntList) {
    if (paramBasicBlock != null) {
      boolean bool;
      int i = labelToResultIndex(paramBasicBlock.getLabel());
      if (i < 0) {
        bool = false;
      } else {
        removeBlockAndSpecialSuccessors(i);
        bool = true;
      } 
      this.result.add(paramBasicBlock);
      paramIntList.throwIfMutable();
      this.resultSubroutines.add(paramIntList);
      return bool;
    } 
    throw new NullPointerException("block == null");
  }
  
  private boolean addOrReplaceBlockNoDelete(BasicBlock paramBasicBlock, IntList paramIntList) {
    if (paramBasicBlock != null) {
      boolean bool;
      int i = labelToResultIndex(paramBasicBlock.getLabel());
      if (i < 0) {
        bool = false;
      } else {
        this.result.remove(i);
        this.resultSubroutines.remove(i);
        bool = true;
      } 
      this.result.add(paramBasicBlock);
      paramIntList.throwIfMutable();
      this.resultSubroutines.add(paramIntList);
      return bool;
    } 
    throw new NullPointerException("block == null");
  }
  
  private void addReturnBlock() {
    RegisterSpecList registerSpecList;
    Rop rop = this.machine.getReturnOp();
    if (rop == null)
      return; 
    SourcePosition sourcePosition = this.machine.getReturnPosition();
    int j = getSpecialLabel(-2);
    int i = j;
    if (isSynchronized()) {
      InsnList insnList1 = new InsnList(1);
      insnList1.set(0, (Insn)new ThrowingInsn(Rops.MONITOR_EXIT, sourcePosition, RegisterSpecList.make(getSynchReg()), (TypeList)StdTypeList.EMPTY));
      insnList1.setImmutable();
      i = getSpecialLabel(-3);
      addBlock(new BasicBlock(j, insnList1, IntList.makeImmutable(i), i), IntList.EMPTY);
    } 
    InsnList insnList = new InsnList(1);
    TypeList typeList = rop.getSources();
    if (typeList.size() == 0) {
      registerSpecList = RegisterSpecList.EMPTY;
    } else {
      registerSpecList = RegisterSpecList.make(RegisterSpec.make(0, (TypeBearer)registerSpecList.getType(0)));
    } 
    insnList.set(0, (Insn)new PlainInsn(rop, sourcePosition, null, registerSpecList));
    insnList.setImmutable();
    addBlock(new BasicBlock(i, insnList, IntList.EMPTY, -1), IntList.EMPTY);
  }
  
  private void addSetupBlocks() {
    throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:659)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
  }
  
  private void addSynchExceptionHandlerBlock() {
    if (!this.synchNeedsExceptionHandler)
      return; 
    SourcePosition sourcePosition = this.method.makeSourcePosistion(0);
    RegisterSpec registerSpec = RegisterSpec.make(0, (TypeBearer)Type.THROWABLE);
    InsnList insnList = new InsnList(2);
    insnList.set(0, (Insn)new PlainInsn(Rops.opMoveException((TypeBearer)Type.THROWABLE), sourcePosition, registerSpec, RegisterSpecList.EMPTY));
    insnList.set(1, (Insn)new ThrowingInsn(Rops.MONITOR_EXIT, sourcePosition, RegisterSpecList.make(getSynchReg()), (TypeList)StdTypeList.EMPTY));
    insnList.setImmutable();
    int i = getSpecialLabel(-7);
    addBlock(new BasicBlock(getSpecialLabel(-6), insnList, IntList.makeImmutable(i), i), IntList.EMPTY);
    insnList = new InsnList(1);
    insnList.set(0, (Insn)new ThrowingInsn(Rops.THROW, sourcePosition, RegisterSpecList.make(registerSpec), (TypeList)StdTypeList.EMPTY));
    insnList.setImmutable();
    addBlock(new BasicBlock(i, insnList, IntList.EMPTY, -1), IntList.EMPTY);
  }
  
  public static RopMethod convert(ConcreteMethod paramConcreteMethod, TranslationAdvice paramTranslationAdvice, MethodList paramMethodList) {
    try {
      Ropper ropper = new Ropper(paramConcreteMethod, paramTranslationAdvice, paramMethodList);
      ropper.doit();
      return ropper.getRopMethod();
    } catch (SimException simException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("...while working on method ");
      stringBuilder.append(paramConcreteMethod.getNat().toHuman());
      simException.addContext(stringBuilder.toString());
      throw simException;
    } 
  }
  
  private void deleteUnreachableBlocks() {
    final IntList reachableLabels = new IntList(this.result.size());
    this.resultSubroutines.clear();
    forEachNonSubBlockDepthFirst(getSpecialLabel(-1), new BasicBlock.Visitor() {
          public void visitBlock(BasicBlock param1BasicBlock) {
            reachableLabels.add(param1BasicBlock.getLabel());
          }
        });
    intList.sort();
    for (int i = this.result.size() - 1; i >= 0; i--) {
      if (intList.indexOf(((BasicBlock)this.result.get(i)).getLabel()) < 0)
        this.result.remove(i); 
    } 
  }
  
  private void doit() {
    int[] arrayOfInt = Bits.makeBitSet(this.maxLabel);
    Bits.set(arrayOfInt, 0);
    addSetupBlocks();
    setFirstFrame();
    while (true) {
      int i = Bits.findFirst(arrayOfInt, 0);
      if (i < 0) {
        addReturnBlock();
        addSynchExceptionHandlerBlock();
        addExceptionSetupBlocks();
        if (this.hasSubroutines)
          inlineSubroutines(); 
        return;
      } 
      Bits.clear(arrayOfInt, i);
      ByteBlock byteBlock = this.blocks.labelToBlock(i);
      Frame frame = this.startFrames[i];
      try {
        processBlock(byteBlock, frame, arrayOfInt);
      } catch (SimException simException) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("...while working on block ");
        stringBuilder.append(Hex.u2(i));
        simException.addContext(stringBuilder.toString());
        throw simException;
      } 
    } 
  }
  
  private InsnList filterMoveReturnAddressInsns(InsnList paramInsnList) {
    int k = paramInsnList.size();
    boolean bool = false;
    int i = 0;
    int j;
    for (j = i; i < k; j = m) {
      int m = j;
      if (paramInsnList.get(i).getOpcode() != Rops.MOVE_RETURN_ADDRESS)
        m = j + 1; 
      i++;
    } 
    if (j == k)
      return paramInsnList; 
    InsnList insnList = new InsnList(j);
    j = 0;
    i = bool;
    while (i < k) {
      Insn insn = paramInsnList.get(i);
      int m = j;
      if (insn.getOpcode() != Rops.MOVE_RETURN_ADDRESS) {
        insnList.set(j, insn);
        m = j + 1;
      } 
      i++;
      j = m;
    } 
    insnList.setImmutable();
    return insnList;
  }
  
  private void forEachNonSubBlockDepthFirst(int paramInt, BasicBlock.Visitor paramVisitor) {
    forEachNonSubBlockDepthFirst0(labelToBlock(paramInt), paramVisitor, new BitSet(this.maxLabel));
  }
  
  private void forEachNonSubBlockDepthFirst0(BasicBlock paramBasicBlock, BasicBlock.Visitor paramVisitor, BitSet paramBitSet) {
    paramVisitor.visitBlock(paramBasicBlock);
    paramBitSet.set(paramBasicBlock.getLabel());
    IntList intList = paramBasicBlock.getSuccessors();
    int j = intList.size();
    int i;
    for (i = 0; i < j; i++) {
      int k = intList.get(i);
      if (!paramBitSet.get(k) && (!isSubroutineCaller(paramBasicBlock) || i <= 0)) {
        k = labelToResultIndex(k);
        if (k >= 0)
          forEachNonSubBlockDepthFirst0(this.result.get(k), paramVisitor, paramBitSet); 
      } 
    } 
  }
  
  private int getAvailableLabel() {
    int i = getMinimumUnreservedLabel();
    Iterator<BasicBlock> iterator = this.result.iterator();
    while (iterator.hasNext()) {
      int j = ((BasicBlock)iterator.next()).getLabel();
      if (j >= i)
        i = j + 1; 
    } 
    return i;
  }
  
  private int getMinimumUnreservedLabel() {
    return this.maxLabel + this.method.getCatches().size() + 7;
  }
  
  private int getNormalRegCount() {
    return this.maxLocals + this.method.getMaxStack();
  }
  
  private RopMethod getRopMethod() {
    int j = this.result.size();
    BasicBlockList basicBlockList = new BasicBlockList(j);
    for (int i = 0; i < j; i++)
      basicBlockList.set(i, this.result.get(i)); 
    basicBlockList.setImmutable();
    return new RopMethod(basicBlockList, getSpecialLabel(-1));
  }
  
  private int getSpecialLabel(int paramInt) {
    return this.maxLabel + this.method.getCatches().size() + paramInt;
  }
  
  private RegisterSpec getSynchReg() {
    int j = getNormalRegCount();
    int i = j;
    if (j < 1)
      i = 1; 
    return RegisterSpec.make(i, (TypeBearer)Type.OBJECT);
  }
  
  private void inlineSubroutines() {
    final IntList reachableSubroutineCallerLabels = new IntList(4);
    BasicBlock.Visitor visitor = new BasicBlock.Visitor() {
        public void visitBlock(BasicBlock param1BasicBlock) {
          if (Ropper.this.isSubroutineCaller(param1BasicBlock))
            reachableSubroutineCallerLabels.add(param1BasicBlock.getLabel()); 
        }
      };
    int j = 0;
    forEachNonSubBlockDepthFirst(0, visitor);
    int k = getAvailableLabel();
    ArrayList<IntList> arrayList = new ArrayList(k);
    int i;
    for (i = 0; i < k; i++)
      arrayList.add(null); 
    for (i = 0; i < this.result.size(); i++) {
      BasicBlock basicBlock = this.result.get(i);
      if (basicBlock != null) {
        IntList intList1 = this.resultSubroutines.get(i);
        arrayList.set(basicBlock.getLabel(), intList1);
      } 
    } 
    k = intList.size();
    for (i = j; i < k; i++) {
      j = intList.get(i);
      (new SubroutineInliner(new LabelAllocator(getAvailableLabel()), arrayList)).inlineSubroutineCalledFrom(labelToBlock(j));
    } 
    deleteUnreachableBlocks();
  }
  
  private boolean isStatic() {
    return ((this.method.getAccessFlags() & 0x8) != 0);
  }
  
  private boolean isSubroutineCaller(BasicBlock paramBasicBlock) {
    IntList intList = paramBasicBlock.getSuccessors();
    int i = intList.size();
    boolean bool2 = false;
    if (i < 2)
      return false; 
    i = intList.get(1);
    Subroutine[] arrayOfSubroutine = this.subroutines;
    boolean bool1 = bool2;
    if (i < arrayOfSubroutine.length) {
      bool1 = bool2;
      if (arrayOfSubroutine[i] != null)
        bool1 = true; 
    } 
    return bool1;
  }
  
  private boolean isSynchronized() {
    return ((this.method.getAccessFlags() & 0x20) != 0);
  }
  
  private BasicBlock labelToBlock(int paramInt) {
    int i = labelToResultIndex(paramInt);
    if (i >= 0)
      return this.result.get(i); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("no such label ");
    stringBuilder.append(Hex.u2(paramInt));
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  private int labelToResultIndex(int paramInt) {
    int j = this.result.size();
    for (int i = 0; i < j; i++) {
      if (((BasicBlock)this.result.get(i)).getLabel() == paramInt)
        return i; 
    } 
    return -1;
  }
  
  private void mergeAndWorkAsNecessary(int paramInt1, int paramInt2, Subroutine paramSubroutine, Frame paramFrame, int[] paramArrayOfint) {
    Frame frame1;
    Frame[] arrayOfFrame = this.startFrames;
    Frame frame2 = arrayOfFrame[paramInt1];
    if (frame2 != null) {
      if (paramSubroutine != null) {
        frame1 = frame2.mergeWithSubroutineCaller(paramFrame, paramSubroutine.getStartBlock(), paramInt2);
      } else {
        frame1 = frame2.mergeWith(paramFrame);
      } 
      if (frame1 != frame2) {
        this.startFrames[paramInt1] = frame1;
        Bits.set(paramArrayOfint, paramInt1);
        return;
      } 
    } else {
      if (frame1 != null) {
        arrayOfFrame[paramInt1] = paramFrame.makeNewSubroutineStartFrame(paramInt1, paramInt2);
      } else {
        arrayOfFrame[paramInt1] = paramFrame;
      } 
      Bits.set(paramArrayOfint, paramInt1);
    } 
  }
  
  private void processBlock(ByteBlock paramByteBlock, Frame paramFrame, int[] paramArrayOfint) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getCatches : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/ByteCatchList;
    //   4: astore #18
    //   6: aload_0
    //   7: getfield machine : Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/RopperMachine;
    //   10: aload #18
    //   12: invokevirtual toRopCatchList : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeList;
    //   15: invokevirtual startBlock : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeList;)V
    //   18: aload_2
    //   19: invokevirtual copy : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/Frame;
    //   22: astore #16
    //   24: aload_0
    //   25: getfield sim : Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/Simulator;
    //   28: aload_1
    //   29: aload #16
    //   31: invokevirtual simulate : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/ByteBlock;Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/Frame;)V
    //   34: aload #16
    //   36: invokevirtual setImmutable : ()V
    //   39: aload_0
    //   40: getfield machine : Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/RopperMachine;
    //   43: invokevirtual getExtraBlockCount : ()I
    //   46: istore #9
    //   48: aload_0
    //   49: getfield machine : Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/RopperMachine;
    //   52: invokevirtual getInsns : ()Ljava/util/ArrayList;
    //   55: astore #17
    //   57: aload #17
    //   59: invokevirtual size : ()I
    //   62: istore #10
    //   64: aload #18
    //   66: invokevirtual size : ()I
    //   69: istore #11
    //   71: aload_1
    //   72: invokevirtual getSuccessors : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/IntList;
    //   75: astore_2
    //   76: aload_0
    //   77: getfield machine : Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/RopperMachine;
    //   80: invokevirtual hasJsr : ()Z
    //   83: istore #13
    //   85: iconst_1
    //   86: istore #5
    //   88: iload #13
    //   90: ifeq -> 158
    //   93: aload_2
    //   94: iconst_1
    //   95: invokevirtual get : (I)I
    //   98: istore #4
    //   100: aload_0
    //   101: getfield subroutines : [Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/Ropper$Subroutine;
    //   104: astore #14
    //   106: aload #14
    //   108: iload #4
    //   110: aaload
    //   111: ifnonnull -> 129
    //   114: aload #14
    //   116: iload #4
    //   118: new org/firstinspires/ftc/robotcore/internal/android/dx/cf/code/Ropper$Subroutine
    //   121: dup
    //   122: aload_0
    //   123: iload #4
    //   125: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/Ropper;I)V
    //   128: aastore
    //   129: aload_0
    //   130: getfield subroutines : [Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/Ropper$Subroutine;
    //   133: iload #4
    //   135: aaload
    //   136: aload_1
    //   137: invokevirtual getLabel : ()I
    //   140: invokevirtual addCallerBlock : (I)V
    //   143: aload_0
    //   144: getfield subroutines : [Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/Ropper$Subroutine;
    //   147: iload #4
    //   149: aaload
    //   150: astore #14
    //   152: iconst_1
    //   153: istore #4
    //   155: goto -> 283
    //   158: aload_0
    //   159: getfield machine : Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/RopperMachine;
    //   162: invokevirtual hasRet : ()Z
    //   165: ifeq -> 257
    //   168: aload_0
    //   169: getfield machine : Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/RopperMachine;
    //   172: invokevirtual getReturnAddress : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/ReturnAddress;
    //   175: invokevirtual getSubroutineAddress : ()I
    //   178: istore #4
    //   180: aload_0
    //   181: getfield subroutines : [Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/Ropper$Subroutine;
    //   184: astore_2
    //   185: aload_2
    //   186: iload #4
    //   188: aaload
    //   189: ifnonnull -> 213
    //   192: aload_2
    //   193: iload #4
    //   195: new org/firstinspires/ftc/robotcore/internal/android/dx/cf/code/Ropper$Subroutine
    //   198: dup
    //   199: aload_0
    //   200: iload #4
    //   202: aload_1
    //   203: invokevirtual getLabel : ()I
    //   206: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/Ropper;II)V
    //   209: aastore
    //   210: goto -> 224
    //   213: aload_2
    //   214: iload #4
    //   216: aaload
    //   217: aload_1
    //   218: invokevirtual getLabel : ()I
    //   221: invokevirtual addRetBlock : (I)V
    //   224: aload_0
    //   225: getfield subroutines : [Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/Ropper$Subroutine;
    //   228: iload #4
    //   230: aaload
    //   231: invokevirtual getSuccessors : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/IntList;
    //   234: astore_2
    //   235: aload_0
    //   236: getfield subroutines : [Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/Ropper$Subroutine;
    //   239: iload #4
    //   241: aaload
    //   242: aload #16
    //   244: aload_3
    //   245: invokevirtual mergeToSuccessors : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/Frame;[I)V
    //   248: aload_2
    //   249: invokevirtual size : ()I
    //   252: istore #4
    //   254: goto -> 271
    //   257: aload_0
    //   258: getfield machine : Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/RopperMachine;
    //   261: invokevirtual wereCatchesUsed : ()Z
    //   264: ifeq -> 277
    //   267: iload #11
    //   269: istore #4
    //   271: aconst_null
    //   272: astore #14
    //   274: goto -> 283
    //   277: aconst_null
    //   278: astore #14
    //   280: iconst_0
    //   281: istore #4
    //   283: aload_2
    //   284: invokevirtual size : ()I
    //   287: istore #7
    //   289: iload #4
    //   291: istore #6
    //   293: iload #5
    //   295: istore #4
    //   297: iload #7
    //   299: istore #5
    //   301: iload #6
    //   303: iload #5
    //   305: if_icmpge -> 377
    //   308: aload_2
    //   309: iload #6
    //   311: invokevirtual get : (I)I
    //   314: istore #7
    //   316: aload_0
    //   317: iload #7
    //   319: aload_1
    //   320: invokevirtual getLabel : ()I
    //   323: aload #14
    //   325: aload #16
    //   327: aload_3
    //   328: invokespecial mergeAndWorkAsNecessary : (IILorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/Ropper$Subroutine;Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/Frame;[I)V
    //   331: iload #6
    //   333: iconst_1
    //   334: iadd
    //   335: istore #6
    //   337: goto -> 301
    //   340: astore_1
    //   341: new java/lang/StringBuilder
    //   344: dup
    //   345: invokespecial <init> : ()V
    //   348: astore_2
    //   349: aload_2
    //   350: ldc_w '...while merging to block '
    //   353: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   356: pop
    //   357: aload_2
    //   358: iload #7
    //   360: invokestatic u2 : (I)Ljava/lang/String;
    //   363: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   366: pop
    //   367: aload_1
    //   368: aload_2
    //   369: invokevirtual toString : ()Ljava/lang/String;
    //   372: invokevirtual addContext : (Ljava/lang/String;)V
    //   375: aload_1
    //   376: athrow
    //   377: iload #5
    //   379: ifne -> 409
    //   382: aload_0
    //   383: getfield machine : Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/RopperMachine;
    //   386: invokevirtual returns : ()Z
    //   389: ifeq -> 409
    //   392: aload_0
    //   393: bipush #-2
    //   395: invokespecial getSpecialLabel : (I)I
    //   398: invokestatic makeImmutable : (I)Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/IntList;
    //   401: astore_2
    //   402: iload #4
    //   404: istore #6
    //   406: goto -> 413
    //   409: iload #5
    //   411: istore #6
    //   413: iload #6
    //   415: ifne -> 424
    //   418: iconst_m1
    //   419: istore #5
    //   421: goto -> 450
    //   424: aload_0
    //   425: getfield machine : Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/RopperMachine;
    //   428: invokevirtual getPrimarySuccessorIndex : ()I
    //   431: istore #7
    //   433: iload #7
    //   435: istore #5
    //   437: iload #7
    //   439: iflt -> 450
    //   442: aload_2
    //   443: iload #7
    //   445: invokevirtual get : (I)I
    //   448: istore #5
    //   450: aload_0
    //   451: invokespecial isSynchronized : ()Z
    //   454: ifeq -> 474
    //   457: aload_0
    //   458: getfield machine : Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/RopperMachine;
    //   461: invokevirtual canThrow : ()Z
    //   464: ifeq -> 474
    //   467: iload #4
    //   469: istore #7
    //   471: goto -> 477
    //   474: iconst_0
    //   475: istore #7
    //   477: iload #7
    //   479: ifne -> 497
    //   482: iload #11
    //   484: ifeq -> 490
    //   487: goto -> 497
    //   490: iload #5
    //   492: istore #4
    //   494: goto -> 801
    //   497: new org/firstinspires/ftc/robotcore/internal/android/dx/util/IntList
    //   500: dup
    //   501: iload #6
    //   503: invokespecial <init> : (I)V
    //   506: astore_2
    //   507: iconst_0
    //   508: istore #12
    //   510: iconst_0
    //   511: istore #8
    //   513: iload #4
    //   515: istore #6
    //   517: iload #5
    //   519: istore #4
    //   521: iload #12
    //   523: istore #5
    //   525: iload #8
    //   527: iload #11
    //   529: if_icmpge -> 703
    //   532: aload #18
    //   534: iload #8
    //   536: invokevirtual get : (I)Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/ByteCatchList$Item;
    //   539: astore #14
    //   541: aload #14
    //   543: invokevirtual getExceptionClass : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType;
    //   546: astore #19
    //   548: aload #14
    //   550: invokevirtual getHandlerPc : ()I
    //   553: istore #12
    //   555: aload #19
    //   557: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType.OBJECT : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType;
    //   560: if_acmpne -> 566
    //   563: goto -> 569
    //   566: iconst_0
    //   567: istore #6
    //   569: aload #16
    //   571: aload #19
    //   573: invokevirtual makeExceptionHandlerStartFrame : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/Frame;
    //   576: astore #14
    //   578: aload_0
    //   579: iload #12
    //   581: aload_1
    //   582: invokevirtual getLabel : ()I
    //   585: aconst_null
    //   586: aload #14
    //   588: aload_3
    //   589: invokespecial mergeAndWorkAsNecessary : (IILorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/Ropper$Subroutine;Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/Frame;[I)V
    //   592: aload_0
    //   593: getfield catchInfos : [Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/Ropper$CatchInfo;
    //   596: iload #12
    //   598: aaload
    //   599: astore #15
    //   601: aload #15
    //   603: astore #14
    //   605: aload #15
    //   607: ifnonnull -> 630
    //   610: new org/firstinspires/ftc/robotcore/internal/android/dx/cf/code/Ropper$CatchInfo
    //   613: dup
    //   614: aload_0
    //   615: aconst_null
    //   616: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/Ropper;Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/Ropper$1;)V
    //   619: astore #14
    //   621: aload_0
    //   622: getfield catchInfos : [Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/Ropper$CatchInfo;
    //   625: iload #12
    //   627: aload #14
    //   629: aastore
    //   630: aload_2
    //   631: aload #14
    //   633: aload #19
    //   635: invokevirtual getClassType : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type;
    //   638: invokevirtual getSetup : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/Ropper$ExceptionHandlerSetup;
    //   641: invokevirtual getLabel : ()I
    //   644: invokevirtual add : (I)V
    //   647: iload #8
    //   649: iconst_1
    //   650: iadd
    //   651: istore #8
    //   653: iload #5
    //   655: iload #6
    //   657: ior
    //   658: istore #5
    //   660: iconst_1
    //   661: istore #6
    //   663: goto -> 525
    //   666: astore_1
    //   667: new java/lang/StringBuilder
    //   670: dup
    //   671: invokespecial <init> : ()V
    //   674: astore_2
    //   675: aload_2
    //   676: ldc_w '...while merging exception to block '
    //   679: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   682: pop
    //   683: aload_2
    //   684: iload #12
    //   686: invokestatic u2 : (I)Ljava/lang/String;
    //   689: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   692: pop
    //   693: aload_1
    //   694: aload_2
    //   695: invokevirtual toString : ()Ljava/lang/String;
    //   698: invokevirtual addContext : (Ljava/lang/String;)V
    //   701: aload_1
    //   702: athrow
    //   703: iload #7
    //   705: ifeq -> 786
    //   708: iload #5
    //   710: ifne -> 786
    //   713: aload_2
    //   714: aload_0
    //   715: bipush #-6
    //   717: invokespecial getSpecialLabel : (I)I
    //   720: invokevirtual add : (I)V
    //   723: aload_0
    //   724: iconst_1
    //   725: putfield synchNeedsExceptionHandler : Z
    //   728: iload #10
    //   730: iload #9
    //   732: isub
    //   733: iconst_1
    //   734: isub
    //   735: istore #5
    //   737: iload #5
    //   739: iload #10
    //   741: if_icmpge -> 786
    //   744: aload #17
    //   746: iload #5
    //   748: invokevirtual get : (I)Ljava/lang/Object;
    //   751: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Insn
    //   754: astore_3
    //   755: aload_3
    //   756: invokevirtual canThrow : ()Z
    //   759: ifeq -> 777
    //   762: aload #17
    //   764: iload #5
    //   766: aload_3
    //   767: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type.OBJECT : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type;
    //   770: invokevirtual withAddedCatch : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Insn;
    //   773: invokevirtual set : (ILjava/lang/Object;)Ljava/lang/Object;
    //   776: pop
    //   777: iload #5
    //   779: iconst_1
    //   780: iadd
    //   781: istore #5
    //   783: goto -> 737
    //   786: iload #4
    //   788: iflt -> 797
    //   791: aload_2
    //   792: iload #4
    //   794: invokevirtual add : (I)V
    //   797: aload_2
    //   798: invokevirtual setImmutable : ()V
    //   801: aload_2
    //   802: iload #4
    //   804: invokevirtual indexOf : (I)I
    //   807: istore #11
    //   809: iload #4
    //   811: istore #5
    //   813: iload #10
    //   815: istore #4
    //   817: iload #9
    //   819: istore #6
    //   821: iload #6
    //   823: ifle -> 1003
    //   826: iload #4
    //   828: iconst_1
    //   829: isub
    //   830: istore #8
    //   832: aload #17
    //   834: iload #8
    //   836: invokevirtual get : (I)Ljava/lang/Object;
    //   839: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Insn
    //   842: astore_3
    //   843: aload_3
    //   844: invokevirtual getOpcode : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;
    //   847: invokevirtual getBranchingness : ()I
    //   850: iconst_1
    //   851: if_icmpne -> 860
    //   854: iconst_1
    //   855: istore #4
    //   857: goto -> 863
    //   860: iconst_0
    //   861: istore #4
    //   863: iload #4
    //   865: ifeq -> 874
    //   868: iconst_2
    //   869: istore #7
    //   871: goto -> 877
    //   874: iconst_1
    //   875: istore #7
    //   877: new org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/InsnList
    //   880: dup
    //   881: iload #7
    //   883: invokespecial <init> : (I)V
    //   886: astore #14
    //   888: aload #14
    //   890: iconst_0
    //   891: aload_3
    //   892: invokevirtual set : (ILorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Insn;)V
    //   895: iload #4
    //   897: ifeq -> 933
    //   900: aload #14
    //   902: iconst_1
    //   903: new org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/PlainInsn
    //   906: dup
    //   907: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rops.GOTO : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;
    //   910: aload_3
    //   911: invokevirtual getPosition : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;
    //   914: aconst_null
    //   915: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList.EMPTY : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;
    //   918: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;)V
    //   921: invokevirtual set : (ILorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Insn;)V
    //   924: iload #5
    //   926: invokestatic makeImmutable : (I)Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/IntList;
    //   929: astore_3
    //   930: goto -> 935
    //   933: aload_2
    //   934: astore_3
    //   935: aload #14
    //   937: invokevirtual setImmutable : ()V
    //   940: aload_0
    //   941: invokespecial getAvailableLabel : ()I
    //   944: istore #4
    //   946: aload_0
    //   947: new org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/BasicBlock
    //   950: dup
    //   951: iload #4
    //   953: aload #14
    //   955: aload_3
    //   956: iload #5
    //   958: invokespecial <init> : (ILorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/InsnList;Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/IntList;I)V
    //   961: aload #16
    //   963: invokevirtual getSubroutines : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/IntList;
    //   966: invokespecial addBlock : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/BasicBlock;Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/IntList;)V
    //   969: aload_2
    //   970: invokevirtual mutableCopy : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/IntList;
    //   973: astore_2
    //   974: aload_2
    //   975: iload #11
    //   977: iload #4
    //   979: invokevirtual set : (II)V
    //   982: aload_2
    //   983: invokevirtual setImmutable : ()V
    //   986: iload #6
    //   988: iconst_1
    //   989: isub
    //   990: istore #6
    //   992: iload #4
    //   994: istore #5
    //   996: iload #8
    //   998: istore #4
    //   1000: goto -> 821
    //   1003: iload #4
    //   1005: ifne -> 1013
    //   1008: aconst_null
    //   1009: astore_3
    //   1010: goto -> 1026
    //   1013: aload #17
    //   1015: iload #4
    //   1017: iconst_1
    //   1018: isub
    //   1019: invokevirtual get : (I)Ljava/lang/Object;
    //   1022: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Insn
    //   1025: astore_3
    //   1026: aload_3
    //   1027: ifnull -> 1045
    //   1030: iload #4
    //   1032: istore #6
    //   1034: aload_3
    //   1035: invokevirtual getOpcode : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;
    //   1038: invokevirtual getBranchingness : ()I
    //   1041: iconst_1
    //   1042: if_icmpne -> 1088
    //   1045: aload_3
    //   1046: ifnonnull -> 1056
    //   1049: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition.NO_INFO : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;
    //   1052: astore_3
    //   1053: goto -> 1061
    //   1056: aload_3
    //   1057: invokevirtual getPosition : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;
    //   1060: astore_3
    //   1061: aload #17
    //   1063: new org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/PlainInsn
    //   1066: dup
    //   1067: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rops.GOTO : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;
    //   1070: aload_3
    //   1071: aconst_null
    //   1072: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList.EMPTY : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;
    //   1075: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;)V
    //   1078: invokevirtual add : (Ljava/lang/Object;)Z
    //   1081: pop
    //   1082: iload #4
    //   1084: iconst_1
    //   1085: iadd
    //   1086: istore #6
    //   1088: new org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/InsnList
    //   1091: dup
    //   1092: iload #6
    //   1094: invokespecial <init> : (I)V
    //   1097: astore_3
    //   1098: iconst_0
    //   1099: istore #4
    //   1101: iload #4
    //   1103: iload #6
    //   1105: if_icmpge -> 1133
    //   1108: aload_3
    //   1109: iload #4
    //   1111: aload #17
    //   1113: iload #4
    //   1115: invokevirtual get : (I)Ljava/lang/Object;
    //   1118: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Insn
    //   1121: invokevirtual set : (ILorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Insn;)V
    //   1124: iload #4
    //   1126: iconst_1
    //   1127: iadd
    //   1128: istore #4
    //   1130: goto -> 1101
    //   1133: aload_3
    //   1134: invokevirtual setImmutable : ()V
    //   1137: aload_0
    //   1138: new org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/BasicBlock
    //   1141: dup
    //   1142: aload_1
    //   1143: invokevirtual getLabel : ()I
    //   1146: aload_3
    //   1147: aload_2
    //   1148: iload #5
    //   1150: invokespecial <init> : (ILorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/InsnList;Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/IntList;I)V
    //   1153: aload #16
    //   1155: invokevirtual getSubroutines : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/IntList;
    //   1158: invokespecial addOrReplaceBlock : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/BasicBlock;Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/IntList;)Z
    //   1161: pop
    //   1162: return
    // Exception table:
    //   from	to	target	type
    //   316	331	340	org/firstinspires/ftc/robotcore/internal/android/dx/cf/code/SimException
    //   578	592	666	org/firstinspires/ftc/robotcore/internal/android/dx/cf/code/SimException
  }
  
  private void removeBlockAndSpecialSuccessors(int paramInt) {
    int i = getMinimumUnreservedLabel();
    IntList intList = ((BasicBlock)this.result.get(paramInt)).getSuccessors();
    int j = intList.size();
    this.result.remove(paramInt);
    this.resultSubroutines.remove(paramInt);
    for (paramInt = 0; paramInt < j; paramInt++) {
      int k = intList.get(paramInt);
      if (k >= i) {
        int m = labelToResultIndex(k);
        if (m >= 0) {
          removeBlockAndSpecialSuccessors(m);
        } else {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Invalid label ");
          stringBuilder.append(Hex.u2(k));
          throw new RuntimeException(stringBuilder.toString());
        } 
      } 
    } 
  }
  
  private void setFirstFrame() {
    Prototype prototype = this.method.getEffectiveDescriptor();
    this.startFrames[0].initializeWithParameters(prototype.getParameterTypes());
    this.startFrames[0].setImmutable();
  }
  
  private Subroutine subroutineFromRetBlock(int paramInt) {
    for (int i = this.subroutines.length - 1; i >= 0; i--) {
      Subroutine[] arrayOfSubroutine = this.subroutines;
      if (arrayOfSubroutine[i] != null) {
        Subroutine subroutine = arrayOfSubroutine[i];
        if (subroutine.retBlocks.get(paramInt))
          return subroutine; 
      } 
    } 
    return null;
  }
  
  int getFirstTempStackReg() {
    int j = getNormalRegCount();
    int i = j;
    if (isSynchronized())
      i = j + 1; 
    return i;
  }
  
  private class CatchInfo {
    private final Map<Type, Ropper.ExceptionHandlerSetup> setups = new HashMap<Type, Ropper.ExceptionHandlerSetup>();
    
    private CatchInfo() {}
    
    Ropper.ExceptionHandlerSetup getSetup(Type param1Type) {
      Ropper.ExceptionHandlerSetup exceptionHandlerSetup2 = this.setups.get(param1Type);
      Ropper.ExceptionHandlerSetup exceptionHandlerSetup1 = exceptionHandlerSetup2;
      if (exceptionHandlerSetup2 == null) {
        exceptionHandlerSetup1 = new Ropper.ExceptionHandlerSetup(param1Type, Ropper.this.exceptionSetupLabelAllocator.getNextLabel());
        this.setups.put(param1Type, exceptionHandlerSetup1);
      } 
      return exceptionHandlerSetup1;
    }
    
    Collection<Ropper.ExceptionHandlerSetup> getSetups() {
      return this.setups.values();
    }
  }
  
  private static class ExceptionHandlerSetup {
    private Type caughtType;
    
    private int label;
    
    ExceptionHandlerSetup(Type param1Type, int param1Int) {
      this.caughtType = param1Type;
      this.label = param1Int;
    }
    
    Type getCaughtType() {
      return this.caughtType;
    }
    
    public int getLabel() {
      return this.label;
    }
  }
  
  private class ExceptionSetupLabelAllocator extends LabelAllocator {
    int maxSetupLabel = Ropper.this.maxLabel + Ropper.this.method.getCatches().size();
    
    ExceptionSetupLabelAllocator() {
      super(Ropper.this.maxLabel);
    }
    
    int getNextLabel() {
      if (this.nextAvailableLabel < this.maxSetupLabel) {
        int i = this.nextAvailableLabel;
        this.nextAvailableLabel = i + 1;
        return i;
      } 
      throw new IndexOutOfBoundsException();
    }
  }
  
  private static class LabelAllocator {
    int nextAvailableLabel;
    
    LabelAllocator(int param1Int) {
      this.nextAvailableLabel = param1Int;
    }
    
    int getNextLabel() {
      int i = this.nextAvailableLabel;
      this.nextAvailableLabel = i + 1;
      return i;
    }
  }
  
  private class Subroutine {
    private BitSet callerBlocks;
    
    private BitSet retBlocks;
    
    private int startBlock;
    
    Subroutine(int param1Int) {
      this.startBlock = param1Int;
      this.retBlocks = new BitSet(Ropper.this.maxLabel);
      this.callerBlocks = new BitSet(Ropper.this.maxLabel);
      Ropper.access$202(Ropper.this, true);
    }
    
    Subroutine(int param1Int1, int param1Int2) {
      this(param1Int1);
      addRetBlock(param1Int2);
    }
    
    void addCallerBlock(int param1Int) {
      this.callerBlocks.set(param1Int);
    }
    
    void addRetBlock(int param1Int) {
      this.retBlocks.set(param1Int);
    }
    
    int getStartBlock() {
      return this.startBlock;
    }
    
    IntList getSuccessors() {
      IntList intList = new IntList(this.callerBlocks.size());
      for (int i = this.callerBlocks.nextSetBit(0); i >= 0; i = this.callerBlocks.nextSetBit(i + 1))
        intList.add(Ropper.this.labelToBlock(i).getSuccessors().get(0)); 
      intList.setImmutable();
      return intList;
    }
    
    void mergeToSuccessors(Frame param1Frame, int[] param1ArrayOfint) {
      for (int i = this.callerBlocks.nextSetBit(0); i >= 0; i = this.callerBlocks.nextSetBit(i + 1)) {
        int j = Ropper.this.labelToBlock(i).getSuccessors().get(0);
        Frame frame = param1Frame.subFrameForLabel(this.startBlock, i);
        if (frame != null) {
          Ropper.this.mergeAndWorkAsNecessary(j, -1, null, frame, param1ArrayOfint);
        } else {
          Bits.set(param1ArrayOfint, i);
        } 
      } 
    }
  }
  
  private class SubroutineInliner {
    private final Ropper.LabelAllocator labelAllocator;
    
    private final ArrayList<IntList> labelToSubroutines;
    
    private final HashMap<Integer, Integer> origLabelToCopiedLabel = new HashMap<Integer, Integer>();
    
    private int subroutineStart;
    
    private int subroutineSuccessor;
    
    private final BitSet workList = new BitSet(Ropper.this.maxLabel);
    
    SubroutineInliner(Ropper.LabelAllocator param1LabelAllocator, ArrayList<IntList> param1ArrayList) {
      this.labelAllocator = param1LabelAllocator;
      this.labelToSubroutines = param1ArrayList;
    }
    
    private void copyBlock(int param1Int1, int param1Int2) {
      IntList intList1;
      StringBuilder stringBuilder;
      BasicBlock basicBlock = Ropper.this.labelToBlock(param1Int1);
      IntList intList2 = basicBlock.getSuccessors();
      boolean bool = Ropper.this.isSubroutineCaller(basicBlock);
      int j = 0;
      int i = -1;
      if (bool) {
        intList1 = IntList.makeImmutable(mapOrAllocateLabel(intList2.get(0)), intList2.get(1));
        param1Int1 = i;
      } else {
        Ropper.Subroutine subroutine = Ropper.this.subroutineFromRetBlock(param1Int1);
        if (subroutine != null) {
          if (subroutine.startBlock == this.subroutineStart) {
            intList1 = IntList.makeImmutable(this.subroutineSuccessor);
            param1Int1 = this.subroutineSuccessor;
          } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("ret instruction returns to label ");
            stringBuilder.append(Hex.u2(((Ropper.Subroutine)intList1).startBlock));
            stringBuilder.append(" expected: ");
            stringBuilder.append(Hex.u2(this.subroutineStart));
            throw new RuntimeException(stringBuilder.toString());
          } 
        } else {
          int k = stringBuilder.getPrimarySuccessor();
          int m = intList2.size();
          intList1 = new IntList(m);
          param1Int1 = i;
          for (i = j; i < m; i++) {
            int n = intList2.get(i);
            j = mapOrAllocateLabel(n);
            intList1.add(j);
            if (k == n)
              param1Int1 = j; 
          } 
          intList1.setImmutable();
        } 
      } 
      Ropper.this.addBlock(new BasicBlock(param1Int2, Ropper.this.filterMoveReturnAddressInsns(stringBuilder.getInsns()), intList1, param1Int1), this.labelToSubroutines.get(param1Int2));
    }
    
    private boolean involvedInSubroutine(int param1Int1, int param1Int2) {
      IntList intList = this.labelToSubroutines.get(param1Int1);
      return (intList != null && intList.size() > 0 && intList.top() == param1Int2);
    }
    
    private int mapOrAllocateLabel(int param1Int) {
      Integer integer = this.origLabelToCopiedLabel.get(Integer.valueOf(param1Int));
      if (integer != null)
        return integer.intValue(); 
      if (!involvedInSubroutine(param1Int, this.subroutineStart))
        return param1Int; 
      int i = this.labelAllocator.getNextLabel();
      this.workList.set(param1Int);
      this.origLabelToCopiedLabel.put(Integer.valueOf(param1Int), Integer.valueOf(i));
      while (this.labelToSubroutines.size() <= i)
        this.labelToSubroutines.add(null); 
      ArrayList<IntList> arrayList = this.labelToSubroutines;
      arrayList.set(i, arrayList.get(param1Int));
      return i;
    }
    
    void inlineSubroutineCalledFrom(BasicBlock param1BasicBlock) {
      this.subroutineSuccessor = param1BasicBlock.getSuccessors().get(0);
      int i = param1BasicBlock.getSuccessors().get(1);
      this.subroutineStart = i;
      int j = mapOrAllocateLabel(i);
      for (i = this.workList.nextSetBit(0); i >= 0; i = this.workList.nextSetBit(0)) {
        this.workList.clear(i);
        int k = ((Integer)this.origLabelToCopiedLabel.get(Integer.valueOf(i))).intValue();
        copyBlock(i, k);
        Ropper ropper = Ropper.this;
        if (ropper.isSubroutineCaller(ropper.labelToBlock(i)))
          (new SubroutineInliner(this.labelAllocator, this.labelToSubroutines)).inlineSubroutineCalledFrom(Ropper.this.labelToBlock(k)); 
      } 
      Ropper.this.addOrReplaceBlockNoDelete(new BasicBlock(param1BasicBlock.getLabel(), param1BasicBlock.getInsns(), IntList.makeImmutable(j), j), this.labelToSubroutines.get(param1BasicBlock.getLabel()));
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\code\Ropper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
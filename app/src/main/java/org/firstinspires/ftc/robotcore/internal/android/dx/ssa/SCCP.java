package org.firstinspires.ftc.robotcore.internal.android.dx.ssa;

import java.util.ArrayList;
import java.util.BitSet;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.CstInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.Insn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.PlainInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.Rops;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstInteger;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeBearer;

public class SCCP {
  private static final int CONSTANT = 1;
  
  private static final int TOP = 0;
  
  private static final int VARYING = 2;
  
  private ArrayList<SsaInsn> branchWorklist;
  
  private ArrayList<SsaBasicBlock> cfgPhiWorklist;
  
  private ArrayList<SsaBasicBlock> cfgWorklist;
  
  private BitSet executableBlocks;
  
  private Constant[] latticeConstants;
  
  private int[] latticeValues;
  
  private int regCount;
  
  private SsaMethod ssaMeth;
  
  private ArrayList<SsaInsn> ssaWorklist;
  
  private ArrayList<SsaInsn> varyingWorklist;
  
  private SCCP(SsaMethod paramSsaMethod) {
    this.ssaMeth = paramSsaMethod;
    int i = paramSsaMethod.getRegCount();
    this.regCount = i;
    this.latticeValues = new int[i];
    this.latticeConstants = new Constant[i];
    this.cfgWorklist = new ArrayList<SsaBasicBlock>();
    this.cfgPhiWorklist = new ArrayList<SsaBasicBlock>();
    this.executableBlocks = new BitSet(paramSsaMethod.getBlocks().size());
    this.ssaWorklist = new ArrayList<SsaInsn>();
    this.varyingWorklist = new ArrayList<SsaInsn>();
    this.branchWorklist = new ArrayList<SsaInsn>();
    for (i = 0; i < this.regCount; i++) {
      this.latticeValues[i] = 0;
      this.latticeConstants[i] = null;
    } 
  }
  
  private void addBlockToWorklist(SsaBasicBlock paramSsaBasicBlock) {
    if (!this.executableBlocks.get(paramSsaBasicBlock.getIndex())) {
      this.cfgWorklist.add(paramSsaBasicBlock);
      this.executableBlocks.set(paramSsaBasicBlock.getIndex());
      return;
    } 
    this.cfgPhiWorklist.add(paramSsaBasicBlock);
  }
  
  private void addUsersToWorklist(int paramInt1, int paramInt2) {
    if (paramInt2 == 2) {
      for (SsaInsn ssaInsn : this.ssaMeth.getUseListForRegister(paramInt1))
        this.varyingWorklist.add(ssaInsn); 
    } else {
      for (SsaInsn ssaInsn : this.ssaMeth.getUseListForRegister(paramInt1))
        this.ssaWorklist.add(ssaInsn); 
    } 
  }
  
  private static String latticeValName(int paramInt) {
    return (paramInt != 0) ? ((paramInt != 1) ? ((paramInt != 2) ? "UNKNOWN" : "VARYING") : "CONSTANT") : "TOP";
  }
  
  public static void process(SsaMethod paramSsaMethod) {
    (new SCCP(paramSsaMethod)).run();
  }
  
  private void replaceBranches() {
    for (SsaInsn ssaInsn : this.branchWorklist) {
      SsaBasicBlock ssaBasicBlock = ssaInsn.getBlock();
      int k = ssaBasicBlock.getSuccessorList().size();
      int i = 0;
      int j = -1;
      while (i < k) {
        int m = ssaBasicBlock.getSuccessorList().get(i);
        if (!this.executableBlocks.get(m))
          j = m; 
        i++;
      } 
      if (k != 2 || j == -1)
        continue; 
      Insn insn = ssaInsn.getOriginalRopInsn();
      ssaBasicBlock.replaceLastInsn((Insn)new PlainInsn(Rops.GOTO, insn.getPosition(), null, RegisterSpecList.EMPTY));
      ssaBasicBlock.removeSuccessor(j);
    } 
  }
  
  private void replaceConstants() {
    for (int i = 0; i < this.regCount; i++) {
      if (this.latticeValues[i] == 1 && this.latticeConstants[i] instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.TypedConstant) {
        SsaInsn ssaInsn = this.ssaMeth.getDefinitionForRegister(i);
        if (!ssaInsn.getResult().getTypeBearer().isConstant()) {
          ssaInsn.setResult(ssaInsn.getResult().withType((TypeBearer)this.latticeConstants[i]));
          for (SsaInsn ssaInsn1 : this.ssaMeth.getUseListForRegister(i)) {
            if (ssaInsn1.isPhiOrMove())
              continue; 
            NormalSsaInsn normalSsaInsn = (NormalSsaInsn)ssaInsn1;
            RegisterSpecList registerSpecList = ssaInsn1.getSources();
            int j = registerSpecList.indexOfRegister(i);
            normalSsaInsn.changeOneSource(j, registerSpecList.get(j).withType((TypeBearer)this.latticeConstants[i]));
          } 
        } 
      } 
    } 
  }
  
  private void run() {
    addBlockToWorklist(this.ssaMeth.getEntryBlock());
    while (true) {
      if (!this.cfgWorklist.isEmpty() || !this.cfgPhiWorklist.isEmpty() || !this.ssaWorklist.isEmpty() || !this.varyingWorklist.isEmpty()) {
        while (!this.cfgWorklist.isEmpty()) {
          int i = this.cfgWorklist.size();
          simulateBlock(this.cfgWorklist.remove(i - 1));
        } 
        while (!this.cfgPhiWorklist.isEmpty()) {
          int i = this.cfgPhiWorklist.size();
          simulatePhiBlock(this.cfgPhiWorklist.remove(i - 1));
        } 
        while (!this.varyingWorklist.isEmpty()) {
          int i = this.varyingWorklist.size();
          SsaInsn ssaInsn = this.varyingWorklist.remove(i - 1);
          if (!this.executableBlocks.get(ssaInsn.getBlock().getIndex()))
            continue; 
          if (ssaInsn instanceof PhiInsn) {
            simulatePhi((PhiInsn)ssaInsn);
            continue;
          } 
          simulateStmt(ssaInsn);
        } 
        while (!this.ssaWorklist.isEmpty()) {
          int i = this.ssaWorklist.size();
          SsaInsn ssaInsn = this.ssaWorklist.remove(i - 1);
          if (!this.executableBlocks.get(ssaInsn.getBlock().getIndex()))
            continue; 
          if (ssaInsn instanceof PhiInsn) {
            simulatePhi((PhiInsn)ssaInsn);
            continue;
          } 
          simulateStmt(ssaInsn);
        } 
        continue;
      } 
      replaceConstants();
      replaceBranches();
      return;
    } 
  }
  
  private boolean setLatticeValueTo(int paramInt1, int paramInt2, Constant paramConstant) {
    int[] arrayOfInt;
    if (paramInt2 != 1) {
      arrayOfInt = this.latticeValues;
      if (arrayOfInt[paramInt1] != paramInt2) {
        arrayOfInt[paramInt1] = paramInt2;
        return true;
      } 
      return false;
    } 
    if (this.latticeValues[paramInt1] != paramInt2 || !this.latticeConstants[paramInt1].equals(arrayOfInt)) {
      this.latticeValues[paramInt1] = paramInt2;
      this.latticeConstants[paramInt1] = (Constant)arrayOfInt;
      return true;
    } 
    return false;
  }
  
  private void simulateBlock(SsaBasicBlock paramSsaBasicBlock) {
    for (SsaInsn ssaInsn : paramSsaBasicBlock.getInsns()) {
      if (ssaInsn instanceof PhiInsn) {
        simulatePhi((PhiInsn)ssaInsn);
        continue;
      } 
      simulateStmt(ssaInsn);
    } 
  }
  
  private void simulateBranch(SsaInsn paramSsaInsn) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getOpcode : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;
    //   4: astore #9
    //   6: aload_1
    //   7: invokevirtual getSources : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;
    //   10: astore #10
    //   12: aload #9
    //   14: invokevirtual getBranchingness : ()I
    //   17: istore_2
    //   18: iconst_0
    //   19: istore #4
    //   21: iload_2
    //   22: iconst_4
    //   23: if_icmpne -> 439
    //   26: aload #10
    //   28: iconst_0
    //   29: invokevirtual get : (I)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;
    //   32: astore #6
    //   34: aload #6
    //   36: invokevirtual getReg : ()I
    //   39: istore_2
    //   40: aload_0
    //   41: getfield ssaMeth : Lorg/firstinspires/ftc/robotcore/internal/android/dx/ssa/SsaMethod;
    //   44: aload #6
    //   46: invokevirtual isRegALocal : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;)Z
    //   49: istore #5
    //   51: aconst_null
    //   52: astore #8
    //   54: iload #5
    //   56: ifne -> 80
    //   59: aload_0
    //   60: getfield latticeValues : [I
    //   63: iload_2
    //   64: iaload
    //   65: iconst_1
    //   66: if_icmpne -> 80
    //   69: aload_0
    //   70: getfield latticeConstants : [Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/Constant;
    //   73: iload_2
    //   74: aaload
    //   75: astore #6
    //   77: goto -> 83
    //   80: aconst_null
    //   81: astore #6
    //   83: aload #8
    //   85: astore #7
    //   87: aload #10
    //   89: invokevirtual size : ()I
    //   92: iconst_2
    //   93: if_icmpne -> 148
    //   96: aload #10
    //   98: iconst_1
    //   99: invokevirtual get : (I)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;
    //   102: astore #11
    //   104: aload #11
    //   106: invokevirtual getReg : ()I
    //   109: istore_2
    //   110: aload #8
    //   112: astore #7
    //   114: aload_0
    //   115: getfield ssaMeth : Lorg/firstinspires/ftc/robotcore/internal/android/dx/ssa/SsaMethod;
    //   118: aload #11
    //   120: invokevirtual isRegALocal : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;)Z
    //   123: ifne -> 148
    //   126: aload #8
    //   128: astore #7
    //   130: aload_0
    //   131: getfield latticeValues : [I
    //   134: iload_2
    //   135: iaload
    //   136: iconst_1
    //   137: if_icmpne -> 148
    //   140: aload_0
    //   141: getfield latticeConstants : [Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/Constant;
    //   144: iload_2
    //   145: aaload
    //   146: astore #7
    //   148: aload #6
    //   150: ifnull -> 294
    //   153: aload #10
    //   155: invokevirtual size : ()I
    //   158: iconst_1
    //   159: if_icmpne -> 294
    //   162: aload #6
    //   164: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/TypedConstant
    //   167: invokevirtual getBasicType : ()I
    //   170: bipush #6
    //   172: if_icmpeq -> 178
    //   175: goto -> 439
    //   178: aload #6
    //   180: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstInteger
    //   183: invokevirtual getValue : ()I
    //   186: istore_2
    //   187: aload #9
    //   189: invokevirtual getOpcode : ()I
    //   192: tableswitch default -> 232, 7 -> 278, 8 -> 271, 9 -> 264, 10 -> 257, 11 -> 250, 12 -> 243
    //   232: new java/lang/RuntimeException
    //   235: dup
    //   236: ldc_w 'Unexpected op'
    //   239: invokespecial <init> : (Ljava/lang/String;)V
    //   242: athrow
    //   243: iload_2
    //   244: ifle -> 287
    //   247: goto -> 282
    //   250: iload_2
    //   251: ifgt -> 287
    //   254: goto -> 282
    //   257: iload_2
    //   258: iflt -> 287
    //   261: goto -> 282
    //   264: iload_2
    //   265: ifge -> 287
    //   268: goto -> 282
    //   271: iload_2
    //   272: ifeq -> 287
    //   275: goto -> 282
    //   278: iload_2
    //   279: ifne -> 287
    //   282: iconst_1
    //   283: istore_2
    //   284: goto -> 289
    //   287: iconst_0
    //   288: istore_2
    //   289: iconst_1
    //   290: istore_3
    //   291: goto -> 443
    //   294: aload #6
    //   296: ifnull -> 439
    //   299: aload #7
    //   301: ifnull -> 439
    //   304: aload #6
    //   306: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/TypedConstant
    //   309: invokevirtual getBasicType : ()I
    //   312: bipush #6
    //   314: if_icmpeq -> 320
    //   317: goto -> 439
    //   320: aload #6
    //   322: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstInteger
    //   325: invokevirtual getValue : ()I
    //   328: istore_2
    //   329: aload #7
    //   331: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstInteger
    //   334: invokevirtual getValue : ()I
    //   337: istore_3
    //   338: aload #9
    //   340: invokevirtual getOpcode : ()I
    //   343: tableswitch default -> 380, 7 -> 431, 8 -> 423, 9 -> 415, 10 -> 407, 11 -> 399, 12 -> 391
    //   380: new java/lang/RuntimeException
    //   383: dup
    //   384: ldc_w 'Unexpected op'
    //   387: invokespecial <init> : (Ljava/lang/String;)V
    //   390: athrow
    //   391: iload_2
    //   392: iload_3
    //   393: if_icmple -> 287
    //   396: goto -> 282
    //   399: iload_2
    //   400: iload_3
    //   401: if_icmpgt -> 287
    //   404: goto -> 282
    //   407: iload_2
    //   408: iload_3
    //   409: if_icmplt -> 287
    //   412: goto -> 282
    //   415: iload_2
    //   416: iload_3
    //   417: if_icmpge -> 287
    //   420: goto -> 282
    //   423: iload_2
    //   424: iload_3
    //   425: if_icmpeq -> 287
    //   428: goto -> 282
    //   431: iload_2
    //   432: iload_3
    //   433: if_icmpne -> 287
    //   436: goto -> 282
    //   439: iconst_0
    //   440: istore_2
    //   441: iload_2
    //   442: istore_3
    //   443: aload_1
    //   444: invokevirtual getBlock : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/ssa/SsaBasicBlock;
    //   447: astore #6
    //   449: iload_3
    //   450: ifeq -> 508
    //   453: iload_2
    //   454: ifeq -> 470
    //   457: aload #6
    //   459: invokevirtual getSuccessorList : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/IntList;
    //   462: iconst_1
    //   463: invokevirtual get : (I)I
    //   466: istore_2
    //   467: goto -> 480
    //   470: aload #6
    //   472: invokevirtual getSuccessorList : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/IntList;
    //   475: iconst_0
    //   476: invokevirtual get : (I)I
    //   479: istore_2
    //   480: aload_0
    //   481: aload_0
    //   482: getfield ssaMeth : Lorg/firstinspires/ftc/robotcore/internal/android/dx/ssa/SsaMethod;
    //   485: invokevirtual getBlocks : ()Ljava/util/ArrayList;
    //   488: iload_2
    //   489: invokevirtual get : (I)Ljava/lang/Object;
    //   492: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/ssa/SsaBasicBlock
    //   495: invokespecial addBlockToWorklist : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/ssa/SsaBasicBlock;)V
    //   498: aload_0
    //   499: getfield branchWorklist : Ljava/util/ArrayList;
    //   502: aload_1
    //   503: invokevirtual add : (Ljava/lang/Object;)Z
    //   506: pop
    //   507: return
    //   508: iload #4
    //   510: aload #6
    //   512: invokevirtual getSuccessorList : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/IntList;
    //   515: invokevirtual size : ()I
    //   518: if_icmpge -> 559
    //   521: aload #6
    //   523: invokevirtual getSuccessorList : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/IntList;
    //   526: iload #4
    //   528: invokevirtual get : (I)I
    //   531: istore_2
    //   532: aload_0
    //   533: aload_0
    //   534: getfield ssaMeth : Lorg/firstinspires/ftc/robotcore/internal/android/dx/ssa/SsaMethod;
    //   537: invokevirtual getBlocks : ()Ljava/util/ArrayList;
    //   540: iload_2
    //   541: invokevirtual get : (I)Ljava/lang/Object;
    //   544: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/ssa/SsaBasicBlock
    //   547: invokespecial addBlockToWorklist : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/ssa/SsaBasicBlock;)V
    //   550: iload #4
    //   552: iconst_1
    //   553: iadd
    //   554: istore #4
    //   556: goto -> 508
    //   559: return
  }
  
  private Constant simulateMath(SsaInsn paramSsaInsn, int paramInt) {
    Constant constant1;
    Constant constant2;
    Insn insn = paramSsaInsn.getOriginalRopInsn();
    int i = paramSsaInsn.getOpcode().getOpcode();
    RegisterSpecList registerSpecList = paramSsaInsn.getSources();
    boolean bool = false;
    int j = registerSpecList.get(0).getReg();
    int k = this.latticeValues[j];
    CstInteger cstInteger = null;
    if (k != 1) {
      constant2 = null;
    } else {
      constant2 = this.latticeConstants[j];
    } 
    if (registerSpecList.size() == 1) {
      constant1 = ((CstInsn)insn).getConstant();
    } else {
      j = registerSpecList.get(1).getReg();
      if (this.latticeValues[j] != 1) {
        paramSsaInsn = null;
      } else {
        constant1 = this.latticeConstants[j];
      } 
    } 
    if (constant2 != null) {
      if (constant1 == null)
        return null; 
      if (paramInt != 6)
        return null; 
      paramInt = ((CstInteger)constant2).getValue();
      j = ((CstInteger)constant1).getValue();
      switch (i) {
        default:
          throw new RuntimeException("Unexpected op");
        case 25:
          paramInt >>>= j;
          break;
        case 24:
          paramInt >>= j;
          break;
        case 23:
          paramInt <<= j;
          break;
        case 22:
          paramInt ^= j;
          break;
        case 21:
          paramInt |= j;
          break;
        case 20:
          paramInt &= j;
          break;
        case 18:
          if (j != 0) {
            paramInt %= j;
            break;
          } 
          paramInt = 0;
          bool = true;
          break;
        case 17:
          if (j != 0) {
            paramInt /= j;
            break;
          } 
          paramInt = 0;
          bool = true;
          break;
        case 16:
          paramInt *= j;
          break;
        case 15:
          if (registerSpecList.size() == 1) {
            paramInt = j - paramInt;
            break;
          } 
          paramInt -= j;
          break;
        case 14:
          paramInt += j;
          break;
      } 
      if (bool)
        return null; 
      cstInteger = CstInteger.make(paramInt);
    } 
    return (Constant)cstInteger;
  }
  
  private void simulatePhi(PhiInsn paramPhiInsn) {
    int k = paramPhiInsn.getResult().getReg();
    int i = this.latticeValues[k];
    byte b = 2;
    if (i == 2)
      return; 
    RegisterSpecList registerSpecList = paramPhiInsn.getSources();
    Constant constant = null;
    int m = registerSpecList.size();
    int j = 0;
    i = 0;
    while (j < m) {
      Constant constant1;
      int i2 = paramPhiInsn.predBlockIndexForSourcesIndex(j);
      int i1 = registerSpecList.get(j).getReg();
      int n = this.latticeValues[i1];
      if (!this.executableBlocks.get(i2)) {
        constant1 = constant;
      } else if (n == 1) {
        if (constant == null) {
          constant1 = this.latticeConstants[i1];
          i = 1;
        } else {
          constant1 = constant;
          if (!this.latticeConstants[i1].equals(constant)) {
            i = b;
            break;
          } 
        } 
      } else {
        i = n;
        break;
      } 
      j++;
      constant = constant1;
    } 
    if (setLatticeValueTo(k, i, constant))
      addUsersToWorklist(k, i); 
  }
  
  private void simulatePhiBlock(SsaBasicBlock paramSsaBasicBlock) {
    for (SsaInsn ssaInsn : paramSsaBasicBlock.getInsns()) {
      if (ssaInsn instanceof PhiInsn)
        simulatePhi((PhiInsn)ssaInsn); 
    } 
  }
  
  private void simulateStmt(SsaInsn paramSsaInsn) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getOriginalRopInsn : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Insn;
    //   4: astore #7
    //   6: aload #7
    //   8: invokevirtual getOpcode : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;
    //   11: invokevirtual getBranchingness : ()I
    //   14: istore_3
    //   15: iconst_1
    //   16: istore_2
    //   17: iload_3
    //   18: iconst_1
    //   19: if_icmpne -> 33
    //   22: aload #7
    //   24: invokevirtual getOpcode : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;
    //   27: invokevirtual isCallLike : ()Z
    //   30: ifeq -> 38
    //   33: aload_0
    //   34: aload_1
    //   35: invokespecial simulateBranch : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/ssa/SsaInsn;)V
    //   38: aload_1
    //   39: invokevirtual getOpcode : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;
    //   42: invokevirtual getOpcode : ()I
    //   45: istore #4
    //   47: aload_1
    //   48: invokevirtual getResult : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;
    //   51: astore #6
    //   53: aload #6
    //   55: astore #5
    //   57: aload #6
    //   59: ifnonnull -> 102
    //   62: iload #4
    //   64: bipush #17
    //   66: if_icmpeq -> 80
    //   69: iload #4
    //   71: bipush #18
    //   73: if_icmpne -> 79
    //   76: goto -> 80
    //   79: return
    //   80: aload_1
    //   81: invokevirtual getBlock : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/ssa/SsaBasicBlock;
    //   84: invokevirtual getPrimarySuccessor : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/ssa/SsaBasicBlock;
    //   87: invokevirtual getInsns : ()Ljava/util/ArrayList;
    //   90: iconst_0
    //   91: invokevirtual get : (I)Ljava/lang/Object;
    //   94: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/ssa/SsaInsn
    //   97: invokevirtual getResult : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;
    //   100: astore #5
    //   102: aload #5
    //   104: invokevirtual getReg : ()I
    //   107: istore_3
    //   108: aconst_null
    //   109: astore #6
    //   111: iload #4
    //   113: iconst_2
    //   114: if_icmpeq -> 278
    //   117: iload #4
    //   119: iconst_5
    //   120: if_icmpeq -> 266
    //   123: iload #4
    //   125: bipush #56
    //   127: if_icmpeq -> 236
    //   130: iload #4
    //   132: tableswitch default -> 168, 14 -> 215, 15 -> 215, 16 -> 215, 17 -> 215, 18 -> 215
    //   168: iload #4
    //   170: tableswitch default -> 208, 20 -> 215, 21 -> 215, 22 -> 215, 23 -> 215, 24 -> 215, 25 -> 215
    //   208: aload #6
    //   210: astore #5
    //   212: goto -> 325
    //   215: aload_0
    //   216: aload_1
    //   217: aload #5
    //   219: invokevirtual getBasicType : ()I
    //   222: invokespecial simulateMath : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/ssa/SsaInsn;I)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/Constant;
    //   225: astore_1
    //   226: aload_1
    //   227: astore #5
    //   229: aload_1
    //   230: ifnull -> 325
    //   233: goto -> 330
    //   236: aload_0
    //   237: getfield latticeValues : [I
    //   240: astore_1
    //   241: aload #6
    //   243: astore #5
    //   245: aload_1
    //   246: iload_3
    //   247: iaload
    //   248: iconst_1
    //   249: if_icmpne -> 325
    //   252: aload_1
    //   253: iload_3
    //   254: iaload
    //   255: istore_2
    //   256: aload_0
    //   257: getfield latticeConstants : [Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/Constant;
    //   260: iload_3
    //   261: aaload
    //   262: astore_1
    //   263: goto -> 330
    //   266: aload #7
    //   268: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/CstInsn
    //   271: invokevirtual getConstant : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/Constant;
    //   274: astore_1
    //   275: goto -> 330
    //   278: aload #6
    //   280: astore #5
    //   282: aload_1
    //   283: invokevirtual getSources : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;
    //   286: invokevirtual size : ()I
    //   289: iconst_1
    //   290: if_icmpne -> 325
    //   293: aload_1
    //   294: invokevirtual getSources : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;
    //   297: iconst_0
    //   298: invokevirtual get : (I)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;
    //   301: invokevirtual getReg : ()I
    //   304: istore #4
    //   306: aload_0
    //   307: getfield latticeValues : [I
    //   310: iload #4
    //   312: iaload
    //   313: istore_2
    //   314: aload_0
    //   315: getfield latticeConstants : [Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/Constant;
    //   318: iload #4
    //   320: aaload
    //   321: astore_1
    //   322: goto -> 330
    //   325: iconst_2
    //   326: istore_2
    //   327: aload #5
    //   329: astore_1
    //   330: aload_0
    //   331: iload_3
    //   332: iload_2
    //   333: aload_1
    //   334: invokespecial setLatticeValueTo : (IILorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/Constant;)Z
    //   337: ifeq -> 346
    //   340: aload_0
    //   341: iload_3
    //   342: iload_2
    //   343: invokespecial addUsersToWorklist : (II)V
    //   346: return
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\ssa\SCCP.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
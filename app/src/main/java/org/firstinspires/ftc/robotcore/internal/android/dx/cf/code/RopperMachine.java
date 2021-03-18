package org.firstinspires.ftc.robotcore.internal.android.dx.cf.code;

import java.util.ArrayList;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.Method;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.MethodList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.AccessFlags;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.Insn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.Rop;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.SourcePosition;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.TranslationAdvice;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstMethodRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstNat;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeList;

final class RopperMachine extends ValueAwareMachine {
  private static final CstType ARRAY_REFLECT_TYPE = new CstType(Type.internClassName("java/lang/reflect/Array"));
  
  private static final CstMethodRef MULTIANEWARRAY_METHOD = new CstMethodRef(ARRAY_REFLECT_TYPE, new CstNat(new CstString("newInstance"), new CstString("(Ljava/lang/Class;[I)Ljava/lang/Object;")));
  
  private final TranslationAdvice advice;
  
  private boolean blockCanThrow;
  
  private TypeList catches;
  
  private boolean catchesUsed;
  
  private int extraBlockCount;
  
  private boolean hasJsr;
  
  private final ArrayList<Insn> insns;
  
  private final int maxLocals;
  
  private final ConcreteMethod method;
  
  private final MethodList methods;
  
  private int primarySuccessorIndex;
  
  private ReturnAddress returnAddress;
  
  private Rop returnOp;
  
  private SourcePosition returnPosition;
  
  private boolean returns;
  
  private final Ropper ropper;
  
  public RopperMachine(Ropper paramRopper, ConcreteMethod paramConcreteMethod, TranslationAdvice paramTranslationAdvice, MethodList paramMethodList) {
    super(paramConcreteMethod.getEffectiveDescriptor());
    if (paramMethodList != null) {
      if (paramRopper != null) {
        if (paramTranslationAdvice != null) {
          this.ropper = paramRopper;
          this.method = paramConcreteMethod;
          this.methods = paramMethodList;
          this.advice = paramTranslationAdvice;
          this.maxLocals = paramConcreteMethod.getMaxLocals();
          this.insns = new ArrayList<Insn>(25);
          this.catches = null;
          this.catchesUsed = false;
          this.returns = false;
          this.primarySuccessorIndex = -1;
          this.extraBlockCount = 0;
          this.blockCanThrow = false;
          this.returnOp = null;
          this.returnPosition = null;
          return;
        } 
        throw new NullPointerException("advice == null");
      } 
      throw new NullPointerException("ropper == null");
    } 
    throw new NullPointerException("methods == null");
  }
  
  private RegisterSpecList getSources(int paramInt1, int paramInt2) {
    RegisterSpecList registerSpecList;
    int j = argCount();
    if (j == 0)
      return RegisterSpecList.EMPTY; 
    int i = getLocalIndex();
    if (i >= 0) {
      registerSpecList = new RegisterSpecList(1);
      registerSpecList.set(0, RegisterSpec.make(i, arg(0)));
    } else {
      registerSpecList = new RegisterSpecList(j);
      boolean bool = false;
      i = paramInt2;
      for (paramInt2 = bool; paramInt2 < j; paramInt2++) {
        RegisterSpec registerSpec = RegisterSpec.make(i, arg(paramInt2));
        registerSpecList.set(paramInt2, registerSpec);
        i += registerSpec.getCategory();
      } 
      if (paramInt1 != 79) {
        if (paramInt1 == 181)
          if (j == 2) {
            RegisterSpec registerSpec = registerSpecList.get(0);
            registerSpecList.set(0, registerSpecList.get(1));
            registerSpecList.set(1, registerSpec);
          } else {
            throw new RuntimeException("shouldn't happen");
          }  
      } else {
        if (j == 3) {
          RegisterSpec registerSpec1 = registerSpecList.get(0);
          RegisterSpec registerSpec2 = registerSpecList.get(1);
          registerSpecList.set(0, registerSpecList.get(2));
          registerSpecList.set(1, registerSpec1);
          registerSpecList.set(2, registerSpec2);
          registerSpecList.setImmutable();
          return registerSpecList;
        } 
        throw new RuntimeException("shouldn't happen");
      } 
    } 
    registerSpecList.setImmutable();
    return registerSpecList;
  }
  
  private int jopToRopOpcode(int paramInt, Constant paramConstant) {
    if (paramInt != 0)
      if (paramInt != 20)
        if (paramInt != 21) {
          if (paramInt != 171) {
            if (paramInt != 172) {
              if (paramInt != 198)
                if (paramInt != 199) {
                  CstMethodRef cstMethodRef;
                  switch (paramInt) {
                    default:
                      switch (paramInt) {
                        default:
                          switch (paramInt) {
                            default:
                              switch (paramInt) {
                                default:
                                  throw new RuntimeException("shouldn't happen");
                                case 195:
                                  return 37;
                                case 194:
                                  return 36;
                                case 193:
                                  return 44;
                                case 192:
                                  return 43;
                                case 191:
                                  return 35;
                                case 190:
                                  return 34;
                                case 188:
                                case 189:
                                  return 41;
                                case 187:
                                  break;
                              } 
                              return 40;
                            case 185:
                              return 53;
                            case 184:
                              return 49;
                            case 183:
                              cstMethodRef = (CstMethodRef)paramConstant;
                              return (!cstMethodRef.isInstanceInit() && !cstMethodRef.getDefiningClass().equals(this.method.getDefiningClass())) ? (!this.method.getAccSuper() ? 52 : 51) : 52;
                            case 182:
                              cstMethodRef = cstMethodRef;
                              if (cstMethodRef.getDefiningClass().equals(this.method.getDefiningClass()))
                                for (paramInt = 0; paramInt < this.methods.size(); paramInt++) {
                                  Method method = this.methods.get(paramInt);
                                  if (AccessFlags.isPrivate(method.getAccessFlags()) && cstMethodRef.getNat().equals(method.getNat()))
                                    return 52; 
                                }  
                              return 50;
                            case 181:
                              return 47;
                            case 180:
                              return 45;
                            case 179:
                              return 48;
                            case 178:
                              return 46;
                            case 177:
                              break;
                          } 
                          return 33;
                        case 167:
                          return 6;
                        case 158:
                        case 164:
                          return 11;
                        case 157:
                        case 163:
                          return 12;
                        case 156:
                        case 162:
                          return 10;
                        case 155:
                        case 161:
                          return 9;
                        case 150:
                        case 152:
                          return 28;
                        case 148:
                        case 149:
                        case 151:
                          return 27;
                        case 147:
                          return 32;
                        case 146:
                          return 31;
                        case 145:
                          return 30;
                        case 133:
                        case 134:
                        case 135:
                        case 136:
                        case 137:
                        case 138:
                        case 139:
                        case 140:
                        case 141:
                        case 142:
                        case 143:
                        case 144:
                          return 29;
                        case 132:
                          return 14;
                        case 154:
                        case 160:
                        case 166:
                          return 8;
                        case 153:
                        case 159:
                        case 165:
                          break;
                      } 
                      return 7;
                    case 130:
                      return 22;
                    case 128:
                      return 21;
                    case 126:
                      return 20;
                    case 124:
                      return 25;
                    case 122:
                      return 24;
                    case 120:
                      return 23;
                    case 116:
                      return 19;
                    case 112:
                      return 18;
                    case 108:
                      return 17;
                    case 104:
                      return 16;
                    case 100:
                      return 15;
                    case 96:
                    
                    case 79:
                      return 39;
                    case 46:
                      return 38;
                    case 54:
                      return 2;
                    case 18:
                      return 5;
                    case 0:
                      break;
                  } 
                  return 1;
                }  
              return 7;
            } 
            return 33;
          } 
          return 13;
        }   
    return 1;
  }
  
  private void updateReturnOp(Rop paramRop, SourcePosition paramSourcePosition) {
    if (paramRop != null) {
      if (paramSourcePosition != null) {
        Rop rop = this.returnOp;
        if (rop == null) {
          this.returnOp = paramRop;
          this.returnPosition = paramSourcePosition;
          return;
        } 
        if (rop == paramRop) {
          if (paramSourcePosition.getLine() > this.returnPosition.getLine())
            this.returnPosition = paramSourcePosition; 
          return;
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("return op mismatch: ");
        stringBuilder.append(paramRop);
        stringBuilder.append(", ");
        stringBuilder.append(this.returnOp);
        throw new SimException(stringBuilder.toString());
      } 
      throw new NullPointerException("pos == null");
    } 
    throw new NullPointerException("op == null");
  }
  
  public boolean canThrow() {
    return this.blockCanThrow;
  }
  
  public int getExtraBlockCount() {
    return this.extraBlockCount;
  }
  
  public ArrayList<Insn> getInsns() {
    return this.insns;
  }
  
  public int getPrimarySuccessorIndex() {
    return this.primarySuccessorIndex;
  }
  
  public ReturnAddress getReturnAddress() {
    return this.returnAddress;
  }
  
  public Rop getReturnOp() {
    return this.returnOp;
  }
  
  public SourcePosition getReturnPosition() {
    return this.returnPosition;
  }
  
  public boolean hasJsr() {
    return this.hasJsr;
  }
  
  public boolean hasRet() {
    return (this.returnAddress != null);
  }
  
  public boolean returns() {
    return this.returns;
  }
  
  public void run(Frame paramFrame, int paramInt1, int paramInt2) {
    // Byte code:
    //   0: iload_3
    //   1: istore #4
    //   3: aload_0
    //   4: getfield maxLocals : I
    //   7: aload_1
    //   8: invokevirtual getStack : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/ExecutionStack;
    //   11: invokevirtual size : ()I
    //   14: iadd
    //   15: istore #5
    //   17: aload_0
    //   18: iload #4
    //   20: iload #5
    //   22: invokespecial getSources : (II)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;
    //   25: astore #8
    //   27: aload #8
    //   29: invokevirtual size : ()I
    //   32: istore #6
    //   34: aload_0
    //   35: aload_1
    //   36: iload_2
    //   37: iload_3
    //   38: invokespecial run : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/Frame;II)V
    //   41: aload_0
    //   42: getfield method : Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/ConcreteMethod;
    //   45: iload_2
    //   46: invokevirtual makeSourcePosistion : (I)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;
    //   49: astore #10
    //   51: iload #4
    //   53: bipush #54
    //   55: if_icmpne -> 64
    //   58: iconst_1
    //   59: istore #7
    //   61: goto -> 67
    //   64: iconst_0
    //   65: istore #7
    //   67: aload_0
    //   68: iload #7
    //   70: invokevirtual getLocalTarget : (Z)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;
    //   73: astore_1
    //   74: aload_0
    //   75: invokevirtual resultCount : ()I
    //   78: istore_2
    //   79: iload_2
    //   80: ifne -> 103
    //   83: iload #4
    //   85: bipush #87
    //   87: if_icmpeq -> 102
    //   90: iload #4
    //   92: bipush #88
    //   94: if_icmpeq -> 102
    //   97: aconst_null
    //   98: astore_1
    //   99: goto -> 129
    //   102: return
    //   103: aload_1
    //   104: ifnull -> 110
    //   107: goto -> 129
    //   110: iload_2
    //   111: iconst_1
    //   112: if_icmpne -> 1349
    //   115: iload #5
    //   117: aload_0
    //   118: iconst_0
    //   119: invokevirtual result : (I)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeBearer;
    //   122: invokestatic make : (ILorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeBearer;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;
    //   125: astore_1
    //   126: goto -> 107
    //   129: aload_1
    //   130: ifnull -> 139
    //   133: aload_1
    //   134: astore #9
    //   136: goto -> 144
    //   139: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type.VOID : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type;
    //   142: astore #9
    //   144: aload_0
    //   145: invokevirtual getAuxCst : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/Constant;
    //   148: astore #11
    //   150: iload #4
    //   152: sipush #197
    //   155: if_icmpne -> 522
    //   158: aload_0
    //   159: iconst_1
    //   160: putfield blockCanThrow : Z
    //   163: aload_0
    //   164: bipush #6
    //   166: putfield extraBlockCount : I
    //   169: aload_1
    //   170: invokevirtual getNextReg : ()I
    //   173: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type.INT_ARRAY : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type;
    //   176: invokestatic make : (ILorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeBearer;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;
    //   179: astore #13
    //   181: new org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/ThrowingCstInsn
    //   184: dup
    //   185: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type.INT_ARRAY : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type;
    //   188: iload #6
    //   190: invokestatic opFilledNewArray : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeBearer;I)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;
    //   193: aload #10
    //   195: aload #8
    //   197: aload_0
    //   198: getfield catches : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeList;
    //   201: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType.INT_ARRAY : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType;
    //   204: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeList;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/Constant;)V
    //   207: astore #8
    //   209: aload_0
    //   210: getfield insns : Ljava/util/ArrayList;
    //   213: aload #8
    //   215: invokevirtual add : (Ljava/lang/Object;)Z
    //   218: pop
    //   219: new org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/PlainInsn
    //   222: dup
    //   223: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type.INT_ARRAY : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type;
    //   226: invokestatic opMoveResult : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeBearer;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;
    //   229: aload #10
    //   231: aload #13
    //   233: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList.EMPTY : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;
    //   236: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;)V
    //   239: astore #8
    //   241: aload_0
    //   242: getfield insns : Ljava/util/ArrayList;
    //   245: aload #8
    //   247: invokevirtual add : (Ljava/lang/Object;)Z
    //   250: pop
    //   251: aload #11
    //   253: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType
    //   256: invokevirtual getClassType : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type;
    //   259: astore #8
    //   261: iconst_0
    //   262: istore_2
    //   263: iload_2
    //   264: iload #6
    //   266: if_icmpge -> 283
    //   269: aload #8
    //   271: invokevirtual getComponentType : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type;
    //   274: astore #8
    //   276: iload_2
    //   277: iconst_1
    //   278: iadd
    //   279: istore_2
    //   280: goto -> 263
    //   283: aload_1
    //   284: invokevirtual getReg : ()I
    //   287: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type.CLASS : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type;
    //   290: invokestatic make : (ILorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeBearer;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;
    //   293: astore #14
    //   295: aload #8
    //   297: invokevirtual isPrimitive : ()Z
    //   300: ifeq -> 336
    //   303: aload #8
    //   305: invokestatic forPrimitiveType : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstFieldRef;
    //   308: astore #8
    //   310: new org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/ThrowingCstInsn
    //   313: dup
    //   314: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rops.GET_STATIC_OBJECT : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;
    //   317: aload #10
    //   319: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList.EMPTY : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;
    //   322: aload_0
    //   323: getfield catches : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeList;
    //   326: aload #8
    //   328: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeList;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/Constant;)V
    //   331: astore #8
    //   333: goto -> 366
    //   336: new org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/ThrowingCstInsn
    //   339: dup
    //   340: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rops.CONST_OBJECT : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;
    //   343: aload #10
    //   345: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList.EMPTY : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;
    //   348: aload_0
    //   349: getfield catches : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeList;
    //   352: new org/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType
    //   355: dup
    //   356: aload #8
    //   358: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type;)V
    //   361: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeList;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/Constant;)V
    //   364: astore #8
    //   366: aload #10
    //   368: astore #12
    //   370: aload_0
    //   371: getfield insns : Ljava/util/ArrayList;
    //   374: aload #8
    //   376: invokevirtual add : (Ljava/lang/Object;)Z
    //   379: pop
    //   380: new org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/PlainInsn
    //   383: dup
    //   384: aload #14
    //   386: invokevirtual getType : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type;
    //   389: invokestatic opMoveResultPseudo : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeBearer;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;
    //   392: aload #12
    //   394: aload #14
    //   396: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList.EMPTY : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;
    //   399: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;)V
    //   402: astore #8
    //   404: aload_0
    //   405: getfield insns : Ljava/util/ArrayList;
    //   408: aload #8
    //   410: invokevirtual add : (Ljava/lang/Object;)Z
    //   413: pop
    //   414: aload_1
    //   415: invokevirtual getReg : ()I
    //   418: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type.OBJECT : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type;
    //   421: invokestatic make : (ILorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeBearer;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;
    //   424: astore #8
    //   426: new org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/ThrowingCstInsn
    //   429: dup
    //   430: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/cf/code/RopperMachine.MULTIANEWARRAY_METHOD : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstMethodRef;
    //   433: invokevirtual getPrototype : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Prototype;
    //   436: invokestatic opInvokeStatic : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Prototype;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;
    //   439: aload #12
    //   441: aload #14
    //   443: aload #13
    //   445: invokestatic make : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;
    //   448: aload_0
    //   449: getfield catches : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeList;
    //   452: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/cf/code/RopperMachine.MULTIANEWARRAY_METHOD : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstMethodRef;
    //   455: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeList;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/Constant;)V
    //   458: astore #13
    //   460: aload_0
    //   461: getfield insns : Ljava/util/ArrayList;
    //   464: aload #13
    //   466: invokevirtual add : (Ljava/lang/Object;)Z
    //   469: pop
    //   470: new org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/PlainInsn
    //   473: dup
    //   474: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/cf/code/RopperMachine.MULTIANEWARRAY_METHOD : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstMethodRef;
    //   477: invokevirtual getPrototype : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Prototype;
    //   480: invokevirtual getReturnType : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type;
    //   483: invokestatic opMoveResult : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeBearer;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;
    //   486: aload #12
    //   488: aload #8
    //   490: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList.EMPTY : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;
    //   493: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;)V
    //   496: astore #12
    //   498: aload_0
    //   499: getfield insns : Ljava/util/ArrayList;
    //   502: aload #12
    //   504: invokevirtual add : (Ljava/lang/Object;)Z
    //   507: pop
    //   508: sipush #192
    //   511: istore_3
    //   512: aload #8
    //   514: invokestatic make : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;
    //   517: astore #8
    //   519: goto -> 573
    //   522: iload #4
    //   524: sipush #168
    //   527: if_icmpne -> 536
    //   530: aload_0
    //   531: iconst_1
    //   532: putfield hasJsr : Z
    //   535: return
    //   536: iload #4
    //   538: istore_3
    //   539: iload #4
    //   541: sipush #169
    //   544: if_icmpne -> 573
    //   547: aload_0
    //   548: aload_0
    //   549: iconst_0
    //   550: invokevirtual arg : (I)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeBearer;
    //   553: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/cf/code/ReturnAddress
    //   556: putfield returnAddress : Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/ReturnAddress;
    //   559: return
    //   560: astore_1
    //   561: new java/lang/RuntimeException
    //   564: dup
    //   565: ldc_w 'Argument to RET was not a ReturnAddress'
    //   568: aload_1
    //   569: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   572: athrow
    //   573: aload #10
    //   575: astore #13
    //   577: aload #9
    //   579: astore #14
    //   581: aload_0
    //   582: iload_3
    //   583: aload #11
    //   585: invokespecial jopToRopOpcode : (ILorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/Constant;)I
    //   588: istore_2
    //   589: iload_2
    //   590: aload #14
    //   592: aload #8
    //   594: aload #11
    //   596: invokestatic ropFor : (ILorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeBearer;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeList;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/Constant;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;
    //   599: astore #12
    //   601: aload_1
    //   602: ifnull -> 660
    //   605: aload #12
    //   607: invokevirtual isCallLike : ()Z
    //   610: ifeq -> 660
    //   613: aload_0
    //   614: aload_0
    //   615: getfield extraBlockCount : I
    //   618: iconst_1
    //   619: iadd
    //   620: putfield extraBlockCount : I
    //   623: new org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/PlainInsn
    //   626: dup
    //   627: aload #11
    //   629: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstMethodRef
    //   632: invokevirtual getPrototype : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Prototype;
    //   635: invokevirtual getReturnType : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type;
    //   638: invokestatic opMoveResult : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeBearer;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;
    //   641: aload #13
    //   643: aload_1
    //   644: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList.EMPTY : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;
    //   647: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;)V
    //   650: astore_1
    //   651: aconst_null
    //   652: astore #10
    //   654: aload_1
    //   655: astore #9
    //   657: goto -> 712
    //   660: aload_1
    //   661: ifnull -> 706
    //   664: aload #12
    //   666: invokevirtual canThrow : ()Z
    //   669: ifeq -> 706
    //   672: aload_0
    //   673: aload_0
    //   674: getfield extraBlockCount : I
    //   677: iconst_1
    //   678: iadd
    //   679: putfield extraBlockCount : I
    //   682: new org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/PlainInsn
    //   685: dup
    //   686: aload_1
    //   687: invokevirtual getTypeBearer : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeBearer;
    //   690: invokestatic opMoveResultPseudo : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeBearer;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;
    //   693: aload #13
    //   695: aload_1
    //   696: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList.EMPTY : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;
    //   699: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;)V
    //   702: astore_1
    //   703: goto -> 651
    //   706: aconst_null
    //   707: astore #9
    //   709: aload_1
    //   710: astore #10
    //   712: iload_2
    //   713: bipush #41
    //   715: if_icmpne -> 742
    //   718: aload #12
    //   720: invokevirtual getResult : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type;
    //   723: invokestatic intern : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType;
    //   726: astore #11
    //   728: aload #8
    //   730: astore_1
    //   731: aload #11
    //   733: astore #8
    //   735: aload #12
    //   737: astore #11
    //   739: goto -> 926
    //   742: aload #11
    //   744: ifnonnull -> 915
    //   747: iload #6
    //   749: iconst_2
    //   750: if_icmpne -> 915
    //   753: aload #8
    //   755: iconst_0
    //   756: invokevirtual get : (I)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;
    //   759: invokevirtual getTypeBearer : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeBearer;
    //   762: astore_1
    //   763: aload #8
    //   765: iconst_1
    //   766: invokevirtual get : (I)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;
    //   769: invokevirtual getTypeBearer : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeBearer;
    //   772: astore #15
    //   774: aload #15
    //   776: invokeinterface isConstant : ()Z
    //   781: ifne -> 793
    //   784: aload_1
    //   785: invokeinterface isConstant : ()Z
    //   790: ifeq -> 915
    //   793: aload_0
    //   794: getfield advice : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/TranslationAdvice;
    //   797: aload #12
    //   799: aload #8
    //   801: iconst_0
    //   802: invokevirtual get : (I)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;
    //   805: aload #8
    //   807: iconst_1
    //   808: invokevirtual get : (I)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;
    //   811: invokeinterface hasConstantOperation : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;)Z
    //   816: ifeq -> 915
    //   819: aload #15
    //   821: invokeinterface isConstant : ()Z
    //   826: ifeq -> 879
    //   829: aload #15
    //   831: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/Constant
    //   834: astore_1
    //   835: aload #8
    //   837: invokevirtual withoutLast : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;
    //   840: astore #11
    //   842: aload #11
    //   844: astore #8
    //   846: aload #12
    //   848: invokevirtual getOpcode : ()I
    //   851: bipush #15
    //   853: if_icmpne -> 891
    //   856: aload #15
    //   858: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstInteger
    //   861: invokevirtual getValue : ()I
    //   864: ineg
    //   865: invokestatic make : (I)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstInteger;
    //   868: astore_1
    //   869: bipush #14
    //   871: istore_2
    //   872: aload #11
    //   874: astore #8
    //   876: goto -> 891
    //   879: aload_1
    //   880: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/Constant
    //   883: astore_1
    //   884: aload #8
    //   886: invokevirtual withoutFirst : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;
    //   889: astore #8
    //   891: iload_2
    //   892: aload #14
    //   894: aload #8
    //   896: aload_1
    //   897: invokestatic ropFor : (ILorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeBearer;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeList;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/Constant;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;
    //   900: astore #11
    //   902: aload #8
    //   904: astore #12
    //   906: aload_1
    //   907: astore #8
    //   909: aload #12
    //   911: astore_1
    //   912: goto -> 926
    //   915: aload #8
    //   917: astore_1
    //   918: aload #11
    //   920: astore #8
    //   922: aload #12
    //   924: astore #11
    //   926: aload_0
    //   927: invokevirtual getAuxCases : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/SwitchList;
    //   930: astore #14
    //   932: aload_0
    //   933: invokevirtual getInitValues : ()Ljava/util/ArrayList;
    //   936: astore #12
    //   938: aload #11
    //   940: invokevirtual canThrow : ()Z
    //   943: istore #7
    //   945: aload_0
    //   946: aload_0
    //   947: getfield blockCanThrow : Z
    //   950: iload #7
    //   952: ior
    //   953: putfield blockCanThrow : Z
    //   956: aload #14
    //   958: ifnull -> 1030
    //   961: aload #14
    //   963: invokevirtual size : ()I
    //   966: ifne -> 994
    //   969: new org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/PlainInsn
    //   972: dup
    //   973: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rops.GOTO : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;
    //   976: aload #13
    //   978: aconst_null
    //   979: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList.EMPTY : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;
    //   982: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;)V
    //   985: astore_1
    //   986: aload_0
    //   987: iconst_0
    //   988: putfield primarySuccessorIndex : I
    //   991: goto -> 1275
    //   994: aload #14
    //   996: invokevirtual getValues : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/IntList;
    //   999: astore #14
    //   1001: new org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SwitchInsn
    //   1004: dup
    //   1005: aload #11
    //   1007: aload #13
    //   1009: aload #10
    //   1011: aload_1
    //   1012: aload #14
    //   1014: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/IntList;)V
    //   1017: astore_1
    //   1018: aload_0
    //   1019: aload #14
    //   1021: invokevirtual size : ()I
    //   1024: putfield primarySuccessorIndex : I
    //   1027: goto -> 1275
    //   1030: iload_2
    //   1031: bipush #33
    //   1033: if_icmpne -> 1132
    //   1036: aload_1
    //   1037: invokevirtual size : ()I
    //   1040: ifeq -> 1094
    //   1043: aload_1
    //   1044: iconst_0
    //   1045: invokevirtual get : (I)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;
    //   1048: astore_1
    //   1049: aload_1
    //   1050: invokevirtual getTypeBearer : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeBearer;
    //   1053: astore #10
    //   1055: aload_1
    //   1056: invokevirtual getReg : ()I
    //   1059: ifeq -> 1094
    //   1062: aload_0
    //   1063: getfield insns : Ljava/util/ArrayList;
    //   1066: new org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/PlainInsn
    //   1069: dup
    //   1070: aload #10
    //   1072: invokestatic opMove : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeBearer;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;
    //   1075: aload #13
    //   1077: iconst_0
    //   1078: aload #10
    //   1080: invokestatic make : (ILorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeBearer;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;
    //   1083: aload_1
    //   1084: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;)V
    //   1087: invokevirtual add : (Ljava/lang/Object;)Z
    //   1090: pop
    //   1091: goto -> 1094
    //   1094: new org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/PlainInsn
    //   1097: dup
    //   1098: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rops.GOTO : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;
    //   1101: aload #13
    //   1103: aconst_null
    //   1104: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList.EMPTY : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;
    //   1107: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;)V
    //   1110: astore_1
    //   1111: aload_0
    //   1112: iconst_0
    //   1113: putfield primarySuccessorIndex : I
    //   1116: aload_0
    //   1117: aload #11
    //   1119: aload #13
    //   1121: invokespecial updateReturnOp : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;)V
    //   1124: aload_0
    //   1125: iconst_1
    //   1126: putfield returns : Z
    //   1129: goto -> 1027
    //   1132: aload #8
    //   1134: ifnull -> 1202
    //   1137: iload #7
    //   1139: ifeq -> 1182
    //   1142: new org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/ThrowingCstInsn
    //   1145: dup
    //   1146: aload #11
    //   1148: aload #13
    //   1150: aload_1
    //   1151: aload_0
    //   1152: getfield catches : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeList;
    //   1155: aload #8
    //   1157: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeList;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/Constant;)V
    //   1160: astore_1
    //   1161: aload_0
    //   1162: iconst_1
    //   1163: putfield catchesUsed : Z
    //   1166: aload_0
    //   1167: aload_0
    //   1168: getfield catches : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeList;
    //   1171: invokeinterface size : ()I
    //   1176: putfield primarySuccessorIndex : I
    //   1179: goto -> 1275
    //   1182: new org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/PlainCstInsn
    //   1185: dup
    //   1186: aload #11
    //   1188: aload #13
    //   1190: aload #10
    //   1192: aload_1
    //   1193: aload #8
    //   1195: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/Constant;)V
    //   1198: astore_1
    //   1199: goto -> 1275
    //   1202: iload #7
    //   1204: ifeq -> 1260
    //   1207: new org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/ThrowingInsn
    //   1210: dup
    //   1211: aload #11
    //   1213: aload #13
    //   1215: aload_1
    //   1216: aload_0
    //   1217: getfield catches : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeList;
    //   1220: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeList;)V
    //   1223: astore_1
    //   1224: aload_0
    //   1225: iconst_1
    //   1226: putfield catchesUsed : Z
    //   1229: iload_3
    //   1230: sipush #191
    //   1233: if_icmpne -> 1244
    //   1236: aload_0
    //   1237: iconst_m1
    //   1238: putfield primarySuccessorIndex : I
    //   1241: goto -> 1027
    //   1244: aload_0
    //   1245: aload_0
    //   1246: getfield catches : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeList;
    //   1249: invokeinterface size : ()I
    //   1254: putfield primarySuccessorIndex : I
    //   1257: goto -> 1027
    //   1260: new org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/PlainInsn
    //   1263: dup
    //   1264: aload #11
    //   1266: aload #13
    //   1268: aload #10
    //   1270: aload_1
    //   1271: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;)V
    //   1274: astore_1
    //   1275: aload_0
    //   1276: getfield insns : Ljava/util/ArrayList;
    //   1279: aload_1
    //   1280: invokevirtual add : (Ljava/lang/Object;)Z
    //   1283: pop
    //   1284: aload #9
    //   1286: ifnull -> 1299
    //   1289: aload_0
    //   1290: getfield insns : Ljava/util/ArrayList;
    //   1293: aload #9
    //   1295: invokevirtual add : (Ljava/lang/Object;)Z
    //   1298: pop
    //   1299: aload #12
    //   1301: ifnull -> 1348
    //   1304: aload_0
    //   1305: aload_0
    //   1306: getfield extraBlockCount : I
    //   1309: iconst_1
    //   1310: iadd
    //   1311: putfield extraBlockCount : I
    //   1314: new org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/FillArrayDataInsn
    //   1317: dup
    //   1318: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rops.FILL_ARRAY_DATA : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;
    //   1321: aload #13
    //   1323: aload #9
    //   1325: invokevirtual getResult : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;
    //   1328: invokestatic make : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;
    //   1331: aload #12
    //   1333: aload #8
    //   1335: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpecList;Ljava/util/ArrayList;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/Constant;)V
    //   1338: astore_1
    //   1339: aload_0
    //   1340: getfield insns : Ljava/util/ArrayList;
    //   1343: aload_1
    //   1344: invokevirtual add : (Ljava/lang/Object;)Z
    //   1347: pop
    //   1348: return
    //   1349: iconst_0
    //   1350: istore_2
    //   1351: aload_0
    //   1352: getfield ropper : Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/Ropper;
    //   1355: invokevirtual getFirstTempStackReg : ()I
    //   1358: istore_3
    //   1359: iload #6
    //   1361: anewarray org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec
    //   1364: astore_1
    //   1365: iload_2
    //   1366: iload #6
    //   1368: if_icmpge -> 1440
    //   1371: aload #8
    //   1373: iload_2
    //   1374: invokevirtual get : (I)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;
    //   1377: astore #9
    //   1379: aload #9
    //   1381: invokevirtual getTypeBearer : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeBearer;
    //   1384: astore #11
    //   1386: aload #9
    //   1388: iload_3
    //   1389: invokevirtual withReg : (I)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;
    //   1392: astore #12
    //   1394: aload_0
    //   1395: getfield insns : Ljava/util/ArrayList;
    //   1398: new org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/PlainInsn
    //   1401: dup
    //   1402: aload #11
    //   1404: invokestatic opMove : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeBearer;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;
    //   1407: aload #10
    //   1409: aload #12
    //   1411: aload #9
    //   1413: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;)V
    //   1416: invokevirtual add : (Ljava/lang/Object;)Z
    //   1419: pop
    //   1420: aload_1
    //   1421: iload_2
    //   1422: aload #12
    //   1424: aastore
    //   1425: iload_3
    //   1426: aload #9
    //   1428: invokevirtual getCategory : ()I
    //   1431: iadd
    //   1432: istore_3
    //   1433: iload_2
    //   1434: iconst_1
    //   1435: iadd
    //   1436: istore_2
    //   1437: goto -> 1365
    //   1440: aload_0
    //   1441: invokevirtual getAuxInt : ()I
    //   1444: istore_2
    //   1445: iload #5
    //   1447: istore_3
    //   1448: iload_2
    //   1449: ifeq -> 1519
    //   1452: aload_1
    //   1453: iload_2
    //   1454: bipush #15
    //   1456: iand
    //   1457: iconst_1
    //   1458: isub
    //   1459: aaload
    //   1460: astore #8
    //   1462: aload #8
    //   1464: invokevirtual getTypeBearer : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeBearer;
    //   1467: astore #9
    //   1469: aload_0
    //   1470: getfield insns : Ljava/util/ArrayList;
    //   1473: new org/firstinspires/ftc/robotcore/internal/android/dx/rop/code/PlainInsn
    //   1476: dup
    //   1477: aload #9
    //   1479: invokestatic opMove : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/TypeBearer;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;
    //   1482: aload #10
    //   1484: aload #8
    //   1486: iload_3
    //   1487: invokevirtual withReg : (I)Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;
    //   1490: aload #8
    //   1492: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/SourcePosition;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;)V
    //   1495: invokevirtual add : (Ljava/lang/Object;)Z
    //   1498: pop
    //   1499: iload_3
    //   1500: aload #9
    //   1502: invokeinterface getType : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type;
    //   1507: invokevirtual getCategory : ()I
    //   1510: iadd
    //   1511: istore_3
    //   1512: iload_2
    //   1513: iconst_4
    //   1514: ishr
    //   1515: istore_2
    //   1516: goto -> 1448
    //   1519: return
    // Exception table:
    //   from	to	target	type
    //   547	559	560	java/lang/ClassCastException
  }
  
  public void startBlock(TypeList paramTypeList) {
    this.catches = paramTypeList;
    this.insns.clear();
    this.catchesUsed = false;
    this.returns = false;
    this.primarySuccessorIndex = 0;
    this.extraBlockCount = 0;
    this.blockCanThrow = false;
    this.hasJsr = false;
    this.returnAddress = null;
  }
  
  public boolean wereCatchesUsed() {
    return this.catchesUsed;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\code\RopperMachine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
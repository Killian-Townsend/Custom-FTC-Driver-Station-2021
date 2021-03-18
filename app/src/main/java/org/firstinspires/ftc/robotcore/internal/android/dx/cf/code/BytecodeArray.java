package org.firstinspires.ftc.robotcore.internal.android.dx.cf.code;

import java.util.ArrayList;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.ParseException;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.ConstantPool;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstDouble;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstFloat;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstInteger;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstKnownNull;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstLong;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Bits;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.ByteArray;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public final class BytecodeArray {
  public static final Visitor EMPTY_VISITOR = new BaseVisitor();
  
  private final ByteArray bytes;
  
  private final ConstantPool pool;
  
  public BytecodeArray(ByteArray paramByteArray, ConstantPool paramConstantPool) {
    if (paramByteArray != null) {
      if (paramConstantPool != null) {
        this.bytes = paramByteArray;
        this.pool = paramConstantPool;
        return;
      } 
      throw new NullPointerException("pool == null");
    } 
    throw new NullPointerException("bytes == null");
  }
  
  private int parseLookupswitch(int paramInt, Visitor paramVisitor) {
    int m = paramInt + 4 & 0xFFFFFFFC;
    int j = paramInt + 1;
    int k = 0;
    int i = 0;
    while (j < m) {
      i = i << 8 | this.bytes.getUnsignedByte(j);
      j++;
    } 
    int n = this.bytes.getInt(m);
    int i1 = this.bytes.getInt(m + 4);
    m += 8;
    SwitchList switchList = new SwitchList(i1);
    j = k;
    k = m;
    while (j < i1) {
      m = this.bytes.getInt(k);
      int i2 = this.bytes.getInt(k + 4);
      k += 8;
      switchList.add(m, i2 + paramInt);
      j++;
    } 
    switchList.setDefaultTarget(n + paramInt);
    switchList.removeSuperfluousDefaults();
    switchList.setImmutable();
    j = k - paramInt;
    paramVisitor.visitSwitch(171, paramInt, j, switchList, i);
    return j;
  }
  
  private int parseNewarray(int paramInt, Visitor paramVisitor) {
    // Byte code:
    //   0: aload_0
    //   1: getfield bytes : Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/ByteArray;
    //   4: iload_1
    //   5: iconst_1
    //   6: iadd
    //   7: invokevirtual getUnsignedByte : (I)I
    //   10: istore #7
    //   12: iload #7
    //   14: tableswitch default -> 60, 4 -> 153, 5 -> 145, 6 -> 137, 7 -> 129, 8 -> 121, 9 -> 113, 10 -> 105, 11 -> 97
    //   60: new java/lang/StringBuilder
    //   63: dup
    //   64: invokespecial <init> : ()V
    //   67: astore_2
    //   68: aload_2
    //   69: ldc 'bad newarray code '
    //   71: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   74: pop
    //   75: aload_2
    //   76: iload #7
    //   78: invokestatic u1 : (I)Ljava/lang/String;
    //   81: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   84: pop
    //   85: new org/firstinspires/ftc/robotcore/internal/android/dx/cf/code/SimException
    //   88: dup
    //   89: aload_2
    //   90: invokevirtual toString : ()Ljava/lang/String;
    //   93: invokespecial <init> : (Ljava/lang/String;)V
    //   96: athrow
    //   97: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType.LONG_ARRAY : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType;
    //   100: astore #10
    //   102: goto -> 158
    //   105: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType.INT_ARRAY : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType;
    //   108: astore #10
    //   110: goto -> 158
    //   113: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType.SHORT_ARRAY : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType;
    //   116: astore #10
    //   118: goto -> 158
    //   121: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType.BYTE_ARRAY : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType;
    //   124: astore #10
    //   126: goto -> 158
    //   129: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType.DOUBLE_ARRAY : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType;
    //   132: astore #10
    //   134: goto -> 158
    //   137: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType.FLOAT_ARRAY : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType;
    //   140: astore #10
    //   142: goto -> 158
    //   145: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType.CHAR_ARRAY : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType;
    //   148: astore #10
    //   150: goto -> 158
    //   153: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType.BOOLEAN_ARRAY : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType;
    //   156: astore #10
    //   158: aload_2
    //   159: invokeinterface getPreviousOffset : ()I
    //   164: istore_3
    //   165: new org/firstinspires/ftc/robotcore/internal/android/dx/cf/code/BytecodeArray$ConstantParserVisitor
    //   168: dup
    //   169: aload_0
    //   170: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/BytecodeArray;)V
    //   173: astore #11
    //   175: iconst_0
    //   176: istore #6
    //   178: iload_3
    //   179: iflt -> 222
    //   182: aload_0
    //   183: iload_3
    //   184: aload #11
    //   186: invokevirtual parseInstruction : (ILorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/BytecodeArray$Visitor;)I
    //   189: pop
    //   190: aload #11
    //   192: getfield cst : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/Constant;
    //   195: instanceof org/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstInteger
    //   198: ifeq -> 222
    //   201: aload #11
    //   203: getfield length : I
    //   206: iload_3
    //   207: iadd
    //   208: iload_1
    //   209: if_icmpne -> 222
    //   212: aload #11
    //   214: getfield value : I
    //   217: istore #5
    //   219: goto -> 225
    //   222: iconst_0
    //   223: istore #5
    //   225: iload_1
    //   226: iconst_2
    //   227: iadd
    //   228: istore_3
    //   229: new java/util/ArrayList
    //   232: dup
    //   233: invokespecial <init> : ()V
    //   236: astore #12
    //   238: iload_3
    //   239: istore #4
    //   241: iload #5
    //   243: ifeq -> 545
    //   246: iconst_0
    //   247: istore #4
    //   249: aload_0
    //   250: getfield bytes : Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/ByteArray;
    //   253: astore #13
    //   255: iload_3
    //   256: iconst_1
    //   257: iadd
    //   258: istore #6
    //   260: aload #13
    //   262: iload_3
    //   263: invokevirtual getUnsignedByte : (I)I
    //   266: bipush #89
    //   268: if_icmpeq -> 274
    //   271: goto -> 538
    //   274: aload_0
    //   275: iload #6
    //   277: aload #11
    //   279: invokevirtual parseInstruction : (ILorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/BytecodeArray$Visitor;)I
    //   282: pop
    //   283: aload #11
    //   285: getfield length : I
    //   288: ifeq -> 538
    //   291: aload #11
    //   293: getfield cst : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/Constant;
    //   296: instanceof org/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstInteger
    //   299: ifeq -> 538
    //   302: aload #11
    //   304: getfield value : I
    //   307: iload #4
    //   309: if_icmpeq -> 315
    //   312: goto -> 538
    //   315: iload #6
    //   317: aload #11
    //   319: getfield length : I
    //   322: iadd
    //   323: istore #6
    //   325: aload_0
    //   326: iload #6
    //   328: aload #11
    //   330: invokevirtual parseInstruction : (ILorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/BytecodeArray$Visitor;)I
    //   333: pop
    //   334: aload #11
    //   336: getfield length : I
    //   339: ifeq -> 538
    //   342: aload #11
    //   344: getfield cst : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/Constant;
    //   347: instanceof org/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstLiteralBits
    //   350: ifne -> 356
    //   353: goto -> 538
    //   356: iload #6
    //   358: aload #11
    //   360: getfield length : I
    //   363: iadd
    //   364: istore #8
    //   366: aload #12
    //   368: aload #11
    //   370: getfield cst : Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/Constant;
    //   373: invokevirtual add : (Ljava/lang/Object;)Z
    //   376: pop
    //   377: aload_0
    //   378: getfield bytes : Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/ByteArray;
    //   381: iload #8
    //   383: invokevirtual getUnsignedByte : (I)I
    //   386: istore #9
    //   388: iconst_1
    //   389: istore #6
    //   391: iload #7
    //   393: tableswitch default -> 440, 4 -> 503, 5 -> 493, 6 -> 483, 7 -> 473, 8 -> 503, 9 -> 463, 10 -> 453, 11 -> 443
    //   440: goto -> 516
    //   443: iload #9
    //   445: bipush #80
    //   447: if_icmpeq -> 513
    //   450: goto -> 516
    //   453: iload #9
    //   455: bipush #79
    //   457: if_icmpeq -> 513
    //   460: goto -> 516
    //   463: iload #9
    //   465: bipush #86
    //   467: if_icmpeq -> 513
    //   470: goto -> 516
    //   473: iload #9
    //   475: bipush #82
    //   477: if_icmpeq -> 513
    //   480: goto -> 516
    //   483: iload #9
    //   485: bipush #81
    //   487: if_icmpeq -> 513
    //   490: goto -> 516
    //   493: iload #9
    //   495: bipush #85
    //   497: if_icmpeq -> 513
    //   500: goto -> 516
    //   503: iload #9
    //   505: bipush #84
    //   507: if_icmpeq -> 513
    //   510: goto -> 516
    //   513: iconst_0
    //   514: istore #6
    //   516: iload #6
    //   518: ifeq -> 524
    //   521: goto -> 538
    //   524: iload #4
    //   526: iconst_1
    //   527: iadd
    //   528: istore #4
    //   530: iload #8
    //   532: iconst_1
    //   533: iadd
    //   534: istore_3
    //   535: goto -> 249
    //   538: iload #4
    //   540: istore #6
    //   542: iload_3
    //   543: istore #4
    //   545: iload #6
    //   547: iconst_2
    //   548: if_icmplt -> 580
    //   551: iload #6
    //   553: iload #5
    //   555: if_icmpeq -> 561
    //   558: goto -> 580
    //   561: iload #4
    //   563: iload_1
    //   564: isub
    //   565: istore_3
    //   566: aload_2
    //   567: iload_1
    //   568: iload_3
    //   569: aload #10
    //   571: aload #12
    //   573: invokeinterface visitNewarray : (IILorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType;Ljava/util/ArrayList;)V
    //   578: iload_3
    //   579: ireturn
    //   580: aload_2
    //   581: iload_1
    //   582: iconst_2
    //   583: aload #10
    //   585: aconst_null
    //   586: invokeinterface visitNewarray : (IILorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType;Ljava/util/ArrayList;)V
    //   591: iconst_2
    //   592: ireturn
  }
  
  private int parseTableswitch(int paramInt, Visitor paramVisitor) {
    int m = paramInt + 4 & 0xFFFFFFFC;
    int j = paramInt + 1;
    int k = 0;
    int i = 0;
    while (j < m) {
      i = i << 8 | this.bytes.getUnsignedByte(j);
      j++;
    } 
    int n = this.bytes.getInt(m);
    int i1 = this.bytes.getInt(m + 4);
    j = this.bytes.getInt(m + 8);
    int i2 = j - i1 + 1;
    m += 12;
    if (i1 <= j) {
      SwitchList switchList = new SwitchList(i2);
      j = k;
      k = m;
      while (j < i2) {
        m = this.bytes.getInt(k);
        k += 4;
        switchList.add(i1 + j, m + paramInt);
        j++;
      } 
      switchList.setDefaultTarget(n + paramInt);
      switchList.removeSuperfluousDefaults();
      switchList.setImmutable();
      j = k - paramInt;
      paramVisitor.visitSwitch(171, paramInt, j, switchList, i);
      return j;
    } 
    throw new SimException("low / high inversion");
  }
  
  private int parseWide(int paramInt, Visitor paramVisitor) {
    int i = this.bytes.getUnsignedByte(paramInt + 1);
    int j = this.bytes.getUnsignedShort(paramInt + 2);
    if (i != 132) {
      if (i != 169) {
        switch (i) {
          default:
            switch (i) {
              default:
                paramVisitor.visitInvalid(196, paramInt, 1);
                return 1;
              case 58:
                paramVisitor.visitLocal(54, paramInt, 4, j, Type.OBJECT, 0);
                return 4;
              case 57:
                paramVisitor.visitLocal(54, paramInt, 4, j, Type.DOUBLE, 0);
                return 4;
              case 56:
                paramVisitor.visitLocal(54, paramInt, 4, j, Type.FLOAT, 0);
                return 4;
              case 55:
                paramVisitor.visitLocal(54, paramInt, 4, j, Type.LONG, 0);
                return 4;
              case 54:
                break;
            } 
            paramVisitor.visitLocal(54, paramInt, 4, j, Type.INT, 0);
            return 4;
          case 25:
            paramVisitor.visitLocal(21, paramInt, 4, j, Type.OBJECT, 0);
            return 4;
          case 24:
            paramVisitor.visitLocal(21, paramInt, 4, j, Type.DOUBLE, 0);
            return 4;
          case 23:
            paramVisitor.visitLocal(21, paramInt, 4, j, Type.FLOAT, 0);
            return 4;
          case 22:
            paramVisitor.visitLocal(21, paramInt, 4, j, Type.LONG, 0);
            return 4;
          case 21:
            break;
        } 
        paramVisitor.visitLocal(21, paramInt, 4, j, Type.INT, 0);
        return 4;
      } 
      paramVisitor.visitLocal(i, paramInt, 4, j, Type.RETURN_ADDRESS, 0);
      return 4;
    } 
    int k = this.bytes.getShort(paramInt + 4);
    paramVisitor.visitLocal(i, paramInt, 6, j, Type.INT, k);
    return 6;
  }
  
  public int byteLength() {
    return this.bytes.size() + 4;
  }
  
  public void forEach(Visitor paramVisitor) {
    int j = this.bytes.size();
    for (int i = 0; i < j; i += parseInstruction(i, paramVisitor));
  }
  
  public ByteArray getBytes() {
    return this.bytes;
  }
  
  public int[] getInstructionOffsets() {
    int j = this.bytes.size();
    int[] arrayOfInt = Bits.makeBitSet(j);
    for (int i = 0; i < j; i += parseInstruction(i, null))
      Bits.set(arrayOfInt, i, true); 
    return arrayOfInt;
  }
  
  public int parseInstruction(int paramInt, Visitor paramVisitor) {
    int i;
    StringBuilder stringBuilder;
    Visitor visitor = paramVisitor;
    if (paramVisitor == null)
      visitor = EMPTY_VISITOR; 
    try {
      Constant constant;
      int m;
      int k = this.bytes.getUnsignedByte(paramInt);
      ByteOps.opInfo(k);
      i = 0;
      int j = 0;
      switch (k) {
        case 200:
        case 201:
          i = this.bytes.getInt(paramInt + 1);
          if (k == 200) {
            j = 167;
            visitor.visitBranch(j, paramInt, 5, i + paramInt);
            return 5;
          } 
          break;
        case 197:
          j = this.bytes.getUnsignedShort(paramInt + 1);
          i = this.bytes.getUnsignedByte(paramInt + 3);
          visitor.visitConstant(k, paramInt, 4, this.pool.get(j), i);
          return 4;
        case 196:
          return parseWide(paramInt, visitor);
        case 188:
          return parseNewarray(paramInt, visitor);
        case 186:
          throw new ParseException("invokedynamic not supported");
        case 185:
          j = this.bytes.getUnsignedShort(paramInt + 1);
          i = this.bytes.getUnsignedByte(paramInt + 3);
          m = this.bytes.getUnsignedByte(paramInt + 4);
          visitor.visitConstant(k, paramInt, 5, this.pool.get(j), i | m << 8);
          return 5;
        case 178:
        case 179:
        case 180:
        case 181:
        case 182:
        case 183:
        case 184:
        case 187:
        case 189:
        case 192:
        case 193:
          j = this.bytes.getUnsignedShort(paramInt + 1);
          visitor.visitConstant(k, paramInt, 3, this.pool.get(j), 0);
          return 3;
        case 177:
        case 191:
        case 194:
        case 195:
          visitor.visitNoArgs(k, paramInt, 1, Type.VOID);
          return 1;
        case 176:
          visitor.visitNoArgs(172, paramInt, 1, Type.OBJECT);
          return 1;
        case 175:
          visitor.visitNoArgs(172, paramInt, 1, Type.DOUBLE);
          return 1;
        case 174:
          visitor.visitNoArgs(172, paramInt, 1, Type.FLOAT);
          return 1;
        case 173:
          visitor.visitNoArgs(172, paramInt, 1, Type.LONG);
          return 1;
        case 172:
          visitor.visitNoArgs(172, paramInt, 1, Type.INT);
          return 1;
        case 171:
          return parseLookupswitch(paramInt, visitor);
        case 170:
          return parseTableswitch(paramInt, visitor);
        case 169:
          visitor.visitLocal(k, paramInt, 2, this.bytes.getUnsignedByte(paramInt + 1), Type.RETURN_ADDRESS, 0);
          return 2;
        case 153:
        case 154:
        case 155:
        case 156:
        case 157:
        case 158:
        case 159:
        case 160:
        case 161:
        case 162:
        case 163:
        case 164:
        case 165:
        case 166:
        case 167:
        case 168:
        case 198:
        case 199:
          visitor.visitBranch(k, paramInt, 3, this.bytes.getShort(paramInt + 1) + paramInt);
          return 3;
        case 136:
        case 139:
        case 142:
        case 145:
        case 146:
        case 147:
        case 148:
        case 149:
        case 150:
        case 151:
        case 152:
        case 190:
          visitor.visitNoArgs(k, paramInt, 1, Type.INT);
          return 1;
        case 135:
        case 138:
        case 141:
          visitor.visitNoArgs(k, paramInt, 1, Type.DOUBLE);
          return 1;
        case 134:
        case 137:
        case 144:
          visitor.visitNoArgs(k, paramInt, 1, Type.FLOAT);
          return 1;
        case 133:
        case 140:
        case 143:
          visitor.visitNoArgs(k, paramInt, 1, Type.LONG);
          return 1;
        case 132:
          j = this.bytes.getUnsignedByte(paramInt + 1);
          i = this.bytes.getByte(paramInt + 2);
          visitor.visitLocal(k, paramInt, 3, j, Type.INT, i);
          return 3;
        case 99:
        case 103:
        case 107:
        case 111:
        case 115:
        case 119:
          visitor.visitNoArgs(k - 3, paramInt, 1, Type.DOUBLE);
          return 1;
        case 98:
        case 102:
        case 106:
        case 110:
        case 114:
        case 118:
          visitor.visitNoArgs(k - 2, paramInt, 1, Type.FLOAT);
          return 1;
        case 97:
        case 101:
        case 105:
        case 109:
        case 113:
        case 117:
        case 121:
        case 123:
        case 125:
        case 127:
        case 129:
        case 131:
          visitor.visitNoArgs(k - 1, paramInt, 1, Type.LONG);
          return 1;
        case 96:
        case 100:
        case 104:
        case 108:
        case 112:
        case 116:
        case 120:
        case 122:
        case 124:
        case 126:
        case 128:
        case 130:
          visitor.visitNoArgs(k, paramInt, 1, Type.INT);
          return 1;
        case 87:
        case 88:
        case 89:
        case 90:
        case 91:
        case 92:
        case 93:
        case 94:
        case 95:
          visitor.visitNoArgs(k, paramInt, 1, Type.VOID);
          return 1;
        case 86:
          visitor.visitNoArgs(79, paramInt, 1, Type.SHORT);
          return 1;
        case 85:
          visitor.visitNoArgs(79, paramInt, 1, Type.CHAR);
          return 1;
        case 84:
          visitor.visitNoArgs(79, paramInt, 1, Type.BYTE);
          return 1;
        case 83:
          visitor.visitNoArgs(79, paramInt, 1, Type.OBJECT);
          return 1;
        case 82:
          visitor.visitNoArgs(79, paramInt, 1, Type.DOUBLE);
          return 1;
        case 81:
          visitor.visitNoArgs(79, paramInt, 1, Type.FLOAT);
          return 1;
        case 80:
          visitor.visitNoArgs(79, paramInt, 1, Type.LONG);
          return 1;
        case 79:
          visitor.visitNoArgs(79, paramInt, 1, Type.INT);
          return 1;
        case 75:
        case 76:
        case 77:
        case 78:
          visitor.visitLocal(54, paramInt, 1, k - 75, Type.OBJECT, 0);
          return 1;
        case 71:
        case 72:
        case 73:
        case 74:
          visitor.visitLocal(54, paramInt, 1, k - 71, Type.DOUBLE, 0);
          return 1;
        case 67:
        case 68:
        case 69:
        case 70:
          visitor.visitLocal(54, paramInt, 1, k - 67, Type.FLOAT, 0);
          return 1;
        case 63:
        case 64:
        case 65:
        case 66:
          visitor.visitLocal(54, paramInt, 1, k - 63, Type.LONG, 0);
          return 1;
        case 59:
        case 60:
        case 61:
        case 62:
          visitor.visitLocal(54, paramInt, 1, k - 59, Type.INT, 0);
          return 1;
        case 58:
          visitor.visitLocal(54, paramInt, 2, this.bytes.getUnsignedByte(paramInt + 1), Type.OBJECT, 0);
          return 2;
        case 57:
          visitor.visitLocal(54, paramInt, 2, this.bytes.getUnsignedByte(paramInt + 1), Type.DOUBLE, 0);
          return 2;
        case 56:
          visitor.visitLocal(54, paramInt, 2, this.bytes.getUnsignedByte(paramInt + 1), Type.FLOAT, 0);
          return 2;
        case 55:
          visitor.visitLocal(54, paramInt, 2, this.bytes.getUnsignedByte(paramInt + 1), Type.LONG, 0);
          return 2;
        case 54:
          visitor.visitLocal(54, paramInt, 2, this.bytes.getUnsignedByte(paramInt + 1), Type.INT, 0);
          return 2;
        case 53:
          visitor.visitNoArgs(46, paramInt, 1, Type.SHORT);
          return 1;
        case 52:
          visitor.visitNoArgs(46, paramInt, 1, Type.CHAR);
          return 1;
        case 51:
          visitor.visitNoArgs(46, paramInt, 1, Type.BYTE);
          return 1;
        case 50:
          visitor.visitNoArgs(46, paramInt, 1, Type.OBJECT);
          return 1;
        case 49:
          visitor.visitNoArgs(46, paramInt, 1, Type.DOUBLE);
          return 1;
        case 48:
          visitor.visitNoArgs(46, paramInt, 1, Type.FLOAT);
          return 1;
        case 47:
          visitor.visitNoArgs(46, paramInt, 1, Type.LONG);
          return 1;
        case 46:
          visitor.visitNoArgs(46, paramInt, 1, Type.INT);
          return 1;
        case 42:
        case 43:
        case 44:
        case 45:
          visitor.visitLocal(21, paramInt, 1, k - 42, Type.OBJECT, 0);
          return 1;
        case 38:
        case 39:
        case 40:
        case 41:
          visitor.visitLocal(21, paramInt, 1, k - 38, Type.DOUBLE, 0);
          return 1;
        case 34:
        case 35:
        case 36:
        case 37:
          visitor.visitLocal(21, paramInt, 1, k - 34, Type.FLOAT, 0);
          return 1;
        case 30:
        case 31:
        case 32:
        case 33:
          visitor.visitLocal(21, paramInt, 1, k - 30, Type.LONG, 0);
          return 1;
        case 26:
        case 27:
        case 28:
        case 29:
          visitor.visitLocal(21, paramInt, 1, k - 26, Type.INT, 0);
          return 1;
        case 25:
          visitor.visitLocal(21, paramInt, 2, this.bytes.getUnsignedByte(paramInt + 1), Type.OBJECT, 0);
          return 2;
        case 24:
          visitor.visitLocal(21, paramInt, 2, this.bytes.getUnsignedByte(paramInt + 1), Type.DOUBLE, 0);
          return 2;
        case 23:
          visitor.visitLocal(21, paramInt, 2, this.bytes.getUnsignedByte(paramInt + 1), Type.FLOAT, 0);
          return 2;
        case 22:
          visitor.visitLocal(21, paramInt, 2, this.bytes.getUnsignedByte(paramInt + 1), Type.LONG, 0);
          return 2;
        case 21:
          visitor.visitLocal(21, paramInt, 2, this.bytes.getUnsignedByte(paramInt + 1), Type.INT, 0);
          return 2;
        case 20:
          j = this.bytes.getUnsignedShort(paramInt + 1);
          visitor.visitConstant(20, paramInt, 3, this.pool.get(j), 0);
          return 3;
        case 19:
          i = this.bytes.getUnsignedShort(paramInt + 1);
          constant = this.pool.get(i);
          if (constant instanceof CstInteger)
            j = ((CstInteger)constant).getValue(); 
          visitor.visitConstant(18, paramInt, 3, constant, j);
          return 3;
        case 18:
          j = this.bytes.getUnsignedByte(paramInt + 1);
          constant = this.pool.get(j);
          j = i;
          if (constant instanceof CstInteger)
            j = ((CstInteger)constant).getValue(); 
          visitor.visitConstant(18, paramInt, 2, constant, j);
          return 2;
        case 17:
          j = this.bytes.getShort(paramInt + 1);
          visitor.visitConstant(18, paramInt, 3, (Constant)CstInteger.make(j), j);
          return 3;
        case 16:
          j = this.bytes.getByte(paramInt + 1);
          visitor.visitConstant(18, paramInt, 2, (Constant)CstInteger.make(j), j);
          return 2;
        case 15:
          visitor.visitConstant(18, paramInt, 1, (Constant)CstDouble.VALUE_1, 0);
          return 1;
        case 14:
          visitor.visitConstant(18, paramInt, 1, (Constant)CstDouble.VALUE_0, 0);
          return 1;
        case 13:
          visitor.visitConstant(18, paramInt, 1, (Constant)CstFloat.VALUE_2, 0);
          return 1;
        case 12:
          visitor.visitConstant(18, paramInt, 1, (Constant)CstFloat.VALUE_1, 0);
          return 1;
        case 11:
          visitor.visitConstant(18, paramInt, 1, (Constant)CstFloat.VALUE_0, 0);
          return 1;
        case 10:
          visitor.visitConstant(18, paramInt, 1, (Constant)CstLong.VALUE_1, 0);
          return 1;
        case 9:
          visitor.visitConstant(18, paramInt, 1, (Constant)CstLong.VALUE_0, 0);
          return 1;
        case 8:
          visitor.visitConstant(18, paramInt, 1, (Constant)CstInteger.VALUE_5, 5);
          return 1;
        case 7:
          visitor.visitConstant(18, paramInt, 1, (Constant)CstInteger.VALUE_4, 4);
          return 1;
        case 6:
          visitor.visitConstant(18, paramInt, 1, (Constant)CstInteger.VALUE_3, 3);
          return 1;
        case 5:
          visitor.visitConstant(18, paramInt, 1, (Constant)CstInteger.VALUE_2, 2);
          return 1;
        case 4:
          visitor.visitConstant(18, paramInt, 1, (Constant)CstInteger.VALUE_1, 1);
          return 1;
        case 3:
          visitor.visitConstant(18, paramInt, 1, (Constant)CstInteger.VALUE_0, 0);
          return 1;
        case 2:
          visitor.visitConstant(18, paramInt, 1, (Constant)CstInteger.VALUE_M1, -1);
          return 1;
        case 1:
          visitor.visitConstant(18, paramInt, 1, (Constant)CstKnownNull.THE_ONE, 0);
          return 1;
        case 0:
          visitor.visitNoArgs(k, paramInt, 1, Type.VOID);
          return 1;
        default:
          visitor.visitInvalid(k, paramInt, 1);
          return 1;
      } 
    } catch (SimException simException) {
      stringBuilder = new StringBuilder();
      stringBuilder.append("...at bytecode offset ");
      stringBuilder.append(Hex.u4(paramInt));
      simException.addContext(stringBuilder.toString());
      throw simException;
    } catch (RuntimeException runtimeException) {
      SimException simException = new SimException(runtimeException);
      stringBuilder = new StringBuilder();
      stringBuilder.append("...at bytecode offset ");
      stringBuilder.append(Hex.u4(paramInt));
      simException.addContext(stringBuilder.toString());
      throw simException;
    } 
    char c = '¨';
    stringBuilder.visitBranch(c, paramInt, 5, i + paramInt);
    return 5;
  }
  
  public void processWorkSet(int[] paramArrayOfint, Visitor paramVisitor) {
    if (paramVisitor != null)
      while (true) {
        int i = Bits.findFirst(paramArrayOfint, 0);
        if (i < 0)
          return; 
        Bits.clear(paramArrayOfint, i);
        parseInstruction(i, paramVisitor);
        paramVisitor.setPreviousOffset(i);
      }  
    throw new NullPointerException("visitor == null");
  }
  
  public int size() {
    return this.bytes.size();
  }
  
  public static class BaseVisitor implements Visitor {
    private int previousOffset = -1;
    
    public int getPreviousOffset() {
      return this.previousOffset;
    }
    
    public void setPreviousOffset(int param1Int) {
      this.previousOffset = param1Int;
    }
    
    public void visitBranch(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {}
    
    public void visitConstant(int param1Int1, int param1Int2, int param1Int3, Constant param1Constant, int param1Int4) {}
    
    public void visitInvalid(int param1Int1, int param1Int2, int param1Int3) {}
    
    public void visitLocal(int param1Int1, int param1Int2, int param1Int3, int param1Int4, Type param1Type, int param1Int5) {}
    
    public void visitNewarray(int param1Int1, int param1Int2, CstType param1CstType, ArrayList<Constant> param1ArrayList) {}
    
    public void visitNoArgs(int param1Int1, int param1Int2, int param1Int3, Type param1Type) {}
    
    public void visitSwitch(int param1Int1, int param1Int2, int param1Int3, SwitchList param1SwitchList, int param1Int4) {}
  }
  
  class ConstantParserVisitor extends BaseVisitor {
    Constant cst;
    
    int length;
    
    int value;
    
    private void clear() {
      this.length = 0;
    }
    
    public int getPreviousOffset() {
      return -1;
    }
    
    public void setPreviousOffset(int param1Int) {}
    
    public void visitBranch(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      clear();
    }
    
    public void visitConstant(int param1Int1, int param1Int2, int param1Int3, Constant param1Constant, int param1Int4) {
      this.cst = param1Constant;
      this.length = param1Int3;
      this.value = param1Int4;
    }
    
    public void visitInvalid(int param1Int1, int param1Int2, int param1Int3) {
      clear();
    }
    
    public void visitLocal(int param1Int1, int param1Int2, int param1Int3, int param1Int4, Type param1Type, int param1Int5) {
      clear();
    }
    
    public void visitNewarray(int param1Int1, int param1Int2, CstType param1CstType, ArrayList<Constant> param1ArrayList) {
      clear();
    }
    
    public void visitNoArgs(int param1Int1, int param1Int2, int param1Int3, Type param1Type) {
      clear();
    }
    
    public void visitSwitch(int param1Int1, int param1Int2, int param1Int3, SwitchList param1SwitchList, int param1Int4) {
      clear();
    }
  }
  
  public static interface Visitor {
    int getPreviousOffset();
    
    void setPreviousOffset(int param1Int);
    
    void visitBranch(int param1Int1, int param1Int2, int param1Int3, int param1Int4);
    
    void visitConstant(int param1Int1, int param1Int2, int param1Int3, Constant param1Constant, int param1Int4);
    
    void visitInvalid(int param1Int1, int param1Int2, int param1Int3);
    
    void visitLocal(int param1Int1, int param1Int2, int param1Int3, int param1Int4, Type param1Type, int param1Int5);
    
    void visitNewarray(int param1Int1, int param1Int2, CstType param1CstType, ArrayList<Constant> param1ArrayList);
    
    void visitNoArgs(int param1Int1, int param1Int2, int param1Int3, Type param1Type);
    
    void visitSwitch(int param1Int1, int param1Int2, int param1Int3, SwitchList param1SwitchList, int param1Int4);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\code\BytecodeArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.android.dx.cf.cst;

import java.util.BitSet;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.ParseException;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.ParseObserver;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstDouble;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstFieldRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstFloat;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstInteger;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstInterfaceMethodRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstLong;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstMethodRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstNat;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.StdConstantPool;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.ByteArray;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public final class ConstantPoolParser {
  private final ByteArray bytes;
  
  private int endOffset;
  
  private ParseObserver observer;
  
  private final int[] offsets;
  
  private final StdConstantPool pool;
  
  public ConstantPoolParser(ByteArray paramByteArray) {
    int i = paramByteArray.getUnsignedShort(8);
    this.bytes = paramByteArray;
    this.pool = new StdConstantPool(i);
    this.offsets = new int[i];
    this.endOffset = -1;
  }
  
  private void determineOffsets() {
    // Byte code:
    //   0: bipush #10
    //   2: istore_1
    //   3: iconst_1
    //   4: istore_2
    //   5: aload_0
    //   6: getfield offsets : [I
    //   9: astore #4
    //   11: iload_2
    //   12: aload #4
    //   14: arraylength
    //   15: if_icmpge -> 271
    //   18: aload #4
    //   20: iload_2
    //   21: iload_1
    //   22: iastore
    //   23: aload_0
    //   24: getfield bytes : Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/ByteArray;
    //   27: iload_1
    //   28: invokevirtual getUnsignedByte : (I)I
    //   31: istore_3
    //   32: iload_3
    //   33: tableswitch default -> 120, 1 -> 153, 2 -> 120, 3 -> 294, 4 -> 294, 5 -> 284, 6 -> 284, 7 -> 277, 8 -> 277, 9 -> 294, 10 -> 294, 11 -> 294, 12 -> 294, 13 -> 120, 14 -> 120, 15 -> 143, 16 -> 133, 17 -> 120, 18 -> 123
    //   120: goto -> 173
    //   123: new org/firstinspires/ftc/robotcore/internal/android/dx/cf/iface/ParseException
    //   126: dup
    //   127: ldc 'InvokeDynamic not supported'
    //   129: invokespecial <init> : (Ljava/lang/String;)V
    //   132: athrow
    //   133: new org/firstinspires/ftc/robotcore/internal/android/dx/cf/iface/ParseException
    //   136: dup
    //   137: ldc 'MethodType not supported'
    //   139: invokespecial <init> : (Ljava/lang/String;)V
    //   142: athrow
    //   143: new org/firstinspires/ftc/robotcore/internal/android/dx/cf/iface/ParseException
    //   146: dup
    //   147: ldc 'MethodHandle not supported'
    //   149: invokespecial <init> : (Ljava/lang/String;)V
    //   152: athrow
    //   153: iload_1
    //   154: aload_0
    //   155: getfield bytes : Lorg/firstinspires/ftc/robotcore/internal/android/dx/util/ByteArray;
    //   158: iload_1
    //   159: iconst_1
    //   160: iadd
    //   161: invokevirtual getUnsignedShort : (I)I
    //   164: iconst_3
    //   165: iadd
    //   166: iadd
    //   167: istore_3
    //   168: iload_3
    //   169: istore_1
    //   170: goto -> 298
    //   173: new java/lang/StringBuilder
    //   176: dup
    //   177: invokespecial <init> : ()V
    //   180: astore #4
    //   182: aload #4
    //   184: ldc 'unknown tag byte: '
    //   186: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   189: pop
    //   190: aload #4
    //   192: iload_3
    //   193: invokestatic u1 : (I)Ljava/lang/String;
    //   196: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   199: pop
    //   200: new org/firstinspires/ftc/robotcore/internal/android/dx/cf/iface/ParseException
    //   203: dup
    //   204: aload #4
    //   206: invokevirtual toString : ()Ljava/lang/String;
    //   209: invokespecial <init> : (Ljava/lang/String;)V
    //   212: athrow
    //   213: new java/lang/StringBuilder
    //   216: dup
    //   217: invokespecial <init> : ()V
    //   220: astore #4
    //   222: aload #4
    //   224: ldc '...while preparsing cst '
    //   226: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   229: pop
    //   230: aload #4
    //   232: iload_2
    //   233: invokestatic u2 : (I)Ljava/lang/String;
    //   236: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   239: pop
    //   240: aload #4
    //   242: ldc ' at offset '
    //   244: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   247: pop
    //   248: aload #4
    //   250: iload_1
    //   251: invokestatic u4 : (I)Ljava/lang/String;
    //   254: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   257: pop
    //   258: aload #5
    //   260: aload #4
    //   262: invokevirtual toString : ()Ljava/lang/String;
    //   265: invokevirtual addContext : (Ljava/lang/String;)V
    //   268: aload #5
    //   270: athrow
    //   271: aload_0
    //   272: iload_1
    //   273: putfield endOffset : I
    //   276: return
    //   277: iload_1
    //   278: iconst_3
    //   279: iadd
    //   280: istore_1
    //   281: goto -> 298
    //   284: iconst_2
    //   285: istore_3
    //   286: iload_1
    //   287: bipush #9
    //   289: iadd
    //   290: istore_1
    //   291: goto -> 300
    //   294: iload_1
    //   295: iconst_5
    //   296: iadd
    //   297: istore_1
    //   298: iconst_1
    //   299: istore_3
    //   300: iload_2
    //   301: iload_3
    //   302: iadd
    //   303: istore_2
    //   304: goto -> 5
    //   307: astore #5
    //   309: goto -> 213
    // Exception table:
    //   from	to	target	type
    //   123	133	307	org/firstinspires/ftc/robotcore/internal/android/dx/cf/iface/ParseException
    //   133	143	307	org/firstinspires/ftc/robotcore/internal/android/dx/cf/iface/ParseException
    //   143	153	307	org/firstinspires/ftc/robotcore/internal/android/dx/cf/iface/ParseException
    //   153	168	307	org/firstinspires/ftc/robotcore/internal/android/dx/cf/iface/ParseException
    //   173	213	307	org/firstinspires/ftc/robotcore/internal/android/dx/cf/iface/ParseException
  }
  
  private void parse() {
    determineOffsets();
    ParseObserver parseObserver = this.observer;
    int j = 1;
    if (parseObserver != null) {
      ByteArray byteArray = this.bytes;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("constant_pool_count: ");
      stringBuilder.append(Hex.u2(this.offsets.length));
      parseObserver.parsed(byteArray, 8, 2, stringBuilder.toString());
      this.observer.parsed(this.bytes, 10, 0, "\nconstant_pool:");
      this.observer.changeIndent(1);
    } 
    BitSet bitSet = new BitSet(this.offsets.length);
    int i = 1;
    while (true) {
      int[] arrayOfInt = this.offsets;
      if (i < arrayOfInt.length) {
        if (arrayOfInt[i] != 0 && this.pool.getOrNull(i) == null)
          parse0(i, bitSet); 
        i++;
        continue;
      } 
      if (this.observer != null) {
        for (i = j; i < this.offsets.length; i++) {
          Constant constant = this.pool.getOrNull(i);
          if (constant != null) {
            int k;
            String str;
            int n = this.offsets[i];
            int m = this.endOffset;
            j = i + 1;
            while (true) {
              int[] arrayOfInt1 = this.offsets;
              k = m;
              if (j < arrayOfInt1.length) {
                k = arrayOfInt1[j];
                if (k != 0)
                  break; 
                j++;
                continue;
              } 
              break;
            } 
            if (bitSet.get(i)) {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append(Hex.u2(i));
              stringBuilder.append(": utf8{\"");
              stringBuilder.append(constant.toHuman());
              stringBuilder.append("\"}");
              str = stringBuilder.toString();
            } else {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append(Hex.u2(i));
              stringBuilder.append(": ");
              stringBuilder.append(str.toString());
              str = stringBuilder.toString();
            } 
            this.observer.parsed(this.bytes, n, k - n, str);
          } 
        } 
        this.observer.changeIndent(-1);
        this.observer.parsed(this.bytes, this.endOffset, 0, "end constant_pool");
      } 
      return;
    } 
  }
  
  private Constant parse0(int paramInt, BitSet paramBitSet) {
    Constant constant = this.pool.getOrNull(paramInt);
    if (constant != null)
      return constant; 
    int i = this.offsets[paramInt];
    try {
      CstNat cstNat;
      CstInterfaceMethodRef cstInterfaceMethodRef;
      CstMethodRef cstMethodRef;
      CstFieldRef cstFieldRef;
      Constant constant1;
      CstType cstType;
      CstDouble cstDouble;
      CstLong cstLong;
      CstFloat cstFloat;
      CstInteger cstInteger;
      CstString cstString1;
      CstString cstString2;
      int j = this.bytes.getUnsignedByte(i);
      switch (j) {
        case 18:
          throw new ParseException("InvokeDynamic not supported");
        case 16:
          throw new ParseException("MethodType not supported");
        case 15:
          throw new ParseException("MethodHandle not supported");
        case 12:
          cstNat = new CstNat((CstString)parse0(this.bytes.getUnsignedShort(i + 1), paramBitSet), (CstString)parse0(this.bytes.getUnsignedShort(i + 3), paramBitSet));
          this.pool.set(paramInt, (Constant)cstNat);
          return (Constant)cstNat;
        case 11:
          cstInterfaceMethodRef = new CstInterfaceMethodRef((CstType)parse0(this.bytes.getUnsignedShort(i + 1), (BitSet)cstNat), (CstNat)parse0(this.bytes.getUnsignedShort(i + 3), (BitSet)cstNat));
          this.pool.set(paramInt, (Constant)cstInterfaceMethodRef);
          return (Constant)cstInterfaceMethodRef;
        case 10:
          cstMethodRef = new CstMethodRef((CstType)parse0(this.bytes.getUnsignedShort(i + 1), (BitSet)cstInterfaceMethodRef), (CstNat)parse0(this.bytes.getUnsignedShort(i + 3), (BitSet)cstInterfaceMethodRef));
          this.pool.set(paramInt, (Constant)cstMethodRef);
          return (Constant)cstMethodRef;
        case 9:
          cstFieldRef = new CstFieldRef((CstType)parse0(this.bytes.getUnsignedShort(i + 1), (BitSet)cstMethodRef), (CstNat)parse0(this.bytes.getUnsignedShort(i + 3), (BitSet)cstMethodRef));
          this.pool.set(paramInt, (Constant)cstFieldRef);
          return (Constant)cstFieldRef;
        case 8:
          constant1 = parse0(this.bytes.getUnsignedShort(i + 1), (BitSet)cstFieldRef);
          this.pool.set(paramInt, constant1);
          return constant1;
        case 7:
          cstType = new CstType(Type.internClassName(((CstString)parse0(this.bytes.getUnsignedShort(i + 1), (BitSet)constant1)).getString()));
          this.pool.set(paramInt, (Constant)cstType);
          return (Constant)cstType;
        case 6:
          cstDouble = CstDouble.make(this.bytes.getLong(i + 1));
          this.pool.set(paramInt, (Constant)cstDouble);
          return (Constant)cstDouble;
        case 5:
          cstLong = CstLong.make(this.bytes.getLong(i + 1));
          this.pool.set(paramInt, (Constant)cstLong);
          return (Constant)cstLong;
        case 4:
          cstFloat = CstFloat.make(this.bytes.getInt(i + 1));
          this.pool.set(paramInt, (Constant)cstFloat);
          return (Constant)cstFloat;
        case 3:
          cstInteger = CstInteger.make(this.bytes.getInt(i + 1));
          this.pool.set(paramInt, (Constant)cstInteger);
          return (Constant)cstInteger;
        case 1:
          cstString2 = parseUtf8(i);
          cstInteger.set(paramInt);
          cstString1 = cstString2;
          this.pool.set(paramInt, (Constant)cstString1);
          return (Constant)cstString1;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("unknown tag byte: ");
      stringBuilder.append(Hex.u1(j));
      throw new ParseException(stringBuilder.toString());
    } catch (ParseException parseException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("...while parsing cst ");
      stringBuilder.append(Hex.u2(paramInt));
      stringBuilder.append(" at offset ");
      stringBuilder.append(Hex.u4(i));
      parseException.addContext(stringBuilder.toString());
      throw parseException;
    } catch (RuntimeException runtimeException) {
      ParseException parseException = new ParseException(runtimeException);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("...while parsing cst ");
      stringBuilder.append(Hex.u2(paramInt));
      stringBuilder.append(" at offset ");
      stringBuilder.append(Hex.u4(i));
      parseException.addContext(stringBuilder.toString());
      throw parseException;
    } 
  }
  
  private void parseIfNecessary() {
    if (this.endOffset < 0)
      parse(); 
  }
  
  private CstString parseUtf8(int paramInt) {
    int i = this.bytes.getUnsignedShort(paramInt + 1);
    paramInt += 3;
    ByteArray byteArray = this.bytes.slice(paramInt, i + paramInt);
    try {
      return new CstString(byteArray);
    } catch (IllegalArgumentException illegalArgumentException) {
      throw new ParseException(illegalArgumentException);
    } 
  }
  
  public int getEndOffset() {
    parseIfNecessary();
    return this.endOffset;
  }
  
  public StdConstantPool getPool() {
    parseIfNecessary();
    return this.pool;
  }
  
  public void setObserver(ParseObserver paramParseObserver) {
    this.observer = paramParseObserver;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\cst\ConstantPoolParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
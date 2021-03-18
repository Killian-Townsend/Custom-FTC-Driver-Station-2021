package org.firstinspires.ftc.robotcore.internal.android.dx.cf.code;

import java.util.ArrayList;
import org.firstinspires.ftc.robotcore.internal.android.dex.util.ExceptionWithContext;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeBearer;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public class LocalsArraySet extends LocalsArray {
  private final OneLocalsArray primary;
  
  private final ArrayList<LocalsArray> secondaries;
  
  public LocalsArraySet(int paramInt) {
    super(bool);
    boolean bool;
    this.primary = new OneLocalsArray(paramInt);
    this.secondaries = new ArrayList<LocalsArray>();
  }
  
  private LocalsArraySet(LocalsArraySet paramLocalsArraySet) {
    super(bool);
    boolean bool;
    this.primary = paramLocalsArraySet.primary.copy();
    this.secondaries = new ArrayList<LocalsArray>(paramLocalsArraySet.secondaries.size());
    j = paramLocalsArraySet.secondaries.size();
    while (i < j) {
      LocalsArray localsArray = paramLocalsArraySet.secondaries.get(i);
      if (localsArray == null) {
        this.secondaries.add(null);
      } else {
        this.secondaries.add(localsArray.copy());
      } 
      i++;
    } 
  }
  
  public LocalsArraySet(OneLocalsArray paramOneLocalsArray, ArrayList<LocalsArray> paramArrayList) {
    super(bool);
    boolean bool;
    this.primary = paramOneLocalsArray;
    this.secondaries = paramArrayList;
  }
  
  private LocalsArray getSecondaryForLabel(int paramInt) {
    return (paramInt >= this.secondaries.size()) ? null : this.secondaries.get(paramInt);
  }
  
  private LocalsArraySet mergeWithOne(OneLocalsArray paramOneLocalsArray) {
    OneLocalsArray oneLocalsArray = this.primary.merge(paramOneLocalsArray.getPrimary());
    ArrayList<LocalsArray> arrayList = new ArrayList(this.secondaries.size());
    int k = this.secondaries.size();
    int j = 0;
    int i = j;
    while (j < k) {
      LocalsArray localsArray3 = this.secondaries.get(j);
      LocalsArray localsArray2 = null;
      LocalsArray localsArray1 = localsArray2;
      if (localsArray3 != null)
        try {
          localsArray1 = localsArray3.merge(paramOneLocalsArray);
        } catch (SimException simException) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Merging one locals against caller block ");
          stringBuilder.append(Hex.u2(j));
          simException.addContext(stringBuilder.toString());
          localsArray1 = localsArray2;
        }  
      if (i != 0 || localsArray3 != localsArray1) {
        i = 1;
      } else {
        i = 0;
      } 
      arrayList.add(localsArray1);
      j++;
    } 
    return (this.primary == oneLocalsArray && i == 0) ? this : new LocalsArraySet(oneLocalsArray, arrayList);
  }
  
  private LocalsArraySet mergeWithSet(LocalsArraySet paramLocalsArraySet) {
    // Byte code:
    //   0: aload_0
    //   1: getfield primary : Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/OneLocalsArray;
    //   4: aload_1
    //   5: invokevirtual getPrimary : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/OneLocalsArray;
    //   8: invokevirtual merge : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/OneLocalsArray;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/OneLocalsArray;
    //   11: astore #10
    //   13: aload_0
    //   14: getfield secondaries : Ljava/util/ArrayList;
    //   17: invokevirtual size : ()I
    //   20: istore #4
    //   22: aload_1
    //   23: getfield secondaries : Ljava/util/ArrayList;
    //   26: invokevirtual size : ()I
    //   29: istore #5
    //   31: iload #4
    //   33: iload #5
    //   35: invokestatic max : (II)I
    //   38: istore #6
    //   40: new java/util/ArrayList
    //   43: dup
    //   44: iload #6
    //   46: invokespecial <init> : (I)V
    //   49: astore #11
    //   51: iconst_0
    //   52: istore_3
    //   53: iload_3
    //   54: istore_2
    //   55: iload_3
    //   56: iload #6
    //   58: if_icmpge -> 235
    //   61: aconst_null
    //   62: astore #9
    //   64: iload_3
    //   65: iload #4
    //   67: if_icmpge -> 86
    //   70: aload_0
    //   71: getfield secondaries : Ljava/util/ArrayList;
    //   74: iload_3
    //   75: invokevirtual get : (I)Ljava/lang/Object;
    //   78: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/cf/code/LocalsArray
    //   81: astore #8
    //   83: goto -> 89
    //   86: aconst_null
    //   87: astore #8
    //   89: iload_3
    //   90: iload #5
    //   92: if_icmpge -> 111
    //   95: aload_1
    //   96: getfield secondaries : Ljava/util/ArrayList;
    //   99: iload_3
    //   100: invokevirtual get : (I)Ljava/lang/Object;
    //   103: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/cf/code/LocalsArray
    //   106: astore #7
    //   108: goto -> 114
    //   111: aconst_null
    //   112: astore #7
    //   114: aload #8
    //   116: aload #7
    //   118: if_acmpne -> 124
    //   121: goto -> 137
    //   124: aload #8
    //   126: ifnonnull -> 132
    //   129: goto -> 199
    //   132: aload #7
    //   134: ifnonnull -> 144
    //   137: aload #8
    //   139: astore #7
    //   141: goto -> 199
    //   144: aload #8
    //   146: aload #7
    //   148: invokevirtual merge : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/LocalsArray;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/LocalsArray;
    //   151: astore #7
    //   153: goto -> 199
    //   156: astore #7
    //   158: new java/lang/StringBuilder
    //   161: dup
    //   162: invokespecial <init> : ()V
    //   165: astore #12
    //   167: aload #12
    //   169: ldc 'Merging locals set for caller block '
    //   171: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   174: pop
    //   175: aload #12
    //   177: iload_3
    //   178: invokestatic u2 : (I)Ljava/lang/String;
    //   181: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   184: pop
    //   185: aload #7
    //   187: aload #12
    //   189: invokevirtual toString : ()Ljava/lang/String;
    //   192: invokevirtual addContext : (Ljava/lang/String;)V
    //   195: aload #9
    //   197: astore #7
    //   199: iload_2
    //   200: ifne -> 218
    //   203: aload #8
    //   205: aload #7
    //   207: if_acmpeq -> 213
    //   210: goto -> 218
    //   213: iconst_0
    //   214: istore_2
    //   215: goto -> 220
    //   218: iconst_1
    //   219: istore_2
    //   220: aload #11
    //   222: aload #7
    //   224: invokevirtual add : (Ljava/lang/Object;)Z
    //   227: pop
    //   228: iload_3
    //   229: iconst_1
    //   230: iadd
    //   231: istore_3
    //   232: goto -> 55
    //   235: aload_0
    //   236: getfield primary : Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/OneLocalsArray;
    //   239: aload #10
    //   241: if_acmpne -> 250
    //   244: iload_2
    //   245: ifne -> 250
    //   248: aload_0
    //   249: areturn
    //   250: new org/firstinspires/ftc/robotcore/internal/android/dx/cf/code/LocalsArraySet
    //   253: dup
    //   254: aload #10
    //   256: aload #11
    //   258: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/code/OneLocalsArray;Ljava/util/ArrayList;)V
    //   261: areturn
    // Exception table:
    //   from	to	target	type
    //   144	153	156	org/firstinspires/ftc/robotcore/internal/android/dx/cf/code/SimException
  }
  
  public void annotate(ExceptionWithContext paramExceptionWithContext) {
    paramExceptionWithContext.addContext("(locals array set; primary)");
    this.primary.annotate(paramExceptionWithContext);
    int j = this.secondaries.size();
    for (int i = 0; i < j; i++) {
      LocalsArray localsArray = this.secondaries.get(i);
      if (localsArray != null) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(locals array set: primary for caller ");
        stringBuilder.append(Hex.u2(i));
        stringBuilder.append(')');
        paramExceptionWithContext.addContext(stringBuilder.toString());
        localsArray.getPrimary().annotate(paramExceptionWithContext);
      } 
    } 
  }
  
  public LocalsArray copy() {
    return new LocalsArraySet(this);
  }
  
  public TypeBearer get(int paramInt) {
    return this.primary.get(paramInt);
  }
  
  public TypeBearer getCategory1(int paramInt) {
    return this.primary.getCategory1(paramInt);
  }
  
  public TypeBearer getCategory2(int paramInt) {
    return this.primary.getCategory2(paramInt);
  }
  
  public int getMaxLocals() {
    return this.primary.getMaxLocals();
  }
  
  public TypeBearer getOrNull(int paramInt) {
    return this.primary.getOrNull(paramInt);
  }
  
  protected OneLocalsArray getPrimary() {
    return this.primary;
  }
  
  public void invalidate(int paramInt) {
    throwIfImmutable();
    this.primary.invalidate(paramInt);
    for (LocalsArray localsArray : this.secondaries) {
      if (localsArray != null)
        localsArray.invalidate(paramInt); 
    } 
  }
  
  public void makeInitialized(Type paramType) {
    if (this.primary.getMaxLocals() == 0)
      return; 
    throwIfImmutable();
    this.primary.makeInitialized(paramType);
    for (LocalsArray localsArray : this.secondaries) {
      if (localsArray != null)
        localsArray.makeInitialized(paramType); 
    } 
  }
  
  public LocalsArraySet merge(LocalsArray paramLocalsArray) {
    try {
      if (paramLocalsArray instanceof LocalsArraySet) {
        LocalsArraySet localsArraySet = mergeWithSet((LocalsArraySet)paramLocalsArray);
        paramLocalsArray = localsArraySet;
      } else {
        LocalsArraySet localsArraySet = mergeWithOne((OneLocalsArray)paramLocalsArray);
        paramLocalsArray = localsArraySet;
      } 
      paramLocalsArray.setImmutable();
      return (LocalsArraySet)paramLocalsArray;
    } catch (SimException simException) {
      simException.addContext("underlay locals:");
      annotate(simException);
      simException.addContext("overlay locals:");
      paramLocalsArray.annotate(simException);
      throw simException;
    } 
  }
  
  public LocalsArraySet mergeWithSubroutineCaller(LocalsArray paramLocalsArray, int paramInt) {
    LocalsArray localsArray = getSecondaryForLabel(paramInt);
    OneLocalsArray oneLocalsArray1 = this.primary.merge(paramLocalsArray.getPrimary());
    if (localsArray == paramLocalsArray) {
      paramLocalsArray = localsArray;
    } else if (localsArray != null) {
      paramLocalsArray = localsArray.merge(paramLocalsArray);
    } 
    if (paramLocalsArray == localsArray && oneLocalsArray1 == this.primary)
      return this; 
    int j = this.secondaries.size();
    int k = Math.max(paramInt + 1, j);
    ArrayList<LocalsArray> arrayList = new ArrayList(k);
    int i = 0;
    OneLocalsArray oneLocalsArray2;
    for (oneLocalsArray2 = null; i < k; oneLocalsArray2 = oneLocalsArray1) {
      if (i == paramInt) {
        localsArray = paramLocalsArray;
      } else if (i < j) {
        localsArray = this.secondaries.get(i);
      } else {
        localsArray = null;
      } 
      oneLocalsArray1 = oneLocalsArray2;
      if (localsArray != null)
        if (oneLocalsArray2 == null) {
          oneLocalsArray1 = localsArray.getPrimary();
        } else {
          oneLocalsArray1 = oneLocalsArray2.merge(localsArray.getPrimary());
        }  
      arrayList.add(localsArray);
      i++;
    } 
    paramLocalsArray = new LocalsArraySet(oneLocalsArray2, arrayList);
    paramLocalsArray.setImmutable();
    return (LocalsArraySet)paramLocalsArray;
  }
  
  public void set(int paramInt, TypeBearer paramTypeBearer) {
    throwIfImmutable();
    this.primary.set(paramInt, paramTypeBearer);
    for (LocalsArray localsArray : this.secondaries) {
      if (localsArray != null)
        localsArray.set(paramInt, paramTypeBearer); 
    } 
  }
  
  public void set(RegisterSpec paramRegisterSpec) {
    set(paramRegisterSpec.getReg(), (TypeBearer)paramRegisterSpec);
  }
  
  public void setImmutable() {
    this.primary.setImmutable();
    for (LocalsArray localsArray : this.secondaries) {
      if (localsArray != null)
        localsArray.setImmutable(); 
    } 
    super.setImmutable();
  }
  
  public LocalsArray subArrayForLabel(int paramInt) {
    return getSecondaryForLabel(paramInt);
  }
  
  public String toHuman() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("(locals array set; primary)\n");
    stringBuilder.append(getPrimary().toHuman());
    stringBuilder.append('\n');
    int j = this.secondaries.size();
    for (int i = 0; i < j; i++) {
      LocalsArray localsArray = this.secondaries.get(i);
      if (localsArray != null) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("(locals array set: primary for caller ");
        stringBuilder1.append(Hex.u2(i));
        stringBuilder1.append(")\n");
        stringBuilder.append(stringBuilder1.toString());
        stringBuilder.append(localsArray.getPrimary().toHuman());
        stringBuilder.append('\n');
      } 
    } 
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\code\LocalsArraySet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
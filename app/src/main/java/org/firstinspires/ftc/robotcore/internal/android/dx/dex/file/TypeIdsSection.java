package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;
import org.firstinspires.ftc.robotcore.internal.android.dex.DexIndexOverflowException;
import org.firstinspires.ftc.robotcore.internal.android.dx.command.dexer.Main;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public final class TypeIdsSection extends UniformItemSection {
  private final TreeMap<Type, TypeIdItem> typeIds = new TreeMap<Type, TypeIdItem>();
  
  public TypeIdsSection(DexFile paramDexFile) {
    super("type_ids", paramDexFile, 4);
  }
  
  public IndexedItem get(Constant paramConstant) {
    if (paramConstant != null) {
      throwIfNotPrepared();
      Type type = ((CstType)paramConstant).getClassType();
      IndexedItem indexedItem = this.typeIds.get(type);
      if (indexedItem != null)
        return indexedItem; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("not found: ");
      stringBuilder.append(paramConstant);
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    throw new NullPointerException("cst == null");
  }
  
  public int indexOf(CstType paramCstType) {
    if (paramCstType != null)
      return indexOf(paramCstType.getClassType()); 
    throw new NullPointerException("type == null");
  }
  
  public int indexOf(Type paramType) {
    if (paramType != null) {
      throwIfNotPrepared();
      TypeIdItem typeIdItem = this.typeIds.get(paramType);
      if (typeIdItem != null)
        return typeIdItem.getIndex(); 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("not found: ");
      stringBuilder.append(paramType);
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    throw new NullPointerException("type == null");
  }
  
  public TypeIdItem intern(CstType paramCstType) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: ifnull -> 63
    //   6: aload_0
    //   7: invokevirtual throwIfPrepared : ()V
    //   10: aload_1
    //   11: invokevirtual getClassType : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type;
    //   14: astore #4
    //   16: aload_0
    //   17: getfield typeIds : Ljava/util/TreeMap;
    //   20: aload #4
    //   22: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   25: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/dex/file/TypeIdItem
    //   28: astore_3
    //   29: aload_3
    //   30: astore_2
    //   31: aload_3
    //   32: ifnonnull -> 55
    //   35: new org/firstinspires/ftc/robotcore/internal/android/dx/dex/file/TypeIdItem
    //   38: dup
    //   39: aload_1
    //   40: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType;)V
    //   43: astore_2
    //   44: aload_0
    //   45: getfield typeIds : Ljava/util/TreeMap;
    //   48: aload #4
    //   50: aload_2
    //   51: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   54: pop
    //   55: aload_0
    //   56: monitorexit
    //   57: aload_2
    //   58: areturn
    //   59: astore_1
    //   60: goto -> 73
    //   63: new java/lang/NullPointerException
    //   66: dup
    //   67: ldc 'type == null'
    //   69: invokespecial <init> : (Ljava/lang/String;)V
    //   72: athrow
    //   73: aload_0
    //   74: monitorexit
    //   75: aload_1
    //   76: athrow
    // Exception table:
    //   from	to	target	type
    //   6	29	59	finally
    //   35	55	59	finally
    //   63	73	59	finally
  }
  
  public TypeIdItem intern(Type paramType) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: ifnull -> 62
    //   6: aload_0
    //   7: invokevirtual throwIfPrepared : ()V
    //   10: aload_0
    //   11: getfield typeIds : Ljava/util/TreeMap;
    //   14: aload_1
    //   15: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   18: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/dex/file/TypeIdItem
    //   21: astore_3
    //   22: aload_3
    //   23: astore_2
    //   24: aload_3
    //   25: ifnonnull -> 54
    //   28: new org/firstinspires/ftc/robotcore/internal/android/dx/dex/file/TypeIdItem
    //   31: dup
    //   32: new org/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType
    //   35: dup
    //   36: aload_1
    //   37: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/type/Type;)V
    //   40: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstType;)V
    //   43: astore_2
    //   44: aload_0
    //   45: getfield typeIds : Ljava/util/TreeMap;
    //   48: aload_1
    //   49: aload_2
    //   50: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   53: pop
    //   54: aload_0
    //   55: monitorexit
    //   56: aload_2
    //   57: areturn
    //   58: astore_1
    //   59: goto -> 72
    //   62: new java/lang/NullPointerException
    //   65: dup
    //   66: ldc 'type == null'
    //   68: invokespecial <init> : (Ljava/lang/String;)V
    //   71: athrow
    //   72: aload_0
    //   73: monitorexit
    //   74: aload_1
    //   75: athrow
    // Exception table:
    //   from	to	target	type
    //   6	22	58	finally
    //   28	54	58	finally
    //   62	72	58	finally
  }
  
  public Collection<? extends Item> items() {
    return this.typeIds.values();
  }
  
  protected void orderItems() {
    Iterator<? extends Item> iterator = items().iterator();
    for (int i = 0; iterator.hasNext(); i++)
      ((TypeIdItem)iterator.next()).setIndex(i); 
  }
  
  public void writeHeaderPart(AnnotatedOutput paramAnnotatedOutput) {
    int i;
    throwIfNotPrepared();
    int j = this.typeIds.size();
    if (j == 0) {
      i = 0;
    } else {
      i = getFileOffset();
    } 
    if (j <= 65536) {
      if (paramAnnotatedOutput.annotates()) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("type_ids_size:   ");
        stringBuilder1.append(Hex.u4(j));
        paramAnnotatedOutput.annotate(4, stringBuilder1.toString());
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append("type_ids_off:    ");
        stringBuilder1.append(Hex.u4(i));
        paramAnnotatedOutput.annotate(4, stringBuilder1.toString());
      } 
      paramAnnotatedOutput.writeInt(j);
      paramAnnotatedOutput.writeInt(i);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Too many type references: ");
    stringBuilder.append(j);
    stringBuilder.append("; max is ");
    stringBuilder.append(65536);
    stringBuilder.append(".\n");
    stringBuilder.append(Main.getTooManyIdsErrorMessage());
    throw new DexIndexOverflowException(stringBuilder.toString());
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\TypeIdsSection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
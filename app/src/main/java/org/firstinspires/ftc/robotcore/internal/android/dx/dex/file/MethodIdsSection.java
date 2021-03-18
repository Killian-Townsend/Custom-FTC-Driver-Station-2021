package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import java.util.Collection;
import java.util.TreeMap;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstBaseMethodRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public final class MethodIdsSection extends MemberIdsSection {
  private final TreeMap<CstBaseMethodRef, MethodIdItem> methodIds = new TreeMap<CstBaseMethodRef, MethodIdItem>();
  
  public MethodIdsSection(DexFile paramDexFile) {
    super("method_ids", paramDexFile);
  }
  
  public IndexedItem get(Constant paramConstant) {
    if (paramConstant != null) {
      throwIfNotPrepared();
      IndexedItem indexedItem = this.methodIds.get(paramConstant);
      if (indexedItem != null)
        return indexedItem; 
      throw new IllegalArgumentException("not found");
    } 
    throw new NullPointerException("cst == null");
  }
  
  public int indexOf(CstBaseMethodRef paramCstBaseMethodRef) {
    if (paramCstBaseMethodRef != null) {
      throwIfNotPrepared();
      MethodIdItem methodIdItem = this.methodIds.get(paramCstBaseMethodRef);
      if (methodIdItem != null)
        return methodIdItem.getIndex(); 
      throw new IllegalArgumentException("not found");
    } 
    throw new NullPointerException("ref == null");
  }
  
  public MethodIdItem intern(CstBaseMethodRef paramCstBaseMethodRef) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: ifnull -> 55
    //   6: aload_0
    //   7: invokevirtual throwIfPrepared : ()V
    //   10: aload_0
    //   11: getfield methodIds : Ljava/util/TreeMap;
    //   14: aload_1
    //   15: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   18: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/dex/file/MethodIdItem
    //   21: astore_3
    //   22: aload_3
    //   23: astore_2
    //   24: aload_3
    //   25: ifnonnull -> 47
    //   28: new org/firstinspires/ftc/robotcore/internal/android/dx/dex/file/MethodIdItem
    //   31: dup
    //   32: aload_1
    //   33: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstBaseMethodRef;)V
    //   36: astore_2
    //   37: aload_0
    //   38: getfield methodIds : Ljava/util/TreeMap;
    //   41: aload_1
    //   42: aload_2
    //   43: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   46: pop
    //   47: aload_0
    //   48: monitorexit
    //   49: aload_2
    //   50: areturn
    //   51: astore_1
    //   52: goto -> 65
    //   55: new java/lang/NullPointerException
    //   58: dup
    //   59: ldc 'method == null'
    //   61: invokespecial <init> : (Ljava/lang/String;)V
    //   64: athrow
    //   65: aload_0
    //   66: monitorexit
    //   67: aload_1
    //   68: athrow
    // Exception table:
    //   from	to	target	type
    //   6	22	51	finally
    //   28	47	51	finally
    //   55	65	51	finally
  }
  
  public Collection<? extends Item> items() {
    return this.methodIds.values();
  }
  
  public void writeHeaderPart(AnnotatedOutput paramAnnotatedOutput) {
    int i;
    throwIfNotPrepared();
    int j = this.methodIds.size();
    if (j == 0) {
      i = 0;
    } else {
      i = getFileOffset();
    } 
    if (paramAnnotatedOutput.annotates()) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("method_ids_size: ");
      stringBuilder.append(Hex.u4(j));
      paramAnnotatedOutput.annotate(4, stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("method_ids_off:  ");
      stringBuilder.append(Hex.u4(i));
      paramAnnotatedOutput.annotate(4, stringBuilder.toString());
    } 
    paramAnnotatedOutput.writeInt(j);
    paramAnnotatedOutput.writeInt(i);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\MethodIdsSection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
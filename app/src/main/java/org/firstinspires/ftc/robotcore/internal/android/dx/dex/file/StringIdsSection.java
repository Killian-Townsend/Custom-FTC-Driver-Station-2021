package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstNat;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public final class StringIdsSection extends UniformItemSection {
  private final TreeMap<CstString, StringIdItem> strings = new TreeMap<CstString, StringIdItem>();
  
  public StringIdsSection(DexFile paramDexFile) {
    super("string_ids", paramDexFile, 4);
  }
  
  public IndexedItem get(Constant paramConstant) {
    if (paramConstant != null) {
      throwIfNotPrepared();
      IndexedItem indexedItem = this.strings.get(paramConstant);
      if (indexedItem != null)
        return indexedItem; 
      throw new IllegalArgumentException("not found");
    } 
    throw new NullPointerException("cst == null");
  }
  
  public int indexOf(CstString paramCstString) {
    if (paramCstString != null) {
      throwIfNotPrepared();
      StringIdItem stringIdItem = this.strings.get(paramCstString);
      if (stringIdItem != null)
        return stringIdItem.getIndex(); 
      throw new IllegalArgumentException("not found");
    } 
    throw new NullPointerException("string == null");
  }
  
  public StringIdItem intern(String paramString) {
    return intern(new StringIdItem(new CstString(paramString)));
  }
  
  public StringIdItem intern(StringIdItem paramStringIdItem) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: ifnull -> 53
    //   6: aload_0
    //   7: invokevirtual throwIfPrepared : ()V
    //   10: aload_1
    //   11: invokevirtual getValue : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstString;
    //   14: astore_2
    //   15: aload_0
    //   16: getfield strings : Ljava/util/TreeMap;
    //   19: aload_2
    //   20: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   23: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/dex/file/StringIdItem
    //   26: astore_3
    //   27: aload_3
    //   28: ifnull -> 35
    //   31: aload_0
    //   32: monitorexit
    //   33: aload_3
    //   34: areturn
    //   35: aload_0
    //   36: getfield strings : Ljava/util/TreeMap;
    //   39: aload_2
    //   40: aload_1
    //   41: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   44: pop
    //   45: aload_0
    //   46: monitorexit
    //   47: aload_1
    //   48: areturn
    //   49: astore_1
    //   50: goto -> 63
    //   53: new java/lang/NullPointerException
    //   56: dup
    //   57: ldc 'string == null'
    //   59: invokespecial <init> : (Ljava/lang/String;)V
    //   62: athrow
    //   63: aload_0
    //   64: monitorexit
    //   65: aload_1
    //   66: athrow
    // Exception table:
    //   from	to	target	type
    //   6	27	49	finally
    //   35	45	49	finally
    //   53	63	49	finally
  }
  
  public StringIdItem intern(CstString paramCstString) {
    return intern(new StringIdItem(paramCstString));
  }
  
  public void intern(CstNat paramCstNat) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: invokevirtual getName : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstString;
    //   7: invokevirtual intern : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstString;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/dex/file/StringIdItem;
    //   10: pop
    //   11: aload_0
    //   12: aload_1
    //   13: invokevirtual getDescriptor : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstString;
    //   16: invokevirtual intern : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/CstString;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/dex/file/StringIdItem;
    //   19: pop
    //   20: aload_0
    //   21: monitorexit
    //   22: return
    //   23: astore_1
    //   24: aload_0
    //   25: monitorexit
    //   26: aload_1
    //   27: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	23	finally
  }
  
  public Collection<? extends Item> items() {
    return this.strings.values();
  }
  
  protected void orderItems() {
    Iterator<StringIdItem> iterator = this.strings.values().iterator();
    for (int i = 0; iterator.hasNext(); i++)
      ((StringIdItem)iterator.next()).setIndex(i); 
  }
  
  public void writeHeaderPart(AnnotatedOutput paramAnnotatedOutput) {
    int i;
    throwIfNotPrepared();
    int j = this.strings.size();
    if (j == 0) {
      i = 0;
    } else {
      i = getFileOffset();
    } 
    if (paramAnnotatedOutput.annotates()) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("string_ids_size: ");
      stringBuilder.append(Hex.u4(j));
      paramAnnotatedOutput.annotate(4, stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("string_ids_off:  ");
      stringBuilder.append(Hex.u4(i));
      paramAnnotatedOutput.annotate(4, stringBuilder.toString());
    } 
    paramAnnotatedOutput.writeInt(j);
    paramAnnotatedOutput.writeInt(i);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\StringIdsSection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
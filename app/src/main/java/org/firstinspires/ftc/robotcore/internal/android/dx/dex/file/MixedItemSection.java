package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import org.firstinspires.ftc.robotcore.internal.android.dex.util.ExceptionWithContext;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public final class MixedItemSection extends Section {
  private static final Comparator<OffsettedItem> TYPE_SORTER = new Comparator<OffsettedItem>() {
      public int compare(OffsettedItem param1OffsettedItem1, OffsettedItem param1OffsettedItem2) {
        return param1OffsettedItem1.itemType().compareTo(param1OffsettedItem2.itemType());
      }
    };
  
  private final HashMap<OffsettedItem, OffsettedItem> interns = new HashMap<OffsettedItem, OffsettedItem>(100);
  
  private final ArrayList<OffsettedItem> items = new ArrayList<OffsettedItem>(100);
  
  private final SortType sort;
  
  private int writeSize;
  
  public MixedItemSection(String paramString, DexFile paramDexFile, int paramInt, SortType paramSortType) {
    super(paramString, paramDexFile, paramInt);
    this.sort = paramSortType;
    this.writeSize = -1;
  }
  
  public void add(OffsettedItem paramOffsettedItem) {
    throwIfPrepared();
    try {
      int i = paramOffsettedItem.getAlignment();
      int j = getAlignment();
      if (i <= j) {
        this.items.add(paramOffsettedItem);
        return;
      } 
      throw new IllegalArgumentException("incompatible item alignment");
    } catch (NullPointerException nullPointerException) {
      throw new NullPointerException("item == null");
    } 
  }
  
  public <T extends OffsettedItem> T get(T paramT) {
    throwIfNotPrepared();
    OffsettedItem offsettedItem = this.interns.get(paramT);
    if (offsettedItem != null)
      return (T)offsettedItem; 
    throw new NoSuchElementException(paramT.toString());
  }
  
  public int getAbsoluteItemOffset(Item paramItem) {
    return ((OffsettedItem)paramItem).getAbsoluteOffset();
  }
  
  public <T extends OffsettedItem> T intern(T paramT) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual throwIfPrepared : ()V
    //   6: aload_0
    //   7: getfield interns : Ljava/util/HashMap;
    //   10: aload_1
    //   11: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   14: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/dex/file/OffsettedItem
    //   17: astore_2
    //   18: aload_2
    //   19: ifnull -> 26
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_2
    //   25: areturn
    //   26: aload_0
    //   27: aload_1
    //   28: invokevirtual add : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/dex/file/OffsettedItem;)V
    //   31: aload_0
    //   32: getfield interns : Ljava/util/HashMap;
    //   35: aload_1
    //   36: aload_1
    //   37: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   40: pop
    //   41: aload_0
    //   42: monitorexit
    //   43: aload_1
    //   44: areturn
    //   45: astore_1
    //   46: aload_0
    //   47: monitorexit
    //   48: aload_1
    //   49: athrow
    // Exception table:
    //   from	to	target	type
    //   2	18	45	finally
    //   26	41	45	finally
  }
  
  public Collection<? extends Item> items() {
    return (Collection)this.items;
  }
  
  public void placeItems() {
    throwIfNotPrepared();
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$internal$android$dx$dex$file$MixedItemSection$SortType[this.sort.ordinal()];
    if (i != 1) {
      if (i == 2)
        Collections.sort(this.items, TYPE_SORTER); 
    } else {
      Collections.sort(this.items);
    } 
    int k = this.items.size();
    i = 0;
    int j = 0;
    while (i < k) {
      OffsettedItem offsettedItem = this.items.get(i);
      try {
        int m = offsettedItem.place(this, j);
        if (m >= j) {
          j = offsettedItem.writeSize() + m;
          i++;
          continue;
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("bogus place() result for ");
        stringBuilder.append(offsettedItem);
        throw new RuntimeException(stringBuilder.toString());
      } catch (RuntimeException runtimeException) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("...while placing ");
        stringBuilder.append(offsettedItem);
        throw ExceptionWithContext.withContext(runtimeException, stringBuilder.toString());
      } 
    } 
    this.writeSize = j;
  }
  
  protected void prepare0() {
    DexFile dexFile = getFile();
    int i = 0;
    label11: while (true) {
      int k = this.items.size();
      int j = i;
      if (i >= k)
        return; 
      while (true) {
        i = j;
        if (j < k) {
          ((OffsettedItem)this.items.get(j)).addContents(dexFile);
          j++;
          continue;
        } 
        continue label11;
      } 
      break;
    } 
  }
  
  public int size() {
    return this.items.size();
  }
  
  public void writeHeaderPart(AnnotatedOutput paramAnnotatedOutput) {
    throwIfNotPrepared();
    int i = this.writeSize;
    if (i != -1) {
      int j;
      if (i == 0) {
        j = 0;
      } else {
        j = getFileOffset();
      } 
      String str3 = getName();
      String str1 = str3;
      if (str3 == null)
        str1 = "<unnamed>"; 
      char[] arrayOfChar = new char[15 - str1.length()];
      Arrays.fill(arrayOfChar, ' ');
      String str2 = new String(arrayOfChar);
      if (paramAnnotatedOutput.annotates()) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str1);
        stringBuilder.append("_size:");
        stringBuilder.append(str2);
        stringBuilder.append(Hex.u4(i));
        paramAnnotatedOutput.annotate(4, stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(str1);
        stringBuilder.append("_off: ");
        stringBuilder.append(str2);
        stringBuilder.append(Hex.u4(j));
        paramAnnotatedOutput.annotate(4, stringBuilder.toString());
      } 
      paramAnnotatedOutput.writeInt(i);
      paramAnnotatedOutput.writeInt(j);
      return;
    } 
    throw new RuntimeException("write size not yet set");
  }
  
  public void writeIndexAnnotation(AnnotatedOutput paramAnnotatedOutput, ItemType paramItemType, String paramString) {
    throwIfNotPrepared();
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    for (OffsettedItem offsettedItem : this.items) {
      if (offsettedItem.itemType() == paramItemType)
        treeMap.put(offsettedItem.toHuman(), offsettedItem); 
    } 
    if (treeMap.size() == 0)
      return; 
    paramAnnotatedOutput.annotate(0, paramString);
    for (Map.Entry<Object, Object> entry : treeMap.entrySet()) {
      paramString = (String)entry.getKey();
      OffsettedItem offsettedItem = (OffsettedItem)entry.getValue();
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(offsettedItem.offsetString());
      stringBuilder.append(' ');
      stringBuilder.append(paramString);
      stringBuilder.append('\n');
      paramAnnotatedOutput.annotate(0, stringBuilder.toString());
    } 
  }
  
  public int writeSize() {
    throwIfNotPrepared();
    return this.writeSize;
  }
  
  protected void writeTo0(AnnotatedOutput paramAnnotatedOutput) {
    boolean bool = paramAnnotatedOutput.annotates();
    DexFile dexFile = getFile();
    Iterator<OffsettedItem> iterator = this.items.iterator();
    int j = 1;
    int i = 0;
    while (iterator.hasNext()) {
      OffsettedItem offsettedItem = iterator.next();
      int k = j;
      if (bool)
        if (j) {
          k = 0;
        } else {
          paramAnnotatedOutput.annotate(0, "\n");
          k = j;
        }  
      j = offsettedItem.getAlignment() - 1;
      int m = j & i + j;
      j = i;
      if (i != m) {
        paramAnnotatedOutput.writeZeroes(m - i);
        j = m;
      } 
      offsettedItem.writeTo(dexFile, paramAnnotatedOutput);
      i = j + offsettedItem.writeSize();
      j = k;
    } 
    if (i == this.writeSize)
      return; 
    throw new RuntimeException("output size mismatch");
  }
  
  enum SortType {
    INSTANCE, NONE, TYPE;
    
    static {
      SortType sortType = new SortType("INSTANCE", 2);
      INSTANCE = sortType;
      $VALUES = new SortType[] { NONE, TYPE, sortType };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\MixedItemSection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
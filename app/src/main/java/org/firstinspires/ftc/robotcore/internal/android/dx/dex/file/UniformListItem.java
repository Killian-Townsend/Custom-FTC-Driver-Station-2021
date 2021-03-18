package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import java.util.Iterator;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public final class UniformListItem<T extends OffsettedItem> extends OffsettedItem {
  private static final int HEADER_SIZE = 4;
  
  private final ItemType itemType;
  
  private final List<T> items;
  
  public UniformListItem(ItemType paramItemType, List<T> paramList) {
    super(getAlignment(paramList), writeSize(paramList));
    if (paramItemType != null) {
      this.items = paramList;
      this.itemType = paramItemType;
      return;
    } 
    throw new NullPointerException("itemType == null");
  }
  
  private static int getAlignment(List<? extends OffsettedItem> paramList) {
    try {
      return Math.max(4, ((OffsettedItem)paramList.get(0)).getAlignment());
    } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
      throw new IllegalArgumentException("items.size() == 0");
    } catch (NullPointerException nullPointerException) {
      throw new NullPointerException("items == null");
    } 
  }
  
  private int headerSize() {
    return getAlignment();
  }
  
  private static int writeSize(List<? extends OffsettedItem> paramList) {
    OffsettedItem offsettedItem = paramList.get(0);
    return paramList.size() * offsettedItem.writeSize() + getAlignment(paramList);
  }
  
  public void addContents(DexFile paramDexFile) {
    Iterator<T> iterator = this.items.iterator();
    while (iterator.hasNext())
      ((OffsettedItem)iterator.next()).addContents(paramDexFile); 
  }
  
  public final List<T> getItems() {
    return this.items;
  }
  
  public ItemType itemType() {
    return this.itemType;
  }
  
  protected void place0(Section paramSection, int paramInt) {
    paramInt += headerSize();
    Iterator<T> iterator = this.items.iterator();
    int i = -1;
    boolean bool = true;
    int j = -1;
    while (iterator.hasNext()) {
      OffsettedItem offsettedItem = (OffsettedItem)iterator.next();
      int k = offsettedItem.writeSize();
      if (bool) {
        j = offsettedItem.getAlignment();
        bool = false;
        i = k;
      } else if (k == i) {
        if (offsettedItem.getAlignment() != j)
          throw new UnsupportedOperationException("item alignment mismatch"); 
      } else {
        throw new UnsupportedOperationException("item size mismatch");
      } 
      paramInt = offsettedItem.place(paramSection, paramInt) + k;
    } 
  }
  
  public final String toHuman() {
    StringBuffer stringBuffer = new StringBuffer(100);
    stringBuffer.append("{");
    Iterator<T> iterator = this.items.iterator();
    boolean bool = true;
    while (iterator.hasNext()) {
      OffsettedItem offsettedItem = (OffsettedItem)iterator.next();
      if (bool) {
        bool = false;
      } else {
        stringBuffer.append(", ");
      } 
      stringBuffer.append(offsettedItem.toHuman());
    } 
    stringBuffer.append("}");
    return stringBuffer.toString();
  }
  
  public String toString() {
    StringBuffer stringBuffer = new StringBuffer(100);
    stringBuffer.append(getClass().getName());
    stringBuffer.append(this.items);
    return stringBuffer.toString();
  }
  
  protected void writeTo0(DexFile paramDexFile, AnnotatedOutput paramAnnotatedOutput) {
    int i = this.items.size();
    if (paramAnnotatedOutput.annotates()) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(offsetString());
      stringBuilder.append(" ");
      stringBuilder.append(typeName());
      paramAnnotatedOutput.annotate(0, stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("  size: ");
      stringBuilder.append(Hex.u4(i));
      paramAnnotatedOutput.annotate(4, stringBuilder.toString());
    } 
    paramAnnotatedOutput.writeInt(i);
    Iterator<T> iterator = this.items.iterator();
    while (iterator.hasNext())
      ((OffsettedItem)iterator.next()).writeTo(paramDexFile, paramAnnotatedOutput); 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\UniformListItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
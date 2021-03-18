package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import org.firstinspires.ftc.robotcore.internal.android.dex.util.ExceptionWithContext;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;

public abstract class OffsettedItem extends Item implements Comparable<OffsettedItem> {
  private Section addedTo;
  
  private final int alignment;
  
  private int offset;
  
  private int writeSize;
  
  public OffsettedItem(int paramInt1, int paramInt2) {
    Section.validateAlignment(paramInt1);
    if (paramInt2 >= -1) {
      this.alignment = paramInt1;
      this.writeSize = paramInt2;
      this.addedTo = null;
      this.offset = -1;
      return;
    } 
    throw new IllegalArgumentException("writeSize < -1");
  }
  
  public static int getAbsoluteOffsetOr0(OffsettedItem paramOffsettedItem) {
    return (paramOffsettedItem == null) ? 0 : paramOffsettedItem.getAbsoluteOffset();
  }
  
  public final int compareTo(OffsettedItem paramOffsettedItem) {
    if (this == paramOffsettedItem)
      return 0; 
    ItemType itemType1 = itemType();
    ItemType itemType2 = paramOffsettedItem.itemType();
    return (itemType1 != itemType2) ? itemType1.compareTo(itemType2) : compareTo0(paramOffsettedItem);
  }
  
  protected int compareTo0(OffsettedItem paramOffsettedItem) {
    throw new UnsupportedOperationException("unsupported");
  }
  
  public final boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    paramObject = paramObject;
    return (itemType() != paramObject.itemType()) ? false : ((compareTo0((OffsettedItem)paramObject) == 0));
  }
  
  public final int getAbsoluteOffset() {
    int i = this.offset;
    if (i >= 0)
      return this.addedTo.getAbsoluteOffset(i); 
    throw new RuntimeException("offset not yet known");
  }
  
  public final int getAlignment() {
    return this.alignment;
  }
  
  public final int getRelativeOffset() {
    int i = this.offset;
    if (i >= 0)
      return i; 
    throw new RuntimeException("offset not yet known");
  }
  
  public final String offsetString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append('[');
    stringBuilder.append(Integer.toHexString(getAbsoluteOffset()));
    stringBuilder.append(']');
    return stringBuilder.toString();
  }
  
  public final int place(Section paramSection, int paramInt) {
    if (paramSection != null) {
      if (paramInt >= 0) {
        if (this.addedTo == null) {
          int i = this.alignment - 1;
          paramInt = paramInt + i & i;
          this.addedTo = paramSection;
          this.offset = paramInt;
          place0(paramSection, paramInt);
          return paramInt;
        } 
        throw new RuntimeException("already written");
      } 
      throw new IllegalArgumentException("offset < 0");
    } 
    throw new NullPointerException("addedTo == null");
  }
  
  protected void place0(Section paramSection, int paramInt) {}
  
  public final void setWriteSize(int paramInt) {
    if (paramInt >= 0) {
      if (this.writeSize < 0) {
        this.writeSize = paramInt;
        return;
      } 
      throw new UnsupportedOperationException("writeSize already set");
    } 
    throw new IllegalArgumentException("writeSize < 0");
  }
  
  public abstract String toHuman();
  
  public final int writeSize() {
    int i = this.writeSize;
    if (i >= 0)
      return i; 
    throw new UnsupportedOperationException("writeSize is unknown");
  }
  
  public final void writeTo(DexFile paramDexFile, AnnotatedOutput paramAnnotatedOutput) {
    paramAnnotatedOutput.alignTo(this.alignment);
    try {
      if (this.writeSize >= 0) {
        paramAnnotatedOutput.assertCursor(getAbsoluteOffset());
        writeTo0(paramDexFile, paramAnnotatedOutput);
        return;
      } 
      throw new UnsupportedOperationException("writeSize is unknown");
    } catch (RuntimeException runtimeException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("...while writing ");
      stringBuilder.append(this);
      throw ExceptionWithContext.withContext(runtimeException, stringBuilder.toString());
    } 
  }
  
  protected abstract void writeTo0(DexFile paramDexFile, AnnotatedOutput paramAnnotatedOutput);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\OffsettedItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import java.util.Iterator;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.annotation.Annotation;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.annotation.Annotations;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public final class AnnotationSetItem extends OffsettedItem {
  private static final int ALIGNMENT = 4;
  
  private static final int ENTRY_WRITE_SIZE = 4;
  
  private final Annotations annotations;
  
  private final AnnotationItem[] items;
  
  public AnnotationSetItem(Annotations paramAnnotations, DexFile paramDexFile) {
    super(4, writeSize(paramAnnotations));
    this.annotations = paramAnnotations;
    this.items = new AnnotationItem[paramAnnotations.size()];
    Iterator<Annotation> iterator = paramAnnotations.getAnnotations().iterator();
    for (int i = 0; iterator.hasNext(); i++) {
      Annotation annotation = iterator.next();
      this.items[i] = new AnnotationItem(annotation, paramDexFile);
    } 
  }
  
  private static int writeSize(Annotations paramAnnotations) {
    try {
      int i = paramAnnotations.size();
      return i * 4 + 4;
    } catch (NullPointerException nullPointerException) {
      throw new NullPointerException("list == null");
    } 
  }
  
  public void addContents(DexFile paramDexFile) {
    MixedItemSection mixedItemSection = paramDexFile.getByteData();
    int j = this.items.length;
    for (int i = 0; i < j; i++) {
      AnnotationItem[] arrayOfAnnotationItem = this.items;
      arrayOfAnnotationItem[i] = mixedItemSection.<AnnotationItem>intern(arrayOfAnnotationItem[i]);
    } 
  }
  
  protected int compareTo0(OffsettedItem paramOffsettedItem) {
    paramOffsettedItem = paramOffsettedItem;
    return this.annotations.compareTo(((AnnotationSetItem)paramOffsettedItem).annotations);
  }
  
  public Annotations getAnnotations() {
    return this.annotations;
  }
  
  public int hashCode() {
    return this.annotations.hashCode();
  }
  
  public ItemType itemType() {
    return ItemType.TYPE_ANNOTATION_SET_ITEM;
  }
  
  protected void place0(Section paramSection, int paramInt) {
    AnnotationItem.sortByTypeIdIndex(this.items);
  }
  
  public String toHuman() {
    return this.annotations.toString();
  }
  
  protected void writeTo0(DexFile paramDexFile, AnnotatedOutput paramAnnotatedOutput) {
    boolean bool = paramAnnotatedOutput.annotates();
    int j = this.items.length;
    int i = 0;
    if (bool) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(offsetString());
      stringBuilder.append(" annotation set");
      paramAnnotatedOutput.annotate(0, stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("  size: ");
      stringBuilder.append(Hex.u4(j));
      paramAnnotatedOutput.annotate(4, stringBuilder.toString());
    } 
    paramAnnotatedOutput.writeInt(j);
    while (i < j) {
      int k = this.items[i].getAbsoluteOffset();
      if (bool) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("  entries[");
        stringBuilder.append(Integer.toHexString(i));
        stringBuilder.append("]: ");
        stringBuilder.append(Hex.u4(k));
        paramAnnotatedOutput.annotate(4, stringBuilder.toString());
        this.items[i].annotateTo(paramAnnotatedOutput, "    ");
      } 
      paramAnnotatedOutput.writeInt(k);
      i++;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\AnnotationSetItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
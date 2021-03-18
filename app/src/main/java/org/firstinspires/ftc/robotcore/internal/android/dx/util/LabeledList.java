package org.firstinspires.ftc.robotcore.internal.android.dx.util;

import java.util.Arrays;

public class LabeledList extends FixedSizeList {
  private final IntList labelToIndex;
  
  public LabeledList(int paramInt) {
    super(paramInt);
    this.labelToIndex = new IntList(paramInt);
  }
  
  public LabeledList(LabeledList paramLabeledList) {
    super(paramLabeledList.size());
    this.labelToIndex = paramLabeledList.labelToIndex.mutableCopy();
    int j = paramLabeledList.size();
    for (int i = 0; i < j; i++) {
      Object object = paramLabeledList.get0(i);
      if (object != null)
        set0(i, object); 
    } 
  }
  
  private void addLabelIndex(int paramInt1, int paramInt2) {
    int j = this.labelToIndex.size();
    for (int i = 0; i <= paramInt1 - j; i++)
      this.labelToIndex.add(-1); 
    this.labelToIndex.set(paramInt1, paramInt2);
  }
  
  private void rebuildLabelToIndex() {
    int j = size();
    for (int i = 0; i < j; i++) {
      LabeledItem labeledItem = (LabeledItem)get0(i);
      if (labeledItem != null)
        this.labelToIndex.set(labeledItem.getLabel(), i); 
    } 
  }
  
  private void removeLabel(int paramInt) {
    this.labelToIndex.set(paramInt, -1);
  }
  
  public final int[] getLabelsInOrder() {
    StringBuilder stringBuilder;
    int j = size();
    int[] arrayOfInt = new int[j];
    int i = 0;
    while (i < j) {
      LabeledItem labeledItem = (LabeledItem)get0(i);
      if (labeledItem != null) {
        arrayOfInt[i] = labeledItem.getLabel();
        i++;
        continue;
      } 
      stringBuilder = new StringBuilder();
      stringBuilder.append("null at index ");
      stringBuilder.append(i);
      throw new NullPointerException(stringBuilder.toString());
    } 
    Arrays.sort((int[])stringBuilder);
    return (int[])stringBuilder;
  }
  
  public final int getMaxLabel() {
    int i;
    for (i = this.labelToIndex.size() - 1; i >= 0 && this.labelToIndex.get(i) < 0; i--);
    this.labelToIndex.shrink(++i);
    return i;
  }
  
  public final int indexOfLabel(int paramInt) {
    return (paramInt >= this.labelToIndex.size()) ? -1 : this.labelToIndex.get(paramInt);
  }
  
  protected void set(int paramInt, LabeledItem paramLabeledItem) {
    LabeledItem labeledItem = (LabeledItem)getOrNull0(paramInt);
    set0(paramInt, paramLabeledItem);
    if (labeledItem != null)
      removeLabel(labeledItem.getLabel()); 
    if (paramLabeledItem != null)
      addLabelIndex(paramLabeledItem.getLabel(), paramInt); 
  }
  
  public void shrinkToFit() {
    super.shrinkToFit();
    rebuildLabelToIndex();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\d\\util\LabeledList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import java.util.Collection;
import java.util.Iterator;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;

public abstract class UniformItemSection extends Section {
  public UniformItemSection(String paramString, DexFile paramDexFile, int paramInt) {
    super(paramString, paramDexFile, paramInt);
  }
  
  public abstract IndexedItem get(Constant paramConstant);
  
  public final int getAbsoluteItemOffset(Item paramItem) {
    paramItem = paramItem;
    return getAbsoluteOffset(paramItem.getIndex() * paramItem.writeSize());
  }
  
  protected abstract void orderItems();
  
  protected final void prepare0() {
    DexFile dexFile = getFile();
    orderItems();
    Iterator<? extends Item> iterator = items().iterator();
    while (iterator.hasNext())
      ((Item)iterator.next()).addContents(dexFile); 
  }
  
  public final int writeSize() {
    Collection<? extends Item> collection = items();
    int i = collection.size();
    return (i == 0) ? 0 : (i * ((Item)collection.iterator().next()).writeSize());
  }
  
  protected final void writeTo0(AnnotatedOutput paramAnnotatedOutput) {
    DexFile dexFile = getFile();
    int i = getAlignment();
    Iterator<? extends Item> iterator = items().iterator();
    while (iterator.hasNext()) {
      ((Item)iterator.next()).writeTo(dexFile, paramAnnotatedOutput);
      paramAnnotatedOutput.alignTo(i);
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\UniformItemSection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public final class TypeIdItem extends IdItem {
  public TypeIdItem(CstType paramCstType) {
    super(paramCstType);
  }
  
  public void addContents(DexFile paramDexFile) {
    paramDexFile.getStringIds().intern(getDefiningClass().getDescriptor());
  }
  
  public ItemType itemType() {
    return ItemType.TYPE_TYPE_ID_ITEM;
  }
  
  public int writeSize() {
    return 4;
  }
  
  public void writeTo(DexFile paramDexFile, AnnotatedOutput paramAnnotatedOutput) {
    CstString cstString = getDefiningClass().getDescriptor();
    int i = paramDexFile.getStringIds().indexOf(cstString);
    if (paramAnnotatedOutput.annotates()) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(indexString());
      stringBuilder.append(' ');
      stringBuilder.append(cstString.toHuman());
      paramAnnotatedOutput.annotate(0, stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("  descriptor_idx: ");
      stringBuilder.append(Hex.u4(i));
      paramAnnotatedOutput.annotate(4, stringBuilder.toString());
    } 
    paramAnnotatedOutput.writeInt(i);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\TypeIdItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
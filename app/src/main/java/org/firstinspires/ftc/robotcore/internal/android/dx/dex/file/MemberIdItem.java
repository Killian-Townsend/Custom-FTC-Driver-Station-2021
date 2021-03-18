package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstMemberRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstNat;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public abstract class MemberIdItem extends IdItem {
  private final CstMemberRef cst;
  
  public MemberIdItem(CstMemberRef paramCstMemberRef) {
    super(paramCstMemberRef.getDefiningClass());
    this.cst = paramCstMemberRef;
  }
  
  public void addContents(DexFile paramDexFile) {
    super.addContents(paramDexFile);
    paramDexFile.getStringIds().intern(getRef().getNat().getName());
  }
  
  public final CstMemberRef getRef() {
    return this.cst;
  }
  
  protected abstract int getTypoidIdx(DexFile paramDexFile);
  
  protected abstract String getTypoidName();
  
  public int writeSize() {
    return 8;
  }
  
  public final void writeTo(DexFile paramDexFile, AnnotatedOutput paramAnnotatedOutput) {
    TypeIdsSection typeIdsSection = paramDexFile.getTypeIds();
    StringIdsSection stringIdsSection = paramDexFile.getStringIds();
    CstNat cstNat = this.cst.getNat();
    int i = typeIdsSection.indexOf(getDefiningClass());
    int j = stringIdsSection.indexOf(cstNat.getName());
    int k = getTypoidIdx(paramDexFile);
    if (paramAnnotatedOutput.annotates()) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(indexString());
      stringBuilder.append(' ');
      stringBuilder.append(this.cst.toHuman());
      paramAnnotatedOutput.annotate(0, stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("  class_idx: ");
      stringBuilder.append(Hex.u2(i));
      paramAnnotatedOutput.annotate(2, stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append(getTypoidName());
      stringBuilder.append(':');
      paramAnnotatedOutput.annotate(2, String.format("  %-10s %s", new Object[] { stringBuilder.toString(), Hex.u2(k) }));
      stringBuilder = new StringBuilder();
      stringBuilder.append("  name_idx:  ");
      stringBuilder.append(Hex.u4(j));
      paramAnnotatedOutput.annotate(4, stringBuilder.toString());
    } 
    paramAnnotatedOutput.writeShort(i);
    paramAnnotatedOutput.writeShort(k);
    paramAnnotatedOutput.writeInt(j);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\MemberIdItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
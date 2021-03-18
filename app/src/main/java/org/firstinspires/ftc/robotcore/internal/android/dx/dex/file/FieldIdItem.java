package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstFieldRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstMemberRef;

public final class FieldIdItem extends MemberIdItem {
  public FieldIdItem(CstFieldRef paramCstFieldRef) {
    super((CstMemberRef)paramCstFieldRef);
  }
  
  public void addContents(DexFile paramDexFile) {
    super.addContents(paramDexFile);
    paramDexFile.getTypeIds().intern(getFieldRef().getType());
  }
  
  public CstFieldRef getFieldRef() {
    return (CstFieldRef)getRef();
  }
  
  protected int getTypoidIdx(DexFile paramDexFile) {
    return paramDexFile.getTypeIds().indexOf(getFieldRef().getType());
  }
  
  protected String getTypoidName() {
    return "type_idx";
  }
  
  public ItemType itemType() {
    return ItemType.TYPE_FIELD_ID_ITEM;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\FieldIdItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
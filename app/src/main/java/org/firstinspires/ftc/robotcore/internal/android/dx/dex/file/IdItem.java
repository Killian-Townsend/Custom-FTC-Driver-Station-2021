package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;

public abstract class IdItem extends IndexedItem {
  private final CstType type;
  
  public IdItem(CstType paramCstType) {
    if (paramCstType != null) {
      this.type = paramCstType;
      return;
    } 
    throw new NullPointerException("type == null");
  }
  
  public void addContents(DexFile paramDexFile) {
    paramDexFile.getTypeIds().intern(this.type);
  }
  
  public final CstType getDefiningClass() {
    return this.type;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\IdItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
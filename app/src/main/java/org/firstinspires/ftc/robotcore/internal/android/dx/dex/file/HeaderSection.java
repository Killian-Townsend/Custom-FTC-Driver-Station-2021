package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;

public final class HeaderSection extends UniformItemSection {
  private final List<HeaderItem> list;
  
  public HeaderSection(DexFile paramDexFile) {
    super(null, paramDexFile, 4);
    HeaderItem headerItem = new HeaderItem();
    headerItem.setIndex(0);
    this.list = Collections.singletonList(headerItem);
  }
  
  public IndexedItem get(Constant paramConstant) {
    return null;
  }
  
  public Collection<? extends Item> items() {
    return (Collection)this.list;
  }
  
  protected void orderItems() {}
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\HeaderSection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
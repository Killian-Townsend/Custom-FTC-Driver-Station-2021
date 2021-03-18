package org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.FixedSizeList;

public final class InnerClassList extends FixedSizeList {
  public InnerClassList(int paramInt) {
    super(paramInt);
  }
  
  public Item get(int paramInt) {
    return (Item)get0(paramInt);
  }
  
  public void set(int paramInt1, CstType paramCstType1, CstType paramCstType2, CstString paramCstString, int paramInt2) {
    set0(paramInt1, new Item(paramCstType1, paramCstType2, paramCstString, paramInt2));
  }
  
  public static class Item {
    private final int accessFlags;
    
    private final CstType innerClass;
    
    private final CstString innerName;
    
    private final CstType outerClass;
    
    public Item(CstType param1CstType1, CstType param1CstType2, CstString param1CstString, int param1Int) {
      if (param1CstType1 != null) {
        this.innerClass = param1CstType1;
        this.outerClass = param1CstType2;
        this.innerName = param1CstString;
        this.accessFlags = param1Int;
        return;
      } 
      throw new NullPointerException("innerClass == null");
    }
    
    public int getAccessFlags() {
      return this.accessFlags;
    }
    
    public CstType getInnerClass() {
      return this.innerClass;
    }
    
    public CstString getInnerName() {
      return this.innerName;
    }
    
    public CstType getOuterClass() {
      return this.outerClass;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\attrib\InnerClassList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
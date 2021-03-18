package org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface;

import org.firstinspires.ftc.robotcore.internal.android.dx.util.FixedSizeList;

public final class StdFieldList extends FixedSizeList implements FieldList {
  public StdFieldList(int paramInt) {
    super(paramInt);
  }
  
  public Field get(int paramInt) {
    return (Field)get0(paramInt);
  }
  
  public void set(int paramInt, Field paramField) {
    set0(paramInt, paramField);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\iface\StdFieldList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
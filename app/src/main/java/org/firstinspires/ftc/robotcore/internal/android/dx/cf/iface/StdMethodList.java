package org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface;

import org.firstinspires.ftc.robotcore.internal.android.dx.util.FixedSizeList;

public final class StdMethodList extends FixedSizeList implements MethodList {
  public StdMethodList(int paramInt) {
    super(paramInt);
  }
  
  public Method get(int paramInt) {
    return (Method)get0(paramInt);
  }
  
  public void set(int paramInt, Method paramMethod) {
    set0(paramInt, paramMethod);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\iface\StdMethodList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
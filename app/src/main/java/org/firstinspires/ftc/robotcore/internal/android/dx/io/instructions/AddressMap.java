package org.firstinspires.ftc.robotcore.internal.android.dx.io.instructions;

import java.util.HashMap;

public final class AddressMap {
  private final HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
  
  public int get(int paramInt) {
    Integer integer = this.map.get(Integer.valueOf(paramInt));
    return (integer == null) ? -1 : integer.intValue();
  }
  
  public void put(int paramInt1, int paramInt2) {
    this.map.put(Integer.valueOf(paramInt1), Integer.valueOf(paramInt2));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\io\instructions\AddressMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
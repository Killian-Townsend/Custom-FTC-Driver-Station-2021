package org.firstinspires.ftc.robotcore.internal.android.dx.io.instructions;

public abstract class BaseCodeCursor implements CodeCursor {
  private final AddressMap baseAddressMap = new AddressMap();
  
  private int cursor = 0;
  
  protected final void advance(int paramInt) {
    this.cursor += paramInt;
  }
  
  public final int baseAddressForCursor() {
    int i = this.baseAddressMap.get(this.cursor);
    return (i >= 0) ? i : this.cursor;
  }
  
  public final int cursor() {
    return this.cursor;
  }
  
  public final void setBaseAddress(int paramInt1, int paramInt2) {
    this.baseAddressMap.put(paramInt1, paramInt2);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\io\instructions\BaseCodeCursor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

public abstract class IndexedItem extends Item {
  private int index = -1;
  
  public final int getIndex() {
    int i = this.index;
    if (i >= 0)
      return i; 
    throw new RuntimeException("index not yet set");
  }
  
  public final boolean hasIndex() {
    return (this.index >= 0);
  }
  
  public final String indexString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append('[');
    stringBuilder.append(Integer.toHexString(this.index));
    stringBuilder.append(']');
    return stringBuilder.toString();
  }
  
  public final void setIndex(int paramInt) {
    if (this.index == -1) {
      this.index = paramInt;
      return;
    } 
    throw new RuntimeException("index already set");
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\IndexedItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
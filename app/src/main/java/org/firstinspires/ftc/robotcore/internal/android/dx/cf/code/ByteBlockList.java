package org.firstinspires.ftc.robotcore.internal.android.dx.cf.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.LabeledList;

public final class ByteBlockList extends LabeledList {
  public ByteBlockList(int paramInt) {
    super(paramInt);
  }
  
  public ByteBlock get(int paramInt) {
    return (ByteBlock)get0(paramInt);
  }
  
  public ByteBlock labelToBlock(int paramInt) {
    int i = indexOfLabel(paramInt);
    if (i >= 0)
      return get(i); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("no such label: ");
    stringBuilder.append(Hex.u2(paramInt));
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void set(int paramInt, ByteBlock paramByteBlock) {
    set(paramInt, paramByteBlock);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\code\ByteBlockList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
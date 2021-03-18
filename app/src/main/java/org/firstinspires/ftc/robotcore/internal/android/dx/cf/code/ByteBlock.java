package org.firstinspires.ftc.robotcore.internal.android.dx.cf.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IntList;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.LabeledItem;

public final class ByteBlock implements LabeledItem {
  private final ByteCatchList catches;
  
  private final int end;
  
  private final int label;
  
  private final int start;
  
  private final IntList successors;
  
  public ByteBlock(int paramInt1, int paramInt2, int paramInt3, IntList paramIntList, ByteCatchList paramByteCatchList) {
    if (paramInt1 >= 0) {
      if (paramInt2 >= 0) {
        if (paramInt3 > paramInt2) {
          if (paramIntList != null) {
            StringBuilder stringBuilder;
            int j = paramIntList.size();
            int i = 0;
            while (i < j) {
              if (paramIntList.get(i) >= 0) {
                i++;
                continue;
              } 
              stringBuilder = new StringBuilder();
              stringBuilder.append("successors[");
              stringBuilder.append(i);
              stringBuilder.append("] == ");
              stringBuilder.append(paramIntList.get(i));
              throw new IllegalArgumentException(stringBuilder.toString());
            } 
            if (stringBuilder != null) {
              this.label = paramInt1;
              this.start = paramInt2;
              this.end = paramInt3;
              this.successors = paramIntList;
              this.catches = (ByteCatchList)stringBuilder;
              return;
            } 
            throw new NullPointerException("catches == null");
          } 
          throw new NullPointerException("targets == null");
        } 
        throw new IllegalArgumentException("end <= start");
      } 
      throw new IllegalArgumentException("start < 0");
    } 
    throw new IllegalArgumentException("label < 0");
  }
  
  public ByteCatchList getCatches() {
    return this.catches;
  }
  
  public int getEnd() {
    return this.end;
  }
  
  public int getLabel() {
    return this.label;
  }
  
  public int getStart() {
    return this.start;
  }
  
  public IntList getSuccessors() {
    return this.successors;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append('{');
    stringBuilder.append(Hex.u2(this.label));
    stringBuilder.append(": ");
    stringBuilder.append(Hex.u2(this.start));
    stringBuilder.append("build/generated/source/aidl");
    stringBuilder.append(Hex.u2(this.end));
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\code\ByteBlock.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
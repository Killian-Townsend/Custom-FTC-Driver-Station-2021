package org.firstinspires.ftc.robotcore.internal.android.dex;

import org.firstinspires.ftc.robotcore.internal.android.dex.util.Unsigned;

public final class ProtoId implements Comparable<ProtoId> {
  private final Dex dex;
  
  private final int parametersOffset;
  
  private final int returnTypeIndex;
  
  private final int shortyIndex;
  
  public ProtoId(Dex paramDex, int paramInt1, int paramInt2, int paramInt3) {
    this.dex = paramDex;
    this.shortyIndex = paramInt1;
    this.returnTypeIndex = paramInt2;
    this.parametersOffset = paramInt3;
  }
  
  public int compareTo(ProtoId paramProtoId) {
    int i = this.returnTypeIndex;
    int j = paramProtoId.returnTypeIndex;
    return (i != j) ? Unsigned.compare(i, j) : Unsigned.compare(this.parametersOffset, paramProtoId.parametersOffset);
  }
  
  public int getParametersOffset() {
    return this.parametersOffset;
  }
  
  public int getReturnTypeIndex() {
    return this.returnTypeIndex;
  }
  
  public int getShortyIndex() {
    return this.shortyIndex;
  }
  
  public String toString() {
    if (this.dex == null) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(this.shortyIndex);
      stringBuilder1.append(" ");
      stringBuilder1.append(this.returnTypeIndex);
      stringBuilder1.append(" ");
      stringBuilder1.append(this.parametersOffset);
      return stringBuilder1.toString();
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.dex.strings().get(this.shortyIndex));
    stringBuilder.append(": ");
    stringBuilder.append(this.dex.typeNames().get(this.returnTypeIndex));
    stringBuilder.append(" ");
    stringBuilder.append(this.dex.readTypeList(this.parametersOffset));
    return stringBuilder.toString();
  }
  
  public void writeTo(Dex.Section paramSection) {
    paramSection.writeInt(this.shortyIndex);
    paramSection.writeInt(this.returnTypeIndex);
    paramSection.writeInt(this.parametersOffset);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dex\ProtoId.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
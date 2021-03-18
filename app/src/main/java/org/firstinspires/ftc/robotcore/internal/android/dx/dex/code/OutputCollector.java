package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code;

import java.util.ArrayList;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.DexOptions;

public final class OutputCollector {
  private final OutputFinisher finisher;
  
  private ArrayList<DalvInsn> suffix;
  
  public OutputCollector(DexOptions paramDexOptions, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.finisher = new OutputFinisher(paramDexOptions, paramInt1, paramInt3, paramInt4);
    this.suffix = new ArrayList<DalvInsn>(paramInt2);
  }
  
  private void appendSuffixToOutput() {
    int j = this.suffix.size();
    for (int i = 0; i < j; i++)
      this.finisher.add(this.suffix.get(i)); 
    this.suffix = null;
  }
  
  public void add(DalvInsn paramDalvInsn) {
    this.finisher.add(paramDalvInsn);
  }
  
  public void addSuffix(DalvInsn paramDalvInsn) {
    this.suffix.add(paramDalvInsn);
  }
  
  public OutputFinisher getFinisher() {
    if (this.suffix != null) {
      appendSuffixToOutput();
      return this.finisher;
    } 
    throw new UnsupportedOperationException("already processed");
  }
  
  public void reverseBranch(int paramInt, CodeAddress paramCodeAddress) {
    this.finisher.reverseBranch(paramInt, paramCodeAddress);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\OutputCollector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
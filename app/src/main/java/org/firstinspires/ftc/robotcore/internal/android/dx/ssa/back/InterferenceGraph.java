package org.firstinspires.ftc.robotcore.internal.android.dx.ssa.back;

import java.util.ArrayList;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.SetFactory;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IntSet;

public class InterferenceGraph {
  private final ArrayList<IntSet> interference;
  
  public InterferenceGraph(int paramInt) {
    this.interference = new ArrayList<IntSet>(paramInt);
    for (int i = 0; i < paramInt; i++)
      this.interference.add(SetFactory.makeInterferenceSet(paramInt)); 
  }
  
  private void ensureCapacity(int paramInt) {
    int i = this.interference.size();
    this.interference.ensureCapacity(paramInt);
    while (i < paramInt) {
      this.interference.add(SetFactory.makeInterferenceSet(paramInt));
      i++;
    } 
  }
  
  public void add(int paramInt1, int paramInt2) {
    ensureCapacity(Math.max(paramInt1, paramInt2) + 1);
    ((IntSet)this.interference.get(paramInt1)).add(paramInt2);
    ((IntSet)this.interference.get(paramInt2)).add(paramInt1);
  }
  
  public void dumpToStdout() {
    int j = this.interference.size();
    for (int i = 0; i < j; i++) {
      StringBuilder stringBuilder1 = new StringBuilder();
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append("Reg ");
      stringBuilder2.append(i);
      stringBuilder2.append(":");
      stringBuilder2.append(((IntSet)this.interference.get(i)).toString());
      stringBuilder1.append(stringBuilder2.toString());
      System.out.println(stringBuilder1.toString());
    } 
  }
  
  public void mergeInterferenceSet(int paramInt, IntSet paramIntSet) {
    if (paramInt < this.interference.size())
      paramIntSet.merge(this.interference.get(paramInt)); 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\ssa\back\InterferenceGraph.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
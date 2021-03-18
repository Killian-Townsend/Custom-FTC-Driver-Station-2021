package org.firstinspires.ftc.robotcore.internal.android.dx.util;

public interface IntSet {
  void add(int paramInt);
  
  int elements();
  
  boolean has(int paramInt);
  
  IntIterator iterator();
  
  void merge(IntSet paramIntSet);
  
  void remove(int paramInt);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\d\\util\IntSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
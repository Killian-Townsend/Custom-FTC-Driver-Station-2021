package org.firstinspires.ftc.robotcore.internal.android.dx.merge;

import java.util.Comparator;
import org.firstinspires.ftc.robotcore.internal.android.dex.ClassDef;
import org.firstinspires.ftc.robotcore.internal.android.dex.Dex;

final class SortableType {
  public static final Comparator<SortableType> NULLS_LAST_ORDER = new Comparator<SortableType>() {
      public int compare(SortableType param1SortableType1, SortableType param1SortableType2) {
        if (param1SortableType1 == param1SortableType2)
          return 0; 
        if (param1SortableType2 == null)
          return -1; 
        if (param1SortableType1 == null)
          return 1; 
        if (param1SortableType1.depth != param1SortableType2.depth) {
          int k = param1SortableType1.depth;
          int m = param1SortableType2.depth;
          return k - m;
        } 
        int i = param1SortableType1.getTypeIndex();
        int j = param1SortableType2.getTypeIndex();
        return i - j;
      }
    };
  
  private ClassDef classDef;
  
  private int depth = -1;
  
  private final Dex dex;
  
  private final IndexMap indexMap;
  
  public SortableType(Dex paramDex, IndexMap paramIndexMap, ClassDef paramClassDef) {
    this.dex = paramDex;
    this.indexMap = paramIndexMap;
    this.classDef = paramClassDef;
  }
  
  public ClassDef getClassDef() {
    return this.classDef;
  }
  
  public Dex getDex() {
    return this.dex;
  }
  
  public IndexMap getIndexMap() {
    return this.indexMap;
  }
  
  public int getTypeIndex() {
    return this.classDef.getTypeIndex();
  }
  
  public boolean isDepthAssigned() {
    return (this.depth != -1);
  }
  
  public boolean tryAssignDepth(SortableType[] paramArrayOfSortableType) {
    int i;
    if (this.classDef.getSupertypeIndex() == -1) {
      i = 0;
    } else {
      SortableType sortableType = paramArrayOfSortableType[this.classDef.getSupertypeIndex()];
      if (sortableType == null) {
        i = 1;
      } else {
        int m = sortableType.depth;
        i = m;
        if (m == -1)
          return false; 
      } 
    } 
    short[] arrayOfShort = this.classDef.getInterfaces();
    int k = arrayOfShort.length;
    for (int j = 0; j < k; j++) {
      SortableType sortableType = paramArrayOfSortableType[arrayOfShort[j]];
      if (sortableType == null) {
        i = Math.max(i, 1);
      } else {
        int m = sortableType.depth;
        if (m == -1)
          return false; 
        i = Math.max(i, m);
      } 
    } 
    this.depth = i + 1;
    return true;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\merge\SortableType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst;

import org.firstinspires.ftc.robotcore.internal.android.dx.util.FixedSizeList;

public final class CstArray extends Constant {
  private final List list;
  
  public CstArray(List paramList) {
    if (paramList != null) {
      paramList.throwIfMutable();
      this.list = paramList;
      return;
    } 
    throw new NullPointerException("list == null");
  }
  
  protected int compareTo0(Constant paramConstant) {
    return this.list.compareTo(((CstArray)paramConstant).list);
  }
  
  public boolean equals(Object paramObject) {
    return !(paramObject instanceof CstArray) ? false : this.list.equals(((CstArray)paramObject).list);
  }
  
  public List getList() {
    return this.list;
  }
  
  public int hashCode() {
    return this.list.hashCode();
  }
  
  public boolean isCategory2() {
    return false;
  }
  
  public String toHuman() {
    return this.list.toHuman("{", ", ", "}");
  }
  
  public String toString() {
    return this.list.toString("array{", ", ", "}");
  }
  
  public String typeName() {
    return "array";
  }
  
  public static final class List extends FixedSizeList implements Comparable<List> {
    public List(int param1Int) {
      super(param1Int);
    }
    
    public int compareTo(List param1List) {
      int i;
      int k = size();
      int m = param1List.size();
      if (k < m) {
        i = k;
      } else {
        i = m;
      } 
      for (int j = 0; j < i; j++) {
        int n = ((Constant)get0(j)).compareTo((Constant)param1List.get0(j));
        if (n != 0)
          return n; 
      } 
      return (k < m) ? -1 : ((k > m) ? 1 : 0);
    }
    
    public Constant get(int param1Int) {
      return (Constant)get0(param1Int);
    }
    
    public void set(int param1Int, Constant param1Constant) {
      set0(param1Int, param1Constant);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\cst\CstArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
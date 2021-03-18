package org.firstinspires.ftc.robotcore.internal.android.dx.cf.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.util.FixedSizeList;

public final class LineNumberList extends FixedSizeList {
  public static final LineNumberList EMPTY = new LineNumberList(0);
  
  public LineNumberList(int paramInt) {
    super(paramInt);
  }
  
  public static LineNumberList concat(LineNumberList paramLineNumberList1, LineNumberList paramLineNumberList2) {
    int j;
    if (paramLineNumberList1 == EMPTY)
      return paramLineNumberList2; 
    int k = paramLineNumberList1.size();
    int m = paramLineNumberList2.size();
    LineNumberList lineNumberList = new LineNumberList(k + m);
    byte b = 0;
    int i = 0;
    while (true) {
      j = b;
      if (i < k) {
        lineNumberList.set(i, paramLineNumberList1.get(i));
        i++;
        continue;
      } 
      break;
    } 
    while (j < m) {
      lineNumberList.set(k + j, paramLineNumberList2.get(j));
      j++;
    } 
    return lineNumberList;
  }
  
  public Item get(int paramInt) {
    return (Item)get0(paramInt);
  }
  
  public int pcToLine(int paramInt) {
    int m = size();
    int k = -1;
    int i = 0;
    int j;
    for (j = -1; i < m; j = n) {
      Item item = get(i);
      int i2 = item.getStartPc();
      int i1 = k;
      int n = j;
      if (i2 <= paramInt) {
        i1 = k;
        n = j;
        if (i2 > k) {
          n = item.getLineNumber();
          if (i2 == paramInt)
            return n; 
          i1 = i2;
        } 
      } 
      i++;
      k = i1;
    } 
    return j;
  }
  
  public void set(int paramInt1, int paramInt2, int paramInt3) {
    set0(paramInt1, new Item(paramInt2, paramInt3));
  }
  
  public void set(int paramInt, Item paramItem) {
    if (paramItem != null) {
      set0(paramInt, paramItem);
      return;
    } 
    throw new NullPointerException("item == null");
  }
  
  public static class Item {
    private final int lineNumber;
    
    private final int startPc;
    
    public Item(int param1Int1, int param1Int2) {
      if (param1Int1 >= 0) {
        if (param1Int2 >= 0) {
          this.startPc = param1Int1;
          this.lineNumber = param1Int2;
          return;
        } 
        throw new IllegalArgumentException("lineNumber < 0");
      } 
      throw new IllegalArgumentException("startPc < 0");
    }
    
    public int getLineNumber() {
      return this.lineNumber;
    }
    
    public int getStartPc() {
      return this.startPc;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\code\LineNumberList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
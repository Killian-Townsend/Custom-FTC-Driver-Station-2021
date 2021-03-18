package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.FixedSizeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public final class CatchHandlerList extends FixedSizeList implements Comparable<CatchHandlerList> {
  public static final CatchHandlerList EMPTY = new CatchHandlerList(0);
  
  public CatchHandlerList(int paramInt) {
    super(paramInt);
  }
  
  public boolean catchesAll() {
    int i = size();
    return (i == 0) ? false : get(i - 1).getExceptionType().equals(CstType.OBJECT);
  }
  
  public int compareTo(CatchHandlerList paramCatchHandlerList) {
    if (this == paramCatchHandlerList)
      return 0; 
    int j = size();
    int k = paramCatchHandlerList.size();
    int m = Math.min(j, k);
    for (int i = 0; i < m; i++) {
      int n = get(i).compareTo(paramCatchHandlerList.get(i));
      if (n != 0)
        return n; 
    } 
    return (j < k) ? -1 : ((j > k) ? 1 : 0);
  }
  
  public Entry get(int paramInt) {
    return (Entry)get0(paramInt);
  }
  
  public void set(int paramInt, Entry paramEntry) {
    set0(paramInt, paramEntry);
  }
  
  public void set(int paramInt1, CstType paramCstType, int paramInt2) {
    set0(paramInt1, new Entry(paramCstType, paramInt2));
  }
  
  public String toHuman() {
    return toHuman("", "");
  }
  
  public String toHuman(String paramString1, String paramString2) {
    StringBuilder stringBuilder = new StringBuilder(100);
    int j = size();
    stringBuilder.append(paramString1);
    stringBuilder.append(paramString2);
    stringBuilder.append("catch ");
    for (int i = 0; i < j; i++) {
      Entry entry = get(i);
      if (i != 0) {
        stringBuilder.append(",\n");
        stringBuilder.append(paramString1);
        stringBuilder.append("  ");
      } 
      if (i == j - 1 && catchesAll()) {
        stringBuilder.append("<any>");
      } else {
        stringBuilder.append(entry.getExceptionType().toHuman());
      } 
      stringBuilder.append(" -> ");
      stringBuilder.append(Hex.u2or4(entry.getHandler()));
    } 
    return stringBuilder.toString();
  }
  
  public static class Entry implements Comparable<Entry> {
    private final CstType exceptionType;
    
    private final int handler;
    
    public Entry(CstType param1CstType, int param1Int) {
      if (param1Int >= 0) {
        if (param1CstType != null) {
          this.handler = param1Int;
          this.exceptionType = param1CstType;
          return;
        } 
        throw new NullPointerException("exceptionType == null");
      } 
      throw new IllegalArgumentException("handler < 0");
    }
    
    public int compareTo(Entry param1Entry) {
      int i = this.handler;
      int j = param1Entry.handler;
      return (i < j) ? -1 : ((i > j) ? 1 : this.exceptionType.compareTo((Constant)param1Entry.exceptionType));
    }
    
    public boolean equals(Object param1Object) {
      boolean bool = param1Object instanceof Entry;
      boolean bool2 = false;
      boolean bool1 = bool2;
      if (bool) {
        bool1 = bool2;
        if (compareTo((Entry)param1Object) == 0)
          bool1 = true; 
      } 
      return bool1;
    }
    
    public CstType getExceptionType() {
      return this.exceptionType;
    }
    
    public int getHandler() {
      return this.handler;
    }
    
    public int hashCode() {
      return this.handler * 31 + this.exceptionType.hashCode();
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\CatchHandlerList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
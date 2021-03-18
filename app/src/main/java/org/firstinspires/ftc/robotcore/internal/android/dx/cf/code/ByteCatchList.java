package org.firstinspires.ftc.robotcore.internal.android.dx.cf.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.StdTypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.FixedSizeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IntList;

public final class ByteCatchList extends FixedSizeList {
  public static final ByteCatchList EMPTY = new ByteCatchList(0);
  
  public ByteCatchList(int paramInt) {
    super(paramInt);
  }
  
  private static boolean typeNotFound(Item paramItem, Item[] paramArrayOfItem, int paramInt) {
    CstType cstType = paramItem.getExceptionClass();
    int i = 0;
    while (i < paramInt) {
      CstType cstType1 = paramArrayOfItem[i].getExceptionClass();
      if (cstType1 != cstType) {
        if (cstType1 == CstType.OBJECT)
          return false; 
        i++;
        continue;
      } 
      return false;
    } 
    return true;
  }
  
  public int byteLength() {
    return size() * 8 + 2;
  }
  
  public Item get(int paramInt) {
    return (Item)get0(paramInt);
  }
  
  public ByteCatchList listFor(int paramInt) {
    int k = size();
    Item[] arrayOfItem = new Item[k];
    boolean bool = false;
    int j = 0;
    int i;
    for (i = j; j < k; i = m) {
      Item item = get(j);
      int m = i;
      if (item.covers(paramInt)) {
        m = i;
        if (typeNotFound(item, arrayOfItem, i)) {
          arrayOfItem[i] = item;
          m = i + 1;
        } 
      } 
      j++;
    } 
    if (i == 0)
      return EMPTY; 
    ByteCatchList byteCatchList = new ByteCatchList(i);
    for (paramInt = bool; paramInt < i; paramInt++)
      byteCatchList.set(paramInt, arrayOfItem[paramInt]); 
    byteCatchList.setImmutable();
    return byteCatchList;
  }
  
  public void set(int paramInt1, int paramInt2, int paramInt3, int paramInt4, CstType paramCstType) {
    set0(paramInt1, new Item(paramInt2, paramInt3, paramInt4, paramCstType));
  }
  
  public void set(int paramInt, Item paramItem) {
    if (paramItem != null) {
      set0(paramInt, paramItem);
      return;
    } 
    throw new NullPointerException("item == null");
  }
  
  public TypeList toRopCatchList() {
    int j = size();
    if (j == 0)
      return (TypeList)StdTypeList.EMPTY; 
    StdTypeList stdTypeList = new StdTypeList(j);
    for (int i = 0; i < j; i++)
      stdTypeList.set(i, get(i).getExceptionClass().getClassType()); 
    stdTypeList.setImmutable();
    return (TypeList)stdTypeList;
  }
  
  public IntList toTargetList(int paramInt) {
    if (paramInt >= -1) {
      byte b;
      int i = 0;
      if (paramInt >= 0) {
        b = 1;
      } else {
        b = 0;
      } 
      int j = size();
      if (j == 0)
        return b ? IntList.makeImmutable(paramInt) : IntList.EMPTY; 
      IntList intList = new IntList(j + b);
      while (i < j) {
        intList.add(get(i).getHandlerPc());
        i++;
      } 
      if (b != 0)
        intList.add(paramInt); 
      intList.setImmutable();
      return intList;
    } 
    throw new IllegalArgumentException("noException < -1");
  }
  
  public static class Item {
    private final int endPc;
    
    private final CstType exceptionClass;
    
    private final int handlerPc;
    
    private final int startPc;
    
    public Item(int param1Int1, int param1Int2, int param1Int3, CstType param1CstType) {
      if (param1Int1 >= 0) {
        if (param1Int2 >= param1Int1) {
          if (param1Int3 >= 0) {
            this.startPc = param1Int1;
            this.endPc = param1Int2;
            this.handlerPc = param1Int3;
            this.exceptionClass = param1CstType;
            return;
          } 
          throw new IllegalArgumentException("handlerPc < 0");
        } 
        throw new IllegalArgumentException("endPc < startPc");
      } 
      throw new IllegalArgumentException("startPc < 0");
    }
    
    public boolean covers(int param1Int) {
      return (param1Int >= this.startPc && param1Int < this.endPc);
    }
    
    public int getEndPc() {
      return this.endPc;
    }
    
    public CstType getExceptionClass() {
      CstType cstType = this.exceptionClass;
      return (cstType != null) ? cstType : CstType.OBJECT;
    }
    
    public int getHandlerPc() {
      return this.handlerPc;
    }
    
    public int getStartPc() {
      return this.startPc;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\code\ByteCatchList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
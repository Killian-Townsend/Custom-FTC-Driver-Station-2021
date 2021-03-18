package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.SourcePosition;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.FixedSizeList;

public final class PositionList extends FixedSizeList {
  public static final PositionList EMPTY = new PositionList(0);
  
  public static final int IMPORTANT = 3;
  
  public static final int LINES = 2;
  
  public static final int NONE = 1;
  
  public PositionList(int paramInt) {
    super(paramInt);
  }
  
  public static PositionList make(DalvInsnList paramDalvInsnList, int paramInt) {
    if (paramInt != 1) {
      if (paramInt == 2 || paramInt == 3) {
        SourcePosition sourcePosition2 = SourcePosition.NO_INFO;
        int n = paramDalvInsnList.size();
        Entry[] arrayOfEntry = new Entry[n];
        boolean bool = false;
        SourcePosition sourcePosition1 = sourcePosition2;
        int k = 0;
        int i = k;
        int m = i;
        int j = i;
        while (k < n) {
          int i1;
          SourcePosition sourcePosition;
          DalvInsn dalvInsn = paramDalvInsnList.get(k);
          if (dalvInsn instanceof CodeAddress) {
            i = 1;
            i1 = j;
            sourcePosition = sourcePosition1;
          } else {
            SourcePosition sourcePosition3 = dalvInsn.getPosition();
            i1 = j;
            sourcePosition = sourcePosition1;
            i = m;
            if (!sourcePosition3.equals(sourcePosition2))
              if (sourcePosition3.sameLine(sourcePosition1)) {
                i1 = j;
                sourcePosition = sourcePosition1;
                i = m;
              } else if (paramInt == 3 && m == 0) {
                i1 = j;
                sourcePosition = sourcePosition1;
                i = m;
              } else {
                arrayOfEntry[j] = new Entry(dalvInsn.getAddress(), sourcePosition3);
                i1 = j + 1;
                i = 0;
                sourcePosition = sourcePosition3;
              }  
          } 
          k++;
          j = i1;
          sourcePosition1 = sourcePosition;
          m = i;
        } 
        PositionList positionList = new PositionList(j);
        for (paramInt = bool; paramInt < j; paramInt++)
          positionList.set(paramInt, arrayOfEntry[paramInt]); 
        positionList.setImmutable();
        return positionList;
      } 
      throw new IllegalArgumentException("bogus howMuch");
    } 
    return EMPTY;
  }
  
  public Entry get(int paramInt) {
    return (Entry)get0(paramInt);
  }
  
  public void set(int paramInt, Entry paramEntry) {
    set0(paramInt, paramEntry);
  }
  
  public static class Entry {
    private final int address;
    
    private final SourcePosition position;
    
    public Entry(int param1Int, SourcePosition param1SourcePosition) {
      if (param1Int >= 0) {
        if (param1SourcePosition != null) {
          this.address = param1Int;
          this.position = param1SourcePosition;
          return;
        } 
        throw new NullPointerException("position == null");
      } 
      throw new IllegalArgumentException("address < 0");
    }
    
    public int getAddress() {
      return this.address;
    }
    
    public SourcePosition getPosition() {
      return this.position;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\PositionList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
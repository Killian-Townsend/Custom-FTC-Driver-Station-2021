package org.firstinspires.ftc.robotcore.internal.android.dx.util;

import java.util.NoSuchElementException;

public class ListIntSet implements IntSet {
  final IntList ints;
  
  public ListIntSet() {
    IntList intList = new IntList();
    this.ints = intList;
    intList.sort();
  }
  
  public void add(int paramInt) {
    int i = this.ints.binarysearch(paramInt);
    if (i < 0)
      this.ints.insert(-(i + 1), paramInt); 
  }
  
  public int elements() {
    return this.ints.size();
  }
  
  public boolean has(int paramInt) {
    return (this.ints.indexOf(paramInt) >= 0);
  }
  
  public IntIterator iterator() {
    return new IntIterator() {
        private int idx = 0;
        
        public boolean hasNext() {
          return (this.idx < ListIntSet.this.ints.size());
        }
        
        public int next() {
          if (hasNext()) {
            IntList intList = ListIntSet.this.ints;
            int i = this.idx;
            this.idx = i + 1;
            return intList.get(i);
          } 
          throw new NoSuchElementException();
        }
      };
  }
  
  public void merge(IntSet paramIntSet) {
    boolean bool = paramIntSet instanceof ListIntSet;
    int j = 0;
    int i = 0;
    if (bool) {
      paramIntSet = paramIntSet;
      int n = this.ints.size();
      int m = ((ListIntSet)paramIntSet).ints.size();
      int k = 0;
      label46: while (true) {
        j = i;
        if (i < m) {
          j = i;
          if (k < n) {
            for (j = i; j < m && ((ListIntSet)paramIntSet).ints.get(j) < this.ints.get(k); j++)
              add(((ListIntSet)paramIntSet).ints.get(j)); 
            int i1 = k;
            if (j == m)
              break; 
            while (true) {
              i = j;
              k = i1;
              if (i1 < n) {
                i = j;
                k = i1;
                if (((ListIntSet)paramIntSet).ints.get(j) >= this.ints.get(i1)) {
                  i1++;
                  continue;
                } 
                continue label46;
              } 
              continue label46;
            } 
          } 
        } 
        break;
      } 
      while (j < m) {
        add(((ListIntSet)paramIntSet).ints.get(j));
        j++;
      } 
      this.ints.sort();
      return;
    } 
    if (paramIntSet instanceof BitIntSet) {
      paramIntSet = paramIntSet;
      for (i = j; i >= 0; i = Bits.findFirst(((BitIntSet)paramIntSet).bits, i + 1))
        this.ints.add(i); 
      this.ints.sort();
      return;
    } 
    IntIterator intIterator = paramIntSet.iterator();
    while (intIterator.hasNext())
      add(intIterator.next()); 
  }
  
  public void remove(int paramInt) {
    paramInt = this.ints.indexOf(paramInt);
    if (paramInt >= 0)
      this.ints.removeIndex(paramInt); 
  }
  
  public String toString() {
    return this.ints.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\d\\util\ListIntSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
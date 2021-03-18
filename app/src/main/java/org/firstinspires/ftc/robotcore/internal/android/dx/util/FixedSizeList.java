package org.firstinspires.ftc.robotcore.internal.android.dx.util;

import java.util.Arrays;

public class FixedSizeList extends MutabilityControl implements ToHuman {
  private Object[] arr;
  
  public FixedSizeList(int paramInt) {
    super(bool);
    boolean bool;
    try {
      this.arr = new Object[paramInt];
      return;
    } catch (NegativeArraySizeException negativeArraySizeException) {
      throw new IllegalArgumentException("size < 0");
    } 
  }
  
  private Object throwIndex(int paramInt) {
    if (paramInt < 0)
      throw new IndexOutOfBoundsException("n < 0"); 
    throw new IndexOutOfBoundsException("n >= size()");
  }
  
  private String toString0(String paramString1, String paramString2, String paramString3, boolean paramBoolean) {
    int j = this.arr.length;
    StringBuffer stringBuffer = new StringBuffer(j * 10 + 10);
    if (paramString1 != null)
      stringBuffer.append(paramString1); 
    int i;
    for (i = 0; i < j; i++) {
      if (i != 0 && paramString2 != null)
        stringBuffer.append(paramString2); 
      if (paramBoolean) {
        stringBuffer.append(((ToHuman)this.arr[i]).toHuman());
      } else {
        stringBuffer.append(this.arr[i]);
      } 
    } 
    if (paramString3 != null)
      stringBuffer.append(paramString3); 
    return stringBuffer.toString();
  }
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (paramObject == null || getClass() != paramObject.getClass())
      return false; 
    paramObject = paramObject;
    return Arrays.equals(this.arr, ((FixedSizeList)paramObject).arr);
  }
  
  protected final Object get0(int paramInt) {
    try {
      Object object = this.arr[paramInt];
      if (object != null)
        return object; 
      object = new StringBuilder();
      object.append("unset: ");
      object.append(paramInt);
      throw new NullPointerException(object.toString());
    } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
      return throwIndex(paramInt);
    } 
  }
  
  protected final Object getOrNull0(int paramInt) {
    return this.arr[paramInt];
  }
  
  public int hashCode() {
    return Arrays.hashCode(this.arr);
  }
  
  protected final void set0(int paramInt, Object paramObject) {
    throwIfImmutable();
    try {
      this.arr[paramInt] = paramObject;
      return;
    } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
      throwIndex(paramInt);
      return;
    } 
  }
  
  public void shrinkToFit() {
    int n = this.arr.length;
    int m = 0;
    int j = 0;
    int i;
    for (i = j; j < n; i = i1) {
      int i1 = i;
      if (this.arr[j] != null)
        i1 = i + 1; 
      j++;
    } 
    if (n == i)
      return; 
    throwIfImmutable();
    Object[] arrayOfObject = new Object[i];
    int k = 0;
    j = m;
    while (j < n) {
      Object object = this.arr[j];
      m = k;
      if (object != null) {
        arrayOfObject[k] = object;
        m = k + 1;
      } 
      j++;
      k = m;
    } 
    this.arr = arrayOfObject;
    if (i == 0)
      setImmutable(); 
  }
  
  public final int size() {
    return this.arr.length;
  }
  
  public String toHuman() {
    String str = getClass().getName();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(str.substring(str.lastIndexOf('.') + 1));
    stringBuilder.append('{');
    return toString0(stringBuilder.toString(), ", ", "}", true);
  }
  
  public String toHuman(String paramString1, String paramString2, String paramString3) {
    return toString0(paramString1, paramString2, paramString3, true);
  }
  
  public String toString() {
    String str = getClass().getName();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(str.substring(str.lastIndexOf('.') + 1));
    stringBuilder.append('{');
    return toString0(stringBuilder.toString(), ", ", "}", false);
  }
  
  public String toString(String paramString1, String paramString2, String paramString3) {
    return toString0(paramString1, paramString2, paramString3, false);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\d\\util\FixedSizeList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
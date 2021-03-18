package org.firstinspires.ftc.robotcore.internal.android.dx.rop.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;

public class LocalItem implements Comparable<LocalItem> {
  private final CstString name;
  
  private final CstString signature;
  
  private LocalItem(CstString paramCstString1, CstString paramCstString2) {
    this.name = paramCstString1;
    this.signature = paramCstString2;
  }
  
  private static int compareHandlesNulls(CstString paramCstString1, CstString paramCstString2) {
    return (paramCstString1 == paramCstString2) ? 0 : ((paramCstString1 == null) ? -1 : ((paramCstString2 == null) ? 1 : paramCstString1.compareTo((Constant)paramCstString2)));
  }
  
  public static LocalItem make(CstString paramCstString1, CstString paramCstString2) {
    return (paramCstString1 == null && paramCstString2 == null) ? null : new LocalItem(paramCstString1, paramCstString2);
  }
  
  public int compareTo(LocalItem paramLocalItem) {
    int i = compareHandlesNulls(this.name, paramLocalItem.name);
    return (i != 0) ? i : compareHandlesNulls(this.signature, paramLocalItem.signature);
  }
  
  public boolean equals(Object paramObject) {
    boolean bool1 = paramObject instanceof LocalItem;
    boolean bool = false;
    if (!bool1)
      return false; 
    if (compareTo((LocalItem)paramObject) == 0)
      bool = true; 
    return bool;
  }
  
  public CstString getName() {
    return this.name;
  }
  
  public CstString getSignature() {
    return this.signature;
  }
  
  public int hashCode() {
    int i;
    CstString cstString = this.name;
    int j = 0;
    if (cstString == null) {
      i = 0;
    } else {
      i = cstString.hashCode();
    } 
    cstString = this.signature;
    if (cstString != null)
      j = cstString.hashCode(); 
    return i * 31 + j;
  }
  
  public String toString() {
    String str2;
    String str1;
    CstString cstString2 = this.name;
    if (cstString2 != null && this.signature == null)
      return cstString2.toQuoted(); 
    cstString2 = this.name;
    String str3 = "";
    if (cstString2 == null && this.signature == null)
      return ""; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[");
    cstString2 = this.name;
    if (cstString2 == null) {
      str2 = "";
    } else {
      str2 = str2.toQuoted();
    } 
    stringBuilder.append(str2);
    stringBuilder.append("|");
    CstString cstString1 = this.signature;
    if (cstString1 == null) {
      str1 = str3;
    } else {
      str1 = str1.toQuoted();
    } 
    stringBuilder.append(str1);
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\code\LocalItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
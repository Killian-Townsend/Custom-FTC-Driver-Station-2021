package org.firstinspires.ftc.robotcore.internal.android.dex;

import java.io.Serializable;
import org.firstinspires.ftc.robotcore.internal.android.dex.util.Unsigned;

public final class TypeList implements Comparable<TypeList> {
  public static final TypeList EMPTY = new TypeList(null, Dex.EMPTY_SHORT_ARRAY);
  
  private final Dex dex;
  
  private final short[] types;
  
  public TypeList(Dex paramDex, short[] paramArrayOfshort) {
    this.dex = paramDex;
    this.types = paramArrayOfshort;
  }
  
  public int compareTo(TypeList paramTypeList) {
    int i = 0;
    while (true) {
      short[] arrayOfShort = this.types;
      if (i < arrayOfShort.length) {
        short[] arrayOfShort1 = paramTypeList.types;
        if (i < arrayOfShort1.length) {
          if (arrayOfShort[i] != arrayOfShort1[i])
            return Unsigned.compare(arrayOfShort[i], arrayOfShort1[i]); 
          i++;
          continue;
        } 
      } 
      break;
    } 
    return Unsigned.compare(this.types.length, paramTypeList.types.length);
  }
  
  public short[] getTypes() {
    return this.types;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("(");
    int j = this.types.length;
    for (int i = 0; i < j; i++) {
      Serializable serializable;
      Dex dex = this.dex;
      if (dex != null) {
        serializable = dex.typeNames().get(this.types[i]);
      } else {
        serializable = Short.valueOf(this.types[i]);
      } 
      stringBuilder.append(serializable);
    } 
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dex\TypeList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
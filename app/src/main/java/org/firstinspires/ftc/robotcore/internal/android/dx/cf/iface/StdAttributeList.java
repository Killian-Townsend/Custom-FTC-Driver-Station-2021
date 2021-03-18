package org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface;

import org.firstinspires.ftc.robotcore.internal.android.dx.util.FixedSizeList;

public final class StdAttributeList extends FixedSizeList implements AttributeList {
  public StdAttributeList(int paramInt) {
    super(paramInt);
  }
  
  public int byteLength() {
    int k = size();
    int j = 2;
    for (int i = 0; i < k; i++)
      j += get(i).byteLength(); 
    return j;
  }
  
  public Attribute findFirst(String paramString) {
    int j = size();
    for (int i = 0; i < j; i++) {
      Attribute attribute = get(i);
      if (attribute.getName().equals(paramString))
        return attribute; 
    } 
    return null;
  }
  
  public Attribute findNext(Attribute paramAttribute) {
    int j = size();
    for (int i = 0; i < j; i++) {
      if (get(i) == paramAttribute) {
        String str = paramAttribute.getName();
        while (true) {
          if (++i < j) {
            Attribute attribute = get(i);
            if (attribute.getName().equals(str))
              return attribute; 
            continue;
          } 
          return null;
        } 
      } 
    } 
    return null;
  }
  
  public Attribute get(int paramInt) {
    return (Attribute)get0(paramInt);
  }
  
  public void set(int paramInt, Attribute paramAttribute) {
    set0(paramInt, paramAttribute);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\iface\StdAttributeList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
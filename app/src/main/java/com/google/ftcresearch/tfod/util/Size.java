package com.google.ftcresearch.tfod.util;

import android.graphics.Bitmap;
import android.text.TextUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Size implements Comparable<Size>, Serializable {
  public static final long serialVersionUID = 7689808733290872361L;
  
  public final int height;
  
  public final int width;
  
  public Size(int paramInt1, int paramInt2) {
    this.width = paramInt1;
    this.height = paramInt2;
  }
  
  public Size(Bitmap paramBitmap) {
    this.width = paramBitmap.getWidth();
    this.height = paramBitmap.getHeight();
  }
  
  public static final String dimensionsAsString(int paramInt1, int paramInt2) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramInt1);
    stringBuilder.append("x");
    stringBuilder.append(paramInt2);
    return stringBuilder.toString();
  }
  
  public static Size getRotatedSize(Size paramSize, int paramInt) {
    return (paramInt % 180 != 0) ? new Size(paramSize.height, paramSize.width) : paramSize;
  }
  
  public static Size parseFromString(String paramString) {
    if (TextUtils.isEmpty(paramString))
      return null; 
    String[] arrayOfString = paramString.trim().split("x");
    if (arrayOfString.length == 2)
      try {
        return new Size(Integer.parseInt(arrayOfString[0]), Integer.parseInt(arrayOfString[1]));
      } catch (NumberFormatException numberFormatException) {
        return null;
      }  
    return null;
  }
  
  public static String sizeListToString(List<Size> paramList) {
    String str;
    if (paramList != null && paramList.size() > 0) {
      String str1 = ((Size)paramList.get(0)).toString();
      int i = 1;
      while (true) {
        str = str1;
        if (i < paramList.size()) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(str1);
          stringBuilder.append(",");
          stringBuilder.append(((Size)paramList.get(i)).toString());
          str1 = stringBuilder.toString();
          i++;
          continue;
        } 
        break;
      } 
    } else {
      str = "";
    } 
    return str;
  }
  
  public static List<Size> sizeStringToList(String paramString) {
    ArrayList<Size> arrayList = new ArrayList();
    if (paramString != null) {
      String[] arrayOfString = paramString.split(",");
      int j = arrayOfString.length;
      for (int i = 0; i < j; i++) {
        Size size = parseFromString(arrayOfString[i]);
        if (size != null)
          arrayList.add(size); 
      } 
    } 
    return arrayList;
  }
  
  public final float aspectRatio() {
    return this.width / this.height;
  }
  
  public int compareTo(Size paramSize) {
    return this.width * this.height - paramSize.width * paramSize.height;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool2 = false;
    if (paramObject == null)
      return false; 
    if (!(paramObject instanceof Size))
      return false; 
    paramObject = paramObject;
    boolean bool1 = bool2;
    if (this.width == ((Size)paramObject).width) {
      bool1 = bool2;
      if (this.height == ((Size)paramObject).height)
        bool1 = true; 
    } 
    return bool1;
  }
  
  public int hashCode() {
    return this.width * 32713 + this.height;
  }
  
  public String toString() {
    return dimensionsAsString(this.width, this.height);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\ftcresearch\tfo\\util\Size.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
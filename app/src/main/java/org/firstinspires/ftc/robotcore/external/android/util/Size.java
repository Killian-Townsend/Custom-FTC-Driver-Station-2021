package org.firstinspires.ftc.robotcore.external.android.util;

public final class Size {
  private final int mHeight;
  
  private final int mWidth;
  
  public Size(int paramInt1, int paramInt2) {
    this.mWidth = paramInt1;
    this.mHeight = paramInt2;
  }
  
  private static NumberFormatException invalidSize(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Invalid Size: \"");
    stringBuilder.append(paramString);
    stringBuilder.append("\"");
    throw new NumberFormatException(stringBuilder.toString());
  }
  
  public static Size parseSize(String paramString) throws NumberFormatException {
    if (paramString != null) {
      int j = paramString.indexOf('*');
      int i = j;
      if (j < 0)
        i = paramString.indexOf('x'); 
      if (i >= 0)
        try {
          return new Size(Integer.parseInt(paramString.substring(0, i)), Integer.parseInt(paramString.substring(i + 1)));
        } catch (NumberFormatException numberFormatException) {
          throw invalidSize(paramString);
        }  
      throw invalidSize(paramString);
    } 
    throw new IllegalArgumentException("string must not be null");
  }
  
  public boolean equals(Object paramObject) {
    boolean bool2 = false;
    if (paramObject == null)
      return false; 
    if (this == paramObject)
      return true; 
    boolean bool1 = bool2;
    if (paramObject instanceof Size) {
      paramObject = paramObject;
      bool1 = bool2;
      if (this.mWidth == ((Size)paramObject).mWidth) {
        bool1 = bool2;
        if (this.mHeight == ((Size)paramObject).mHeight)
          bool1 = true; 
      } 
    } 
    return bool1;
  }
  
  public int getHeight() {
    return this.mHeight;
  }
  
  public int getWidth() {
    return this.mWidth;
  }
  
  public int hashCode() {
    int i = this.mHeight;
    int j = this.mWidth;
    return i ^ (j >>> 16 | j << 16);
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.mWidth);
    stringBuilder.append("x");
    stringBuilder.append(this.mHeight);
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\androi\\util\Size.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
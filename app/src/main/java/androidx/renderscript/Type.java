package androidx.renderscript;

public class Type extends BaseObj {
  boolean mDimFaces;
  
  boolean mDimMipmaps;
  
  int mDimX;
  
  int mDimY;
  
  int mDimYuv;
  
  int mDimZ;
  
  Element mElement;
  
  int mElementCount;
  
  Type(long paramLong, RenderScript paramRenderScript) {
    super(paramLong, paramRenderScript);
  }
  
  public static Type createX(RenderScript paramRenderScript, Element paramElement, int paramInt) {
    if (paramInt >= 1) {
      Type type = new Type(paramRenderScript.nTypeCreate(paramElement.getID(paramRenderScript), paramInt, 0, 0, false, false, 0), paramRenderScript);
      type.mElement = paramElement;
      type.mDimX = paramInt;
      type.calcElementCount();
      return type;
    } 
    throw new RSInvalidStateException("Dimension must be >= 1.");
  }
  
  public static Type createXY(RenderScript paramRenderScript, Element paramElement, int paramInt1, int paramInt2) {
    if (paramInt1 >= 1 && paramInt2 >= 1) {
      Type type = new Type(paramRenderScript.nTypeCreate(paramElement.getID(paramRenderScript), paramInt1, paramInt2, 0, false, false, 0), paramRenderScript);
      type.mElement = paramElement;
      type.mDimX = paramInt1;
      type.mDimY = paramInt2;
      type.calcElementCount();
      return type;
    } 
    throw new RSInvalidStateException("Dimension must be >= 1.");
  }
  
  public static Type createXYZ(RenderScript paramRenderScript, Element paramElement, int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt1 >= 1 && paramInt2 >= 1 && paramInt3 >= 1) {
      Type type = new Type(paramRenderScript.nTypeCreate(paramElement.getID(paramRenderScript), paramInt1, paramInt2, paramInt3, false, false, 0), paramRenderScript);
      type.mElement = paramElement;
      type.mDimX = paramInt1;
      type.mDimY = paramInt2;
      type.mDimZ = paramInt3;
      type.calcElementCount();
      return type;
    } 
    throw new RSInvalidStateException("Dimension must be >= 1.");
  }
  
  void calcElementCount() {
    byte b;
    boolean bool = hasMipmaps();
    int j = getX();
    int k = getY();
    int m = getZ();
    if (hasFaces()) {
      b = 6;
    } else {
      b = 1;
    } 
    int i = j;
    if (j == 0)
      i = 1; 
    j = k;
    if (k == 0)
      j = 1; 
    k = m;
    if (m == 0)
      k = 1; 
    m = i * j * k * b;
    int n = i;
    while (bool && (n > 1 || j > 1 || k > 1)) {
      i = n;
      if (n > 1)
        i = n >> 1; 
      int i1 = j;
      if (j > 1)
        i1 = j >> 1; 
      int i2 = k;
      if (k > 1)
        i2 = k >> 1; 
      m += i * i1 * i2 * b;
      n = i;
      j = i1;
      k = i2;
    } 
    this.mElementCount = m;
  }
  
  public int getCount() {
    return this.mElementCount;
  }
  
  public long getDummyType(RenderScript paramRenderScript, long paramLong) {
    return paramRenderScript.nIncTypeCreate(paramLong, this.mDimX, this.mDimY, this.mDimZ, this.mDimMipmaps, this.mDimFaces, this.mDimYuv);
  }
  
  public Element getElement() {
    return this.mElement;
  }
  
  public int getX() {
    return this.mDimX;
  }
  
  public int getY() {
    return this.mDimY;
  }
  
  public int getYuv() {
    return this.mDimYuv;
  }
  
  public int getZ() {
    return this.mDimZ;
  }
  
  public boolean hasFaces() {
    return this.mDimFaces;
  }
  
  public boolean hasMipmaps() {
    return this.mDimMipmaps;
  }
  
  public static class Builder {
    boolean mDimFaces;
    
    boolean mDimMipmaps;
    
    int mDimX = 1;
    
    int mDimY;
    
    int mDimZ;
    
    Element mElement;
    
    RenderScript mRS;
    
    int mYuv;
    
    public Builder(RenderScript param1RenderScript, Element param1Element) {
      param1Element.checkValid();
      this.mRS = param1RenderScript;
      this.mElement = param1Element;
    }
    
    public Type create() {
      if (this.mDimZ > 0)
        if (this.mDimX >= 1 && this.mDimY >= 1) {
          if (this.mDimFaces)
            throw new RSInvalidStateException("Cube maps not supported with 3D types."); 
        } else {
          throw new RSInvalidStateException("Both X and Y dimension required when Z is present.");
        }  
      if (this.mDimY <= 0 || this.mDimX >= 1) {
        if (!this.mDimFaces || this.mDimY >= 1) {
          if (this.mYuv == 0 || (this.mDimZ == 0 && !this.mDimFaces && !this.mDimMipmaps)) {
            RenderScript renderScript = this.mRS;
            Type type = new Type(renderScript.nTypeCreate(this.mElement.getID(renderScript), this.mDimX, this.mDimY, this.mDimZ, this.mDimMipmaps, this.mDimFaces, this.mYuv), this.mRS);
            type.mElement = this.mElement;
            type.mDimX = this.mDimX;
            type.mDimY = this.mDimY;
            type.mDimZ = this.mDimZ;
            type.mDimMipmaps = this.mDimMipmaps;
            type.mDimFaces = this.mDimFaces;
            type.mDimYuv = this.mYuv;
            type.calcElementCount();
            return type;
          } 
          throw new RSInvalidStateException("YUV only supports basic 2D.");
        } 
        throw new RSInvalidStateException("Cube maps require 2D Types.");
      } 
      throw new RSInvalidStateException("X dimension required when Y is present.");
    }
    
    public Builder setFaces(boolean param1Boolean) {
      this.mDimFaces = param1Boolean;
      return this;
    }
    
    public Builder setMipmaps(boolean param1Boolean) {
      this.mDimMipmaps = param1Boolean;
      return this;
    }
    
    public Builder setX(int param1Int) {
      if (param1Int >= 1) {
        this.mDimX = param1Int;
        return this;
      } 
      throw new RSIllegalArgumentException("Values of less than 1 for Dimension X are not valid.");
    }
    
    public Builder setY(int param1Int) {
      if (param1Int >= 1) {
        this.mDimY = param1Int;
        return this;
      } 
      throw new RSIllegalArgumentException("Values of less than 1 for Dimension Y are not valid.");
    }
    
    public Builder setYuvFormat(int param1Int) {
      if (param1Int == 17 || param1Int == 842094169) {
        this.mYuv = param1Int;
        return this;
      } 
      throw new RSIllegalArgumentException("Only NV21 and YV12 are supported..");
    }
    
    public Builder setZ(int param1Int) {
      if (param1Int >= 1) {
        this.mDimZ = param1Int;
        return this;
      } 
      throw new RSIllegalArgumentException("Values of less than 1 for Dimension Z are not valid.");
    }
  }
  
  public enum CubemapFace {
    NEGATIVE_X,
    NEGATIVE_Y,
    NEGATIVE_Z,
    POSITIVE_X(0),
    POSITIVE_Y(0),
    POSITIVE_Z(0);
    
    int mID;
    
    static {
      NEGATIVE_Y = new CubemapFace("NEGATIVE_Y", 3, 3);
      POSITIVE_Z = new CubemapFace("POSITIVE_Z", 4, 4);
      CubemapFace cubemapFace = new CubemapFace("NEGATIVE_Z", 5, 5);
      NEGATIVE_Z = cubemapFace;
      $VALUES = new CubemapFace[] { POSITIVE_X, NEGATIVE_X, POSITIVE_Y, NEGATIVE_Y, POSITIVE_Z, cubemapFace };
    }
    
    CubemapFace(int param1Int1) {
      this.mID = param1Int1;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\renderscript\Type.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package androidx.renderscript;

public class Element extends BaseObj {
  int[] mArraySizes;
  
  String[] mElementNames;
  
  Element[] mElements;
  
  DataKind mKind;
  
  boolean mNormalized;
  
  int[] mOffsetInBytes;
  
  int mSize;
  
  DataType mType;
  
  int mVectorSize;
  
  int[] mVisibleElementMap;
  
  Element(long paramLong, RenderScript paramRenderScript) {
    super(paramLong, paramRenderScript);
  }
  
  Element(long paramLong, RenderScript paramRenderScript, DataType paramDataType, DataKind paramDataKind, boolean paramBoolean, int paramInt) {
    super(paramLong, paramRenderScript);
    if (paramDataType != DataType.UNSIGNED_5_6_5 && paramDataType != DataType.UNSIGNED_4_4_4_4 && paramDataType != DataType.UNSIGNED_5_5_5_1) {
      if (paramInt == 3) {
        this.mSize = paramDataType.mSize * 4;
      } else {
        this.mSize = paramDataType.mSize * paramInt;
      } 
    } else {
      this.mSize = paramDataType.mSize;
    } 
    this.mType = paramDataType;
    this.mKind = paramDataKind;
    this.mNormalized = paramBoolean;
    this.mVectorSize = paramInt;
  }
  
  Element(long paramLong, RenderScript paramRenderScript, Element[] paramArrayOfElement, String[] paramArrayOfString, int[] paramArrayOfint) {
    super(paramLong, paramRenderScript);
    int i = 0;
    this.mSize = 0;
    this.mVectorSize = 1;
    this.mElements = paramArrayOfElement;
    this.mElementNames = paramArrayOfString;
    this.mArraySizes = paramArrayOfint;
    this.mType = DataType.NONE;
    this.mKind = DataKind.USER;
    this.mOffsetInBytes = new int[this.mElements.length];
    while (true) {
      Element[] arrayOfElement = this.mElements;
      if (i < arrayOfElement.length) {
        int[] arrayOfInt = this.mOffsetInBytes;
        int j = this.mSize;
        arrayOfInt[i] = j;
        this.mSize = j + (arrayOfElement[i]).mSize * this.mArraySizes[i];
        i++;
        continue;
      } 
      updateVisibleSubElements();
      return;
    } 
  }
  
  public static Element ALLOCATION(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_ALLOCATION == null)
      paramRenderScript.mElement_ALLOCATION = createUser(paramRenderScript, DataType.RS_ALLOCATION); 
    return paramRenderScript.mElement_ALLOCATION;
  }
  
  public static Element A_8(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_A_8 == null)
      paramRenderScript.mElement_A_8 = createPixel(paramRenderScript, DataType.UNSIGNED_8, DataKind.PIXEL_A); 
    return paramRenderScript.mElement_A_8;
  }
  
  public static Element BOOLEAN(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_BOOLEAN == null)
      paramRenderScript.mElement_BOOLEAN = createUser(paramRenderScript, DataType.BOOLEAN); 
    return paramRenderScript.mElement_BOOLEAN;
  }
  
  public static Element ELEMENT(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_ELEMENT == null)
      paramRenderScript.mElement_ELEMENT = createUser(paramRenderScript, DataType.RS_ELEMENT); 
    return paramRenderScript.mElement_ELEMENT;
  }
  
  public static Element F32(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_F32 == null)
      paramRenderScript.mElement_F32 = createUser(paramRenderScript, DataType.FLOAT_32); 
    return paramRenderScript.mElement_F32;
  }
  
  public static Element F32_2(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_FLOAT_2 == null)
      paramRenderScript.mElement_FLOAT_2 = createVector(paramRenderScript, DataType.FLOAT_32, 2); 
    return paramRenderScript.mElement_FLOAT_2;
  }
  
  public static Element F32_3(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_FLOAT_3 == null)
      paramRenderScript.mElement_FLOAT_3 = createVector(paramRenderScript, DataType.FLOAT_32, 3); 
    return paramRenderScript.mElement_FLOAT_3;
  }
  
  public static Element F32_4(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_FLOAT_4 == null)
      paramRenderScript.mElement_FLOAT_4 = createVector(paramRenderScript, DataType.FLOAT_32, 4); 
    return paramRenderScript.mElement_FLOAT_4;
  }
  
  public static Element F64(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_F64 == null)
      paramRenderScript.mElement_F64 = createUser(paramRenderScript, DataType.FLOAT_64); 
    return paramRenderScript.mElement_F64;
  }
  
  public static Element F64_2(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_DOUBLE_2 == null)
      paramRenderScript.mElement_DOUBLE_2 = createVector(paramRenderScript, DataType.FLOAT_64, 2); 
    return paramRenderScript.mElement_DOUBLE_2;
  }
  
  public static Element F64_3(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_DOUBLE_3 == null)
      paramRenderScript.mElement_DOUBLE_3 = createVector(paramRenderScript, DataType.FLOAT_64, 3); 
    return paramRenderScript.mElement_DOUBLE_3;
  }
  
  public static Element F64_4(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_DOUBLE_4 == null)
      paramRenderScript.mElement_DOUBLE_4 = createVector(paramRenderScript, DataType.FLOAT_64, 4); 
    return paramRenderScript.mElement_DOUBLE_4;
  }
  
  public static Element I16(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_I16 == null)
      paramRenderScript.mElement_I16 = createUser(paramRenderScript, DataType.SIGNED_16); 
    return paramRenderScript.mElement_I16;
  }
  
  public static Element I16_2(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_SHORT_2 == null)
      paramRenderScript.mElement_SHORT_2 = createVector(paramRenderScript, DataType.SIGNED_16, 2); 
    return paramRenderScript.mElement_SHORT_2;
  }
  
  public static Element I16_3(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_SHORT_3 == null)
      paramRenderScript.mElement_SHORT_3 = createVector(paramRenderScript, DataType.SIGNED_16, 3); 
    return paramRenderScript.mElement_SHORT_3;
  }
  
  public static Element I16_4(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_SHORT_4 == null)
      paramRenderScript.mElement_SHORT_4 = createVector(paramRenderScript, DataType.SIGNED_16, 4); 
    return paramRenderScript.mElement_SHORT_4;
  }
  
  public static Element I32(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_I32 == null)
      paramRenderScript.mElement_I32 = createUser(paramRenderScript, DataType.SIGNED_32); 
    return paramRenderScript.mElement_I32;
  }
  
  public static Element I32_2(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_INT_2 == null)
      paramRenderScript.mElement_INT_2 = createVector(paramRenderScript, DataType.SIGNED_32, 2); 
    return paramRenderScript.mElement_INT_2;
  }
  
  public static Element I32_3(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_INT_3 == null)
      paramRenderScript.mElement_INT_3 = createVector(paramRenderScript, DataType.SIGNED_32, 3); 
    return paramRenderScript.mElement_INT_3;
  }
  
  public static Element I32_4(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_INT_4 == null)
      paramRenderScript.mElement_INT_4 = createVector(paramRenderScript, DataType.SIGNED_32, 4); 
    return paramRenderScript.mElement_INT_4;
  }
  
  public static Element I64(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_I64 == null)
      paramRenderScript.mElement_I64 = createUser(paramRenderScript, DataType.SIGNED_64); 
    return paramRenderScript.mElement_I64;
  }
  
  public static Element I64_2(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_LONG_2 == null)
      paramRenderScript.mElement_LONG_2 = createVector(paramRenderScript, DataType.SIGNED_64, 2); 
    return paramRenderScript.mElement_LONG_2;
  }
  
  public static Element I64_3(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_LONG_3 == null)
      paramRenderScript.mElement_LONG_3 = createVector(paramRenderScript, DataType.SIGNED_64, 3); 
    return paramRenderScript.mElement_LONG_3;
  }
  
  public static Element I64_4(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_LONG_4 == null)
      paramRenderScript.mElement_LONG_4 = createVector(paramRenderScript, DataType.SIGNED_64, 4); 
    return paramRenderScript.mElement_LONG_4;
  }
  
  public static Element I8(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_I8 == null)
      paramRenderScript.mElement_I8 = createUser(paramRenderScript, DataType.SIGNED_8); 
    return paramRenderScript.mElement_I8;
  }
  
  public static Element I8_2(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_CHAR_2 == null)
      paramRenderScript.mElement_CHAR_2 = createVector(paramRenderScript, DataType.SIGNED_8, 2); 
    return paramRenderScript.mElement_CHAR_2;
  }
  
  public static Element I8_3(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_CHAR_3 == null)
      paramRenderScript.mElement_CHAR_3 = createVector(paramRenderScript, DataType.SIGNED_8, 3); 
    return paramRenderScript.mElement_CHAR_3;
  }
  
  public static Element I8_4(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_CHAR_4 == null)
      paramRenderScript.mElement_CHAR_4 = createVector(paramRenderScript, DataType.SIGNED_8, 4); 
    return paramRenderScript.mElement_CHAR_4;
  }
  
  public static Element MATRIX_2X2(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_MATRIX_2X2 == null)
      paramRenderScript.mElement_MATRIX_2X2 = createUser(paramRenderScript, DataType.MATRIX_2X2); 
    return paramRenderScript.mElement_MATRIX_2X2;
  }
  
  public static Element MATRIX_3X3(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_MATRIX_3X3 == null)
      paramRenderScript.mElement_MATRIX_3X3 = createUser(paramRenderScript, DataType.MATRIX_3X3); 
    return paramRenderScript.mElement_MATRIX_3X3;
  }
  
  public static Element MATRIX_4X4(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_MATRIX_4X4 == null)
      paramRenderScript.mElement_MATRIX_4X4 = createUser(paramRenderScript, DataType.MATRIX_4X4); 
    return paramRenderScript.mElement_MATRIX_4X4;
  }
  
  public static Element RGBA_4444(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_RGBA_4444 == null)
      paramRenderScript.mElement_RGBA_4444 = createPixel(paramRenderScript, DataType.UNSIGNED_4_4_4_4, DataKind.PIXEL_RGBA); 
    return paramRenderScript.mElement_RGBA_4444;
  }
  
  public static Element RGBA_5551(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_RGBA_5551 == null)
      paramRenderScript.mElement_RGBA_5551 = createPixel(paramRenderScript, DataType.UNSIGNED_5_5_5_1, DataKind.PIXEL_RGBA); 
    return paramRenderScript.mElement_RGBA_5551;
  }
  
  public static Element RGBA_8888(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_RGBA_8888 == null)
      paramRenderScript.mElement_RGBA_8888 = createPixel(paramRenderScript, DataType.UNSIGNED_8, DataKind.PIXEL_RGBA); 
    return paramRenderScript.mElement_RGBA_8888;
  }
  
  public static Element RGB_565(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_RGB_565 == null)
      paramRenderScript.mElement_RGB_565 = createPixel(paramRenderScript, DataType.UNSIGNED_5_6_5, DataKind.PIXEL_RGB); 
    return paramRenderScript.mElement_RGB_565;
  }
  
  public static Element RGB_888(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_RGB_888 == null)
      paramRenderScript.mElement_RGB_888 = createPixel(paramRenderScript, DataType.UNSIGNED_8, DataKind.PIXEL_RGB); 
    return paramRenderScript.mElement_RGB_888;
  }
  
  public static Element SAMPLER(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_SAMPLER == null)
      paramRenderScript.mElement_SAMPLER = createUser(paramRenderScript, DataType.RS_SAMPLER); 
    return paramRenderScript.mElement_SAMPLER;
  }
  
  public static Element SCRIPT(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_SCRIPT == null)
      paramRenderScript.mElement_SCRIPT = createUser(paramRenderScript, DataType.RS_SCRIPT); 
    return paramRenderScript.mElement_SCRIPT;
  }
  
  public static Element TYPE(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_TYPE == null)
      paramRenderScript.mElement_TYPE = createUser(paramRenderScript, DataType.RS_TYPE); 
    return paramRenderScript.mElement_TYPE;
  }
  
  public static Element U16(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_U16 == null)
      paramRenderScript.mElement_U16 = createUser(paramRenderScript, DataType.UNSIGNED_16); 
    return paramRenderScript.mElement_U16;
  }
  
  public static Element U16_2(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_USHORT_2 == null)
      paramRenderScript.mElement_USHORT_2 = createVector(paramRenderScript, DataType.UNSIGNED_16, 2); 
    return paramRenderScript.mElement_USHORT_2;
  }
  
  public static Element U16_3(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_USHORT_3 == null)
      paramRenderScript.mElement_USHORT_3 = createVector(paramRenderScript, DataType.UNSIGNED_16, 3); 
    return paramRenderScript.mElement_USHORT_3;
  }
  
  public static Element U16_4(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_USHORT_4 == null)
      paramRenderScript.mElement_USHORT_4 = createVector(paramRenderScript, DataType.UNSIGNED_16, 4); 
    return paramRenderScript.mElement_USHORT_4;
  }
  
  public static Element U32(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_U32 == null)
      paramRenderScript.mElement_U32 = createUser(paramRenderScript, DataType.UNSIGNED_32); 
    return paramRenderScript.mElement_U32;
  }
  
  public static Element U32_2(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_UINT_2 == null)
      paramRenderScript.mElement_UINT_2 = createVector(paramRenderScript, DataType.UNSIGNED_32, 2); 
    return paramRenderScript.mElement_UINT_2;
  }
  
  public static Element U32_3(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_UINT_3 == null)
      paramRenderScript.mElement_UINT_3 = createVector(paramRenderScript, DataType.UNSIGNED_32, 3); 
    return paramRenderScript.mElement_UINT_3;
  }
  
  public static Element U32_4(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_UINT_4 == null)
      paramRenderScript.mElement_UINT_4 = createVector(paramRenderScript, DataType.UNSIGNED_32, 4); 
    return paramRenderScript.mElement_UINT_4;
  }
  
  public static Element U64(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_U64 == null)
      paramRenderScript.mElement_U64 = createUser(paramRenderScript, DataType.UNSIGNED_64); 
    return paramRenderScript.mElement_U64;
  }
  
  public static Element U64_2(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_ULONG_2 == null)
      paramRenderScript.mElement_ULONG_2 = createVector(paramRenderScript, DataType.UNSIGNED_64, 2); 
    return paramRenderScript.mElement_ULONG_2;
  }
  
  public static Element U64_3(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_ULONG_3 == null)
      paramRenderScript.mElement_ULONG_3 = createVector(paramRenderScript, DataType.UNSIGNED_64, 3); 
    return paramRenderScript.mElement_ULONG_3;
  }
  
  public static Element U64_4(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_ULONG_4 == null)
      paramRenderScript.mElement_ULONG_4 = createVector(paramRenderScript, DataType.UNSIGNED_64, 4); 
    return paramRenderScript.mElement_ULONG_4;
  }
  
  public static Element U8(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_U8 == null)
      paramRenderScript.mElement_U8 = createUser(paramRenderScript, DataType.UNSIGNED_8); 
    return paramRenderScript.mElement_U8;
  }
  
  public static Element U8_2(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_UCHAR_2 == null)
      paramRenderScript.mElement_UCHAR_2 = createVector(paramRenderScript, DataType.UNSIGNED_8, 2); 
    return paramRenderScript.mElement_UCHAR_2;
  }
  
  public static Element U8_3(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_UCHAR_3 == null)
      paramRenderScript.mElement_UCHAR_3 = createVector(paramRenderScript, DataType.UNSIGNED_8, 3); 
    return paramRenderScript.mElement_UCHAR_3;
  }
  
  public static Element U8_4(RenderScript paramRenderScript) {
    if (paramRenderScript.mElement_UCHAR_4 == null)
      paramRenderScript.mElement_UCHAR_4 = createVector(paramRenderScript, DataType.UNSIGNED_8, 4); 
    return paramRenderScript.mElement_UCHAR_4;
  }
  
  public static Element createPixel(RenderScript paramRenderScript, DataType paramDataType, DataKind paramDataKind) {
    if (paramDataKind == DataKind.PIXEL_L || paramDataKind == DataKind.PIXEL_A || paramDataKind == DataKind.PIXEL_LA || paramDataKind == DataKind.PIXEL_RGB || paramDataKind == DataKind.PIXEL_RGBA || paramDataKind == DataKind.PIXEL_DEPTH || paramDataKind == DataKind.PIXEL_YUV) {
      if (paramDataType == DataType.UNSIGNED_8 || paramDataType == DataType.UNSIGNED_16 || paramDataType == DataType.UNSIGNED_5_6_5 || paramDataType == DataType.UNSIGNED_4_4_4_4 || paramDataType == DataType.UNSIGNED_5_5_5_1) {
        if (paramDataType != DataType.UNSIGNED_5_6_5 || paramDataKind == DataKind.PIXEL_RGB) {
          if (paramDataType != DataType.UNSIGNED_5_5_5_1 || paramDataKind == DataKind.PIXEL_RGBA) {
            if (paramDataType != DataType.UNSIGNED_4_4_4_4 || paramDataKind == DataKind.PIXEL_RGBA) {
              if (paramDataType != DataType.UNSIGNED_16 || paramDataKind == DataKind.PIXEL_DEPTH) {
                int i = null.$SwitchMap$androidx$renderscript$Element$DataKind[paramDataKind.ordinal()];
                byte b = 3;
                if (i != 1) {
                  if (i != 2)
                    if (i != 3) {
                      b = 1;
                    } else {
                      b = 4;
                    }  
                } else {
                  b = 2;
                } 
                return new Element(paramRenderScript.nElementCreate(paramDataType.mID, paramDataKind.mID, true, b), paramRenderScript, paramDataType, paramDataKind, true, b);
              } 
              throw new RSIllegalArgumentException("Bad kind and type combo");
            } 
            throw new RSIllegalArgumentException("Bad kind and type combo");
          } 
          throw new RSIllegalArgumentException("Bad kind and type combo");
        } 
        throw new RSIllegalArgumentException("Bad kind and type combo");
      } 
      throw new RSIllegalArgumentException("Unsupported DataType");
    } 
    throw new RSIllegalArgumentException("Unsupported DataKind");
  }
  
  static Element createUser(RenderScript paramRenderScript, DataType paramDataType) {
    DataKind dataKind = DataKind.USER;
    return new Element(paramRenderScript.nElementCreate(paramDataType.mID, dataKind.mID, false, 1), paramRenderScript, paramDataType, dataKind, false, 1);
  }
  
  public static Element createVector(RenderScript paramRenderScript, DataType paramDataType, int paramInt) {
    if (paramInt >= 2 && paramInt <= 4) {
      switch (paramDataType) {
        default:
          throw new RSIllegalArgumentException("Cannot create vector of non-primitive type.");
        case null:
        case null:
        case null:
        case null:
        case null:
        case null:
        case null:
        case null:
        case null:
        case null:
        case null:
          break;
      } 
      DataKind dataKind = DataKind.USER;
      return new Element(paramRenderScript.nElementCreate(paramDataType.mID, dataKind.mID, false, paramInt), paramRenderScript, paramDataType, dataKind, false, paramInt);
    } 
    throw new RSIllegalArgumentException("Vector size out of range 2-4.");
  }
  
  private void updateVisibleSubElements() {
    if (this.mElements == null)
      return; 
    int k = this.mElementNames.length;
    int i = 0;
    int j;
    for (j = i; i < k; j = m) {
      int m = j;
      if (this.mElementNames[i].charAt(0) != '#')
        m = j + 1; 
      i++;
    } 
    this.mVisibleElementMap = new int[j];
    i = 0;
    for (j = i; i < k; j = m) {
      int m = j;
      if (this.mElementNames[i].charAt(0) != '#') {
        this.mVisibleElementMap[j] = i;
        m = j + 1;
      } 
      i++;
    } 
  }
  
  public int getBytesSize() {
    return this.mSize;
  }
  
  public DataKind getDataKind() {
    return this.mKind;
  }
  
  public DataType getDataType() {
    return this.mType;
  }
  
  public long getDummyElement(RenderScript paramRenderScript) {
    return paramRenderScript.nIncElementCreate(this.mType.mID, this.mKind.mID, this.mNormalized, this.mVectorSize);
  }
  
  public Element getSubElement(int paramInt) {
    int[] arrayOfInt = this.mVisibleElementMap;
    if (arrayOfInt != null) {
      if (paramInt >= 0 && paramInt < arrayOfInt.length)
        return this.mElements[arrayOfInt[paramInt]]; 
      throw new RSIllegalArgumentException("Illegal sub-element index");
    } 
    throw new RSIllegalArgumentException("Element contains no sub-elements");
  }
  
  public int getSubElementArraySize(int paramInt) {
    int[] arrayOfInt = this.mVisibleElementMap;
    if (arrayOfInt != null) {
      if (paramInt >= 0 && paramInt < arrayOfInt.length)
        return this.mArraySizes[arrayOfInt[paramInt]]; 
      throw new RSIllegalArgumentException("Illegal sub-element index");
    } 
    throw new RSIllegalArgumentException("Element contains no sub-elements");
  }
  
  public int getSubElementCount() {
    int[] arrayOfInt = this.mVisibleElementMap;
    return (arrayOfInt == null) ? 0 : arrayOfInt.length;
  }
  
  public String getSubElementName(int paramInt) {
    int[] arrayOfInt = this.mVisibleElementMap;
    if (arrayOfInt != null) {
      if (paramInt >= 0 && paramInt < arrayOfInt.length)
        return this.mElementNames[arrayOfInt[paramInt]]; 
      throw new RSIllegalArgumentException("Illegal sub-element index");
    } 
    throw new RSIllegalArgumentException("Element contains no sub-elements");
  }
  
  public int getSubElementOffsetBytes(int paramInt) {
    int[] arrayOfInt = this.mVisibleElementMap;
    if (arrayOfInt != null) {
      if (paramInt >= 0 && paramInt < arrayOfInt.length)
        return this.mOffsetInBytes[arrayOfInt[paramInt]]; 
      throw new RSIllegalArgumentException("Illegal sub-element index");
    } 
    throw new RSIllegalArgumentException("Element contains no sub-elements");
  }
  
  public int getVectorSize() {
    return this.mVectorSize;
  }
  
  public boolean isCompatible(Element paramElement) {
    return equals(paramElement) ? true : ((this.mSize == paramElement.mSize && this.mType != DataType.NONE && this.mType == paramElement.mType && this.mVectorSize == paramElement.mVectorSize));
  }
  
  public boolean isComplex() {
    if (this.mElements == null)
      return false; 
    int i = 0;
    while (true) {
      Element[] arrayOfElement = this.mElements;
      if (i < arrayOfElement.length) {
        if ((arrayOfElement[i]).mElements != null)
          return true; 
        i++;
        continue;
      } 
      return false;
    } 
  }
  
  public static class Builder {
    int[] mArraySizes;
    
    int mCount;
    
    String[] mElementNames;
    
    Element[] mElements;
    
    RenderScript mRS;
    
    int mSkipPadding;
    
    public Builder(RenderScript param1RenderScript) {
      this.mRS = param1RenderScript;
      this.mCount = 0;
      this.mElements = new Element[8];
      this.mElementNames = new String[8];
      this.mArraySizes = new int[8];
    }
    
    public Builder add(Element param1Element, String param1String) {
      return add(param1Element, param1String, 1);
    }
    
    public Builder add(Element param1Element, String param1String, int param1Int) {
      if (param1Int >= 1) {
        if (this.mSkipPadding != 0 && param1String.startsWith("#padding_")) {
          this.mSkipPadding = 0;
          return this;
        } 
        if (param1Element.mVectorSize == 3) {
          this.mSkipPadding = 1;
        } else {
          this.mSkipPadding = 0;
        } 
        int i = this.mCount;
        Element[] arrayOfElement = this.mElements;
        if (i == arrayOfElement.length) {
          Element[] arrayOfElement1 = new Element[i + 8];
          String[] arrayOfString = new String[i + 8];
          int[] arrayOfInt = new int[i + 8];
          System.arraycopy(arrayOfElement, 0, arrayOfElement1, 0, i);
          System.arraycopy(this.mElementNames, 0, arrayOfString, 0, this.mCount);
          System.arraycopy(this.mArraySizes, 0, arrayOfInt, 0, this.mCount);
          this.mElements = arrayOfElement1;
          this.mElementNames = arrayOfString;
          this.mArraySizes = arrayOfInt;
        } 
        arrayOfElement = this.mElements;
        i = this.mCount;
        arrayOfElement[i] = param1Element;
        this.mElementNames[i] = param1String;
        this.mArraySizes[i] = param1Int;
        this.mCount = i + 1;
        return this;
      } 
      throw new RSIllegalArgumentException("Array size cannot be less than 1.");
    }
    
    public Element create() {
      this.mRS.validate();
      int j = this.mCount;
      Element[] arrayOfElement1 = new Element[j];
      String[] arrayOfString = new String[j];
      int[] arrayOfInt = new int[j];
      Element[] arrayOfElement2 = this.mElements;
      int i = 0;
      System.arraycopy(arrayOfElement2, 0, arrayOfElement1, 0, j);
      System.arraycopy(this.mElementNames, 0, arrayOfString, 0, this.mCount);
      System.arraycopy(this.mArraySizes, 0, arrayOfInt, 0, this.mCount);
      long[] arrayOfLong = new long[j];
      while (i < j) {
        arrayOfLong[i] = arrayOfElement1[i].getID(this.mRS);
        i++;
      } 
      return new Element(this.mRS.nElementCreate2(arrayOfLong, arrayOfString, arrayOfInt), this.mRS, arrayOfElement1, arrayOfString, arrayOfInt);
    }
  }
  
  public enum DataKind {
    PIXEL_A,
    PIXEL_DEPTH,
    PIXEL_L,
    PIXEL_LA,
    PIXEL_RGB,
    PIXEL_RGBA,
    PIXEL_YUV,
    USER(0);
    
    int mID;
    
    static {
      PIXEL_A = new DataKind("PIXEL_A", 2, 8);
      PIXEL_LA = new DataKind("PIXEL_LA", 3, 9);
      PIXEL_RGB = new DataKind("PIXEL_RGB", 4, 10);
      PIXEL_RGBA = new DataKind("PIXEL_RGBA", 5, 11);
      PIXEL_DEPTH = new DataKind("PIXEL_DEPTH", 6, 12);
      DataKind dataKind = new DataKind("PIXEL_YUV", 7, 13);
      PIXEL_YUV = dataKind;
      $VALUES = new DataKind[] { USER, PIXEL_L, PIXEL_A, PIXEL_LA, PIXEL_RGB, PIXEL_RGBA, PIXEL_DEPTH, dataKind };
    }
    
    DataKind(int param1Int1) {
      this.mID = param1Int1;
    }
  }
  
  public enum DataType {
    BOOLEAN(0),
    FLOAT_32(0),
    FLOAT_64(0),
    MATRIX_2X2(0),
    MATRIX_3X3(0),
    MATRIX_4X4(0),
    NONE(0, 0),
    RS_ALLOCATION(0, 0),
    RS_ELEMENT(0, 0),
    RS_SAMPLER(0, 0),
    RS_SCRIPT(0, 0),
    RS_TYPE(0, 0),
    SIGNED_16(0, 0),
    SIGNED_32(0, 0),
    SIGNED_64(0, 0),
    SIGNED_8(0, 0),
    UNSIGNED_16(0, 0),
    UNSIGNED_32(0, 0),
    UNSIGNED_4_4_4_4(0, 0),
    UNSIGNED_5_5_5_1(0, 0),
    UNSIGNED_5_6_5(0, 0),
    UNSIGNED_64(0, 0),
    UNSIGNED_8(0, 0);
    
    int mID;
    
    int mSize;
    
    static {
      SIGNED_16 = new DataType("SIGNED_16", 4, 5, 2);
      SIGNED_32 = new DataType("SIGNED_32", 5, 6, 4);
      SIGNED_64 = new DataType("SIGNED_64", 6, 7, 8);
      UNSIGNED_8 = new DataType("UNSIGNED_8", 7, 8, 1);
      UNSIGNED_16 = new DataType("UNSIGNED_16", 8, 9, 2);
      UNSIGNED_32 = new DataType("UNSIGNED_32", 9, 10, 4);
      UNSIGNED_64 = new DataType("UNSIGNED_64", 10, 11, 8);
      BOOLEAN = new DataType("BOOLEAN", 11, 12, 1);
      UNSIGNED_5_6_5 = new DataType("UNSIGNED_5_6_5", 12, 13, 2);
      UNSIGNED_5_5_5_1 = new DataType("UNSIGNED_5_5_5_1", 13, 14, 2);
      UNSIGNED_4_4_4_4 = new DataType("UNSIGNED_4_4_4_4", 14, 15, 2);
      MATRIX_4X4 = new DataType("MATRIX_4X4", 15, 16, 64);
      MATRIX_3X3 = new DataType("MATRIX_3X3", 16, 17, 36);
      MATRIX_2X2 = new DataType("MATRIX_2X2", 17, 18, 16);
      RS_ELEMENT = new DataType("RS_ELEMENT", 18, 1000);
      RS_TYPE = new DataType("RS_TYPE", 19, 1001);
      RS_ALLOCATION = new DataType("RS_ALLOCATION", 20, 1002);
      RS_SAMPLER = new DataType("RS_SAMPLER", 21, 1003);
      DataType dataType = new DataType("RS_SCRIPT", 22, 1004);
      RS_SCRIPT = dataType;
      $VALUES = new DataType[] { 
          NONE, FLOAT_32, FLOAT_64, SIGNED_8, SIGNED_16, SIGNED_32, SIGNED_64, UNSIGNED_8, UNSIGNED_16, UNSIGNED_32, 
          UNSIGNED_64, BOOLEAN, UNSIGNED_5_6_5, UNSIGNED_5_5_5_1, UNSIGNED_4_4_4_4, MATRIX_4X4, MATRIX_3X3, MATRIX_2X2, RS_ELEMENT, RS_TYPE, 
          RS_ALLOCATION, RS_SAMPLER, dataType };
    }
    
    DataType(int param1Int1) {
      this.mID = param1Int1;
      this.mSize = 4;
      if (RenderScript.sPointerSize == 8)
        this.mSize = 32; 
    }
    
    DataType(int param1Int1, int param1Int2) {
      this.mID = param1Int1;
      this.mSize = param1Int2;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\renderscript\Element.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
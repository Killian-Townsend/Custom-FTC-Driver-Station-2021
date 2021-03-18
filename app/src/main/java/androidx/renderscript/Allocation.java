package androidx.renderscript;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.Surface;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;

public class Allocation extends BaseObj {
  public static final int USAGE_GRAPHICS_TEXTURE = 2;
  
  public static final int USAGE_IO_INPUT = 32;
  
  public static final int USAGE_IO_OUTPUT = 64;
  
  public static final int USAGE_SCRIPT = 1;
  
  public static final int USAGE_SHARED = 128;
  
  static BitmapFactory.Options mBitmapOptions;
  
  Allocation mAdaptedAllocation;
  
  boolean mAutoPadding = false;
  
  Bitmap mBitmap;
  
  ByteBuffer mByteBuffer = null;
  
  long mByteBufferStride = 0L;
  
  boolean mConstrainedFace;
  
  boolean mConstrainedLOD;
  
  boolean mConstrainedY;
  
  boolean mConstrainedZ;
  
  int mCurrentCount;
  
  int mCurrentDimX;
  
  int mCurrentDimY;
  
  int mCurrentDimZ;
  
  boolean mIncAllocDestroyed;
  
  long mIncCompatAllocation;
  
  boolean mReadAllowed = true;
  
  Type.CubemapFace mSelectedFace = Type.CubemapFace.POSITIVE_X;
  
  int mSelectedLOD;
  
  int mSelectedY;
  
  int mSelectedZ;
  
  int mSize;
  
  Type mType;
  
  int mUsage;
  
  boolean mWriteAllowed = true;
  
  static {
    BitmapFactory.Options options = new BitmapFactory.Options();
    mBitmapOptions = options;
    options.inScaled = false;
  }
  
  Allocation(long paramLong, RenderScript paramRenderScript, Type paramType, int paramInt) {
    super(paramLong, paramRenderScript);
    if ((paramInt & 0xFFFFFF1C) == 0) {
      if ((paramInt & 0x20) != 0) {
        this.mWriteAllowed = false;
        if ((paramInt & 0xFFFFFFDC) != 0)
          throw new RSIllegalArgumentException("Invalid usage combination."); 
      } 
      this.mType = paramType;
      this.mUsage = paramInt;
      this.mIncCompatAllocation = 0L;
      this.mIncAllocDestroyed = false;
      if (paramType != null) {
        this.mSize = paramType.getCount() * this.mType.getElement().getBytesSize();
        updateCacheInfo(paramType);
      } 
      if (RenderScript.sUseGCHooks == true)
        try {
          RenderScript.registerNativeAllocation.invoke(RenderScript.sRuntime, new Object[] { Integer.valueOf(this.mSize) });
          return;
        } catch (Exception exception) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Couldn't invoke registerNativeAllocation:");
          stringBuilder.append(exception);
          Log.e("RenderScript_jni", stringBuilder.toString());
          stringBuilder = new StringBuilder();
          stringBuilder.append("Couldn't invoke registerNativeAllocation:");
          stringBuilder.append(exception);
          throw new RSRuntimeException(stringBuilder.toString());
        }  
      return;
    } 
    throw new RSIllegalArgumentException("Unknown usage specified.");
  }
  
  private void copy1DRangeFromUnchecked(int paramInt1, int paramInt2, Object paramObject, Element.DataType paramDataType, int paramInt3) {
    boolean bool;
    int i = this.mType.mElement.getBytesSize() * paramInt2;
    if (this.mAutoPadding && this.mType.getElement().getVectorSize() == 3) {
      bool = true;
    } else {
      bool = false;
    } 
    data1DChecks(paramInt1, paramInt2, paramInt3 * paramDataType.mSize, i, bool);
    this.mRS.nAllocationData1D(getIDSafe(), paramInt1, this.mSelectedLOD, paramInt2, paramObject, i, paramDataType, this.mType.mElement.mType.mSize, bool);
  }
  
  private void copy1DRangeToUnchecked(int paramInt1, int paramInt2, Object paramObject, Element.DataType paramDataType, int paramInt3) {
    boolean bool;
    int i = this.mType.mElement.getBytesSize() * paramInt2;
    if (this.mAutoPadding && this.mType.getElement().getVectorSize() == 3) {
      bool = true;
    } else {
      bool = false;
    } 
    data1DChecks(paramInt1, paramInt2, paramInt3 * paramDataType.mSize, i, bool);
    this.mRS.nAllocationRead1D(getIDSafe(), paramInt1, this.mSelectedLOD, paramInt2, paramObject, i, paramDataType, this.mType.mElement.mType.mSize, bool);
  }
  
  private void copy3DRangeFromUnchecked(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, Object paramObject, Element.DataType paramDataType, int paramInt7) {
    boolean bool;
    this.mRS.validate();
    validate3DRange(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
    int i = this.mType.mElement.getBytesSize() * paramInt4 * paramInt5 * paramInt6;
    paramInt7 = paramDataType.mSize * paramInt7;
    if (this.mAutoPadding && this.mType.getElement().getVectorSize() == 3) {
      if (i / 4 * 3 <= paramInt7) {
        paramInt7 = i;
        bool = true;
      } else {
        throw new RSIllegalArgumentException("Array too small for allocation type.");
      } 
    } else if (i <= paramInt7) {
      bool = false;
    } else {
      throw new RSIllegalArgumentException("Array too small for allocation type.");
    } 
    this.mRS.nAllocationData3D(getIDSafe(), paramInt1, paramInt2, paramInt3, this.mSelectedLOD, paramInt4, paramInt5, paramInt6, paramObject, paramInt7, paramDataType, this.mType.mElement.mType.mSize, bool);
  }
  
  private void copyFromUnchecked(Object paramObject, Element.DataType paramDataType, int paramInt) {
    this.mRS.validate();
    int i = this.mCurrentDimZ;
    if (i > 0) {
      copy3DRangeFromUnchecked(0, 0, 0, this.mCurrentDimX, this.mCurrentDimY, i, paramObject, paramDataType, paramInt);
      return;
    } 
    i = this.mCurrentDimY;
    if (i > 0) {
      copy2DRangeFromUnchecked(0, 0, this.mCurrentDimX, i, paramObject, paramDataType, paramInt);
      return;
    } 
    copy1DRangeFromUnchecked(0, this.mCurrentCount, paramObject, paramDataType, paramInt);
  }
  
  private void copyTo(Object paramObject, Element.DataType paramDataType, int paramInt) {
    boolean bool;
    this.mRS.validate();
    if (this.mAutoPadding && this.mType.getElement().getVectorSize() == 3) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool) {
      if (paramDataType.mSize * paramInt < this.mSize / 4 * 3)
        throw new RSIllegalArgumentException("Size of output array cannot be smaller than size of allocation."); 
    } else if (paramDataType.mSize * paramInt < this.mSize) {
      throw new RSIllegalArgumentException("Size of output array cannot be smaller than size of allocation.");
    } 
    this.mRS.nAllocationRead(getID(this.mRS), paramObject, paramDataType, this.mType.mElement.mType.mSize, bool);
  }
  
  public static Allocation createCubemapFromBitmap(RenderScript paramRenderScript, Bitmap paramBitmap) {
    return createCubemapFromBitmap(paramRenderScript, paramBitmap, MipmapControl.MIPMAP_NONE, 2);
  }
  
  public static Allocation createCubemapFromBitmap(RenderScript paramRenderScript, Bitmap paramBitmap, MipmapControl paramMipmapControl, int paramInt) {
    paramRenderScript.validate();
    int j = paramBitmap.getHeight();
    int i = paramBitmap.getWidth();
    if (i % 6 == 0) {
      if (i / 6 == j) {
        boolean bool = false;
        if ((j - 1 & j) == 0) {
          i = 1;
        } else {
          i = 0;
        } 
        if (i != 0) {
          Element element = elementFromBitmap(paramRenderScript, paramBitmap);
          Type.Builder builder = new Type.Builder(paramRenderScript, element);
          builder.setX(j);
          builder.setY(j);
          builder.setFaces(true);
          if (paramMipmapControl == MipmapControl.MIPMAP_FULL)
            bool = true; 
          builder.setMipmaps(bool);
          Type type = builder.create();
          long l = paramRenderScript.nAllocationCubeCreateFromBitmap(type.getID(paramRenderScript), paramMipmapControl.mID, paramBitmap, paramInt);
          if (l != 0L)
            return new Allocation(l, paramRenderScript, type, paramInt); 
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Load failed for bitmap ");
          stringBuilder.append(paramBitmap);
          stringBuilder.append(" element ");
          stringBuilder.append(element);
          throw new RSRuntimeException(stringBuilder.toString());
        } 
        throw new RSIllegalArgumentException("Only power of 2 cube faces supported");
      } 
      throw new RSIllegalArgumentException("Only square cube map faces supported");
    } 
    throw new RSIllegalArgumentException("Cubemap height must be multiple of 6");
  }
  
  public static Allocation createCubemapFromCubeFaces(RenderScript paramRenderScript, Bitmap paramBitmap1, Bitmap paramBitmap2, Bitmap paramBitmap3, Bitmap paramBitmap4, Bitmap paramBitmap5, Bitmap paramBitmap6) {
    return createCubemapFromCubeFaces(paramRenderScript, paramBitmap1, paramBitmap2, paramBitmap3, paramBitmap4, paramBitmap5, paramBitmap6, MipmapControl.MIPMAP_NONE, 2);
  }
  
  public static Allocation createCubemapFromCubeFaces(RenderScript paramRenderScript, Bitmap paramBitmap1, Bitmap paramBitmap2, Bitmap paramBitmap3, Bitmap paramBitmap4, Bitmap paramBitmap5, Bitmap paramBitmap6, MipmapControl paramMipmapControl, int paramInt) {
    return null;
  }
  
  public static Allocation createFromBitmap(RenderScript paramRenderScript, Bitmap paramBitmap) {
    return createFromBitmap(paramRenderScript, paramBitmap, MipmapControl.MIPMAP_NONE, 131);
  }
  
  public static Allocation createFromBitmap(RenderScript paramRenderScript, Bitmap paramBitmap, MipmapControl paramMipmapControl, int paramInt) {
    Allocation allocation;
    paramRenderScript.validate();
    if (paramBitmap.getConfig() == null) {
      if ((paramInt & 0x80) == 0) {
        Bitmap bitmap = Bitmap.createBitmap(paramBitmap.getWidth(), paramBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        (new Canvas(bitmap)).drawBitmap(paramBitmap, 0.0F, 0.0F, null);
        return createFromBitmap(paramRenderScript, bitmap, paramMipmapControl, paramInt);
      } 
      throw new RSIllegalArgumentException("USAGE_SHARED cannot be used with a Bitmap that has a null config.");
    } 
    Type type = typeFromBitmap(paramRenderScript, paramBitmap, paramMipmapControl);
    if (paramMipmapControl == MipmapControl.MIPMAP_NONE && type.getElement().isCompatible(Element.RGBA_8888(paramRenderScript)) && paramInt == 131) {
      long l1 = paramRenderScript.nAllocationCreateBitmapBackedAllocation(type.getID(paramRenderScript), paramMipmapControl.mID, paramBitmap, paramInt);
      if (l1 != 0L) {
        allocation = new Allocation(l1, paramRenderScript, type, paramInt);
        allocation.setBitmap(paramBitmap);
        return allocation;
      } 
      throw new RSRuntimeException("Load failed.");
    } 
    long l = allocation.nAllocationCreateFromBitmap(type.getID((RenderScript)allocation), paramMipmapControl.mID, paramBitmap, paramInt);
    if (l != 0L)
      return new Allocation(l, (RenderScript)allocation, type, paramInt); 
    throw new RSRuntimeException("Load failed.");
  }
  
  public static Allocation createFromBitmapResource(RenderScript paramRenderScript, Resources paramResources, int paramInt) {
    return createFromBitmapResource(paramRenderScript, paramResources, paramInt, MipmapControl.MIPMAP_NONE, 3);
  }
  
  public static Allocation createFromBitmapResource(RenderScript paramRenderScript, Resources paramResources, int paramInt1, MipmapControl paramMipmapControl, int paramInt2) {
    paramRenderScript.validate();
    if ((paramInt2 & 0xE0) == 0) {
      Bitmap bitmap = BitmapFactory.decodeResource(paramResources, paramInt1);
      Allocation allocation = createFromBitmap(paramRenderScript, bitmap, paramMipmapControl, paramInt2);
      bitmap.recycle();
      return allocation;
    } 
    throw new RSIllegalArgumentException("Unsupported usage specified.");
  }
  
  public static Allocation createFromString(RenderScript paramRenderScript, String paramString, int paramInt) {
    paramRenderScript.validate();
    try {
      byte[] arrayOfByte = paramString.getBytes("UTF-8");
      Allocation allocation = createSized(paramRenderScript, Element.U8(paramRenderScript), arrayOfByte.length, paramInt);
      allocation.copyFrom(arrayOfByte);
      return allocation;
    } catch (Exception exception) {
      throw new RSRuntimeException("Could not convert string to utf-8.");
    } 
  }
  
  public static Allocation createSized(RenderScript paramRenderScript, Element paramElement, int paramInt) {
    return createSized(paramRenderScript, paramElement, paramInt, 1);
  }
  
  public static Allocation createSized(RenderScript paramRenderScript, Element paramElement, int paramInt1, int paramInt2) {
    paramRenderScript.validate();
    Type.Builder builder = new Type.Builder(paramRenderScript, paramElement);
    builder.setX(paramInt1);
    Type type = builder.create();
    long l = paramRenderScript.nAllocationCreateTyped(type.getID(paramRenderScript), MipmapControl.MIPMAP_NONE.mID, paramInt2, 0L);
    if (l != 0L)
      return new Allocation(l, paramRenderScript, type, paramInt2); 
    throw new RSRuntimeException("Allocation creation failed.");
  }
  
  public static Allocation createTyped(RenderScript paramRenderScript, Type paramType) {
    return createTyped(paramRenderScript, paramType, MipmapControl.MIPMAP_NONE, 1);
  }
  
  public static Allocation createTyped(RenderScript paramRenderScript, Type paramType, int paramInt) {
    return createTyped(paramRenderScript, paramType, MipmapControl.MIPMAP_NONE, paramInt);
  }
  
  public static Allocation createTyped(RenderScript paramRenderScript, Type paramType, MipmapControl paramMipmapControl, int paramInt) {
    paramRenderScript.validate();
    if (paramType.getID(paramRenderScript) != 0L) {
      if (paramRenderScript.usingIO() || (paramInt & 0x20) == 0) {
        long l = paramRenderScript.nAllocationCreateTyped(paramType.getID(paramRenderScript), paramMipmapControl.mID, paramInt, 0L);
        if (l != 0L)
          return new Allocation(l, paramRenderScript, paramType, paramInt); 
        throw new RSRuntimeException("Allocation creation failed.");
      } 
      throw new RSRuntimeException("USAGE_IO not supported, Allocation creation failed.");
    } 
    throw new RSInvalidStateException("Bad Type");
  }
  
  private void data1DChecks(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean) {
    this.mRS.validate();
    if (paramInt1 >= 0) {
      if (paramInt2 >= 1) {
        if (paramInt1 + paramInt2 <= this.mCurrentCount) {
          if (paramBoolean) {
            if (paramInt3 >= paramInt4 / 4 * 3)
              return; 
            throw new RSIllegalArgumentException("Array too small for allocation type.");
          } 
          if (paramInt3 >= paramInt4)
            return; 
          throw new RSIllegalArgumentException("Array too small for allocation type.");
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Overflow, Available count ");
        stringBuilder.append(this.mCurrentCount);
        stringBuilder.append(", got ");
        stringBuilder.append(paramInt2);
        stringBuilder.append(" at offset ");
        stringBuilder.append(paramInt1);
        stringBuilder.append(".");
        throw new RSIllegalArgumentException(stringBuilder.toString());
      } 
      throw new RSIllegalArgumentException("Count must be >= 1.");
    } 
    throw new RSIllegalArgumentException("Offset must be >= 0.");
  }
  
  static Element elementFromBitmap(RenderScript paramRenderScript, Bitmap paramBitmap) {
    Bitmap.Config config = paramBitmap.getConfig();
    if (config == Bitmap.Config.ALPHA_8)
      return Element.A_8(paramRenderScript); 
    if (config == Bitmap.Config.ARGB_4444)
      return Element.RGBA_4444(paramRenderScript); 
    if (config == Bitmap.Config.ARGB_8888)
      return Element.RGBA_8888(paramRenderScript); 
    if (config == Bitmap.Config.RGB_565)
      return Element.RGB_565(paramRenderScript); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Bad bitmap type: ");
    stringBuilder.append(config);
    throw new RSInvalidStateException(stringBuilder.toString());
  }
  
  private long getIDSafe() {
    Allocation allocation = this.mAdaptedAllocation;
    return (allocation != null) ? allocation.getID(this.mRS) : getID(this.mRS);
  }
  
  private void setBitmap(Bitmap paramBitmap) {
    this.mBitmap = paramBitmap;
  }
  
  static Type typeFromBitmap(RenderScript paramRenderScript, Bitmap paramBitmap, MipmapControl paramMipmapControl) {
    boolean bool;
    Type.Builder builder = new Type.Builder(paramRenderScript, elementFromBitmap(paramRenderScript, paramBitmap));
    builder.setX(paramBitmap.getWidth());
    builder.setY(paramBitmap.getHeight());
    if (paramMipmapControl == MipmapControl.MIPMAP_FULL) {
      bool = true;
    } else {
      bool = false;
    } 
    builder.setMipmaps(bool);
    return builder.create();
  }
  
  private void updateCacheInfo(Type paramType) {
    this.mCurrentDimX = paramType.getX();
    this.mCurrentDimY = paramType.getY();
    this.mCurrentDimZ = paramType.getZ();
    int i = this.mCurrentDimX;
    this.mCurrentCount = i;
    int j = this.mCurrentDimY;
    if (j > 1)
      this.mCurrentCount = i * j; 
    i = this.mCurrentDimZ;
    if (i > 1)
      this.mCurrentCount *= i; 
  }
  
  private void validate2DRange(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (this.mAdaptedAllocation != null)
      return; 
    if (paramInt1 >= 0 && paramInt2 >= 0) {
      if (paramInt4 >= 0 && paramInt3 >= 0) {
        if (paramInt1 + paramInt3 <= this.mCurrentDimX && paramInt2 + paramInt4 <= this.mCurrentDimY)
          return; 
        throw new RSIllegalArgumentException("Updated region larger than allocation.");
      } 
      throw new RSIllegalArgumentException("Height or width cannot be negative.");
    } 
    throw new RSIllegalArgumentException("Offset cannot be negative.");
  }
  
  private void validate3DRange(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
    if (this.mAdaptedAllocation != null)
      return; 
    if (paramInt1 >= 0 && paramInt2 >= 0 && paramInt3 >= 0) {
      if (paramInt5 >= 0 && paramInt4 >= 0 && paramInt6 >= 0) {
        if (paramInt1 + paramInt4 <= this.mCurrentDimX && paramInt2 + paramInt5 <= this.mCurrentDimY && paramInt3 + paramInt6 <= this.mCurrentDimZ)
          return; 
        throw new RSIllegalArgumentException("Updated region larger than allocation.");
      } 
      throw new RSIllegalArgumentException("Height or width cannot be negative.");
    } 
    throw new RSIllegalArgumentException("Offset cannot be negative.");
  }
  
  private void validateBitmapFormat(Bitmap paramBitmap) {
    Bitmap.Config config = paramBitmap.getConfig();
    if (config != null) {
      int i = null.$SwitchMap$android$graphics$Bitmap$Config[config.ordinal()];
      if (i != 1) {
        if (i != 2) {
          if (i != 3) {
            if (i != 4)
              return; 
            if ((this.mType.getElement()).mKind == Element.DataKind.PIXEL_RGBA && this.mType.getElement().getBytesSize() == 2)
              return; 
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("Allocation kind is ");
            stringBuilder3.append((this.mType.getElement()).mKind);
            stringBuilder3.append(", type ");
            stringBuilder3.append((this.mType.getElement()).mType);
            stringBuilder3.append(" of ");
            stringBuilder3.append(this.mType.getElement().getBytesSize());
            stringBuilder3.append(" bytes, passed bitmap was ");
            stringBuilder3.append(config);
            throw new RSIllegalArgumentException(stringBuilder3.toString());
          } 
          if ((this.mType.getElement()).mKind == Element.DataKind.PIXEL_RGB && this.mType.getElement().getBytesSize() == 2)
            return; 
          StringBuilder stringBuilder2 = new StringBuilder();
          stringBuilder2.append("Allocation kind is ");
          stringBuilder2.append((this.mType.getElement()).mKind);
          stringBuilder2.append(", type ");
          stringBuilder2.append((this.mType.getElement()).mType);
          stringBuilder2.append(" of ");
          stringBuilder2.append(this.mType.getElement().getBytesSize());
          stringBuilder2.append(" bytes, passed bitmap was ");
          stringBuilder2.append(config);
          throw new RSIllegalArgumentException(stringBuilder2.toString());
        } 
        if ((this.mType.getElement()).mKind == Element.DataKind.PIXEL_RGBA && this.mType.getElement().getBytesSize() == 4)
          return; 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("Allocation kind is ");
        stringBuilder1.append((this.mType.getElement()).mKind);
        stringBuilder1.append(", type ");
        stringBuilder1.append((this.mType.getElement()).mType);
        stringBuilder1.append(" of ");
        stringBuilder1.append(this.mType.getElement().getBytesSize());
        stringBuilder1.append(" bytes, passed bitmap was ");
        stringBuilder1.append(config);
        throw new RSIllegalArgumentException(stringBuilder1.toString());
      } 
      if ((this.mType.getElement()).mKind == Element.DataKind.PIXEL_A)
        return; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Allocation kind is ");
      stringBuilder.append((this.mType.getElement()).mKind);
      stringBuilder.append(", type ");
      stringBuilder.append((this.mType.getElement()).mType);
      stringBuilder.append(" of ");
      stringBuilder.append(this.mType.getElement().getBytesSize());
      stringBuilder.append(" bytes, passed bitmap was ");
      stringBuilder.append(config);
      throw new RSIllegalArgumentException(stringBuilder.toString());
    } 
    throw new RSIllegalArgumentException("Bitmap has an unsupported format for this operation");
  }
  
  private void validateBitmapSize(Bitmap paramBitmap) {
    if (this.mCurrentDimX == paramBitmap.getWidth() && this.mCurrentDimY == paramBitmap.getHeight())
      return; 
    throw new RSIllegalArgumentException("Cannot update allocation from bitmap, sizes mismatch");
  }
  
  private void validateIsFloat32() {
    if (this.mType.mElement.mType == Element.DataType.FLOAT_32)
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("32 bit float source does not match allocation type ");
    stringBuilder.append(this.mType.mElement.mType);
    throw new RSIllegalArgumentException(stringBuilder.toString());
  }
  
  private void validateIsFloat64() {
    if (this.mType.mElement.mType == Element.DataType.FLOAT_64)
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("64 bit float source does not match allocation type ");
    stringBuilder.append(this.mType.mElement.mType);
    throw new RSIllegalArgumentException(stringBuilder.toString());
  }
  
  private void validateIsInt16() {
    if (this.mType.mElement.mType != Element.DataType.SIGNED_16) {
      if (this.mType.mElement.mType == Element.DataType.UNSIGNED_16)
        return; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("16 bit integer source does not match allocation type ");
      stringBuilder.append(this.mType.mElement.mType);
      throw new RSIllegalArgumentException(stringBuilder.toString());
    } 
  }
  
  private void validateIsInt32() {
    if (this.mType.mElement.mType != Element.DataType.SIGNED_32) {
      if (this.mType.mElement.mType == Element.DataType.UNSIGNED_32)
        return; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("32 bit integer source does not match allocation type ");
      stringBuilder.append(this.mType.mElement.mType);
      throw new RSIllegalArgumentException(stringBuilder.toString());
    } 
  }
  
  private void validateIsInt64() {
    if (this.mType.mElement.mType != Element.DataType.SIGNED_64) {
      if (this.mType.mElement.mType == Element.DataType.UNSIGNED_64)
        return; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("64 bit integer source does not match allocation type ");
      stringBuilder.append(this.mType.mElement.mType);
      throw new RSIllegalArgumentException(stringBuilder.toString());
    } 
  }
  
  private void validateIsInt8() {
    if (this.mType.mElement.mType != Element.DataType.SIGNED_8) {
      if (this.mType.mElement.mType == Element.DataType.UNSIGNED_8)
        return; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("8 bit integer source does not match allocation type ");
      stringBuilder.append(this.mType.mElement.mType);
      throw new RSIllegalArgumentException(stringBuilder.toString());
    } 
  }
  
  private void validateIsObject() {
    if (this.mType.mElement.mType != Element.DataType.RS_ELEMENT && this.mType.mElement.mType != Element.DataType.RS_TYPE && this.mType.mElement.mType != Element.DataType.RS_ALLOCATION && this.mType.mElement.mType != Element.DataType.RS_SAMPLER) {
      if (this.mType.mElement.mType == Element.DataType.RS_SCRIPT)
        return; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Object source does not match allocation type ");
      stringBuilder.append(this.mType.mElement.mType);
      throw new RSIllegalArgumentException(stringBuilder.toString());
    } 
  }
  
  private Element.DataType validateObjectIsPrimitiveArray(Object<?> paramObject, boolean paramBoolean) {
    paramObject = (Object<?>)paramObject.getClass();
    if (paramObject.isArray()) {
      paramObject = (Object<?>)paramObject.getComponentType();
      if (paramObject.isPrimitive()) {
        if (paramObject == long.class) {
          if (paramBoolean) {
            validateIsInt64();
            return this.mType.mElement.mType;
          } 
          return Element.DataType.SIGNED_64;
        } 
        if (paramObject == int.class) {
          if (paramBoolean) {
            validateIsInt32();
            return this.mType.mElement.mType;
          } 
          return Element.DataType.SIGNED_32;
        } 
        if (paramObject == short.class) {
          if (paramBoolean) {
            validateIsInt16();
            return this.mType.mElement.mType;
          } 
          return Element.DataType.SIGNED_16;
        } 
        if (paramObject == byte.class) {
          if (paramBoolean) {
            validateIsInt8();
            return this.mType.mElement.mType;
          } 
          return Element.DataType.SIGNED_8;
        } 
        if (paramObject == float.class) {
          if (paramBoolean)
            validateIsFloat32(); 
          return Element.DataType.FLOAT_32;
        } 
        if (paramObject == double.class) {
          if (paramBoolean)
            validateIsFloat64(); 
          return Element.DataType.FLOAT_64;
        } 
        return null;
      } 
      throw new RSIllegalArgumentException("Object passed is not an Array of primitives.");
    } 
    throw new RSIllegalArgumentException("Object passed is not an array of primitives.");
  }
  
  public void copy1DRangeFrom(int paramInt1, int paramInt2, Allocation paramAllocation, int paramInt3) {
    this.mRS.nAllocationData2D(getIDSafe(), paramInt1, 0, this.mSelectedLOD, this.mSelectedFace.mID, paramInt2, 1, paramAllocation.getID(this.mRS), paramInt3, 0, paramAllocation.mSelectedLOD, paramAllocation.mSelectedFace.mID);
  }
  
  public void copy1DRangeFrom(int paramInt1, int paramInt2, Object paramObject) {
    copy1DRangeFromUnchecked(paramInt1, paramInt2, paramObject, validateObjectIsPrimitiveArray(paramObject, true), Array.getLength(paramObject));
  }
  
  public void copy1DRangeFrom(int paramInt1, int paramInt2, byte[] paramArrayOfbyte) {
    validateIsInt8();
    copy1DRangeFromUnchecked(paramInt1, paramInt2, paramArrayOfbyte, Element.DataType.SIGNED_8, paramArrayOfbyte.length);
  }
  
  public void copy1DRangeFrom(int paramInt1, int paramInt2, float[] paramArrayOffloat) {
    validateIsFloat32();
    copy1DRangeFromUnchecked(paramInt1, paramInt2, paramArrayOffloat, Element.DataType.FLOAT_32, paramArrayOffloat.length);
  }
  
  public void copy1DRangeFrom(int paramInt1, int paramInt2, int[] paramArrayOfint) {
    validateIsInt32();
    copy1DRangeFromUnchecked(paramInt1, paramInt2, paramArrayOfint, Element.DataType.SIGNED_32, paramArrayOfint.length);
  }
  
  public void copy1DRangeFrom(int paramInt1, int paramInt2, short[] paramArrayOfshort) {
    validateIsInt16();
    copy1DRangeFromUnchecked(paramInt1, paramInt2, paramArrayOfshort, Element.DataType.SIGNED_16, paramArrayOfshort.length);
  }
  
  public void copy1DRangeFromUnchecked(int paramInt1, int paramInt2, Object paramObject) {
    copy1DRangeFromUnchecked(paramInt1, paramInt2, paramObject, validateObjectIsPrimitiveArray(paramObject, false), Array.getLength(paramObject));
  }
  
  public void copy1DRangeFromUnchecked(int paramInt1, int paramInt2, byte[] paramArrayOfbyte) {
    copy1DRangeFromUnchecked(paramInt1, paramInt2, paramArrayOfbyte, Element.DataType.SIGNED_8, paramArrayOfbyte.length);
  }
  
  public void copy1DRangeFromUnchecked(int paramInt1, int paramInt2, float[] paramArrayOffloat) {
    copy1DRangeFromUnchecked(paramInt1, paramInt2, paramArrayOffloat, Element.DataType.FLOAT_32, paramArrayOffloat.length);
  }
  
  public void copy1DRangeFromUnchecked(int paramInt1, int paramInt2, int[] paramArrayOfint) {
    copy1DRangeFromUnchecked(paramInt1, paramInt2, paramArrayOfint, Element.DataType.SIGNED_32, paramArrayOfint.length);
  }
  
  public void copy1DRangeFromUnchecked(int paramInt1, int paramInt2, short[] paramArrayOfshort) {
    copy1DRangeFromUnchecked(paramInt1, paramInt2, paramArrayOfshort, Element.DataType.SIGNED_16, paramArrayOfshort.length);
  }
  
  public void copy1DRangeTo(int paramInt1, int paramInt2, Object paramObject) {
    copy1DRangeToUnchecked(paramInt1, paramInt2, paramObject, validateObjectIsPrimitiveArray(paramObject, true), Array.getLength(paramObject));
  }
  
  public void copy1DRangeTo(int paramInt1, int paramInt2, byte[] paramArrayOfbyte) {
    validateIsInt8();
    copy1DRangeToUnchecked(paramInt1, paramInt2, paramArrayOfbyte, Element.DataType.SIGNED_8, paramArrayOfbyte.length);
  }
  
  public void copy1DRangeTo(int paramInt1, int paramInt2, float[] paramArrayOffloat) {
    validateIsFloat32();
    copy1DRangeToUnchecked(paramInt1, paramInt2, paramArrayOffloat, Element.DataType.FLOAT_32, paramArrayOffloat.length);
  }
  
  public void copy1DRangeTo(int paramInt1, int paramInt2, int[] paramArrayOfint) {
    validateIsInt32();
    copy1DRangeToUnchecked(paramInt1, paramInt2, paramArrayOfint, Element.DataType.SIGNED_32, paramArrayOfint.length);
  }
  
  public void copy1DRangeTo(int paramInt1, int paramInt2, short[] paramArrayOfshort) {
    validateIsInt16();
    copy1DRangeToUnchecked(paramInt1, paramInt2, paramArrayOfshort, Element.DataType.SIGNED_16, paramArrayOfshort.length);
  }
  
  public void copy1DRangeToUnchecked(int paramInt1, int paramInt2, Object paramObject) {
    copy1DRangeToUnchecked(paramInt1, paramInt2, paramObject, validateObjectIsPrimitiveArray(paramObject, false), Array.getLength(paramObject));
  }
  
  public void copy1DRangeToUnchecked(int paramInt1, int paramInt2, byte[] paramArrayOfbyte) {
    copy1DRangeToUnchecked(paramInt1, paramInt2, paramArrayOfbyte, Element.DataType.SIGNED_8, paramArrayOfbyte.length);
  }
  
  public void copy1DRangeToUnchecked(int paramInt1, int paramInt2, float[] paramArrayOffloat) {
    copy1DRangeToUnchecked(paramInt1, paramInt2, paramArrayOffloat, Element.DataType.FLOAT_32, paramArrayOffloat.length);
  }
  
  public void copy1DRangeToUnchecked(int paramInt1, int paramInt2, int[] paramArrayOfint) {
    copy1DRangeToUnchecked(paramInt1, paramInt2, paramArrayOfint, Element.DataType.SIGNED_32, paramArrayOfint.length);
  }
  
  public void copy1DRangeToUnchecked(int paramInt1, int paramInt2, short[] paramArrayOfshort) {
    copy1DRangeToUnchecked(paramInt1, paramInt2, paramArrayOfshort, Element.DataType.SIGNED_16, paramArrayOfshort.length);
  }
  
  public void copy2DRangeFrom(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Allocation paramAllocation, int paramInt5, int paramInt6) {
    this.mRS.validate();
    validate2DRange(paramInt1, paramInt2, paramInt3, paramInt4);
    this.mRS.nAllocationData2D(getIDSafe(), paramInt1, paramInt2, this.mSelectedLOD, this.mSelectedFace.mID, paramInt3, paramInt4, paramAllocation.getID(this.mRS), paramInt5, paramInt6, paramAllocation.mSelectedLOD, paramAllocation.mSelectedFace.mID);
  }
  
  public void copy2DRangeFrom(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Object paramObject) {
    copy2DRangeFromUnchecked(paramInt1, paramInt2, paramInt3, paramInt4, paramObject, validateObjectIsPrimitiveArray(paramObject, true), Array.getLength(paramObject));
  }
  
  public void copy2DRangeFrom(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfbyte) {
    validateIsInt8();
    copy2DRangeFromUnchecked(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfbyte, Element.DataType.SIGNED_8, paramArrayOfbyte.length);
  }
  
  public void copy2DRangeFrom(int paramInt1, int paramInt2, int paramInt3, int paramInt4, float[] paramArrayOffloat) {
    validateIsFloat32();
    copy2DRangeFromUnchecked(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOffloat, Element.DataType.FLOAT_32, paramArrayOffloat.length);
  }
  
  public void copy2DRangeFrom(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint) {
    validateIsInt32();
    copy2DRangeFromUnchecked(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfint, Element.DataType.SIGNED_32, paramArrayOfint.length);
  }
  
  public void copy2DRangeFrom(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short[] paramArrayOfshort) {
    validateIsInt16();
    copy2DRangeFromUnchecked(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfshort, Element.DataType.SIGNED_16, paramArrayOfshort.length);
  }
  
  public void copy2DRangeFrom(int paramInt1, int paramInt2, Bitmap paramBitmap) {
    this.mRS.validate();
    if (paramBitmap.getConfig() == null) {
      Bitmap bitmap = Bitmap.createBitmap(paramBitmap.getWidth(), paramBitmap.getHeight(), Bitmap.Config.ARGB_8888);
      (new Canvas(bitmap)).drawBitmap(paramBitmap, 0.0F, 0.0F, null);
      copy2DRangeFrom(paramInt1, paramInt2, bitmap);
      return;
    } 
    validateBitmapFormat(paramBitmap);
    validate2DRange(paramInt1, paramInt2, paramBitmap.getWidth(), paramBitmap.getHeight());
    this.mRS.nAllocationData2D(getIDSafe(), paramInt1, paramInt2, this.mSelectedLOD, this.mSelectedFace.mID, paramBitmap);
  }
  
  void copy2DRangeFromUnchecked(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Object paramObject, Element.DataType paramDataType, int paramInt5) {
    boolean bool;
    this.mRS.validate();
    validate2DRange(paramInt1, paramInt2, paramInt3, paramInt4);
    int i = this.mType.mElement.getBytesSize() * paramInt3 * paramInt4;
    paramInt5 = paramDataType.mSize * paramInt5;
    if (this.mAutoPadding && this.mType.getElement().getVectorSize() == 3) {
      if (i / 4 * 3 <= paramInt5) {
        paramInt5 = i;
        bool = true;
      } else {
        throw new RSIllegalArgumentException("Array too small for allocation type.");
      } 
    } else if (i <= paramInt5) {
      bool = false;
    } else {
      throw new RSIllegalArgumentException("Array too small for allocation type.");
    } 
    this.mRS.nAllocationData2D(getIDSafe(), paramInt1, paramInt2, this.mSelectedLOD, this.mSelectedFace.mID, paramInt3, paramInt4, paramObject, paramInt5, paramDataType, this.mType.mElement.mType.mSize, bool);
  }
  
  public void copy2DRangeTo(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Object paramObject) {
    copy2DRangeToUnchecked(paramInt1, paramInt2, paramInt3, paramInt4, paramObject, validateObjectIsPrimitiveArray(paramObject, true), Array.getLength(paramObject));
  }
  
  public void copy2DRangeTo(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfbyte) {
    validateIsInt8();
    copy2DRangeToUnchecked(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfbyte, Element.DataType.SIGNED_8, paramArrayOfbyte.length);
  }
  
  public void copy2DRangeTo(int paramInt1, int paramInt2, int paramInt3, int paramInt4, float[] paramArrayOffloat) {
    validateIsFloat32();
    copy2DRangeToUnchecked(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOffloat, Element.DataType.FLOAT_32, paramArrayOffloat.length);
  }
  
  public void copy2DRangeTo(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint) {
    validateIsInt32();
    copy2DRangeToUnchecked(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfint, Element.DataType.SIGNED_32, paramArrayOfint.length);
  }
  
  public void copy2DRangeTo(int paramInt1, int paramInt2, int paramInt3, int paramInt4, short[] paramArrayOfshort) {
    validateIsInt16();
    copy2DRangeToUnchecked(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfshort, Element.DataType.SIGNED_16, paramArrayOfshort.length);
  }
  
  void copy2DRangeToUnchecked(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Object paramObject, Element.DataType paramDataType, int paramInt5) {
    boolean bool;
    this.mRS.validate();
    validate2DRange(paramInt1, paramInt2, paramInt3, paramInt4);
    int i = this.mType.mElement.getBytesSize() * paramInt3 * paramInt4;
    paramInt5 = paramDataType.mSize * paramInt5;
    if (this.mAutoPadding && this.mType.getElement().getVectorSize() == 3) {
      if (i / 4 * 3 <= paramInt5) {
        paramInt5 = i;
        bool = true;
      } else {
        throw new RSIllegalArgumentException("Array too small for allocation type.");
      } 
    } else if (i <= paramInt5) {
      bool = false;
    } else {
      throw new RSIllegalArgumentException("Array too small for allocation type.");
    } 
    this.mRS.nAllocationRead2D(getIDSafe(), paramInt1, paramInt2, this.mSelectedLOD, this.mSelectedFace.mID, paramInt3, paramInt4, paramObject, paramInt5, paramDataType, this.mType.mElement.mType.mSize, bool);
  }
  
  public void copy3DRangeFrom(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, Allocation paramAllocation, int paramInt7, int paramInt8, int paramInt9) {
    this.mRS.validate();
    validate3DRange(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
    this.mRS.nAllocationData3D(getIDSafe(), paramInt1, paramInt2, paramInt3, this.mSelectedLOD, paramInt4, paramInt5, paramInt6, paramAllocation.getID(this.mRS), paramInt7, paramInt8, paramInt9, paramAllocation.mSelectedLOD);
  }
  
  public void copy3DRangeFrom(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, Object paramObject) {
    copy3DRangeFromUnchecked(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramObject, validateObjectIsPrimitiveArray(paramObject, true), Array.getLength(paramObject));
  }
  
  public void copyFrom(Bitmap paramBitmap) {
    this.mRS.validate();
    if (paramBitmap.getConfig() == null) {
      Bitmap bitmap = Bitmap.createBitmap(paramBitmap.getWidth(), paramBitmap.getHeight(), Bitmap.Config.ARGB_8888);
      (new Canvas(bitmap)).drawBitmap(paramBitmap, 0.0F, 0.0F, null);
      copyFrom(bitmap);
      return;
    } 
    validateBitmapSize(paramBitmap);
    validateBitmapFormat(paramBitmap);
    this.mRS.nAllocationCopyFromBitmap(getID(this.mRS), paramBitmap);
  }
  
  public void copyFrom(Allocation paramAllocation) {
    this.mRS.validate();
    if (this.mType.equals(paramAllocation.getType())) {
      copy2DRangeFrom(0, 0, this.mCurrentDimX, this.mCurrentDimY, paramAllocation, 0, 0);
      return;
    } 
    throw new RSIllegalArgumentException("Types of allocations must match.");
  }
  
  public void copyFrom(Object paramObject) {
    copyFromUnchecked(paramObject, validateObjectIsPrimitiveArray(paramObject, true), Array.getLength(paramObject));
  }
  
  public void copyFrom(byte[] paramArrayOfbyte) {
    validateIsInt8();
    copyFromUnchecked(paramArrayOfbyte, Element.DataType.SIGNED_8, paramArrayOfbyte.length);
  }
  
  public void copyFrom(float[] paramArrayOffloat) {
    validateIsFloat32();
    copyFromUnchecked(paramArrayOffloat, Element.DataType.FLOAT_32, paramArrayOffloat.length);
  }
  
  public void copyFrom(int[] paramArrayOfint) {
    validateIsInt32();
    copyFromUnchecked(paramArrayOfint, Element.DataType.SIGNED_32, paramArrayOfint.length);
  }
  
  public void copyFrom(BaseObj[] paramArrayOfBaseObj) {
    this.mRS.validate();
    validateIsObject();
    if (paramArrayOfBaseObj.length == this.mCurrentCount) {
      if (RenderScript.sPointerSize == 8) {
        long[] arrayOfLong = new long[paramArrayOfBaseObj.length * 4];
        for (int j = 0; j < paramArrayOfBaseObj.length; j++)
          arrayOfLong[j * 4] = paramArrayOfBaseObj[j].getID(this.mRS); 
        copy1DRangeFromUnchecked(0, this.mCurrentCount, arrayOfLong);
        return;
      } 
      int[] arrayOfInt = new int[paramArrayOfBaseObj.length];
      for (int i = 0; i < paramArrayOfBaseObj.length; i++)
        arrayOfInt[i] = (int)paramArrayOfBaseObj[i].getID(this.mRS); 
      copy1DRangeFromUnchecked(0, this.mCurrentCount, arrayOfInt);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Array size mismatch, allocation sizeX = ");
    stringBuilder.append(this.mCurrentCount);
    stringBuilder.append(", array length = ");
    stringBuilder.append(paramArrayOfBaseObj.length);
    throw new RSIllegalArgumentException(stringBuilder.toString());
  }
  
  public void copyFrom(short[] paramArrayOfshort) {
    validateIsInt16();
    copyFromUnchecked(paramArrayOfshort, Element.DataType.SIGNED_16, paramArrayOfshort.length);
  }
  
  public void copyFromUnchecked(Object paramObject) {
    copyFromUnchecked(paramObject, validateObjectIsPrimitiveArray(paramObject, false), Array.getLength(paramObject));
  }
  
  public void copyFromUnchecked(byte[] paramArrayOfbyte) {
    copyFromUnchecked(paramArrayOfbyte, Element.DataType.SIGNED_8, paramArrayOfbyte.length);
  }
  
  public void copyFromUnchecked(float[] paramArrayOffloat) {
    copyFromUnchecked(paramArrayOffloat, Element.DataType.FLOAT_32, paramArrayOffloat.length);
  }
  
  public void copyFromUnchecked(int[] paramArrayOfint) {
    copyFromUnchecked(paramArrayOfint, Element.DataType.SIGNED_32, paramArrayOfint.length);
  }
  
  public void copyFromUnchecked(short[] paramArrayOfshort) {
    copyFromUnchecked(paramArrayOfshort, Element.DataType.SIGNED_16, paramArrayOfshort.length);
  }
  
  public void copyTo(Bitmap paramBitmap) {
    this.mRS.validate();
    validateBitmapFormat(paramBitmap);
    validateBitmapSize(paramBitmap);
    this.mRS.nAllocationCopyToBitmap(getID(this.mRS), paramBitmap);
  }
  
  public void copyTo(Object paramObject) {
    copyTo(paramObject, validateObjectIsPrimitiveArray(paramObject, true), Array.getLength(paramObject));
  }
  
  public void copyTo(byte[] paramArrayOfbyte) {
    validateIsInt8();
    copyTo(paramArrayOfbyte, Element.DataType.SIGNED_8, paramArrayOfbyte.length);
  }
  
  public void copyTo(float[] paramArrayOffloat) {
    validateIsFloat32();
    copyTo(paramArrayOffloat, Element.DataType.FLOAT_32, paramArrayOffloat.length);
  }
  
  public void copyTo(int[] paramArrayOfint) {
    validateIsInt32();
    copyTo(paramArrayOfint, Element.DataType.SIGNED_32, paramArrayOfint.length);
  }
  
  public void copyTo(short[] paramArrayOfshort) {
    validateIsInt16();
    copyTo(paramArrayOfshort, Element.DataType.SIGNED_16, paramArrayOfshort.length);
  }
  
  public void destroy() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mIncCompatAllocation : J
    //   4: lconst_0
    //   5: lcmp
    //   6: ifeq -> 86
    //   9: iconst_0
    //   10: istore_1
    //   11: aload_0
    //   12: monitorenter
    //   13: aload_0
    //   14: getfield mIncAllocDestroyed : Z
    //   17: ifne -> 27
    //   20: aload_0
    //   21: iconst_1
    //   22: putfield mIncAllocDestroyed : Z
    //   25: iconst_1
    //   26: istore_1
    //   27: aload_0
    //   28: monitorexit
    //   29: iload_1
    //   30: ifeq -> 86
    //   33: aload_0
    //   34: getfield mRS : Landroidx/renderscript/RenderScript;
    //   37: getfield mRWLock : Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   40: invokevirtual readLock : ()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   43: astore_2
    //   44: aload_2
    //   45: invokevirtual lock : ()V
    //   48: aload_0
    //   49: getfield mRS : Landroidx/renderscript/RenderScript;
    //   52: invokevirtual isAlive : ()Z
    //   55: ifeq -> 69
    //   58: aload_0
    //   59: getfield mRS : Landroidx/renderscript/RenderScript;
    //   62: aload_0
    //   63: getfield mIncCompatAllocation : J
    //   66: invokevirtual nIncObjDestroy : (J)V
    //   69: aload_2
    //   70: invokevirtual unlock : ()V
    //   73: aload_0
    //   74: lconst_0
    //   75: putfield mIncCompatAllocation : J
    //   78: goto -> 86
    //   81: astore_2
    //   82: aload_0
    //   83: monitorexit
    //   84: aload_2
    //   85: athrow
    //   86: aload_0
    //   87: getfield mUsage : I
    //   90: bipush #96
    //   92: iand
    //   93: ifeq -> 101
    //   96: aload_0
    //   97: aconst_null
    //   98: invokevirtual setSurface : (Landroid/view/Surface;)V
    //   101: aload_0
    //   102: invokespecial destroy : ()V
    //   105: return
    // Exception table:
    //   from	to	target	type
    //   13	25	81	finally
    //   27	29	81	finally
    //   82	84	81	finally
  }
  
  protected void finalize() throws Throwable {
    if (RenderScript.sUseGCHooks == true)
      RenderScript.registerNativeFree.invoke(RenderScript.sRuntime, new Object[] { Integer.valueOf(this.mSize) }); 
    super.finalize();
  }
  
  public void generateMipmaps() {
    this.mRS.nAllocationGenerateMipmaps(getID(this.mRS));
  }
  
  public ByteBuffer getByteBuffer() {
    int i = this.mType.getX() * this.mType.getElement().getBytesSize();
    if (this.mRS.getDispatchAPILevel() < 21) {
      byte[] arrayOfByte;
      if (this.mType.getZ() > 0)
        return null; 
      if (this.mType.getY() > 0) {
        arrayOfByte = new byte[this.mType.getY() * i];
        copy2DRangeToUnchecked(0, 0, this.mType.getX(), this.mType.getY(), arrayOfByte, Element.DataType.SIGNED_8, i * this.mType.getY());
      } else {
        arrayOfByte = new byte[i];
        copy1DRangeToUnchecked(0, this.mType.getX(), arrayOfByte);
      } 
      ByteBuffer byteBuffer = ByteBuffer.wrap(arrayOfByte).asReadOnlyBuffer();
      this.mByteBufferStride = i;
      return byteBuffer;
    } 
    if (this.mByteBuffer == null || (this.mUsage & 0x20) != 0)
      this.mByteBuffer = this.mRS.nAllocationGetByteBuffer(getID(this.mRS), i, this.mType.getY(), this.mType.getZ()); 
    return this.mByteBuffer;
  }
  
  public int getBytesSize() {
    return (this.mType.mDimYuv != 0) ? (int)Math.ceil((this.mType.getCount() * this.mType.getElement().getBytesSize()) * 1.5D) : (this.mType.getCount() * this.mType.getElement().getBytesSize());
  }
  
  public Element getElement() {
    return this.mType.getElement();
  }
  
  public long getIncAllocID() {
    return this.mIncCompatAllocation;
  }
  
  public long getStride() {
    if (this.mByteBufferStride == 0L)
      if (this.mRS.getDispatchAPILevel() > 21) {
        this.mByteBufferStride = this.mRS.nAllocationGetStride(getID(this.mRS));
      } else {
        this.mByteBufferStride = (this.mType.getX() * this.mType.getElement().getBytesSize());
      }  
    return this.mByteBufferStride;
  }
  
  public Type getType() {
    return this.mType;
  }
  
  public int getUsage() {
    return this.mUsage;
  }
  
  public void ioReceive() {
    if ((this.mUsage & 0x20) != 0) {
      this.mRS.validate();
      this.mRS.nAllocationIoReceive(getID(this.mRS));
      return;
    } 
    throw new RSIllegalArgumentException("Can only receive if IO_INPUT usage specified.");
  }
  
  public void ioSend() {
    if ((this.mUsage & 0x40) != 0) {
      this.mRS.validate();
      this.mRS.nAllocationIoSend(getID(this.mRS));
      return;
    } 
    throw new RSIllegalArgumentException("Can only send buffer if IO_OUTPUT usage specified.");
  }
  
  public void ioSendOutput() {
    ioSend();
  }
  
  public void setAutoPadding(boolean paramBoolean) {
    this.mAutoPadding = paramBoolean;
  }
  
  public void setFromFieldPacker(int paramInt1, int paramInt2, FieldPacker paramFieldPacker) {
    this.mRS.validate();
    if (paramInt2 < this.mType.mElement.mElements.length) {
      if (paramInt1 >= 0) {
        byte[] arrayOfByte = paramFieldPacker.getData();
        int i = paramFieldPacker.getPos();
        int j = this.mType.mElement.mElements[paramInt2].getBytesSize() * this.mType.mElement.mArraySizes[paramInt2];
        if (i == j) {
          this.mRS.nAllocationElementData1D(getIDSafe(), paramInt1, this.mSelectedLOD, paramInt2, arrayOfByte, i);
          return;
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("Field packer sizelength ");
        stringBuilder1.append(i);
        stringBuilder1.append(" does not match component size ");
        stringBuilder1.append(j);
        stringBuilder1.append(".");
        throw new RSIllegalArgumentException(stringBuilder1.toString());
      } 
      throw new RSIllegalArgumentException("Offset must be >= 0.");
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Component_number ");
    stringBuilder.append(paramInt2);
    stringBuilder.append(" out of range.");
    throw new RSIllegalArgumentException(stringBuilder.toString());
  }
  
  public void setFromFieldPacker(int paramInt, FieldPacker paramFieldPacker) {
    this.mRS.validate();
    int i = this.mType.mElement.getBytesSize();
    byte[] arrayOfByte = paramFieldPacker.getData();
    int j = paramFieldPacker.getPos();
    int k = j / i;
    if (i * k == j) {
      copy1DRangeFromUnchecked(paramInt, k, arrayOfByte);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Field packer length ");
    stringBuilder.append(j);
    stringBuilder.append(" not divisible by element size ");
    stringBuilder.append(i);
    stringBuilder.append(".");
    throw new RSIllegalArgumentException(stringBuilder.toString());
  }
  
  public void setIncAllocID(long paramLong) {
    this.mIncCompatAllocation = paramLong;
  }
  
  public void setSurface(Surface paramSurface) {
    this.mRS.validate();
    if ((this.mUsage & 0x40) != 0) {
      this.mRS.nAllocationSetSurface(getID(this.mRS), paramSurface);
      return;
    } 
    throw new RSInvalidStateException("Allocation is not USAGE_IO_OUTPUT.");
  }
  
  public void syncAll(int paramInt) {
    if (paramInt == 1 || paramInt == 2) {
      this.mRS.validate();
      this.mRS.nAllocationSyncAll(getIDSafe(), paramInt);
      return;
    } 
    throw new RSIllegalArgumentException("Source must be exactly one usage type.");
  }
  
  public enum MipmapControl {
    MIPMAP_FULL,
    MIPMAP_NONE(0),
    MIPMAP_ON_SYNC_TO_TEXTURE(0);
    
    int mID;
    
    static {
      MipmapControl mipmapControl = new MipmapControl("MIPMAP_ON_SYNC_TO_TEXTURE", 2, 2);
      MIPMAP_ON_SYNC_TO_TEXTURE = mipmapControl;
      $VALUES = new MipmapControl[] { MIPMAP_NONE, MIPMAP_FULL, mipmapControl };
    }
    
    MipmapControl(int param1Int1) {
      this.mID = param1Int1;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\renderscript\Allocation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
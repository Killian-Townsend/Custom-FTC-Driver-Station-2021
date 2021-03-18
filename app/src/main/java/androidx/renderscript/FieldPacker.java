package androidx.renderscript;

import android.util.Log;
import java.util.BitSet;

public class FieldPacker {
  private BitSet mAlignment;
  
  private byte[] mData;
  
  private int mLen;
  
  private int mPos = 0;
  
  public FieldPacker(int paramInt) {
    this.mLen = paramInt;
    this.mData = new byte[paramInt];
    this.mAlignment = new BitSet();
  }
  
  public FieldPacker(byte[] paramArrayOfbyte) {
    this.mLen = paramArrayOfbyte.length;
    this.mData = paramArrayOfbyte;
    this.mAlignment = new BitSet();
  }
  
  private void addSafely(Object paramObject) {
    int i = this.mPos;
    while (true) {
      boolean bool = false;
      try {
        addToPack(this, paramObject);
      } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
        this.mPos = i;
        resize(this.mLen * 2);
        bool = true;
      } 
      if (!bool)
        return; 
    } 
  }
  
  private static void addToPack(FieldPacker paramFieldPacker, Object paramObject) {
    if (paramObject instanceof Boolean) {
      paramFieldPacker.addBoolean(((Boolean)paramObject).booleanValue());
      return;
    } 
    if (paramObject instanceof Byte) {
      paramFieldPacker.addI8(((Byte)paramObject).byteValue());
      return;
    } 
    if (paramObject instanceof Short) {
      paramFieldPacker.addI16(((Short)paramObject).shortValue());
      return;
    } 
    if (paramObject instanceof Integer) {
      paramFieldPacker.addI32(((Integer)paramObject).intValue());
      return;
    } 
    if (paramObject instanceof Long) {
      paramFieldPacker.addI64(((Long)paramObject).longValue());
      return;
    } 
    if (paramObject instanceof Float) {
      paramFieldPacker.addF32(((Float)paramObject).floatValue());
      return;
    } 
    if (paramObject instanceof Double) {
      paramFieldPacker.addF64(((Double)paramObject).doubleValue());
      return;
    } 
    if (paramObject instanceof Byte2) {
      paramFieldPacker.addI8((Byte2)paramObject);
      return;
    } 
    if (paramObject instanceof Byte3) {
      paramFieldPacker.addI8((Byte3)paramObject);
      return;
    } 
    if (paramObject instanceof Byte4) {
      paramFieldPacker.addI8((Byte4)paramObject);
      return;
    } 
    if (paramObject instanceof Short2) {
      paramFieldPacker.addI16((Short2)paramObject);
      return;
    } 
    if (paramObject instanceof Short3) {
      paramFieldPacker.addI16((Short3)paramObject);
      return;
    } 
    if (paramObject instanceof Short4) {
      paramFieldPacker.addI16((Short4)paramObject);
      return;
    } 
    if (paramObject instanceof Int2) {
      paramFieldPacker.addI32((Int2)paramObject);
      return;
    } 
    if (paramObject instanceof Int3) {
      paramFieldPacker.addI32((Int3)paramObject);
      return;
    } 
    if (paramObject instanceof Int4) {
      paramFieldPacker.addI32((Int4)paramObject);
      return;
    } 
    if (paramObject instanceof Long2) {
      paramFieldPacker.addI64((Long2)paramObject);
      return;
    } 
    if (paramObject instanceof Long3) {
      paramFieldPacker.addI64((Long3)paramObject);
      return;
    } 
    if (paramObject instanceof Long4) {
      paramFieldPacker.addI64((Long4)paramObject);
      return;
    } 
    if (paramObject instanceof Float2) {
      paramFieldPacker.addF32((Float2)paramObject);
      return;
    } 
    if (paramObject instanceof Float3) {
      paramFieldPacker.addF32((Float3)paramObject);
      return;
    } 
    if (paramObject instanceof Float4) {
      paramFieldPacker.addF32((Float4)paramObject);
      return;
    } 
    if (paramObject instanceof Double2) {
      paramFieldPacker.addF64((Double2)paramObject);
      return;
    } 
    if (paramObject instanceof Double3) {
      paramFieldPacker.addF64((Double3)paramObject);
      return;
    } 
    if (paramObject instanceof Double4) {
      paramFieldPacker.addF64((Double4)paramObject);
      return;
    } 
    if (paramObject instanceof Matrix2f) {
      paramFieldPacker.addMatrix((Matrix2f)paramObject);
      return;
    } 
    if (paramObject instanceof Matrix3f) {
      paramFieldPacker.addMatrix((Matrix3f)paramObject);
      return;
    } 
    if (paramObject instanceof Matrix4f) {
      paramFieldPacker.addMatrix((Matrix4f)paramObject);
      return;
    } 
    if (paramObject instanceof BaseObj)
      paramFieldPacker.addObj((BaseObj)paramObject); 
  }
  
  static FieldPacker createFieldPack(Object[] paramArrayOfObject) {
    int k = paramArrayOfObject.length;
    boolean bool = false;
    int i = 0;
    int j = i;
    while (i < k) {
      j += getPackedSize(paramArrayOfObject[i]);
      i++;
    } 
    FieldPacker fieldPacker = new FieldPacker(j);
    j = paramArrayOfObject.length;
    for (i = bool; i < j; i++)
      addToPack(fieldPacker, paramArrayOfObject[i]); 
    return fieldPacker;
  }
  
  static FieldPacker createFromArray(Object[] paramArrayOfObject) {
    FieldPacker fieldPacker = new FieldPacker(RenderScript.sPointerSize * 8);
    int j = paramArrayOfObject.length;
    for (int i = 0; i < j; i++)
      fieldPacker.addSafely(paramArrayOfObject[i]); 
    fieldPacker.resize(fieldPacker.mPos);
    return fieldPacker;
  }
  
  private static int getPackedSize(Object paramObject) {
    return (paramObject instanceof Boolean) ? 1 : ((paramObject instanceof Byte) ? 1 : ((paramObject instanceof Short) ? 2 : ((paramObject instanceof Integer) ? 4 : ((paramObject instanceof Long) ? 8 : ((paramObject instanceof Float) ? 4 : ((paramObject instanceof Double) ? 8 : ((paramObject instanceof Byte2) ? 2 : ((paramObject instanceof Byte3) ? 3 : ((paramObject instanceof Byte4) ? 4 : ((paramObject instanceof Short2) ? 4 : ((paramObject instanceof Short3) ? 6 : ((paramObject instanceof Short4) ? 8 : ((paramObject instanceof Int2) ? 8 : ((paramObject instanceof Int3) ? 12 : ((paramObject instanceof Int4) ? 16 : ((paramObject instanceof Long2) ? 16 : ((paramObject instanceof Long3) ? 24 : ((paramObject instanceof Long4) ? 32 : ((paramObject instanceof Float2) ? 8 : ((paramObject instanceof Float3) ? 12 : ((paramObject instanceof Float4) ? 16 : ((paramObject instanceof Double2) ? 16 : ((paramObject instanceof Double3) ? 24 : ((paramObject instanceof Double4) ? 32 : ((paramObject instanceof Matrix2f) ? 16 : ((paramObject instanceof Matrix3f) ? 36 : ((paramObject instanceof Matrix4f) ? 64 : ((paramObject instanceof BaseObj) ? ((RenderScript.sPointerSize == 8) ? 32 : 4) : 0))))))))))))))))))))))))))));
  }
  
  private boolean resize(int paramInt) {
    if (paramInt == this.mLen)
      return false; 
    byte[] arrayOfByte = new byte[paramInt];
    System.arraycopy(this.mData, 0, arrayOfByte, 0, this.mPos);
    this.mData = arrayOfByte;
    this.mLen = paramInt;
    return true;
  }
  
  public void addBoolean(boolean paramBoolean) {
    addI8((byte)paramBoolean);
  }
  
  public void addF32(float paramFloat) {
    addI32(Float.floatToRawIntBits(paramFloat));
  }
  
  public void addF32(Float2 paramFloat2) {
    addF32(paramFloat2.x);
    addF32(paramFloat2.y);
  }
  
  public void addF32(Float3 paramFloat3) {
    addF32(paramFloat3.x);
    addF32(paramFloat3.y);
    addF32(paramFloat3.z);
  }
  
  public void addF32(Float4 paramFloat4) {
    addF32(paramFloat4.x);
    addF32(paramFloat4.y);
    addF32(paramFloat4.z);
    addF32(paramFloat4.w);
  }
  
  public void addF64(double paramDouble) {
    addI64(Double.doubleToRawLongBits(paramDouble));
  }
  
  public void addF64(Double2 paramDouble2) {
    addF64(paramDouble2.x);
    addF64(paramDouble2.y);
  }
  
  public void addF64(Double3 paramDouble3) {
    addF64(paramDouble3.x);
    addF64(paramDouble3.y);
    addF64(paramDouble3.z);
  }
  
  public void addF64(Double4 paramDouble4) {
    addF64(paramDouble4.x);
    addF64(paramDouble4.y);
    addF64(paramDouble4.z);
    addF64(paramDouble4.w);
  }
  
  public void addI16(Short2 paramShort2) {
    addI16(paramShort2.x);
    addI16(paramShort2.y);
  }
  
  public void addI16(Short3 paramShort3) {
    addI16(paramShort3.x);
    addI16(paramShort3.y);
    addI16(paramShort3.z);
  }
  
  public void addI16(Short4 paramShort4) {
    addI16(paramShort4.x);
    addI16(paramShort4.y);
    addI16(paramShort4.z);
    addI16(paramShort4.w);
  }
  
  public void addI16(short paramShort) {
    align(2);
    byte[] arrayOfByte = this.mData;
    int i = this.mPos;
    int j = i + 1;
    this.mPos = j;
    arrayOfByte[i] = (byte)(paramShort & 0xFF);
    this.mPos = j + 1;
    arrayOfByte[j] = (byte)(paramShort >> 8);
  }
  
  public void addI32(int paramInt) {
    align(4);
    byte[] arrayOfByte = this.mData;
    int j = this.mPos;
    int i = j + 1;
    this.mPos = i;
    arrayOfByte[j] = (byte)(paramInt & 0xFF);
    j = i + 1;
    this.mPos = j;
    arrayOfByte[i] = (byte)(paramInt >> 8 & 0xFF);
    i = j + 1;
    this.mPos = i;
    arrayOfByte[j] = (byte)(paramInt >> 16 & 0xFF);
    this.mPos = i + 1;
    arrayOfByte[i] = (byte)(paramInt >> 24 & 0xFF);
  }
  
  public void addI32(Int2 paramInt2) {
    addI32(paramInt2.x);
    addI32(paramInt2.y);
  }
  
  public void addI32(Int3 paramInt3) {
    addI32(paramInt3.x);
    addI32(paramInt3.y);
    addI32(paramInt3.z);
  }
  
  public void addI32(Int4 paramInt4) {
    addI32(paramInt4.x);
    addI32(paramInt4.y);
    addI32(paramInt4.z);
    addI32(paramInt4.w);
  }
  
  public void addI64(long paramLong) {
    align(8);
    byte[] arrayOfByte = this.mData;
    int j = this.mPos;
    int i = j + 1;
    this.mPos = i;
    arrayOfByte[j] = (byte)(int)(paramLong & 0xFFL);
    j = i + 1;
    this.mPos = j;
    arrayOfByte[i] = (byte)(int)(paramLong >> 8L & 0xFFL);
    i = j + 1;
    this.mPos = i;
    arrayOfByte[j] = (byte)(int)(paramLong >> 16L & 0xFFL);
    j = i + 1;
    this.mPos = j;
    arrayOfByte[i] = (byte)(int)(paramLong >> 24L & 0xFFL);
    i = j + 1;
    this.mPos = i;
    arrayOfByte[j] = (byte)(int)(paramLong >> 32L & 0xFFL);
    j = i + 1;
    this.mPos = j;
    arrayOfByte[i] = (byte)(int)(paramLong >> 40L & 0xFFL);
    i = j + 1;
    this.mPos = i;
    arrayOfByte[j] = (byte)(int)(paramLong >> 48L & 0xFFL);
    this.mPos = i + 1;
    arrayOfByte[i] = (byte)(int)(paramLong >> 56L & 0xFFL);
  }
  
  public void addI64(Long2 paramLong2) {
    addI64(paramLong2.x);
    addI64(paramLong2.y);
  }
  
  public void addI64(Long3 paramLong3) {
    addI64(paramLong3.x);
    addI64(paramLong3.y);
    addI64(paramLong3.z);
  }
  
  public void addI64(Long4 paramLong4) {
    addI64(paramLong4.x);
    addI64(paramLong4.y);
    addI64(paramLong4.z);
    addI64(paramLong4.w);
  }
  
  public void addI8(byte paramByte) {
    byte[] arrayOfByte = this.mData;
    int i = this.mPos;
    this.mPos = i + 1;
    arrayOfByte[i] = paramByte;
  }
  
  public void addI8(Byte2 paramByte2) {
    addI8(paramByte2.x);
    addI8(paramByte2.y);
  }
  
  public void addI8(Byte3 paramByte3) {
    addI8(paramByte3.x);
    addI8(paramByte3.y);
    addI8(paramByte3.z);
  }
  
  public void addI8(Byte4 paramByte4) {
    addI8(paramByte4.x);
    addI8(paramByte4.y);
    addI8(paramByte4.z);
    addI8(paramByte4.w);
  }
  
  public void addMatrix(Matrix2f paramMatrix2f) {
    for (int i = 0; i < paramMatrix2f.mMat.length; i++)
      addF32(paramMatrix2f.mMat[i]); 
  }
  
  public void addMatrix(Matrix3f paramMatrix3f) {
    for (int i = 0; i < paramMatrix3f.mMat.length; i++)
      addF32(paramMatrix3f.mMat[i]); 
  }
  
  public void addMatrix(Matrix4f paramMatrix4f) {
    for (int i = 0; i < paramMatrix4f.mMat.length; i++)
      addF32(paramMatrix4f.mMat[i]); 
  }
  
  public void addObj(BaseObj paramBaseObj) {
    if (paramBaseObj != null) {
      if (RenderScript.sPointerSize == 8) {
        addI64(paramBaseObj.getID(null));
        addI64(0L);
        addI64(0L);
        addI64(0L);
        return;
      } 
      addI32((int)paramBaseObj.getID(null));
      return;
    } 
    if (RenderScript.sPointerSize == 8) {
      addI64(0L);
      addI64(0L);
      addI64(0L);
      addI64(0L);
      return;
    } 
    addI32(0);
  }
  
  public void addU16(int paramInt) {
    if (paramInt >= 0 && paramInt <= 65535) {
      align(2);
      byte[] arrayOfByte = this.mData;
      int i = this.mPos;
      int j = i + 1;
      this.mPos = j;
      arrayOfByte[i] = (byte)(paramInt & 0xFF);
      this.mPos = j + 1;
      arrayOfByte[j] = (byte)(paramInt >> 8);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("FieldPacker.addU16( ");
    stringBuilder.append(paramInt);
    stringBuilder.append(" )");
    Log.e("rs", stringBuilder.toString());
    throw new IllegalArgumentException("Saving value out of range for type");
  }
  
  public void addU16(Int2 paramInt2) {
    addU16(paramInt2.x);
    addU16(paramInt2.y);
  }
  
  public void addU16(Int3 paramInt3) {
    addU16(paramInt3.x);
    addU16(paramInt3.y);
    addU16(paramInt3.z);
  }
  
  public void addU16(Int4 paramInt4) {
    addU16(paramInt4.x);
    addU16(paramInt4.y);
    addU16(paramInt4.z);
    addU16(paramInt4.w);
  }
  
  public void addU32(long paramLong) {
    if (paramLong >= 0L && paramLong <= 4294967295L) {
      align(4);
      byte[] arrayOfByte = this.mData;
      int j = this.mPos;
      int i = j + 1;
      this.mPos = i;
      arrayOfByte[j] = (byte)(int)(paramLong & 0xFFL);
      j = i + 1;
      this.mPos = j;
      arrayOfByte[i] = (byte)(int)(paramLong >> 8L & 0xFFL);
      i = j + 1;
      this.mPos = i;
      arrayOfByte[j] = (byte)(int)(paramLong >> 16L & 0xFFL);
      this.mPos = i + 1;
      arrayOfByte[i] = (byte)(int)(paramLong >> 24L & 0xFFL);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("FieldPacker.addU32( ");
    stringBuilder.append(paramLong);
    stringBuilder.append(" )");
    Log.e("rs", stringBuilder.toString());
    throw new IllegalArgumentException("Saving value out of range for type");
  }
  
  public void addU32(Long2 paramLong2) {
    addU32(paramLong2.x);
    addU32(paramLong2.y);
  }
  
  public void addU32(Long3 paramLong3) {
    addU32(paramLong3.x);
    addU32(paramLong3.y);
    addU32(paramLong3.z);
  }
  
  public void addU32(Long4 paramLong4) {
    addU32(paramLong4.x);
    addU32(paramLong4.y);
    addU32(paramLong4.z);
    addU32(paramLong4.w);
  }
  
  public void addU64(long paramLong) {
    if (paramLong >= 0L) {
      align(8);
      byte[] arrayOfByte = this.mData;
      int j = this.mPos;
      int i = j + 1;
      this.mPos = i;
      arrayOfByte[j] = (byte)(int)(paramLong & 0xFFL);
      j = i + 1;
      this.mPos = j;
      arrayOfByte[i] = (byte)(int)(paramLong >> 8L & 0xFFL);
      i = j + 1;
      this.mPos = i;
      arrayOfByte[j] = (byte)(int)(paramLong >> 16L & 0xFFL);
      j = i + 1;
      this.mPos = j;
      arrayOfByte[i] = (byte)(int)(paramLong >> 24L & 0xFFL);
      i = j + 1;
      this.mPos = i;
      arrayOfByte[j] = (byte)(int)(paramLong >> 32L & 0xFFL);
      j = i + 1;
      this.mPos = j;
      arrayOfByte[i] = (byte)(int)(paramLong >> 40L & 0xFFL);
      i = j + 1;
      this.mPos = i;
      arrayOfByte[j] = (byte)(int)(paramLong >> 48L & 0xFFL);
      this.mPos = i + 1;
      arrayOfByte[i] = (byte)(int)(paramLong >> 56L & 0xFFL);
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("FieldPacker.addU64( ");
    stringBuilder.append(paramLong);
    stringBuilder.append(" )");
    Log.e("rs", stringBuilder.toString());
    throw new IllegalArgumentException("Saving value out of range for type");
  }
  
  public void addU64(Long2 paramLong2) {
    addU64(paramLong2.x);
    addU64(paramLong2.y);
  }
  
  public void addU64(Long3 paramLong3) {
    addU64(paramLong3.x);
    addU64(paramLong3.y);
    addU64(paramLong3.z);
  }
  
  public void addU64(Long4 paramLong4) {
    addU64(paramLong4.x);
    addU64(paramLong4.y);
    addU64(paramLong4.z);
    addU64(paramLong4.w);
  }
  
  public void addU8(Short2 paramShort2) {
    addU8(paramShort2.x);
    addU8(paramShort2.y);
  }
  
  public void addU8(Short3 paramShort3) {
    addU8(paramShort3.x);
    addU8(paramShort3.y);
    addU8(paramShort3.z);
  }
  
  public void addU8(Short4 paramShort4) {
    addU8(paramShort4.x);
    addU8(paramShort4.y);
    addU8(paramShort4.z);
    addU8(paramShort4.w);
  }
  
  public void addU8(short paramShort) {
    if (paramShort >= 0 && paramShort <= 255) {
      byte[] arrayOfByte = this.mData;
      int i = this.mPos;
      this.mPos = i + 1;
      arrayOfByte[i] = (byte)paramShort;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("FieldPacker.addU8( ");
    stringBuilder.append(paramShort);
    stringBuilder.append(" )");
    Log.e("rs", stringBuilder.toString());
    throw new IllegalArgumentException("Saving value out of range for type");
  }
  
  public void align(int paramInt) {
    if (paramInt > 0) {
      int i = paramInt - 1;
      if ((paramInt & i) == 0) {
        while (true) {
          paramInt = this.mPos;
          if ((paramInt & i) != 0) {
            this.mAlignment.flip(paramInt);
            byte[] arrayOfByte = this.mData;
            paramInt = this.mPos;
            this.mPos = paramInt + 1;
            arrayOfByte[paramInt] = 0;
            continue;
          } 
          break;
        } 
        return;
      } 
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("argument must be a non-negative non-zero power of 2: ");
    stringBuilder.append(paramInt);
    throw new RSIllegalArgumentException(stringBuilder.toString());
  }
  
  public final byte[] getData() {
    return this.mData;
  }
  
  public int getPos() {
    return this.mPos;
  }
  
  public void reset() {
    this.mPos = 0;
  }
  
  public void reset(int paramInt) {
    if (paramInt >= 0 && paramInt <= this.mLen) {
      this.mPos = paramInt;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("out of range argument: ");
    stringBuilder.append(paramInt);
    throw new RSIllegalArgumentException(stringBuilder.toString());
  }
  
  public void skip(int paramInt) {
    int i = this.mPos + paramInt;
    if (i >= 0 && i <= this.mLen) {
      this.mPos = i;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("out of range argument: ");
    stringBuilder.append(paramInt);
    throw new RSIllegalArgumentException(stringBuilder.toString());
  }
  
  public boolean subBoolean() {
    return (subI8() == 1);
  }
  
  public Byte2 subByte2() {
    Byte2 byte2 = new Byte2();
    byte2.y = subI8();
    byte2.x = subI8();
    return byte2;
  }
  
  public Byte3 subByte3() {
    Byte3 byte3 = new Byte3();
    byte3.z = subI8();
    byte3.y = subI8();
    byte3.x = subI8();
    return byte3;
  }
  
  public Byte4 subByte4() {
    Byte4 byte4 = new Byte4();
    byte4.w = subI8();
    byte4.z = subI8();
    byte4.y = subI8();
    byte4.x = subI8();
    return byte4;
  }
  
  public Double2 subDouble2() {
    Double2 double2 = new Double2();
    double2.y = subF64();
    double2.x = subF64();
    return double2;
  }
  
  public Double3 subDouble3() {
    Double3 double3 = new Double3();
    double3.z = subF64();
    double3.y = subF64();
    double3.x = subF64();
    return double3;
  }
  
  public Double4 subDouble4() {
    Double4 double4 = new Double4();
    double4.w = subF64();
    double4.z = subF64();
    double4.y = subF64();
    double4.x = subF64();
    return double4;
  }
  
  public float subF32() {
    return Float.intBitsToFloat(subI32());
  }
  
  public double subF64() {
    return Double.longBitsToDouble(subI64());
  }
  
  public Float2 subFloat2() {
    Float2 float2 = new Float2();
    float2.y = subF32();
    float2.x = subF32();
    return float2;
  }
  
  public Float3 subFloat3() {
    Float3 float3 = new Float3();
    float3.z = subF32();
    float3.y = subF32();
    float3.x = subF32();
    return float3;
  }
  
  public Float4 subFloat4() {
    Float4 float4 = new Float4();
    float4.w = subF32();
    float4.z = subF32();
    float4.y = subF32();
    float4.x = subF32();
    return float4;
  }
  
  public short subI16() {
    subalign(2);
    byte[] arrayOfByte = this.mData;
    int i = this.mPos - 1;
    this.mPos = i;
    short s = (short)((arrayOfByte[i] & 0xFF) << 8);
    this.mPos = --i;
    return (short)((short)(arrayOfByte[i] & 0xFF) | s);
  }
  
  public int subI32() {
    subalign(4);
    byte[] arrayOfByte = this.mData;
    int i = this.mPos - 1;
    this.mPos = i;
    byte b = arrayOfByte[i];
    int j = i - 1;
    this.mPos = j;
    i = arrayOfByte[j];
    int k = j - 1;
    this.mPos = k;
    j = arrayOfByte[k];
    this.mPos = --k;
    return arrayOfByte[k] & 0xFF | (b & 0xFF) << 24 | (i & 0xFF) << 16 | (j & 0xFF) << 8;
  }
  
  public long subI64() {
    subalign(8);
    byte[] arrayOfByte = this.mData;
    int i = this.mPos - 1;
    this.mPos = i;
    long l1 = arrayOfByte[i];
    this.mPos = --i;
    long l2 = arrayOfByte[i];
    this.mPos = --i;
    long l3 = arrayOfByte[i];
    this.mPos = --i;
    long l4 = arrayOfByte[i];
    this.mPos = --i;
    long l5 = arrayOfByte[i];
    this.mPos = --i;
    long l6 = arrayOfByte[i];
    this.mPos = --i;
    long l7 = arrayOfByte[i];
    this.mPos = --i;
    return arrayOfByte[i] & 0xFFL | (l1 & 0xFFL) << 56L | 0x0L | (l2 & 0xFFL) << 48L | (l3 & 0xFFL) << 40L | (l4 & 0xFFL) << 32L | (l5 & 0xFFL) << 24L | (l6 & 0xFFL) << 16L | (l7 & 0xFFL) << 8L;
  }
  
  public byte subI8() {
    subalign(1);
    byte[] arrayOfByte = this.mData;
    int i = this.mPos - 1;
    this.mPos = i;
    return arrayOfByte[i];
  }
  
  public Int2 subInt2() {
    Int2 int2 = new Int2();
    int2.y = subI32();
    int2.x = subI32();
    return int2;
  }
  
  public Int3 subInt3() {
    Int3 int3 = new Int3();
    int3.z = subI32();
    int3.y = subI32();
    int3.x = subI32();
    return int3;
  }
  
  public Int4 subInt4() {
    Int4 int4 = new Int4();
    int4.w = subI32();
    int4.z = subI32();
    int4.y = subI32();
    int4.x = subI32();
    return int4;
  }
  
  public Long2 subLong2() {
    Long2 long2 = new Long2();
    long2.y = subI64();
    long2.x = subI64();
    return long2;
  }
  
  public Long3 subLong3() {
    Long3 long3 = new Long3();
    long3.z = subI64();
    long3.y = subI64();
    long3.x = subI64();
    return long3;
  }
  
  public Long4 subLong4() {
    Long4 long4 = new Long4();
    long4.w = subI64();
    long4.z = subI64();
    long4.y = subI64();
    long4.x = subI64();
    return long4;
  }
  
  public Matrix2f subMatrix2f() {
    Matrix2f matrix2f = new Matrix2f();
    for (int i = matrix2f.mMat.length - 1; i >= 0; i--)
      matrix2f.mMat[i] = subF32(); 
    return matrix2f;
  }
  
  public Matrix3f subMatrix3f() {
    Matrix3f matrix3f = new Matrix3f();
    for (int i = matrix3f.mMat.length - 1; i >= 0; i--)
      matrix3f.mMat[i] = subF32(); 
    return matrix3f;
  }
  
  public Matrix4f subMatrix4f() {
    Matrix4f matrix4f = new Matrix4f();
    for (int i = matrix4f.mMat.length - 1; i >= 0; i--)
      matrix4f.mMat[i] = subF32(); 
    return matrix4f;
  }
  
  public Short2 subShort2() {
    Short2 short2 = new Short2();
    short2.y = subI16();
    short2.x = subI16();
    return short2;
  }
  
  public Short3 subShort3() {
    Short3 short3 = new Short3();
    short3.z = subI16();
    short3.y = subI16();
    short3.x = subI16();
    return short3;
  }
  
  public Short4 subShort4() {
    Short4 short4 = new Short4();
    short4.w = subI16();
    short4.z = subI16();
    short4.y = subI16();
    short4.x = subI16();
    return short4;
  }
  
  public void subalign(int paramInt) {
    int i = paramInt - 1;
    if ((paramInt & i) == 0)
      while (true) {
        paramInt = this.mPos;
        if ((paramInt & i) != 0) {
          this.mPos = paramInt - 1;
          continue;
        } 
        if (paramInt > 0)
          while (this.mAlignment.get(this.mPos - 1) == true) {
            paramInt = this.mPos - 1;
            this.mPos = paramInt;
            this.mAlignment.flip(paramInt);
          }  
        return;
      }  
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("argument must be a non-negative non-zero power of 2: ");
    stringBuilder.append(paramInt);
    throw new RSIllegalArgumentException(stringBuilder.toString());
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\renderscript\FieldPacker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
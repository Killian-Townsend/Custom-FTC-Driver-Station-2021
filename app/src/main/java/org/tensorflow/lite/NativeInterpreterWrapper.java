package org.tensorflow.lite;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

final class NativeInterpreterWrapper implements AutoCloseable {
  private static final int ERROR_BUFFER_SIZE = 512;
  
  private long errorHandle;
  
  private long inferenceDurationNanoseconds = -1L;
  
  private int inputSize;
  
  private Map<String, Integer> inputsIndexes;
  
  private long interpreterHandle;
  
  private boolean isMemoryAllocated = false;
  
  private ByteBuffer modelByteBuffer;
  
  private long modelHandle;
  
  private Map<String, Integer> outputsIndexes;
  
  static {
    TensorFlowLite.init();
  }
  
  NativeInterpreterWrapper(String paramString) {
    this(paramString, -1);
  }
  
  NativeInterpreterWrapper(String paramString, int paramInt) {
    long l = createErrorReporter(512);
    this.errorHandle = l;
    l = createModel(paramString, l);
    this.modelHandle = l;
    this.interpreterHandle = createInterpreter(l, this.errorHandle, paramInt);
    this.isMemoryAllocated = true;
  }
  
  NativeInterpreterWrapper(ByteBuffer paramByteBuffer) {
    this(paramByteBuffer, -1);
  }
  
  NativeInterpreterWrapper(ByteBuffer paramByteBuffer, int paramInt) {
    if (paramByteBuffer != null && (paramByteBuffer instanceof java.nio.MappedByteBuffer || (paramByteBuffer.isDirect() && paramByteBuffer.order() == ByteOrder.nativeOrder()))) {
      this.modelByteBuffer = paramByteBuffer;
      long l = createErrorReporter(512);
      this.errorHandle = l;
      l = createModelWithBuffer(this.modelByteBuffer, l);
      this.modelHandle = l;
      this.interpreterHandle = createInterpreter(l, this.errorHandle, paramInt);
      this.isMemoryAllocated = true;
      return;
    } 
    throw new IllegalArgumentException("Model ByteBuffer should be either a MappedByteBuffer of the model file, or a direct ByteBuffer using ByteOrder.nativeOrder() which contains bytes of model content.");
  }
  
  private static native long createErrorReporter(int paramInt);
  
  private static native long createInterpreter(long paramLong1, long paramLong2, int paramInt);
  
  private static native long createModel(String paramString, long paramLong);
  
  private static native long createModelWithBuffer(ByteBuffer paramByteBuffer, long paramLong);
  
  static DataType dataTypeOf(Object paramObject) {
    if (paramObject != null) {
      Class<?> clazz;
      for (clazz = paramObject.getClass(); clazz.isArray(); clazz = clazz.getComponentType());
      if (float.class.equals(clazz))
        return DataType.FLOAT32; 
      if (int.class.equals(clazz))
        return DataType.INT32; 
      if (byte.class.equals(clazz))
        return DataType.UINT8; 
      if (long.class.equals(clazz))
        return DataType.INT64; 
      if (ByteBuffer.class.isInstance(paramObject))
        return DataType.BYTEBUFFER; 
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("DataType error: cannot resolve DataType of ");
    stringBuilder.append(paramObject.getClass().getName());
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  private static native void delete(long paramLong1, long paramLong2, long paramLong3);
  
  static void fillShape(Object paramObject, int paramInt, int[] paramArrayOfint) {
    if (paramArrayOfint != null) {
      if (paramInt == paramArrayOfint.length)
        return; 
      int j = Array.getLength(paramObject);
      int k = paramArrayOfint[paramInt];
      int i = 0;
      if (k == 0) {
        paramArrayOfint[paramInt] = j;
      } else if (paramArrayOfint[paramInt] != j) {
        throw new IllegalArgumentException(String.format("Mismatched lengths (%d and %d) in dimension %d", new Object[] { Integer.valueOf(paramArrayOfint[paramInt]), Integer.valueOf(j), Integer.valueOf(paramInt) }));
      } 
      while (i < j) {
        fillShape(Array.get(paramObject, i), paramInt + 1, paramArrayOfint);
        i++;
      } 
      return;
    } 
  }
  
  private static native int[] getInputDims(long paramLong, int paramInt1, int paramInt2);
  
  private static native String[] getInputNames(long paramLong);
  
  private static native int getOutputDataType(long paramLong, int paramInt);
  
  private static native String[] getOutputNames(long paramLong);
  
  private static native float getOutputQuantizationScale(long paramLong, int paramInt);
  
  private static native int getOutputQuantizationZeroPoint(long paramLong, int paramInt);
  
  static boolean isNonEmptyArray(Object paramObject) {
    return (paramObject != null && paramObject.getClass().isArray() && Array.getLength(paramObject) != 0);
  }
  
  static int numDimensions(Object paramObject) {
    if (paramObject != null) {
      if (!paramObject.getClass().isArray())
        return 0; 
      if (Array.getLength(paramObject) != 0)
        return numDimensions(Array.get(paramObject, 0)) + 1; 
      throw new IllegalArgumentException("Array lengths cannot be 0.");
    } 
    return 0;
  }
  
  static int numElements(int[] paramArrayOfint) {
    int i = 0;
    if (paramArrayOfint == null)
      return 0; 
    int j = 1;
    while (i < paramArrayOfint.length) {
      j *= paramArrayOfint[i];
      i++;
    } 
    return j;
  }
  
  private static native void numThreads(long paramLong, int paramInt);
  
  private static native boolean resizeInput(long paramLong1, long paramLong2, int paramInt, int[] paramArrayOfint);
  
  private static native long[] run(long paramLong1, long paramLong2, Object[] paramArrayOfObject1, int[] paramArrayOfint1, int[] paramArrayOfint2, Object[] paramArrayOfObject2, NativeInterpreterWrapper paramNativeInterpreterWrapper, boolean paramBoolean);
  
  static int[] shapeOf(Object paramObject) {
    int[] arrayOfInt = new int[numDimensions(paramObject)];
    fillShape(paramObject, 0, arrayOfInt);
    return arrayOfInt;
  }
  
  private static native void useNNAPI(long paramLong, boolean paramBoolean);
  
  public void close() {
    delete(this.errorHandle, this.modelHandle, this.interpreterHandle);
    this.errorHandle = 0L;
    this.modelHandle = 0L;
    this.interpreterHandle = 0L;
    this.modelByteBuffer = null;
    this.inputsIndexes = null;
    this.outputsIndexes = null;
    this.isMemoryAllocated = false;
  }
  
  int[] getInputDims(int paramInt) {
    return getInputDims(this.interpreterHandle, paramInt, -1);
  }
  
  int getInputIndex(String paramString) {
    if (this.inputsIndexes == null) {
      String[] arrayOfString = getInputNames(this.interpreterHandle);
      this.inputsIndexes = new HashMap<String, Integer>();
      if (arrayOfString != null)
        for (int i = 0; i < arrayOfString.length; i++)
          this.inputsIndexes.put(arrayOfString[i], Integer.valueOf(i));  
    } 
    if (this.inputsIndexes.containsKey(paramString))
      return ((Integer)this.inputsIndexes.get(paramString)).intValue(); 
    throw new IllegalArgumentException(String.format("Input error: '%s' is not a valid name for any input. Names of inputs and their indexes are %s", new Object[] { paramString, this.inputsIndexes.toString() }));
  }
  
  Long getLastNativeInferenceDurationNanoseconds() {
    long l = this.inferenceDurationNanoseconds;
    return (l < 0L) ? null : Long.valueOf(l);
  }
  
  String getOutputDataType(int paramInt) {
    return DataType.fromNumber(getOutputDataType(this.interpreterHandle, paramInt)).toStringName();
  }
  
  int getOutputIndex(String paramString) {
    if (this.outputsIndexes == null) {
      String[] arrayOfString = getOutputNames(this.interpreterHandle);
      this.outputsIndexes = new HashMap<String, Integer>();
      if (arrayOfString != null)
        for (int i = 0; i < arrayOfString.length; i++)
          this.outputsIndexes.put(arrayOfString[i], Integer.valueOf(i));  
    } 
    if (this.outputsIndexes.containsKey(paramString))
      return ((Integer)this.outputsIndexes.get(paramString)).intValue(); 
    throw new IllegalArgumentException(String.format("Input error: '%s' is not a valid name for any output. Names of outputs and their indexes are %s", new Object[] { paramString, this.outputsIndexes.toString() }));
  }
  
  float getOutputQuantizationScale(int paramInt) {
    return getOutputQuantizationScale(this.interpreterHandle, paramInt);
  }
  
  int getOutputQuantizationZeroPoint(int paramInt) {
    return getOutputQuantizationZeroPoint(this.interpreterHandle, paramInt);
  }
  
  void resizeInput(int paramInt, int[] paramArrayOfint) {
    if (resizeInput(this.interpreterHandle, this.errorHandle, paramInt, paramArrayOfint))
      this.isMemoryAllocated = false; 
  }
  
  Tensor[] run(Object[] paramArrayOfObject) {
    if (paramArrayOfObject != null && paramArrayOfObject.length != 0) {
      int[] arrayOfInt1 = new int[paramArrayOfObject.length];
      Object[] arrayOfObject = new Object[paramArrayOfObject.length];
      int[] arrayOfInt2 = new int[paramArrayOfObject.length];
      boolean bool = false;
      int i;
      for (i = 0; i < paramArrayOfObject.length; i++) {
        ByteBuffer byteBuffer;
        DataType dataType = dataTypeOf(paramArrayOfObject[i]);
        arrayOfInt1[i] = dataType.getNumber();
        if (dataType == DataType.BYTEBUFFER) {
          byteBuffer = (ByteBuffer)paramArrayOfObject[i];
          if (byteBuffer != null && byteBuffer.isDirect() && byteBuffer.order() == ByteOrder.nativeOrder()) {
            arrayOfInt2[i] = byteBuffer.limit();
            arrayOfObject[i] = getInputDims(this.interpreterHandle, i, arrayOfInt2[i]);
          } else {
            throw new IllegalArgumentException("Input error: ByteBuffer should be a direct ByteBuffer that uses ByteOrder.nativeOrder().");
          } 
        } else if (isNonEmptyArray(paramArrayOfObject[i])) {
          int[] arrayOfInt = shapeOf(paramArrayOfObject[i]);
          arrayOfObject[i] = arrayOfInt;
          arrayOfInt2[i] = byteBuffer.elemByteSize() * numElements(arrayOfInt);
        } else {
          throw new IllegalArgumentException(String.format("Input error: %d-th element of the %d inputs is not an array or a ByteBuffer.", new Object[] { Integer.valueOf(i), Integer.valueOf(paramArrayOfObject.length) }));
        } 
      } 
      this.inferenceDurationNanoseconds = -1L;
      long[] arrayOfLong = run(this.interpreterHandle, this.errorHandle, arrayOfObject, arrayOfInt1, arrayOfInt2, paramArrayOfObject, this, this.isMemoryAllocated);
      if (arrayOfLong != null && arrayOfLong.length != 0) {
        this.isMemoryAllocated = true;
        Tensor[] arrayOfTensor = new Tensor[arrayOfLong.length];
        for (i = bool; i < arrayOfLong.length; i++)
          arrayOfTensor[i] = Tensor.fromHandle(arrayOfLong[i]); 
        return arrayOfTensor;
      } 
      throw new IllegalStateException("Internal error: Interpreter has no outputs.");
    } 
    throw new IllegalArgumentException("Input error: Inputs should not be null or empty.");
  }
  
  void setNumThreads(int paramInt) {
    numThreads(this.interpreterHandle, paramInt);
  }
  
  void setUseNNAPI(boolean paramBoolean) {
    useNNAPI(this.interpreterHandle, paramBoolean);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\tensorflow\lite\NativeInterpreterWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
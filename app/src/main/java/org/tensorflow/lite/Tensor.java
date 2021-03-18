package org.tensorflow.lite;

import java.util.Arrays;

final class Tensor {
  final DataType dtype;
  
  final long nativeHandle;
  
  final int[] shapeCopy;
  
  static {
    TensorFlowLite.init();
  }
  
  private Tensor(long paramLong) {
    this.nativeHandle = paramLong;
    this.dtype = DataType.fromNumber(dtype(paramLong));
    this.shapeCopy = shape(paramLong);
  }
  
  private static native int dtype(long paramLong);
  
  static Tensor fromHandle(long paramLong) {
    return new Tensor(paramLong);
  }
  
  private static native void readMultiDimensionalArray(long paramLong, Object paramObject);
  
  private static native int[] shape(long paramLong);
  
  <T> T copyTo(T paramT) {
    if (NativeInterpreterWrapper.dataTypeOf(paramT) == this.dtype) {
      int[] arrayOfInt = NativeInterpreterWrapper.shapeOf(paramT);
      if (Arrays.equals(arrayOfInt, this.shapeCopy)) {
        readMultiDimensionalArray(this.nativeHandle, paramT);
        return paramT;
      } 
      throw new IllegalArgumentException(String.format("Output error: Shape of output target %s does not match with the shape of the Tensor %s.", new Object[] { Arrays.toString(arrayOfInt), Arrays.toString(this.shapeCopy) }));
    } 
    throw new IllegalArgumentException(String.format("Output error: Cannot convert an TensorFlowLite tensor with type %s to a Java object of type %s (which is compatible with the TensorFlowLite type %s)", new Object[] { this.dtype, paramT.getClass().getName(), NativeInterpreterWrapper.dataTypeOf(paramT) }));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\tensorflow\lite\Tensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.tensorflow.lite;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.util.HashMap;
import java.util.Map;

public final class Interpreter implements AutoCloseable {
  NativeInterpreterWrapper wrapper;
  
  public Interpreter(File paramFile) {
    if (paramFile == null)
      return; 
    this.wrapper = new NativeInterpreterWrapper(paramFile.getAbsolutePath());
  }
  
  public Interpreter(File paramFile, int paramInt) {
    if (paramFile == null)
      return; 
    this.wrapper = new NativeInterpreterWrapper(paramFile.getAbsolutePath(), paramInt);
  }
  
  public Interpreter(ByteBuffer paramByteBuffer) {
    this.wrapper = new NativeInterpreterWrapper(paramByteBuffer);
  }
  
  public Interpreter(ByteBuffer paramByteBuffer, int paramInt) {
    this.wrapper = new NativeInterpreterWrapper(paramByteBuffer, paramInt);
  }
  
  public Interpreter(MappedByteBuffer paramMappedByteBuffer) {
    this.wrapper = new NativeInterpreterWrapper(paramMappedByteBuffer);
  }
  
  public Interpreter(MappedByteBuffer paramMappedByteBuffer, int paramInt) {
    this.wrapper = new NativeInterpreterWrapper(paramMappedByteBuffer, paramInt);
  }
  
  public void close() {
    this.wrapper.close();
    this.wrapper = null;
  }
  
  protected void finalize() throws Throwable {
    try {
      close();
      return;
    } finally {
      super.finalize();
    } 
  }
  
  public int getInputIndex(String paramString) {
    NativeInterpreterWrapper nativeInterpreterWrapper = this.wrapper;
    if (nativeInterpreterWrapper != null)
      return nativeInterpreterWrapper.getInputIndex(paramString); 
    throw new IllegalStateException("Internal error: The Interpreter has already been closed.");
  }
  
  public Long getLastNativeInferenceDurationNanoseconds() {
    NativeInterpreterWrapper nativeInterpreterWrapper = this.wrapper;
    if (nativeInterpreterWrapper != null)
      return nativeInterpreterWrapper.getLastNativeInferenceDurationNanoseconds(); 
    throw new IllegalStateException("Internal error: The interpreter has already been closed.");
  }
  
  public int getOutputIndex(String paramString) {
    NativeInterpreterWrapper nativeInterpreterWrapper = this.wrapper;
    if (nativeInterpreterWrapper != null)
      return nativeInterpreterWrapper.getOutputIndex(paramString); 
    throw new IllegalStateException("Internal error: The Interpreter has already been closed.");
  }
  
  public void resizeInput(int paramInt, int[] paramArrayOfint) {
    NativeInterpreterWrapper nativeInterpreterWrapper = this.wrapper;
    if (nativeInterpreterWrapper != null) {
      nativeInterpreterWrapper.resizeInput(paramInt, paramArrayOfint);
      return;
    } 
    throw new IllegalStateException("Internal error: The Interpreter has already been closed.");
  }
  
  public void run(Object paramObject1, Object paramObject2) {
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    hashMap.put(Integer.valueOf(0), paramObject2);
    runForMultipleInputsOutputs(new Object[] { paramObject1 }, (Map)hashMap);
  }
  
  public void runForMultipleInputsOutputs(Object[] paramArrayOfObject, Map<Integer, Object> paramMap) {
    NativeInterpreterWrapper nativeInterpreterWrapper = this.wrapper;
    if (nativeInterpreterWrapper != null) {
      Tensor[] arrayOfTensor = nativeInterpreterWrapper.run(paramArrayOfObject);
      if (paramMap != null && arrayOfTensor != null && paramMap.size() <= arrayOfTensor.length) {
        int i = arrayOfTensor.length;
        for (Integer integer : paramMap.keySet()) {
          if (integer != null && integer.intValue() >= 0 && integer.intValue() < i) {
            arrayOfTensor[integer.intValue()].copyTo(paramMap.get(integer));
            continue;
          } 
          throw new IllegalArgumentException(String.format("Output error: Invalid index of output %d (should be in range [0, %d))", new Object[] { integer, Integer.valueOf(i) }));
        } 
        return;
      } 
      throw new IllegalArgumentException("Output error: Outputs do not match with model outputs.");
    } 
    throw new IllegalStateException("Internal error: The Interpreter has already been closed.");
  }
  
  public void setNumThreads(int paramInt) {
    NativeInterpreterWrapper nativeInterpreterWrapper = this.wrapper;
    if (nativeInterpreterWrapper != null) {
      nativeInterpreterWrapper.setNumThreads(paramInt);
      return;
    } 
    throw new IllegalStateException("The interpreter has already been closed.");
  }
  
  public void setUseNNAPI(boolean paramBoolean) {
    NativeInterpreterWrapper nativeInterpreterWrapper = this.wrapper;
    if (nativeInterpreterWrapper != null) {
      nativeInterpreterWrapper.setUseNNAPI(paramBoolean);
      return;
    } 
    throw new IllegalStateException("Internal error: NativeInterpreterWrapper has already been closed.");
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\tensorflow\lite\Interpreter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
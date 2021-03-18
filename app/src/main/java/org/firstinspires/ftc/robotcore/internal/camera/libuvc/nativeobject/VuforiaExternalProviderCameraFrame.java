package org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject;

import org.firstinspires.ftc.robotcore.internal.system.NativeObject;
import org.firstinspires.ftc.robotcore.internal.system.RefCounted;

public class VuforiaExternalProviderCameraFrame extends NativeObject {
  protected static final int[] fieldOffsets = nativeGetFieldOffsets((Fields.values()).length);
  
  public VuforiaExternalProviderCameraFrame() {
    super(RefCounted.TraceLevel.None);
    allocateMemory(getStructSize());
  }
  
  protected static native int[] nativeGetFieldOffsets(int paramInt);
  
  protected int cbIntrinsics() {
    return getStructSize() - Fields.intrinsics.offset();
  }
  
  public long getBuffer() {
    return getPointer(Fields.buffer.offset());
  }
  
  public int getBufferSize() {
    return getInt(Fields.bufferSize.offset());
  }
  
  public long getExposureTime() {
    return getLong(Fields.exposureTime.offset());
  }
  
  public int getFormat() {
    return getInt(Fields.format.offset());
  }
  
  public int getFrameIndex() {
    return getInt(Fields.index.offset());
  }
  
  public int getHeight() {
    return getInt(Fields.height.offset());
  }
  
  public long getPointer() {
    return this.pointer;
  }
  
  public int getStride() {
    return getInt(Fields.stride.offset());
  }
  
  protected int getStructSize() {
    return fieldOffsets[Fields.sizeof.ordinal()];
  }
  
  public long getTimestamp() {
    return getLong(Fields.timestamp.offset());
  }
  
  public int getWidth() {
    return getInt(Fields.width.offset());
  }
  
  public void setBuffer(long paramLong) {
    setPointer(Fields.buffer.offset(), paramLong);
  }
  
  public void setBufferSize(int paramInt) {
    setInt(Fields.bufferSize.offset(), paramInt);
  }
  
  public void setExposureTime(long paramLong) {
    setLong(Fields.exposureTime.offset(), paramLong);
  }
  
  public void setFormat(int paramInt) {
    setInt(Fields.format.offset(), paramInt);
  }
  
  public void setFrameIndex(int paramInt) {
    setInt(Fields.index.offset(), paramInt);
  }
  
  public void setHeight(int paramInt) {
    setInt(Fields.height.offset(), paramInt);
  }
  
  public void setStride(int paramInt) {
    setInt(Fields.stride.offset(), paramInt);
  }
  
  public void setTimestamp(long paramLong) {
    setLong(Fields.timestamp.offset(), paramLong);
  }
  
  public void setWidth(int paramInt) {
    setInt(Fields.width.offset(), paramInt);
  }
  
  protected enum Fields {
    buffer, bufferSize, exposureTime, format, height, index, intrinsics, sizeof, stride, timestamp, width;
    
    static {
      buffer = new Fields("buffer", 3);
      bufferSize = new Fields("bufferSize", 4);
      index = new Fields("index", 5);
      width = new Fields("width", 6);
      height = new Fields("height", 7);
      stride = new Fields("stride", 8);
      format = new Fields("format", 9);
      Fields fields = new Fields("intrinsics", 10);
      intrinsics = fields;
      $VALUES = new Fields[] { 
          sizeof, timestamp, exposureTime, buffer, bufferSize, index, width, height, stride, format, 
          fields };
    }
    
    public int offset() {
      return VuforiaExternalProviderCameraFrame.fieldOffsets[ordinal()];
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\nativeobject\VuforiaExternalProviderCameraFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
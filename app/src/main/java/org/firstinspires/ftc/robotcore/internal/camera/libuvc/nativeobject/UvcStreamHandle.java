package org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject;

import java.io.IOException;
import org.firstinspires.ftc.robotcore.external.Consumer;
import org.firstinspires.ftc.robotcore.internal.system.NativeObject;
import org.firstinspires.ftc.robotcore.internal.system.RefCounted;

public class UvcStreamHandle extends NativeObject<UvcDeviceHandle> {
  public static final String TAG = UvcStreamHandle.class.getSimpleName();
  
  protected UvcFrameCallback frameCallback = null;
  
  public UvcStreamHandle(long paramLong, UvcDeviceHandle paramUvcDeviceHandle) {
    super(paramLong);
    setParent((RefCounted)paramUvcDeviceHandle);
  }
  
  protected static native void nativeCloseStreamHandle(long paramLong);
  
  protected static native boolean nativeIsStreaming(long paramLong);
  
  protected static native boolean nativeStartStreaming(long paramLong1, long paramLong2);
  
  protected static native void nativeStopStreaming(long paramLong);
  
  protected void destructor() {
    if (this.pointer != 0L) {
      stopStreaming();
      nativeCloseStreamHandle(this.pointer);
      clearPointer();
    } 
    releaseFrameCallback();
    super.destructor();
  }
  
  public String getTag() {
    return TAG;
  }
  
  public UvcContext getUvcContext() {
    return ((UvcDeviceHandle)getParent()).getUvcContext();
  }
  
  public boolean isStreaming() {
    return nativeIsStreaming(this.pointer);
  }
  
  protected void releaseFrameCallback() {
    UvcFrameCallback uvcFrameCallback = this.frameCallback;
    if (uvcFrameCallback != null) {
      uvcFrameCallback.releaseRef();
      this.frameCallback = null;
    } 
  }
  
  public void startStreaming(Consumer<UvcFrame> paramConsumer) throws IOException {
    if (paramConsumer != null)
      synchronized (this.lock) {
        stopStreaming();
        this.frameCallback = new UvcFrameCallback(getUvcContext(), paramConsumer);
        boolean bool = nativeStartStreaming(this.pointer, this.frameCallback.getCallbackPointer());
        if (bool)
          return; 
        throw new IOException("unable to start streaming");
      }  
    throw new IllegalArgumentException("callback must not be null");
  }
  
  public void stopStreaming() {
    synchronized (this.lock) {
      nativeStopStreaming(this.pointer);
      releaseFrameCallback();
      return;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\nativeobject\UvcStreamHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
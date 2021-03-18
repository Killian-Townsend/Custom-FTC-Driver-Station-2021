package org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject;

import org.firstinspires.ftc.robotcore.external.Consumer;
import org.firstinspires.ftc.robotcore.internal.system.DestructOnFinalize;
import org.firstinspires.ftc.robotcore.internal.system.NativeObject;

class UvcFrameCallback extends DestructOnFinalize {
  protected final Consumer<UvcFrame> callback;
  
  protected UvcFrameCallbackData callbackData;
  
  protected UvcContext uvcContext;
  
  public UvcFrameCallback(UvcContext paramUvcContext, Consumer<UvcFrame> paramConsumer) {
    this.callback = paramConsumer;
    this.callbackData = new UvcFrameCallbackData(this);
    this.uvcContext = paramUvcContext;
    paramUvcContext.addRef();
  }
  
  protected static native long nativeAllocCallbackState(UvcFrameCallback paramUvcFrameCallback);
  
  protected static native void nativeReleaseCallbackState(long paramLong);
  
  protected void destructor() {
    UvcFrameCallbackData uvcFrameCallbackData = this.callbackData;
    if (uvcFrameCallbackData != null) {
      uvcFrameCallbackData.releaseRef();
      this.callbackData = null;
    } 
    UvcContext uvcContext = this.uvcContext;
    if (uvcContext != null) {
      uvcContext.releaseRef();
      this.uvcContext = null;
    } 
    super.destructor();
  }
  
  public long getCallbackPointer() {
    return this.callbackData.getPointer();
  }
  
  public void onFrame(long paramLong) {
    addRef();
    try {
      UvcCallbackFrame uvcCallbackFrame = new UvcCallbackFrame(paramLong, this.uvcContext);
    } finally {
      releaseRef();
    } 
  }
  
  protected static class UvcFrameCallbackData extends NativeObject {
    public UvcFrameCallbackData(UvcFrameCallback param1UvcFrameCallback) {
      super(allocate(param1UvcFrameCallback));
    }
    
    protected static long allocate(UvcFrameCallback param1UvcFrameCallback) {
      long l = UvcFrameCallback.nativeAllocCallbackState(param1UvcFrameCallback);
      if (0L != l)
        return l; 
      throw new IllegalStateException("unable to allocate streaming callback");
    }
    
    protected void destructor() {
      if (this.pointer != 0L) {
        UvcFrameCallback.nativeReleaseCallbackState(this.pointer);
        clearPointer();
      } 
      super.destructor();
    }
    
    public long getPointer() {
      return this.pointer;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\nativeobject\UvcFrameCallback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
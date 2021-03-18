package org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject;

import org.firstinspires.ftc.robotcore.internal.system.NativeObject;

public class UvcCallbackFrame extends UvcFrame {
  public UvcCallbackFrame(long paramLong, UvcContext paramUvcContext) {
    super(paramLong, NativeObject.MemoryAllocator.UNKNOWN, paramUvcContext);
  }
  
  public void callbackComplete() {
    clearPointer();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\nativeobject\UvcCallbackFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
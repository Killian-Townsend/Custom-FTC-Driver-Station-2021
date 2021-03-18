package org.firstinspires.ftc.robotcore.internal.camera.libuvc.api;

import android.graphics.Bitmap;
import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureRequest;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSequenceId;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraFrame;
import org.firstinspires.ftc.robotcore.internal.camera.CameraFrameInternal;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.constants.UvcFrameFormat;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject.UvcFrame;
import org.firstinspires.ftc.robotcore.internal.system.DestructOnFinalize;
import org.firstinspires.ftc.robotcore.internal.system.RefCounted;

public class UvcApiCameraFrame extends DestructOnFinalize implements CameraFrame, CameraFrameInternal {
  protected UvcApiCameraCaptureSequence captureSequence;
  
  protected UvcFrame uvcFrame;
  
  public UvcApiCameraFrame(UvcApiCameraCaptureSequence paramUvcApiCameraCaptureSequence, UvcFrame paramUvcFrame, boolean paramBoolean) {
    super(RefCounted.TraceLevel.VeryVerbose);
    this.captureSequence = paramUvcApiCameraCaptureSequence;
    if (paramBoolean) {
      this.uvcFrame = paramUvcFrame.copy();
      return;
    } 
    this.uvcFrame = paramUvcFrame;
    paramUvcFrame.addRef();
  }
  
  public CameraFrame copy() {
    return new UvcApiCameraFrame(this.captureSequence, this.uvcFrame, true);
  }
  
  public void copyToBitmap(Bitmap paramBitmap) {
    this.uvcFrame.copyToBitmap(paramBitmap);
  }
  
  protected void destructor() {
    UvcFrame uvcFrame = this.uvcFrame;
    if (uvcFrame != null) {
      uvcFrame.releaseRef();
      this.uvcFrame = null;
    } 
    super.destructor();
  }
  
  public CameraCaptureSequenceId getCaptureSequenceId() {
    return (CameraCaptureSequenceId)this.captureSequence.uvcCaptureSequenceId;
  }
  
  public long getCaptureTime() {
    return this.uvcFrame.getCaptureTime();
  }
  
  public long getFrameNumber() {
    return this.uvcFrame.getFrameNumber();
  }
  
  public long getImageBuffer() {
    return this.uvcFrame.getImageBuffer();
  }
  
  public int getImageSize() {
    return this.uvcFrame.getImageSize();
  }
  
  public long getPointer() {
    return this.uvcFrame.getPointer();
  }
  
  public CameraCaptureRequest getRequest() {
    return (CameraCaptureRequest)this.captureSequence.uvcCameraCaptureRequest;
  }
  
  public Size getSize() {
    return getRequest().getSize();
  }
  
  public int getStride() {
    return this.uvcFrame.getStride();
  }
  
  public UvcApiCameraFrame getUvcApiCameraFrame() {
    return this;
  }
  
  public UvcFrameFormat getUvcFrameFormat() {
    return this.uvcFrame.getFrameFormat();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\api\UvcApiCameraFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
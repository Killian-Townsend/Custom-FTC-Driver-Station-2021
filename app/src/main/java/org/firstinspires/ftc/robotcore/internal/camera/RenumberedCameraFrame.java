package org.firstinspires.ftc.robotcore.internal.camera;

import android.graphics.Bitmap;
import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureRequest;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSequenceId;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraFrame;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.api.UvcApiCameraFrame;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.constants.UvcFrameFormat;
import org.firstinspires.ftc.robotcore.internal.system.RefCounted;

public class RenumberedCameraFrame extends RefCounted implements CameraFrame, CameraFrameInternal {
  public static final String TAG = "RenumberedCameraFrame";
  
  protected final CameraCaptureRequest captureRequest;
  
  protected final CameraCaptureSequenceId captureSequenceId;
  
  protected final long frameNumber;
  
  protected final CameraFrame innerFrame;
  
  public RenumberedCameraFrame(CameraCaptureRequest paramCameraCaptureRequest, CameraCaptureSequenceId paramCameraCaptureSequenceId, CameraFrame paramCameraFrame, long paramLong) {
    super(RefCounted.TraceLevel.VeryVerbose);
    this.captureRequest = paramCameraCaptureRequest;
    this.captureSequenceId = paramCameraCaptureSequenceId;
    this.innerFrame = paramCameraFrame;
    this.frameNumber = paramLong;
    paramCameraFrame.addRef();
  }
  
  public CameraFrame copy() {
    CameraFrame cameraFrame = this.innerFrame.copy();
    RenumberedCameraFrame renumberedCameraFrame = new RenumberedCameraFrame(this.captureRequest, this.captureSequenceId, cameraFrame, this.frameNumber);
    cameraFrame.releaseRef();
    return renumberedCameraFrame;
  }
  
  public void copyToBitmap(Bitmap paramBitmap) {
    this.innerFrame.copyToBitmap(paramBitmap);
  }
  
  protected void destructor() {
    this.innerFrame.releaseRef();
    super.destructor();
  }
  
  public CameraCaptureSequenceId getCaptureSequenceId() {
    return this.captureSequenceId;
  }
  
  public long getCaptureTime() {
    return this.innerFrame.getCaptureTime();
  }
  
  public long getFrameNumber() {
    return this.frameNumber;
  }
  
  public long getImageBuffer() {
    return this.innerFrame.getImageBuffer();
  }
  
  public int getImageSize() {
    return this.innerFrame.getImageSize();
  }
  
  public CameraCaptureRequest getRequest() {
    return this.captureRequest;
  }
  
  public Size getSize() {
    return this.innerFrame.getSize();
  }
  
  public int getStride() {
    return this.innerFrame.getStride();
  }
  
  public String getTag() {
    return "RenumberedCameraFrame";
  }
  
  public UvcApiCameraFrame getUvcApiCameraFrame() {
    CameraFrame cameraFrame = this.innerFrame;
    return (cameraFrame instanceof CameraFrameInternal) ? ((CameraFrameInternal)cameraFrame).getUvcApiCameraFrame() : null;
  }
  
  public UvcFrameFormat getUvcFrameFormat() {
    return this.innerFrame.getUvcFrameFormat();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\RenumberedCameraFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
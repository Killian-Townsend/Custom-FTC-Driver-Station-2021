package com.vuforia.ar.pl;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.IOException;

class CameraSurface extends SurfaceView implements SurfaceHolder.Callback {
  private static final String MODULENAME = "CameraSurface";
  
  Camera camera = null;
  
  SurfaceHolder surfaceHolder;
  
  public CameraSurface(Context paramContext) {
    super(paramContext);
    SurfaceHolder surfaceHolder = getHolder();
    this.surfaceHolder = surfaceHolder;
    surfaceHolder.addCallback(this);
    this.surfaceHolder.setType(3);
  }
  
  public void setCamera(Camera paramCamera) {
    this.camera = paramCamera;
  }
  
  public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3) {}
  
  public void surfaceCreated(SurfaceHolder paramSurfaceHolder) {
    try {
      if (this.camera != null) {
        this.camera.setPreviewDisplay(paramSurfaceHolder);
        return;
      } 
    } catch (IOException iOException) {
      this.camera = null;
    } 
  }
  
  public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder) {
    this.camera = null;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\ar\pl\CameraSurface.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
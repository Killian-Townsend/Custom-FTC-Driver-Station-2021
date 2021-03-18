package com.vuforia.ar.pl;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SurfaceManager {
  private static final String MODULENAME = "SurfaceManager";
  
  Lock addSurfaceLock = new ReentrantLock();
  
  View cameraSurfaceParentView = null;
  
  Camera1_Preview.CameraCacheInfo cciForSurface;
  
  GLSurfaceView glSurfaceView = null;
  
  int glSurfaceViewChildPosition = 0;
  
  boolean renderWhenDirtyEnabled = false;
  
  Lock viewLock = new ReentrantLock();
  
  private boolean applyRenderWhenDirty() {
    GLSurfaceView gLSurfaceView = this.glSurfaceView;
    if (gLSurfaceView != null) {
      gLSurfaceView.setRenderMode(this.renderWhenDirtyEnabled ^ true);
      return true;
    } 
    return false;
  }
  
  private GLSurfaceView searchForGLSurfaceView(View paramView) {
    int i = 0;
    this.glSurfaceViewChildPosition = 0;
    try {
      GLSurfaceView gLSurfaceView;
      ViewGroup viewGroup = (ViewGroup)paramView;
      int j = viewGroup.getChildCount();
      paramView = null;
      while (i < j) {
        View view = viewGroup.getChildAt(i);
        if (view instanceof GLSurfaceView) {
          gLSurfaceView = (GLSurfaceView)view;
          this.glSurfaceViewChildPosition = i;
          return gLSurfaceView;
        } 
        if (view instanceof ViewGroup) {
          GLSurfaceView gLSurfaceView1 = searchForGLSurfaceView(view);
          gLSurfaceView = gLSurfaceView1;
          if (gLSurfaceView1 != null)
            return gLSurfaceView1; 
        } 
        i++;
      } 
      return gLSurfaceView;
    } catch (Exception exception) {
      return null;
    } 
  }
  
  private void setupCameraSurface(Camera1_Preview.CameraCacheInfo paramCameraCacheInfo) {
    if (paramCameraCacheInfo.surface == null) {
      Activity activity = SystemTools.getActivityFromNative();
      if (activity != null) {
        paramCameraCacheInfo.surface = new CameraSurface((Context)activity);
      } else {
        return;
      } 
    } else if (paramCameraCacheInfo.surface.getParent() != null && ViewGroup.class.isInstance(paramCameraCacheInfo.surface.getParent())) {
      ((ViewGroup)paramCameraCacheInfo.surface.getParent()).removeView((View)paramCameraCacheInfo.surface);
    } 
    paramCameraCacheInfo.surface.setCamera(paramCameraCacheInfo.camera);
  }
  
  public boolean addCameraSurface(Camera1_Preview.CameraCacheInfo paramCameraCacheInfo) {
    Activity activity = SystemTools.getActivityFromNative();
    boolean bool = false;
    if (activity == null)
      return false; 
    this.cciForSurface = paramCameraCacheInfo;
    this.viewLock.lock();
    try {
      activity.runOnUiThread(new Runnable() {
            public void run() {
              SurfaceManager.this.addSurfaceLock.lock();
              SurfaceManager.this.retrieveGLSurfaceView();
              try {
                SurfaceManager.this.setupCameraSurface(SurfaceManager.this.cciForSurface);
                ((ViewGroup)SurfaceManager.this.cameraSurfaceParentView).addView((View)SurfaceManager.this.cciForSurface.surface, SurfaceManager.this.glSurfaceViewChildPosition + 1, (ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-1, -1));
                SurfaceManager.this.cciForSurface.surface.setVisibility(0);
              } catch (Exception exception) {
              
              } finally {
                SurfaceManager.this.addSurfaceLock.unlock();
              } 
            }
          });
    } catch (Exception exception) {
      this.viewLock.unlock();
    } finally {
      this.viewLock.unlock();
    } 
    return true ^ bool;
  }
  
  public void requestRender() {
    GLSurfaceView gLSurfaceView = this.glSurfaceView;
    if (gLSurfaceView != null)
      gLSurfaceView.requestRender(); 
  }
  
  public boolean retrieveGLSurfaceView() {
    boolean bool = false;
    try {
      Activity activity = SystemTools.getActivityFromNative();
      if (activity == null)
        return false; 
      View view = activity.getWindow().getDecorView();
      GLSurfaceView gLSurfaceView = searchForGLSurfaceView(view);
      this.glSurfaceView = gLSurfaceView;
      if (gLSurfaceView == null) {
        this.cameraSurfaceParentView = view;
      } else {
        this.cameraSurfaceParentView = (View)gLSurfaceView.getParent();
      } 
      if (this.glSurfaceView != null)
        bool = true; 
      return bool;
    } catch (Exception exception) {
      return false;
    } 
  }
  
  public boolean setEnableRenderWhenDirty(boolean paramBoolean) {
    this.renderWhenDirtyEnabled = paramBoolean;
    return applyRenderWhenDirty();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\ar\pl\SurfaceManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
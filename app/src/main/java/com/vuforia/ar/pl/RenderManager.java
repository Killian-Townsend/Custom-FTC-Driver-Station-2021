package com.vuforia.ar.pl;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class RenderManager {
  private static final int AR_RENDERING_MODE_CONTINUOUS = 2;
  
  private static final int AR_RENDERING_MODE_DISABLED = 1;
  
  private static final int AR_RENDERING_MODE_UNKNOWN = 0;
  
  private static final int AR_RENDERING_MODE_WHENDIRTY = 3;
  
  private static final String MODULENAME = "RenderManager";
  
  private static int viewId;
  
  long delayMS = 0L;
  
  ScheduledFuture<?> fixedFrameRateRunnerTask;
  
  long maxMS = 0L;
  
  long minMS = 0L;
  
  int renderMode;
  
  AtomicBoolean renderRequestServiced;
  
  ScheduledFuture<?> renderRequestWatcherTask;
  
  AtomicBoolean renderRequested;
  
  SurfaceManager surfaceManager;
  
  boolean synchronousMode;
  
  ScheduledThreadPoolExecutor timer;
  
  public RenderManager(SurfaceManager paramSurfaceManager) {
    this.surfaceManager = paramSurfaceManager;
    this.renderMode = 2;
    this.timer = new ScheduledThreadPoolExecutor(1);
    this.synchronousMode = false;
    this.renderRequestServiced = new AtomicBoolean(false);
    this.renderRequested = new AtomicBoolean(false);
  }
  
  public View addOverlay(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, float[] paramArrayOffloat, int[] paramArrayOfint) {
    final Activity activity = SystemTools.getActivityFromNative();
    if (activity == null) {
      DebugLog.LOGE("RenderManager", "drawOverlay could not get access to an activity");
      return null;
    } 
    final DrawOverlayView wm = new DrawOverlayView((Context)activity, paramArrayOfbyte, paramInt1, paramInt2, paramArrayOffloat, paramArrayOfint);
    activity.runOnUiThread(new Runnable() {
          public void run() {
            wm.addOverlay(activity);
          }
        });
    return drawOverlayView;
  }
  
  public boolean canSetRenderMode() {
    boolean bool = this.surfaceManager.retrieveGLSurfaceView();
    if (!bool)
      DebugLog.LOGD("RenderManager", "Could not retrieve a valid GLSurfaceView in view hierarchy, therefore cannot set any render mode"); 
    return bool;
  }
  
  public int getRenderMode() {
    return this.renderMode;
  }
  
  public boolean removeOverlay(final View view) {
    final Activity activity = SystemTools.getActivityFromNative();
    if (activity == null)
      return false; 
    if (view == null)
      return false; 
    activity.runOnUiThread(new Runnable() {
          public void run() {
            (new DrawOverlayView((Context)activity)).removeOverlay(activity, view);
          }
        });
    return true;
  }
  
  public boolean requestRender() {
    this.renderRequested.set(true);
    return true;
  }
  
  public boolean setRenderFpsLimits(boolean paramBoolean, int paramInt1, int paramInt2) {
    long l1;
    this.synchronousMode = paramBoolean;
    if (paramInt1 == 0 || paramInt2 == 0) {
      SystemTools.setSystemErrorCode(2);
      return false;
    } 
    long l2 = 1L;
    if (paramInt1 > 1000) {
      l1 = 1L;
    } else {
      l1 = 1000L / paramInt1;
    } 
    this.minMS = l1;
    if (paramInt2 > 1000) {
      l1 = l2;
    } else {
      l1 = 1000L / paramInt2;
    } 
    this.maxMS = l1;
    if (this.renderMode == 3) {
      if (this.synchronousMode)
        l1 = this.minMS; 
      if (l1 != this.delayMS) {
        this.delayMS = l1;
        startTimer();
      } 
    } 
    return true;
  }
  
  public boolean setRenderMode(int paramInt) {
    SurfaceManager surfaceManager = this.surfaceManager;
    if (surfaceManager == null) {
      SystemTools.setSystemErrorCode(6);
      return false;
    } 
    surfaceManager.retrieveGLSurfaceView();
    if (paramInt != 1)
      if (paramInt != 2) {
        if (paramInt != 3) {
          SystemTools.setSystemErrorCode(2);
          return false;
        } 
      } else {
        boolean bool2 = this.surfaceManager.setEnableRenderWhenDirty(false);
        boolean bool1 = bool2;
        if (bool2) {
          shutdownTimer();
          bool1 = bool2;
        } 
        if (!bool1) {
          SystemTools.setSystemErrorCode(6);
          return bool1;
        } 
      }  
    boolean bool = this.surfaceManager.setEnableRenderWhenDirty(true);
    if (bool)
      if (paramInt == 1) {
        shutdownTimer();
      } else if (paramInt != this.renderMode || this.timer.isShutdown()) {
        long l;
        if (this.synchronousMode) {
          l = this.minMS;
        } else {
          l = this.maxMS;
        } 
        if (l != 0L) {
          this.delayMS = l;
          startTimer();
        } 
      }  
    if (!bool) {
      SystemTools.setSystemErrorCode(6);
      return bool;
    } 
  }
  
  void shutdownTimer() {
    if (!this.timer.isShutdown())
      this.timer.shutdown(); 
  }
  
  void startTimer() {
    if (this.timer.isShutdown())
      this.timer = new ScheduledThreadPoolExecutor(1); 
    ScheduledFuture<?> scheduledFuture = this.fixedFrameRateRunnerTask;
    if (scheduledFuture != null && !scheduledFuture.isCancelled())
      this.fixedFrameRateRunnerTask.cancel(true); 
    scheduledFuture = this.renderRequestWatcherTask;
    if (scheduledFuture != null && !scheduledFuture.isCancelled())
      this.renderRequestWatcherTask.cancel(true); 
    this.fixedFrameRateRunnerTask = null;
    this.renderRequestWatcherTask = null;
    long l = this.delayMS;
    if (l < 4L) {
      l = 1L;
    } else {
      l /= 4L;
    } 
    this.renderRequestWatcherTask = this.timer.scheduleWithFixedDelay(new RenderRequestWatcher(), 0L, l, TimeUnit.MILLISECONDS);
  }
  
  private final class FixedFrameRateRunner implements Runnable {
    private FixedFrameRateRunner() {}
    
    public void run() {
      if (!RenderManager.this.renderRequestServiced.getAndSet(false) && RenderManager.this.surfaceManager != null) {
        RenderManager.this.surfaceManager.requestRender();
        if (!RenderManager.this.synchronousMode && !RenderManager.this.renderRequestWatcherTask.isCancelled())
          RenderManager.this.renderRequestWatcherTask.cancel(false); 
      } 
    }
  }
  
  private final class RenderRequestWatcher implements Runnable {
    private RenderRequestWatcher() {}
    
    public void run() {
      if (RenderManager.this.renderRequested.compareAndSet(true, false) && RenderManager.this.surfaceManager != null) {
        RenderManager.this.surfaceManager.requestRender();
        RenderManager.this.renderRequestServiced.set(true);
        if (RenderManager.this.fixedFrameRateRunnerTask == null) {
          RenderManager renderManager = RenderManager.this;
          renderManager.fixedFrameRateRunnerTask = renderManager.timer.scheduleAtFixedRate(new RenderManager.FixedFrameRateRunner(), 0L, RenderManager.this.delayMS, TimeUnit.MILLISECONDS);
        } 
      } 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\ar\pl\RenderManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
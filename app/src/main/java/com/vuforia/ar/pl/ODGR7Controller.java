package com.vuforia.ar.pl;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.osterhoutgroup.api.ext.ExtendDisplay;
import java.util.ArrayList;
import java.util.List;

public class ODGR7Controller {
  private static final String MODULENAME = "ODGR7Controller";
  
  private boolean stereoEnabled = false;
  
  public ODGR7Controller() throws ClassNotFoundException {
    Class.forName("com.osterhoutgroup.api.ext.ExtendDisplay");
  }
  
  private List<SurfaceView> findSurfaceViews(Window paramWindow) {
    ArrayList<SurfaceView> arrayList = new ArrayList();
    findSurfaceViews((ViewGroup)paramWindow.getDecorView(), arrayList);
    return arrayList;
  }
  
  private void findSurfaceViews(ViewGroup paramViewGroup, List<SurfaceView> paramList) {
    int j = paramViewGroup.getChildCount();
    for (int i = 0; i < j; i++) {
      View view = paramViewGroup.getChildAt(i);
      if (view instanceof SurfaceView) {
        paramList.add((SurfaceView)view);
      } else if (view instanceof ViewGroup) {
        findSurfaceViews((ViewGroup)view, paramList);
      } 
    } 
  }
  
  private void logMetrics(String paramString, Activity paramActivity, Window paramWindow) {
    DisplayMetrics displayMetrics = new DisplayMetrics();
    ExtendDisplay.getDisplayMetrics((Context)paramActivity, paramWindow, displayMetrics);
    int i = displayMetrics.widthPixels;
    int j = displayMetrics.heightPixels;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString);
    stringBuilder.append(" display metrics ");
    stringBuilder.append(i);
    stringBuilder.append(" x ");
    stringBuilder.append(j);
    DebugLog.LOGD("ODGR7Controller", stringBuilder.toString());
  }
  
  private void setStereoSurfaces(Window paramWindow, boolean paramBoolean) {
    for (SurfaceView surfaceView : findSurfaceViews(paramWindow)) {
      if (surfaceView.getHolder().getSurface().isValid())
        ExtendDisplay.extendSurface(surfaceView, paramBoolean); 
    } 
  }
  
  public boolean getStereo() {
    return this.stereoEnabled;
  }
  
  public boolean setStereo(final boolean enable) {
    Activity activity = SystemTools.getActivityFromNative();
    if (activity != null) {
      final Window window = activity.getWindow();
      if (window != null) {
        if (findSurfaceViews(window).size() == 0) {
          DebugLog.LOGE("ODGR7Controller", "ODG Display control: Cannot change display mode, there are no SurfaceViews created.");
          return false;
        } 
        activity.runOnUiThread(new Runnable() {
              public void run() {
                ExtendDisplay.extendWindow(window, enable);
                ODGR7Controller.this.setStereoSurfaces(window, enable);
                ODGR7Controller.access$102(ODGR7Controller.this, enable);
              }
            });
        return true;
      } 
    } 
    return false;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\ar\pl\ODGR7Controller.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
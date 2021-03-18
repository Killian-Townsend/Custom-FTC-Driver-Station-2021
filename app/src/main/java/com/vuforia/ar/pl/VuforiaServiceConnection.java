package com.vuforia.ar.pl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import java.util.concurrent.CountDownLatch;

public class VuforiaServiceConnection implements ServiceConnection {
  private static final String SUBTAG = "VuforiaConn";
  
  private IBinder mService;
  
  private CountDownLatch mServiceLatch = null;
  
  public IBinder awaitService() {
    CountDownLatch countDownLatch = this.mServiceLatch;
    if (countDownLatch == null) {
      DebugLog.LOGE("VuforiaConn", "ERROR: awaitService called before bind()");
      return null;
    } 
    try {
      countDownLatch.await();
      return this.mService;
    } catch (InterruptedException interruptedException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("bind failed to complete");
      stringBuilder.append(interruptedException);
      DebugLog.LOGD("VuforiaConn", stringBuilder.toString());
      this.mServiceLatch = null;
      return null;
    } 
  }
  
  public boolean bindService(Context paramContext, ComponentName paramComponentName) {
    boolean bool;
    this.mServiceLatch = new CountDownLatch(1);
    Intent intent = new Intent();
    intent.setComponent(paramComponentName);
    try {
      bool = paramContext.bindService(intent, this, 1);
    } catch (SecurityException securityException) {
      paramContext.unbindService(this);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Not permitted to bind to service: ");
      stringBuilder.append(paramComponentName);
      stringBuilder.append(securityException);
      DebugLog.LOGD("VuforiaConn", stringBuilder.toString());
      bool = false;
    } 
    if (!bool) {
      paramContext.unbindService(this);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Bind to service ");
      stringBuilder.append(paramComponentName);
      stringBuilder.append(" failed");
      DebugLog.LOGD("VuforiaConn", stringBuilder.toString());
      this.mServiceLatch = null;
    } 
    return bool;
  }
  
  public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder) {
    this.mService = paramIBinder;
    this.mServiceLatch.countDown();
  }
  
  public void onServiceDisconnected(ComponentName paramComponentName) {
    this.mService = null;
    this.mServiceLatch = null;
  }
  
  public boolean unbindService(Context paramContext) {
    if (this.mService != null) {
      paramContext.unbindService(this);
      this.mService = null;
    } 
    return true;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\ar\pl\VuforiaServiceConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
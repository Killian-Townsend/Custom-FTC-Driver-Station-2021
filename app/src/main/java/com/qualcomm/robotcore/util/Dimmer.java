package com.qualcomm.robotcore.util;

import android.app.Activity;
import android.os.Handler;
import android.view.WindowManager;

public class Dimmer {
  public static final int DEFAULT_DIM_TIME = 30000;
  
  public static final int LONG_BRIGHT_TIME = 60000;
  
  public static final float MAXIMUM_BRIGHTNESS = 1.0F;
  
  public static final float MINIMUM_BRIGHTNESS = 0.05F;
  
  Activity activity;
  
  Handler handler = new Handler();
  
  final WindowManager.LayoutParams layoutParams;
  
  float userBrightness = 1.0F;
  
  long waitTime;
  
  public Dimmer(long paramLong, Activity paramActivity) {
    this.waitTime = paramLong;
    this.activity = paramActivity;
    WindowManager.LayoutParams layoutParams = paramActivity.getWindow().getAttributes();
    this.layoutParams = layoutParams;
    this.userBrightness = layoutParams.screenBrightness;
  }
  
  public Dimmer(Activity paramActivity) {
    this(30000L, paramActivity);
  }
  
  private float percentageDim() {
    float f = this.userBrightness * 0.05F;
    return (f < 0.05F) ? 0.05F : f;
  }
  
  private void sendToUIThread(float paramFloat) {
    this.layoutParams.screenBrightness = paramFloat;
    this.activity.runOnUiThread(new Runnable() {
          public void run() {
            Dimmer.this.activity.getWindow().setAttributes(Dimmer.this.layoutParams);
          }
        });
  }
  
  public void handleDimTimer() {
    sendToUIThread(this.userBrightness);
    this.handler.removeCallbacks(null);
    this.handler.postDelayed(new Runnable() {
          public void run() {
            Dimmer dimmer = Dimmer.this;
            dimmer.sendToUIThread(dimmer.percentageDim());
          }
        },  this.waitTime);
  }
  
  public void longBright() {
    sendToUIThread(this.userBrightness);
    Runnable runnable = new Runnable() {
        public void run() {
          Dimmer dimmer = Dimmer.this;
          dimmer.sendToUIThread(dimmer.percentageDim());
        }
      };
    this.handler.removeCallbacksAndMessages(null);
    this.handler.postDelayed(runnable, 60000L);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcor\\util\Dimmer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
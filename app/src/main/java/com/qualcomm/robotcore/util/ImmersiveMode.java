package com.qualcomm.robotcore.util;

import android.view.View;

public class ImmersiveMode {
  View decorView;
  
  public ImmersiveMode(View paramView) {
    this.decorView = paramView;
  }
  
  public void hideSystemUI() {
    this.decorView.setSystemUiVisibility(5894);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcor\\util\ImmersiveMode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
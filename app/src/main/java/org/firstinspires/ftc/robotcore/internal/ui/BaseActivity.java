package org.firstinspires.ftc.robotcore.internal.ui;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.util.Device;

public abstract class BaseActivity extends Activity {
  private void hideBackBar() {
    FrameLayout frameLayout = getBackBar();
    if (frameLayout != null) {
      ViewGroup.LayoutParams layoutParams = frameLayout.getLayoutParams();
      layoutParams.height = 0;
      frameLayout.setLayoutParams(layoutParams);
    } 
  }
  
  private void setupBackButton() {
    if (getBackBar() != null)
      ((ImageButton)findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
              BaseActivity.this.onBackPressed();
            }
          }); 
  }
  
  protected FrameLayout getBackBar() {
    return null;
  }
  
  public abstract String getTag();
  
  public void setContentView(int paramInt) {
    super.setContentView(paramInt);
    if (Device.deviceHasBackButton()) {
      hideBackBar();
      return;
    } 
    setupBackButton();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\interna\\ui\BaseActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
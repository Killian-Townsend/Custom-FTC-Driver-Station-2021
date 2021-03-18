package androidx.core.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;

public interface TintableCompoundButton {
  ColorStateList getSupportButtonTintList();
  
  PorterDuff.Mode getSupportButtonTintMode();
  
  void setSupportButtonTintList(ColorStateList paramColorStateList);
  
  void setSupportButtonTintMode(PorterDuff.Mode paramMode);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\core\widget\TintableCompoundButton.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
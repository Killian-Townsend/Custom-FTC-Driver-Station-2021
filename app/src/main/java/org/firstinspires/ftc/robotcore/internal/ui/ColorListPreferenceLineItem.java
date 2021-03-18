package org.firstinspires.ftc.robotcore.internal.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

public class ColorListPreferenceLineItem extends LinearLayout implements Checkable {
  public ColorListPreferenceLineItem(Context paramContext) {
    this(paramContext, null);
  }
  
  public ColorListPreferenceLineItem(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public ColorListPreferenceLineItem(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  protected CheckedTextView getCheckedTextView() {
    return (CheckedTextView)findViewById(16908308);
  }
  
  public boolean isChecked() {
    return getCheckedTextView().isChecked();
  }
  
  public void setChecked(boolean paramBoolean) {
    getCheckedTextView().setChecked(paramBoolean);
  }
  
  public void toggle() {
    getCheckedTextView().toggle();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\interna\\ui\ColorListPreferenceLineItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.qualcomm.ftcdriverstation;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.KeyEvent;

public class KeyEventCapturingProgressDialog extends ProgressDialog {
  Listener listener;
  
  public KeyEventCapturingProgressDialog(Context paramContext) {
    super(paramContext);
  }
  
  public KeyEventCapturingProgressDialog(Context paramContext, int paramInt) {
    super(paramContext, paramInt);
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    Listener listener = this.listener;
    if (listener != null)
      listener.onKeyDown(paramKeyEvent); 
    return true;
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent) {
    return true;
  }
  
  public void setListener(Listener paramListener) {
    this.listener = paramListener;
  }
  
  static interface Listener {
    void onKeyDown(KeyEvent param1KeyEvent);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftcdriverstation\KeyEventCapturingProgressDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.qualcomm.ftcdriverstation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class ManualKeyInDialog extends AlertDialog.Builder {
  Button doneBtn;
  
  EditText input;
  
  Listener listener;
  
  String title;
  
  public ManualKeyInDialog(Context paramContext, String paramString, Listener paramListener) {
    super(paramContext);
    this.title = paramString;
    this.listener = paramListener;
  }
  
  public static void setSoftInputMode(Context paramContext, int paramInt) {
    ((InputMethodManager)paramContext.getSystemService("input_method")).toggleSoftInput(paramInt, 0);
  }
  
  public AlertDialog show() {
    setTitle(this.title);
    setCancelable(false);
    View view = create().getLayoutInflater().inflate(2131427387, null, false);
    this.input = (EditText)view.findViewById(2131230938);
    setView(view);
    setPositiveButton(17039370, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            param1DialogInterface.dismiss();
            ManualKeyInDialog.this.listener.onInput(ManualKeyInDialog.this.input.getText().toString());
            ManualKeyInDialog.setSoftInputMode(ManualKeyInDialog.this.getContext(), 1);
          }
        });
    setNegativeButton(17039360, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            param1DialogInterface.cancel();
            ManualKeyInDialog.setSoftInputMode(ManualKeyInDialog.this.getContext(), 1);
          }
        });
    AlertDialog alertDialog = super.show();
    this.doneBtn = alertDialog.getButton(-1);
    setSoftInputMode(getContext(), 2);
    return alertDialog;
  }
  
  public static abstract class Listener {
    public abstract void onInput(String param1String);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftcdriverstation\ManualKeyInDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
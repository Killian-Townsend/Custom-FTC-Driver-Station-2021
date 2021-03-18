package com.qualcomm.ftcdriverstation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import com.qualcomm.robotcore.hardware.Gamepad;

public class SelectGamepadMappingDialog extends AlertDialog.Builder {
  private ArrayAdapter<CharSequence> fieldTypeAdapter;
  
  private Spinner fieldTypeSpinner;
  
  private Listener listener;
  
  public SelectGamepadMappingDialog(Context paramContext) {
    super(paramContext);
  }
  
  private void setupTypeSpinner() {
    this.fieldTypeAdapter = new ArrayAdapter(getContext(), 17367048);
    for (Gamepad.Type type : Gamepad.Type.values())
      this.fieldTypeAdapter.add(type.toString()); 
    this.fieldTypeAdapter.setDropDownViewResource(17367049);
    this.fieldTypeSpinner.setAdapter((SpinnerAdapter)this.fieldTypeAdapter);
  }
  
  public void setListener(Listener paramListener) {
    this.listener = paramListener;
  }
  
  public AlertDialog show() {
    setTitle("Choose Mapping");
    LayoutInflater layoutInflater = create().getLayoutInflater();
    FrameLayout frameLayout = new FrameLayout(getContext());
    setView((View)frameLayout);
    layoutInflater.inflate(2131427389, (ViewGroup)frameLayout);
    this.fieldTypeSpinner = (Spinner)frameLayout.findViewById(2131230915);
    setupTypeSpinner();
    setPositiveButton("OK", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            if (SelectGamepadMappingDialog.this.listener != null)
              SelectGamepadMappingDialog.this.listener.onOk(Gamepad.Type.valueOf((String)SelectGamepadMappingDialog.this.fieldTypeSpinner.getSelectedItem())); 
          }
        });
    setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {}
        });
    return super.show();
  }
  
  static interface Listener {
    void onOk(Gamepad.Type param1Type);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftcdriverstation\SelectGamepadMappingDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
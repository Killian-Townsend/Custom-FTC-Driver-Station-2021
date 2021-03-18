package com.qualcomm.ftcdriverstation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;

public class OpModeSelectionDialogFragment extends DialogFragment {
  private OpModeSelectionDialogListener listener = null;
  
  private List<OpModeMeta> opModes = new LinkedList<OpModeMeta>();
  
  private int title = 0;
  
  public Dialog onCreateDialog(Bundle paramBundle) {
    View view = LayoutInflater.from((Context)getActivity()).inflate(2131427420, null);
    ((TextView)view.findViewById(2131231013)).setText(this.title);
    AlertDialog.Builder builder = new AlertDialog.Builder((Context)getActivity());
    builder.setCustomTitle(view);
    ArrayAdapter<OpModeMeta> arrayAdapter = new ArrayAdapter<OpModeMeta>((Context)getActivity(), 2131427419, 2131231011, this.opModes) {
        public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
          param1View = super.getView(param1Int, param1View, param1ViewGroup);
          ImageView imageView = (ImageView)param1View.findViewById(2131231012);
          if (param1Int < OpModeSelectionDialogFragment.this.opModes.size() - 1 && !(OpModeSelectionDialogFragment.this.opModes.get(param1Int)).group.equals((OpModeSelectionDialogFragment.this.opModes.get(param1Int + 1)).group)) {
            param1Int = 0;
          } else {
            param1Int = 8;
          } 
          imageView.setVisibility(param1Int);
          return param1View;
        }
      };
    builder.setTitle(this.title);
    builder.setAdapter((ListAdapter)arrayAdapter, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            if (OpModeSelectionDialogFragment.this.listener != null)
              OpModeSelectionDialogFragment.this.listener.onOpModeSelectionClick(OpModeSelectionDialogFragment.this.opModes.get(param1Int)); 
          }
        });
    return (Dialog)builder.create();
  }
  
  public void onStart() {
    super.onStart();
    Dialog dialog = getDialog();
    dialog.findViewById(dialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null)).setBackground(dialog.findViewById(2131231014).getBackground());
  }
  
  public void setOnSelectionDialogListener(OpModeSelectionDialogListener paramOpModeSelectionDialogListener) {
    this.listener = paramOpModeSelectionDialogListener;
  }
  
  public void setOpModes(List<OpModeMeta> paramList) {
    paramList = new LinkedList<OpModeMeta>(paramList);
    this.opModes = paramList;
    Collections.sort(paramList, new Comparator<OpModeMeta>() {
          public int compare(OpModeMeta param1OpModeMeta1, OpModeMeta param1OpModeMeta2) {
            int j = param1OpModeMeta1.group.compareTo(param1OpModeMeta2.group);
            int i = j;
            if (j == 0)
              i = param1OpModeMeta1.name.compareTo(param1OpModeMeta2.name); 
            return i;
          }
        });
  }
  
  public void setTitle(int paramInt) {
    this.title = paramInt;
  }
  
  public static interface OpModeSelectionDialogListener {
    void onOpModeSelectionClick(OpModeMeta param1OpModeMeta);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftcdriverstation\OpModeSelectionDialogFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
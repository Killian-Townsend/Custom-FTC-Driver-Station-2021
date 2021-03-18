package org.firstinspires.ftc.robotcore.internal.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.qualcomm.robotcore.R;
import java.util.ArrayList;
import java.util.List;

public class ColorListPreference extends ListPreference {
  protected int clickedDialogEntryIndex;
  
  protected int[] colors;
  
  public ColorListPreference(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public ColorListPreference(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ColorListPreference, 0, 0);
    try {
      int i = typedArray.getResourceId(R.styleable.ColorListPreference_colors, 0);
      this.colors = paramContext.getResources().getIntArray(i);
      return;
    } finally {
      typedArray.recycle();
    } 
  }
  
  private int getValueIndex() {
    return findIndexOfValue(getValue());
  }
  
  protected void onDialogClosed(boolean paramBoolean) {
    if (paramBoolean && this.clickedDialogEntryIndex >= 0 && getEntryValues() != null) {
      String str = getEntryValues()[this.clickedDialogEntryIndex].toString();
      if (callChangeListener(str))
        setValue(str); 
    } 
  }
  
  protected void onPrepareDialogBuilder(AlertDialog.Builder paramBuilder) {
    if (getEntries() != null && getEntryValues() != null && this.colors != null) {
      ArrayList<Pair> arrayList = new ArrayList();
      final int swatchRes;
      for (i = 0; i < (getEntries()).length; i++)
        arrayList.add(new Pair(getEntries()[i], Integer.valueOf(this.colors[i]))); 
      this.clickedDialogEntryIndex = getValueIndex();
      i = R.id.colorSwatch;
      final int layoutRes = R.layout.color_list_preference_line_item;
      paramBuilder.setSingleChoiceItems((ListAdapter)new ArrayAdapter<Pair<CharSequence, Integer>>(getContext(), j, 16908308, arrayList) {
            public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
              View view = param1View;
              if (param1View == null)
                view = LayoutInflater.from(getContext()).inflate(layoutRes, param1ViewGroup, false); 
              Pair pair = (Pair)getItem(param1Int);
              ((TextView)view.findViewById(16908308)).setText((CharSequence)pair.first);
              param1Int = swatchRes;
              if (param1Int != 0)
                ((ImageView)view.findViewById(param1Int)).setImageDrawable((Drawable)new ColorDrawable(((Integer)pair.second).intValue())); 
              return view;
            }
          }this.clickedDialogEntryIndex, new DialogClickListener());
      paramBuilder.setPositiveButton(null, null);
      return;
    } 
    throw new IllegalStateException("ColorListPreference: entries, values, and colors required");
  }
  
  protected class DialogClickListener implements DialogInterface.OnClickListener {
    public void onClick(DialogInterface param1DialogInterface, int param1Int) {
      ColorListPreference.this.clickedDialogEntryIndex = param1Int;
      ColorListPreference.this.onClick(param1DialogInterface, -1);
      param1DialogInterface.dismiss();
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\interna\\ui\ColorListPreference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.qualcomm.ftccommon.configuration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ConfigurationTypeArrayAdapter extends ArrayAdapter<EditActivity.ConfigurationTypeAndDisplayName> {
  public ConfigurationTypeArrayAdapter(Context paramContext, EditActivity.ConfigurationTypeAndDisplayName[] paramArrayOfConfigurationTypeAndDisplayName) {
    super(paramContext, 17367049, (Object[])paramArrayOfConfigurationTypeAndDisplayName);
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
    TextView textView2 = (TextView)paramView;
    TextView textView1 = textView2;
    if (textView2 == null)
      textView1 = (TextView)LayoutInflater.from(getContext()).inflate(17367049, paramViewGroup, false); 
    EditActivity.ConfigurationTypeAndDisplayName configurationTypeAndDisplayName = (EditActivity.ConfigurationTypeAndDisplayName)getItem(paramInt);
    if (configurationTypeAndDisplayName.configurationType.isDeprecated())
      textView1.setPaintFlags(textView1.getPaintFlags() | 0x10); 
    textView1.setText(configurationTypeAndDisplayName.displayName);
    return (View)textView1;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\ConfigurationTypeArrayAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
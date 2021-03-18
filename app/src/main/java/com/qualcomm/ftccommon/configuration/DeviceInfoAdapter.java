package com.qualcomm.ftccommon.configuration;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import java.util.LinkedList;
import java.util.List;

public class DeviceInfoAdapter extends BaseAdapter implements ListAdapter {
  private List<ControllerConfiguration> deviceControllers = new LinkedList<ControllerConfiguration>();
  
  private EditActivity editActivity;
  
  private int list_id;
  
  public DeviceInfoAdapter(EditActivity paramEditActivity, int paramInt, List<ControllerConfiguration> paramList) {
    this.editActivity = paramEditActivity;
    this.deviceControllers = paramList;
    this.list_id = paramInt;
  }
  
  public int getCount() {
    return this.deviceControllers.size();
  }
  
  public Object getItem(int paramInt) {
    return this.deviceControllers.get(paramInt);
  }
  
  public long getItemId(int paramInt) {
    return 0L;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
    View view = paramView;
    if (paramView == null)
      view = this.editActivity.getLayoutInflater().inflate(this.list_id, paramViewGroup, false); 
    ControllerConfiguration controllerConfiguration = this.deviceControllers.get(paramInt);
    String str = EditActivity.formatSerialNumber((Context)this.editActivity, controllerConfiguration);
    ((TextView)view.findViewById(16908309)).setText(str);
    str = ((ControllerConfiguration)this.deviceControllers.get(paramInt)).getName();
    ((TextView)view.findViewById(16908308)).setText(str);
    return view;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\DeviceInfoAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
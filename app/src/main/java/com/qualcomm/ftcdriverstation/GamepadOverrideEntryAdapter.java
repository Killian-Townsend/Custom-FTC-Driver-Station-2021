package com.qualcomm.ftcdriverstation;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import java.util.List;

public class GamepadOverrideEntryAdapter extends BaseAdapter implements ListAdapter {
  private Activity activity;
  
  private List<GamepadTypeOverrideMapper.GamepadTypeOverrideEntry> gamepadOverrideEntries;
  
  private int list_id;
  
  public GamepadOverrideEntryAdapter(Activity paramActivity, int paramInt, List<GamepadTypeOverrideMapper.GamepadTypeOverrideEntry> paramList) {
    this.activity = paramActivity;
    this.gamepadOverrideEntries = paramList;
    this.list_id = paramInt;
  }
  
  public int getCount() {
    return this.gamepadOverrideEntries.size();
  }
  
  public Object getItem(int paramInt) {
    return this.gamepadOverrideEntries.get(paramInt);
  }
  
  public long getItemId(int paramInt) {
    return 0L;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
    View view = paramView;
    if (paramView == null)
      view = this.activity.getLayoutInflater().inflate(this.list_id, paramViewGroup, false); 
    GamepadTypeOverrideMapper.GamepadTypeOverrideEntry gamepadTypeOverrideEntry = this.gamepadOverrideEntries.get(paramInt);
    ((TextView)view.findViewById(16908309)).setText(String.format("Mapped as %s", new Object[] { gamepadTypeOverrideEntry.mappedType.toString() }));
    ((TextView)view.findViewById(16908308)).setText(String.format("VID: %d, PID: %d", new Object[] { Integer.valueOf(gamepadTypeOverrideEntry.vid), Integer.valueOf(gamepadTypeOverrideEntry.pid) }));
    return view;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftcdriverstation\GamepadOverrideEntryAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
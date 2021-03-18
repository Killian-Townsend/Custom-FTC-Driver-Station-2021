package org.firstinspires.ftc.robotcore.internal.network;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.qualcomm.robotcore.R;

public class WifiMuteFragment extends Fragment {
  TextView description;
  
  TextView nofication;
  
  WifiMuteStateMachine stateMachine;
  
  TextView timer;
  
  public void displayDisabledMessage() {
    getActivity().runOnUiThread(new Runnable() {
          public void run() {
            WifiMuteFragment.this.description.setVisibility(8);
            WifiMuteFragment.this.timer.setVisibility(8);
            WifiMuteFragment.this.nofication.setVisibility(0);
          }
        });
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
    View view = paramLayoutInflater.inflate(R.layout.fragment_wifi_mute, paramViewGroup, false);
    this.timer = (TextView)view.findViewById(R.id.countdownNumber);
    this.description = (TextView)view.findViewById(R.id.countdownDescription);
    this.nofication = (TextView)view.findViewById(R.id.wifiDisabledNotification);
    view.setOnTouchListener(new View.OnTouchListener() {
          public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
            if (param1MotionEvent.getAction() == 1)
              WifiMuteFragment.this.stateMachine.consumeEvent(WifiMuteEvent.USER_ACTIVITY); 
            return true;
          }
        });
    return view;
  }
  
  public void reset() {
    getActivity().runOnUiThread(new Runnable() {
          public void run() {
            WifiMuteFragment.this.description.setVisibility(0);
            WifiMuteFragment.this.timer.setVisibility(0);
            WifiMuteFragment.this.nofication.setVisibility(8);
          }
        });
  }
  
  public void setCountdownNumber(final long num) {
    getActivity().runOnUiThread(new Runnable() {
          public void run() {
            WifiMuteFragment.this.timer.setText(String.valueOf(num));
          }
        });
  }
  
  public void setStateMachine(WifiMuteStateMachine paramWifiMuteStateMachine) {
    this.stateMachine = paramWifiMuteStateMachine;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\WifiMuteFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.qualcomm.ftcdriverstation;

import android.app.Activity;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class GamepadIndicator {
  protected ImageView activeView;
  
  protected ImageView baseView;
  
  protected final Context context;
  
  protected final int idActive;
  
  protected final int idBase;
  
  protected State state = State.INVISIBLE;
  
  public GamepadIndicator(Activity paramActivity, int paramInt1, int paramInt2) {
    this.context = (Context)paramActivity;
    this.idActive = paramInt1;
    this.idBase = paramInt2;
    initialize(paramActivity);
  }
  
  protected void indicate() {
    Animation animation = AnimationUtils.loadAnimation(this.context, 2130771993);
    this.activeView.setImageResource(2131165339);
    animation.setAnimationListener(new Animation.AnimationListener() {
          public void onAnimationEnd(Animation param1Animation) {
            GamepadIndicator.this.activeView.setImageResource(2131165338);
          }
          
          public void onAnimationRepeat(Animation param1Animation) {
            GamepadIndicator.this.activeView.setImageResource(2131165338);
          }
          
          public void onAnimationStart(Animation param1Animation) {}
        });
    this.activeView.startAnimation(animation);
  }
  
  public void initialize(Activity paramActivity) {
    this.activeView = (ImageView)paramActivity.findViewById(this.idActive);
    this.baseView = (ImageView)paramActivity.findViewById(this.idBase);
  }
  
  public void setState(final State state) {
    this.state = state;
    AppUtil.getInstance().runOnUiThread(new Runnable() {
          public void run() {
            int i = GamepadIndicator.null.$SwitchMap$com$qualcomm$ftcdriverstation$GamepadIndicator$State[state.ordinal()];
            if (i != 1) {
              if (i != 2) {
                if (i != 3)
                  return; 
                GamepadIndicator.this.indicate();
                return;
              } 
              GamepadIndicator.this.activeView.setVisibility(4);
              GamepadIndicator.this.baseView.setVisibility(0);
              return;
            } 
            GamepadIndicator.this.activeView.setVisibility(4);
            GamepadIndicator.this.baseView.setVisibility(4);
          }
        });
  }
  
  public enum State {
    INDICATE, INVISIBLE, VISIBLE;
    
    static {
      State state = new State("INDICATE", 2);
      INDICATE = state;
      $VALUES = new State[] { INVISIBLE, VISIBLE, state };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftcdriverstation\GamepadIndicator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
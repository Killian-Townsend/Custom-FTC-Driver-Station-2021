package com.qualcomm.ftcdriverstation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PracticeTimerManager {
  private static final int AUTO_END_LENGTH = 3000;
  
  private static final int AUTO_LENGTH = 30000;
  
  private static final int DRIVER_CONTROL_LENGTH = 120000;
  
  private static final int ENDGAME_LENGTH = 30000;
  
  private static final int END_MATCH_BUZZER_LENGTH = 2000;
  
  private static final int GAME_ANNOUNCER_LENGTH = 5275;
  
  private static final int MATCH_LENGTH = 150000;
  
  private static final int OFFICIAL_TIMER_OFFSET = 1000;
  
  private static final int PICK_UP_CTRLS_LENGTH = 5000;
  
  private static final int TELEOP_LENGTH = 90000;
  
  private static final int TELE_COUNTDOWN_LENGTH = 3000;
  
  private static final int TIMER_TICK_PERIOD = 100;
  
  private int PLAYING_SOUND_STREAM_ID = -1;
  
  private int SOUND_ID_ABORT_MATCH;
  
  private int SOUND_ID_END_AUTO;
  
  private int SOUND_ID_END_MATCH;
  
  private int SOUND_ID_FACTWHISTLE;
  
  private int SOUND_ID_FIREBELL;
  
  private int SOUND_ID_MC_BEGIN_AUTO;
  
  private int SOUND_ID_PICK_UP_CTRLS;
  
  private int SOUND_ID_START_AUTO;
  
  private int SOUND_ID_TELE_COUNTDOWN;
  
  private CountDownTimer countDownTimer;
  
  private boolean running = false;
  
  private SoundPool soundPool;
  
  private ImageView startStopBtn;
  
  private final Object syncobj = new Object();
  
  private Context theContext;
  
  private TextView timerView;
  
  public PracticeTimerManager(Context paramContext, ImageView paramImageView, TextView paramTextView) {
    this.theContext = paramContext;
    this.startStopBtn = paramImageView;
    this.timerView = paramTextView;
    paramImageView.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            synchronized (PracticeTimerManager.this.syncobj) {
              if (!PracticeTimerManager.this.running) {
                PracticeTimerManager.this.showStartDialog(PracticeTimerManager.this.theContext);
              } else {
                if (PracticeTimerManager.this.countDownTimer != null)
                  PracticeTimerManager.this.countDownTimer.cancel(); 
                PracticeTimerManager.access$102(PracticeTimerManager.this, false);
                if (PracticeTimerManager.this.PLAYING_SOUND_STREAM_ID != -1)
                  PracticeTimerManager.this.soundPool.stop(PracticeTimerManager.this.PLAYING_SOUND_STREAM_ID); 
                PracticeTimerManager.this.playSound(PracticeTimerManager.this.SOUND_ID_ABORT_MATCH);
                PracticeTimerManager.this.resetUi();
              } 
              return;
            } 
          }
        });
    SoundPool soundPool = new SoundPool(9, 3, 0);
    this.soundPool = soundPool;
    this.SOUND_ID_PICK_UP_CTRLS = soundPool.load(paramContext, 2131558415, 1);
    this.SOUND_ID_TELE_COUNTDOWN = this.soundPool.load(paramContext, 2131558414, 1);
    this.SOUND_ID_FIREBELL = this.soundPool.load(paramContext, 2131558412, 1);
    this.SOUND_ID_FACTWHISTLE = this.soundPool.load(paramContext, 2131558411, 1);
    this.SOUND_ID_END_MATCH = this.soundPool.load(paramContext, 2131558409, 1);
    this.SOUND_ID_ABORT_MATCH = this.soundPool.load(paramContext, 2131558413, 1);
    this.SOUND_ID_START_AUTO = this.soundPool.load(paramContext, 2131558400, 1);
    this.SOUND_ID_END_AUTO = this.soundPool.load(paramContext, 2131558408, 1);
    this.SOUND_ID_MC_BEGIN_AUTO = this.soundPool.load(paramContext, 2131558417, 1);
  }
  
  private void autoEndTimer() {
    synchronized (this.syncobj) {
      if (!this.running)
        return; 
      playSound(this.SOUND_ID_END_AUTO);
      this.timerView.setTextColor(this.theContext.getResources().getColor(2131034250));
      this.timerView.setText(formatTimeLeft(120000L));
      this.countDownTimer = (new CountDownTimer(3000L, 100L) {
          public void onFinish() {
            PracticeTimerManager.this.pickUpControllersTimer();
          }
          
          public void onTick(long param1Long) {}
        }).start();
      return;
    } 
  }
  
  private void autoTimer() {
    synchronized (this.syncobj) {
      if (!this.running)
        return; 
      playSound(this.SOUND_ID_START_AUTO);
      this.timerView.setTextColor(this.theContext.getResources().getColor(2131034250));
      this.countDownTimer = (new CountDownTimer(30000L, 100L) {
          public void onFinish() {
            PracticeTimerManager.this.autoEndTimer();
          }
          
          public void onTick(long param1Long) {
            PracticeTimerManager.this.timerView.setText(PracticeTimerManager.this.formatTimeLeft(param1Long + 1000L + 120000L));
          }
        }).start();
      return;
    } 
  }
  
  private void endMatchTimer() {
    synchronized (this.syncobj) {
      if (!this.running)
        return; 
      playSound(this.SOUND_ID_END_MATCH);
      this.timerView.setTextColor(this.theContext.getResources().getColor(2131034250));
      this.timerView.setText(formatTimeLeft(0L));
      this.countDownTimer = (new CountDownTimer(2000L, 100L) {
          public void onFinish() {
            PracticeTimerManager.this.timerView.setText(PracticeTimerManager.this.formatTimeLeft(0L));
            PracticeTimerManager.access$102(PracticeTimerManager.this, false);
            PracticeTimerManager.this.resetUi();
          }
          
          public void onTick(long param1Long) {}
        }).start();
      return;
    } 
  }
  
  private void endgameTimer() {
    synchronized (this.syncobj) {
      if (!this.running)
        return; 
      playSound(this.SOUND_ID_FACTWHISTLE);
      this.countDownTimer = (new CountDownTimer(30000L, 100L) {
          public void onFinish() {
            PracticeTimerManager.this.endMatchTimer();
          }
          
          public void onTick(long param1Long) {
            PracticeTimerManager.this.timerView.setText(PracticeTimerManager.this.formatTimeLeft(param1Long + 1000L));
          }
        }).start();
      return;
    } 
  }
  
  private String formatSeconds(int paramInt) {
    if (paramInt < 10) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("0");
      stringBuilder.append(paramInt);
      return stringBuilder.toString();
    } 
    return Integer.toString(paramInt);
  }
  
  private String formatTimeLeft(long paramLong) {
    int i = (int)(paramLong / 1000L);
    int j = (int)(paramLong / 60000L % 60L);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(j);
    stringBuilder.append(":");
    stringBuilder.append(formatSeconds(i % 60));
    return stringBuilder.toString();
  }
  
  private void gameAnnouncerTimer() {
    synchronized (this.syncobj) {
      if (!this.running)
        return; 
      playSound(this.SOUND_ID_MC_BEGIN_AUTO);
      this.timerView.setTextColor(this.theContext.getResources().getColor(2131034251));
      this.timerView.setText(formatTimeLeft(150000L));
      this.countDownTimer = (new CountDownTimer(5275L, 100L) {
          public void onFinish() {
            PracticeTimerManager.this.autoTimer();
          }
          
          public void onTick(long param1Long) {}
        }).start();
      return;
    } 
  }
  
  private void pickUpControllersTimer() {
    synchronized (this.syncobj) {
      if (!this.running)
        return; 
      playSound(this.SOUND_ID_PICK_UP_CTRLS);
      this.timerView.setTextColor(this.theContext.getResources().getColor(2131034251));
      this.countDownTimer = (new CountDownTimer(5000L, 100L) {
          public void onFinish() {
            PracticeTimerManager.this.teleCountDownTimer();
          }
          
          public void onTick(long param1Long) {
            PracticeTimerManager.this.timerView.setText(PracticeTimerManager.this.formatTimeLeft(param1Long + 1000L));
          }
        }).start();
      return;
    } 
  }
  
  private void playSound(int paramInt) {
    this.PLAYING_SOUND_STREAM_ID = this.soundPool.play(paramInt, 1.0F, 1.0F, 1, 0, 1.0F);
  }
  
  private void teleCountDownTimer() {
    synchronized (this.syncobj) {
      if (!this.running)
        return; 
      playSound(this.SOUND_ID_TELE_COUNTDOWN);
      this.timerView.setTextColor(this.theContext.getResources().getColor(2131034249));
      this.countDownTimer = (new CountDownTimer(3000L, 100L) {
          public void onFinish() {
            PracticeTimerManager.this.teleopTimer();
          }
          
          public void onTick(long param1Long) {
            PracticeTimerManager.this.timerView.setText(PracticeTimerManager.this.formatTimeLeft(param1Long + 1000L));
          }
        }).start();
      return;
    } 
  }
  
  private void teleopTimer() {
    synchronized (this.syncobj) {
      if (!this.running)
        return; 
      playSound(this.SOUND_ID_FIREBELL);
      this.timerView.setTextColor(this.theContext.getResources().getColor(2131034250));
      this.countDownTimer = (new CountDownTimer(90000L, 100L) {
          public void onFinish() {
            PracticeTimerManager.this.endgameTimer();
          }
          
          public void onTick(long param1Long) {
            PracticeTimerManager.this.timerView.setText(PracticeTimerManager.this.formatTimeLeft(param1Long + 1000L + 30000L));
          }
        }).start();
      return;
    } 
  }
  
  public void reset() {
    synchronized (this.syncobj) {
      if (this.countDownTimer != null)
        this.countDownTimer.cancel(); 
      this.running = false;
      this.soundPool.stop(this.SOUND_ID_PICK_UP_CTRLS);
      this.soundPool.stop(this.SOUND_ID_TELE_COUNTDOWN);
      this.soundPool.stop(this.SOUND_ID_FIREBELL);
      this.soundPool.stop(this.SOUND_ID_FACTWHISTLE);
      this.soundPool.stop(this.SOUND_ID_END_MATCH);
      this.soundPool.stop(this.SOUND_ID_ABORT_MATCH);
      this.soundPool.stop(this.SOUND_ID_START_AUTO);
      this.soundPool.stop(this.SOUND_ID_END_AUTO);
      this.soundPool.stop(this.SOUND_ID_MC_BEGIN_AUTO);
      resetUi();
      return;
    } 
  }
  
  void resetUi() {
    this.timerView.setTextColor(this.theContext.getResources().getColor(2131034250));
    this.timerView.setText(formatTimeLeft(150000L));
    this.startStopBtn.setImageDrawable(this.theContext.getResources().getDrawable(2131165317));
  }
  
  void showStartDialog(Context paramContext) {
    AlertDialog.Builder builder = new AlertDialog.Builder(paramContext);
    builder.setTitle("Start from...");
    DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface param1DialogInterface, int param1Int) {
          PracticeTimerManager.access$102(PracticeTimerManager.this, true);
          PracticeTimerManager.this.startStopBtn.setImageDrawable(PracticeTimerManager.this.theContext.getResources().getDrawable(2131165325));
          if (param1Int != 0) {
            if (param1Int != 1) {
              if (param1Int != 2) {
                if (param1Int != 3)
                  return; 
                PracticeTimerManager.this.endgameTimer();
                return;
              } 
              PracticeTimerManager.this.teleopTimer();
              return;
            } 
            PracticeTimerManager.this.pickUpControllersTimer();
            return;
          } 
          PracticeTimerManager.this.gameAnnouncerTimer();
        }
      };
    builder.setItems((CharSequence[])new String[] { "Autonomous", "Auto --> Tele-Op Transition", "Tele-Op", "Endgame" }, onClickListener);
    builder.create().show();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftcdriverstation\PracticeTimerManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
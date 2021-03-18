package org.firstinspires.ftc.robotcore.internal.android;

import android.content.Context;
import java.io.File;

public interface SoundPoolIntf {
  void close();
  
  void play(Context paramContext, int paramInt1, float paramFloat1, int paramInt2, float paramFloat2);
  
  void play(Context paramContext, File paramFile, float paramFloat1, int paramInt, float paramFloat2);
  
  boolean preload(Context paramContext, int paramInt);
  
  boolean preload(Context paramContext, File paramFile);
  
  void stopPlayingAll();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\SoundPoolIntf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
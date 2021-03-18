package org.firstinspires.ftc.robotcore.external.android;

import android.content.Context;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.firstinspires.ftc.robotcore.internal.android.SoundPoolIntf;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class AndroidSoundPool {
  public static final String RAW_RES_PREFIX = "RawRes:";
  
  private static final String SOUNDS_DIR = "sounds";
  
  private volatile int loop = 0;
  
  private volatile float rate = 1.0F;
  
  private final Map<String, File> soundFileMap = new HashMap<String, File>();
  
  private volatile SoundPoolIntf soundPool;
  
  private final Map<String, Integer> soundResIdMap = new HashMap<String, Integer>();
  
  private volatile float volume = 1.0F;
  
  private Context getContext() {
    return (Context)AppUtil.getInstance().getRootActivity();
  }
  
  private File getSoundFile(String paramString) {
    File file = this.soundFileMap.get(paramString);
    if (file != null)
      return file; 
    file = new File(AppUtil.BLOCKS_SOUNDS_DIR, paramString);
    if (this.soundPool.preload(getContext(), file)) {
      this.soundFileMap.put(paramString, file);
      return file;
    } 
    return null;
  }
  
  private Integer getSoundResId(String paramString) {
    Integer integer = this.soundResIdMap.get(paramString);
    if (integer != null)
      return integer; 
    Context context = getContext();
    int i = context.getResources().getIdentifier(paramString, "raw", context.getPackageName());
    if (i != 0 && this.soundPool.preload(context, i)) {
      this.soundResIdMap.put(paramString, Integer.valueOf(i));
      return Integer.valueOf(i);
    } 
    return null;
  }
  
  public void close() {
    if (this.soundPool != null) {
      this.soundPool.stopPlayingAll();
      this.soundFileMap.clear();
      this.soundResIdMap.clear();
      this.soundPool = null;
    } 
  }
  
  public int getLoop() {
    return this.loop;
  }
  
  public float getRate() {
    return this.rate;
  }
  
  public float getVolume() {
    return this.volume;
  }
  
  public void initialize(SoundPoolIntf paramSoundPoolIntf) {
    this.soundPool = paramSoundPoolIntf;
  }
  
  public boolean play(String paramString) {
    if (this.soundPool != null) {
      Integer integer;
      if (paramString.startsWith("RawRes:")) {
        integer = getSoundResId(paramString.substring(7));
        if (integer != null) {
          this.soundPool.play(getContext(), integer.intValue(), this.volume, this.loop, this.rate);
          return true;
        } 
        return false;
      } 
      File file = getSoundFile((String)integer);
      if (file != null) {
        this.soundPool.play(getContext(), file, this.volume, this.loop, this.rate);
        return true;
      } 
      return false;
    } 
    throw new IllegalStateException("You forgot to call AndroidSoundPool.initialize!");
  }
  
  public boolean preloadSound(String paramString) {
    if (this.soundPool != null)
      return paramString.startsWith("RawRes:") ? ((getSoundResId(paramString.substring(7)) != null)) : ((getSoundFile(paramString) != null)); 
    throw new IllegalStateException("You forgot to call AndroidSoundPool.initialize!");
  }
  
  public void setLoop(int paramInt) {
    if (paramInt >= -1)
      this.loop = paramInt; 
  }
  
  public void setRate(float paramFloat) {
    if (paramFloat >= 0.5F && paramFloat <= 2.0F)
      this.rate = paramFloat; 
  }
  
  public void setVolume(float paramFloat) {
    if (paramFloat >= 0.0F && paramFloat <= 1.0F)
      this.volume = paramFloat; 
  }
  
  public void stop() {
    if (this.soundPool != null)
      this.soundPool.stopPlayingAll(); 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\android\AndroidSoundPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
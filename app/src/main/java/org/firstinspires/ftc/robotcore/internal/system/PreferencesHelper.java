package org.firstinspires.ftc.robotcore.internal.system;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.HashMap;
import java.util.Set;
import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson;

public class PreferencesHelper {
  protected final SharedPreferences sharedPreferences;
  
  protected final String tag;
  
  public PreferencesHelper(String paramString) {
    this(paramString, AppUtil.getDefContext());
  }
  
  public PreferencesHelper(String paramString, Context paramContext) {
    this(paramString, PreferenceManager.getDefaultSharedPreferences(paramContext));
  }
  
  public PreferencesHelper(String paramString, SharedPreferences paramSharedPreferences) {
    this.tag = paramString;
    this.sharedPreferences = paramSharedPreferences;
  }
  
  public boolean contains(String paramString) {
    return this.sharedPreferences.contains(paramString);
  }
  
  public SharedPreferences getSharedPreferences() {
    return this.sharedPreferences;
  }
  
  protected void logWrite(String paramString, Object paramObject) {
    RobotLog.vv(this.tag, "writing pref name=%s value=%s", new Object[] { paramString, paramObject });
  }
  
  public boolean readBoolean(String paramString, boolean paramBoolean) {
    return this.sharedPreferences.getBoolean(paramString, paramBoolean);
  }
  
  public float readFloat(String paramString, float paramFloat) {
    return this.sharedPreferences.getFloat(paramString, paramFloat);
  }
  
  public int readInt(String paramString, int paramInt) {
    return this.sharedPreferences.getInt(paramString, paramInt);
  }
  
  public long readLong(String paramString, long paramLong) {
    return this.sharedPreferences.getLong(paramString, paramLong);
  }
  
  public Object readPref(String paramString) {
    return this.sharedPreferences.getAll().get(paramString);
  }
  
  public String readString(String paramString1, String paramString2) {
    return this.sharedPreferences.getString(paramString1, paramString2);
  }
  
  public StringMap readStringMap(String paramString, StringMap paramStringMap) {
    StringMap stringMap = StringMap.deserialize(readString(paramString, null));
    if (stringMap != null)
      paramStringMap = stringMap; 
    return paramStringMap;
  }
  
  public Set<String> readStringSet(String paramString, Set<String> paramSet) {
    return this.sharedPreferences.getStringSet(paramString, paramSet);
  }
  
  public void remove(String paramString) {
    this.sharedPreferences.edit().remove(paramString).apply();
  }
  
  public boolean writeBooleanPrefIfDifferent(String paramString, boolean paramBoolean) {
    Assert.assertNotNull(Boolean.valueOf(paramBoolean));
    for (boolean bool = false;; bool = true) {
      if (contains(paramString) && paramBoolean == readBoolean(paramString, false))
        return bool; 
      logWrite(paramString, Boolean.valueOf(paramBoolean));
      this.sharedPreferences.edit().putBoolean(paramString, paramBoolean).apply();
    } 
  }
  
  public boolean writeFloatPrefIfDifferent(String paramString, float paramFloat) {
    Assert.assertNotNull(Float.valueOf(paramFloat));
    for (boolean bool = false;; bool = true) {
      if (contains(paramString) && paramFloat == readFloat(paramString, 0.0F))
        return bool; 
      logWrite(paramString, Float.valueOf(paramFloat));
      this.sharedPreferences.edit().putFloat(paramString, paramFloat).apply();
    } 
  }
  
  public boolean writeIntPrefIfDifferent(String paramString, int paramInt) {
    Assert.assertNotNull(Integer.valueOf(paramInt));
    for (boolean bool = false;; bool = true) {
      if (contains(paramString) && paramInt == readInt(paramString, 0))
        return bool; 
      logWrite(paramString, Integer.valueOf(paramInt));
      this.sharedPreferences.edit().putInt(paramString, paramInt).apply();
    } 
  }
  
  public boolean writeLongPrefIfDifferent(String paramString, long paramLong) {
    Assert.assertNotNull(Long.valueOf(paramLong));
    boolean bool;
    for (bool = false;; bool = true) {
      if (contains(paramString) && paramLong == readLong(paramString, 0L))
        return bool; 
      logWrite(paramString, Long.valueOf(paramLong));
      this.sharedPreferences.edit().putLong(paramString, paramLong).apply();
    } 
  }
  
  public boolean writePrefIfDifferent(String paramString, Object paramObject) {
    return (paramObject instanceof String) ? writeStringPrefIfDifferent(paramString, (String)paramObject) : ((paramObject instanceof Boolean) ? writeBooleanPrefIfDifferent(paramString, ((Boolean)paramObject).booleanValue()) : ((paramObject instanceof Integer) ? writeIntPrefIfDifferent(paramString, ((Integer)paramObject).intValue()) : ((paramObject instanceof Long) ? writeLongPrefIfDifferent(paramString, ((Long)paramObject).longValue()) : ((paramObject instanceof Float) ? writeFloatPrefIfDifferent(paramString, ((Float)paramObject).floatValue()) : ((paramObject instanceof StringMap) ? writeStringMapPrefIfDifferent(paramString, (StringMap)paramObject) : ((paramObject instanceof Set) ? writeStringSetPrefIfDifferent(paramString, (Set<String>)paramObject) : false))))));
  }
  
  public boolean writeStringMapPrefIfDifferent(String paramString, StringMap paramStringMap) {
    return writeStringPrefIfDifferent(paramString, paramStringMap.serialize());
  }
  
  public boolean writeStringPrefIfDifferent(String paramString1, String paramString2) {
    Assert.assertNotNull(paramString2);
    for (boolean bool = false;; bool = true) {
      if (contains(paramString1) && paramString2.equals(readString(paramString1, "")))
        return bool; 
      logWrite(paramString1, paramString2);
      this.sharedPreferences.edit().putString(paramString1, paramString2).apply();
    } 
  }
  
  public boolean writeStringSetPrefIfDifferent(String paramString, Set<String> paramSet) {
    Assert.assertNotNull(paramSet);
    for (boolean bool = false;; bool = true) {
      if (contains(paramString) && paramSet.equals(readStringSet(paramString, (Set<String>)null)))
        return bool; 
      logWrite(paramString, paramSet);
      this.sharedPreferences.edit().putStringSet(paramString, paramSet).apply();
    } 
  }
  
  public static class StringMap extends HashMap<String, String> {
    public static StringMap deserialize(String param1String) {
      return (param1String == null) ? null : (StringMap)SimpleGson.getInstance().fromJson(param1String, StringMap.class);
    }
    
    public String serialize() {
      return SimpleGson.getInstance().toJson(this);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\PreferencesHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
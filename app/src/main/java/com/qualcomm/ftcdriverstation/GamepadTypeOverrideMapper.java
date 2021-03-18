package com.qualcomm.ftcdriverstation;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.qualcomm.hardware.logitech.LogitechGamepadF310;
import com.qualcomm.hardware.microsoft.MicrosoftGamepadXbox360;
import com.qualcomm.hardware.sony.SonyGamepadPS4;
import com.qualcomm.robotcore.hardware.Gamepad;
import java.util.ArrayList;
import java.util.Set;

public class GamepadTypeOverrideMapper {
  static final String KEY_GAMEPAD_MAPPING = "GAMEPAD_MAPPING";
  
  Context context;
  
  Set<String> serializedEntries;
  
  SharedPreferences sharedPreferences;
  
  GamepadTypeOverrideMapper(Context paramContext) {
    this.context = paramContext;
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(paramContext);
    this.sharedPreferences = sharedPreferences;
    this.serializedEntries = sharedPreferences.getStringSet("GAMEPAD_MAPPING", null);
  }
  
  static String checkForClash(Set<String> paramSet, GamepadTypeOverrideEntry paramGamepadTypeOverrideEntry) {
    for (String str : paramSet) {
      if (GamepadTypeOverrideEntry.fromString(str).usbIdsMatch(paramGamepadTypeOverrideEntry))
        return str; 
    } 
    return null;
  }
  
  void addOrUpdate(GamepadTypeOverrideEntry paramGamepadTypeOverrideEntry) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield sharedPreferences : Landroid/content/SharedPreferences;
    //   6: ldc 'GAMEPAD_MAPPING'
    //   8: aconst_null
    //   9: invokeinterface getStringSet : (Ljava/lang/String;Ljava/util/Set;)Ljava/util/Set;
    //   14: astore_2
    //   15: aload_0
    //   16: aload_2
    //   17: putfield serializedEntries : Ljava/util/Set;
    //   20: aload_2
    //   21: ifnull -> 62
    //   24: aload_2
    //   25: aload_1
    //   26: invokestatic checkForClash : (Ljava/util/Set;Lcom/qualcomm/ftcdriverstation/GamepadTypeOverrideMapper$GamepadTypeOverrideEntry;)Ljava/lang/String;
    //   29: astore_2
    //   30: aload_2
    //   31: ifnull -> 45
    //   34: aload_0
    //   35: getfield serializedEntries : Ljava/util/Set;
    //   38: aload_2
    //   39: invokeinterface remove : (Ljava/lang/Object;)Z
    //   44: pop
    //   45: aload_0
    //   46: getfield serializedEntries : Ljava/util/Set;
    //   49: aload_1
    //   50: invokevirtual toString : ()Ljava/lang/String;
    //   53: invokeinterface add : (Ljava/lang/Object;)Z
    //   58: pop
    //   59: goto -> 86
    //   62: new android/util/ArraySet
    //   65: dup
    //   66: invokespecial <init> : ()V
    //   69: astore_2
    //   70: aload_0
    //   71: aload_2
    //   72: putfield serializedEntries : Ljava/util/Set;
    //   75: aload_2
    //   76: aload_1
    //   77: invokevirtual toString : ()Ljava/lang/String;
    //   80: invokeinterface add : (Ljava/lang/Object;)Z
    //   85: pop
    //   86: aload_0
    //   87: getfield sharedPreferences : Landroid/content/SharedPreferences;
    //   90: invokeinterface edit : ()Landroid/content/SharedPreferences$Editor;
    //   95: ldc 'GAMEPAD_MAPPING'
    //   97: aload_0
    //   98: getfield serializedEntries : Ljava/util/Set;
    //   101: invokeinterface putStringSet : (Ljava/lang/String;Ljava/util/Set;)Landroid/content/SharedPreferences$Editor;
    //   106: invokeinterface commit : ()Z
    //   111: pop
    //   112: aload_0
    //   113: monitorexit
    //   114: return
    //   115: astore_1
    //   116: aload_0
    //   117: monitorexit
    //   118: aload_1
    //   119: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	115	finally
    //   24	30	115	finally
    //   34	45	115	finally
    //   45	59	115	finally
    //   62	86	115	finally
    //   86	112	115	finally
  }
  
  void delete(GamepadTypeOverrideEntry paramGamepadTypeOverrideEntry) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield sharedPreferences : Landroid/content/SharedPreferences;
    //   6: ldc 'GAMEPAD_MAPPING'
    //   8: aconst_null
    //   9: invokeinterface getStringSet : (Ljava/lang/String;Ljava/util/Set;)Ljava/util/Set;
    //   14: astore_3
    //   15: aload_0
    //   16: aload_3
    //   17: putfield serializedEntries : Ljava/util/Set;
    //   20: aload_3
    //   21: ifnonnull -> 27
    //   24: aload_0
    //   25: monitorexit
    //   26: return
    //   27: iconst_0
    //   28: istore_2
    //   29: aload_3
    //   30: invokeinterface iterator : ()Ljava/util/Iterator;
    //   35: astore_3
    //   36: aload_3
    //   37: invokeinterface hasNext : ()Z
    //   42: ifeq -> 69
    //   45: aload_3
    //   46: invokeinterface next : ()Ljava/lang/Object;
    //   51: checkcast java/lang/String
    //   54: aload_1
    //   55: invokevirtual toString : ()Ljava/lang/String;
    //   58: invokevirtual equals : (Ljava/lang/Object;)Z
    //   61: ifeq -> 36
    //   64: iconst_1
    //   65: istore_2
    //   66: goto -> 36
    //   69: iload_2
    //   70: ifeq -> 116
    //   73: aload_0
    //   74: getfield serializedEntries : Ljava/util/Set;
    //   77: aload_1
    //   78: invokevirtual toString : ()Ljava/lang/String;
    //   81: invokeinterface remove : (Ljava/lang/Object;)Z
    //   86: pop
    //   87: aload_0
    //   88: getfield sharedPreferences : Landroid/content/SharedPreferences;
    //   91: invokeinterface edit : ()Landroid/content/SharedPreferences$Editor;
    //   96: ldc 'GAMEPAD_MAPPING'
    //   98: aload_0
    //   99: getfield serializedEntries : Ljava/util/Set;
    //   102: invokeinterface putStringSet : (Ljava/lang/String;Ljava/util/Set;)Landroid/content/SharedPreferences$Editor;
    //   107: invokeinterface commit : ()Z
    //   112: pop
    //   113: aload_0
    //   114: monitorexit
    //   115: return
    //   116: new java/lang/IllegalArgumentException
    //   119: dup
    //   120: invokespecial <init> : ()V
    //   123: athrow
    //   124: astore_1
    //   125: aload_0
    //   126: monitorexit
    //   127: aload_1
    //   128: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	124	finally
    //   29	36	124	finally
    //   36	64	124	finally
    //   73	113	124	finally
    //   116	124	124	finally
  }
  
  ArrayList<GamepadTypeOverrideEntry> getEntries() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield sharedPreferences : Landroid/content/SharedPreferences;
    //   6: ldc 'GAMEPAD_MAPPING'
    //   8: aconst_null
    //   9: invokeinterface getStringSet : (Ljava/lang/String;Ljava/util/Set;)Ljava/util/Set;
    //   14: astore_1
    //   15: aload_0
    //   16: aload_1
    //   17: putfield serializedEntries : Ljava/util/Set;
    //   20: aload_1
    //   21: ifnonnull -> 36
    //   24: new java/util/ArrayList
    //   27: dup
    //   28: invokespecial <init> : ()V
    //   31: astore_1
    //   32: aload_0
    //   33: monitorexit
    //   34: aload_1
    //   35: areturn
    //   36: new java/util/ArrayList
    //   39: dup
    //   40: invokespecial <init> : ()V
    //   43: astore_1
    //   44: aload_0
    //   45: getfield serializedEntries : Ljava/util/Set;
    //   48: invokeinterface iterator : ()Ljava/util/Iterator;
    //   53: astore_2
    //   54: aload_2
    //   55: invokeinterface hasNext : ()Z
    //   60: ifeq -> 83
    //   63: aload_1
    //   64: aload_2
    //   65: invokeinterface next : ()Ljava/lang/Object;
    //   70: checkcast java/lang/String
    //   73: invokestatic fromString : (Ljava/lang/String;)Lcom/qualcomm/ftcdriverstation/GamepadTypeOverrideMapper$GamepadTypeOverrideEntry;
    //   76: invokevirtual add : (Ljava/lang/Object;)Z
    //   79: pop
    //   80: goto -> 54
    //   83: aload_0
    //   84: monitorexit
    //   85: aload_1
    //   86: areturn
    //   87: astore_1
    //   88: aload_0
    //   89: monitorexit
    //   90: aload_1
    //   91: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	87	finally
    //   24	32	87	finally
    //   36	54	87	finally
    //   54	80	87	finally
  }
  
  GamepadTypeOverrideEntry getEntryFor(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual getEntries : ()Ljava/util/ArrayList;
    //   6: invokevirtual iterator : ()Ljava/util/Iterator;
    //   9: astore #4
    //   11: aload #4
    //   13: invokeinterface hasNext : ()Z
    //   18: ifeq -> 50
    //   21: aload #4
    //   23: invokeinterface next : ()Ljava/lang/Object;
    //   28: checkcast com/qualcomm/ftcdriverstation/GamepadTypeOverrideMapper$GamepadTypeOverrideEntry
    //   31: astore #5
    //   33: aload #5
    //   35: iload_1
    //   36: iload_2
    //   37: invokevirtual usbIdsMatch : (II)Z
    //   40: istore_3
    //   41: iload_3
    //   42: ifeq -> 11
    //   45: aload_0
    //   46: monitorexit
    //   47: aload #5
    //   49: areturn
    //   50: aload_0
    //   51: monitorexit
    //   52: aconst_null
    //   53: areturn
    //   54: astore #4
    //   56: aload_0
    //   57: monitorexit
    //   58: aload #4
    //   60: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	54	finally
    //   11	41	54	finally
  }
  
  void setEntries(ArrayList<GamepadTypeOverrideEntry> paramArrayList) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield serializedEntries : Ljava/util/Set;
    //   6: ifnull -> 21
    //   9: aload_0
    //   10: getfield serializedEntries : Ljava/util/Set;
    //   13: invokeinterface clear : ()V
    //   18: goto -> 32
    //   21: aload_0
    //   22: new android/util/ArraySet
    //   25: dup
    //   26: invokespecial <init> : ()V
    //   29: putfield serializedEntries : Ljava/util/Set;
    //   32: aload_1
    //   33: invokevirtual isEmpty : ()Z
    //   36: ifeq -> 64
    //   39: aload_0
    //   40: getfield sharedPreferences : Landroid/content/SharedPreferences;
    //   43: invokeinterface edit : ()Landroid/content/SharedPreferences$Editor;
    //   48: ldc 'GAMEPAD_MAPPING'
    //   50: invokeinterface remove : (Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;
    //   55: invokeinterface commit : ()Z
    //   60: pop
    //   61: goto -> 153
    //   64: aload_1
    //   65: invokevirtual iterator : ()Ljava/util/Iterator;
    //   68: astore_1
    //   69: aload_1
    //   70: invokeinterface hasNext : ()Z
    //   75: ifeq -> 105
    //   78: aload_1
    //   79: invokeinterface next : ()Ljava/lang/Object;
    //   84: checkcast com/qualcomm/ftcdriverstation/GamepadTypeOverrideMapper$GamepadTypeOverrideEntry
    //   87: astore_2
    //   88: aload_0
    //   89: getfield serializedEntries : Ljava/util/Set;
    //   92: aload_2
    //   93: invokevirtual toString : ()Ljava/lang/String;
    //   96: invokeinterface add : (Ljava/lang/Object;)Z
    //   101: pop
    //   102: goto -> 69
    //   105: aload_0
    //   106: getfield sharedPreferences : Landroid/content/SharedPreferences;
    //   109: invokeinterface edit : ()Landroid/content/SharedPreferences$Editor;
    //   114: ldc 'GAMEPAD_MAPPING'
    //   116: invokeinterface remove : (Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;
    //   121: invokeinterface commit : ()Z
    //   126: pop
    //   127: aload_0
    //   128: getfield sharedPreferences : Landroid/content/SharedPreferences;
    //   131: invokeinterface edit : ()Landroid/content/SharedPreferences$Editor;
    //   136: ldc 'GAMEPAD_MAPPING'
    //   138: aload_0
    //   139: getfield serializedEntries : Ljava/util/Set;
    //   142: invokeinterface putStringSet : (Ljava/lang/String;Ljava/util/Set;)Landroid/content/SharedPreferences$Editor;
    //   147: invokeinterface commit : ()Z
    //   152: pop
    //   153: aload_0
    //   154: monitorexit
    //   155: return
    //   156: astore_1
    //   157: aload_0
    //   158: monitorexit
    //   159: aload_1
    //   160: athrow
    // Exception table:
    //   from	to	target	type
    //   2	18	156	finally
    //   21	32	156	finally
    //   32	61	156	finally
    //   64	69	156	finally
    //   69	102	156	finally
    //   105	153	156	finally
  }
  
  static class GamepadTypeOverrideEntry {
    Gamepad.Type mappedType;
    
    int pid;
    
    int vid;
    
    GamepadTypeOverrideEntry(int param1Int1, int param1Int2, Gamepad.Type param1Type) {
      this.vid = param1Int1;
      this.pid = param1Int2;
      this.mappedType = param1Type;
    }
    
    static GamepadTypeOverrideEntry fromString(String param1String) {
      String[] arrayOfString = param1String.split(":");
      return new GamepadTypeOverrideEntry(Integer.valueOf(arrayOfString[0]).intValue(), Integer.valueOf(arrayOfString[1]).intValue(), Gamepad.Type.valueOf(arrayOfString[2]));
    }
    
    Gamepad createGamepad() {
      int i = GamepadTypeOverrideMapper.null.$SwitchMap$com$qualcomm$robotcore$hardware$Gamepad$Type[this.mappedType.ordinal()];
      if (i != 1) {
        if (i != 2) {
          if (i == 3)
            return (Gamepad)new SonyGamepadPS4(); 
          throw new IllegalStateException();
        } 
        return (Gamepad)new MicrosoftGamepadXbox360();
      } 
      return (Gamepad)new LogitechGamepadF310();
    }
    
    public boolean equals(GamepadTypeOverrideEntry param1GamepadTypeOverrideEntry) {
      int i = this.vid;
      int j = param1GamepadTypeOverrideEntry.vid;
      boolean bool = false;
      if (i == j) {
        i = 1;
      } else {
        i = 0;
      } 
      if (this.pid == param1GamepadTypeOverrideEntry.pid)
        bool = true; 
      return this.mappedType.equals(param1GamepadTypeOverrideEntry.mappedType) & i & 0x1 & bool;
    }
    
    public String toString() {
      return String.format("%d:%d:%s", new Object[] { Integer.valueOf(this.vid), Integer.valueOf(this.pid), this.mappedType.toString() });
    }
    
    public boolean usbIdsMatch(int param1Int1, int param1Int2) {
      int i = this.vid;
      boolean bool = false;
      if (i == param1Int1) {
        param1Int1 = 1;
      } else {
        param1Int1 = 0;
      } 
      if (this.pid == param1Int2)
        bool = true; 
      return param1Int1 & 0x1 & bool;
    }
    
    public boolean usbIdsMatch(GamepadTypeOverrideEntry param1GamepadTypeOverrideEntry) {
      int i = this.vid;
      int j = param1GamepadTypeOverrideEntry.vid;
      boolean bool = false;
      if (i == j) {
        i = 1;
      } else {
        i = 0;
      } 
      if (this.pid == param1GamepadTypeOverrideEntry.pid)
        bool = true; 
      return i & 0x1 & bool;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftcdriverstation\GamepadTypeOverrideMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
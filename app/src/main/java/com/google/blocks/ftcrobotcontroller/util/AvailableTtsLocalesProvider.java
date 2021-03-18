package com.google.blocks.ftcrobotcontroller.util;

import java.util.Locale;
import java.util.Set;

public class AvailableTtsLocalesProvider {
  private static final String TAG = "AvailableTtsLocalesProvider";
  
  private static final AvailableTtsLocalesProvider instance = new AvailableTtsLocalesProvider();
  
  private Set<Locale> availableTtsLocales = null;
  
  public static AvailableTtsLocalesProvider getInstance() {
    return instance;
  }
  
  public Set<Locale> getAvailableTtsLocales() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: invokestatic getMainLooper : ()Landroid/os/Looper;
    //   5: invokevirtual getThread : ()Ljava/lang/Thread;
    //   8: invokestatic currentThread : ()Ljava/lang/Thread;
    //   11: if_acmpeq -> 103
    //   14: aload_0
    //   15: getfield availableTtsLocales : Ljava/util/Set;
    //   18: ifnonnull -> 94
    //   21: aload_0
    //   22: new java/util/HashSet
    //   25: dup
    //   26: invokespecial <init> : ()V
    //   29: putfield availableTtsLocales : Ljava/util/Set;
    //   32: new org/firstinspires/ftc/robotcore/external/android/AndroidTextToSpeech
    //   35: dup
    //   36: invokespecial <init> : ()V
    //   39: astore_3
    //   40: aload_3
    //   41: invokevirtual initialize : ()V
    //   44: invokestatic getAvailableLocales : ()[Ljava/util/Locale;
    //   47: astore #4
    //   49: aload #4
    //   51: arraylength
    //   52: istore_2
    //   53: iconst_0
    //   54: istore_1
    //   55: iload_1
    //   56: iload_2
    //   57: if_icmpge -> 90
    //   60: aload #4
    //   62: iload_1
    //   63: aaload
    //   64: astore #5
    //   66: aload_3
    //   67: aload #5
    //   69: invokevirtual isLocaleAvailable : (Ljava/util/Locale;)Z
    //   72: ifeq -> 125
    //   75: aload_0
    //   76: getfield availableTtsLocales : Ljava/util/Set;
    //   79: aload #5
    //   81: invokeinterface add : (Ljava/lang/Object;)Z
    //   86: pop
    //   87: goto -> 125
    //   90: aload_3
    //   91: invokevirtual close : ()V
    //   94: aload_0
    //   95: getfield availableTtsLocales : Ljava/util/Set;
    //   98: astore_3
    //   99: aload_0
    //   100: monitorexit
    //   101: aload_3
    //   102: areturn
    //   103: ldc 'AvailableTtsLocalesProvider'
    //   105: ldc 'AvailableTtsLocalesProvider used from Android Main Thread. This is not allowed.'
    //   107: invokestatic ee : (Ljava/lang/String;Ljava/lang/String;)V
    //   110: new java/lang/RuntimeException
    //   113: dup
    //   114: ldc 'AvailableTtsLocalesProvider used from Android Main Thread. This is not allowed.'
    //   116: invokespecial <init> : (Ljava/lang/String;)V
    //   119: athrow
    //   120: astore_3
    //   121: aload_0
    //   122: monitorexit
    //   123: aload_3
    //   124: athrow
    //   125: iload_1
    //   126: iconst_1
    //   127: iadd
    //   128: istore_1
    //   129: goto -> 55
    // Exception table:
    //   from	to	target	type
    //   2	53	120	finally
    //   66	87	120	finally
    //   90	94	120	finally
    //   94	99	120	finally
    //   103	120	120	finally
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontrolle\\util\AvailableTtsLocalesProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
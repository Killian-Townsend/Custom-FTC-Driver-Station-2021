package androidx.renderscript;

import android.content.res.Resources;

public class ScriptC extends Script {
  private static final String TAG = "ScriptC";
  
  protected ScriptC(long paramLong, RenderScript paramRenderScript) {
    super(paramLong, paramRenderScript);
  }
  
  protected ScriptC(RenderScript paramRenderScript, Resources paramResources, int paramInt) {
    super(0L, paramRenderScript);
    long l = internalCreate(paramRenderScript, paramResources, paramInt);
    if (l != 0L) {
      setID(l);
      return;
    } 
    throw new RSRuntimeException("Loading of ScriptC script failed.");
  }
  
  protected ScriptC(RenderScript paramRenderScript, String paramString, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    super(0L, paramRenderScript);
    long l;
    if (RenderScript.sPointerSize == 4) {
      l = internalStringCreate(paramRenderScript, paramString, paramArrayOfbyte1);
    } else {
      l = internalStringCreate(paramRenderScript, paramString, paramArrayOfbyte2);
    } 
    if (l != 0L) {
      setID(l);
      return;
    } 
    throw new RSRuntimeException("Loading of ScriptC script failed.");
  }
  
  private static long internalCreate(RenderScript paramRenderScript, Resources paramResources, int paramInt) {
    // Byte code:
    //   0: ldc androidx/renderscript/ScriptC
    //   2: monitorenter
    //   3: aload_1
    //   4: iload_2
    //   5: invokevirtual openRawResource : (I)Ljava/io/InputStream;
    //   8: astore #10
    //   10: sipush #1024
    //   13: newarray byte
    //   15: astore #8
    //   17: iconst_0
    //   18: istore_3
    //   19: aload #8
    //   21: arraylength
    //   22: iload_3
    //   23: isub
    //   24: istore #5
    //   26: aload #8
    //   28: astore #9
    //   30: iload #5
    //   32: istore #4
    //   34: iload #5
    //   36: ifne -> 70
    //   39: aload #8
    //   41: arraylength
    //   42: iconst_2
    //   43: imul
    //   44: istore #4
    //   46: iload #4
    //   48: newarray byte
    //   50: astore #9
    //   52: aload #8
    //   54: iconst_0
    //   55: aload #9
    //   57: iconst_0
    //   58: aload #8
    //   60: arraylength
    //   61: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   64: iload #4
    //   66: iload_3
    //   67: isub
    //   68: istore #4
    //   70: aload #10
    //   72: aload #9
    //   74: iload_3
    //   75: iload #4
    //   77: invokevirtual read : ([BII)I
    //   80: istore #4
    //   82: iload #4
    //   84: ifgt -> 122
    //   87: aload #10
    //   89: invokevirtual close : ()V
    //   92: aload_0
    //   93: aload_1
    //   94: iload_2
    //   95: invokevirtual getResourceEntryName : (I)Ljava/lang/String;
    //   98: aload_0
    //   99: invokevirtual getApplicationContext : ()Landroid/content/Context;
    //   102: invokevirtual getCacheDir : ()Ljava/io/File;
    //   105: invokevirtual toString : ()Ljava/lang/String;
    //   108: aload #9
    //   110: iload_3
    //   111: invokevirtual nScriptCCreate : (Ljava/lang/String;Ljava/lang/String;[BI)J
    //   114: lstore #6
    //   116: ldc androidx/renderscript/ScriptC
    //   118: monitorexit
    //   119: lload #6
    //   121: lreturn
    //   122: iload_3
    //   123: iload #4
    //   125: iadd
    //   126: istore_3
    //   127: aload #9
    //   129: astore #8
    //   131: goto -> 19
    //   134: astore_0
    //   135: aload #10
    //   137: invokevirtual close : ()V
    //   140: aload_0
    //   141: athrow
    //   142: new android/content/res/Resources$NotFoundException
    //   145: dup
    //   146: invokespecial <init> : ()V
    //   149: athrow
    //   150: astore_0
    //   151: ldc androidx/renderscript/ScriptC
    //   153: monitorexit
    //   154: aload_0
    //   155: athrow
    //   156: astore_0
    //   157: goto -> 142
    // Exception table:
    //   from	to	target	type
    //   3	10	150	finally
    //   10	17	134	finally
    //   19	26	134	finally
    //   39	64	134	finally
    //   70	82	134	finally
    //   87	92	156	java/io/IOException
    //   87	92	150	finally
    //   92	116	150	finally
    //   135	142	156	java/io/IOException
    //   135	142	150	finally
    //   142	150	150	finally
  }
  
  private static long internalStringCreate(RenderScript paramRenderScript, String paramString, byte[] paramArrayOfbyte) {
    // Byte code:
    //   0: ldc androidx/renderscript/ScriptC
    //   2: monitorenter
    //   3: aload_0
    //   4: aload_1
    //   5: aload_0
    //   6: invokevirtual getApplicationContext : ()Landroid/content/Context;
    //   9: invokevirtual getCacheDir : ()Ljava/io/File;
    //   12: invokevirtual toString : ()Ljava/lang/String;
    //   15: aload_2
    //   16: aload_2
    //   17: arraylength
    //   18: invokevirtual nScriptCCreate : (Ljava/lang/String;Ljava/lang/String;[BI)J
    //   21: lstore_3
    //   22: ldc androidx/renderscript/ScriptC
    //   24: monitorexit
    //   25: lload_3
    //   26: lreturn
    //   27: astore_0
    //   28: ldc androidx/renderscript/ScriptC
    //   30: monitorexit
    //   31: aload_0
    //   32: athrow
    // Exception table:
    //   from	to	target	type
    //   3	22	27	finally
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\renderscript\ScriptC.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
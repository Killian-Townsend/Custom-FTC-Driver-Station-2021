package androidx.renderscript;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import java.io.File;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RenderScript {
  private static final String CACHE_PATH = "com.android.renderscript.cache";
  
  public static final int CREATE_FLAG_NONE = 0;
  
  static final boolean DEBUG = false;
  
  static final boolean LOG_ENABLED = false;
  
  static final String LOG_TAG = "RenderScript_jni";
  
  static final int SUPPORT_LIB_API = 23;
  
  static final int SUPPORT_LIB_VERSION = 2301;
  
  static Object lock;
  
  private static String mBlackList;
  
  static String mCachePath;
  
  private static ArrayList<RenderScript> mProcessContextList = new ArrayList<RenderScript>();
  
  static Method registerNativeAllocation;
  
  static Method registerNativeFree;
  
  static boolean sInitialized;
  
  private static int sNative;
  
  static int sPointerSize;
  
  static Object sRuntime;
  
  private static int sSdkVersion;
  
  static boolean sUseGCHooks;
  
  private static boolean useIOlib;
  
  private static boolean useNative;
  
  private Context mApplicationContext;
  
  long mContext;
  
  private int mContextFlags = 0;
  
  private int mContextSdkVersion = 0;
  
  ContextType mContextType = ContextType.NORMAL;
  
  private boolean mDestroyed = false;
  
  private int mDispatchAPILevel = 0;
  
  Element mElement_ALLOCATION;
  
  Element mElement_A_8;
  
  Element mElement_BOOLEAN;
  
  Element mElement_CHAR_2;
  
  Element mElement_CHAR_3;
  
  Element mElement_CHAR_4;
  
  Element mElement_DOUBLE_2;
  
  Element mElement_DOUBLE_3;
  
  Element mElement_DOUBLE_4;
  
  Element mElement_ELEMENT;
  
  Element mElement_F32;
  
  Element mElement_F64;
  
  Element mElement_FLOAT_2;
  
  Element mElement_FLOAT_3;
  
  Element mElement_FLOAT_4;
  
  Element mElement_I16;
  
  Element mElement_I32;
  
  Element mElement_I64;
  
  Element mElement_I8;
  
  Element mElement_INT_2;
  
  Element mElement_INT_3;
  
  Element mElement_INT_4;
  
  Element mElement_LONG_2;
  
  Element mElement_LONG_3;
  
  Element mElement_LONG_4;
  
  Element mElement_MATRIX_2X2;
  
  Element mElement_MATRIX_3X3;
  
  Element mElement_MATRIX_4X4;
  
  Element mElement_RGBA_4444;
  
  Element mElement_RGBA_5551;
  
  Element mElement_RGBA_8888;
  
  Element mElement_RGB_565;
  
  Element mElement_RGB_888;
  
  Element mElement_SAMPLER;
  
  Element mElement_SCRIPT;
  
  Element mElement_SHORT_2;
  
  Element mElement_SHORT_3;
  
  Element mElement_SHORT_4;
  
  Element mElement_TYPE;
  
  Element mElement_U16;
  
  Element mElement_U32;
  
  Element mElement_U64;
  
  Element mElement_U8;
  
  Element mElement_UCHAR_2;
  
  Element mElement_UCHAR_3;
  
  Element mElement_UCHAR_4;
  
  Element mElement_UINT_2;
  
  Element mElement_UINT_3;
  
  Element mElement_UINT_4;
  
  Element mElement_ULONG_2;
  
  Element mElement_ULONG_3;
  
  Element mElement_ULONG_4;
  
  Element mElement_USHORT_2;
  
  Element mElement_USHORT_3;
  
  Element mElement_USHORT_4;
  
  private boolean mEnableMultiInput = false;
  
  RSErrorHandler mErrorCallback = null;
  
  long mIncCon;
  
  boolean mIncLoaded;
  
  private boolean mIsProcessContext = false;
  
  RSMessageHandler mMessageCallback = null;
  
  MessageThread mMessageThread;
  
  private String mNativeLibDir;
  
  ReentrantReadWriteLock mRWLock;
  
  Sampler mSampler_CLAMP_LINEAR;
  
  Sampler mSampler_CLAMP_LINEAR_MIP_LINEAR;
  
  Sampler mSampler_CLAMP_NEAREST;
  
  Sampler mSampler_MIRRORED_REPEAT_LINEAR;
  
  Sampler mSampler_MIRRORED_REPEAT_LINEAR_MIP_LINEAR;
  
  Sampler mSampler_MIRRORED_REPEAT_NEAREST;
  
  Sampler mSampler_WRAP_LINEAR;
  
  Sampler mSampler_WRAP_LINEAR_MIP_LINEAR;
  
  Sampler mSampler_WRAP_NEAREST;
  
  static {
    mBlackList = "";
    lock = new Object();
    sNative = -1;
    sSdkVersion = -1;
    useIOlib = false;
  }
  
  RenderScript(Context paramContext) {
    if (paramContext != null) {
      paramContext = paramContext.getApplicationContext();
      this.mApplicationContext = paramContext;
      this.mNativeLibDir = (paramContext.getApplicationInfo()).nativeLibraryDir;
    } 
    this.mIncCon = 0L;
    this.mIncLoaded = false;
    this.mRWLock = new ReentrantReadWriteLock();
  }
  
  public static RenderScript create(Context paramContext) {
    return create(paramContext, ContextType.NORMAL);
  }
  
  public static RenderScript create(Context paramContext, int paramInt) {
    return create(paramContext, paramInt, ContextType.NORMAL, 0);
  }
  
  public static RenderScript create(Context paramContext, int paramInt, ContextType paramContextType) {
    return create(paramContext, paramInt, paramContextType, 0);
  }
  
  public static RenderScript create(Context paramContext, int paramInt1, ContextType paramContextType, int paramInt2) {
    synchronized (mProcessContextList) {
      for (RenderScript renderScript1 : mProcessContextList) {
        if (renderScript1.mContextType == paramContextType && renderScript1.mContextFlags == paramInt2 && renderScript1.mContextSdkVersion == paramInt1)
          return renderScript1; 
      } 
      RenderScript renderScript = internalCreate(paramContext, paramInt1, paramContextType, paramInt2);
      renderScript.mIsProcessContext = true;
      mProcessContextList.add(renderScript);
      return renderScript;
    } 
  }
  
  public static RenderScript create(Context paramContext, ContextType paramContextType) {
    return create(paramContext, paramContextType, 0);
  }
  
  public static RenderScript create(Context paramContext, ContextType paramContextType, int paramInt) {
    return create(paramContext, (paramContext.getApplicationInfo()).targetSdkVersion, paramContextType, paramInt);
  }
  
  public static RenderScript createMultiContext(Context paramContext, ContextType paramContextType, int paramInt1, int paramInt2) {
    return internalCreate(paramContext, paramInt2, paramContextType, paramInt1);
  }
  
  public static void forceCompat() {
    sNative = 0;
  }
  
  public static int getPointerSize() {
    synchronized (lock) {
      if (sInitialized)
        return sPointerSize; 
      throw new RSInvalidStateException("Calling getPointerSize() before any RenderScript instantiated");
    } 
  }
  
  private void helpDestroy() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mDestroyed : Z
    //   6: istore #4
    //   8: iconst_0
    //   9: istore_2
    //   10: iload #4
    //   12: ifne -> 143
    //   15: aload_0
    //   16: iconst_1
    //   17: putfield mDestroyed : Z
    //   20: iconst_1
    //   21: istore_1
    //   22: goto -> 25
    //   25: aload_0
    //   26: monitorexit
    //   27: iload_1
    //   28: ifeq -> 130
    //   31: aload_0
    //   32: invokevirtual nContextFinish : ()V
    //   35: aload_0
    //   36: getfield mIncCon : J
    //   39: lconst_0
    //   40: lcmp
    //   41: ifeq -> 57
    //   44: aload_0
    //   45: invokevirtual nIncContextFinish : ()V
    //   48: aload_0
    //   49: invokevirtual nIncContextDestroy : ()V
    //   52: aload_0
    //   53: lconst_0
    //   54: putfield mIncCon : J
    //   57: aload_0
    //   58: aload_0
    //   59: getfield mContext : J
    //   62: invokevirtual nContextDeinitToClient : (J)V
    //   65: aload_0
    //   66: getfield mMessageThread : Landroidx/renderscript/RenderScript$MessageThread;
    //   69: iconst_0
    //   70: putfield mRun : Z
    //   73: aload_0
    //   74: getfield mMessageThread : Landroidx/renderscript/RenderScript$MessageThread;
    //   77: invokevirtual interrupt : ()V
    //   80: iconst_0
    //   81: istore_3
    //   82: iload_2
    //   83: istore_1
    //   84: iload_3
    //   85: istore_2
    //   86: iload_1
    //   87: ifne -> 107
    //   90: aload_0
    //   91: getfield mMessageThread : Landroidx/renderscript/RenderScript$MessageThread;
    //   94: invokevirtual join : ()V
    //   97: iconst_1
    //   98: istore_1
    //   99: goto -> 86
    //   102: iconst_1
    //   103: istore_2
    //   104: goto -> 86
    //   107: iload_2
    //   108: ifeq -> 126
    //   111: ldc 'RenderScript_jni'
    //   113: ldc_w 'Interrupted during wait for MessageThread to join'
    //   116: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   119: pop
    //   120: invokestatic currentThread : ()Ljava/lang/Thread;
    //   123: invokevirtual interrupt : ()V
    //   126: aload_0
    //   127: invokevirtual nContextDestroy : ()V
    //   130: return
    //   131: astore #5
    //   133: aload_0
    //   134: monitorexit
    //   135: aload #5
    //   137: athrow
    //   138: astore #5
    //   140: goto -> 102
    //   143: iconst_0
    //   144: istore_1
    //   145: goto -> 25
    // Exception table:
    //   from	to	target	type
    //   2	8	131	finally
    //   15	20	131	finally
    //   25	27	131	finally
    //   90	97	138	java/lang/InterruptedException
    //   133	135	131	finally
  }
  
  private static RenderScript internalCreate(Context paramContext, int paramInt1, ContextType paramContextType, int paramInt2) {
    RenderScript renderScript = new RenderScript(paramContext);
    int i = sSdkVersion;
    if (i == -1) {
      sSdkVersion = paramInt1;
    } else if (i != paramInt1) {
      throw new RSRuntimeException("Can't have two contexts with different SDK versions in support lib");
    } 
    useNative = setupNative(sSdkVersion, paramContext);
    synchronized (lock) {
      StringBuilder stringBuilder1;
      boolean bool = sInitialized;
      StringBuilder stringBuilder2 = null;
      if (!bool) {
        try {
          Class<?> clazz = Class.forName("dalvik.system.VMRuntime");
          sRuntime = clazz.getDeclaredMethod("getRuntime", new Class[0]).invoke(null, new Object[0]);
          registerNativeAllocation = clazz.getDeclaredMethod("registerNativeAllocation", new Class[] { int.class });
          registerNativeFree = clazz.getDeclaredMethod("registerNativeFree", new Class[] { int.class });
          sUseGCHooks = true;
        } catch (Exception exception) {
          Log.e("RenderScript_jni", "No GC methods");
          sUseGCHooks = false;
        } 
        try {
          if (Build.VERSION.SDK_INT < 23 && renderScript.mNativeLibDir != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(renderScript.mNativeLibDir);
            stringBuilder.append("/librsjni_androidx.so");
            System.load(stringBuilder.toString());
          } else {
            System.loadLibrary("rsjni_androidx");
          } 
          sInitialized = true;
          sPointerSize = rsnSystemGetPointerSize();
        } catch (UnsatisfiedLinkError unsatisfiedLinkError) {
          stringBuilder2 = new StringBuilder();
          stringBuilder2.append("Error loading RS jni library: ");
          stringBuilder2.append(unsatisfiedLinkError);
          Log.e("RenderScript_jni", stringBuilder2.toString());
          stringBuilder2 = new StringBuilder();
          stringBuilder2.append("Error loading RS jni library: ");
          stringBuilder2.append(unsatisfiedLinkError);
          stringBuilder2.append(" Support lib API: ");
          stringBuilder2.append(2301);
          throw new RSRuntimeException(stringBuilder2.toString());
        } 
      } 
      if (useNative) {
        Log.v("RenderScript_jni", "RS native mode");
      } else {
        Log.v("RenderScript_jni", "RS compat mode");
      } 
      if (Build.VERSION.SDK_INT >= 14)
        useIOlib = true; 
      if (paramInt1 < Build.VERSION.SDK_INT) {
        i = Build.VERSION.SDK_INT;
      } else {
        i = paramInt1;
      } 
      null = stringBuilder2;
      if (Build.VERSION.SDK_INT < 23) {
        null = stringBuilder2;
        if (renderScript.mNativeLibDir != null) {
          null = new StringBuilder();
          null.append(renderScript.mNativeLibDir);
          null.append("/libRSSupport.so");
          null = null.toString();
        } 
      } 
      if (!renderScript.nLoadSO(useNative, i, (String)null)) {
        if (useNative) {
          Log.v("RenderScript_jni", "Unable to load libRS.so, falling back to compat mode");
          useNative = false;
        } 
        try {
          if (Build.VERSION.SDK_INT < 23 && renderScript.mNativeLibDir != null) {
            System.load((String)null);
          } else {
            System.loadLibrary("RSSupport");
          } 
          if (!renderScript.nLoadSO(false, i, (String)null)) {
            Log.e("RenderScript_jni", "Error loading RS Compat library: nLoadSO() failed; Support lib version: 2301");
            throw new RSRuntimeException("Error loading libRSSupport library, Support lib version: 2301");
          } 
        } catch (UnsatisfiedLinkError unsatisfiedLinkError) {
          stringBuilder1 = new StringBuilder();
          stringBuilder1.append("Error loading RS Compat library: ");
          stringBuilder1.append(unsatisfiedLinkError);
          stringBuilder1.append(" Support lib version: ");
          stringBuilder1.append(2301);
          Log.e("RenderScript_jni", stringBuilder1.toString());
          stringBuilder1 = new StringBuilder();
          stringBuilder1.append("Error loading RS Compat library: ");
          stringBuilder1.append(unsatisfiedLinkError);
          stringBuilder1.append(" Support lib version: ");
          stringBuilder1.append(2301);
          throw new RSRuntimeException(stringBuilder1.toString());
        } 
      } 
      if (useIOlib) {
        try {
          System.loadLibrary("RSSupportIO");
        } catch (UnsatisfiedLinkError unsatisfiedLinkError) {
          useIOlib = false;
        } 
        if (!useIOlib || !renderScript.nLoadIOSO()) {
          Log.v("RenderScript_jni", "Unable to load libRSSupportIO.so, USAGE_IO not supported");
          useIOlib = false;
        } 
      } 
      if (i >= 23) {
        renderScript.mEnableMultiInput = true;
        try {
          System.loadLibrary("blasV8");
        } catch (UnsatisfiedLinkError unsatisfiedLinkError) {
          stringBuilder2 = new StringBuilder();
          stringBuilder2.append("Unable to load BLAS lib, ONLY BNNM will be supported: ");
          stringBuilder2.append(unsatisfiedLinkError);
          Log.v("RenderScript_jni", stringBuilder2.toString());
        } 
      } 
      long l = renderScript.nContextCreate(renderScript.nDeviceCreate(), 0, paramInt1, ((ContextType)stringBuilder1).mID, renderScript.mNativeLibDir);
      renderScript.mContext = l;
      renderScript.mContextType = (ContextType)stringBuilder1;
      renderScript.mContextFlags = paramInt2;
      renderScript.mContextSdkVersion = paramInt1;
      renderScript.mDispatchAPILevel = i;
      if (l != 0L) {
        null = new MessageThread(renderScript);
        renderScript.mMessageThread = (MessageThread)null;
        null.start();
        return renderScript;
      } 
      throw new RSDriverException("Failed to create RS context.");
    } 
  }
  
  public static void releaseAllContexts() {
    synchronized (mProcessContextList) {
      ArrayList<RenderScript> arrayList = mProcessContextList;
      mProcessContextList = new ArrayList<RenderScript>();
      for (RenderScript renderScript : arrayList) {
        renderScript.mIsProcessContext = false;
        renderScript.destroy();
      } 
      arrayList.clear();
      return;
    } 
  }
  
  static native int rsnSystemGetPointerSize();
  
  public static void setBlackList(String paramString) {
    if (paramString != null)
      mBlackList = paramString; 
  }
  
  public static void setupDiskCache(File paramFile) {
    paramFile = new File(paramFile, "com.android.renderscript.cache");
    mCachePath = paramFile.getAbsolutePath();
    paramFile.mkdirs();
  }
  
  private static boolean setupNative(int paramInt, Context paramContext) {
    // Byte code:
    //   0: getstatic android/os/Build$VERSION.SDK_INT : I
    //   3: iload_0
    //   4: if_icmpge -> 19
    //   7: getstatic android/os/Build$VERSION.SDK_INT : I
    //   10: bipush #21
    //   12: if_icmpge -> 19
    //   15: iconst_0
    //   16: putstatic androidx/renderscript/RenderScript.sNative : I
    //   19: getstatic androidx/renderscript/RenderScript.sNative : I
    //   22: iconst_m1
    //   23: if_icmpne -> 232
    //   26: ldc_w 'android.os.SystemProperties'
    //   29: invokestatic forName : (Ljava/lang/String;)Ljava/lang/Class;
    //   32: ldc_w 'getInt'
    //   35: iconst_2
    //   36: anewarray java/lang/Class
    //   39: dup
    //   40: iconst_0
    //   41: ldc_w java/lang/String
    //   44: aastore
    //   45: dup
    //   46: iconst_1
    //   47: getstatic java/lang/Integer.TYPE : Ljava/lang/Class;
    //   50: aastore
    //   51: invokevirtual getDeclaredMethod : (Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   54: aconst_null
    //   55: iconst_2
    //   56: anewarray java/lang/Object
    //   59: dup
    //   60: iconst_0
    //   61: ldc_w 'debug.rs.forcecompat'
    //   64: aastore
    //   65: dup
    //   66: iconst_1
    //   67: new java/lang/Integer
    //   70: dup
    //   71: iconst_0
    //   72: invokespecial <init> : (I)V
    //   75: aastore
    //   76: invokevirtual invoke : (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   79: checkcast java/lang/Integer
    //   82: invokevirtual intValue : ()I
    //   85: istore_0
    //   86: goto -> 91
    //   89: iconst_0
    //   90: istore_0
    //   91: getstatic android/os/Build$VERSION.SDK_INT : I
    //   94: bipush #19
    //   96: if_icmplt -> 110
    //   99: iload_0
    //   100: ifne -> 110
    //   103: iconst_1
    //   104: putstatic androidx/renderscript/RenderScript.sNative : I
    //   107: goto -> 114
    //   110: iconst_0
    //   111: putstatic androidx/renderscript/RenderScript.sNative : I
    //   114: getstatic androidx/renderscript/RenderScript.sNative : I
    //   117: iconst_1
    //   118: if_icmpne -> 232
    //   121: aload_1
    //   122: invokevirtual getPackageManager : ()Landroid/content/pm/PackageManager;
    //   125: aload_1
    //   126: invokevirtual getPackageName : ()Ljava/lang/String;
    //   129: sipush #128
    //   132: invokevirtual getApplicationInfo : (Ljava/lang/String;I)Landroid/content/pm/ApplicationInfo;
    //   135: astore_1
    //   136: ldc_w 'android.renderscript.RenderScript'
    //   139: invokestatic forName : (Ljava/lang/String;)Ljava/lang/Class;
    //   142: ldc_w 'getMinorID'
    //   145: iconst_0
    //   146: anewarray java/lang/Class
    //   149: invokevirtual getDeclaredMethod : (Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   152: aconst_null
    //   153: iconst_0
    //   154: anewarray java/lang/Object
    //   157: invokevirtual invoke : (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   160: checkcast java/lang/Long
    //   163: invokevirtual longValue : ()J
    //   166: lstore_2
    //   167: goto -> 172
    //   170: lconst_0
    //   171: lstore_2
    //   172: aload_1
    //   173: getfield metaData : Landroid/os/Bundle;
    //   176: ifnull -> 232
    //   179: aload_1
    //   180: getfield metaData : Landroid/os/Bundle;
    //   183: ldc_w 'androidx.renderscript.EnableAsyncTeardown'
    //   186: invokevirtual getBoolean : (Ljava/lang/String;)Z
    //   189: iconst_1
    //   190: if_icmpne -> 203
    //   193: lload_2
    //   194: lconst_0
    //   195: lcmp
    //   196: ifne -> 203
    //   199: iconst_0
    //   200: putstatic androidx/renderscript/RenderScript.sNative : I
    //   203: aload_1
    //   204: getfield metaData : Landroid/os/Bundle;
    //   207: ldc_w 'androidx.renderscript.EnableBlurWorkaround'
    //   210: invokevirtual getBoolean : (Ljava/lang/String;)Z
    //   213: iconst_1
    //   214: if_icmpne -> 232
    //   217: getstatic android/os/Build$VERSION.SDK_INT : I
    //   220: bipush #19
    //   222: if_icmpgt -> 232
    //   225: iconst_0
    //   226: putstatic androidx/renderscript/RenderScript.sNative : I
    //   229: goto -> 232
    //   232: getstatic androidx/renderscript/RenderScript.sNative : I
    //   235: iconst_1
    //   236: if_icmpne -> 331
    //   239: getstatic androidx/renderscript/RenderScript.mBlackList : Ljava/lang/String;
    //   242: invokevirtual length : ()I
    //   245: ifle -> 329
    //   248: new java/lang/StringBuilder
    //   251: dup
    //   252: invokespecial <init> : ()V
    //   255: astore_1
    //   256: aload_1
    //   257: bipush #40
    //   259: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   262: pop
    //   263: aload_1
    //   264: getstatic android/os/Build.MANUFACTURER : Ljava/lang/String;
    //   267: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   270: pop
    //   271: aload_1
    //   272: bipush #58
    //   274: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   277: pop
    //   278: aload_1
    //   279: getstatic android/os/Build.PRODUCT : Ljava/lang/String;
    //   282: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   285: pop
    //   286: aload_1
    //   287: bipush #58
    //   289: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   292: pop
    //   293: aload_1
    //   294: getstatic android/os/Build.MODEL : Ljava/lang/String;
    //   297: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   300: pop
    //   301: aload_1
    //   302: bipush #41
    //   304: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   307: pop
    //   308: aload_1
    //   309: invokevirtual toString : ()Ljava/lang/String;
    //   312: astore_1
    //   313: getstatic androidx/renderscript/RenderScript.mBlackList : Ljava/lang/String;
    //   316: aload_1
    //   317: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   320: ifeq -> 329
    //   323: iconst_0
    //   324: putstatic androidx/renderscript/RenderScript.sNative : I
    //   327: iconst_0
    //   328: ireturn
    //   329: iconst_1
    //   330: ireturn
    //   331: iconst_0
    //   332: ireturn
    //   333: astore #4
    //   335: goto -> 89
    //   338: astore_1
    //   339: iconst_1
    //   340: ireturn
    //   341: astore #4
    //   343: goto -> 170
    // Exception table:
    //   from	to	target	type
    //   26	86	333	java/lang/Exception
    //   121	136	338	android/content/pm/PackageManager$NameNotFoundException
    //   136	167	341	java/lang/Exception
  }
  
  public void contextDump() {
    validate();
    nContextDump(0);
  }
  
  public void destroy() {
    if (this.mIsProcessContext)
      return; 
    validate();
    helpDestroy();
  }
  
  protected void finalize() throws Throwable {
    helpDestroy();
    super.finalize();
  }
  
  public void finish() {
    nContextFinish();
  }
  
  public final Context getApplicationContext() {
    return this.mApplicationContext;
  }
  
  int getDispatchAPILevel() {
    return this.mDispatchAPILevel;
  }
  
  public RSErrorHandler getErrorHandler() {
    return this.mErrorCallback;
  }
  
  public RSMessageHandler getMessageHandler() {
    return this.mMessageCallback;
  }
  
  boolean isAlive() {
    return (this.mContext != 0L);
  }
  
  boolean isUseNative() {
    return useNative;
  }
  
  void nAllocationCopyFromBitmap(long paramLong, Bitmap paramBitmap) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: aload_3
    //   13: invokevirtual rsnAllocationCopyFromBitmap : (JJLandroid/graphics/Bitmap;)V
    //   16: aload_0
    //   17: monitorexit
    //   18: return
    //   19: astore_3
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_3
    //   23: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	19	finally
  }
  
  void nAllocationCopyToBitmap(long paramLong, Bitmap paramBitmap) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: aload_3
    //   13: invokevirtual rsnAllocationCopyToBitmap : (JJLandroid/graphics/Bitmap;)V
    //   16: aload_0
    //   17: monitorexit
    //   18: return
    //   19: astore_3
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_3
    //   23: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	19	finally
  }
  
  long nAllocationCreateBitmapBackedAllocation(long paramLong, int paramInt1, Bitmap paramBitmap, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: iload_3
    //   13: aload #4
    //   15: iload #5
    //   17: invokevirtual rsnAllocationCreateBitmapBackedAllocation : (JJILandroid/graphics/Bitmap;I)J
    //   20: lstore_1
    //   21: aload_0
    //   22: monitorexit
    //   23: lload_1
    //   24: lreturn
    //   25: astore #4
    //   27: aload_0
    //   28: monitorexit
    //   29: aload #4
    //   31: athrow
    // Exception table:
    //   from	to	target	type
    //   2	21	25	finally
  }
  
  long nAllocationCreateBitmapRef(long paramLong, Bitmap paramBitmap) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: aload_3
    //   13: invokevirtual rsnAllocationCreateBitmapRef : (JJLandroid/graphics/Bitmap;)J
    //   16: lstore_1
    //   17: aload_0
    //   18: monitorexit
    //   19: lload_1
    //   20: lreturn
    //   21: astore_3
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_3
    //   25: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	21	finally
  }
  
  long nAllocationCreateFromAssetStream(int paramInt1, int paramInt2, int paramInt3) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: iload_1
    //   12: iload_2
    //   13: iload_3
    //   14: invokevirtual rsnAllocationCreateFromAssetStream : (JIII)J
    //   17: lstore #4
    //   19: aload_0
    //   20: monitorexit
    //   21: lload #4
    //   23: lreturn
    //   24: astore #6
    //   26: aload_0
    //   27: monitorexit
    //   28: aload #6
    //   30: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	24	finally
  }
  
  long nAllocationCreateFromBitmap(long paramLong, int paramInt1, Bitmap paramBitmap, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: iload_3
    //   13: aload #4
    //   15: iload #5
    //   17: invokevirtual rsnAllocationCreateFromBitmap : (JJILandroid/graphics/Bitmap;I)J
    //   20: lstore_1
    //   21: aload_0
    //   22: monitorexit
    //   23: lload_1
    //   24: lreturn
    //   25: astore #4
    //   27: aload_0
    //   28: monitorexit
    //   29: aload #4
    //   31: athrow
    // Exception table:
    //   from	to	target	type
    //   2	21	25	finally
  }
  
  long nAllocationCreateTyped(long paramLong1, int paramInt1, int paramInt2, long paramLong2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: iload_3
    //   13: iload #4
    //   15: lload #5
    //   17: invokevirtual rsnAllocationCreateTyped : (JJIIJ)J
    //   20: lstore_1
    //   21: aload_0
    //   22: monitorexit
    //   23: lload_1
    //   24: lreturn
    //   25: astore #7
    //   27: aload_0
    //   28: monitorexit
    //   29: aload #7
    //   31: athrow
    // Exception table:
    //   from	to	target	type
    //   2	21	25	finally
  }
  
  long nAllocationCubeCreateFromBitmap(long paramLong, int paramInt1, Bitmap paramBitmap, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: iload_3
    //   13: aload #4
    //   15: iload #5
    //   17: invokevirtual rsnAllocationCubeCreateFromBitmap : (JJILandroid/graphics/Bitmap;I)J
    //   20: lstore_1
    //   21: aload_0
    //   22: monitorexit
    //   23: lload_1
    //   24: lreturn
    //   25: astore #4
    //   27: aload_0
    //   28: monitorexit
    //   29: aload #4
    //   31: athrow
    // Exception table:
    //   from	to	target	type
    //   2	21	25	finally
  }
  
  void nAllocationData1D(long paramLong, int paramInt1, int paramInt2, int paramInt3, Object paramObject, int paramInt4, Element.DataType paramDataType, int paramInt5, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: iload_3
    //   13: iload #4
    //   15: iload #5
    //   17: aload #6
    //   19: iload #7
    //   21: aload #8
    //   23: getfield mID : I
    //   26: iload #9
    //   28: iload #10
    //   30: invokevirtual rsnAllocationData1D : (JJIIILjava/lang/Object;IIIZ)V
    //   33: aload_0
    //   34: monitorexit
    //   35: return
    //   36: astore #6
    //   38: aload_0
    //   39: monitorexit
    //   40: aload #6
    //   42: athrow
    // Exception table:
    //   from	to	target	type
    //   2	33	36	finally
  }
  
  void nAllocationData2D(long paramLong1, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong2, int paramInt7, int paramInt8, int paramInt9, int paramInt10) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: iload_3
    //   13: iload #4
    //   15: iload #5
    //   17: iload #6
    //   19: iload #7
    //   21: iload #8
    //   23: lload #9
    //   25: iload #11
    //   27: iload #12
    //   29: iload #13
    //   31: iload #14
    //   33: invokevirtual rsnAllocationData2D : (JJIIIIIIJIIII)V
    //   36: aload_0
    //   37: monitorexit
    //   38: return
    //   39: astore #15
    //   41: aload_0
    //   42: monitorexit
    //   43: aload #15
    //   45: athrow
    // Exception table:
    //   from	to	target	type
    //   2	36	39	finally
  }
  
  void nAllocationData2D(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, Object paramObject, int paramInt7, Element.DataType paramDataType, int paramInt8, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: iload_3
    //   13: iload #4
    //   15: iload #5
    //   17: iload #6
    //   19: iload #7
    //   21: iload #8
    //   23: aload #9
    //   25: iload #10
    //   27: aload #11
    //   29: getfield mID : I
    //   32: iload #12
    //   34: iload #13
    //   36: invokevirtual rsnAllocationData2D : (JJIIIIIILjava/lang/Object;IIIZ)V
    //   39: aload_0
    //   40: monitorexit
    //   41: return
    //   42: astore #9
    //   44: aload_0
    //   45: monitorexit
    //   46: aload #9
    //   48: athrow
    // Exception table:
    //   from	to	target	type
    //   2	39	42	finally
  }
  
  void nAllocationData2D(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Bitmap paramBitmap) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: iload_3
    //   13: iload #4
    //   15: iload #5
    //   17: iload #6
    //   19: aload #7
    //   21: invokevirtual rsnAllocationData2D : (JJIIIILandroid/graphics/Bitmap;)V
    //   24: aload_0
    //   25: monitorexit
    //   26: return
    //   27: astore #7
    //   29: aload_0
    //   30: monitorexit
    //   31: aload #7
    //   33: athrow
    // Exception table:
    //   from	to	target	type
    //   2	24	27	finally
  }
  
  void nAllocationData3D(long paramLong1, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, long paramLong2, int paramInt8, int paramInt9, int paramInt10, int paramInt11) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: iload_3
    //   13: iload #4
    //   15: iload #5
    //   17: iload #6
    //   19: iload #7
    //   21: iload #8
    //   23: iload #9
    //   25: lload #10
    //   27: iload #12
    //   29: iload #13
    //   31: iload #14
    //   33: iload #15
    //   35: invokevirtual rsnAllocationData3D : (JJIIIIIIIJIIII)V
    //   38: aload_0
    //   39: monitorexit
    //   40: return
    //   41: astore #16
    //   43: aload_0
    //   44: monitorexit
    //   45: aload #16
    //   47: athrow
    // Exception table:
    //   from	to	target	type
    //   2	38	41	finally
  }
  
  void nAllocationData3D(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, Object paramObject, int paramInt8, Element.DataType paramDataType, int paramInt9, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: iload_3
    //   13: iload #4
    //   15: iload #5
    //   17: iload #6
    //   19: iload #7
    //   21: iload #8
    //   23: iload #9
    //   25: aload #10
    //   27: iload #11
    //   29: aload #12
    //   31: getfield mID : I
    //   34: iload #13
    //   36: iload #14
    //   38: invokevirtual rsnAllocationData3D : (JJIIIIIIILjava/lang/Object;IIIZ)V
    //   41: aload_0
    //   42: monitorexit
    //   43: return
    //   44: astore #10
    //   46: aload_0
    //   47: monitorexit
    //   48: aload #10
    //   50: athrow
    // Exception table:
    //   from	to	target	type
    //   2	41	44	finally
  }
  
  void nAllocationElementData1D(long paramLong, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfbyte, int paramInt4) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: iload_3
    //   13: iload #4
    //   15: iload #5
    //   17: aload #6
    //   19: iload #7
    //   21: invokevirtual rsnAllocationElementData1D : (JJIII[BI)V
    //   24: aload_0
    //   25: monitorexit
    //   26: return
    //   27: astore #6
    //   29: aload_0
    //   30: monitorexit
    //   31: aload #6
    //   33: athrow
    // Exception table:
    //   from	to	target	type
    //   2	24	27	finally
  }
  
  void nAllocationGenerateMipmaps(long paramLong) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: invokevirtual rsnAllocationGenerateMipmaps : (JJ)V
    //   15: aload_0
    //   16: monitorexit
    //   17: return
    //   18: astore_3
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_3
    //   22: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	18	finally
  }
  
  ByteBuffer nAllocationGetByteBuffer(long paramLong, int paramInt1, int paramInt2, int paramInt3) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: iload_3
    //   13: iload #4
    //   15: iload #5
    //   17: invokevirtual rsnAllocationGetByteBuffer : (JJIII)Ljava/nio/ByteBuffer;
    //   20: astore #6
    //   22: aload_0
    //   23: monitorexit
    //   24: aload #6
    //   26: areturn
    //   27: astore #6
    //   29: aload_0
    //   30: monitorexit
    //   31: aload #6
    //   33: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	27	finally
  }
  
  long nAllocationGetStride(long paramLong) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: invokevirtual rsnAllocationGetStride : (JJ)J
    //   15: lstore_1
    //   16: aload_0
    //   17: monitorexit
    //   18: lload_1
    //   19: lreturn
    //   20: astore_3
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	20	finally
  }
  
  long nAllocationGetType(long paramLong) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: invokevirtual rsnAllocationGetType : (JJ)J
    //   15: lstore_1
    //   16: aload_0
    //   17: monitorexit
    //   18: lload_1
    //   19: lreturn
    //   20: astore_3
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	20	finally
  }
  
  void nAllocationIoReceive(long paramLong) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: invokevirtual rsnAllocationIoReceive : (JJ)V
    //   15: aload_0
    //   16: monitorexit
    //   17: return
    //   18: astore_3
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_3
    //   22: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	18	finally
  }
  
  void nAllocationIoSend(long paramLong) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: invokevirtual rsnAllocationIoSend : (JJ)V
    //   15: aload_0
    //   16: monitorexit
    //   17: return
    //   18: astore_3
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_3
    //   22: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	18	finally
  }
  
  void nAllocationRead(long paramLong, Object paramObject, Element.DataType paramDataType, int paramInt, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: aload_3
    //   13: aload #4
    //   15: getfield mID : I
    //   18: iload #5
    //   20: iload #6
    //   22: invokevirtual rsnAllocationRead : (JJLjava/lang/Object;IIZ)V
    //   25: aload_0
    //   26: monitorexit
    //   27: return
    //   28: astore_3
    //   29: aload_0
    //   30: monitorexit
    //   31: aload_3
    //   32: athrow
    // Exception table:
    //   from	to	target	type
    //   2	25	28	finally
  }
  
  void nAllocationRead1D(long paramLong, int paramInt1, int paramInt2, int paramInt3, Object paramObject, int paramInt4, Element.DataType paramDataType, int paramInt5, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: iload_3
    //   13: iload #4
    //   15: iload #5
    //   17: aload #6
    //   19: iload #7
    //   21: aload #8
    //   23: getfield mID : I
    //   26: iload #9
    //   28: iload #10
    //   30: invokevirtual rsnAllocationRead1D : (JJIIILjava/lang/Object;IIIZ)V
    //   33: aload_0
    //   34: monitorexit
    //   35: return
    //   36: astore #6
    //   38: aload_0
    //   39: monitorexit
    //   40: aload #6
    //   42: athrow
    // Exception table:
    //   from	to	target	type
    //   2	33	36	finally
  }
  
  void nAllocationRead2D(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, Object paramObject, int paramInt7, Element.DataType paramDataType, int paramInt8, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: iload_3
    //   13: iload #4
    //   15: iload #5
    //   17: iload #6
    //   19: iload #7
    //   21: iload #8
    //   23: aload #9
    //   25: iload #10
    //   27: aload #11
    //   29: getfield mID : I
    //   32: iload #12
    //   34: iload #13
    //   36: invokevirtual rsnAllocationRead2D : (JJIIIIIILjava/lang/Object;IIIZ)V
    //   39: aload_0
    //   40: monitorexit
    //   41: return
    //   42: astore #9
    //   44: aload_0
    //   45: monitorexit
    //   46: aload #9
    //   48: athrow
    // Exception table:
    //   from	to	target	type
    //   2	39	42	finally
  }
  
  void nAllocationResize1D(long paramLong, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: iload_3
    //   13: invokevirtual rsnAllocationResize1D : (JJI)V
    //   16: aload_0
    //   17: monitorexit
    //   18: return
    //   19: astore #4
    //   21: aload_0
    //   22: monitorexit
    //   23: aload #4
    //   25: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	19	finally
  }
  
  void nAllocationResize2D(long paramLong, int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: iload_3
    //   13: iload #4
    //   15: invokevirtual rsnAllocationResize2D : (JJII)V
    //   18: aload_0
    //   19: monitorexit
    //   20: return
    //   21: astore #5
    //   23: aload_0
    //   24: monitorexit
    //   25: aload #5
    //   27: athrow
    // Exception table:
    //   from	to	target	type
    //   2	18	21	finally
  }
  
  void nAllocationSetSurface(long paramLong, Surface paramSurface) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: aload_3
    //   13: invokevirtual rsnAllocationSetSurface : (JJLandroid/view/Surface;)V
    //   16: aload_0
    //   17: monitorexit
    //   18: return
    //   19: astore_3
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_3
    //   23: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	19	finally
  }
  
  void nAllocationSyncAll(long paramLong, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: iload_3
    //   13: invokevirtual rsnAllocationSyncAll : (JJI)V
    //   16: aload_0
    //   17: monitorexit
    //   18: return
    //   19: astore #4
    //   21: aload_0
    //   22: monitorexit
    //   23: aload #4
    //   25: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	19	finally
  }
  
  long nClosureCreate(long paramLong1, long paramLong2, long[] paramArrayOflong1, long[] paramArrayOflong2, int[] paramArrayOfint, long[] paramArrayOflong3, long[] paramArrayOflong4) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: lload_3
    //   13: aload #5
    //   15: aload #6
    //   17: aload #7
    //   19: aload #8
    //   21: aload #9
    //   23: invokevirtual rsnClosureCreate : (JJJ[J[J[I[J[J)J
    //   26: lstore_1
    //   27: lload_1
    //   28: lconst_0
    //   29: lcmp
    //   30: ifeq -> 37
    //   33: aload_0
    //   34: monitorexit
    //   35: lload_1
    //   36: lreturn
    //   37: new androidx/renderscript/RSRuntimeException
    //   40: dup
    //   41: ldc_w 'Failed creating closure.'
    //   44: invokespecial <init> : (Ljava/lang/String;)V
    //   47: athrow
    //   48: astore #5
    //   50: aload_0
    //   51: monitorexit
    //   52: aload #5
    //   54: athrow
    // Exception table:
    //   from	to	target	type
    //   2	27	48	finally
    //   37	48	48	finally
  }
  
  void nClosureSetArg(long paramLong1, int paramInt1, long paramLong2, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: iload_3
    //   13: lload #4
    //   15: iload #6
    //   17: invokevirtual rsnClosureSetArg : (JJIJI)V
    //   20: aload_0
    //   21: monitorexit
    //   22: return
    //   23: astore #7
    //   25: aload_0
    //   26: monitorexit
    //   27: aload #7
    //   29: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	23	finally
  }
  
  void nClosureSetGlobal(long paramLong1, long paramLong2, long paramLong3, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: lload_3
    //   13: lload #5
    //   15: iload #7
    //   17: invokevirtual rsnClosureSetGlobal : (JJJJI)V
    //   20: aload_0
    //   21: monitorexit
    //   22: return
    //   23: astore #8
    //   25: aload_0
    //   26: monitorexit
    //   27: aload #8
    //   29: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	23	finally
  }
  
  long nContextCreate(long paramLong, int paramInt1, int paramInt2, int paramInt3, String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: lload_1
    //   4: iload_3
    //   5: iload #4
    //   7: iload #5
    //   9: aload #6
    //   11: invokevirtual rsnContextCreate : (JIIILjava/lang/String;)J
    //   14: lstore_1
    //   15: aload_0
    //   16: monitorexit
    //   17: lload_1
    //   18: lreturn
    //   19: astore #6
    //   21: aload_0
    //   22: monitorexit
    //   23: aload #6
    //   25: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	19	finally
  }
  
  native void nContextDeinitToClient(long paramLong);
  
  void nContextDestroy() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: getfield mRWLock : Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   10: invokevirtual writeLock : ()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
    //   13: astore_3
    //   14: aload_3
    //   15: invokevirtual lock : ()V
    //   18: aload_0
    //   19: getfield mContext : J
    //   22: lstore_1
    //   23: aload_0
    //   24: lconst_0
    //   25: putfield mContext : J
    //   28: aload_3
    //   29: invokevirtual unlock : ()V
    //   32: aload_0
    //   33: lload_1
    //   34: invokevirtual rsnContextDestroy : (J)V
    //   37: aload_0
    //   38: monitorexit
    //   39: return
    //   40: astore_3
    //   41: aload_0
    //   42: monitorexit
    //   43: aload_3
    //   44: athrow
    // Exception table:
    //   from	to	target	type
    //   2	37	40	finally
  }
  
  void nContextDump(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: iload_1
    //   12: invokevirtual rsnContextDump : (JI)V
    //   15: aload_0
    //   16: monitorexit
    //   17: return
    //   18: astore_2
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_2
    //   22: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	18	finally
  }
  
  void nContextFinish() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: invokevirtual rsnContextFinish : (J)V
    //   14: aload_0
    //   15: monitorexit
    //   16: return
    //   17: astore_1
    //   18: aload_0
    //   19: monitorexit
    //   20: aload_1
    //   21: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	17	finally
  }
  
  native String nContextGetErrorMessage(long paramLong);
  
  native int nContextGetUserMessage(long paramLong, int[] paramArrayOfint);
  
  native void nContextInitToClient(long paramLong);
  
  native int nContextPeekMessage(long paramLong, int[] paramArrayOfint);
  
  void nContextSendMessage(int paramInt, int[] paramArrayOfint) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: iload_1
    //   12: aload_2
    //   13: invokevirtual rsnContextSendMessage : (JI[I)V
    //   16: aload_0
    //   17: monitorexit
    //   18: return
    //   19: astore_2
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_2
    //   23: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	19	finally
  }
  
  void nContextSetPriority(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: iload_1
    //   12: invokevirtual rsnContextSetPriority : (JI)V
    //   15: aload_0
    //   16: monitorexit
    //   17: return
    //   18: astore_2
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_2
    //   22: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	18	finally
  }
  
  native long nDeviceCreate();
  
  native void nDeviceDestroy(long paramLong);
  
  native void nDeviceSetConfig(long paramLong, int paramInt1, int paramInt2);
  
  long nElementCreate(long paramLong, int paramInt1, boolean paramBoolean, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: iload_3
    //   13: iload #4
    //   15: iload #5
    //   17: invokevirtual rsnElementCreate : (JJIZI)J
    //   20: lstore_1
    //   21: aload_0
    //   22: monitorexit
    //   23: lload_1
    //   24: lreturn
    //   25: astore #6
    //   27: aload_0
    //   28: monitorexit
    //   29: aload #6
    //   31: athrow
    // Exception table:
    //   from	to	target	type
    //   2	21	25	finally
  }
  
  long nElementCreate2(long[] paramArrayOflong, String[] paramArrayOfString, int[] paramArrayOfint) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: aload_1
    //   12: aload_2
    //   13: aload_3
    //   14: invokevirtual rsnElementCreate2 : (J[J[Ljava/lang/String;[I)J
    //   17: lstore #4
    //   19: aload_0
    //   20: monitorexit
    //   21: lload #4
    //   23: lreturn
    //   24: astore_1
    //   25: aload_0
    //   26: monitorexit
    //   27: aload_1
    //   28: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	24	finally
  }
  
  void nElementGetNativeData(long paramLong, int[] paramArrayOfint) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: aload_3
    //   13: invokevirtual rsnElementGetNativeData : (JJ[I)V
    //   16: aload_0
    //   17: monitorexit
    //   18: return
    //   19: astore_3
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_3
    //   23: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	19	finally
  }
  
  void nElementGetSubElements(long paramLong, long[] paramArrayOflong, String[] paramArrayOfString, int[] paramArrayOfint) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: aload_3
    //   13: aload #4
    //   15: aload #5
    //   17: invokevirtual rsnElementGetSubElements : (JJ[J[Ljava/lang/String;[I)V
    //   20: aload_0
    //   21: monitorexit
    //   22: return
    //   23: astore_3
    //   24: aload_0
    //   25: monitorexit
    //   26: aload_3
    //   27: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	23	finally
  }
  
  long nIncAllocationCreateTyped(long paramLong1, long paramLong2, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: aload_0
    //   12: getfield mIncCon : J
    //   15: lload_1
    //   16: lload_3
    //   17: iload #5
    //   19: invokevirtual rsnIncAllocationCreateTyped : (JJJJI)J
    //   22: lstore_1
    //   23: aload_0
    //   24: monitorexit
    //   25: lload_1
    //   26: lreturn
    //   27: astore #6
    //   29: aload_0
    //   30: monitorexit
    //   31: aload #6
    //   33: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	27	finally
  }
  
  long nIncContextCreate(long paramLong, int paramInt1, int paramInt2, int paramInt3) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: lload_1
    //   4: iload_3
    //   5: iload #4
    //   7: iload #5
    //   9: invokevirtual rsnIncContextCreate : (JIII)J
    //   12: lstore_1
    //   13: aload_0
    //   14: monitorexit
    //   15: lload_1
    //   16: lreturn
    //   17: astore #6
    //   19: aload_0
    //   20: monitorexit
    //   21: aload #6
    //   23: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	17	finally
  }
  
  void nIncContextDestroy() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: getfield mRWLock : Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   10: invokevirtual writeLock : ()Ljava/util/concurrent/locks/ReentrantReadWriteLock$WriteLock;
    //   13: astore_3
    //   14: aload_3
    //   15: invokevirtual lock : ()V
    //   18: aload_0
    //   19: getfield mIncCon : J
    //   22: lstore_1
    //   23: aload_0
    //   24: lconst_0
    //   25: putfield mIncCon : J
    //   28: aload_3
    //   29: invokevirtual unlock : ()V
    //   32: aload_0
    //   33: lload_1
    //   34: invokevirtual rsnIncContextDestroy : (J)V
    //   37: aload_0
    //   38: monitorexit
    //   39: return
    //   40: astore_3
    //   41: aload_0
    //   42: monitorexit
    //   43: aload_3
    //   44: athrow
    // Exception table:
    //   from	to	target	type
    //   2	37	40	finally
  }
  
  void nIncContextFinish() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mIncCon : J
    //   11: invokevirtual rsnIncContextFinish : (J)V
    //   14: aload_0
    //   15: monitorexit
    //   16: return
    //   17: astore_1
    //   18: aload_0
    //   19: monitorexit
    //   20: aload_1
    //   21: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	17	finally
  }
  
  native long nIncDeviceCreate();
  
  native void nIncDeviceDestroy(long paramLong);
  
  long nIncElementCreate(long paramLong, int paramInt1, boolean paramBoolean, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mIncCon : J
    //   11: lload_1
    //   12: iload_3
    //   13: iload #4
    //   15: iload #5
    //   17: invokevirtual rsnIncElementCreate : (JJIZI)J
    //   20: lstore_1
    //   21: aload_0
    //   22: monitorexit
    //   23: lload_1
    //   24: lreturn
    //   25: astore #6
    //   27: aload_0
    //   28: monitorexit
    //   29: aload #6
    //   31: athrow
    // Exception table:
    //   from	to	target	type
    //   2	21	25	finally
  }
  
  native boolean nIncLoadSO(int paramInt, String paramString);
  
  void nIncObjDestroy(long paramLong) {
    long l = this.mIncCon;
    if (l != 0L)
      rsnIncObjDestroy(l, paramLong); 
  }
  
  long nIncTypeCreate(long paramLong, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, int paramInt4) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mIncCon : J
    //   11: lload_1
    //   12: iload_3
    //   13: iload #4
    //   15: iload #5
    //   17: iload #6
    //   19: iload #7
    //   21: iload #8
    //   23: invokevirtual rsnIncTypeCreate : (JJIIIZZI)J
    //   26: lstore_1
    //   27: aload_0
    //   28: monitorexit
    //   29: lload_1
    //   30: lreturn
    //   31: astore #9
    //   33: aload_0
    //   34: monitorexit
    //   35: aload #9
    //   37: athrow
    // Exception table:
    //   from	to	target	type
    //   2	27	31	finally
  }
  
  long nInvokeClosureCreate(long paramLong, byte[] paramArrayOfbyte, long[] paramArrayOflong1, long[] paramArrayOflong2, int[] paramArrayOfint) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: aload_3
    //   13: aload #4
    //   15: aload #5
    //   17: aload #6
    //   19: invokevirtual rsnInvokeClosureCreate : (JJ[B[J[J[I)J
    //   22: lstore_1
    //   23: lload_1
    //   24: lconst_0
    //   25: lcmp
    //   26: ifeq -> 33
    //   29: aload_0
    //   30: monitorexit
    //   31: lload_1
    //   32: lreturn
    //   33: new androidx/renderscript/RSRuntimeException
    //   36: dup
    //   37: ldc_w 'Failed creating closure.'
    //   40: invokespecial <init> : (Ljava/lang/String;)V
    //   43: athrow
    //   44: astore_3
    //   45: aload_0
    //   46: monitorexit
    //   47: aload_3
    //   48: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	44	finally
    //   33	44	44	finally
  }
  
  native boolean nLoadIOSO();
  
  native boolean nLoadSO(boolean paramBoolean, int paramInt, String paramString);
  
  void nObjDestroy(long paramLong) {
    long l = this.mContext;
    if (l != 0L)
      rsnObjDestroy(l, paramLong); 
  }
  
  long nSamplerCreate(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, float paramFloat) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: iload_1
    //   12: iload_2
    //   13: iload_3
    //   14: iload #4
    //   16: iload #5
    //   18: fload #6
    //   20: invokevirtual rsnSamplerCreate : (JIIIIIF)J
    //   23: lstore #7
    //   25: aload_0
    //   26: monitorexit
    //   27: lload #7
    //   29: lreturn
    //   30: astore #9
    //   32: aload_0
    //   33: monitorexit
    //   34: aload #9
    //   36: athrow
    // Exception table:
    //   from	to	target	type
    //   2	25	30	finally
  }
  
  void nScriptBindAllocation(long paramLong1, long paramLong2, int paramInt, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: getfield mContext : J
    //   10: lstore #7
    //   12: iload #6
    //   14: ifeq -> 23
    //   17: aload_0
    //   18: getfield mIncCon : J
    //   21: lstore #7
    //   23: aload_0
    //   24: lload #7
    //   26: lload_1
    //   27: lload_3
    //   28: iload #5
    //   30: iload #6
    //   32: invokevirtual rsnScriptBindAllocation : (JJJIZ)V
    //   35: aload_0
    //   36: monitorexit
    //   37: return
    //   38: astore #9
    //   40: aload_0
    //   41: monitorexit
    //   42: aload #9
    //   44: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	38	finally
    //   17	23	38	finally
    //   23	35	38	finally
  }
  
  long nScriptCCreate(String paramString1, String paramString2, byte[] paramArrayOfbyte, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: aload_1
    //   12: aload_2
    //   13: aload_3
    //   14: iload #4
    //   16: invokevirtual rsnScriptCCreate : (JLjava/lang/String;Ljava/lang/String;[BI)J
    //   19: lstore #5
    //   21: aload_0
    //   22: monitorexit
    //   23: lload #5
    //   25: lreturn
    //   26: astore_1
    //   27: aload_0
    //   28: monitorexit
    //   29: aload_1
    //   30: athrow
    // Exception table:
    //   from	to	target	type
    //   2	21	26	finally
  }
  
  long nScriptFieldIDCreate(long paramLong, int paramInt, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: getfield mContext : J
    //   10: lstore #5
    //   12: iload #4
    //   14: ifeq -> 23
    //   17: aload_0
    //   18: getfield mIncCon : J
    //   21: lstore #5
    //   23: aload_0
    //   24: lload #5
    //   26: lload_1
    //   27: iload_3
    //   28: iload #4
    //   30: invokevirtual rsnScriptFieldIDCreate : (JJIZ)J
    //   33: lstore_1
    //   34: aload_0
    //   35: monitorexit
    //   36: lload_1
    //   37: lreturn
    //   38: astore #7
    //   40: aload_0
    //   41: monitorexit
    //   42: aload #7
    //   44: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	38	finally
    //   17	23	38	finally
    //   23	34	38	finally
  }
  
  void nScriptForEach(long paramLong1, int paramInt, long paramLong2, long paramLong3, byte[] paramArrayOfbyte, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload #8
    //   8: ifnonnull -> 34
    //   11: aload_0
    //   12: aload_0
    //   13: getfield mContext : J
    //   16: aload_0
    //   17: getfield mIncCon : J
    //   20: lload_1
    //   21: iload_3
    //   22: lload #4
    //   24: lload #6
    //   26: iload #9
    //   28: invokevirtual rsnScriptForEach : (JJJIJJZ)V
    //   31: goto -> 56
    //   34: aload_0
    //   35: aload_0
    //   36: getfield mContext : J
    //   39: aload_0
    //   40: getfield mIncCon : J
    //   43: lload_1
    //   44: iload_3
    //   45: lload #4
    //   47: lload #6
    //   49: aload #8
    //   51: iload #9
    //   53: invokevirtual rsnScriptForEach : (JJJIJJ[BZ)V
    //   56: aload_0
    //   57: monitorexit
    //   58: return
    //   59: astore #8
    //   61: aload_0
    //   62: monitorexit
    //   63: aload #8
    //   65: athrow
    // Exception table:
    //   from	to	target	type
    //   2	6	59	finally
    //   11	31	59	finally
    //   34	56	59	finally
  }
  
  void nScriptForEach(long paramLong1, int paramInt, long[] paramArrayOflong, long paramLong2, byte[] paramArrayOfbyte, int[] paramArrayOfint) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mEnableMultiInput : Z
    //   6: ifeq -> 34
    //   9: aload_0
    //   10: invokevirtual validate : ()V
    //   13: aload_0
    //   14: aload_0
    //   15: getfield mContext : J
    //   18: lload_1
    //   19: iload_3
    //   20: aload #4
    //   22: lload #5
    //   24: aload #7
    //   26: aload #8
    //   28: invokevirtual rsnScriptForEach : (JJI[JJ[B[I)V
    //   31: aload_0
    //   32: monitorexit
    //   33: return
    //   34: ldc 'RenderScript_jni'
    //   36: ldc_w 'Multi-input kernels are not supported, please change targetSdkVersion to >= 23'
    //   39: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   42: pop
    //   43: new androidx/renderscript/RSRuntimeException
    //   46: dup
    //   47: ldc_w 'Multi-input kernels are not supported before API 23)'
    //   50: invokespecial <init> : (Ljava/lang/String;)V
    //   53: athrow
    //   54: astore #4
    //   56: aload_0
    //   57: monitorexit
    //   58: aload #4
    //   60: athrow
    // Exception table:
    //   from	to	target	type
    //   2	31	54	finally
    //   34	54	54	finally
  }
  
  void nScriptForEachClipped(long paramLong1, int paramInt1, long paramLong2, long paramLong3, byte[] paramArrayOfbyte, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload #8
    //   8: ifnonnull -> 46
    //   11: aload_0
    //   12: aload_0
    //   13: getfield mContext : J
    //   16: aload_0
    //   17: getfield mIncCon : J
    //   20: lload_1
    //   21: iload_3
    //   22: lload #4
    //   24: lload #6
    //   26: iload #9
    //   28: iload #10
    //   30: iload #11
    //   32: iload #12
    //   34: iload #13
    //   36: iload #14
    //   38: iload #15
    //   40: invokevirtual rsnScriptForEachClipped : (JJJIJJIIIIIIZ)V
    //   43: goto -> 80
    //   46: aload_0
    //   47: aload_0
    //   48: getfield mContext : J
    //   51: aload_0
    //   52: getfield mIncCon : J
    //   55: lload_1
    //   56: iload_3
    //   57: lload #4
    //   59: lload #6
    //   61: aload #8
    //   63: iload #9
    //   65: iload #10
    //   67: iload #11
    //   69: iload #12
    //   71: iload #13
    //   73: iload #14
    //   75: iload #15
    //   77: invokevirtual rsnScriptForEachClipped : (JJJIJJ[BIIIIIIZ)V
    //   80: aload_0
    //   81: monitorexit
    //   82: return
    //   83: astore #8
    //   85: aload_0
    //   86: monitorexit
    //   87: aload #8
    //   89: athrow
    // Exception table:
    //   from	to	target	type
    //   2	6	83	finally
    //   11	43	83	finally
    //   46	80	83	finally
  }
  
  long nScriptGroup2Create(String paramString1, String paramString2, long[] paramArrayOflong) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: aload_1
    //   12: aload_2
    //   13: aload_3
    //   14: invokevirtual rsnScriptGroup2Create : (JLjava/lang/String;Ljava/lang/String;[J)J
    //   17: lstore #4
    //   19: aload_0
    //   20: monitorexit
    //   21: lload #4
    //   23: lreturn
    //   24: astore_1
    //   25: aload_0
    //   26: monitorexit
    //   27: aload_1
    //   28: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	24	finally
  }
  
  void nScriptGroup2Execute(long paramLong) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: invokevirtual rsnScriptGroup2Execute : (JJ)V
    //   15: aload_0
    //   16: monitorexit
    //   17: return
    //   18: astore_3
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_3
    //   22: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	18	finally
  }
  
  long nScriptGroupCreate(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3, long[] paramArrayOflong4, long[] paramArrayOflong5) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: aload_1
    //   12: aload_2
    //   13: aload_3
    //   14: aload #4
    //   16: aload #5
    //   18: invokevirtual rsnScriptGroupCreate : (J[J[J[J[J[J)J
    //   21: lstore #6
    //   23: aload_0
    //   24: monitorexit
    //   25: lload #6
    //   27: lreturn
    //   28: astore_1
    //   29: aload_0
    //   30: monitorexit
    //   31: aload_1
    //   32: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	28	finally
  }
  
  void nScriptGroupExecute(long paramLong) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: invokevirtual rsnScriptGroupExecute : (JJ)V
    //   15: aload_0
    //   16: monitorexit
    //   17: return
    //   18: astore_3
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_3
    //   22: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	18	finally
  }
  
  void nScriptGroupSetInput(long paramLong1, long paramLong2, long paramLong3) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: lload_3
    //   13: lload #5
    //   15: invokevirtual rsnScriptGroupSetInput : (JJJJ)V
    //   18: aload_0
    //   19: monitorexit
    //   20: return
    //   21: astore #7
    //   23: aload_0
    //   24: monitorexit
    //   25: aload #7
    //   27: athrow
    // Exception table:
    //   from	to	target	type
    //   2	18	21	finally
  }
  
  void nScriptGroupSetOutput(long paramLong1, long paramLong2, long paramLong3) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: lload_3
    //   13: lload #5
    //   15: invokevirtual rsnScriptGroupSetOutput : (JJJJ)V
    //   18: aload_0
    //   19: monitorexit
    //   20: return
    //   21: astore #7
    //   23: aload_0
    //   24: monitorexit
    //   25: aload #7
    //   27: athrow
    // Exception table:
    //   from	to	target	type
    //   2	18	21	finally
  }
  
  void nScriptIntrinsicBLAS_BNNM(long paramLong1, int paramInt1, int paramInt2, int paramInt3, long paramLong2, int paramInt4, long paramLong3, int paramInt5, long paramLong4, int paramInt6, int paramInt7, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: aload_0
    //   12: getfield mIncCon : J
    //   15: lload_1
    //   16: iload_3
    //   17: iload #4
    //   19: iload #5
    //   21: lload #6
    //   23: iload #8
    //   25: lload #9
    //   27: iload #11
    //   29: lload #12
    //   31: iload #14
    //   33: iload #15
    //   35: iload #16
    //   37: invokevirtual rsnScriptIntrinsicBLAS_BNNM : (JJJIIIJIJIJIIZ)V
    //   40: aload_0
    //   41: monitorexit
    //   42: return
    //   43: astore #17
    //   45: aload_0
    //   46: monitorexit
    //   47: aload #17
    //   49: athrow
    // Exception table:
    //   from	to	target	type
    //   2	40	43	finally
  }
  
  void nScriptIntrinsicBLAS_Complex(long paramLong1, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, float paramFloat1, float paramFloat2, long paramLong2, long paramLong3, float paramFloat3, float paramFloat4, long paramLong4, int paramInt10, int paramInt11, int paramInt12, int paramInt13, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: aload_0
    //   12: getfield mIncCon : J
    //   15: lload_1
    //   16: iload_3
    //   17: iload #4
    //   19: iload #5
    //   21: iload #6
    //   23: iload #7
    //   25: iload #8
    //   27: iload #9
    //   29: iload #10
    //   31: iload #11
    //   33: fload #12
    //   35: fload #13
    //   37: lload #14
    //   39: lload #16
    //   41: fload #18
    //   43: fload #19
    //   45: lload #20
    //   47: iload #22
    //   49: iload #23
    //   51: iload #24
    //   53: iload #25
    //   55: iload #26
    //   57: invokevirtual rsnScriptIntrinsicBLAS_Complex : (JJJIIIIIIIIIFFJJFFJIIIIZ)V
    //   60: aload_0
    //   61: monitorexit
    //   62: return
    //   63: astore #27
    //   65: aload_0
    //   66: monitorexit
    //   67: aload #27
    //   69: athrow
    // Exception table:
    //   from	to	target	type
    //   2	60	63	finally
  }
  
  void nScriptIntrinsicBLAS_Double(long paramLong1, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, double paramDouble1, long paramLong2, long paramLong3, double paramDouble2, long paramLong4, int paramInt10, int paramInt11, int paramInt12, int paramInt13, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: aload_0
    //   12: getfield mIncCon : J
    //   15: lload_1
    //   16: iload_3
    //   17: iload #4
    //   19: iload #5
    //   21: iload #6
    //   23: iload #7
    //   25: iload #8
    //   27: iload #9
    //   29: iload #10
    //   31: iload #11
    //   33: dload #12
    //   35: lload #14
    //   37: lload #16
    //   39: dload #18
    //   41: lload #20
    //   43: iload #22
    //   45: iload #23
    //   47: iload #24
    //   49: iload #25
    //   51: iload #26
    //   53: invokevirtual rsnScriptIntrinsicBLAS_Double : (JJJIIIIIIIIIDJJDJIIIIZ)V
    //   56: aload_0
    //   57: monitorexit
    //   58: return
    //   59: astore #27
    //   61: aload_0
    //   62: monitorexit
    //   63: aload #27
    //   65: athrow
    // Exception table:
    //   from	to	target	type
    //   2	56	59	finally
  }
  
  void nScriptIntrinsicBLAS_Single(long paramLong1, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, float paramFloat1, long paramLong2, long paramLong3, float paramFloat2, long paramLong4, int paramInt10, int paramInt11, int paramInt12, int paramInt13, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: aload_0
    //   12: getfield mIncCon : J
    //   15: lload_1
    //   16: iload_3
    //   17: iload #4
    //   19: iload #5
    //   21: iload #6
    //   23: iload #7
    //   25: iload #8
    //   27: iload #9
    //   29: iload #10
    //   31: iload #11
    //   33: fload #12
    //   35: lload #13
    //   37: lload #15
    //   39: fload #17
    //   41: lload #18
    //   43: iload #20
    //   45: iload #21
    //   47: iload #22
    //   49: iload #23
    //   51: iload #24
    //   53: invokevirtual rsnScriptIntrinsicBLAS_Single : (JJJIIIIIIIIIFJJFJIIIIZ)V
    //   56: aload_0
    //   57: monitorexit
    //   58: return
    //   59: astore #25
    //   61: aload_0
    //   62: monitorexit
    //   63: aload #25
    //   65: athrow
    // Exception table:
    //   from	to	target	type
    //   2	56	59	finally
  }
  
  void nScriptIntrinsicBLAS_Z(long paramLong1, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, double paramDouble1, double paramDouble2, long paramLong2, long paramLong3, double paramDouble3, double paramDouble4, long paramLong4, int paramInt10, int paramInt11, int paramInt12, int paramInt13, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: aload_0
    //   12: getfield mIncCon : J
    //   15: lload_1
    //   16: iload_3
    //   17: iload #4
    //   19: iload #5
    //   21: iload #6
    //   23: iload #7
    //   25: iload #8
    //   27: iload #9
    //   29: iload #10
    //   31: iload #11
    //   33: dload #12
    //   35: dload #14
    //   37: lload #16
    //   39: lload #18
    //   41: dload #20
    //   43: dload #22
    //   45: lload #24
    //   47: iload #26
    //   49: iload #27
    //   51: iload #28
    //   53: iload #29
    //   55: iload #30
    //   57: invokevirtual rsnScriptIntrinsicBLAS_Z : (JJJIIIIIIIIIDDJJDDJIIIIZ)V
    //   60: aload_0
    //   61: monitorexit
    //   62: return
    //   63: astore #31
    //   65: aload_0
    //   66: monitorexit
    //   67: aload #31
    //   69: athrow
    // Exception table:
    //   from	to	target	type
    //   2	60	63	finally
  }
  
  long nScriptIntrinsicCreate(int paramInt, long paramLong, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: iload #4
    //   8: ifeq -> 236
    //   11: getstatic android/os/Build$VERSION.SDK_INT : I
    //   14: bipush #21
    //   16: if_icmplt -> 216
    //   19: aload_0
    //   20: getfield mIncLoaded : Z
    //   23: istore #5
    //   25: iload #5
    //   27: ifne -> 175
    //   30: ldc_w 'RSSupport'
    //   33: invokestatic loadLibrary : (Ljava/lang/String;)V
    //   36: new java/lang/StringBuilder
    //   39: dup
    //   40: invokespecial <init> : ()V
    //   43: astore #6
    //   45: aload #6
    //   47: aload_0
    //   48: getfield mNativeLibDir : Ljava/lang/String;
    //   51: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   54: pop
    //   55: aload #6
    //   57: ldc_w '/libRSSupport.so'
    //   60: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   63: pop
    //   64: aload_0
    //   65: bipush #23
    //   67: aload #6
    //   69: invokevirtual toString : ()Ljava/lang/String;
    //   72: invokevirtual nIncLoadSO : (ILjava/lang/String;)Z
    //   75: ifeq -> 86
    //   78: aload_0
    //   79: iconst_1
    //   80: putfield mIncLoaded : Z
    //   83: goto -> 175
    //   86: new androidx/renderscript/RSRuntimeException
    //   89: dup
    //   90: ldc_w 'Error loading libRSSupport library for Incremental Intrinsic Support'
    //   93: invokespecial <init> : (Ljava/lang/String;)V
    //   96: athrow
    //   97: astore #6
    //   99: new java/lang/StringBuilder
    //   102: dup
    //   103: invokespecial <init> : ()V
    //   106: astore #7
    //   108: aload #7
    //   110: ldc_w 'Error loading RS Compat library for Incremental Intrinsic Support: '
    //   113: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   116: pop
    //   117: aload #7
    //   119: aload #6
    //   121: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   124: pop
    //   125: ldc 'RenderScript_jni'
    //   127: aload #7
    //   129: invokevirtual toString : ()Ljava/lang/String;
    //   132: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   135: pop
    //   136: new java/lang/StringBuilder
    //   139: dup
    //   140: invokespecial <init> : ()V
    //   143: astore #7
    //   145: aload #7
    //   147: ldc_w 'Error loading RS Compat library for Incremental Intrinsic Support: '
    //   150: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   153: pop
    //   154: aload #7
    //   156: aload #6
    //   158: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   161: pop
    //   162: new androidx/renderscript/RSRuntimeException
    //   165: dup
    //   166: aload #7
    //   168: invokevirtual toString : ()Ljava/lang/String;
    //   171: invokespecial <init> : (Ljava/lang/String;)V
    //   174: athrow
    //   175: aload_0
    //   176: getfield mIncCon : J
    //   179: lconst_0
    //   180: lcmp
    //   181: ifne -> 199
    //   184: aload_0
    //   185: aload_0
    //   186: aload_0
    //   187: invokevirtual nIncDeviceCreate : ()J
    //   190: iconst_0
    //   191: iconst_0
    //   192: iconst_0
    //   193: invokevirtual nIncContextCreate : (JIII)J
    //   196: putfield mIncCon : J
    //   199: aload_0
    //   200: aload_0
    //   201: getfield mIncCon : J
    //   204: iload_1
    //   205: lload_2
    //   206: iload #4
    //   208: invokevirtual rsnScriptIntrinsicCreate : (JIJZ)J
    //   211: lstore_2
    //   212: aload_0
    //   213: monitorexit
    //   214: lload_2
    //   215: lreturn
    //   216: ldc 'RenderScript_jni'
    //   218: ldc_w 'Incremental Intrinsics are not supported, please change targetSdkVersion to >= 21'
    //   221: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   224: pop
    //   225: new androidx/renderscript/RSRuntimeException
    //   228: dup
    //   229: ldc_w 'Incremental Intrinsics are not supported before Lollipop (API 21)'
    //   232: invokespecial <init> : (Ljava/lang/String;)V
    //   235: athrow
    //   236: aload_0
    //   237: aload_0
    //   238: getfield mContext : J
    //   241: iload_1
    //   242: lload_2
    //   243: iload #4
    //   245: invokevirtual rsnScriptIntrinsicCreate : (JIJZ)J
    //   248: lstore_2
    //   249: aload_0
    //   250: monitorexit
    //   251: lload_2
    //   252: lreturn
    //   253: astore #6
    //   255: aload_0
    //   256: monitorexit
    //   257: aload #6
    //   259: athrow
    // Exception table:
    //   from	to	target	type
    //   2	6	253	finally
    //   11	25	253	finally
    //   30	36	97	java/lang/UnsatisfiedLinkError
    //   30	36	253	finally
    //   36	83	253	finally
    //   86	97	253	finally
    //   99	175	253	finally
    //   175	199	253	finally
    //   199	212	253	finally
    //   216	236	253	finally
    //   236	249	253	finally
  }
  
  void nScriptInvoke(long paramLong, int paramInt, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: getfield mContext : J
    //   10: lstore #5
    //   12: iload #4
    //   14: ifeq -> 23
    //   17: aload_0
    //   18: getfield mIncCon : J
    //   21: lstore #5
    //   23: aload_0
    //   24: lload #5
    //   26: lload_1
    //   27: iload_3
    //   28: iload #4
    //   30: invokevirtual rsnScriptInvoke : (JJIZ)V
    //   33: aload_0
    //   34: monitorexit
    //   35: return
    //   36: astore #7
    //   38: aload_0
    //   39: monitorexit
    //   40: aload #7
    //   42: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	36	finally
    //   17	23	36	finally
    //   23	33	36	finally
  }
  
  long nScriptInvokeIDCreate(long paramLong, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: iload_3
    //   13: invokevirtual rsnScriptInvokeIDCreate : (JJI)J
    //   16: lstore_1
    //   17: aload_0
    //   18: monitorexit
    //   19: lload_1
    //   20: lreturn
    //   21: astore #4
    //   23: aload_0
    //   24: monitorexit
    //   25: aload #4
    //   27: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	21	finally
  }
  
  void nScriptInvokeV(long paramLong, int paramInt, byte[] paramArrayOfbyte, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: getfield mContext : J
    //   10: lstore #6
    //   12: iload #5
    //   14: ifeq -> 23
    //   17: aload_0
    //   18: getfield mIncCon : J
    //   21: lstore #6
    //   23: aload_0
    //   24: lload #6
    //   26: lload_1
    //   27: iload_3
    //   28: aload #4
    //   30: iload #5
    //   32: invokevirtual rsnScriptInvokeV : (JJI[BZ)V
    //   35: aload_0
    //   36: monitorexit
    //   37: return
    //   38: astore #4
    //   40: aload_0
    //   41: monitorexit
    //   42: aload #4
    //   44: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	38	finally
    //   17	23	38	finally
    //   23	35	38	finally
  }
  
  long nScriptKernelIDCreate(long paramLong, int paramInt1, int paramInt2, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: getfield mContext : J
    //   10: lstore #6
    //   12: iload #5
    //   14: ifeq -> 23
    //   17: aload_0
    //   18: getfield mIncCon : J
    //   21: lstore #6
    //   23: aload_0
    //   24: lload #6
    //   26: lload_1
    //   27: iload_3
    //   28: iload #4
    //   30: iload #5
    //   32: invokevirtual rsnScriptKernelIDCreate : (JJIIZ)J
    //   35: lstore_1
    //   36: aload_0
    //   37: monitorexit
    //   38: lload_1
    //   39: lreturn
    //   40: astore #8
    //   42: aload_0
    //   43: monitorexit
    //   44: aload #8
    //   46: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	40	finally
    //   17	23	40	finally
    //   23	36	40	finally
  }
  
  void nScriptReduce(long paramLong1, int paramInt, long[] paramArrayOflong, long paramLong2, int[] paramArrayOfint) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: iload_3
    //   13: aload #4
    //   15: lload #5
    //   17: aload #7
    //   19: invokevirtual rsnScriptReduce : (JJI[JJ[I)V
    //   22: aload_0
    //   23: monitorexit
    //   24: return
    //   25: astore #4
    //   27: aload_0
    //   28: monitorexit
    //   29: aload #4
    //   31: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	25	finally
  }
  
  void nScriptSetTimeZone(long paramLong, byte[] paramArrayOfbyte, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: getfield mContext : J
    //   10: lstore #5
    //   12: iload #4
    //   14: ifeq -> 23
    //   17: aload_0
    //   18: getfield mIncCon : J
    //   21: lstore #5
    //   23: aload_0
    //   24: lload #5
    //   26: lload_1
    //   27: aload_3
    //   28: iload #4
    //   30: invokevirtual rsnScriptSetTimeZone : (JJ[BZ)V
    //   33: aload_0
    //   34: monitorexit
    //   35: return
    //   36: astore_3
    //   37: aload_0
    //   38: monitorexit
    //   39: aload_3
    //   40: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	36	finally
    //   17	23	36	finally
    //   23	33	36	finally
  }
  
  void nScriptSetVarD(long paramLong, int paramInt, double paramDouble, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: getfield mContext : J
    //   10: lstore #7
    //   12: iload #6
    //   14: ifeq -> 23
    //   17: aload_0
    //   18: getfield mIncCon : J
    //   21: lstore #7
    //   23: aload_0
    //   24: lload #7
    //   26: lload_1
    //   27: iload_3
    //   28: dload #4
    //   30: iload #6
    //   32: invokevirtual rsnScriptSetVarD : (JJIDZ)V
    //   35: aload_0
    //   36: monitorexit
    //   37: return
    //   38: astore #9
    //   40: aload_0
    //   41: monitorexit
    //   42: aload #9
    //   44: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	38	finally
    //   17	23	38	finally
    //   23	35	38	finally
  }
  
  void nScriptSetVarF(long paramLong, int paramInt, float paramFloat, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: getfield mContext : J
    //   10: lstore #6
    //   12: iload #5
    //   14: ifeq -> 23
    //   17: aload_0
    //   18: getfield mIncCon : J
    //   21: lstore #6
    //   23: aload_0
    //   24: lload #6
    //   26: lload_1
    //   27: iload_3
    //   28: fload #4
    //   30: iload #5
    //   32: invokevirtual rsnScriptSetVarF : (JJIFZ)V
    //   35: aload_0
    //   36: monitorexit
    //   37: return
    //   38: astore #8
    //   40: aload_0
    //   41: monitorexit
    //   42: aload #8
    //   44: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	38	finally
    //   17	23	38	finally
    //   23	35	38	finally
  }
  
  void nScriptSetVarI(long paramLong, int paramInt1, int paramInt2, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: getfield mContext : J
    //   10: lstore #6
    //   12: iload #5
    //   14: ifeq -> 23
    //   17: aload_0
    //   18: getfield mIncCon : J
    //   21: lstore #6
    //   23: aload_0
    //   24: lload #6
    //   26: lload_1
    //   27: iload_3
    //   28: iload #4
    //   30: iload #5
    //   32: invokevirtual rsnScriptSetVarI : (JJIIZ)V
    //   35: aload_0
    //   36: monitorexit
    //   37: return
    //   38: astore #8
    //   40: aload_0
    //   41: monitorexit
    //   42: aload #8
    //   44: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	38	finally
    //   17	23	38	finally
    //   23	35	38	finally
  }
  
  void nScriptSetVarJ(long paramLong1, int paramInt, long paramLong2, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: getfield mContext : J
    //   10: lstore #7
    //   12: iload #6
    //   14: ifeq -> 23
    //   17: aload_0
    //   18: getfield mIncCon : J
    //   21: lstore #7
    //   23: aload_0
    //   24: lload #7
    //   26: lload_1
    //   27: iload_3
    //   28: lload #4
    //   30: iload #6
    //   32: invokevirtual rsnScriptSetVarJ : (JJIJZ)V
    //   35: aload_0
    //   36: monitorexit
    //   37: return
    //   38: astore #9
    //   40: aload_0
    //   41: monitorexit
    //   42: aload #9
    //   44: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	38	finally
    //   17	23	38	finally
    //   23	35	38	finally
  }
  
  void nScriptSetVarObj(long paramLong1, int paramInt, long paramLong2, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: getfield mContext : J
    //   10: lstore #7
    //   12: iload #6
    //   14: ifeq -> 23
    //   17: aload_0
    //   18: getfield mIncCon : J
    //   21: lstore #7
    //   23: aload_0
    //   24: lload #7
    //   26: lload_1
    //   27: iload_3
    //   28: lload #4
    //   30: iload #6
    //   32: invokevirtual rsnScriptSetVarObj : (JJIJZ)V
    //   35: aload_0
    //   36: monitorexit
    //   37: return
    //   38: astore #9
    //   40: aload_0
    //   41: monitorexit
    //   42: aload #9
    //   44: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	38	finally
    //   17	23	38	finally
    //   23	35	38	finally
  }
  
  void nScriptSetVarV(long paramLong, int paramInt, byte[] paramArrayOfbyte, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: getfield mContext : J
    //   10: lstore #6
    //   12: iload #5
    //   14: ifeq -> 23
    //   17: aload_0
    //   18: getfield mIncCon : J
    //   21: lstore #6
    //   23: aload_0
    //   24: lload #6
    //   26: lload_1
    //   27: iload_3
    //   28: aload #4
    //   30: iload #5
    //   32: invokevirtual rsnScriptSetVarV : (JJI[BZ)V
    //   35: aload_0
    //   36: monitorexit
    //   37: return
    //   38: astore #4
    //   40: aload_0
    //   41: monitorexit
    //   42: aload #4
    //   44: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	38	finally
    //   17	23	38	finally
    //   23	35	38	finally
  }
  
  void nScriptSetVarVE(long paramLong1, int paramInt, byte[] paramArrayOfbyte, long paramLong2, int[] paramArrayOfint, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: getfield mContext : J
    //   10: lstore #9
    //   12: iload #8
    //   14: ifeq -> 23
    //   17: aload_0
    //   18: getfield mIncCon : J
    //   21: lstore #9
    //   23: aload_0
    //   24: lload #9
    //   26: lload_1
    //   27: iload_3
    //   28: aload #4
    //   30: lload #5
    //   32: aload #7
    //   34: iload #8
    //   36: invokevirtual rsnScriptSetVarVE : (JJI[BJ[IZ)V
    //   39: aload_0
    //   40: monitorexit
    //   41: return
    //   42: astore #4
    //   44: aload_0
    //   45: monitorexit
    //   46: aload #4
    //   48: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	42	finally
    //   17	23	42	finally
    //   23	39	42	finally
  }
  
  long nTypeCreate(long paramLong, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, int paramInt4) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: iload_3
    //   13: iload #4
    //   15: iload #5
    //   17: iload #6
    //   19: iload #7
    //   21: iload #8
    //   23: invokevirtual rsnTypeCreate : (JJIIIZZI)J
    //   26: lstore_1
    //   27: aload_0
    //   28: monitorexit
    //   29: lload_1
    //   30: lreturn
    //   31: astore #9
    //   33: aload_0
    //   34: monitorexit
    //   35: aload #9
    //   37: athrow
    // Exception table:
    //   from	to	target	type
    //   2	27	31	finally
  }
  
  void nTypeGetNativeData(long paramLong, long[] paramArrayOflong) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual validate : ()V
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mContext : J
    //   11: lload_1
    //   12: aload_3
    //   13: invokevirtual rsnTypeGetNativeData : (JJ[J)V
    //   16: aload_0
    //   17: monitorexit
    //   18: return
    //   19: astore_3
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_3
    //   23: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	19	finally
  }
  
  native void rsnAllocationCopyFromBitmap(long paramLong1, long paramLong2, Bitmap paramBitmap);
  
  native void rsnAllocationCopyToBitmap(long paramLong1, long paramLong2, Bitmap paramBitmap);
  
  native long rsnAllocationCreateBitmapBackedAllocation(long paramLong1, long paramLong2, int paramInt1, Bitmap paramBitmap, int paramInt2);
  
  native long rsnAllocationCreateBitmapRef(long paramLong1, long paramLong2, Bitmap paramBitmap);
  
  native long rsnAllocationCreateFromAssetStream(long paramLong, int paramInt1, int paramInt2, int paramInt3);
  
  native long rsnAllocationCreateFromBitmap(long paramLong1, long paramLong2, int paramInt1, Bitmap paramBitmap, int paramInt2);
  
  native long rsnAllocationCreateTyped(long paramLong1, long paramLong2, int paramInt1, int paramInt2, long paramLong3);
  
  native long rsnAllocationCubeCreateFromBitmap(long paramLong1, long paramLong2, int paramInt1, Bitmap paramBitmap, int paramInt2);
  
  native void rsnAllocationData1D(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, Object paramObject, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean);
  
  native void rsnAllocationData2D(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong3, int paramInt7, int paramInt8, int paramInt9, int paramInt10);
  
  native void rsnAllocationData2D(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, Object paramObject, int paramInt7, int paramInt8, int paramInt9, boolean paramBoolean);
  
  native void rsnAllocationData2D(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Bitmap paramBitmap);
  
  native void rsnAllocationData3D(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, long paramLong3, int paramInt8, int paramInt9, int paramInt10, int paramInt11);
  
  native void rsnAllocationData3D(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, Object paramObject, int paramInt8, int paramInt9, int paramInt10, boolean paramBoolean);
  
  native void rsnAllocationElementData1D(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfbyte, int paramInt4);
  
  native void rsnAllocationGenerateMipmaps(long paramLong1, long paramLong2);
  
  native ByteBuffer rsnAllocationGetByteBuffer(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3);
  
  native long rsnAllocationGetStride(long paramLong1, long paramLong2);
  
  native long rsnAllocationGetType(long paramLong1, long paramLong2);
  
  native void rsnAllocationIoReceive(long paramLong1, long paramLong2);
  
  native void rsnAllocationIoSend(long paramLong1, long paramLong2);
  
  native void rsnAllocationRead(long paramLong1, long paramLong2, Object paramObject, int paramInt1, int paramInt2, boolean paramBoolean);
  
  native void rsnAllocationRead1D(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, Object paramObject, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean);
  
  native void rsnAllocationRead2D(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, Object paramObject, int paramInt7, int paramInt8, int paramInt9, boolean paramBoolean);
  
  native void rsnAllocationResize1D(long paramLong1, long paramLong2, int paramInt);
  
  native void rsnAllocationResize2D(long paramLong1, long paramLong2, int paramInt1, int paramInt2);
  
  native void rsnAllocationSetSurface(long paramLong1, long paramLong2, Surface paramSurface);
  
  native void rsnAllocationSyncAll(long paramLong1, long paramLong2, int paramInt);
  
  native long rsnClosureCreate(long paramLong1, long paramLong2, long paramLong3, long[] paramArrayOflong1, long[] paramArrayOflong2, int[] paramArrayOfint, long[] paramArrayOflong3, long[] paramArrayOflong4);
  
  native void rsnClosureSetArg(long paramLong1, long paramLong2, int paramInt1, long paramLong3, int paramInt2);
  
  native void rsnClosureSetGlobal(long paramLong1, long paramLong2, long paramLong3, long paramLong4, int paramInt);
  
  native long rsnContextCreate(long paramLong, int paramInt1, int paramInt2, int paramInt3, String paramString);
  
  native void rsnContextDestroy(long paramLong);
  
  native void rsnContextDump(long paramLong, int paramInt);
  
  native void rsnContextFinish(long paramLong);
  
  native void rsnContextSendMessage(long paramLong, int paramInt, int[] paramArrayOfint);
  
  native void rsnContextSetPriority(long paramLong, int paramInt);
  
  native long rsnElementCreate(long paramLong1, long paramLong2, int paramInt1, boolean paramBoolean, int paramInt2);
  
  native long rsnElementCreate2(long paramLong, long[] paramArrayOflong, String[] paramArrayOfString, int[] paramArrayOfint);
  
  native void rsnElementGetNativeData(long paramLong1, long paramLong2, int[] paramArrayOfint);
  
  native void rsnElementGetSubElements(long paramLong1, long paramLong2, long[] paramArrayOflong, String[] paramArrayOfString, int[] paramArrayOfint);
  
  native long rsnIncAllocationCreateTyped(long paramLong1, long paramLong2, long paramLong3, long paramLong4, int paramInt);
  
  native long rsnIncContextCreate(long paramLong, int paramInt1, int paramInt2, int paramInt3);
  
  native void rsnIncContextDestroy(long paramLong);
  
  native void rsnIncContextFinish(long paramLong);
  
  native long rsnIncElementCreate(long paramLong1, long paramLong2, int paramInt1, boolean paramBoolean, int paramInt2);
  
  native void rsnIncObjDestroy(long paramLong1, long paramLong2);
  
  native long rsnIncTypeCreate(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, int paramInt4);
  
  native long rsnInvokeClosureCreate(long paramLong1, long paramLong2, byte[] paramArrayOfbyte, long[] paramArrayOflong1, long[] paramArrayOflong2, int[] paramArrayOfint);
  
  native void rsnObjDestroy(long paramLong1, long paramLong2);
  
  native long rsnSamplerCreate(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, float paramFloat);
  
  native void rsnScriptBindAllocation(long paramLong1, long paramLong2, long paramLong3, int paramInt, boolean paramBoolean);
  
  native long rsnScriptCCreate(long paramLong, String paramString1, String paramString2, byte[] paramArrayOfbyte, int paramInt);
  
  native long rsnScriptFieldIDCreate(long paramLong1, long paramLong2, int paramInt, boolean paramBoolean);
  
  native void rsnScriptForEach(long paramLong1, long paramLong2, int paramInt, long[] paramArrayOflong, long paramLong3, byte[] paramArrayOfbyte, int[] paramArrayOfint);
  
  native void rsnScriptForEach(long paramLong1, long paramLong2, long paramLong3, int paramInt, long paramLong4, long paramLong5, boolean paramBoolean);
  
  native void rsnScriptForEach(long paramLong1, long paramLong2, long paramLong3, int paramInt, long paramLong4, long paramLong5, byte[] paramArrayOfbyte, boolean paramBoolean);
  
  native void rsnScriptForEachClipped(long paramLong1, long paramLong2, long paramLong3, int paramInt1, long paramLong4, long paramLong5, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, boolean paramBoolean);
  
  native void rsnScriptForEachClipped(long paramLong1, long paramLong2, long paramLong3, int paramInt1, long paramLong4, long paramLong5, byte[] paramArrayOfbyte, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, boolean paramBoolean);
  
  native long rsnScriptGroup2Create(long paramLong, String paramString1, String paramString2, long[] paramArrayOflong);
  
  native void rsnScriptGroup2Execute(long paramLong1, long paramLong2);
  
  native long rsnScriptGroupCreate(long paramLong, long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3, long[] paramArrayOflong4, long[] paramArrayOflong5);
  
  native void rsnScriptGroupExecute(long paramLong1, long paramLong2);
  
  native void rsnScriptGroupSetInput(long paramLong1, long paramLong2, long paramLong3, long paramLong4);
  
  native void rsnScriptGroupSetOutput(long paramLong1, long paramLong2, long paramLong3, long paramLong4);
  
  native void rsnScriptIntrinsicBLAS_BNNM(long paramLong1, long paramLong2, long paramLong3, int paramInt1, int paramInt2, int paramInt3, long paramLong4, int paramInt4, long paramLong5, int paramInt5, long paramLong6, int paramInt6, int paramInt7, boolean paramBoolean);
  
  native void rsnScriptIntrinsicBLAS_Complex(long paramLong1, long paramLong2, long paramLong3, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, float paramFloat1, float paramFloat2, long paramLong4, long paramLong5, float paramFloat3, float paramFloat4, long paramLong6, int paramInt10, int paramInt11, int paramInt12, int paramInt13, boolean paramBoolean);
  
  native void rsnScriptIntrinsicBLAS_Double(long paramLong1, long paramLong2, long paramLong3, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, double paramDouble1, long paramLong4, long paramLong5, double paramDouble2, long paramLong6, int paramInt10, int paramInt11, int paramInt12, int paramInt13, boolean paramBoolean);
  
  native void rsnScriptIntrinsicBLAS_Single(long paramLong1, long paramLong2, long paramLong3, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, float paramFloat1, long paramLong4, long paramLong5, float paramFloat2, long paramLong6, int paramInt10, int paramInt11, int paramInt12, int paramInt13, boolean paramBoolean);
  
  native void rsnScriptIntrinsicBLAS_Z(long paramLong1, long paramLong2, long paramLong3, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, double paramDouble1, double paramDouble2, long paramLong4, long paramLong5, double paramDouble3, double paramDouble4, long paramLong6, int paramInt10, int paramInt11, int paramInt12, int paramInt13, boolean paramBoolean);
  
  native long rsnScriptIntrinsicCreate(long paramLong1, int paramInt, long paramLong2, boolean paramBoolean);
  
  native void rsnScriptInvoke(long paramLong1, long paramLong2, int paramInt, boolean paramBoolean);
  
  native long rsnScriptInvokeIDCreate(long paramLong1, long paramLong2, int paramInt);
  
  native void rsnScriptInvokeV(long paramLong1, long paramLong2, int paramInt, byte[] paramArrayOfbyte, boolean paramBoolean);
  
  native long rsnScriptKernelIDCreate(long paramLong1, long paramLong2, int paramInt1, int paramInt2, boolean paramBoolean);
  
  native void rsnScriptReduce(long paramLong1, long paramLong2, int paramInt, long[] paramArrayOflong, long paramLong3, int[] paramArrayOfint);
  
  native void rsnScriptSetTimeZone(long paramLong1, long paramLong2, byte[] paramArrayOfbyte, boolean paramBoolean);
  
  native void rsnScriptSetVarD(long paramLong1, long paramLong2, int paramInt, double paramDouble, boolean paramBoolean);
  
  native void rsnScriptSetVarF(long paramLong1, long paramLong2, int paramInt, float paramFloat, boolean paramBoolean);
  
  native void rsnScriptSetVarI(long paramLong1, long paramLong2, int paramInt1, int paramInt2, boolean paramBoolean);
  
  native void rsnScriptSetVarJ(long paramLong1, long paramLong2, int paramInt, long paramLong3, boolean paramBoolean);
  
  native void rsnScriptSetVarObj(long paramLong1, long paramLong2, int paramInt, long paramLong3, boolean paramBoolean);
  
  native void rsnScriptSetVarV(long paramLong1, long paramLong2, int paramInt, byte[] paramArrayOfbyte, boolean paramBoolean);
  
  native void rsnScriptSetVarVE(long paramLong1, long paramLong2, int paramInt, byte[] paramArrayOfbyte, long paramLong3, int[] paramArrayOfint, boolean paramBoolean);
  
  native long rsnTypeCreate(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, int paramInt4);
  
  native void rsnTypeGetNativeData(long paramLong1, long paramLong2, long[] paramArrayOflong);
  
  long safeID(BaseObj paramBaseObj) {
    return (paramBaseObj != null) ? paramBaseObj.getID(this) : 0L;
  }
  
  public void sendMessage(int paramInt, int[] paramArrayOfint) {
    nContextSendMessage(paramInt, paramArrayOfint);
  }
  
  public void setErrorHandler(RSErrorHandler paramRSErrorHandler) {
    this.mErrorCallback = paramRSErrorHandler;
  }
  
  public void setMessageHandler(RSMessageHandler paramRSMessageHandler) {
    this.mMessageCallback = paramRSMessageHandler;
  }
  
  public void setPriority(Priority paramPriority) {
    validate();
    nContextSetPriority(paramPriority.mID);
  }
  
  boolean usingIO() {
    return useIOlib;
  }
  
  void validate() {
    if (this.mContext != 0L)
      return; 
    throw new RSInvalidStateException("Calling RS with no Context active.");
  }
  
  void validateObject(BaseObj paramBaseObj) {
    if (paramBaseObj != null) {
      if (paramBaseObj.mRS == this)
        return; 
      throw new RSIllegalArgumentException("Attempting to use an object across contexts.");
    } 
  }
  
  public enum ContextType {
    DEBUG,
    NORMAL(0),
    PROFILE(0);
    
    int mID;
    
    static {
      ContextType contextType = new ContextType("PROFILE", 2, 2);
      PROFILE = contextType;
      $VALUES = new ContextType[] { NORMAL, DEBUG, contextType };
    }
    
    ContextType(int param1Int1) {
      this.mID = param1Int1;
    }
  }
  
  static class MessageThread extends Thread {
    static final int RS_ERROR_FATAL_DEBUG = 2048;
    
    static final int RS_ERROR_FATAL_UNKNOWN = 4096;
    
    static final int RS_MESSAGE_TO_CLIENT_ERROR = 3;
    
    static final int RS_MESSAGE_TO_CLIENT_EXCEPTION = 1;
    
    static final int RS_MESSAGE_TO_CLIENT_NONE = 0;
    
    static final int RS_MESSAGE_TO_CLIENT_RESIZE = 2;
    
    static final int RS_MESSAGE_TO_CLIENT_USER = 4;
    
    int[] mAuxData = new int[2];
    
    RenderScript mRS;
    
    boolean mRun = true;
    
    MessageThread(RenderScript param1RenderScript) {
      super("RSMessageThread");
      this.mRS = param1RenderScript;
    }
    
    public void run() {
      int[] arrayOfInt = new int[16];
      RenderScript renderScript = this.mRS;
      renderScript.nContextInitToClient(renderScript.mContext);
      while (true) {
        if (this.mRun) {
          arrayOfInt[0] = 0;
          renderScript = this.mRS;
          int i = renderScript.nContextPeekMessage(renderScript.mContext, this.mAuxData);
          int[] arrayOfInt1 = this.mAuxData;
          int j = arrayOfInt1[1];
          int k = arrayOfInt1[0];
          if (i == 4) {
            arrayOfInt1 = arrayOfInt;
            if (j >> 2 >= arrayOfInt.length)
              arrayOfInt1 = new int[j + 3 >> 2]; 
            RenderScript renderScript1 = this.mRS;
            if (renderScript1.nContextGetUserMessage(renderScript1.mContext, arrayOfInt1) == 4) {
              if (this.mRS.mMessageCallback != null) {
                this.mRS.mMessageCallback.mData = arrayOfInt1;
                this.mRS.mMessageCallback.mID = k;
                this.mRS.mMessageCallback.mLength = j;
                this.mRS.mMessageCallback.run();
                int[] arrayOfInt2 = arrayOfInt1;
                continue;
              } 
              throw new RSInvalidStateException("Received a message from the script with no message handler installed.");
            } 
            throw new RSDriverException("Error processing message from RenderScript.");
          } 
          if (i == 3) {
            RenderScript renderScript1 = this.mRS;
            String str = renderScript1.nContextGetErrorMessage(renderScript1.mContext);
            if (k < 4096 && (k < 2048 || (this.mRS.mContextType == RenderScript.ContextType.DEBUG && this.mRS.mErrorCallback != null))) {
              if (this.mRS.mErrorCallback != null) {
                this.mRS.mErrorCallback.mErrorMessage = str;
                this.mRS.mErrorCallback.mErrorNum = k;
                this.mRS.mErrorCallback.run();
                continue;
              } 
              StringBuilder stringBuilder1 = new StringBuilder();
              stringBuilder1.append("non fatal RS error, ");
              stringBuilder1.append(str);
              Log.e("RenderScript_jni", stringBuilder1.toString());
              continue;
            } 
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("fatal RS error, ");
            stringBuilder.append(str);
            Log.e("RenderScript_jni", stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("Fatal error ");
            stringBuilder.append(k);
            stringBuilder.append(", details: ");
            stringBuilder.append(str);
            throw new RSRuntimeException(stringBuilder.toString());
          } 
          try {
            sleep(1L, 0);
          } catch (InterruptedException interruptedException) {}
          continue;
        } 
        return;
      } 
    }
  }
  
  public enum Priority {
    LOW(15),
    NORMAL(15);
    
    int mID;
    
    static {
      Priority priority = new Priority("NORMAL", 1, -4);
      NORMAL = priority;
      $VALUES = new Priority[] { LOW, priority };
    }
    
    Priority(int param1Int1) {
      this.mID = param1Int1;
    }
  }
  
  public static class RSErrorHandler implements Runnable {
    protected String mErrorMessage;
    
    protected int mErrorNum;
    
    public void run() {}
  }
  
  public static class RSMessageHandler implements Runnable {
    protected int[] mData;
    
    protected int mID;
    
    protected int mLength;
    
    public void run() {}
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\renderscript\RenderScript.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
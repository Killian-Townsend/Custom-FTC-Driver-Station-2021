package com.vuforia.ar.pl;

import android.app.Activity;
import android.app.Application;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.HandlerThread;
import java.util.HashMap;
import java.util.Vector;

public class SensorController extends HandlerThread implements SensorEventListener {
  private static final int AR_SENSOR_CONFIDENCE_HIGH = 4;
  
  private static final int AR_SENSOR_CONFIDENCE_LOW = 2;
  
  private static final int AR_SENSOR_CONFIDENCE_MEDIUM = 3;
  
  private static final int AR_SENSOR_CONFIDENCE_UNKNOWN = 0;
  
  private static final int AR_SENSOR_CONFIDENCE_UNRELIABLE = 1;
  
  private static int AR_SENSOR_INDEX_DONTCARE = 0;
  
  private static final int AR_SENSOR_PARAMTYPE_ACCURACY = -2147483640;
  
  private static final int AR_SENSOR_PARAMTYPE_BASE = -2147483648;
  
  private static final int AR_SENSOR_PARAMTYPE_DATARANGE_MAX = -2147483646;
  
  private static final int AR_SENSOR_PARAMTYPE_DATARANGE_MIN = -2147483647;
  
  private static final int AR_SENSOR_PARAMTYPE_RESOLUTION = -2147483644;
  
  private static final int AR_SENSOR_PARAMTYPE_SENSITIVITY = -2147483632;
  
  private static final int AR_SENSOR_PARAMTYPE_UPDATEINTERVAL = -2147483616;
  
  private static final int AR_SENSOR_PARAMTYPE_UPDATEINTERVAL_ABSTRACT = -2147483584;
  
  private static final int AR_SENSOR_PARAMTYPE_UPDATEINTERVAL_ENFORCED = -2147483392;
  
  private static final int AR_SENSOR_PARAMTYPE_UPDATEINTERVAL_MIN = -2147483520;
  
  private static final int AR_SENSOR_STATUS_IDLE = 1342242818;
  
  private static final int AR_SENSOR_STATUS_RUNNING = 1342242819;
  
  private static final int AR_SENSOR_STATUS_UNINITIALIZED = 1342242817;
  
  private static final int AR_SENSOR_STATUS_UNKNOWN = 1342242816;
  
  private static final int AR_SENSOR_TYPE_ACCELEROMETER = 1342177282;
  
  private static final int AR_SENSOR_TYPE_AMBIENT_LIGHT = 1342177286;
  
  private static final int AR_SENSOR_TYPE_DEVICE_ROTATION = 1342177288;
  
  private static final int AR_SENSOR_TYPE_GYROSCOPE = 1342177281;
  
  private static final int AR_SENSOR_TYPE_MAGNETOMETER = 1342177283;
  
  private static final int AR_SENSOR_TYPE_PROXIMITY = 1342177285;
  
  private static final int AR_SENSOR_TYPE_STEP_DETECTOR = 1342177287;
  
  private static final int AR_SENSOR_TYPE_UNKNOWN = 1342177280;
  
  private static final int AR_SENSOR_UPDATEINTERVAL_HIGHESTRATE = 4;
  
  private static final int AR_SENSOR_UPDATEINTERVAL_HIGHRATE = 3;
  
  private static final int AR_SENSOR_UPDATEINTERVAL_LOWRATE = 1;
  
  private static final int AR_SENSOR_UPDATEINTERVAL_MEDIUMRATE = 2;
  
  private static final int AR_SENSOR_UPDATEINTERVAL_UNKNOWN = 0;
  
  private static boolean CONVERT_FORMAT_TO_ANDROID = false;
  
  private static boolean CONVERT_FORMAT_TO_PL = false;
  
  private static final String MODULENAME = "SensorController";
  
  private static final int SENSORINFO_VALUE_ANDROIDSENSORTYPE = 1;
  
  private static final int SENSORINFO_VALUE_ISDEFAULT = 2;
  
  private static final int SENSORINFO_VALUE_PLSENSORTYPE = 0;
  
  private static final int[] SENSOR_TYPE_CONVERSIONTABLE = new int[] { 
      4, 1342177281, 1, 1342177282, 2, 1342177283, 8, 1342177285, 5, 1342177286, 
      18, 1342177287, 11, 1342177288 };
  
  private static final int _NUM_SENSORINFO_VALUE_ = 3;
  
  private Vector<SensorCacheInfo> sensorCacheInfo = null;
  
  private Handler sensorEventHandler;
  
  private HashMap<Sensor, Integer> sensorIndexMap = null;
  
  private SensorManager sensorManager;
  
  static {
    CONVERT_FORMAT_TO_PL = true;
    CONVERT_FORMAT_TO_ANDROID = false;
    AR_SENSOR_INDEX_DONTCARE = -1;
  }
  
  public SensorController() {
    super("SensorController");
  }
  
  private SensorCacheInfo getSensorCacheInfo(int paramInt) {
    return (paramInt < 0 || paramInt >= this.sensorCacheInfo.size()) ? null : this.sensorCacheInfo.get(paramInt);
  }
  
  private native void newDataAvailable(int paramInt1, long paramLong, int paramInt2, float[] paramArrayOffloat);
  
  private int translateSensorType(int paramInt, boolean paramBoolean) {
    boolean bool = false;
    int i = 0;
    while (true) {
      int[] arrayOfInt = SENSOR_TYPE_CONVERSIONTABLE;
      if (i < arrayOfInt.length / 2) {
        int j;
        if (paramBoolean == CONVERT_FORMAT_TO_PL) {
          j = arrayOfInt[i * 2];
        } else {
          j = arrayOfInt[i * 2 + 1];
        } 
        if (paramInt == j)
          return (paramBoolean == CONVERT_FORMAT_TO_PL) ? SENSOR_TYPE_CONVERSIONTABLE[i * 2 + 1] : SENSOR_TYPE_CONVERSIONTABLE[i * 2]; 
        i++;
        continue;
      } 
      paramInt = bool;
      if (paramBoolean == CONVERT_FORMAT_TO_PL)
        paramInt = 1342177280; 
      return paramInt;
    } 
  }
  
  private int translateSensorUpdateIntervalToAndroid(int paramInt) {
    byte b = 3;
    if (paramInt != 1) {
      if (paramInt != 2)
        return (paramInt != 3) ? ((paramInt != 4) ? -1 : 0) : 1; 
      b = 2;
    } 
    return b;
  }
  
  boolean close(int paramInt) {
    SensorCacheInfo sensorCacheInfo = getSensorCacheInfo(paramInt);
    boolean bool = false;
    if (sensorCacheInfo == null) {
      SystemTools.setSystemErrorCode(4);
      SystemTools.logSystemError("Sensor handle is invalid");
      return false;
    } 
    try {
      this.sensorManager.unregisterListener(this, sensorCacheInfo.sensor);
      bool = true;
    } catch (Exception exception) {
      SystemTools.setSystemErrorCode(6);
      SystemTools.logSystemError("Failed to unregister sensor event listerer");
    } 
    System.gc();
    return bool;
  }
  
  public int getAllSupportedSensors() {
    Activity activity = SystemTools.getActivityFromNative();
    if (activity == null) {
      SystemTools.logSystemError("No valid activity set in native!");
      return -1;
    } 
    Application application = activity.getApplication();
    if (application == null)
      return -1; 
    SensorManager sensorManager = (SensorManager)application.getSystemService("sensor");
    this.sensorManager = sensorManager;
    if (sensorManager == null) {
      SystemTools.setSystemErrorCode(6);
      SystemTools.logSystemError("Failed to retrieve Context's Sensor Service");
      return -1;
    } 
    if (this.sensorCacheInfo.size() > 0)
      return this.sensorCacheInfo.size(); 
    for (Sensor sensor : this.sensorManager.getSensorList(-1)) {
      int i = sensor.getType();
      boolean bool = sensor.equals(this.sensorManager.getDefaultSensor(i));
      i = translateSensorType(i, CONVERT_FORMAT_TO_PL);
      if (i != 1342177280) {
        SensorCacheInfo sensorCacheInfo = new SensorCacheInfo();
        sensorCacheInfo.sensor = sensor;
        sensorCacheInfo.plSensorType = i;
        sensorCacheInfo.isDefaultSensor = bool;
        sensorCacheInfo.cacheIndex = this.sensorCacheInfo.size();
        sensorCacheInfo.requestedAbstractUpdateRate = 0;
        this.sensorCacheInfo.add(sensorCacheInfo);
        this.sensorIndexMap.put(sensorCacheInfo.sensor, Integer.valueOf(sensorCacheInfo.cacheIndex));
      } 
    } 
    return this.sensorCacheInfo.size();
  }
  
  int[] getSensorInfoValues(int paramInt) {
    throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
  }
  
  String getSensorName(int paramInt) {
    SensorCacheInfo sensorCacheInfo = getSensorCacheInfo(paramInt);
    return (sensorCacheInfo == null) ? null : sensorCacheInfo.sensor.getName();
  }
  
  Object getTypedSensorParameter(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: iload_1
    //   2: invokespecial getSensorCacheInfo : (I)Lcom/vuforia/ar/pl/SensorController$SensorCacheInfo;
    //   5: astore_3
    //   6: aload_3
    //   7: ifnonnull -> 21
    //   10: iconst_4
    //   11: invokestatic setSystemErrorCode : (I)V
    //   14: ldc 'Sensor handle is invalid'
    //   16: invokestatic logSystemError : (Ljava/lang/String;)V
    //   19: aconst_null
    //   20: areturn
    //   21: iload_2
    //   22: lookupswitch default -> 96, -2147483647 -> 164, -2147483646 -> 153, -2147483644 -> 142, -2147483640 -> 164, -2147483632 -> 164, -2147483616 -> 164, -2147483584 -> 134, -2147483520 -> 103
    //   96: iconst_3
    //   97: invokestatic setSystemErrorCode : (I)V
    //   100: goto -> 242
    //   103: bipush #9
    //   105: invokestatic checkMinimumApiLevel : (I)Z
    //   108: ifeq -> 122
    //   111: aload_3
    //   112: getfield sensor : Landroid/hardware/Sensor;
    //   115: invokevirtual getMinDelay : ()I
    //   118: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   121: areturn
    //   122: iconst_3
    //   123: invokestatic setSystemErrorCode : (I)V
    //   126: ldc_w 'Unknown sensor parameter'
    //   129: invokestatic logSystemError : (Ljava/lang/String;)V
    //   132: aconst_null
    //   133: areturn
    //   134: aload_3
    //   135: getfield requestedAbstractUpdateRate : I
    //   138: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   141: areturn
    //   142: aload_3
    //   143: getfield sensor : Landroid/hardware/Sensor;
    //   146: invokevirtual getResolution : ()F
    //   149: invokestatic valueOf : (F)Ljava/lang/Float;
    //   152: areturn
    //   153: aload_3
    //   154: getfield sensor : Landroid/hardware/Sensor;
    //   157: invokevirtual getMaximumRange : ()F
    //   160: invokestatic valueOf : (F)Ljava/lang/Float;
    //   163: areturn
    //   164: iconst_3
    //   165: invokestatic setSystemErrorCode : (I)V
    //   168: new java/lang/StringBuilder
    //   171: dup
    //   172: invokespecial <init> : ()V
    //   175: astore #4
    //   177: aload #4
    //   179: ldc_w 'Querying sensor parameter '
    //   182: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   185: pop
    //   186: aload #4
    //   188: iload_2
    //   189: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   192: pop
    //   193: aload #4
    //   195: ldc_w ' is not supported for sensor type '
    //   198: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   201: pop
    //   202: aload #4
    //   204: aload_3
    //   205: getfield plSensorType : I
    //   208: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   211: pop
    //   212: iload_2
    //   213: ldc -2147483616
    //   215: if_icmpne -> 290
    //   218: ldc_w ' when using the Java-based sensor API'
    //   221: astore_3
    //   222: goto -> 225
    //   225: aload #4
    //   227: aload_3
    //   228: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   231: pop
    //   232: aload #4
    //   234: invokevirtual toString : ()Ljava/lang/String;
    //   237: invokestatic logSystemError : (Ljava/lang/String;)V
    //   240: aconst_null
    //   241: areturn
    //   242: ldc_w 'Unknown sensor parameter'
    //   245: invokestatic logSystemError : (Ljava/lang/String;)V
    //   248: aconst_null
    //   249: areturn
    //   250: bipush #6
    //   252: invokestatic setSystemErrorCode : (I)V
    //   255: new java/lang/StringBuilder
    //   258: dup
    //   259: invokespecial <init> : ()V
    //   262: astore_3
    //   263: aload_3
    //   264: ldc_w 'Failed to get sensor parameter: '
    //   267: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   270: pop
    //   271: aload_3
    //   272: aload #4
    //   274: invokevirtual toString : ()Ljava/lang/String;
    //   277: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   280: pop
    //   281: aload_3
    //   282: invokevirtual toString : ()Ljava/lang/String;
    //   285: invokestatic logSystemError : (Ljava/lang/String;)V
    //   288: aconst_null
    //   289: areturn
    //   290: ldc_w ''
    //   293: astore_3
    //   294: goto -> 225
    //   297: astore #4
    //   299: goto -> 250
    // Exception table:
    //   from	to	target	type
    //   96	100	297	java/lang/Exception
    //   103	122	297	java/lang/Exception
    //   122	132	297	java/lang/Exception
    //   134	142	297	java/lang/Exception
    //   142	153	297	java/lang/Exception
    //   153	164	297	java/lang/Exception
    //   164	212	297	java/lang/Exception
    //   225	240	297	java/lang/Exception
    //   242	248	297	java/lang/Exception
  }
  
  public boolean init() {
    this.sensorManager = null;
    this.sensorCacheInfo = new Vector<SensorCacheInfo>();
    this.sensorIndexMap = new HashMap<Sensor, Integer>();
    return true;
  }
  
  public void onAccuracyChanged(Sensor paramSensor, int paramInt) {}
  
  public void onSensorChanged(SensorEvent paramSensorEvent) {
    SensorCacheInfo sensorCacheInfo = (SensorCacheInfo)this.sensorIndexMap.get(paramSensorEvent.sensor);
    if (sensorCacheInfo == null)
      return; 
    sensorCacheInfo = getSensorCacheInfo(((Integer)sensorCacheInfo).intValue());
    if (sensorCacheInfo == null)
      return; 
    byte b = 0;
    int i = paramSensorEvent.accuracy;
    if (i != 0) {
      if (i != 1) {
        if (i != 2) {
          if (i == 3)
            b = 4; 
        } else {
          b = 3;
        } 
      } else {
        b = 2;
      } 
    } else {
      b = 1;
    } 
    newDataAvailable(sensorCacheInfo.cacheIndex, paramSensorEvent.timestamp, b, paramSensorEvent.values);
  }
  
  boolean open(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: iload_2
    //   1: getstatic com/vuforia/ar/pl/SensorController.AR_SENSOR_INDEX_DONTCARE : I
    //   4: if_icmpne -> 62
    //   7: iconst_0
    //   8: istore_2
    //   9: iload_2
    //   10: aload_0
    //   11: getfield sensorCacheInfo : Ljava/util/Vector;
    //   14: invokevirtual size : ()I
    //   17: if_icmpge -> 57
    //   20: aload_0
    //   21: getfield sensorCacheInfo : Ljava/util/Vector;
    //   24: iload_2
    //   25: invokevirtual get : (I)Ljava/lang/Object;
    //   28: checkcast com/vuforia/ar/pl/SensorController$SensorCacheInfo
    //   31: astore_3
    //   32: aload_3
    //   33: getfield plSensorType : I
    //   36: iload_1
    //   37: if_icmpne -> 50
    //   40: aload_3
    //   41: getfield isDefaultSensor : Z
    //   44: ifeq -> 50
    //   47: goto -> 74
    //   50: iload_2
    //   51: iconst_1
    //   52: iadd
    //   53: istore_2
    //   54: goto -> 9
    //   57: aconst_null
    //   58: astore_3
    //   59: goto -> 74
    //   62: aload_0
    //   63: getfield sensorCacheInfo : Ljava/util/Vector;
    //   66: iload_2
    //   67: invokevirtual get : (I)Ljava/lang/Object;
    //   70: checkcast com/vuforia/ar/pl/SensorController$SensorCacheInfo
    //   73: astore_3
    //   74: aload_3
    //   75: ifnonnull -> 90
    //   78: iconst_2
    //   79: invokestatic setSystemErrorCode : (I)V
    //   82: ldc_w 'No sensor matching the requested sensor device info has been found'
    //   85: invokestatic logSystemError : (Ljava/lang/String;)V
    //   88: iconst_0
    //   89: ireturn
    //   90: aload_0
    //   91: getfield sensorEventHandler : Landroid/os/Handler;
    //   94: ifnonnull -> 199
    //   97: aload_0
    //   98: invokevirtual start : ()V
    //   101: aload_0
    //   102: new android/os/Handler
    //   105: dup
    //   106: aload_0
    //   107: invokevirtual getLooper : ()Landroid/os/Looper;
    //   110: invokespecial <init> : (Landroid/os/Looper;)V
    //   113: putfield sensorEventHandler : Landroid/os/Handler;
    //   116: goto -> 199
    //   119: astore #4
    //   121: bipush #6
    //   123: invokestatic setSystemErrorCode : (I)V
    //   126: new java/lang/StringBuilder
    //   129: dup
    //   130: invokespecial <init> : ()V
    //   133: astore #5
    //   135: aload #5
    //   137: ldc_w 'Failed to '
    //   140: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   143: pop
    //   144: aload_0
    //   145: invokevirtual isAlive : ()Z
    //   148: ifeq -> 158
    //   151: ldc_w 'retrieve a handler for the sensor event handler thread'
    //   154: astore_3
    //   155: goto -> 162
    //   158: ldc_w 'start Java handler thread for sensor events'
    //   161: astore_3
    //   162: aload #5
    //   164: aload_3
    //   165: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   168: pop
    //   169: aload #5
    //   171: ldc_w ': '
    //   174: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   177: pop
    //   178: aload #5
    //   180: aload #4
    //   182: invokevirtual toString : ()Ljava/lang/String;
    //   185: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   188: pop
    //   189: aload #5
    //   191: invokevirtual toString : ()Ljava/lang/String;
    //   194: invokestatic logSystemError : (Ljava/lang/String;)V
    //   197: iconst_0
    //   198: ireturn
    //   199: iconst_1
    //   200: ireturn
    // Exception table:
    //   from	to	target	type
    //   97	116	119	java/lang/Exception
  }
  
  boolean setTypedSensorParameter(int paramInt1, int paramInt2, Object paramObject) {
    // Byte code:
    //   0: aload_0
    //   1: iload_1
    //   2: invokespecial getSensorCacheInfo : (I)Lcom/vuforia/ar/pl/SensorController$SensorCacheInfo;
    //   5: astore #5
    //   7: aload #5
    //   9: ifnonnull -> 23
    //   12: iconst_4
    //   13: invokestatic setSystemErrorCode : (I)V
    //   16: ldc 'Sensor handle is invalid'
    //   18: invokestatic logSystemError : (Ljava/lang/String;)V
    //   21: iconst_0
    //   22: ireturn
    //   23: iload_2
    //   24: lookupswitch default -> 100, -2147483647 -> 179, -2147483646 -> 179, -2147483644 -> 179, -2147483640 -> 179, -2147483632 -> 179, -2147483616 -> 179, -2147483584 -> 107, -2147483520 -> 179
    //   100: iconst_3
    //   101: invokestatic setSystemErrorCode : (I)V
    //   104: goto -> 258
    //   107: aload_3
    //   108: checkcast java/lang/Number
    //   111: invokevirtual intValue : ()I
    //   114: istore_1
    //   115: iload_1
    //   116: iconst_1
    //   117: if_icmplt -> 136
    //   120: iload_1
    //   121: iconst_4
    //   122: if_icmple -> 128
    //   125: goto -> 136
    //   128: aload #5
    //   130: iload_1
    //   131: putfield requestedAbstractUpdateRate : I
    //   134: iconst_1
    //   135: ireturn
    //   136: iconst_2
    //   137: invokestatic setSystemErrorCode : (I)V
    //   140: new java/lang/StringBuilder
    //   143: dup
    //   144: invokespecial <init> : ()V
    //   147: astore_3
    //   148: aload_3
    //   149: ldc_w 'Invalid abstract sensor update interval ('
    //   152: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   155: pop
    //   156: aload_3
    //   157: iload_1
    //   158: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   161: pop
    //   162: aload_3
    //   163: ldc_w ')'
    //   166: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   169: pop
    //   170: aload_3
    //   171: invokevirtual toString : ()Ljava/lang/String;
    //   174: invokestatic logSystemError : (Ljava/lang/String;)V
    //   177: iconst_0
    //   178: ireturn
    //   179: iconst_3
    //   180: invokestatic setSystemErrorCode : (I)V
    //   183: new java/lang/StringBuilder
    //   186: dup
    //   187: invokespecial <init> : ()V
    //   190: astore #4
    //   192: aload #4
    //   194: ldc_w 'Sensor parameter '
    //   197: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   200: pop
    //   201: aload #4
    //   203: iload_2
    //   204: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   207: pop
    //   208: aload #4
    //   210: ldc_w ' cannot be set for sensor type '
    //   213: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   216: pop
    //   217: aload #4
    //   219: aload #5
    //   221: getfield plSensorType : I
    //   224: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   227: pop
    //   228: iload_2
    //   229: ldc -2147483616
    //   231: if_icmpne -> 306
    //   234: ldc_w ' when using the Java-based sensor API'
    //   237: astore_3
    //   238: goto -> 241
    //   241: aload #4
    //   243: aload_3
    //   244: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   247: pop
    //   248: aload #4
    //   250: invokevirtual toString : ()Ljava/lang/String;
    //   253: invokestatic logSystemError : (Ljava/lang/String;)V
    //   256: iconst_0
    //   257: ireturn
    //   258: ldc_w 'Unknown sensor parameter'
    //   261: invokestatic logSystemError : (Ljava/lang/String;)V
    //   264: iconst_0
    //   265: ireturn
    //   266: bipush #6
    //   268: invokestatic setSystemErrorCode : (I)V
    //   271: new java/lang/StringBuilder
    //   274: dup
    //   275: invokespecial <init> : ()V
    //   278: astore_3
    //   279: aload_3
    //   280: ldc_w 'Failed to get sensor parameter: '
    //   283: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   286: pop
    //   287: aload_3
    //   288: aload #4
    //   290: invokevirtual toString : ()Ljava/lang/String;
    //   293: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   296: pop
    //   297: aload_3
    //   298: invokevirtual toString : ()Ljava/lang/String;
    //   301: invokestatic logSystemError : (Ljava/lang/String;)V
    //   304: iconst_0
    //   305: ireturn
    //   306: ldc_w ''
    //   309: astore_3
    //   310: goto -> 241
    //   313: astore #4
    //   315: goto -> 266
    // Exception table:
    //   from	to	target	type
    //   100	104	313	java/lang/Exception
    //   107	115	313	java/lang/Exception
    //   128	134	313	java/lang/Exception
    //   136	177	313	java/lang/Exception
    //   179	228	313	java/lang/Exception
    //   241	256	313	java/lang/Exception
    //   258	264	313	java/lang/Exception
  }
  
  boolean start(int paramInt) {
    SensorCacheInfo sensorCacheInfo = getSensorCacheInfo(paramInt);
    boolean bool = false;
    if (sensorCacheInfo == null) {
      SystemTools.setSystemErrorCode(4);
      SystemTools.logSystemError("Sensor handle is invalid");
      return false;
    } 
    int i = translateSensorUpdateIntervalToAndroid(sensorCacheInfo.requestedAbstractUpdateRate);
    paramInt = i;
    if (i < 0)
      paramInt = 1; 
    try {
      boolean bool1 = this.sensorManager.registerListener(this, sensorCacheInfo.sensor, paramInt, this.sensorEventHandler);
      bool = bool1;
    } catch (Exception exception) {}
    if (!bool) {
      SystemTools.setSystemErrorCode(6);
      SystemTools.logSystemError("Failed to start sensor, could not register sensor event listerer");
    } 
    return bool;
  }
  
  boolean stop(int paramInt) {
    SensorCacheInfo sensorCacheInfo = getSensorCacheInfo(paramInt);
    if (sensorCacheInfo == null) {
      SystemTools.setSystemErrorCode(4);
      SystemTools.logSystemError("Sensor handle is invalid");
      return false;
    } 
    try {
      this.sensorManager.unregisterListener(this, sensorCacheInfo.sensor);
      return true;
    } catch (Exception exception) {
      SystemTools.setSystemErrorCode(6);
      SystemTools.logSystemError("Failed to stop sensor, could not unregister sensor event listerer");
      return false;
    } 
  }
  
  public class SensorCacheInfo {
    int cacheIndex;
    
    boolean isDefaultSensor;
    
    int plSensorType;
    
    int requestedAbstractUpdateRate;
    
    Sensor sensor;
    
    float[] valuesForForcedSensorEvent;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\ar\pl\SensorController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
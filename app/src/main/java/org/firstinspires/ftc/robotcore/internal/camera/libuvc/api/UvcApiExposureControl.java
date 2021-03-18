package org.firstinspires.ftc.robotcore.internal.camera.libuvc.api;

import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.external.function.Supplier;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject.UvcDeviceHandle;
import org.firstinspires.ftc.robotcore.internal.collections.MutableReference;
import org.firstinspires.ftc.robotcore.internal.system.Tracer;
import org.firstinspires.ftc.robotcore.internal.vuforia.externalprovider.ExtendedExposureMode;

public class UvcApiExposureControl implements ExposureControl {
  public static final String TAG = "UvcApiExposureControl";
  
  public static boolean TRACE = true;
  
  public static boolean TRACE_VERBOSE = false;
  
  protected long cachedExposureNs = 0L;
  
  protected ElapsedTime cachedExposureRefresh = null;
  
  protected final Object lock = new Object();
  
  protected Tracer tracer = Tracer.create(getTag(), TRACE);
  
  protected UvcDeviceHandle uvcDeviceHandle;
  
  protected Tracer verboseTracer = Tracer.create(getTag(), TRACE_VERBOSE);
  
  public UvcApiExposureControl(UvcDeviceHandle paramUvcDeviceHandle) {
    this.uvcDeviceHandle = paramUvcDeviceHandle;
  }
  
  public static ExposureControl.Mode fromVuforia(int paramInt) {
    return fromVuforia(ExtendedExposureMode.from(paramInt));
  }
  
  public static ExposureControl.Mode fromVuforia(ExtendedExposureMode paramExtendedExposureMode) {
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$internal$vuforia$externalprovider$ExtendedExposureMode[paramExtendedExposureMode.ordinal()];
    return (i != 1) ? ((i != 2) ? ((i != 3) ? ((i != 4) ? ((i != 5) ? ExposureControl.Mode.Unknown : ExposureControl.Mode.ShutterPriority) : ExposureControl.Mode.Manual) : ExposureControl.Mode.ContinuousAuto) : ExposureControl.Mode.Auto) : ExposureControl.Mode.AperturePriority;
  }
  
  public static ExtendedExposureMode toVuforia(ExposureControl.Mode paramMode) {
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$hardware$camera$controls$ExposureControl$Mode[paramMode.ordinal()];
    return (i != 1) ? ((i != 2) ? ((i != 3) ? ((i != 4) ? ((i != 5) ? ExtendedExposureMode.UNKNOWN : ExtendedExposureMode.SHUTTER_PRIORITY) : ExtendedExposureMode.MANUAL) : ExtendedExposureMode.CONTINUOUS_AUTO) : ExtendedExposureMode.AUTO) : ExtendedExposureMode.APERTURE_PRIORITY;
  }
  
  public long getCachedExposure(final TimeUnit resultUnit, final MutableReference<Boolean> refreshed, final long permittedStaleness, final TimeUnit permittedStalenessUnit) {
    Tracer tracer = this.verboseTracer;
    return ((Long)tracer.traceResult(tracer.format("getCachedExposure(%s)", new Object[] { resultUnit }), new Supplier<Long>() {
          public Long get() {
            // Byte code:
            //   0: aload_0
            //   1: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/libuvc/api/UvcApiExposureControl;
            //   4: getfield lock : Ljava/lang/Object;
            //   7: astore #4
            //   9: aload #4
            //   11: monitorenter
            //   12: aload_0
            //   13: getfield val$refreshed : Lorg/firstinspires/ftc/robotcore/internal/collections/MutableReference;
            //   16: astore #5
            //   18: iconst_0
            //   19: istore_1
            //   20: aload #5
            //   22: iconst_0
            //   23: invokestatic valueOf : (Z)Ljava/lang/Boolean;
            //   26: invokevirtual setValue : (Ljava/lang/Object;)V
            //   29: aload_0
            //   30: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/libuvc/api/UvcApiExposureControl;
            //   33: getfield cachedExposureRefresh : Lcom/qualcomm/robotcore/util/ElapsedTime;
            //   36: ifnonnull -> 42
            //   39: goto -> 135
            //   42: getstatic java/util/concurrent/TimeUnit.NANOSECONDS : Ljava/util/concurrent/TimeUnit;
            //   45: aload_0
            //   46: getfield val$permittedStaleness : J
            //   49: aload_0
            //   50: getfield val$permittedStalenessUnit : Ljava/util/concurrent/TimeUnit;
            //   53: invokevirtual convert : (JLjava/util/concurrent/TimeUnit;)J
            //   56: lstore_2
            //   57: aload_0
            //   58: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/libuvc/api/UvcApiExposureControl;
            //   61: getfield cachedExposureRefresh : Lcom/qualcomm/robotcore/util/ElapsedTime;
            //   64: invokevirtual nanoseconds : ()J
            //   67: lload_2
            //   68: lcmp
            //   69: iflt -> 75
            //   72: goto -> 135
            //   75: iload_1
            //   76: ifeq -> 101
            //   79: aload_0
            //   80: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/libuvc/api/UvcApiExposureControl;
            //   83: getstatic java/util/concurrent/TimeUnit.NANOSECONDS : Ljava/util/concurrent/TimeUnit;
            //   86: invokevirtual getExposure : (Ljava/util/concurrent/TimeUnit;)J
            //   89: pop2
            //   90: aload_0
            //   91: getfield val$refreshed : Lorg/firstinspires/ftc/robotcore/internal/collections/MutableReference;
            //   94: iconst_1
            //   95: invokestatic valueOf : (Z)Ljava/lang/Boolean;
            //   98: invokevirtual setValue : (Ljava/lang/Object;)V
            //   101: aload_0
            //   102: getfield val$resultUnit : Ljava/util/concurrent/TimeUnit;
            //   105: aload_0
            //   106: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/camera/libuvc/api/UvcApiExposureControl;
            //   109: getfield cachedExposureNs : J
            //   112: getstatic java/util/concurrent/TimeUnit.NANOSECONDS : Ljava/util/concurrent/TimeUnit;
            //   115: invokevirtual convert : (JLjava/util/concurrent/TimeUnit;)J
            //   118: lstore_2
            //   119: aload #4
            //   121: monitorexit
            //   122: lload_2
            //   123: invokestatic valueOf : (J)Ljava/lang/Long;
            //   126: areturn
            //   127: astore #5
            //   129: aload #4
            //   131: monitorexit
            //   132: aload #5
            //   134: athrow
            //   135: iconst_1
            //   136: istore_1
            //   137: goto -> 75
            // Exception table:
            //   from	to	target	type
            //   12	18	127	finally
            //   20	39	127	finally
            //   42	57	127	finally
            //   57	72	127	finally
            //   79	101	127	finally
            //   101	127	127	finally
            //   129	132	127	finally
          }
        })).longValue();
  }
  
  public long getExposure(final TimeUnit resultUnit) {
    Tracer tracer = this.verboseTracer;
    return ((Long)tracer.traceResult(tracer.format("getExposure(%s)", new Object[] { resultUnit }), new Supplier<Long>() {
          public Long get() {
            synchronized (UvcApiExposureControl.this.lock) {
              UvcApiExposureControl.this.setCachedExposureNs(UvcApiExposureControl.this.uvcDeviceHandle.getExposure());
              long l = resultUnit.convert(UvcApiExposureControl.this.cachedExposureNs, TimeUnit.NANOSECONDS);
              return Long.valueOf(l);
            } 
          }
        })).longValue();
  }
  
  public long getMaxExposure(final TimeUnit resultUnit) {
    Tracer tracer = this.tracer;
    return ((Long)tracer.traceResult(tracer.format("getMaxExposure(%s)", new Object[] { resultUnit }), new Supplier<Long>() {
          public Long get() {
            return Long.valueOf(resultUnit.convert(UvcApiExposureControl.this.uvcDeviceHandle.getMaxExposure(), TimeUnit.NANOSECONDS));
          }
        })).longValue();
  }
  
  public long getMinExposure(final TimeUnit resultUnit) {
    Tracer tracer = this.tracer;
    return ((Long)tracer.traceResult(tracer.format("getMinExposure(%s)", new Object[] { resultUnit }), new Supplier<Long>() {
          public Long get() {
            return Long.valueOf(resultUnit.convert(UvcApiExposureControl.this.uvcDeviceHandle.getMinExposure(), TimeUnit.NANOSECONDS));
          }
        })).longValue();
  }
  
  public ExposureControl.Mode getMode() {
    return (ExposureControl.Mode)this.tracer.traceResult("getMode()", new Supplier<ExposureControl.Mode>() {
          public ExposureControl.Mode get() {
            return UvcApiExposureControl.fromVuforia(UvcApiExposureControl.this.uvcDeviceHandle.getVuforiaExposureMode());
          }
        });
  }
  
  public String getTag() {
    return "UvcApiExposureControl";
  }
  
  public boolean isExposureSupported() {
    return ((Boolean)this.tracer.traceResult("isExposureSupported()", new Supplier<Boolean>() {
          public Boolean get() {
            return Boolean.valueOf(UvcApiExposureControl.this.uvcDeviceHandle.isExposureSupported());
          }
        })).booleanValue();
  }
  
  public boolean isModeSupported(final ExposureControl.Mode mode) {
    Tracer tracer = this.tracer;
    return ((Boolean)tracer.traceResult(tracer.format("isModeSupported(%s)", new Object[] { mode }), new Supplier<Boolean>() {
          public Boolean get() {
            return Boolean.valueOf(UvcApiExposureControl.this.uvcDeviceHandle.isVuforiaExposureModeSupported(UvcApiExposureControl.toVuforia(mode)));
          }
        })).booleanValue();
  }
  
  protected void setCachedExposureNs(long paramLong) {
    this.cachedExposureNs = paramLong;
    this.cachedExposureRefresh = new ElapsedTime();
  }
  
  public boolean setExposure(final long duration, final TimeUnit durationUnit) {
    Tracer tracer = this.tracer;
    return ((Boolean)tracer.traceResult(tracer.format("setExposure(%s %s)", new Object[] { Long.valueOf(duration), durationUnit }), new Supplier<Boolean>() {
          public Boolean get() {
            synchronized (UvcApiExposureControl.this.lock) {
              long l = TimeUnit.NANOSECONDS.convert(duration, durationUnit);
              boolean bool = UvcApiExposureControl.this.uvcDeviceHandle.setExposure(l);
              if (bool)
                UvcApiExposureControl.this.setCachedExposureNs(l); 
              return Boolean.valueOf(bool);
            } 
          }
        })).booleanValue();
  }
  
  public boolean setMode(final ExposureControl.Mode mode) {
    Tracer tracer = this.tracer;
    return ((Boolean)tracer.traceResult(tracer.format("setMode(%s)", new Object[] { mode }), new Supplier<Boolean>() {
          public Boolean get() {
            return Boolean.valueOf(UvcApiExposureControl.this.uvcDeviceHandle.setVuforiaExposureMode(UvcApiExposureControl.toVuforia(mode)));
          }
        })).booleanValue();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\api\UvcApiExposureControl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
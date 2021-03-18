package org.firstinspires.ftc.robotcore.internal.camera.delegating;

import com.qualcomm.robotcore.util.TypeConversion;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.external.function.Supplier;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.internal.collections.MutableReference;
import org.firstinspires.ftc.robotcore.internal.system.Tracer;

public class CachingExposureControl implements ExposureControl, DelegatingCameraControl {
  public static final String TAG = "CachingExposureControl";
  
  public static boolean TRACE = true;
  
  protected Camera camera = null;
  
  protected ExposureControl delegatedExposureControl;
  
  protected final ExposureControl fakeExposureControl;
  
  protected boolean isExposureSupported = false;
  
  protected boolean limitsInitialized = false;
  
  protected final Object lock = new Object();
  
  protected ExposureControl.Mode mode = null;
  
  protected long nsExposure = 0L;
  
  protected long nsMaxExposure = 0L;
  
  protected long nsMinExposure = 0L;
  
  protected Map<ExposureControl.Mode, Boolean> supportedModes = new HashMap<ExposureControl.Mode, Boolean>();
  
  protected Tracer tracer = Tracer.create(getTag(), TRACE);
  
  public CachingExposureControl() {
    ExposureControl exposureControl = new ExposureControl() {
        public long getCachedExposure(TimeUnit param1TimeUnit1, MutableReference<Boolean> param1MutableReference, long param1Long, TimeUnit param1TimeUnit2) {
          param1MutableReference.setValue(Boolean.valueOf(false));
          return 0L;
        }
        
        public long getExposure(TimeUnit param1TimeUnit) {
          return 0L;
        }
        
        public long getMaxExposure(TimeUnit param1TimeUnit) {
          return 0L;
        }
        
        public long getMinExposure(TimeUnit param1TimeUnit) {
          return 0L;
        }
        
        public ExposureControl.Mode getMode() {
          return ExposureControl.Mode.Unknown;
        }
        
        public boolean isExposureSupported() {
          return false;
        }
        
        public boolean isModeSupported(ExposureControl.Mode param1Mode) {
          return false;
        }
        
        public boolean setExposure(long param1Long, TimeUnit param1TimeUnit) {
          return false;
        }
        
        public boolean setMode(ExposureControl.Mode param1Mode) {
          return false;
        }
      };
    this.fakeExposureControl = exposureControl;
    this.delegatedExposureControl = exposureControl;
  }
  
  protected static boolean isUnknownExposure(long paramLong) {
    int i = paramLong cmp 0L;
    return (i == 0 || i < 0);
  }
  
  public long getCachedExposure(TimeUnit paramTimeUnit1, MutableReference<Boolean> paramMutableReference, long paramLong, TimeUnit paramTimeUnit2) {
    synchronized (this.lock) {
      if (this.isExposureSupported) {
        paramMutableReference.setValue(Boolean.valueOf(false));
        paramLong = this.delegatedExposureControl.getCachedExposure(TimeUnit.NANOSECONDS, paramMutableReference, paramLong, paramTimeUnit2);
        if (((Boolean)paramMutableReference.getValue()).booleanValue())
          this.nsExposure = paramLong; 
        paramLong = paramTimeUnit1.convert(paramLong, TimeUnit.NANOSECONDS);
        return paramLong;
      } 
      return 0L;
    } 
  }
  
  public long getExposure(TimeUnit paramTimeUnit) {
    synchronized (this.lock) {
      if (this.isExposureSupported) {
        long l = this.delegatedExposureControl.getExposure(TimeUnit.NANOSECONDS);
        this.nsExposure = l;
        l = paramTimeUnit.convert(l, TimeUnit.NANOSECONDS);
        return l;
      } 
      return 0L;
    } 
  }
  
  public long getMaxExposure(TimeUnit paramTimeUnit) {
    return paramTimeUnit.convert(this.nsMaxExposure, TimeUnit.NANOSECONDS);
  }
  
  public long getMinExposure(TimeUnit paramTimeUnit) {
    return paramTimeUnit.convert(this.nsMinExposure, TimeUnit.NANOSECONDS);
  }
  
  public ExposureControl.Mode getMode() {
    synchronized (this.lock) {
      ExposureControl.Mode mode = this.delegatedExposureControl.getMode();
      this.mode = mode;
      return mode;
    } 
  }
  
  public String getTag() {
    return "CachingExposureControl";
  }
  
  protected void initializeLimits() {
    for (ExposureControl.Mode mode : ExposureControl.Mode.values()) {
      if (mode != ExposureControl.Mode.Unknown)
        this.supportedModes.put(mode, Boolean.valueOf(this.delegatedExposureControl.isModeSupported(mode))); 
    } 
    boolean bool = this.delegatedExposureControl.isExposureSupported();
    this.isExposureSupported = bool;
    if (bool) {
      this.nsMinExposure = this.delegatedExposureControl.getMinExposure(TimeUnit.NANOSECONDS);
      this.nsMaxExposure = this.delegatedExposureControl.getMaxExposure(TimeUnit.NANOSECONDS);
    } 
  }
  
  public boolean isExposureSupported() {
    return this.isExposureSupported;
  }
  
  public boolean isModeSupported(final ExposureControl.Mode mode) {
    Tracer tracer = this.tracer;
    return ((Boolean)tracer.traceResult(tracer.format("isModeSupported(%s)", new Object[] { mode }), new Supplier<Boolean>() {
          public Boolean get() {
            return Boolean.valueOf(TypeConversion.toBoolean(CachingExposureControl.this.supportedModes.get(mode)));
          }
        })).booleanValue();
  }
  
  public void onCameraChanged(final Camera newCamera) {
    synchronized (this.lock) {
      this.tracer.trace(this.tracer.format("onCameraChanged(%s->%s)", new Object[] { this.camera, newCamera }), new Runnable() {
            public void run() {
              Camera camera1 = CachingExposureControl.this.camera;
              Camera camera2 = newCamera;
              if (camera1 != camera2) {
                CachingExposureControl.this.camera = camera2;
                if (CachingExposureControl.this.camera != null) {
                  CachingExposureControl cachingExposureControl1 = CachingExposureControl.this;
                  cachingExposureControl1.delegatedExposureControl = (ExposureControl)cachingExposureControl1.camera.getControl(ExposureControl.class);
                  if (CachingExposureControl.this.delegatedExposureControl == null) {
                    cachingExposureControl1 = CachingExposureControl.this;
                    cachingExposureControl1.delegatedExposureControl = cachingExposureControl1.fakeExposureControl;
                  } 
                  if (!CachingExposureControl.this.limitsInitialized) {
                    CachingExposureControl.this.initializeLimits();
                    if (CachingExposureControl.this.delegatedExposureControl != CachingExposureControl.this.fakeExposureControl)
                      CachingExposureControl.this.limitsInitialized = true; 
                  } 
                  CachingExposureControl.this.write();
                  CachingExposureControl.this.read();
                  return;
                } 
                CachingExposureControl cachingExposureControl = CachingExposureControl.this;
                cachingExposureControl.delegatedExposureControl = cachingExposureControl.fakeExposureControl;
              } 
            }
          });
      return;
    } 
  }
  
  protected void read() {
    this.mode = this.delegatedExposureControl.getMode();
    if (!this.limitsInitialized || this.isExposureSupported)
      this.nsExposure = this.delegatedExposureControl.getExposure(TimeUnit.NANOSECONDS); 
  }
  
  public boolean setExposure(long paramLong, TimeUnit paramTimeUnit) {
    if (paramLong > 0L)
      synchronized (this.lock) {
        if (this.isExposureSupported && this.delegatedExposureControl.setExposure(paramLong, paramTimeUnit)) {
          this.nsExposure = TimeUnit.NANOSECONDS.convert(paramLong, paramTimeUnit);
          return true;
        } 
      }  
    return false;
  }
  
  public boolean setMode(ExposureControl.Mode paramMode) {
    synchronized (this.lock) {
      if (this.isExposureSupported && this.delegatedExposureControl.setMode(paramMode)) {
        this.mode = paramMode;
        return true;
      } 
      return false;
    } 
  }
  
  protected void write() {
    ExposureControl.Mode mode = this.mode;
    if (mode != null && isModeSupported(mode))
      this.delegatedExposureControl.setMode(this.mode); 
    if (!isUnknownExposure(this.nsExposure) && this.isExposureSupported) {
      mode = this.mode;
      if (mode != null && mode != ExposureControl.Mode.Unknown && this.mode != ExposureControl.Mode.Auto && this.mode != ExposureControl.Mode.AperturePriority)
        this.delegatedExposureControl.setExposure(this.nsExposure, TimeUnit.NANOSECONDS); 
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\delegating\CachingExposureControl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
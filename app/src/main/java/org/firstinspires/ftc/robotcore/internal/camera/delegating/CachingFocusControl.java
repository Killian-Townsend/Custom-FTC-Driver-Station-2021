package org.firstinspires.ftc.robotcore.internal.camera.delegating;

import com.qualcomm.robotcore.util.TypeConversion;
import java.util.HashMap;
import java.util.Map;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.FocusControl;
import org.firstinspires.ftc.robotcore.internal.system.Tracer;

public class CachingFocusControl implements FocusControl, DelegatingCameraControl {
  public static final String TAG = "CachingFocusControl";
  
  public static boolean TRACE = true;
  
  protected Camera camera = null;
  
  protected FocusControl delegatedFocusControl;
  
  protected final FocusControl fakeFocusControl;
  
  protected double focusLength = -1.0D;
  
  protected boolean isFocusLengthSupported = false;
  
  protected boolean limitsInitialized = false;
  
  protected final Object lock = new Object();
  
  protected double maxFocusLength = -1.0D;
  
  protected double minFocusLength = -1.0D;
  
  protected FocusControl.Mode mode = null;
  
  protected Map<FocusControl.Mode, Boolean> supportedModes = new HashMap<FocusControl.Mode, Boolean>();
  
  protected Tracer tracer = Tracer.create(getTag(), TRACE);
  
  public CachingFocusControl() {
    FocusControl focusControl = new FocusControl() {
        public double getFocusLength() {
          return -1.0D;
        }
        
        public double getMaxFocusLength() {
          return -1.0D;
        }
        
        public double getMinFocusLength() {
          return -1.0D;
        }
        
        public FocusControl.Mode getMode() {
          return FocusControl.Mode.Unknown;
        }
        
        public boolean isFocusLengthSupported() {
          return false;
        }
        
        public boolean isModeSupported(FocusControl.Mode param1Mode) {
          return false;
        }
        
        public boolean setFocusLength(double param1Double) {
          return false;
        }
        
        public boolean setMode(FocusControl.Mode param1Mode) {
          return false;
        }
      };
    this.fakeFocusControl = focusControl;
    this.delegatedFocusControl = focusControl;
  }
  
  public static boolean isUnknownFocusLength(double paramDouble) {
    return (paramDouble < 0.0D);
  }
  
  public double getFocusLength() {
    synchronized (this.lock) {
      if (this.isFocusLengthSupported)
        this.focusLength = this.delegatedFocusControl.getFocusLength(); 
      return this.focusLength;
    } 
  }
  
  public double getMaxFocusLength() {
    return this.maxFocusLength;
  }
  
  public double getMinFocusLength() {
    return this.minFocusLength;
  }
  
  public FocusControl.Mode getMode() {
    synchronized (this.lock) {
      FocusControl.Mode mode = this.delegatedFocusControl.getMode();
      this.mode = mode;
      return mode;
    } 
  }
  
  public String getTag() {
    return "CachingFocusControl";
  }
  
  void initializeLimits() {
    for (FocusControl.Mode mode : FocusControl.Mode.values()) {
      if (mode != FocusControl.Mode.Unknown)
        this.supportedModes.put(mode, Boolean.valueOf(this.delegatedFocusControl.isModeSupported(mode))); 
    } 
    boolean bool = this.delegatedFocusControl.isFocusLengthSupported();
    this.isFocusLengthSupported = bool;
    if (bool) {
      this.minFocusLength = this.delegatedFocusControl.getMinFocusLength();
      this.maxFocusLength = this.delegatedFocusControl.getMaxFocusLength();
    } 
  }
  
  public boolean isFocusLengthSupported() {
    return this.isFocusLengthSupported;
  }
  
  public boolean isModeSupported(FocusControl.Mode paramMode) {
    return TypeConversion.toBoolean(this.supportedModes.get(paramMode));
  }
  
  public void onCameraChanged(Camera paramCamera) {
    synchronized (this.lock) {
      if (this.camera != paramCamera) {
        this.camera = paramCamera;
        if (paramCamera != null) {
          FocusControl focusControl = (FocusControl)paramCamera.getControl(FocusControl.class);
          this.delegatedFocusControl = focusControl;
          if (focusControl == null)
            this.delegatedFocusControl = this.fakeFocusControl; 
          if (!this.limitsInitialized) {
            initializeLimits();
            if (this.delegatedFocusControl != this.fakeFocusControl)
              this.limitsInitialized = true; 
          } 
          write();
          read();
        } else {
          this.delegatedFocusControl = this.fakeFocusControl;
        } 
      } 
      return;
    } 
  }
  
  protected void read() {
    this.mode = this.delegatedFocusControl.getMode();
    if (!this.limitsInitialized || this.isFocusLengthSupported)
      this.focusLength = this.delegatedFocusControl.getFocusLength(); 
  }
  
  public boolean setFocusLength(double paramDouble) {
    if (paramDouble >= 0.0D)
      synchronized (this.lock) {
        if (this.isFocusLengthSupported && this.delegatedFocusControl.setFocusLength(paramDouble)) {
          this.focusLength = paramDouble;
          return true;
        } 
      }  
    return false;
  }
  
  public boolean setMode(FocusControl.Mode paramMode) {
    synchronized (this.lock) {
      if (isModeSupported(paramMode) && this.delegatedFocusControl.setMode(paramMode)) {
        this.mode = paramMode;
        return true;
      } 
      return false;
    } 
  }
  
  protected void write() {
    FocusControl.Mode mode = this.mode;
    if (mode != null && isModeSupported(mode))
      this.delegatedFocusControl.setMode(this.mode); 
    if (!isUnknownFocusLength(this.focusLength) && this.isFocusLengthSupported) {
      mode = this.mode;
      if (mode != null && mode != FocusControl.Mode.Unknown && this.mode != FocusControl.Mode.Auto && this.mode != FocusControl.Mode.Fixed)
        this.delegatedFocusControl.setFocusLength(this.focusLength); 
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\delegating\CachingFocusControl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
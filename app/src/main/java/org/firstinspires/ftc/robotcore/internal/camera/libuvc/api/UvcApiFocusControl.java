package org.firstinspires.ftc.robotcore.internal.camera.libuvc.api;

import org.firstinspires.ftc.robotcore.external.function.Supplier;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.FocusControl;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject.UvcDeviceHandle;
import org.firstinspires.ftc.robotcore.internal.system.Tracer;
import org.firstinspires.ftc.robotcore.internal.vuforia.externalprovider.FocusMode;

public class UvcApiFocusControl implements FocusControl {
  public static final String TAG = "UvcApiFocusControl";
  
  public static boolean TRACE = true;
  
  protected Tracer tracer = Tracer.create(getTag(), TRACE);
  
  protected UvcDeviceHandle uvcDeviceHandle;
  
  public UvcApiFocusControl(UvcDeviceHandle paramUvcDeviceHandle) {
    this.uvcDeviceHandle = paramUvcDeviceHandle;
  }
  
  public static FocusControl.Mode fromVuforia(int paramInt) {
    return fromVuforia(FocusMode.from(paramInt));
  }
  
  public static FocusControl.Mode fromVuforia(FocusMode paramFocusMode) {
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$internal$vuforia$externalprovider$FocusMode[paramFocusMode.ordinal()];
    return (i != 1) ? ((i != 2) ? ((i != 3) ? ((i != 4) ? ((i != 5) ? FocusControl.Mode.Unknown : FocusControl.Mode.Fixed) : FocusControl.Mode.Infinity) : FocusControl.Mode.Macro) : FocusControl.Mode.ContinuousAuto) : FocusControl.Mode.Auto;
  }
  
  public static FocusMode toVuforia(FocusControl.Mode paramMode) {
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$hardware$camera$controls$FocusControl$Mode[paramMode.ordinal()];
    return (i != 1) ? ((i != 2) ? ((i != 3) ? ((i != 4) ? ((i != 5) ? FocusMode.UNKNOWN : FocusMode.FIXED) : FocusMode.INFINITY_FOCUS) : FocusMode.MACRO) : FocusMode.CONTINUOUS_AUTO) : FocusMode.AUTO;
  }
  
  public double getFocusLength() {
    return ((Double)this.tracer.traceResult("getFocusLength()", new Supplier<Double>() {
          public Double get() {
            return Double.valueOf(UvcApiFocusControl.this.uvcDeviceHandle.getFocusLength());
          }
        })).doubleValue();
  }
  
  public double getMaxFocusLength() {
    return ((Double)this.tracer.traceResult("getMaxFocusLength()", new Supplier<Double>() {
          public Double get() {
            return Double.valueOf(UvcApiFocusControl.this.uvcDeviceHandle.getMaxFocusLength());
          }
        })).doubleValue();
  }
  
  public double getMinFocusLength() {
    return ((Double)this.tracer.traceResult("getMinFocusLength()", new Supplier<Double>() {
          public Double get() {
            return Double.valueOf(UvcApiFocusControl.this.uvcDeviceHandle.getMinFocusLength());
          }
        })).doubleValue();
  }
  
  public FocusControl.Mode getMode() {
    return fromVuforia(this.uvcDeviceHandle.getVuforiaFocusMode());
  }
  
  public String getTag() {
    return "UvcApiFocusControl";
  }
  
  public boolean isFocusLengthSupported() {
    return ((Boolean)this.tracer.traceResult("isFocusLengthSupported", new Supplier<Boolean>() {
          public Boolean get() {
            return Boolean.valueOf(UvcApiFocusControl.this.uvcDeviceHandle.isFocusLengthSupported());
          }
        })).booleanValue();
  }
  
  public boolean isModeSupported(final FocusControl.Mode mode) {
    Tracer tracer = this.tracer;
    return ((Boolean)tracer.traceResult(tracer.format("isModeSupported(%s)", new Object[] { mode }), new Supplier<Boolean>() {
          public Boolean get() {
            return Boolean.valueOf(UvcApiFocusControl.this.uvcDeviceHandle.isVuforiaFocusModeSupported(UvcApiFocusControl.toVuforia(mode)));
          }
        })).booleanValue();
  }
  
  public boolean setFocusLength(final double focusLength) {
    Tracer tracer = this.tracer;
    return ((Boolean)tracer.traceResult(tracer.format("setFocusLength(%s)", new Object[] { Double.valueOf(focusLength) }), new Supplier<Boolean>() {
          public Boolean get() {
            return Boolean.valueOf(UvcApiFocusControl.this.uvcDeviceHandle.setFocusLength(focusLength));
          }
        })).booleanValue();
  }
  
  public boolean setMode(final FocusControl.Mode mode) {
    Tracer tracer = this.tracer;
    return ((Boolean)tracer.traceResult(tracer.format("setMode(%s)", new Object[] { mode }), new Supplier<Boolean>() {
          public Boolean get() {
            return Boolean.valueOf(UvcApiFocusControl.this.uvcDeviceHandle.setVuforiaFocusMode(UvcApiFocusControl.toVuforia(mode)));
          }
        })).booleanValue();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\api\UvcApiFocusControl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
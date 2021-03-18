package org.firstinspires.ftc.robotcore.internal.camera.calibration;

import org.firstinspires.ftc.robotcore.internal.system.Misc;

public class VendorProductCalibrationIdentity implements CameraCalibrationIdentity {
  public final int pid;
  
  public final int vid;
  
  public VendorProductCalibrationIdentity(int paramInt1, int paramInt2) {
    this.vid = paramInt1;
    this.pid = paramInt2;
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject instanceof VendorProductCalibrationIdentity) {
      paramObject = paramObject;
      return (this.vid == ((VendorProductCalibrationIdentity)paramObject).vid && this.pid == ((VendorProductCalibrationIdentity)paramObject).pid);
    } 
    return super.equals(paramObject);
  }
  
  public int hashCode() {
    return Integer.valueOf(this.vid).hashCode() ^ Integer.valueOf(this.pid).hashCode() ^ 0xB438B;
  }
  
  public boolean isDegenerate() {
    return (this.vid == 0 || this.pid == 0);
  }
  
  public String toString() {
    return Misc.formatInvariant("%s(vid=0x%04x,pid=0x%04x)", new Object[] { getClass().getSimpleName(), Integer.valueOf(this.vid), Integer.valueOf(this.pid) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\calibration\VendorProductCalibrationIdentity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
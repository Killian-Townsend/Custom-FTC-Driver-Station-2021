package org.firstinspires.ftc.robotcore.internal.camera;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSequenceId;
import org.firstinspires.ftc.robotcore.internal.system.Misc;

public class CameraCaptureSequenceIdImpl implements CameraCaptureSequenceId {
  private final RefCountedCamera camera;
  
  private final Class<? extends CameraCaptureSequenceIdImpl> clazz;
  
  private final int idValue;
  
  public CameraCaptureSequenceIdImpl(RefCountedCamera paramRefCountedCamera, int paramInt, Class<? extends CameraCaptureSequenceIdImpl> paramClass) {
    this.camera = paramRefCountedCamera;
    this.idValue = paramInt;
    this.clazz = paramClass;
  }
  
  public boolean equals(Object paramObject) {
    if (this.clazz.isInstance(paramObject)) {
      paramObject = this.clazz.cast(paramObject);
      return (this.camera == ((CameraCaptureSequenceIdImpl)paramObject).camera && getIdValue() == paramObject.getIdValue());
    } 
    return super.equals(paramObject);
  }
  
  public int getIdValue() {
    return this.idValue;
  }
  
  public int hashCode() {
    return Integer.valueOf(this.idValue).hashCode() ^ this.clazz.hashCode() ^ 0x5358E;
  }
  
  public String toString() {
    return Misc.formatInvariant("%s(%d)", new Object[] { getClass().getSimpleName(), Integer.valueOf(this.idValue) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\CameraCaptureSequenceIdImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
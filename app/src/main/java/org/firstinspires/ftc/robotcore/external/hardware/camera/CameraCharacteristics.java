package org.firstinspires.ftc.robotcore.external.hardware.camera;

import java.util.List;
import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.internal.system.Misc;

public interface CameraCharacteristics {
  List<CameraMode> getAllCameraModes();
  
  int[] getAndroidFormats();
  
  Size getDefaultSize(int paramInt);
  
  int getMaxFramesPerSecond(int paramInt, Size paramSize);
  
  long getMinFrameDuration(int paramInt, Size paramSize);
  
  Size[] getSizes(int paramInt);
  
  public static class CameraMode {
    public final int androidFormat;
    
    public final int fps;
    
    public final boolean isDefaultSize;
    
    public final long nsFrameDuration;
    
    public final Size size;
    
    public CameraMode(int param1Int, Size param1Size, long param1Long, boolean param1Boolean) {
      this.androidFormat = param1Int;
      this.size = param1Size;
      this.nsFrameDuration = param1Long;
      this.fps = (int)(1000000000L / param1Long);
      this.isDefaultSize = param1Boolean;
    }
    
    public boolean equals(Object param1Object) {
      if (param1Object instanceof CameraMode) {
        param1Object = param1Object;
        return (this.androidFormat == ((CameraMode)param1Object).androidFormat && this.size.equals(((CameraMode)param1Object).size) && this.nsFrameDuration == ((CameraMode)param1Object).nsFrameDuration && this.fps == ((CameraMode)param1Object).fps);
      } 
      return super.equals(param1Object);
    }
    
    public int hashCode() {
      return Integer.valueOf(this.androidFormat).hashCode() ^ this.size.hashCode() ^ Integer.valueOf(this.fps).hashCode();
    }
    
    public String toString() {
      return Misc.formatInvariant("CameraMode(format=%d %dx%d fps=%d)", new Object[] { Integer.valueOf(this.androidFormat), Integer.valueOf(this.size.getWidth()), Integer.valueOf(this.size.getHeight()), Integer.valueOf(this.fps) });
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\hardware\camera\CameraCharacteristics.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
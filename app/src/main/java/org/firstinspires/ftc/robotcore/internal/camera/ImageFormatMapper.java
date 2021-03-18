package org.firstinspires.ftc.robotcore.internal.camera;

import android.graphics.ImageFormat;
import com.qualcomm.robotcore.util.ClassUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.constants.UvcFrameFormat;
import org.firstinspires.ftc.robotcore.internal.vuforia.externalprovider.FrameFormat;

public class ImageFormatMapper {
  protected static Format[] formats = new Format[] { 
      new Format("YUY2", 20, UvcFrameFormat.YUY2, 0, FrameFormat.YUYV, UUID.fromString("32595559-0000-0010-8000-00AA00389B71"), "YUY2"), new Format("H264", 0, UvcFrameFormat.UNKNOWN, 0, FrameFormat.UNKNOWN, UUID.fromString("34363248-0000-0010-8000-00AA00389B71"), "H264"), new Format("MJPG", 0, UvcFrameFormat.UNKNOWN, 0, FrameFormat.UNKNOWN, UUID.fromString("47504A4D-0000-0000-0000-000000000000"), "MJPG"), new Format("RGB565", 4, UvcFrameFormat.UNKNOWN, 1, FrameFormat.UNKNOWN, UUID.fromString("e436eb7b-524f-11ce-9f53-0020af0ba770"), null), new Format("RGB888", 3, UvcFrameFormat.RGB, 2, FrameFormat.UNKNOWN, UUID.fromString("e436eb7d-524f-11ce-9f53-0020af0ba770"), null), new Format("RGB8888", 1, UvcFrameFormat.UNKNOWN, 16, FrameFormat.UNKNOWN, UUID.fromString("e436eb7e-524f-11ce-9f53-0020af0ba770"), null), new Format("Y8", getImageFormatConst("Y8"), UvcFrameFormat.GRAY8, 4, FrameFormat.UNKNOWN, UUID.fromString("20203859-0000-0010-8000-00AA00389B71"), "Y8"), new Format("Y800", getImageFormatConst("Y8"), UvcFrameFormat.GRAY8, 4, FrameFormat.UNKNOWN, UUID.fromString("30303859-0000-0010-8000-00AA00389B71"), "Y800"), new Format("GREY", getImageFormatConst("Y8"), UvcFrameFormat.GRAY8, 4, FrameFormat.UNKNOWN, UUID.fromString("59455247-0000-0010-8000-00AA00389B71"), "GREY"), new Format("YV12", 842094169, UvcFrameFormat.UNKNOWN, 0, FrameFormat.UNKNOWN, UUID.fromString("32315659-0000-0010-8000-00AA00389B71"), "YV12"), 
      new Format("NV12", 0, UvcFrameFormat.UNKNOWN, 0, FrameFormat.UNKNOWN, UUID.fromString("3231564E-0000-0010-8000-00AA00389B71"), "NV12"), new Format("M420", 0, UvcFrameFormat.UNKNOWN, 0, FrameFormat.UNKNOWN, UUID.fromString("3032344D-0000-0010-8000-00AA00389B71"), "M420"), new Format("I420", 0, UvcFrameFormat.UNKNOWN, 0, FrameFormat.UNKNOWN, UUID.fromString("30323449-0000-0010-8000-00AA00389B71"), "I420") };
  
  public static Format[] all() {
    return formats;
  }
  
  public static List<Format> allFromAndroid(int paramInt) {
    ArrayList<Format> arrayList = new ArrayList();
    for (Format format : formats) {
      if (format.android == paramInt)
        arrayList.add(format); 
    } 
    return arrayList;
  }
  
  public static List<Format> allFromGuid(UUID paramUUID) {
    ArrayList<Format> arrayList = new ArrayList();
    for (Format format : formats) {
      if (format.guid.equals(paramUUID))
        arrayList.add(format); 
    } 
    return arrayList;
  }
  
  public static List<Format> allFromUvc(UvcFrameFormat paramUvcFrameFormat) {
    ArrayList<Format> arrayList = new ArrayList();
    for (Format format : formats) {
      if (format.uvc == paramUvcFrameFormat)
        arrayList.add(format); 
    } 
    return arrayList;
  }
  
  public static List<Format> allFromVuforiaPixelFormat(int paramInt) {
    ArrayList<Format> arrayList = new ArrayList();
    for (Format format : formats) {
      if (format.vuforiaPixelFormat == paramInt)
        arrayList.add(format); 
    } 
    return arrayList;
  }
  
  public static List<Format> allFromVuforiaWebcam(FrameFormat paramFrameFormat) {
    ArrayList<Format> arrayList = new ArrayList();
    for (Format format : formats) {
      if (format.vuforiaWebcam == paramFrameFormat)
        arrayList.add(format); 
    } 
    return arrayList;
  }
  
  public static int androidFromVuforiaPixelFormat(int paramInt) {
    for (Format format : formats) {
      if (format.vuforiaPixelFormat == paramInt)
        return format.android; 
    } 
    return 0;
  }
  
  public static int androidFromVuforiaWebcam(FrameFormat paramFrameFormat) {
    for (Format format : formats) {
      if (format.vuforiaWebcam == paramFrameFormat)
        return format.android; 
    } 
    return 0;
  }
  
  protected static int getImageFormatConst(String paramString) {
    try {
      return ClassUtil.getDeclaredField(ImageFormat.class, paramString).getInt(null);
    } catch (IllegalAccessException illegalAccessException) {
    
    } catch (RuntimeException runtimeException) {}
    throw new RuntimeException("internal error", runtimeException);
  }
  
  public static UvcFrameFormat uvcFromAndroid(int paramInt) {
    for (Format format : formats) {
      if (format.android == paramInt)
        return format.uvc; 
    } 
    return UvcFrameFormat.UNKNOWN;
  }
  
  public static FrameFormat vuforiaWebcamFromAndroid(int paramInt) {
    for (Format format : formats) {
      if (format.android == paramInt)
        return format.vuforiaWebcam; 
    } 
    return FrameFormat.UNKNOWN;
  }
  
  public static FrameFormat vuforiaWebcamFromUvc(UvcFrameFormat paramUvcFrameFormat) {
    for (Format format : formats) {
      if (format.uvc == paramUvcFrameFormat)
        return format.vuforiaWebcam; 
    } 
    return FrameFormat.UNKNOWN;
  }
  
  public static class Format {
    public final int android;
    
    public final String fourCC;
    
    public final UUID guid;
    
    public final String name;
    
    public final UvcFrameFormat uvc;
    
    public final int vuforiaPixelFormat;
    
    public final FrameFormat vuforiaWebcam;
    
    public Format(String param1String1, int param1Int1, UvcFrameFormat param1UvcFrameFormat, int param1Int2, FrameFormat param1FrameFormat, UUID param1UUID, String param1String2) {
      this.name = param1String1;
      this.android = param1Int1;
      this.uvc = param1UvcFrameFormat;
      this.vuforiaPixelFormat = param1Int2;
      this.vuforiaWebcam = param1FrameFormat;
      this.guid = param1UUID;
      this.fourCC = param1String2;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\ImageFormatMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
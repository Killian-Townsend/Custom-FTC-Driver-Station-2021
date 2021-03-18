package org.firstinspires.ftc.robotcore.internal.camera.libuvc.constants;

public enum UvcFrameFormat {
  ANY,
  BGR,
  BY8,
  COMPRESSED,
  GRAY8,
  MJPEG,
  RGB,
  UNCOMPRESSED,
  UNKNOWN(0),
  UYVY(0),
  YUY2(0);
  
  public int value;
  
  static {
    ANY = new UvcFrameFormat("ANY", 1, 0);
    UNCOMPRESSED = new UvcFrameFormat("UNCOMPRESSED", 2, 1);
    COMPRESSED = new UvcFrameFormat("COMPRESSED", 3, 2);
    YUY2 = new UvcFrameFormat("YUY2", 4, 3);
    UYVY = new UvcFrameFormat("UYVY", 5, 4);
    RGB = new UvcFrameFormat("RGB", 6, 5);
    BGR = new UvcFrameFormat("BGR", 7, 6);
    MJPEG = new UvcFrameFormat("MJPEG", 8, 7);
    GRAY8 = new UvcFrameFormat("GRAY8", 9, 8);
    UvcFrameFormat uvcFrameFormat = new UvcFrameFormat("BY8", 10, 9);
    BY8 = uvcFrameFormat;
    $VALUES = new UvcFrameFormat[] { 
        UNKNOWN, ANY, UNCOMPRESSED, COMPRESSED, YUY2, UYVY, RGB, BGR, MJPEG, GRAY8, 
        uvcFrameFormat };
  }
  
  UvcFrameFormat(int paramInt1) {
    this.value = paramInt1;
  }
  
  public static UvcFrameFormat from(int paramInt) {
    for (UvcFrameFormat uvcFrameFormat : values()) {
      if (uvcFrameFormat.value == paramInt)
        return uvcFrameFormat; 
    } 
    return UNKNOWN;
  }
  
  public int getValue() {
    return this.value;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\constants\UvcFrameFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
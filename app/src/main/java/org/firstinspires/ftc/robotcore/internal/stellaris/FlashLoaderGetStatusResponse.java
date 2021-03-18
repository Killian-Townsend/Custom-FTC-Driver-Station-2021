package org.firstinspires.ftc.robotcore.internal.stellaris;

public class FlashLoaderGetStatusResponse extends FlashLoaderDatagram {
  public static final byte STATUS_FLASH_FAIL = 68;
  
  public static final byte STATUS_INVALID_ADDR = 67;
  
  public static final byte STATUS_INVALID_CMD = 66;
  
  public static final byte STATUS_SUCCESS = 64;
  
  public static final byte STATUS_UNKNOWN_CMD = 65;
  
  public FlashLoaderGetStatusResponse() {
    super(1);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\stellaris\FlashLoaderGetStatusResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
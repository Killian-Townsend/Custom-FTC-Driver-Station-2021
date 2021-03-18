package org.firstinspires.ftc.robotcore.internal.stellaris;

public class FlashLoaderProtocolException extends Exception {
  public FlashLoaderProtocolException() {
    this("");
  }
  
  public FlashLoaderProtocolException(String paramString) {
    super(paramString);
  }
  
  public FlashLoaderProtocolException(String paramString, Exception paramException) {
    this(stringBuilder.toString());
  }
  
  public FlashLoaderProtocolException(String paramString, FlashLoaderCommand paramFlashLoaderCommand) {
    this(stringBuilder.toString());
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\stellaris\FlashLoaderProtocolException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
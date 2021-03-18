package org.firstinspires.ftc.robotcore.internal.stellaris;

import com.qualcomm.robotcore.util.Util;

public class FlashLoaderCommand extends FlashLoaderDatagram {
  public FlashLoaderCommand(int paramInt) {
    this(paramInt, new byte[0]);
  }
  
  public FlashLoaderCommand(int paramInt, byte[] paramArrayOfbyte) {
    super(makePayload(paramInt, paramArrayOfbyte));
  }
  
  protected static byte[] makePayload(int paramInt, byte[] paramArrayOfbyte) {
    return Util.concatenateByteArrays(new byte[] { (byte)paramInt }, paramArrayOfbyte);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\stellaris\FlashLoaderCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
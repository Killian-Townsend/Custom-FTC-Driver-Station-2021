package org.firstinspires.ftc.robotcore.internal.stellaris;

import java.util.Arrays;

public class FlashLoaderSendDataCommand extends FlashLoaderCommand {
  public static final int QUANTUM = 8;
  
  public FlashLoaderSendDataCommand(byte[] paramArrayOfbyte) {
    this(paramArrayOfbyte, 0);
  }
  
  public FlashLoaderSendDataCommand(byte[] paramArrayOfbyte, int paramInt) {
    super(36, makePayload(paramArrayOfbyte, paramInt));
  }
  
  protected static byte[] makePayload(byte[] paramArrayOfbyte, int paramInt) {
    return Arrays.copyOfRange(paramArrayOfbyte, paramInt, Math.min(8, paramArrayOfbyte.length - paramInt) + paramInt);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\stellaris\FlashLoaderSendDataCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
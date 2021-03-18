package org.firstinspires.ftc.robotcore.internal.stellaris;

import com.qualcomm.robotcore.util.TypeConversion;
import com.qualcomm.robotcore.util.Util;
import java.nio.ByteOrder;

public class FlashLoaderDownloadCommand extends FlashLoaderCommand {
  public FlashLoaderDownloadCommand(int paramInt1, int paramInt2) {
    super(33, makePayload(paramInt1, paramInt2));
  }
  
  protected static byte[] makePayload(int paramInt1, int paramInt2) {
    ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
    return Util.concatenateByteArrays(TypeConversion.intToByteArray(paramInt1, byteOrder), TypeConversion.intToByteArray(paramInt2, byteOrder));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\stellaris\FlashLoaderDownloadCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
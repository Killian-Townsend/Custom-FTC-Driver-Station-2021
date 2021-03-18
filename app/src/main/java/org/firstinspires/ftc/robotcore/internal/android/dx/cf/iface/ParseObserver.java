package org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface;

import org.firstinspires.ftc.robotcore.internal.android.dx.util.ByteArray;

public interface ParseObserver {
  void changeIndent(int paramInt);
  
  void endParsingMember(ByteArray paramByteArray, int paramInt, String paramString1, String paramString2, Member paramMember);
  
  void parsed(ByteArray paramByteArray, int paramInt1, int paramInt2, String paramString);
  
  void startParsingMember(ByteArray paramByteArray, int paramInt, String paramString1, String paramString2);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\iface\ParseObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
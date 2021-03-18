package org.firstinspires.ftc.robotcore.internal.android.dx.util;

import java.io.PrintWriter;
import java.io.Writer;

public final class Writers {
  public static PrintWriter printWriterFor(Writer paramWriter) {
    return (paramWriter instanceof PrintWriter) ? (PrintWriter)paramWriter : new PrintWriter(paramWriter);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\d\\util\Writers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
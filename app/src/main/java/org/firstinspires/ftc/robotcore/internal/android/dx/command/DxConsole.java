package org.firstinspires.ftc.robotcore.internal.android.dx.command;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class DxConsole {
  public static PrintStream err;
  
  public static final PrintStream noop;
  
  public static PrintStream out = System.out;
  
  static {
    err = System.err;
    noop = new PrintStream(new OutputStream() {
          public void write(int param1Int) throws IOException {}
        });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\command\DxConsole.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
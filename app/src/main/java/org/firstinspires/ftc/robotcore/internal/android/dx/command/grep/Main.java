package org.firstinspires.ftc.robotcore.internal.android.dx.command.grep;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;
import org.firstinspires.ftc.robotcore.internal.android.dex.Dex;

public final class Main {
  public static void main(String[] paramArrayOfString) throws IOException {
    boolean bool = false;
    String str2 = paramArrayOfString[0];
    String str1 = paramArrayOfString[1];
    if ((new Grep(new Dex(new File(str2)), Pattern.compile(str1), new PrintWriter(System.out))).grep() <= 0)
      bool = true; 
    System.exit(bool);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\command\grep\Main.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
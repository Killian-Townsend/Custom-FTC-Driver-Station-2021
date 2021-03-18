package org.firstinspires.ftc.robotcore.internal.android.dx.command.dump;

import java.io.PrintStream;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct.AttributeFactory;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct.DirectClassFile;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct.StdAttributeFactory;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.ByteArray;

public final class ClassDumper extends BaseDumper {
  private ClassDumper(byte[] paramArrayOfbyte, PrintStream paramPrintStream, String paramString, Args paramArgs) {
    super(paramArrayOfbyte, paramPrintStream, paramString, paramArgs);
  }
  
  public static void dump(byte[] paramArrayOfbyte, PrintStream paramPrintStream, String paramString, Args paramArgs) {
    (new ClassDumper(paramArrayOfbyte, paramPrintStream, paramString, paramArgs)).dump();
  }
  
  public void dump() {
    byte[] arrayOfByte = getBytes();
    ByteArray byteArray = new ByteArray(arrayOfByte);
    DirectClassFile directClassFile = new DirectClassFile(byteArray, getFilePath(), getStrictParse());
    directClassFile.setAttributeFactory((AttributeFactory)StdAttributeFactory.THE_ONE);
    directClassFile.setObserver(this);
    directClassFile.getMagic();
    int i = getAt();
    if (i != arrayOfByte.length)
      parsed(byteArray, i, arrayOfByte.length - i, "<extra data at end of file>"); 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\command\dump\ClassDumper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
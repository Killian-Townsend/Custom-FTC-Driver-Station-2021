package org.firstinspires.ftc.robotcore.internal.android.dx.command.dump;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import org.firstinspires.ftc.robotcore.internal.android.dex.util.FileUtils;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.ParseException;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.HexParser;

public class Main {
  static Args parsedArgs = new Args();
  
  public static void main(String[] paramArrayOfString) {
    PrintStream printStream;
    int i;
    for (i = 0; i < paramArrayOfString.length; i++) {
      String str = paramArrayOfString[i];
      if (str.equals("--") || !str.startsWith("--"))
        break; 
      if (str.equals("--bytes")) {
        parsedArgs.rawBytes = true;
      } else if (str.equals("--basic-blocks")) {
        parsedArgs.basicBlocks = true;
      } else if (str.equals("--rop-blocks")) {
        parsedArgs.ropBlocks = true;
      } else if (str.equals("--optimize")) {
        parsedArgs.optimize = true;
      } else if (str.equals("--ssa-blocks")) {
        parsedArgs.ssaBlocks = true;
      } else if (str.startsWith("--ssa-step=")) {
        parsedArgs.ssaStep = str.substring(str.indexOf('=') + 1);
      } else if (str.equals("--debug")) {
        parsedArgs.debug = true;
      } else if (str.equals("--dot")) {
        parsedArgs.dotDump = true;
      } else if (str.equals("--strict")) {
        parsedArgs.strictParse = true;
      } else if (str.startsWith("--width=")) {
        str = str.substring(str.indexOf('=') + 1);
        parsedArgs.width = Integer.parseInt(str);
      } else if (str.startsWith("--method=")) {
        str = str.substring(str.indexOf('=') + 1);
        parsedArgs.method = str;
      } else {
        printStream = System.err;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("unknown option: ");
        stringBuilder.append(str);
        printStream.println(stringBuilder.toString());
        throw new RuntimeException("usage");
      } 
    } 
    if (i != printStream.length) {
      while (i < printStream.length) {
        PrintStream printStream1 = printStream[i];
        try {
          PrintStream printStream2 = System.out;
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("reading ");
          stringBuilder.append((String)printStream1);
          stringBuilder.append("...");
          printStream2.println(stringBuilder.toString());
          byte[] arrayOfByte2 = FileUtils.readFile((String)printStream1);
          boolean bool = printStream1.endsWith(".class");
          byte[] arrayOfByte1 = arrayOfByte2;
          if (!bool)
            try {
              String str = new String(arrayOfByte2, "utf-8");
              byte[] arrayOfByte = HexParser.parse(str);
            } catch (UnsupportedEncodingException unsupportedEncodingException) {
              throw new RuntimeException("shouldn't happen", unsupportedEncodingException);
            }  
          processOne((String)printStream1, (byte[])unsupportedEncodingException);
        } catch (ParseException parseException) {
          System.err.println("\ntrouble parsing:");
          if (parsedArgs.debug) {
            parseException.printStackTrace();
          } else {
            parseException.printContext(System.err);
          } 
        } 
        i++;
      } 
      return;
    } 
    System.err.println("no input files specified");
    throw new RuntimeException("usage");
  }
  
  private static void processOne(String paramString, byte[] paramArrayOfbyte) {
    if (parsedArgs.dotDump) {
      DotDumper.dump(paramArrayOfbyte, paramString, parsedArgs);
      return;
    } 
    if (parsedArgs.basicBlocks) {
      BlockDumper.dump(paramArrayOfbyte, System.out, paramString, false, parsedArgs);
      return;
    } 
    if (parsedArgs.ropBlocks) {
      BlockDumper.dump(paramArrayOfbyte, System.out, paramString, true, parsedArgs);
      return;
    } 
    if (parsedArgs.ssaBlocks) {
      parsedArgs.optimize = false;
      SsaDumper.dump(paramArrayOfbyte, System.out, paramString, parsedArgs);
      return;
    } 
    ClassDumper.dump(paramArrayOfbyte, System.out, paramString, parsedArgs);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\command\dump\Main.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
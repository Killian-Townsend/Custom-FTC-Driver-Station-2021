package org.firstinspires.ftc.robotcore.internal.android.dx.command;

public class Main {
  private static String USAGE_MESSAGE = "usage:\n  dx --dex [--debug] [--verbose] [--positions=<style>] [--no-locals]\n  [--no-optimize] [--statistics] [--[no-]optimize-list=<file>] [--no-strict]\n  [--keep-classes] [--output=<file>] [--dump-to=<file>] [--dump-width=<n>]\n  [--dump-method=<name>[*]] [--verbose-dump] [--no-files] [--core-library]\n  [--num-threads=<n>] [--incremental] [--force-jumbo] [--no-warning]\n  [--multi-dex [--main-dex-list=<file> [--minimal-main-dex]]\n  [--input-list=<file>]\n  [<file>.class | <file>.{zip,jar,apk} | <directory>] ...\n    Convert a set of classfiles into a dex file, optionally embedded in a\n    jar/zip. Output name must end with one of: .dex .jar .zip .apk or be a directory.\n    Positions options: none, important, lines.\n    --multi-dex: allows to generate several dex files if needed. This option is \n    exclusive with --incremental, causes --num-threads to be ignored and only\n    supports folder or archive output.\n    --main-dex-list=<file>: <file> is a list of class file names, classes defined by\n    those class files are put in classes.dex.\n    --minimal-main-dex: only classes selected by --main-dex-list are to be put in\n    the main dex.\n    --input-list: <file> is a list of inputs.\n    Each line in <file> must end with one of: .class .jar .zip .apk or be a directory.\n  dx --annotool --annotation=<class> [--element=<element types>]\n  [--print=<print types>]\n  dx --dump [--debug] [--strict] [--bytes] [--optimize]\n  [--basic-blocks | --rop-blocks | --ssa-blocks | --dot] [--ssa-step=<step>]\n  [--width=<n>] [<file>.class | <file>.txt] ...\n    Dump classfiles, or transformations thereof, in a human-oriented format.\n  dx --find-usages <file.dex> <declaring type> <member>\n    Find references and declarations to a field or method.\n    declaring type: a class name in internal form, like Ljava/lang/Object;\n    member: a field or method name, like hashCode\n  dx -J<option> ... <arguments, in one of the above forms>\n    Pass VM-specific options to the virtual machine that runs dx.\n  dx --version\n    Print the version of this tool (1.12).\n  dx --help\n    Print this message.";
  
  public static void main(String[] paramArrayOfString) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_3
    //   2: iconst_0
    //   3: istore #4
    //   5: iconst_0
    //   6: istore_1
    //   7: iload_1
    //   8: aload_0
    //   9: arraylength
    //   10: if_icmpge -> 196
    //   13: aload_0
    //   14: iload_1
    //   15: aaload
    //   16: astore #6
    //   18: iload #4
    //   20: istore_2
    //   21: aload #6
    //   23: ldc '--'
    //   25: invokevirtual equals : (Ljava/lang/Object;)Z
    //   28: ifne -> 275
    //   31: aload #6
    //   33: ldc '--'
    //   35: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   38: istore #5
    //   40: iload #5
    //   42: ifne -> 51
    //   45: iload #4
    //   47: istore_2
    //   48: goto -> 275
    //   51: aload #6
    //   53: ldc '--dex'
    //   55: invokevirtual equals : (Ljava/lang/Object;)Z
    //   58: ifeq -> 72
    //   61: aload_0
    //   62: iload_1
    //   63: invokestatic without : ([Ljava/lang/String;I)[Ljava/lang/String;
    //   66: invokestatic main : ([Ljava/lang/String;)V
    //   69: goto -> 314
    //   72: aload #6
    //   74: ldc '--dump'
    //   76: invokevirtual equals : (Ljava/lang/Object;)Z
    //   79: ifeq -> 93
    //   82: aload_0
    //   83: iload_1
    //   84: invokestatic without : ([Ljava/lang/String;I)[Ljava/lang/String;
    //   87: invokestatic main : ([Ljava/lang/String;)V
    //   90: goto -> 314
    //   93: aload #6
    //   95: ldc '--annotool'
    //   97: invokevirtual equals : (Ljava/lang/Object;)Z
    //   100: ifeq -> 114
    //   103: aload_0
    //   104: iload_1
    //   105: invokestatic without : ([Ljava/lang/String;I)[Ljava/lang/String;
    //   108: invokestatic main : ([Ljava/lang/String;)V
    //   111: goto -> 314
    //   114: aload #6
    //   116: ldc '--find-usages'
    //   118: invokevirtual equals : (Ljava/lang/Object;)Z
    //   121: ifeq -> 135
    //   124: aload_0
    //   125: iload_1
    //   126: invokestatic without : ([Ljava/lang/String;I)[Ljava/lang/String;
    //   129: invokestatic main : ([Ljava/lang/String;)V
    //   132: goto -> 314
    //   135: aload #6
    //   137: ldc '--version'
    //   139: invokevirtual equals : (Ljava/lang/Object;)Z
    //   142: ifeq -> 151
    //   145: invokestatic version : ()V
    //   148: goto -> 314
    //   151: aload #6
    //   153: ldc '--help'
    //   155: invokevirtual equals : (Ljava/lang/Object;)Z
    //   158: istore #5
    //   160: iload #5
    //   162: ifeq -> 172
    //   165: iconst_1
    //   166: istore_1
    //   167: iload_1
    //   168: istore_2
    //   169: goto -> 277
    //   172: iload_1
    //   173: iconst_1
    //   174: iadd
    //   175: istore_1
    //   176: goto -> 7
    //   179: astore_0
    //   180: iconst_1
    //   181: istore_1
    //   182: goto -> 206
    //   185: astore_0
    //   186: iconst_1
    //   187: istore_1
    //   188: goto -> 250
    //   191: iconst_1
    //   192: istore_2
    //   193: goto -> 275
    //   196: iconst_0
    //   197: istore_1
    //   198: iload_3
    //   199: istore_2
    //   200: goto -> 277
    //   203: astore_0
    //   204: iconst_0
    //   205: istore_1
    //   206: getstatic java/lang/System.err : Ljava/io/PrintStream;
    //   209: ldc '\\nUNEXPECTED TOP-LEVEL ERROR:'
    //   211: invokevirtual println : (Ljava/lang/String;)V
    //   214: aload_0
    //   215: invokevirtual printStackTrace : ()V
    //   218: aload_0
    //   219: instanceof java/lang/NoClassDefFoundError
    //   222: ifne -> 232
    //   225: aload_0
    //   226: instanceof java/lang/NoSuchMethodError
    //   229: ifeq -> 240
    //   232: getstatic java/lang/System.err : Ljava/io/PrintStream;
    //   235: ldc 'Note: You may be using an incompatible virtual machine or class library.\\n(This program is known to be incompatible with recent releases of GCJ.)'
    //   237: invokevirtual println : (Ljava/lang/String;)V
    //   240: iconst_3
    //   241: invokestatic exit : (I)V
    //   244: goto -> 266
    //   247: astore_0
    //   248: iconst_0
    //   249: istore_1
    //   250: getstatic java/lang/System.err : Ljava/io/PrintStream;
    //   253: ldc '\\nUNEXPECTED TOP-LEVEL EXCEPTION:'
    //   255: invokevirtual println : (Ljava/lang/String;)V
    //   258: aload_0
    //   259: invokevirtual printStackTrace : ()V
    //   262: iconst_2
    //   263: invokestatic exit : (I)V
    //   266: iconst_0
    //   267: istore_3
    //   268: iload_1
    //   269: istore_2
    //   270: iload_3
    //   271: istore_1
    //   272: goto -> 277
    //   275: iconst_1
    //   276: istore_1
    //   277: iload_2
    //   278: ifne -> 291
    //   281: getstatic java/lang/System.err : Ljava/io/PrintStream;
    //   284: ldc 'error: no command specified'
    //   286: invokevirtual println : (Ljava/lang/String;)V
    //   289: iconst_1
    //   290: istore_1
    //   291: iload_1
    //   292: ifeq -> 302
    //   295: invokestatic usage : ()V
    //   298: iconst_1
    //   299: invokestatic exit : (I)V
    //   302: return
    //   303: astore_0
    //   304: iload #4
    //   306: istore_2
    //   307: goto -> 275
    //   310: astore_0
    //   311: goto -> 191
    //   314: iconst_0
    //   315: istore_1
    //   316: iconst_1
    //   317: istore_2
    //   318: goto -> 277
    // Exception table:
    //   from	to	target	type
    //   7	13	303	org/firstinspires/ftc/robotcore/internal/android/dx/command/UsageException
    //   7	13	247	java/lang/RuntimeException
    //   7	13	203	finally
    //   21	40	303	org/firstinspires/ftc/robotcore/internal/android/dx/command/UsageException
    //   21	40	247	java/lang/RuntimeException
    //   21	40	203	finally
    //   51	69	310	org/firstinspires/ftc/robotcore/internal/android/dx/command/UsageException
    //   51	69	185	java/lang/RuntimeException
    //   51	69	179	finally
    //   72	90	310	org/firstinspires/ftc/robotcore/internal/android/dx/command/UsageException
    //   72	90	185	java/lang/RuntimeException
    //   72	90	179	finally
    //   93	111	310	org/firstinspires/ftc/robotcore/internal/android/dx/command/UsageException
    //   93	111	185	java/lang/RuntimeException
    //   93	111	179	finally
    //   114	132	310	org/firstinspires/ftc/robotcore/internal/android/dx/command/UsageException
    //   114	132	185	java/lang/RuntimeException
    //   114	132	179	finally
    //   135	148	310	org/firstinspires/ftc/robotcore/internal/android/dx/command/UsageException
    //   135	148	185	java/lang/RuntimeException
    //   135	148	179	finally
    //   151	160	310	org/firstinspires/ftc/robotcore/internal/android/dx/command/UsageException
    //   151	160	185	java/lang/RuntimeException
    //   151	160	179	finally
  }
  
  private static void usage() {
    System.err.println(USAGE_MESSAGE);
  }
  
  private static void version() {
    System.err.println("dx version 1.12");
    System.exit(0);
  }
  
  private static String[] without(String[] paramArrayOfString, int paramInt) {
    int i = paramArrayOfString.length - 1;
    String[] arrayOfString = new String[i];
    System.arraycopy(paramArrayOfString, 0, arrayOfString, 0, paramInt);
    System.arraycopy(paramArrayOfString, paramInt + 1, arrayOfString, paramInt, i - paramInt);
    return arrayOfString;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\command\Main.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.android.dx.command.annotool;

import java.lang.annotation.ElementType;
import java.util.EnumSet;

public class Main {
  public static void main(String[] paramArrayOfString) {
    Arguments arguments = new Arguments();
    try {
      arguments.parse(paramArrayOfString);
      (new AnnotationLister(arguments)).process();
      return;
    } catch (InvalidArgumentException invalidArgumentException) {
      System.err.println(invalidArgumentException.getMessage());
      throw new RuntimeException("usage");
    } 
  }
  
  static class Arguments {
    String aclass;
    
    EnumSet<ElementType> eTypes = EnumSet.noneOf(ElementType.class);
    
    String[] files;
    
    EnumSet<Main.PrintType> printTypes = EnumSet.noneOf(Main.PrintType.class);
    
    void parse(String[] param1ArrayOfString) throws Main.InvalidArgumentException {
      // Byte code:
      //   0: iconst_0
      //   1: istore_2
      //   2: iload_2
      //   3: aload_1
      //   4: arraylength
      //   5: if_icmpge -> 285
      //   8: aload_1
      //   9: iload_2
      //   10: aaload
      //   11: astore #5
      //   13: aload #5
      //   15: ldc '--annotation='
      //   17: invokevirtual startsWith : (Ljava/lang/String;)Z
      //   20: ifeq -> 72
      //   23: aload #5
      //   25: aload #5
      //   27: bipush #61
      //   29: invokevirtual indexOf : (I)I
      //   32: iconst_1
      //   33: iadd
      //   34: invokevirtual substring : (I)Ljava/lang/String;
      //   37: astore #5
      //   39: aload_0
      //   40: getfield aclass : Ljava/lang/String;
      //   43: ifnonnull -> 62
      //   46: aload_0
      //   47: aload #5
      //   49: bipush #46
      //   51: bipush #47
      //   53: invokevirtual replace : (CC)Ljava/lang/String;
      //   56: putfield aclass : Ljava/lang/String;
      //   59: goto -> 242
      //   62: new org/firstinspires/ftc/robotcore/internal/android/dx/command/annotool/Main$InvalidArgumentException
      //   65: dup
      //   66: ldc '--annotation can only be specified once.'
      //   68: invokespecial <init> : (Ljava/lang/String;)V
      //   71: athrow
      //   72: aload #5
      //   74: ldc '--element='
      //   76: invokevirtual startsWith : (Ljava/lang/String;)Z
      //   79: ifeq -> 162
      //   82: aload #5
      //   84: aload #5
      //   86: bipush #61
      //   88: invokevirtual indexOf : (I)I
      //   91: iconst_1
      //   92: iadd
      //   93: invokevirtual substring : (I)Ljava/lang/String;
      //   96: astore #5
      //   98: aload #5
      //   100: ldc ','
      //   102: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
      //   105: astore #5
      //   107: aload #5
      //   109: arraylength
      //   110: istore #4
      //   112: iconst_0
      //   113: istore_3
      //   114: iload_3
      //   115: iload #4
      //   117: if_icmpge -> 242
      //   120: aload #5
      //   122: iload_3
      //   123: aaload
      //   124: astore #6
      //   126: aload_0
      //   127: getfield eTypes : Ljava/util/EnumSet;
      //   130: aload #6
      //   132: getstatic java/util/Locale.ROOT : Ljava/util/Locale;
      //   135: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
      //   138: invokestatic valueOf : (Ljava/lang/String;)Ljava/lang/annotation/ElementType;
      //   141: invokevirtual add : (Ljava/lang/Object;)Z
      //   144: pop
      //   145: iload_3
      //   146: iconst_1
      //   147: iadd
      //   148: istore_3
      //   149: goto -> 114
      //   152: new org/firstinspires/ftc/robotcore/internal/android/dx/command/annotool/Main$InvalidArgumentException
      //   155: dup
      //   156: ldc 'invalid --element'
      //   158: invokespecial <init> : (Ljava/lang/String;)V
      //   161: athrow
      //   162: aload #5
      //   164: ldc '--print='
      //   166: invokevirtual startsWith : (Ljava/lang/String;)Z
      //   169: ifeq -> 259
      //   172: aload #5
      //   174: aload #5
      //   176: bipush #61
      //   178: invokevirtual indexOf : (I)I
      //   181: iconst_1
      //   182: iadd
      //   183: invokevirtual substring : (I)Ljava/lang/String;
      //   186: astore #5
      //   188: aload #5
      //   190: ldc ','
      //   192: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
      //   195: astore #5
      //   197: aload #5
      //   199: arraylength
      //   200: istore #4
      //   202: iconst_0
      //   203: istore_3
      //   204: iload_3
      //   205: iload #4
      //   207: if_icmpge -> 242
      //   210: aload #5
      //   212: iload_3
      //   213: aaload
      //   214: astore #6
      //   216: aload_0
      //   217: getfield printTypes : Ljava/util/EnumSet;
      //   220: aload #6
      //   222: getstatic java/util/Locale.ROOT : Ljava/util/Locale;
      //   225: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
      //   228: invokestatic valueOf : (Ljava/lang/String;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/command/annotool/Main$PrintType;
      //   231: invokevirtual add : (Ljava/lang/Object;)Z
      //   234: pop
      //   235: iload_3
      //   236: iconst_1
      //   237: iadd
      //   238: istore_3
      //   239: goto -> 204
      //   242: iload_2
      //   243: iconst_1
      //   244: iadd
      //   245: istore_2
      //   246: goto -> 2
      //   249: new org/firstinspires/ftc/robotcore/internal/android/dx/command/annotool/Main$InvalidArgumentException
      //   252: dup
      //   253: ldc 'invalid --print'
      //   255: invokespecial <init> : (Ljava/lang/String;)V
      //   258: athrow
      //   259: aload_1
      //   260: arraylength
      //   261: iload_2
      //   262: isub
      //   263: anewarray java/lang/String
      //   266: astore #5
      //   268: aload_0
      //   269: aload #5
      //   271: putfield files : [Ljava/lang/String;
      //   274: aload_1
      //   275: iload_2
      //   276: aload #5
      //   278: iconst_0
      //   279: aload #5
      //   281: arraylength
      //   282: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
      //   285: aload_0
      //   286: getfield aclass : Ljava/lang/String;
      //   289: ifnull -> 376
      //   292: aload_0
      //   293: getfield printTypes : Ljava/util/EnumSet;
      //   296: invokevirtual isEmpty : ()Z
      //   299: ifeq -> 313
      //   302: aload_0
      //   303: getfield printTypes : Ljava/util/EnumSet;
      //   306: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/annotool/Main$PrintType.CLASS : Lorg/firstinspires/ftc/robotcore/internal/android/dx/command/annotool/Main$PrintType;
      //   309: invokevirtual add : (Ljava/lang/Object;)Z
      //   312: pop
      //   313: aload_0
      //   314: getfield eTypes : Ljava/util/EnumSet;
      //   317: invokevirtual isEmpty : ()Z
      //   320: ifeq -> 334
      //   323: aload_0
      //   324: getfield eTypes : Ljava/util/EnumSet;
      //   327: getstatic java/lang/annotation/ElementType.TYPE : Ljava/lang/annotation/ElementType;
      //   330: invokevirtual add : (Ljava/lang/Object;)Z
      //   333: pop
      //   334: aload_0
      //   335: getfield eTypes : Ljava/util/EnumSet;
      //   338: invokevirtual clone : ()Ljava/util/EnumSet;
      //   341: astore_1
      //   342: aload_1
      //   343: getstatic java/lang/annotation/ElementType.TYPE : Ljava/lang/annotation/ElementType;
      //   346: invokevirtual remove : (Ljava/lang/Object;)Z
      //   349: pop
      //   350: aload_1
      //   351: getstatic java/lang/annotation/ElementType.PACKAGE : Ljava/lang/annotation/ElementType;
      //   354: invokevirtual remove : (Ljava/lang/Object;)Z
      //   357: pop
      //   358: aload_1
      //   359: invokevirtual isEmpty : ()Z
      //   362: ifeq -> 366
      //   365: return
      //   366: new org/firstinspires/ftc/robotcore/internal/android/dx/command/annotool/Main$InvalidArgumentException
      //   369: dup
      //   370: ldc 'only --element parameters 'type' and 'package' supported'
      //   372: invokespecial <init> : (Ljava/lang/String;)V
      //   375: athrow
      //   376: new org/firstinspires/ftc/robotcore/internal/android/dx/command/annotool/Main$InvalidArgumentException
      //   379: dup
      //   380: ldc '--annotation must be specified'
      //   382: invokespecial <init> : (Ljava/lang/String;)V
      //   385: athrow
      //   386: astore_1
      //   387: goto -> 152
      //   390: astore_1
      //   391: goto -> 249
      // Exception table:
      //   from	to	target	type
      //   98	112	386	java/lang/IllegalArgumentException
      //   126	145	386	java/lang/IllegalArgumentException
      //   188	202	390	java/lang/IllegalArgumentException
      //   216	235	390	java/lang/IllegalArgumentException
    }
  }
  
  private static class InvalidArgumentException extends Exception {
    InvalidArgumentException() {}
    
    InvalidArgumentException(String param1String) {
      super(param1String);
    }
  }
  
  enum PrintType {
    CLASS, INNERCLASS, METHOD, PACKAGE;
    
    static {
      PrintType printType = new PrintType("PACKAGE", 3);
      PACKAGE = printType;
      $VALUES = new PrintType[] { CLASS, INNERCLASS, METHOD, printType };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\command\annotool\Main.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.android.dx.command.dexer;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import org.firstinspires.ftc.robotcore.internal.android.dex.Dex;
import org.firstinspires.ftc.robotcore.internal.android.dex.DexException;
import org.firstinspires.ftc.robotcore.internal.android.dex.util.FileUtils;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.code.SimException;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct.AttributeFactory;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct.ClassPathOpener;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct.DirectClassFile;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct.StdAttributeFactory;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.ParseException;
import org.firstinspires.ftc.robotcore.internal.android.dx.command.DxConsole;
import org.firstinspires.ftc.robotcore.internal.android.dx.command.UsageException;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.DexOptions;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.cf.CfOptions;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.cf.CfTranslator;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.file.ClassDefItem;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.file.DexFile;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.file.EncodedMethod;
import org.firstinspires.ftc.robotcore.internal.android.dx.merge.CollisionPolicy;
import org.firstinspires.ftc.robotcore.internal.android.dx.merge.DexMerger;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.annotation.Annotation;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.annotation.Annotations;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.annotation.AnnotationsList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;

public class Main {
  private static final Attributes.Name CREATED_BY = new Attributes.Name("Created-By");
  
  private static final String DEX_EXTENSION = ".dex";
  
  private static final String DEX_PREFIX = "classes";
  
  private static final String IN_RE_CORE_CLASSES = "Ill-advised or mistaken usage of a core class (java.* or javax.*)\nwhen not building a core library.\n\nThis is often due to inadvertently including a core library file\nin your application's project, when using an IDE (such as\nEclipse). If you are sure you're not intentionally defining a\ncore class, then this is the most likely explanation of what's\ngoing on.\n\nHowever, you might actually be trying to define a class in a core\nnamespace, the source of which you may have taken, for example,\nfrom a non-Android virtual machine project. This will most\nassuredly not work. At a minimum, it jeopardizes the\ncompatibility of your app with future versions of the platform.\nIt is also often of questionable legality.\n\nIf you really intend to build a core library -- which is only\nappropriate as part of creating a full virtual machine\ndistribution, as opposed to compiling an application -- then use\nthe \"--core-library\" option to suppress this error message.\n\nIf you go ahead and use \"--core-library\" but are in fact\nbuilding an application, then be forewarned that your application\nwill still fail to build or run, at some point. Please be\nprepared for angry customers who find, for example, that your\napplication ceases to function once they upgrade their operating\nsystem. You will be to blame for this problem.\n\nIf you are legitimately using some code that happens to be in a\ncore package, then the easiest safe alternative you have is to\nrepackage that code. That is, move the classes in question into\nyour own package namespace. This means that they will never be in\nconflict with core system classes. JarJar is a tool that may help\nyou in this endeavor. If you find that you cannot do this, then\nthat is an indication that the path you are on will ultimately\nlead to pain, suffering, grief, and lamentation.\n";
  
  private static final String[] JAVAX_CORE = new String[] { 
      "accessibility", "crypto", "imageio", "management", "naming", "net", "print", "rmi", "security", "sip", 
      "sound", "sql", "swing", "transaction", "xml" };
  
  private static final String MANIFEST_NAME = "META-INF/MANIFEST.MF";
  
  private static final int MAX_FIELD_ADDED_DURING_DEX_CREATION = 9;
  
  private static final int MAX_METHOD_ADDED_DURING_DEX_CREATION = 2;
  
  private static List<Future<Boolean>> addToDexFutures;
  
  private static volatile boolean anyFilesProcessed;
  
  private static Arguments args;
  
  private static ExecutorService classDefItemConsumer;
  
  private static ExecutorService classTranslatorPool;
  
  private static Set<String> classesInMainDex;
  
  private static ExecutorService dexOutPool;
  
  private static List<byte[]> dexOutputArrays;
  
  private static List<Future<byte[]>> dexOutputFutures;
  
  private static Object dexRotationLock;
  
  private static AtomicInteger errors = new AtomicInteger(0);
  
  private static OutputStreamWriter humanOutWriter;
  
  private static final List<byte[]> libraryDexBuffers = (List)new ArrayList<byte>();
  
  private static int maxFieldIdsInProcess;
  
  private static int maxMethodIdsInProcess;
  
  private static long minimumFileAge;
  
  private static DexFile outputDex;
  
  private static TreeMap<String, byte[]> outputResources;
  
  static {
    addToDexFutures = new ArrayList<Future<Boolean>>();
    dexOutputFutures = new ArrayList<Future<byte[]>>();
    dexRotationLock = new Object();
    maxMethodIdsInProcess = 0;
    maxFieldIdsInProcess = 0;
    minimumFileAge = 0L;
    classesInMainDex = null;
    dexOutputArrays = (List)new ArrayList<byte>();
    humanOutWriter = null;
  }
  
  private static boolean addClassToDex(ClassDefItem paramClassDefItem) {
    synchronized (outputDex) {
      outputDex.add(paramClassDefItem);
      return true;
    } 
  }
  
  private static void checkClassName(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: ldc_w 'java/'
    //   4: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   7: istore_3
    //   8: iconst_0
    //   9: istore_2
    //   10: iload_3
    //   11: ifeq -> 19
    //   14: iconst_1
    //   15: istore_1
    //   16: goto -> 73
    //   19: iload_2
    //   20: istore_1
    //   21: aload_0
    //   22: ldc_w 'javax/'
    //   25: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   28: ifeq -> 73
    //   31: aload_0
    //   32: bipush #47
    //   34: bipush #6
    //   36: invokevirtual indexOf : (II)I
    //   39: istore_1
    //   40: iload_1
    //   41: iconst_m1
    //   42: if_icmpne -> 48
    //   45: goto -> 14
    //   48: aload_0
    //   49: bipush #6
    //   51: iload_1
    //   52: invokevirtual substring : (II)Ljava/lang/String;
    //   55: astore #4
    //   57: iload_2
    //   58: istore_1
    //   59: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.JAVAX_CORE : [Ljava/lang/String;
    //   62: aload #4
    //   64: invokestatic binarySearch : ([Ljava/lang/Object;Ljava/lang/Object;)I
    //   67: iflt -> 73
    //   70: goto -> 14
    //   73: iload_1
    //   74: ifne -> 78
    //   77: return
    //   78: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/DxConsole.err : Ljava/io/PrintStream;
    //   81: astore #4
    //   83: new java/lang/StringBuilder
    //   86: dup
    //   87: invokespecial <init> : ()V
    //   90: astore #5
    //   92: aload #5
    //   94: ldc_w '\\ntrouble processing "'
    //   97: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   100: pop
    //   101: aload #5
    //   103: aload_0
    //   104: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   107: pop
    //   108: aload #5
    //   110: ldc_w '":\\n\\n'
    //   113: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   116: pop
    //   117: aload #5
    //   119: ldc 'Ill-advised or mistaken usage of a core class (java.* or javax.*)\\nwhen not building a core library.\\n\\nThis is often due to inadvertently including a core library file\\nin your application's project, when using an IDE (such as\\nEclipse). If you are sure you're not intentionally defining a\\ncore class, then this is the most likely explanation of what's\\ngoing on.\\n\\nHowever, you might actually be trying to define a class in a core\\nnamespace, the source of which you may have taken, for example,\\nfrom a non-Android virtual machine project. This will most\\nassuredly not work. At a minimum, it jeopardizes the\\ncompatibility of your app with future versions of the platform.\\nIt is also often of questionable legality.\\n\\nIf you really intend to build a core library -- which is only\\nappropriate as part of creating a full virtual machine\\ndistribution, as opposed to compiling an application -- then use\\nthe "--core-library" option to suppress this error message.\\n\\nIf you go ahead and use "--core-library" but are in fact\\nbuilding an application, then be forewarned that your application\\nwill still fail to build or run, at some point. Please be\\nprepared for angry customers who find, for example, that your\\napplication ceases to function once they upgrade their operating\\nsystem. You will be to blame for this problem.\\n\\nIf you are legitimately using some code that happens to be in a\\ncore package, then the easiest safe alternative you have is to\\nrepackage that code. That is, move the classes in question into\\nyour own package namespace. This means that they will never be in\\nconflict with core system classes. JarJar is a tool that may help\\nyou in this endeavor. If you find that you cannot do this, then\\nthat is an indication that the path you are on will ultimately\\nlead to pain, suffering, grief, and lamentation.\\n'
    //   121: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   124: pop
    //   125: aload #4
    //   127: aload #5
    //   129: invokevirtual toString : ()Ljava/lang/String;
    //   132: invokevirtual println : (Ljava/lang/String;)V
    //   135: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.errors : Ljava/util/concurrent/atomic/AtomicInteger;
    //   138: invokevirtual incrementAndGet : ()I
    //   141: pop
    //   142: new org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$StopProcessing
    //   145: dup
    //   146: aconst_null
    //   147: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$1;)V
    //   150: athrow
  }
  
  private static void closeOutput(OutputStream paramOutputStream) throws IOException {
    if (paramOutputStream == null)
      return; 
    paramOutputStream.flush();
    if (paramOutputStream != System.out)
      paramOutputStream.close(); 
  }
  
  private static void createDexFile() {
    outputDex = new DexFile(args.dexOptions);
    if (args.dumpWidth != 0)
      outputDex.setDumpWidth(args.dumpWidth); 
  }
  
  private static boolean createJar(String paramString) {
    try {
      Manifest manifest = makeManifest();
      OutputStream outputStream = openOutput(paramString);
      JarOutputStream jarOutputStream = new JarOutputStream(outputStream, manifest);
      try {
        for (Map.Entry<String, byte> entry : outputResources.entrySet()) {
          String str = (String)entry.getKey();
          byte[] arrayOfByte = (byte[])entry.getValue();
          JarEntry jarEntry = new JarEntry(str);
          int i = arrayOfByte.length;
          if (args.verbose) {
            PrintStream printStream = DxConsole.out;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("writing ");
            stringBuilder.append(str);
            stringBuilder.append("; size ");
            stringBuilder.append(i);
            stringBuilder.append("...");
            printStream.println(stringBuilder.toString());
          } 
          jarEntry.setSize(i);
          jarOutputStream.putNextEntry(jarEntry);
          jarOutputStream.write(arrayOfByte);
          jarOutputStream.closeEntry();
        } 
        return true;
      } finally {
        jarOutputStream.finish();
        jarOutputStream.flush();
        closeOutput(outputStream);
      } 
    } catch (Exception exception) {
      if (args.debug) {
        DxConsole.err.println("\ntrouble writing output:");
        exception.printStackTrace(DxConsole.err);
      } else {
        PrintStream printStream = DxConsole.err;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\ntrouble writing output: ");
        stringBuilder.append(exception.getMessage());
        printStream.println(stringBuilder.toString());
      } 
      return false;
    } 
  }
  
  private static void dumpMethod(DexFile paramDexFile, String paramString, OutputStreamWriter paramOutputStreamWriter) {
    PrintStream printStream;
    StringBuilder stringBuilder1;
    StringBuilder stringBuilder2;
    boolean bool = paramString.endsWith("*");
    int i = paramString.lastIndexOf('.');
    if (i <= 0 || i == paramString.length() - 1) {
      printStream = DxConsole.err;
      stringBuilder2 = new StringBuilder();
      stringBuilder2.append("bogus fully-qualified method name: ");
      stringBuilder2.append(paramString);
      printStream.println(stringBuilder2.toString());
      return;
    } 
    String str3 = paramString.substring(0, i).replace('.', '/');
    String str2 = paramString.substring(i + 1);
    ClassDefItem classDefItem = printStream.getClassOrNull(str3);
    if (classDefItem == null) {
      printStream = DxConsole.err;
      stringBuilder1 = new StringBuilder();
      stringBuilder1.append("no such class: ");
      stringBuilder1.append(str3);
      printStream.println(stringBuilder1.toString());
      return;
    } 
    String str1 = str2;
    if (bool)
      str1 = str2.substring(0, str2.length() - 1); 
    ArrayList arrayList = classDefItem.getMethods();
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    for (EncodedMethod encodedMethod : arrayList) {
      String str = encodedMethod.getName().getString();
      if ((bool && str.startsWith(str1)) || (!bool && str.equals(str1)))
        treeMap.put(encodedMethod.getRef().getNat(), encodedMethod); 
    } 
    if (treeMap.size() == 0) {
      PrintStream printStream1 = DxConsole.err;
      stringBuilder2 = new StringBuilder();
      stringBuilder2.append("no such method: ");
      stringBuilder2.append((String)stringBuilder1);
      printStream1.println(stringBuilder2.toString());
      return;
    } 
    PrintWriter printWriter = new PrintWriter((Writer)stringBuilder2);
    for (EncodedMethod encodedMethod : treeMap.values()) {
      encodedMethod.debugPrint(printWriter, args.verboseDump);
      CstString cstString = classDefItem.getSourceFile();
      if (cstString != null) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("  source file: ");
        stringBuilder.append(cstString.toQuoted());
        printWriter.println(stringBuilder.toString());
      } 
      Annotations annotations = classDefItem.getMethodAnnotations(encodedMethod.getRef());
      AnnotationsList annotationsList = classDefItem.getParameterAnnotations(encodedMethod.getRef());
      if (annotations != null) {
        printWriter.println("  method annotations:");
        for (Annotation annotation : annotations.getAnnotations()) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("    ");
          stringBuilder.append(annotation);
          printWriter.println(stringBuilder.toString());
        } 
      } 
      if (annotationsList != null) {
        printWriter.println("  parameter annotations:");
        int j = annotationsList.size();
        for (i = 0; i < j; i++) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("    parameter ");
          stringBuilder.append(i);
          printWriter.println(stringBuilder.toString());
          for (Annotation annotation : annotationsList.get(i).getAnnotations()) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("      ");
            stringBuilder3.append(annotation);
            printWriter.println(stringBuilder3.toString());
          } 
        } 
      } 
    } 
    printWriter.flush();
  }
  
  private static String fixPath(String paramString) {
    String str = paramString;
    if (File.separatorChar == '\\')
      str = paramString.replace('\\', '/'); 
    int i = str.lastIndexOf("/./");
    if (i != -1)
      return str.substring(i + 3); 
    paramString = str;
    if (str.startsWith("./"))
      paramString = str.substring(2); 
    return paramString;
  }
  
  private static String getDexFileName(int paramInt) {
    if (paramInt == 0)
      return "classes.dex"; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("classes");
    stringBuilder.append(paramInt + 1);
    stringBuilder.append(".dex");
    return stringBuilder.toString();
  }
  
  public static String getTooManyIdsErrorMessage() {
    return args.multiDex ? "The list of classes given in --main-dex-list is too big and does not fit in the main dex." : "You may try using --multi-dex option.";
  }
  
  public static void main(String[] paramArrayOfString) throws IOException {
    Arguments arguments = new Arguments();
    arguments.parse(paramArrayOfString);
    int i = run(arguments);
    if (i != 0)
      System.exit(i); 
  }
  
  private static Manifest makeManifest() throws IOException {
    Manifest manifest;
    Attributes attributes;
    byte[] arrayOfByte = outputResources.get("META-INF/MANIFEST.MF");
    if (arrayOfByte == null) {
      manifest = new Manifest();
      attributes = manifest.getMainAttributes();
      attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
    } else {
      manifest = new Manifest(new ByteArrayInputStream((byte[])manifest));
      attributes = manifest.getMainAttributes();
      outputResources.remove("META-INF/MANIFEST.MF");
    } 
    String str = attributes.getValue(CREATED_BY);
    if (str == null) {
      str = "";
    } else {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(str);
      stringBuilder1.append(" + ");
      str = stringBuilder1.toString();
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(str);
    stringBuilder.append("dx 1.12");
    str = stringBuilder.toString();
    attributes.put(CREATED_BY, str);
    attributes.putValue("Dex-Location", "classes.dex");
    return manifest;
  }
  
  private static byte[] mergeIncremental(byte[] paramArrayOfbyte, File paramFile) throws IOException {
    File file;
    Dex dex;
    if (paramArrayOfbyte != null) {
      dex = new Dex(paramArrayOfbyte);
    } else {
      paramArrayOfbyte = null;
    } 
    if (paramFile.exists()) {
      Dex dex1 = new Dex(paramFile);
    } else {
      paramFile = null;
    } 
    if (paramArrayOfbyte == null && paramFile == null)
      return null; 
    if (paramArrayOfbyte == null) {
      file = paramFile;
    } else if (paramFile != null) {
      CollisionPolicy collisionPolicy = CollisionPolicy.KEEP_FIRST;
      dex = (new DexMerger(new Dex[] { (Dex)file, (Dex)paramFile }, collisionPolicy)).merge();
    } 
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    dex.writeTo(byteArrayOutputStream);
    return byteArrayOutputStream.toByteArray();
  }
  
  private static byte[] mergeLibraryDexBuffers(byte[] paramArrayOfbyte) throws IOException {
    ArrayList<Dex> arrayList = new ArrayList();
    if (paramArrayOfbyte != null)
      arrayList.add(new Dex(paramArrayOfbyte)); 
    Iterator<byte> iterator = libraryDexBuffers.iterator();
    while (iterator.hasNext())
      arrayList.add(new Dex((byte[])iterator.next())); 
    return arrayList.isEmpty() ? null : (new DexMerger(arrayList.<Dex>toArray(new Dex[arrayList.size()]), CollisionPolicy.FAIL)).merge().getBytes();
  }
  
  private static OutputStream openOutput(String paramString) throws IOException {
    return (OutputStream)((paramString.equals("-") || paramString.startsWith("-.")) ? System.out : new FileOutputStream(paramString));
  }
  
  private static DirectClassFile parseClass(String paramString, byte[] paramArrayOfbyte) {
    DirectClassFile directClassFile = new DirectClassFile(paramArrayOfbyte, paramString, args.cfOptions.strictNameCheck);
    directClassFile.setAttributeFactory((AttributeFactory)StdAttributeFactory.THE_ONE);
    directClassFile.getMagic();
    return directClassFile;
  }
  
  private static boolean processAllFiles() {
    // Byte code:
    //   0: invokestatic createDexFile : ()V
    //   3: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.args : Lorg/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$Arguments;
    //   6: getfield jarOutput : Z
    //   9: ifeq -> 22
    //   12: new java/util/TreeMap
    //   15: dup
    //   16: invokespecial <init> : ()V
    //   19: putstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.outputResources : Ljava/util/TreeMap;
    //   22: iconst_0
    //   23: putstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.anyFilesProcessed : Z
    //   26: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.args : Lorg/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$Arguments;
    //   29: getfield fileNames : [Ljava/lang/String;
    //   32: astore_2
    //   33: aload_2
    //   34: invokestatic sort : ([Ljava/lang/Object;)V
    //   37: new java/util/concurrent/ThreadPoolExecutor
    //   40: dup
    //   41: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.args : Lorg/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$Arguments;
    //   44: getfield numThreads : I
    //   47: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.args : Lorg/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$Arguments;
    //   50: getfield numThreads : I
    //   53: lconst_0
    //   54: getstatic java/util/concurrent/TimeUnit.SECONDS : Ljava/util/concurrent/TimeUnit;
    //   57: new java/util/concurrent/ArrayBlockingQueue
    //   60: dup
    //   61: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.args : Lorg/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$Arguments;
    //   64: getfield numThreads : I
    //   67: iconst_2
    //   68: imul
    //   69: iconst_1
    //   70: invokespecial <init> : (IZ)V
    //   73: new java/util/concurrent/ThreadPoolExecutor$CallerRunsPolicy
    //   76: dup
    //   77: invokespecial <init> : ()V
    //   80: invokespecial <init> : (IIJLjava/util/concurrent/TimeUnit;Ljava/util/concurrent/BlockingQueue;Ljava/util/concurrent/RejectedExecutionHandler;)V
    //   83: putstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.classTranslatorPool : Ljava/util/concurrent/ExecutorService;
    //   86: invokestatic newSingleThreadExecutor : ()Ljava/util/concurrent/ExecutorService;
    //   89: putstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.classDefItemConsumer : Ljava/util/concurrent/ExecutorService;
    //   92: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.args : Lorg/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$Arguments;
    //   95: getfield mainDexListFile : Ljava/lang/String;
    //   98: ifnull -> 693
    //   101: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.args : Lorg/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$Arguments;
    //   104: getfield strictNameCheck : Z
    //   107: ifeq -> 122
    //   110: new org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$MainDexListFilter
    //   113: dup
    //   114: aconst_null
    //   115: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$1;)V
    //   118: astore_1
    //   119: goto -> 683
    //   122: new org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$BestEffortMainDexListFilter
    //   125: dup
    //   126: invokespecial <init> : ()V
    //   129: astore_1
    //   130: goto -> 683
    //   133: iload_0
    //   134: aload_2
    //   135: arraylength
    //   136: if_icmpge -> 153
    //   139: aload_2
    //   140: iload_0
    //   141: aaload
    //   142: aload_1
    //   143: invokestatic processOne : (Ljava/lang/String;Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/direct/ClassPathOpener$FileNameFilter;)V
    //   146: iload_0
    //   147: iconst_1
    //   148: iadd
    //   149: istore_0
    //   150: goto -> 133
    //   153: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.dexOutputFutures : Ljava/util/List;
    //   156: invokeinterface size : ()I
    //   161: ifgt -> 244
    //   164: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.args : Lorg/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$Arguments;
    //   167: getfield minimalMainDex : Z
    //   170: ifeq -> 688
    //   173: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.dexRotationLock : Ljava/lang/Object;
    //   176: astore_3
    //   177: aload_3
    //   178: monitorenter
    //   179: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.maxMethodIdsInProcess : I
    //   182: ifgt -> 202
    //   185: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.maxFieldIdsInProcess : I
    //   188: ifle -> 194
    //   191: goto -> 202
    //   194: aload_3
    //   195: monitorexit
    //   196: invokestatic rotateDexFile : ()V
    //   199: goto -> 688
    //   202: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.dexRotationLock : Ljava/lang/Object;
    //   205: invokevirtual wait : ()V
    //   208: goto -> 179
    //   211: astore_1
    //   212: aload_3
    //   213: monitorexit
    //   214: aload_1
    //   215: athrow
    //   216: iload_0
    //   217: aload_2
    //   218: arraylength
    //   219: if_icmpge -> 277
    //   222: aload_2
    //   223: iload_0
    //   224: aaload
    //   225: new org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$NotFilter
    //   228: dup
    //   229: aload_1
    //   230: aconst_null
    //   231: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/direct/ClassPathOpener$FileNameFilter;Lorg/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$1;)V
    //   234: invokestatic processOne : (Ljava/lang/String;Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/direct/ClassPathOpener$FileNameFilter;)V
    //   237: iload_0
    //   238: iconst_1
    //   239: iadd
    //   240: istore_0
    //   241: goto -> 216
    //   244: new org/firstinspires/ftc/robotcore/internal/android/dex/DexException
    //   247: dup
    //   248: ldc_w 'Too many classes in --main-dex-list, main dex capacity exceeded'
    //   251: invokespecial <init> : (Ljava/lang/String;)V
    //   254: athrow
    //   255: iload_0
    //   256: aload_2
    //   257: arraylength
    //   258: if_icmpge -> 277
    //   261: aload_2
    //   262: iload_0
    //   263: aaload
    //   264: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/cf/direct/ClassPathOpener.acceptAll : Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/direct/ClassPathOpener$FileNameFilter;
    //   267: invokestatic processOne : (Ljava/lang/String;Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/direct/ClassPathOpener$FileNameFilter;)V
    //   270: iload_0
    //   271: iconst_1
    //   272: iadd
    //   273: istore_0
    //   274: goto -> 255
    //   277: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.classTranslatorPool : Ljava/util/concurrent/ExecutorService;
    //   280: invokeinterface shutdown : ()V
    //   285: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.classTranslatorPool : Ljava/util/concurrent/ExecutorService;
    //   288: ldc2_w 600
    //   291: getstatic java/util/concurrent/TimeUnit.SECONDS : Ljava/util/concurrent/TimeUnit;
    //   294: invokeinterface awaitTermination : (JLjava/util/concurrent/TimeUnit;)Z
    //   299: pop
    //   300: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.classDefItemConsumer : Ljava/util/concurrent/ExecutorService;
    //   303: invokeinterface shutdown : ()V
    //   308: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.classDefItemConsumer : Ljava/util/concurrent/ExecutorService;
    //   311: ldc2_w 600
    //   314: getstatic java/util/concurrent/TimeUnit.SECONDS : Ljava/util/concurrent/TimeUnit;
    //   317: invokeinterface awaitTermination : (JLjava/util/concurrent/TimeUnit;)Z
    //   322: pop
    //   323: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.addToDexFutures : Ljava/util/List;
    //   326: invokeinterface iterator : ()Ljava/util/Iterator;
    //   331: astore_1
    //   332: aload_1
    //   333: invokeinterface hasNext : ()Z
    //   338: ifeq -> 459
    //   341: aload_1
    //   342: invokeinterface next : ()Ljava/lang/Object;
    //   347: checkcast java/util/concurrent/Future
    //   350: astore_2
    //   351: aload_2
    //   352: invokeinterface get : ()Ljava/lang/Object;
    //   357: pop
    //   358: goto -> 332
    //   361: astore_2
    //   362: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.errors : Ljava/util/concurrent/atomic/AtomicInteger;
    //   365: invokevirtual incrementAndGet : ()I
    //   368: bipush #10
    //   370: if_icmpge -> 448
    //   373: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.args : Lorg/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$Arguments;
    //   376: getfield debug : Z
    //   379: ifeq -> 404
    //   382: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/DxConsole.err : Ljava/io/PrintStream;
    //   385: ldc_w 'Uncaught translation error:'
    //   388: invokevirtual println : (Ljava/lang/String;)V
    //   391: aload_2
    //   392: invokevirtual getCause : ()Ljava/lang/Throwable;
    //   395: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/DxConsole.err : Ljava/io/PrintStream;
    //   398: invokevirtual printStackTrace : (Ljava/io/PrintStream;)V
    //   401: goto -> 332
    //   404: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/DxConsole.err : Ljava/io/PrintStream;
    //   407: astore_3
    //   408: new java/lang/StringBuilder
    //   411: dup
    //   412: invokespecial <init> : ()V
    //   415: astore #4
    //   417: aload #4
    //   419: ldc_w 'Uncaught translation error: '
    //   422: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   425: pop
    //   426: aload #4
    //   428: aload_2
    //   429: invokevirtual getCause : ()Ljava/lang/Throwable;
    //   432: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   435: pop
    //   436: aload_3
    //   437: aload #4
    //   439: invokevirtual toString : ()Ljava/lang/String;
    //   442: invokevirtual println : (Ljava/lang/String;)V
    //   445: goto -> 332
    //   448: new java/lang/InterruptedException
    //   451: dup
    //   452: ldc_w 'Too many errors'
    //   455: invokespecial <init> : (Ljava/lang/String;)V
    //   458: athrow
    //   459: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.errors : Ljava/util/concurrent/atomic/AtomicInteger;
    //   462: invokevirtual get : ()I
    //   465: istore_0
    //   466: iload_0
    //   467: ifeq -> 536
    //   470: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/DxConsole.err : Ljava/io/PrintStream;
    //   473: astore_2
    //   474: new java/lang/StringBuilder
    //   477: dup
    //   478: invokespecial <init> : ()V
    //   481: astore_3
    //   482: aload_3
    //   483: iload_0
    //   484: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   487: pop
    //   488: aload_3
    //   489: ldc_w ' error'
    //   492: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   495: pop
    //   496: iload_0
    //   497: iconst_1
    //   498: if_icmpne -> 508
    //   501: ldc_w ''
    //   504: astore_1
    //   505: goto -> 512
    //   508: ldc_w 's'
    //   511: astore_1
    //   512: aload_3
    //   513: aload_1
    //   514: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   517: pop
    //   518: aload_3
    //   519: ldc_w '; aborting'
    //   522: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   525: pop
    //   526: aload_2
    //   527: aload_3
    //   528: invokevirtual toString : ()Ljava/lang/String;
    //   531: invokevirtual println : (Ljava/lang/String;)V
    //   534: iconst_0
    //   535: ireturn
    //   536: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.args : Lorg/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$Arguments;
    //   539: getfield incremental : Z
    //   542: ifeq -> 553
    //   545: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.anyFilesProcessed : Z
    //   548: ifne -> 553
    //   551: iconst_1
    //   552: ireturn
    //   553: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.anyFilesProcessed : Z
    //   556: ifne -> 579
    //   559: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.args : Lorg/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$Arguments;
    //   562: getfield emptyOk : Z
    //   565: ifne -> 579
    //   568: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/DxConsole.err : Ljava/io/PrintStream;
    //   571: ldc_w 'no classfiles specified'
    //   574: invokevirtual println : (Ljava/lang/String;)V
    //   577: iconst_0
    //   578: ireturn
    //   579: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.args : Lorg/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$Arguments;
    //   582: getfield optimize : Z
    //   585: ifeq -> 603
    //   588: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.args : Lorg/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$Arguments;
    //   591: getfield statistics : Z
    //   594: ifeq -> 603
    //   597: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/DxConsole.out : Ljava/io/PrintStream;
    //   600: invokestatic dumpStatistics : (Ljava/io/PrintStream;)V
    //   603: iconst_1
    //   604: ireturn
    //   605: astore_1
    //   606: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.classTranslatorPool : Ljava/util/concurrent/ExecutorService;
    //   609: invokeinterface shutdownNow : ()Ljava/util/List;
    //   614: pop
    //   615: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.classDefItemConsumer : Ljava/util/concurrent/ExecutorService;
    //   618: invokeinterface shutdownNow : ()Ljava/util/List;
    //   623: pop
    //   624: aload_1
    //   625: getstatic java/lang/System.out : Ljava/io/PrintStream;
    //   628: invokevirtual printStackTrace : (Ljava/io/PrintStream;)V
    //   631: new java/lang/RuntimeException
    //   634: dup
    //   635: ldc_w 'Unexpected exception in translator thread.'
    //   638: aload_1
    //   639: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   642: athrow
    //   643: astore_1
    //   644: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.classTranslatorPool : Ljava/util/concurrent/ExecutorService;
    //   647: invokeinterface shutdownNow : ()Ljava/util/List;
    //   652: pop
    //   653: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main.classDefItemConsumer : Ljava/util/concurrent/ExecutorService;
    //   656: invokeinterface shutdownNow : ()Ljava/util/List;
    //   661: pop
    //   662: new java/lang/RuntimeException
    //   665: dup
    //   666: ldc_w 'Translation has been interrupted'
    //   669: aload_1
    //   670: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   673: athrow
    //   674: astore_1
    //   675: goto -> 277
    //   678: astore #4
    //   680: goto -> 179
    //   683: iconst_0
    //   684: istore_0
    //   685: goto -> 133
    //   688: iconst_0
    //   689: istore_0
    //   690: goto -> 216
    //   693: iconst_0
    //   694: istore_0
    //   695: goto -> 255
    // Exception table:
    //   from	to	target	type
    //   92	119	674	org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$StopProcessing
    //   122	130	674	org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$StopProcessing
    //   133	146	674	org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$StopProcessing
    //   153	179	674	org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$StopProcessing
    //   179	191	211	finally
    //   194	196	211	finally
    //   196	199	674	org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$StopProcessing
    //   202	208	678	java/lang/InterruptedException
    //   202	208	211	finally
    //   212	214	211	finally
    //   214	216	674	org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$StopProcessing
    //   216	237	674	org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$StopProcessing
    //   244	255	674	org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$StopProcessing
    //   255	270	674	org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$StopProcessing
    //   277	332	643	java/lang/InterruptedException
    //   277	332	605	java/lang/Exception
    //   332	351	643	java/lang/InterruptedException
    //   332	351	605	java/lang/Exception
    //   351	358	361	java/util/concurrent/ExecutionException
    //   351	358	643	java/lang/InterruptedException
    //   351	358	605	java/lang/Exception
    //   362	401	643	java/lang/InterruptedException
    //   362	401	605	java/lang/Exception
    //   404	445	643	java/lang/InterruptedException
    //   404	445	605	java/lang/Exception
    //   448	459	643	java/lang/InterruptedException
    //   448	459	605	java/lang/Exception
  }
  
  private static boolean processClass(String paramString, byte[] paramArrayOfbyte) {
    if (!args.coreLibrary)
      checkClassName(paramString); 
    try {
      (new DirectClassFileConsumer(paramString, paramArrayOfbyte, null)).call((new ClassParserTask(paramString, paramArrayOfbyte)).call());
      return true;
    } catch (ParseException parseException) {
      throw parseException;
    } catch (Exception exception) {
      throw new RuntimeException("Exception parsing classes", exception);
    } 
  }
  
  private static boolean processFileBytes(String paramString, long paramLong, byte[] paramArrayOfbyte) {
    PrintStream printStream;
    boolean bool;
    boolean bool1 = paramString.endsWith(".class");
    boolean bool2 = paramString.equals("classes.dex");
    if (outputResources != null) {
      bool = true;
    } else {
      bool = false;
    } 
    if (!bool1 && !bool2 && !bool) {
      if (args.verbose) {
        printStream = DxConsole.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ignored resource ");
        stringBuilder.append(paramString);
        printStream.println(stringBuilder.toString());
      } 
      return false;
    } 
    if (args.verbose) {
      PrintStream printStream1 = DxConsole.out;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("processing ");
      stringBuilder.append(paramString);
      stringBuilder.append("...");
      printStream1.println(stringBuilder.toString());
    } 
    paramString = fixPath(paramString);
    if (bool1) {
      if (bool && args.keepClassesInJar)
        synchronized (outputResources) {
          outputResources.put(paramString, printStream);
        }  
      if (paramLong < minimumFileAge)
        return true; 
      processClass(paramString, (byte[])printStream);
      return false;
    } 
    if (bool2)
      synchronized (libraryDexBuffers) {
        libraryDexBuffers.add(printStream);
        return true;
      }  
    synchronized (outputResources) {
      outputResources.put(paramString, printStream);
      return true;
    } 
  }
  
  private static void processOne(String paramString, ClassPathOpener.FileNameFilter paramFileNameFilter) {
    if ((new ClassPathOpener(paramString, true, paramFileNameFilter, new FileBytesConsumer())).process())
      updateStatus(true); 
  }
  
  private static void readPathsFromFile(String paramString, Collection<String> paramCollection) throws IOException {
    String str = null;
    try {
      BufferedReader bufferedReader = new BufferedReader(new FileReader(paramString));
    } finally {
      paramCollection = null;
    } 
    if (paramString != null)
      paramString.close(); 
    throw paramCollection;
  }
  
  private static void rotateDexFile() {
    DexFile dexFile = outputDex;
    if (dexFile != null) {
      ExecutorService executorService = dexOutPool;
      if (executorService != null) {
        dexOutputFutures.add((Future)executorService.submit(new DexWriter(outputDex)));
      } else {
        dexOutputArrays.add(writeDex(dexFile));
      } 
    } 
    createDexFile();
  }
  
  public static int run(Arguments paramArguments) throws IOException {
    errors.set(0);
    libraryDexBuffers.clear();
    args = paramArguments;
    paramArguments.makeOptionsObjects();
    if (args.humanOutName != null) {
      OutputStream outputStream = openOutput(args.humanOutName);
      humanOutWriter = new OutputStreamWriter(outputStream);
    } else {
      paramArguments = null;
    } 
    try {
      if (args.multiDex)
        return runMultiDex(); 
      return runMonoDex();
    } finally {
      closeOutput((OutputStream)paramArguments);
    } 
  }
  
  private static int runMonoDex() throws IOException {
    File file;
    if (args.incremental) {
      if (args.outName == null) {
        System.err.println("error: no incremental output name specified");
        return -1;
      } 
      arrayOfByte1 = (byte[])new File(args.outName);
      file = (File)arrayOfByte1;
      if (arrayOfByte1.exists()) {
        minimumFileAge = arrayOfByte1.lastModified();
        file = (File)arrayOfByte1;
      } 
    } else {
      file = null;
    } 
    if (!processAllFiles())
      return 1; 
    if (args.incremental && !anyFilesProcessed)
      return 0; 
    if (!outputDex.isEmpty() || args.humanOutName != null) {
      byte[] arrayOfByte = writeDex(outputDex);
      arrayOfByte1 = arrayOfByte;
      if (arrayOfByte == null)
        return 2; 
    } else {
      arrayOfByte1 = null;
    } 
    byte[] arrayOfByte2 = arrayOfByte1;
    if (args.incremental)
      arrayOfByte2 = mergeIncremental(arrayOfByte1, file); 
    byte[] arrayOfByte1 = mergeLibraryDexBuffers(arrayOfByte2);
    if (args.jarOutput) {
      outputDex = null;
      if (arrayOfByte1 != null)
        outputResources.put("classes.dex", arrayOfByte1); 
      if (!createJar(args.outName))
        return 3; 
    } else if (arrayOfByte1 != null && args.outName != null) {
      OutputStream outputStream = openOutput(args.outName);
      outputStream.write(arrayOfByte1);
      closeOutput(outputStream);
    } 
    return 0;
  }
  
  private static int runMultiDex() throws IOException {
    if (args.mainDexListFile != null) {
      classesInMainDex = new HashSet<String>();
      readPathsFromFile(args.mainDexListFile, classesInMainDex);
    } 
    dexOutPool = Executors.newFixedThreadPool(args.numThreads);
    if (!processAllFiles())
      return 1; 
    if (libraryDexBuffers.isEmpty()) {
      if (outputDex != null) {
        dexOutputFutures.add((Future)dexOutPool.submit(new DexWriter(outputDex)));
        outputDex = null;
      } 
      try {
        dexOutPool.shutdown();
        if (dexOutPool.awaitTermination(600L, TimeUnit.SECONDS)) {
          for (Future<byte[]> future : dexOutputFutures)
            dexOutputArrays.add(future.get()); 
          if (args.jarOutput) {
            for (int i = 0; i < dexOutputArrays.size(); i++)
              outputResources.put(getDexFileName(i), dexOutputArrays.get(i)); 
            if (!createJar(args.outName))
              return 3; 
          } else if (args.outName != null) {
            File file = new File(args.outName);
            int i = 0;
            while (i < dexOutputArrays.size()) {
              FileOutputStream fileOutputStream = new FileOutputStream(new File(file, getDexFileName(i)));
              try {
                fileOutputStream.write(dexOutputArrays.get(i));
                closeOutput(fileOutputStream);
              } finally {
                closeOutput(fileOutputStream);
              } 
            } 
          } 
          return 0;
        } 
        throw new RuntimeException("Timed out waiting for dex writer threads.");
      } catch (InterruptedException interruptedException) {
        dexOutPool.shutdownNow();
        throw new RuntimeException("A dex writer thread has been interrupted.");
      } catch (Exception exception) {
        dexOutPool.shutdownNow();
        throw new RuntimeException("Unexpected exception in dex writer thread");
      } 
    } 
    throw new DexException("Library dex files are not supported in multi-dex mode");
  }
  
  private static ClassDefItem translateClass(byte[] paramArrayOfbyte, DirectClassFile paramDirectClassFile) {
    try {
      return CfTranslator.translate(paramDirectClassFile, paramArrayOfbyte, args.cfOptions, args.dexOptions, outputDex);
    } catch (ParseException parseException) {
      DxConsole.err.println("\ntrouble processing:");
      if (args.debug) {
        parseException.printStackTrace(DxConsole.err);
      } else {
        parseException.printContext(DxConsole.err);
      } 
      errors.incrementAndGet();
      return null;
    } 
  }
  
  private static void updateStatus(boolean paramBoolean) {
    anyFilesProcessed = paramBoolean | anyFilesProcessed;
  }
  
  private static byte[] writeDex(DexFile paramDexFile) {
    try {
      byte[] arrayOfByte;
      if (args.methodToDump != null) {
        paramDexFile.toDex(null, false);
        dumpMethod(paramDexFile, args.methodToDump, humanOutWriter);
        arrayOfByte = null;
      } else {
        arrayOfByte = paramDexFile.toDex(humanOutWriter, args.verboseDump);
      } 
      if (args.statistics)
        DxConsole.out.println(paramDexFile.getStatistics().toHuman()); 
    } finally {
      if (humanOutWriter != null)
        humanOutWriter.flush(); 
    } 
    if (args.debug) {
      DxConsole.err.println("\ntrouble writing output:");
      paramDexFile.printStackTrace(DxConsole.err);
      return null;
    } 
    PrintStream printStream = DxConsole.err;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("\ntrouble writing output: ");
    stringBuilder.append(paramDexFile.getMessage());
    printStream.println(stringBuilder.toString());
    return null;
  }
  
  public static class Arguments {
    private static final String INCREMENTAL_OPTION = "--incremental";
    
    private static final String INPUT_LIST_OPTION = "--input-list";
    
    private static final String MAIN_DEX_LIST_OPTION = "--main-dex-list";
    
    private static final String MINIMAL_MAIN_DEX_OPTION = "--minimal-main-dex";
    
    private static final String MULTI_DEX_OPTION = "--multi-dex";
    
    private static final String NUM_THREADS_OPTION = "--num-threads";
    
    public CfOptions cfOptions;
    
    public boolean coreLibrary = false;
    
    public boolean debug = false;
    
    public DexOptions dexOptions;
    
    public String dontOptimizeListFile = null;
    
    public int dumpWidth = 0;
    
    public boolean emptyOk = false;
    
    public String[] fileNames;
    
    public boolean forceJumbo = false;
    
    public String humanOutName = null;
    
    public boolean incremental = false;
    
    private List<String> inputList = null;
    
    public boolean jarOutput = false;
    
    public boolean keepClassesInJar = false;
    
    public boolean localInfo = true;
    
    public String mainDexListFile = null;
    
    private int maxNumberOfIdxPerDex = 65536;
    
    public String methodToDump = null;
    
    public boolean minimalMainDex = false;
    
    public boolean multiDex = false;
    
    public int numThreads = 1;
    
    public boolean optimize = true;
    
    public String optimizeListFile = null;
    
    public String outName = null;
    
    public int positionInfo = 2;
    
    public boolean statistics;
    
    public boolean strictNameCheck = true;
    
    public boolean verbose = false;
    
    public boolean verboseDump = false;
    
    public boolean warnings = true;
    
    private void makeOptionsObjects() {
      CfOptions cfOptions = new CfOptions();
      this.cfOptions = cfOptions;
      cfOptions.positionInfo = this.positionInfo;
      this.cfOptions.localInfo = this.localInfo;
      this.cfOptions.strictNameCheck = this.strictNameCheck;
      this.cfOptions.optimize = this.optimize;
      this.cfOptions.optimizeListFile = this.optimizeListFile;
      this.cfOptions.dontOptimizeListFile = this.dontOptimizeListFile;
      this.cfOptions.statistics = this.statistics;
      if (this.warnings) {
        this.cfOptions.warn = DxConsole.err;
      } else {
        this.cfOptions.warn = DxConsole.noop;
      } 
      DexOptions dexOptions = new DexOptions();
      this.dexOptions = dexOptions;
      dexOptions.forceJumbo = this.forceJumbo;
    }
    
    public void parse(String[] param1ArrayOfString) {
      ArgumentsParser argumentsParser = new ArgumentsParser(param1ArrayOfString);
      boolean bool2 = false;
      boolean bool1 = bool2;
      while (true) {
        PrintStream printStream;
        if (argumentsParser.getNext()) {
          if (argumentsParser.isArg("--debug")) {
            this.debug = true;
            continue;
          } 
          if (argumentsParser.isArg("--no-warning")) {
            this.warnings = false;
            continue;
          } 
          if (argumentsParser.isArg("--verbose")) {
            this.verbose = true;
            continue;
          } 
          if (argumentsParser.isArg("--verbose-dump")) {
            this.verboseDump = true;
            continue;
          } 
          if (argumentsParser.isArg("--no-files")) {
            this.emptyOk = true;
            continue;
          } 
          if (argumentsParser.isArg("--no-optimize")) {
            this.optimize = false;
            continue;
          } 
          if (argumentsParser.isArg("--no-strict")) {
            this.strictNameCheck = false;
            continue;
          } 
          if (argumentsParser.isArg("--core-library")) {
            this.coreLibrary = true;
            continue;
          } 
          if (argumentsParser.isArg("--statistics")) {
            this.statistics = true;
            continue;
          } 
          if (argumentsParser.isArg("--optimize-list=")) {
            if (this.dontOptimizeListFile == null) {
              this.optimize = true;
              this.optimizeListFile = argumentsParser.getLastValue();
              continue;
            } 
            System.err.println("--optimize-list and --no-optimize-list are incompatible.");
            throw new UsageException();
          } 
          if (argumentsParser.isArg("--no-optimize-list=")) {
            if (this.dontOptimizeListFile == null) {
              this.optimize = true;
              this.dontOptimizeListFile = argumentsParser.getLastValue();
              continue;
            } 
            System.err.println("--optimize-list and --no-optimize-list are incompatible.");
            throw new UsageException();
          } 
          if (argumentsParser.isArg("--keep-classes")) {
            this.keepClassesInJar = true;
            continue;
          } 
          if (argumentsParser.isArg("--output=")) {
            this.outName = argumentsParser.getLastValue();
            if ((new File(this.outName)).isDirectory()) {
              this.jarOutput = false;
              bool1 = true;
              continue;
            } 
            if (FileUtils.hasArchiveSuffix(this.outName)) {
              this.jarOutput = true;
              continue;
            } 
            if (this.outName.endsWith(".dex") || this.outName.equals("-")) {
              this.jarOutput = false;
              bool2 = true;
              continue;
            } 
            printStream = System.err;
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("unknown output extension: ");
            stringBuilder1.append(this.outName);
            printStream.println(stringBuilder1.toString());
            throw new UsageException();
          } 
          if (printStream.isArg("--dump-to=")) {
            this.humanOutName = printStream.getLastValue();
            continue;
          } 
          if (printStream.isArg("--dump-width=")) {
            this.dumpWidth = Integer.parseInt(printStream.getLastValue());
            continue;
          } 
          if (printStream.isArg("--dump-method=")) {
            this.methodToDump = printStream.getLastValue();
            this.jarOutput = false;
            continue;
          } 
          if (printStream.isArg("--positions=")) {
            String str = printStream.getLastValue().intern();
            if (str == "none") {
              this.positionInfo = 1;
              continue;
            } 
            if (str == "important") {
              this.positionInfo = 3;
              continue;
            } 
            if (str == "lines") {
              this.positionInfo = 2;
              continue;
            } 
            printStream = System.err;
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("unknown positions option: ");
            stringBuilder1.append(str);
            printStream.println(stringBuilder1.toString());
            throw new UsageException();
          } 
          if (printStream.isArg("--no-locals")) {
            this.localInfo = false;
            continue;
          } 
          if (printStream.isArg("--num-threads=")) {
            this.numThreads = Integer.parseInt(printStream.getLastValue());
            continue;
          } 
          if (printStream.isArg("--incremental")) {
            this.incremental = true;
            continue;
          } 
          if (printStream.isArg("--force-jumbo")) {
            this.forceJumbo = true;
            continue;
          } 
          if (printStream.isArg("--multi-dex")) {
            this.multiDex = true;
            continue;
          } 
          if (printStream.isArg("--main-dex-list=")) {
            this.mainDexListFile = printStream.getLastValue();
            continue;
          } 
          if (printStream.isArg("--minimal-main-dex")) {
            this.minimalMainDex = true;
            continue;
          } 
          if (printStream.isArg("--set-max-idx-number=")) {
            this.maxNumberOfIdxPerDex = Integer.parseInt(printStream.getLastValue());
            continue;
          } 
          if (printStream.isArg("--input-list=")) {
            File file = new File(printStream.getLastValue());
            try {
              this.inputList = new ArrayList<String>();
              Main.readPathsFromFile(file.getAbsolutePath(), this.inputList);
            } catch (IOException iOException) {
              printStream = System.err;
              StringBuilder stringBuilder1 = new StringBuilder();
              stringBuilder1.append("Unable to read input list file: ");
              stringBuilder1.append(file.getName());
              printStream.println(stringBuilder1.toString());
              throw new UsageException();
            } 
            continue;
          } 
          PrintStream printStream1 = System.err;
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("unknown option: ");
          stringBuilder.append(printStream.getCurrent());
          printStream1.println(stringBuilder.toString());
          throw new UsageException();
        } 
        this.fileNames = printStream.getRemaining();
        List<String> list = this.inputList;
        if (list != null && !list.isEmpty()) {
          this.inputList.addAll(Arrays.asList(this.fileNames));
          list = this.inputList;
          this.fileNames = list.<String>toArray(new String[list.size()]);
        } 
        if (this.fileNames.length == 0) {
          if (!this.emptyOk) {
            System.err.println("no input files specified");
            throw new UsageException();
          } 
        } else if (this.emptyOk) {
          System.out.println("ignoring input files");
        } 
        if (this.humanOutName == null && this.methodToDump != null)
          this.humanOutName = "-"; 
        if (this.mainDexListFile == null || this.multiDex) {
          if (!this.minimalMainDex || (this.mainDexListFile != null && this.multiDex)) {
            if (!this.multiDex || !this.incremental) {
              if (!this.multiDex || !bool2) {
                if (bool1 && !this.multiDex)
                  this.outName = (new File(this.outName, "classes.dex")).getPath(); 
                makeOptionsObjects();
                return;
              } 
              PrintStream printStream1 = System.err;
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("Unsupported output \"");
              stringBuilder.append(this.outName);
              stringBuilder.append("\". ");
              stringBuilder.append("--multi-dex");
              stringBuilder.append(" supports only archive or directory output");
              printStream1.println(stringBuilder.toString());
              throw new UsageException();
            } 
            System.err.println("--incremental is not supported with --multi-dex");
            throw new UsageException();
          } 
          System.err.println("--minimal-main-dex is only supported in combination with --multi-dex and --main-dex-list");
          throw new UsageException();
        } 
        System.err.println("--main-dex-list is only supported in combination with --multi-dex");
        throw new UsageException();
      } 
    }
    
    private static class ArgumentsParser {
      private final String[] arguments;
      
      private String current;
      
      private int index;
      
      private String lastValue;
      
      public ArgumentsParser(String[] param2ArrayOfString) {
        this.arguments = param2ArrayOfString;
        this.index = 0;
      }
      
      private boolean getNextValue() {
        int i = this.index;
        String[] arrayOfString = this.arguments;
        if (i >= arrayOfString.length)
          return false; 
        this.current = arrayOfString[i];
        this.index = i + 1;
        return true;
      }
      
      public String getCurrent() {
        return this.current;
      }
      
      public String getLastValue() {
        return this.lastValue;
      }
      
      public boolean getNext() {
        int i = this.index;
        String[] arrayOfString = this.arguments;
        if (i >= arrayOfString.length)
          return false; 
        String str = arrayOfString[i];
        this.current = str;
        if (!str.equals("--")) {
          if (!this.current.startsWith("--"))
            return false; 
          this.index++;
          return true;
        } 
        return false;
      }
      
      public String[] getRemaining() {
        String[] arrayOfString1 = this.arguments;
        int j = arrayOfString1.length;
        int i = this.index;
        j -= i;
        String[] arrayOfString2 = new String[j];
        if (j > 0)
          System.arraycopy(arrayOfString1, i, arrayOfString2, 0, j); 
        return arrayOfString2;
      }
      
      public boolean isArg(String param2String) {
        int i = param2String.length();
        if (i > 0) {
          int j = i - 1;
          if (param2String.charAt(j) == '=') {
            if (this.current.startsWith(param2String)) {
              this.lastValue = this.current.substring(i);
              return true;
            } 
            param2String = param2String.substring(0, j);
            if (this.current.equals(param2String)) {
              if (getNextValue()) {
                this.lastValue = this.current;
                return true;
              } 
              PrintStream printStream = System.err;
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("Missing value after parameter ");
              stringBuilder.append(param2String);
              printStream.println(stringBuilder.toString());
              throw new UsageException();
            } 
            return false;
          } 
        } 
        return this.current.equals(param2String);
      }
    }
  }
  
  private static class ArgumentsParser {
    private final String[] arguments;
    
    private String current;
    
    private int index;
    
    private String lastValue;
    
    public ArgumentsParser(String[] param1ArrayOfString) {
      this.arguments = param1ArrayOfString;
      this.index = 0;
    }
    
    private boolean getNextValue() {
      int i = this.index;
      String[] arrayOfString = this.arguments;
      if (i >= arrayOfString.length)
        return false; 
      this.current = arrayOfString[i];
      this.index = i + 1;
      return true;
    }
    
    public String getCurrent() {
      return this.current;
    }
    
    public String getLastValue() {
      return this.lastValue;
    }
    
    public boolean getNext() {
      int i = this.index;
      String[] arrayOfString = this.arguments;
      if (i >= arrayOfString.length)
        return false; 
      String str = arrayOfString[i];
      this.current = str;
      if (!str.equals("--")) {
        if (!this.current.startsWith("--"))
          return false; 
        this.index++;
        return true;
      } 
      return false;
    }
    
    public String[] getRemaining() {
      String[] arrayOfString1 = this.arguments;
      int j = arrayOfString1.length;
      int i = this.index;
      j -= i;
      String[] arrayOfString2 = new String[j];
      if (j > 0)
        System.arraycopy(arrayOfString1, i, arrayOfString2, 0, j); 
      return arrayOfString2;
    }
    
    public boolean isArg(String param1String) {
      int i = param1String.length();
      if (i > 0) {
        int j = i - 1;
        if (param1String.charAt(j) == '=') {
          if (this.current.startsWith(param1String)) {
            this.lastValue = this.current.substring(i);
            return true;
          } 
          param1String = param1String.substring(0, j);
          if (this.current.equals(param1String)) {
            if (getNextValue()) {
              this.lastValue = this.current;
              return true;
            } 
            PrintStream printStream = System.err;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Missing value after parameter ");
            stringBuilder.append(param1String);
            printStream.println(stringBuilder.toString());
            throw new UsageException();
          } 
          return false;
        } 
      } 
      return this.current.equals(param1String);
    }
  }
  
  private static class BestEffortMainDexListFilter implements ClassPathOpener.FileNameFilter {
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    
    public BestEffortMainDexListFilter() {
      Iterator<String> iterator = Main.classesInMainDex.iterator();
      while (iterator.hasNext()) {
        String str1 = Main.fixPath(iterator.next());
        String str2 = getSimpleName(str1);
        List<String> list2 = this.map.get(str2);
        List<String> list1 = list2;
        if (list2 == null) {
          list1 = new ArrayList(1);
          this.map.put(str2, list1);
        } 
        list1.add(str1);
      } 
    }
    
    private static String getSimpleName(String param1String) {
      int i = param1String.lastIndexOf('/');
      String str = param1String;
      if (i >= 0)
        str = param1String.substring(i + 1); 
      return str;
    }
    
    public boolean accept(String param1String) {
      if (param1String.endsWith(".class")) {
        param1String = Main.fixPath(param1String);
        String str = getSimpleName(param1String);
        List list = this.map.get(str);
        if (list != null) {
          Iterator<String> iterator = list.iterator();
          while (iterator.hasNext()) {
            if (param1String.endsWith(iterator.next()))
              return true; 
          } 
        } 
        return false;
      } 
      return true;
    }
  }
  
  private static class ClassDefItemConsumer implements Callable<Boolean> {
    Future<ClassDefItem> futureClazz;
    
    int maxFieldIdsInClass;
    
    int maxMethodIdsInClass;
    
    String name;
    
    private ClassDefItemConsumer(String param1String, Future<ClassDefItem> param1Future, int param1Int1, int param1Int2) {
      this.name = param1String;
      this.futureClazz = param1Future;
      this.maxMethodIdsInClass = param1Int1;
      this.maxFieldIdsInClass = param1Int2;
    }
    
    public Boolean call() throws Exception {
      try {
        ClassDefItem classDefItem = this.futureClazz.get();
        if (classDefItem != null) {
          Main.addClassToDex(classDefItem);
          Main.updateStatus(true);
        } 
        Boolean bool = Boolean.valueOf(true);
        if (Main.args.multiDex)
          synchronized (Main.dexRotationLock) {
            Main.access$1802(Main.maxMethodIdsInProcess - this.maxMethodIdsInClass);
            Main.access$2002(Main.maxFieldIdsInProcess - this.maxFieldIdsInClass);
            Main.dexRotationLock.notifyAll();
            return bool;
          }  
        return bool;
      } catch (ExecutionException null) {
        Exception exception;
        Throwable throwable = exception.getCause();
        if (throwable instanceof Exception)
          exception = (Exception)throwable; 
        throw exception;
      } finally {
        Exception exception;
      } 
      if (Main.args.multiDex)
        synchronized (Main.dexRotationLock) {
          Main.access$1802(Main.maxMethodIdsInProcess - this.maxMethodIdsInClass);
          Main.access$2002(Main.maxFieldIdsInProcess - this.maxFieldIdsInClass);
          Main.dexRotationLock.notifyAll();
        }  
      throw SYNTHETIC_LOCAL_VARIABLE_2;
    }
  }
  
  private static class ClassParserTask implements Callable<DirectClassFile> {
    byte[] bytes;
    
    String name;
    
    private ClassParserTask(String param1String, byte[] param1ArrayOfbyte) {
      this.name = param1String;
      this.bytes = param1ArrayOfbyte;
    }
    
    public DirectClassFile call() throws Exception {
      return Main.parseClass(this.name, this.bytes);
    }
  }
  
  private static class ClassTranslatorTask implements Callable<ClassDefItem> {
    byte[] bytes;
    
    DirectClassFile classFile;
    
    String name;
    
    private ClassTranslatorTask(String param1String, byte[] param1ArrayOfbyte, DirectClassFile param1DirectClassFile) {
      this.name = param1String;
      this.bytes = param1ArrayOfbyte;
      this.classFile = param1DirectClassFile;
    }
    
    public ClassDefItem call() {
      return Main.translateClass(this.bytes, this.classFile);
    }
  }
  
  private static class DexWriter implements Callable<byte[]> {
    private DexFile dexFile;
    
    private DexWriter(DexFile param1DexFile) {
      this.dexFile = param1DexFile;
    }
    
    public byte[] call() throws IOException {
      return Main.writeDex(this.dexFile);
    }
  }
  
  private static class DirectClassFileConsumer implements Callable<Boolean> {
    byte[] bytes;
    
    Future<DirectClassFile> dcff;
    
    String name;
    
    private DirectClassFileConsumer(String param1String, byte[] param1ArrayOfbyte, Future<DirectClassFile> param1Future) {
      this.name = param1String;
      this.bytes = param1ArrayOfbyte;
      this.dcff = param1Future;
    }
    
    private Boolean call(DirectClassFile param1DirectClassFile) {
      // Byte code:
      //   0: invokestatic access$1300 : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$Arguments;
      //   3: getfield multiDex : Z
      //   6: ifeq -> 274
      //   9: aload_1
      //   10: invokevirtual getConstantPool : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/cst/ConstantPool;
      //   13: invokeinterface size : ()I
      //   18: istore_2
      //   19: aload_1
      //   20: invokevirtual getMethods : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/iface/MethodList;
      //   23: invokeinterface size : ()I
      //   28: iload_2
      //   29: iadd
      //   30: iconst_2
      //   31: iadd
      //   32: istore #4
      //   34: iload_2
      //   35: aload_1
      //   36: invokevirtual getFields : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/iface/FieldList;
      //   39: invokeinterface size : ()I
      //   44: iadd
      //   45: bipush #9
      //   47: iadd
      //   48: istore #5
      //   50: invokestatic access$1600 : ()Ljava/lang/Object;
      //   53: astore #6
      //   55: aload #6
      //   57: monitorenter
      //   58: invokestatic access$1700 : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/dex/file/DexFile;
      //   61: astore #7
      //   63: aload #7
      //   65: monitorenter
      //   66: invokestatic access$1700 : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/dex/file/DexFile;
      //   69: invokevirtual getMethodIds : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/dex/file/MethodIdsSection;
      //   72: invokevirtual items : ()Ljava/util/Collection;
      //   75: invokeinterface size : ()I
      //   80: istore_2
      //   81: invokestatic access$1700 : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/dex/file/DexFile;
      //   84: invokevirtual getFieldIds : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/dex/file/FieldIdsSection;
      //   87: invokevirtual items : ()Ljava/util/Collection;
      //   90: invokeinterface size : ()I
      //   95: istore_3
      //   96: aload #7
      //   98: monitorexit
      //   99: iload_2
      //   100: iload #4
      //   102: iadd
      //   103: invokestatic access$1800 : ()I
      //   106: iadd
      //   107: invokestatic access$1300 : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$Arguments;
      //   110: invokestatic access$1900 : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$Arguments;)I
      //   113: if_icmpgt -> 133
      //   116: iload_3
      //   117: iload #5
      //   119: iadd
      //   120: invokestatic access$2000 : ()I
      //   123: iadd
      //   124: invokestatic access$1300 : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$Arguments;
      //   127: invokestatic access$1900 : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$Arguments;)I
      //   130: if_icmple -> 171
      //   133: invokestatic access$1800 : ()I
      //   136: ifgt -> 206
      //   139: invokestatic access$2000 : ()I
      //   142: ifle -> 148
      //   145: goto -> 206
      //   148: invokestatic access$1700 : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/dex/file/DexFile;
      //   151: invokevirtual getClassDefs : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/dex/file/ClassDefsSection;
      //   154: invokevirtual items : ()Ljava/util/Collection;
      //   157: invokeinterface size : ()I
      //   162: ifle -> 171
      //   165: invokestatic access$2100 : ()V
      //   168: goto -> 212
      //   171: invokestatic access$1800 : ()I
      //   174: iload #4
      //   176: iadd
      //   177: invokestatic access$1802 : (I)I
      //   180: pop
      //   181: invokestatic access$2000 : ()I
      //   184: iload #5
      //   186: iadd
      //   187: invokestatic access$2002 : (I)I
      //   190: pop
      //   191: aload #6
      //   193: monitorexit
      //   194: iload #5
      //   196: istore_2
      //   197: iload #4
      //   199: istore_3
      //   200: iload_2
      //   201: istore #4
      //   203: goto -> 281
      //   206: invokestatic access$1600 : ()Ljava/lang/Object;
      //   209: invokevirtual wait : ()V
      //   212: invokestatic access$1700 : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/dex/file/DexFile;
      //   215: astore #7
      //   217: aload #7
      //   219: monitorenter
      //   220: invokestatic access$1700 : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/dex/file/DexFile;
      //   223: invokevirtual getMethodIds : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/dex/file/MethodIdsSection;
      //   226: invokevirtual items : ()Ljava/util/Collection;
      //   229: invokeinterface size : ()I
      //   234: istore_2
      //   235: invokestatic access$1700 : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/dex/file/DexFile;
      //   238: invokevirtual getFieldIds : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/dex/file/FieldIdsSection;
      //   241: invokevirtual items : ()Ljava/util/Collection;
      //   244: invokeinterface size : ()I
      //   249: istore_3
      //   250: aload #7
      //   252: monitorexit
      //   253: goto -> 99
      //   256: astore_1
      //   257: aload #7
      //   259: monitorexit
      //   260: aload_1
      //   261: athrow
      //   262: astore_1
      //   263: aload #7
      //   265: monitorexit
      //   266: aload_1
      //   267: athrow
      //   268: astore_1
      //   269: aload #6
      //   271: monitorexit
      //   272: aload_1
      //   273: athrow
      //   274: iconst_0
      //   275: istore_2
      //   276: iload_2
      //   277: istore #4
      //   279: iload_2
      //   280: istore_3
      //   281: invokestatic access$2300 : ()Ljava/util/concurrent/ExecutorService;
      //   284: new org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$ClassTranslatorTask
      //   287: dup
      //   288: aload_0
      //   289: getfield name : Ljava/lang/String;
      //   292: aload_0
      //   293: getfield bytes : [B
      //   296: aload_1
      //   297: aconst_null
      //   298: invokespecial <init> : (Ljava/lang/String;[BLorg/firstinspires/ftc/robotcore/internal/android/dx/cf/direct/DirectClassFile;Lorg/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$1;)V
      //   301: invokeinterface submit : (Ljava/util/concurrent/Callable;)Ljava/util/concurrent/Future;
      //   306: astore_1
      //   307: invokestatic access$2500 : ()Ljava/util/concurrent/ExecutorService;
      //   310: new org/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$ClassDefItemConsumer
      //   313: dup
      //   314: aload_0
      //   315: getfield name : Ljava/lang/String;
      //   318: aload_1
      //   319: iload_3
      //   320: iload #4
      //   322: aconst_null
      //   323: invokespecial <init> : (Ljava/lang/String;Ljava/util/concurrent/Future;IILorg/firstinspires/ftc/robotcore/internal/android/dx/command/dexer/Main$1;)V
      //   326: invokeinterface submit : (Ljava/util/concurrent/Callable;)Ljava/util/concurrent/Future;
      //   331: astore_1
      //   332: invokestatic access$2600 : ()Ljava/util/List;
      //   335: aload_1
      //   336: invokeinterface add : (Ljava/lang/Object;)Z
      //   341: pop
      //   342: iconst_1
      //   343: invokestatic valueOf : (Z)Ljava/lang/Boolean;
      //   346: areturn
      //   347: astore #7
      //   349: goto -> 212
      // Exception table:
      //   from	to	target	type
      //   58	66	268	finally
      //   66	99	262	finally
      //   99	133	268	finally
      //   133	145	268	finally
      //   148	168	268	finally
      //   171	194	268	finally
      //   206	212	347	java/lang/InterruptedException
      //   206	212	268	finally
      //   212	220	268	finally
      //   220	253	256	finally
      //   257	260	256	finally
      //   260	262	268	finally
      //   263	266	262	finally
      //   266	268	268	finally
      //   269	272	268	finally
    }
    
    public Boolean call() throws Exception {
      return call(this.dcff.get());
    }
  }
  
  private static class FileBytesConsumer implements ClassPathOpener.Consumer {
    private FileBytesConsumer() {}
    
    public void onException(Exception param1Exception) {
      ParseException parseException;
      if (!(param1Exception instanceof Main.StopProcessing)) {
        if (param1Exception instanceof SimException) {
          DxConsole.err.println("\nEXCEPTION FROM SIMULATION:");
          PrintStream printStream = DxConsole.err;
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(param1Exception.getMessage());
          stringBuilder.append("\n");
          printStream.println(stringBuilder.toString());
          DxConsole.err.println(((SimException)param1Exception).getContext());
        } else if (param1Exception instanceof ParseException) {
          DxConsole.err.println("\nPARSE ERROR:");
          parseException = (ParseException)param1Exception;
          if (Main.args.debug) {
            parseException.printStackTrace(DxConsole.err);
          } else {
            parseException.printContext(DxConsole.err);
          } 
        } else {
          DxConsole.err.println("\nUNEXPECTED TOP-LEVEL EXCEPTION:");
          parseException.printStackTrace(DxConsole.err);
        } 
        Main.errors.incrementAndGet();
        return;
      } 
      throw (Main.StopProcessing)parseException;
    }
    
    public void onProcessArchiveStart(File param1File) {
      if (Main.args.verbose) {
        PrintStream printStream = DxConsole.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("processing archive ");
        stringBuilder.append(param1File);
        stringBuilder.append("...");
        printStream.println(stringBuilder.toString());
      } 
    }
    
    public boolean processFileBytes(String param1String, long param1Long, byte[] param1ArrayOfbyte) {
      return Main.processFileBytes(param1String, param1Long, param1ArrayOfbyte);
    }
  }
  
  private static class MainDexListFilter implements ClassPathOpener.FileNameFilter {
    private MainDexListFilter() {}
    
    public boolean accept(String param1String) {
      if (param1String.endsWith(".class")) {
        param1String = Main.fixPath(param1String);
        return Main.classesInMainDex.contains(param1String);
      } 
      return true;
    }
  }
  
  private static class NotFilter implements ClassPathOpener.FileNameFilter {
    private final ClassPathOpener.FileNameFilter filter;
    
    private NotFilter(ClassPathOpener.FileNameFilter param1FileNameFilter) {
      this.filter = param1FileNameFilter;
    }
    
    public boolean accept(String param1String) {
      return this.filter.accept(param1String) ^ true;
    }
  }
  
  private static class StopProcessing extends RuntimeException {
    private StopProcessing() {}
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\command\dexer\Main.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
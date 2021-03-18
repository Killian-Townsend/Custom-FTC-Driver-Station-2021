package org.firstinspires.ftc.robotcore.internal.android.multidex;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct.DirectClassFile;

class Path {
  private final ByteArrayOutputStream baos = new ByteArrayOutputStream(40960);
  
  private final String definition;
  
  List<ClassPathElement> elements = new ArrayList<ClassPathElement>();
  
  private final byte[] readBuffer = new byte[20480];
  
  Path(String paramString) throws IOException {
    this.definition = paramString;
    String[] arrayOfString = paramString.split(Pattern.quote(File.pathSeparator));
    int j = arrayOfString.length;
    int i = 0;
    while (i < j) {
      String str = arrayOfString[i];
      try {
        addElement(getClassPathElement(new File(str)));
        i++;
      } catch (IOException iOException) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Wrong classpath: ");
        stringBuilder.append(iOException.getMessage());
        throw new IOException(stringBuilder.toString(), iOException);
      } 
    } 
  }
  
  private void addElement(ClassPathElement paramClassPathElement) {
    this.elements.add(paramClassPathElement);
  }
  
  static ClassPathElement getClassPathElement(File paramFile) throws ZipException, IOException {
    if (paramFile.isDirectory())
      return new FolderPathElement(paramFile); 
    if (paramFile.isFile())
      return new ArchivePathElement(new ZipFile(paramFile)); 
    if (paramFile.exists()) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("\"");
      stringBuilder1.append(paramFile.getPath());
      stringBuilder1.append("\" is not a directory neither a zip file");
      throw new IOException(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("File \"");
    stringBuilder.append(paramFile.getPath());
    stringBuilder.append("\" not found");
    throw new FileNotFoundException(stringBuilder.toString());
  }
  
  private static byte[] readStream(InputStream paramInputStream, ByteArrayOutputStream paramByteArrayOutputStream, byte[] paramArrayOfbyte) throws IOException {
    try {
      while (true) {
        int i = paramInputStream.read(paramArrayOfbyte);
        if (i < 0)
          return paramByteArrayOutputStream.toByteArray(); 
        paramByteArrayOutputStream.write(paramArrayOfbyte, 0, i);
      } 
    } finally {
      paramInputStream.close();
    } 
  }
  
  DirectClassFile getClass(String paramString) throws FileNotFoundException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aconst_null
    //   3: astore_2
    //   4: aload_0
    //   5: getfield elements : Ljava/util/List;
    //   8: invokeinterface iterator : ()Ljava/util/Iterator;
    //   13: astore #5
    //   15: aload_2
    //   16: astore_3
    //   17: aload #5
    //   19: invokeinterface hasNext : ()Z
    //   24: ifeq -> 111
    //   27: aload #5
    //   29: invokeinterface next : ()Ljava/lang/Object;
    //   34: checkcast org/firstinspires/ftc/robotcore/internal/android/multidex/ClassPathElement
    //   37: astore_3
    //   38: aload_3
    //   39: aload_1
    //   40: invokeinterface open : (Ljava/lang/String;)Ljava/io/InputStream;
    //   45: astore #6
    //   47: aload #6
    //   49: aload_0
    //   50: getfield baos : Ljava/io/ByteArrayOutputStream;
    //   53: aload_0
    //   54: getfield readBuffer : [B
    //   57: invokestatic readStream : (Ljava/io/InputStream;Ljava/io/ByteArrayOutputStream;[B)[B
    //   60: astore_3
    //   61: aload_0
    //   62: getfield baos : Ljava/io/ByteArrayOutputStream;
    //   65: invokevirtual reset : ()V
    //   68: new org/firstinspires/ftc/robotcore/internal/android/dx/cf/direct/DirectClassFile
    //   71: dup
    //   72: aload_3
    //   73: aload_1
    //   74: iconst_0
    //   75: invokespecial <init> : ([BLjava/lang/String;Z)V
    //   78: astore_3
    //   79: aload_3
    //   80: getstatic org/firstinspires/ftc/robotcore/internal/android/dx/cf/direct/StdAttributeFactory.THE_ONE : Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/direct/StdAttributeFactory;
    //   83: invokevirtual setAttributeFactory : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/cf/direct/AttributeFactory;)V
    //   86: aload_3
    //   87: astore_2
    //   88: aload #6
    //   90: invokevirtual close : ()V
    //   93: goto -> 111
    //   96: aload_3
    //   97: astore_2
    //   98: aload #6
    //   100: invokevirtual close : ()V
    //   103: aload_3
    //   104: astore_2
    //   105: aload #4
    //   107: athrow
    //   108: goto -> 15
    //   111: aload_3
    //   112: ifnull -> 119
    //   115: aload_0
    //   116: monitorexit
    //   117: aload_3
    //   118: areturn
    //   119: new java/lang/StringBuilder
    //   122: dup
    //   123: invokespecial <init> : ()V
    //   126: astore_2
    //   127: aload_2
    //   128: ldc 'File "'
    //   130: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   133: pop
    //   134: aload_2
    //   135: aload_1
    //   136: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   139: pop
    //   140: aload_2
    //   141: ldc '" not found'
    //   143: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   146: pop
    //   147: new java/io/FileNotFoundException
    //   150: dup
    //   151: aload_2
    //   152: invokevirtual toString : ()Ljava/lang/String;
    //   155: invokespecial <init> : (Ljava/lang/String;)V
    //   158: athrow
    //   159: astore_1
    //   160: aload_0
    //   161: monitorexit
    //   162: aload_1
    //   163: athrow
    //   164: astore_3
    //   165: goto -> 15
    //   168: astore_3
    //   169: goto -> 108
    //   172: astore #4
    //   174: goto -> 96
    //   177: astore #4
    //   179: aload_2
    //   180: astore_3
    //   181: goto -> 96
    // Exception table:
    //   from	to	target	type
    //   4	15	159	finally
    //   17	38	159	finally
    //   38	47	164	java/io/IOException
    //   38	47	159	finally
    //   47	79	177	finally
    //   79	86	172	finally
    //   88	93	168	java/io/IOException
    //   88	93	159	finally
    //   98	103	168	java/io/IOException
    //   98	103	159	finally
    //   105	108	168	java/io/IOException
    //   105	108	159	finally
    //   119	159	159	finally
  }
  
  Iterable<ClassPathElement> getElements() {
    return this.elements;
  }
  
  public String toString() {
    return this.definition;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\multidex\Path.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
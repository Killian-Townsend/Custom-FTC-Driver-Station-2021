package org.firstinspires.ftc.robotcore.internal.android.multidex;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttRuntimeVisibleAnnotations;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct.DirectClassFile;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.Attribute;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.FieldList;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.HasAttribute;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.MethodList;

public class MainDexListBuilder {
  private static final String CLASS_EXTENSION = ".class";
  
  private static final String DISABLE_ANNOTATION_RESOLUTION_WORKAROUND = "--disable-annotation-resolution-workaround";
  
  private static final String EOL = System.getProperty("line.separator");
  
  private static final int STATUS_ERROR = 1;
  
  private static String USAGE_MESSAGE;
  
  private Set<String> filesToKeep;
  
  static {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Usage:");
    stringBuilder.append(EOL);
    stringBuilder.append(EOL);
    stringBuilder.append("Short version: Don't use this.");
    stringBuilder.append(EOL);
    stringBuilder.append(EOL);
    stringBuilder.append("Slightly longer version: This tool is used by mainDexClasses script to build");
    stringBuilder.append(EOL);
    stringBuilder.append("the main dex list.");
    stringBuilder.append(EOL);
    USAGE_MESSAGE = stringBuilder.toString();
  }
  
  public MainDexListBuilder(boolean paramBoolean, String paramString1, String paramString2) throws IOException {
    String str1;
    this.filesToKeep = new HashSet<String>();
    str2 = null;
    try {
    
    } catch (IOException iOException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("\"");
      stringBuilder.append(paramString1);
      stringBuilder.append("\" can not be read as a zip archive. (");
      stringBuilder.append(iOException.getMessage());
      stringBuilder.append(")");
      throw new IOException(stringBuilder.toString(), iOException);
    } finally {
      paramString2 = null;
      paramString1 = null;
    } 
    try {
      str1.close();
    } catch (IOException iOException) {}
    if (paramString1 != null) {
      Iterator<ClassPathElement> iterator = ((Path)paramString1).elements.iterator();
      while (true) {
        if (iterator.hasNext()) {
          ClassPathElement classPathElement = iterator.next();
          try {
            classPathElement.close();
          } catch (IOException iOException) {}
          continue;
        } 
        throw paramString2;
      } 
    } 
    throw paramString2;
  }
  
  private boolean hasRuntimeVisibleAnnotation(HasAttribute paramHasAttribute) {
    Attribute attribute = paramHasAttribute.getAttributes().findFirst("RuntimeVisibleAnnotations");
    return (attribute != null && ((AttRuntimeVisibleAnnotations)attribute).getAnnotations().size() > 0);
  }
  
  private void keepAnnotated(Path paramPath) throws FileNotFoundException {
    Iterator<ClassPathElement> iterator = paramPath.getElements().iterator();
    while (iterator.hasNext()) {
      label29: for (String str : ((ClassPathElement)iterator.next()).list()) {
        if (str.endsWith(".class")) {
          DirectClassFile directClassFile = paramPath.getClass(str);
          if (hasRuntimeVisibleAnnotation((HasAttribute)directClassFile)) {
            this.filesToKeep.add(str);
            continue;
          } 
          MethodList methodList = directClassFile.getMethods();
          boolean bool = false;
          int i;
          for (i = 0; i < methodList.size(); i++) {
            if (hasRuntimeVisibleAnnotation((HasAttribute)methodList.get(i))) {
              this.filesToKeep.add(str);
              continue label29;
            } 
          } 
          FieldList fieldList = directClassFile.getFields();
          for (i = bool; i < fieldList.size(); i++) {
            if (hasRuntimeVisibleAnnotation((HasAttribute)fieldList.get(i))) {
              this.filesToKeep.add(str);
              break;
            } 
          } 
        } 
      } 
    } 
  }
  
  public static void main(String[] paramArrayOfString) {
    int i = 0;
    boolean bool = true;
    while (i < paramArrayOfString.length - 2) {
      if (paramArrayOfString[i].equals("--disable-annotation-resolution-workaround")) {
        bool = false;
      } else {
        PrintStream printStream = System.err;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid option ");
        stringBuilder.append(paramArrayOfString[i]);
        printStream.println(stringBuilder.toString());
        printUsage();
        System.exit(1);
      } 
      i++;
    } 
    if (paramArrayOfString.length - i != 2) {
      printUsage();
      System.exit(1);
    } 
    try {
      printList((new MainDexListBuilder(bool, paramArrayOfString[i], paramArrayOfString[i + 1])).getMainDexList());
      return;
    } catch (IOException iOException) {
      PrintStream printStream = System.err;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("A fatal error occured: ");
      stringBuilder.append(iOException.getMessage());
      printStream.println(stringBuilder.toString());
      System.exit(1);
      return;
    } 
  }
  
  private static void printList(Set<String> paramSet) {
    for (String str : paramSet)
      System.out.println(str); 
  }
  
  private static void printUsage() {
    System.err.print(USAGE_MESSAGE);
  }
  
  public Set<String> getMainDexList() {
    return this.filesToKeep;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\multidex\MainDexListBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
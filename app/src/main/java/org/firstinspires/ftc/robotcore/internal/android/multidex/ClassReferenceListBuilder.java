package org.firstinspires.ftc.robotcore.internal.android.multidex;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct.DirectClassFile;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.FieldList;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.MethodList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstBaseMethodRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstFieldRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Prototype;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.StdTypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeList;

public class ClassReferenceListBuilder {
  private static final String CLASS_EXTENSION = ".class";
  
  private final Set<String> classNames = new HashSet<String>();
  
  private final Path path;
  
  public ClassReferenceListBuilder(Path paramPath) {
    this.path = paramPath;
  }
  
  private void addClassWithHierachy(String paramString) {
    if (this.classNames.contains(paramString))
      return; 
    try {
      Path path = this.path;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramString);
      stringBuilder.append(".class");
      DirectClassFile directClassFile = path.getClass(stringBuilder.toString());
      this.classNames.add(paramString);
      CstType cstType = directClassFile.getSuperclass();
      if (cstType != null)
        addClassWithHierachy(cstType.getClassType().getClassName()); 
      TypeList typeList = directClassFile.getInterfaces();
      int j = typeList.size();
      for (int i = 0; i < j; i++)
        addClassWithHierachy(typeList.getType(i).getClassName()); 
      return;
    } catch (FileNotFoundException fileNotFoundException) {
      return;
    } 
  }
  
  private void addDependencies(DirectClassFile paramDirectClassFile) {
    Constant[] arrayOfConstant = paramDirectClassFile.getConstantPool().getEntries();
    int j = arrayOfConstant.length;
    boolean bool = false;
    int i;
    for (i = 0; i < j; i++) {
      Constant constant = arrayOfConstant[i];
      if (constant instanceof CstType) {
        checkDescriptor(((CstType)constant).getClassType().getDescriptor());
      } else if (constant instanceof CstFieldRef) {
        checkDescriptor(((CstFieldRef)constant).getType().getDescriptor());
      } else if (constant instanceof CstBaseMethodRef) {
        checkPrototype(((CstBaseMethodRef)constant).getPrototype());
      } 
    } 
    FieldList fieldList = paramDirectClassFile.getFields();
    j = fieldList.size();
    for (i = 0; i < j; i++)
      checkDescriptor(fieldList.get(i).getDescriptor().getString()); 
    MethodList methodList = paramDirectClassFile.getMethods();
    j = methodList.size();
    for (i = bool; i < j; i++)
      checkPrototype(Prototype.intern(methodList.get(i).getDescriptor().getString())); 
  }
  
  private void checkDescriptor(String paramString) {
    if (paramString.endsWith(";")) {
      int i = paramString.lastIndexOf('[');
      if (i < 0) {
        addClassWithHierachy(paramString.substring(1, paramString.length() - 1));
        return;
      } 
      addClassWithHierachy(paramString.substring(i + 2, paramString.length() - 1));
    } 
  }
  
  private void checkPrototype(Prototype paramPrototype) {
    checkDescriptor(paramPrototype.getReturnType().getDescriptor());
    StdTypeList stdTypeList = paramPrototype.getParameterTypes();
    for (int i = 0; i < stdTypeList.size(); i++)
      checkDescriptor(stdTypeList.get(i).getDescriptor()); 
  }
  
  @Deprecated
  public static void main(String[] paramArrayOfString) {
    MainDexListBuilder.main(paramArrayOfString);
  }
  
  public void addRoots(ZipFile paramZipFile) throws IOException {
    Enumeration<? extends ZipEntry> enumeration = paramZipFile.entries();
    while (enumeration.hasMoreElements()) {
      String str = ((ZipEntry)enumeration.nextElement()).getName();
      if (str.endsWith(".class"))
        this.classNames.add(str.substring(0, str.length() - 6)); 
    } 
    enumeration = paramZipFile.entries();
    while (enumeration.hasMoreElements()) {
      String str = ((ZipEntry)enumeration.nextElement()).getName();
      if (str.endsWith(".class"))
        try {
          DirectClassFile directClassFile = this.path.getClass(str);
          addDependencies(directClassFile);
        } catch (FileNotFoundException fileNotFoundException) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Class ");
          stringBuilder.append(str);
          stringBuilder.append(" is missing form original class path ");
          stringBuilder.append(this.path);
          throw new IOException(stringBuilder.toString(), fileNotFoundException);
        }  
    } 
  }
  
  Set<String> getClassNames() {
    return this.classNames;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\multidex\ClassReferenceListBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.android.dx.command.findusages;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.firstinspires.ftc.robotcore.internal.android.dex.ClassData;
import org.firstinspires.ftc.robotcore.internal.android.dex.ClassDef;
import org.firstinspires.ftc.robotcore.internal.android.dex.Dex;
import org.firstinspires.ftc.robotcore.internal.android.dex.FieldId;
import org.firstinspires.ftc.robotcore.internal.android.dex.MethodId;
import org.firstinspires.ftc.robotcore.internal.android.dx.io.CodeReader;
import org.firstinspires.ftc.robotcore.internal.android.dx.io.OpcodeInfo;
import org.firstinspires.ftc.robotcore.internal.android.dx.io.instructions.DecodedInstruction;

public final class FindUsages {
  private final CodeReader codeReader = new CodeReader();
  
  private ClassDef currentClass;
  
  private ClassData.Method currentMethod;
  
  private final Dex dex;
  
  private final Set<Integer> fieldIds;
  
  private final Set<Integer> methodIds;
  
  private final PrintWriter out;
  
  public FindUsages(final Dex dex, String paramString1, String paramString2, final PrintWriter out) {
    this.dex = dex;
    this.out = out;
    HashSet<Integer> hashSet2 = new HashSet();
    HashSet<Integer> hashSet1 = new HashSet();
    Pattern pattern1 = Pattern.compile(paramString1);
    Pattern pattern2 = Pattern.compile(paramString2);
    List<String> list = dex.strings();
    int i;
    for (i = 0; i < list.size(); i++) {
      String str = list.get(i);
      if (pattern1.matcher(str).matches())
        hashSet2.add(Integer.valueOf(i)); 
      if (pattern2.matcher(str).matches())
        hashSet1.add(Integer.valueOf(i)); 
    } 
    if (hashSet2.isEmpty() || hashSet1.isEmpty()) {
      this.fieldIds = null;
      this.methodIds = null;
      return;
    } 
    this.methodIds = new HashSet<Integer>();
    this.fieldIds = new HashSet<Integer>();
    Iterator<Integer> iterator = hashSet2.iterator();
    while (iterator.hasNext()) {
      i = ((Integer)iterator.next()).intValue();
      i = Collections.binarySearch(dex.typeIds(), Integer.valueOf(i));
      if (i < 0)
        continue; 
      this.methodIds.addAll(getMethodIds(dex, hashSet1, i));
      this.fieldIds.addAll(getFieldIds(dex, hashSet1, i));
    } 
    this.codeReader.setFieldVisitor(new CodeReader.Visitor() {
          public void visit(DecodedInstruction[] param1ArrayOfDecodedInstruction, DecodedInstruction param1DecodedInstruction) {
            int i = param1DecodedInstruction.getIndex();
            if (FindUsages.this.fieldIds.contains(Integer.valueOf(i))) {
              PrintWriter printWriter = out;
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append(FindUsages.this.location());
              stringBuilder.append(": field reference ");
              stringBuilder.append(dex.fieldIds().get(i));
              stringBuilder.append(" (");
              stringBuilder.append(OpcodeInfo.getName(param1DecodedInstruction.getOpcode()));
              stringBuilder.append(")");
              printWriter.println(stringBuilder.toString());
            } 
          }
        });
    this.codeReader.setMethodVisitor(new CodeReader.Visitor() {
          public void visit(DecodedInstruction[] param1ArrayOfDecodedInstruction, DecodedInstruction param1DecodedInstruction) {
            int i = param1DecodedInstruction.getIndex();
            if (FindUsages.this.methodIds.contains(Integer.valueOf(i))) {
              PrintWriter printWriter = out;
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append(FindUsages.this.location());
              stringBuilder.append(": method reference ");
              stringBuilder.append(dex.methodIds().get(i));
              stringBuilder.append(" (");
              stringBuilder.append(OpcodeInfo.getName(param1DecodedInstruction.getOpcode()));
              stringBuilder.append(")");
              printWriter.println(stringBuilder.toString());
            } 
          }
        });
  }
  
  private Set<Integer> findAssignableTypes(Dex paramDex, int paramInt) {
    HashSet<Integer> hashSet = new HashSet();
    hashSet.add(Integer.valueOf(paramInt));
    for (ClassDef classDef : paramDex.classDefs()) {
      if (hashSet.contains(Integer.valueOf(classDef.getSupertypeIndex()))) {
        hashSet.add(Integer.valueOf(classDef.getTypeIndex()));
        continue;
      } 
      short[] arrayOfShort = classDef.getInterfaces();
      int i = arrayOfShort.length;
      for (paramInt = 0; paramInt < i; paramInt++) {
        if (hashSet.contains(Integer.valueOf(arrayOfShort[paramInt]))) {
          hashSet.add(Integer.valueOf(classDef.getTypeIndex()));
          break;
        } 
      } 
    } 
    return hashSet;
  }
  
  private Set<Integer> getFieldIds(Dex paramDex, Set<Integer> paramSet, int paramInt) {
    HashSet<Integer> hashSet = new HashSet();
    Iterator<FieldId> iterator = paramDex.fieldIds().iterator();
    int i;
    for (i = 0; iterator.hasNext(); i++) {
      FieldId fieldId = iterator.next();
      if (paramSet.contains(Integer.valueOf(fieldId.getNameIndex())) && paramInt == fieldId.getDeclaringClassIndex())
        hashSet.add(Integer.valueOf(i)); 
    } 
    return hashSet;
  }
  
  private Set<Integer> getMethodIds(Dex paramDex, Set<Integer> paramSet, int paramInt) {
    Set<Integer> set = findAssignableTypes(paramDex, paramInt);
    HashSet<Integer> hashSet = new HashSet();
    Iterator<MethodId> iterator = paramDex.methodIds().iterator();
    for (paramInt = 0; iterator.hasNext(); paramInt++) {
      MethodId methodId = iterator.next();
      if (paramSet.contains(Integer.valueOf(methodId.getNameIndex())) && set.contains(Integer.valueOf(methodId.getDeclaringClassIndex())))
        hashSet.add(Integer.valueOf(paramInt)); 
    } 
    return hashSet;
  }
  
  private String location() {
    String str2 = this.dex.typeNames().get(this.currentClass.getTypeIndex());
    String str1 = str2;
    if (this.currentMethod != null) {
      MethodId methodId = this.dex.methodIds().get(this.currentMethod.getMethodIndex());
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str2);
      stringBuilder.append(".");
      stringBuilder.append(this.dex.strings().get(methodId.getNameIndex()));
      str1 = stringBuilder.toString();
    } 
    return str1;
  }
  
  public void findUsages() {
    if (this.fieldIds != null) {
      if (this.methodIds == null)
        return; 
      for (ClassDef classDef : this.dex.classDefs()) {
        this.currentClass = classDef;
        this.currentMethod = null;
        if (classDef.getClassDataOffset() == 0)
          continue; 
        ClassData classData = this.dex.readClassData(classDef);
        ClassData.Field[] arrayOfField = classData.allFields();
        int k = arrayOfField.length;
        int j = 0;
        int i;
        for (i = 0; i < k; i++) {
          int m = arrayOfField[i].getFieldIndex();
          if (this.fieldIds.contains(Integer.valueOf(m))) {
            PrintWriter printWriter = this.out;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(location());
            stringBuilder.append(" field declared ");
            stringBuilder.append(this.dex.fieldIds().get(m));
            printWriter.println(stringBuilder.toString());
          } 
        } 
        ClassData.Method[] arrayOfMethod = classData.allMethods();
        k = arrayOfMethod.length;
        for (i = j; i < k; i++) {
          ClassData.Method method = arrayOfMethod[i];
          this.currentMethod = method;
          j = method.getMethodIndex();
          if (this.methodIds.contains(Integer.valueOf(j))) {
            PrintWriter printWriter = this.out;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(location());
            stringBuilder.append(" method declared ");
            stringBuilder.append(this.dex.methodIds().get(j));
            printWriter.println(stringBuilder.toString());
          } 
          if (method.getCodeOffset() != 0)
            this.codeReader.visitAll(this.dex.readCode(method).getInstructions()); 
        } 
      } 
      this.currentClass = null;
      this.currentMethod = null;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\command\findusages\FindUsages.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
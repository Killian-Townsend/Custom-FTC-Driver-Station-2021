package org.firstinspires.ftc.robotcore.internal.android.dx.command.grep;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;
import org.firstinspires.ftc.robotcore.internal.android.dex.ClassData;
import org.firstinspires.ftc.robotcore.internal.android.dex.ClassDef;
import org.firstinspires.ftc.robotcore.internal.android.dex.Dex;
import org.firstinspires.ftc.robotcore.internal.android.dex.EncodedValueReader;
import org.firstinspires.ftc.robotcore.internal.android.dex.MethodId;
import org.firstinspires.ftc.robotcore.internal.android.dex.util.ByteInput;
import org.firstinspires.ftc.robotcore.internal.android.dx.io.CodeReader;
import org.firstinspires.ftc.robotcore.internal.android.dx.io.instructions.DecodedInstruction;

public final class Grep {
  private final CodeReader codeReader = new CodeReader();
  
  private int count = 0;
  
  private ClassDef currentClass;
  
  private ClassData.Method currentMethod;
  
  private final Dex dex;
  
  private final PrintWriter out;
  
  private final Set<Integer> stringIds;
  
  public Grep(Dex paramDex, Pattern paramPattern, PrintWriter paramPrintWriter) {
    this.dex = paramDex;
    this.out = paramPrintWriter;
    this.stringIds = getStringIds(paramDex, paramPattern);
    this.codeReader.setStringVisitor(new CodeReader.Visitor() {
          public void visit(DecodedInstruction[] param1ArrayOfDecodedInstruction, DecodedInstruction param1DecodedInstruction) {
            Grep.this.encounterString(param1DecodedInstruction.getIndex());
          }
        });
  }
  
  private void encounterString(int paramInt) {
    if (this.stringIds.contains(Integer.valueOf(paramInt))) {
      PrintWriter printWriter = this.out;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(location());
      stringBuilder.append(" ");
      stringBuilder.append(this.dex.strings().get(paramInt));
      printWriter.println(stringBuilder.toString());
      this.count++;
    } 
  }
  
  private Set<Integer> getStringIds(Dex paramDex, Pattern paramPattern) {
    HashSet<Integer> hashSet = new HashSet();
    Iterator<String> iterator = paramDex.strings().iterator();
    for (int i = 0; iterator.hasNext(); i++) {
      if (paramPattern.matcher(iterator.next()).find())
        hashSet.add(Integer.valueOf(i)); 
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
  
  private void readArray(EncodedValueReader paramEncodedValueReader) {
    int j = paramEncodedValueReader.readArray();
    for (int i = 0; i < j; i++) {
      int k = paramEncodedValueReader.peek();
      if (k != 23) {
        if (k == 28)
          readArray(paramEncodedValueReader); 
      } else {
        encounterString(paramEncodedValueReader.readString());
      } 
    } 
  }
  
  public int grep() {
    for (ClassDef classDef : this.dex.classDefs()) {
      this.currentClass = classDef;
      this.currentMethod = null;
      if (classDef.getClassDataOffset() == 0)
        continue; 
      ClassData classData = this.dex.readClassData(classDef);
      null = classDef.getStaticValuesOffset();
      if (null != 0)
        readArray(new EncodedValueReader((ByteInput)this.dex.open(null))); 
      for (ClassData.Method method : classData.allMethods()) {
        this.currentMethod = method;
        if (method.getCodeOffset() != 0)
          this.codeReader.visitAll(this.dex.readCode(method).getInstructions()); 
      } 
    } 
    this.currentClass = null;
    this.currentMethod = null;
    return this.count;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\command\grep\Grep.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
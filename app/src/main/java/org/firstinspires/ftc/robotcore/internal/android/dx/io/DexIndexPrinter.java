package org.firstinspires.ftc.robotcore.internal.android.dx.io;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import org.firstinspires.ftc.robotcore.internal.android.dex.ClassDef;
import org.firstinspires.ftc.robotcore.internal.android.dex.Dex;
import org.firstinspires.ftc.robotcore.internal.android.dex.FieldId;
import org.firstinspires.ftc.robotcore.internal.android.dex.MethodId;
import org.firstinspires.ftc.robotcore.internal.android.dex.ProtoId;
import org.firstinspires.ftc.robotcore.internal.android.dex.TableOfContents;

public final class DexIndexPrinter {
  private final Dex dex;
  
  private final TableOfContents tableOfContents;
  
  public DexIndexPrinter(File paramFile) throws IOException {
    Dex dex = new Dex(paramFile);
    this.dex = dex;
    this.tableOfContents = dex.getTableOfContents();
  }
  
  public static void main(String[] paramArrayOfString) throws IOException {
    DexIndexPrinter dexIndexPrinter = new DexIndexPrinter(new File(paramArrayOfString[0]));
    dexIndexPrinter.printMap();
    dexIndexPrinter.printStrings();
    dexIndexPrinter.printTypeIds();
    dexIndexPrinter.printProtoIds();
    dexIndexPrinter.printFieldIds();
    dexIndexPrinter.printMethodIds();
    dexIndexPrinter.printTypeLists();
    dexIndexPrinter.printClassDefs();
  }
  
  private void printClassDefs() {
    Iterator<ClassDef> iterator = this.dex.classDefs().iterator();
    for (int i = 0; iterator.hasNext(); i++) {
      ClassDef classDef = iterator.next();
      PrintStream printStream = System.out;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("class def ");
      stringBuilder.append(i);
      stringBuilder.append(": ");
      stringBuilder.append(classDef);
      printStream.println(stringBuilder.toString());
    } 
  }
  
  private void printFieldIds() throws IOException {
    Iterator<FieldId> iterator = this.dex.fieldIds().iterator();
    for (int i = 0; iterator.hasNext(); i++) {
      FieldId fieldId = iterator.next();
      PrintStream printStream = System.out;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("field ");
      stringBuilder.append(i);
      stringBuilder.append(": ");
      stringBuilder.append(fieldId);
      printStream.println(stringBuilder.toString());
    } 
  }
  
  private void printMap() {
    for (TableOfContents.Section section : this.tableOfContents.sections) {
      if (section.off != -1) {
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("section ");
        stringBuilder.append(Integer.toHexString(section.type));
        stringBuilder.append(" off=");
        stringBuilder.append(Integer.toHexString(section.off));
        stringBuilder.append(" size=");
        stringBuilder.append(Integer.toHexString(section.size));
        stringBuilder.append(" byteCount=");
        stringBuilder.append(Integer.toHexString(section.byteCount));
        printStream.println(stringBuilder.toString());
      } 
    } 
  }
  
  private void printMethodIds() throws IOException {
    Iterator<MethodId> iterator = this.dex.methodIds().iterator();
    for (int i = 0; iterator.hasNext(); i++) {
      MethodId methodId = iterator.next();
      PrintStream printStream = System.out;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("methodId ");
      stringBuilder.append(i);
      stringBuilder.append(": ");
      stringBuilder.append(methodId);
      printStream.println(stringBuilder.toString());
    } 
  }
  
  private void printProtoIds() throws IOException {
    Iterator<ProtoId> iterator = this.dex.protoIds().iterator();
    for (int i = 0; iterator.hasNext(); i++) {
      ProtoId protoId = iterator.next();
      PrintStream printStream = System.out;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("proto ");
      stringBuilder.append(i);
      stringBuilder.append(": ");
      stringBuilder.append(protoId);
      printStream.println(stringBuilder.toString());
    } 
  }
  
  private void printStrings() throws IOException {
    Iterator<String> iterator = this.dex.strings().iterator();
    for (int i = 0; iterator.hasNext(); i++) {
      String str = iterator.next();
      PrintStream printStream = System.out;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("string ");
      stringBuilder.append(i);
      stringBuilder.append(": ");
      stringBuilder.append(str);
      printStream.println(stringBuilder.toString());
    } 
  }
  
  private void printTypeIds() throws IOException {
    Iterator<Integer> iterator = this.dex.typeIds().iterator();
    for (int i = 0; iterator.hasNext(); i++) {
      Integer integer = iterator.next();
      PrintStream printStream = System.out;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("type ");
      stringBuilder.append(i);
      stringBuilder.append(": ");
      stringBuilder.append(this.dex.strings().get(integer.intValue()));
      printStream.println(stringBuilder.toString());
    } 
  }
  
  private void printTypeLists() throws IOException {
    if (this.tableOfContents.typeLists.off == -1) {
      System.out.println("No type lists");
      return;
    } 
    Dex.Section section = this.dex.open(this.tableOfContents.typeLists.off);
    for (int i = 0; i < this.tableOfContents.typeLists.size; i++) {
      int k = section.readInt();
      PrintStream printStream = System.out;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Type list i=");
      stringBuilder.append(i);
      stringBuilder.append(", size=");
      stringBuilder.append(k);
      stringBuilder.append(", elements=");
      printStream.print(stringBuilder.toString());
      for (int j = 0; j < k; j++) {
        printStream = System.out;
        stringBuilder = new StringBuilder();
        stringBuilder.append(" ");
        stringBuilder.append(this.dex.typeNames().get(section.readShort()));
        printStream.print(stringBuilder.toString());
      } 
      if (k % 2 == 1)
        section.readShort(); 
      System.out.println();
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\io\DexIndexPrinter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.android.dx.command.dump;

import java.io.PrintStream;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.code.ConcreteMethod;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.code.Ropper;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct.AttributeFactory;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct.DirectClassFile;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct.StdAttributeFactory;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.ClassFile;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.Member;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.Method;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.ParseObserver;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.AccessFlags;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.BasicBlock;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.BasicBlockList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.DexTranslationAdvice;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RopMethod;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.TranslationAdvice;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.Optimizer;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.ByteArray;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IntList;

public class DotDumper implements ParseObserver {
  private final Args args;
  
  private final byte[] bytes;
  
  private DirectClassFile classFile;
  
  private final String filePath;
  
  private final boolean optimize;
  
  private final boolean strictParse;
  
  DotDumper(byte[] paramArrayOfbyte, String paramString, Args paramArgs) {
    this.bytes = paramArrayOfbyte;
    this.filePath = paramString;
    this.strictParse = paramArgs.strictParse;
    this.optimize = paramArgs.optimize;
    this.args = paramArgs;
  }
  
  static void dump(byte[] paramArrayOfbyte, String paramString, Args paramArgs) {
    (new DotDumper(paramArrayOfbyte, paramString, paramArgs)).run();
  }
  
  private void run() {
    ByteArray byteArray = new ByteArray(this.bytes);
    DirectClassFile directClassFile2 = new DirectClassFile(byteArray, this.filePath, this.strictParse);
    this.classFile = directClassFile2;
    directClassFile2.setAttributeFactory((AttributeFactory)StdAttributeFactory.THE_ONE);
    this.classFile.getMagic();
    DirectClassFile directClassFile1 = new DirectClassFile(byteArray, this.filePath, this.strictParse);
    directClassFile1.setAttributeFactory((AttributeFactory)StdAttributeFactory.THE_ONE);
    directClassFile1.setObserver(this);
    directClassFile1.getMagic();
  }
  
  public void changeIndent(int paramInt) {}
  
  public void endParsingMember(ByteArray paramByteArray, int paramInt, String paramString1, String paramString2, Member paramMember) {
    if (!(paramMember instanceof Method))
      return; 
    if (!shouldDumpMethod(paramString1))
      return; 
    ConcreteMethod concreteMethod = new ConcreteMethod((Method)paramMember, (ClassFile)this.classFile, true, true);
    DexTranslationAdvice dexTranslationAdvice = DexTranslationAdvice.THE_ONE;
    RopMethod ropMethod2 = Ropper.convert(concreteMethod, (TranslationAdvice)dexTranslationAdvice, this.classFile.getMethods());
    RopMethod ropMethod1 = ropMethod2;
    if (this.optimize) {
      boolean bool = AccessFlags.isStatic(concreteMethod.getAccessFlags());
      ropMethod1 = Optimizer.optimize(ropMethod2, BaseDumper.computeParamWidth(concreteMethod, bool), bool, true, (TranslationAdvice)dexTranslationAdvice);
    } 
    PrintStream printStream2 = System.out;
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append("digraph ");
    stringBuilder2.append(paramString1);
    stringBuilder2.append("{");
    printStream2.println(stringBuilder2.toString());
    PrintStream printStream1 = System.out;
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append("\tfirst -> n");
    stringBuilder1.append(Hex.u2(ropMethod1.getFirstLabel()));
    stringBuilder1.append(";");
    printStream1.println(stringBuilder1.toString());
    BasicBlockList basicBlockList = ropMethod1.getBlocks();
    int i = basicBlockList.size();
    for (paramInt = 0; paramInt < i; paramInt++) {
      StringBuilder stringBuilder;
      BasicBlock basicBlock = basicBlockList.get(paramInt);
      int j = basicBlock.getLabel();
      IntList intList = basicBlock.getSuccessors();
      if (intList.size() == 0) {
        PrintStream printStream = System.out;
        stringBuilder = new StringBuilder();
        stringBuilder.append("\tn");
        stringBuilder.append(Hex.u2(j));
        stringBuilder.append(" -> returns;");
        printStream.println(stringBuilder.toString());
      } else {
        PrintStream printStream;
        if (stringBuilder.size() == 1) {
          printStream = System.out;
          stringBuilder2 = new StringBuilder();
          stringBuilder2.append("\tn");
          stringBuilder2.append(Hex.u2(j));
          stringBuilder2.append(" -> n");
          stringBuilder2.append(Hex.u2(stringBuilder.get(0)));
          stringBuilder2.append(";");
          printStream.println(stringBuilder2.toString());
        } else {
          PrintStream printStream4 = System.out;
          StringBuilder stringBuilder4 = new StringBuilder();
          stringBuilder4.append("\tn");
          stringBuilder4.append(Hex.u2(j));
          stringBuilder4.append(" -> {");
          printStream4.print(stringBuilder4.toString());
          int k;
          for (k = 0; k < stringBuilder.size(); k++) {
            int m = stringBuilder.get(k);
            if (m != printStream.getPrimarySuccessor()) {
              printStream4 = System.out;
              stringBuilder4 = new StringBuilder();
              stringBuilder4.append(" n");
              stringBuilder4.append(Hex.u2(m));
              stringBuilder4.append(" ");
              printStream4.print(stringBuilder4.toString());
            } 
          } 
          System.out.println("};");
          PrintStream printStream3 = System.out;
          StringBuilder stringBuilder3 = new StringBuilder();
          stringBuilder3.append("\tn");
          stringBuilder3.append(Hex.u2(j));
          stringBuilder3.append(" -> n");
          stringBuilder3.append(Hex.u2(printStream.getPrimarySuccessor()));
          stringBuilder3.append(" [label=\"primary\"];");
          printStream3.println(stringBuilder3.toString());
        } 
      } 
    } 
    System.out.println("}");
  }
  
  public void parsed(ByteArray paramByteArray, int paramInt1, int paramInt2, String paramString) {}
  
  protected boolean shouldDumpMethod(String paramString) {
    return (this.args.method == null || this.args.method.equals(paramString));
  }
  
  public void startParsingMember(ByteArray paramByteArray, int paramInt, String paramString1, String paramString2) {}
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\command\dump\DotDumper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
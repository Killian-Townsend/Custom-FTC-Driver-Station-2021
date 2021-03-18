package org.firstinspires.ftc.robotcore.internal.android.dx.command.dump;

import java.io.PrintStream;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.code.BasicBlocker;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.code.ByteBlock;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.code.ByteBlockList;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.code.ByteCatchList;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.code.BytecodeArray;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.code.ConcreteMethod;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.code.Ropper;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct.AttributeFactory;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct.CodeObserver;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct.DirectClassFile;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct.StdAttributeFactory;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.ClassFile;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.Member;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.Method;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.AccessFlags;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.BasicBlock;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.BasicBlockList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.DexTranslationAdvice;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.InsnList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RopMethod;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.TranslationAdvice;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.Optimizer;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.ByteArray;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IntList;

public class BlockDumper extends BaseDumper {
  protected DirectClassFile classFile;
  
  private boolean first;
  
  private boolean optimize;
  
  private boolean rop;
  
  protected boolean suppressDump;
  
  BlockDumper(byte[] paramArrayOfbyte, PrintStream paramPrintStream, String paramString, boolean paramBoolean, Args paramArgs) {
    super(paramArrayOfbyte, paramPrintStream, paramString, paramArgs);
    this.rop = paramBoolean;
    this.classFile = null;
    this.suppressDump = true;
    this.first = true;
    this.optimize = paramArgs.optimize;
  }
  
  public static void dump(byte[] paramArrayOfbyte, PrintStream paramPrintStream, String paramString, boolean paramBoolean, Args paramArgs) {
    (new BlockDumper(paramArrayOfbyte, paramPrintStream, paramString, paramBoolean, paramArgs)).dump();
  }
  
  private void regularDump(ConcreteMethod paramConcreteMethod) {
    BytecodeArray bytecodeArray = paramConcreteMethod.getCode();
    ByteArray byteArray = bytecodeArray.getBytes();
    ByteBlockList byteBlockList = BasicBlocker.identifyBlocks(paramConcreteMethod);
    int k = byteBlockList.size();
    CodeObserver codeObserver = new CodeObserver(byteArray, this);
    setAt(byteArray, 0);
    this.suppressDump = false;
    int i = 0;
    int j;
    for (j = i; i < k; j = m) {
      ByteBlock byteBlock = byteBlockList.get(i);
      int n = byteBlock.getStart();
      int m = byteBlock.getEnd();
      if (j < n) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("dead code ");
        stringBuilder1.append(Hex.u2(j));
        stringBuilder1.append("..");
        stringBuilder1.append(Hex.u2(n));
        parsed(byteArray, j, n - j, stringBuilder1.toString());
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("block ");
      stringBuilder.append(Hex.u2(byteBlock.getLabel()));
      stringBuilder.append(": ");
      stringBuilder.append(Hex.u2(n));
      stringBuilder.append("..");
      stringBuilder.append(Hex.u2(m));
      parsed(byteArray, n, 0, stringBuilder.toString());
      changeIndent(1);
      for (j = n; j < m; j += n) {
        n = bytecodeArray.parseInstruction(j, (BytecodeArray.Visitor)codeObserver);
        codeObserver.setPreviousOffset(j);
      } 
      IntList intList = byteBlock.getSuccessors();
      n = intList.size();
      if (n == 0) {
        parsed(byteArray, m, 0, "returns");
      } else {
        for (j = 0; j < n; j++) {
          int i1 = intList.get(j);
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("next ");
          stringBuilder1.append(Hex.u2(i1));
          parsed(byteArray, m, 0, stringBuilder1.toString());
        } 
      } 
      ByteCatchList byteCatchList = byteBlock.getCatches();
      n = byteCatchList.size();
      for (j = 0; j < n; j++) {
        String str;
        ByteCatchList.Item item = byteCatchList.get(j);
        CstType cstType = item.getExceptionClass();
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("catch ");
        if (cstType == CstType.OBJECT) {
          str = "<any>";
        } else {
          str = str.toHuman();
        } 
        stringBuilder1.append(str);
        stringBuilder1.append(" -> ");
        stringBuilder1.append(Hex.u2(item.getHandlerPc()));
        parsed(byteArray, m, 0, stringBuilder1.toString());
      } 
      changeIndent(-1);
      i++;
    } 
    i = byteArray.size();
    if (j < i) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("dead code ");
      stringBuilder.append(Hex.u2(j));
      stringBuilder.append("..");
      stringBuilder.append(Hex.u2(i));
      parsed(byteArray, j, i - j, stringBuilder.toString());
    } 
    this.suppressDump = true;
  }
  
  private void ropDump(ConcreteMethod paramConcreteMethod) {
    DexTranslationAdvice dexTranslationAdvice = DexTranslationAdvice.THE_ONE;
    ByteArray byteArray = paramConcreteMethod.getCode().getBytes();
    RopMethod ropMethod2 = Ropper.convert(paramConcreteMethod, (TranslationAdvice)dexTranslationAdvice, this.classFile.getMethods());
    StringBuffer stringBuffer = new StringBuffer(2000);
    RopMethod ropMethod1 = ropMethod2;
    if (this.optimize) {
      boolean bool = AccessFlags.isStatic(paramConcreteMethod.getAccessFlags());
      ropMethod1 = Optimizer.optimize(ropMethod2, computeParamWidth(paramConcreteMethod, bool), bool, true, (TranslationAdvice)dexTranslationAdvice);
    } 
    BasicBlockList basicBlockList = ropMethod1.getBlocks();
    int[] arrayOfInt = basicBlockList.getLabelsInOrder();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("first ");
    stringBuilder.append(Hex.u2(ropMethod1.getFirstLabel()));
    stringBuilder.append("\n");
    stringBuffer.append(stringBuilder.toString());
    int j = arrayOfInt.length;
    for (int i = 0; i < j; i++) {
      int k = arrayOfInt[i];
      BasicBlock basicBlock = basicBlockList.get(basicBlockList.indexOfLabel(k));
      stringBuffer.append("block ");
      stringBuffer.append(Hex.u2(k));
      stringBuffer.append("\n");
      IntList intList2 = ropMethod1.labelToPredecessors(k);
      int m = intList2.size();
      for (k = 0; k < m; k++) {
        stringBuffer.append("  pred ");
        stringBuffer.append(Hex.u2(intList2.get(k)));
        stringBuffer.append("\n");
      } 
      InsnList insnList = basicBlock.getInsns();
      m = insnList.size();
      for (k = 0; k < m; k++) {
        insnList.get(k);
        stringBuffer.append("  ");
        stringBuffer.append(insnList.get(k).toHuman());
        stringBuffer.append("\n");
      } 
      IntList intList1 = basicBlock.getSuccessors();
      m = intList1.size();
      if (m == 0) {
        stringBuffer.append("  returns\n");
      } else {
        int n = basicBlock.getPrimarySuccessor();
        for (k = 0; k < m; k++) {
          int i1 = intList1.get(k);
          stringBuffer.append("  next ");
          stringBuffer.append(Hex.u2(i1));
          if (m != 1 && i1 == n)
            stringBuffer.append(" *"); 
          stringBuffer.append("\n");
        } 
      } 
    } 
    this.suppressDump = false;
    setAt(byteArray, 0);
    parsed(byteArray, 0, byteArray.size(), stringBuffer.toString());
    this.suppressDump = true;
  }
  
  public void changeIndent(int paramInt) {
    if (!this.suppressDump)
      super.changeIndent(paramInt); 
  }
  
  public void dump() {
    ByteArray byteArray = new ByteArray(getBytes());
    DirectClassFile directClassFile2 = new DirectClassFile(byteArray, getFilePath(), getStrictParse());
    this.classFile = directClassFile2;
    directClassFile2.setAttributeFactory((AttributeFactory)StdAttributeFactory.THE_ONE);
    this.classFile.getMagic();
    DirectClassFile directClassFile1 = new DirectClassFile(byteArray, getFilePath(), getStrictParse());
    directClassFile1.setAttributeFactory((AttributeFactory)StdAttributeFactory.THE_ONE);
    directClassFile1.setObserver(this);
    directClassFile1.getMagic();
  }
  
  public void endParsingMember(ByteArray paramByteArray, int paramInt, String paramString1, String paramString2, Member paramMember) {
    if (!(paramMember instanceof Method))
      return; 
    if (!shouldDumpMethod(paramString1))
      return; 
    if ((paramMember.getAccessFlags() & 0x500) != 0)
      return; 
    ConcreteMethod concreteMethod = new ConcreteMethod((Method)paramMember, (ClassFile)this.classFile, true, true);
    if (this.rop) {
      ropDump(concreteMethod);
      return;
    } 
    regularDump(concreteMethod);
  }
  
  public void parsed(ByteArray paramByteArray, int paramInt1, int paramInt2, String paramString) {
    if (!this.suppressDump)
      super.parsed(paramByteArray, paramInt1, paramInt2, paramString); 
  }
  
  protected boolean shouldDumpMethod(String paramString) {
    return (this.args.method == null || this.args.method.equals(paramString));
  }
  
  public void startParsingMember(ByteArray paramByteArray, int paramInt, String paramString1, String paramString2) {
    if (paramString2.indexOf('(') < 0)
      return; 
    if (!shouldDumpMethod(paramString1))
      return; 
    setAt(paramByteArray, paramInt);
    this.suppressDump = false;
    if (this.first) {
      this.first = false;
    } else {
      parsed(paramByteArray, paramInt, 0, "\n");
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("method ");
    stringBuilder.append(paramString1);
    stringBuilder.append(" ");
    stringBuilder.append(paramString2);
    parsed(paramByteArray, paramInt, 0, stringBuilder.toString());
    this.suppressDump = true;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\command\dump\BlockDumper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
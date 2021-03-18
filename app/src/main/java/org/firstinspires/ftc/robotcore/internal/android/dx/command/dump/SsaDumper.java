package org.firstinspires.ftc.robotcore.internal.android.dx.command.dump;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.code.ConcreteMethod;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.code.Ropper;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.ClassFile;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.Member;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.Method;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.AccessFlags;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.DexTranslationAdvice;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RopMethod;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.TranslationAdvice;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.Optimizer;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.SsaBasicBlock;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.SsaInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.SsaMethod;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.ByteArray;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IntList;

public class SsaDumper extends BlockDumper {
  private SsaDumper(byte[] paramArrayOfbyte, PrintStream paramPrintStream, String paramString, Args paramArgs) {
    super(paramArrayOfbyte, paramPrintStream, paramString, true, paramArgs);
  }
  
  public static void dump(byte[] paramArrayOfbyte, PrintStream paramPrintStream, String paramString, Args paramArgs) {
    (new SsaDumper(paramArrayOfbyte, paramPrintStream, paramString, paramArgs)).dump();
  }
  
  public void endParsingMember(ByteArray paramByteArray, int paramInt, String paramString1, String paramString2, Member paramMember) {
    SsaMethod ssaMethod;
    if (!(paramMember instanceof Method))
      return; 
    if (!shouldDumpMethod(paramString1))
      return; 
    if ((paramMember.getAccessFlags() & 0x500) != 0)
      return; 
    ConcreteMethod concreteMethod = new ConcreteMethod((Method)paramMember, (ClassFile)this.classFile, true, true);
    DexTranslationAdvice dexTranslationAdvice = DexTranslationAdvice.THE_ONE;
    RopMethod ropMethod = Ropper.convert(concreteMethod, (TranslationAdvice)dexTranslationAdvice, this.classFile.getMethods());
    paramString1 = null;
    boolean bool = AccessFlags.isStatic(concreteMethod.getAccessFlags());
    paramInt = computeParamWidth(concreteMethod, bool);
    if (this.args.ssaStep == null) {
      ssaMethod = Optimizer.debugNoRegisterAllocation(ropMethod, paramInt, bool, true, (TranslationAdvice)dexTranslationAdvice, EnumSet.allOf(Optimizer.OptionalStep.class));
    } else if ("edge-split".equals(this.args.ssaStep)) {
      ssaMethod = Optimizer.debugEdgeSplit(ropMethod, paramInt, bool, true, (TranslationAdvice)dexTranslationAdvice);
    } else if ("phi-placement".equals(this.args.ssaStep)) {
      ssaMethod = Optimizer.debugPhiPlacement(ropMethod, paramInt, bool, true, (TranslationAdvice)dexTranslationAdvice);
    } else if ("renaming".equals(this.args.ssaStep)) {
      ssaMethod = Optimizer.debugRenaming(ropMethod, paramInt, bool, true, (TranslationAdvice)dexTranslationAdvice);
    } else if ("dead-code".equals(this.args.ssaStep)) {
      ssaMethod = Optimizer.debugDeadCodeRemover(ropMethod, paramInt, bool, true, (TranslationAdvice)dexTranslationAdvice);
    } 
    StringBuffer stringBuffer = new StringBuffer(2000);
    stringBuffer.append("first ");
    stringBuffer.append(Hex.u2(ssaMethod.blockIndexToRopLabel(ssaMethod.getEntryBlockIndex())));
    stringBuffer.append('\n');
    ArrayList<?> arrayList = (ArrayList)ssaMethod.getBlocks().clone();
    Collections.sort(arrayList, SsaBasicBlock.LABEL_COMPARATOR);
    Iterator<?> iterator = arrayList.iterator();
    while (true) {
      bool = iterator.hasNext();
      boolean bool1 = false;
      if (bool) {
        SsaBasicBlock ssaBasicBlock = (SsaBasicBlock)iterator.next();
        stringBuffer.append("block ");
        stringBuffer.append(Hex.u2(ssaBasicBlock.getRopLabel()));
        stringBuffer.append('\n');
        BitSet bitSet = ssaBasicBlock.getPredecessors();
        for (paramInt = bitSet.nextSetBit(0); paramInt >= 0; paramInt = bitSet.nextSetBit(paramInt + 1)) {
          stringBuffer.append("  pred ");
          stringBuffer.append(Hex.u2(ssaMethod.blockIndexToRopLabel(paramInt)));
          stringBuffer.append('\n');
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("  live in:");
        stringBuilder.append(ssaBasicBlock.getLiveInRegs());
        stringBuffer.append(stringBuilder.toString());
        stringBuffer.append("\n");
        for (SsaInsn ssaInsn : ssaBasicBlock.getInsns()) {
          stringBuffer.append("  ");
          stringBuffer.append(ssaInsn.toHuman());
          stringBuffer.append('\n');
        } 
        if (ssaBasicBlock.getSuccessors().cardinality() == 0) {
          stringBuffer.append("  returns\n");
        } else {
          int i = ssaBasicBlock.getPrimarySuccessorRopLabel();
          IntList intList = ssaBasicBlock.getRopLabelSuccessorList();
          int j = intList.size();
          for (paramInt = bool1; paramInt < j; paramInt++) {
            stringBuffer.append("  next ");
            stringBuffer.append(Hex.u2(intList.get(paramInt)));
            if (j != 1 && i == intList.get(paramInt))
              stringBuffer.append(" *"); 
            stringBuffer.append('\n');
          } 
        } 
        stringBuilder = new StringBuilder();
        stringBuilder.append("  live out:");
        stringBuilder.append(ssaBasicBlock.getLiveOutRegs());
        stringBuffer.append(stringBuilder.toString());
        stringBuffer.append("\n");
        continue;
      } 
      this.suppressDump = false;
      setAt(paramByteArray, 0);
      parsed(paramByteArray, 0, paramByteArray.size(), stringBuffer.toString());
      this.suppressDump = true;
      return;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\command\dump\SsaDumper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
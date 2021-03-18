package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import java.io.PrintWriter;
import org.firstinspires.ftc.robotcore.internal.android.dex.util.ExceptionWithContext;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.DalvCode;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.DalvInsnList;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.LocalList;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.PositionList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstMethodRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;

public class DebugInfoItem extends OffsettedItem {
  private static final int ALIGNMENT = 1;
  
  private static final boolean ENABLE_ENCODER_SELF_CHECK = false;
  
  private final DalvCode code;
  
  private byte[] encoded;
  
  private final boolean isStatic;
  
  private final CstMethodRef ref;
  
  public DebugInfoItem(DalvCode paramDalvCode, boolean paramBoolean, CstMethodRef paramCstMethodRef) {
    super(1, -1);
    if (paramDalvCode != null) {
      this.code = paramDalvCode;
      this.isStatic = paramBoolean;
      this.ref = paramCstMethodRef;
      return;
    } 
    throw new NullPointerException("code == null");
  }
  
  private byte[] encode(DexFile paramDexFile, String paramString, PrintWriter paramPrintWriter, AnnotatedOutput paramAnnotatedOutput, boolean paramBoolean) {
    return encode0(paramDexFile, paramString, paramPrintWriter, paramAnnotatedOutput, paramBoolean);
  }
  
  private byte[] encode0(DexFile paramDexFile, String paramString, PrintWriter paramPrintWriter, AnnotatedOutput paramAnnotatedOutput, boolean paramBoolean) {
    PositionList positionList = this.code.getPositions();
    LocalList localList = this.code.getLocals();
    DalvInsnList dalvInsnList = this.code.getInsns();
    DebugInfoEncoder debugInfoEncoder = new DebugInfoEncoder(positionList, localList, paramDexFile, dalvInsnList.codeSize(), dalvInsnList.getRegistersSize(), this.isStatic, this.ref);
    return (paramPrintWriter == null && paramAnnotatedOutput == null) ? debugInfoEncoder.convert() : debugInfoEncoder.convertAndAnnotate(paramString, paramPrintWriter, paramAnnotatedOutput, paramBoolean);
  }
  
  public void addContents(DexFile paramDexFile) {}
  
  public void annotateTo(DexFile paramDexFile, AnnotatedOutput paramAnnotatedOutput, String paramString) {
    encode(paramDexFile, paramString, (PrintWriter)null, paramAnnotatedOutput, false);
  }
  
  public void debugPrint(PrintWriter paramPrintWriter, String paramString) {
    encode((DexFile)null, paramString, paramPrintWriter, (AnnotatedOutput)null, false);
  }
  
  public ItemType itemType() {
    return ItemType.TYPE_DEBUG_INFO_ITEM;
  }
  
  protected void place0(Section paramSection, int paramInt) {
    try {
      byte[] arrayOfByte = encode(paramSection.getFile(), (String)null, (PrintWriter)null, (AnnotatedOutput)null, false);
      this.encoded = arrayOfByte;
      setWriteSize(arrayOfByte.length);
      return;
    } catch (RuntimeException runtimeException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("...while placing debug info for ");
      stringBuilder.append(this.ref.toHuman());
      throw ExceptionWithContext.withContext(runtimeException, stringBuilder.toString());
    } 
  }
  
  public String toHuman() {
    throw new RuntimeException("unsupported");
  }
  
  protected void writeTo0(DexFile paramDexFile, AnnotatedOutput paramAnnotatedOutput) {
    if (paramAnnotatedOutput.annotates()) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(offsetString());
      stringBuilder.append(" debug info");
      paramAnnotatedOutput.annotate(stringBuilder.toString());
      encode(paramDexFile, (String)null, (PrintWriter)null, paramAnnotatedOutput, true);
    } 
    paramAnnotatedOutput.write(this.encoded);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\DebugInfoItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
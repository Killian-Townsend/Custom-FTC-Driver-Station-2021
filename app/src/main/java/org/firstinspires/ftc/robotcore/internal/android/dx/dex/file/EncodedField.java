package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import java.io.PrintWriter;
import org.firstinspires.ftc.robotcore.internal.android.dex.Leb128;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.AccessFlags;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstFieldRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public final class EncodedField extends EncodedMember implements Comparable<EncodedField> {
  private final CstFieldRef field;
  
  public EncodedField(CstFieldRef paramCstFieldRef, int paramInt) {
    super(paramInt);
    if (paramCstFieldRef != null) {
      this.field = paramCstFieldRef;
      return;
    } 
    throw new NullPointerException("field == null");
  }
  
  public void addContents(DexFile paramDexFile) {
    paramDexFile.getFieldIds().intern(this.field);
  }
  
  public int compareTo(EncodedField paramEncodedField) {
    return this.field.compareTo((Constant)paramEncodedField.field);
  }
  
  public void debugPrint(PrintWriter paramPrintWriter, boolean paramBoolean) {
    paramPrintWriter.println(toString());
  }
  
  public int encode(DexFile paramDexFile, AnnotatedOutput paramAnnotatedOutput, int paramInt1, int paramInt2) {
    int i = paramDexFile.getFieldIds().indexOf(this.field);
    paramInt1 = i - paramInt1;
    int j = getAccessFlags();
    if (paramAnnotatedOutput.annotates()) {
      paramAnnotatedOutput.annotate(0, String.format("  [%x] %s", new Object[] { Integer.valueOf(paramInt2), this.field.toHuman() }));
      paramInt2 = Leb128.unsignedLeb128Size(paramInt1);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("    field_idx:    ");
      stringBuilder.append(Hex.u4(i));
      paramAnnotatedOutput.annotate(paramInt2, stringBuilder.toString());
      paramInt2 = Leb128.unsignedLeb128Size(j);
      stringBuilder = new StringBuilder();
      stringBuilder.append("    access_flags: ");
      stringBuilder.append(AccessFlags.fieldString(j));
      paramAnnotatedOutput.annotate(paramInt2, stringBuilder.toString());
    } 
    paramAnnotatedOutput.writeUleb128(paramInt1);
    paramAnnotatedOutput.writeUleb128(j);
    return i;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool1 = paramObject instanceof EncodedField;
    boolean bool = false;
    if (!bool1)
      return false; 
    if (compareTo((EncodedField)paramObject) == 0)
      bool = true; 
    return bool;
  }
  
  public CstString getName() {
    return this.field.getNat().getName();
  }
  
  public CstFieldRef getRef() {
    return this.field;
  }
  
  public int hashCode() {
    return this.field.hashCode();
  }
  
  public String toHuman() {
    return this.field.toHuman();
  }
  
  public String toString() {
    StringBuffer stringBuffer = new StringBuffer(100);
    stringBuffer.append(getClass().getName());
    stringBuffer.append('{');
    stringBuffer.append(Hex.u2(getAccessFlags()));
    stringBuffer.append(' ');
    stringBuffer.append(this.field);
    stringBuffer.append('}');
    return stringBuffer.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\EncodedField.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
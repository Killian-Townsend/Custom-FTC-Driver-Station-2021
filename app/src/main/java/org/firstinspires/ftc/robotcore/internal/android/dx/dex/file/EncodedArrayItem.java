package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstArray;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.ByteArrayAnnotatedOutput;

public final class EncodedArrayItem extends OffsettedItem {
  private static final int ALIGNMENT = 1;
  
  private final CstArray array;
  
  private byte[] encodedForm;
  
  public EncodedArrayItem(CstArray paramCstArray) {
    super(1, -1);
    if (paramCstArray != null) {
      this.array = paramCstArray;
      this.encodedForm = null;
      return;
    } 
    throw new NullPointerException("array == null");
  }
  
  public void addContents(DexFile paramDexFile) {
    ValueEncoder.addContents(paramDexFile, (Constant)this.array);
  }
  
  protected int compareTo0(OffsettedItem paramOffsettedItem) {
    paramOffsettedItem = paramOffsettedItem;
    return this.array.compareTo((Constant)((EncodedArrayItem)paramOffsettedItem).array);
  }
  
  public int hashCode() {
    return this.array.hashCode();
  }
  
  public ItemType itemType() {
    return ItemType.TYPE_ENCODED_ARRAY_ITEM;
  }
  
  protected void place0(Section paramSection, int paramInt) {
    ByteArrayAnnotatedOutput byteArrayAnnotatedOutput = new ByteArrayAnnotatedOutput();
    (new ValueEncoder(paramSection.getFile(), (AnnotatedOutput)byteArrayAnnotatedOutput)).writeArray(this.array, false);
    byte[] arrayOfByte = byteArrayAnnotatedOutput.toByteArray();
    this.encodedForm = arrayOfByte;
    setWriteSize(arrayOfByte.length);
  }
  
  public String toHuman() {
    return this.array.toHuman();
  }
  
  protected void writeTo0(DexFile paramDexFile, AnnotatedOutput paramAnnotatedOutput) {
    if (paramAnnotatedOutput.annotates()) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(offsetString());
      stringBuilder.append(" encoded array");
      paramAnnotatedOutput.annotate(0, stringBuilder.toString());
      (new ValueEncoder(paramDexFile, paramAnnotatedOutput)).writeArray(this.array, true);
      return;
    } 
    paramAnnotatedOutput.write(this.encodedForm);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\EncodedArrayItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
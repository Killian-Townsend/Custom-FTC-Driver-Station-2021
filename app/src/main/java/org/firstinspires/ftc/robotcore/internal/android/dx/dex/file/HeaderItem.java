package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public final class HeaderItem extends IndexedItem {
  public void addContents(DexFile paramDexFile) {}
  
  public ItemType itemType() {
    return ItemType.TYPE_HEADER_ITEM;
  }
  
  public int writeSize() {
    return 112;
  }
  
  public void writeTo(DexFile paramDexFile, AnnotatedOutput paramAnnotatedOutput) {
    int j = paramDexFile.getMap().getFileOffset();
    Section section1 = paramDexFile.getFirstDataSection();
    Section section2 = paramDexFile.getLastDataSection();
    int k = section1.getFileOffset();
    int m = section2.getFileOffset() + section2.writeSize() - k;
    String str = paramDexFile.getDexOptions().getMagic();
    if (paramAnnotatedOutput.annotates()) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("magic: ");
      stringBuilder.append((new CstString(str)).toQuoted());
      paramAnnotatedOutput.annotate(8, stringBuilder.toString());
      paramAnnotatedOutput.annotate(4, "checksum");
      paramAnnotatedOutput.annotate(20, "signature");
      stringBuilder = new StringBuilder();
      stringBuilder.append("file_size:       ");
      stringBuilder.append(Hex.u4(paramDexFile.getFileSize()));
      paramAnnotatedOutput.annotate(4, stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("header_size:     ");
      stringBuilder.append(Hex.u4(112));
      paramAnnotatedOutput.annotate(4, stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("endian_tag:      ");
      stringBuilder.append(Hex.u4(305419896));
      paramAnnotatedOutput.annotate(4, stringBuilder.toString());
      paramAnnotatedOutput.annotate(4, "link_size:       0");
      paramAnnotatedOutput.annotate(4, "link_off:        0");
      stringBuilder = new StringBuilder();
      stringBuilder.append("map_off:         ");
      stringBuilder.append(Hex.u4(j));
      paramAnnotatedOutput.annotate(4, stringBuilder.toString());
    } 
    for (int i = 0; i < 8; i++)
      paramAnnotatedOutput.writeByte(str.charAt(i)); 
    paramAnnotatedOutput.writeZeroes(24);
    paramAnnotatedOutput.writeInt(paramDexFile.getFileSize());
    paramAnnotatedOutput.writeInt(112);
    paramAnnotatedOutput.writeInt(305419896);
    paramAnnotatedOutput.writeZeroes(8);
    paramAnnotatedOutput.writeInt(j);
    paramDexFile.getStringIds().writeHeaderPart(paramAnnotatedOutput);
    paramDexFile.getTypeIds().writeHeaderPart(paramAnnotatedOutput);
    paramDexFile.getProtoIds().writeHeaderPart(paramAnnotatedOutput);
    paramDexFile.getFieldIds().writeHeaderPart(paramAnnotatedOutput);
    paramDexFile.getMethodIds().writeHeaderPart(paramAnnotatedOutput);
    paramDexFile.getClassDefs().writeHeaderPart(paramAnnotatedOutput);
    if (paramAnnotatedOutput.annotates()) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("data_size:       ");
      stringBuilder.append(Hex.u4(m));
      paramAnnotatedOutput.annotate(4, stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("data_off:        ");
      stringBuilder.append(Hex.u4(k));
      paramAnnotatedOutput.annotate(4, stringBuilder.toString());
    } 
    paramAnnotatedOutput.writeInt(m);
    paramAnnotatedOutput.writeInt(k);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\HeaderItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
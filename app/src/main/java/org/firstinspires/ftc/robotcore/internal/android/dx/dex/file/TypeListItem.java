package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.StdTypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public final class TypeListItem extends OffsettedItem {
  private static final int ALIGNMENT = 4;
  
  private static final int ELEMENT_SIZE = 2;
  
  private static final int HEADER_SIZE = 4;
  
  private final TypeList list;
  
  public TypeListItem(TypeList paramTypeList) {
    super(4, paramTypeList.size() * 2 + 4);
    this.list = paramTypeList;
  }
  
  public void addContents(DexFile paramDexFile) {
    TypeIdsSection typeIdsSection = paramDexFile.getTypeIds();
    int j = this.list.size();
    for (int i = 0; i < j; i++)
      typeIdsSection.intern(this.list.getType(i)); 
  }
  
  protected int compareTo0(OffsettedItem paramOffsettedItem) {
    return StdTypeList.compareContents(this.list, ((TypeListItem)paramOffsettedItem).list);
  }
  
  public TypeList getList() {
    return this.list;
  }
  
  public int hashCode() {
    return StdTypeList.hashContents(this.list);
  }
  
  public ItemType itemType() {
    return ItemType.TYPE_TYPE_LIST;
  }
  
  public String toHuman() {
    throw new RuntimeException("unsupported");
  }
  
  protected void writeTo0(DexFile paramDexFile, AnnotatedOutput paramAnnotatedOutput) {
    TypeIdsSection typeIdsSection = paramDexFile.getTypeIds();
    int j = this.list.size();
    boolean bool = paramAnnotatedOutput.annotates();
    byte b = 0;
    if (bool) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(offsetString());
      stringBuilder.append(" type_list");
      paramAnnotatedOutput.annotate(0, stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("  size: ");
      stringBuilder.append(Hex.u4(j));
      paramAnnotatedOutput.annotate(4, stringBuilder.toString());
      for (int k = 0; k < j; k++) {
        Type type = this.list.getType(k);
        int m = typeIdsSection.indexOf(type);
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("  ");
        stringBuilder1.append(Hex.u2(m));
        stringBuilder1.append(" // ");
        stringBuilder1.append(type.toHuman());
        paramAnnotatedOutput.annotate(2, stringBuilder1.toString());
      } 
    } 
    paramAnnotatedOutput.writeInt(j);
    for (int i = b; i < j; i++)
      paramAnnotatedOutput.writeShort(typeIdsSection.indexOf(this.list.getType(i))); 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\TypeListItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import java.util.ArrayList;
import java.util.Iterator;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public final class MapItem extends OffsettedItem {
  private static final int ALIGNMENT = 4;
  
  private static final int WRITE_SIZE = 12;
  
  private final Item firstItem;
  
  private final int itemCount;
  
  private final Item lastItem;
  
  private final Section section;
  
  private final ItemType type;
  
  private MapItem(ItemType paramItemType, Section paramSection, Item paramItem1, Item paramItem2, int paramInt) {
    super(4, 12);
    if (paramItemType != null) {
      if (paramSection != null) {
        if (paramItem1 != null) {
          if (paramItem2 != null) {
            if (paramInt > 0) {
              this.type = paramItemType;
              this.section = paramSection;
              this.firstItem = paramItem1;
              this.lastItem = paramItem2;
              this.itemCount = paramInt;
              return;
            } 
            throw new IllegalArgumentException("itemCount <= 0");
          } 
          throw new NullPointerException("lastItem == null");
        } 
        throw new NullPointerException("firstItem == null");
      } 
      throw new NullPointerException("section == null");
    } 
    throw new NullPointerException("type == null");
  }
  
  private MapItem(Section paramSection) {
    super(4, 12);
    if (paramSection != null) {
      this.type = ItemType.TYPE_MAP_LIST;
      this.section = paramSection;
      this.firstItem = null;
      this.lastItem = null;
      this.itemCount = 1;
      return;
    } 
    throw new NullPointerException("section == null");
  }
  
  public static void addMap(Section[] paramArrayOfSection, MixedItemSection paramMixedItemSection) {
    if (paramArrayOfSection != null) {
      if (paramMixedItemSection.items().size() == 0) {
        ArrayList<MapItem> arrayList = new ArrayList(50);
        int j = paramArrayOfSection.length;
        for (int i = 0; i < j; i++) {
          Item item1;
          Item item2;
          Section section = paramArrayOfSection[i];
          Iterator<? extends Item> iterator = section.items().iterator();
          ItemType itemType3 = null;
          ItemType itemType1 = itemType3;
          ItemType itemType2 = itemType1;
          int k = 0;
          while (iterator.hasNext()) {
            Item item4;
            Item item3 = iterator.next();
            ItemType itemType6 = item3.itemType();
            ItemType itemType5 = itemType3;
            ItemType itemType4 = itemType1;
            int m = k;
            if (itemType6 != itemType3) {
              if (k)
                arrayList.add(new MapItem(itemType3, section, (Item)itemType1, (Item)itemType2, k)); 
              item4 = item3;
              itemType5 = itemType6;
              m = 0;
            } 
            k = m + 1;
            item2 = item3;
            itemType3 = itemType5;
            item1 = item4;
          } 
          if (k != 0) {
            arrayList.add(new MapItem(itemType3, section, item1, item2, k));
          } else if (section == paramMixedItemSection) {
            arrayList.add(new MapItem(paramMixedItemSection));
          } 
        } 
        paramMixedItemSection.add(new UniformListItem<MapItem>(ItemType.TYPE_MAP_LIST, arrayList));
        return;
      } 
      throw new IllegalArgumentException("mapSection.items().size() != 0");
    } 
    throw new NullPointerException("sections == null");
  }
  
  public void addContents(DexFile paramDexFile) {}
  
  public ItemType itemType() {
    return ItemType.TYPE_MAP_ITEM;
  }
  
  public final String toHuman() {
    return toString();
  }
  
  public String toString() {
    StringBuffer stringBuffer = new StringBuffer(100);
    stringBuffer.append(getClass().getName());
    stringBuffer.append('{');
    stringBuffer.append(this.section.toString());
    stringBuffer.append(' ');
    stringBuffer.append(this.type.toHuman());
    stringBuffer.append('}');
    return stringBuffer.toString();
  }
  
  protected void writeTo0(DexFile paramDexFile, AnnotatedOutput paramAnnotatedOutput) {
    int i;
    int j = this.type.getMapValue();
    Item item = this.firstItem;
    if (item == null) {
      i = this.section.getFileOffset();
    } else {
      i = this.section.getAbsoluteItemOffset(item);
    } 
    if (paramAnnotatedOutput.annotates()) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(offsetString());
      stringBuilder.append(' ');
      stringBuilder.append(this.type.getTypeName());
      stringBuilder.append(" map");
      paramAnnotatedOutput.annotate(0, stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("  type:   ");
      stringBuilder.append(Hex.u2(j));
      stringBuilder.append(" // ");
      stringBuilder.append(this.type.toString());
      paramAnnotatedOutput.annotate(2, stringBuilder.toString());
      paramAnnotatedOutput.annotate(2, "  unused: 0");
      stringBuilder = new StringBuilder();
      stringBuilder.append("  size:   ");
      stringBuilder.append(Hex.u4(this.itemCount));
      paramAnnotatedOutput.annotate(4, stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("  offset: ");
      stringBuilder.append(Hex.u4(i));
      paramAnnotatedOutput.annotate(4, stringBuilder.toString());
    } 
    paramAnnotatedOutput.writeShort(j);
    paramAnnotatedOutput.writeShort(0);
    paramAnnotatedOutput.writeInt(this.itemCount);
    paramAnnotatedOutput.writeInt(i);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\MapItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
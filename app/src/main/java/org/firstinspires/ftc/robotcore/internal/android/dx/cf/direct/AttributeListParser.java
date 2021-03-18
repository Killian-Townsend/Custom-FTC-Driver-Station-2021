package org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct;

import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.Attribute;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.ParseException;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.ParseObserver;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.StdAttributeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.ByteArray;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

final class AttributeListParser {
  private final AttributeFactory attributeFactory;
  
  private final DirectClassFile cf;
  
  private final int context;
  
  private int endOffset;
  
  private final StdAttributeList list;
  
  private ParseObserver observer;
  
  private final int offset;
  
  public AttributeListParser(DirectClassFile paramDirectClassFile, int paramInt1, int paramInt2, AttributeFactory paramAttributeFactory) {
    if (paramDirectClassFile != null) {
      if (paramAttributeFactory != null) {
        int i = paramDirectClassFile.getBytes().getUnsignedShort(paramInt2);
        this.cf = paramDirectClassFile;
        this.context = paramInt1;
        this.offset = paramInt2;
        this.attributeFactory = paramAttributeFactory;
        this.list = new StdAttributeList(i);
        this.endOffset = -1;
        return;
      } 
      throw new NullPointerException("attributeFactory == null");
    } 
    throw new NullPointerException("cf == null");
  }
  
  private void parse() {
    int k = this.list.size();
    int j = this.offset + 2;
    ByteArray byteArray = this.cf.getBytes();
    ParseObserver parseObserver = this.observer;
    if (parseObserver != null) {
      int m = this.offset;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("attributes_count: ");
      stringBuilder.append(Hex.u2(k));
      parseObserver.parsed(byteArray, m, 2, stringBuilder.toString());
    } 
    int i = 0;
    while (i < k) {
      try {
        if (this.observer != null) {
          parseObserver = this.observer;
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("\nattributes[");
          stringBuilder.append(i);
          stringBuilder.append("]:\n");
          parseObserver.parsed(byteArray, j, 0, stringBuilder.toString());
          this.observer.changeIndent(1);
        } 
        Attribute attribute = this.attributeFactory.parse(this.cf, this.context, j, this.observer);
        j += attribute.byteLength();
        this.list.set(i, attribute);
        if (this.observer != null) {
          this.observer.changeIndent(-1);
          ParseObserver parseObserver1 = this.observer;
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("end attributes[");
          stringBuilder.append(i);
          stringBuilder.append("]\n");
          parseObserver1.parsed(byteArray, j, 0, stringBuilder.toString());
        } 
        i++;
      } catch (ParseException parseException) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("...while parsing attributes[");
        stringBuilder.append(i);
        stringBuilder.append("]");
        parseException.addContext(stringBuilder.toString());
        throw parseException;
      } catch (RuntimeException runtimeException) {
        ParseException parseException = new ParseException(runtimeException);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("...while parsing attributes[");
        stringBuilder.append(i);
        stringBuilder.append("]");
        parseException.addContext(stringBuilder.toString());
        throw parseException;
      } 
    } 
    this.endOffset = j;
  }
  
  private void parseIfNecessary() {
    if (this.endOffset < 0)
      parse(); 
  }
  
  public int getEndOffset() {
    parseIfNecessary();
    return this.endOffset;
  }
  
  public StdAttributeList getList() {
    parseIfNecessary();
    return this.list;
  }
  
  public void setObserver(ParseObserver paramParseObserver) {
    this.observer = paramParseObserver;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\direct\AttributeListParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct;

import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.AttributeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.Member;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.ParseException;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.ParseObserver;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.StdAttributeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.ConstantPool;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstNat;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.ByteArray;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

abstract class MemberListParser {
  private final AttributeFactory attributeFactory;
  
  private final DirectClassFile cf;
  
  private final CstType definer;
  
  private int endOffset;
  
  private ParseObserver observer;
  
  private final int offset;
  
  public MemberListParser(DirectClassFile paramDirectClassFile, CstType paramCstType, int paramInt, AttributeFactory paramAttributeFactory) {
    if (paramDirectClassFile != null) {
      if (paramInt >= 0) {
        if (paramAttributeFactory != null) {
          this.cf = paramDirectClassFile;
          this.definer = paramCstType;
          this.offset = paramInt;
          this.attributeFactory = paramAttributeFactory;
          this.endOffset = -1;
          return;
        } 
        throw new NullPointerException("attributeFactory == null");
      } 
      throw new IllegalArgumentException("offset < 0");
    } 
    throw new NullPointerException("cf == null");
  }
  
  private void parse() {
    int m = getAttributeContext();
    int j = getCount();
    int k = this.offset + 2;
    ByteArray byteArray = this.cf.getBytes();
    ConstantPool constantPool = this.cf.getConstantPool();
    ParseObserver parseObserver = this.observer;
    if (parseObserver != null) {
      int n = this.offset;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(humanName());
      stringBuilder.append("s_count: ");
      stringBuilder.append(Hex.u2(j));
      parseObserver.parsed(byteArray, n, 2, stringBuilder.toString());
    } 
    int i = 0;
    while (i < j) {
      try {
        int n = byteArray.getUnsignedShort(k);
        int i1 = k + 2;
        int i2 = byteArray.getUnsignedShort(i1);
        int i3 = k + 4;
        int i4 = byteArray.getUnsignedShort(i3);
        CstString cstString2 = (CstString)constantPool.get(i2);
        CstString cstString1 = (CstString)constantPool.get(i4);
        if (this.observer != null) {
          this.observer.startParsingMember(byteArray, k, cstString2.getString(), cstString1.getString());
          ParseObserver parseObserver1 = this.observer;
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("\n");
          stringBuilder.append(humanName());
          stringBuilder.append("s[");
          stringBuilder.append(i);
          stringBuilder.append("]:\n");
          parseObserver1.parsed(byteArray, k, 0, stringBuilder.toString());
          this.observer.changeIndent(1);
          parseObserver1 = this.observer;
          stringBuilder = new StringBuilder();
          stringBuilder.append("access_flags: ");
          stringBuilder.append(humanAccessFlags(n));
          parseObserver1.parsed(byteArray, k, 2, stringBuilder.toString());
          parseObserver1 = this.observer;
          stringBuilder = new StringBuilder();
          stringBuilder.append("name: ");
          stringBuilder.append(cstString2.toHuman());
          parseObserver1.parsed(byteArray, i1, 2, stringBuilder.toString());
          parseObserver1 = this.observer;
          stringBuilder = new StringBuilder();
          stringBuilder.append("descriptor: ");
          stringBuilder.append(cstString1.toHuman());
          parseObserver1.parsed(byteArray, i3, 2, stringBuilder.toString());
        } 
        AttributeListParser attributeListParser = new AttributeListParser(this.cf, m, k + 6, this.attributeFactory);
        attributeListParser.setObserver(this.observer);
        k = attributeListParser.getEndOffset();
        StdAttributeList stdAttributeList = attributeListParser.getList();
        stdAttributeList.setImmutable();
        Member member = set(i, n, new CstNat(cstString2, cstString1), (AttributeList)stdAttributeList);
        if (this.observer != null) {
          this.observer.changeIndent(-1);
          ParseObserver parseObserver1 = this.observer;
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("end ");
          stringBuilder.append(humanName());
          stringBuilder.append("s[");
          stringBuilder.append(i);
          stringBuilder.append("]\n");
          parseObserver1.parsed(byteArray, k, 0, stringBuilder.toString());
          parseObserver1 = this.observer;
          String str2 = cstString2.getString();
          String str1 = cstString1.getString();
          try {
            parseObserver1.endParsingMember(byteArray, k, str2, str1, member);
          } catch (ParseException parseException) {
          
          } catch (RuntimeException runtimeException) {}
        } 
        i++;
      } catch (ParseException parseException) {
        continue;
      } catch (RuntimeException runtimeException) {
        ParseException parseException = new ParseException(runtimeException);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("...while parsing ");
        stringBuilder.append(humanName());
        stringBuilder.append("s[");
        stringBuilder.append(i);
        stringBuilder.append("]");
        parseException.addContext(stringBuilder.toString());
        throw parseException;
      } 
    } 
    this.endOffset = k;
  }
  
  protected abstract int getAttributeContext();
  
  protected final int getCount() {
    return this.cf.getBytes().getUnsignedShort(this.offset);
  }
  
  protected final CstType getDefiner() {
    return this.definer;
  }
  
  public int getEndOffset() {
    parseIfNecessary();
    return this.endOffset;
  }
  
  protected abstract String humanAccessFlags(int paramInt);
  
  protected abstract String humanName();
  
  protected final void parseIfNecessary() {
    if (this.endOffset < 0)
      parse(); 
  }
  
  protected abstract Member set(int paramInt1, int paramInt2, CstNat paramCstNat, AttributeList paramAttributeList);
  
  public final void setObserver(ParseObserver paramParseObserver) {
    this.observer = paramParseObserver;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\direct\MemberListParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
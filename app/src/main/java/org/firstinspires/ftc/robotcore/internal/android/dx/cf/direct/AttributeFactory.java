package org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct;

import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.RawAttribute;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.Attribute;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.ParseException;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.ParseObserver;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.ConstantPool;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.ByteArray;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public class AttributeFactory {
  public static final int CTX_CLASS = 0;
  
  public static final int CTX_CODE = 3;
  
  public static final int CTX_COUNT = 4;
  
  public static final int CTX_FIELD = 1;
  
  public static final int CTX_METHOD = 2;
  
  public final Attribute parse(DirectClassFile paramDirectClassFile, int paramInt1, int paramInt2, ParseObserver paramParseObserver) {
    if (paramDirectClassFile != null) {
      if (paramInt1 >= 0 && paramInt1 < 4) {
        StringBuilder stringBuilder1;
        String str;
        StringBuilder stringBuilder3 = null;
        try {
          CstString cstString1;
          ByteArray byteArray = paramDirectClassFile.getBytes();
          ConstantPool constantPool = paramDirectClassFile.getConstantPool();
          int i = byteArray.getUnsignedShort(paramInt2);
          int j = paramInt2 + 2;
          int k = byteArray.getInt(j);
          CstString cstString2 = (CstString)constantPool.get(i);
          if (paramParseObserver != null) {
            try {
              stringBuilder3 = new StringBuilder();
              stringBuilder3.append("name: ");
              stringBuilder3.append(cstString2.toHuman());
              paramParseObserver.parsed(byteArray, paramInt2, 2, stringBuilder3.toString());
              stringBuilder3 = new StringBuilder();
              stringBuilder3.append("length: ");
              stringBuilder3.append(Hex.u4(k));
              paramParseObserver.parsed(byteArray, j, 4, stringBuilder3.toString());
              return parse0(paramDirectClassFile, paramInt1, cstString2.getString(), paramInt2 + 6, k, paramParseObserver);
            } catch (ParseException null) {
              cstString1 = cstString2;
            } 
          } else {
            return parse0((DirectClassFile)parseException, paramInt1, cstString2.getString(), paramInt2 + 6, k, (ParseObserver)cstString1);
          } 
        } catch (ParseException parseException) {
          stringBuilder1 = stringBuilder3;
        } 
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("...while parsing ");
        if (stringBuilder1 != null) {
          stringBuilder3 = new StringBuilder();
          stringBuilder3.append(stringBuilder1.toHuman());
          stringBuilder3.append(" ");
          str = stringBuilder3.toString();
        } else {
          str = "";
        } 
        stringBuilder2.append(str);
        stringBuilder2.append("attribute at offset ");
        stringBuilder2.append(Hex.u4(paramInt2));
        parseException.addContext(stringBuilder2.toString());
        throw parseException;
      } 
      throw new IllegalArgumentException("bad context");
    } 
    throw new NullPointerException("cf == null");
  }
  
  protected Attribute parse0(DirectClassFile paramDirectClassFile, int paramInt1, String paramString, int paramInt2, int paramInt3, ParseObserver paramParseObserver) {
    ByteArray byteArray = paramDirectClassFile.getBytes();
    RawAttribute rawAttribute = new RawAttribute(paramString, byteArray, paramInt2, paramInt3, paramDirectClassFile.getConstantPool());
    if (paramParseObserver != null)
      paramParseObserver.parsed(byteArray, paramInt2, paramInt3, "attribute data"); 
    return (Attribute)rawAttribute;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\direct\AttributeFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct;

import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttSourceFile;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.cst.ConstantPoolParser;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.Attribute;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.AttributeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.ClassFile;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.FieldList;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.MethodList;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.ParseException;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.ParseObserver;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.StdAttributeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.AccessFlags;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.ConstantPool;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.StdConstantPool;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.StdTypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.ByteArray;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public class DirectClassFile implements ClassFile {
  private static final int CLASS_FILE_MAGIC = -889275714;
  
  private static final int CLASS_FILE_MAX_MAJOR_VERSION = 51;
  
  private static final int CLASS_FILE_MAX_MINOR_VERSION = 0;
  
  private static final int CLASS_FILE_MIN_MAJOR_VERSION = 45;
  
  private int accessFlags;
  
  private AttributeFactory attributeFactory;
  
  private StdAttributeList attributes;
  
  private final ByteArray bytes;
  
  private FieldList fields;
  
  private final String filePath;
  
  private TypeList interfaces;
  
  private MethodList methods;
  
  private ParseObserver observer;
  
  private StdConstantPool pool;
  
  private final boolean strictParse;
  
  private CstType superClass;
  
  private CstType thisClass;
  
  public DirectClassFile(ByteArray paramByteArray, String paramString, boolean paramBoolean) {
    if (paramByteArray != null) {
      if (paramString != null) {
        this.filePath = paramString;
        this.bytes = paramByteArray;
        this.strictParse = paramBoolean;
        this.accessFlags = -1;
        return;
      } 
      throw new NullPointerException("filePath == null");
    } 
    throw new NullPointerException("bytes == null");
  }
  
  public DirectClassFile(byte[] paramArrayOfbyte, String paramString, boolean paramBoolean) {
    this(new ByteArray(paramArrayOfbyte), paramString, paramBoolean);
  }
  
  private boolean isGoodMagic(int paramInt) {
    return (paramInt == -889275714);
  }
  
  private boolean isGoodVersion(int paramInt1, int paramInt2) {
    if (paramInt1 >= 0)
      if (paramInt2 == 51) {
        if (paramInt1 <= 0)
          return true; 
      } else if (paramInt2 < 51 && paramInt2 >= 45) {
        return true;
      }  
    return false;
  }
  
  private void parse() {
    try {
      parse0();
      return;
    } catch (ParseException parseException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("...while parsing ");
      stringBuilder.append(this.filePath);
      parseException.addContext(stringBuilder.toString());
      throw parseException;
    } catch (RuntimeException runtimeException) {
      ParseException parseException = new ParseException(runtimeException);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("...while parsing ");
      stringBuilder.append(this.filePath);
      parseException.addContext(stringBuilder.toString());
      throw parseException;
    } 
  }
  
  private void parse0() {
    if (this.bytes.size() >= 10) {
      ParseObserver parseObserver2 = this.observer;
      if (parseObserver2 != null) {
        parseObserver2.parsed(this.bytes, 0, 0, "begin classfile");
        parseObserver2 = this.observer;
        ByteArray byteArray1 = this.bytes;
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("magic: ");
        stringBuilder1.append(Hex.u4(getMagic0()));
        parseObserver2.parsed(byteArray1, 0, 4, stringBuilder1.toString());
        parseObserver2 = this.observer;
        byteArray1 = this.bytes;
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append("minor_version: ");
        stringBuilder1.append(Hex.u2(getMinorVersion0()));
        parseObserver2.parsed(byteArray1, 4, 2, stringBuilder1.toString());
        parseObserver2 = this.observer;
        byteArray1 = this.bytes;
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append("major_version: ");
        stringBuilder1.append(Hex.u2(getMajorVersion0()));
        parseObserver2.parsed(byteArray1, 6, 2, stringBuilder1.toString());
      } 
      if (this.strictParse)
        if (isGoodMagic(getMagic0())) {
          if (!isGoodVersion(getMinorVersion0(), getMajorVersion0())) {
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("unsupported class file version ");
            stringBuilder1.append(getMajorVersion0());
            stringBuilder1.append(".");
            stringBuilder1.append(getMinorVersion0());
            throw new ParseException(stringBuilder1.toString());
          } 
        } else {
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("bad class file magic (");
          stringBuilder1.append(Hex.u4(getMagic0()));
          stringBuilder1.append(")");
          throw new ParseException(stringBuilder1.toString());
        }  
      ConstantPoolParser constantPoolParser = new ConstantPoolParser(this.bytes);
      constantPoolParser.setObserver(this.observer);
      StdConstantPool stdConstantPool = constantPoolParser.getPool();
      this.pool = stdConstantPool;
      stdConstantPool.setImmutable();
      int j = constantPoolParser.getEndOffset();
      int i = this.bytes.getUnsignedShort(j);
      ByteArray byteArray = this.bytes;
      int k = j + 2;
      int m = byteArray.getUnsignedShort(k);
      this.thisClass = (CstType)this.pool.get(m);
      byteArray = this.bytes;
      m = j + 4;
      int n = byteArray.getUnsignedShort(m);
      this.superClass = (CstType)this.pool.get0Ok(n);
      byteArray = this.bytes;
      int i1 = j + 6;
      n = byteArray.getUnsignedShort(i1);
      ParseObserver parseObserver1 = this.observer;
      if (parseObserver1 != null) {
        ByteArray byteArray1 = this.bytes;
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("access_flags: ");
        stringBuilder1.append(AccessFlags.classString(i));
        parseObserver1.parsed(byteArray1, j, 2, stringBuilder1.toString());
        parseObserver1 = this.observer;
        byteArray1 = this.bytes;
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append("this_class: ");
        stringBuilder1.append(this.thisClass);
        parseObserver1.parsed(byteArray1, k, 2, stringBuilder1.toString());
        parseObserver1 = this.observer;
        byteArray1 = this.bytes;
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append("super_class: ");
        stringBuilder1.append(stringOrNone(this.superClass));
        parseObserver1.parsed(byteArray1, m, 2, stringBuilder1.toString());
        parseObserver1 = this.observer;
        byteArray1 = this.bytes;
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append("interfaces_count: ");
        stringBuilder1.append(Hex.u2(n));
        parseObserver1.parsed(byteArray1, i1, 2, stringBuilder1.toString());
        if (n != 0)
          this.observer.parsed(this.bytes, j + 8, 0, "interfaces:"); 
      } 
      j += 8;
      this.interfaces = makeTypeList(j, n);
      if (this.strictParse) {
        String str = this.thisClass.getClassType().getClassName();
        if (!this.filePath.endsWith(".class") || !this.filePath.startsWith(str) || this.filePath.length() != str.length() + 6) {
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("class name (");
          stringBuilder1.append(str);
          stringBuilder1.append(") does not match path (");
          stringBuilder1.append(this.filePath);
          stringBuilder1.append(")");
          throw new ParseException(stringBuilder1.toString());
        } 
      } 
      this.accessFlags = i;
      FieldListParser fieldListParser = new FieldListParser(this, this.thisClass, j + n * 2, this.attributeFactory);
      fieldListParser.setObserver(this.observer);
      this.fields = (FieldList)fieldListParser.getList();
      i = fieldListParser.getEndOffset();
      MethodListParser methodListParser = new MethodListParser(this, this.thisClass, i, this.attributeFactory);
      methodListParser.setObserver(this.observer);
      this.methods = (MethodList)methodListParser.getList();
      AttributeListParser attributeListParser = new AttributeListParser(this, 0, methodListParser.getEndOffset(), this.attributeFactory);
      attributeListParser.setObserver(this.observer);
      StdAttributeList stdAttributeList = attributeListParser.getList();
      this.attributes = stdAttributeList;
      stdAttributeList.setImmutable();
      i = attributeListParser.getEndOffset();
      if (i == this.bytes.size()) {
        ParseObserver parseObserver = this.observer;
        if (parseObserver != null)
          parseObserver.parsed(this.bytes, i, 0, "end classfile"); 
        return;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("extra bytes at end of class file, at offset ");
      stringBuilder.append(Hex.u4(i));
      throw new ParseException(stringBuilder.toString());
    } 
    throw new ParseException("severely truncated class file");
  }
  
  private void parseToEndIfNecessary() {
    if (this.attributes == null)
      parse(); 
  }
  
  private void parseToInterfacesIfNecessary() {
    if (this.accessFlags == -1)
      parse(); 
  }
  
  public static String stringOrNone(Object paramObject) {
    return (paramObject == null) ? "(none)" : paramObject.toString();
  }
  
  public int getAccessFlags() {
    parseToInterfacesIfNecessary();
    return this.accessFlags;
  }
  
  public AttributeList getAttributes() {
    parseToEndIfNecessary();
    return (AttributeList)this.attributes;
  }
  
  public ByteArray getBytes() {
    return this.bytes;
  }
  
  public ConstantPool getConstantPool() {
    parseToInterfacesIfNecessary();
    return (ConstantPool)this.pool;
  }
  
  public FieldList getFields() {
    parseToEndIfNecessary();
    return this.fields;
  }
  
  public String getFilePath() {
    return this.filePath;
  }
  
  public TypeList getInterfaces() {
    parseToInterfacesIfNecessary();
    return this.interfaces;
  }
  
  public int getMagic() {
    parseToInterfacesIfNecessary();
    return getMagic0();
  }
  
  public int getMagic0() {
    return this.bytes.getInt(0);
  }
  
  public int getMajorVersion() {
    parseToInterfacesIfNecessary();
    return getMajorVersion0();
  }
  
  public int getMajorVersion0() {
    return this.bytes.getUnsignedShort(6);
  }
  
  public MethodList getMethods() {
    parseToEndIfNecessary();
    return this.methods;
  }
  
  public int getMinorVersion() {
    parseToInterfacesIfNecessary();
    return getMinorVersion0();
  }
  
  public int getMinorVersion0() {
    return this.bytes.getUnsignedShort(4);
  }
  
  public CstString getSourceFile() {
    Attribute attribute = getAttributes().findFirst("SourceFile");
    return (attribute instanceof AttSourceFile) ? ((AttSourceFile)attribute).getSourceFile() : null;
  }
  
  public CstType getSuperclass() {
    parseToInterfacesIfNecessary();
    return this.superClass;
  }
  
  public CstType getThisClass() {
    parseToInterfacesIfNecessary();
    return this.thisClass;
  }
  
  public TypeList makeTypeList(int paramInt1, int paramInt2) {
    if (paramInt2 == 0)
      return (TypeList)StdTypeList.EMPTY; 
    if (this.pool != null)
      return new DcfTypeList(this.bytes, paramInt1, paramInt2, this.pool, this.observer); 
    throw new IllegalStateException("pool not yet initialized");
  }
  
  public void setAttributeFactory(AttributeFactory paramAttributeFactory) {
    if (paramAttributeFactory != null) {
      this.attributeFactory = paramAttributeFactory;
      return;
    } 
    throw new NullPointerException("attributeFactory == null");
  }
  
  public void setObserver(ParseObserver paramParseObserver) {
    this.observer = paramParseObserver;
  }
  
  private static class DcfTypeList implements TypeList {
    private final ByteArray bytes;
    
    private final StdConstantPool pool;
    
    private final int size;
    
    public DcfTypeList(ByteArray param1ByteArray, int param1Int1, int param1Int2, StdConstantPool param1StdConstantPool, ParseObserver param1ParseObserver) {
      if (param1Int2 >= 0) {
        param1ByteArray = param1ByteArray.slice(param1Int1, param1Int2 * 2 + param1Int1);
        this.bytes = param1ByteArray;
        this.size = param1Int2;
        this.pool = param1StdConstantPool;
        param1Int1 = 0;
        while (param1Int1 < param1Int2) {
          int i = param1Int1 * 2;
          int j = param1ByteArray.getUnsignedShort(i);
          try {
            CstType cstType = (CstType)param1StdConstantPool.get(j);
            if (param1ParseObserver != null) {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("  ");
              stringBuilder.append(cstType);
              param1ParseObserver.parsed(param1ByteArray, i, 2, stringBuilder.toString());
            } 
            param1Int1++;
          } catch (ClassCastException classCastException) {
            throw new RuntimeException("bogus class cpi", classCastException);
          } 
        } 
        return;
      } 
      throw new IllegalArgumentException("size < 0");
    }
    
    public Type getType(int param1Int) {
      param1Int = this.bytes.getUnsignedShort(param1Int * 2);
      return ((CstType)this.pool.get(param1Int)).getClassType();
    }
    
    public int getWordCount() {
      return this.size;
    }
    
    public boolean isMutable() {
      return false;
    }
    
    public int size() {
      return this.size;
    }
    
    public TypeList withAddedType(Type param1Type) {
      throw new UnsupportedOperationException("unsupported");
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\direct\DirectClassFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
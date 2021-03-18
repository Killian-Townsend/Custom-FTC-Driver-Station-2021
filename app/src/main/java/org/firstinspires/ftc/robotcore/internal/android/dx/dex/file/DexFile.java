package org.firstinspires.ftc.robotcore.internal.android.dx.dex.file;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Adler32;
import org.firstinspires.ftc.robotcore.internal.android.dex.util.ExceptionWithContext;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.DexOptions;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstBaseMethodRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstEnumRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstFieldRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.ByteArrayAnnotatedOutput;

public final class DexFile {
  private final MixedItemSection byteData;
  
  private final MixedItemSection classData;
  
  private final ClassDefsSection classDefs;
  
  private DexOptions dexOptions;
  
  private int dumpWidth;
  
  private final FieldIdsSection fieldIds;
  
  private int fileSize;
  
  private final HeaderSection header;
  
  private final MixedItemSection map;
  
  private final MethodIdsSection methodIds;
  
  private final ProtoIdsSection protoIds;
  
  private final Section[] sections;
  
  private final MixedItemSection stringData;
  
  private final StringIdsSection stringIds;
  
  private final TypeIdsSection typeIds;
  
  private final MixedItemSection typeLists;
  
  private final MixedItemSection wordData;
  
  public DexFile(DexOptions paramDexOptions) {
    this.dexOptions = paramDexOptions;
    this.header = new HeaderSection(this);
    this.typeLists = new MixedItemSection(null, this, 4, MixedItemSection.SortType.NONE);
    this.wordData = new MixedItemSection("word_data", this, 4, MixedItemSection.SortType.TYPE);
    this.stringData = new MixedItemSection("string_data", this, 1, MixedItemSection.SortType.INSTANCE);
    this.classData = new MixedItemSection(null, this, 1, MixedItemSection.SortType.NONE);
    this.byteData = new MixedItemSection("byte_data", this, 1, MixedItemSection.SortType.TYPE);
    this.stringIds = new StringIdsSection(this);
    this.typeIds = new TypeIdsSection(this);
    this.protoIds = new ProtoIdsSection(this);
    this.fieldIds = new FieldIdsSection(this);
    this.methodIds = new MethodIdsSection(this);
    this.classDefs = new ClassDefsSection(this);
    MixedItemSection mixedItemSection = new MixedItemSection("map", this, 4, MixedItemSection.SortType.NONE);
    this.map = mixedItemSection;
    this.sections = new Section[] { 
        this.header, this.stringIds, this.typeIds, this.protoIds, this.fieldIds, this.methodIds, this.classDefs, this.wordData, this.typeLists, this.stringData, 
        this.byteData, this.classData, mixedItemSection };
    this.fileSize = -1;
    this.dumpWidth = 79;
  }
  
  private static void calcChecksum(byte[] paramArrayOfbyte) {
    Adler32 adler32 = new Adler32();
    adler32.update(paramArrayOfbyte, 12, paramArrayOfbyte.length - 12);
    int i = (int)adler32.getValue();
    paramArrayOfbyte[8] = (byte)i;
    paramArrayOfbyte[9] = (byte)(i >> 8);
    paramArrayOfbyte[10] = (byte)(i >> 16);
    paramArrayOfbyte[11] = (byte)(i >> 24);
  }
  
  private static void calcSignature(byte[] paramArrayOfbyte) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
      messageDigest.update(paramArrayOfbyte, 32, paramArrayOfbyte.length - 32);
      try {
        int i = messageDigest.digest(paramArrayOfbyte, 12, 20);
        if (i == 20)
          return; 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("unexpected digest write: ");
        stringBuilder.append(i);
        stringBuilder.append(" bytes");
        throw new RuntimeException(stringBuilder.toString());
      } catch (DigestException digestException) {
        throw new RuntimeException(digestException);
      } 
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      throw new RuntimeException(noSuchAlgorithmException);
    } 
  }
  
  private ByteArrayAnnotatedOutput toDex0(boolean paramBoolean1, boolean paramBoolean2) {
    ExceptionWithContext exceptionWithContext;
    StringBuilder stringBuilder;
    this.classDefs.prepare();
    this.classData.prepare();
    this.wordData.prepare();
    this.byteData.prepare();
    this.methodIds.prepare();
    this.fieldIds.prepare();
    this.protoIds.prepare();
    this.typeLists.prepare();
    this.typeIds.prepare();
    this.stringIds.prepare();
    this.stringData.prepare();
    this.header.prepare();
    int k = this.sections.length;
    boolean bool = false;
    int i = 0;
    int j = i;
    while (i < k) {
      Section section = this.sections[i];
      int m = section.setFileOffset(j);
      if (m >= j) {
        try {
          if (section == this.map) {
            MapItem.addMap(this.sections, this.map);
            this.map.prepare();
          } 
          if (section instanceof MixedItemSection)
            ((MixedItemSection)section).placeItems(); 
          j = section.writeSize();
          j += m;
          i++;
        } catch (RuntimeException runtimeException) {
          stringBuilder = new StringBuilder();
          stringBuilder.append("...while writing section ");
          stringBuilder.append(i);
          throw ExceptionWithContext.withContext(runtimeException, stringBuilder.toString());
        } 
        continue;
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("bogus placement for section ");
      stringBuilder1.append(i);
      throw new RuntimeException(stringBuilder1.toString());
    } 
    this.fileSize = j;
    byte[] arrayOfByte = new byte[j];
    ByteArrayAnnotatedOutput byteArrayAnnotatedOutput = new ByteArrayAnnotatedOutput(arrayOfByte);
    i = bool;
    if (paramBoolean1) {
      byteArrayAnnotatedOutput.enableAnnotations(this.dumpWidth, paramBoolean2);
      i = bool;
    } 
    while (i < k) {
      try {
        Section section = this.sections[i];
        j = section.getFileOffset() - byteArrayAnnotatedOutput.getCursor();
        if (j >= 0) {
          byteArrayAnnotatedOutput.writeZeroes(section.getFileOffset() - byteArrayAnnotatedOutput.getCursor());
          section.writeTo((AnnotatedOutput)byteArrayAnnotatedOutput);
          i++;
          continue;
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("excess write of ");
        stringBuilder1.append(-j);
        throw new ExceptionWithContext(stringBuilder1.toString());
      } catch (RuntimeException runtimeException) {
        if (runtimeException instanceof ExceptionWithContext) {
          exceptionWithContext = (ExceptionWithContext)runtimeException;
        } else {
          exceptionWithContext = new ExceptionWithContext((Throwable)exceptionWithContext);
        } 
        stringBuilder = new StringBuilder();
        stringBuilder.append("...while writing section ");
        stringBuilder.append(i);
        exceptionWithContext.addContext(stringBuilder.toString());
        throw exceptionWithContext;
      } 
    } 
    if (stringBuilder.getCursor() == this.fileSize) {
      calcSignature((byte[])exceptionWithContext);
      calcChecksum((byte[])exceptionWithContext);
      if (paramBoolean1) {
        this.wordData.writeIndexAnnotation((AnnotatedOutput)stringBuilder, ItemType.TYPE_CODE_ITEM, "\nmethod code index:\n\n");
        getStatistics().writeAnnotation((AnnotatedOutput)stringBuilder);
        stringBuilder.finishAnnotating();
      } 
      return (ByteArrayAnnotatedOutput)stringBuilder;
    } 
    throw new RuntimeException("foreshortened write");
  }
  
  public void add(ClassDefItem paramClassDefItem) {
    this.classDefs.add(paramClassDefItem);
  }
  
  IndexedItem findItemOrNull(Constant paramConstant) {
    return (paramConstant instanceof CstString) ? this.stringIds.get(paramConstant) : ((paramConstant instanceof CstType) ? this.typeIds.get(paramConstant) : ((paramConstant instanceof CstBaseMethodRef) ? this.methodIds.get(paramConstant) : ((paramConstant instanceof CstFieldRef) ? this.fieldIds.get(paramConstant) : null)));
  }
  
  MixedItemSection getByteData() {
    return this.byteData;
  }
  
  MixedItemSection getClassData() {
    return this.classData;
  }
  
  public ClassDefsSection getClassDefs() {
    return this.classDefs;
  }
  
  public ClassDefItem getClassOrNull(String paramString) {
    try {
      Type type = Type.internClassName(paramString);
      return (ClassDefItem)this.classDefs.get((Constant)new CstType(type));
    } catch (IllegalArgumentException illegalArgumentException) {
      return null;
    } 
  }
  
  public DexOptions getDexOptions() {
    return this.dexOptions;
  }
  
  public FieldIdsSection getFieldIds() {
    return this.fieldIds;
  }
  
  public int getFileSize() {
    int i = this.fileSize;
    if (i >= 0)
      return i; 
    throw new RuntimeException("file size not yet known");
  }
  
  Section getFirstDataSection() {
    return this.wordData;
  }
  
  Section getLastDataSection() {
    return this.map;
  }
  
  MixedItemSection getMap() {
    return this.map;
  }
  
  public MethodIdsSection getMethodIds() {
    return this.methodIds;
  }
  
  ProtoIdsSection getProtoIds() {
    return this.protoIds;
  }
  
  public Statistics getStatistics() {
    Statistics statistics = new Statistics();
    Section[] arrayOfSection = this.sections;
    int j = arrayOfSection.length;
    for (int i = 0; i < j; i++)
      statistics.addAll(arrayOfSection[i]); 
    return statistics;
  }
  
  MixedItemSection getStringData() {
    return this.stringData;
  }
  
  StringIdsSection getStringIds() {
    return this.stringIds;
  }
  
  public TypeIdsSection getTypeIds() {
    return this.typeIds;
  }
  
  MixedItemSection getTypeLists() {
    return this.typeLists;
  }
  
  MixedItemSection getWordData() {
    return this.wordData;
  }
  
  void internIfAppropriate(Constant paramConstant) {
    if (paramConstant instanceof CstString) {
      this.stringIds.intern((CstString)paramConstant);
      return;
    } 
    if (paramConstant instanceof CstType) {
      this.typeIds.intern((CstType)paramConstant);
      return;
    } 
    if (paramConstant instanceof CstBaseMethodRef) {
      this.methodIds.intern((CstBaseMethodRef)paramConstant);
      return;
    } 
    if (paramConstant instanceof CstFieldRef) {
      this.fieldIds.intern((CstFieldRef)paramConstant);
      return;
    } 
    if (paramConstant instanceof CstEnumRef) {
      this.fieldIds.intern(((CstEnumRef)paramConstant).getFieldRef());
      return;
    } 
    if (paramConstant != null)
      return; 
    throw new NullPointerException("cst == null");
  }
  
  public boolean isEmpty() {
    return this.classDefs.items().isEmpty();
  }
  
  public void setDumpWidth(int paramInt) {
    if (paramInt >= 40) {
      this.dumpWidth = paramInt;
      return;
    } 
    throw new IllegalArgumentException("dumpWidth < 40");
  }
  
  public byte[] toDex(Writer paramWriter, boolean paramBoolean) throws IOException {
    boolean bool;
    if (paramWriter != null) {
      bool = true;
    } else {
      bool = false;
    } 
    ByteArrayAnnotatedOutput byteArrayAnnotatedOutput = toDex0(bool, paramBoolean);
    if (bool)
      byteArrayAnnotatedOutput.writeAnnotationsTo(paramWriter); 
    return byteArrayAnnotatedOutput.getArray();
  }
  
  public void writeTo(OutputStream paramOutputStream, Writer paramWriter, boolean paramBoolean) throws IOException {
    boolean bool;
    if (paramWriter != null) {
      bool = true;
    } else {
      bool = false;
    } 
    ByteArrayAnnotatedOutput byteArrayAnnotatedOutput = toDex0(bool, paramBoolean);
    if (paramOutputStream != null)
      paramOutputStream.write(byteArrayAnnotatedOutput.getArray()); 
    if (bool)
      byteArrayAnnotatedOutput.writeAnnotationsTo(paramWriter); 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\file\DexFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
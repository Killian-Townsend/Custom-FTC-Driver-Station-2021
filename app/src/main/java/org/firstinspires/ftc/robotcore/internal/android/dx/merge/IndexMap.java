package org.firstinspires.ftc.robotcore.internal.android.dx.merge;

import java.util.HashMap;
import org.firstinspires.ftc.robotcore.internal.android.dex.Annotation;
import org.firstinspires.ftc.robotcore.internal.android.dex.ClassDef;
import org.firstinspires.ftc.robotcore.internal.android.dex.Dex;
import org.firstinspires.ftc.robotcore.internal.android.dex.EncodedValue;
import org.firstinspires.ftc.robotcore.internal.android.dex.EncodedValueReader;
import org.firstinspires.ftc.robotcore.internal.android.dex.FieldId;
import org.firstinspires.ftc.robotcore.internal.android.dex.Leb128;
import org.firstinspires.ftc.robotcore.internal.android.dex.MethodId;
import org.firstinspires.ftc.robotcore.internal.android.dex.ProtoId;
import org.firstinspires.ftc.robotcore.internal.android.dex.TableOfContents;
import org.firstinspires.ftc.robotcore.internal.android.dex.TypeList;
import org.firstinspires.ftc.robotcore.internal.android.dex.util.ByteOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.ByteArrayAnnotatedOutput;

public final class IndexMap {
  private final HashMap<Integer, Integer> annotationDirectoryOffsets;
  
  private final HashMap<Integer, Integer> annotationOffsets;
  
  private final HashMap<Integer, Integer> annotationSetOffsets;
  
  private final HashMap<Integer, Integer> annotationSetRefListOffsets;
  
  public final short[] fieldIds;
  
  public final short[] methodIds;
  
  public final short[] protoIds;
  
  private final HashMap<Integer, Integer> staticValuesOffsets;
  
  public final int[] stringIds;
  
  private final Dex target;
  
  public final short[] typeIds;
  
  private final HashMap<Integer, Integer> typeListOffsets;
  
  public IndexMap(Dex paramDex, TableOfContents paramTableOfContents) {
    this.target = paramDex;
    this.stringIds = new int[paramTableOfContents.stringIds.size];
    this.typeIds = new short[paramTableOfContents.typeIds.size];
    this.protoIds = new short[paramTableOfContents.protoIds.size];
    this.fieldIds = new short[paramTableOfContents.fieldIds.size];
    this.methodIds = new short[paramTableOfContents.methodIds.size];
    this.typeListOffsets = new HashMap<Integer, Integer>();
    this.annotationOffsets = new HashMap<Integer, Integer>();
    this.annotationSetOffsets = new HashMap<Integer, Integer>();
    this.annotationSetRefListOffsets = new HashMap<Integer, Integer>();
    this.annotationDirectoryOffsets = new HashMap<Integer, Integer>();
    this.staticValuesOffsets = new HashMap<Integer, Integer>();
    HashMap<Integer, Integer> hashMap = this.typeListOffsets;
    Integer integer = Integer.valueOf(0);
    hashMap.put(integer, integer);
    this.annotationSetOffsets.put(integer, integer);
    this.annotationDirectoryOffsets.put(integer, integer);
    this.staticValuesOffsets.put(integer, integer);
  }
  
  public Annotation adjust(Annotation paramAnnotation) {
    ByteArrayAnnotatedOutput byteArrayAnnotatedOutput = new ByteArrayAnnotatedOutput(32);
    (new EncodedValueTransformer((ByteOutput)byteArrayAnnotatedOutput)).transformAnnotation(paramAnnotation.getReader());
    return new Annotation(this.target, paramAnnotation.getVisibility(), new EncodedValue(byteArrayAnnotatedOutput.toByteArray()));
  }
  
  public ClassDef adjust(ClassDef paramClassDef) {
    return new ClassDef(this.target, paramClassDef.getOffset(), adjustType(paramClassDef.getTypeIndex()), paramClassDef.getAccessFlags(), adjustType(paramClassDef.getSupertypeIndex()), adjustTypeListOffset(paramClassDef.getInterfacesOffset()), paramClassDef.getSourceFileIndex(), paramClassDef.getAnnotationsOffset(), paramClassDef.getClassDataOffset(), paramClassDef.getStaticValuesOffset());
  }
  
  public FieldId adjust(FieldId paramFieldId) {
    return new FieldId(this.target, adjustType(paramFieldId.getDeclaringClassIndex()), adjustType(paramFieldId.getTypeIndex()), adjustString(paramFieldId.getNameIndex()));
  }
  
  public MethodId adjust(MethodId paramMethodId) {
    return new MethodId(this.target, adjustType(paramMethodId.getDeclaringClassIndex()), adjustProto(paramMethodId.getProtoIndex()), adjustString(paramMethodId.getNameIndex()));
  }
  
  public ProtoId adjust(ProtoId paramProtoId) {
    return new ProtoId(this.target, adjustString(paramProtoId.getShortyIndex()), adjustType(paramProtoId.getReturnTypeIndex()), adjustTypeListOffset(paramProtoId.getParametersOffset()));
  }
  
  public SortableType adjust(SortableType paramSortableType) {
    return new SortableType(paramSortableType.getDex(), paramSortableType.getIndexMap(), adjust(paramSortableType.getClassDef()));
  }
  
  public int adjustAnnotation(int paramInt) {
    return ((Integer)this.annotationOffsets.get(Integer.valueOf(paramInt))).intValue();
  }
  
  public int adjustAnnotationDirectory(int paramInt) {
    return ((Integer)this.annotationDirectoryOffsets.get(Integer.valueOf(paramInt))).intValue();
  }
  
  public int adjustAnnotationSet(int paramInt) {
    return ((Integer)this.annotationSetOffsets.get(Integer.valueOf(paramInt))).intValue();
  }
  
  public int adjustAnnotationSetRefList(int paramInt) {
    return ((Integer)this.annotationSetRefListOffsets.get(Integer.valueOf(paramInt))).intValue();
  }
  
  public EncodedValue adjustEncodedArray(EncodedValue paramEncodedValue) {
    ByteArrayAnnotatedOutput byteArrayAnnotatedOutput = new ByteArrayAnnotatedOutput(32);
    (new EncodedValueTransformer((ByteOutput)byteArrayAnnotatedOutput)).transformArray(new EncodedValueReader(paramEncodedValue, 28));
    return new EncodedValue(byteArrayAnnotatedOutput.toByteArray());
  }
  
  public EncodedValue adjustEncodedValue(EncodedValue paramEncodedValue) {
    ByteArrayAnnotatedOutput byteArrayAnnotatedOutput = new ByteArrayAnnotatedOutput(32);
    (new EncodedValueTransformer((ByteOutput)byteArrayAnnotatedOutput)).transform(new EncodedValueReader(paramEncodedValue));
    return new EncodedValue(byteArrayAnnotatedOutput.toByteArray());
  }
  
  public int adjustField(int paramInt) {
    return this.fieldIds[paramInt] & 0xFFFF;
  }
  
  public int adjustMethod(int paramInt) {
    return this.methodIds[paramInt] & 0xFFFF;
  }
  
  public int adjustProto(int paramInt) {
    return this.protoIds[paramInt] & 0xFFFF;
  }
  
  public int adjustStaticValues(int paramInt) {
    return ((Integer)this.staticValuesOffsets.get(Integer.valueOf(paramInt))).intValue();
  }
  
  public int adjustString(int paramInt) {
    return (paramInt == -1) ? -1 : this.stringIds[paramInt];
  }
  
  public int adjustType(int paramInt) {
    return (paramInt == -1) ? -1 : (0xFFFF & this.typeIds[paramInt]);
  }
  
  public TypeList adjustTypeList(TypeList paramTypeList) {
    if (paramTypeList == TypeList.EMPTY)
      return paramTypeList; 
    short[] arrayOfShort = (short[])paramTypeList.getTypes().clone();
    for (int i = 0; i < arrayOfShort.length; i++)
      arrayOfShort[i] = (short)adjustType(arrayOfShort[i]); 
    return new TypeList(this.target, arrayOfShort);
  }
  
  public int adjustTypeListOffset(int paramInt) {
    return ((Integer)this.typeListOffsets.get(Integer.valueOf(paramInt))).intValue();
  }
  
  public void putAnnotationDirectoryOffset(int paramInt1, int paramInt2) {
    if (paramInt1 > 0 && paramInt2 > 0) {
      this.annotationDirectoryOffsets.put(Integer.valueOf(paramInt1), Integer.valueOf(paramInt2));
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  public void putAnnotationOffset(int paramInt1, int paramInt2) {
    if (paramInt1 > 0 && paramInt2 > 0) {
      this.annotationOffsets.put(Integer.valueOf(paramInt1), Integer.valueOf(paramInt2));
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  public void putAnnotationSetOffset(int paramInt1, int paramInt2) {
    if (paramInt1 > 0 && paramInt2 > 0) {
      this.annotationSetOffsets.put(Integer.valueOf(paramInt1), Integer.valueOf(paramInt2));
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  public void putAnnotationSetRefListOffset(int paramInt1, int paramInt2) {
    if (paramInt1 > 0 && paramInt2 > 0) {
      this.annotationSetRefListOffsets.put(Integer.valueOf(paramInt1), Integer.valueOf(paramInt2));
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  public void putStaticValuesOffset(int paramInt1, int paramInt2) {
    if (paramInt1 > 0 && paramInt2 > 0) {
      this.staticValuesOffsets.put(Integer.valueOf(paramInt1), Integer.valueOf(paramInt2));
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  public void putTypeListOffset(int paramInt1, int paramInt2) {
    if (paramInt1 > 0 && paramInt2 > 0) {
      this.typeListOffsets.put(Integer.valueOf(paramInt1), Integer.valueOf(paramInt2));
      return;
    } 
    throw new IllegalArgumentException();
  }
  
  private final class EncodedValueTransformer {
    private final ByteOutput out;
    
    public EncodedValueTransformer(ByteOutput param1ByteOutput) {
      this.out = param1ByteOutput;
    }
    
    private void transformAnnotation(EncodedValueReader param1EncodedValueReader) {
      int j = param1EncodedValueReader.readAnnotation();
      Leb128.writeUnsignedLeb128(this.out, IndexMap.this.adjustType(param1EncodedValueReader.getAnnotationType()));
      Leb128.writeUnsignedLeb128(this.out, j);
      for (int i = 0; i < j; i++) {
        Leb128.writeUnsignedLeb128(this.out, IndexMap.this.adjustString(param1EncodedValueReader.readAnnotationName()));
        transform(param1EncodedValueReader);
      } 
    }
    
    private void transformArray(EncodedValueReader param1EncodedValueReader) {
      int j = param1EncodedValueReader.readArray();
      Leb128.writeUnsignedLeb128(this.out, j);
      for (int i = 0; i < j; i++)
        transform(param1EncodedValueReader); 
    }
    
    private void writeTypeAndArg(int param1Int1, int param1Int2) {
      this.out.writeByte(param1Int1 | param1Int2 << 5);
    }
    
    public void transform(EncodedValueReader param1EncodedValueReader) {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:659)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\merge\IndexMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
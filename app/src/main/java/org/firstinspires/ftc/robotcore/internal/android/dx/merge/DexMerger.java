package org.firstinspires.ftc.robotcore.internal.android.dx.merge;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.firstinspires.ftc.robotcore.internal.android.dex.Annotation;
import org.firstinspires.ftc.robotcore.internal.android.dex.ClassData;
import org.firstinspires.ftc.robotcore.internal.android.dex.ClassDef;
import org.firstinspires.ftc.robotcore.internal.android.dex.Code;
import org.firstinspires.ftc.robotcore.internal.android.dex.Dex;
import org.firstinspires.ftc.robotcore.internal.android.dex.DexException;
import org.firstinspires.ftc.robotcore.internal.android.dex.DexIndexOverflowException;
import org.firstinspires.ftc.robotcore.internal.android.dex.FieldId;
import org.firstinspires.ftc.robotcore.internal.android.dex.MethodId;
import org.firstinspires.ftc.robotcore.internal.android.dex.ProtoId;
import org.firstinspires.ftc.robotcore.internal.android.dex.TableOfContents;
import org.firstinspires.ftc.robotcore.internal.android.dex.TypeList;

public final class DexMerger {
  private static final byte DBG_ADVANCE_LINE = 2;
  
  private static final byte DBG_ADVANCE_PC = 1;
  
  private static final byte DBG_END_LOCAL = 5;
  
  private static final byte DBG_END_SEQUENCE = 0;
  
  private static final byte DBG_RESTART_LOCAL = 6;
  
  private static final byte DBG_SET_EPILOGUE_BEGIN = 8;
  
  private static final byte DBG_SET_FILE = 9;
  
  private static final byte DBG_SET_PROLOGUE_END = 7;
  
  private static final byte DBG_START_LOCAL = 3;
  
  private static final byte DBG_START_LOCAL_EXTENDED = 4;
  
  private final Dex.Section annotationOut;
  
  private final Dex.Section annotationSetOut;
  
  private final Dex.Section annotationSetRefListOut;
  
  private final Dex.Section annotationsDirectoryOut;
  
  private final Dex.Section classDataOut;
  
  private final Dex.Section codeOut;
  
  private final CollisionPolicy collisionPolicy;
  
  private int compactWasteThreshold = 1048576;
  
  private final TableOfContents contentsOut;
  
  private final Dex.Section debugInfoOut;
  
  private final Dex dexOut;
  
  private final Dex[] dexes;
  
  private final Dex.Section encodedArrayOut;
  
  private final Dex.Section headerOut;
  
  private final Dex.Section idsDefsOut;
  
  private final IndexMap[] indexMaps;
  
  private final InstructionTransformer instructionTransformer;
  
  private final Dex.Section mapListOut;
  
  private final Dex.Section stringDataOut;
  
  private final Dex.Section typeListOut;
  
  private final WriterSizes writerSizes;
  
  public DexMerger(Dex[] paramArrayOfDex, CollisionPolicy paramCollisionPolicy) throws IOException {
    this(paramArrayOfDex, paramCollisionPolicy, new WriterSizes(paramArrayOfDex));
  }
  
  private DexMerger(Dex[] paramArrayOfDex, CollisionPolicy paramCollisionPolicy, WriterSizes paramWriterSizes) throws IOException {
    this.dexes = paramArrayOfDex;
    this.collisionPolicy = paramCollisionPolicy;
    this.writerSizes = paramWriterSizes;
    this.dexOut = new Dex(paramWriterSizes.size());
    this.indexMaps = new IndexMap[paramArrayOfDex.length];
    int i;
    for (i = 0; i < paramArrayOfDex.length; i++)
      this.indexMaps[i] = new IndexMap(this.dexOut, paramArrayOfDex[i].getTableOfContents()); 
    this.instructionTransformer = new InstructionTransformer();
    this.headerOut = this.dexOut.appendSection(paramWriterSizes.header, "header");
    this.idsDefsOut = this.dexOut.appendSection(paramWriterSizes.idsDefs, "ids defs");
    TableOfContents tableOfContents = this.dexOut.getTableOfContents();
    this.contentsOut = tableOfContents;
    tableOfContents.dataOff = this.dexOut.getNextSectionStart();
    this.contentsOut.mapList.off = this.dexOut.getNextSectionStart();
    this.contentsOut.mapList.size = 1;
    this.mapListOut = this.dexOut.appendSection(paramWriterSizes.mapList, "map list");
    this.contentsOut.typeLists.off = this.dexOut.getNextSectionStart();
    this.typeListOut = this.dexOut.appendSection(paramWriterSizes.typeList, "type list");
    this.contentsOut.annotationSetRefLists.off = this.dexOut.getNextSectionStart();
    this.annotationSetRefListOut = this.dexOut.appendSection(paramWriterSizes.annotationsSetRefList, "annotation set ref list");
    this.contentsOut.annotationSets.off = this.dexOut.getNextSectionStart();
    this.annotationSetOut = this.dexOut.appendSection(paramWriterSizes.annotationsSet, "annotation sets");
    this.contentsOut.classDatas.off = this.dexOut.getNextSectionStart();
    this.classDataOut = this.dexOut.appendSection(paramWriterSizes.classData, "class data");
    this.contentsOut.codes.off = this.dexOut.getNextSectionStart();
    this.codeOut = this.dexOut.appendSection(paramWriterSizes.code, "code");
    this.contentsOut.stringDatas.off = this.dexOut.getNextSectionStart();
    this.stringDataOut = this.dexOut.appendSection(paramWriterSizes.stringData, "string data");
    this.contentsOut.debugInfos.off = this.dexOut.getNextSectionStart();
    this.debugInfoOut = this.dexOut.appendSection(paramWriterSizes.debugInfo, "debug info");
    this.contentsOut.annotations.off = this.dexOut.getNextSectionStart();
    this.annotationOut = this.dexOut.appendSection(paramWriterSizes.annotation, "annotation");
    this.contentsOut.encodedArrays.off = this.dexOut.getNextSectionStart();
    this.encodedArrayOut = this.dexOut.appendSection(paramWriterSizes.encodedArray, "encoded array");
    this.contentsOut.annotationsDirectories.off = this.dexOut.getNextSectionStart();
    this.annotationsDirectoryOut = this.dexOut.appendSection(paramWriterSizes.annotationsDirectory, "annotations directory");
    this.contentsOut.dataSize = this.dexOut.getNextSectionStart() - this.contentsOut.dataOff;
  }
  
  private SortableType[] getSortedTypes() {
    int j = this.contentsOut.typeIds.size;
    SortableType[] arrayOfSortableType = new SortableType[j];
    int i = 0;
    while (true) {
      Dex[] arrayOfDex = this.dexes;
      if (i < arrayOfDex.length) {
        readSortableTypes(arrayOfSortableType, arrayOfDex[i], this.indexMaps[i]);
        i++;
        continue;
      } 
      break;
    } 
    while (true) {
      boolean bool = true;
      i = 0;
      while (i < j) {
        SortableType sortableType = arrayOfSortableType[i];
        boolean bool1 = bool;
        if (sortableType != null) {
          bool1 = bool;
          if (!sortableType.isDepthAssigned())
            bool1 = bool & sortableType.tryAssignDepth(arrayOfSortableType); 
        } 
        i++;
        bool = bool1;
      } 
      if (bool) {
        Arrays.sort(arrayOfSortableType, SortableType.NULLS_LAST_ORDER);
        i = Arrays.<SortableType>asList(arrayOfSortableType).indexOf(null);
        SortableType[] arrayOfSortableType1 = arrayOfSortableType;
        if (i != -1)
          arrayOfSortableType1 = Arrays.<SortableType>copyOfRange(arrayOfSortableType, 0, i); 
        return arrayOfSortableType1;
      } 
    } 
  }
  
  public static void main(String[] paramArrayOfString) throws IOException {
    if (paramArrayOfString.length < 2) {
      printUsage();
      return;
    } 
    int j = paramArrayOfString.length;
    int i = 1;
    Dex[] arrayOfDex = new Dex[j - 1];
    while (i < paramArrayOfString.length) {
      arrayOfDex[i - 1] = new Dex(new File(paramArrayOfString[i]));
      i++;
    } 
    (new DexMerger(arrayOfDex, CollisionPolicy.KEEP_FIRST)).merge().writeTo(new File(paramArrayOfString[0]));
  }
  
  private void mergeAnnotations() {
    (new IdMerger<Annotation>(this.annotationOut) {
        TableOfContents.Section getSection(TableOfContents param1TableOfContents) {
          return param1TableOfContents.annotations;
        }
        
        Annotation read(Dex.Section param1Section, IndexMap param1IndexMap, int param1Int) {
          return param1IndexMap.adjust(param1Section.readAnnotation());
        }
        
        void updateIndex(int param1Int1, IndexMap param1IndexMap, int param1Int2, int param1Int3) {
          param1IndexMap.putAnnotationOffset(param1Int1, DexMerger.this.annotationOut.getPosition());
        }
        
        void write(Annotation param1Annotation) {
          param1Annotation.writeTo(DexMerger.this.annotationOut);
        }
      }).mergeUnsorted();
  }
  
  private int mergeApiLevels() {
    int j = -1;
    int i = 0;
    while (true) {
      Dex[] arrayOfDex = this.dexes;
      if (i < arrayOfDex.length) {
        int m = (arrayOfDex[i].getTableOfContents()).apiLevel;
        int k = j;
        if (j < m)
          k = m; 
        i++;
        j = k;
        continue;
      } 
      return j;
    } 
  }
  
  private void mergeClassDefs() {
    SortableType[] arrayOfSortableType = getSortedTypes();
    this.contentsOut.classDefs.off = this.idsDefsOut.getPosition();
    this.contentsOut.classDefs.size = arrayOfSortableType.length;
    int j = arrayOfSortableType.length;
    for (int i = 0; i < j; i++) {
      SortableType sortableType = arrayOfSortableType[i];
      transformClassDef(sortableType.getDex(), sortableType.getClassDef(), sortableType.getIndexMap());
    } 
  }
  
  private Dex mergeDexes() throws IOException {
    mergeStringIds();
    mergeTypeIds();
    mergeTypeLists();
    mergeProtoIds();
    mergeFieldIds();
    mergeMethodIds();
    mergeAnnotations();
    unionAnnotationSetsAndDirectories();
    mergeClassDefs();
    this.contentsOut.header.off = 0;
    this.contentsOut.header.size = 1;
    this.contentsOut.fileSize = this.dexOut.getLength();
    this.contentsOut.computeSizesFromOffsets();
    this.contentsOut.writeHeader(this.headerOut, mergeApiLevels());
    this.contentsOut.writeMap(this.mapListOut);
    this.dexOut.writeHashes();
    return this.dexOut;
  }
  
  private void mergeFieldIds() {
    (new IdMerger<FieldId>(this.idsDefsOut) {
        TableOfContents.Section getSection(TableOfContents param1TableOfContents) {
          return param1TableOfContents.fieldIds;
        }
        
        FieldId read(Dex.Section param1Section, IndexMap param1IndexMap, int param1Int) {
          return param1IndexMap.adjust(param1Section.readFieldId());
        }
        
        void updateIndex(int param1Int1, IndexMap param1IndexMap, int param1Int2, int param1Int3) {
          if (param1Int3 >= 0 && param1Int3 <= 65535) {
            param1IndexMap.fieldIds[param1Int2] = (short)param1Int3;
            return;
          } 
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("field ID not in [0, 0xffff]: ");
          stringBuilder.append(param1Int3);
          throw new DexIndexOverflowException(stringBuilder.toString());
        }
        
        void write(FieldId param1FieldId) {
          param1FieldId.writeTo(DexMerger.this.idsDefsOut);
        }
      }).mergeSorted();
  }
  
  private void mergeMethodIds() {
    (new IdMerger<MethodId>(this.idsDefsOut) {
        TableOfContents.Section getSection(TableOfContents param1TableOfContents) {
          return param1TableOfContents.methodIds;
        }
        
        MethodId read(Dex.Section param1Section, IndexMap param1IndexMap, int param1Int) {
          return param1IndexMap.adjust(param1Section.readMethodId());
        }
        
        void updateIndex(int param1Int1, IndexMap param1IndexMap, int param1Int2, int param1Int3) {
          if (param1Int3 >= 0 && param1Int3 <= 65535) {
            param1IndexMap.methodIds[param1Int2] = (short)param1Int3;
            return;
          } 
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("method ID not in [0, 0xffff]: ");
          stringBuilder.append(param1Int3);
          throw new DexIndexOverflowException(stringBuilder.toString());
        }
        
        void write(MethodId param1MethodId) {
          param1MethodId.writeTo(DexMerger.this.idsDefsOut);
        }
      }).mergeSorted();
  }
  
  private void mergeProtoIds() {
    (new IdMerger<ProtoId>(this.idsDefsOut) {
        TableOfContents.Section getSection(TableOfContents param1TableOfContents) {
          return param1TableOfContents.protoIds;
        }
        
        ProtoId read(Dex.Section param1Section, IndexMap param1IndexMap, int param1Int) {
          return param1IndexMap.adjust(param1Section.readProtoId());
        }
        
        void updateIndex(int param1Int1, IndexMap param1IndexMap, int param1Int2, int param1Int3) {
          if (param1Int3 >= 0 && param1Int3 <= 65535) {
            param1IndexMap.protoIds[param1Int2] = (short)param1Int3;
            return;
          } 
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("proto ID not in [0, 0xffff]: ");
          stringBuilder.append(param1Int3);
          throw new DexIndexOverflowException(stringBuilder.toString());
        }
        
        void write(ProtoId param1ProtoId) {
          param1ProtoId.writeTo(DexMerger.this.idsDefsOut);
        }
      }).mergeSorted();
  }
  
  private void mergeStringIds() {
    (new IdMerger<String>(this.idsDefsOut) {
        TableOfContents.Section getSection(TableOfContents param1TableOfContents) {
          return param1TableOfContents.stringIds;
        }
        
        String read(Dex.Section param1Section, IndexMap param1IndexMap, int param1Int) {
          return param1Section.readString();
        }
        
        void updateIndex(int param1Int1, IndexMap param1IndexMap, int param1Int2, int param1Int3) {
          param1IndexMap.stringIds[param1Int2] = param1Int3;
        }
        
        void write(String param1String) {
          TableOfContents.Section section = DexMerger.this.contentsOut.stringDatas;
          section.size++;
          DexMerger.this.idsDefsOut.writeInt(DexMerger.this.stringDataOut.getPosition());
          DexMerger.this.stringDataOut.writeStringData(param1String);
        }
      }).mergeSorted();
  }
  
  private void mergeTypeIds() {
    (new IdMerger<Integer>(this.idsDefsOut) {
        TableOfContents.Section getSection(TableOfContents param1TableOfContents) {
          return param1TableOfContents.typeIds;
        }
        
        Integer read(Dex.Section param1Section, IndexMap param1IndexMap, int param1Int) {
          return Integer.valueOf(param1IndexMap.adjustString(param1Section.readInt()));
        }
        
        void updateIndex(int param1Int1, IndexMap param1IndexMap, int param1Int2, int param1Int3) {
          if (param1Int3 >= 0 && param1Int3 <= 65535) {
            param1IndexMap.typeIds[param1Int2] = (short)param1Int3;
            return;
          } 
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("type ID not in [0, 0xffff]: ");
          stringBuilder.append(param1Int3);
          throw new DexIndexOverflowException(stringBuilder.toString());
        }
        
        void write(Integer param1Integer) {
          DexMerger.this.idsDefsOut.writeInt(param1Integer.intValue());
        }
      }).mergeSorted();
  }
  
  private void mergeTypeLists() {
    (new IdMerger<TypeList>(this.typeListOut) {
        TableOfContents.Section getSection(TableOfContents param1TableOfContents) {
          return param1TableOfContents.typeLists;
        }
        
        TypeList read(Dex.Section param1Section, IndexMap param1IndexMap, int param1Int) {
          return param1IndexMap.adjustTypeList(param1Section.readTypeList());
        }
        
        void updateIndex(int param1Int1, IndexMap param1IndexMap, int param1Int2, int param1Int3) {
          param1IndexMap.putTypeListOffset(param1Int1, DexMerger.this.typeListOut.getPosition());
        }
        
        void write(TypeList param1TypeList) {
          DexMerger.this.typeListOut.writeTypeList(param1TypeList);
        }
      }).mergeUnsorted();
  }
  
  private static void printUsage() {
    System.out.println("Usage: DexMerger <out.dex> <a.dex> <b.dex> ...");
    System.out.println();
    System.out.println("If a class is defined in several dex, the class found in the first dex will be used.");
  }
  
  private void readSortableTypes(SortableType[] paramArrayOfSortableType, Dex paramDex, IndexMap paramIndexMap) {
    for (ClassDef classDef : paramDex.classDefs()) {
      SortableType sortableType = paramIndexMap.adjust(new SortableType(paramDex, paramIndexMap, classDef));
      int i = sortableType.getTypeIndex();
      if (paramArrayOfSortableType[i] == null) {
        paramArrayOfSortableType[i] = sortableType;
        continue;
      } 
      if (this.collisionPolicy == CollisionPolicy.KEEP_FIRST)
        continue; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Multiple dex files define ");
      stringBuilder.append(paramDex.typeNames().get(classDef.getTypeIndex()));
      throw new DexException(stringBuilder.toString());
    } 
  }
  
  private void transformAnnotationDirectories(Dex paramDex, IndexMap paramIndexMap) {
    TableOfContents.Section section = (paramDex.getTableOfContents()).annotationsDirectories;
    if (section.exists()) {
      Dex.Section section1 = paramDex.open(section.off);
      for (int i = 0; i < section.size; i++)
        transformAnnotationDirectory(section1, paramIndexMap); 
    } 
  }
  
  private void transformAnnotationDirectory(Dex.Section paramSection, IndexMap paramIndexMap) {
    TableOfContents.Section section = this.contentsOut.annotationsDirectories;
    section.size++;
    this.annotationsDirectoryOut.assertFourByteAligned();
    paramIndexMap.putAnnotationDirectoryOffset(paramSection.getPosition(), this.annotationsDirectoryOut.getPosition());
    int i = paramIndexMap.adjustAnnotationSet(paramSection.readInt());
    this.annotationsDirectoryOut.writeInt(i);
    int j = paramSection.readInt();
    this.annotationsDirectoryOut.writeInt(j);
    int m = paramSection.readInt();
    this.annotationsDirectoryOut.writeInt(m);
    int k = paramSection.readInt();
    this.annotationsDirectoryOut.writeInt(k);
    boolean bool = false;
    for (i = 0; i < j; i++) {
      this.annotationsDirectoryOut.writeInt(paramIndexMap.adjustField(paramSection.readInt()));
      this.annotationsDirectoryOut.writeInt(paramIndexMap.adjustAnnotationSet(paramSection.readInt()));
    } 
    i = 0;
    while (true) {
      j = bool;
      if (i < m) {
        this.annotationsDirectoryOut.writeInt(paramIndexMap.adjustMethod(paramSection.readInt()));
        this.annotationsDirectoryOut.writeInt(paramIndexMap.adjustAnnotationSet(paramSection.readInt()));
        i++;
        continue;
      } 
      break;
    } 
    while (j < k) {
      this.annotationsDirectoryOut.writeInt(paramIndexMap.adjustMethod(paramSection.readInt()));
      this.annotationsDirectoryOut.writeInt(paramIndexMap.adjustAnnotationSetRefList(paramSection.readInt()));
      j++;
    } 
  }
  
  private void transformAnnotationSet(IndexMap paramIndexMap, Dex.Section paramSection) {
    TableOfContents.Section section = this.contentsOut.annotationSets;
    section.size++;
    this.annotationSetOut.assertFourByteAligned();
    paramIndexMap.putAnnotationSetOffset(paramSection.getPosition(), this.annotationSetOut.getPosition());
    int j = paramSection.readInt();
    this.annotationSetOut.writeInt(j);
    for (int i = 0; i < j; i++)
      this.annotationSetOut.writeInt(paramIndexMap.adjustAnnotation(paramSection.readInt())); 
  }
  
  private void transformAnnotationSetRefList(IndexMap paramIndexMap, Dex.Section paramSection) {
    TableOfContents.Section section = this.contentsOut.annotationSetRefLists;
    section.size++;
    this.annotationSetRefListOut.assertFourByteAligned();
    paramIndexMap.putAnnotationSetRefListOffset(paramSection.getPosition(), this.annotationSetRefListOut.getPosition());
    int j = paramSection.readInt();
    this.annotationSetRefListOut.writeInt(j);
    for (int i = 0; i < j; i++)
      this.annotationSetRefListOut.writeInt(paramIndexMap.adjustAnnotationSet(paramSection.readInt())); 
  }
  
  private void transformAnnotationSetRefLists(Dex paramDex, IndexMap paramIndexMap) {
    TableOfContents.Section section = (paramDex.getTableOfContents()).annotationSetRefLists;
    if (section.exists()) {
      Dex.Section section1 = paramDex.open(section.off);
      for (int i = 0; i < section.size; i++)
        transformAnnotationSetRefList(paramIndexMap, section1); 
    } 
  }
  
  private void transformAnnotationSets(Dex paramDex, IndexMap paramIndexMap) {
    TableOfContents.Section section = (paramDex.getTableOfContents()).annotationSets;
    if (section.exists()) {
      Dex.Section section1 = paramDex.open(section.off);
      for (int i = 0; i < section.size; i++)
        transformAnnotationSet(paramIndexMap, section1); 
    } 
  }
  
  private int[] transformCatchHandlers(IndexMap paramIndexMap, Code.CatchHandler[] paramArrayOfCatchHandler) {
    int j = this.codeOut.getPosition();
    this.codeOut.writeUleb128(paramArrayOfCatchHandler.length);
    int[] arrayOfInt = new int[paramArrayOfCatchHandler.length];
    for (int i = 0; i < paramArrayOfCatchHandler.length; i++) {
      arrayOfInt[i] = this.codeOut.getPosition() - j;
      transformEncodedCatchHandler(paramArrayOfCatchHandler[i], paramIndexMap);
    } 
    return arrayOfInt;
  }
  
  private void transformClassData(Dex paramDex, ClassData paramClassData, IndexMap paramIndexMap) {
    TableOfContents.Section section = this.contentsOut.classDatas;
    section.size++;
    ClassData.Field[] arrayOfField1 = paramClassData.getStaticFields();
    ClassData.Field[] arrayOfField2 = paramClassData.getInstanceFields();
    ClassData.Method[] arrayOfMethod2 = paramClassData.getDirectMethods();
    ClassData.Method[] arrayOfMethod1 = paramClassData.getVirtualMethods();
    this.classDataOut.writeUleb128(arrayOfField1.length);
    this.classDataOut.writeUleb128(arrayOfField2.length);
    this.classDataOut.writeUleb128(arrayOfMethod2.length);
    this.classDataOut.writeUleb128(arrayOfMethod1.length);
    transformFields(paramIndexMap, arrayOfField1);
    transformFields(paramIndexMap, arrayOfField2);
    transformMethods(paramDex, paramIndexMap, arrayOfMethod2);
    transformMethods(paramDex, paramIndexMap, arrayOfMethod1);
  }
  
  private void transformClassDef(Dex paramDex, ClassDef paramClassDef, IndexMap paramIndexMap) {
    this.idsDefsOut.assertFourByteAligned();
    this.idsDefsOut.writeInt(paramClassDef.getTypeIndex());
    this.idsDefsOut.writeInt(paramClassDef.getAccessFlags());
    this.idsDefsOut.writeInt(paramClassDef.getSupertypeIndex());
    this.idsDefsOut.writeInt(paramClassDef.getInterfacesOffset());
    int i = paramIndexMap.adjustString(paramClassDef.getSourceFileIndex());
    this.idsDefsOut.writeInt(i);
    i = paramClassDef.getAnnotationsOffset();
    this.idsDefsOut.writeInt(paramIndexMap.adjustAnnotationDirectory(i));
    if (paramClassDef.getClassDataOffset() == 0) {
      this.idsDefsOut.writeInt(0);
    } else {
      this.idsDefsOut.writeInt(this.classDataOut.getPosition());
      transformClassData(paramDex, paramDex.readClassData(paramClassDef), paramIndexMap);
    } 
    i = paramClassDef.getStaticValuesOffset();
    this.idsDefsOut.writeInt(paramIndexMap.adjustStaticValues(i));
  }
  
  private void transformCode(Dex paramDex, Code paramCode, IndexMap paramIndexMap) {
    TableOfContents.Section section = this.contentsOut.codes;
    section.size++;
    this.codeOut.assertFourByteAligned();
    this.codeOut.writeUnsignedShort(paramCode.getRegistersSize());
    this.codeOut.writeUnsignedShort(paramCode.getInsSize());
    this.codeOut.writeUnsignedShort(paramCode.getOutsSize());
    Code.Try[] arrayOfTry = paramCode.getTries();
    Code.CatchHandler[] arrayOfCatchHandler = paramCode.getCatchHandlers();
    this.codeOut.writeUnsignedShort(arrayOfTry.length);
    int i = paramCode.getDebugInfoOffset();
    if (i != 0) {
      this.codeOut.writeInt(this.debugInfoOut.getPosition());
      transformDebugInfoItem(paramDex.open(i), paramIndexMap);
    } else {
      this.codeOut.writeInt(0);
    } 
    short[] arrayOfShort = paramCode.getInstructions();
    arrayOfShort = this.instructionTransformer.transform(paramIndexMap, arrayOfShort);
    this.codeOut.writeInt(arrayOfShort.length);
    this.codeOut.write(arrayOfShort);
    if (arrayOfTry.length > 0) {
      if (arrayOfShort.length % 2 == 1)
        this.codeOut.writeShort((short)0); 
      Dex.Section section1 = this.dexOut.open(this.codeOut.getPosition());
      this.codeOut.skip(arrayOfTry.length * 8);
      transformTries(section1, arrayOfTry, transformCatchHandlers(paramIndexMap, arrayOfCatchHandler));
    } 
  }
  
  private void transformDebugInfoItem(Dex.Section paramSection, IndexMap paramIndexMap) {
    TableOfContents.Section section = this.contentsOut.debugInfos;
    section.size++;
    int i = paramSection.readUleb128();
    this.debugInfoOut.writeUleb128(i);
    int j = paramSection.readUleb128();
    this.debugInfoOut.writeUleb128(j);
    for (i = 0; i < j; i++) {
      int k = paramSection.readUleb128p1();
      this.debugInfoOut.writeUleb128p1(paramIndexMap.adjustString(k));
    } 
    while (true) {
      i = paramSection.readByte();
      this.debugInfoOut.writeByte(i);
      if (i != 9) {
        switch (i) {
          default:
            continue;
          case 5:
          case 6:
            i = paramSection.readUleb128();
            this.debugInfoOut.writeUleb128(i);
            continue;
          case 3:
          case 4:
            j = paramSection.readUleb128();
            this.debugInfoOut.writeUleb128(j);
            j = paramSection.readUleb128p1();
            this.debugInfoOut.writeUleb128p1(paramIndexMap.adjustString(j));
            j = paramSection.readUleb128p1();
            this.debugInfoOut.writeUleb128p1(paramIndexMap.adjustType(j));
            if (i == 4) {
              i = paramSection.readUleb128p1();
              this.debugInfoOut.writeUleb128p1(paramIndexMap.adjustString(i));
            } 
            continue;
          case 2:
            i = paramSection.readSleb128();
            this.debugInfoOut.writeSleb128(i);
            continue;
          case 1:
            i = paramSection.readUleb128();
            this.debugInfoOut.writeUleb128(i);
            continue;
          case 0:
            break;
        } 
        return;
      } 
      i = paramSection.readUleb128p1();
      this.debugInfoOut.writeUleb128p1(paramIndexMap.adjustString(i));
    } 
  }
  
  private void transformEncodedCatchHandler(Code.CatchHandler paramCatchHandler, IndexMap paramIndexMap) {
    int j = paramCatchHandler.getCatchAllAddress();
    int[] arrayOfInt2 = paramCatchHandler.getTypeIndexes();
    int[] arrayOfInt1 = paramCatchHandler.getAddresses();
    if (j != -1) {
      this.codeOut.writeSleb128(-arrayOfInt2.length);
    } else {
      this.codeOut.writeSleb128(arrayOfInt2.length);
    } 
    for (int i = 0; i < arrayOfInt2.length; i++) {
      this.codeOut.writeUleb128(paramIndexMap.adjustType(arrayOfInt2[i]));
      this.codeOut.writeUleb128(arrayOfInt1[i]);
    } 
    if (j != -1)
      this.codeOut.writeUleb128(j); 
  }
  
  private void transformFields(IndexMap paramIndexMap, ClassData.Field[] paramArrayOfField) {
    int k = paramArrayOfField.length;
    int i = 0;
    int j;
    for (j = 0; i < k; j = m) {
      ClassData.Field field = paramArrayOfField[i];
      int m = paramIndexMap.adjustField(field.getFieldIndex());
      this.classDataOut.writeUleb128(m - j);
      this.classDataOut.writeUleb128(field.getAccessFlags());
      i++;
    } 
  }
  
  private void transformMethods(Dex paramDex, IndexMap paramIndexMap, ClassData.Method[] paramArrayOfMethod) {
    int k = paramArrayOfMethod.length;
    int i = 0;
    int j;
    for (j = i; i < k; j = m) {
      ClassData.Method method = paramArrayOfMethod[i];
      int m = paramIndexMap.adjustMethod(method.getMethodIndex());
      this.classDataOut.writeUleb128(m - j);
      this.classDataOut.writeUleb128(method.getAccessFlags());
      if (method.getCodeOffset() == 0) {
        this.classDataOut.writeUleb128(0);
      } else {
        this.codeOut.alignToFourBytesWithZeroFill();
        this.classDataOut.writeUleb128(this.codeOut.getPosition());
        transformCode(paramDex, paramDex.readCode(method), paramIndexMap);
      } 
      i++;
    } 
  }
  
  private void transformStaticValues(Dex.Section paramSection, IndexMap paramIndexMap) {
    TableOfContents.Section section = this.contentsOut.encodedArrays;
    section.size++;
    paramIndexMap.putStaticValuesOffset(paramSection.getPosition(), this.encodedArrayOut.getPosition());
    paramIndexMap.adjustEncodedArray(paramSection.readEncodedArray()).writeTo(this.encodedArrayOut);
  }
  
  private void transformStaticValues(Dex paramDex, IndexMap paramIndexMap) {
    TableOfContents.Section section = (paramDex.getTableOfContents()).encodedArrays;
    if (section.exists()) {
      Dex.Section section1 = paramDex.open(section.off);
      for (int i = 0; i < section.size; i++)
        transformStaticValues(section1, paramIndexMap); 
    } 
  }
  
  private void transformTries(Dex.Section paramSection, Code.Try[] paramArrayOfTry, int[] paramArrayOfint) {
    int j = paramArrayOfTry.length;
    int i;
    for (i = 0; i < j; i++) {
      Code.Try try_ = paramArrayOfTry[i];
      paramSection.writeInt(try_.getStartAddress());
      paramSection.writeUnsignedShort(try_.getInstructionCount());
      paramSection.writeUnsignedShort(paramArrayOfint[try_.getCatchHandlerIndex()]);
    } 
  }
  
  private void unionAnnotationSetsAndDirectories() {
    byte b = 0;
    int i = 0;
    while (true) {
      Dex[] arrayOfDex = this.dexes;
      if (i < arrayOfDex.length) {
        transformAnnotationSets(arrayOfDex[i], this.indexMaps[i]);
        i++;
        continue;
      } 
      i = 0;
      while (true) {
        int j;
        arrayOfDex = this.dexes;
        if (i < arrayOfDex.length) {
          transformAnnotationSetRefLists(arrayOfDex[i], this.indexMaps[i]);
          i++;
          continue;
        } 
        i = 0;
        while (true) {
          arrayOfDex = this.dexes;
          j = b;
          if (i < arrayOfDex.length) {
            transformAnnotationDirectories(arrayOfDex[i], this.indexMaps[i]);
            i++;
            continue;
          } 
          break;
        } 
        while (true) {
          arrayOfDex = this.dexes;
          if (j < arrayOfDex.length) {
            transformStaticValues(arrayOfDex[j], this.indexMaps[j]);
            j++;
            continue;
          } 
          break;
        } 
        return;
      } 
      break;
    } 
  }
  
  public Dex merge() throws IOException {
    Dex[] arrayOfDex = this.dexes;
    if (arrayOfDex.length == 1)
      return arrayOfDex[0]; 
    if (arrayOfDex.length == 0)
      return null; 
    long l1 = System.nanoTime();
    Dex dex = mergeDexes();
    WriterSizes writerSizes = new WriterSizes(this);
    int i = this.writerSizes.size() - writerSizes.size();
    if (i > this.compactWasteThreshold) {
      dex = this.dexOut;
      Dex dex1 = new Dex(0);
      CollisionPolicy collisionPolicy = CollisionPolicy.FAIL;
      dex = (new DexMerger(new Dex[] { dex, dex1 }, collisionPolicy, writerSizes)).mergeDexes();
      System.out.printf("Result compacted from %.1fKiB to %.1fKiB to save %.1fKiB%n", new Object[] { Float.valueOf(this.dexOut.getLength() / 1024.0F), Float.valueOf(dex.getLength() / 1024.0F), Float.valueOf(i / 1024.0F) });
    } 
    long l2 = System.nanoTime();
    for (i = 0; i < this.dexes.length; i = j) {
      PrintStream printStream = System.out;
      int j = i + 1;
      printStream.printf("Merged dex #%d (%d defs/%.1fKiB)%n", new Object[] { Integer.valueOf(j), Integer.valueOf((this.dexes[i].getTableOfContents()).classDefs.size), Float.valueOf(this.dexes[i].getLength() / 1024.0F) });
    } 
    System.out.printf("Result is %d defs/%.1fKiB. Took %.1fs%n", new Object[] { Integer.valueOf((dex.getTableOfContents()).classDefs.size), Float.valueOf(dex.getLength() / 1024.0F), Float.valueOf((float)(l2 - l1) / 1.0E9F) });
    return dex;
  }
  
  public void setCompactWasteThreshold(int paramInt) {
    this.compactWasteThreshold = paramInt;
  }
  
  abstract class IdMerger<T extends Comparable<T>> {
    private final Dex.Section out;
    
    protected IdMerger(Dex.Section param1Section) {
      this.out = param1Section;
    }
    
    private int readIntoMap(Dex.Section param1Section, TableOfContents.Section param1Section1, IndexMap param1IndexMap, int param1Int1, TreeMap<T, List<Integer>> param1TreeMap, int param1Int2) {
      byte b;
      if (param1Section != null) {
        b = param1Section.getPosition();
      } else {
        b = -1;
      } 
      if (param1Int1 < param1Section1.size) {
        param1IndexMap = (IndexMap)read(param1Section, param1IndexMap, param1Int1);
        List<Integer> list2 = param1TreeMap.get(param1IndexMap);
        List<Integer> list1 = list2;
        if (list2 == null) {
          list1 = new ArrayList();
          param1TreeMap.put((T)param1IndexMap, list1);
        } 
        list1.add(new Integer(param1Int2));
      } 
      return b;
    }
    
    private List<UnsortedValue> readUnsortedValues(Dex param1Dex, IndexMap param1IndexMap) {
      TableOfContents.Section section = getSection(param1Dex.getTableOfContents());
      if (!section.exists())
        return Collections.emptyList(); 
      ArrayList<UnsortedValue> arrayList = new ArrayList();
      Dex.Section section1 = param1Dex.open(section.off);
      for (int i = 0; i < section.size; i++) {
        int j = section1.getPosition();
        arrayList.add(new UnsortedValue(param1Dex, param1IndexMap, read(section1, param1IndexMap, 0), i, j));
      } 
      return arrayList;
    }
    
    abstract TableOfContents.Section getSection(TableOfContents param1TableOfContents);
    
    public final void mergeSorted() {
      TableOfContents.Section[] arrayOfSection = new TableOfContents.Section[DexMerger.this.dexes.length];
      Dex.Section[] arrayOfSection1 = new Dex.Section[DexMerger.this.dexes.length];
      int[] arrayOfInt1 = new int[DexMerger.this.dexes.length];
      int[] arrayOfInt2 = new int[DexMerger.this.dexes.length];
      TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
      int j = 0;
      int i;
      for (i = 0; i < DexMerger.this.dexes.length; i++) {
        Dex.Section section;
        arrayOfSection[i] = getSection(DexMerger.this.dexes[i].getTableOfContents());
        if (arrayOfSection[i].exists()) {
          section = DexMerger.this.dexes[i].open((arrayOfSection[i]).off);
        } else {
          section = null;
        } 
        arrayOfSection1[i] = section;
        arrayOfInt1[i] = readIntoMap(arrayOfSection1[i], arrayOfSection[i], DexMerger.this.indexMaps[i], arrayOfInt2[i], (TreeMap)treeMap, i);
      } 
      (getSection(DexMerger.this.contentsOut)).off = this.out.getPosition();
      for (i = j; !treeMap.isEmpty(); i++) {
        Map.Entry<Object, Object> entry = treeMap.pollFirstEntry();
        for (Integer integer : entry.getValue()) {
          j = arrayOfInt1[integer.intValue()];
          IndexMap indexMap = DexMerger.this.indexMaps[integer.intValue()];
          int k = integer.intValue();
          int m = arrayOfInt2[k];
          arrayOfInt2[k] = m + 1;
          updateIndex(j, indexMap, m, i);
          arrayOfInt1[integer.intValue()] = readIntoMap(arrayOfSection1[integer.intValue()], arrayOfSection[integer.intValue()], DexMerger.this.indexMaps[integer.intValue()], arrayOfInt2[integer.intValue()], (TreeMap)treeMap, integer.intValue());
        } 
        write((T)entry.getKey());
      } 
      (getSection(DexMerger.this.contentsOut)).size = i;
    }
    
    public final void mergeUnsorted() {
      (getSection(DexMerger.this.contentsOut)).off = this.out.getPosition();
      ArrayList<UnsortedValue> arrayList = new ArrayList();
      int k = 0;
      int i;
      for (i = 0; i < DexMerger.this.dexes.length; i++)
        arrayList.addAll(readUnsortedValues(DexMerger.this.dexes[i], DexMerger.this.indexMaps[i])); 
      Collections.sort(arrayList);
      int j = 0;
      i = k;
      while (i < arrayList.size()) {
        k = i + 1;
        UnsortedValue unsortedValue = arrayList.get(i);
        i = unsortedValue.offset;
        IndexMap indexMap = unsortedValue.indexMap;
        int n = unsortedValue.index;
        int m = j - 1;
        updateIndex(i, indexMap, n, m);
        for (i = k; i < arrayList.size() && unsortedValue.compareTo(arrayList.get(i)) == 0; i++) {
          UnsortedValue unsortedValue1 = arrayList.get(i);
          updateIndex(unsortedValue1.offset, unsortedValue1.indexMap, unsortedValue1.index, m);
        } 
        write(unsortedValue.value);
        j++;
      } 
      (getSection(DexMerger.this.contentsOut)).size = j;
    }
    
    abstract T read(Dex.Section param1Section, IndexMap param1IndexMap, int param1Int);
    
    abstract void updateIndex(int param1Int1, IndexMap param1IndexMap, int param1Int2, int param1Int3);
    
    abstract void write(T param1T);
    
    class UnsortedValue implements Comparable<UnsortedValue> {
      final int index;
      
      final IndexMap indexMap;
      
      final int offset;
      
      final Dex source;
      
      final T value;
      
      UnsortedValue(Dex param2Dex, IndexMap param2IndexMap, T param2T, int param2Int1, int param2Int2) {
        this.source = param2Dex;
        this.indexMap = param2IndexMap;
        this.value = param2T;
        this.index = param2Int1;
        this.offset = param2Int2;
      }
      
      public int compareTo(UnsortedValue param2UnsortedValue) {
        return this.value.compareTo(param2UnsortedValue.value);
      }
    }
  }
  
  class UnsortedValue implements Comparable<IdMerger<T>.UnsortedValue> {
    final int index;
    
    final IndexMap indexMap;
    
    final int offset;
    
    final Dex source;
    
    final T value;
    
    UnsortedValue(Dex param1Dex, IndexMap param1IndexMap, T param1T, int param1Int1, int param1Int2) {
      this.source = param1Dex;
      this.indexMap = param1IndexMap;
      this.value = param1T;
      this.index = param1Int1;
      this.offset = param1Int2;
    }
    
    public int compareTo(UnsortedValue param1UnsortedValue) {
      return this.value.compareTo(param1UnsortedValue.value);
    }
  }
  
  private static class WriterSizes {
    private int annotation;
    
    private int annotationsDirectory;
    
    private int annotationsSet;
    
    private int annotationsSetRefList;
    
    private int classData;
    
    private int code;
    
    private int debugInfo;
    
    private int encodedArray;
    
    private int header = 112;
    
    private int idsDefs;
    
    private int mapList;
    
    private int stringData;
    
    private int typeList;
    
    public WriterSizes(DexMerger param1DexMerger) {
      this.header = param1DexMerger.headerOut.used();
      this.idsDefs = param1DexMerger.idsDefsOut.used();
      this.mapList = param1DexMerger.mapListOut.used();
      this.typeList = param1DexMerger.typeListOut.used();
      this.classData = param1DexMerger.classDataOut.used();
      this.code = param1DexMerger.codeOut.used();
      this.stringData = param1DexMerger.stringDataOut.used();
      this.debugInfo = param1DexMerger.debugInfoOut.used();
      this.encodedArray = param1DexMerger.encodedArrayOut.used();
      this.annotationsDirectory = param1DexMerger.annotationsDirectoryOut.used();
      this.annotationsSet = param1DexMerger.annotationSetOut.used();
      this.annotationsSetRefList = param1DexMerger.annotationSetRefListOut.used();
      this.annotation = param1DexMerger.annotationOut.used();
      fourByteAlign();
    }
    
    public WriterSizes(Dex[] param1ArrayOfDex) {
      for (int i = 0; i < param1ArrayOfDex.length; i++)
        plus(param1ArrayOfDex[i].getTableOfContents(), false); 
      fourByteAlign();
    }
    
    private static int fourByteAlign(int param1Int) {
      return param1Int + 3 & 0xFFFFFFFC;
    }
    
    private void fourByteAlign() {
      this.header = fourByteAlign(this.header);
      this.idsDefs = fourByteAlign(this.idsDefs);
      this.mapList = fourByteAlign(this.mapList);
      this.typeList = fourByteAlign(this.typeList);
      this.classData = fourByteAlign(this.classData);
      this.code = fourByteAlign(this.code);
      this.stringData = fourByteAlign(this.stringData);
      this.debugInfo = fourByteAlign(this.debugInfo);
      this.encodedArray = fourByteAlign(this.encodedArray);
      this.annotationsDirectory = fourByteAlign(this.annotationsDirectory);
      this.annotationsSet = fourByteAlign(this.annotationsSet);
      this.annotationsSetRefList = fourByteAlign(this.annotationsSetRefList);
      this.annotation = fourByteAlign(this.annotation);
    }
    
    private void plus(TableOfContents param1TableOfContents, boolean param1Boolean) {
      this.idsDefs += param1TableOfContents.stringIds.size * 4 + param1TableOfContents.typeIds.size * 4 + param1TableOfContents.protoIds.size * 12 + param1TableOfContents.fieldIds.size * 8 + param1TableOfContents.methodIds.size * 8 + param1TableOfContents.classDefs.size * 32;
      this.mapList = param1TableOfContents.sections.length * 12 + 4;
      this.typeList += fourByteAlign(param1TableOfContents.typeLists.byteCount);
      this.stringData += param1TableOfContents.stringDatas.byteCount;
      this.annotationsDirectory += param1TableOfContents.annotationsDirectories.byteCount;
      this.annotationsSet += param1TableOfContents.annotationSets.byteCount;
      this.annotationsSetRefList += param1TableOfContents.annotationSetRefLists.byteCount;
      if (param1Boolean) {
        this.code += param1TableOfContents.codes.byteCount;
        this.classData += param1TableOfContents.classDatas.byteCount;
        this.encodedArray += param1TableOfContents.encodedArrays.byteCount;
        this.annotation += param1TableOfContents.annotations.byteCount;
        this.debugInfo += param1TableOfContents.debugInfos.byteCount;
        return;
      } 
      this.code += (int)Math.ceil(param1TableOfContents.codes.byteCount * 1.25D);
      this.classData += (int)Math.ceil(param1TableOfContents.classDatas.byteCount * 1.67D);
      this.encodedArray += param1TableOfContents.encodedArrays.byteCount * 2;
      this.annotation += (int)Math.ceil((param1TableOfContents.annotations.byteCount * 2));
      this.debugInfo += param1TableOfContents.debugInfos.byteCount * 2;
    }
    
    public int size() {
      return this.header + this.idsDefs + this.mapList + this.typeList + this.classData + this.code + this.stringData + this.debugInfo + this.encodedArray + this.annotationsDirectory + this.annotationsSet + this.annotationsSetRefList + this.annotation;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\merge\DexMerger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
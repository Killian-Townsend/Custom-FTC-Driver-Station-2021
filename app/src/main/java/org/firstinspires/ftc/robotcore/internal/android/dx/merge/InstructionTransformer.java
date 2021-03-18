package org.firstinspires.ftc.robotcore.internal.android.dx.merge;

import org.firstinspires.ftc.robotcore.internal.android.dex.DexException;
import org.firstinspires.ftc.robotcore.internal.android.dex.DexIndexOverflowException;
import org.firstinspires.ftc.robotcore.internal.android.dx.io.CodeReader;
import org.firstinspires.ftc.robotcore.internal.android.dx.io.instructions.CodeOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.io.instructions.DecodedInstruction;
import org.firstinspires.ftc.robotcore.internal.android.dx.io.instructions.ShortArrayCodeOutput;

final class InstructionTransformer {
  private IndexMap indexMap;
  
  private int mappedAt;
  
  private DecodedInstruction[] mappedInstructions;
  
  private final CodeReader reader;
  
  public InstructionTransformer() {
    CodeReader codeReader = new CodeReader();
    this.reader = codeReader;
    codeReader.setAllVisitors(new GenericVisitor());
    this.reader.setStringVisitor(new StringVisitor());
    this.reader.setTypeVisitor(new TypeVisitor());
    this.reader.setFieldVisitor(new FieldVisitor());
    this.reader.setMethodVisitor(new MethodVisitor());
  }
  
  private static void jumboCheck(boolean paramBoolean, int paramInt) {
    if (!paramBoolean) {
      if (paramInt <= 65535)
        return; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Cannot merge new index ");
      stringBuilder.append(paramInt);
      stringBuilder.append(" into a non-jumbo instruction!");
      throw new DexIndexOverflowException(stringBuilder.toString());
    } 
  }
  
  public short[] transform(IndexMap paramIndexMap, short[] paramArrayOfshort) throws DexException {
    DecodedInstruction[] arrayOfDecodedInstruction = DecodedInstruction.decodeAll(paramArrayOfshort);
    int j = arrayOfDecodedInstruction.length;
    this.indexMap = paramIndexMap;
    this.mappedInstructions = new DecodedInstruction[j];
    int i = 0;
    this.mappedAt = 0;
    this.reader.visitAll(arrayOfDecodedInstruction);
    ShortArrayCodeOutput shortArrayCodeOutput = new ShortArrayCodeOutput(j);
    arrayOfDecodedInstruction = this.mappedInstructions;
    j = arrayOfDecodedInstruction.length;
    while (i < j) {
      DecodedInstruction decodedInstruction = arrayOfDecodedInstruction[i];
      if (decodedInstruction != null)
        decodedInstruction.encode((CodeOutput)shortArrayCodeOutput); 
      i++;
    } 
    this.indexMap = null;
    return shortArrayCodeOutput.getArray();
  }
  
  private class FieldVisitor implements CodeReader.Visitor {
    private FieldVisitor() {}
    
    public void visit(DecodedInstruction[] param1ArrayOfDecodedInstruction, DecodedInstruction param1DecodedInstruction) {
      boolean bool;
      int i = param1DecodedInstruction.getIndex();
      i = InstructionTransformer.this.indexMap.adjustField(i);
      if (param1DecodedInstruction.getOpcode() == 27) {
        bool = true;
      } else {
        bool = false;
      } 
      InstructionTransformer.jumboCheck(bool, i);
      InstructionTransformer.this.mappedInstructions[InstructionTransformer.access$608(InstructionTransformer.this)] = param1DecodedInstruction.withIndex(i);
    }
  }
  
  private class GenericVisitor implements CodeReader.Visitor {
    private GenericVisitor() {}
    
    public void visit(DecodedInstruction[] param1ArrayOfDecodedInstruction, DecodedInstruction param1DecodedInstruction) {
      InstructionTransformer.this.mappedInstructions[InstructionTransformer.access$608(InstructionTransformer.this)] = param1DecodedInstruction;
    }
  }
  
  private class MethodVisitor implements CodeReader.Visitor {
    private MethodVisitor() {}
    
    public void visit(DecodedInstruction[] param1ArrayOfDecodedInstruction, DecodedInstruction param1DecodedInstruction) {
      boolean bool;
      int i = param1DecodedInstruction.getIndex();
      i = InstructionTransformer.this.indexMap.adjustMethod(i);
      if (param1DecodedInstruction.getOpcode() == 27) {
        bool = true;
      } else {
        bool = false;
      } 
      InstructionTransformer.jumboCheck(bool, i);
      InstructionTransformer.this.mappedInstructions[InstructionTransformer.access$608(InstructionTransformer.this)] = param1DecodedInstruction.withIndex(i);
    }
  }
  
  private class StringVisitor implements CodeReader.Visitor {
    private StringVisitor() {}
    
    public void visit(DecodedInstruction[] param1ArrayOfDecodedInstruction, DecodedInstruction param1DecodedInstruction) {
      boolean bool;
      int i = param1DecodedInstruction.getIndex();
      i = InstructionTransformer.this.indexMap.adjustString(i);
      if (param1DecodedInstruction.getOpcode() == 27) {
        bool = true;
      } else {
        bool = false;
      } 
      InstructionTransformer.jumboCheck(bool, i);
      InstructionTransformer.this.mappedInstructions[InstructionTransformer.access$608(InstructionTransformer.this)] = param1DecodedInstruction.withIndex(i);
    }
  }
  
  private class TypeVisitor implements CodeReader.Visitor {
    private TypeVisitor() {}
    
    public void visit(DecodedInstruction[] param1ArrayOfDecodedInstruction, DecodedInstruction param1DecodedInstruction) {
      boolean bool;
      int i = param1DecodedInstruction.getIndex();
      i = InstructionTransformer.this.indexMap.adjustType(i);
      if (param1DecodedInstruction.getOpcode() == 27) {
        bool = true;
      } else {
        bool = false;
      } 
      InstructionTransformer.jumboCheck(bool, i);
      InstructionTransformer.this.mappedInstructions[InstructionTransformer.access$608(InstructionTransformer.this)] = param1DecodedInstruction.withIndex(i);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\merge\InstructionTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
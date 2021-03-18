package org.firstinspires.ftc.robotcore.internal.android.dx.io;

import org.firstinspires.ftc.robotcore.internal.android.dex.DexException;
import org.firstinspires.ftc.robotcore.internal.android.dx.io.instructions.DecodedInstruction;

public final class CodeReader {
  private Visitor fallbackVisitor = null;
  
  private Visitor fieldVisitor = null;
  
  private Visitor methodVisitor = null;
  
  private Visitor stringVisitor = null;
  
  private Visitor typeVisitor = null;
  
  private void callVisit(DecodedInstruction[] paramArrayOfDecodedInstruction, DecodedInstruction paramDecodedInstruction) {
    Visitor visitor1;
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$internal$android$dx$io$IndexType[OpcodeInfo.getIndexType(paramDecodedInstruction.getOpcode()).ordinal()];
    if (i != 1) {
      if (i != 2) {
        if (i != 3) {
          if (i != 4) {
            visitor1 = null;
          } else {
            visitor1 = this.methodVisitor;
          } 
        } else {
          visitor1 = this.fieldVisitor;
        } 
      } else {
        visitor1 = this.typeVisitor;
      } 
    } else {
      visitor1 = this.stringVisitor;
    } 
    Visitor visitor2 = visitor1;
    if (visitor1 == null)
      visitor2 = this.fallbackVisitor; 
    if (visitor2 != null)
      visitor2.visit(paramArrayOfDecodedInstruction, paramDecodedInstruction); 
  }
  
  public void setAllVisitors(Visitor paramVisitor) {
    this.fallbackVisitor = paramVisitor;
    this.stringVisitor = paramVisitor;
    this.typeVisitor = paramVisitor;
    this.fieldVisitor = paramVisitor;
    this.methodVisitor = paramVisitor;
  }
  
  public void setFallbackVisitor(Visitor paramVisitor) {
    this.fallbackVisitor = paramVisitor;
  }
  
  public void setFieldVisitor(Visitor paramVisitor) {
    this.fieldVisitor = paramVisitor;
  }
  
  public void setMethodVisitor(Visitor paramVisitor) {
    this.methodVisitor = paramVisitor;
  }
  
  public void setStringVisitor(Visitor paramVisitor) {
    this.stringVisitor = paramVisitor;
  }
  
  public void setTypeVisitor(Visitor paramVisitor) {
    this.typeVisitor = paramVisitor;
  }
  
  public void visitAll(DecodedInstruction[] paramArrayOfDecodedInstruction) throws DexException {
    int j = paramArrayOfDecodedInstruction.length;
    for (int i = 0; i < j; i++) {
      DecodedInstruction decodedInstruction = paramArrayOfDecodedInstruction[i];
      if (decodedInstruction != null)
        callVisit(paramArrayOfDecodedInstruction, decodedInstruction); 
    } 
  }
  
  public void visitAll(short[] paramArrayOfshort) throws DexException {
    visitAll(DecodedInstruction.decodeAll(paramArrayOfshort));
  }
  
  public static interface Visitor {
    void visit(DecodedInstruction[] param1ArrayOfDecodedInstruction, DecodedInstruction param1DecodedInstruction);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\io\CodeReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
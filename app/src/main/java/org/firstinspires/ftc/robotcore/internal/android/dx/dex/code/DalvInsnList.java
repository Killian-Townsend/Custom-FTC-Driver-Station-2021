package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import org.firstinspires.ftc.robotcore.internal.android.dex.util.ExceptionWithContext;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstBaseMethodRef;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.FixedSizeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IndentingWriter;

public final class DalvInsnList extends FixedSizeList {
  private final int regCount;
  
  public DalvInsnList(int paramInt1, int paramInt2) {
    super(paramInt1);
    this.regCount = paramInt2;
  }
  
  public static DalvInsnList makeImmutable(ArrayList<DalvInsn> paramArrayList, int paramInt) {
    int i = paramArrayList.size();
    DalvInsnList dalvInsnList = new DalvInsnList(i, paramInt);
    for (paramInt = 0; paramInt < i; paramInt++)
      dalvInsnList.set(paramInt, paramArrayList.get(paramInt)); 
    dalvInsnList.setImmutable();
    return dalvInsnList;
  }
  
  public int codeSize() {
    int i = size();
    return (i == 0) ? 0 : get(i - 1).getNextAddress();
  }
  
  public void debugPrint(OutputStream paramOutputStream, String paramString, boolean paramBoolean) {
    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(paramOutputStream);
    debugPrint(outputStreamWriter, paramString, paramBoolean);
    try {
      outputStreamWriter.flush();
      return;
    } catch (IOException iOException) {
      throw new RuntimeException(iOException);
    } 
  }
  
  public void debugPrint(Writer paramWriter, String paramString, boolean paramBoolean) {
    IndentingWriter indentingWriter = new IndentingWriter(paramWriter, 0, paramString);
    int j = size();
    int i;
    for (i = 0;; i++) {
      if (i < j) {
        try {
          DalvInsn dalvInsn = (DalvInsn)get0(i);
          if (dalvInsn.codeSize() != 0 || paramBoolean) {
            String str = dalvInsn.listingString("", 0, paramBoolean);
          } else {
            dalvInsn = null;
          } 
          if (dalvInsn != null)
            indentingWriter.write((String)dalvInsn); 
        } catch (IOException iOException) {
          throw new RuntimeException(iOException);
        } 
      } else {
        indentingWriter.flush();
        return;
      } 
    } 
  }
  
  public DalvInsn get(int paramInt) {
    return (DalvInsn)get0(paramInt);
  }
  
  public int getOutsSize() {
    int k = size();
    int i = 0;
    int j;
    for (j = i; i < k; j = m) {
      int m;
      DalvInsn dalvInsn = (DalvInsn)get0(i);
      if (!(dalvInsn instanceof CstInsn)) {
        m = j;
      } else {
        Constant constant = ((CstInsn)dalvInsn).getConstant();
        if (!(constant instanceof CstBaseMethodRef)) {
          m = j;
        } else {
          boolean bool;
          if (dalvInsn.getOpcode().getFamily() == 113) {
            bool = true;
          } else {
            bool = false;
          } 
          int n = ((CstBaseMethodRef)constant).getParameterWordCount(bool);
          m = j;
          if (n > j)
            m = n; 
        } 
      } 
      i++;
    } 
    return j;
  }
  
  public int getRegistersSize() {
    return this.regCount;
  }
  
  public void set(int paramInt, DalvInsn paramDalvInsn) {
    set0(paramInt, paramDalvInsn);
  }
  
  public void writeTo(AnnotatedOutput paramAnnotatedOutput) {
    int k = paramAnnotatedOutput.getCursor();
    int m = size();
    boolean bool = paramAnnotatedOutput.annotates();
    byte b = 0;
    int j = b;
    if (bool) {
      bool = paramAnnotatedOutput.isVerbose();
      int n = 0;
      while (true) {
        j = b;
        if (n < m) {
          DalvInsn dalvInsn = (DalvInsn)get0(n);
          j = dalvInsn.codeSize() * 2;
          if (j != 0 || bool) {
            String str = dalvInsn.listingString("  ", paramAnnotatedOutput.getAnnotationWidth(), true);
          } else {
            dalvInsn = null;
          } 
          if (dalvInsn != null) {
            paramAnnotatedOutput.annotate(j, (String)dalvInsn);
          } else if (j != 0) {
            paramAnnotatedOutput.annotate(j, "");
          } 
          n++;
          continue;
        } 
        break;
      } 
    } 
    while (j < m) {
      DalvInsn dalvInsn = (DalvInsn)get0(j);
      try {
        dalvInsn.writeTo(paramAnnotatedOutput);
        j++;
      } catch (RuntimeException runtimeException) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("...while writing ");
        stringBuilder1.append(dalvInsn);
        throw ExceptionWithContext.withContext(runtimeException, stringBuilder1.toString());
      } 
    } 
    int i = (runtimeException.getCursor() - k) / 2;
    if (i == codeSize())
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("write length mismatch; expected ");
    stringBuilder.append(codeSize());
    stringBuilder.append(" but actually wrote ");
    stringBuilder.append(i);
    throw new RuntimeException(stringBuilder.toString());
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\DalvInsnList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
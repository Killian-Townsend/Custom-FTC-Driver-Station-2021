package org.firstinspires.ftc.robotcore.internal.android.dx.ssa;

import java.util.BitSet;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.code.Merger;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.LocalItem;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeBearer;

public class PhiTypeResolver {
  SsaMethod ssaMeth;
  
  private final BitSet worklist;
  
  private PhiTypeResolver(SsaMethod paramSsaMethod) {
    this.ssaMeth = paramSsaMethod;
    this.worklist = new BitSet(paramSsaMethod.getRegCount());
  }
  
  private static boolean equalsHandlesNulls(LocalItem paramLocalItem1, LocalItem paramLocalItem2) {
    return (paramLocalItem1 == paramLocalItem2 || (paramLocalItem1 != null && paramLocalItem1.equals(paramLocalItem2)));
  }
  
  public static void process(SsaMethod paramSsaMethod) {
    (new PhiTypeResolver(paramSsaMethod)).run();
  }
  
  private void run() {
    int j = this.ssaMeth.getRegCount();
    int i;
    for (i = 0; i < j; i++) {
      SsaInsn ssaInsn = this.ssaMeth.getDefinitionForRegister(i);
      if (ssaInsn != null && ssaInsn.getResult().getBasicType() == 0)
        this.worklist.set(i); 
    } 
    while (true) {
      i = this.worklist.nextSetBit(0);
      if (i >= 0) {
        this.worklist.clear(i);
        if (resolveResultType((PhiInsn)this.ssaMeth.getDefinitionForRegister(i))) {
          List<SsaInsn> list = this.ssaMeth.getUseListForRegister(i);
          j = list.size();
          for (i = 0; i < j; i++) {
            SsaInsn ssaInsn = list.get(i);
            RegisterSpec registerSpec = ssaInsn.getResult();
            if (registerSpec != null && ssaInsn instanceof PhiInsn)
              this.worklist.set(registerSpec.getReg()); 
          } 
        } 
        continue;
      } 
      break;
    } 
  }
  
  boolean resolveResultType(PhiInsn paramPhiInsn) {
    TypeBearer typeBearer;
    paramPhiInsn.updateSourcesToDefinitions(this.ssaMeth);
    RegisterSpecList registerSpecList = paramPhiInsn.getSources();
    int m = registerSpecList.size();
    LocalItem localItem1 = null;
    boolean bool = false;
    int j = -1;
    RegisterSpec registerSpec = null;
    int i;
    for (i = 0; i < m; i++) {
      RegisterSpec registerSpec1 = registerSpecList.get(i);
      if (registerSpec1.getBasicType() != 0) {
        j = i;
        registerSpec = registerSpec1;
      } 
    } 
    if (registerSpec == null)
      return false; 
    LocalItem localItem2 = registerSpec.getLocalItem();
    Type type = registerSpec.getType();
    int k = 0;
    i = 1;
    while (k < m) {
      if (k != j) {
        RegisterSpec registerSpec1 = registerSpecList.get(k);
        if (registerSpec1.getBasicType() != 0) {
          if (i != 0 && equalsHandlesNulls(localItem2, registerSpec1.getLocalItem())) {
            i = 1;
          } else {
            i = 0;
          } 
          typeBearer = Merger.mergeType((TypeBearer)type, (TypeBearer)registerSpec1.getType());
        } 
      } 
      k++;
    } 
    if (typeBearer != null) {
      if (i != 0)
        localItem1 = localItem2; 
      RegisterSpec registerSpec1 = paramPhiInsn.getResult();
      if (registerSpec1.getTypeBearer() == typeBearer && equalsHandlesNulls(localItem1, registerSpec1.getLocalItem()))
        return false; 
      paramPhiInsn.changeResultType(typeBearer, localItem1);
      return true;
    } 
    StringBuilder stringBuilder1 = new StringBuilder();
    for (i = bool; i < m; i++) {
      stringBuilder1.append(registerSpecList.get(i).toString());
      stringBuilder1.append(' ');
    } 
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append("Couldn't map types in phi insn:");
    stringBuilder2.append(stringBuilder1);
    throw new RuntimeException(stringBuilder2.toString());
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\ssa\PhiTypeResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
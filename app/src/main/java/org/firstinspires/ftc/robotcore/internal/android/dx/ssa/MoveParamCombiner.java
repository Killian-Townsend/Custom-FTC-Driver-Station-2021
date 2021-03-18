package org.firstinspires.ftc.robotcore.internal.android.dx.ssa;

import java.util.HashSet;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.CstInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstInteger;

public class MoveParamCombiner {
  private final SsaMethod ssaMeth;
  
  private MoveParamCombiner(SsaMethod paramSsaMethod) {
    this.ssaMeth = paramSsaMethod;
  }
  
  private int getParamIndex(NormalSsaInsn paramNormalSsaInsn) {
    return ((CstInteger)((CstInsn)paramNormalSsaInsn.getOriginalRopInsn()).getConstant()).getValue();
  }
  
  public static void process(SsaMethod paramSsaMethod) {
    (new MoveParamCombiner(paramSsaMethod)).run();
  }
  
  private void run() {
    final RegisterSpec[] paramSpecs = new RegisterSpec[this.ssaMeth.getParamWidth()];
    final HashSet<SsaInsn> deletedInsns = new HashSet();
    this.ssaMeth.forEachInsn(new SsaInsn.Visitor() {
          public void visitMoveInsn(NormalSsaInsn param1NormalSsaInsn) {}
          
          public void visitNonMoveInsn(NormalSsaInsn param1NormalSsaInsn) {
            // Byte code:
            //   0: aload_1
            //   1: invokevirtual getOpcode : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/Rop;
            //   4: invokevirtual getOpcode : ()I
            //   7: iconst_3
            //   8: if_icmpeq -> 12
            //   11: return
            //   12: aload_0
            //   13: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/android/dx/ssa/MoveParamCombiner;
            //   16: aload_1
            //   17: invokestatic access$000 : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/ssa/MoveParamCombiner;Lorg/firstinspires/ftc/robotcore/internal/android/dx/ssa/NormalSsaInsn;)I
            //   20: istore_2
            //   21: aload_0
            //   22: getfield val$paramSpecs : [Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;
            //   25: astore_3
            //   26: aload_3
            //   27: iload_2
            //   28: aaload
            //   29: ifnonnull -> 40
            //   32: aload_3
            //   33: iload_2
            //   34: aload_1
            //   35: invokevirtual getResult : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;
            //   38: aastore
            //   39: return
            //   40: aload_3
            //   41: iload_2
            //   42: aaload
            //   43: astore #6
            //   45: aload_1
            //   46: invokevirtual getResult : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;
            //   49: astore #5
            //   51: aload #6
            //   53: invokevirtual getLocalItem : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/LocalItem;
            //   56: astore_3
            //   57: aload #5
            //   59: invokevirtual getLocalItem : ()Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/LocalItem;
            //   62: astore #4
            //   64: aload_3
            //   65: ifnonnull -> 74
            //   68: aload #4
            //   70: astore_3
            //   71: goto -> 91
            //   74: aload #4
            //   76: ifnonnull -> 82
            //   79: goto -> 91
            //   82: aload_3
            //   83: aload #4
            //   85: invokevirtual equals : (Ljava/lang/Object;)Z
            //   88: ifeq -> 185
            //   91: aload_0
            //   92: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/android/dx/ssa/MoveParamCombiner;
            //   95: invokestatic access$100 : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/ssa/MoveParamCombiner;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/ssa/SsaMethod;
            //   98: aload #6
            //   100: invokevirtual getReg : ()I
            //   103: invokevirtual getDefinitionForRegister : (I)Lorg/firstinspires/ftc/robotcore/internal/android/dx/ssa/SsaInsn;
            //   106: aload_3
            //   107: invokevirtual setResultLocal : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/LocalItem;)V
            //   110: new org/firstinspires/ftc/robotcore/internal/android/dx/ssa/MoveParamCombiner$1$1
            //   113: dup
            //   114: aload_0
            //   115: aload #5
            //   117: aload #6
            //   119: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/ssa/MoveParamCombiner$1;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;Lorg/firstinspires/ftc/robotcore/internal/android/dx/rop/code/RegisterSpec;)V
            //   122: astore_3
            //   123: aload_0
            //   124: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/android/dx/ssa/MoveParamCombiner;
            //   127: invokestatic access$100 : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/ssa/MoveParamCombiner;)Lorg/firstinspires/ftc/robotcore/internal/android/dx/ssa/SsaMethod;
            //   130: aload #5
            //   132: invokevirtual getReg : ()I
            //   135: invokevirtual getUseListForRegister : (I)Ljava/util/List;
            //   138: astore #4
            //   140: aload #4
            //   142: invokeinterface size : ()I
            //   147: iconst_1
            //   148: isub
            //   149: istore_2
            //   150: iload_2
            //   151: iflt -> 176
            //   154: aload #4
            //   156: iload_2
            //   157: invokeinterface get : (I)Ljava/lang/Object;
            //   162: checkcast org/firstinspires/ftc/robotcore/internal/android/dx/ssa/SsaInsn
            //   165: aload_3
            //   166: invokevirtual mapSourceRegisters : (Lorg/firstinspires/ftc/robotcore/internal/android/dx/ssa/RegisterMapper;)V
            //   169: iload_2
            //   170: iconst_1
            //   171: isub
            //   172: istore_2
            //   173: goto -> 150
            //   176: aload_0
            //   177: getfield val$deletedInsns : Ljava/util/HashSet;
            //   180: aload_1
            //   181: invokevirtual add : (Ljava/lang/Object;)Z
            //   184: pop
            //   185: return
          }
          
          public void visitPhiInsn(PhiInsn param1PhiInsn) {}
        });
    this.ssaMeth.deleteInsns(hashSet);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\ssa\MoveParamCombiner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
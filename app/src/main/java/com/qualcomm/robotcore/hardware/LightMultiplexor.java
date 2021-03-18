package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.util.WeakReferenceSet;
import java.util.Set;

public class LightMultiplexor implements SwitchableLight {
  protected static final Set<LightMultiplexor> extantMultiplexors = (Set<LightMultiplexor>)new WeakReferenceSet();
  
  protected int enableCount;
  
  protected final SwitchableLight target;
  
  protected LightMultiplexor(SwitchableLight paramSwitchableLight) {
    this.target = paramSwitchableLight;
    this.enableCount = 0;
  }
  
  public static LightMultiplexor forLight(SwitchableLight paramSwitchableLight) {
    // Byte code:
    //   0: ldc com/qualcomm/robotcore/hardware/LightMultiplexor
    //   2: monitorenter
    //   3: getstatic com/qualcomm/robotcore/hardware/LightMultiplexor.extantMultiplexors : Ljava/util/Set;
    //   6: invokeinterface iterator : ()Ljava/util/Iterator;
    //   11: astore_2
    //   12: aload_2
    //   13: invokeinterface hasNext : ()Z
    //   18: ifeq -> 49
    //   21: aload_2
    //   22: invokeinterface next : ()Ljava/lang/Object;
    //   27: checkcast com/qualcomm/robotcore/hardware/LightMultiplexor
    //   30: astore_3
    //   31: aload_3
    //   32: getfield target : Lcom/qualcomm/robotcore/hardware/SwitchableLight;
    //   35: aload_0
    //   36: invokevirtual equals : (Ljava/lang/Object;)Z
    //   39: istore_1
    //   40: iload_1
    //   41: ifeq -> 12
    //   44: ldc com/qualcomm/robotcore/hardware/LightMultiplexor
    //   46: monitorexit
    //   47: aload_3
    //   48: areturn
    //   49: new com/qualcomm/robotcore/hardware/LightMultiplexor
    //   52: dup
    //   53: aload_0
    //   54: invokespecial <init> : (Lcom/qualcomm/robotcore/hardware/SwitchableLight;)V
    //   57: astore_0
    //   58: getstatic com/qualcomm/robotcore/hardware/LightMultiplexor.extantMultiplexors : Ljava/util/Set;
    //   61: aload_0
    //   62: invokeinterface add : (Ljava/lang/Object;)Z
    //   67: pop
    //   68: ldc com/qualcomm/robotcore/hardware/LightMultiplexor
    //   70: monitorexit
    //   71: aload_0
    //   72: areturn
    //   73: astore_0
    //   74: ldc com/qualcomm/robotcore/hardware/LightMultiplexor
    //   76: monitorexit
    //   77: aload_0
    //   78: athrow
    // Exception table:
    //   from	to	target	type
    //   3	12	73	finally
    //   12	40	73	finally
    //   49	68	73	finally
  }
  
  public void enableLight(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iload_1
    //   3: ifeq -> 35
    //   6: aload_0
    //   7: getfield enableCount : I
    //   10: istore_2
    //   11: aload_0
    //   12: iload_2
    //   13: iconst_1
    //   14: iadd
    //   15: putfield enableCount : I
    //   18: iload_2
    //   19: ifne -> 68
    //   22: aload_0
    //   23: getfield target : Lcom/qualcomm/robotcore/hardware/SwitchableLight;
    //   26: iconst_1
    //   27: invokeinterface enableLight : (Z)V
    //   32: goto -> 68
    //   35: aload_0
    //   36: getfield enableCount : I
    //   39: ifle -> 68
    //   42: aload_0
    //   43: getfield enableCount : I
    //   46: iconst_1
    //   47: isub
    //   48: istore_2
    //   49: aload_0
    //   50: iload_2
    //   51: putfield enableCount : I
    //   54: iload_2
    //   55: ifne -> 68
    //   58: aload_0
    //   59: getfield target : Lcom/qualcomm/robotcore/hardware/SwitchableLight;
    //   62: iconst_0
    //   63: invokeinterface enableLight : (Z)V
    //   68: aload_0
    //   69: monitorexit
    //   70: return
    //   71: astore_3
    //   72: aload_0
    //   73: monitorexit
    //   74: aload_3
    //   75: athrow
    // Exception table:
    //   from	to	target	type
    //   6	18	71	finally
    //   22	32	71	finally
    //   35	54	71	finally
    //   58	68	71	finally
  }
  
  public boolean isLightOn() {
    return this.target.isLightOn();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\LightMultiplexor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
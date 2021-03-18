package com.qualcomm.robotcore.hardware;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class LightBlinker implements Blinker {
  public static final String TAG = "LightBlinker";
  
  protected ArrayList<Blinker.Step> currentSteps;
  
  protected ScheduledFuture<?> future;
  
  protected final SwitchableLight light;
  
  protected int nextStep;
  
  protected Deque<ArrayList<Blinker.Step>> previousSteps;
  
  public LightBlinker(SwitchableLight paramSwitchableLight) {
    this.light = paramSwitchableLight;
    this.currentSteps = new ArrayList<Blinker.Step>();
    this.previousSteps = new ArrayDeque<ArrayList<Blinker.Step>>();
    this.future = null;
  }
  
  public int getBlinkerPatternMaxLength() {
    return Integer.MAX_VALUE;
  }
  
  public Collection<Blinker.Step> getPattern() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/ArrayList
    //   5: dup
    //   6: aload_0
    //   7: getfield currentSteps : Ljava/util/ArrayList;
    //   10: invokespecial <init> : (Ljava/util/Collection;)V
    //   13: astore_1
    //   14: aload_0
    //   15: monitorexit
    //   16: aload_1
    //   17: areturn
    //   18: astore_1
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_1
    //   22: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	18	finally
  }
  
  protected boolean isCurrentPattern(Collection<Blinker.Step> paramCollection) {
    if (paramCollection.size() != this.currentSteps.size())
      return false; 
    Iterator<Blinker.Step> iterator = paramCollection.iterator();
    for (int i = 0; iterator.hasNext(); i++) {
      if (!((Blinker.Step)iterator.next()).equals(this.currentSteps.get(i)))
        return false; 
    } 
    return true;
  }
  
  public boolean patternStackNotEmpty() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield previousSteps : Ljava/util/Deque;
    //   6: invokeinterface size : ()I
    //   11: istore_1
    //   12: iload_1
    //   13: ifle -> 21
    //   16: iconst_1
    //   17: istore_2
    //   18: goto -> 23
    //   21: iconst_0
    //   22: istore_2
    //   23: aload_0
    //   24: monitorexit
    //   25: iload_2
    //   26: ireturn
    //   27: astore_3
    //   28: aload_0
    //   29: monitorexit
    //   30: aload_3
    //   31: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	27	finally
  }
  
  public boolean popPattern() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield previousSteps : Ljava/util/Deque;
    //   7: invokeinterface pop : ()Ljava/lang/Object;
    //   12: checkcast java/util/Collection
    //   15: invokevirtual setPattern : (Ljava/util/Collection;)V
    //   18: aload_0
    //   19: monitorexit
    //   20: iconst_1
    //   21: ireturn
    //   22: astore_1
    //   23: goto -> 35
    //   26: aload_0
    //   27: aconst_null
    //   28: invokevirtual setPattern : (Ljava/util/Collection;)V
    //   31: aload_0
    //   32: monitorexit
    //   33: iconst_0
    //   34: ireturn
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_1
    //   38: athrow
    //   39: astore_1
    //   40: goto -> 26
    // Exception table:
    //   from	to	target	type
    //   2	18	39	java/util/NoSuchElementException
    //   2	18	22	finally
    //   26	31	22	finally
  }
  
  public void pushPattern(Collection<Blinker.Step> paramCollection) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield previousSteps : Ljava/util/Deque;
    //   6: aload_0
    //   7: getfield currentSteps : Ljava/util/ArrayList;
    //   10: invokeinterface push : (Ljava/lang/Object;)V
    //   15: aload_0
    //   16: aload_1
    //   17: invokevirtual setPattern : (Ljava/util/Collection;)V
    //   20: aload_0
    //   21: monitorexit
    //   22: return
    //   23: astore_1
    //   24: aload_0
    //   25: monitorexit
    //   26: aload_1
    //   27: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	23	finally
  }
  
  protected void scheduleNext() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield currentSteps : Ljava/util/ArrayList;
    //   6: astore_3
    //   7: aload_0
    //   8: getfield nextStep : I
    //   11: istore_1
    //   12: aload_0
    //   13: iload_1
    //   14: iconst_1
    //   15: iadd
    //   16: putfield nextStep : I
    //   19: aload_3
    //   20: iload_1
    //   21: invokevirtual get : (I)Ljava/lang/Object;
    //   24: checkcast com/qualcomm/robotcore/hardware/Blinker$Step
    //   27: astore_3
    //   28: aload_0
    //   29: getfield nextStep : I
    //   32: aload_0
    //   33: getfield currentSteps : Ljava/util/ArrayList;
    //   36: invokevirtual size : ()I
    //   39: if_icmplt -> 47
    //   42: aload_0
    //   43: iconst_0
    //   44: putfield nextStep : I
    //   47: aload_3
    //   48: invokevirtual isLit : ()Z
    //   51: istore_2
    //   52: aload_0
    //   53: getfield light : Lcom/qualcomm/robotcore/hardware/SwitchableLight;
    //   56: iload_2
    //   57: invokeinterface enableLight : (Z)V
    //   62: aload_0
    //   63: invokestatic getDefaultScheduler : ()Ljava/util/concurrent/ScheduledExecutorService;
    //   66: new com/qualcomm/robotcore/hardware/LightBlinker$1
    //   69: dup
    //   70: aload_0
    //   71: invokespecial <init> : (Lcom/qualcomm/robotcore/hardware/LightBlinker;)V
    //   74: aload_3
    //   75: invokevirtual getDurationMs : ()I
    //   78: i2l
    //   79: getstatic java/util/concurrent/TimeUnit.MILLISECONDS : Ljava/util/concurrent/TimeUnit;
    //   82: invokeinterface schedule : (Ljava/lang/Runnable;JLjava/util/concurrent/TimeUnit;)Ljava/util/concurrent/ScheduledFuture;
    //   87: putfield future : Ljava/util/concurrent/ScheduledFuture;
    //   90: aload_0
    //   91: monitorexit
    //   92: return
    //   93: astore_3
    //   94: aload_0
    //   95: monitorexit
    //   96: aload_3
    //   97: athrow
    // Exception table:
    //   from	to	target	type
    //   2	47	93	finally
    //   47	90	93	finally
  }
  
  public void setConstant(int paramInt) {
    Blinker.Step step = new Blinker.Step(paramInt, 1L, TimeUnit.SECONDS);
    ArrayList<Blinker.Step> arrayList = new ArrayList();
    arrayList.add(step);
    setPattern(arrayList);
  }
  
  public void setPattern(Collection<Blinker.Step> paramCollection) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: invokevirtual isCurrentPattern : (Ljava/util/Collection;)Z
    //   7: ifeq -> 111
    //   10: aload_0
    //   11: invokevirtual stop : ()V
    //   14: aload_1
    //   15: ifnull -> 90
    //   18: aload_1
    //   19: invokeinterface size : ()I
    //   24: ifne -> 30
    //   27: goto -> 90
    //   30: aload_0
    //   31: new java/util/ArrayList
    //   34: dup
    //   35: aload_1
    //   36: invokespecial <init> : (Ljava/util/Collection;)V
    //   39: putfield currentSteps : Ljava/util/ArrayList;
    //   42: aload_1
    //   43: invokeinterface size : ()I
    //   48: iconst_1
    //   49: if_icmpne -> 78
    //   52: aload_0
    //   53: getfield light : Lcom/qualcomm/robotcore/hardware/SwitchableLight;
    //   56: aload_0
    //   57: getfield currentSteps : Ljava/util/ArrayList;
    //   60: iconst_0
    //   61: invokevirtual get : (I)Ljava/lang/Object;
    //   64: checkcast com/qualcomm/robotcore/hardware/Blinker$Step
    //   67: invokevirtual isLit : ()Z
    //   70: invokeinterface enableLight : (Z)V
    //   75: goto -> 111
    //   78: aload_0
    //   79: iconst_0
    //   80: putfield nextStep : I
    //   83: aload_0
    //   84: invokevirtual scheduleNext : ()V
    //   87: goto -> 111
    //   90: aload_0
    //   91: new java/util/ArrayList
    //   94: dup
    //   95: invokespecial <init> : ()V
    //   98: putfield currentSteps : Ljava/util/ArrayList;
    //   101: aload_0
    //   102: getfield light : Lcom/qualcomm/robotcore/hardware/SwitchableLight;
    //   105: iconst_0
    //   106: invokeinterface enableLight : (Z)V
    //   111: aload_0
    //   112: monitorexit
    //   113: return
    //   114: astore_1
    //   115: aload_0
    //   116: monitorexit
    //   117: aload_1
    //   118: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	114	finally
    //   18	27	114	finally
    //   30	75	114	finally
    //   78	87	114	finally
    //   90	111	114	finally
  }
  
  protected void stop() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield future : Ljava/util/concurrent/ScheduledFuture;
    //   6: ifnull -> 25
    //   9: aload_0
    //   10: getfield future : Ljava/util/concurrent/ScheduledFuture;
    //   13: iconst_0
    //   14: invokeinterface cancel : (Z)Z
    //   19: pop
    //   20: aload_0
    //   21: aconst_null
    //   22: putfield future : Ljava/util/concurrent/ScheduledFuture;
    //   25: aload_0
    //   26: monitorexit
    //   27: return
    //   28: astore_1
    //   29: aload_0
    //   30: monitorexit
    //   31: aload_1
    //   32: athrow
    // Exception table:
    //   from	to	target	type
    //   2	25	28	finally
  }
  
  public void stopBlinking() {
    setConstant(-16777216);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\LightBlinker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
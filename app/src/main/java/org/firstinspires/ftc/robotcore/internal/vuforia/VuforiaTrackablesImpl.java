package org.firstinspires.ftc.robotcore.internal.vuforia;

import com.vuforia.DataSet;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

public class VuforiaTrackablesImpl extends AbstractList<VuforiaTrackable> implements VuforiaTrackables {
  DataSet dataSet;
  
  boolean isActive;
  
  String name;
  
  List<VuforiaTrackableImpl> trackables;
  
  VuforiaLocalizerImpl vuforiaLocalizer;
  
  public VuforiaTrackablesImpl(VuforiaLocalizerImpl paramVuforiaLocalizerImpl, DataSet paramDataSet, Class<? extends VuforiaTrackable.Listener> paramClass) {
    this.vuforiaLocalizer = paramVuforiaLocalizerImpl;
    this.dataSet = paramDataSet;
    int i = 0;
    this.isActive = false;
    this.trackables = new ArrayList<VuforiaTrackableImpl>(this.dataSet.getNumTrackables());
    while (i < this.dataSet.getNumTrackables()) {
      VuforiaTrackableImpl vuforiaTrackableImpl = new VuforiaTrackableImpl(this, i, paramClass);
      this.trackables.add(vuforiaTrackableImpl);
      i++;
    } 
  }
  
  public void activate() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isActive : Z
    //   6: ifne -> 38
    //   9: invokestatic getObjectTracker : ()Lcom/vuforia/ObjectTracker;
    //   12: aload_0
    //   13: getfield dataSet : Lcom/vuforia/DataSet;
    //   16: invokevirtual activateDataSet : (Lcom/vuforia/DataSet;)Z
    //   19: invokestatic throwIfFail : (Z)V
    //   22: aload_0
    //   23: iconst_1
    //   24: putfield isActive : Z
    //   27: aload_0
    //   28: aload_0
    //   29: getfield vuforiaLocalizer : Lorg/firstinspires/ftc/robotcore/internal/vuforia/VuforiaLocalizerImpl;
    //   32: getfield isExtendedTrackingActive : Z
    //   35: invokevirtual adjustExtendedTracking : (Z)V
    //   38: aload_0
    //   39: monitorexit
    //   40: return
    //   41: astore_1
    //   42: aload_0
    //   43: monitorexit
    //   44: aload_1
    //   45: athrow
    // Exception table:
    //   from	to	target	type
    //   2	38	41	finally
  }
  
  public void adjustExtendedTracking(boolean paramBoolean) {
    if (this.isActive)
      for (VuforiaTrackableImpl vuforiaTrackableImpl : this.trackables) {
        if (paramBoolean) {
          vuforiaTrackableImpl.getTrackable().startExtendedTracking();
          continue;
        } 
        vuforiaTrackableImpl.getTrackable().stopExtendedTracking();
      }  
  }
  
  public void deactivate() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isActive : Z
    //   6: ifeq -> 27
    //   9: invokestatic getObjectTracker : ()Lcom/vuforia/ObjectTracker;
    //   12: aload_0
    //   13: getfield dataSet : Lcom/vuforia/DataSet;
    //   16: invokevirtual deactivateDataSet : (Lcom/vuforia/DataSet;)Z
    //   19: invokestatic throwIfFail : (Z)V
    //   22: aload_0
    //   23: iconst_0
    //   24: putfield isActive : Z
    //   27: aload_0
    //   28: monitorexit
    //   29: return
    //   30: astore_1
    //   31: aload_0
    //   32: monitorexit
    //   33: aload_1
    //   34: athrow
    // Exception table:
    //   from	to	target	type
    //   2	27	30	finally
  }
  
  public void destroy() {
    deactivate();
    VuforiaLocalizerImpl.getObjectTracker().destroyDataSet(this.dataSet);
    this.dataSet = null;
  }
  
  public VuforiaTrackable get(int paramInt) {
    return this.trackables.get(paramInt);
  }
  
  public VuforiaLocalizer getLocalizer() {
    return this.vuforiaLocalizer;
  }
  
  public String getName() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield name : Ljava/lang/String;
    //   6: astore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_1
    //   10: areturn
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public void setName(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield name : Ljava/lang/String;
    //   7: aload_0
    //   8: getfield trackables : Ljava/util/List;
    //   11: invokeinterface iterator : ()Ljava/util/Iterator;
    //   16: astore_2
    //   17: aload_2
    //   18: invokeinterface hasNext : ()Z
    //   23: ifeq -> 51
    //   26: aload_2
    //   27: invokeinterface next : ()Ljava/lang/Object;
    //   32: checkcast org/firstinspires/ftc/robotcore/internal/vuforia/VuforiaTrackableImpl
    //   35: astore_3
    //   36: aload_3
    //   37: invokevirtual getName : ()Ljava/lang/String;
    //   40: ifnonnull -> 17
    //   43: aload_3
    //   44: aload_1
    //   45: invokevirtual setName : (Ljava/lang/String;)V
    //   48: goto -> 17
    //   51: aload_0
    //   52: monitorexit
    //   53: return
    //   54: astore_1
    //   55: aload_0
    //   56: monitorexit
    //   57: aload_1
    //   58: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	54	finally
    //   17	48	54	finally
  }
  
  public int size() {
    return this.trackables.size();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\vuforia\VuforiaTrackablesImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
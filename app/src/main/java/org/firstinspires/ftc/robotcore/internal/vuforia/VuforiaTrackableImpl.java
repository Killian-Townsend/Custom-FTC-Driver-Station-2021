package org.firstinspires.ftc.robotcore.internal.vuforia;

import com.vuforia.Trackable;
import com.vuforia.TrackableResult;
import com.vuforia.VuMarkTarget;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

public class VuforiaTrackableImpl implements VuforiaTrackable, VuforiaTrackableNotify, VuforiaTrackableContainer {
  protected OpenGLMatrix ftcFieldFromTarget;
  
  protected VuforiaTrackable.Listener listener;
  
  protected Class<? extends VuforiaTrackable.Listener> listenerClass;
  
  protected final Object locationLock = new Object();
  
  protected String name;
  
  protected VuforiaTrackable parent;
  
  protected Trackable trackable;
  
  protected VuforiaTrackablesImpl trackables;
  
  protected Object userData;
  
  protected final Map<VuMarkInstanceId, VuforiaTrackable> vuMarkMap = new HashMap<VuMarkInstanceId, VuforiaTrackable>();
  
  public VuforiaTrackableImpl(VuforiaTrackable paramVuforiaTrackable, VuforiaTrackablesImpl paramVuforiaTrackablesImpl, Trackable paramTrackable, Class<? extends VuforiaTrackable.Listener> paramClass) {
    this.parent = paramVuforiaTrackable;
    this.trackable = paramTrackable;
    this.trackables = paramVuforiaTrackablesImpl;
    this.userData = null;
    this.ftcFieldFromTarget = OpenGLMatrix.identityMatrix();
    this.name = null;
    this.listenerClass = paramClass;
    try {
      Constructor<? extends VuforiaTrackable.Listener> constructor = paramClass.getConstructor(new Class[] { VuforiaTrackable.class });
      try {
        this.listener = constructor.newInstance(new Object[] { this });
        this.trackable.setUserData(this);
        return;
      } catch (InstantiationException instantiationException) {
      
      } catch (IllegalAccessException illegalAccessException) {
      
      } catch (InvocationTargetException invocationTargetException) {}
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("unable to instantiate ");
      stringBuilder.append(paramClass.getSimpleName());
      throw new RuntimeException(stringBuilder.toString(), invocationTargetException);
    } catch (NoSuchMethodException noSuchMethodException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("class ");
      stringBuilder.append(paramClass.getSimpleName());
      stringBuilder.append(" missing VuforiaTrackable ctor");
      throw new RuntimeException(stringBuilder.toString());
    } 
  }
  
  public VuforiaTrackableImpl(VuforiaTrackablesImpl paramVuforiaTrackablesImpl, int paramInt, Class<? extends VuforiaTrackable.Listener> paramClass) {
    this(null, paramVuforiaTrackablesImpl, paramVuforiaTrackablesImpl.dataSet.getTrackable(paramInt), paramClass);
  }
  
  static VuforiaTrackable from(Trackable paramTrackable) {
    VuMarkTarget vuMarkTarget;
    if (paramTrackable.isOfType(VuMarkTarget.getClassType())) {
      vuMarkTarget = (VuMarkTarget)paramTrackable;
      return ((VuforiaTrackableContainer)from((Trackable)vuMarkTarget.getTemplate())).getChild(vuMarkTarget);
    } 
    return (VuforiaTrackable)vuMarkTarget.getUserData();
  }
  
  public static VuforiaTrackable from(TrackableResult paramTrackableResult) {
    return from(paramTrackableResult.getTrackable());
  }
  
  public List<VuforiaTrackable> children() {
    synchronized (this.vuMarkMap) {
      return new ArrayList(this.vuMarkMap.values());
    } 
  }
  
  public VuforiaTrackable getChild(VuMarkTarget paramVuMarkTarget) {
    synchronized (this.vuMarkMap) {
      VuMarkInstanceId vuMarkInstanceId = new VuMarkInstanceId(paramVuMarkTarget.getInstanceId());
      VuforiaTrackable vuforiaTrackable2 = this.vuMarkMap.get(vuMarkInstanceId);
      VuforiaTrackable vuforiaTrackable1 = vuforiaTrackable2;
      if (vuforiaTrackable2 == null) {
        vuforiaTrackable1 = new VuforiaTrackableImpl(this, this.trackables, (Trackable)paramVuMarkTarget, this.listenerClass);
        this.vuMarkMap.put(vuMarkInstanceId, vuforiaTrackable1);
      } 
      return vuforiaTrackable1;
    } 
  }
  
  public OpenGLMatrix getFtcFieldFromTarget() {
    synchronized (this.locationLock) {
      return this.ftcFieldFromTarget;
    } 
  }
  
  public VuforiaTrackable.Listener getListener() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield listener : Lorg/firstinspires/ftc/robotcore/external/navigation/VuforiaTrackable$Listener;
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
  
  public OpenGLMatrix getLocation() {
    return getFtcFieldFromTarget();
  }
  
  public String getName() {
    return this.name;
  }
  
  public VuforiaTrackable getParent() {
    return this.parent;
  }
  
  public Trackable getTrackable() {
    return this.trackable;
  }
  
  public VuforiaTrackables getTrackables() {
    return this.trackables;
  }
  
  public Object getUserData() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield userData : Ljava/lang/Object;
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
  
  public void noteNotTracked() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual getListener : ()Lorg/firstinspires/ftc/robotcore/external/navigation/VuforiaTrackable$Listener;
    //   6: invokeinterface onNotTracked : ()V
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: astore_1
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_1
    //   18: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	14	finally
  }
  
  public void noteTracked(TrackableResult paramTrackableResult, CameraName paramCameraName, Camera paramCamera) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual getListener : ()Lorg/firstinspires/ftc/robotcore/external/navigation/VuforiaTrackable$Listener;
    //   6: aload_1
    //   7: aload_2
    //   8: aload_3
    //   9: aconst_null
    //   10: invokeinterface onTracked : (Lcom/vuforia/TrackableResult;Lorg/firstinspires/ftc/robotcore/external/hardware/camera/CameraName;Lorg/firstinspires/ftc/robotcore/external/hardware/camera/Camera;Lorg/firstinspires/ftc/robotcore/external/navigation/VuforiaTrackable;)V
    //   15: aload_0
    //   16: getfield parent : Lorg/firstinspires/ftc/robotcore/external/navigation/VuforiaTrackable;
    //   19: instanceof org/firstinspires/ftc/robotcore/internal/vuforia/VuforiaTrackableNotify
    //   22: ifeq -> 43
    //   25: aload_0
    //   26: getfield parent : Lorg/firstinspires/ftc/robotcore/external/navigation/VuforiaTrackable;
    //   29: invokeinterface getListener : ()Lorg/firstinspires/ftc/robotcore/external/navigation/VuforiaTrackable$Listener;
    //   34: aload_1
    //   35: aload_2
    //   36: aload_3
    //   37: aload_0
    //   38: invokeinterface onTracked : (Lcom/vuforia/TrackableResult;Lorg/firstinspires/ftc/robotcore/external/hardware/camera/CameraName;Lorg/firstinspires/ftc/robotcore/external/hardware/camera/Camera;Lorg/firstinspires/ftc/robotcore/external/navigation/VuforiaTrackable;)V
    //   43: aload_0
    //   44: monitorexit
    //   45: return
    //   46: astore_1
    //   47: aload_0
    //   48: monitorexit
    //   49: aload_1
    //   50: athrow
    // Exception table:
    //   from	to	target	type
    //   2	43	46	finally
  }
  
  public void setListener(VuforiaTrackable.Listener paramListener) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: astore_2
    //   4: aload_1
    //   5: ifnonnull -> 20
    //   8: new org/firstinspires/ftc/robotcore/external/navigation/VuforiaTrackableDefaultListener
    //   11: dup
    //   12: aload_0
    //   13: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/external/navigation/VuforiaTrackable;)V
    //   16: astore_2
    //   17: goto -> 20
    //   20: aload_0
    //   21: aload_2
    //   22: putfield listener : Lorg/firstinspires/ftc/robotcore/external/navigation/VuforiaTrackable$Listener;
    //   25: aload_2
    //   26: aload_0
    //   27: invokeinterface addTrackable : (Lorg/firstinspires/ftc/robotcore/external/navigation/VuforiaTrackable;)V
    //   32: aload_0
    //   33: monitorexit
    //   34: return
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_1
    //   38: athrow
    //   39: astore_1
    //   40: goto -> 35
    // Exception table:
    //   from	to	target	type
    //   8	17	39	finally
    //   20	32	39	finally
  }
  
  public void setLocation(OpenGLMatrix paramOpenGLMatrix) {
    setLocationFtcFieldFromTarget(paramOpenGLMatrix);
  }
  
  public void setLocationFtcFieldFromTarget(OpenGLMatrix paramOpenGLMatrix) {
    synchronized (this.locationLock) {
      this.ftcFieldFromTarget = paramOpenGLMatrix;
      return;
    } 
  }
  
  public void setName(String paramString) {
    this.name = paramString;
  }
  
  public void setUserData(Object paramObject) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield userData : Ljava/lang/Object;
    //   7: aload_0
    //   8: monitorexit
    //   9: return
    //   10: astore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: aload_1
    //   14: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	10	finally
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\vuforia\VuforiaTrackableImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
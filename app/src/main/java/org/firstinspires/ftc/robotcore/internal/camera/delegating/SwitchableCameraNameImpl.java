package org.firstinspires.ftc.robotcore.internal.camera.delegating;

import android.content.Context;
import com.qualcomm.robotcore.util.ThreadPool;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.function.ContinuationResult;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCharacteristics;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.api.UvcApiCameraCharacteristics;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.api.UvcApiCameraCharacteristicsBuilder;
import org.firstinspires.ftc.robotcore.internal.camera.names.CameraNameImplBase;
import org.firstinspires.ftc.robotcore.internal.collections.MutableReference;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.robotcore.internal.system.Tracer;

public class SwitchableCameraNameImpl extends CameraNameImplBase implements SwitchableCameraName {
  public static final String TAG = "SwitchableCameraName";
  
  public static boolean TRACE = true;
  
  protected final CameraName[] members;
  
  protected final Tracer tracer = Tracer.create("SwitchableCameraName", TRACE);
  
  private SwitchableCameraNameImpl(CameraName... paramVarArgs) {
    if (paramVarArgs.length != 0) {
      LinkedHashSet<CameraName> linkedHashSet = new LinkedHashSet();
      int j = paramVarArgs.length;
      for (int i = 0; i < j; i++)
        add(linkedHashSet, paramVarArgs[i]); 
      this.members = linkedHashSet.<CameraName>toArray(new CameraName[linkedHashSet.size()]);
      return;
    } 
    throw Misc.illegalArgumentException("the list of CameraNames cannot be empty");
  }
  
  protected static void add(Set<CameraName> paramSet, CameraName paramCameraName) {
    if (paramCameraName != null) {
      CameraName[] arrayOfCameraName;
      if (paramCameraName instanceof SwitchableCameraName) {
        arrayOfCameraName = ((SwitchableCameraName)paramCameraName).getMembers();
        int j = arrayOfCameraName.length;
        for (int i = 0; i < j; i++)
          add(paramSet, arrayOfCameraName[i]); 
      } else {
        paramSet.add((CameraName)arrayOfCameraName);
      } 
      return;
    } 
    throw Misc.illegalArgumentException("a member of a SwitchableCameraName cannot be null");
  }
  
  public static SwitchableCameraName forSwitchable(CameraName... paramVarArgs) {
    return new SwitchableCameraNameImpl(paramVarArgs);
  }
  
  public boolean allMembersAreWebcams() {
    CameraName[] arrayOfCameraName = this.members;
    int j = arrayOfCameraName.length;
    for (int i = 0; i < j; i++) {
      if (!arrayOfCameraName[i].isWebcam())
        return false; 
    } 
    return true;
  }
  
  public void asyncRequestCameraPermission(Context paramContext, Deadline paramDeadline, final Continuation<? extends Consumer<Boolean>> continuation) {
    final MutableReference goodSoFar = new MutableReference(Boolean.valueOf(true));
    final MutableReference responsesToCome = new MutableReference(Integer.valueOf(this.members.length));
    int i = 0;
    final MutableReference calledContinuation = new MutableReference(Boolean.valueOf(false));
    continuation = Continuation.create(ThreadPool.getDefault(), new Consumer<Boolean>() {
          public void accept(Boolean param1Boolean) {
            synchronized (goodSoFar) {
              final boolean stillGood;
              if (((Boolean)goodSoFar.getValue()).booleanValue() && param1Boolean.booleanValue()) {
                bool = true;
              } else {
                bool = false;
              } 
              goodSoFar.setValue(Boolean.valueOf(bool));
              responsesToCome.setValue(Integer.valueOf(((Integer)responsesToCome.getValue()).intValue() - 1));
              if ((!bool || ((Integer)responsesToCome.getValue()).intValue() == 0) && !((Boolean)calledContinuation.getValue()).booleanValue()) {
                calledContinuation.setValue(Boolean.valueOf(true));
                continuation.dispatch(new ContinuationResult<Consumer<Boolean>>() {
                      public void handle(Consumer<Boolean> param2Consumer) {
                        param2Consumer.accept(Boolean.valueOf(stillGood));
                      }
                    });
              } 
              return;
            } 
          }
        });
    CameraName[] arrayOfCameraName = this.members;
    int j = arrayOfCameraName.length;
    while (i < j) {
      arrayOfCameraName[i].asyncRequestCameraPermission(paramContext, paramDeadline, continuation);
      i++;
    } 
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject instanceof SwitchableCameraNameImpl) {
      paramObject = paramObject;
      return Arrays.equals((Object[])this.members, (Object[])((SwitchableCameraNameImpl)paramObject).members);
    } 
    return super.equals(paramObject);
  }
  
  public CameraCharacteristics getCameraCharacteristics() {
    CameraName[] arrayOfCameraName = getMembers();
    int j = arrayOfCameraName.length;
    Set set = null;
    for (int i = 0; i < j; i++) {
      CameraCharacteristics cameraCharacteristics = arrayOfCameraName[i].getCameraCharacteristics();
      this.tracer.trace("memberCharacteristics: %s", new Object[] { cameraCharacteristics });
      HashSet hashSet = new HashSet(cameraCharacteristics.getAllCameraModes());
      if (set == null) {
        set = hashSet;
      } else {
        set = Misc.intersect(set, hashSet);
      } 
    } 
    UvcApiCameraCharacteristics uvcApiCameraCharacteristics = UvcApiCameraCharacteristicsBuilder.buildFromModes(set);
    this.tracer.trace("result = %s", new Object[] { uvcApiCameraCharacteristics });
    return (CameraCharacteristics)uvcApiCameraCharacteristics;
  }
  
  public CameraName[] getMembers() {
    return this.members;
  }
  
  public int hashCode() {
    return Arrays.hashCode((Object[])this.members);
  }
  
  public boolean isSwitchable() {
    return true;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Switchable(");
    CameraName[] arrayOfCameraName = this.members;
    int j = arrayOfCameraName.length;
    boolean bool = true;
    int i = 0;
    while (i < j) {
      CameraName cameraName = arrayOfCameraName[i];
      if (!bool)
        stringBuilder.append("|"); 
      stringBuilder.append(cameraName.toString());
      i++;
      bool = false;
    } 
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\delegating\SwitchableCameraNameImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
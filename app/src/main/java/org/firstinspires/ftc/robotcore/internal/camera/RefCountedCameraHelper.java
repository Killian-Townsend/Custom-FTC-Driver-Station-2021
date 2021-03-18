package org.firstinspires.ftc.robotcore.internal.camera;

import org.firstinspires.ftc.robotcore.internal.system.RefCounted;
import org.firstinspires.ftc.robotcore.internal.system.Tracer;

public class RefCountedCameraHelper {
  protected boolean externalDestructed = false;
  
  protected final Runnable externalDestructor;
  
  protected int externalRefCount = 0;
  
  protected final Object outerLock;
  
  protected final RefCounted owner;
  
  protected final Tracer tracer;
  
  public RefCountedCameraHelper(RefCounted paramRefCounted, Object paramObject, Tracer paramTracer, Runnable paramRunnable) {
    this.owner = paramRefCounted;
    this.outerLock = paramObject;
    this.tracer = paramTracer;
    this.externalDestructor = paramRunnable;
  }
  
  public void addRefExternal() {
    synchronized (this.outerLock) {
      if (!this.externalDestructed) {
        this.externalRefCount++;
        this.owner.addRef();
      } 
      return;
    } 
  }
  
  public int releaseRefExternal() {
    synchronized (this.outerLock) {
      if (!this.externalDestructed) {
        int i = this.externalRefCount - 1;
        this.externalRefCount = i;
        if (i == 0) {
          this.externalDestructed = true;
          this.tracer.trace("externalDestructor()", this.externalDestructor);
        } 
        this.owner.releaseRef();
        return i;
      } 
    } 
    byte b = -1;
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_2} */
    return b;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\RefCountedCameraHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
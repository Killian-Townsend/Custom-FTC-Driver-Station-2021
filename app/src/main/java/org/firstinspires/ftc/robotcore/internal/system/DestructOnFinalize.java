package org.firstinspires.ftc.robotcore.internal.system;

import com.qualcomm.robotcore.util.RobotLog;

public abstract class DestructOnFinalize<ParentType extends RefCounted> extends RefCounted implements Finalizable {
  protected Finalizer finalizer = Finalizer.forTarget(this);
  
  protected boolean inFinalize = false;
  
  protected boolean ownParentRef = false;
  
  protected ParentType parent = null;
  
  protected DestructOnFinalize() {}
  
  protected DestructOnFinalize(RefCounted.TraceLevel paramTraceLevel) {
    super(paramTraceLevel);
  }
  
  protected void destructor() {
    if (this.ownParentRef) {
      this.parent.releaseRef();
      this.ownParentRef = false;
    } 
    super.destructor();
  }
  
  public void doFinalize() {
    synchronized (this.lock) {
      this.inFinalize = true;
      try {
        doLockAndDestruct();
        return;
      } finally {
        this.inFinalize = false;
      } 
    } 
  }
  
  protected ParentType getParent() {
    return this.parent;
  }
  
  protected void preDestructor() {
    if (traceDtor() && this.inFinalize)
      RobotLog.vv(getTag(), "finalize(%s)", new Object[] { getTraceIdentifier() }); 
    suppressFinalize();
    super.preDestructor();
  }
  
  protected void setParent(ParentType paramParentType) {
    synchronized (this.lock) {
      if (this.parent != paramParentType) {
        if (this.parent != null) {
          this.parent.releaseRef();
          this.ownParentRef = false;
        } 
        if (paramParentType != null) {
          paramParentType.addRef();
          this.ownParentRef = true;
        } 
        this.parent = paramParentType;
      } 
      return;
    } 
  }
  
  protected void suppressFinalize() {
    synchronized (this.lock) {
      if (this.finalizer != null) {
        this.finalizer.dispose();
        this.finalizer = null;
      } 
      return;
    } 
  }
  
  protected boolean traceDtor() {
    return (super.traceDtor() || this.inFinalize);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\DestructOnFinalize.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
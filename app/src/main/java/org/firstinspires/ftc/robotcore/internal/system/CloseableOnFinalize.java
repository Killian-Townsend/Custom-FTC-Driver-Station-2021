package org.firstinspires.ftc.robotcore.internal.system;

public abstract class CloseableOnFinalize<ParentType extends RefCounted> extends Closeable implements Finalizable {
  protected Finalizer finalizer = Finalizer.forTarget(this);
  
  protected boolean inFinalize = false;
  
  protected boolean ownParentRef = false;
  
  protected ParentType parent = null;
  
  protected void doClose() {
    if (this.ownParentRef) {
      this.parent.releaseRef();
      this.ownParentRef = false;
    } 
    super.doClose();
  }
  
  public void doFinalize() {
    synchronized (this.lock) {
      this.inFinalize = true;
      try {
        close();
        return;
      } finally {
        this.inFinalize = false;
      } 
    } 
  }
  
  protected ParentType getParent() {
    return this.parent;
  }
  
  protected void preClose() {
    suppressFinalize();
    super.preClose();
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
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\CloseableOnFinalize.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
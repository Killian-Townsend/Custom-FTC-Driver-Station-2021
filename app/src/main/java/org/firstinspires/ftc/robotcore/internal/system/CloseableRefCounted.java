package org.firstinspires.ftc.robotcore.internal.system;

public class CloseableRefCounted extends RefCounted {
  protected boolean closeCalled = false;
  
  protected int closeCount = 0;
  
  protected CloseableRefCounted() {}
  
  protected CloseableRefCounted(RefCounted.TraceLevel paramTraceLevel) {
    super(paramTraceLevel);
  }
  
  public final void close() {
    synchronized (this.lock) {
      if (this.closeCount != 0) {
        int i = this.closeCount - 1;
        this.closeCount = i;
        if (i == 0) {
          this.closeCalled = true;
          preClose();
          doClose();
          postClose();
          releaseRef();
        } 
      } 
      return;
    } 
  }
  
  protected final boolean ctorOnlyCloseNeededToDestruct() {
    synchronized (this.lock) {
      int i = this.closeCount;
      boolean bool = true;
      if (i != 1 || this.refCount.get() != 1)
        bool = false; 
      return bool;
    } 
  }
  
  protected void doClose() {}
  
  protected final void enableClose() {
    synchronized (this.lock) {
      if (!this.closeCalled) {
        int i = this.closeCount;
        this.closeCount = i + 1;
        if (i == 0)
          addRef(); 
        return;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("enableClose() on an already closed object: ");
      stringBuilder.append(this);
      throw new IllegalStateException(stringBuilder.toString());
    } 
  }
  
  protected final void enableOnlyClose() {
    enableClose();
    releaseRef();
    Assert.assertTrue(ctorOnlyCloseNeededToDestruct());
  }
  
  protected void postClose() {}
  
  protected void preClose() {}
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\CloseableRefCounted.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
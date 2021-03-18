package org.firstinspires.ftc.robotcore.internal.system;

public class Closeable {
  protected boolean closeCalled = false;
  
  protected int closeCount = 1;
  
  protected final Object lock = new Object();
  
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
        } 
      } 
      return;
    } 
  }
  
  protected final boolean ctorOnlyCloseNeededToDestruct() {
    return true;
  }
  
  protected void doClose() {}
  
  protected final void enableOnlyClose() {
    Assert.assertTrue(ctorOnlyCloseNeededToDestruct());
  }
  
  protected void postClose() {}
  
  protected void preClose() {}
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\Closeable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
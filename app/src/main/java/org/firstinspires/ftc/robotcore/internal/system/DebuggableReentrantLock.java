package org.firstinspires.ftc.robotcore.internal.system;

import java.util.concurrent.locks.ReentrantLock;

public class DebuggableReentrantLock extends ReentrantLock {
  public DebuggableReentrantLock() {}
  
  public DebuggableReentrantLock(boolean paramBoolean) {
    super(paramBoolean);
  }
  
  public Thread getOwner() {
    return super.getOwner();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\DebuggableReentrantLock.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.opmode;

import org.firstinspires.ftc.robotcore.internal.system.LockingRunner;

public class OnBotJavaBuildLocker {
  private static final LockingRunner lock = new LockingRunner();
  
  public static void lockBuildExclusiveWhile(Runnable paramRunnable) {
    try {
      lock.lockWhile(paramRunnable);
      return;
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opmode\OnBotJavaBuildLocker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
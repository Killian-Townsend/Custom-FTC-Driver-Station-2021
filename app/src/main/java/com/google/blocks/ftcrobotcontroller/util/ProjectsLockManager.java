package com.google.blocks.ftcrobotcontroller.util;

import org.firstinspires.ftc.robotcore.external.Supplier;
import org.firstinspires.ftc.robotcore.external.ThrowingCallable;
import org.firstinspires.ftc.robotcore.internal.system.LockingRunner;

public final class ProjectsLockManager {
  private static final LockingRunner PROJECTS_LOCK = new LockingRunner();
  
  public static <T> T lockProjectsWhile(Supplier<T> paramSupplier) {
    try {
      return (T)PROJECTS_LOCK.lockWhile(paramSupplier);
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return null;
    } 
  }
  
  public static <T, E extends Throwable> T lockProjectsWhile(ThrowingCallable<T, E> paramThrowingCallable) throws E {
    try {
      return (T)PROJECTS_LOCK.lockWhile(paramThrowingCallable);
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return null;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontrolle\\util\ProjectsLockManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
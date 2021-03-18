package com.qualcomm.hardware.lynx;

import com.qualcomm.robotcore.exception.RobotCoreException;

public interface Supplier<T> {
  T get() throws InterruptedException, RobotCoreException, LynxNackException;
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\Supplier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
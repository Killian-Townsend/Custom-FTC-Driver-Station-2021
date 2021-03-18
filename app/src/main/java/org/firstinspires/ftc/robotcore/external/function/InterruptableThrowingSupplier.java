package org.firstinspires.ftc.robotcore.external.function;

public interface InterruptableThrowingSupplier<VALUE, EXCEPTION extends Throwable> {
  VALUE get() throws EXCEPTION, InterruptedException;
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\function\InterruptableThrowingSupplier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
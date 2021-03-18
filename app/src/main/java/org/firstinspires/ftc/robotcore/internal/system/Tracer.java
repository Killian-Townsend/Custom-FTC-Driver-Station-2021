package org.firstinspires.ftc.robotcore.internal.system;

import com.qualcomm.robotcore.util.RobotLog;
import java.util.concurrent.Callable;
import org.firstinspires.ftc.robotcore.external.function.InterruptableThrowingRunnable;
import org.firstinspires.ftc.robotcore.external.function.Supplier;
import org.firstinspires.ftc.robotcore.external.function.ThrowingRunnable;
import org.firstinspires.ftc.robotcore.external.function.ThrowingSupplier;

public class Tracer {
  public static boolean DEBUG = true;
  
  protected boolean enableErrorTrace;
  
  protected boolean enableTrace;
  
  public final String tag;
  
  protected Tracer(String paramString, boolean paramBoolean1, boolean paramBoolean2) {
    this.tag = paramString;
    this.enableTrace = paramBoolean1;
    this.enableErrorTrace = paramBoolean2;
  }
  
  public static Tracer create(Class paramClass) {
    return create(paramClass.getSimpleName());
  }
  
  public static Tracer create(Object paramObject) {
    return create(paramObject.getClass().getSimpleName());
  }
  
  public static Tracer create(Object paramObject, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramObject.getClass().getSimpleName());
    stringBuilder.append(paramString);
    return create(stringBuilder.toString());
  }
  
  public static Tracer create(String paramString) {
    return create(paramString, DEBUG);
  }
  
  public static Tracer create(String paramString, boolean paramBoolean) {
    return create(paramString, paramBoolean, paramBoolean);
  }
  
  public static Tracer create(String paramString, boolean paramBoolean1, boolean paramBoolean2) {
    return new Tracer(paramString, paramBoolean1, paramBoolean2);
  }
  
  public boolean enableErrorTrace() {
    return this.enableErrorTrace;
  }
  
  public boolean enableTrace() {
    return this.enableTrace;
  }
  
  public String format(String paramString, Object... paramVarArgs) {
    return this.enableTrace ? Misc.formatInvariant(paramString, paramVarArgs) : "";
  }
  
  public String getTag() {
    return this.tag;
  }
  
  protected void log(String paramString, Object... paramVarArgs) {
    if (this.enableTrace)
      RobotLog.dd(getTag(), paramString, paramVarArgs); 
  }
  
  protected void logError(String paramString, Object... paramVarArgs) {
    if (this.enableErrorTrace)
      RobotLog.ee(getTag(), paramString, paramVarArgs); 
  }
  
  protected void logError(Throwable paramThrowable, String paramString, Object... paramVarArgs) {
    if (this.enableErrorTrace)
      RobotLog.ee(getTag(), paramThrowable, paramString, paramVarArgs); 
  }
  
  public <T> T trace(String paramString, Callable<T> paramCallable) throws Exception {
    log("%s...", new Object[] { paramString });
    try {
      paramCallable = (Callable<T>)paramCallable.call();
      return (T)paramCallable;
    } finally {
      log("...%s", new Object[] { paramString });
    } 
  }
  
  public <T> T trace(String paramString, Supplier<T> paramSupplier) {
    log("%s...", new Object[] { paramString });
    try {
      return (T)paramSupplier.get();
    } finally {
      log("...%s", new Object[] { paramString });
    } 
  }
  
  public <T, E extends Throwable> T trace(String paramString, ThrowingSupplier<T, E> paramThrowingSupplier) throws E {
    log("%s...", new Object[] { paramString });
    try {
      return (T)paramThrowingSupplier.get();
    } finally {
      log("...%s", new Object[] { paramString });
    } 
  }
  
  public void trace(String paramString, Runnable paramRunnable) {
    log("%s...", new Object[] { paramString });
    try {
      paramRunnable.run();
      return;
    } finally {
      log("...%s", new Object[] { paramString });
    } 
  }
  
  public <E extends Throwable> void trace(String paramString, InterruptableThrowingRunnable<E> paramInterruptableThrowingRunnable) throws E, InterruptedException {
    log("%s...", new Object[] { paramString });
    try {
      paramInterruptableThrowingRunnable.run();
      return;
    } finally {
      log("...%s", new Object[] { paramString });
    } 
  }
  
  public <E extends Throwable> void trace(String paramString, ThrowingRunnable<E> paramThrowingRunnable) throws E {
    log("%s...", new Object[] { paramString });
    try {
      paramThrowingRunnable.run();
      return;
    } finally {
      log("...%s", new Object[] { paramString });
    } 
  }
  
  public void trace(String paramString, Object... paramVarArgs) {
    log(paramString, paramVarArgs);
  }
  
  public void traceError(String paramString, Object... paramVarArgs) {
    logError(paramString, paramVarArgs);
  }
  
  public void traceError(Throwable paramThrowable, String paramString, Object... paramVarArgs) {
    logError(paramThrowable, paramString, paramVarArgs);
  }
  
  public <R> R traceResult(String paramString, Supplier<R> paramSupplier) {
    log("%s...", new Object[] { paramString });
    try {
      Object object = paramSupplier.get();
      return (R)object;
    } finally {
      log("...%s: %s", new Object[] { paramString, null });
    } 
  }
  
  public <R, E extends Throwable> R traceResult(String paramString, ThrowingSupplier<R, E> paramThrowingSupplier) throws E {
    log("%s...", new Object[] { paramString });
    try {
      Object object = paramThrowingSupplier.get();
      return (R)object;
    } finally {
      log("...%s: %s", new Object[] { paramString, null });
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\Tracer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.qualcomm.robotcore.eventloop.opmode;

import com.qualcomm.robotcore.hardware.TimestampedI2cData;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ThreadPool;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryInternal;

public abstract class LinearOpMode extends OpMode {
  private ExecutorService executorService = null;
  
  private LinearOpModeHelper helper = null;
  
  private volatile boolean isStarted = false;
  
  private final Object runningNotifier = new Object();
  
  private volatile boolean stopRequested = false;
  
  private boolean userMonitoredForStart = false;
  
  protected void handleLoop() {
    if (!this.helper.hasRuntimeException())
      synchronized (this.runningNotifier) {
        this.runningNotifier.notifyAll();
        return;
      }  
    throw this.helper.getRuntimeException();
  }
  
  public final void idle() {
    Thread.yield();
  }
  
  public final void init() {
    this.executorService = ThreadPool.newSingleThreadExecutor("LinearOpMode");
    this.helper = new LinearOpModeHelper();
    this.isStarted = false;
    this.stopRequested = false;
    this.executorService.execute(this.helper);
  }
  
  public final void init_loop() {
    handleLoop();
  }
  
  public void internalPostInitLoop() {
    if (this.telemetry instanceof TelemetryInternal)
      ((TelemetryInternal)this.telemetry).tryUpdateIfDirty(); 
  }
  
  public void internalPostLoop() {
    if (this.telemetry instanceof TelemetryInternal)
      ((TelemetryInternal)this.telemetry).tryUpdateIfDirty(); 
  }
  
  public final boolean isStarted() {
    boolean bool1 = this.isStarted;
    boolean bool = true;
    if (bool1)
      this.userMonitoredForStart = true; 
    if (!this.isStarted) {
      if (Thread.currentThread().isInterrupted())
        return true; 
      bool = false;
    } 
    return bool;
  }
  
  public final boolean isStopRequested() {
    return (this.stopRequested || Thread.currentThread().isInterrupted());
  }
  
  public final void loop() {
    handleLoop();
  }
  
  public final boolean opModeIsActive() {
    boolean bool;
    if (!isStopRequested() && isStarted()) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool)
      idle(); 
    return bool;
  }
  
  public abstract void runOpMode() throws InterruptedException;
  
  public final void sleep(long paramLong) {
    try {
      Thread.sleep(paramLong);
      return;
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return;
    } 
  }
  
  public final void start() {
    this.stopRequested = false;
    this.isStarted = true;
    synchronized (this.runningNotifier) {
      this.runningNotifier.notifyAll();
      return;
    } 
  }
  
  public final void stop() {
    if (this.stopRequested)
      return; 
    LinearOpModeHelper linearOpModeHelper = this.helper;
    if (linearOpModeHelper == null)
      return; 
    if (!this.userMonitoredForStart && linearOpModeHelper.userMethodReturned)
      RobotLog.addGlobalWarningMessage("The OpMode which was just initialized ended prematurely as a result of not monitoring for the start condition. Did you forget to call waitForStart()?"); 
    this.stopRequested = true;
    ExecutorService executorService = this.executorService;
    if (executorService != null) {
      executorService.shutdownNow();
      try {
        ThreadPool.awaitTermination(this.executorService, 100L, TimeUnit.DAYS, "user linear op mode");
        return;
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
      } 
    } 
  }
  
  public void waitForStart() {
    while (true) {
      if (!isStarted())
        synchronized (this.runningNotifier) {
          this.runningNotifier.wait();
          continue;
        }  
      return;
    } 
  }
  
  protected class LinearOpModeHelper implements Runnable {
    protected RuntimeException exception = null;
    
    protected boolean isShutdown = false;
    
    protected volatile boolean userMethodReturned = false;
    
    public RuntimeException getRuntimeException() {
      return this.exception;
    }
    
    public boolean hasRuntimeException() {
      return (this.exception != null);
    }
    
    public boolean isShutdown() {
      return this.isShutdown;
    }
    
    public void run() {
      ThreadPool.logThreadLifeCycle("LinearOpMode main", new Runnable() {
            public void run() {
              LinearOpMode.LinearOpModeHelper.this.exception = null;
              LinearOpMode.LinearOpModeHelper.this.isShutdown = false;
              try {
                LinearOpMode.this.runOpMode();
                LinearOpMode.LinearOpModeHelper.this.userMethodReturned = true;
              } catch (InterruptedException interruptedException) {
              
              } catch (CancellationException cancellationException) {
              
              } catch (RuntimeException runtimeException) {
              
              } finally {
                TimestampedI2cData.suppressNewHealthWarningsWhile(new Runnable() {
                      public void run() {
                        if (LinearOpMode.this.telemetry instanceof TelemetryInternal) {
                          LinearOpMode.this.telemetry.setMsTransmissionInterval(0);
                          ((TelemetryInternal)LinearOpMode.this.telemetry).tryUpdateIfDirty();
                        } 
                      }
                    });
                LinearOpMode.LinearOpModeHelper.this.isShutdown = true;
              } 
              TimestampedI2cData.suppressNewHealthWarningsWhile((Runnable)SYNTHETIC_LOCAL_VARIABLE_1);
              LinearOpMode.LinearOpModeHelper.this.isShutdown = true;
            }
          });
    }
  }
  
  class null implements Runnable {
    public void run() {
      this.this$1.exception = null;
      this.this$1.isShutdown = false;
      try {
        LinearOpMode.this.runOpMode();
        this.this$1.userMethodReturned = true;
      } catch (InterruptedException interruptedException) {
      
      } catch (CancellationException cancellationException) {
      
      } catch (RuntimeException runtimeException) {
      
      } finally {
        TimestampedI2cData.suppressNewHealthWarningsWhile(new Runnable() {
              public void run() {
                if (LinearOpMode.this.telemetry instanceof TelemetryInternal) {
                  LinearOpMode.this.telemetry.setMsTransmissionInterval(0);
                  ((TelemetryInternal)LinearOpMode.this.telemetry).tryUpdateIfDirty();
                } 
              }
            });
        this.this$1.isShutdown = true;
      } 
      TimestampedI2cData.suppressNewHealthWarningsWhile((Runnable)SYNTHETIC_LOCAL_VARIABLE_1);
      this.this$1.isShutdown = true;
    }
  }
  
  class null implements Runnable {
    public void run() {
      if (LinearOpMode.this.telemetry instanceof TelemetryInternal) {
        LinearOpMode.this.telemetry.setMsTransmissionInterval(0);
        ((TelemetryInternal)LinearOpMode.this.telemetry).tryUpdateIfDirty();
      } 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\eventloop\opmode\LinearOpMode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
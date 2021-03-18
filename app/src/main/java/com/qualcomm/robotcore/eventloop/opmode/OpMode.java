package com.qualcomm.robotcore.eventloop.opmode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.robocol.TelemetryMessage;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeServices;
import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryImpl;
import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryInternal;

public abstract class OpMode {
  public Gamepad gamepad1 = null;
  
  public Gamepad gamepad2 = null;
  
  public HardwareMap hardwareMap = null;
  
  public OpModeServices internalOpModeServices = null;
  
  public int msStuckDetectInit = 5000;
  
  public int msStuckDetectInitLoop = 5000;
  
  public int msStuckDetectLoop = 5000;
  
  public int msStuckDetectStart = 5000;
  
  public int msStuckDetectStop = 900;
  
  private long startTime = 0L;
  
  public Telemetry telemetry = (Telemetry)new TelemetryImpl(this);
  
  public double time = 0.0D;
  
  public OpMode() {
    this.startTime = System.nanoTime();
  }
  
  public double getRuntime() {
    double d = TimeUnit.SECONDS.toNanos(1L);
    return (System.nanoTime() - this.startTime) / d;
  }
  
  public abstract void init();
  
  public void init_loop() {}
  
  public void internalPostInitLoop() {
    this.telemetry.update();
  }
  
  public void internalPostLoop() {
    this.telemetry.update();
  }
  
  public void internalPreInit() {
    Telemetry telemetry = this.telemetry;
    if (telemetry instanceof TelemetryInternal)
      ((TelemetryInternal)telemetry).resetTelemetryForOpMode(); 
  }
  
  public final void internalUpdateTelemetryNow(TelemetryMessage paramTelemetryMessage) {
    this.internalOpModeServices.refreshUserTelemetry(paramTelemetryMessage, 0.0D);
  }
  
  public abstract void loop();
  
  public final void requestOpModeStop() {
    this.internalOpModeServices.requestOpModeStop(this);
  }
  
  public void resetStartTime() {
    this.startTime = System.nanoTime();
  }
  
  public void start() {}
  
  public void stop() {}
  
  public void updateTelemetry(Telemetry paramTelemetry) {
    paramTelemetry.update();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\eventloop\opmode\OpMode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
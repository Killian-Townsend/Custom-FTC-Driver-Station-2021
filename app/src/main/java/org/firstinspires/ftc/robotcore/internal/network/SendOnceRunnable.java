package org.firstinspires.ftc.robotcore.internal.network;

import android.os.SystemClock;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.robocol.Heartbeat;
import com.qualcomm.robotcore.robocol.KeepAlive;
import com.qualcomm.robotcore.robocol.RobocolParsable;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CopyOnWriteArrayList;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.ui.RobotCoreGamepadManager;

public class SendOnceRunnable implements Runnable {
  public static final double ASSUME_DISCONNECT_TIMER = 2.0D;
  
  public static boolean DEBUG = false;
  
  public static final long GAMEPAD_UPDATE_THRESHOLD = 1000L;
  
  public static final int MAX_COMMAND_ATTEMPTS = 10;
  
  public static final int MS_BATCH_TRANSMISSION_INTERVAL = 40;
  
  public static final int MS_HEARTBEAT_TRANSMISSION_INTERVAL = 100;
  
  public static final int MS_KEEPALIVE_TRANSMISSION_INTERVAL = 20;
  
  public static final String TAG = "Robocol";
  
  protected final AppUtil appUtil = AppUtil.getInstance();
  
  protected DisconnectionCallback disconnectionCallback;
  
  protected Heartbeat heartbeatSend = new Heartbeat();
  
  protected KeepAlive keepAliveSend = new KeepAlive();
  
  protected final ElapsedTime lastRecvPacket;
  
  protected final Parameters parameters;
  
  protected volatile List<Command> pendingCommands = new CopyOnWriteArrayList<Command>();
  
  public SendOnceRunnable(DisconnectionCallback paramDisconnectionCallback, ElapsedTime paramElapsedTime) {
    this.disconnectionCallback = paramDisconnectionCallback;
    this.lastRecvPacket = paramElapsedTime;
    this.parameters = new Parameters();
    RobotLog.vv("Robocol", "SendOnceRunnable created");
  }
  
  public void clearCommands() {
    this.pendingCommands.clear();
  }
  
  public boolean removeCommand(Command paramCommand) {
    return this.pendingCommands.remove(paramCommand);
  }
  
  public void run() {
    NetworkConnectionHandler networkConnectionHandler = NetworkConnectionHandler.getInstance();
    try {
      byte b1;
      double d = this.lastRecvPacket.seconds();
      if (this.parameters.disconnectOnTimeout && d > 2.0D) {
        this.disconnectionCallback.disconnected();
        return;
      } 
      if (this.parameters.originateHeartbeats && this.heartbeatSend.getElapsedSeconds() > 0.1D) {
        Heartbeat heartbeat = Heartbeat.createWithTimeStamp();
        this.heartbeatSend = heartbeat;
        heartbeat.setTimeZoneId(TimeZone.getDefault().getID());
        this.heartbeatSend.t0 = this.appUtil.getWallClockTime();
        networkConnectionHandler.sendDataToPeer((RobocolParsable)this.heartbeatSend);
        b1 = 1;
      } else {
        b1 = 0;
      } 
      byte b2 = b1;
      if (this.parameters.gamepadManager != null) {
        long l1 = SystemClock.uptimeMillis();
        Iterator<Gamepad> iterator = this.parameters.gamepadManager.getGamepadsForTransmission().iterator();
        while (true) {
          b2 = b1;
          if (iterator.hasNext()) {
            Gamepad gamepad = iterator.next();
            if (l1 - gamepad.timestamp > 1000L && gamepad.atRest())
              continue; 
            gamepad.setSequenceNumber();
            networkConnectionHandler.sendDataToPeer((RobocolParsable)gamepad);
            b1 = 1;
            continue;
          } 
          break;
        } 
      } 
      if (!b2 && this.parameters.originateKeepAlives && this.keepAliveSend.getElapsedSeconds() > 0.02D) {
        KeepAlive keepAlive = KeepAlive.createWithTimeStamp();
        this.keepAliveSend = keepAlive;
        networkConnectionHandler.sendDataToPeer((RobocolParsable)keepAlive);
      } 
      long l = System.nanoTime();
      ArrayList<Command> arrayList = new ArrayList();
      for (Command command : this.pendingCommands) {
        b1 = command.getAttempts();
        if (b1 > 10 || command.hasExpired()) {
          RobotLog.vv("Robocol", String.format(AppUtil.getDefContext().getString(R.string.configGivingUpOnCommand), new Object[] { command.getName(), Integer.valueOf(command.getSequenceNumber()), Byte.valueOf(command.getAttempts()) }));
          arrayList.add(command);
          continue;
        } 
        if (command.isAcknowledged() || command.shouldTransmit(l)) {
          if (!command.isAcknowledged()) {
            RobotLog.vv("Robocol", "sending %s(%d), attempt: %d", new Object[] { command.getName(), Integer.valueOf(command.getSequenceNumber()), Byte.valueOf(command.getAttempts()) });
          } else if (DEBUG) {
            RobotLog.vv("Robocol", "acking %s(%d)", new Object[] { command.getName(), Integer.valueOf(command.getSequenceNumber()) });
          } 
          networkConnectionHandler.sendDataToPeer((RobocolParsable)command);
          if (command.isAcknowledged())
            arrayList.add(command); 
        } 
      } 
      this.pendingCommands.removeAll(arrayList);
      return;
    } catch (Exception exception) {
      exception.printStackTrace();
      return;
    } 
  }
  
  public void sendCommand(Command paramCommand) {
    this.pendingCommands.add(paramCommand);
  }
  
  public static interface DisconnectionCallback {
    void disconnected();
  }
  
  public static class Parameters {
    public boolean disconnectOnTimeout = true;
    
    public volatile RobotCoreGamepadManager gamepadManager = null;
    
    public boolean originateHeartbeats = AppUtil.getInstance().isDriverStation();
    
    public boolean originateKeepAlives = false;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\SendOnceRunnable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
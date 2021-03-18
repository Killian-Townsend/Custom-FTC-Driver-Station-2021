package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ThreadPool;
import java.net.InetAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class PeerDiscoveryManager {
  private static final boolean DEBUG = false;
  
  public static final String TAG = "PeerDiscovery";
  
  private ScheduledFuture<?> discoveryLoopFuture;
  
  private ScheduledExecutorService discoveryLoopService;
  
  private CountDownLatch interlock = new CountDownLatch(1);
  
  private final PeerDiscovery message;
  
  private InetAddress peerDiscoveryDevice;
  
  private final RobocolDatagramSocket socket;
  
  public PeerDiscoveryManager(RobocolDatagramSocket paramRobocolDatagramSocket, InetAddress paramInetAddress) {
    this.socket = paramRobocolDatagramSocket;
    this.message = new PeerDiscovery(PeerDiscovery.PeerType.PEER);
    this.peerDiscoveryDevice = paramInetAddress;
    start();
  }
  
  private void start() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Starting peer discovery remote: ");
    stringBuilder.append(this.peerDiscoveryDevice.toString());
    stringBuilder.append(" local: ");
    stringBuilder.append(this.socket.getLocalAddress().toString());
    RobotLog.vv("PeerDiscovery", stringBuilder.toString());
    if (this.peerDiscoveryDevice.equals(this.socket.getLocalAddress())) {
      RobotLog.vv("PeerDiscovery", "No need for initiating peer discovery, we are the Robot Controller");
    } else {
      ThreadPool.RecordingScheduledExecutor recordingScheduledExecutor = ThreadPool.newScheduledExecutor(1, "discovery service");
      this.discoveryLoopService = (ScheduledExecutorService)recordingScheduledExecutor;
      this.discoveryLoopFuture = recordingScheduledExecutor.scheduleAtFixedRate(new PeerDiscoveryRunnable(), 1L, 1L, TimeUnit.SECONDS);
    } 
    this.interlock.countDown();
  }
  
  public InetAddress getPeerDiscoveryDevice() {
    return this.peerDiscoveryDevice;
  }
  
  public void stop() {
    RobotLog.vv("PeerDiscovery", "Stopping peer discovery");
    try {
      this.interlock.await();
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
    } 
    ScheduledFuture<?> scheduledFuture = this.discoveryLoopFuture;
    if (scheduledFuture != null) {
      scheduledFuture.cancel(true);
      this.discoveryLoopFuture = null;
    } 
  }
  
  private class PeerDiscoveryRunnable implements Runnable {
    private PeerDiscoveryRunnable() {}
    
    public void run() {
      try {
        RobocolDatagram robocolDatagram = new RobocolDatagram(PeerDiscoveryManager.this.message, PeerDiscoveryManager.this.peerDiscoveryDevice);
        PeerDiscoveryManager.this.socket.send(robocolDatagram);
        return;
      } catch (RobotCoreException robotCoreException) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to send peer discovery packet: ");
        stringBuilder.append(robotCoreException.toString());
        RobotLog.ee("PeerDiscovery", stringBuilder.toString());
        return;
      } 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\robocol\PeerDiscoveryManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
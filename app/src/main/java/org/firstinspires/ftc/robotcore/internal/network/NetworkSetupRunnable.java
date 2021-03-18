package org.firstinspires.ftc.robotcore.internal.network;

import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.robocol.PeerDiscoveryManager;
import com.qualcomm.robotcore.robocol.RobocolDatagramSocket;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ThreadPool;
import com.qualcomm.robotcore.wifi.NetworkConnection;
import java.net.SocketException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class NetworkSetupRunnable implements Runnable {
  public static final String TAG = "SetupRunnable";
  
  protected CountDownLatch countDownLatch;
  
  protected final ElapsedTime lastRecvPacket;
  
  protected NetworkConnection networkConnection;
  
  protected PeerDiscoveryManager peerDiscoveryManager;
  
  protected RecvLoopRunnable.RecvLoopCallback recvLoopCallback;
  
  protected volatile RecvLoopRunnable recvLoopRunnable;
  
  protected ExecutorService recvLoopService;
  
  protected RobocolDatagramSocket socket;
  
  public NetworkSetupRunnable(RecvLoopRunnable.RecvLoopCallback paramRecvLoopCallback, NetworkConnection paramNetworkConnection, ElapsedTime paramElapsedTime) {
    this.recvLoopCallback = paramRecvLoopCallback;
    this.networkConnection = paramNetworkConnection;
    this.lastRecvPacket = paramElapsedTime;
    this.countDownLatch = new CountDownLatch(1);
  }
  
  protected void closeSocket() {
    RobocolDatagramSocket robocolDatagramSocket = this.socket;
    if (robocolDatagramSocket != null) {
      robocolDatagramSocket.close();
      this.socket = null;
    } 
  }
  
  public long getBytesPerSecond() {
    return (this.recvLoopRunnable != null) ? this.recvLoopRunnable.getBytesPerSecond() : 0L;
  }
  
  public long getRxDataCount() {
    return this.socket.getRxDataCount();
  }
  
  public RobocolDatagramSocket getSocket() {
    return this.socket;
  }
  
  public long getTxDataCount() {
    return this.socket.getTxDataCount();
  }
  
  public void injectReceivedCommand(Command paramCommand) {
    RecvLoopRunnable recvLoopRunnable = this.recvLoopRunnable;
    if (recvLoopRunnable != null) {
      recvLoopRunnable.injectReceivedCommand(paramCommand);
      return;
    } 
    RobotLog.vv("SetupRunnable", "injectReceivedCommand(): recvLoopRunnable==null; command ignored");
  }
  
  public void run() {
    ThreadPool.logThreadLifeCycle("SetupRunnable.run()", new Runnable() {
          public void run() {
            try {
              if (NetworkSetupRunnable.this.socket != null)
                NetworkSetupRunnable.this.socket.close(); 
              NetworkSetupRunnable.this.socket = new RobocolDatagramSocket();
              NetworkSetupRunnable.this.socket.listenUsingDestination(NetworkSetupRunnable.this.networkConnection.getConnectionOwnerAddress());
            } catch (SocketException socketException) {
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("Failed to open socket: ");
              stringBuilder.append(socketException.toString());
              RobotLog.e(stringBuilder.toString());
            } 
            NetworkSetupRunnable.this.recvLoopService = ThreadPool.newFixedThreadPool(2, "ReceiveLoopService");
            NetworkSetupRunnable.this.recvLoopRunnable = new RecvLoopRunnable(NetworkSetupRunnable.this.recvLoopCallback, NetworkSetupRunnable.this.socket, NetworkSetupRunnable.this.lastRecvPacket);
            RecvLoopRunnable recvLoopRunnable = NetworkSetupRunnable.this.recvLoopRunnable;
            recvLoopRunnable.getClass();
            RecvLoopRunnable.CommandProcessor commandProcessor = new RecvLoopRunnable.CommandProcessor(recvLoopRunnable);
            NetworkConnectionHandler.getInstance().setRecvLoopRunnable(NetworkSetupRunnable.this.recvLoopRunnable);
            NetworkSetupRunnable.this.recvLoopService.execute(commandProcessor);
            NetworkSetupRunnable.this.recvLoopService.execute(NetworkSetupRunnable.this.recvLoopRunnable);
            if (NetworkSetupRunnable.this.peerDiscoveryManager != null)
              NetworkSetupRunnable.this.peerDiscoveryManager.stop(); 
            NetworkSetupRunnable.this.peerDiscoveryManager = new PeerDiscoveryManager(NetworkSetupRunnable.this.socket, NetworkSetupRunnable.this.networkConnection.getConnectionOwnerAddress());
            NetworkSetupRunnable.this.countDownLatch.countDown();
            RobotLog.v("Setup complete");
          }
        });
  }
  
  public void shutdown() {
    RobotLog.ii("SetupRunnable", "Shutting down setup and receive loop");
    try {
      this.countDownLatch.await();
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
    } 
    ExecutorService executorService = this.recvLoopService;
    if (executorService != null) {
      executorService.shutdownNow();
      ThreadPool.awaitTerminationOrExitApplication(this.recvLoopService, 5L, TimeUnit.SECONDS, "ReceiveLoopService", "internal error");
      this.recvLoopService = null;
      this.recvLoopRunnable = null;
    } 
    stopPeerDiscovery();
    closeSocket();
  }
  
  public void stopPeerDiscovery() {
    PeerDiscoveryManager peerDiscoveryManager = this.peerDiscoveryManager;
    if (peerDiscoveryManager != null) {
      peerDiscoveryManager.stop();
      this.peerDiscoveryManager = null;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\NetworkSetupRunnable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
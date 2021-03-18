package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.util.RobotLog;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class RobocolDatagramSocket {
  private static final boolean DEBUG = false;
  
  public static final String TAG = "Robocol";
  
  private static final boolean VERBOSE_DEBUG = false;
  
  private final Object bindCloseLock = new Object();
  
  private int msReceiveTimeout;
  
  private int receiveBufferSize;
  
  private boolean recvErrorReported = false;
  
  private final Object recvLock = new Object();
  
  private long rxDataSample = 0L;
  
  private long rxDataTotal = 0L;
  
  private int sendBufferSize;
  
  private boolean sendErrorReported = false;
  
  private final Object sendLock = new Object();
  
  private DatagramSocket socket;
  
  private volatile State state = State.CLOSED;
  
  private boolean trafficDataCollection = false;
  
  private long txDataSample = 0L;
  
  private long txDataTotal = 0L;
  
  public void bind(InetSocketAddress paramInetSocketAddress) throws SocketException {
    synchronized (this.bindCloseLock) {
      if (this.state != State.CLOSED)
        close(); 
      this.state = State.LISTENING;
      DatagramSocket datagramSocket = new DatagramSocket(paramInetSocketAddress);
      this.socket = datagramSocket;
      this.sendErrorReported = false;
      this.recvErrorReported = false;
      datagramSocket.setSoTimeout(300);
      this.receiveBufferSize = Math.min(65520, this.socket.getReceiveBufferSize());
      this.sendBufferSize = this.socket.getSendBufferSize();
      this.msReceiveTimeout = this.socket.getSoTimeout();
      RobotLog.dd("Robocol", String.format("RobocolDatagramSocket listening addr=%s cbRec=%d cbSend=%d msRecTO=%d", new Object[] { paramInetSocketAddress.toString(), Integer.valueOf(this.receiveBufferSize), Integer.valueOf(this.sendBufferSize), Integer.valueOf(this.msReceiveTimeout) }));
      return;
    } 
  }
  
  public void close() {
    synchronized (this.bindCloseLock) {
      this.state = State.CLOSED;
      if (this.socket != null)
        this.socket.close(); 
      RobotLog.dd("Robocol", "RobocolDatagramSocket is closed");
      return;
    } 
  }
  
  public void gatherTrafficData(boolean paramBoolean) {
    this.trafficDataCollection = paramBoolean;
  }
  
  public InetAddress getInetAddress() {
    DatagramSocket datagramSocket = this.socket;
    return (datagramSocket == null) ? null : datagramSocket.getInetAddress();
  }
  
  public InetAddress getLocalAddress() {
    DatagramSocket datagramSocket = this.socket;
    return (datagramSocket == null) ? null : datagramSocket.getLocalAddress();
  }
  
  public long getRxDataCount() {
    return this.rxDataTotal;
  }
  
  public long getRxDataSample() {
    return this.rxDataSample;
  }
  
  public State getState() {
    return this.state;
  }
  
  public long getTxDataCount() {
    return this.txDataTotal;
  }
  
  public long getTxDataSample() {
    return this.txDataSample;
  }
  
  public boolean isClosed() {
    return (this.state == State.CLOSED);
  }
  
  public boolean isRunning() {
    return (this.state == State.LISTENING);
  }
  
  public void listenUsingDestination(InetAddress paramInetAddress) throws SocketException {
    bind(new InetSocketAddress(RobocolConfig.determineBindAddress(paramInetAddress), 20884));
  }
  
  public RobocolDatagram recv() {
    synchronized (this.recvLock) {
      RobocolDatagram robocolDatagram = RobocolDatagram.forReceive(this.receiveBufferSize);
      DatagramPacket datagramPacket = robocolDatagram.getPacket();
      try {
        DatagramSocket datagramSocket = this.socket;
        if (datagramSocket == null)
          return null; 
        this.socket.receive(datagramPacket);
        if (this.trafficDataCollection)
          this.rxDataSample += robocolDatagram.getPayloadLength(); 
        return robocolDatagram;
      } catch (SocketException socketException) {
      
      } catch (SocketTimeoutException socketTimeoutException) {
      
      } catch (IOException iOException) {
        RobotLog.logExceptionHeader("Robocol", iOException, "no packet received", new Object[0]);
        return null;
      } catch (RuntimeException runtimeException) {
        RobotLog.logExceptionHeader("Robocol", runtimeException, "no packet received", new Object[0]);
        return null;
      } 
      if (!this.recvErrorReported) {
        this.recvErrorReported = true;
        RobotLog.logExceptionHeader("Robocol", runtimeException, "no packet received", new Object[0]);
      } 
      return null;
    } 
  }
  
  public void resetDataSample() {
    this.rxDataTotal += this.rxDataSample;
    this.txDataTotal += this.txDataSample;
    this.rxDataSample = 0L;
    this.txDataSample = 0L;
  }
  
  public void send(RobocolDatagram paramRobocolDatagram) {
    Object object = this.sendLock;
    /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
    try {
      if (paramRobocolDatagram.getLength() <= this.sendBufferSize) {
        this.socket.send(paramRobocolDatagram.getPacket());
        if (this.trafficDataCollection)
          this.txDataSample += paramRobocolDatagram.getPayloadLength(); 
      } else {
        throw new RuntimeException(String.format("send packet too large: size=%d max=%d", new Object[] { Integer.valueOf(paramRobocolDatagram.getLength()), Integer.valueOf(this.sendBufferSize) }));
      } 
    } catch (RuntimeException runtimeException) {
      RobotLog.logExceptionHeader("Robocol", runtimeException, "exception sending datagram", new Object[0]);
    } catch (IOException iOException) {
      if (!this.sendErrorReported) {
        this.sendErrorReported = true;
        RobotLog.logExceptionHeader("Robocol", iOException, "exception sending datagram", new Object[0]);
      } 
    } finally {}
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
  }
  
  public enum State {
    CLOSED, ERROR, LISTENING;
    
    static {
      State state = new State("ERROR", 2);
      ERROR = state;
      $VALUES = new State[] { LISTENING, CLOSED, state };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\robocol\RobocolDatagramSocket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
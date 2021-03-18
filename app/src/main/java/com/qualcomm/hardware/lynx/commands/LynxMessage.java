package com.qualcomm.hardware.lynx.commands;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.robotcore.util.TypeConversion;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.firstinspires.ftc.robotcore.internal.hardware.TimeWindow;

public abstract class LynxMessage {
  protected boolean hasBeenTransmitted;
  
  protected byte messageNumber;
  
  protected LynxModuleIntf module;
  
  protected long nanotimeLastTransmit;
  
  protected TimeWindow payloadTimeWindow;
  
  protected byte referenceNumber;
  
  protected LynxDatagram serialization;
  
  public LynxMessage(LynxModuleIntf paramLynxModuleIntf) {
    this.module = paramLynxModuleIntf;
    this.messageNumber = 0;
    this.referenceNumber = 0;
    this.serialization = null;
    this.hasBeenTransmitted = false;
    this.nanotimeLastTransmit = 0L;
    setPayloadTimeWindow(null);
  }
  
  public static Object invokeStaticNullaryMethod(Class paramClass, String paramString) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    Method method = paramClass.getDeclaredMethod(paramString, new Class[0]);
    if ((method.getModifiers() & 0x9) == 9 && (method.getModifiers() & 0x400) == 0)
      return method.invoke(null, new Object[0]); 
    throw new IllegalAccessException("incorrect modifiers");
  }
  
  public void acquireNetworkLock() throws InterruptedException {
    this.module.acquireNetworkTransmissionLock(this);
  }
  
  public void forgetSerialization() {
    setSerialization(null);
  }
  
  public abstract void fromPayloadByteArray(byte[] paramArrayOfbyte);
  
  public abstract int getCommandNumber();
  
  public int getDestModuleAddress() {
    return getModule().getModuleAddress();
  }
  
  public int getMessageNumber() {
    return TypeConversion.unsignedByteToInt(this.messageNumber);
  }
  
  public LynxModuleIntf getModule() {
    return this.module;
  }
  
  public int getModuleAddress() {
    return this.module.getModuleAddress();
  }
  
  public long getNanotimeLastTransmit() {
    return this.nanotimeLastTransmit;
  }
  
  public TimeWindow getPayloadTimeWindow() {
    return this.payloadTimeWindow;
  }
  
  public int getReferenceNumber() {
    return TypeConversion.unsignedByteToInt(this.referenceNumber);
  }
  
  public LynxDatagram getSerialization() {
    return this.serialization;
  }
  
  public boolean hasBeenTransmitted() {
    return this.hasBeenTransmitted;
  }
  
  public boolean isAck() {
    return false;
  }
  
  public boolean isAckable() {
    return false;
  }
  
  public boolean isNack() {
    return false;
  }
  
  public boolean isResponse() {
    return false;
  }
  
  public boolean isResponseExpected() {
    return false;
  }
  
  public void loadFromSerialization() {
    setPayloadTimeWindow(this.serialization.getPayloadTimeWindow());
    fromPayloadByteArray(this.serialization.getPayloadData());
    setMessageNumber(this.serialization.getMessageNumber());
    setReferenceNumber(this.serialization.getReferenceNumber());
  }
  
  public void noteHasBeenTransmitted() {
    this.hasBeenTransmitted = true;
  }
  
  public void noteRetransmission() {}
  
  public void onPretendTransmit() throws InterruptedException {}
  
  public void releaseNetworkLock() throws InterruptedException {
    this.module.releaseNetworkTransmissionLock(this);
  }
  
  public void resetModulePingTimer() {
    this.module.resetPingTimer(this);
  }
  
  public void setMessageNumber(int paramInt) {
    this.messageNumber = (byte)paramInt;
  }
  
  public void setModule(LynxModule paramLynxModule) {
    this.module = (LynxModuleIntf)paramLynxModule;
  }
  
  public void setNanotimeLastTransmit(long paramLong) {
    this.nanotimeLastTransmit = paramLong;
  }
  
  public void setPayloadTimeWindow(TimeWindow paramTimeWindow) {
    this.payloadTimeWindow = paramTimeWindow;
  }
  
  public void setReferenceNumber(int paramInt) {
    this.referenceNumber = (byte)paramInt;
  }
  
  public void setSerialization(LynxDatagram paramLynxDatagram) {
    this.serialization = paramLynxDatagram;
  }
  
  public abstract byte[] toPayloadByteArray();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\LynxMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
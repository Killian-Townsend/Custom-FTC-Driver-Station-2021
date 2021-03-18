package com.qualcomm.hardware.modernrobotics.comm;

import com.qualcomm.robotcore.util.TypeConversion;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public abstract class ModernRoboticsDatagram {
  public static final int CB_HEADER = 5;
  
  public static final int IB_ADDRESS = 3;
  
  public static final int IB_FUNCTION = 2;
  
  public static final int IB_LENGTH = 4;
  
  public static final int IB_SYNC_0 = 0;
  
  public static final int IB_SYNC_1 = 1;
  
  public byte[] data;
  
  protected ModernRoboticsDatagram(int paramInt) {
    this.data = new byte[paramInt + 5];
  }
  
  public void clearPayload() {
    byte[] arrayOfByte = this.data;
    Arrays.fill(arrayOfByte, 5, arrayOfByte.length, (byte)0);
  }
  
  public int getAddress() {
    return TypeConversion.unsignedByteToInt(this.data[3]);
  }
  
  public int getAllocatedPayload() {
    return this.data.length - 5;
  }
  
  public int getFunction() {
    return this.data[2] & Byte.MAX_VALUE;
  }
  
  public int getPayloadLength() {
    return TypeConversion.unsignedByteToInt(this.data[4]);
  }
  
  protected void initialize(int paramInt1, int paramInt2) {
    byte[] arrayOfByte = this.data;
    arrayOfByte[0] = (byte)paramInt1;
    arrayOfByte[1] = (byte)paramInt2;
    arrayOfByte[2] = 0;
    arrayOfByte[3] = 0;
    arrayOfByte[4] = 0;
  }
  
  public boolean isFailure() {
    byte[] arrayOfByte = this.data;
    return (arrayOfByte[2] == -1 && arrayOfByte[3] == -1);
  }
  
  public boolean isRead() {
    return ((this.data[2] & 0x80) != 0);
  }
  
  public boolean isWrite() {
    return isRead() ^ true;
  }
  
  public void setAddress(int paramInt) {
    if (paramInt >= 0 && paramInt <= 255) {
      this.data[3] = (byte)paramInt;
      return;
    } 
    throw new IllegalArgumentException(String.format("address=%d; must be unsigned byte", new Object[] { Integer.valueOf(paramInt) }));
  }
  
  public void setFunction(int paramInt) {
    byte[] arrayOfByte = this.data;
    arrayOfByte[2] = (byte)(paramInt & 0x7F | arrayOfByte[2] & 0x80);
  }
  
  public void setPayload(byte[] paramArrayOfbyte) {
    setPayloadLength(paramArrayOfbyte.length);
    System.arraycopy(paramArrayOfbyte, 0, this.data, 5, paramArrayOfbyte.length);
  }
  
  public void setPayloadLength(int paramInt) {
    if (paramInt >= 0 && paramInt <= 255) {
      this.data[4] = (byte)paramInt;
      return;
    } 
    throw new IllegalArgumentException(String.format("length=%d; must be unsigned byte", new Object[] { Integer.valueOf(paramInt) }));
  }
  
  public void setRead() {
    setRead(getFunction());
  }
  
  public void setRead(int paramInt) {
    this.data[2] = (byte)(paramInt & 0x7F | 0x80);
  }
  
  public void setWrite() {
    setWrite(getFunction());
  }
  
  public void setWrite(int paramInt) {
    this.data[2] = (byte)(paramInt & 0x7F);
  }
  
  public static class AllocationContext<DATAGRAM_TYPE extends ModernRoboticsDatagram> {
    protected AtomicReference<DATAGRAM_TYPE> cacheHeaderOnly0 = new AtomicReference<DATAGRAM_TYPE>(null);
    
    protected AtomicReference<DATAGRAM_TYPE> cacheHeaderOnly1 = new AtomicReference<DATAGRAM_TYPE>(null);
    
    protected AtomicReference<DATAGRAM_TYPE> cachedFullInstance0 = new AtomicReference<DATAGRAM_TYPE>(null);
    
    protected AtomicReference<DATAGRAM_TYPE> cachedFullInstance1 = new AtomicReference<DATAGRAM_TYPE>(null);
    
    DATAGRAM_TYPE tryAlloc(int param1Int) {
      if (param1Int == 0) {
        ModernRoboticsDatagram modernRoboticsDatagram2 = (ModernRoboticsDatagram)this.cacheHeaderOnly0.getAndSet(null);
        ModernRoboticsDatagram modernRoboticsDatagram1 = modernRoboticsDatagram2;
        if (modernRoboticsDatagram2 == null)
          modernRoboticsDatagram1 = (ModernRoboticsDatagram)this.cacheHeaderOnly1.getAndSet(null); 
        return (DATAGRAM_TYPE)modernRoboticsDatagram1;
      } 
      ModernRoboticsDatagram modernRoboticsDatagram = (ModernRoboticsDatagram)this.cachedFullInstance0.getAndSet(null);
      if (modernRoboticsDatagram != null) {
        if (modernRoboticsDatagram.getAllocatedPayload() == param1Int)
          return (DATAGRAM_TYPE)modernRoboticsDatagram; 
        tryCache0((DATAGRAM_TYPE)modernRoboticsDatagram);
      } 
      modernRoboticsDatagram = (ModernRoboticsDatagram)this.cachedFullInstance1.getAndSet(null);
      if (modernRoboticsDatagram != null) {
        if (modernRoboticsDatagram.getAllocatedPayload() == param1Int)
          return (DATAGRAM_TYPE)modernRoboticsDatagram; 
        tryCache1((DATAGRAM_TYPE)modernRoboticsDatagram);
      } 
      return null;
    }
    
    void tryCache0(DATAGRAM_TYPE param1DATAGRAM_TYPE) {
      if (param1DATAGRAM_TYPE.getAllocatedPayload() == 0) {
        if (!this.cacheHeaderOnly0.compareAndSet(null, param1DATAGRAM_TYPE)) {
          this.cacheHeaderOnly1.compareAndSet(null, param1DATAGRAM_TYPE);
          return;
        } 
      } else if (!this.cachedFullInstance0.compareAndSet(null, param1DATAGRAM_TYPE)) {
        this.cachedFullInstance1.compareAndSet(null, param1DATAGRAM_TYPE);
      } 
    }
    
    void tryCache1(DATAGRAM_TYPE param1DATAGRAM_TYPE) {
      if (param1DATAGRAM_TYPE.getAllocatedPayload() == 0) {
        if (!this.cacheHeaderOnly1.compareAndSet(null, param1DATAGRAM_TYPE)) {
          this.cacheHeaderOnly0.compareAndSet(null, param1DATAGRAM_TYPE);
          return;
        } 
      } else if (!this.cachedFullInstance1.compareAndSet(null, param1DATAGRAM_TYPE)) {
        this.cachedFullInstance0.compareAndSet(null, param1DATAGRAM_TYPE);
      } 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\modernrobotics\comm\ModernRoboticsDatagram.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import java.nio.ByteBuffer;

public class LynxGetBulkInputDataResponse extends LynxDekaInterfaceResponse {
  short[] analogInputs = new short[4];
  
  public final int cbPayload = 34;
  
  byte digitalInputs = 0;
  
  int[] encoders = new int[4];
  
  byte motorStatus = 0;
  
  short[] velocities = new short[4];
  
  public LynxGetBulkInputDataResponse(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN);
    this.digitalInputs = byteBuffer.get();
    byte b = 0;
    int i = 0;
    while (true) {
      int j;
      int[] arrayOfInt = this.encoders;
      if (i < arrayOfInt.length) {
        arrayOfInt[i] = byteBuffer.getInt();
        i++;
        continue;
      } 
      this.motorStatus = byteBuffer.get();
      i = 0;
      while (true) {
        short[] arrayOfShort = this.velocities;
        j = b;
        if (i < arrayOfShort.length) {
          arrayOfShort[i] = byteBuffer.getShort();
          i++;
          continue;
        } 
        break;
      } 
      while (true) {
        short[] arrayOfShort = this.analogInputs;
        if (j < arrayOfShort.length) {
          arrayOfShort[j] = byteBuffer.getShort();
          j++;
          continue;
        } 
        break;
      } 
      return;
    } 
  }
  
  public int getAnalogInput(int paramInt) {
    LynxConstants.validateAnalogInputZ(paramInt);
    return this.analogInputs[paramInt];
  }
  
  public boolean getDigitalInput(int paramInt) {
    LynxConstants.validateDigitalIOZ(paramInt);
    return ((1 << paramInt & this.digitalInputs) != 0);
  }
  
  public int getEncoder(int paramInt) {
    LynxConstants.validateMotorZ(paramInt);
    return this.encoders[paramInt];
  }
  
  public int getVelocity(int paramInt) {
    LynxConstants.validateMotorZ(paramInt);
    return this.velocities[paramInt];
  }
  
  public boolean isAtTarget(int paramInt) {
    LynxConstants.validateMotorZ(paramInt);
    return ((1 << paramInt + 4 & this.motorStatus) != 0);
  }
  
  public boolean isOverCurrent(int paramInt) {
    LynxConstants.validateMotorZ(paramInt);
    return ((1 << paramInt & this.motorStatus) != 0);
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(34).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.put(this.digitalInputs);
    byte b = 0;
    int i = 0;
    while (true) {
      int j;
      int[] arrayOfInt = this.encoders;
      if (i < arrayOfInt.length) {
        byteBuffer.putInt(arrayOfInt[i]);
        i++;
        continue;
      } 
      byteBuffer.put(this.motorStatus);
      i = 0;
      while (true) {
        short[] arrayOfShort = this.velocities;
        j = b;
        if (i < arrayOfShort.length) {
          byteBuffer.putShort(arrayOfShort[i]);
          i++;
          continue;
        } 
        break;
      } 
      while (true) {
        short[] arrayOfShort = this.analogInputs;
        if (j < arrayOfShort.length) {
          byteBuffer.putShort(arrayOfShort[j]);
          j++;
          continue;
        } 
        return byteBuffer.array();
      } 
      break;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxGetBulkInputDataResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
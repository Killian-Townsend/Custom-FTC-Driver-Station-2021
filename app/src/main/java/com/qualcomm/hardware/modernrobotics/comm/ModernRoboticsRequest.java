package com.qualcomm.hardware.modernrobotics.comm;

import org.firstinspires.ftc.robotcore.internal.system.Assert;

public class ModernRoboticsRequest extends ModernRoboticsDatagram {
  public static final byte[] syncBytes = new byte[] { 85, -86 };
  
  protected final ModernRoboticsDatagram.AllocationContext<ModernRoboticsRequest> allocationContext;
  
  private ModernRoboticsRequest(ModernRoboticsDatagram.AllocationContext<ModernRoboticsRequest> paramAllocationContext, int paramInt) {
    super(paramInt);
    this.allocationContext = paramAllocationContext;
  }
  
  public static ModernRoboticsRequest from(ModernRoboticsDatagram.AllocationContext<ModernRoboticsRequest> paramAllocationContext, byte[] paramArrayOfbyte) {
    ModernRoboticsRequest modernRoboticsRequest = newInstance(paramAllocationContext, paramArrayOfbyte.length - 5);
    System.arraycopy(paramArrayOfbyte, 0, modernRoboticsRequest.data, 0, paramArrayOfbyte.length);
    Assert.assertTrue(modernRoboticsRequest.syncBytesValid());
    return modernRoboticsRequest;
  }
  
  public static ModernRoboticsRequest newInstance(ModernRoboticsDatagram.AllocationContext<ModernRoboticsRequest> paramAllocationContext, int paramInt) {
    ModernRoboticsRequest modernRoboticsRequest2 = paramAllocationContext.tryAlloc(paramInt);
    ModernRoboticsRequest modernRoboticsRequest1 = modernRoboticsRequest2;
    if (modernRoboticsRequest2 == null)
      modernRoboticsRequest1 = new ModernRoboticsRequest(paramAllocationContext, paramInt); 
    byte[] arrayOfByte = syncBytes;
    modernRoboticsRequest1.initialize(arrayOfByte[0], arrayOfByte[1]);
    return modernRoboticsRequest1;
  }
  
  public void close() {
    this.allocationContext.tryCache0(this);
  }
  
  public boolean syncBytesValid() {
    byte[] arrayOfByte = this.data;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (arrayOfByte[0] == syncBytes[0]) {
      bool1 = bool2;
      if (this.data[1] == syncBytes[1])
        bool1 = true; 
    } 
    return bool1;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\modernrobotics\comm\ModernRoboticsRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
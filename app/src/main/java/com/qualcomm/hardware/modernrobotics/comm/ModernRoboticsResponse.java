package com.qualcomm.hardware.modernrobotics.comm;

public class ModernRoboticsResponse extends ModernRoboticsDatagram {
  public static final byte[] syncBytes = new byte[] { 51, -52 };
  
  protected final ModernRoboticsDatagram.AllocationContext<ModernRoboticsResponse> allocationContext;
  
  private ModernRoboticsResponse(ModernRoboticsDatagram.AllocationContext<ModernRoboticsResponse> paramAllocationContext, int paramInt) {
    super(paramInt);
    this.allocationContext = paramAllocationContext;
  }
  
  public static ModernRoboticsResponse newInstance(ModernRoboticsDatagram.AllocationContext<ModernRoboticsResponse> paramAllocationContext, int paramInt) {
    ModernRoboticsResponse modernRoboticsResponse2 = paramAllocationContext.tryAlloc(paramInt);
    ModernRoboticsResponse modernRoboticsResponse1 = modernRoboticsResponse2;
    if (modernRoboticsResponse2 == null)
      modernRoboticsResponse1 = new ModernRoboticsResponse(paramAllocationContext, paramInt); 
    byte[] arrayOfByte = syncBytes;
    modernRoboticsResponse1.initialize(arrayOfByte[0], arrayOfByte[1]);
    return modernRoboticsResponse1;
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


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\modernrobotics\comm\ModernRoboticsResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
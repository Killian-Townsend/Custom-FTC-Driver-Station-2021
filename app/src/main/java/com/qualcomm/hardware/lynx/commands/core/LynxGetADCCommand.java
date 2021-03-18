package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.hardware.lynx.commands.LynxInterfaceResponse;
import com.qualcomm.hardware.lynx.commands.LynxMessage;
import java.nio.ByteBuffer;

public class LynxGetADCCommand extends LynxDekaInterfaceCommand<LynxGetADCResponse> {
  private static final int cbPayload = 2;
  
  private byte channel;
  
  private byte mode;
  
  public LynxGetADCCommand(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
    this.response = (LynxMessage)new LynxGetADCResponse(paramLynxModuleIntf);
  }
  
  public LynxGetADCCommand(LynxModuleIntf paramLynxModuleIntf, Channel paramChannel, Mode paramMode) {
    this(paramLynxModuleIntf);
    this.channel = paramChannel.bVal;
    this.mode = paramMode.bVal;
  }
  
  public static Class<? extends LynxInterfaceResponse> getResponseClass() {
    return (Class)LynxGetADCResponse.class;
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN);
    this.channel = byteBuffer.get();
    this.mode = byteBuffer.get();
  }
  
  public boolean isResponseExpected() {
    return true;
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(2).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.put(this.channel);
    byteBuffer.put(this.mode);
    return byteBuffer.array();
  }
  
  public enum Channel {
    BATTERY_CURRENT,
    BATTERY_MONITOR,
    CONTROLLER_TEMPERATURE,
    FIVE_VOLT_MONITOR,
    GPIO_CURRENT,
    I2C_BUS_CURRENT,
    MOTOR0_CURRENT,
    MOTOR1_CURRENT,
    MOTOR2_CURRENT,
    MOTOR3_CURRENT,
    SERVO_CURRENT,
    USER0(0),
    USER1(1),
    USER2(2),
    USER3(3);
    
    public final byte bVal;
    
    static {
      BATTERY_CURRENT = new Channel("BATTERY_CURRENT", 7, 7);
      MOTOR0_CURRENT = new Channel("MOTOR0_CURRENT", 8, 8);
      MOTOR1_CURRENT = new Channel("MOTOR1_CURRENT", 9, 9);
      MOTOR2_CURRENT = new Channel("MOTOR2_CURRENT", 10, 10);
      MOTOR3_CURRENT = new Channel("MOTOR3_CURRENT", 11, 11);
      FIVE_VOLT_MONITOR = new Channel("FIVE_VOLT_MONITOR", 12, 12);
      BATTERY_MONITOR = new Channel("BATTERY_MONITOR", 13, 13);
      Channel channel = new Channel("CONTROLLER_TEMPERATURE", 14, 14);
      CONTROLLER_TEMPERATURE = channel;
      $VALUES = new Channel[] { 
          USER0, USER1, USER2, USER3, GPIO_CURRENT, I2C_BUS_CURRENT, SERVO_CURRENT, BATTERY_CURRENT, MOTOR0_CURRENT, MOTOR1_CURRENT, 
          MOTOR2_CURRENT, MOTOR3_CURRENT, FIVE_VOLT_MONITOR, BATTERY_MONITOR, channel };
    }
    
    Channel(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
    
    public static Channel motorCurrent(int param1Int) {
      if (param1Int != 0) {
        if (param1Int != 1) {
          if (param1Int != 2) {
            if (param1Int == 3)
              return MOTOR3_CURRENT; 
            throw new IllegalArgumentException(String.format("illegal motor port %d", new Object[] { Integer.valueOf(param1Int) }));
          } 
          return MOTOR2_CURRENT;
        } 
        return MOTOR1_CURRENT;
      } 
      return MOTOR0_CURRENT;
    }
    
    public static Channel user(int param1Int) {
      if (param1Int != 0) {
        if (param1Int != 1) {
          if (param1Int != 2) {
            if (param1Int == 3)
              return USER3; 
            throw new IllegalArgumentException(String.format("illegal user port %d", new Object[] { Integer.valueOf(param1Int) }));
          } 
          return USER2;
        } 
        return USER1;
      } 
      return USER0;
    }
  }
  
  public enum Mode {
    ENGINEERING(0),
    RAW(0);
    
    public final byte bVal;
    
    static {
      Mode mode = new Mode("RAW", 1, 1);
      RAW = mode;
      $VALUES = new Mode[] { ENGINEERING, mode };
    }
    
    Mode(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxGetADCCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
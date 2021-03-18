package com.qualcomm.hardware.lynx.commands.standard;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxMessage;
import com.qualcomm.robotcore.util.TypeConversion;
import org.firstinspires.ftc.robotcore.internal.system.Assert;

public class LynxNack extends LynxMessage {
  private int nackReasonCode;
  
  public LynxNack(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public LynxNack(LynxModuleIntf paramLynxModuleIntf, int paramInt) {
    this(paramLynxModuleIntf);
    this.nackReasonCode = paramInt;
  }
  
  public LynxNack(LynxModuleIntf paramLynxModuleIntf, ReasonCode paramReasonCode) {
    this(paramLynxModuleIntf, paramReasonCode.getValue());
  }
  
  public static int getStandardCommandNumber() {
    return 32514;
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    this.nackReasonCode = TypeConversion.unsignedByteToInt(paramArrayOfbyte[0]);
  }
  
  public int getCommandNumber() {
    return getStandardCommandNumber();
  }
  
  public ReasonCode getNackReasonCode() {
    if (this.nackReasonCode == StandardReasonCode.I2C_OPERATION_IN_PROGRESS.getValue())
      return StandardReasonCode.I2C_OPERATION_IN_PROGRESS; 
    if (this.nackReasonCode == StandardReasonCode.I2C_MASTER_BUSY.getValue())
      return StandardReasonCode.I2C_MASTER_BUSY; 
    for (StandardReasonCode standardReasonCode : StandardReasonCode.values()) {
      if (this.nackReasonCode == standardReasonCode.getValue())
        return standardReasonCode; 
    } 
    return new UnrecognizedReasonCode(this.nackReasonCode);
  }
  
  public StandardReasonCode getNackReasonCodeAsEnum() {
    ReasonCode reasonCode = getNackReasonCode();
    return (reasonCode instanceof StandardReasonCode) ? (StandardReasonCode)reasonCode : StandardReasonCode.UNRECOGNIZED_REASON_CODE;
  }
  
  public boolean isNack() {
    return true;
  }
  
  public byte[] toPayloadByteArray() {
    boolean bool;
    int i = this.nackReasonCode;
    if ((byte)i == i) {
      bool = true;
    } else {
      bool = false;
    } 
    Assert.assertTrue(bool);
    return new byte[] { (byte)this.nackReasonCode };
  }
  
  public static interface ReasonCode {
    int getValue();
    
    boolean isUnsupportedReason();
    
    String toString();
  }
  
  public enum StandardReasonCode implements ReasonCode {
    ABANDONED_WAITING_FOR_ACK,
    ABANDONED_WAITING_FOR_RESPONSE,
    BATTERY_TOO_LOW_TO_RUN_MOTOR,
    BATTERY_TOO_LOW_TO_RUN_SERVO,
    COMMAND_IMPL_PENDING,
    COMMAND_INVALID_FOR_MOTOR_MODE,
    COMMAND_ROUTING_ERROR,
    GPIO_IN0,
    GPIO_IN1,
    GPIO_IN2,
    GPIO_IN3,
    GPIO_IN4,
    GPIO_IN5,
    GPIO_IN6,
    GPIO_IN7,
    GPIO_NO_INPUT,
    GPIO_NO_OUTPUT,
    GPIO_OUT0,
    GPIO_OUT1,
    GPIO_OUT2,
    GPIO_OUT3,
    GPIO_OUT4,
    GPIO_OUT5,
    GPIO_OUT6,
    GPIO_OUT7,
    I2C_MASTER_BUSY,
    I2C_NO_RESULTS_PENDING,
    I2C_OPERATION_IN_PROGRESS,
    I2C_QUERY_MISMATCH,
    I2C_TIMEOUT_SCK_STUCK,
    I2C_TIMEOUT_SDA_STUCK,
    I2C_TIMEOUT_UNKNOWN_CAUSE,
    MOTOR_NOT_CONFIG_BEFORE_ENABLED,
    PACKET_TYPE_ID_UNKNOWN,
    PARAM0(0),
    PARAM1(1),
    PARAM2(2),
    PARAM3(3),
    PARAM4(4),
    PARAM5(5),
    PARAM6(6),
    PARAM7(7),
    PARAM8(8),
    PARAM9(9),
    SERVO_NOT_CONFIG_BEFORE_ENABLED(9),
    UNRECOGNIZED_REASON_CODE(9);
    
    private int iVal;
    
    static {
      GPIO_NO_OUTPUT = new StandardReasonCode("GPIO_NO_OUTPUT", 18, 18);
      GPIO_IN0 = new StandardReasonCode("GPIO_IN0", 19, 20);
      GPIO_IN1 = new StandardReasonCode("GPIO_IN1", 20, 21);
      GPIO_IN2 = new StandardReasonCode("GPIO_IN2", 21, 22);
      GPIO_IN3 = new StandardReasonCode("GPIO_IN3", 22, 23);
      GPIO_IN4 = new StandardReasonCode("GPIO_IN4", 23, 24);
      GPIO_IN5 = new StandardReasonCode("GPIO_IN5", 24, 25);
      GPIO_IN6 = new StandardReasonCode("GPIO_IN6", 25, 26);
      GPIO_IN7 = new StandardReasonCode("GPIO_IN7", 26, 27);
      GPIO_NO_INPUT = new StandardReasonCode("GPIO_NO_INPUT", 27, 28);
      SERVO_NOT_CONFIG_BEFORE_ENABLED = new StandardReasonCode("SERVO_NOT_CONFIG_BEFORE_ENABLED", 28, 30);
      BATTERY_TOO_LOW_TO_RUN_SERVO = new StandardReasonCode("BATTERY_TOO_LOW_TO_RUN_SERVO", 29, 31);
      I2C_MASTER_BUSY = new StandardReasonCode("I2C_MASTER_BUSY", 30, 40);
      I2C_OPERATION_IN_PROGRESS = new StandardReasonCode("I2C_OPERATION_IN_PROGRESS", 31, 41);
      I2C_NO_RESULTS_PENDING = new StandardReasonCode("I2C_NO_RESULTS_PENDING", 32, 42);
      I2C_QUERY_MISMATCH = new StandardReasonCode("I2C_QUERY_MISMATCH", 33, 43);
      I2C_TIMEOUT_SDA_STUCK = new StandardReasonCode("I2C_TIMEOUT_SDA_STUCK", 34, 44);
      I2C_TIMEOUT_SCK_STUCK = new StandardReasonCode("I2C_TIMEOUT_SCK_STUCK", 35, 45);
      I2C_TIMEOUT_UNKNOWN_CAUSE = new StandardReasonCode("I2C_TIMEOUT_UNKNOWN_CAUSE", 36, 46);
      MOTOR_NOT_CONFIG_BEFORE_ENABLED = new StandardReasonCode("MOTOR_NOT_CONFIG_BEFORE_ENABLED", 37, 50);
      COMMAND_INVALID_FOR_MOTOR_MODE = new StandardReasonCode("COMMAND_INVALID_FOR_MOTOR_MODE", 38, 51);
      BATTERY_TOO_LOW_TO_RUN_MOTOR = new StandardReasonCode("BATTERY_TOO_LOW_TO_RUN_MOTOR", 39, 52);
      COMMAND_IMPL_PENDING = new StandardReasonCode("COMMAND_IMPL_PENDING", 40, 253);
      COMMAND_ROUTING_ERROR = new StandardReasonCode("COMMAND_ROUTING_ERROR", 41, 254);
      PACKET_TYPE_ID_UNKNOWN = new StandardReasonCode("PACKET_TYPE_ID_UNKNOWN", 42, 255);
      ABANDONED_WAITING_FOR_RESPONSE = new StandardReasonCode("ABANDONED_WAITING_FOR_RESPONSE", 43, 256);
      ABANDONED_WAITING_FOR_ACK = new StandardReasonCode("ABANDONED_WAITING_FOR_ACK", 44, 257);
      StandardReasonCode standardReasonCode = new StandardReasonCode("UNRECOGNIZED_REASON_CODE", 45, 258);
      UNRECOGNIZED_REASON_CODE = standardReasonCode;
      $VALUES = new StandardReasonCode[] { 
          PARAM0, PARAM1, PARAM2, PARAM3, PARAM4, PARAM5, PARAM6, PARAM7, PARAM8, PARAM9, 
          GPIO_OUT0, GPIO_OUT1, GPIO_OUT2, GPIO_OUT3, GPIO_OUT4, GPIO_OUT5, GPIO_OUT6, GPIO_OUT7, GPIO_NO_OUTPUT, GPIO_IN0, 
          GPIO_IN1, GPIO_IN2, GPIO_IN3, GPIO_IN4, GPIO_IN5, GPIO_IN6, GPIO_IN7, GPIO_NO_INPUT, SERVO_NOT_CONFIG_BEFORE_ENABLED, BATTERY_TOO_LOW_TO_RUN_SERVO, 
          I2C_MASTER_BUSY, I2C_OPERATION_IN_PROGRESS, I2C_NO_RESULTS_PENDING, I2C_QUERY_MISMATCH, I2C_TIMEOUT_SDA_STUCK, I2C_TIMEOUT_SCK_STUCK, I2C_TIMEOUT_UNKNOWN_CAUSE, MOTOR_NOT_CONFIG_BEFORE_ENABLED, COMMAND_INVALID_FOR_MOTOR_MODE, BATTERY_TOO_LOW_TO_RUN_MOTOR, 
          COMMAND_IMPL_PENDING, COMMAND_ROUTING_ERROR, PACKET_TYPE_ID_UNKNOWN, ABANDONED_WAITING_FOR_RESPONSE, ABANDONED_WAITING_FOR_ACK, standardReasonCode };
    }
    
    StandardReasonCode(int param1Int1) {
      this.iVal = param1Int1;
    }
    
    public int getValue() {
      return this.iVal;
    }
    
    public boolean isUnsupportedReason() {
      int i = LynxNack.null.$SwitchMap$com$qualcomm$hardware$lynx$commands$standard$LynxNack$StandardReasonCode[ordinal()];
      return !(i != 1 && i != 2 && i != 3);
    }
  }
  
  private static class UnrecognizedReasonCode implements ReasonCode {
    private final int value;
    
    UnrecognizedReasonCode(int param1Int) {
      this.value = param1Int;
    }
    
    public int getValue() {
      return this.value;
    }
    
    public boolean isUnsupportedReason() {
      return true;
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unrecognized NACK code ");
      stringBuilder.append(this.value);
      return stringBuilder.toString();
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\standard\LynxNack.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
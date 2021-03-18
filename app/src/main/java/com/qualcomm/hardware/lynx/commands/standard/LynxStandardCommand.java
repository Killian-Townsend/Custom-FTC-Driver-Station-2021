package com.qualcomm.hardware.lynx.commands.standard;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxCommand;
import com.qualcomm.hardware.lynx.commands.LynxMessage;

public abstract class LynxStandardCommand<RESPONSE extends LynxMessage> extends LynxCommand<RESPONSE> {
  public static final int COMMAND_NUMBER_ACK = 32513;
  
  public static final int COMMAND_NUMBER_DEBUG_LOG_LEVEL = 32526;
  
  public static final int COMMAND_NUMBER_DISCOVERY = 32527;
  
  public static final int COMMAND_NUMBER_DOWNLOAD_CHUNK = 32521;
  
  public static final int COMMAND_NUMBER_FAIL_SAFE = 32517;
  
  public static final int COMMAND_NUMBER_FIRST = 32513;
  
  public static final int COMMAND_NUMBER_GET_MODULE_LED_COLOR = 32523;
  
  public static final int COMMAND_NUMBER_GET_MODULE_LED_PATTERN = 32525;
  
  public static final int COMMAND_NUMBER_GET_MODULE_STATUS = 32515;
  
  public static final int COMMAND_NUMBER_KEEP_ALIVE = 32516;
  
  public static final int COMMAND_NUMBER_LAST = 32527;
  
  public static final int COMMAND_NUMBER_NACK = 32514;
  
  public static final int COMMAND_NUMBER_QUERY_INTERFACE = 32519;
  
  public static final int COMMAND_NUMBER_SET_MODULE_LED_COLOR = 32522;
  
  public static final int COMMAND_NUMBER_SET_MODULE_LED_PATTERN = 32524;
  
  public static final int COMMAND_NUMBER_SET_NEW_MODULE_ADDRESS = 32518;
  
  public static final int COMMAND_NUMBER_START_DOWNLOAD = 32520;
  
  public LynxStandardCommand(LynxModule paramLynxModule) {
    super((LynxModuleIntf)paramLynxModule);
  }
  
  public static boolean isStandardCommandNumber(int paramInt) {
    return (32513 <= paramInt && paramInt <= 32527);
  }
  
  public static boolean isStandardPacketId(int paramInt) {
    return (isStandardCommandNumber(paramInt) || isStandardResponseNumber(paramInt));
  }
  
  public static boolean isStandardResponseNumber(int paramInt) {
    return ((0x8000 & paramInt) != 0 && isStandardCommandNumber(paramInt & 0xFFFF7FFF));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\standard\LynxStandardCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxInterface;
import com.qualcomm.hardware.lynx.commands.LynxInterfaceCommand;
import com.qualcomm.hardware.lynx.commands.LynxMessage;

public abstract class LynxDekaInterfaceCommand<RESPONSE extends LynxMessage> extends LynxInterfaceCommand<RESPONSE> {
  public static LynxInterface theInterface = new LynxInterface("DEKA", new Class[] { 
        LynxGetBulkInputDataCommand.class, LynxSetSingleDIOOutputCommand.class, LynxSetAllDIOOutputsCommand.class, LynxSetDIODirectionCommand.class, LynxGetDIODirectionCommand.class, LynxGetSingleDIOInputCommand.class, LynxGetAllDIOInputsCommand.class, LynxGetADCCommand.class, LynxSetMotorChannelModeCommand.class, LynxGetMotorChannelModeCommand.class, 
        LynxSetMotorChannelEnableCommand.class, LynxGetMotorChannelEnableCommand.class, LynxSetMotorChannelCurrentAlertLevelCommand.class, LynxGetMotorChannelCurrentAlertLevelCommand.class, LynxResetMotorEncoderCommand.class, LynxSetMotorConstantPowerCommand.class, LynxGetMotorConstantPowerCommand.class, LynxSetMotorTargetVelocityCommand.class, LynxGetMotorTargetVelocityCommand.class, LynxSetMotorTargetPositionCommand.class, 
        LynxGetMotorTargetPositionCommand.class, LynxIsMotorAtTargetCommand.class, LynxGetMotorEncoderPositionCommand.class, LynxSetMotorPIDControlLoopCoefficientsCommand.class, LynxGetMotorPIDControlLoopCoefficientsCommand.class, LynxSetPWMConfigurationCommand.class, LynxGetPWMConfigurationCommand.class, LynxSetPWMPulseWidthCommand.class, LynxGetPWMPulseWidthCommand.class, LynxSetPWMEnableCommand.class, 
        LynxGetPWMEnableCommand.class, LynxSetServoConfigurationCommand.class, LynxGetServoConfigurationCommand.class, LynxSetServoPulseWidthCommand.class, LynxGetServoPulseWidthCommand.class, LynxSetServoEnableCommand.class, LynxGetServoEnableCommand.class, LynxI2cWriteSingleByteCommand.class, LynxI2cWriteMultipleBytesCommand.class, LynxI2cReadSingleByteCommand.class, 
        LynxI2cReadMultipleBytesCommand.class, LynxI2cReadStatusQueryCommand.class, LynxI2cWriteStatusQueryCommand.class, LynxI2cConfigureChannelCommand.class, LynxPhoneChargeControlCommand.class, LynxPhoneChargeQueryCommand.class, LynxInjectDataLogHintCommand.class, LynxI2cConfigureQueryCommand.class, LynxReadVersionStringCommand.class, LynxFtdiResetControlCommand.class, 
        LynxFtdiResetQueryCommand.class, LynxSetMotorPIDFControlLoopCoefficientsCommand.class, LynxI2cWriteReadMultipleBytesCommand.class, LynxGetMotorPIDFControlLoopCoefficientsCommand.class });
  
  public static final String theInterfaceName = "DEKA";
  
  public LynxDekaInterfaceCommand(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public LynxInterface getInterface() {
    return theInterface;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxDekaInterfaceCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
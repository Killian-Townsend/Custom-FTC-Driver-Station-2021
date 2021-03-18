package com.qualcomm.hardware.lynx;

import android.content.Context;
import com.qualcomm.hardware.R;
import com.qualcomm.hardware.lynx.commands.core.LynxGetADCCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetADCResponse;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.VoltageSensor;

public class LynxVoltageSensor extends LynxController implements VoltageSensor {
  public static final String TAG = "LynxVoltageSensor";
  
  public LynxVoltageSensor(Context paramContext, LynxModule paramLynxModule) throws RobotCoreException, InterruptedException {
    super(paramContext, paramLynxModule);
    finishConstruction();
  }
  
  public String getDeviceName() {
    return this.context.getString(R.string.lynxVoltageSensorDisplayName);
  }
  
  protected String getTag() {
    return "LynxVoltageSensor";
  }
  
  public double getVoltage() {
    LynxGetADCCommand lynxGetADCCommand = new LynxGetADCCommand(getModule(), LynxGetADCCommand.Channel.BATTERY_MONITOR, LynxGetADCCommand.Mode.ENGINEERING);
    try {
      int i = ((LynxGetADCResponse)lynxGetADCCommand.sendReceive()).getValue();
      return i * 0.001D;
    } catch (InterruptedException interruptedException) {
    
    } catch (RuntimeException runtimeException) {
    
    } catch (LynxNackException lynxNackException) {}
    handleException(lynxNackException);
    return ((Double)LynxUsbUtil.<Double>makePlaceholderValue(Double.valueOf(0.0D))).doubleValue();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\LynxVoltageSensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
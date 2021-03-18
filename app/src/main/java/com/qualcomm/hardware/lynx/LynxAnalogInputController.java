package com.qualcomm.hardware.lynx;

import android.content.Context;
import com.qualcomm.hardware.R;
import com.qualcomm.hardware.lynx.commands.core.LynxDekaInterfaceCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetADCCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetADCResponse;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.AnalogInputController;
import com.qualcomm.robotcore.util.SerialNumber;

public class LynxAnalogInputController extends LynxController implements AnalogInputController {
  public static final String TAG = "LynxAnalogInputController";
  
  public static final int apiPortFirst = 0;
  
  public static final int apiPortLast = 3;
  
  public LynxAnalogInputController(Context paramContext, LynxModule paramLynxModule) throws RobotCoreException, InterruptedException {
    super(paramContext, paramLynxModule);
    finishConstruction();
  }
  
  private void validatePort(int paramInt) {
    if (paramInt >= 0 && paramInt <= 3)
      return; 
    throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are %d..%d", new Object[] { Integer.valueOf(paramInt), Integer.valueOf(0), Integer.valueOf(3) }));
  }
  
  public double getAnalogInputVoltage(int paramInt) {
    validatePort(paramInt);
    paramInt += 0;
    LynxGetADCCommand lynxGetADCCommand = new LynxGetADCCommand(getModule(), LynxGetADCCommand.Channel.user(paramInt), LynxGetADCCommand.Mode.ENGINEERING);
    if (getModule() instanceof LynxModule) {
      LynxModule lynxModule = (LynxModule)getModule();
      if (lynxModule.getBulkCachingMode() != LynxModule.BulkCachingMode.OFF)
        return lynxModule.recordBulkCachingCommandIntent((LynxDekaInterfaceCommand<?>)lynxGetADCCommand).getAnalogInputVoltage(paramInt); 
    } 
    try {
      paramInt = ((LynxGetADCResponse)lynxGetADCCommand.sendReceive()).getValue();
      return paramInt * 0.001D;
    } catch (InterruptedException interruptedException) {
    
    } catch (RuntimeException runtimeException) {
    
    } catch (LynxNackException lynxNackException) {}
    handleException(lynxNackException);
    return ((Double)LynxUsbUtil.<Double>makePlaceholderValue(Double.valueOf(0.0D))).doubleValue();
  }
  
  public String getDeviceName() {
    return this.context.getString(R.string.lynxAnalogInputControllerDisplayName);
  }
  
  public double getMaxAnalogInputVoltage() {
    return 3.3D;
  }
  
  public SerialNumber getSerialNumber() {
    return getModule().getSerialNumber();
  }
  
  protected String getTag() {
    return "LynxAnalogInputController";
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\LynxAnalogInputController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
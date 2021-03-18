package org.firstinspires.inspection;

import android.os.Bundle;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robocol.Command;
import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.network.RecvLoopRunnable;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class RcInspectionActivity extends InspectionActivity {
  final RecvLoopRunnable.RecvLoopCallback recvLoopCallback = (RecvLoopRunnable.RecvLoopCallback)new RecvLoopRunnable.DegenerateCallback() {
      public CallbackResult commandEvent(Command param1Command) throws RobotCoreException {
        if (RcInspectionActivity.this.remoteConfigure) {
          String str = param1Command.getName();
          byte b = -1;
          if (str.hashCode() == -355866299 && str.equals("CMD_REQUEST_INSPECTION_REPORT_RESP"))
            b = 0; 
          if (b == 0) {
            final InspectionState rcState = InspectionState.deserialize(param1Command.getExtra());
            AppUtil.getInstance().runOnUiThread(new Runnable() {
                  public void run() {
                    RcInspectionActivity.this.refresh(rcState);
                  }
                });
            return CallbackResult.HANDLED;
          } 
        } 
        return CallbackResult.NOT_HANDLED;
      }
    };
  
  final boolean remoteConfigure = AppUtil.getInstance().isDriverStation();
  
  protected boolean inspectingRobotController() {
    return true;
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    NetworkConnectionHandler.getInstance().pushReceiveLoopCallback(this.recvLoopCallback);
  }
  
  protected void onDestroy() {
    super.onDestroy();
    NetworkConnectionHandler.getInstance().removeReceiveLoopCallback(this.recvLoopCallback);
  }
  
  protected void refresh() {
    if (this.remoteConfigure) {
      NetworkConnectionHandler.getInstance().sendCommand(new Command("CMD_REQUEST_INSPECTION_REPORT"));
      return;
    } 
    super.refresh();
  }
  
  protected boolean useMenu() {
    return this.remoteConfigure ^ true;
  }
  
  protected boolean validateAppsInstalled(InspectionState paramInspectionState) {
    return paramInspectionState.isDriverStationInstalled() ? false : paramInspectionState.isRobotControllerInstalled();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\inspection\RcInspectionActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
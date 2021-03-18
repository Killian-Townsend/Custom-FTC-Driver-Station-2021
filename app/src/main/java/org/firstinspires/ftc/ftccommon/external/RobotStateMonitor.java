package org.firstinspires.ftc.ftccommon.external;

import com.qualcomm.robotcore.robot.RobotState;
import com.qualcomm.robotcore.robot.RobotStatus;
import org.firstinspires.ftc.robotcore.internal.network.NetworkStatus;
import org.firstinspires.ftc.robotcore.internal.network.PeerStatus;

public interface RobotStateMonitor {
  void updateErrorMessage(String paramString);
  
  void updateNetworkStatus(NetworkStatus paramNetworkStatus, String paramString);
  
  void updatePeerStatus(PeerStatus paramPeerStatus);
  
  void updateRobotState(RobotState paramRobotState);
  
  void updateRobotStatus(RobotStatus paramRobotStatus);
  
  void updateWarningMessage(String paramString);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\ftccommon\external\RobotStateMonitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.inspection;

public class DsInspectionActivity extends InspectionActivity {
  protected boolean inspectingRobotController() {
    return false;
  }
  
  protected boolean useMenu() {
    return true;
  }
  
  protected boolean validateAppsInstalled(InspectionState paramInspectionState) {
    return paramInspectionState.isRobotControllerInstalled() ? false : paramInspectionState.isDriverStationInstalled();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\inspection\DsInspectionActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
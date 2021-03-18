package org.firstinspires.ftc.robotserver.internal.webserver.controlhubupdater.result;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.webserver.R;

public enum OtaResultType implements ResultType {
  DEVICE_NOT_SUPPORTED,
  DOWNGRADE_NOT_AUTHORIZED,
  ERROR_READING_FILE,
  FAILED_TO_INSTALL,
  INVALID_FILE_LOCATION,
  INVALID_UPDATE_FILE,
  OTA_UPDATE_FINISHED,
  VERIFICATION_SUCCEEDED(1, Result.PresentationType.STATUS, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.ota_result_type_verification_succeeded));
  
  private final int code;
  
  private final Result.DetailMessageType detailMessageType;
  
  private final String message;
  
  private final Result.PresentationType presentationType;
  
  static {
    INVALID_FILE_LOCATION = new OtaResultType("INVALID_FILE_LOCATION", 1, 2, Result.PresentationType.ERROR, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.ota_result_type_invalid_file_location));
    INVALID_UPDATE_FILE = new OtaResultType("INVALID_UPDATE_FILE", 2, 3, Result.PresentationType.ERROR, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.ota_result_type_invalid_update_file));
    ERROR_READING_FILE = new OtaResultType("ERROR_READING_FILE", 3, 4, Result.PresentationType.ERROR, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.ota_result_type_error_reading_file));
    FAILED_TO_INSTALL = new OtaResultType("FAILED_TO_INSTALL", 4, 5, Result.PresentationType.ERROR, Result.DetailMessageType.DISPLAYED, AppUtil.getDefContext().getString(R.string.ota_result_type_failed_to_install));
    OTA_UPDATE_FINISHED = new OtaResultType("OTA_UPDATE_FINISHED", 5, 6, Result.PresentationType.SUCCESS, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.ota_result_type_ota_update_finished));
    DOWNGRADE_NOT_AUTHORIZED = new OtaResultType("DOWNGRADE_NOT_AUTHORIZED", 6, 7, Result.PresentationType.PROMPT, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.ota_result_type_downgrade_not_authorized));
    OtaResultType otaResultType = new OtaResultType("DEVICE_NOT_SUPPORTED", 7, 8, Result.PresentationType.ERROR, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.ota_result_type_device_not_supported));
    DEVICE_NOT_SUPPORTED = otaResultType;
    $VALUES = new OtaResultType[] { VERIFICATION_SUCCEEDED, INVALID_FILE_LOCATION, INVALID_UPDATE_FILE, ERROR_READING_FILE, FAILED_TO_INSTALL, OTA_UPDATE_FINISHED, DOWNGRADE_NOT_AUTHORIZED, otaResultType };
  }
  
  OtaResultType(int paramInt1, Result.PresentationType paramPresentationType, Result.DetailMessageType paramDetailMessageType, String paramString1) {
    this.code = paramInt1;
    this.presentationType = paramPresentationType;
    this.detailMessageType = paramDetailMessageType;
    this.message = paramString1;
  }
  
  public static OtaResultType fromCode(int paramInt) {
    for (OtaResultType otaResultType : values()) {
      if (paramInt == otaResultType.getCode())
        return otaResultType; 
    } 
    return null;
  }
  
  public Result.Category getCategory() {
    return Result.Category.OTA_UPDATE;
  }
  
  public int getCode() {
    return this.code;
  }
  
  public Result.DetailMessageType getDetailMessageType() {
    return this.detailMessageType;
  }
  
  public String getMessage() {
    return this.message;
  }
  
  public Result.PresentationType getPresentationType() {
    return this.presentationType;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\webserver\controlhubupdater\result\OtaResultType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
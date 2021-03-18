package org.firstinspires.ftc.robotserver.internal.webserver.controlhubupdater.result;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.webserver.R;

public enum CommonResultType implements ResultType {
  BUSY_WITH_PREVIOUS_REQUEST,
  FILE_DOES_NOT_EXIST,
  NO_UPDATE_FILE(1, Result.PresentationType.ERROR, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.common_result_type_no_update_file)),
  PERFORMING_STARTUP_OPERATIONS(1, Result.PresentationType.ERROR, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.common_result_type_no_update_file));
  
  private final int code;
  
  private final Result.DetailMessageType detailMessageType;
  
  private final String message;
  
  private final Result.PresentationType presentationType;
  
  static {
    FILE_DOES_NOT_EXIST = new CommonResultType("FILE_DOES_NOT_EXIST", 1, 2, Result.PresentationType.ERROR, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.common_result_type_file_does_not_exist));
    PERFORMING_STARTUP_OPERATIONS = new CommonResultType("PERFORMING_STARTUP_OPERATIONS", 2, 3, Result.PresentationType.ERROR, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.common_result_type_performing_startup_operations));
    CommonResultType commonResultType = new CommonResultType("BUSY_WITH_PREVIOUS_REQUEST", 3, 4, Result.PresentationType.ERROR, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.common_result_type_busy_with_previous_request));
    BUSY_WITH_PREVIOUS_REQUEST = commonResultType;
    $VALUES = new CommonResultType[] { NO_UPDATE_FILE, FILE_DOES_NOT_EXIST, PERFORMING_STARTUP_OPERATIONS, commonResultType };
  }
  
  CommonResultType(int paramInt1, Result.PresentationType paramPresentationType, Result.DetailMessageType paramDetailMessageType, String paramString1) {
    this.code = paramInt1;
    this.presentationType = paramPresentationType;
    this.detailMessageType = paramDetailMessageType;
    this.message = paramString1;
  }
  
  public static CommonResultType fromCode(int paramInt) {
    for (CommonResultType commonResultType : values()) {
      if (paramInt == commonResultType.getCode())
        return commonResultType; 
    } 
    return null;
  }
  
  public Result.Category getCategory() {
    return Result.Category.COMMON;
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


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\webserver\controlhubupdater\result\CommonResultType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
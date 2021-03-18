package org.firstinspires.ftc.robotserver.internal.webserver.controlhubupdater.result;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.webserver.R;

public enum AppResultType implements ResultType {
  ATTEMPTING_INSTALLATION(1, Result.PresentationType.STATUS, Result.DetailMessageType.SUBSTITUTED, AppUtil.getDefContext().getString(R.string.app_result_type_attempting_installation)),
  AUTO_UPDATED_APP(1, Result.PresentationType.STATUS, Result.DetailMessageType.SUBSTITUTED, AppUtil.getDefContext().getString(R.string.app_result_type_attempting_installation)),
  BACKUP_UNINSTALLING_EXISTING_APP(1, Result.PresentationType.STATUS, Result.DetailMessageType.SUBSTITUTED, AppUtil.getDefContext().getString(R.string.app_result_type_attempting_installation)),
  FAILED_AND_REVERTED(1, Result.PresentationType.STATUS, Result.DetailMessageType.SUBSTITUTED, AppUtil.getDefContext().getString(R.string.app_result_type_attempting_installation)),
  FAILED_TO_RESTORE(1, Result.PresentationType.STATUS, Result.DetailMessageType.SUBSTITUTED, AppUtil.getDefContext().getString(R.string.app_result_type_attempting_installation)),
  GENERIC_INSTALL_FAILURE(1, Result.PresentationType.STATUS, Result.DetailMessageType.SUBSTITUTED, AppUtil.getDefContext().getString(R.string.app_result_type_attempting_installation)),
  ILLEGAL_PACKAGE(1, Result.PresentationType.STATUS, Result.DetailMessageType.SUBSTITUTED, AppUtil.getDefContext().getString(R.string.app_result_type_attempting_installation)),
  INCOMPATIBLE_WITH_DEVICE(1, Result.PresentationType.STATUS, Result.DetailMessageType.SUBSTITUTED, AppUtil.getDefContext().getString(R.string.app_result_type_attempting_installation)),
  INSTALLATION_ABORTED(1, Result.PresentationType.STATUS, Result.DetailMessageType.SUBSTITUTED, AppUtil.getDefContext().getString(R.string.app_result_type_attempting_installation)),
  INSTALLATION_BLOCKED(1, Result.PresentationType.STATUS, Result.DetailMessageType.SUBSTITUTED, AppUtil.getDefContext().getString(R.string.app_result_type_attempting_installation)),
  INSTALLATION_SUCCEEDED(2, Result.PresentationType.SUCCESS, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.app_result_type_installation_succeeded)),
  INSTALLED_MISSING_APP(2, Result.PresentationType.SUCCESS, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.app_result_type_installation_succeeded)),
  INVALID_APK_FILE(3, Result.PresentationType.ERROR, Result.DetailMessageType.DISPLAYED, AppUtil.getDefContext().getString(R.string.app_result_type_invalid_apk_file)),
  IO_EXCEPTION_DURING_LOADING(4, Result.PresentationType.ERROR, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.app_result_type_io_exception)),
  REBOOTING_WITH_NEW_AP_SERVICE(4, Result.PresentationType.ERROR, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.app_result_type_io_exception)),
  REPLACED_WRONG_APP_VARIANT(4, Result.PresentationType.ERROR, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.app_result_type_io_exception)),
  STORAGE_FAILURE(4, Result.PresentationType.ERROR, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.app_result_type_io_exception)),
  TIMEOUT_EXPIRED(5, Result.PresentationType.ERROR, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.app_result_type_timeout_expired)),
  UNINSTALLED_WRONG_APP_VARIANT(5, Result.PresentationType.ERROR, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.app_result_type_timeout_expired)),
  UNINSTALLING_EXISTING_APP(5, Result.PresentationType.ERROR, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.app_result_type_timeout_expired)),
  UNINSTALL_FAILED(5, Result.PresentationType.ERROR, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.app_result_type_timeout_expired)),
  UNINSTALL_REQUIRED(5, Result.PresentationType.ERROR, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.app_result_type_timeout_expired)),
  UNINSTALL_REQUIRED_BACKUP_POSSIBLE(5, Result.PresentationType.ERROR, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.app_result_type_timeout_expired)),
  UNKNOWN_INSTALL_STATUS(5, Result.PresentationType.ERROR, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.app_result_type_timeout_expired));
  
  private final int code;
  
  private final Result.DetailMessageType detailMessageType;
  
  private final String message;
  
  private final Result.PresentationType presentationType;
  
  static {
    GENERIC_INSTALL_FAILURE = new AppResultType("GENERIC_INSTALL_FAILURE", 5, 6, Result.PresentationType.ERROR, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.app_result_type_generic_install_failure));
    INSTALLATION_BLOCKED = new AppResultType("INSTALLATION_BLOCKED", 6, 7, Result.PresentationType.ERROR, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.app_result_type_installation_blocked));
    INSTALLATION_ABORTED = new AppResultType("INSTALLATION_ABORTED", 7, 8, Result.PresentationType.ERROR, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.app_result_type_installation_aborted));
    STORAGE_FAILURE = new AppResultType("STORAGE_FAILURE", 8, 9, Result.PresentationType.ERROR, Result.DetailMessageType.DISPLAYED, AppUtil.getDefContext().getString(R.string.app_result_type_storage_failure));
    INCOMPATIBLE_WITH_DEVICE = new AppResultType("INCOMPATIBLE_WITH_DEVICE", 9, 10, Result.PresentationType.ERROR, Result.DetailMessageType.DISPLAYED, AppUtil.getDefContext().getString(R.string.app_result_type_incompatible_with_device));
    UNKNOWN_INSTALL_STATUS = new AppResultType("UNKNOWN_INSTALL_STATUS", 10, 11, Result.PresentationType.ERROR, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.app_result_type_unknown_install_status));
    ILLEGAL_PACKAGE = new AppResultType("ILLEGAL_PACKAGE", 11, 12, Result.PresentationType.ERROR, Result.DetailMessageType.DISPLAYED, AppUtil.getDefContext().getString(R.string.app_result_type_illegal_package));
    UNINSTALL_REQUIRED = new AppResultType("UNINSTALL_REQUIRED", 12, 13, Result.PresentationType.PROMPT, Result.DetailMessageType.SUBSTITUTED, AppUtil.getDefContext().getString(R.string.app_result_type_uninstall_required));
    UNINSTALL_FAILED = new AppResultType("UNINSTALL_FAILED", 13, 14, Result.PresentationType.ERROR, Result.DetailMessageType.DISPLAYED, AppUtil.getDefContext().getString(R.string.app_result_type_uninstall_failed));
    FAILED_AND_REVERTED = new AppResultType("FAILED_AND_REVERTED", 14, 15, Result.PresentationType.ERROR, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.app_result_type_failed_and_reverted));
    FAILED_TO_RESTORE = new AppResultType("FAILED_TO_RESTORE", 15, 16, Result.PresentationType.ERROR, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.app_result_type_failed_to_restore));
    REBOOTING_WITH_NEW_AP_SERVICE = new AppResultType("REBOOTING_WITH_NEW_AP_SERVICE", 16, 17, Result.PresentationType.SUCCESS, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.app_result_type_rebooting_with_new_ap_service));
    AUTO_UPDATED_APP = new AppResultType("AUTO_UPDATED_APP", 17, 18, Result.PresentationType.SUCCESS, Result.DetailMessageType.SUBSTITUTED, AppUtil.getDefContext().getString(R.string.app_result_type_auto_updated_app));
    REPLACED_WRONG_APP_VARIANT = new AppResultType("REPLACED_WRONG_APP_VARIANT", 18, 19, Result.PresentationType.ERROR, Result.DetailMessageType.SUBSTITUTED, AppUtil.getDefContext().getString(R.string.app_result_type_replaced_wrong_app_variant));
    UNINSTALLED_WRONG_APP_VARIANT = new AppResultType("UNINSTALLED_WRONG_APP_VARIANT", 19, 20, Result.PresentationType.ERROR, Result.DetailMessageType.SUBSTITUTED, AppUtil.getDefContext().getString(R.string.app_result_type_uninstalled_wrong_app_variant));
    INSTALLED_MISSING_APP = new AppResultType("INSTALLED_MISSING_APP", 20, 21, Result.PresentationType.ERROR, Result.DetailMessageType.SUBSTITUTED, AppUtil.getDefContext().getString(R.string.app_result_type_installed_missing_app));
    UNINSTALLING_EXISTING_APP = new AppResultType("UNINSTALLING_EXISTING_APP", 21, 22, Result.PresentationType.STATUS, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.app_result_type_uninstalling_existing_app));
    BACKUP_UNINSTALLING_EXISTING_APP = new AppResultType("BACKUP_UNINSTALLING_EXISTING_APP", 22, 23, Result.PresentationType.STATUS, Result.DetailMessageType.LOGGED, AppUtil.getDefContext().getString(R.string.app_result_type_backup_uninstalling_existing_app));
    AppResultType appResultType = new AppResultType("UNINSTALL_REQUIRED_BACKUP_POSSIBLE", 23, 24, Result.PresentationType.PROMPT, Result.DetailMessageType.SUBSTITUTED, AppUtil.getDefContext().getString(R.string.app_result_type_uninstall_required_backup_possible));
    UNINSTALL_REQUIRED_BACKUP_POSSIBLE = appResultType;
    $VALUES = new AppResultType[] { 
        ATTEMPTING_INSTALLATION, INSTALLATION_SUCCEEDED, INVALID_APK_FILE, IO_EXCEPTION_DURING_LOADING, TIMEOUT_EXPIRED, GENERIC_INSTALL_FAILURE, INSTALLATION_BLOCKED, INSTALLATION_ABORTED, STORAGE_FAILURE, INCOMPATIBLE_WITH_DEVICE, 
        UNKNOWN_INSTALL_STATUS, ILLEGAL_PACKAGE, UNINSTALL_REQUIRED, UNINSTALL_FAILED, FAILED_AND_REVERTED, FAILED_TO_RESTORE, REBOOTING_WITH_NEW_AP_SERVICE, AUTO_UPDATED_APP, REPLACED_WRONG_APP_VARIANT, UNINSTALLED_WRONG_APP_VARIANT, 
        INSTALLED_MISSING_APP, UNINSTALLING_EXISTING_APP, BACKUP_UNINSTALLING_EXISTING_APP, appResultType };
  }
  
  AppResultType(int paramInt1, Result.PresentationType paramPresentationType, Result.DetailMessageType paramDetailMessageType, String paramString1) {
    this.code = paramInt1;
    this.presentationType = paramPresentationType;
    this.detailMessageType = paramDetailMessageType;
    this.message = paramString1;
  }
  
  public static AppResultType fromCode(int paramInt) {
    for (AppResultType appResultType : values()) {
      if (paramInt == appResultType.getCode())
        return appResultType; 
    } 
    return null;
  }
  
  public Result.Category getCategory() {
    return Result.Category.APP_UPDATE;
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


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\webserver\controlhubupdater\result\AppResultType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
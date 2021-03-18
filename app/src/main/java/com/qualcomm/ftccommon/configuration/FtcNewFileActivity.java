package com.qualcomm.ftccommon.configuration;

import android.os.Bundle;

public class FtcNewFileActivity extends FtcConfigurationActivity {
  public static final RequestCode requestCode = RequestCode.NEW_FILE;
  
  protected void ensureConfigFileIsFresh() {}
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    dirtyCheckThenSingletonUSBScanAndUpdateUI(false);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\FtcNewFileActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
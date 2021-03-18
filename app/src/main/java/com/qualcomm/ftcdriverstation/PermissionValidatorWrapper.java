package com.qualcomm.ftcdriverstation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import java.util.ArrayList;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.robotcore.internal.system.PermissionValidatorActivity;

public class PermissionValidatorWrapper extends PermissionValidatorActivity {
  private final String TAG = "PermissionValidatorWrapper";
  
  protected List<String> robotControllerPermissions = new ArrayList<String>() {
    
    };
  
  private SharedPreferences sharedPreferences;
  
  public String mapPermissionToExplanation(String paramString) {
    return paramString.equals("android.permission.WRITE_EXTERNAL_STORAGE") ? Misc.formatForUser(2131624382) : (paramString.equals("android.permission.READ_EXTERNAL_STORAGE") ? Misc.formatForUser(2131624381) : (paramString.equals("android.permission.ACCESS_COARSE_LOCATION") ? Misc.formatForUser(2131624380) : Misc.formatForUser(2131624383)));
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences((Context)this);
    this.permissions = this.robotControllerPermissions;
  }
  
  protected Class onStartApplication() {
    FtcDriverStationActivityBase.setPermissionsValidated();
    String str1 = getResources().getString(2131624245);
    String str2 = getResources().getString(2131624186);
    return (Class)(this.sharedPreferences.getString(str1, str2).equals(str2) ? FtcDriverStationActivityPortrait.class : FtcDriverStationActivityLandscape.class);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftcdriverstation\PermissionValidatorWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
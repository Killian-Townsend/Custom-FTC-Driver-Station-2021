package org.firstinspires.ftc.robotcore.internal.system;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.ArrayList;
import java.util.List;

public final class PermissionValidator {
  private static final int SOME_RANDOM_NUMBER = 1;
  
  private static Activity activity;
  
  private final String TAG = "PermissionValidator";
  
  private List<String> asked;
  
  private PermissionListener listener;
  
  private PreferencesHelper preferencesHelper;
  
  public PermissionValidator(Activity paramActivity, PermissionListener paramPermissionListener) {
    activity = paramActivity;
    this.listener = paramPermissionListener;
    this.preferencesHelper = new PreferencesHelper("PermissionValidator");
    this.asked = new ArrayList<String>();
  }
  
  public void checkPermission(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Checking permission for ");
    stringBuilder.append(paramString);
    RobotLog.ii("PermissionValidator", stringBuilder.toString());
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$internal$system$PermissionValidator$PermissionState[getPermissionState(paramString).ordinal()];
    if (i != 1) {
      if (i != 2) {
        if (i != 3)
          return; 
        stringBuilder = new StringBuilder();
        stringBuilder.append("    Permanently denied: ");
        stringBuilder.append(paramString);
        RobotLog.ii("PermissionValidator", stringBuilder.toString());
        this.listener.onPermissionPermanentlyDenied(paramString);
        return;
      } 
      stringBuilder = new StringBuilder();
      stringBuilder.append("    Denied: ");
      stringBuilder.append(paramString);
      RobotLog.ii("PermissionValidator", stringBuilder.toString());
      requestPermission(paramString);
      return;
    } 
    stringBuilder = new StringBuilder();
    stringBuilder.append("    Granted: ");
    stringBuilder.append(paramString);
    RobotLog.ii("PermissionValidator", stringBuilder.toString());
    this.listener.onPermissionGranted(paramString);
  }
  
  public void explain(final String permission) {
    AlertDialog.Builder builder = new AlertDialog.Builder((Context)activity);
    builder.setMessage(((PermissionValidatorActivity)activity).mapPermissionToExplanation(permission));
    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            PermissionValidator.this.checkPermission(permission);
          }
        });
    builder.create();
    builder.show();
  }
  
  protected PermissionState getPermissionState(String paramString) {
    if (ContextCompat.checkSelfPermission((Context)activity, paramString) == 0) {
      this.preferencesHelper.writeBooleanPrefIfDifferent(paramString, false);
      return PermissionState.GRANTED;
    } 
    return this.preferencesHelper.readBoolean(paramString, false) ? PermissionState.PERMANENTLY_DENIED : PermissionState.DENIED;
  }
  
  public void onRequestPermissionsResult(int paramInt, String[] paramArrayOfString, int[] paramArrayOfint) {
    for (paramInt = 0; paramInt < paramArrayOfint.length; paramInt++) {
      if (paramArrayOfint[paramInt] != 0) {
        RobotLog.ee("PermissionValidator", "You must grant permission to %s.", new Object[] { paramArrayOfString[paramInt] });
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, paramArrayOfString[paramInt])) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("PR permanently denied: ");
          stringBuilder.append(paramArrayOfString[paramInt]);
          RobotLog.ee("PermissionValidator", stringBuilder.toString());
          this.preferencesHelper.writeBooleanPrefIfDifferent(paramArrayOfString[paramInt], true);
        } 
        this.listener.onPermissionDenied(paramArrayOfString[paramInt]);
      } else {
        this.listener.onPermissionGranted(paramArrayOfString[paramInt]);
      } 
    } 
  }
  
  protected void requestPermission(String paramString) {
    ActivityCompat.requestPermissions(activity, new String[] { paramString }, 1);
    this.asked.add(paramString);
  }
  
  private enum PermissionState {
    DENIED, GRANTED, PERMANENTLY_DENIED;
    
    static {
      PermissionState permissionState = new PermissionState("PERMANENTLY_DENIED", 2);
      PERMANENTLY_DENIED = permissionState;
      $VALUES = new PermissionState[] { GRANTED, DENIED, permissionState };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\PermissionValidator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
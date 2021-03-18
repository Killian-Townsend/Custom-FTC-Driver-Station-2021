package org.firstinspires.ftc.robotcore.internal.system;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.ArrayList;
import java.util.List;

public abstract class PermissionValidatorActivity extends Activity {
  public static final String PERMS_VALID_KEY = "org.firstinspires.ftc.robotcore.PERMS_VALID_KEY";
  
  private final String TAG = "PermissionValidatorActivity";
  
  private TextView instructions;
  
  private TextView permDenied;
  
  List<String> permanentDenials = new ArrayList<String>();
  
  private PermissionValidator permissionValidator;
  
  protected List<String> permissions;
  
  protected void enforcePermissions() {
    if (this.permissions.isEmpty()) {
      startRobotController();
      return;
    } 
    this.permissionValidator.checkPermission(this.permissions.get(0));
  }
  
  public abstract String mapPermissionToExplanation(String paramString);
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    RobotLog.ii("PermissionValidatorActivity", "onCreate");
    setContentView(R.layout.activity_permissions_validator);
    TextView textView = (TextView)findViewById(R.id.permDeniedText);
    this.permDenied = textView;
    textView.setVisibility(4);
    textView = (TextView)findViewById(R.id.explanationText);
    this.instructions = textView;
    textView.setVisibility(4);
  }
  
  public void onRequestPermissionsResult(int paramInt, String[] paramArrayOfString, int[] paramArrayOfint) {
    this.permissionValidator.onRequestPermissionsResult(paramInt, paramArrayOfString, paramArrayOfint);
  }
  
  protected void onResume() {
    super.onResume();
    RobotLog.ii("PermissionValidatorActivity", "onResume");
  }
  
  protected void onStart() {
    super.onStart();
    RobotLog.ii("PermissionValidatorActivity", "onStart");
    this.permissionValidator = new PermissionValidator(this, new PermissionListenerImpl());
    enforcePermissions();
  }
  
  protected abstract Class onStartApplication();
  
  protected void startRobotController() {
    if (!this.permanentDenials.isEmpty()) {
      this.permDenied.setVisibility(0);
      this.instructions.setVisibility(0);
      return;
    } 
    RobotLog.ii("PermissionValidatorActivity", "All permissions validated.  Starting RobotController");
    startActivity(new Intent(AppUtil.getDefContext(), onStartApplication()));
    finish();
  }
  
  private class PermissionListenerImpl implements PermissionListener {
    private PermissionListenerImpl() {}
    
    public void onPermissionDenied(String param1String) {
      PermissionValidatorActivity.this.permissionValidator.explain(PermissionValidatorActivity.this.permissions.get(0));
    }
    
    public void onPermissionGranted(String param1String) {
      PermissionValidatorActivity.this.permissions.remove(param1String);
      if (PermissionValidatorActivity.this.permissions.isEmpty()) {
        ServiceController.onApplicationStart();
        PermissionValidatorActivity.this.startRobotController();
        return;
      } 
      PermissionValidatorActivity.this.permissionValidator.checkPermission(PermissionValidatorActivity.this.permissions.get(0));
    }
    
    public void onPermissionPermanentlyDenied(String param1String) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Permission permanently denied for ");
      stringBuilder.append(param1String);
      RobotLog.ee("PermissionValidatorActivity", stringBuilder.toString());
      RobotLog.ee("PermissionValidatorActivity", "Robot Controller will not run");
      if (AppUtil.getInstance().isRobotController()) {
        param1String = "Robot Controller";
      } else {
        param1String = "Driver Station";
      } 
      PermissionValidatorActivity.this.permDenied.setText(String.format(Misc.formatForUser(R.string.permPermanentlyDenied), new Object[] { param1String }));
      PermissionValidatorActivity.this.permDenied.setVisibility(0);
      if (AppUtil.getInstance().isRobotController()) {
        param1String = "Storage, Location, and Camera";
      } else {
        param1String = "Storage and Location";
      } 
      PermissionValidatorActivity.this.instructions.setText(String.format(Misc.formatForUser(R.string.permPermanentlyDeniedRecovery), new Object[] { param1String }));
      PermissionValidatorActivity.this.instructions.setVisibility(0);
      PermissionValidatorActivity.this.instructions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param2View) {
              Intent intent = new Intent();
              intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
              intent.setData(Uri.fromParts("package", PermissionValidatorActivity.this.getPackageName(), null));
              PermissionValidatorActivity.this.startActivity(intent);
            }
          });
    }
  }
  
  class null implements View.OnClickListener {
    public void onClick(View param1View) {
      Intent intent = new Intent();
      intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
      intent.setData(Uri.fromParts("package", PermissionValidatorActivity.this.getPackageName(), null));
      PermissionValidatorActivity.this.startActivity(intent);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\PermissionValidatorActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
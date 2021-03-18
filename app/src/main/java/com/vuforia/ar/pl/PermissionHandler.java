package com.vuforia.ar.pl;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Build;
import java.util.ArrayList;

public class PermissionHandler {
  private static final int AR_PERMISSIONS_STATUS_DENIED = 2;
  
  private static final int AR_PERMISSIONS_STATUS_FAILED = 0;
  
  private static final int AR_PERMISSIONS_STATUS_GRANTED = 3;
  
  private static final int AR_PERMISSIONS_STATUS_REQUESTED = 1;
  
  private static final String MODULENAME = "PermissionHandler";
  
  private static final int PERMISSIONS_REQUEST_CODE = 100;
  
  private boolean mIsPermissionsRequested = false;
  
  private String[] mPermissionsArrayToRequest;
  
  private int mPermissionsStatus = 0;
  
  public int requestPermissions(Activity paramActivity, String[] paramArrayOfString) {
    int i = this.mPermissionsStatus;
    if (i == 3 || i == 1 || i == 2)
      return this.mPermissionsStatus; 
    if (paramActivity == null)
      return 0; 
    PackageManager packageManager = paramActivity.getPackageManager();
    ArrayList<String> arrayList = new ArrayList(paramArrayOfString.length);
    for (i = 0; i < paramArrayOfString.length; i++) {
      if (packageManager.checkPermission(paramArrayOfString[i], paramActivity.getPackageName()) != 0)
        arrayList.add(paramArrayOfString[i]); 
    } 
    if (arrayList.isEmpty())
      this.mPermissionsStatus = 3; 
    if (Build.VERSION.SDK_INT >= 23 && !this.mIsPermissionsRequested && !arrayList.isEmpty())
      try {
        FragmentManager fragmentManager = paramActivity.getFragmentManager();
        this.mPermissionsArrayToRequest = arrayList.<String>toArray(new String[arrayList.size()]);
        PermissionsRequestFragment permissionsRequestFragment = new PermissionsRequestFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(0, permissionsRequestFragment);
        fragmentTransaction.commit();
        this.mPermissionsStatus = 1;
        return this.mPermissionsStatus;
      } catch (Exception exception) {
        return 0;
      }  
    return this.mPermissionsStatus;
  }
  
  public class PermissionsRequestFragment extends Fragment {
    private void removeSelf() {
      FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
      fragmentTransaction.remove(this);
      fragmentTransaction.commit();
    }
    
    public void onRequestPermissionsResult(int param1Int, String[] param1ArrayOfString, int[] param1ArrayOfint) {
      if (param1Int != 100)
        return; 
      for (param1Int = 0; param1Int < param1ArrayOfint.length; param1Int++) {
        if (param1ArrayOfint[param1Int] == -1) {
          PermissionHandler.access$202(PermissionHandler.this, 2);
          break;
        } 
      } 
      if (PermissionHandler.this.mPermissionsStatus != 2)
        PermissionHandler.access$202(PermissionHandler.this, 3); 
      removeSelf();
    }
    
    public void onStart() {
      super.onStart();
      if (!PermissionHandler.this.mIsPermissionsRequested) {
        requestPermissions(PermissionHandler.this.mPermissionsArrayToRequest, 100);
        PermissionHandler.access$002(PermissionHandler.this, true);
      } 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\ar\pl\PermissionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.inspection;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.util.ThreadPool;
import com.qualcomm.robotcore.wifi.NetworkType;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import org.firstinspires.ftc.robotcore.internal.network.DeviceNameManagerFactory;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.network.StartResult;
import org.firstinspires.ftc.robotcore.internal.network.WifiDirectAgent;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.ui.ThemedActivity;
import org.firstinspires.ftc.robotcore.internal.ui.UILocation;

public abstract class InspectionActivity extends ThemedActivity {
  protected static final int CH_OS_MIN_VERSIONNUM = 4;
  
  protected static final int DS_MIN_VERSIONCODE = 21;
  
  protected static final int RC_MIN_VERSIONCODE = 21;
  
  private static final boolean SHOW_TRAFFIC_STATS = false;
  
  public static final String TAG = "InspectionActivity";
  
  private static final String badMark = "X";
  
  private static final String goodMark = "✓";
  
  private static final String notApplicable = "N/A";
  
  TextView airplaneMode;
  
  LinearLayout airplaneModeLayout;
  
  TextView androidVersion;
  
  TextView appsStatus;
  
  TextView batteryLevel;
  
  TextView bluetooth;
  
  TextView bytesPerSecond;
  
  TextView bytesPerSecondLabel;
  
  TextView controlHubOsVersion;
  
  LinearLayout controlHubOsVersionLayout;
  
  TextView firmwareVersion;
  
  StartResult nameManagerStartResult;
  
  private boolean properBluetoothState;
  
  private boolean properWifiConnectedState;
  
  Future refreshFuture = null;
  
  private final boolean remoteConfigure = AppUtil.getInstance().isDriverStation();
  
  Pattern teamNoRegex;
  
  int textError;
  
  int textOk;
  
  int textWarning;
  
  TextView trafficCount;
  
  TextView trafficCountLabel;
  
  TextView txtAppVersion;
  
  TextView txtIsDSInstalled;
  
  TextView txtIsDefaultPassword;
  
  TextView txtIsRCInstalled;
  
  TextView txtManufacturer;
  
  TextView txtModel;
  
  TextView widiConnected;
  
  TextView wifiConnected;
  
  TextView wifiEnabled;
  
  TextView wifiName;
  
  public InspectionActivity() {
    AppUtil.getInstance();
    this.textOk = AppUtil.getColor(R.color.text_okay);
    AppUtil.getInstance();
    this.textWarning = AppUtil.getColor(R.color.text_warning);
    AppUtil.getInstance();
    this.textError = AppUtil.getColor(R.color.text_error);
    this.nameManagerStartResult = new StartResult();
  }
  
  private void refresh(TextView paramTextView, long paramLong) {
    paramTextView.setText(String.format("%d", new Object[] { Long.valueOf(paramLong) }));
  }
  
  private void refresh(TextView paramTextView, String paramString, boolean paramBoolean) {
    int i;
    paramTextView.setText(paramString);
    if (paramBoolean) {
      i = this.textOk;
    } else {
      i = this.textError;
    } 
    paramTextView.setTextColor(i);
  }
  
  private void refresh(TextView paramTextView, boolean paramBoolean) {
    refresh(paramTextView, paramBoolean, true);
  }
  
  private void refresh(TextView paramTextView, boolean paramBoolean1, boolean paramBoolean2) {
    int i;
    String str;
    if (paramBoolean1) {
      str = "✓";
    } else {
      str = "X";
    } 
    paramTextView.setText(str);
    if (paramBoolean1 == paramBoolean2) {
      i = this.textOk;
    } else {
      i = this.textError;
    } 
    paramTextView.setTextColor(i);
  }
  
  private boolean refreshOptional(TextView paramTextView, String paramString, boolean paramBoolean) {
    boolean bool = InspectionState.isPackageInstalled(paramString);
    if (paramBoolean) {
      refresh(paramTextView, bool);
      return bool;
    } 
    paramTextView.setText("N/A");
    paramTextView.setTextColor(this.textOk);
    return true;
  }
  
  private boolean refreshPackage(TextView paramTextView, String paramString, int paramInt1, int paramInt2) {
    if (InspectionState.isPackageInstalled(paramString)) {
      paramTextView.setText(paramString);
      if (paramInt1 < paramInt2) {
        paramTextView.setTextColor(this.textWarning);
        return false;
      } 
      paramTextView.setTextColor(this.textOk);
    } else {
      paramTextView.setText("X");
      paramTextView.setTextColor(this.textOk);
    } 
    return true;
  }
  
  private void refreshTrafficCount(TextView paramTextView, long paramLong1, long paramLong2) {
    paramTextView.setText(String.format("%d/%d", new Object[] { Long.valueOf(paramLong1), Long.valueOf(paramLong2) }));
  }
  
  private void refreshTrafficStats(InspectionState paramInspectionState) {}
  
  private void showToast(String paramString) {
    AppUtil.getInstance().showToast(UILocation.BOTH, paramString);
  }
  
  private void startRefreshing() {
    stopRefreshing();
    ScheduledExecutorService scheduledExecutorService = ThreadPool.getDefaultScheduler();
    Runnable runnable = new Runnable() {
        public void run() {
          AppUtil.getInstance().runOnUiThread(new Runnable() {
                public void run() {
                  InspectionActivity.this.refresh();
                }
              });
        }
      };
    long l = 5000L;
    this.refreshFuture = scheduledExecutorService.scheduleAtFixedRate(runnable, l, l, TimeUnit.MILLISECONDS);
  }
  
  private void stopRefreshing() {
    Future future = this.refreshFuture;
    if (future != null) {
      future.cancel(false);
      this.refreshFuture = null;
    } 
  }
  
  protected void enableTrafficDataReporting(boolean paramBoolean) {
    if (paramBoolean) {
      this.trafficCount.setVisibility(0);
      this.bytesPerSecond.setVisibility(0);
      this.trafficCountLabel.setVisibility(0);
      this.bytesPerSecondLabel.setVisibility(0);
      return;
    } 
    this.trafficCount.setVisibility(8);
    this.bytesPerSecond.setVisibility(8);
    this.trafficCountLabel.setVisibility(8);
    this.bytesPerSecondLabel.setVisibility(8);
  }
  
  protected FrameLayout getBackBar() {
    return (FrameLayout)findViewById(R.id.backbar);
  }
  
  public String getTag() {
    return "InspectionActivity";
  }
  
  protected abstract boolean inspectingRobotController();
  
  public boolean isValidAndroidVersion(InspectionState paramInspectionState) {
    return (paramInspectionState.sdkInt >= 23);
  }
  
  public boolean isValidControlHubOsVersion(InspectionState paramInspectionState) {
    return (paramInspectionState.controlHubOsVersionNum >= 4);
  }
  
  public boolean isValidDeviceName(InspectionState paramInspectionState) {
    return (paramInspectionState.deviceName.contains("\n") || paramInspectionState.deviceName.contains("\r")) ? false : this.teamNoRegex.matcher(paramInspectionState.deviceName).find();
  }
  
  public boolean isValidFirmwareVersion(InspectionState paramInspectionState) {
    return !(paramInspectionState.firmwareVersion != null && (paramInspectionState.firmwareVersion.contains("1.6.0") || paramInspectionState.firmwareVersion.contains("1.7.0") || paramInspectionState.firmwareVersion.contains("1.7.2") || paramInspectionState.firmwareVersion.contains("mismatched")));
  }
  
  protected void makeWirelessAPModeSane() {
    ((TextView)findViewById(R.id.labelWifiName)).setText(getString(R.string.wifiAccessPointLabel));
    this.properWifiConnectedState = true;
  }
  
  protected void onCreate(Bundle paramBundle) {
    String str;
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_inspection);
    this.txtIsRCInstalled = (TextView)findViewById(R.id.txtIsRCInstalled);
    this.txtIsDSInstalled = (TextView)findViewById(R.id.txtIsDSInstalled);
    this.wifiName = (TextView)findViewById(R.id.wifiName);
    this.trafficCount = (TextView)findViewById(R.id.trafficCount);
    this.bytesPerSecond = (TextView)findViewById(R.id.bytesPerSecond);
    this.trafficCountLabel = (TextView)findViewById(R.id.trafficCountLabel);
    this.bytesPerSecondLabel = (TextView)findViewById(R.id.bytesPerSecondLabel);
    this.widiConnected = (TextView)findViewById(R.id.widiConnected);
    this.wifiEnabled = (TextView)findViewById(R.id.wifiEnabled);
    this.batteryLevel = (TextView)findViewById(R.id.batteryLevel);
    this.androidVersion = (TextView)findViewById(R.id.androidVersion);
    this.controlHubOsVersion = (TextView)findViewById(R.id.controlHubOsVersion);
    this.firmwareVersion = (TextView)findViewById(R.id.hubFirmware);
    this.airplaneMode = (TextView)findViewById(R.id.airplaneMode);
    this.bluetooth = (TextView)findViewById(R.id.bluetoothEnabled);
    this.wifiConnected = (TextView)findViewById(R.id.wifiConnected);
    this.appsStatus = (TextView)findViewById(R.id.appsStatus);
    this.txtAppVersion = (TextView)findViewById(R.id.textDeviceName);
    this.txtIsDefaultPassword = (TextView)findViewById(R.id.isDefaultPassword);
    this.controlHubOsVersionLayout = (LinearLayout)findViewById(R.id.controlHubOsVersionLayout);
    this.airplaneModeLayout = (LinearLayout)findViewById(R.id.airplaneModeLayout);
    TextView textView = this.txtAppVersion;
    if (inspectingRobotController()) {
      str = getString(R.string.titleInspectionReportRC);
    } else {
      str = getString(R.string.titleInspectionReportDS);
    } 
    textView.setText(str);
    if (!inspectingRobotController()) {
      this.txtIsDefaultPassword.setVisibility(8);
      findViewById(R.id.textViewPassword).setVisibility(8);
    } 
    this.txtManufacturer = (TextView)findViewById(R.id.txtManufacturer);
    this.txtModel = (TextView)findViewById(R.id.txtModel);
    this.teamNoRegex = Pattern.compile("^\\d{1,5}(-\\w)?-(RC|DS)\\z", 2);
    ImageButton imageButton = (ImageButton)findViewById(R.id.menu_buttons);
    if (useMenu()) {
      imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
              PopupMenu popupMenu = new PopupMenu((Context)InspectionActivity.this, param1View);
              popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem param2MenuItem) {
                      return InspectionActivity.this.onOptionsItemSelected(param2MenuItem);
                    }
                  });
              popupMenu.inflate(R.menu.main_menu);
              popupMenu.show();
            }
          });
    } else {
      imageButton.setEnabled(false);
      imageButton.setVisibility(4);
    } 
    DeviceNameManagerFactory.getInstance().start(this.nameManagerStartResult);
    this.properWifiConnectedState = false;
    this.properBluetoothState = false;
    if (NetworkConnectionHandler.getDefaultNetworkType((Context)this) == NetworkType.WIRELESSAP)
      makeWirelessAPModeSane(); 
    enableTrafficDataReporting(false);
    refresh();
  }
  
  public boolean onCreateOptionsMenu(Menu paramMenu) {
    getMenuInflater().inflate(R.menu.main_menu, paramMenu);
    return true;
  }
  
  protected void onDestroy() {
    super.onDestroy();
    DeviceNameManagerFactory.getInstance().stop(this.nameManagerStartResult);
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
    if (paramMenuItem.getItemId() == R.id.disconnect_from_wifidirect) {
      if (!this.remoteConfigure) {
        if (WifiDirectAgent.getInstance().disconnectFromWifiDirect()) {
          showToast(getString(R.string.toastDisconnectedFromWifiDirect));
        } else {
          showToast(getString(R.string.toastErrorDisconnectingFromWifiDirect));
        } 
      } else {
        NetworkConnectionHandler.getInstance().sendCommand(new Command("CMD_DISCONNECT_FROM_WIFI_DIRECT"));
      } 
      return true;
    } 
    return super.onOptionsItemSelected(paramMenuItem);
  }
  
  protected void onPause() {
    super.onPause();
    stopRefreshing();
  }
  
  protected void onResume() {
    super.onResume();
    startRefreshing();
  }
  
  protected void refresh() {
    InspectionState inspectionState = new InspectionState();
    inspectionState.initializeLocal(DeviceNameManagerFactory.getInstance());
    refresh(inspectionState);
  }
  
  protected void refresh(InspectionState paramInspectionState) {
    // Byte code:
    //   0: aload_0
    //   1: aload_0
    //   2: getfield widiConnected : Landroid/widget/TextView;
    //   5: aload_1
    //   6: getfield wifiDirectConnected : Z
    //   9: invokespecial refresh : (Landroid/widget/TextView;Z)V
    //   12: aload_0
    //   13: aload_0
    //   14: getfield wifiEnabled : Landroid/widget/TextView;
    //   17: aload_1
    //   18: getfield wifiEnabled : Z
    //   21: invokespecial refresh : (Landroid/widget/TextView;Z)V
    //   24: aload_0
    //   25: aload_1
    //   26: invokespecial refreshTrafficStats : (Lorg/firstinspires/inspection/InspectionState;)V
    //   29: aload_0
    //   30: aload_0
    //   31: getfield bluetooth : Landroid/widget/TextView;
    //   34: aload_1
    //   35: getfield bluetoothOn : Z
    //   38: aload_0
    //   39: getfield properBluetoothState : Z
    //   42: invokespecial refresh : (Landroid/widget/TextView;ZZ)V
    //   45: aload_0
    //   46: aload_0
    //   47: getfield wifiConnected : Landroid/widget/TextView;
    //   50: aload_1
    //   51: getfield wifiConnected : Z
    //   54: aload_0
    //   55: getfield properWifiConnectedState : Z
    //   58: invokespecial refresh : (Landroid/widget/TextView;ZZ)V
    //   61: aload_0
    //   62: getfield txtManufacturer : Landroid/widget/TextView;
    //   65: aload_1
    //   66: getfield manufacturer : Ljava/lang/String;
    //   69: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   72: aload_0
    //   73: getfield txtModel : Landroid/widget/TextView;
    //   76: aload_1
    //   77: getfield model : Ljava/lang/String;
    //   80: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   83: aload_0
    //   84: aload_0
    //   85: getfield androidVersion : Landroid/widget/TextView;
    //   88: aload_1
    //   89: getfield osVersion : Ljava/lang/String;
    //   92: aload_0
    //   93: aload_1
    //   94: invokevirtual isValidAndroidVersion : (Lorg/firstinspires/inspection/InspectionState;)Z
    //   97: invokespecial refresh : (Landroid/widget/TextView;Ljava/lang/String;Z)V
    //   100: aload_0
    //   101: aload_0
    //   102: getfield firmwareVersion : Landroid/widget/TextView;
    //   105: aload_1
    //   106: getfield firmwareVersion : Ljava/lang/String;
    //   109: aload_0
    //   110: aload_1
    //   111: invokevirtual isValidFirmwareVersion : (Lorg/firstinspires/inspection/InspectionState;)Z
    //   114: invokespecial refresh : (Landroid/widget/TextView;Ljava/lang/String;Z)V
    //   117: aload_0
    //   118: aload_0
    //   119: getfield wifiName : Landroid/widget/TextView;
    //   122: aload_1
    //   123: getfield deviceName : Ljava/lang/String;
    //   126: aload_0
    //   127: aload_1
    //   128: invokevirtual isValidDeviceName : (Lorg/firstinspires/inspection/InspectionState;)Z
    //   131: invokespecial refresh : (Landroid/widget/TextView;Ljava/lang/String;Z)V
    //   134: aload_0
    //   135: getfield batteryLevel : Landroid/widget/TextView;
    //   138: astore #6
    //   140: new java/lang/StringBuilder
    //   143: dup
    //   144: invokespecial <init> : ()V
    //   147: astore #7
    //   149: aload #7
    //   151: aload_1
    //   152: getfield batteryFraction : D
    //   155: ldc2_w 100.0
    //   158: dmul
    //   159: invokestatic round : (D)J
    //   162: invokevirtual append : (J)Ljava/lang/StringBuilder;
    //   165: pop
    //   166: aload #7
    //   168: ldc_w '%'
    //   171: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   174: pop
    //   175: aload #6
    //   177: aload #7
    //   179: invokevirtual toString : ()Ljava/lang/String;
    //   182: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   185: aload_0
    //   186: getfield batteryLevel : Landroid/widget/TextView;
    //   189: astore #6
    //   191: aload_1
    //   192: getfield batteryFraction : D
    //   195: ldc2_w 0.6
    //   198: dcmpl
    //   199: ifle -> 210
    //   202: aload_0
    //   203: getfield textOk : I
    //   206: istore_2
    //   207: goto -> 215
    //   210: aload_0
    //   211: getfield textWarning : I
    //   214: istore_2
    //   215: aload #6
    //   217: iload_2
    //   218: invokevirtual setTextColor : (I)V
    //   221: aload_0
    //   222: getfield txtIsDefaultPassword : Landroid/widget/TextView;
    //   225: astore #6
    //   227: aload_1
    //   228: getfield isDefaultPassword : Z
    //   231: istore #5
    //   233: iconst_1
    //   234: istore #4
    //   236: aload_0
    //   237: aload #6
    //   239: iload #5
    //   241: iconst_1
    //   242: ixor
    //   243: invokespecial refresh : (Landroid/widget/TextView;Z)V
    //   246: ldc_w ''
    //   249: aload_1
    //   250: getfield controlHubOsVersion : Ljava/lang/String;
    //   253: invokevirtual equals : (Ljava/lang/Object;)Z
    //   256: ifeq -> 271
    //   259: aload_0
    //   260: getfield controlHubOsVersionLayout : Landroid/widget/LinearLayout;
    //   263: bipush #8
    //   265: invokevirtual setVisibility : (I)V
    //   268: goto -> 296
    //   271: aload_0
    //   272: getfield controlHubOsVersionLayout : Landroid/widget/LinearLayout;
    //   275: iconst_0
    //   276: invokevirtual setVisibility : (I)V
    //   279: aload_0
    //   280: aload_0
    //   281: getfield controlHubOsVersion : Landroid/widget/TextView;
    //   284: aload_1
    //   285: getfield controlHubOsVersion : Ljava/lang/String;
    //   288: aload_0
    //   289: aload_1
    //   290: invokevirtual isValidControlHubOsVersion : (Lorg/firstinspires/inspection/InspectionState;)Z
    //   293: invokespecial refresh : (Landroid/widget/TextView;Ljava/lang/String;Z)V
    //   296: aload_1
    //   297: getfield manufacturer : Ljava/lang/String;
    //   300: ldc_w 'REV Robotics'
    //   303: invokevirtual equals : (Ljava/lang/Object;)Z
    //   306: ifeq -> 321
    //   309: aload_0
    //   310: getfield airplaneModeLayout : Landroid/widget/LinearLayout;
    //   313: bipush #8
    //   315: invokevirtual setVisibility : (I)V
    //   318: goto -> 341
    //   321: aload_0
    //   322: getfield airplaneModeLayout : Landroid/widget/LinearLayout;
    //   325: iconst_0
    //   326: invokevirtual setVisibility : (I)V
    //   329: aload_0
    //   330: aload_0
    //   331: getfield airplaneMode : Landroid/widget/TextView;
    //   334: aload_1
    //   335: getfield airplaneModeOn : Z
    //   338: invokespecial refresh : (Landroid/widget/TextView;Z)V
    //   341: aload_0
    //   342: aload_0
    //   343: getfield txtIsRCInstalled : Landroid/widget/TextView;
    //   346: aload_1
    //   347: getfield robotControllerVersion : Ljava/lang/String;
    //   350: aload_1
    //   351: getfield robotControllerVersionCode : I
    //   354: bipush #21
    //   356: invokespecial refreshPackage : (Landroid/widget/TextView;Ljava/lang/String;II)Z
    //   359: ifeq -> 367
    //   362: iconst_1
    //   363: istore_2
    //   364: goto -> 369
    //   367: iconst_0
    //   368: istore_2
    //   369: aload_0
    //   370: aload_0
    //   371: getfield txtIsDSInstalled : Landroid/widget/TextView;
    //   374: aload_1
    //   375: getfield driverStationVersion : Ljava/lang/String;
    //   378: aload_1
    //   379: getfield driverStationVersionCode : I
    //   382: bipush #21
    //   384: invokespecial refreshPackage : (Landroid/widget/TextView;Ljava/lang/String;II)Z
    //   387: ifeq -> 399
    //   390: iload_2
    //   391: ifeq -> 399
    //   394: iconst_1
    //   395: istore_2
    //   396: goto -> 401
    //   399: iconst_0
    //   400: istore_2
    //   401: aload_1
    //   402: invokevirtual isRobotControllerInstalled : ()Z
    //   405: ifne -> 415
    //   408: aload_1
    //   409: invokevirtual isDriverStationInstalled : ()Z
    //   412: ifeq -> 433
    //   415: iload_2
    //   416: istore_3
    //   417: aload_1
    //   418: invokevirtual isRobotControllerInstalled : ()Z
    //   421: ifeq -> 457
    //   424: iload_2
    //   425: istore_3
    //   426: aload_1
    //   427: invokevirtual isDriverStationInstalled : ()Z
    //   430: ifeq -> 457
    //   433: aload_0
    //   434: getfield txtIsDSInstalled : Landroid/widget/TextView;
    //   437: aload_0
    //   438: getfield textError : I
    //   441: invokevirtual setTextColor : (I)V
    //   444: aload_0
    //   445: getfield txtIsRCInstalled : Landroid/widget/TextView;
    //   448: aload_0
    //   449: getfield textError : I
    //   452: invokevirtual setTextColor : (I)V
    //   455: iconst_0
    //   456: istore_3
    //   457: aload_0
    //   458: aload_1
    //   459: invokevirtual validateAppsInstalled : (Lorg/firstinspires/inspection/InspectionState;)Z
    //   462: ifeq -> 475
    //   465: iload_3
    //   466: ifeq -> 475
    //   469: iload #4
    //   471: istore_2
    //   472: goto -> 477
    //   475: iconst_0
    //   476: istore_2
    //   477: aload_0
    //   478: getfield appsStatus : Landroid/widget/TextView;
    //   481: astore_1
    //   482: iload_2
    //   483: ifeq -> 494
    //   486: aload_0
    //   487: getfield textOk : I
    //   490: istore_3
    //   491: goto -> 499
    //   494: aload_0
    //   495: getfield textError : I
    //   498: istore_3
    //   499: aload_1
    //   500: iload_3
    //   501: invokevirtual setTextColor : (I)V
    //   504: aload_0
    //   505: getfield appsStatus : Landroid/widget/TextView;
    //   508: astore #6
    //   510: iload_2
    //   511: ifeq -> 520
    //   514: ldc '✓'
    //   516: astore_1
    //   517: goto -> 523
    //   520: ldc 'X'
    //   522: astore_1
    //   523: aload #6
    //   525: aload_1
    //   526: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   529: return
  }
  
  protected abstract boolean useMenu();
  
  protected abstract boolean validateAppsInstalled(InspectionState paramInspectionState);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\inspection\InspectionActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
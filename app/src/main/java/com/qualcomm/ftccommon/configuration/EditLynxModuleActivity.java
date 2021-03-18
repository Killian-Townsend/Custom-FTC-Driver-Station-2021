package com.qualcomm.ftccommon.configuration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.qualcomm.ftccommon.CommandList;
import com.qualcomm.ftccommon.R;
import com.qualcomm.robotcore.hardware.ControlSystem;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.LynxI2cDeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.LynxModuleConfiguration;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.system.Assert;

public class EditLynxModuleActivity extends EditActivity {
  public static final RequestCode requestCode = RequestCode.EDIT_LYNX_MODULE;
  
  private AdapterView.OnItemClickListener editLaunchListener = new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
        // Byte code:
        //   0: aload_0
        //   1: getfield this$0 : Lcom/qualcomm/ftccommon/configuration/EditLynxModuleActivity;
        //   4: invokestatic access$000 : (Lcom/qualcomm/ftccommon/configuration/EditLynxModuleActivity;)[Lcom/qualcomm/ftccommon/configuration/EditActivity$DisplayNameAndRequestCode;
        //   7: iload_3
        //   8: aaload
        //   9: astore_1
        //   10: getstatic com/qualcomm/ftccommon/configuration/EditLynxModuleActivity$2.$SwitchMap$com$qualcomm$ftccommon$configuration$RequestCode : [I
        //   13: aload_1
        //   14: getfield requestCode : Lcom/qualcomm/ftccommon/configuration/RequestCode;
        //   17: invokevirtual ordinal : ()I
        //   20: iaload
        //   21: tableswitch default -> 68, 1 -> 172, 2 -> 151, 3 -> 141, 4 -> 131, 5 -> 121, 6 -> 111, 7 -> 90, 8 -> 69
        //   68: return
        //   69: aload_0
        //   70: getfield this$0 : Lcom/qualcomm/ftccommon/configuration/EditLynxModuleActivity;
        //   73: astore_2
        //   74: aload_2
        //   75: aload_1
        //   76: iconst_0
        //   77: ldc com/qualcomm/ftccommon/configuration/EditAnalogInputDevicesActivity
        //   79: aload_2
        //   80: invokestatic access$200 : (Lcom/qualcomm/ftccommon/configuration/EditLynxModuleActivity;)Lcom/qualcomm/robotcore/hardware/configuration/LynxModuleConfiguration;
        //   83: invokevirtual getAnalogInputs : ()Ljava/util/List;
        //   86: invokestatic access$500 : (Lcom/qualcomm/ftccommon/configuration/EditLynxModuleActivity;Lcom/qualcomm/ftccommon/configuration/EditActivity$DisplayNameAndRequestCode;ILjava/lang/Class;Ljava/util/List;)V
        //   89: return
        //   90: aload_0
        //   91: getfield this$0 : Lcom/qualcomm/ftccommon/configuration/EditLynxModuleActivity;
        //   94: astore_2
        //   95: aload_2
        //   96: aload_1
        //   97: iconst_0
        //   98: ldc com/qualcomm/ftccommon/configuration/EditDigitalDevicesActivityLynx
        //   100: aload_2
        //   101: invokestatic access$200 : (Lcom/qualcomm/ftccommon/configuration/EditLynxModuleActivity;)Lcom/qualcomm/robotcore/hardware/configuration/LynxModuleConfiguration;
        //   104: invokevirtual getDigitalDevices : ()Ljava/util/List;
        //   107: invokestatic access$500 : (Lcom/qualcomm/ftccommon/configuration/EditLynxModuleActivity;Lcom/qualcomm/ftccommon/configuration/EditActivity$DisplayNameAndRequestCode;ILjava/lang/Class;Ljava/util/List;)V
        //   110: return
        //   111: aload_0
        //   112: getfield this$0 : Lcom/qualcomm/ftccommon/configuration/EditLynxModuleActivity;
        //   115: aload_1
        //   116: iconst_3
        //   117: invokestatic access$400 : (Lcom/qualcomm/ftccommon/configuration/EditLynxModuleActivity;Lcom/qualcomm/ftccommon/configuration/EditActivity$DisplayNameAndRequestCode;I)V
        //   120: return
        //   121: aload_0
        //   122: getfield this$0 : Lcom/qualcomm/ftccommon/configuration/EditLynxModuleActivity;
        //   125: aload_1
        //   126: iconst_2
        //   127: invokestatic access$400 : (Lcom/qualcomm/ftccommon/configuration/EditLynxModuleActivity;Lcom/qualcomm/ftccommon/configuration/EditActivity$DisplayNameAndRequestCode;I)V
        //   130: return
        //   131: aload_0
        //   132: getfield this$0 : Lcom/qualcomm/ftccommon/configuration/EditLynxModuleActivity;
        //   135: aload_1
        //   136: iconst_1
        //   137: invokestatic access$400 : (Lcom/qualcomm/ftccommon/configuration/EditLynxModuleActivity;Lcom/qualcomm/ftccommon/configuration/EditActivity$DisplayNameAndRequestCode;I)V
        //   140: return
        //   141: aload_0
        //   142: getfield this$0 : Lcom/qualcomm/ftccommon/configuration/EditLynxModuleActivity;
        //   145: aload_1
        //   146: iconst_0
        //   147: invokestatic access$400 : (Lcom/qualcomm/ftccommon/configuration/EditLynxModuleActivity;Lcom/qualcomm/ftccommon/configuration/EditActivity$DisplayNameAndRequestCode;I)V
        //   150: return
        //   151: aload_0
        //   152: getfield this$0 : Lcom/qualcomm/ftccommon/configuration/EditLynxModuleActivity;
        //   155: astore_2
        //   156: aload_2
        //   157: aload_1
        //   158: iconst_0
        //   159: ldc com/qualcomm/ftccommon/configuration/EditServoListActivity
        //   161: aload_2
        //   162: invokestatic access$200 : (Lcom/qualcomm/ftccommon/configuration/EditLynxModuleActivity;)Lcom/qualcomm/robotcore/hardware/configuration/LynxModuleConfiguration;
        //   165: invokevirtual getServos : ()Ljava/util/List;
        //   168: invokestatic access$300 : (Lcom/qualcomm/ftccommon/configuration/EditLynxModuleActivity;Lcom/qualcomm/ftccommon/configuration/EditActivity$DisplayNameAndRequestCode;ILjava/lang/Class;Ljava/util/List;)V
        //   171: return
        //   172: aload_0
        //   173: getfield this$0 : Lcom/qualcomm/ftccommon/configuration/EditLynxModuleActivity;
        //   176: aload_1
        //   177: invokestatic access$100 : (Lcom/qualcomm/ftccommon/configuration/EditLynxModuleActivity;Lcom/qualcomm/ftccommon/configuration/EditActivity$DisplayNameAndRequestCode;)V
        //   180: return
      }
    };
  
  private EditActivity.DisplayNameAndRequestCode[] listKeys;
  
  private LynxModuleConfiguration lynxModuleConfiguration;
  
  private EditText lynx_module_name;
  
  private void editI2cBus(EditActivity.DisplayNameAndRequestCode paramDisplayNameAndRequestCode, int paramInt) {
    EditParameters editParameters = initParameters(0, LynxI2cDeviceConfiguration.class, this.lynxModuleConfiguration.getI2cDevices(paramInt));
    editParameters.setI2cBus(paramInt);
    editParameters.setGrowable(true);
    handleLaunchEdit(paramDisplayNameAndRequestCode.requestCode, EditI2cDevicesActivityLynx.class, editParameters);
  }
  
  private void editMotors(EditActivity.DisplayNameAndRequestCode paramDisplayNameAndRequestCode) {
    boolean bool1;
    int i = this.lynxModuleConfiguration.getMotors().size();
    boolean bool2 = true;
    if (i == 4) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    Assert.assertTrue(bool1);
    if (((DeviceConfiguration)this.lynxModuleConfiguration.getMotors().get(0)).getPort() == 0) {
      bool1 = bool2;
    } else {
      bool1 = false;
    } 
    Assert.assertTrue(bool1);
    EditParameters editParameters = initParameters(0, DeviceConfiguration.class, this.lynxModuleConfiguration.getMotors());
    handleLaunchEdit(paramDisplayNameAndRequestCode.requestCode, EditMotorListActivity.class, editParameters);
  }
  
  private void editServos(EditActivity.DisplayNameAndRequestCode paramDisplayNameAndRequestCode, int paramInt, Class paramClass, List<DeviceConfiguration> paramList) {
    EditParameters editParameters = initParameters(paramInt, DeviceConfiguration.class, paramList);
    handleLaunchEdit(paramDisplayNameAndRequestCode.requestCode, paramClass, editParameters);
  }
  
  private void editSimple(EditActivity.DisplayNameAndRequestCode paramDisplayNameAndRequestCode, int paramInt, Class paramClass, List<DeviceConfiguration> paramList) {
    EditParameters editParameters = initParameters(paramInt, DeviceConfiguration.class, paramList);
    handleLaunchEdit(paramDisplayNameAndRequestCode.requestCode, paramClass, editParameters);
  }
  
  protected void finishOk() {
    this.controllerConfiguration.setName(this.lynx_module_name.getText().toString());
    finishOk(new EditParameters<DeviceConfiguration>(this, (DeviceConfiguration)this.controllerConfiguration));
  }
  
  public String getTag() {
    return getClass().getSimpleName();
  }
  
  <ITEM_T extends DeviceConfiguration> EditParameters initParameters(int paramInt, Class<ITEM_T> paramClass, List<ITEM_T> paramList) {
    EditParameters<ITEM_T> editParameters = new EditParameters<ITEM_T>(this, paramClass, paramList);
    editParameters.setInitialPortNumber(paramInt);
    editParameters.setControlSystem(ControlSystem.REV_HUB);
    return editParameters;
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    logActivityResult(paramInt1, paramInt2, paramIntent);
    RequestCode requestCode = RequestCode.fromValue(paramInt1);
    if (paramInt2 == -1) {
      EditParameters<DeviceConfiguration> editParameters;
      RequestCode requestCode1 = RequestCode.EDIT_MOTOR_LIST;
      boolean bool = true;
      if (requestCode == requestCode1) {
        boolean bool1;
        editParameters = EditParameters.fromIntent(this, paramIntent);
        this.lynxModuleConfiguration.setMotors(editParameters.getCurrentItems());
        if (this.lynxModuleConfiguration.getMotors().size() == 4) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        Assert.assertTrue(bool1);
        if (((DeviceConfiguration)this.lynxModuleConfiguration.getMotors().get(0)).getPort() == 0) {
          bool1 = bool;
        } else {
          bool1 = false;
        } 
        Assert.assertTrue(bool1);
      } else if (requestCode == RequestCode.EDIT_SERVO_LIST) {
        editParameters = EditParameters.fromIntent(this, (Intent)editParameters);
        this.lynxModuleConfiguration.setServos(editParameters.getCurrentItems());
      } else if (requestCode == RequestCode.EDIT_ANALOG_INPUT) {
        editParameters = EditParameters.fromIntent(this, (Intent)editParameters);
        this.lynxModuleConfiguration.setAnalogInputs(editParameters.getCurrentItems());
      } else if (requestCode == RequestCode.EDIT_DIGITAL) {
        editParameters = EditParameters.fromIntent(this, (Intent)editParameters);
        this.lynxModuleConfiguration.setDigitalDevices(editParameters.getCurrentItems());
      } else {
        editParameters = EditParameters.fromIntent(this, (Intent)editParameters);
        if (requestCode == RequestCode.EDIT_I2C_BUS0) {
          this.lynxModuleConfiguration.setI2cDevices(0, editParameters.getCurrentItems());
        } else if (requestCode == RequestCode.EDIT_I2C_BUS1) {
          this.lynxModuleConfiguration.setI2cDevices(1, editParameters.getCurrentItems());
        } else if (requestCode == RequestCode.EDIT_I2C_BUS2) {
          this.lynxModuleConfiguration.setI2cDevices(2, editParameters.getCurrentItems());
        } else if (requestCode == RequestCode.EDIT_I2C_BUS3) {
          this.lynxModuleConfiguration.setI2cDevices(3, editParameters.getCurrentItems());
        } 
      } 
      this.currentCfgFile.markDirty();
      this.robotConfigFileManager.setActiveConfig(this.currentCfgFile);
    } 
  }
  
  public void onCancelButtonPressed(View paramView) {
    finishCancel();
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(R.layout.lynx_module);
    this.listKeys = EditActivity.DisplayNameAndRequestCode.fromArray(getResources().getStringArray(R.array.lynx_module_options_array));
    ListView listView = (ListView)findViewById(R.id.lynx_module_devices);
    listView.setAdapter((ListAdapter)new ArrayAdapter((Context)this, 17367043, (Object[])this.listKeys));
    listView.setOnItemClickListener(this.editLaunchListener);
    this.lynx_module_name = (EditText)findViewById(R.id.lynx_module_name);
    deserialize(EditParameters.fromIntent(this, getIntent()));
    this.lynxModuleConfiguration = (LynxModuleConfiguration)this.controllerConfiguration;
    this.lynx_module_name.addTextChangedListener(new EditActivity.SetNameTextWatcher(this, (DeviceConfiguration)this.lynxModuleConfiguration));
    this.lynx_module_name.setText(this.lynxModuleConfiguration.getName());
    RobotLog.vv("EditActivity", "lynxModuleConfiguration.getSerialNumber()=%s", new Object[] { this.lynxModuleConfiguration.getSerialNumber() });
    visuallyIdentify();
  }
  
  protected void onDestroy() {
    super.onDestroy();
    visuallyUnidentify();
  }
  
  public void onDoneButtonPressed(View paramView) {
    finishOk();
  }
  
  protected void sendIdentify(boolean paramBoolean) {
    sendOrInject(new Command("CMD_VISUALLY_IDENTIFY", (new CommandList.CmdVisuallyIdentify(this.lynxModuleConfiguration.getModuleSerialNumber(), paramBoolean)).serialize()));
  }
  
  protected void visuallyIdentify() {
    sendIdentify(true);
  }
  
  protected void visuallyUnidentify() {
    sendIdentify(false);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\EditLynxModuleActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
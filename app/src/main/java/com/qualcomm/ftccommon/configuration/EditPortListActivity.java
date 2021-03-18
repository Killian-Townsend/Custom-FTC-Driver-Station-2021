package com.qualcomm.ftccommon.configuration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.qualcomm.ftccommon.R;
import com.qualcomm.robotcore.hardware.configuration.BuiltInConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public abstract class EditPortListActivity<ITEM_T extends DeviceConfiguration> extends EditUSBDeviceActivity {
  public static final String TAG = "EditPortListActivity";
  
  protected EditText editTextBannerControllerName;
  
  protected int idBannerParent = R.id.bannerParent;
  
  protected int idControllerName = R.id.controller_name;
  
  protected int idControllerSerialNumber = R.id.serialNumber;
  
  protected int idItemEditTextResult;
  
  protected int idItemPortNumber;
  
  protected int idItemRowPort;
  
  protected int idListParentLayout;
  
  protected int initialPortNumber;
  
  protected Class<ITEM_T> itemClass;
  
  protected List<ITEM_T> itemList = new ArrayList<ITEM_T>();
  
  protected ArrayList<View> itemViews = new ArrayList<View>();
  
  protected int layoutControllerNameBanner = 0;
  
  protected int layoutItem;
  
  protected int layoutMain;
  
  protected TextView textViewSerialNumber;
  
  protected void addNameTextChangeWatcherOnIndex(int paramInt) {
    ((EditText)findViewByIndex(paramInt).findViewById(this.idItemEditTextResult)).addTextChangedListener(new EditActivity.SetNameTextWatcher(this, findConfigByIndex(paramInt)));
  }
  
  protected void addNewItem() {
    try {
      int i;
      if (this.itemList.isEmpty()) {
        i = this.initialPortNumber;
      } else {
        i = ((DeviceConfiguration)this.itemList.get(this.itemList.size() - 1)).getPort() + 1;
      } 
      int j = this.itemList.size();
      DeviceConfiguration deviceConfiguration = (DeviceConfiguration)this.itemClass.newInstance();
      deviceConfiguration.setPort(i);
      deviceConfiguration.setConfigurationType((ConfigurationType)BuiltInConfigurationType.NOTHING);
      this.itemList.add((ITEM_T)deviceConfiguration);
      this.itemViews.add(createItemViewForPort(i));
      addViewListenersOnIndex(j);
      return;
    } catch (IllegalAccessException illegalAccessException) {
    
    } catch (InstantiationException instantiationException) {}
    RobotLog.ee("EditPortListActivity", instantiationException, "exception thrown during addNewItem(); ignoring add");
  }
  
  protected void addViewListeners() {
    for (int i = 0; i < this.itemList.size(); i++)
      addViewListenersOnIndex(i); 
  }
  
  protected abstract void addViewListenersOnIndex(int paramInt);
  
  protected View createItemViewForPort(int paramInt) {
    LinearLayout linearLayout = (LinearLayout)findViewById(this.idListParentLayout);
    View view2 = getLayoutInflater().inflate(this.layoutItem, (ViewGroup)linearLayout, false);
    linearLayout.addView(view2);
    View view1 = view2.findViewById(this.idItemRowPort);
    TextView textView = (TextView)view1.findViewById(this.idItemPortNumber);
    if (textView != null)
      textView.setText(String.format(Locale.getDefault(), "%d", new Object[] { Integer.valueOf(paramInt) })); 
    return view1;
  }
  
  protected void createListViews(EditParameters<ITEM_T> paramEditParameters) {
    if (paramEditParameters != null) {
      this.itemList = paramEditParameters.getCurrentItems();
      this.itemClass = paramEditParameters.getItemClass();
      Collections.sort(this.itemList);
      for (int i = 0; i < this.itemList.size(); i++) {
        View view = createItemViewForPort(findConfigByIndex(i).getPort());
        this.itemViews.add(view);
      } 
    } 
  }
  
  protected DeviceConfiguration findConfigByIndex(int paramInt) {
    return (DeviceConfiguration)this.itemList.get(paramInt);
  }
  
  protected DeviceConfiguration findConfigByPort(int paramInt) {
    for (DeviceConfiguration deviceConfiguration : this.itemList) {
      if (deviceConfiguration.getPort() == paramInt)
        return deviceConfiguration; 
    } 
    return null;
  }
  
  protected View findViewByIndex(int paramInt) {
    return this.itemViews.get(paramInt);
  }
  
  protected void finishOk() {
    if (this.controllerConfiguration != null) {
      this.controllerConfiguration.setName(this.editTextBannerControllerName.getText().toString());
      finishOk(new EditParameters<DeviceConfiguration>(this, (DeviceConfiguration)this.controllerConfiguration, getRobotConfigMap()));
      return;
    } 
    finishOk(new EditParameters<ITEM_T>(this, this.itemClass, this.itemList));
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    if (paramInt2 == -1) {
      completeSwapConfiguration(paramInt1, paramInt2, paramIntent);
      this.currentCfgFile.markDirty();
      this.robotConfigFileManager.updateActiveConfigHeader(this.currentCfgFile);
    } 
  }
  
  public void onAddButtonPressed(View paramView) {
    addNewItem();
  }
  
  public void onCancelButtonPressed(View paramView) {
    finishCancel();
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(this.layoutMain);
    EditParameters<DeviceConfiguration> editParameters = EditParameters.fromIntent(this, getIntent());
    deserialize(editParameters);
    this.initialPortNumber = editParameters.getInitialPortNumber();
    showButton(this.idAddButton, editParameters.isGrowable());
    if (this.layoutControllerNameBanner != 0) {
      LinearLayout linearLayout = (LinearLayout)findViewById(this.idBannerParent);
      View view = getLayoutInflater().inflate(this.layoutControllerNameBanner, (ViewGroup)linearLayout, false);
      linearLayout.addView(view);
      this.editTextBannerControllerName = (EditText)view.findViewById(this.idControllerName);
      this.textViewSerialNumber = (TextView)view.findViewById(this.idControllerSerialNumber);
      this.editTextBannerControllerName.setText(this.controllerConfiguration.getName());
      showFixSwapButtons();
    } 
    createListViews((EditParameters)editParameters);
    addViewListeners();
  }
  
  public void onDoneButtonPressed(View paramView) {
    finishOk();
  }
  
  public void onFixButtonPressed(View paramView) {
    fixConfiguration();
  }
  
  protected void onStart() {
    super.onStart();
  }
  
  public void onSwapButtonPressed(View paramView) {
    swapConfiguration();
  }
  
  protected void refreshSerialNumber() {
    String str = formatSerialNumber((Context)this, this.controllerConfiguration);
    this.textViewSerialNumber.setText(str);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\EditPortListActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
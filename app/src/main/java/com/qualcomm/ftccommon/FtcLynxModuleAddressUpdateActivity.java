package com.qualcomm.ftccommon;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.qualcomm.ftccommon.configuration.EditActivity;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.USBAccessibleLynxModule;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.network.RecvLoopRunnable;
import org.firstinspires.ftc.robotcore.internal.network.RobotCoreCommandList;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.ui.UILocation;

public class FtcLynxModuleAddressUpdateActivity extends EditActivity {
  public static final String TAG = "FtcLynxModuleAddressUpdateActivity";
  
  protected BlockingQueue<RobotCoreCommandList.USBAccessibleLynxModulesResp> availableLynxModules = new ArrayBlockingQueue<RobotCoreCommandList.USBAccessibleLynxModulesResp>(1);
  
  protected List<USBAccessibleLynxModule> currentModules = new ArrayList<USBAccessibleLynxModule>();
  
  protected DisplayedModuleList displayedModuleList = new DisplayedModuleList();
  
  DialogInterface.OnClickListener doNothingAndCloseListener = new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface param1DialogInterface, int param1Int) {}
    };
  
  protected int msResponseWait = 10000;
  
  protected NetworkConnectionHandler networkConnectionHandler = NetworkConnectionHandler.getInstance();
  
  protected RecvLoopRunnable.RecvLoopCallback recvLoopCallback = (RecvLoopRunnable.RecvLoopCallback)new ReceiveLoopCallback();
  
  protected <T> T awaitResponse(BlockingQueue<T> paramBlockingQueue, T paramT) {
    return awaitResponse(paramBlockingQueue, paramT, this.msResponseWait, TimeUnit.MILLISECONDS);
  }
  
  protected <T> T awaitResponse(BlockingQueue<T> paramBlockingQueue, T paramT, long paramLong, TimeUnit paramTimeUnit) {
    try {
      paramBlockingQueue = (BlockingQueue<T>)paramBlockingQueue.poll(paramLong, paramTimeUnit);
      if (paramBlockingQueue != null)
        return (T)paramBlockingQueue; 
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
    } 
    return paramT;
  }
  
  protected void doBackOrCancel() {
    if (isDirty()) {
      DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            FtcLynxModuleAddressUpdateActivity.this.finishCancel();
          }
        };
      AlertDialog.Builder builder = this.utility.buildBuilder(getString(R.string.saveChangesTitle), getString(R.string.saveChangesMessageScreen));
      builder.setPositiveButton(R.string.buttonExitWithoutSaving, onClickListener);
      builder.setNegativeButton(R.string.buttonNameCancel, this.doNothingAndCloseListener);
      builder.show();
      return;
    } 
    finishCancel();
  }
  
  public String getTag() {
    return "FtcLynxModuleAddressUpdateActivity";
  }
  
  protected List<USBAccessibleLynxModule> getUSBAccessibleLynxModules() {
    RobotCoreCommandList.USBAccessibleLynxModulesRequest uSBAccessibleLynxModulesRequest = new RobotCoreCommandList.USBAccessibleLynxModulesRequest();
    RobotCoreCommandList.USBAccessibleLynxModulesResp uSBAccessibleLynxModulesResp2 = new RobotCoreCommandList.USBAccessibleLynxModulesResp();
    this.availableLynxModules.clear();
    uSBAccessibleLynxModulesRequest.forFirmwareUpdate = true;
    sendOrInject(new Command("CMD_GET_USB_ACCESSIBLE_LYNX_MODULES", uSBAccessibleLynxModulesRequest.serialize()));
    RobotCoreCommandList.USBAccessibleLynxModulesResp uSBAccessibleLynxModulesResp1 = awaitResponse(this.availableLynxModules, uSBAccessibleLynxModulesResp2);
    RobotLog.vv("FtcLynxModuleAddressUpdateActivity", "found %d lynx modules", new Object[] { Integer.valueOf(uSBAccessibleLynxModulesResp1.modules.size()) });
    return uSBAccessibleLynxModulesResp1.modules;
  }
  
  protected boolean isDirty() {
    for (USBAccessibleLynxModule uSBAccessibleLynxModule : this.currentModules) {
      DisplayedModule displayedModule = this.displayedModuleList.from(uSBAccessibleLynxModule.getSerialNumber());
      if (displayedModule.getStartingAddress() != displayedModule.getCurrentAddress())
        return true; 
    } 
    return false;
  }
  
  public void onBackPressed() {
    RobotLog.vv("FtcLynxModuleAddressUpdateActivity", "onBackPressed()");
    doBackOrCancel();
  }
  
  public void onCancelButtonPressed(View paramView) {
    RobotLog.vv("FtcLynxModuleAddressUpdateActivity", "onCancelButtonPressed()");
    doBackOrCancel();
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_ftc_lynx_address_update);
    this.networkConnectionHandler.pushReceiveLoopCallback(this.recvLoopCallback);
  }
  
  protected void onDestroy() {
    super.onDestroy();
    this.networkConnectionHandler.removeReceiveLoopCallback(this.recvLoopCallback);
  }
  
  public void onDoneButtonPressed(View paramView) {
    RobotLog.vv("FtcLynxModuleAddressUpdateActivity", "onDoneButtonPressed()");
    ArrayList<CommandList.LynxAddressChangeRequest.AddressChange> arrayList = new ArrayList();
    for (USBAccessibleLynxModule uSBAccessibleLynxModule : this.currentModules) {
      DisplayedModule displayedModule = this.displayedModuleList.from(uSBAccessibleLynxModule.getSerialNumber());
      if (displayedModule.getStartingAddress() != displayedModule.getCurrentAddress()) {
        CommandList.LynxAddressChangeRequest.AddressChange addressChange = new CommandList.LynxAddressChangeRequest.AddressChange();
        addressChange.serialNumber = displayedModule.getSerialNumber();
        addressChange.oldAddress = displayedModule.getStartingAddress();
        addressChange.newAddress = displayedModule.getCurrentAddress();
        arrayList.add(addressChange);
      } 
    } 
    if (this.currentModules.size() > 0)
      if (arrayList.size() > 0) {
        CommandList.LynxAddressChangeRequest lynxAddressChangeRequest = new CommandList.LynxAddressChangeRequest();
        lynxAddressChangeRequest.modulesToChange = arrayList;
        sendOrInject(new Command("CMD_LYNX_ADDRESS_CHANGE", lynxAddressChangeRequest.serialize()));
      } else {
        AppUtil.getInstance().showToast(UILocation.BOTH, getString(R.string.toastLynxAddressChangeNothingToDo));
      }  
    finishOk();
  }
  
  protected void onStart() {
    super.onStart();
    AppUtil.getInstance().showWaitCursor(getString(R.string.dialogMessagePleaseWait), new Runnable() {
          public void run() {
            FtcLynxModuleAddressUpdateActivity ftcLynxModuleAddressUpdateActivity = FtcLynxModuleAddressUpdateActivity.this;
            ftcLynxModuleAddressUpdateActivity.currentModules = ftcLynxModuleAddressUpdateActivity.getUSBAccessibleLynxModules();
            Iterator<USBAccessibleLynxModule> iterator = FtcLynxModuleAddressUpdateActivity.this.currentModules.iterator();
            while (iterator.hasNext()) {
              USBAccessibleLynxModule uSBAccessibleLynxModule = iterator.next();
              if (!uSBAccessibleLynxModule.isModuleAddressChangeable() && uSBAccessibleLynxModule.getSerialNumber().isEmbedded())
                iterator.remove(); 
            } 
          }
        },  new Runnable() {
          public void run() {
            FtcLynxModuleAddressUpdateActivity.this.displayedModuleList.initialize(FtcLynxModuleAddressUpdateActivity.this.currentModules);
            TextView textView = (TextView)FtcLynxModuleAddressUpdateActivity.this.findViewById(R.id.lynxAddressListInstructions);
            if (FtcLynxModuleAddressUpdateActivity.this.currentModules.isEmpty()) {
              textView.setText(FtcLynxModuleAddressUpdateActivity.this.getString(R.string.lynx_address_instructions_no_devices));
              return;
            } 
            textView.setText(FtcLynxModuleAddressUpdateActivity.this.getString(R.string.lynx_address_instructions_update));
          }
        });
  }
  
  protected class AddressAndDisplayName implements Comparable<AddressAndDisplayName> {
    public final int address;
    
    public final String displayName;
    
    public AddressAndDisplayName(int param1Int, boolean param1Boolean) {
      String str;
      this.address = param1Int;
      if (param1Int == 0) {
        if (param1Boolean) {
          param1Int = R.string.lynx_address_format_no_change;
        } else {
          param1Int = R.string.lynx_address_format_not_changeable;
        } 
        str = FtcLynxModuleAddressUpdateActivity.this.getString(param1Int);
      } else {
        str = str.getString(R.string.lynx_address_format_new_module_address, new Object[] { Integer.valueOf(param1Int) });
      } 
      this.displayName = str;
    }
    
    public int compareTo(AddressAndDisplayName param1AddressAndDisplayName) {
      return this.address - param1AddressAndDisplayName.address;
    }
    
    public String toString() {
      return this.displayName;
    }
  }
  
  protected class AddressConfiguration {
    protected Map<SerialNumber, Integer> current = new ConcurrentHashMap<SerialNumber, Integer>();
    
    protected Map<SerialNumber, Integer> starting = new ConcurrentHashMap<SerialNumber, Integer>();
    
    public AddressConfiguration() {}
    
    public AddressConfiguration(List<USBAccessibleLynxModule> param1List) {
      for (USBAccessibleLynxModule uSBAccessibleLynxModule : param1List) {
        this.starting.put(uSBAccessibleLynxModule.getSerialNumber(), Integer.valueOf(uSBAccessibleLynxModule.getModuleAddress()));
        this.current.put(uSBAccessibleLynxModule.getSerialNumber(), Integer.valueOf(uSBAccessibleLynxModule.getModuleAddress()));
      } 
    }
    
    public boolean containsCurrentAddress(int param1Int) {
      return this.current.values().contains(Integer.valueOf(param1Int));
    }
    
    public SerialNumber findByCurrentAddress(int param1Int) {
      for (Map.Entry<SerialNumber, Integer> entry : this.current.entrySet()) {
        if (((Integer)entry.getValue()).equals(Integer.valueOf(param1Int)))
          return (SerialNumber)entry.getKey(); 
      } 
      return null;
    }
    
    public int getCurrentAddress(SerialNumber param1SerialNumber) {
      return ((Integer)this.current.get(param1SerialNumber)).intValue();
    }
    
    public int getStartingAddress(SerialNumber param1SerialNumber) {
      return ((Integer)this.starting.get(param1SerialNumber)).intValue();
    }
    
    public void putCurrentAddress(SerialNumber param1SerialNumber, int param1Int) {
      this.current.put(param1SerialNumber, Integer.valueOf(param1Int));
    }
  }
  
  protected class DisplayedModule {
    Spinner spinner;
    
    View view;
    
    public DisplayedModule(View param1View) {
      this.view = param1View;
      this.spinner = (Spinner)param1View.findViewById(R.id.spinnerChooseAddress);
    }
    
    public int getCurrentAddress() {
      return FtcLynxModuleAddressUpdateActivity.this.displayedModuleList.currentAddressConfiguration.getCurrentAddress(getSerialNumber());
    }
    
    protected FtcLynxModuleAddressUpdateActivity.AddressAndDisplayName getItem(int param1Int) {
      return (FtcLynxModuleAddressUpdateActivity.AddressAndDisplayName)this.spinner.getAdapter().getItem(param1Int);
    }
    
    public SerialNumber getSerialNumber() {
      return (SerialNumber)((TextView)this.view.findViewById(R.id.moduleSerialText)).getTag();
    }
    
    public int getStartingAddress() {
      return FtcLynxModuleAddressUpdateActivity.this.displayedModuleList.currentAddressConfiguration.getStartingAddress(getSerialNumber());
    }
    
    public void initialize(USBAccessibleLynxModule param1USBAccessibleLynxModule, List<Integer> param1List) {
      TextView textView = (TextView)this.view.findViewById(R.id.moduleSerialText);
      textView.setText(param1USBAccessibleLynxModule.getSerialNumber().toString());
      textView.setTag(param1USBAccessibleLynxModule.getSerialNumber());
      ((TextView)this.view.findViewById(R.id.moduleAddressText)).setText(FtcLynxModuleAddressUpdateActivity.this.getString(R.string.lynx_address_format_module_address, new Object[] { Integer.valueOf(param1USBAccessibleLynxModule.getModuleAddress()) }));
      boolean bool = param1USBAccessibleLynxModule.isModuleAddressChangeable();
      this.spinner.setEnabled(bool);
      initializeSpinnerList(this.spinner, param1List, bool);
      this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> param2AdapterView, View param2View, int param2Int, long param2Long) {
              int i = ((FtcLynxModuleAddressUpdateActivity.AddressAndDisplayName)param2AdapterView.getItemAtPosition(param2Int)).address;
              if (i == FtcLynxModuleAddressUpdateActivity.DisplayedModule.this.getStartingAddress()) {
                FtcLynxModuleAddressUpdateActivity.DisplayedModule.this.selectNoChange();
                param2Int = i;
              } else {
                param2Int = i;
                if (i == 0)
                  param2Int = FtcLynxModuleAddressUpdateActivity.DisplayedModule.this.getStartingAddress(); 
              } 
              FtcLynxModuleAddressUpdateActivity.this.displayedModuleList.changeAddress(FtcLynxModuleAddressUpdateActivity.DisplayedModule.this.getSerialNumber(), param2Int);
            }
            
            public void onNothingSelected(AdapterView<?> param2AdapterView) {}
          });
    }
    
    protected void initializeSpinnerList(Spinner param1Spinner, List<Integer> param1List, boolean param1Boolean) {
      FtcLynxModuleAddressUpdateActivity.AddressAndDisplayName[] arrayOfAddressAndDisplayName = new FtcLynxModuleAddressUpdateActivity.AddressAndDisplayName[param1List.size()];
      int i;
      for (i = 0; i < param1List.size(); i++)
        arrayOfAddressAndDisplayName[i] = new FtcLynxModuleAddressUpdateActivity.AddressAndDisplayName(((Integer)param1List.get(i)).intValue(), param1Boolean); 
      Arrays.sort((Object[])arrayOfAddressAndDisplayName);
      param1Spinner.setAdapter((SpinnerAdapter)new ArrayAdapter((Context)FtcLynxModuleAddressUpdateActivity.this, R.layout.lynx_module_configure_address_spin_item, (Object[])arrayOfAddressAndDisplayName));
    }
    
    protected void selectNoChange() {
      RobotLog.vv("FtcLynxModuleAddressUpdateActivity", "selectNoChange(%s)", new Object[] { getSerialNumber() });
      this.spinner.setSelection(0);
    }
    
    public void setNewAddress(int param1Int) {
      SerialNumber serialNumber = getSerialNumber();
      int i = 0;
      RobotLog.vv("FtcLynxModuleAddressUpdateActivity", "setNewAddress(%s)=%d", new Object[] { serialNumber, Integer.valueOf(param1Int) });
      if (param1Int == getStartingAddress()) {
        selectNoChange();
        return;
      } 
      while (i < this.spinner.getAdapter().getCount()) {
        if ((getItem(i)).address == param1Int) {
          this.spinner.setSelection(i);
          return;
        } 
        i++;
      } 
    }
  }
  
  class null implements AdapterView.OnItemSelectedListener {
    public void onItemSelected(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
      int i = ((FtcLynxModuleAddressUpdateActivity.AddressAndDisplayName)param1AdapterView.getItemAtPosition(param1Int)).address;
      if (i == this.this$1.getStartingAddress()) {
        this.this$1.selectNoChange();
        param1Int = i;
      } else {
        param1Int = i;
        if (i == 0)
          param1Int = this.this$1.getStartingAddress(); 
      } 
      FtcLynxModuleAddressUpdateActivity.this.displayedModuleList.changeAddress(this.this$1.getSerialNumber(), param1Int);
    }
    
    public void onNothingSelected(AdapterView<?> param1AdapterView) {}
  }
  
  protected class DisplayedModuleList {
    protected FtcLynxModuleAddressUpdateActivity.AddressConfiguration currentAddressConfiguration = new FtcLynxModuleAddressUpdateActivity.AddressConfiguration();
    
    protected int lastModuleAddressChoice = 10;
    
    protected ViewGroup moduleList;
    
    protected void add(USBAccessibleLynxModule param1USBAccessibleLynxModule, List<Integer> param1List) {
      View view = LayoutInflater.from(FtcLynxModuleAddressUpdateActivity.this.context).inflate(R.layout.lynx_module_configure_address, null);
      this.moduleList.addView(view);
      (new FtcLynxModuleAddressUpdateActivity.DisplayedModule(view)).initialize(param1USBAccessibleLynxModule, param1List);
    }
    
    public void changeAddress(SerialNumber param1SerialNumber, int param1Int) {
      boolean bool = false;
      RobotLog.vv("FtcLynxModuleAddressUpdateActivity", "changeAddress(%s) from:%d to:%d", new Object[] { param1SerialNumber, Integer.valueOf(this.currentAddressConfiguration.getCurrentAddress(param1SerialNumber)), Integer.valueOf(param1Int) });
      if (this.currentAddressConfiguration.getCurrentAddress(param1SerialNumber) != param1Int) {
        SerialNumber serialNumber = this.currentAddressConfiguration.findByCurrentAddress(param1Int);
        this.currentAddressConfiguration.putCurrentAddress(param1SerialNumber, param1Int);
        if (serialNumber != null) {
          param1Int = findUnusedAddress();
          RobotLog.vv("FtcLynxModuleAddressUpdateActivity", "conflict with %s: that goes to %d", new Object[] { serialNumber, Integer.valueOf(param1Int) });
          if (param1Int != 0)
            bool = true; 
          Assert.assertTrue(bool);
          this.currentAddressConfiguration.putCurrentAddress(serialNumber, param1Int);
          from(serialNumber).setNewAddress(param1Int);
        } 
      } 
    }
    
    protected int findUnusedAddress() {
      for (int i = 1; i <= this.lastModuleAddressChoice; i++) {
        if (!this.currentAddressConfiguration.containsCurrentAddress(i))
          return i; 
      } 
      return 0;
    }
    
    protected FtcLynxModuleAddressUpdateActivity.DisplayedModule from(SerialNumber param1SerialNumber) {
      ViewGroup viewGroup = (ViewGroup)FtcLynxModuleAddressUpdateActivity.this.findViewById(R.id.moduleList);
      for (int i = 0; i < viewGroup.getChildCount(); i++) {
        FtcLynxModuleAddressUpdateActivity.DisplayedModule displayedModule = new FtcLynxModuleAddressUpdateActivity.DisplayedModule(viewGroup.getChildAt(i));
        if (displayedModule.getSerialNumber().equals(param1SerialNumber))
          return displayedModule; 
      } 
      return null;
    }
    
    public void initialize(List<USBAccessibleLynxModule> param1List) {
      ViewGroup viewGroup = (ViewGroup)FtcLynxModuleAddressUpdateActivity.this.findViewById(R.id.moduleList);
      this.moduleList = viewGroup;
      viewGroup.removeAllViews();
      ArrayList<Integer> arrayList = new ArrayList();
      for (int i = 0; i <= this.lastModuleAddressChoice; i++)
        arrayList.add(Integer.valueOf(i)); 
      for (USBAccessibleLynxModule uSBAccessibleLynxModule : param1List) {
        if (!uSBAccessibleLynxModule.isModuleAddressChangeable())
          arrayList.remove(Integer.valueOf(uSBAccessibleLynxModule.getModuleAddress())); 
      } 
      for (USBAccessibleLynxModule uSBAccessibleLynxModule : param1List) {
        boolean bool;
        if (uSBAccessibleLynxModule.getModuleAddress() != 0) {
          bool = true;
        } else {
          bool = false;
        } 
        Assert.assertTrue(bool);
        if (size() + 1 >= arrayList.size() - 1)
          break; 
        add(uSBAccessibleLynxModule, arrayList);
      } 
      this.currentAddressConfiguration = new FtcLynxModuleAddressUpdateActivity.AddressConfiguration(param1List);
    }
    
    protected int size() {
      return this.moduleList.getChildCount();
    }
  }
  
  protected class ReceiveLoopCallback extends RecvLoopRunnable.DegenerateCallback {
    public CallbackResult commandEvent(Command param1Command) throws RobotCoreException {
      byte b;
      String str = param1Command.getName();
      if (str.hashCode() == 1474679152 && str.equals("CMD_GET_USB_ACCESSIBLE_LYNX_MODULES_RESP")) {
        b = 0;
      } else {
        b = -1;
      } 
      if (b != 0)
        return super.commandEvent(param1Command); 
      RobotCoreCommandList.USBAccessibleLynxModulesResp uSBAccessibleLynxModulesResp = RobotCoreCommandList.USBAccessibleLynxModulesResp.deserialize(param1Command.getExtra());
      FtcLynxModuleAddressUpdateActivity.this.availableLynxModules.offer(uSBAccessibleLynxModulesResp);
      return CallbackResult.HANDLED_CONTINUE;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\FtcLynxModuleAddressUpdateActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
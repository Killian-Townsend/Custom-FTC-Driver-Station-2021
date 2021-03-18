package com.qualcomm.ftcdriverstation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.qualcomm.hardware.logitech.LogitechGamepadF310;
import com.qualcomm.hardware.microsoft.MicrosoftGamepadXbox360;
import com.qualcomm.hardware.sony.SonyGamepadPS4;
import com.qualcomm.robotcore.hardware.Gamepad;
import java.util.ArrayList;
import java.util.Iterator;

public class EditGamepadTypeOverridesActivity extends Activity {
  boolean changesMade = false;
  
  KeyEventCapturingProgressDialog detectionDialog;
  
  ArrayList<GamepadTypeOverrideMapper.GamepadTypeOverrideEntry> entries = new ArrayList<GamepadTypeOverrideMapper.GamepadTypeOverrideEntry>();
  
  ListView listView;
  
  GamepadTypeOverrideMapper mapper;
  
  GamepadOverrideEntryAdapter overrideEntryAdapter;
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
    if (paramKeyEvent.getKeyCode() == 4 && paramKeyEvent.getAction() == 0) {
      handleCancelClicked((View)null);
      return true;
    } 
    return super.dispatchKeyEvent(paramKeyEvent);
  }
  
  public void handleAddEntryClicked(View paramView) {
    KeyEventCapturingProgressDialog keyEventCapturingProgressDialog = new KeyEventCapturingProgressDialog((Context)this);
    this.detectionDialog = keyEventCapturingProgressDialog;
    keyEventCapturingProgressDialog.setTitle("Gamepad identification");
    this.detectionDialog.setMessage("Please press any key on the gamepad.");
    this.detectionDialog.setCancelable(false);
    this.detectionDialog.setProgressStyle(0);
    this.detectionDialog.setButton(-2, "Abort", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            Toast.makeText((Context)EditGamepadTypeOverridesActivity.this, "Aborted gamepad detection", 0).show();
          }
        });
    this.detectionDialog.setListener(new KeyEventCapturingProgressDialog.Listener() {
          public void onKeyDown(KeyEvent param1KeyEvent) {
            if (Gamepad.isGamepadDevice(param1KeyEvent.getDeviceId()) && EditGamepadTypeOverridesActivity.this.detectionDialog != null) {
              if (!EditGamepadTypeOverridesActivity.this.detectionDialog.isShowing())
                return; 
              EditGamepadTypeOverridesActivity.this.detectionDialog.dismiss();
              InputDevice inputDevice = InputDevice.getDevice(param1KeyEvent.getDeviceId());
              int i = inputDevice.getVendorId();
              int j = inputDevice.getProductId();
              Iterator<GamepadTypeOverrideMapper.GamepadTypeOverrideEntry> iterator = EditGamepadTypeOverridesActivity.this.entries.iterator();
              while (iterator.hasNext()) {
                if (((GamepadTypeOverrideMapper.GamepadTypeOverrideEntry)iterator.next()).usbIdsMatch(i, j)) {
                  EditGamepadTypeOverridesActivity.this.showEntryAlreadyExistsDialog(i, j);
                  return;
                } 
              } 
              if (LogitechGamepadF310.matchesVidPid(i, j) || MicrosoftGamepadXbox360.matchesVidPid(i, j) || SonyGamepadPS4.matchesVidPid(i, j)) {
                EditGamepadTypeOverridesActivity.this.showOfficiallySupportedDialog(i, j);
                return;
              } 
              EditGamepadTypeOverridesActivity.this.showGamepadTypeChooserDialog(i, j);
              return;
            } 
          }
        });
    this.detectionDialog.show();
  }
  
  public void handleCancelClicked(View paramView) {
    if (!this.changesMade) {
      finish();
      return;
    } 
    AlertDialog.Builder builder = new AlertDialog.Builder((Context)this);
    builder.setCancelable(false);
    builder.setTitle("Discard changes?");
    builder.setMessage("Discard changes and exit?");
    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {}
        });
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            EditGamepadTypeOverridesActivity.this.finish();
          }
        });
    builder.show();
  }
  
  public void handleSaveClicked(View paramView) {
    if (!this.changesMade) {
      finish();
      return;
    } 
    AlertDialog.Builder builder = new AlertDialog.Builder((Context)this);
    builder.setCancelable(false);
    builder.setTitle("Save changes?");
    builder.setMessage("Save changes and exit?");
    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {}
        });
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            EditGamepadTypeOverridesActivity.this.mapper.setEntries(EditGamepadTypeOverridesActivity.this.entries);
            EditGamepadTypeOverridesActivity.this.finish();
          }
        });
    builder.show();
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131427362);
    GamepadTypeOverrideMapper gamepadTypeOverrideMapper = new GamepadTypeOverrideMapper((Context)this);
    this.mapper = gamepadTypeOverrideMapper;
    this.entries.addAll(gamepadTypeOverrideMapper.getEntries());
    this.listView = (ListView)findViewById(2131231017);
    GamepadOverrideEntryAdapter gamepadOverrideEntryAdapter = new GamepadOverrideEntryAdapter(this, 17367044, this.entries);
    this.overrideEntryAdapter = gamepadOverrideEntryAdapter;
    this.listView.setAdapter(gamepadOverrideEntryAdapter);
    this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {}
        });
    this.listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
          public boolean onItemLongClick(AdapterView<?> param1AdapterView, View param1View, final int position, long param1Long) {
            AlertDialog.Builder builder = new AlertDialog.Builder((Context)EditGamepadTypeOverridesActivity.this);
            GamepadTypeOverrideMapper.GamepadTypeOverrideEntry gamepadTypeOverrideEntry = EditGamepadTypeOverridesActivity.this.entries.get(position);
            builder.setTitle("Delete entry?");
            builder.setMessage(String.format("Delete entry which maps %d:%d to %s?", new Object[] { Integer.valueOf(gamepadTypeOverrideEntry.vid), Integer.valueOf(gamepadTypeOverrideEntry.pid), gamepadTypeOverrideEntry.mappedType.toString() }));
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface param2DialogInterface, int param2Int) {
                    EditGamepadTypeOverridesActivity.this.entries.remove(position);
                    EditGamepadTypeOverridesActivity.this.overrideEntryAdapter.notifyDataSetChanged();
                    EditGamepadTypeOverridesActivity.this.changesMade = true;
                  }
                });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface param2DialogInterface, int param2Int) {}
                });
            builder.show();
            return false;
          }
        });
  }
  
  public void showEntryAlreadyExistsDialog(int paramInt1, int paramInt2) {
    AlertDialog.Builder builder = new AlertDialog.Builder((Context)this);
    builder.setCancelable(false);
    builder.setTitle("Already Exists");
    builder.setMessage(String.format("An entry which maps %d:%d already exits. If you'd like to change the mapping target, please delete the current entry first.", new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) }));
    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {}
        });
    builder.show();
  }
  
  public void showGamepadTypeChooserDialog(final int vid, final int pid) {
    SelectGamepadMappingDialog selectGamepadMappingDialog = new SelectGamepadMappingDialog((Context)this);
    selectGamepadMappingDialog.setListener(new SelectGamepadMappingDialog.Listener() {
          public void onOk(Gamepad.Type param1Type) {
            EditGamepadTypeOverridesActivity.this.entries.add(new GamepadTypeOverrideMapper.GamepadTypeOverrideEntry(vid, pid, param1Type));
            EditGamepadTypeOverridesActivity.this.overrideEntryAdapter.notifyDataSetChanged();
            EditGamepadTypeOverridesActivity.this.changesMade = true;
          }
        });
    selectGamepadMappingDialog.show();
  }
  
  public void showOfficiallySupportedDialog(int paramInt1, int paramInt2) {
    AlertDialog.Builder builder = new AlertDialog.Builder((Context)this);
    builder.setCancelable(false);
    builder.setTitle("Officially Supported");
    builder.setMessage(String.format("The USB identifier %d:%d is that of an officially supported gamepad. Overrides are not allowed for officially supported gamepads.", new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) }));
    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {}
        });
    builder.show();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftcdriverstation\EditGamepadTypeOverridesActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
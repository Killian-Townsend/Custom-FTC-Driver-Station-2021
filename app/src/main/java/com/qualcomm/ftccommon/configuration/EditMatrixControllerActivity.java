package com.qualcomm.ftccommon.configuration;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.qualcomm.ftccommon.R;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MatrixControllerConfiguration;
import java.util.List;

public class EditMatrixControllerActivity extends EditActivity {
  public static final RequestCode requestCode = RequestCode.EDIT_MATRIX_CONTROLLER;
  
  private EditText controller_name;
  
  private View info_port1;
  
  private View info_port2;
  
  private View info_port3;
  
  private View info_port4;
  
  private View info_port5;
  
  private View info_port6;
  
  private View info_port7;
  
  private View info_port8;
  
  private MatrixControllerConfiguration matrixControllerConfigurationConfig;
  
  private List<DeviceConfiguration> motors;
  
  private List<DeviceConfiguration> servos;
  
  private void addCheckBoxListenerOnPort(int paramInt, View paramView, List<? extends DeviceConfiguration> paramList) {
    final EditText name = (EditText)paramView.findViewById(R.id.editTextResult);
    final DeviceConfiguration device = paramList.get(paramInt - 1);
    ((CheckBox)paramView.findViewById(R.id.checkbox_port)).setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (((CheckBox)param1View).isChecked()) {
              name.setEnabled(true);
              name.setText("");
              device.setEnabled(true);
              device.setName("");
              return;
            } 
            name.setEnabled(false);
            device.setEnabled(false);
            device.setName(EditMatrixControllerActivity.this.disabledDeviceName());
            name.setText(EditMatrixControllerActivity.this.disabledDeviceName());
          }
        });
  }
  
  private void addNameTextChangeWatcherOnPort(View paramView, DeviceConfiguration paramDeviceConfiguration) {
    ((EditText)paramView.findViewById(R.id.editTextResult)).addTextChangedListener(new UsefulTextWatcher(paramDeviceConfiguration));
  }
  
  private View findMotorViewByPort(int paramInt) {
    return (paramInt != 1) ? ((paramInt != 2) ? ((paramInt != 3) ? ((paramInt != 4) ? null : this.info_port8) : this.info_port7) : this.info_port6) : this.info_port5;
  }
  
  private View findServoViewByPort(int paramInt) {
    return (paramInt != 1) ? ((paramInt != 2) ? ((paramInt != 3) ? ((paramInt != 4) ? null : this.info_port4) : this.info_port3) : this.info_port2) : this.info_port1;
  }
  
  private void handleDisabledDevice(int paramInt, View paramView, List<? extends DeviceConfiguration> paramList) {
    CheckBox checkBox = (CheckBox)paramView.findViewById(R.id.checkbox_port);
    DeviceConfiguration deviceConfiguration = paramList.get(paramInt - 1);
    if (deviceConfiguration.isEnabled()) {
      checkBox.setChecked(true);
      ((EditText)paramView.findViewById(R.id.editTextResult)).setText(deviceConfiguration.getName());
      return;
    } 
    checkBox.setChecked(true);
    checkBox.performClick();
  }
  
  protected void finishOk() {
    this.matrixControllerConfigurationConfig.setServos(this.servos);
    this.matrixControllerConfigurationConfig.setMotors(this.motors);
    this.matrixControllerConfigurationConfig.setName(this.controller_name.getText().toString());
    finishOk(new EditParameters<DeviceConfiguration>(this, (DeviceConfiguration)this.matrixControllerConfigurationConfig));
  }
  
  public String getTag() {
    return getClass().getSimpleName();
  }
  
  public void onCancelButtonPressed(View paramView) {
    finishCancel();
  }
  
  protected void onCreate(Bundle paramBundle) {
    int j;
    super.onCreate(paramBundle);
    setContentView(R.layout.matrices);
    this.controller_name = (EditText)findViewById(R.id.matrixcontroller_name);
    LinearLayout linearLayout8 = (LinearLayout)findViewById(R.id.linearLayout_matrix1);
    View view8 = getLayoutInflater().inflate(R.layout.matrix_devices, (ViewGroup)linearLayout8, true);
    this.info_port1 = view8;
    ((TextView)view8.findViewById(R.id.port_number)).setText("1");
    LinearLayout linearLayout7 = (LinearLayout)findViewById(R.id.linearLayout_matrix2);
    View view7 = getLayoutInflater().inflate(R.layout.matrix_devices, (ViewGroup)linearLayout7, true);
    this.info_port2 = view7;
    ((TextView)view7.findViewById(R.id.port_number)).setText("2");
    LinearLayout linearLayout6 = (LinearLayout)findViewById(R.id.linearLayout_matrix3);
    View view6 = getLayoutInflater().inflate(R.layout.matrix_devices, (ViewGroup)linearLayout6, true);
    this.info_port3 = view6;
    ((TextView)view6.findViewById(R.id.port_number)).setText("3");
    LinearLayout linearLayout5 = (LinearLayout)findViewById(R.id.linearLayout_matrix4);
    View view5 = getLayoutInflater().inflate(R.layout.matrix_devices, (ViewGroup)linearLayout5, true);
    this.info_port4 = view5;
    ((TextView)view5.findViewById(R.id.port_number)).setText("4");
    LinearLayout linearLayout4 = (LinearLayout)findViewById(R.id.linearLayout_matrix5);
    View view4 = getLayoutInflater().inflate(R.layout.matrix_devices, (ViewGroup)linearLayout4, true);
    this.info_port5 = view4;
    ((TextView)view4.findViewById(R.id.port_number)).setText("1");
    LinearLayout linearLayout3 = (LinearLayout)findViewById(R.id.linearLayout_matrix6);
    View view3 = getLayoutInflater().inflate(R.layout.matrix_devices, (ViewGroup)linearLayout3, true);
    this.info_port6 = view3;
    ((TextView)view3.findViewById(R.id.port_number)).setText("2");
    LinearLayout linearLayout2 = (LinearLayout)findViewById(R.id.linearLayout_matrix7);
    View view2 = getLayoutInflater().inflate(R.layout.matrix_devices, (ViewGroup)linearLayout2, true);
    this.info_port7 = view2;
    ((TextView)view2.findViewById(R.id.port_number)).setText("3");
    LinearLayout linearLayout1 = (LinearLayout)findViewById(R.id.linearLayout_matrix8);
    View view1 = getLayoutInflater().inflate(R.layout.matrix_devices, (ViewGroup)linearLayout1, true);
    this.info_port8 = view1;
    ((TextView)view1.findViewById(R.id.port_number)).setText("4");
    EditParameters<DeviceConfiguration> editParameters = EditParameters.fromIntent(this, getIntent());
    if (editParameters != null) {
      MatrixControllerConfiguration matrixControllerConfiguration = (MatrixControllerConfiguration)editParameters.getConfiguration();
      this.matrixControllerConfigurationConfig = matrixControllerConfiguration;
      this.motors = matrixControllerConfiguration.getMotors();
      this.servos = this.matrixControllerConfigurationConfig.getServos();
    } 
    this.controller_name.setText(this.matrixControllerConfigurationConfig.getName());
    byte b = 0;
    int i = 0;
    while (true) {
      j = b;
      if (i < this.motors.size()) {
        j = i + 1;
        View view = findMotorViewByPort(j);
        addCheckBoxListenerOnPort(j, view, this.motors);
        addNameTextChangeWatcherOnPort(view, this.motors.get(i));
        handleDisabledDevice(j, view, this.motors);
        i = j;
        continue;
      } 
      break;
    } 
    while (j < this.servos.size()) {
      i = j + 1;
      View view = findServoViewByPort(i);
      addCheckBoxListenerOnPort(i, view, this.servos);
      addNameTextChangeWatcherOnPort(view, this.servos.get(j));
      handleDisabledDevice(i, view, this.servos);
      j = i;
    } 
  }
  
  public void onDoneButtonPressed(View paramView) {
    finishOk();
  }
  
  protected void onStart() {
    super.onStart();
  }
  
  private class UsefulTextWatcher implements TextWatcher {
    private DeviceConfiguration module;
    
    private UsefulTextWatcher(DeviceConfiguration param1DeviceConfiguration) {
      this.module = param1DeviceConfiguration;
    }
    
    public void afterTextChanged(Editable param1Editable) {
      String str = param1Editable.toString();
      this.module.setName(str);
    }
    
    public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
    
    public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\EditMatrixControllerActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotcore.internal.ui;

import android.os.Handler;
import android.view.InputDevice;
import android.view.InputEvent;
import com.qualcomm.robotcore.util.ClassUtil;
import java.lang.reflect.Method;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class InputManager {
  public static final int INJECT_INPUT_EVENT_MODE_ASYNC = 0;
  
  public static final int INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH = 2;
  
  public static final int INJECT_INPUT_EVENT_MODE_WAIT_FOR_RESULT = 1;
  
  protected static InputManager theInstance = new InputManager();
  
  protected android.hardware.input.InputManager inputManager = (android.hardware.input.InputManager)AppUtil.getInstance().getActivity().getSystemService("input");
  
  protected Method methodInjectInputEvent;
  
  protected InputManager() {
    try {
      this.methodInjectInputEvent = android.hardware.input.InputManager.class.getMethod("injectInputEvent", new Class[] { InputEvent.class, int.class });
      return;
    } catch (NoSuchMethodException noSuchMethodException) {
      return;
    } 
  }
  
  public static InputManager getInstance() {
    return theInstance;
  }
  
  public InputDevice getInputDevice(int paramInt) {
    return this.inputManager.getInputDevice(paramInt);
  }
  
  public int[] getInputDeviceIds() {
    return this.inputManager.getInputDeviceIds();
  }
  
  public boolean injectInputEvent(InputEvent paramInputEvent, int paramInt) {
    return ((Boolean)ClassUtil.invoke(this.inputManager, this.methodInjectInputEvent, new Object[] { paramInputEvent, Integer.valueOf(paramInt) })).booleanValue();
  }
  
  public void registerInputDeviceListener(android.hardware.input.InputManager.InputDeviceListener paramInputDeviceListener, Handler paramHandler) {
    this.inputManager.registerInputDeviceListener(paramInputDeviceListener, paramHandler);
  }
  
  public void unregisterInputDeviceListener(android.hardware.input.InputManager.InputDeviceListener paramInputDeviceListener) {
    this.inputManager.unregisterInputDeviceListener(paramInputDeviceListener);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\interna\\ui\InputManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
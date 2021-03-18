package com.qualcomm.hardware.lynx.commands;

import com.qualcomm.hardware.lynx.LynxModule;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.firstinspires.ftc.robotcore.internal.system.Assert;

public class LynxInterface {
  public static final int ERRONEOUS_COMMAND_NUMBER = 0;
  
  public static final int ERRONEOUS_INDEX = 0;
  
  private Integer baseCommandNumber = Integer.valueOf(0);
  
  private Map<Class<? extends LynxInterfaceCommand>, Integer> commandIndices;
  
  private Class<? extends LynxInterfaceCommand>[] commands;
  
  private String interfaceName;
  
  private Map<Class<? extends LynxInterfaceResponse>, Integer> responseIndices;
  
  private boolean wasNacked;
  
  public LynxInterface(String paramString, Class<? extends LynxInterfaceCommand>... paramVarArgs) {
    this.interfaceName = paramString;
    this.commands = paramVarArgs;
    this.commandIndices = new HashMap<Class<? extends LynxInterfaceCommand>, Integer>();
    this.responseIndices = new HashMap<Class<? extends LynxInterfaceResponse>, Integer>();
    this.wasNacked = false;
    int i = 0;
    while (true) {
      Class<? extends LynxInterfaceCommand>[] arrayOfClass = this.commands;
      if (i < arrayOfClass.length) {
        Class<? extends LynxInterfaceCommand> clazz = arrayOfClass[i];
        if (clazz != null) {
          this.commandIndices.put(clazz, Integer.valueOf(i));
          try {
            boolean bool;
            Class<? extends LynxResponse> clazz1 = LynxCommand.getResponseClass(clazz);
            if (clazz1 != null) {
              bool = true;
            } else {
              bool = false;
            } 
            Assert.assertTrue(bool);
            this.responseIndices.put(clazz1, Integer.valueOf(i));
            LynxModule.correlateResponse(clazz, clazz1);
          } catch (Exception exception) {}
        } 
        i++;
        continue;
      } 
      return;
    } 
  }
  
  public Integer getBaseCommandNumber() {
    return this.baseCommandNumber;
  }
  
  public List<Class<? extends LynxInterfaceCommand>> getCommandClasses() {
    return Arrays.asList(this.commands);
  }
  
  public int getCommandCount() {
    return this.commands.length;
  }
  
  public int getCommandIndex(Class<? extends LynxInterfaceCommand> paramClass) {
    return ((Integer)this.commandIndices.get(paramClass)).intValue();
  }
  
  public String getInterfaceName() {
    return this.interfaceName;
  }
  
  public int getResponseIndex(Class<? extends LynxInterfaceResponse> paramClass) {
    return ((Integer)this.responseIndices.get(paramClass)).intValue();
  }
  
  public void setBaseCommandNumber(Integer paramInteger) {
    this.baseCommandNumber = paramInteger;
  }
  
  public void setWasNacked(boolean paramBoolean) {
    this.wasNacked = paramBoolean;
  }
  
  public boolean wasNacked() {
    return this.wasNacked;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\LynxInterface.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
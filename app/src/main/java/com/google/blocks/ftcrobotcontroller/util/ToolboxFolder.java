package com.google.blocks.ftcrobotcontroller.util;

public enum ToolboxFolder {
  ACTUATORS("Actuators"),
  OTHER("Actuators"),
  SENSORS("Sensors");
  
  public final String label;
  
  static {
    ToolboxFolder toolboxFolder = new ToolboxFolder("OTHER", 2, "Other Devices");
    OTHER = toolboxFolder;
    $VALUES = new ToolboxFolder[] { ACTUATORS, SENSORS, toolboxFolder };
  }
  
  ToolboxFolder(String paramString1) {
    this.label = paramString1;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontrolle\\util\ToolboxFolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
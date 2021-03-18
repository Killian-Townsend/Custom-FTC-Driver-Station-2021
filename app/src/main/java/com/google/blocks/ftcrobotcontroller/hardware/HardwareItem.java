package com.google.blocks.ftcrobotcontroller.hardware;

public class HardwareItem {
  public final String deviceName;
  
  public final HardwareType hardwareType;
  
  public final String identifier;
  
  public final HardwareItem parent;
  
  public final String visibleName;
  
  public HardwareItem(HardwareItem paramHardwareItem, HardwareType paramHardwareType, String paramString) {
    if (paramHardwareType != null && paramString != null) {
      this.parent = paramHardwareItem;
      this.hardwareType = paramHardwareType;
      this.deviceName = paramString;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(makeIdentifier(paramString));
      stringBuilder.append(paramHardwareType.identifierSuffixForJavaScript);
      this.identifier = stringBuilder.toString();
      this.visibleName = HardwareUtil.makeVisibleNameForDropdownItem(paramString);
      return;
    } 
    throw null;
  }
  
  static String makeIdentifier(String paramString) {
    int j = paramString.length();
    StringBuilder stringBuilder = new StringBuilder();
    char c = paramString.charAt(0);
    if (Character.isJavaIdentifierStart(c)) {
      stringBuilder.append(c);
    } else if (Character.isJavaIdentifierPart(c)) {
      stringBuilder.append('_');
      stringBuilder.append(c);
    } 
    for (int i = 1; i < j; i++) {
      c = paramString.charAt(i);
      if (Character.isJavaIdentifierPart(c))
        stringBuilder.append(c); 
    } 
    return stringBuilder.toString();
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof HardwareItem;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      paramObject = paramObject;
      bool1 = bool2;
      if (this.hardwareType.equals(((HardwareItem)paramObject).hardwareType)) {
        bool1 = bool2;
        if (this.deviceName.equals(((HardwareItem)paramObject).deviceName)) {
          bool1 = bool2;
          if (this.identifier.equals(((HardwareItem)paramObject).identifier)) {
            bool1 = bool2;
            if (this.visibleName.equals(((HardwareItem)paramObject).visibleName))
              bool1 = true; 
          } 
        } 
      } 
    } 
    return bool1;
  }
  
  boolean hasAncestor(HardwareType paramHardwareType) {
    for (HardwareItem hardwareItem = this.parent; hardwareItem != null; hardwareItem = hardwareItem.parent) {
      if (hardwareItem.hardwareType == paramHardwareType)
        return true; 
    } 
    return false;
  }
  
  public int hashCode() {
    return this.hardwareType.hashCode() + this.deviceName.hashCode() + this.identifier.hashCode() + this.visibleName.hashCode();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\hardware\HardwareItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
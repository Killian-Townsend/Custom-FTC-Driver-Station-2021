package org.java_websocket.enums;

public enum Role {
  CLIENT, SERVER;
  
  static {
    Role role = new Role("SERVER", 1);
    SERVER = role;
    $VALUES = new Role[] { CLIENT, role };
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\java_websocket\enums\Role.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
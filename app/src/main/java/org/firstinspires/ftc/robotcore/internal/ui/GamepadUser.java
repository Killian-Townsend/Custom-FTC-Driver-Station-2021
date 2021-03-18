package org.firstinspires.ftc.robotcore.internal.ui;

public enum GamepadUser {
  ONE(1),
  TWO(1);
  
  public byte id;
  
  static {
    GamepadUser gamepadUser = new GamepadUser("TWO", 1, 2);
    TWO = gamepadUser;
    $VALUES = new GamepadUser[] { ONE, gamepadUser };
  }
  
  GamepadUser(int paramInt1) {
    this.id = (byte)paramInt1;
  }
  
  public static GamepadUser from(int paramInt) {
    return (paramInt == 1) ? ONE : ((paramInt == 2) ? TWO : null);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\interna\\ui\GamepadUser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
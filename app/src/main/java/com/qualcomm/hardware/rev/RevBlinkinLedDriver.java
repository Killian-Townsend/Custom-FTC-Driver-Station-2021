package com.qualcomm.hardware.rev;

import com.qualcomm.hardware.R;
import com.qualcomm.robotcore.hardware.ControlSystem;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.ServoControllerEx;
import com.qualcomm.robotcore.hardware.configuration.ServoFlavor;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.ServoType;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

@DeviceProperties(builtIn = true, compatibleControlSystems = {ControlSystem.REV_HUB}, description = "@string/rev_blinkin_description", name = "@string/rev_blinkin_name", xmlTag = "RevBlinkinLedDriver")
@ServoType(flavor = ServoFlavor.CUSTOM, usPulseLower = 500.0D, usPulseUpper = 2500.0D)
public class RevBlinkinLedDriver implements HardwareDevice {
  protected static final double BASE_SERVO_POSITION = 0.2525D;
  
  protected static final int PATTERN_OFFSET = 10;
  
  protected static final double PULSE_WIDTH_INCREMENTOR = 5.0E-4D;
  
  protected static final String TAG = "RevBlinkinLedDriver";
  
  protected ServoControllerEx controller;
  
  private final int port;
  
  public RevBlinkinLedDriver(ServoControllerEx paramServoControllerEx, int paramInt) {
    this.controller = paramServoControllerEx;
    this.port = paramInt;
  }
  
  public void close() {}
  
  public String getConnectionInfo() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.controller.getConnectionInfo());
    stringBuilder.append("; port ");
    stringBuilder.append(this.port);
    return stringBuilder.toString();
  }
  
  public String getDeviceName() {
    return AppUtil.getDefContext().getString(R.string.rev_blinkin_name);
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.Lynx;
  }
  
  public int getVersion() {
    return 1;
  }
  
  public void resetDeviceConfigurationForOpMode() {}
  
  public void setPattern(BlinkinPattern paramBlinkinPattern) {
    double d = (paramBlinkinPattern.ordinal() * 10) * 5.0E-4D + 0.2525D;
    RobotLog.vv("RevBlinkinLedDriver", "Pattern: %s, %d, %f", new Object[] { paramBlinkinPattern.toString(), Integer.valueOf(paramBlinkinPattern.ordinal()), Double.valueOf(d) });
    this.controller.setServoPosition(this.port, d);
  }
  
  public enum BlinkinPattern {
    AQUA, BEATS_PER_MINUTE_FOREST_PALETTE, BEATS_PER_MINUTE_LAVA_PALETTE, BEATS_PER_MINUTE_OCEAN_PALETTE, BEATS_PER_MINUTE_PARTY_PALETTE, BEATS_PER_MINUTE_RAINBOW_PALETTE, BLACK, BLUE, BLUE_GREEN, BLUE_VIOLET, BREATH_BLUE, BREATH_GRAY, BREATH_RED, COLOR_WAVES_FOREST_PALETTE, COLOR_WAVES_LAVA_PALETTE, COLOR_WAVES_OCEAN_PALETTE, COLOR_WAVES_PARTY_PALETTE, COLOR_WAVES_RAINBOW_PALETTE, CONFETTI, CP1_2_BEATS_PER_MINUTE, CP1_2_COLOR_GRADIENT, CP1_2_COLOR_WAVES, CP1_2_END_TO_END_BLEND, CP1_2_END_TO_END_BLEND_1_TO_2, CP1_2_NO_BLENDING, CP1_2_SINELON, CP1_2_SPARKLE_1_ON_2, CP1_2_SPARKLE_2_ON_1, CP1_2_TWINKLES, CP1_BREATH_FAST, CP1_BREATH_SLOW, CP1_END_TO_END_BLEND_TO_BLACK, CP1_HEARTBEAT_FAST, CP1_HEARTBEAT_MEDIUM, CP1_HEARTBEAT_SLOW, CP1_LARSON_SCANNER, CP1_LIGHT_CHASE, CP1_SHOT, CP1_STROBE, CP2_BREATH_FAST, CP2_BREATH_SLOW, CP2_END_TO_END_BLEND_TO_BLACK, CP2_HEARTBEAT_FAST, CP2_HEARTBEAT_MEDIUM, CP2_HEARTBEAT_SLOW, CP2_LARSON_SCANNER, CP2_LIGHT_CHASE, CP2_SHOT, CP2_STROBE, DARK_BLUE, DARK_GRAY, DARK_GREEN, DARK_RED, FIRE_LARGE, FIRE_MEDIUM, GOLD, GRAY, GREEN, HEARTBEAT_BLUE, HEARTBEAT_GRAY, HEARTBEAT_RED, HEARTBEAT_WHITE, HOT_PINK, LARSON_SCANNER_GRAY, LARSON_SCANNER_RED, LAWN_GREEN, LIGHT_CHASE_BLUE, LIGHT_CHASE_GRAY, LIGHT_CHASE_RED, LIME, ORANGE, RAINBOW_FOREST_PALETTE, RAINBOW_LAVA_PALETTE, RAINBOW_OCEAN_PALETTE, RAINBOW_PARTY_PALETTE, RAINBOW_RAINBOW_PALETTE, RAINBOW_WITH_GLITTER, RED, RED_ORANGE, SHOT_BLUE, SHOT_RED, SHOT_WHITE, SINELON_FOREST_PALETTE, SINELON_LAVA_PALETTE, SINELON_OCEAN_PALETTE, SINELON_PARTY_PALETTE, SINELON_RAINBOW_PALETTE, SKY_BLUE, STROBE_BLUE, STROBE_GOLD, STROBE_RED, STROBE_WHITE, TWINKLES_FOREST_PALETTE, TWINKLES_LAVA_PALETTE, TWINKLES_OCEAN_PALETTE, TWINKLES_PARTY_PALETTE, TWINKLES_RAINBOW_PALETTE, VIOLET, WHITE, YELLOW;
    
    private static BlinkinPattern[] elements;
    
    static {
      RAINBOW_OCEAN_PALETTE = new BlinkinPattern("RAINBOW_OCEAN_PALETTE", 2);
      RAINBOW_LAVA_PALETTE = new BlinkinPattern("RAINBOW_LAVA_PALETTE", 3);
      RAINBOW_FOREST_PALETTE = new BlinkinPattern("RAINBOW_FOREST_PALETTE", 4);
      RAINBOW_WITH_GLITTER = new BlinkinPattern("RAINBOW_WITH_GLITTER", 5);
      CONFETTI = new BlinkinPattern("CONFETTI", 6);
      SHOT_RED = new BlinkinPattern("SHOT_RED", 7);
      SHOT_BLUE = new BlinkinPattern("SHOT_BLUE", 8);
      SHOT_WHITE = new BlinkinPattern("SHOT_WHITE", 9);
      SINELON_RAINBOW_PALETTE = new BlinkinPattern("SINELON_RAINBOW_PALETTE", 10);
      SINELON_PARTY_PALETTE = new BlinkinPattern("SINELON_PARTY_PALETTE", 11);
      SINELON_OCEAN_PALETTE = new BlinkinPattern("SINELON_OCEAN_PALETTE", 12);
      SINELON_LAVA_PALETTE = new BlinkinPattern("SINELON_LAVA_PALETTE", 13);
      SINELON_FOREST_PALETTE = new BlinkinPattern("SINELON_FOREST_PALETTE", 14);
      BEATS_PER_MINUTE_RAINBOW_PALETTE = new BlinkinPattern("BEATS_PER_MINUTE_RAINBOW_PALETTE", 15);
      BEATS_PER_MINUTE_PARTY_PALETTE = new BlinkinPattern("BEATS_PER_MINUTE_PARTY_PALETTE", 16);
      BEATS_PER_MINUTE_OCEAN_PALETTE = new BlinkinPattern("BEATS_PER_MINUTE_OCEAN_PALETTE", 17);
      BEATS_PER_MINUTE_LAVA_PALETTE = new BlinkinPattern("BEATS_PER_MINUTE_LAVA_PALETTE", 18);
      BEATS_PER_MINUTE_FOREST_PALETTE = new BlinkinPattern("BEATS_PER_MINUTE_FOREST_PALETTE", 19);
      FIRE_MEDIUM = new BlinkinPattern("FIRE_MEDIUM", 20);
      FIRE_LARGE = new BlinkinPattern("FIRE_LARGE", 21);
      TWINKLES_RAINBOW_PALETTE = new BlinkinPattern("TWINKLES_RAINBOW_PALETTE", 22);
      TWINKLES_PARTY_PALETTE = new BlinkinPattern("TWINKLES_PARTY_PALETTE", 23);
      TWINKLES_OCEAN_PALETTE = new BlinkinPattern("TWINKLES_OCEAN_PALETTE", 24);
      TWINKLES_LAVA_PALETTE = new BlinkinPattern("TWINKLES_LAVA_PALETTE", 25);
      TWINKLES_FOREST_PALETTE = new BlinkinPattern("TWINKLES_FOREST_PALETTE", 26);
      COLOR_WAVES_RAINBOW_PALETTE = new BlinkinPattern("COLOR_WAVES_RAINBOW_PALETTE", 27);
      COLOR_WAVES_PARTY_PALETTE = new BlinkinPattern("COLOR_WAVES_PARTY_PALETTE", 28);
      COLOR_WAVES_OCEAN_PALETTE = new BlinkinPattern("COLOR_WAVES_OCEAN_PALETTE", 29);
      COLOR_WAVES_LAVA_PALETTE = new BlinkinPattern("COLOR_WAVES_LAVA_PALETTE", 30);
      COLOR_WAVES_FOREST_PALETTE = new BlinkinPattern("COLOR_WAVES_FOREST_PALETTE", 31);
      LARSON_SCANNER_RED = new BlinkinPattern("LARSON_SCANNER_RED", 32);
      LARSON_SCANNER_GRAY = new BlinkinPattern("LARSON_SCANNER_GRAY", 33);
      LIGHT_CHASE_RED = new BlinkinPattern("LIGHT_CHASE_RED", 34);
      LIGHT_CHASE_BLUE = new BlinkinPattern("LIGHT_CHASE_BLUE", 35);
      LIGHT_CHASE_GRAY = new BlinkinPattern("LIGHT_CHASE_GRAY", 36);
      HEARTBEAT_RED = new BlinkinPattern("HEARTBEAT_RED", 37);
      HEARTBEAT_BLUE = new BlinkinPattern("HEARTBEAT_BLUE", 38);
      HEARTBEAT_WHITE = new BlinkinPattern("HEARTBEAT_WHITE", 39);
      HEARTBEAT_GRAY = new BlinkinPattern("HEARTBEAT_GRAY", 40);
      BREATH_RED = new BlinkinPattern("BREATH_RED", 41);
      BREATH_BLUE = new BlinkinPattern("BREATH_BLUE", 42);
      BREATH_GRAY = new BlinkinPattern("BREATH_GRAY", 43);
      STROBE_RED = new BlinkinPattern("STROBE_RED", 44);
      STROBE_BLUE = new BlinkinPattern("STROBE_BLUE", 45);
      STROBE_GOLD = new BlinkinPattern("STROBE_GOLD", 46);
      STROBE_WHITE = new BlinkinPattern("STROBE_WHITE", 47);
      CP1_END_TO_END_BLEND_TO_BLACK = new BlinkinPattern("CP1_END_TO_END_BLEND_TO_BLACK", 48);
      CP1_LARSON_SCANNER = new BlinkinPattern("CP1_LARSON_SCANNER", 49);
      CP1_LIGHT_CHASE = new BlinkinPattern("CP1_LIGHT_CHASE", 50);
      CP1_HEARTBEAT_SLOW = new BlinkinPattern("CP1_HEARTBEAT_SLOW", 51);
      CP1_HEARTBEAT_MEDIUM = new BlinkinPattern("CP1_HEARTBEAT_MEDIUM", 52);
      CP1_HEARTBEAT_FAST = new BlinkinPattern("CP1_HEARTBEAT_FAST", 53);
      CP1_BREATH_SLOW = new BlinkinPattern("CP1_BREATH_SLOW", 54);
      CP1_BREATH_FAST = new BlinkinPattern("CP1_BREATH_FAST", 55);
      CP1_SHOT = new BlinkinPattern("CP1_SHOT", 56);
      CP1_STROBE = new BlinkinPattern("CP1_STROBE", 57);
      CP2_END_TO_END_BLEND_TO_BLACK = new BlinkinPattern("CP2_END_TO_END_BLEND_TO_BLACK", 58);
      CP2_LARSON_SCANNER = new BlinkinPattern("CP2_LARSON_SCANNER", 59);
      CP2_LIGHT_CHASE = new BlinkinPattern("CP2_LIGHT_CHASE", 60);
      CP2_HEARTBEAT_SLOW = new BlinkinPattern("CP2_HEARTBEAT_SLOW", 61);
      CP2_HEARTBEAT_MEDIUM = new BlinkinPattern("CP2_HEARTBEAT_MEDIUM", 62);
      CP2_HEARTBEAT_FAST = new BlinkinPattern("CP2_HEARTBEAT_FAST", 63);
      CP2_BREATH_SLOW = new BlinkinPattern("CP2_BREATH_SLOW", 64);
      CP2_BREATH_FAST = new BlinkinPattern("CP2_BREATH_FAST", 65);
      CP2_SHOT = new BlinkinPattern("CP2_SHOT", 66);
      CP2_STROBE = new BlinkinPattern("CP2_STROBE", 67);
      CP1_2_SPARKLE_1_ON_2 = new BlinkinPattern("CP1_2_SPARKLE_1_ON_2", 68);
      CP1_2_SPARKLE_2_ON_1 = new BlinkinPattern("CP1_2_SPARKLE_2_ON_1", 69);
      CP1_2_COLOR_GRADIENT = new BlinkinPattern("CP1_2_COLOR_GRADIENT", 70);
      CP1_2_BEATS_PER_MINUTE = new BlinkinPattern("CP1_2_BEATS_PER_MINUTE", 71);
      CP1_2_END_TO_END_BLEND_1_TO_2 = new BlinkinPattern("CP1_2_END_TO_END_BLEND_1_TO_2", 72);
      CP1_2_END_TO_END_BLEND = new BlinkinPattern("CP1_2_END_TO_END_BLEND", 73);
      CP1_2_NO_BLENDING = new BlinkinPattern("CP1_2_NO_BLENDING", 74);
      CP1_2_TWINKLES = new BlinkinPattern("CP1_2_TWINKLES", 75);
      CP1_2_COLOR_WAVES = new BlinkinPattern("CP1_2_COLOR_WAVES", 76);
      CP1_2_SINELON = new BlinkinPattern("CP1_2_SINELON", 77);
      HOT_PINK = new BlinkinPattern("HOT_PINK", 78);
      DARK_RED = new BlinkinPattern("DARK_RED", 79);
      RED = new BlinkinPattern("RED", 80);
      RED_ORANGE = new BlinkinPattern("RED_ORANGE", 81);
      ORANGE = new BlinkinPattern("ORANGE", 82);
      GOLD = new BlinkinPattern("GOLD", 83);
      YELLOW = new BlinkinPattern("YELLOW", 84);
      LAWN_GREEN = new BlinkinPattern("LAWN_GREEN", 85);
      LIME = new BlinkinPattern("LIME", 86);
      DARK_GREEN = new BlinkinPattern("DARK_GREEN", 87);
      GREEN = new BlinkinPattern("GREEN", 88);
      BLUE_GREEN = new BlinkinPattern("BLUE_GREEN", 89);
      AQUA = new BlinkinPattern("AQUA", 90);
      SKY_BLUE = new BlinkinPattern("SKY_BLUE", 91);
      DARK_BLUE = new BlinkinPattern("DARK_BLUE", 92);
      BLUE = new BlinkinPattern("BLUE", 93);
      BLUE_VIOLET = new BlinkinPattern("BLUE_VIOLET", 94);
      VIOLET = new BlinkinPattern("VIOLET", 95);
      WHITE = new BlinkinPattern("WHITE", 96);
      GRAY = new BlinkinPattern("GRAY", 97);
      DARK_GRAY = new BlinkinPattern("DARK_GRAY", 98);
      BlinkinPattern blinkinPattern = new BlinkinPattern("BLACK", 99);
      BLACK = blinkinPattern;
      $VALUES = new BlinkinPattern[] { 
          RAINBOW_RAINBOW_PALETTE, RAINBOW_PARTY_PALETTE, RAINBOW_OCEAN_PALETTE, RAINBOW_LAVA_PALETTE, RAINBOW_FOREST_PALETTE, RAINBOW_WITH_GLITTER, CONFETTI, SHOT_RED, SHOT_BLUE, SHOT_WHITE, 
          SINELON_RAINBOW_PALETTE, SINELON_PARTY_PALETTE, SINELON_OCEAN_PALETTE, SINELON_LAVA_PALETTE, SINELON_FOREST_PALETTE, BEATS_PER_MINUTE_RAINBOW_PALETTE, BEATS_PER_MINUTE_PARTY_PALETTE, BEATS_PER_MINUTE_OCEAN_PALETTE, BEATS_PER_MINUTE_LAVA_PALETTE, BEATS_PER_MINUTE_FOREST_PALETTE, 
          FIRE_MEDIUM, FIRE_LARGE, TWINKLES_RAINBOW_PALETTE, TWINKLES_PARTY_PALETTE, TWINKLES_OCEAN_PALETTE, TWINKLES_LAVA_PALETTE, TWINKLES_FOREST_PALETTE, COLOR_WAVES_RAINBOW_PALETTE, COLOR_WAVES_PARTY_PALETTE, COLOR_WAVES_OCEAN_PALETTE, 
          COLOR_WAVES_LAVA_PALETTE, COLOR_WAVES_FOREST_PALETTE, LARSON_SCANNER_RED, LARSON_SCANNER_GRAY, LIGHT_CHASE_RED, LIGHT_CHASE_BLUE, LIGHT_CHASE_GRAY, HEARTBEAT_RED, HEARTBEAT_BLUE, HEARTBEAT_WHITE, 
          HEARTBEAT_GRAY, BREATH_RED, BREATH_BLUE, BREATH_GRAY, STROBE_RED, STROBE_BLUE, STROBE_GOLD, STROBE_WHITE, CP1_END_TO_END_BLEND_TO_BLACK, CP1_LARSON_SCANNER, 
          CP1_LIGHT_CHASE, CP1_HEARTBEAT_SLOW, CP1_HEARTBEAT_MEDIUM, CP1_HEARTBEAT_FAST, CP1_BREATH_SLOW, CP1_BREATH_FAST, CP1_SHOT, CP1_STROBE, CP2_END_TO_END_BLEND_TO_BLACK, CP2_LARSON_SCANNER, 
          CP2_LIGHT_CHASE, CP2_HEARTBEAT_SLOW, CP2_HEARTBEAT_MEDIUM, CP2_HEARTBEAT_FAST, CP2_BREATH_SLOW, CP2_BREATH_FAST, CP2_SHOT, CP2_STROBE, CP1_2_SPARKLE_1_ON_2, CP1_2_SPARKLE_2_ON_1, 
          CP1_2_COLOR_GRADIENT, CP1_2_BEATS_PER_MINUTE, CP1_2_END_TO_END_BLEND_1_TO_2, CP1_2_END_TO_END_BLEND, CP1_2_NO_BLENDING, CP1_2_TWINKLES, CP1_2_COLOR_WAVES, CP1_2_SINELON, HOT_PINK, DARK_RED, 
          RED, RED_ORANGE, ORANGE, GOLD, YELLOW, LAWN_GREEN, LIME, DARK_GREEN, GREEN, BLUE_GREEN, 
          AQUA, SKY_BLUE, DARK_BLUE, BLUE, BLUE_VIOLET, VIOLET, WHITE, GRAY, DARK_GRAY, blinkinPattern };
      elements = values();
    }
    
    public static BlinkinPattern fromNumber(int param1Int) {
      BlinkinPattern[] arrayOfBlinkinPattern = elements;
      return arrayOfBlinkinPattern[param1Int % arrayOfBlinkinPattern.length];
    }
    
    public BlinkinPattern next() {
      return elements[(ordinal() + 1) % elements.length];
    }
    
    public BlinkinPattern previous() {
      int i;
      BlinkinPattern[] arrayOfBlinkinPattern = elements;
      if (ordinal() - 1 < 0) {
        i = elements.length;
      } else {
        i = ordinal();
      } 
      return arrayOfBlinkinPattern[i - 1];
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\rev\RevBlinkinLedDriver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
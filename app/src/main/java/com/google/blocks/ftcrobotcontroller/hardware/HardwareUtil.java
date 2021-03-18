package com.google.blocks.ftcrobotcontroller.hardware;

import android.content.res.AssetManager;
import android.hardware.Camera;
import android.hardware.SensorManager;
import com.google.blocks.ftcrobotcontroller.util.AvailableTtsLocalesProvider;
import com.google.blocks.ftcrobotcontroller.util.FileUtil;
import com.google.blocks.ftcrobotcontroller.util.Identifier;
import com.google.blocks.ftcrobotcontroller.util.ProjectsUtil;
import com.google.blocks.ftcrobotcontroller.util.ToolboxFolder;
import com.google.blocks.ftcrobotcontroller.util.ToolboxIcon;
import com.google.blocks.ftcrobotcontroller.util.ToolboxUtil;
import com.qualcomm.ftccommon.R;
import com.qualcomm.ftccommon.configuration.RobotConfigFileManager;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.firstinspires.ftc.robotcore.external.ExportToBlocks;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaCurrentGame;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaRoverRuckus;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaSkyStone;
import org.firstinspires.ftc.robotcore.external.tfod.TfodCurrentGame;
import org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus;
import org.firstinspires.ftc.robotcore.external.tfod.TfodSkyStone;
import org.firstinspires.ftc.robotcore.internal.opmode.BlocksClassFilter;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class HardwareUtil {
  private static final String COLOR_CATEGORY_NAME = "Color";
  
  private static final String DC_MOTOR_DUAL_CATEGORY_NAME = "Dual";
  
  private static final String DC_MOTOR_EX_CATEGORY_NAME = "Extended";
  
  private static final String ELAPSED_TIME_CATEGORY_NAME = "ElapsedTime";
  
  private static final String GAMEPAD_CATEGORY_NAME = "Gamepad";
  
  private static final String LINEAR_OP_MODE_CATEGORY_NAME = "LinearOpMode";
  
  public static final String SWITCHABLE_CAMERA_NAME = "Switchable Camera";
  
  private static final Map<String, List<HardwareType>> XML_TAG_TO_HARDWARE_TYPES;
  
  private static final Set<String> reservedWordsForFtcJava = buildReservedWordsForFtcJava();
  
  static {
    XML_TAG_TO_HARDWARE_TYPES = new HashMap<String, List<HardwareType>>();
    for (HardwareType hardwareType : HardwareType.values()) {
      for (String str : hardwareType.xmlTags) {
        List<HardwareType> list2 = XML_TAG_TO_HARDWARE_TYPES.get(str);
        List<HardwareType> list1 = list2;
        if (list2 == null) {
          list1 = new ArrayList();
          XML_TAG_TO_HARDWARE_TYPES.put(str, list1);
        } 
        list1.add(hardwareType);
      } 
    } 
  }
  
  private static String accessMethod(Class paramClass) {
    return (paramClass.equals(boolean.class) || paramClass.equals(Boolean.class)) ? "callJava_boolean" : ((paramClass.equals(char.class) || paramClass.equals(Character.class) || paramClass.equals(String.class) || paramClass.equals(byte.class) || paramClass.equals(Byte.class) || paramClass.equals(short.class) || paramClass.equals(Short.class) || paramClass.equals(int.class) || paramClass.equals(Integer.class) || paramClass.equals(long.class) || paramClass.equals(Long.class) || paramClass.equals(float.class) || paramClass.equals(Float.class) || paramClass.equals(double.class) || paramClass.equals(Double.class) || paramClass.isEnum()) ? "callJava_String" : "callJava");
  }
  
  private static void addAccelerationSensorCategoryToToolbox(StringBuilder paramStringBuilder, HardwareType paramHardwareType, List<HardwareItem> paramList) {
    String str = ((HardwareItem)paramList.get(0)).identifier;
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    treeMap.put("Acceleration", "Acceleration");
    treeMap.put("XAccel", "Number");
    treeMap.put("YAccel", "Number");
    treeMap.put("ZAccel", "Number");
    ToolboxUtil.addProperties(paramStringBuilder, paramHardwareType, str, treeMap, null, null);
    treeMap = new TreeMap<Object, Object>();
    treeMap.put("toText", null);
    ToolboxUtil.addFunctions(paramStringBuilder, paramHardwareType, str, treeMap);
  }
  
  private static void addAnalogInputCategoryToToolbox(StringBuilder paramStringBuilder, HardwareType paramHardwareType, List<HardwareItem> paramList) {
    String str = ((HardwareItem)paramList.get(0)).identifier;
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    treeMap.put("Voltage", "Number");
    treeMap.put("MaxVoltage", "Number");
    ToolboxUtil.addProperties(paramStringBuilder, paramHardwareType, str, treeMap, null, null);
  }
  
  private static void addAnalogOutputCategoryToToolbox(StringBuilder paramStringBuilder, HardwareType paramHardwareType, List<HardwareItem> paramList) {
    String str = ((HardwareItem)paramList.get(0)).identifier;
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<Object, Object>();
    linkedHashMap.put("VOLTAGE", ToolboxUtil.makeNumberShadow(512));
    treeMap.put("setAnalogOutputVoltage_Number", linkedHashMap);
    linkedHashMap = new LinkedHashMap<Object, Object>();
    linkedHashMap.put("FREQUENCY", ToolboxUtil.makeNumberShadow(100));
    treeMap.put("setAnalogOutputFrequency_Number", linkedHashMap);
    linkedHashMap = new LinkedHashMap<Object, Object>();
    linkedHashMap.put("MODE", ToolboxUtil.makeNumberShadow(0));
    treeMap.put("setAnalogOutputMode_Number", linkedHashMap);
    ToolboxUtil.addFunctions(paramStringBuilder, paramHardwareType, str, treeMap);
  }
  
  private static void addAndroidCategoriesToToolbox(StringBuilder paramStringBuilder, AssetManager paramAssetManager) throws IOException {
    SensorManager sensorManager = (SensorManager)AppUtil.getDefContext().getSystemService("sensor");
    boolean bool1 = true;
    int i = sensorManager.getSensorList(1).isEmpty() ^ true;
    boolean bool3 = sensorManager.getSensorList(4).isEmpty();
    boolean bool4 = sensorManager.getSensorList(2).isEmpty();
    StringBuilder stringBuilder = new StringBuilder();
    boolean bool2 = false;
    if (i != 0) {
      if (paramAssetManager != null) {
        addAsset(stringBuilder, paramAssetManager, "toolbox/android_accelerometer.xml");
        bool1 = false;
      } 
    } else {
      RobotLog.w("Skipping toolbox/android_accelerometer.xml because this device does not have an accelerometer.");
    } 
    if ((bool3 ^ true) != 0) {
      if (paramAssetManager != null) {
        addAsset(stringBuilder, paramAssetManager, "toolbox/android_gyroscope.xml");
        bool1 = false;
      } 
    } else {
      RobotLog.w("Skipping toolbox/android_gyroscope.xml because this device does not have a gyroscope.");
    } 
    if (i != 0 && (bool4 ^ true) != 0) {
      if (paramAssetManager != null) {
        addAsset(stringBuilder, paramAssetManager, "toolbox/android_orientation.xml");
        bool1 = false;
      } 
    } else {
      RobotLog.w("Skipping toolbox/android_gyroscope.xml because this device does not have an accelerometer and a magnetic field sensor.");
    } 
    if (paramAssetManager != null) {
      addAsset(stringBuilder, paramAssetManager, "toolbox/android_sound_pool.xml");
      addAsset(stringBuilder, paramAssetManager, "toolbox/android_text_to_speech.xml");
      bool1 = bool2;
    } 
    if (!bool1) {
      paramStringBuilder.append("<category name=\"Android\">\n");
      paramStringBuilder.append(stringBuilder);
      paramStringBuilder.append("</category>\n");
    } 
  }
  
  private static void addAsset(StringBuilder paramStringBuilder, AssetManager paramAssetManager, String paramString) throws IOException {
    FileUtil.readAsset(paramStringBuilder, paramAssetManager, paramString);
  }
  
  private static void addAssetWithPlaceholders(StringBuilder paramStringBuilder, AssetManager paramAssetManager, Map<Capability, Boolean> paramMap, String paramString) throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(paramAssetManager.open(paramString)));
    try {
      while (true) {
        String str = bufferedReader.readLine();
        if (str != null) {
          String str1 = str.trim().replace("placeholder_current_game_name", "Ultimate Goal").replace("<placeholder_tfod_current_game_labels/>", getTfodCurrentGameLabelBlocks()).replace("<placeholder_vuforia_current_game_trackable_names/>", getVuforiaCurrentGameTrackableNameBlocks());
          if (str1.startsWith("<placeholder_") && str1.endsWith("/>")) {
            int i = str1.indexOf('_', 13);
            if (i != -1) {
              str = str1.substring(13, i);
              StringBuilder stringBuilder2 = new StringBuilder();
              stringBuilder2.append("toolbox/");
              stringBuilder2.append(str1.substring(i + 1, str1.length() - 2).trim());
              stringBuilder2.append(".xml");
              str1 = stringBuilder2.toString();
              Boolean bool = paramMap.get(Capability.fromPlaceholderType(str));
              if (bool != null) {
                if (bool.booleanValue()) {
                  addAssetWithPlaceholders(paramStringBuilder, paramAssetManager, paramMap, str1);
                  continue;
                } 
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("Skipping ");
                stringBuilder3.append(str1);
                stringBuilder3.append(" because type \"");
                stringBuilder3.append(str);
                stringBuilder3.append("\" is not supported by this device and/or hardware.");
                RobotLog.w(stringBuilder3.toString());
                continue;
              } 
              StringBuilder stringBuilder1 = new StringBuilder();
              stringBuilder1.append("Error: Skipping ");
              stringBuilder1.append(str1);
              stringBuilder1.append(" because type \"");
              stringBuilder1.append(str);
              stringBuilder1.append("\" is not recognized.");
              RobotLog.e(stringBuilder1.toString());
              continue;
            } 
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error: Unable to parse placeholder \"");
            stringBuilder.append(str1);
            stringBuilder.append("\"");
            RobotLog.e(stringBuilder.toString());
            continue;
          } 
          paramStringBuilder.append(str1);
          paramStringBuilder.append("\n");
          continue;
        } 
        return;
      } 
    } finally {
      paramStringBuilder = null;
    } 
  }
  
  private static void addBNO055IMUCategoryToToolbox(StringBuilder paramStringBuilder, HardwareType paramHardwareType, List<HardwareItem> paramList) {
    String str = ((HardwareItem)paramList.get(0)).identifier;
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    treeMap.put("Acceleration", "Acceleration");
    treeMap.put("AngularOrientation", "Orientation");
    treeMap.put("AngularOrientationAxes", "Array");
    treeMap.put("AngularVelocity", "AngularVelocity");
    treeMap.put("AngularVelocityAxes", "Array");
    treeMap.put("CalibrationStatus", "String");
    treeMap.put("Gravity", "Acceleration");
    treeMap.put("I2cAddress7Bit", "Number");
    treeMap.put("I2cAddress8Bit", "Number");
    treeMap.put("LinearAcceleration", "Acceleration");
    treeMap.put("MagneticFieldStrength", "MagneticFlux");
    treeMap.put("OverallAcceleration", "Acceleration");
    treeMap.put("Position", "Position");
    treeMap.put("QuaternionOrientation", "Quaternion");
    treeMap.put("SystemError", "String");
    treeMap.put("SystemStatus", "SystemStatus");
    treeMap.put("Temperature", "Temperature");
    treeMap.put("Velocity", "Velocity");
    HashMap<Object, Object> hashMap1 = new HashMap<Object, Object>();
    hashMap1.put("I2cAddress7Bit", new String[] { ToolboxUtil.makeNumberShadow(8) });
    hashMap1.put("I2cAddress8Bit", new String[] { ToolboxUtil.makeNumberShadow(16) });
    HashMap<Object, Object> hashMap2 = new HashMap<Object, Object>();
    hashMap2.put("SystemStatus", new String[] { ToolboxUtil.makeTypedEnumBlock(paramHardwareType, "systemStatus") });
    ToolboxUtil.addProperties(paramStringBuilder, paramHardwareType, str, treeMap, hashMap1, hashMap2);
    treeMap = new TreeMap<Object, Object>();
    hashMap1 = new LinkedHashMap<Object, Object>();
    hashMap1.put("PARAMETERS", ToolboxUtil.makeVariableGetBlock("parameters"));
    treeMap.put("initialize", hashMap1);
    hashMap1 = new LinkedHashMap<Object, Object>();
    hashMap1.put("MS_POLL_INTERVAL", ToolboxUtil.makeNumberShadow(1000));
    treeMap.put("startAccelerationIntegration_with1", hashMap1);
    hashMap1 = new LinkedHashMap<Object, Object>();
    hashMap1.put("INITIAL_POSITION", ToolboxUtil.makeVariableGetBlock("position"));
    hashMap1.put("INITIAL_VELOCITY", ToolboxUtil.makeVariableGetBlock("velocity"));
    hashMap1.put("MS_POLL_INTERVAL", ToolboxUtil.makeNumberShadow(1000));
    treeMap.put("startAccelerationIntegration_with3", hashMap1);
    treeMap.put("stopAccelerationIntegration", null);
    treeMap.put("isSystemCalibrated", null);
    treeMap.put("isGyroCalibrated", null);
    treeMap.put("isAccelerometerCalibrated", null);
    treeMap.put("isMagnetometerCalibrated", null);
    hashMap1 = new LinkedHashMap<Object, Object>();
    hashMap1.put("FILE_NAME", ToolboxUtil.makeTextShadow("IMUCalibration.json"));
    treeMap.put("saveCalibrationData", hashMap1);
    hashMap1 = new LinkedHashMap<Object, Object>();
    hashMap1.put("ANGLE_UNIT", ToolboxUtil.makeTypedEnumShadow("navigation", "angleUnit"));
    treeMap.put("getAngularVelocity", hashMap1);
    hashMap1 = new LinkedHashMap<Object, Object>();
    hashMap1.put("AXES_REFERENCE", ToolboxUtil.makeTypedEnumShadow("navigation", "axesReference"));
    hashMap1.put("AXES_ORDER", ToolboxUtil.makeTypedEnumShadow("navigation", "axesOrder"));
    hashMap1.put("ANGLE_UNIT", ToolboxUtil.makeTypedEnumShadow("navigation", "angleUnit"));
    treeMap.put("getAngularOrientation", hashMap1);
    ToolboxUtil.addFunctions(paramStringBuilder, paramHardwareType, str, treeMap);
  }
  
  private static void addCRServoCategoryToToolbox(StringBuilder paramStringBuilder, HardwareType paramHardwareType, List<HardwareItem> paramList) {
    String str = ((HardwareItem)paramList.get(0)).identifier;
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    treeMap.put("Direction", "Direction");
    treeMap.put("Power", "Number");
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    hashMap.put("Direction", new String[] { ToolboxUtil.makeTypedEnumShadow(paramHardwareType, "direction") });
    hashMap.put("Power", new String[] { ToolboxUtil.makeNumberShadow(1), ToolboxUtil.makeNumberShadow(0) });
    ToolboxUtil.addProperties(paramStringBuilder, paramHardwareType, str, treeMap, hashMap, null);
  }
  
  private static void addColorRangeSensorCategoryToToolbox(StringBuilder paramStringBuilder, HardwareType paramHardwareType, List<HardwareItem> paramList) {
    String str = ((HardwareItem)paramList.get(0)).identifier;
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    treeMap.put("Red", "Number");
    treeMap.put("Green", "Number");
    treeMap.put("Blue", "Number");
    treeMap.put("Alpha", "Number");
    treeMap.put("Argb", "Number");
    treeMap.put("Gain", "Number");
    treeMap.put("I2cAddress7Bit", "Number");
    treeMap.put("I2cAddress8Bit", "Number");
    treeMap.put("LightDetected", "Number");
    treeMap.put("RawLightDetected", "Number");
    treeMap.put("RawLightDetectedMax", "Number");
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    hashMap.put("Gain", new String[] { ToolboxUtil.makeNumberShadow(2) });
    hashMap.put("I2cAddress7Bit", new String[] { ToolboxUtil.makeNumberShadow(8) });
    hashMap.put("I2cAddress8Bit", new String[] { ToolboxUtil.makeNumberShadow(16) });
    ToolboxUtil.addProperties(paramStringBuilder, paramHardwareType, str, treeMap, hashMap, null);
    treeMap = new TreeMap<Object, Object>();
    hashMap = new LinkedHashMap<Object, Object>();
    hashMap.put("UNIT", ToolboxUtil.makeTypedEnumShadow("navigation", "distanceUnit"));
    treeMap.put("getDistance_Number", hashMap);
    treeMap.put("getNormalizedColors", null);
    ToolboxUtil.addFunctions(paramStringBuilder, paramHardwareType, str, treeMap);
  }
  
  private static void addColorSensorCategoryToToolbox(StringBuilder paramStringBuilder, HardwareType paramHardwareType, List<HardwareItem> paramList) {
    String str = ((HardwareItem)paramList.get(0)).identifier;
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    treeMap.put("Red", "Number");
    treeMap.put("Green", "Number");
    treeMap.put("Blue", "Number");
    treeMap.put("Alpha", "Number");
    treeMap.put("Argb", "Number");
    treeMap.put("Gain", "Number");
    treeMap.put("I2cAddress7Bit", "Number");
    treeMap.put("I2cAddress8Bit", "Number");
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    hashMap.put("Gain", new String[] { ToolboxUtil.makeNumberShadow(2) });
    hashMap.put("I2cAddress7Bit", new String[] { ToolboxUtil.makeNumberShadow(8) });
    hashMap.put("I2cAddress8Bit", new String[] { ToolboxUtil.makeNumberShadow(16) });
    ToolboxUtil.addProperties(paramStringBuilder, paramHardwareType, str, treeMap, hashMap, null);
    treeMap = new TreeMap<Object, Object>();
    hashMap = new LinkedHashMap<Object, Object>();
    hashMap.put("ENABLE", ToolboxUtil.makeBooleanShadow(true));
    treeMap.put("enableLed_Boolean", hashMap);
    treeMap.put("isLightOn", null);
    treeMap.put("getNormalizedColors", null);
    treeMap.put("toText", null);
    ToolboxUtil.addFunctions(paramStringBuilder, paramHardwareType, str, treeMap);
  }
  
  private static void addCompassSensorCategoryToToolbox(StringBuilder paramStringBuilder, HardwareType paramHardwareType, List<HardwareItem> paramList) {
    String str = ((HardwareItem)paramList.get(0)).identifier;
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    treeMap.put("Direction", "Number");
    treeMap.put("CalibrationFailed", "Boolean");
    ToolboxUtil.addProperties(paramStringBuilder, paramHardwareType, str, treeMap, null, null);
    treeMap = new TreeMap<Object, Object>();
    LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<Object, Object>();
    linkedHashMap.put("COMPASS_MODE", ToolboxUtil.makeTypedEnumShadow(paramHardwareType, "compassMode"));
    treeMap.put("setMode_CompassMode", linkedHashMap);
    ToolboxUtil.addFunctions(paramStringBuilder, paramHardwareType, str, treeMap);
  }
  
  private static void addCountry(String paramString1, String paramString2, StringBuilder paramStringBuilder1, StringBuilder paramStringBuilder2) {
    paramStringBuilder1.append("      ['");
    paramStringBuilder1.append(ProjectsUtil.escapeSingleQuotes(makeVisibleNameForDropdownItem(paramString1)));
    paramStringBuilder1.append("', '");
    paramStringBuilder1.append(ProjectsUtil.escapeSingleQuotes(paramString1));
    paramStringBuilder1.append("'],\n");
    paramStringBuilder2.append("  ['");
    paramStringBuilder2.append(ProjectsUtil.escapeSingleQuotes(paramString1));
    paramStringBuilder2.append("', 'The country code for ");
    paramStringBuilder2.append(ProjectsUtil.escapeSingleQuotes(paramString2));
    paramStringBuilder2.append(".'],\n");
  }
  
  private static void addDcMotorCategoryToToolbox(StringBuilder paramStringBuilder, HardwareType paramHardwareType, List<HardwareItem> paramList) {
    String str10 = ((HardwareItem)paramList.get(0)).identifier;
    String str6 = ToolboxUtil.makeNumberShadow(0);
    String str7 = ToolboxUtil.makeNumberShadow(1);
    String str2 = ToolboxUtil.makeNumberShadow(5);
    String str5 = ToolboxUtil.makeNumberShadow(10);
    String str8 = ToolboxUtil.makeTypedEnumShadow(paramHardwareType, "runMode");
    String str9 = ToolboxUtil.makeTypedEnumShadow(paramHardwareType, "zeroPowerBehavior");
    String str11 = ToolboxUtil.makeTypedEnumShadow(paramHardwareType, "direction");
    String str4 = ToolboxUtil.makeTypedEnumShadow("navigation", "angleUnit");
    String str1 = ToolboxUtil.makeTypedEnumShadow("navigation", "currentUnit");
    String str3 = ToolboxUtil.makeTypedEnumShadow(paramHardwareType, "runMode", "RUN_MODE", "RUN_USING_ENCODER");
    TreeMap<Object, Object> treeMap1 = new TreeMap<Object, Object>();
    treeMap1.put("CurrentPosition", "Number");
    treeMap1.put("Direction", "Direction");
    treeMap1.put("Mode", "RunMode");
    treeMap1.put("Power", "Number");
    treeMap1.put("PowerFloat", "Boolean");
    treeMap1.put("TargetPosition", "Number");
    treeMap1.put("ZeroPowerBehavior", "ZeroPowerBehavior");
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    hashMap.put("Direction", new String[] { str11 });
    hashMap.put("Mode", new String[] { str8 });
    hashMap.put("Power", new String[] { str7, str6 });
    hashMap.put("TargetPosition", new String[] { str6 });
    hashMap.put("ZeroPowerBehavior", new String[] { str9 });
    ToolboxUtil.addProperties(paramStringBuilder, paramHardwareType, str10, treeMap1, hashMap, null);
    TreeMap<Object, Object> treeMap2 = new TreeMap<Object, Object>();
    treeMap2.put("isBusy", null);
    ToolboxUtil.addFunctions(paramStringBuilder, paramHardwareType, str10, treeMap2);
    treeMap2.clear();
    if (paramList.size() > 1) {
      str10 = ((HardwareItem)paramList.get(0)).identifier;
      String str = ((HardwareItem)paramList.get(1)).identifier;
      paramStringBuilder.append("    <category name=\"Dual\">\n");
      ToolboxUtil.addDualPropertySetters(paramStringBuilder, paramHardwareType, "Power", "Number", str10, str7, str, str7);
      ToolboxUtil.addDualPropertySetters(paramStringBuilder, paramHardwareType, "Power", "Number", str10, str6, str, str6);
      ToolboxUtil.addDualPropertySetters(paramStringBuilder, paramHardwareType, "Mode", "RunMode", str10, str8, str, str8);
      ToolboxUtil.addDualPropertySetters(paramStringBuilder, paramHardwareType, "TargetPosition", "Number", str10, str6, str, str6);
      ToolboxUtil.addDualPropertySetters(paramStringBuilder, paramHardwareType, "ZeroPowerBehavior", "ZeroPowerBehavior", str10, str9, str, str9);
      paramStringBuilder.append("    </category>\n");
    } 
    List<HardwareItem> list = getHardwareItemsForDcMotorEx(paramList);
    if (!list.isEmpty()) {
      String str = ((HardwareItem)list.get(0)).identifier;
      paramStringBuilder.append("    <category name=\"Extended\">\n");
      treeMap1.clear();
      treeMap1.put("TargetPositionTolerance", "Number");
      treeMap1.put("Velocity", "Number");
      hashMap.clear();
      hashMap.put("TargetPositionTolerance", new String[] { str5 });
      hashMap.put("Velocity", new String[] { str5 });
      ToolboxUtil.addProperties(paramStringBuilder, paramHardwareType, str, treeMap1, hashMap, null);
      if (list.size() > 1) {
        String str12 = ((HardwareItem)list.get(0)).identifier;
        String str13 = ((HardwareItem)list.get(1)).identifier;
        paramStringBuilder.append("    <category name=\"Dual\">\n");
        ToolboxUtil.addDualPropertySetters(paramStringBuilder, paramHardwareType, "TargetPositionTolerance", "Number", str12, str5, str13, str5);
        ToolboxUtil.addDualPropertySetters(paramStringBuilder, paramHardwareType, "Velocity", "Number", str12, str5, str13, str5);
        paramStringBuilder.append("    </category>\n");
      } 
      LinkedHashMap<Object, Object> linkedHashMap5 = new LinkedHashMap<Object, Object>();
      linkedHashMap5.put("setMotorEnable", null);
      linkedHashMap5.put("setMotorDisable", null);
      linkedHashMap5.put("isMotorEnabled", null);
      hashMap = new LinkedHashMap<Object, Object>();
      hashMap.put("ANGULAR_RATE", str5);
      hashMap.put("ANGLE_UNIT", str4);
      linkedHashMap5.put("setVelocity_withAngleUnit", hashMap);
      LinkedHashMap<Object, Object> linkedHashMap4 = new LinkedHashMap<Object, Object>();
      linkedHashMap4.put("ANGLE_UNIT", str4);
      linkedHashMap5.put("getVelocity_withAngleUnit", linkedHashMap4);
      LinkedHashMap<Object, Object> linkedHashMap3 = new LinkedHashMap<Object, Object>();
      linkedHashMap3.put("P", ToolboxUtil.makeNumberShadow(10));
      linkedHashMap3.put("I", ToolboxUtil.makeNumberShadow(10));
      linkedHashMap3.put("D", ToolboxUtil.makeNumberShadow(10));
      linkedHashMap3.put("F", ToolboxUtil.makeNumberShadow(10));
      linkedHashMap5.put("setVelocityPIDFCoefficients", linkedHashMap3);
      linkedHashMap3 = new LinkedHashMap<Object, Object>();
      linkedHashMap3.put("P", ToolboxUtil.makeNumberShadow(5));
      linkedHashMap5.put("setPositionPIDFCoefficients", linkedHashMap3);
      linkedHashMap3 = new LinkedHashMap<Object, Object>();
      linkedHashMap3.put("RUN_MODE", str3);
      linkedHashMap3.put("PIDF_COEFFICIENTS", ToolboxUtil.makeVariableGetBlock("pidfCoefficients"));
      linkedHashMap5.put("setPIDFCoefficients", linkedHashMap3);
      linkedHashMap3 = new LinkedHashMap<Object, Object>();
      linkedHashMap3.put("RUN_MODE", str3);
      linkedHashMap5.put("getPIDFCoefficients", linkedHashMap3);
      LinkedHashMap<Object, Object> linkedHashMap2 = new LinkedHashMap<Object, Object>();
      linkedHashMap2.put("CURRENT_UNIT", str1);
      linkedHashMap5.put("getCurrent", linkedHashMap2);
      linkedHashMap2 = new LinkedHashMap<Object, Object>();
      linkedHashMap2.put("CURRENT", str2);
      linkedHashMap2.put("CURRENT_UNIT", str1);
      linkedHashMap5.put("setCurrentAlert", linkedHashMap2);
      LinkedHashMap<Object, Object> linkedHashMap1 = new LinkedHashMap<Object, Object>();
      linkedHashMap1.put("CURRENT_UNIT", str1);
      linkedHashMap5.put("getCurrentAlert", linkedHashMap1);
      linkedHashMap5.put("isOverCurrent", null);
      ToolboxUtil.addFunctions(paramStringBuilder, paramHardwareType, str, linkedHashMap5);
      paramStringBuilder.append("    </category>\n");
    } 
  }
  
  private static void addDigitalChannelCategoryToToolbox(StringBuilder paramStringBuilder, HardwareType paramHardwareType, List<HardwareItem> paramList) {
    String str1 = ((HardwareItem)paramList.get(0)).identifier;
    String str2 = ToolboxUtil.makeTypedEnumShadow(paramHardwareType, "mode");
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    treeMap.put("Mode", "DigitalChannelMode");
    treeMap.put("State", "Boolean");
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    hashMap.put("Mode", new String[] { str2 });
    hashMap.put("State", new String[] { ToolboxUtil.makeBooleanShadow(true) });
    ToolboxUtil.addProperties(paramStringBuilder, paramHardwareType, str1, treeMap, hashMap, null);
  }
  
  private static void addDistanceSensorCategoryToToolbox(StringBuilder paramStringBuilder, HardwareType paramHardwareType, List<HardwareItem> paramList) {
    String str = ((HardwareItem)paramList.get(0)).identifier;
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<Object, Object>();
    linkedHashMap.put("DISTANCE_UNIT", ToolboxUtil.makeTypedEnumShadow("navigation", "distanceUnit"));
    treeMap.put("getDistance", linkedHashMap);
    ToolboxUtil.addFunctions(paramStringBuilder, paramHardwareType, str, treeMap);
  }
  
  private static void addGyroSensorCategoryToToolbox(StringBuilder paramStringBuilder, HardwareType paramHardwareType, List<HardwareItem> paramList) {
    String str1 = ((HardwareItem)paramList.get(0)).identifier;
    String str2 = ToolboxUtil.makeTypedEnumShadow(paramHardwareType, "headingMode");
    TreeMap<Object, Object> treeMap2 = new TreeMap<Object, Object>();
    treeMap2.put("Heading", "Number");
    treeMap2.put("HeadingMode", "HeadingMode");
    treeMap2.put("I2cAddress7Bit", "Number");
    treeMap2.put("I2cAddress8Bit", "Number");
    treeMap2.put("IntegratedZValue", "Number");
    treeMap2.put("RawX", "Number");
    treeMap2.put("RawY", "Number");
    treeMap2.put("RawZ", "Number");
    treeMap2.put("RotationFraction", "Number");
    treeMap2.put("AngularVelocityAxes", "Array");
    treeMap2.put("AngularOrientationAxes", "Array");
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    hashMap.put("HeadingMode", new String[] { str2 });
    hashMap.put("I2cAddress7Bit", new String[] { ToolboxUtil.makeNumberShadow(8) });
    hashMap.put("I2cAddress8Bit", new String[] { ToolboxUtil.makeNumberShadow(16) });
    ToolboxUtil.addProperties(paramStringBuilder, paramHardwareType, str1, treeMap2, hashMap, null);
    TreeMap<Object, Object> treeMap1 = new TreeMap<Object, Object>();
    treeMap1.put("calibrate", null);
    treeMap1.put("isCalibrating", null);
    treeMap1.put("resetZAxisIntegrator", null);
    LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<Object, Object>();
    linkedHashMap.put("ANGLE_UNIT", ToolboxUtil.makeTypedEnumShadow("navigation", "angleUnit"));
    treeMap1.put("getAngularVelocity", linkedHashMap);
    linkedHashMap = new LinkedHashMap<Object, Object>();
    linkedHashMap.put("AXES_REFERENCE", ToolboxUtil.makeTypedEnumShadow("navigation", "axesReference"));
    linkedHashMap.put("AXES_ORDER", ToolboxUtil.makeTypedEnumShadow("navigation", "axesOrder"));
    linkedHashMap.put("ANGLE_UNIT", ToolboxUtil.makeTypedEnumShadow("navigation", "angleUnit"));
    treeMap1.put("getAngularOrientation", linkedHashMap);
    ToolboxUtil.addFunctions(paramStringBuilder, paramHardwareType, str1, treeMap1);
  }
  
  private static void addHardwareCategoryToToolbox(StringBuilder paramStringBuilder, HardwareType paramHardwareType, List<HardwareItem> paramList, AssetManager paramAssetManager) throws IOException {
    if (paramList != null && paramList.size() > 0) {
      paramStringBuilder.append("  <category name=\"");
      paramStringBuilder.append(paramHardwareType.toolboxCategoryName);
      paramStringBuilder.append("\">\n");
      switch (paramHardwareType) {
        default:
          paramStringBuilder = new StringBuilder();
          paramStringBuilder.append("Unexpected hardware type ");
          paramStringBuilder.append(paramHardwareType);
          throw new IllegalArgumentException(paramStringBuilder.toString());
        case null:
          addVoltageSensorCategoryToToolbox(paramStringBuilder, paramHardwareType, paramList);
          break;
        case null:
          addUltrasonicSensorCategoryToToolbox(paramStringBuilder, paramHardwareType, paramList);
          break;
        case null:
          addTouchSensorCategoryToToolbox(paramStringBuilder, paramHardwareType, paramList);
          break;
        case null:
          addServoControllerCategoryToToolbox(paramStringBuilder, paramHardwareType, paramList);
          break;
        case null:
          addServoCategoryToToolbox(paramStringBuilder, paramHardwareType, paramList);
          break;
        case null:
          addRevBlinkinLedDriverCategoryToToolbox(paramStringBuilder, paramAssetManager);
          break;
        case null:
          addOpticalDistanceSensorCategoryToToolbox(paramStringBuilder, paramHardwareType, paramList);
          break;
        case null:
          addMrI2cRangeSensorCategoryToToolbox(paramStringBuilder, paramHardwareType, paramList);
          break;
        case null:
          addMrI2cCompassSensorCategoryToToolbox(paramStringBuilder, paramHardwareType, paramList);
          break;
        case null:
          addLightSensorCategoryToToolbox(paramStringBuilder, paramHardwareType, paramList);
          break;
        case null:
          addLedCategoryToToolbox(paramStringBuilder, paramHardwareType, paramList);
          break;
        case null:
          addIrSeekerSensorCategoryToToolbox(paramStringBuilder, paramHardwareType, paramList);
          break;
        case null:
          addGyroSensorCategoryToToolbox(paramStringBuilder, paramHardwareType, paramList);
          break;
        case null:
          addDistanceSensorCategoryToToolbox(paramStringBuilder, paramHardwareType, paramList);
          break;
        case null:
          addDigitalChannelCategoryToToolbox(paramStringBuilder, paramHardwareType, paramList);
          break;
        case null:
          addDcMotorCategoryToToolbox(paramStringBuilder, paramHardwareType, paramList);
          break;
        case null:
          addCRServoCategoryToToolbox(paramStringBuilder, paramHardwareType, paramList);
          break;
        case null:
          addCompassSensorCategoryToToolbox(paramStringBuilder, paramHardwareType, paramList);
          break;
        case null:
          addColorSensorCategoryToToolbox(paramStringBuilder, paramHardwareType, paramList);
          break;
        case null:
          addColorRangeSensorCategoryToToolbox(paramStringBuilder, paramHardwareType, paramList);
          break;
        case null:
          addBNO055IMUCategoryToToolbox(paramStringBuilder, paramHardwareType, paramList);
          break;
        case null:
          addAnalogOutputCategoryToToolbox(paramStringBuilder, paramHardwareType, paramList);
          break;
        case null:
          addAnalogInputCategoryToToolbox(paramStringBuilder, paramHardwareType, paramList);
          break;
        case null:
          addAccelerationSensorCategoryToToolbox(paramStringBuilder, paramHardwareType, paramList);
          break;
      } 
      paramStringBuilder.append("  </category>\n");
    } 
  }
  
  private static void addIrSeekerSensorCategoryToToolbox(StringBuilder paramStringBuilder, HardwareType paramHardwareType, List<HardwareItem> paramList) {
    String str1 = ((HardwareItem)paramList.get(0)).identifier;
    String str2 = ToolboxUtil.makeTypedEnumShadow(paramHardwareType, "mode");
    String str3 = ToolboxUtil.makeNumberShadow(0.003D);
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    treeMap.put("SignalDetectedThreshold", "Number");
    treeMap.put("Mode", "IrSeekerSensorMode");
    treeMap.put("IsSignalDetected", "Boolean");
    treeMap.put("Angle", "Number");
    treeMap.put("Strength", "Number");
    treeMap.put("I2cAddress7Bit", "Number");
    treeMap.put("I2cAddress8Bit", "Number");
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    hashMap.put("SignalDetectedThreshold", new String[] { str3 });
    hashMap.put("Mode", new String[] { str2 });
    hashMap.put("I2cAddress7Bit", new String[] { ToolboxUtil.makeNumberShadow(8) });
    hashMap.put("I2cAddress8Bit", new String[] { ToolboxUtil.makeNumberShadow(16) });
    ToolboxUtil.addProperties(paramStringBuilder, paramHardwareType, str1, treeMap, hashMap, null);
  }
  
  private static void addLanguage(String paramString1, String paramString2, StringBuilder paramStringBuilder1, StringBuilder paramStringBuilder2) {
    paramStringBuilder1.append("      ['");
    paramStringBuilder1.append(ProjectsUtil.escapeSingleQuotes(makeVisibleNameForDropdownItem(paramString1)));
    paramStringBuilder1.append("', '");
    paramStringBuilder1.append(ProjectsUtil.escapeSingleQuotes(paramString1));
    paramStringBuilder1.append("'],\n");
    paramStringBuilder2.append("  ['");
    paramStringBuilder2.append(ProjectsUtil.escapeSingleQuotes(paramString1));
    paramStringBuilder2.append("', 'The language code for ");
    paramStringBuilder2.append(ProjectsUtil.escapeSingleQuotes(paramString2));
    paramStringBuilder2.append(".'],\n");
  }
  
  private static void addLedCategoryToToolbox(StringBuilder paramStringBuilder, HardwareType paramHardwareType, List<HardwareItem> paramList) {
    String str = ((HardwareItem)paramList.get(0)).identifier;
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<Object, Object>();
    linkedHashMap.put("ENABLE", ToolboxUtil.makeBooleanShadow(true));
    treeMap.put("enableLed_Boolean", linkedHashMap);
    treeMap.put("isLightOn", null);
    ToolboxUtil.addFunctions(paramStringBuilder, paramHardwareType, str, treeMap);
  }
  
  private static void addLightSensorCategoryToToolbox(StringBuilder paramStringBuilder, HardwareType paramHardwareType, List<HardwareItem> paramList) {
    String str = ((HardwareItem)paramList.get(0)).identifier;
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    treeMap.put("LightDetected", "Number");
    treeMap.put("RawLightDetected", "Number");
    treeMap.put("RawLightDetectedMax", "Number");
    ToolboxUtil.addProperties(paramStringBuilder, paramHardwareType, str, treeMap, null, null);
    treeMap = new TreeMap<Object, Object>();
    LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<Object, Object>();
    linkedHashMap.put("ENABLE", ToolboxUtil.makeBooleanShadow(true));
    treeMap.put("enableLed_Boolean", linkedHashMap);
    ToolboxUtil.addFunctions(paramStringBuilder, paramHardwareType, str, treeMap);
  }
  
  private static void addMrI2cCompassSensorCategoryToToolbox(StringBuilder paramStringBuilder, HardwareType paramHardwareType, List<HardwareItem> paramList) {
    String str = ((HardwareItem)paramList.get(0)).identifier;
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    treeMap.put("Direction", "Number");
    treeMap.put("XAccel", "Number");
    treeMap.put("YAccel", "Number");
    treeMap.put("ZAccel", "Number");
    treeMap.put("XMagneticFlux", "Number");
    treeMap.put("YMagneticFlux", "Number");
    treeMap.put("ZMagneticFlux", "Number");
    treeMap.put("I2cAddress7Bit", "Number");
    treeMap.put("I2cAddress8Bit", "Number");
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    hashMap.put("I2cAddress7Bit", new String[] { ToolboxUtil.makeNumberShadow(8) });
    hashMap.put("I2cAddress8Bit", new String[] { ToolboxUtil.makeNumberShadow(16) });
    ToolboxUtil.addProperties(paramStringBuilder, paramHardwareType, str, treeMap, hashMap, null);
    treeMap = new TreeMap<Object, Object>();
    hashMap = new LinkedHashMap<Object, Object>();
    hashMap.put("COMPASS_MODE", ToolboxUtil.makeTypedEnumShadow(paramHardwareType, "compassMode"));
    treeMap.put("setMode_CompassMode", hashMap);
    treeMap.put("isCalibrating", null);
    treeMap.put("calibrationFailed", null);
    ToolboxUtil.addFunctions(paramStringBuilder, paramHardwareType, str, treeMap);
  }
  
  private static void addMrI2cRangeSensorCategoryToToolbox(StringBuilder paramStringBuilder, HardwareType paramHardwareType, List<HardwareItem> paramList) {
    String str = ((HardwareItem)paramList.get(0)).identifier;
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    treeMap.put("LightDetected", "Number");
    treeMap.put("RawLightDetected", "Number");
    treeMap.put("RawLightDetectedMax", "Number");
    treeMap.put("RawUltrasonic", "Number");
    treeMap.put("RawOptical", "Number");
    treeMap.put("CmUltrasonic", "Number");
    treeMap.put("CmOptical", "Number");
    treeMap.put("I2cAddress7Bit", "Number");
    treeMap.put("I2cAddress8Bit", "Number");
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    hashMap.put("I2cAddress7Bit", new String[] { ToolboxUtil.makeNumberShadow(8) });
    hashMap.put("I2cAddress8Bit", new String[] { ToolboxUtil.makeNumberShadow(16) });
    ToolboxUtil.addProperties(paramStringBuilder, paramHardwareType, str, treeMap, hashMap, null);
    treeMap = new TreeMap<Object, Object>();
    hashMap = new LinkedHashMap<Object, Object>();
    hashMap.put("UNIT", ToolboxUtil.makeTypedEnumShadow(paramHardwareType, "distanceUnit"));
    treeMap.put("getDistance_Number", hashMap);
    ToolboxUtil.addFunctions(paramStringBuilder, paramHardwareType, str, treeMap);
  }
  
  private static void addOpticalDistanceSensorCategoryToToolbox(StringBuilder paramStringBuilder, HardwareType paramHardwareType, List<HardwareItem> paramList) {
    String str = ((HardwareItem)paramList.get(0)).identifier;
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    treeMap.put("LightDetected", "Number");
    treeMap.put("RawLightDetected", "Number");
    treeMap.put("RawLightDetectedMax", "Number");
    ToolboxUtil.addProperties(paramStringBuilder, paramHardwareType, str, treeMap, null, null);
    treeMap = new TreeMap<Object, Object>();
    LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<Object, Object>();
    linkedHashMap.put("ENABLE", ToolboxUtil.makeBooleanShadow(true));
    treeMap.put("enableLed_Boolean", linkedHashMap);
    ToolboxUtil.addFunctions(paramStringBuilder, paramHardwareType, str, treeMap);
  }
  
  private static void addRevBlinkinLedDriverCategoryToToolbox(StringBuilder paramStringBuilder, AssetManager paramAssetManager) throws IOException {
    addAsset(paramStringBuilder, paramAssetManager, "toolbox/rev_blinkin_led_driver.xml");
  }
  
  private static void addServoCategoryToToolbox(StringBuilder paramStringBuilder, HardwareType paramHardwareType, List<HardwareItem> paramList) {
    String str = ((HardwareItem)paramList.get(0)).identifier;
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    treeMap.put("Direction", "Direction");
    treeMap.put("Position", "Number");
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    hashMap.put("Direction", new String[] { ToolboxUtil.makeTypedEnumShadow(paramHardwareType, "direction") });
    hashMap.put("Position", new String[] { ToolboxUtil.makeNumberShadow(0) });
    ToolboxUtil.addProperties(paramStringBuilder, paramHardwareType, str, treeMap, hashMap, null);
    treeMap = new TreeMap<Object, Object>();
    hashMap = new LinkedHashMap<Object, Object>();
    hashMap.put("MIN", ToolboxUtil.makeNumberShadow(0.2D));
    hashMap.put("MAX", ToolboxUtil.makeNumberShadow(0.8D));
    treeMap.put("scaleRange_Number", hashMap);
    ToolboxUtil.addFunctions(paramStringBuilder, paramHardwareType, str, treeMap);
  }
  
  private static void addServoControllerCategoryToToolbox(StringBuilder paramStringBuilder, HardwareType paramHardwareType, List<HardwareItem> paramList) {
    String str = ((HardwareItem)paramList.get(0)).identifier;
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    treeMap.put("PwmStatus", "PwmStatus");
    ToolboxUtil.addProperties(paramStringBuilder, paramHardwareType, str, treeMap, null, null);
    treeMap = new TreeMap<Object, Object>();
    treeMap.put("pwmEnable", null);
    treeMap.put("pwmDisable", null);
    ToolboxUtil.addFunctions(paramStringBuilder, paramHardwareType, str, treeMap);
  }
  
  private static void addTouchSensorCategoryToToolbox(StringBuilder paramStringBuilder, HardwareType paramHardwareType, List<HardwareItem> paramList) {
    String str = ((HardwareItem)paramList.get(0)).identifier;
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    treeMap.put("IsPressed", "Boolean");
    ToolboxUtil.addProperties(paramStringBuilder, paramHardwareType, str, treeMap, null, null);
  }
  
  private static void addUltrasonicSensorCategoryToToolbox(StringBuilder paramStringBuilder, HardwareType paramHardwareType, List<HardwareItem> paramList) {
    String str = ((HardwareItem)paramList.get(0)).identifier;
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    treeMap.put("UltrasonicLevel", "Number");
    ToolboxUtil.addProperties(paramStringBuilder, paramHardwareType, str, treeMap, null, null);
  }
  
  private static void addVoltageSensorCategoryToToolbox(StringBuilder paramStringBuilder, HardwareType paramHardwareType, List<HardwareItem> paramList) {
    String str = ((HardwareItem)paramList.get(0)).identifier;
    TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
    treeMap.put("Voltage", "Number");
    ToolboxUtil.addProperties(paramStringBuilder, paramHardwareType, str, treeMap, null, null);
  }
  
  private static void appendCreateDropdownFunction(StringBuilder paramStringBuilder, String paramString, List<HardwareItem> paramList) {
    paramStringBuilder.append("function ");
    paramStringBuilder.append(paramString);
    paramStringBuilder.append("() {\n");
    paramStringBuilder.append("  var CHOICES = [\n");
    for (HardwareItem hardwareItem : paramList) {
      paramStringBuilder.append("      ['");
      paramStringBuilder.append(ProjectsUtil.escapeSingleQuotes(hardwareItem.visibleName));
      paramStringBuilder.append("', '");
      paramStringBuilder.append(hardwareItem.identifier);
      paramStringBuilder.append("'],\n");
    } 
    paramStringBuilder.append("  ];\n");
    paramStringBuilder.append("  return createFieldDropdown(CHOICES);\n");
    paramStringBuilder.append("}\n\n");
  }
  
  private static Set<String> buildReservedWordsForFtcJava() {
    HashSet<String> hashSet = new HashSet();
    hashSet.add("Color");
    hashSet.add("SoundPlayer");
    hashSet.add("ModernRoboticsI2cCompassSensor");
    hashSet.add("ModernRoboticsI2cGyro");
    hashSet.add("ModernRoboticsI2cRangeSensor");
    hashSet.add("BNO055IMU");
    hashSet.add("JustLoggingAccelerationIntegrator");
    hashSet.add("RevBlinkinLedDriver");
    hashSet.add("Autonomous");
    hashSet.add("Disabled");
    hashSet.add("LinearOpMode");
    hashSet.add("TeleOp");
    hashSet.add("AccelerationSensor");
    hashSet.add("AnalogInput");
    hashSet.add("AnalogOutput");
    hashSet.add("CRServo");
    hashSet.add("ColorSensor");
    hashSet.add("CompassSensor");
    hashSet.add("DcMotor");
    hashSet.add("DcMotorEx");
    hashSet.add("DcMotorSimple");
    hashSet.add("DigitalChannel");
    hashSet.add("DistanceSensor");
    hashSet.add("GyroSensor");
    hashSet.add("Gyroscope");
    hashSet.add("I2cAddr");
    hashSet.add("I2cAddrConfig");
    hashSet.add("I2cAddressableDevice");
    hashSet.add("IrSeekerSensor");
    hashSet.add("LED");
    hashSet.add("Light");
    hashSet.add("LightSensor");
    hashSet.add("MotorControlAlgorithm");
    hashSet.add("NormalizedColorSensor");
    hashSet.add("NormalizedRGBA");
    hashSet.add("OpticalDistanceSensor");
    hashSet.add("OrientationSensor");
    hashSet.add("PIDCoefficients");
    hashSet.add("PIDFCoefficients");
    hashSet.add("PWMOutput");
    hashSet.add("Servo");
    hashSet.add("ServoController");
    hashSet.add("SwitchableLight");
    hashSet.add("TouchSensor");
    hashSet.add("UltrasonicSensor");
    hashSet.add("VoltageSensor");
    hashSet.add("ElapsedTime");
    hashSet.add("Range");
    hashSet.add("ReadWriteFile");
    hashSet.add("RobotLog");
    hashSet.add("ArrayList");
    hashSet.add("Collections");
    hashSet.add("List");
    hashSet.add("ClassFactory");
    hashSet.add("JavaUtil");
    hashSet.add("AndroidAccelerometer");
    hashSet.add("AndroidGyroscope");
    hashSet.add("AndroidOrientation");
    hashSet.add("AndroidSoundPool");
    hashSet.add("AndroidTextToSpeech");
    hashSet.add("WebcamName");
    hashSet.add("MatrixF");
    hashSet.add("OpenGLMatrix");
    hashSet.add("VectorF");
    hashSet.add("Acceleration");
    hashSet.add("AngleUnit");
    hashSet.add("AngularVelocity");
    hashSet.add("AxesOrder");
    hashSet.add("AxesReference");
    hashSet.add("Axis");
    hashSet.add("DistanceUnit");
    hashSet.add("MagneticFlux");
    hashSet.add("Orientation");
    hashSet.add("Position");
    hashSet.add("Quaternion");
    hashSet.add("RelicRecoveryVuMark");
    hashSet.add("Temperature");
    hashSet.add("TempUnit");
    hashSet.add("UnnormalizedAngleUnit");
    hashSet.add("Velocity");
    hashSet.add("VuforiaBase");
    hashSet.add("VuforiaLocalizer");
    hashSet.add("VuforiaRelicRecovery");
    hashSet.add("VuforiaRoverRuckus");
    hashSet.add("VuforiaTrackable");
    hashSet.add("VuforiaTrackableDefaultListener");
    hashSet.add("VuforiaTrackables");
    hashSet.add("AppUtil");
    hashSet.add("Recognition");
    hashSet.add("TfodBase");
    hashSet.add("TfodRoverRuckus");
    hashSet.add("waitForStart");
    hashSet.add("idle");
    hashSet.add("sleep");
    hashSet.add("opModeIsActive");
    hashSet.add("isStarted");
    hashSet.add("isStopRequested");
    hashSet.add("init");
    hashSet.add("init_loop");
    hashSet.add("start");
    hashSet.add("loop");
    hashSet.add("stop");
    hashSet.add("handleLoop");
    hashSet.add("LinearOpModeHelper");
    hashSet.add("internalPostInitLoop");
    hashSet.add("internalPostLoop");
    hashSet.add("waitOneFullHardwareCycle");
    hashSet.add("waitForNextHardwareCycle");
    hashSet.add("OpMode");
    hashSet.add("gamepad1");
    hashSet.add("gamepad2");
    hashSet.add("telemetry");
    hashSet.add("time");
    hashSet.add("requestOpModeStop");
    hashSet.add("getRuntime");
    hashSet.add("resetStartTime");
    hashSet.add("updateTelemetry");
    hashSet.add("msStuckDetectInit");
    hashSet.add("msStuckDetectInitLoop");
    hashSet.add("msStuckDetectStart");
    hashSet.add("msStuckDetectLoop");
    hashSet.add("msStuckDetectStop");
    hashSet.add("internalPreInit");
    hashSet.add("internalOpModeServices");
    hashSet.add("internalUpdateTelemetryNow");
    hashSet.add("abstract");
    hashSet.add("assert");
    hashSet.add("boolean");
    hashSet.add("break");
    hashSet.add("byte");
    hashSet.add("case");
    hashSet.add("catch");
    hashSet.add("char");
    hashSet.add("class");
    hashSet.add("const");
    hashSet.add("continue");
    hashSet.add("default");
    hashSet.add("do");
    hashSet.add("double");
    hashSet.add("else");
    hashSet.add("enum");
    hashSet.add("extends");
    hashSet.add("final");
    hashSet.add("finally");
    hashSet.add("float");
    hashSet.add("for");
    hashSet.add("goto");
    hashSet.add("if");
    hashSet.add("implements");
    hashSet.add("import");
    hashSet.add("instanceof");
    hashSet.add("int");
    hashSet.add("interface");
    hashSet.add("long");
    hashSet.add("native");
    hashSet.add("new");
    hashSet.add("package");
    hashSet.add("private");
    hashSet.add("protected");
    hashSet.add("public");
    hashSet.add("return");
    hashSet.add("short");
    hashSet.add("static");
    hashSet.add("strictfp");
    hashSet.add("super");
    hashSet.add("switch");
    hashSet.add("synchronized");
    hashSet.add("this");
    hashSet.add("throw");
    hashSet.add("throws");
    hashSet.add("transient");
    hashSet.add("try");
    hashSet.add("void");
    hashSet.add("volatile");
    hashSet.add("while");
    hashSet.add("AbstractMethodError");
    hashSet.add("Appendable");
    hashSet.add("ArithmeticException");
    hashSet.add("ArrayIndexOutOfBoundsException");
    hashSet.add("ArrayStoreException");
    hashSet.add("AssertionError");
    hashSet.add("AutoCloseable");
    hashSet.add("Boolean");
    hashSet.add("BootstrapMethodError");
    hashSet.add("Byte");
    hashSet.add("Character");
    hashSet.add("Character.Subset");
    hashSet.add("Character.UnicodeBlock");
    hashSet.add("Character.UnicodeScript");
    hashSet.add("CharSequence");
    hashSet.add("Class");
    hashSet.add("ClassCastException");
    hashSet.add("ClassCircularityError");
    hashSet.add("ClassFormatError");
    hashSet.add("ClassLoader");
    hashSet.add("ClassNotFoundException");
    hashSet.add("ClassValue");
    hashSet.add("Cloneable");
    hashSet.add("CloneNotSupportedException");
    hashSet.add("Comparable");
    hashSet.add("Compiler");
    hashSet.add("Deprecated");
    hashSet.add("Double");
    hashSet.add("Enum");
    hashSet.add("Enum");
    hashSet.add("EnumConstantNotPresentException");
    hashSet.add("Error");
    hashSet.add("Exception");
    hashSet.add("ExceptionInInitializerError");
    hashSet.add("Float");
    hashSet.add("FunctionalInterface");
    hashSet.add("IllegalAccessError");
    hashSet.add("IllegalAccessException");
    hashSet.add("IllegalArgumentException");
    hashSet.add("IllegalMonitorStateException");
    hashSet.add("IllegalStateException");
    hashSet.add("IllegalThreadStateException");
    hashSet.add("IncompatibleClassChangeError");
    hashSet.add("IndexOutOfBoundsException");
    hashSet.add("InheritableThreadLocal");
    hashSet.add("InstantiationError");
    hashSet.add("InstantiationException");
    hashSet.add("Integer");
    hashSet.add("InternalError");
    hashSet.add("InterruptedException");
    hashSet.add("Iterable");
    hashSet.add("LinkageError");
    hashSet.add("Long");
    hashSet.add("Math");
    hashSet.add("NegativeArraySizeException");
    hashSet.add("NoClassDefFoundError");
    hashSet.add("NoSuchFieldError");
    hashSet.add("NoSuchFieldException");
    hashSet.add("NoSuchMethodError");
    hashSet.add("NoSuchMethodException");
    hashSet.add("NullPointerException");
    hashSet.add("Number");
    hashSet.add("NumberFormatException");
    hashSet.add("Object");
    hashSet.add("OutOfMemoryError");
    hashSet.add("Override");
    hashSet.add("Package");
    hashSet.add("Process");
    hashSet.add("ProcessBuilder");
    hashSet.add("ProcessBuilder.Redirect");
    hashSet.add("ProcessBuilder.Redirect.Type");
    hashSet.add("Readable");
    hashSet.add("ReflectiveOperationException");
    hashSet.add("Runnable");
    hashSet.add("Runtime");
    hashSet.add("RuntimeException");
    hashSet.add("RuntimePermission");
    hashSet.add("SafeVarargs");
    hashSet.add("SecurityException");
    hashSet.add("SecurityManager");
    hashSet.add("Short");
    hashSet.add("StackOverflowError");
    hashSet.add("StackTraceElement");
    hashSet.add("StrictMath");
    hashSet.add("String");
    hashSet.add("StringBuffer");
    hashSet.add("StringBuilder");
    hashSet.add("StringIndexOutOfBoundsException");
    hashSet.add("SuppressWarnings");
    hashSet.add("System");
    hashSet.add("Thread");
    hashSet.add("ThreadDeath");
    hashSet.add("ThreadGroup");
    hashSet.add("ThreadLocal");
    hashSet.add("Thread.State");
    hashSet.add("Thread.UncaughtExceptionHandler");
    hashSet.add("Throwable");
    hashSet.add("TypeNotPresentException");
    hashSet.add("UnknownError");
    hashSet.add("UnsatisfiedLinkError");
    hashSet.add("UnsupportedClassVersionError");
    hashSet.add("UnsupportedOperationException");
    hashSet.add("VerifyError");
    hashSet.add("VirtualMachineError");
    hashSet.add("Void");
    return hashSet;
  }
  
  private static String convertReturnValue(Class paramClass) {
    return (paramClass.equals(byte.class) || paramClass.equals(Byte.class) || paramClass.equals(short.class) || paramClass.equals(Short.class) || paramClass.equals(int.class) || paramClass.equals(Integer.class) || paramClass.equals(long.class) || paramClass.equals(Long.class) || paramClass.equals(float.class) || paramClass.equals(Float.class) || paramClass.equals(double.class) || paramClass.equals(Double.class)) ? "Number" : "";
  }
  
  public static String fetchJavaScriptForHardware() throws IOException {
    return fetchJavaScriptForHardware(HardwareItemMap.newHardwareItemMap());
  }
  
  public static String fetchJavaScriptForHardware(HardwareItemMap paramHardwareItemMap) throws IOException {
    AssetManager assetManager = AppUtil.getDefContext().getAssets();
    StringBuilder stringBuilder9 = new StringBuilder();
    stringBuilder9.append("\n");
    Map<Capability, Boolean> map = getCapabilities(paramHardwareItemMap);
    HashSet<String> hashSet3 = new HashSet();
    HashSet<String> hashSet1 = new HashSet();
    String str5 = generateToolbox(paramHardwareItemMap, map, assetManager, hashSet3, hashSet1).replace("\n", " ").replaceAll("\\> +\\<", "><");
    stringBuilder9.append("var currentGameName = 'Ultimate Goal';\n");
    stringBuilder9.append("var tfodCurrentGameBlocksFirstName = 'TensorFlowObjectDetectionUltimateGoal';\n");
    stringBuilder9.append("var vuforiaCurrentGameBlocksFirstName = 'VuforiaUltimateGoal';\n");
    stringBuilder9.append("\n");
    stringBuilder9.append("var methodLookupStrings = [\n");
    for (String str : hashSet1) {
      stringBuilder9.append("  '");
      stringBuilder9.append(str);
      stringBuilder9.append("',\n");
    } 
    stringBuilder9.append("];\n\n");
    stringBuilder9.append("function isValidProjectName(projectName) {\n");
    stringBuilder9.append("  if (projectName) {\n");
    stringBuilder9.append("    return /");
    stringBuilder9.append("^[a-zA-Z0-9 \\!\\#\\$\\%\\&\\'\\(\\)\\+\\,\\-\\.\\;\\=\\@\\[\\]\\^_\\{\\}\\~]+$");
    stringBuilder9.append("/.test(projectName);\n");
    stringBuilder9.append("  }\n");
    stringBuilder9.append("  return false;\n");
    stringBuilder9.append("}\n\n");
    stringBuilder9.append("function isValidSoundName(soundName) {\n");
    stringBuilder9.append("  if (soundName) {\n");
    stringBuilder9.append("    return /");
    stringBuilder9.append("^[a-zA-Z0-9 \\!\\#\\$\\%\\&\\'\\(\\)\\+\\,\\-\\.\\;\\=\\@\\[\\]\\^_\\{\\}\\~]+$");
    stringBuilder9.append("/.test(soundName);\n");
    stringBuilder9.append("  }\n");
    stringBuilder9.append("  return false;\n");
    stringBuilder9.append("}\n\n");
    StringBuilder stringBuilder2 = new StringBuilder();
    StringBuilder stringBuilder5 = new StringBuilder();
    stringBuilder2.append("var BLINKIN_PATTERN_TOOLTIPS = [\n");
    stringBuilder5.append("var BLINKIN_PATTERN_FROM_TEXT_TOOLTIP =\n");
    stringBuilder5.append("    'Returns the pattern associated with the given text. Valid input is ' +\n");
    RevBlinkinLedDriver.BlinkinPattern[] arrayOfBlinkinPattern = RevBlinkinLedDriver.BlinkinPattern.values();
    RevBlinkinLedDriver.BlinkinPattern blinkinPattern = arrayOfBlinkinPattern[0];
    null = 0;
    while (null < arrayOfBlinkinPattern.length - 1) {
      stringBuilder2.append("  ['");
      stringBuilder2.append(blinkinPattern);
      stringBuilder2.append("', 'The BlinkinPattern value ");
      stringBuilder2.append(blinkinPattern);
      stringBuilder2.append(".'],\n");
      stringBuilder5.append("    '");
      stringBuilder5.append(blinkinPattern);
      stringBuilder5.append(", ' +\n");
      blinkinPattern = arrayOfBlinkinPattern[++null];
    } 
    stringBuilder2.append("];\n");
    stringBuilder5.append("    'or ");
    stringBuilder5.append(blinkinPattern);
    stringBuilder5.append(".';\n");
    stringBuilder9.append(stringBuilder2);
    stringBuilder9.append("\n");
    stringBuilder9.append(stringBuilder5);
    stringBuilder9.append("\n");
    TreeMap<Object, Object> treeMap2 = new TreeMap<Object, Object>();
    TreeMap<Object, Object> treeMap1 = new TreeMap<Object, Object>();
    for (Locale locale1 : AvailableTtsLocalesProvider.getInstance().getAvailableTtsLocales()) {
      treeMap2.put(locale1.getLanguage(), locale1.getDisplayLanguage());
      String str = locale1.getCountry();
      if (!str.isEmpty())
        treeMap1.put(str, locale1.getDisplayCountry()); 
    } 
    Locale locale = Locale.getDefault();
    String str6 = locale.getLanguage();
    StringBuilder stringBuilder11 = new StringBuilder();
    StringBuilder stringBuilder12 = new StringBuilder();
    stringBuilder11.append("function createLanguageCodeDropdown() {\n");
    stringBuilder11.append("  var CHOICES = [\n");
    stringBuilder12.append("var LANGUAGE_CODE_TOOLTIPS = [\n");
    addLanguage(str6, locale.getDisplayLanguage(), stringBuilder11, stringBuilder12);
    for (Map.Entry<Object, Object> entry : treeMap2.entrySet()) {
      String str = (String)entry.getKey();
      if (!str.equals(str6))
        addLanguage(str, (String)entry.getValue(), stringBuilder11, stringBuilder12); 
    } 
    stringBuilder11.append("  ];\n");
    stringBuilder11.append("  return createFieldDropdown(CHOICES);\n");
    stringBuilder11.append("}\n\n");
    stringBuilder12.append("];\n");
    stringBuilder9.append(stringBuilder11);
    stringBuilder9.append(stringBuilder12);
    stringBuilder9.append("\n");
    String str2 = locale.getCountry();
    StringBuilder stringBuilder8 = new StringBuilder();
    StringBuilder stringBuilder10 = new StringBuilder();
    stringBuilder8.append("function createCountryCodeDropdown() {\n");
    stringBuilder8.append("  var CHOICES = [\n");
    stringBuilder10.append("var COUNTRY_CODE_TOOLTIPS = [\n");
    addCountry(str2, locale.getDisplayCountry(), stringBuilder8, stringBuilder10);
    for (Map.Entry<Object, Object> entry : treeMap1.entrySet()) {
      String str = (String)entry.getKey();
      if (!str.equals(str2))
        addCountry(str, (String)entry.getValue(), stringBuilder8, stringBuilder10); 
    } 
    stringBuilder8.append("  ];\n");
    stringBuilder8.append("  return createFieldDropdown(CHOICES);\n");
    stringBuilder8.append("}\n\n");
    stringBuilder10.append("];\n");
    stringBuilder9.append(stringBuilder8);
    stringBuilder9.append(stringBuilder10);
    stringBuilder9.append("\n");
    StringBuilder stringBuilder1 = new StringBuilder();
    StringBuilder stringBuilder4 = new StringBuilder();
    stringBuilder1.append("function createSkyStoneSoundResourceDropdown() {\n");
    stringBuilder1.append("  var CHOICES = [\n");
    stringBuilder4.append("var SKY_STONE_SOUND_RESOURCE_TOOLTIPS = [\n");
    ArrayList<String> arrayList2 = new ArrayList();
    Field[] arrayOfField = R.raw.class.getFields();
    null = arrayOfField.length;
    for (null = 0; null < null; null++) {
      String str = arrayOfField[null].getName();
      if (str.toUpperCase().startsWith("SS_"))
        arrayList2.add(str); 
    } 
    Collections.sort(arrayList2);
    for (String str : arrayList2) {
      stringBuilder1.append("      ['");
      stringBuilder1.append(ProjectsUtil.escapeSingleQuotes(makeVisibleNameForDropdownItem(str)));
      stringBuilder1.append("', '");
      stringBuilder1.append(ProjectsUtil.escapeSingleQuotes(str));
      stringBuilder1.append("'],\n");
      stringBuilder4.append("  ['");
      stringBuilder4.append(ProjectsUtil.escapeSingleQuotes(str));
      stringBuilder4.append("', 'The SoundResource value ");
      stringBuilder4.append(ProjectsUtil.escapeSingleQuotes(str));
      stringBuilder4.append(".'],\n");
    } 
    stringBuilder1.append("  ];\n");
    stringBuilder1.append("  return createFieldDropdown(CHOICES);\n");
    stringBuilder1.append("}\n\n");
    stringBuilder4.append("];\n");
    stringBuilder9.append(stringBuilder1);
    stringBuilder9.append(stringBuilder4);
    stringBuilder9.append("\n");
    stringBuilder9.append("var androidSoundPoolRawResPrefix = '");
    stringBuilder9.append("RawRes:");
    stringBuilder9.append("';\n");
    for (Identifier identifier : Identifier.values()) {
      if (identifier.variableForJavaScript != null) {
        stringBuilder9.append("var ");
        stringBuilder9.append(identifier.variableForJavaScript);
        stringBuilder9.append(" = '");
        stringBuilder9.append(identifier.identifierForJavaScript);
        stringBuilder9.append("';\n");
      } 
      if (identifier.variableForFtcJava != null) {
        stringBuilder9.append("var ");
        stringBuilder9.append(identifier.variableForFtcJava);
        stringBuilder9.append(" = '");
        stringBuilder9.append(identifier.identifierForFtcJava);
        stringBuilder9.append("';\n");
      } 
    } 
    stringBuilder9.append("\n");
    stringBuilder9.append("function createWebcamDeviceNameDropdown() {\n");
    stringBuilder9.append("  var CHOICES = [\n");
    for (HardwareItem hardwareItem : paramHardwareItemMap.getHardwareItems(HardwareType.WEBCAM_NAME)) {
      stringBuilder9.append("    ['");
      stringBuilder9.append(ProjectsUtil.escapeSingleQuotes(hardwareItem.visibleName));
      stringBuilder9.append("', '");
      stringBuilder9.append(ProjectsUtil.escapeSingleQuotes(hardwareItem.deviceName));
      stringBuilder9.append("'],\n");
    } 
    stringBuilder9.append("  ];\n");
    stringBuilder9.append("  return createFieldDropdown(CHOICES);\n");
    stringBuilder9.append("}\n\n");
    stringBuilder9.append("var switchableCameraName = '");
    stringBuilder9.append("Switchable Camera");
    stringBuilder9.append("';\n");
    stringBuilder1 = new StringBuilder();
    stringBuilder4 = new StringBuilder();
    stringBuilder1.append("function createTfodRoverRuckusLabelDropdown() {\n");
    stringBuilder1.append("  var CHOICES = [\n");
    stringBuilder4.append("var TFOD_ROVER_RUCKUS_LABEL_TOOLTIPS = [\n");
    for (String str : TfodRoverRuckus.LABELS) {
      stringBuilder1.append("      ['");
      stringBuilder1.append(ProjectsUtil.escapeSingleQuotes(makeVisibleNameForDropdownItem(str)));
      stringBuilder1.append("', '");
      stringBuilder1.append(ProjectsUtil.escapeSingleQuotes(str));
      stringBuilder1.append("'],\n");
      stringBuilder4.append("  ['");
      stringBuilder4.append(ProjectsUtil.escapeSingleQuotes(str));
      stringBuilder4.append("', 'The Label value ");
      stringBuilder4.append(ProjectsUtil.escapeSingleQuotes(str));
      stringBuilder4.append(".'],\n");
    } 
    stringBuilder1.append("  ];\n");
    stringBuilder1.append("  return createFieldDropdown(CHOICES);\n");
    stringBuilder1.append("}\n\n");
    stringBuilder4.append("];\n");
    stringBuilder9.append(stringBuilder1);
    stringBuilder9.append(stringBuilder4);
    stringBuilder9.append("\n");
    stringBuilder1 = new StringBuilder();
    stringBuilder4 = new StringBuilder();
    stringBuilder1.append("function createVuforiaRoverRuckusTrackableNameDropdown() {\n");
    stringBuilder1.append("  var CHOICES = [\n");
    stringBuilder4.append("var VUFORIA_ROVER_RUCKUS_TRACKABLE_NAME_TOOLTIPS = [\n");
    String[] arrayOfString4 = VuforiaRoverRuckus.TRACKABLE_NAMES;
    String str1 = "', 'The Label value ";
    null = arrayOfString4.length;
    for (null = 0; null < null; null++) {
      String str = arrayOfString4[null];
      stringBuilder1.append("      ['");
      stringBuilder1.append(ProjectsUtil.escapeSingleQuotes(makeVisibleNameForDropdownItem(str)));
      stringBuilder1.append("', '");
      stringBuilder1.append(ProjectsUtil.escapeSingleQuotes(str));
      stringBuilder1.append("'],\n");
      stringBuilder4.append("  ['");
      stringBuilder4.append(ProjectsUtil.escapeSingleQuotes(str));
      stringBuilder4.append("', 'The TrackableName value ");
      stringBuilder4.append(ProjectsUtil.escapeSingleQuotes(str));
      stringBuilder4.append(".'],\n");
    } 
    stringBuilder1.append("  ];\n");
    stringBuilder1.append("  return createFieldDropdown(CHOICES);\n");
    stringBuilder1.append("}\n\n");
    stringBuilder4.append("];\n");
    stringBuilder9.append(stringBuilder1);
    stringBuilder9.append(stringBuilder4);
    stringBuilder9.append("\n");
    StringBuilder stringBuilder7 = new StringBuilder();
    stringBuilder10 = new StringBuilder();
    stringBuilder7.append("function createTfodSkyStoneLabelDropdown() {\n");
    stringBuilder7.append("  var CHOICES = [\n");
    stringBuilder10.append("var TFOD_SKY_STONE_LABEL_TOOLTIPS = [\n");
    String[] arrayOfString1 = TfodSkyStone.LABELS;
    String str4 = "', 'The TrackableName value ";
    null = arrayOfString1.length;
    for (null = 0; null < null; null++) {
      String str = arrayOfString1[null];
      stringBuilder7.append("      ['");
      stringBuilder7.append(ProjectsUtil.escapeSingleQuotes(makeVisibleNameForDropdownItem(str)));
      stringBuilder7.append("', '");
      stringBuilder7.append(ProjectsUtil.escapeSingleQuotes(str));
      stringBuilder7.append("'],\n");
      stringBuilder10.append("  ['");
      stringBuilder10.append(ProjectsUtil.escapeSingleQuotes(str));
      stringBuilder10.append(str1);
      stringBuilder10.append(ProjectsUtil.escapeSingleQuotes(str));
      stringBuilder10.append(".'],\n");
    } 
    stringBuilder7.append("  ];\n");
    stringBuilder7.append("  return createFieldDropdown(CHOICES);\n");
    stringBuilder7.append("}\n\n");
    stringBuilder10.append("];\n");
    stringBuilder9.append(stringBuilder7);
    stringBuilder9.append(stringBuilder10);
    stringBuilder9.append("\n");
    stringBuilder10 = new StringBuilder();
    stringBuilder11 = new StringBuilder();
    stringBuilder10.append("function createVuforiaSkyStoneTrackableNameDropdown() {\n");
    stringBuilder10.append("  var CHOICES = [\n");
    stringBuilder11.append("var VUFORIA_SKY_STONE_TRACKABLE_NAME_TOOLTIPS = [\n");
    String[] arrayOfString3 = VuforiaSkyStone.TRACKABLE_NAMES;
    null = arrayOfString3.length;
    null = 0;
    str3 = str4;
    String[] arrayOfString2 = arrayOfString3;
    while (null < null) {
      String str = arrayOfString2[null];
      stringBuilder10.append("      ['");
      stringBuilder10.append(ProjectsUtil.escapeSingleQuotes(makeVisibleNameForDropdownItem(str)));
      stringBuilder10.append("', '");
      stringBuilder10.append(ProjectsUtil.escapeSingleQuotes(str));
      stringBuilder10.append("'],\n");
      stringBuilder11.append("  ['");
      stringBuilder11.append(ProjectsUtil.escapeSingleQuotes(str));
      stringBuilder11.append(str3);
      stringBuilder11.append(ProjectsUtil.escapeSingleQuotes(str));
      stringBuilder11.append(".'],\n");
      null++;
    } 
    stringBuilder10.append("  ];\n");
    stringBuilder10.append("  return createFieldDropdown(CHOICES);\n");
    stringBuilder10.append("}\n\n");
    stringBuilder11.append("];\n");
    stringBuilder9.append(stringBuilder10);
    stringBuilder9.append(stringBuilder11);
    stringBuilder9.append("\n");
    StringBuilder stringBuilder3 = new StringBuilder();
    StringBuilder stringBuilder6 = new StringBuilder();
    stringBuilder3.append("function createTfodCurrentGameLabelDropdown() {\n");
    stringBuilder3.append("  var CHOICES = [\n");
    stringBuilder6.append("var TFOD_CURRENT_GAME_LABEL_TOOLTIPS = [\n");
    for (String str : TfodCurrentGame.LABELS) {
      stringBuilder3.append("      ['");
      stringBuilder3.append(ProjectsUtil.escapeSingleQuotes(makeVisibleNameForDropdownItem(str)));
      stringBuilder3.append("', '");
      stringBuilder3.append(ProjectsUtil.escapeSingleQuotes(str));
      stringBuilder3.append("'],\n");
      stringBuilder6.append("  ['");
      stringBuilder6.append(ProjectsUtil.escapeSingleQuotes(str));
      stringBuilder6.append(str1);
      stringBuilder6.append(ProjectsUtil.escapeSingleQuotes(str));
      stringBuilder6.append(".'],\n");
    } 
    stringBuilder3.append("  ];\n");
    stringBuilder3.append("  return createFieldDropdown(CHOICES);\n");
    stringBuilder3.append("}\n\n");
    stringBuilder6.append("];\n");
    stringBuilder9.append(stringBuilder3);
    stringBuilder9.append(stringBuilder6);
    stringBuilder9.append("\n");
    stringBuilder3 = new StringBuilder();
    stringBuilder6 = new StringBuilder();
    stringBuilder3.append("function createVuforiaCurrentGameTrackableNameDropdown() {\n");
    stringBuilder3.append("  var CHOICES = [\n");
    stringBuilder6.append("var VUFORIA_CURRENT_GAME_TRACKABLE_NAME_TOOLTIPS = [\n");
    for (String str : VuforiaCurrentGame.TRACKABLE_NAMES) {
      stringBuilder3.append("      ['");
      stringBuilder3.append(ProjectsUtil.escapeSingleQuotes(makeVisibleNameForDropdownItem(str)));
      stringBuilder3.append("', '");
      stringBuilder3.append(ProjectsUtil.escapeSingleQuotes(str));
      stringBuilder3.append("'],\n");
      stringBuilder6.append("  ['");
      stringBuilder6.append(ProjectsUtil.escapeSingleQuotes(str));
      stringBuilder6.append(str3);
      stringBuilder6.append(ProjectsUtil.escapeSingleQuotes(str));
      stringBuilder6.append(".'],\n");
    } 
    stringBuilder3.append("  ];\n");
    stringBuilder3.append("  return createFieldDropdown(CHOICES);\n");
    stringBuilder3.append("}\n\n");
    stringBuilder6.append("];\n");
    stringBuilder9.append(stringBuilder3);
    stringBuilder9.append(stringBuilder6);
    stringBuilder9.append("\n");
    for (HardwareType hardwareType : HardwareType.values()) {
      if (hardwareType.createDropdownFunctionName != null) {
        List<HardwareItem> list = paramHardwareItemMap.getHardwareItems(hardwareType);
        appendCreateDropdownFunction(stringBuilder9, hardwareType.createDropdownFunctionName, list);
        if (hardwareType == HardwareType.DC_MOTOR)
          appendCreateDropdownFunction(stringBuilder9, "createDcMotorExDropdown", getHardwareItemsForDcMotorEx(list)); 
      } 
    } 
    stringBuilder9.append("function getHardwareIdentifierSuffixes() {\n");
    stringBuilder9.append("  var suffixes = [\n");
    for (HardwareType hardwareType : HardwareType.values()) {
      stringBuilder3 = new StringBuilder();
      stringBuilder3.append("    '");
      stringBuilder3.append(hardwareType.identifierSuffixForJavaScript);
      stringBuilder3.append("',\n");
      stringBuilder9.append(stringBuilder3.toString());
    } 
    stringBuilder9.append("  ];\n");
    stringBuilder9.append("  return suffixes;\n");
    stringBuilder9.append("}\n\n");
    stringBuilder9.append("function addReservedWordsForJavaScript() {\n");
    stringBuilder9.append("  Blockly.JavaScript.addReservedWords('callRunOpMode');\n");
    stringBuilder9.append("  Blockly.JavaScript.addReservedWords('telemetryAddTextData');\n");
    stringBuilder9.append("  Blockly.JavaScript.addReservedWords('telemetrySpeak');\n");
    stringBuilder9.append("  Blockly.JavaScript.addReservedWords('callJava');\n");
    stringBuilder9.append("  Blockly.JavaScript.addReservedWords('callJava_boolean');\n");
    stringBuilder9.append("  Blockly.JavaScript.addReservedWords('callJava_String');\n");
    for (HardwareItem hardwareItem : paramHardwareItemMap.getAllHardwareItems()) {
      stringBuilder9.append("  Blockly.JavaScript.addReservedWords('");
      stringBuilder9.append(hardwareItem.identifier);
      stringBuilder9.append("');\n");
    } 
    ArrayList<String> arrayList1 = new ArrayList();
    for (Identifier identifier : Identifier.values()) {
      if (identifier.identifierForJavaScript != null && !identifier.identifierForJavaScript.isEmpty())
        arrayList1.add(identifier.identifierForJavaScript); 
    } 
    Collections.sort(arrayList1);
    for (String str3 : arrayList1) {
      stringBuilder9.append("  Blockly.JavaScript.addReservedWords('");
      stringBuilder9.append(str3);
      stringBuilder9.append("');\n");
    } 
    stringBuilder9.append("}\n\n");
    stringBuilder9.append("function getHardwareItemDeviceName(identifier) {\n");
    stringBuilder9.append("  switch (identifier) {\n");
    for (HardwareItem hardwareItem : paramHardwareItemMap.getAllHardwareItems()) {
      stringBuilder9.append("    case '");
      stringBuilder9.append(hardwareItem.identifier);
      stringBuilder9.append("':\n");
      stringBuilder9.append("      return '");
      stringBuilder9.append(ProjectsUtil.escapeSingleQuotes(hardwareItem.deviceName));
      stringBuilder9.append("';\n");
    } 
    stringBuilder9.append("  }\n");
    stringBuilder9.append("  throw 'Unexpected identifier (' + identifier + ').';\n");
    stringBuilder9.append("}\n\n");
    stringBuilder9.append("function getIdentifierForFtcJava(identifier) {\n");
    stringBuilder9.append("  switch (identifier) {\n");
    HashSet<String> hashSet2 = new HashSet();
    for (HardwareItem hardwareItem : paramHardwareItemMap.getAllHardwareItems()) {
      String str8;
      str3 = HardwareItem.makeIdentifier(hardwareItem.deviceName);
      String str7 = str3;
      if (reservedWordsForFtcJava.contains(str3)) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str3);
        stringBuilder.append(hardwareItem.hardwareType.identifierSuffixForFtcJava);
        str7 = stringBuilder.toString();
      } 
      str3 = str7;
      if (!hashSet2.add(str7)) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str7);
        stringBuilder.append(hardwareItem.hardwareType.identifierSuffixForFtcJava);
        str8 = stringBuilder.toString();
        hashSet2.add(str8);
      } 
      stringBuilder9.append("    case '");
      stringBuilder9.append(hardwareItem.identifier);
      stringBuilder9.append("':\n");
      stringBuilder9.append("      return '");
      stringBuilder9.append(str8);
      stringBuilder9.append("';\n");
    } 
    stringBuilder9.append("  }\n");
    stringBuilder9.append("  throw 'Unexpected identifier (' + identifier + ').';\n");
    stringBuilder9.append("}\n\n");
    stringBuilder9.append("function addReservedWordsForFtcJava() {\n");
    for (String str3 : reservedWordsForFtcJava) {
      stringBuilder9.append("  Blockly.FtcJava.addReservedWords('");
      stringBuilder9.append(str3);
      stringBuilder9.append("');\n");
    } 
    for (String str3 : hashSet3) {
      stringBuilder9.append("  Blockly.FtcJava.addReservedWords('");
      stringBuilder9.append(str3);
      stringBuilder9.append("');\n");
    } 
    for (HardwareItem hardwareItem : paramHardwareItemMap.getAllHardwareItems()) {
      stringBuilder9.append("  Blockly.FtcJava.addReservedWords(getIdentifierForFtcJava('");
      stringBuilder9.append(hardwareItem.identifier);
      stringBuilder9.append("'));\n");
    } 
    for (Identifier identifier : Identifier.values()) {
      if (identifier.identifierForFtcJava != null && !identifier.identifierForFtcJava.isEmpty()) {
        stringBuilder9.append("  Blockly.FtcJava.addReservedWords('");
        stringBuilder9.append(identifier.identifierForFtcJava);
        stringBuilder9.append("');\n");
      } 
    } 
    stringBuilder9.append("}\n\n");
    stringBuilder9.append("function getIconClass(categoryName) {\n");
    for (HardwareType hardwareType : HardwareType.values()) {
      if (hardwareType.toolboxCategoryName != null && hardwareType.toolboxIcon != null) {
        stringBuilder9.append("  if (categoryName == '");
        stringBuilder9.append(ProjectsUtil.escapeSingleQuotes(hardwareType.toolboxCategoryName));
        stringBuilder9.append("') {\n");
        stringBuilder9.append("    return '");
        stringBuilder9.append(ProjectsUtil.escapeSingleQuotes(hardwareType.toolboxIcon.cssClass));
        stringBuilder9.append("';\n");
        stringBuilder9.append("  }\n");
      } 
    } 
    stringBuilder9.append("  if (categoryName == '");
    stringBuilder9.append(ProjectsUtil.escapeSingleQuotes("Dual"));
    stringBuilder9.append("') {\n");
    stringBuilder9.append("    return '");
    stringBuilder9.append(ProjectsUtil.escapeSingleQuotes(ToolboxIcon.DC_MOTOR.cssClass));
    stringBuilder9.append("';\n");
    stringBuilder9.append("  }\n");
    stringBuilder9.append("  if (categoryName == '");
    stringBuilder9.append(ProjectsUtil.escapeSingleQuotes("Gamepad"));
    stringBuilder9.append("') {\n");
    stringBuilder9.append("    return '");
    stringBuilder9.append(ProjectsUtil.escapeSingleQuotes(ToolboxIcon.GAMEPAD.cssClass));
    stringBuilder9.append("';\n");
    stringBuilder9.append("  }\n");
    stringBuilder9.append("  if (categoryName == '");
    stringBuilder9.append(ProjectsUtil.escapeSingleQuotes("LinearOpMode"));
    stringBuilder9.append("') {\n");
    stringBuilder9.append("    return '");
    stringBuilder9.append(ProjectsUtil.escapeSingleQuotes(ToolboxIcon.LINEAR_OPMODE.cssClass));
    stringBuilder9.append("';\n");
    stringBuilder9.append("  }\n");
    stringBuilder9.append("  if (categoryName == '");
    stringBuilder9.append(ProjectsUtil.escapeSingleQuotes("Color"));
    stringBuilder9.append("') {\n");
    stringBuilder9.append("    return '");
    stringBuilder9.append(ProjectsUtil.escapeSingleQuotes(ToolboxIcon.COLOR_SENSOR.cssClass));
    stringBuilder9.append("';\n");
    stringBuilder9.append("  }\n");
    stringBuilder9.append("  if (categoryName == '");
    stringBuilder9.append(ProjectsUtil.escapeSingleQuotes("ElapsedTime"));
    stringBuilder9.append("') {\n");
    stringBuilder9.append("    return '");
    stringBuilder9.append(ProjectsUtil.escapeSingleQuotes(ToolboxIcon.ELAPSED_TIME.cssClass));
    stringBuilder9.append("';\n");
    stringBuilder9.append("  }\n");
    stringBuilder9.append("  return '';\n");
    stringBuilder9.append("}\n\n");
    stringBuilder9.append("function getWarningForCapabilityRequestedBySample(capability) {\n");
    stringBuilder9.append("  switch (capability) {\n");
    for (Capability capability : Capability.values()) {
      boolean bool2 = ((Boolean)map.get(capability)).booleanValue();
      boolean bool1 = bool2;
      if (capability == Capability.CAMERA) {
        bool1 = bool2;
        if (!bool2) {
          bool1 = bool2;
          if (((Boolean)map.get(Capability.WEBCAM)).booleanValue())
            bool1 = true; 
        } 
      } 
      if (!bool1) {
        str3 = getCapabilityWarning(capability);
        if (str3 != null) {
          stringBuilder9.append("    case '");
          stringBuilder9.append(capability);
          stringBuilder9.append("':\n");
          stringBuilder9.append("      return '");
          stringBuilder9.append(str3);
          stringBuilder9.append("';\n");
        } 
      } 
    } 
    stringBuilder9.append("  }\n");
    stringBuilder9.append("  return '';\n");
    stringBuilder9.append("}\n\n");
    stringBuilder9.append("function getToolbox() {\n");
    stringBuilder9.append("  return '");
    stringBuilder9.append(ProjectsUtil.escapeSingleQuotes(str5));
    stringBuilder9.append("';\n");
    stringBuilder9.append("}\n\n");
    return stringBuilder9.toString();
  }
  
  private static String generateToolbox(HardwareItemMap paramHardwareItemMap, Map<Capability, Boolean> paramMap, AssetManager paramAssetManager, Set<String> paramSet1, Set<String> paramSet2) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("<xml id=\"toolbox\" style=\"display: none\">\n");
    if (paramAssetManager != null) {
      addAsset(stringBuilder, paramAssetManager, "toolbox/linear_op_mode.xml");
      addAsset(stringBuilder, paramAssetManager, "toolbox/gamepad.xml");
    } 
    for (ToolboxFolder toolboxFolder : ToolboxFolder.values()) {
      stringBuilder.append(" <category name=\"");
      stringBuilder.append(toolboxFolder.label);
      stringBuilder.append("\">\n");
      TreeSet<HardwareType> treeSet = new TreeSet<HardwareType>(HardwareType.BY_TOOLBOX_CATEGORY_NAME);
      treeSet.addAll(paramHardwareItemMap.getHardwareTypes());
      for (HardwareType hardwareType : treeSet) {
        if (hardwareType.toolboxFolder == toolboxFolder && hardwareType.toolboxCategoryName != null) {
          addHardwareCategoryToToolbox(stringBuilder, hardwareType, paramHardwareItemMap.getHardwareItems(hardwareType), paramAssetManager);
          if (hardwareType == HardwareType.BNO055IMU && paramAssetManager != null)
            addAsset(stringBuilder, paramAssetManager, "toolbox/bno055imu_parameters.xml"); 
        } 
      } 
      stringBuilder.append(" </category>\n");
    } 
    addAndroidCategoriesToToolbox(stringBuilder, paramAssetManager);
    if (paramAssetManager != null) {
      addAssetWithPlaceholders(stringBuilder, paramAssetManager, paramMap, "toolbox/utilities.xml");
      addAsset(stringBuilder, paramAssetManager, "toolbox/misc.xml");
    } 
    Map map = BlocksClassFilter.getInstance().getMethodsByClass();
    if (!map.isEmpty()) {
      stringBuilder.append("<category name=\"Java Classes\">\n");
      for (Map.Entry entry : map.entrySet()) {
        String str1 = ((Class)entry.getKey()).getName();
        if (str1.startsWith("org.firstinspires.ftc.teamcode.")) {
          str1 = str1.substring(31);
          paramSet1.add(str1);
        } 
        String str2 = str1.replace('$', '.');
        stringBuilder.append("<category name=\"");
        stringBuilder.append(str2);
        stringBuilder.append("\">\n");
        for (Method method : entry.getValue()) {
          String str4 = method.getReturnType().getName();
          if (str4.equals("void")) {
            str3 = "misc_callJava_noReturn";
          } else {
            str3 = "misc_callJava_return";
          } 
          String str6 = method.getName();
          Class[] arrayOfClass = method.getParameterTypes();
          ExportToBlocks exportToBlocks = method.<ExportToBlocks>getAnnotation(ExportToBlocks.class);
          String str5 = exportToBlocks.comment();
          String str7 = exportToBlocks.tooltip();
          String[] arrayOfString = getParameterLabels(method);
          String str8 = BlocksClassFilter.getLookupString(method);
          paramSet2.add(str8);
          stringBuilder.append("<block type=\"");
          stringBuilder.append(str3);
          stringBuilder.append("\">\n");
          stringBuilder.append("<field name=\"CLASS_NAME\">");
          stringBuilder.append(str2);
          stringBuilder.append("</field>");
          stringBuilder.append("<field name=\"METHOD_NAME\">");
          stringBuilder.append(str6);
          stringBuilder.append("</field>");
          stringBuilder.append("<mutation");
          stringBuilder.append(" methodLookupString=\"");
          stringBuilder.append(str8);
          String str3 = "\"";
          stringBuilder.append("\"");
          stringBuilder.append(" parameterCount=\"");
          stringBuilder.append(arrayOfClass.length);
          stringBuilder.append("\"");
          stringBuilder.append(" returnType=\"");
          stringBuilder.append(str4);
          stringBuilder.append("\"");
          stringBuilder.append(" comment=\"");
          stringBuilder.append(str5);
          stringBuilder.append("\"");
          stringBuilder.append(" tooltip=\"");
          stringBuilder.append(str7);
          stringBuilder.append("\"");
          stringBuilder.append(" accessMethod=\"");
          stringBuilder.append(accessMethod(method.getReturnType()));
          stringBuilder.append("\"");
          stringBuilder.append(" convertReturnValue=\"");
          stringBuilder.append(convertReturnValue(method.getReturnType()));
          stringBuilder.append("\"");
          StringBuilder stringBuilder1 = new StringBuilder();
          ArrayList<String> arrayList = new ArrayList();
          int k = arrayOfClass.length;
          int i = 0;
          int j = 0;
          while (i < k) {
            String str;
            Class clazz = arrayOfClass[i];
            stringBuilder.append(" argLabel");
            stringBuilder.append(j);
            stringBuilder.append("=\"");
            stringBuilder.append(arrayOfString[j]);
            stringBuilder.append(str3);
            str7 = clazz.getName();
            stringBuilder.append(" argType");
            stringBuilder.append(j);
            stringBuilder.append("=\"");
            stringBuilder.append(str7);
            stringBuilder.append(str3);
            str4 = parameterProvidedAutomatically(clazz, arrayOfString[j], arrayList);
            stringBuilder.append(" argAuto");
            stringBuilder.append(j);
            stringBuilder.append("=\"");
            if (str4 != null) {
              str = str4;
            } else {
              str = "";
            } 
            stringBuilder.append(str);
            stringBuilder.append(str3);
            if (str4 == null)
              if (str7.equals("boolean") || str7.equals("java.lang.Boolean")) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("<value name=\"ARG");
                stringBuilder2.append(j);
                stringBuilder2.append("\">");
                stringBuilder1.append(stringBuilder2.toString());
                stringBuilder1.append(ToolboxUtil.makeBooleanShadow(false));
                stringBuilder1.append("</value>\n");
              } else if (str7.equals("char") || str7.equals("java.lang.Character") || str7.equals("java.lang.String")) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("<value name=\"ARG");
                stringBuilder2.append(j);
                stringBuilder2.append("\">");
                stringBuilder1.append(stringBuilder2.toString());
                stringBuilder1.append(ToolboxUtil.makeTextShadow("A"));
                stringBuilder1.append("</value>\n");
              } else if (str7.equals("byte") || str7.equals("java.lang.Byte") || str7.equals("short") || str7.equals("java.lang.Short") || str7.equals("int") || str7.equals("java.lang.Integer") || str7.equals("long") || str7.equals("java.lang.Long") || str7.equals("float") || str7.equals("java.lang.Float") || str7.equals("double") || str7.equals("java.lang.Double")) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("<value name=\"ARG");
                stringBuilder2.append(j);
                stringBuilder2.append("\">");
                stringBuilder1.append(stringBuilder2.toString());
                stringBuilder1.append(ToolboxUtil.makeNumberShadow(0));
                stringBuilder1.append("</value>\n");
              }  
            j++;
            i++;
          } 
          stringBuilder.append("/>");
          stringBuilder.append(stringBuilder1);
          stringBuilder.append("</block>\n");
        } 
        stringBuilder.append("</category>\n");
      } 
      stringBuilder.append("</category>\n");
    } 
    stringBuilder.append("</xml>");
    return stringBuilder.toString();
  }
  
  public static Map<Capability, Boolean> getCapabilities(HardwareItemMap paramHardwareItemMap) {
    boolean bool2;
    boolean bool3;
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    int i = Camera.getNumberOfCameras();
    boolean bool4 = false;
    if (i > 0) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    i = paramHardwareItemMap.getHardwareItems(HardwareType.WEBCAM_NAME).size();
    if (i > 0) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    if (i > 1) {
      bool3 = true;
    } else {
      bool3 = false;
    } 
    hashMap.put(Capability.CAMERA, Boolean.valueOf(bool1));
    hashMap.put(Capability.WEBCAM, Boolean.valueOf(bool2));
    hashMap.put(Capability.SWITCHABLE_CAMERA, Boolean.valueOf(bool3));
    Capability capability = Capability.VUFORIA;
    if (bool1 || bool2) {
      bool3 = true;
    } else {
      bool3 = false;
    } 
    hashMap.put(capability, Boolean.valueOf(bool3));
    capability = Capability.TFOD;
    if (!bool1) {
      bool1 = bool4;
      if (bool2) {
        bool1 = true;
        hashMap.put(capability, Boolean.valueOf(bool1));
        return (Map)hashMap;
      } 
      hashMap.put(capability, Boolean.valueOf(bool1));
      return (Map)hashMap;
    } 
    boolean bool1 = true;
    hashMap.put(capability, Boolean.valueOf(bool1));
    return (Map)hashMap;
  }
  
  private static String getCapabilityWarning(Capability paramCapability) {
    int i = null.$SwitchMap$com$google$blocks$ftcrobotcontroller$hardware$HardwareUtil$Capability[paramCapability.ordinal()];
    return (i != 1) ? ((i != 2) ? ((i != 3) ? null : "The current configuration does not have multiple webcams.") : "The current configuration has no webcam.") : "This device does not have a camera.";
  }
  
  public static String getConfigurationName() {
    return (new RobotConfigFileManager()).getActiveConfig().getName();
  }
  
  private static List<HardwareItem> getHardwareItemsForDcMotorEx(List<HardwareItem> paramList) {
    ArrayList<HardwareItem> arrayList = new ArrayList();
    for (HardwareItem hardwareItem : paramList) {
      if (hardwareItem.hasAncestor(HardwareType.LYNX_MODULE))
        arrayList.add(hardwareItem); 
    } 
    return arrayList;
  }
  
  static Iterable<HardwareType> getHardwareTypes(DeviceConfiguration paramDeviceConfiguration) {
    return getHardwareTypes(paramDeviceConfiguration.getConfigurationType().getXmlTag());
  }
  
  static Iterable<HardwareType> getHardwareTypes(String paramString) {
    return XML_TAG_TO_HARDWARE_TYPES.containsKey(paramString) ? Collections.unmodifiableList(XML_TAG_TO_HARDWARE_TYPES.get(paramString)) : Collections.emptyList();
  }
  
  public static String[] getParameterLabels(Method paramMethod) {
    String[] arrayOfString2 = ((ExportToBlocks)paramMethod.<ExportToBlocks>getAnnotation(ExportToBlocks.class)).parameterLabels();
    int i = (paramMethod.getParameterTypes()).length;
    String[] arrayOfString1 = arrayOfString2;
    if (arrayOfString2.length != i) {
      arrayOfString2 = new String[i];
      int j = 0;
      while (true) {
        arrayOfString1 = arrayOfString2;
        if (j < i) {
          arrayOfString2[j] = "";
          j++;
          continue;
        } 
        break;
      } 
    } 
    return arrayOfString1;
  }
  
  private static String getTfodCurrentGameLabelBlocks() {
    StringBuilder stringBuilder = new StringBuilder();
    if (TfodCurrentGame.LABELS.length <= 3) {
      for (String str : TfodCurrentGame.LABELS) {
        stringBuilder.append("<block type=\"tfodCurrentGame_typedEnum_label\"><field name=\"LABEL\">");
        stringBuilder.append(str);
        stringBuilder.append("</field></block>\n");
      } 
    } else {
      stringBuilder.append("<block type=\"tfodCurrentGame_typedEnum_label\"></block>\n");
    } 
    return stringBuilder.toString();
  }
  
  private static String getVuforiaCurrentGameTrackableNameBlocks() {
    StringBuilder stringBuilder = new StringBuilder();
    if (VuforiaCurrentGame.TRACKABLE_NAMES.length <= 3) {
      for (String str : VuforiaCurrentGame.TRACKABLE_NAMES) {
        stringBuilder.append("<block type=\"vuforiaCurrentGame_typedEnum_trackableName\"><field name=\"TRACKABLE_NAME\">");
        stringBuilder.append(str);
        stringBuilder.append("</field></block>\n");
      } 
    } else {
      stringBuilder.append("<block type=\"vuforiaCurrentGame_typedEnum_trackableName\"></block>\n");
    } 
    return stringBuilder.toString();
  }
  
  static String makeVisibleNameForDropdownItem(String paramString) {
    int j = paramString.length();
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < j; i++) {
      char c = paramString.charAt(i);
      if (c == ' ') {
        stringBuilder.append(' ');
      } else {
        stringBuilder.append(c);
      } 
    } 
    return stringBuilder.toString();
  }
  
  private static String parameterProvidedAutomatically(Class paramClass, String paramString, List<String> paramList) {
    if (paramClass.equals(LinearOpMode.class) || paramClass.equals(OpMode.class))
      return "this"; 
    if (paramClass.equals(HardwareMap.class))
      return "hardwareMap"; 
    if (paramClass.equals(Telemetry.class))
      return "telemetry"; 
    if (paramClass.equals(Gamepad.class)) {
      if (!paramString.equals("gamepad1")) {
        if (paramString.equals("gamepad2"))
          return paramString; 
        if (paramList.isEmpty()) {
          paramList.add("gamepad1");
          paramList.add("gamepad2");
        } 
        return paramList.remove(0);
      } 
      return paramString;
    } 
    return null;
  }
  
  private static String replaceIdentifierInJs(String paramString1, String paramString2, String paramString3) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString2);
    stringBuilder.append(".");
    paramString2 = stringBuilder.toString();
    stringBuilder = new StringBuilder();
    stringBuilder.append(paramString3);
    stringBuilder.append(".");
    return paramString1.replace(paramString2, stringBuilder.toString());
  }
  
  private static String replaceIdentifierSuffixInJs(String paramString1, List<HardwareItem> paramList, String paramString2, String paramString3) {
    String str = paramString1;
    if (paramList != null) {
      Iterator<HardwareItem> iterator = paramList.iterator();
      while (true) {
        str = paramString1;
        if (iterator.hasNext()) {
          str = ((HardwareItem)iterator.next()).identifier;
          if (str.endsWith(paramString3)) {
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append(str.substring(0, str.length() - paramString3.length()));
            stringBuilder1.append(paramString2);
            String str1 = stringBuilder1.toString();
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str1);
            stringBuilder2.append(".");
            str1 = stringBuilder2.toString();
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str);
            stringBuilder2.append(".");
            paramString1 = paramString1.replace(str1, stringBuilder2.toString());
          } 
          continue;
        } 
        break;
      } 
    } 
    return str;
  }
  
  public static String upgradeJs(String paramString, HardwareItemMap paramHardwareItemMap) {
    return replaceIdentifierInJs(replaceIdentifierSuffixInJs(paramString, paramHardwareItemMap.getHardwareItems(HardwareType.BNO055IMU), "AsAdafruitBNO055IMU", "AsBNO055IMU"), "adafruitBNO055IMUParametersAccess", "bno055imuParametersAccess");
  }
  
  public enum Capability {
    CAMERA("camera"),
    SWITCHABLE_CAMERA("camera"),
    TFOD("camera"),
    VUFORIA("camera"),
    WEBCAM("webcam");
    
    private final String placeholderType;
    
    static {
      Capability capability = new Capability("TFOD", 4, "tfod");
      TFOD = capability;
      $VALUES = new Capability[] { CAMERA, WEBCAM, SWITCHABLE_CAMERA, VUFORIA, capability };
    }
    
    Capability(String param1String1) {
      this.placeholderType = param1String1;
    }
    
    static Capability fromPlaceholderType(String param1String) {
      for (Capability capability : values()) {
        if (capability.placeholderType.equals(param1String))
          return capability; 
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unexpected capability name ");
      stringBuilder.append(param1String);
      throw new IllegalArgumentException(stringBuilder.toString());
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\hardware\HardwareUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
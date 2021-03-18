package com.google.blocks.ftcrobotcontroller.util;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.text.Html;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareItemMap;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotserver.internal.webserver.AppThemeColors;

public class OfflineBlocksUtil {
  private static void colorToHSL(int paramInt, float[] paramArrayOffloat) {
    int i = Color.red(paramInt);
    int j = Color.green(paramInt);
    paramInt = Color.blue(paramInt);
    int k = Math.max(Math.max(i, j), paramInt);
    int m = Math.min(Math.min(i, j), paramInt);
    float f1 = k / 255.0F;
    float f2 = m / 255.0F;
    float f3 = f1 + f2;
    paramArrayOffloat[2] = f3 / 2.0F;
    if (k == m) {
      paramArrayOffloat[1] = 0.0F;
      paramArrayOffloat[0] = 0.0F;
      return;
    } 
    if (paramArrayOffloat[2] > 0.5D) {
      f1 = (f1 - f2) / (2.0F - f1 - f2);
    } else {
      f1 = (f1 - f2) / f3;
    } 
    paramArrayOffloat[1] = f1;
    paramArrayOffloat[0] = hue(i, j, paramInt);
  }
  
  private static String convertLessToCss(AssetManager paramAssetManager, String paramString) throws IOException {
    AppThemeColors appThemeColors = AppThemeColors.fromTheme();
    StringBuilder stringBuilder = new StringBuilder();
    FileUtil.readAsset(stringBuilder, paramAssetManager, paramString);
    float[] arrayOfFloat = new float[3];
    Color.colorToHSV(appThemeColors.textBright, arrayOfFloat);
    return stringBuilder.toString().replace("@import \"/css/core.less\";", "").replace("hue(@textBright)", String.format("%d", new Object[] { Integer.valueOf(Math.round(arrayOfFloat[0])) })).replace("saturation(@textBright)", String.format("%d%%", new Object[] { Integer.valueOf(Math.round(arrayOfFloat[1] * 100.0F)) })).replace("darken(@backgroundMedium, 5%)", String.format("#%06x", new Object[] { Integer.valueOf(darken(appThemeColors.backgroundMedium, 0.05F) & 0xFFFFFF) })).replace("@textError", String.format("#%06x", new Object[] { Integer.valueOf(appThemeColors.textError & 0xFFFFFF) })).replace("@textWarning", String.format("#%06x", new Object[] { Integer.valueOf(appThemeColors.textWarning & 0xFFFFFF) })).replace("@textOkay", String.format("#%06x", new Object[] { Integer.valueOf(appThemeColors.textOkay & 0xFFFFFF) })).replace("@textBright", String.format("#%06x", new Object[] { Integer.valueOf(appThemeColors.textBright & 0xFFFFFF) })).replace("@textLight", String.format("#%06x", new Object[] { Integer.valueOf(appThemeColors.textLight & 0xFFFFFF) })).replace("@textMediumDark", String.format("#%06x", new Object[] { Integer.valueOf(appThemeColors.textMediumDark & 0xFFFFFF) })).replace("@textMedium", String.format("#%06x", new Object[] { Integer.valueOf(appThemeColors.textMedium & 0xFFFFFF) })).replace("@textVeryDark", String.format("#%06x", new Object[] { Integer.valueOf(appThemeColors.textVeryDark & 0xFFFFFF) })).replace("@textVeryVeryDark", String.format("#%06x", new Object[] { Integer.valueOf(appThemeColors.textVeryVeryDark & 0xFFFFFF) })).replace("@backgroundLight", String.format("#%06x", new Object[] { Integer.valueOf(appThemeColors.backgroundLight & 0xFFFFFF) })).replace("@backgroundMediumLight", String.format("#%06x", new Object[] { Integer.valueOf(appThemeColors.backgroundMediumLight & 0xFFFFFF) })).replace("@backgroundMediumMedium", String.format("#%06x", new Object[] { Integer.valueOf(appThemeColors.backgroundMediumMedium & 0xFFFFFF) })).replace("@backgroundMediumDark", String.format("#%06x", new Object[] { Integer.valueOf(appThemeColors.backgroundMediumDark & 0xFFFFFF) })).replace("@backgroundMedium", String.format("#%06x", new Object[] { Integer.valueOf(appThemeColors.backgroundMedium & 0xFFFFFF) })).replace("@backgroundAlmostDark", String.format("#%06x", new Object[] { Integer.valueOf(appThemeColors.backgroundAlmostDark & 0xFFFFFF) })).replace("@backgroundDark", String.format("#%06x", new Object[] { Integer.valueOf(appThemeColors.backgroundDark & 0xFFFFFF) })).replace("@backgroundVeryDark", String.format("#%06x", new Object[] { Integer.valueOf(appThemeColors.backgroundVeryDark & 0xFFFFFF) })).replace("@backgroundVeryVeryDark", String.format("#%06x", new Object[] { Integer.valueOf(appThemeColors.backgroundVeryVeryDark & 0xFFFFFF) })).replace("@lineBright", String.format("#%06x", new Object[] { Integer.valueOf(appThemeColors.lineBright & 0xFFFFFF) })).replace("@lineLight", String.format("#%06x", new Object[] { Integer.valueOf(appThemeColors.lineLight & 0xFFFFFF) })).replace("@feedbackBackground", String.format("#%06x", new Object[] { Integer.valueOf(appThemeColors.feedbackBackground & 0xFFFFFF) })).replace("@feedbackBorder", String.format("#%06x", new Object[] { Integer.valueOf(appThemeColors.feedbackBorder & 0xFFFFFF) }));
  }
  
  private static void copyAsset(AssetManager paramAssetManager, String paramString, OutputStream paramOutputStream) throws IOException {
    InputStream inputStream = paramAssetManager.open(paramString);
    try {
      byte[] arrayOfByte = new byte[4096];
      while (true) {
        int i = inputStream.read(arrayOfByte);
        if (i > 0) {
          paramOutputStream.write(arrayOfByte, 0, i);
          continue;
        } 
        return;
      } 
    } finally {
      paramString = null;
    } 
  }
  
  private static int darken(int paramInt, float paramFloat) {
    float[] arrayOfFloat = new float[3];
    colorToHSL(paramInt, arrayOfFloat);
    arrayOfFloat[2] = arrayOfFloat[2] - paramFloat;
    return hslToColor(arrayOfFloat);
  }
  
  public static InputStream fetchOfflineBlocksEditor() throws IOException {
    String str = HardwareUtil.getConfigurationName();
    HardwareItemMap hardwareItemMap = HardwareItemMap.newHardwareItemMap();
    AssetManager assetManager = AppUtil.getDefContext().getAssets();
    HashSet<String> hashSet = new HashSet();
    hashSet.add("js/split.min.js");
    hashSet.add("blocks/images.css");
    String[] arrayOfString = assetManager.list("blocks/images");
    int j = arrayOfString.length;
    boolean bool = false;
    int i;
    for (i = 0; i < j; i++) {
      String str1 = arrayOfString[i];
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("blocks/images/");
      stringBuilder.append(str1);
      hashSet.add(stringBuilder.toString());
    } 
    hashSet.add("css/blocks_offline.css");
    hashSet.add("css/blocks_common.css");
    hashSet.add("blockly/blockly_compressed.js");
    arrayOfString = assetManager.list("blockly/media");
    j = arrayOfString.length;
    for (i = bool; i < j; i++) {
      String str1 = arrayOfString[i];
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("blockly/media/");
      stringBuilder.append(str1);
      hashSet.add(stringBuilder.toString());
    } 
    hashSet.add("blockly/msg/messages.js");
    hashSet.add("blockly/blocks_compressed.js");
    hashSet.add("blockly/javascript_compressed.js");
    hashSet.add("ftcblockly/generators/ftcjava.js");
    hashSet.add("ftcblockly/generators/ftcjava/lists.js");
    hashSet.add("ftcblockly/generators/ftcjava/logic.js");
    hashSet.add("ftcblockly/generators/ftcjava/loops.js");
    hashSet.add("ftcblockly/generators/ftcjava/math.js");
    hashSet.add("ftcblockly/generators/ftcjava/procedures.js");
    hashSet.add("ftcblockly/generators/ftcjava/text.js");
    hashSet.add("ftcblockly/generators/ftcjava/variables.js");
    hashSet.add("blocks/FtcBlocks_common.js");
    hashSet.add("blocks/FtcBlocksProjects_common.js");
    hashSet.add("blocks/acceleration.js");
    hashSet.add("blocks/acceleration_sensor.js");
    hashSet.add("blocks/analog_input.js");
    hashSet.add("blocks/analog_output.js");
    hashSet.add("blocks/android_accelerometer.js");
    hashSet.add("blocks/android_gyroscope.js");
    hashSet.add("blocks/android_orientation.js");
    hashSet.add("blocks/android_sound_pool.js");
    hashSet.add("blocks/android_text_to_speech.js");
    hashSet.add("blocks/angular_velocity.js");
    hashSet.add("blocks/bno055imu.js");
    hashSet.add("blocks/bno055imu_parameters.js");
    hashSet.add("blocks/clipboard_util.js");
    hashSet.add("blocks/color.js");
    hashSet.add("blocks/color_range_sensor.js");
    hashSet.add("blocks/color_sensor.js");
    hashSet.add("blocks/compass_sensor.js");
    hashSet.add("blocks/cr_servo.js");
    hashSet.add("blocks/dbg_log.js");
    hashSet.add("blocks/dc_motor.js");
    hashSet.add("blocks/digital_channel.js");
    hashSet.add("blocks/distance_sensor.js");
    hashSet.add("blocks/elapsed_time.js");
    hashSet.add("blocks/elapsed_time2.js");
    hashSet.add("blocks/gamepad.js");
    hashSet.add("blocks/gyro_sensor.js");
    hashSet.add("blocks/hardware_util.js");
    hashSet.add("blocks/ir_seeker_sensor.js");
    hashSet.add("blocks/led.js");
    hashSet.add("blocks/light_sensor.js");
    hashSet.add("blocks/linear_op_mode.js");
    hashSet.add("blocks/locale.js");
    hashSet.add("blocks/magnetic_flux.js");
    hashSet.add("blocks/matrix_f.js");
    hashSet.add("blocks/misc.js");
    hashSet.add("blocks/mr_i2c_compass_sensor.js");
    hashSet.add("blocks/mr_i2c_range_sensor.js");
    hashSet.add("blocks/navigation.js");
    hashSet.add("blocks/open_gl_matrix.js");
    hashSet.add("blocks/optical_distance_sensor.js");
    hashSet.add("blocks/orientation.js");
    hashSet.add("blocks/pidf_coefficients.js");
    hashSet.add("blocks/position.js");
    hashSet.add("blocks/project_util.js");
    hashSet.add("blocks/quaternion.js");
    hashSet.add("blocks/range.js");
    hashSet.add("blocks/rev_blinkin_led_driver.js");
    hashSet.add("blocks/servo.js");
    hashSet.add("blocks/servo_controller.js");
    hashSet.add("blocks/system.js");
    hashSet.add("blocks/telemetry.js");
    hashSet.add("blocks/temperature.js");
    hashSet.add("blocks/tfod.js");
    hashSet.add("blocks/tfod_current_game.js");
    hashSet.add("blocks/tfod_custom_model.js");
    hashSet.add("blocks/tfod_recognition.js");
    hashSet.add("blocks/tfod_rover_ruckus.js");
    hashSet.add("blocks/tfod_sky_stone.js");
    hashSet.add("blocks/toolbox_util.js");
    hashSet.add("blocks/touch_sensor.js");
    hashSet.add("blocks/ultrasonic_sensor.js");
    hashSet.add("blocks/vars.js");
    hashSet.add("blocks/vector_f.js");
    hashSet.add("blocks/velocity.js");
    hashSet.add("blocks/voltage_sensor.js");
    hashSet.add("blocks/vuforia.js");
    hashSet.add("blocks/vuforia_current_game.js");
    hashSet.add("blocks/vuforia_localizer.js");
    hashSet.add("blocks/vuforia_localizer_parameters.js");
    hashSet.add("blocks/vuforia_relic_recovery.js");
    hashSet.add("blocks/vuforia_rover_ruckus.js");
    hashSet.add("blocks/vuforia_sky_stone.js");
    hashSet.add("blocks/vuforia_trackable.js");
    hashSet.add("blocks/vuforia_trackable_default_listener.js");
    hashSet.add("blocks/vuforia_trackables.js");
    hashSet.add("FtcOfflineBlocksProjects.html");
    hashSet.add("FtcOfflineBlocks.html");
    hashSet.add("favicon.ico");
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
    try {
      zipOutputStream.putNextEntry(new ZipEntry("index.html"));
      copyAsset(assetManager, "FtcOfflineFrame.html", zipOutputStream);
      zipOutputStream.closeEntry();
      zipOutputStream.putNextEntry(new ZipEntry("css/blocks_common_less.css"));
      zipOutputStream.write(convertLessToCss(assetManager, "css/blocks_common.less").getBytes());
      zipOutputStream.closeEntry();
      zipOutputStream.putNextEntry(new ZipEntry("css/frame_offline_less.css"));
      zipOutputStream.write(convertLessToCss(assetManager, "css/frame_offline.less").getBytes());
      zipOutputStream.closeEntry();
      zipOutputStream.putNextEntry(new ZipEntry("js/FtcOfflineBlocks.js"));
      zipOutputStream.write(getFtcOfflineBlocksJs(str, hardwareItemMap).getBytes());
      zipOutputStream.closeEntry();
      for (String str1 : hashSet) {
        zipOutputStream.putNextEntry(new ZipEntry(str1));
        copyAsset(assetManager, str1, zipOutputStream);
        zipOutputStream.closeEntry();
      } 
      return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    } finally {
      assetManager = null;
    } 
  }
  
  private static String getFtcOfflineBlocksJs(String paramString, HardwareItemMap paramHardwareItemMap) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("function getBlkFiles() {\n");
    stringBuilder.append("  var BLK_FILES = [\n");
    ArrayList<OfflineBlocksProject> arrayList = new ArrayList();
    ProjectsUtil.fetchProjectsForOfflineBlocksEditor(arrayList);
    Iterator<OfflineBlocksProject> iterator = arrayList.iterator();
    String str;
    for (str = ""; iterator.hasNext(); str = ",\n") {
      OfflineBlocksProject offlineBlocksProject = iterator.next();
      stringBuilder.append(str);
      stringBuilder.append("    {\n");
      stringBuilder.append("      'FileName': '");
      stringBuilder.append(ProjectsUtil.escapeSingleQuotes(offlineBlocksProject.fileName));
      stringBuilder.append("',\n");
      stringBuilder.append("      'Content': '");
      stringBuilder.append(ProjectsUtil.escapeSingleQuotes(offlineBlocksProject.content));
      stringBuilder.append("',\n");
      stringBuilder.append("      'name': '");
      stringBuilder.append(ProjectsUtil.escapeSingleQuotes(offlineBlocksProject.name));
      stringBuilder.append("',\n");
      stringBuilder.append("      'escapedName' : '");
      stringBuilder.append(ProjectsUtil.escapeSingleQuotes(Html.escapeHtml(offlineBlocksProject.name)));
      stringBuilder.append("',\n");
      stringBuilder.append("      'dateModifiedMillis': ");
      stringBuilder.append(offlineBlocksProject.dateModifiedMillis);
      stringBuilder.append(",\n");
      stringBuilder.append("      'enabled': ");
      stringBuilder.append(offlineBlocksProject.enabled);
      stringBuilder.append("\n");
      stringBuilder.append("    }");
    } 
    stringBuilder.append("\n");
    stringBuilder.append("  ];\n");
    stringBuilder.append("  return BLK_FILES;\n");
    stringBuilder.append("}\n\n");
    stringBuilder.append("function getOfflineConfigurationName() {\n");
    stringBuilder.append("  return '");
    stringBuilder.append(ProjectsUtil.escapeSingleQuotes(paramString));
    stringBuilder.append("';\n");
    stringBuilder.append("}\n\n");
    stringBuilder.append("function getSampleNamesJson() {\n");
    stringBuilder.append("  var SAMPLE_NAMES = '");
    stringBuilder.append(ProjectsUtil.fetchSampleNames());
    stringBuilder.append("';\n");
    stringBuilder.append("  return SAMPLE_NAMES;\n");
    stringBuilder.append("}\n\n");
    stringBuilder.append("function getSampleBlkFileContent(sampleName) {\n");
    stringBuilder.append("  switch (sampleName) {\n");
    for (Map.Entry<String, String> entry : ProjectsUtil.getSamples(paramHardwareItemMap).entrySet()) {
      str = (String)entry.getKey();
      String str1 = ((String)entry.getValue()).replace("\n", " ").replaceAll("\\> +\\<", "><");
      if (str.isEmpty())
        stringBuilder.append("    default:\n"); 
      stringBuilder.append("    case '");
      stringBuilder.append(ProjectsUtil.escapeSingleQuotes(str));
      stringBuilder.append("':\n");
      stringBuilder.append("      return '");
      stringBuilder.append(ProjectsUtil.escapeSingleQuotes(str1));
      stringBuilder.append("';\n");
    } 
    stringBuilder.append("  }\n");
    stringBuilder.append("}\n");
    stringBuilder.append(HardwareUtil.fetchJavaScriptForHardware(paramHardwareItemMap));
    return stringBuilder.toString();
  }
  
  private static int hslToColor(float[] paramArrayOffloat) {
    float f1;
    float f2;
    float f3;
    if (paramArrayOffloat[1] == 0.0F) {
      f2 = paramArrayOffloat[2];
      f1 = f2;
      f3 = f1;
    } else {
      if (paramArrayOffloat[2] < 0.5F) {
        f1 = paramArrayOffloat[2] * (paramArrayOffloat[1] + 1.0F);
      } else {
        f1 = paramArrayOffloat[2] + paramArrayOffloat[1] - paramArrayOffloat[2] * paramArrayOffloat[1];
      } 
      float f = paramArrayOffloat[2] * 2.0F - f1;
      f2 = hue2rgb(f, f1, paramArrayOffloat[0] + 0.33333334F);
      f3 = hue2rgb(f, f1, paramArrayOffloat[0]);
      f1 = hue2rgb(f, f1, paramArrayOffloat[0] - 0.33333334F);
    } 
    return Color.rgb(Math.round(f2 * 255.0F), Math.round(f3 * 255.0F), Math.round(f1 * 255.0F));
  }
  
  private static float hue(int paramInt1, int paramInt2, int paramInt3) {
    float[] arrayOfFloat = new float[3];
    Color.RGBToHSV(paramInt1, paramInt2, paramInt3, arrayOfFloat);
    return arrayOfFloat[0] / 360.0F;
  }
  
  private static float hue2rgb(float paramFloat1, float paramFloat2, float paramFloat3) {
    float f = paramFloat3;
    if (paramFloat3 < 0.0F)
      f = paramFloat3 + 1.0F; 
    paramFloat3 = f;
    if (f > 1.0F)
      paramFloat3 = f - 1.0F; 
    if (paramFloat3 < 0.16666667F) {
      paramFloat2 = (paramFloat2 - paramFloat1) * 6.0F * paramFloat3;
      return paramFloat1 + paramFloat2;
    } 
    if (paramFloat3 < 0.5F)
      return paramFloat2; 
    if (paramFloat3 < 0.6666667F) {
      paramFloat2 = (paramFloat2 - paramFloat1) * (0.6666667F - paramFloat3) * 6.0F;
      return paramFloat1 + paramFloat2;
    } 
    return paramFloat1;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontrolle\\util\OfflineBlocksUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
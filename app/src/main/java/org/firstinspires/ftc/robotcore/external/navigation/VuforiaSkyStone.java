package org.firstinspires.ftc.robotcore.external.navigation;

import java.util.HashMap;
import java.util.Map;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;

public class VuforiaSkyStone extends VuforiaBase {
  private static final String ASSET_NAME = "Skystone";
  
  private static final String BLUE_FRONT_BRIDGE = "Blue Front Bridge";
  
  private static final String BLUE_PERIMETER_1 = "Blue Perimeter 1";
  
  private static final String BLUE_PERIMETER_2 = "Blue Perimeter 2";
  
  private static final String BLUE_REAR_BRIDGE = "Blue Rear Bridge";
  
  private static final float BRIDGE_ROTATION_Y = 59.0F;
  
  private static final float BRIDGE_ROTATION_Z = 180.0F;
  
  private static final float BRIDGE_X = 131.57199F;
  
  private static final float BRIDGE_Y = 584.2F;
  
  private static final float BRIDGE_Z = 163.068F;
  
  private static final String FRONT_PERIMETER_1 = "Front Perimeter 1";
  
  private static final String FRONT_PERIMETER_2 = "Front Perimeter 2";
  
  private static final float HALF_FIELD = 1828.7999F;
  
  private static final Map<String, OpenGLMatrix> LOCATIONS_ON_FIELD;
  
  private static final float MM_TARGET_HEIGHT = 152.4F;
  
  private static final float QUAD_FIELD = 914.39996F;
  
  private static final String REAR_PERIMETER_1 = "Rear Perimeter 1";
  
  private static final String REAR_PERIMETER_2 = "Rear Perimeter 2";
  
  private static final String RED_FRONT_BRIDGE = "Red Front Bridge";
  
  private static final String RED_PERIMETER_1 = "Red Perimeter 1";
  
  private static final String RED_PERIMETER_2 = "Red Perimeter 2";
  
  private static final String RED_REAR_BRIDGE = "Red Rear Bridge";
  
  private static final String STONE_TARGET = "Stone Target";
  
  private static final float STONE_Z = 50.8F;
  
  public static final String[] TRACKABLE_NAMES = new String[] { 
      "Stone Target", "Blue Rear Bridge", "Red Rear Bridge", "Red Front Bridge", "Blue Front Bridge", "Red Perimeter 1", "Red Perimeter 2", "Front Perimeter 1", "Front Perimeter 2", "Blue Perimeter 1", 
      "Blue Perimeter 2", "Rear Perimeter 1", "Rear Perimeter 2" };
  
  static {
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    LOCATIONS_ON_FIELD = (Map)hashMap;
    hashMap.put("Stone Target", OpenGLMatrix.translation(0.0F, 0.0F, 50.8F).multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90.0F, 0.0F, -90.0F)));
    LOCATIONS_ON_FIELD.put("Blue Front Bridge", OpenGLMatrix.translation(-131.57199F, 584.2F, 163.068F).multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 0.0F, 59.0F, 180.0F)));
    LOCATIONS_ON_FIELD.put("Blue Rear Bridge", OpenGLMatrix.translation(-131.57199F, 584.2F, 163.068F).multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 0.0F, -59.0F, 180.0F)));
    LOCATIONS_ON_FIELD.put("Red Front Bridge", OpenGLMatrix.translation(-131.57199F, -584.2F, 163.068F).multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 0.0F, -59.0F, 0.0F)));
    LOCATIONS_ON_FIELD.put("Red Rear Bridge", OpenGLMatrix.translation(131.57199F, -584.2F, 163.068F).multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 0.0F, 59.0F, 0.0F)));
    LOCATIONS_ON_FIELD.put("Red Perimeter 1", OpenGLMatrix.translation(914.39996F, -1828.7999F, 152.4F).multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90.0F, 0.0F, 180.0F)));
    LOCATIONS_ON_FIELD.put("Red Perimeter 2", OpenGLMatrix.translation(-914.39996F, -1828.7999F, 152.4F).multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90.0F, 0.0F, 180.0F)));
    LOCATIONS_ON_FIELD.put("Front Perimeter 1", OpenGLMatrix.translation(-1828.7999F, -914.39996F, 152.4F).multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90.0F, 0.0F, 90.0F)));
    LOCATIONS_ON_FIELD.put("Front Perimeter 2", OpenGLMatrix.translation(-1828.7999F, 914.39996F, 152.4F).multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90.0F, 0.0F, 90.0F)));
    LOCATIONS_ON_FIELD.put("Blue Perimeter 1", OpenGLMatrix.translation(-914.39996F, 1828.7999F, 152.4F).multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90.0F, 0.0F, 0.0F)));
    LOCATIONS_ON_FIELD.put("Blue Perimeter 2", OpenGLMatrix.translation(914.39996F, 1828.7999F, 152.4F).multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90.0F, 0.0F, 0.0F)));
    LOCATIONS_ON_FIELD.put("Rear Perimeter 1", OpenGLMatrix.translation(1828.7999F, 914.39996F, 152.4F).multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90.0F, 0.0F, -90.0F)));
    LOCATIONS_ON_FIELD.put("Rear Perimeter 2", OpenGLMatrix.translation(1828.7999F, -914.39996F, 152.4F).multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90.0F, 0.0F, -90.0F)));
  }
  
  public VuforiaSkyStone() {
    super("Skystone", TRACKABLE_NAMES, LOCATIONS_ON_FIELD);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\VuforiaSkyStone.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
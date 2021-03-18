package org.firstinspires.ftc.robotcore.external.navigation;

import java.util.HashMap;
import java.util.Map;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;

public class VuforiaCurrentGame extends VuforiaBase {
  private static final String ASSET_NAME = "UltimateGoal";
  
  private static final String BLUE_ALLIANCE_TARGET = "Blue Alliance Target";
  
  private static final String BLUE_TOWER_GOAL_TARGET = "Blue Tower Goal Target";
  
  private static final String FRONT_WALL_TARGET = "Front Wall Target";
  
  private static final Map<String, OpenGLMatrix> LOCATIONS_ON_FIELD;
  
  private static final String RED_ALLIANCE_TARGET = "Red Alliance Target";
  
  private static final String RED_TOWER_GOAL_TARGET = "Red Tower Goal Target";
  
  public static final String[] TRACKABLE_NAMES = new String[] { "Blue Tower Goal Target", "Red Tower Goal Target", "Red Alliance Target", "Blue Alliance Target", "Front Wall Target" };
  
  private static final float halfField = 1828.7999F;
  
  private static final float mmTargetHeight = 152.4F;
  
  private static final float quadField = 914.39996F;
  
  static {
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    LOCATIONS_ON_FIELD = (Map)hashMap;
    hashMap.put("Red Alliance Target", OpenGLMatrix.translation(0.0F, -1828.7999F, 152.4F).multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90.0F, 0.0F, 180.0F)));
    LOCATIONS_ON_FIELD.put("Blue Alliance Target", OpenGLMatrix.translation(0.0F, 1828.7999F, 152.4F).multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90.0F, 0.0F, 0.0F)));
    LOCATIONS_ON_FIELD.put("Front Wall Target", OpenGLMatrix.translation(-1828.7999F, 0.0F, 152.4F).multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90.0F, 0.0F, 90.0F)));
    LOCATIONS_ON_FIELD.put("Blue Tower Goal Target", OpenGLMatrix.translation(1828.7999F, 914.39996F, 152.4F).multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90.0F, 0.0F, -90.0F)));
    LOCATIONS_ON_FIELD.put("Red Tower Goal Target", OpenGLMatrix.translation(1828.7999F, -914.39996F, 152.4F).multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90.0F, 0.0F, -90.0F)));
  }
  
  public VuforiaCurrentGame() {
    super("UltimateGoal", TRACKABLE_NAMES, LOCATIONS_ON_FIELD);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\VuforiaCurrentGame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
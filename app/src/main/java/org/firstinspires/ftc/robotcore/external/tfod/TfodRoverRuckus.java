package org.firstinspires.ftc.robotcore.external.tfod;

public class TfodRoverRuckus extends TfodBase {
  public static final String[] LABELS = new String[] { "Gold Mineral", "Silver Mineral" };
  
  public static final String LABEL_GOLD_MINERAL = "Gold Mineral";
  
  public static final String LABEL_SILVER_MINERAL = "Silver Mineral";
  
  public static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
  
  public TfodRoverRuckus() {
    super("RoverRuckus.tflite", LABELS);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\tfod\TfodRoverRuckus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
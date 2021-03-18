package org.firstinspires.ftc.robotcore.internal.tfod;

import android.graphics.RectF;
import java.util.Objects;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

class RecognitionImpl implements Recognition {
  private final CameraInformation cameraInformation;
  
  private final float confidence;
  
  private final int frameHeight;
  
  private final float frameHorizontalFocalLength;
  
  private final int frameWidth;
  
  private final String label;
  
  private final RectF location;
  
  private final RectF updatedLocation;
  
  RecognitionImpl(CameraInformation paramCameraInformation, String paramString, float paramFloat, RectF paramRectF) {
    this.cameraInformation = paramCameraInformation;
    this.label = paramString;
    this.confidence = paramFloat;
    this.location = paramRectF;
    this.updatedLocation = new RectF(paramRectF);
    int i = paramCameraInformation.rotation;
    if (i != 0) {
      if (i != 90) {
        if (i != 180) {
          if (i == 270) {
            this.frameHorizontalFocalLength = paramCameraInformation.verticalFocalLength;
            this.frameWidth = paramCameraInformation.size.height;
            this.frameHeight = paramCameraInformation.size.width;
            this.updatedLocation.left = paramCameraInformation.size.height - paramRectF.bottom;
            this.updatedLocation.right = paramCameraInformation.size.height - paramRectF.top;
            this.updatedLocation.top = paramRectF.left;
            this.updatedLocation.bottom = paramRectF.right;
            return;
          } 
          throw new IllegalArgumentException("CameraInformation.rotation must be 0, 90, 180, or 270.");
        } 
        this.frameHorizontalFocalLength = paramCameraInformation.horizontalFocalLength;
        this.frameWidth = paramCameraInformation.size.width;
        this.frameHeight = paramCameraInformation.size.height;
        this.updatedLocation.left = paramCameraInformation.size.width - paramRectF.right;
        this.updatedLocation.right = paramCameraInformation.size.width - paramRectF.left;
        this.updatedLocation.top = paramCameraInformation.size.height - paramRectF.bottom;
        this.updatedLocation.bottom = paramCameraInformation.size.height - paramRectF.top;
        return;
      } 
      this.frameHorizontalFocalLength = paramCameraInformation.verticalFocalLength;
      this.frameWidth = paramCameraInformation.size.height;
      this.frameHeight = paramCameraInformation.size.width;
      this.updatedLocation.left = paramRectF.top;
      this.updatedLocation.right = paramRectF.bottom;
      this.updatedLocation.top = paramCameraInformation.size.width - paramRectF.right;
      this.updatedLocation.bottom = paramCameraInformation.size.width - paramRectF.left;
      return;
    } 
    this.frameHorizontalFocalLength = paramCameraInformation.horizontalFocalLength;
    this.frameWidth = paramCameraInformation.size.width;
    this.frameHeight = paramCameraInformation.size.height;
  }
  
  RecognitionImpl(RecognitionImpl paramRecognitionImpl, RectF paramRectF) {
    this(paramRecognitionImpl.cameraInformation, paramRecognitionImpl.label, paramRecognitionImpl.confidence, paramRectF);
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof RecognitionImpl;
    boolean bool1 = false;
    if (!bool)
      return false; 
    paramObject = paramObject;
    bool = bool1;
    if (this.confidence == ((RecognitionImpl)paramObject).confidence) {
      bool = bool1;
      if (this.label.equals(((RecognitionImpl)paramObject).label)) {
        bool = bool1;
        if (this.location.equals(((RecognitionImpl)paramObject).location))
          bool = true; 
      } 
    } 
    return bool;
  }
  
  public double estimateAngleToObject(AngleUnit paramAngleUnit) {
    double d = this.frameHorizontalFocalLength;
    return paramAngleUnit.fromRadians(Math.atan((this.updatedLocation.centerX() - this.frameWidth * 0.5F) / d));
  }
  
  public float getBottom() {
    return this.updatedLocation.bottom;
  }
  
  public float getConfidence() {
    return this.confidence;
  }
  
  public float getHeight() {
    return this.updatedLocation.height();
  }
  
  public int getImageHeight() {
    return this.frameHeight;
  }
  
  public int getImageWidth() {
    return this.frameWidth;
  }
  
  public String getLabel() {
    return this.label;
  }
  
  public float getLeft() {
    return this.updatedLocation.left;
  }
  
  RectF getLocation() {
    return this.location;
  }
  
  public float getRight() {
    return this.updatedLocation.right;
  }
  
  public float getTop() {
    return this.updatedLocation.top;
  }
  
  public float getWidth() {
    return this.updatedLocation.width();
  }
  
  public int hashCode() {
    return Objects.hash(new Object[] { Float.valueOf(this.confidence), this.label, this.location });
  }
  
  public String toString() {
    return String.format("Recognition(label=%s, confidence=%.3f, left=%.0f, right=%.0f, top=%.0f, bottom=%.0f", new Object[] { this.label, Float.valueOf(this.confidence), Float.valueOf(this.updatedLocation.left), Float.valueOf(this.updatedLocation.right), Float.valueOf(this.updatedLocation.top), Float.valueOf(this.updatedLocation.bottom) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\tfod\RecognitionImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
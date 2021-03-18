package com.qualcomm.robotcore.hardware.configuration.typecontainers;

import com.google.gson.annotations.Expose;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationTypeManager;
import com.qualcomm.robotcore.hardware.configuration.DistributorInfo;
import com.qualcomm.robotcore.hardware.configuration.DistributorInfoState;
import com.qualcomm.robotcore.hardware.configuration.ExpansionHubMotorControllerParamsState;
import com.qualcomm.robotcore.hardware.configuration.ExpansionHubMotorControllerPositionParams;
import com.qualcomm.robotcore.hardware.configuration.ExpansionHubMotorControllerVelocityParams;
import com.qualcomm.robotcore.hardware.configuration.ModernRoboticsMotorControllerParams;
import com.qualcomm.robotcore.hardware.configuration.ModernRoboticsMotorControllerParamsState;
import com.qualcomm.robotcore.hardware.configuration.MotorType;
import com.qualcomm.robotcore.hardware.configuration.annotations.ExpansionHubPIDFPositionParams;
import com.qualcomm.robotcore.hardware.configuration.annotations.ExpansionHubPIDFVelocityParams;
import com.qualcomm.robotcore.hardware.configuration.annotations.MotorType;
import com.qualcomm.robotcore.util.ClassUtil;
import org.firstinspires.ftc.robotcore.external.navigation.Rotation;

public final class MotorConfigurationType extends UserConfigurationType implements Cloneable {
  @Expose
  private double achieveableMaxRPMFraction;
  
  @Expose
  private DistributorInfoState distributorInfo = new DistributorInfoState();
  
  @Expose
  private double gearing;
  
  @Expose
  private ExpansionHubMotorControllerParamsState hubPositionParams = new ExpansionHubMotorControllerParamsState();
  
  @Expose
  private ExpansionHubMotorControllerParamsState hubVelocityParams = new ExpansionHubMotorControllerParamsState();
  
  @Expose
  private double maxRPM;
  
  @Expose
  private ModernRoboticsMotorControllerParamsState modernRoboticsParams = new ModernRoboticsMotorControllerParamsState();
  
  @Expose
  private Rotation orientation;
  
  @Expose
  private double ticksPerRev;
  
  public MotorConfigurationType() {
    super(ConfigurationType.DeviceFlavor.MOTOR);
  }
  
  public MotorConfigurationType(Class paramClass, String paramString) {
    super(paramClass, ConfigurationType.DeviceFlavor.MOTOR, paramString);
  }
  
  public static MotorConfigurationType getMotorType(Class<?> paramClass) {
    return (MotorConfigurationType)ConfigurationTypeManager.getInstance().userTypeFromClass(ConfigurationType.DeviceFlavor.MOTOR, paramClass);
  }
  
  public static MotorConfigurationType getUnspecifiedMotorType() {
    return ConfigurationTypeManager.getInstance().getUnspecifiedMotorType();
  }
  
  private Object writeReplace() {
    return new UserConfigurationType.SerializationProxy(this);
  }
  
  public MotorConfigurationType clone() {
    try {
      MotorConfigurationType motorConfigurationType = (MotorConfigurationType)super.clone();
      motorConfigurationType.distributorInfo = this.distributorInfo.clone();
      motorConfigurationType.modernRoboticsParams = this.modernRoboticsParams.clone();
      motorConfigurationType.hubVelocityParams = this.hubVelocityParams.clone();
      motorConfigurationType.hubPositionParams = this.hubPositionParams.clone();
      return motorConfigurationType;
    } catch (CloneNotSupportedException cloneNotSupportedException) {
      throw new RuntimeException("internal error: Parameters not cloneable");
    } 
  }
  
  public void finishedAnnotations(Class paramClass) {
    if (this.name.isEmpty())
      this.name = paramClass.getSimpleName(); 
  }
  
  public double getAchieveableMaxRPMFraction() {
    return this.achieveableMaxRPMFraction;
  }
  
  public double getAchieveableMaxTicksPerSecond() {
    return getTicksPerRev() * getMaxRPM() * getAchieveableMaxRPMFraction() / 60.0D;
  }
  
  public int getAchieveableMaxTicksPerSecondRounded() {
    return (int)Math.round(getAchieveableMaxTicksPerSecond());
  }
  
  public DistributorInfoState getDistributorInfo() {
    return this.distributorInfo;
  }
  
  public double getGearing() {
    return this.gearing;
  }
  
  public ExpansionHubMotorControllerParamsState getHubPositionParams() {
    return this.hubPositionParams;
  }
  
  public ExpansionHubMotorControllerParamsState getHubVelocityParams() {
    return this.hubVelocityParams;
  }
  
  public double getMaxRPM() {
    return this.maxRPM;
  }
  
  public ModernRoboticsMotorControllerParamsState getModernRoboticsParams() {
    return this.modernRoboticsParams;
  }
  
  public Rotation getOrientation() {
    return this.orientation;
  }
  
  public double getTicksPerRev() {
    return this.ticksPerRev;
  }
  
  public boolean hasExpansionHubPositionParams() {
    return this.hubPositionParams.isDefault() ^ true;
  }
  
  public boolean hasExpansionHubVelocityParams() {
    return this.hubVelocityParams.isDefault() ^ true;
  }
  
  public boolean hasModernRoboticsParams() {
    return this.modernRoboticsParams.isDefault() ^ true;
  }
  
  public boolean processAnnotation(DistributorInfo paramDistributorInfo) {
    if (paramDistributorInfo != null) {
      if (this.name.isEmpty()) {
        String str1 = ClassUtil.decodeStringRes(paramDistributorInfo.distributor().trim());
        String str2 = ClassUtil.decodeStringRes(paramDistributorInfo.model().trim());
        if (!str1.isEmpty() && !str2.isEmpty()) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(str1);
          stringBuilder.append(" ");
          stringBuilder.append(str2);
          this.name = stringBuilder.toString();
        } 
      } 
      this.distributorInfo = DistributorInfoState.from(paramDistributorInfo);
      return true;
    } 
    return false;
  }
  
  public boolean processAnnotation(ExpansionHubMotorControllerPositionParams paramExpansionHubMotorControllerPositionParams) {
    if (paramExpansionHubMotorControllerPositionParams != null) {
      this.hubPositionParams = new ExpansionHubMotorControllerParamsState(paramExpansionHubMotorControllerPositionParams);
      return true;
    } 
    return false;
  }
  
  public boolean processAnnotation(ExpansionHubMotorControllerVelocityParams paramExpansionHubMotorControllerVelocityParams) {
    if (paramExpansionHubMotorControllerVelocityParams != null) {
      this.hubVelocityParams = new ExpansionHubMotorControllerParamsState(paramExpansionHubMotorControllerVelocityParams);
      return true;
    } 
    return false;
  }
  
  public boolean processAnnotation(ModernRoboticsMotorControllerParams paramModernRoboticsMotorControllerParams) {
    if (paramModernRoboticsMotorControllerParams != null) {
      this.modernRoboticsParams = new ModernRoboticsMotorControllerParamsState(paramModernRoboticsMotorControllerParams);
      return true;
    } 
    return false;
  }
  
  public boolean processAnnotation(MotorType paramMotorType) {
    if (paramMotorType != null) {
      if (this.name.isEmpty())
        this.name = ClassUtil.decodeStringRes(paramMotorType.name().trim()); 
      this.ticksPerRev = paramMotorType.ticksPerRev();
      this.gearing = paramMotorType.gearing();
      this.maxRPM = paramMotorType.maxRPM();
      this.achieveableMaxRPMFraction = paramMotorType.achieveableMaxRPMFraction();
      this.orientation = paramMotorType.orientation();
      return true;
    } 
    return false;
  }
  
  public boolean processAnnotation(ExpansionHubPIDFPositionParams paramExpansionHubPIDFPositionParams) {
    if (paramExpansionHubPIDFPositionParams != null) {
      this.hubPositionParams = new ExpansionHubMotorControllerParamsState(paramExpansionHubPIDFPositionParams);
      return true;
    } 
    return false;
  }
  
  public boolean processAnnotation(ExpansionHubPIDFVelocityParams paramExpansionHubPIDFVelocityParams) {
    if (paramExpansionHubPIDFVelocityParams != null) {
      this.hubVelocityParams = new ExpansionHubMotorControllerParamsState(paramExpansionHubPIDFVelocityParams);
      return true;
    } 
    return false;
  }
  
  public boolean processAnnotation(MotorType paramMotorType) {
    if (paramMotorType != null) {
      this.ticksPerRev = paramMotorType.ticksPerRev();
      this.gearing = paramMotorType.gearing();
      this.maxRPM = paramMotorType.maxRPM();
      this.achieveableMaxRPMFraction = paramMotorType.achieveableMaxRPMFraction();
      this.orientation = paramMotorType.orientation();
      return true;
    } 
    return false;
  }
  
  public boolean processAnnotation(Object paramObject) {
    if (paramObject != null) {
      if (paramObject instanceof ExpansionHubPIDFVelocityParams)
        return processAnnotation((ExpansionHubPIDFVelocityParams)paramObject); 
      if (paramObject instanceof ExpansionHubMotorControllerVelocityParams)
        return processAnnotation((ExpansionHubMotorControllerVelocityParams)paramObject); 
      if (paramObject instanceof ExpansionHubPIDFPositionParams)
        return processAnnotation((ExpansionHubPIDFPositionParams)paramObject); 
      if (paramObject instanceof ExpansionHubMotorControllerPositionParams)
        return processAnnotation((ExpansionHubMotorControllerPositionParams)paramObject); 
      if (paramObject instanceof ModernRoboticsMotorControllerParams)
        return processAnnotation((ModernRoboticsMotorControllerParams)paramObject); 
      if (paramObject instanceof DistributorInfo)
        return processAnnotation((DistributorInfo)paramObject); 
    } 
    return false;
  }
  
  public void setAchieveableMaxRPMFraction(double paramDouble) {
    this.achieveableMaxRPMFraction = paramDouble;
  }
  
  public void setGearing(double paramDouble) {
    this.gearing = paramDouble;
  }
  
  public void setMaxRPM(double paramDouble) {
    this.maxRPM = paramDouble;
  }
  
  public void setOrientation(Rotation paramRotation) {
    this.orientation = paramRotation;
  }
  
  public void setTicksPerRev(double paramDouble) {
    this.ticksPerRev = paramDouble;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\typecontainers\MotorConfigurationType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.google.blocks.ftcrobotcontroller.util;

public enum Identifier {
  ACCELERATION("accelerationAccess", "accelerationIdentifierForJavaScript", null, null),
  ANDROID_ACCELEROMETER("androidAccelerometerAccess", "androidAccelerometerIdentifierForJavaScript", "androidAccelerometer", "androidAccelerometerIdentifierForFtcJava"),
  ANDROID_GYROSCOPE("androidGyroscopeAccess", "androidGyroscopeIdentifierForJavaScript", "androidGyroscope", "androidGyroscopeIdentifierForFtcJava"),
  ANDROID_ORIENTATION("androidOrientationAccess", "androidOrientationIdentifierForJavaScript", "androidOrientation", "androidOrientationIdentifierForFtcJava"),
  ANDROID_SOUND_POOL("androidSoundPoolAccess", "androidSoundPoolIdentifierForJavaScript", "androidSoundPool", "androidSoundPoolIdentifierForFtcJava"),
  ANDROID_TEXT_TO_SPEECH("androidTextToSpeechAccess", "androidTextToSpeechIdentifierForJavaScript", "androidTextToSpeech", "androidTextToSpeechIdentifierForFtcJava"),
  ANGULAR_VELOCITY("angularVelocityAccess", "angularVelocityIdentifierForJavaScript", null, null),
  BLINKIN_PATTERN("blinkinPatternAccess", "blinkinPatternIdentifierForJavaScript", null, null),
  BLOCKS_OP_MODE("blocksOpMode", null, null, null),
  BNO055IMU_PARAMETERS("bno055imuParametersAccess", "bno055imuParametersIdentifierForJavaScript", null, null),
  COLOR("colorAccess", "colorIdentifierForJavaScript", null, null),
  DBG_LOG("dbgLogAccess", "dbgLogIdentifierForJavaScript", null, null),
  ELAPSED_TIME("elapsedTimeAccess", "elapsedTimeIdentifierForJavaScript", null, null),
  GAMEPAD_1("gamepad1", null, "gamepad1", null),
  GAMEPAD_2("gamepad2", null, "gamepad2", null),
  LINEAR_OP_MODE("linearOpMode", "linearOpModeIdentifierForJavaScript", null, null),
  MAGNETIC_FLUX("magneticFluxAccess", "magneticFluxIdentifierForJavaScript", null, null),
  MATRIX_F("matrixFAccess", "matrixFIdentifierForJavaScript", null, null),
  MISC("miscAccess", "miscIdentifierForJavaScript", null, null),
  NAVIGATION("navigationAccess", "navigationIdentifierForJavaScript", null, null),
  OPEN_GL_MATRIX("openGLMatrixAccess", "openGLMatrixIdentifierForJavaScript", null, null),
  ORIENTATION("orientationAccess", "orientationIdentifierForJavaScript", null, null),
  PIDF_COEFFICIENTS("pidfCoefficientsAccess", "pidfCoefficientsIdentifierForJavaScript", null, null),
  POSITION("positionAccess", "positionIdentifierForJavaScript", null, null),
  QUATERNION("quaternionAccess", "quaternionIdentifierForJavaScript", null, null),
  RANGE("rangeAccess", "rangeIdentifierForJavaScript", null, null),
  SYSTEM("systemAccess", "systemIdentifierForJavaScript", null, null),
  TELEMETRY("telemetry", "telemetryIdentifierForJavaScript", null, null),
  TEMPERATURE("temperatureAccess", "temperatureIdentifierForJavaScript", null, null),
  TFOD_CURRENT_GAME("temperatureAccess", "temperatureIdentifierForJavaScript", null, null),
  TFOD_CUSTOM_MODEL("tfodCustomModelAccess", "tfodCustomModelIdentifierForJavaScript", "tfodCustomModel", "tfodCustomModelIdentifierForFtcJava"),
  TFOD_ROVER_RUCKUS("tfodCustomModelAccess", "tfodCustomModelIdentifierForJavaScript", "tfodCustomModel", "tfodCustomModelIdentifierForFtcJava"),
  TFOD_SKY_STONE("tfodCustomModelAccess", "tfodCustomModelIdentifierForJavaScript", "tfodCustomModel", "tfodCustomModelIdentifierForFtcJava"),
  VECTOR_F("tfodCustomModelAccess", "tfodCustomModelIdentifierForJavaScript", "tfodCustomModel", "tfodCustomModelIdentifierForFtcJava"),
  VELOCITY("tfodCustomModelAccess", "tfodCustomModelIdentifierForJavaScript", "tfodCustomModel", "tfodCustomModelIdentifierForFtcJava"),
  VUFORIA_CURRENT_GAME("tfodCustomModelAccess", "tfodCustomModelIdentifierForJavaScript", "tfodCustomModel", "tfodCustomModelIdentifierForFtcJava"),
  VUFORIA_LOCALIZER("tfodCustomModelAccess", "tfodCustomModelIdentifierForJavaScript", "tfodCustomModel", "tfodCustomModelIdentifierForFtcJava"),
  VUFORIA_LOCALIZER_PARAMETERS("tfodCustomModelAccess", "tfodCustomModelIdentifierForJavaScript", "tfodCustomModel", "tfodCustomModelIdentifierForFtcJava"),
  VUFORIA_RELIC_RECOVERY("tfodCustomModelAccess", "tfodCustomModelIdentifierForJavaScript", "tfodCustomModel", "tfodCustomModelIdentifierForFtcJava"),
  VUFORIA_ROVER_RUCKUS("tfodCustomModelAccess", "tfodCustomModelIdentifierForJavaScript", "tfodCustomModel", "tfodCustomModelIdentifierForFtcJava"),
  VUFORIA_SKY_STONE("tfodCustomModelAccess", "tfodCustomModelIdentifierForJavaScript", "tfodCustomModel", "tfodCustomModelIdentifierForFtcJava"),
  VUFORIA_TRACKABLE("tfodCustomModelAccess", "tfodCustomModelIdentifierForJavaScript", "tfodCustomModel", "tfodCustomModelIdentifierForFtcJava"),
  VUFORIA_TRACKABLES("tfodCustomModelAccess", "tfodCustomModelIdentifierForJavaScript", "tfodCustomModel", "tfodCustomModelIdentifierForFtcJava"),
  VUFORIA_TRACKABLE_DEFAULT_LISTENER("tfodCustomModelAccess", "tfodCustomModelIdentifierForJavaScript", "tfodCustomModel", "tfodCustomModelIdentifierForFtcJava");
  
  public final String identifierForFtcJava;
  
  public final String identifierForJavaScript;
  
  public final String variableForFtcJava;
  
  public final String variableForJavaScript;
  
  static {
    TFOD_CURRENT_GAME = new Identifier("TFOD_CURRENT_GAME", 30, "tfodCurrentGameAccess", "tfodCurrentGameIdentifierForJavaScript", "tfodUltimateGoal", "tfodCurrentGameIdentifierForFtcJava");
    TFOD_ROVER_RUCKUS = new Identifier("TFOD_ROVER_RUCKUS", 31, "tfodRoverRuckusAccess", "tfodRoverRuckusIdentifierForJavaScript", "tfodRoverRuckus", "tfodRoverRuckusIdentifierForFtcJava");
    TFOD_SKY_STONE = new Identifier("TFOD_SKY_STONE", 32, "tfodSkyStoneAccess", "tfodSkyStoneIdentifierForJavaScript", "tfodSkyStone", "tfodSkyStoneIdentifierForFtcJava");
    VECTOR_F = new Identifier("VECTOR_F", 33, "vectorFAccess", "vectorFIdentifierForJavaScript", null, null);
    VELOCITY = new Identifier("VELOCITY", 34, "velocityAccess", "velocityIdentifierForJavaScript", null, null);
    VUFORIA_CURRENT_GAME = new Identifier("VUFORIA_CURRENT_GAME", 35, "vuforiaCurrentGameAccess", "vuforiaCurrentGameIdentifierForJavaScript", "vuforiaUltimateGoal", "vuforiaCurrentGameIdentifierForFtcJava");
    VUFORIA_RELIC_RECOVERY = new Identifier("VUFORIA_RELIC_RECOVERY", 36, "vuforiaAccess", "vuforiaIdentifierForJavaScript", "vuforiaRelicRecovery", "vuforiaRelicRecoveryIdentifierForFtcJava");
    VUFORIA_ROVER_RUCKUS = new Identifier("VUFORIA_ROVER_RUCKUS", 37, "vuforiaRoverRuckusAccess", "vuforiaRoverRuckusIdentifierForJavaScript", "vuforiaRoverRuckus", "vuforiaRoverRuckusIdentifierForFtcJava");
    VUFORIA_SKY_STONE = new Identifier("VUFORIA_SKY_STONE", 38, "vuforiaSkyStoneAccess", "vuforiaSkyStoneIdentifierForJavaScript", "vuforiaSkyStone", "vuforiaSkyStoneIdentifierForFtcJava");
    VUFORIA_LOCALIZER = new Identifier("VUFORIA_LOCALIZER", 39, "vuforiaLocalizerAccess", "vuforiaLocalizerIdentifierForJavaScript", null, null);
    VUFORIA_LOCALIZER_PARAMETERS = new Identifier("VUFORIA_LOCALIZER_PARAMETERS", 40, "vuforiaLocalizerParametersAccess", "vuforiaLocalizerParametersIdentifierForJavaScript", null, null);
    VUFORIA_TRACKABLE = new Identifier("VUFORIA_TRACKABLE", 41, "vuforiaTrackableAccess", "vuforiaTrackableIdentifierForJavaScript", null, null);
    VUFORIA_TRACKABLE_DEFAULT_LISTENER = new Identifier("VUFORIA_TRACKABLE_DEFAULT_LISTENER", 42, "vuforiaTrackableDefaultListenerAccess", "vuforiaTrackableDefaultListenerIdentifierForJavaScript", null, null);
    Identifier identifier = new Identifier("VUFORIA_TRACKABLES", 43, "vuforiaTrackablesAccess", "vuforiaTrackablesIdentifierForJavaScript", null, null);
    VUFORIA_TRACKABLES = identifier;
    $VALUES = new Identifier[] { 
        ACCELERATION, ANDROID_ACCELEROMETER, ANDROID_GYROSCOPE, ANDROID_ORIENTATION, ANDROID_SOUND_POOL, ANDROID_TEXT_TO_SPEECH, ANGULAR_VELOCITY, BLINKIN_PATTERN, BLOCKS_OP_MODE, BNO055IMU_PARAMETERS, 
        COLOR, DBG_LOG, ELAPSED_TIME, GAMEPAD_1, GAMEPAD_2, LINEAR_OP_MODE, MAGNETIC_FLUX, MATRIX_F, MISC, NAVIGATION, 
        OPEN_GL_MATRIX, ORIENTATION, PIDF_COEFFICIENTS, POSITION, QUATERNION, RANGE, SYSTEM, TELEMETRY, TEMPERATURE, TFOD_CUSTOM_MODEL, 
        TFOD_CURRENT_GAME, TFOD_ROVER_RUCKUS, TFOD_SKY_STONE, VECTOR_F, VELOCITY, VUFORIA_CURRENT_GAME, VUFORIA_RELIC_RECOVERY, VUFORIA_ROVER_RUCKUS, VUFORIA_SKY_STONE, VUFORIA_LOCALIZER, 
        VUFORIA_LOCALIZER_PARAMETERS, VUFORIA_TRACKABLE, VUFORIA_TRACKABLE_DEFAULT_LISTENER, identifier };
  }
  
  Identifier(String paramString1, String paramString2, String paramString3, String paramString4) {
    this.identifierForJavaScript = paramString1;
    this.variableForJavaScript = paramString2;
    this.identifierForFtcJava = paramString3;
    this.variableForFtcJava = paramString4;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontrolle\\util\Identifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
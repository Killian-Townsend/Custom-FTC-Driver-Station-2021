package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareUtil;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson;
import org.firstinspires.ftc.robotcore.internal.opmode.BlocksClassFilter;

class MiscAccess extends Access {
  MiscAccess(BlocksOpMode paramBlocksOpMode, String paramString) {
    super(paramBlocksOpMode, paramString, "");
  }
  
  private Class adjustParameterType(Class<Double> paramClass) {
    if (paramClass.equals(boolean.class))
      return Boolean.class; 
    if (paramClass.equals(char.class))
      return Character.class; 
    if (paramClass.equals(byte.class))
      return Byte.class; 
    if (paramClass.equals(short.class))
      return Short.class; 
    if (paramClass.equals(int.class))
      return Integer.class; 
    if (paramClass.equals(long.class))
      return Long.class; 
    if (paramClass.equals(float.class))
      return Float.class; 
    Class<Double> clazz = paramClass;
    if (paramClass.equals(double.class))
      clazz = Double.class; 
    return clazz;
  }
  
  private Object callJavaVarArgs(String paramString1, String paramString2, Object... paramVarArgs) {
    BlocksOpMode blocksOpMode;
    StringBuilder stringBuilder1;
    BlockType blockType = BlockType.FUNCTION;
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append("Java method ");
    stringBuilder2.append(BlocksClassFilter.getUserVisibleName(paramString1));
    startBlockExecution(blockType, stringBuilder2.toString());
    Method method = BlocksClassFilter.getInstance().findMethod(paramString1);
    if (method == null) {
      blocksOpMode = this.blocksOpMode;
      stringBuilder1 = new StringBuilder();
      stringBuilder1.append("Could not find method ");
      stringBuilder1.append(paramString1);
      stringBuilder1.append(".");
      blocksOpMode.throwException(new RuntimeException(stringBuilder1.toString()));
      return null;
    } 
    Object[] arrayOfObject2 = (Object[])SimpleGson.getInstance().fromJson((String)blocksOpMode, Object[].class);
    Class[] arrayOfClass = method.getParameterTypes();
    if (arrayOfObject2.length != arrayOfClass.length || stringBuilder1.length < arrayOfClass.length) {
      this.blocksOpMode.throwException(new RuntimeException("Number of arguments does not match required number of parameters."));
      return null;
    } 
    String[] arrayOfString = HardwareUtil.getParameterLabels(method);
    ArrayList<Gamepad> arrayList = new ArrayList();
    int i = arrayOfClass.length;
    Object[] arrayOfObject1 = new Object[i];
    int j;
    for (j = 0; j < i; j++)
      arrayOfObject1[j] = determineArgument(adjustParameterType(arrayOfClass[j]), stringBuilder1[j], arrayOfObject2[j], arrayOfString[j], arrayList); 
    try {
      return method.invoke(null, arrayOfObject1);
    } catch (Exception exception) {
      BlocksOpMode blocksOpMode1 = this.blocksOpMode;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unable to invoke method ");
      stringBuilder.append(paramString1);
      stringBuilder.append(".");
      blocksOpMode1.throwException(new RuntimeException(stringBuilder.toString(), exception));
      return null;
    } 
  }
  
  private Object coerceStringValue(String paramString, Class paramClass) {
    if (paramClass.equals(Character.class)) {
      if (paramString.length() >= 1)
        return new Character(paramString.charAt(0)); 
    } else {
      if (paramClass.equals(Byte.class))
        return Byte.valueOf((byte)(int)round(paramString)); 
      if (paramClass.equals(Short.class))
        return Short.valueOf((short)(int)round(paramString)); 
      if (paramClass.equals(Integer.class))
        return Integer.valueOf((int)round(paramString)); 
      if (paramClass.equals(Long.class))
        return Long.valueOf(round(paramString)); 
      if (paramClass.equals(Float.class))
        return Float.valueOf(paramString); 
      if (paramClass.equals(Double.class))
        return Double.valueOf(paramString); 
      if (paramClass.isEnum())
        return coerceToEnum(paramString, paramClass); 
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Unable to convert \"");
    stringBuilder.append(paramString);
    stringBuilder.append("\" to ");
    stringBuilder.append(paramClass);
    throw new RuntimeException(stringBuilder.toString());
  }
  
  private Object coerceToEnum(String paramString, Class<Enum> paramClass) {
    return checkArg(paramString, paramClass, paramClass.getSimpleName());
  }
  
  private Object determineArgument(Class<Exception> paramClass, Object paramObject1, Object paramObject2, String paramString, List<Gamepad> paramList) {
    if (paramClass.equals(LinearOpMode.class) || paramClass.equals(OpMode.class))
      return this.blocksOpMode; 
    if (paramClass.equals(HardwareMap.class))
      return this.blocksOpMode.hardwareMap; 
    if (paramClass.equals(Telemetry.class))
      return this.blocksOpMode.telemetry; 
    if (paramClass.equals(Gamepad.class)) {
      if (paramString.equals("gamepad1"))
        return this.blocksOpMode.gamepad1; 
      if (paramString.equals("gamepad2"))
        return this.blocksOpMode.gamepad2; 
      if (paramList.isEmpty()) {
        paramList.add(this.blocksOpMode.gamepad1);
        paramList.add(this.blocksOpMode.gamepad2);
      } 
      return paramList.remove(0);
    } 
    if (paramObject1 == null) {
      if (paramObject2 == null)
        return null; 
      if (paramClass.equals(paramObject2.getClass()))
        return paramObject2; 
      if (paramObject2 instanceof String)
        try {
          return coerceStringValue((String)paramObject2, paramClass);
        } catch (Exception exception) {
          try {
            return paramClass.cast(paramObject2);
          } catch (Exception exception1) {}
        }  
    } else {
      return paramClass.equals(paramObject1.getClass()) ? paramObject1 : paramClass.cast(paramObject1);
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Unable to convert ");
    stringBuilder.append(paramObject1);
    stringBuilder.append(" and/or ");
    stringBuilder.append(paramObject2);
    stringBuilder.append(" to ");
    stringBuilder.append(paramClass);
    stringBuilder.append(".");
    throw new RuntimeException(stringBuilder.toString());
  }
  
  private static long round(String paramString) {
    return Math.round(Double.valueOf(paramString).doubleValue());
  }
  
  @JavascriptInterface
  public Object callJava(String paramString1, String paramString2, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9, Object paramObject10, Object paramObject11, Object paramObject12, Object paramObject13, Object paramObject14, Object paramObject15, Object paramObject16, Object paramObject17, Object paramObject18, Object paramObject19, Object paramObject20, Object paramObject21) {
    return callJavaVarArgs(paramString1, paramString2, new Object[] { 
          paramObject1, paramObject2, paramObject3, paramObject4, paramObject5, paramObject6, paramObject7, paramObject8, paramObject9, paramObject10, 
          paramObject11, paramObject12, paramObject13, paramObject14, paramObject15, paramObject16, paramObject17, paramObject18, paramObject19, paramObject20, 
          paramObject21 });
  }
  
  @JavascriptInterface
  public String callJava_String(String paramString1, String paramString2, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9, Object paramObject10, Object paramObject11, Object paramObject12, Object paramObject13, Object paramObject14, Object paramObject15, Object paramObject16, Object paramObject17, Object paramObject18, Object paramObject19, Object paramObject20, Object paramObject21) {
    Object object = callJavaVarArgs(paramString1, paramString2, new Object[] { 
          paramObject1, paramObject2, paramObject3, paramObject4, paramObject5, paramObject6, paramObject7, paramObject8, paramObject9, paramObject10, 
          paramObject11, paramObject12, paramObject13, paramObject14, paramObject15, paramObject16, paramObject17, paramObject18, paramObject19, paramObject20, 
          paramObject21 });
    return (object == null) ? null : object.toString();
  }
  
  @JavascriptInterface
  public boolean callJava_boolean(String paramString1, String paramString2, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9, Object paramObject10, Object paramObject11, Object paramObject12, Object paramObject13, Object paramObject14, Object paramObject15, Object paramObject16, Object paramObject17, Object paramObject18, Object paramObject19, Object paramObject20, Object paramObject21) {
    return ((Boolean)callJavaVarArgs(paramString1, paramString2, new Object[] { 
          paramObject1, paramObject2, paramObject3, paramObject4, paramObject5, paramObject6, paramObject7, paramObject8, paramObject9, paramObject10, 
          paramObject11, paramObject12, paramObject13, paramObject14, paramObject15, paramObject16, paramObject17, paramObject18, paramObject19, paramObject20, 
          paramObject21 })).booleanValue();
  }
  
  @JavascriptInterface
  public String formatNumber(double paramDouble, int paramInt) {
    startBlockExecution(BlockType.FUNCTION, "formatNumber");
    return JavaUtil.formatNumber(paramDouble, paramInt);
  }
  
  @JavascriptInterface
  public Object getNull() {
    startBlockExecution(BlockType.SPECIAL, "null");
    return null;
  }
  
  @JavascriptInterface
  public OpenGLMatrix getUpdatedRobotLocation(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6) {
    startBlockExecution(BlockType.FUNCTION, "VuforiaTrackingResults", ".getUpdatedRobotLocation");
    return OpenGLMatrix.translation(paramFloat1, paramFloat2, paramFloat3).multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, paramFloat4, paramFloat5, paramFloat6));
  }
  
  @JavascriptInterface
  public boolean isNotNull(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, "isNotNull");
    return (paramObject != null);
  }
  
  @JavascriptInterface
  public boolean isNull(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, "isNull");
    return (paramObject == null);
  }
  
  @JavascriptInterface
  public double roundDecimal(double paramDouble, int paramInt) {
    startBlockExecution(BlockType.FUNCTION, "roundDecimal");
    return Double.parseDouble(JavaUtil.formatNumber(paramDouble, paramInt));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\MiscAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
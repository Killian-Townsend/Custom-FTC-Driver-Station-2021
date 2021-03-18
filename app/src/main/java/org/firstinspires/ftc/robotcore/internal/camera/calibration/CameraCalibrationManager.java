package org.firstinspires.ftc.robotcore.internal.camera.calibration;

import android.content.res.XmlResourceParser;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.util.ClassUtil;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.robotcore.internal.system.Tracer;
import org.firstinspires.ftc.robotcore.internal.usb.UsbConstants;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class CameraCalibrationManager {
  public static final String TAG = "CameraCalibrationManager";
  
  public static boolean Trace = true;
  
  protected HashMap<CameraCalibrationIdentity, List<CameraCalibration>> calibrationMap = new HashMap<CameraCalibrationIdentity, List<CameraCalibration>>();
  
  protected XmlPullParser parser;
  
  protected final Tracer tracer = Tracer.create("CameraCalibrationManager", Trace);
  
  public CameraCalibrationManager(List<XmlPullParser> paramList) {
    addCalibrationsFromXmlResource(R.xml.builtinwebcamcalibrations);
    if (paramList != null)
      for (XmlPullParser xmlPullParser : paramList) {
        try {
          addCalibrationsFromXmlParser(xmlPullParser);
          continue;
        } catch (XmlPullParserException xmlPullParserException) {
        
        } catch (IOException iOException) {}
        RobotLog.ee("CameraCalibrationManager", iOException, "failure parsing external camera calibrations XML; ignoring this calibration");
      }  
  }
  
  protected static float[] parseFloatArray(int paramInt, String paramString) throws NumberFormatException, IllegalArgumentException {
    if (paramString == null)
      return new float[paramInt]; 
    String[] arrayOfString = paramString.replace(',', ' ').split("\\s+");
    int k = arrayOfString.length;
    float[] arrayOfFloat = new float[k];
    int m = arrayOfString.length;
    int j = 0;
    for (int i = j; j < m; i++) {
      arrayOfFloat[i] = Float.parseFloat(arrayOfString[j]);
      j++;
    } 
    if (k == paramInt)
      return arrayOfFloat; 
    throw Misc.illegalArgumentException("xml element expected to contain %d floats, contains %s", new Object[] { Integer.valueOf(paramInt), Integer.valueOf(k) });
  }
  
  protected void addCalibration(CameraCalibrationIdentity paramCameraCalibrationIdentity, CameraCalibration paramCameraCalibration) {
    List<CameraCalibration> list;
    if (this.calibrationMap.containsKey(paramCameraCalibrationIdentity)) {
      list = this.calibrationMap.get(paramCameraCalibrationIdentity);
    } else {
      list = new ArrayList();
    } 
    for (int i = 0; i < list.size(); i++) {
      CameraCalibration cameraCalibration = list.get(i);
      if (paramCameraCalibration.size.equals(cameraCalibration.size)) {
        list.remove(i);
        break;
      } 
    } 
    if (!paramCameraCalibration.remove) {
      list.add(paramCameraCalibration);
      this.calibrationMap.put(paramCameraCalibrationIdentity, list);
    } 
  }
  
  protected void addCalibrationsFromXmlParser(XmlPullParser paramXmlPullParser) throws XmlPullParserException, IOException {
    this.parser = paramXmlPullParser;
    try {
      for (int i = paramXmlPullParser.getEventType(); i != 1; i = paramXmlPullParser.next()) {
        if (i == 2) {
          String str = paramXmlPullParser.getName();
          i = -1;
          int j = str.hashCode();
          if (j != 1275525097) {
            if (j == 1591624140 && str.equals("CalibrationRoot"))
              i = 0; 
          } else if (str.equals("Calibrations")) {
            i = 1;
          } 
          if (i != 0 && i != 1) {
            parseIgnoreElementChildren();
          } else {
            parseCalibrations();
          } 
        } 
      } 
      return;
    } finally {
      this.parser = null;
    } 
  }
  
  protected void addCalibrationsFromXmlResource(int paramInt) {
    XmlResourceParser xmlResourceParser;
    if (paramInt != 0) {
      Exception exception;
      xmlResourceParser = AppUtil.getDefContext().getResources().getXml(paramInt);
      try {
        addCalibrationsFromXmlParser((XmlPullParser)xmlResourceParser);
        xmlResourceParser.close();
        return;
      } catch (XmlPullParserException xmlPullParserException) {
      
      } catch (IOException null) {
      
      } finally {}
      RobotLog.ee("CameraCalibrationManager", exception, "failure parsing external camera calibrations");
    } else {
      return;
    } 
    xmlResourceParser.close();
  }
  
  protected int decodeInt(String paramString, int paramInt) {
    try {
      return Integer.decode(paramString).intValue();
    } catch (Exception exception) {
      return paramInt;
    } 
  }
  
  int decodePid(String paramString) {
    try {
      return Integer.decode(paramString).intValue();
    } catch (NumberFormatException numberFormatException) {
      return 0;
    } 
  }
  
  int decodeVid(String paramString) {
    try {
      return Integer.decode(paramString).intValue();
    } catch (NumberFormatException numberFormatException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("VENDOR_ID_");
      stringBuilder.append(paramString.toUpperCase());
      Field field = ClassUtil.getDeclaredField(UsbConstants.class, stringBuilder.toString());
      if (field != null && field.getType() == int.class)
        try {
          return field.getInt(null);
        } catch (Exception exception) {
          return 0;
        }  
      return 0;
    } 
  }
  
  protected String getAttributeValue(String... paramVarArgs) {
    int j = paramVarArgs.length;
    for (int i = 0; i < j; i++) {
      String str = paramVarArgs[i];
      str = this.parser.getAttributeValue(null, str);
      if (str != null)
        return str; 
    } 
    return null;
  }
  
  public CameraCalibration getCalibration(CameraCalibrationIdentity paramCameraCalibrationIdentity, Size paramSize) {
    CameraCalibration cameraCalibration2 = internalGetCalibration(paramCameraCalibrationIdentity, paramSize);
    CameraCalibration cameraCalibration1 = cameraCalibration2;
    if (cameraCalibration2 == null)
      cameraCalibration1 = CameraCalibration.forUnavailable(paramCameraCalibrationIdentity, paramSize); 
    this.tracer.trace("getCameraCalibration(): %s", new Object[] { cameraCalibration1 });
    return cameraCalibration1;
  }
  
  protected CameraCalibration[] getCameraCalibrationsWithAspectRatio(CameraCalibrationIdentity paramCameraCalibrationIdentity, Size paramSize) {
    final double targetDiagonal = CameraCalibration.getAspectRatio(paramSize);
    ArrayList<CameraCalibration> arrayList = new ArrayList();
    List<CameraCalibration> list = this.calibrationMap.get(paramCameraCalibrationIdentity);
    if (list != null) {
      int i;
      for (i = 0; i < list.size(); i++) {
        CameraCalibration cameraCalibration = list.get(i);
        if (Misc.approximatelyEquals(cameraCalibration.getAspectRatio(), d))
          arrayList.add(cameraCalibration); 
      } 
    } 
    d = CameraCalibration.getDiagonal(paramSize);
    CameraCalibration[] arrayOfCameraCalibration = arrayList.<CameraCalibration>toArray(new CameraCalibration[arrayList.size()]);
    Arrays.sort(arrayOfCameraCalibration, new Comparator<CameraCalibration>() {
          public int compare(CameraCalibration param1CameraCalibration1, CameraCalibration param1CameraCalibration2) {
            double d = Math.abs(param1CameraCalibration1.getDiagonal() - targetDiagonal) - Math.abs(param1CameraCalibration2.getDiagonal() - targetDiagonal);
            return (d < 0.0D) ? -1 : ((d > 0.0D) ? 1 : 0);
          }
        });
    return arrayOfCameraCalibration;
  }
  
  public boolean hasCalibration(CameraCalibrationIdentity paramCameraCalibrationIdentity, Size paramSize) {
    return (internalGetCalibration(paramCameraCalibrationIdentity, paramSize) != null);
  }
  
  protected CameraCalibration internalGetCalibration(CameraCalibrationIdentity paramCameraCalibrationIdentity, Size paramSize) {
    CameraCalibration cameraCalibration1 = null;
    CameraCalibration cameraCalibration2 = null;
    if (paramCameraCalibrationIdentity != null && !paramCameraCalibrationIdentity.isDegenerate()) {
      List<CameraCalibration> list = this.calibrationMap.get(paramCameraCalibrationIdentity);
      if (list != null) {
        CameraCalibration cameraCalibration;
        int i = 0;
        while (true) {
          cameraCalibration = cameraCalibration2;
          if (i < list.size()) {
            cameraCalibration = list.get(i);
            if (cameraCalibration.size.equals(paramSize))
              break; 
            i++;
            continue;
          } 
          break;
        } 
        cameraCalibration1 = cameraCalibration;
        if (cameraCalibration == null) {
          CameraCalibration[] arrayOfCameraCalibration = getCameraCalibrationsWithAspectRatio(paramCameraCalibrationIdentity, paramSize);
          cameraCalibration1 = cameraCalibration;
          if (arrayOfCameraCalibration.length > 0)
            cameraCalibration1 = arrayOfCameraCalibration[0].scaledTo(paramSize); 
        } 
      } 
    } else {
      this.tracer.trace("getCameraCalibration(size=%s): lacking identity: no calibrations", new Object[] { paramCameraCalibrationIdentity, paramSize });
    } 
    if (cameraCalibration1 != null) {
      this.tracer.trace("%s", new Object[] { cameraCalibration1 });
      return cameraCalibration1;
    } 
    this.tracer.trace("CameraCalibration(%s %dx%d)=null", new Object[] { paramCameraCalibrationIdentity, Integer.valueOf(paramSize.getWidth()), Integer.valueOf(paramSize.getHeight()) });
    return cameraCalibration1;
  }
  
  protected boolean parseBoolean(String paramString) {
    return Boolean.parseBoolean(paramString);
  }
  
  protected CameraCalibration parseCalibrationElement(int paramInt1, int paramInt2) throws IOException, XmlPullParserException {
    boolean bool;
    if (this.parser.getEventType() == 2) {
      bool = true;
    } else {
      bool = false;
    } 
    Assert.assertTrue(bool);
    try {
      String str1 = getAttributeValue(new String[] { "size" });
      String str2 = getAttributeValue(new String[] { "focalLength", "focal_length" });
      String str3 = getAttributeValue(new String[] { "principalPoint", "principal_point" });
      String str4 = getAttributeValue(new String[] { "distortionCoefficients", "distortion_coefficients" });
      String str5 = getAttributeValue(new String[] { "remove" });
      parseIgnoreElementChildren();
      return new CameraCalibration(new VendorProductCalibrationIdentity(paramInt1, paramInt2), parseIntArray(2, str1), parseFloatArray(2, str2), parseFloatArray(2, str3), parseFloatArray(8, str4), parseBoolean(str5), false);
    } catch (RuntimeException runtimeException) {
      return null;
    } 
  }
  
  protected void parseCalibrations() throws IOException, XmlPullParserException {
    boolean bool;
    if (this.parser.getEventType() == 2) {
      bool = true;
    } else {
      bool = false;
    } 
    Assert.assertTrue(bool);
    for (int i = this.parser.next(); i != 3; i = this.parser.next()) {
      if (i == 2) {
        String str = this.parser.getName();
        i = -1;
        int j = str.hashCode();
        if (j != -1212321285) {
          if (j == 2011082565 && str.equals("Camera"))
            i = 1; 
        } else if (str.equals("CameraDevice")) {
          i = 0;
        } 
        if (i != 0 && i != 1) {
          parseIgnoreElementChildren();
        } else {
          parseCameraDeviceElement();
        } 
      } 
    } 
  }
  
  protected void parseCameraDeviceElement() throws IOException, XmlPullParserException {
    // Byte code:
    //   0: aload_0
    //   1: getfield parser : Lorg/xmlpull/v1/XmlPullParser;
    //   4: invokeinterface getEventType : ()I
    //   9: iconst_2
    //   10: if_icmpne -> 19
    //   13: iconst_1
    //   14: istore #4
    //   16: goto -> 22
    //   19: iconst_0
    //   20: istore #4
    //   22: iload #4
    //   24: invokestatic assertTrue : (Z)V
    //   27: aload_0
    //   28: iconst_3
    //   29: anewarray java/lang/String
    //   32: dup
    //   33: iconst_0
    //   34: ldc_w 'VID'
    //   37: aastore
    //   38: dup
    //   39: iconst_1
    //   40: ldc_w 'vid'
    //   43: aastore
    //   44: dup
    //   45: iconst_2
    //   46: ldc_w 'Vid'
    //   49: aastore
    //   50: invokevirtual getAttributeValue : ([Ljava/lang/String;)Ljava/lang/String;
    //   53: astore #5
    //   55: aload_0
    //   56: iconst_3
    //   57: anewarray java/lang/String
    //   60: dup
    //   61: iconst_0
    //   62: ldc_w 'PID'
    //   65: aastore
    //   66: dup
    //   67: iconst_1
    //   68: ldc_w 'pid'
    //   71: aastore
    //   72: dup
    //   73: iconst_2
    //   74: ldc_w 'Pid'
    //   77: aastore
    //   78: invokevirtual getAttributeValue : ([Ljava/lang/String;)Ljava/lang/String;
    //   81: astore #6
    //   83: aload_0
    //   84: aload #5
    //   86: invokevirtual decodeVid : (Ljava/lang/String;)I
    //   89: istore_2
    //   90: iload_2
    //   91: ifeq -> 135
    //   94: aload_0
    //   95: aload #6
    //   97: invokevirtual decodePid : (Ljava/lang/String;)I
    //   100: istore_1
    //   101: iload_1
    //   102: ifeq -> 119
    //   105: new org/firstinspires/ftc/robotcore/internal/camera/calibration/VendorProductCalibrationIdentity
    //   108: dup
    //   109: iload_2
    //   110: iload_1
    //   111: invokespecial <init> : (II)V
    //   114: astore #5
    //   116: goto -> 153
    //   119: new java/lang/IllegalArgumentException
    //   122: dup
    //   123: ldc_w 'pid is zero'
    //   126: invokespecial <init> : (Ljava/lang/String;)V
    //   129: athrow
    //   130: iconst_0
    //   131: istore_1
    //   132: goto -> 150
    //   135: new java/lang/IllegalArgumentException
    //   138: dup
    //   139: ldc_w 'vid is zero'
    //   142: invokespecial <init> : (Ljava/lang/String;)V
    //   145: athrow
    //   146: iconst_0
    //   147: istore_2
    //   148: iload_2
    //   149: istore_1
    //   150: aconst_null
    //   151: astore #5
    //   153: aload_0
    //   154: getfield parser : Lorg/xmlpull/v1/XmlPullParser;
    //   157: invokeinterface next : ()I
    //   162: istore_3
    //   163: iload_3
    //   164: iconst_3
    //   165: if_icmpeq -> 263
    //   168: aload #5
    //   170: ifnull -> 250
    //   173: iload_3
    //   174: iconst_2
    //   175: if_icmpne -> 250
    //   178: aload_0
    //   179: getfield parser : Lorg/xmlpull/v1/XmlPullParser;
    //   182: invokeinterface getName : ()Ljava/lang/String;
    //   187: astore #6
    //   189: iconst_m1
    //   190: istore_3
    //   191: aload #6
    //   193: invokevirtual hashCode : ()I
    //   196: ldc_w -1205780022
    //   199: if_icmpeq -> 205
    //   202: goto -> 218
    //   205: aload #6
    //   207: ldc_w 'Calibration'
    //   210: invokevirtual equals : (Ljava/lang/Object;)Z
    //   213: ifeq -> 218
    //   216: iconst_0
    //   217: istore_3
    //   218: iload_3
    //   219: ifeq -> 229
    //   222: aload_0
    //   223: invokevirtual parseIgnoreElementChildren : ()V
    //   226: goto -> 250
    //   229: aload_0
    //   230: iload_2
    //   231: iload_1
    //   232: invokevirtual parseCalibrationElement : (II)Lorg/firstinspires/ftc/robotcore/internal/camera/calibration/CameraCalibration;
    //   235: astore #6
    //   237: aload #6
    //   239: ifnull -> 250
    //   242: aload_0
    //   243: aload #5
    //   245: aload #6
    //   247: invokevirtual addCalibration : (Lorg/firstinspires/ftc/robotcore/internal/camera/calibration/CameraCalibrationIdentity;Lorg/firstinspires/ftc/robotcore/internal/camera/calibration/CameraCalibration;)V
    //   250: aload_0
    //   251: getfield parser : Lorg/xmlpull/v1/XmlPullParser;
    //   254: invokeinterface next : ()I
    //   259: istore_3
    //   260: goto -> 163
    //   263: return
    //   264: astore #5
    //   266: goto -> 146
    //   269: astore #5
    //   271: goto -> 130
    //   274: astore #5
    //   276: goto -> 150
    // Exception table:
    //   from	to	target	type
    //   83	90	264	java/lang/RuntimeException
    //   94	101	269	java/lang/RuntimeException
    //   105	116	274	java/lang/RuntimeException
    //   119	130	274	java/lang/RuntimeException
    //   135	146	269	java/lang/RuntimeException
  }
  
  protected void parseIgnoreElementChildren() throws IOException, XmlPullParserException {
    boolean bool;
    if (this.parser.getEventType() == 2) {
      bool = true;
    } else {
      bool = false;
    } 
    Assert.assertTrue(bool);
    for (int i = this.parser.next(); i != 3; i = this.parser.next()) {
      if (i == 2)
        parseIgnoreElementChildren(); 
    } 
  }
  
  protected int[] parseIntArray(int paramInt, String paramString) throws NumberFormatException, IllegalArgumentException {
    if (paramString == null)
      return new int[paramInt]; 
    String[] arrayOfString = paramString.replace(',', ' ').split("\\s+");
    int k = arrayOfString.length;
    int[] arrayOfInt = new int[k];
    int m = arrayOfString.length;
    int j = 0;
    for (int i = j; j < m; i++) {
      arrayOfInt[i] = Integer.decode(arrayOfString[j]).intValue();
      j++;
    } 
    if (k == paramInt)
      return arrayOfInt; 
    throw Misc.illegalArgumentException("xml element expected to contain %d integers, contains %s", new Object[] { Integer.valueOf(paramInt), Integer.valueOf(k) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\calibration\CameraCalibrationManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
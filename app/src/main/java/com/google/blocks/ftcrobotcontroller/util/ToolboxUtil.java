package com.google.blocks.ftcrobotcontroller.util;

import com.google.blocks.ftcrobotcontroller.hardware.HardwareType;
import java.util.Map;

public class ToolboxUtil {
  public static void addDualPropertySetters(StringBuilder paramStringBuilder, HardwareType paramHardwareType, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6) {
    paramStringBuilder.append("<block type=\"");
    paramStringBuilder.append(paramHardwareType.blockTypePrefix);
    paramStringBuilder.append("_setDualProperty_");
    paramStringBuilder.append(paramString2);
    paramStringBuilder.append("\">\n");
    paramStringBuilder.append("<field name=\"PROP\">");
    paramStringBuilder.append(paramString1);
    paramStringBuilder.append("</field>\n");
    paramStringBuilder.append("<field name=\"IDENTIFIER1\">");
    paramStringBuilder.append(paramString3);
    paramStringBuilder.append("</field>\n");
    paramStringBuilder.append("<field name=\"IDENTIFIER2\">");
    paramStringBuilder.append(paramString5);
    paramStringBuilder.append("</field>\n");
    paramStringBuilder.append("<value name=\"VALUE1\">\n");
    paramStringBuilder.append(paramString4);
    paramStringBuilder.append("</value>\n");
    paramStringBuilder.append("<value name=\"VALUE2\">\n");
    paramStringBuilder.append(paramString6);
    paramStringBuilder.append("</value>\n");
    paramStringBuilder.append("</block>\n");
  }
  
  public static void addFunctions(StringBuilder paramStringBuilder, HardwareType paramHardwareType, String paramString, Map<String, Map<String, String>> paramMap) {
    for (Map.Entry<String, Map<String, String>> entry : paramMap.entrySet()) {
      String str = (String)entry.getKey();
      Map map = (Map)entry.getValue();
      paramStringBuilder.append("<block type=\"");
      paramStringBuilder.append(paramHardwareType.blockTypePrefix);
      paramStringBuilder.append("_");
      paramStringBuilder.append(str);
      paramStringBuilder.append("\">\n");
      paramStringBuilder.append("<field name=\"IDENTIFIER\">");
      paramStringBuilder.append(paramString);
      paramStringBuilder.append("</field>\n");
      if (map != null)
        for (Map.Entry entry1 : map.entrySet()) {
          String str1 = (String)entry1.getKey();
          String str2 = (String)entry1.getValue();
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("<value name=\"");
          stringBuilder.append(str1);
          stringBuilder.append("\">\n");
          paramStringBuilder.append(stringBuilder.toString());
          paramStringBuilder.append(str2);
          paramStringBuilder.append("</value>\n");
        }  
      paramStringBuilder.append("</block>\n");
    } 
  }
  
  public static void addProperties(StringBuilder paramStringBuilder, HardwareType paramHardwareType, String paramString, Map<String, String> paramMap, Map<String, String[]> paramMap1, Map<String, String[]> paramMap2) {
    for (Map.Entry<String, String> entry : paramMap.entrySet()) {
      String str1 = (String)entry.getKey();
      String str2 = (String)entry.getValue();
      byte b = 0;
      if (paramMap1 != null && paramMap1.containsKey(str1)) {
        String[] arrayOfString = paramMap1.get(str1);
        int j = arrayOfString.length;
        int i;
        for (i = 0; i < j; i++)
          addPropertySetter(paramStringBuilder, paramHardwareType, paramString, str1, str2, arrayOfString[i]); 
      } 
      addPropertyGetter(paramStringBuilder, paramHardwareType, paramString, str1, str2);
      if (paramMap2 != null && paramMap2.containsKey(str1)) {
        String[] arrayOfString = paramMap2.get(str1);
        int j = arrayOfString.length;
        int i;
        for (i = b; i < j; i++)
          paramStringBuilder.append(arrayOfString[i]); 
      } 
    } 
  }
  
  private static void addPropertyGetter(StringBuilder paramStringBuilder, HardwareType paramHardwareType, String paramString1, String paramString2, String paramString3) {
    paramStringBuilder.append("<block type=\"");
    paramStringBuilder.append(paramHardwareType.blockTypePrefix);
    paramStringBuilder.append("_getProperty_");
    paramStringBuilder.append(paramString3);
    paramStringBuilder.append("\">\n");
    paramStringBuilder.append("<field name=\"IDENTIFIER\">");
    paramStringBuilder.append(paramString1);
    paramStringBuilder.append("</field>\n");
    paramStringBuilder.append("<field name=\"PROP\">");
    paramStringBuilder.append(paramString2);
    paramStringBuilder.append("</field>\n");
    paramStringBuilder.append("</block>\n");
  }
  
  private static void addPropertySetter(StringBuilder paramStringBuilder, HardwareType paramHardwareType, String paramString1, String paramString2, String paramString3, String paramString4) {
    paramStringBuilder.append("<block type=\"");
    paramStringBuilder.append(paramHardwareType.blockTypePrefix);
    paramStringBuilder.append("_setProperty_");
    paramStringBuilder.append(paramString3);
    paramStringBuilder.append("\">\n");
    paramStringBuilder.append("<field name=\"IDENTIFIER\">");
    paramStringBuilder.append(paramString1);
    paramStringBuilder.append("</field>\n");
    paramStringBuilder.append("<field name=\"PROP\">");
    paramStringBuilder.append(paramString2);
    paramStringBuilder.append("</field>\n");
    paramStringBuilder.append("<value name=\"VALUE\">\n");
    paramStringBuilder.append(paramString4);
    paramStringBuilder.append("</value>\n");
    paramStringBuilder.append("</block>\n");
  }
  
  public static String makeBooleanShadow(boolean paramBoolean) {
    String str;
    if (paramBoolean) {
      str = "TRUE";
    } else {
      str = "FALSE";
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("<shadow type=\"logic_boolean\"><field name=\"BOOL\">");
    stringBuilder.append(str);
    stringBuilder.append("</field></shadow>\n");
    return stringBuilder.toString();
  }
  
  public static String makeNumberShadow(double paramDouble) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("<shadow type=\"math_number\"><field name=\"NUM\">");
    stringBuilder.append(paramDouble);
    stringBuilder.append("</field></shadow>\n");
    return stringBuilder.toString();
  }
  
  public static String makeNumberShadow(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("<shadow type=\"math_number\"><field name=\"NUM\">");
    stringBuilder.append(paramInt);
    stringBuilder.append("</field></shadow>\n");
    return stringBuilder.toString();
  }
  
  public static String makeTextShadow(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("<shadow type=\"text\"><field name=\"TEXT\">");
    stringBuilder.append(paramString);
    stringBuilder.append("</field></shadow>\n");
    return stringBuilder.toString();
  }
  
  public static String makeTypedEnumBlock(HardwareType paramHardwareType, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("<block type=\"");
    stringBuilder.append(paramHardwareType.blockTypePrefix);
    stringBuilder.append("_typedEnum_");
    stringBuilder.append(paramString);
    stringBuilder.append("\">\n</block>\n");
    return stringBuilder.toString();
  }
  
  static String makeTypedEnumBlock(HardwareType paramHardwareType, String paramString1, String paramString2, String paramString3) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("<block type=\"");
    stringBuilder.append(paramHardwareType.blockTypePrefix);
    stringBuilder.append("_typedEnum_");
    stringBuilder.append(paramString1);
    stringBuilder.append("\">\n<field name=\"");
    stringBuilder.append(paramString2);
    stringBuilder.append("\">");
    stringBuilder.append(paramString3);
    stringBuilder.append("</field></block>\n");
    return stringBuilder.toString();
  }
  
  public static String makeTypedEnumShadow(HardwareType paramHardwareType, String paramString) {
    return makeTypedEnumShadow(paramHardwareType.blockTypePrefix, paramString);
  }
  
  public static String makeTypedEnumShadow(HardwareType paramHardwareType, String paramString1, String paramString2, String paramString3) {
    return makeTypedEnumShadow(paramHardwareType.blockTypePrefix, paramString1, paramString2, paramString3);
  }
  
  public static String makeTypedEnumShadow(String paramString1, String paramString2) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("<shadow type=\"");
    stringBuilder.append(paramString1);
    stringBuilder.append("_typedEnum_");
    stringBuilder.append(paramString2);
    stringBuilder.append("\">\n</shadow>\n");
    return stringBuilder.toString();
  }
  
  static String makeTypedEnumShadow(String paramString1, String paramString2, String paramString3, String paramString4) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("<shadow type=\"");
    stringBuilder.append(paramString1);
    stringBuilder.append("_typedEnum_");
    stringBuilder.append(paramString2);
    stringBuilder.append("\">\n<field name=\"");
    stringBuilder.append(paramString3);
    stringBuilder.append("\">");
    stringBuilder.append(paramString4);
    stringBuilder.append("</field></shadow>\n");
    return stringBuilder.toString();
  }
  
  public static String makeVariableGetBlock(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("<block type=\"variables_get\"><field name=\"VAR\">{");
    stringBuilder.append(paramString);
    stringBuilder.append("Variable}</field></block>\n");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontrolle\\util\ToolboxUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.qualcomm.ftccommon.configuration;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.opmode.ClassFilter;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.xmlpull.v1.XmlPullParserException;

public class RobotConfigResFilter implements ClassFilter {
  public static final String robotConfigRootTag = "Robot";
  
  public static final String robotConfigRootTypeAttribute = "type";
  
  protected Resources resources;
  
  protected String typeAttributeValue;
  
  protected ArrayList<Integer> xmlIdCollection;
  
  public RobotConfigResFilter(Context paramContext, String paramString) {
    this.typeAttributeValue = paramString;
    this.resources = paramContext.getResources();
    this.xmlIdCollection = new ArrayList<Integer>();
    clear();
  }
  
  public RobotConfigResFilter(String paramString) {
    this((Context)AppUtil.getInstance().getApplication(), paramString);
  }
  
  public static String getRootAttribute(XmlResourceParser paramXmlResourceParser, String paramString1, String paramString2, String paramString3) {
    try {
      while (paramXmlResourceParser.getEventType() != 1) {
        String str;
        if (paramXmlResourceParser.getEventType() == 2) {
          if (!paramXmlResourceParser.getName().equals(paramString1))
            return null; 
          str = paramXmlResourceParser.getAttributeValue(null, paramString2);
          if (str != null)
            return str; 
        } else {
          str.next();
          continue;
        } 
        return paramString3;
      } 
    } catch (XmlPullParserException xmlPullParserException) {
      xmlPullParserException.printStackTrace();
    } catch (IOException iOException) {}
    return null;
  }
  
  private boolean isRobotConfiguration(XmlResourceParser paramXmlResourceParser) {
    return this.typeAttributeValue.equals(getRootAttribute(paramXmlResourceParser, "Robot", "type", null));
  }
  
  protected void clear() {
    this.xmlIdCollection.clear();
  }
  
  public void filterAllClassesComplete() {}
  
  public void filterAllClassesStart() {
    clear();
  }
  
  public void filterClass(Class paramClass) {
    if (paramClass.getName().endsWith("R$xml"))
      for (Field field : paramClass.getFields()) {
        try {
          if (field.getType().equals(int.class)) {
            int i = field.getInt(paramClass);
            if (isRobotConfiguration(this.resources.getXml(i)))
              this.xmlIdCollection.add(Integer.valueOf(i)); 
          } 
        } catch (IllegalAccessException illegalAccessException) {
          illegalAccessException.printStackTrace();
        } 
      }  
  }
  
  public void filterOnBotJavaClass(Class paramClass) {
    filterClass(paramClass);
  }
  
  public void filterOnBotJavaClassesComplete() {
    filterAllClassesComplete();
  }
  
  public void filterOnBotJavaClassesStart() {}
  
  public List<Integer> getXmlIds() {
    return this.xmlIdCollection;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\RobotConfigResFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
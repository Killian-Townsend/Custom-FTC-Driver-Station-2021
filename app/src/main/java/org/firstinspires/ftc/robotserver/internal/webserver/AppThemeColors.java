package org.firstinspires.ftc.robotserver.internal.webserver;

import android.content.res.TypedArray;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.webserver.R;

public class AppThemeColors {
  static {
    ArrayList<Field> arrayList1 = new ArrayList();
    ArrayList<Integer> arrayList = new ArrayList();
    Field[] arrayOfField = AppThemeColors.class.getDeclaredFields();
    int k = arrayOfField.length;
    byte b = 0;
    for (int j = 0; j < k; j++) {
      Field field = arrayOfField[j];
      if ((field.getModifiers() & 0x8) == 0) {
        String str = field.getName();
        byte b1 = -1;
        int m = str.hashCode();
        if (m != -1061629733) {
          if (m != -1003786783) {
            if (m == 284509615 && str.equals("textWarning"))
              b1 = 1; 
          } else if (str.equals("textOkay")) {
            b1 = 2;
          } 
        } else if (str.equals("textError")) {
          b1 = 0;
        } 
        if (b1 != 0 && b1 != 1 && b1 != 2) {
          arrayList1.add(field);
          try {
            arrayList.add(Integer.valueOf(R.attr.class.getDeclaredField(str).getInt(null)));
          } catch (NoSuchFieldException noSuchFieldException) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("unable to access r.attr.");
            stringBuilder.append(str);
            throw new RuntimeException(stringBuilder.toString(), noSuchFieldException);
          } catch (IllegalAccessException illegalAccessException) {}
        } 
      } 
    } 
    colorFieldsArray = (Field[])stringBuilder.toArray((Object[])new Field[stringBuilder.size()]);
    colorAttrArray = new int[illegalAccessException.size()];
    int i = b;
    while (true) {
      int[] arrayOfInt = colorAttrArray;
      if (i < arrayOfInt.length) {
        arrayOfInt[i] = ((Integer)illegalAccessException.get(i)).intValue();
        i++;
        continue;
      } 
      break;
    } 
  }
  
  public static String fromHeader(String paramString) {
    String str = paramString;
    if (paramString.length() > 1) {
      str = paramString;
      if (paramString.startsWith("\"")) {
        str = paramString;
        if (paramString.endsWith("\""))
          str = paramString.substring(1, paramString.length() - 1).replace("\\\"", "\""); 
      } 
    } 
    return str;
  }
  
  public static AppThemeColors fromJson(String paramString) {
    return (AppThemeColors)SimpleGson.getInstance().fromJson(paramString, AppThemeColors.class);
  }
  
  public static AppThemeColors fromTheme() {
    AppThemeColors appThemeColors = new AppThemeColors();
    appThemeColors.initializeFromTheme();
    return appThemeColors;
  }
  
  public static String toHeader(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("\"");
    stringBuilder.append(paramString.replace("\"", "\\\""));
    stringBuilder.append("\"");
    return stringBuilder.toString();
  }
  
  protected void addColor(StringBuilder paramStringBuilder, String paramString, int paramInt) {
    paramStringBuilder.append(String.format("@%s:#%06x;", new Object[] { paramString, Integer.valueOf(paramInt & 0xFFFFFF) }));
  }
  
  protected void initializeFromTheme() {
    AppUtil.getInstance();
    this.textError = AppUtil.getColor(R.color.text_error);
    AppUtil.getInstance();
    this.textWarning = AppUtil.getColor(R.color.text_warning);
    AppUtil.getInstance();
    this.textOkay = AppUtil.getColor(R.color.text_okay);
    TypedArray typedArray = AppUtil.getInstance().getRootActivity().obtainStyledAttributes(colorAttrArray);
    int i = 0;
    try {
      while (i < colorAttrArray.length) {
        int j = typedArray.getColor(i, 0);
        try {
          colorFieldsArray[i].setInt(this, j);
          i++;
        } catch (IllegalAccessException illegalAccessException) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("unable to access field");
          stringBuilder.append(colorFieldsArray[i].getName());
          throw new RuntimeException(stringBuilder.toString(), illegalAccessException);
        } 
      } 
      return;
    } finally {
      typedArray.recycle();
    } 
  }
  
  public String toJson() {
    return SimpleGson.getInstance().toJson(this);
  }
  
  public String toLess() {
    StringBuilder stringBuilder = new StringBuilder();
    addColor(stringBuilder, "textError", this.textError);
    addColor(stringBuilder, "textWarning", this.textWarning);
    addColor(stringBuilder, "textOkay", this.textOkay);
    int i = 0;
    while (true) {
      Field[] arrayOfField = colorFieldsArray;
      if (i < arrayOfField.length) {
        Field field = arrayOfField[i];
        try {
          addColor(stringBuilder, field.getName(), field.getInt(this));
          i++;
        } catch (IllegalAccessException illegalAccessException) {
          StringBuilder stringBuilder1 = new StringBuilder();
          stringBuilder1.append("unable to access field");
          stringBuilder1.append(colorFieldsArray[i].getName());
          throw new RuntimeException(stringBuilder1.toString(), illegalAccessException);
        } 
        continue;
      } 
      return illegalAccessException.toString();
    } 
  }
  
  static {
    StringBuilder stringBuilder;
  }
  
  public static final String TAG = AppThemeColors.class.getSimpleName();
  
  protected static int[] colorAttrArray;
  
  protected static Field[] colorFieldsArray;
  
  private static final String escapedQuote = "\\\"";
  
  public static final String htppHeaderNameLower = "Ftc-RCConsole-Theme".toLowerCase();
  
  public static final String httpHeaderName = "Ftc-RCConsole-Theme";
  
  private static final String oneQuote = "\"";
  
  public int backgroundAlmostDark;
  
  public int backgroundDark;
  
  public int backgroundLight;
  
  public int backgroundMedium;
  
  public int backgroundMediumDark;
  
  public int backgroundMediumLight;
  
  public int backgroundMediumMedium;
  
  public int backgroundVeryDark;
  
  public int backgroundVeryVeryDark;
  
  public int feedbackBackground;
  
  public int feedbackBorder;
  
  public int lineBright;
  
  public int lineLight;
  
  public int textBright;
  
  public int textError;
  
  public int textLight;
  
  public int textMedium;
  
  public int textMediumDark;
  
  public int textOkay;
  
  public int textVeryDark;
  
  public int textVeryVeryDark;
  
  public int textWarning;
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\webserver\AppThemeColors.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
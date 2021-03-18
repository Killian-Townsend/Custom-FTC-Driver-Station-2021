package org.firstinspires.ftc.robotcore.external;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JavaUtil {
  public static int ahsvToColor(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3) {
    return Color.HSVToColor(paramInt, new float[] { paramFloat1, paramFloat2, paramFloat3 });
  }
  
  public static double averageOfList(List paramList) {
    double d1 = 0.0D;
    double d2 = d1;
    if (paramList != null) {
      d2 = d1;
      if (!paramList.isEmpty()) {
        for (Number number : paramList) {
          if (number instanceof Number)
            d1 += ((Number)number).doubleValue(); 
        } 
        d2 = d1 / paramList.size();
      } 
    } 
    return d2;
  }
  
  private static float[] colorToHSV(int paramInt) {
    float[] arrayOfFloat = new float[3];
    Color.colorToHSV(paramInt, arrayOfFloat);
    return arrayOfFloat;
  }
  
  public static float colorToHue(int paramInt) {
    return colorToHSV(paramInt)[0];
  }
  
  public static float colorToSaturation(int paramInt) {
    return colorToHSV(paramInt)[1];
  }
  
  public static String colorToText(int paramInt) {
    String str2 = String.format("%08X", new Object[] { Integer.valueOf(paramInt) });
    String str1 = str2;
    if (str2.startsWith("FF"))
      str1 = str2.substring(2); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("#");
    stringBuilder.append(str1);
    return stringBuilder.toString();
  }
  
  public static float colorToValue(int paramInt) {
    return colorToHSV(paramInt)[2];
  }
  
  public static List createListWith(Object... paramVarArgs) {
    ArrayList<? super Object> arrayList = new ArrayList();
    Collections.addAll(arrayList, paramVarArgs);
    return arrayList;
  }
  
  public static List createListWithItemRepeated(Object paramObject, int paramInt) {
    ArrayList<Object> arrayList = new ArrayList();
    for (int i = 0; i < paramInt; i++)
      arrayList.add(paramObject); 
    return arrayList;
  }
  
  public static String formatNumber(double paramDouble, int paramInt) {
    int i = Math.max(0, paramInt);
    double d = Math.pow(10.0D, (-i - 1));
    if (i == 0) {
      paramInt = 1;
    } else {
      paramInt = i;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("%");
    stringBuilder.append(paramInt);
    stringBuilder.append(".");
    stringBuilder.append(i);
    stringBuilder.append("f");
    return String.format(stringBuilder.toString(), new Object[] { Double.valueOf(paramDouble + d) });
  }
  
  private static int getIndex(String paramString, AtMode paramAtMode, int paramInt) {
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$JavaUtil$AtMode[paramAtMode.ordinal()];
    if (i != 1) {
      StringBuilder stringBuilder;
      if (i != 2) {
        if (i != 3) {
          if (i != 4) {
            if (i == 5)
              return (int)Math.floor(Math.random() * paramString.length()); 
            stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown AtMode ");
            stringBuilder.append(paramAtMode);
            throw new IllegalArgumentException(stringBuilder.toString());
          } 
          return stringBuilder.length() - 1 - paramInt;
        } 
        return paramInt;
      } 
      return stringBuilder.length() - 1;
    } 
    return 0;
  }
  
  private static int getIndex(List paramList, AtMode paramAtMode, int paramInt) {
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$JavaUtil$AtMode[paramAtMode.ordinal()];
    if (i != 1) {
      StringBuilder stringBuilder;
      if (i != 2) {
        if (i != 3) {
          if (i != 4) {
            if (i == 5)
              return (int)Math.floor(Math.random() * paramList.size()); 
            stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown AtMode ");
            stringBuilder.append(paramAtMode);
            throw new IllegalArgumentException(stringBuilder.toString());
          } 
          return stringBuilder.size() - 1 - paramInt;
        } 
        return paramInt;
      } 
      return stringBuilder.size() - 1;
    } 
    return 0;
  }
  
  public static int hsvToColor(float paramFloat1, float paramFloat2, float paramFloat3) {
    return Color.HSVToColor(new float[] { paramFloat1, paramFloat2, paramFloat3 });
  }
  
  public static Object inListGet(List paramList, AtMode paramAtMode, int paramInt, boolean paramBoolean) {
    if (paramList == null)
      return null; 
    paramInt = getIndex(paramList, paramAtMode, paramInt);
    return paramBoolean ? paramList.remove(paramInt) : paramList.get(paramInt);
  }
  
  public static List inListGetSublist(List<?> paramList, AtMode paramAtMode1, int paramInt1, AtMode paramAtMode2, int paramInt2) {
    return (paramList == null) ? null : new ArrayList(paramList.subList(getIndex(paramList, paramAtMode1, paramInt1), getIndex(paramList, paramAtMode2, paramInt2) + 1));
  }
  
  public static void inListSet(List<Object> paramList, AtMode paramAtMode, int paramInt, boolean paramBoolean, Object paramObject) {
    if (paramList == null)
      return; 
    paramInt = getIndex(paramList, paramAtMode, paramInt);
    if (paramBoolean) {
      paramList.add(paramInt, paramObject);
      return;
    } 
    paramList.set(paramInt, paramObject);
  }
  
  public static String inTextGetLetter(String paramString, AtMode paramAtMode, int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("");
    stringBuilder.append(paramString.charAt(getIndex(paramString, paramAtMode, paramInt)));
    return stringBuilder.toString();
  }
  
  public static String inTextGetSubstring(String paramString, AtMode paramAtMode1, int paramInt1, AtMode paramAtMode2, int paramInt2) {
    return paramString.substring(getIndex(paramString, paramAtMode1, paramInt1), getIndex(paramString, paramAtMode2, paramInt2) + 1);
  }
  
  public static boolean isPrime(double paramDouble) {
    if (!Double.isNaN(paramDouble) && paramDouble % 1.0D == 0.0D) {
      if (paramDouble < 2.0D)
        return false; 
      long l = (long)paramDouble;
      if (l != 2L) {
        if (l == 3L)
          return true; 
        if (l % 2L != 0L) {
          if (l % 3L == 0L)
            return false; 
          int i = 6;
          while (i <= Math.sqrt(l) + 1.0D) {
            if (l % (i - 1) != 0L) {
              if (l % (i + 1) == 0L)
                return false; 
              i += 6;
              continue;
            } 
            return false;
          } 
          return true;
        } 
        return false;
      } 
      return true;
    } 
    return false;
  }
  
  public static List makeListFromText(String paramString1, String paramString2) {
    ArrayList<String> arrayList = new ArrayList();
    if (paramString1 != null && paramString2 != null) {
      int j = paramString2.length();
      for (int i = 0; i < paramString1.length(); i = k + j) {
        int k = paramString1.indexOf(paramString2, i);
        arrayList.add(paramString1.substring(i, k));
      } 
    } 
    return arrayList;
  }
  
  public static String makeTextFromList(List paramList, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    if (paramList != null && paramString != null) {
      Iterator<Object> iterator = paramList.iterator();
      for (String str = ""; iterator.hasNext(); str = paramString) {
        Object object = iterator.next();
        stringBuilder.append(str);
        stringBuilder.append(object);
      } 
    } 
    return stringBuilder.toString();
  }
  
  public static double maxOfList(List paramList) {
    double d1 = -9.223372036854776E18D;
    double d2 = d1;
    if (paramList != null) {
      Iterator<Object> iterator = paramList.iterator();
      while (true) {
        d2 = d1;
        if (iterator.hasNext()) {
          Number number = (Number)iterator.next();
          if (number instanceof Number) {
            d2 = ((Number)number).doubleValue();
            if (d2 > d1)
              d1 = d2; 
          } 
          continue;
        } 
        break;
      } 
    } 
    return d2;
  }
  
  public static double medianOfList(List paramList) {
    if (paramList == null)
      return 0.0D; 
    ArrayList<Double> arrayList = new ArrayList();
    for (Number number : paramList) {
      if (number instanceof Number)
        arrayList.add(Double.valueOf(((Number)number).doubleValue())); 
    } 
    if (arrayList.isEmpty())
      return 0.0D; 
    Collections.sort(arrayList);
    int i = arrayList.size();
    if (i % 2 == 0) {
      i /= 2;
      return (((Double)arrayList.get(i - 1)).doubleValue() + ((Double)arrayList.get(i)).doubleValue()) / 2.0D;
    } 
    return ((Double)arrayList.get((i - 1) / 2)).doubleValue();
  }
  
  public static double minOfList(List paramList) {
    double d1 = 9.223372036854776E18D;
    double d2 = d1;
    if (paramList != null) {
      Iterator<Object> iterator = paramList.iterator();
      while (true) {
        d2 = d1;
        if (iterator.hasNext()) {
          Number number = (Number)iterator.next();
          if (number instanceof Number) {
            d2 = ((Number)number).doubleValue();
            if (d2 < d1)
              d1 = d2; 
          } 
          continue;
        } 
        break;
      } 
    } 
    return d2;
  }
  
  public static List modesOfList(List paramList) {
    ArrayList arrayList = new ArrayList();
    if (paramList != null && !paramList.isEmpty()) {
      HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
      int i = 0;
      for (Object object : paramList) {
        Integer integer = (Integer)hashMap.get(object);
        int j = 1;
        if (integer != null)
          j = 1 + integer.intValue(); 
        hashMap.put(object, Integer.valueOf(j));
        if (j > i)
          i = j; 
      } 
      for (Map.Entry<Object, Object> entry : hashMap.entrySet()) {
        if (((Integer)entry.getValue()).intValue() == i)
          arrayList.add(entry.getKey()); 
      } 
    } 
    return arrayList;
  }
  
  public static int randomInt(double paramDouble1, double paramDouble2) {
    double d;
    if (paramDouble1 > paramDouble2) {
      d = paramDouble2;
    } else {
      d = paramDouble1;
      paramDouble1 = paramDouble2;
    } 
    return (int)Math.floor(Math.random() * (paramDouble1 - d + 1.0D) + d);
  }
  
  public static Object randomItemOfList(List paramList) {
    return (paramList == null || paramList.isEmpty()) ? null : paramList.get((int)Math.floor(Math.random() * paramList.size()));
  }
  
  private static float[] rgbToHSV(int paramInt1, int paramInt2, int paramInt3) {
    float[] arrayOfFloat = new float[3];
    Color.RGBToHSV(paramInt1, paramInt2, paramInt3, arrayOfFloat);
    return arrayOfFloat;
  }
  
  public static float rgbToHue(int paramInt1, int paramInt2, int paramInt3) {
    return rgbToHSV(paramInt1, paramInt2, paramInt3)[0];
  }
  
  public static float rgbToSaturation(int paramInt1, int paramInt2, int paramInt3) {
    return rgbToHSV(paramInt1, paramInt2, paramInt3)[1];
  }
  
  public static float rgbToValue(int paramInt1, int paramInt2, int paramInt3) {
    return rgbToHSV(paramInt1, paramInt2, paramInt3)[2];
  }
  
  public static void showColor(Context paramContext, final int color) {
    if (paramContext instanceof Activity) {
      int i = paramContext.getResources().getIdentifier("RelativeLayout", "id", paramContext.getPackageName());
      final View relativeLayout = ((Activity)paramContext).findViewById(i);
      view.post(new Runnable() {
            public void run() {
              relativeLayout.setBackgroundColor(color);
            }
          });
    } 
  }
  
  public static List sort(List<?> paramList, SortType paramSortType, final SortDirection sortDirection) {
    Comparator<?> comparator;
    if (paramList == null)
      return null; 
    ArrayList<?> arrayList = new ArrayList(paramList);
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$JavaUtil$SortType[paramSortType.ordinal()];
    if (i != 2) {
      if (i != 3) {
        comparator = new Comparator() {
            public int compare(Object param1Object1, Object param1Object2) {
              // Byte code:
              //   0: aload_1
              //   1: instanceof java/lang/Number
              //   4: istore #9
              //   6: dconst_0
              //   7: dstore #7
              //   9: iload #9
              //   11: ifeq -> 25
              //   14: aload_1
              //   15: checkcast java/lang/Number
              //   18: invokevirtual doubleValue : ()D
              //   21: dstore_3
              //   22: goto -> 42
              //   25: aload_1
              //   26: ifnull -> 40
              //   29: aload_1
              //   30: invokevirtual toString : ()Ljava/lang/String;
              //   33: invokestatic parseDouble : (Ljava/lang/String;)D
              //   36: dstore_3
              //   37: goto -> 42
              //   40: dconst_0
              //   41: dstore_3
              //   42: aload_2
              //   43: instanceof java/lang/Number
              //   46: ifeq -> 61
              //   49: aload_2
              //   50: checkcast java/lang/Number
              //   53: invokevirtual doubleValue : ()D
              //   56: dstore #5
              //   58: goto -> 78
              //   61: dload #7
              //   63: dstore #5
              //   65: aload_2
              //   66: ifnull -> 78
              //   69: aload_2
              //   70: invokevirtual toString : ()Ljava/lang/String;
              //   73: invokestatic parseDouble : (Ljava/lang/String;)D
              //   76: dstore #5
              //   78: aload_0
              //   79: getfield val$sortDirection : Lorg/firstinspires/ftc/robotcore/external/JavaUtil$SortDirection;
              //   82: getstatic org/firstinspires/ftc/robotcore/external/JavaUtil$SortDirection.ASCENDING : Lorg/firstinspires/ftc/robotcore/external/JavaUtil$SortDirection;
              //   85: if_acmpne -> 99
              //   88: dload_3
              //   89: dload #5
              //   91: dsub
              //   92: invokestatic signum : (D)D
              //   95: dstore_3
              //   96: goto -> 107
              //   99: dload #5
              //   101: dload_3
              //   102: dsub
              //   103: invokestatic signum : (D)D
              //   106: dstore_3
              //   107: dload_3
              //   108: d2i
              //   109: ireturn
              //   110: astore_1
              //   111: goto -> 40
              //   114: astore_1
              //   115: dload #7
              //   117: dstore #5
              //   119: goto -> 78
              // Exception table:
              //   from	to	target	type
              //   29	37	110	java/lang/NumberFormatException
              //   69	78	114	java/lang/NumberFormatException
            }
          };
      } else {
        comparator = new Comparator() {
            public int compare(Object param1Object1, Object param1Object2) {
              param1Object1 = param1Object1.toString();
              param1Object2 = param1Object2.toString();
              return (sortDirection == JavaUtil.SortDirection.ASCENDING) ? param1Object1.compareToIgnoreCase((String)param1Object2) : param1Object2.compareToIgnoreCase((String)param1Object1);
            }
          };
      } 
    } else {
      comparator = new Comparator() {
          public int compare(Object param1Object1, Object param1Object2) {
            param1Object1 = param1Object1.toString();
            param1Object2 = param1Object2.toString();
            return (sortDirection == JavaUtil.SortDirection.ASCENDING) ? param1Object1.compareTo((String)param1Object2) : param1Object2.compareTo((String)param1Object1);
          }
        };
    } 
    Collections.sort(arrayList, comparator);
    return arrayList;
  }
  
  public static double standardDeviationOfList(List paramList) {
    double d1 = 0.0D;
    double d2 = d1;
    if (paramList != null) {
      d2 = d1;
      if (!paramList.isEmpty()) {
        d2 = averageOfList(paramList);
        for (Number number : paramList) {
          if (number instanceof Number)
            d1 += Math.pow(((Number)number).doubleValue() - d2, 2.0D); 
        } 
        d2 = d1 / paramList.size();
      } 
    } 
    return Math.sqrt(d2);
  }
  
  public static double sumOfList(List paramList) {
    double d1 = 0.0D;
    double d2 = d1;
    if (paramList != null) {
      Iterator<Object> iterator = paramList.iterator();
      while (true) {
        d2 = d1;
        if (iterator.hasNext()) {
          Number number = (Number)iterator.next();
          if (number instanceof Number)
            d1 += ((Number)number).doubleValue(); 
          continue;
        } 
        break;
      } 
    } 
    return d2;
  }
  
  public static String textTrim(String paramString, TrimMode paramTrimMode) {
    StringBuilder stringBuilder;
    int j = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$JavaUtil$TrimMode[paramTrimMode.ordinal()];
    int i = 0;
    if (j != 1) {
      if (j != 2) {
        if (j == 3)
          return paramString.trim(); 
        stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown TrimMode ");
        stringBuilder.append(paramTrimMode);
        throw new IllegalArgumentException(stringBuilder.toString());
      } 
      for (i = stringBuilder.length() - 1; i >= 0; i--) {
        if (stringBuilder.codePointAt(i) > 32)
          return stringBuilder.substring(0, i + 1); 
      } 
      return (String)stringBuilder;
    } 
    while (i < stringBuilder.length()) {
      if (stringBuilder.codePointAt(i) > 32)
        return stringBuilder.substring(i); 
      i++;
    } 
    return (String)stringBuilder;
  }
  
  public static String toTitleCase(String paramString) {
    String[] arrayOfString = paramString.split("((?<=\\s+)|(?=\\s+))");
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < arrayOfString.length; i++) {
      String str1;
      String str2 = arrayOfString[i];
      char c = str2.charAt(0);
      paramString = str2;
      if (!Character.isWhitespace(c)) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append(Character.toTitleCase(c));
        stringBuilder1.append(str2.substring(1).toLowerCase());
        str1 = stringBuilder1.toString();
      } 
      stringBuilder.append(str1);
    } 
    return stringBuilder.toString();
  }
  
  public enum AtMode {
    FIRST, FROM_END, FROM_START, LAST, RANDOM;
    
    static {
      FROM_END = new AtMode("FROM_END", 3);
      AtMode atMode = new AtMode("RANDOM", 4);
      RANDOM = atMode;
      $VALUES = new AtMode[] { FIRST, LAST, FROM_START, FROM_END, atMode };
    }
  }
  
  public enum SortDirection {
    ASCENDING, DESCENDING;
    
    static {
      SortDirection sortDirection = new SortDirection("DESCENDING", 1);
      DESCENDING = sortDirection;
      $VALUES = new SortDirection[] { ASCENDING, sortDirection };
    }
  }
  
  public enum SortType {
    IGNORE_CASE, NUMERIC, TEXT;
    
    static {
      SortType sortType = new SortType("IGNORE_CASE", 2);
      IGNORE_CASE = sortType;
      $VALUES = new SortType[] { NUMERIC, TEXT, sortType };
    }
  }
  
  public enum TrimMode {
    LEFT, BOTH, RIGHT;
    
    static {
      TrimMode trimMode = new TrimMode("BOTH", 2);
      BOTH = trimMode;
      $VALUES = new TrimMode[] { LEFT, RIGHT, trimMode };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\JavaUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
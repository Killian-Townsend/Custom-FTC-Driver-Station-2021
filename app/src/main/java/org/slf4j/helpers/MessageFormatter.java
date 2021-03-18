package org.slf4j.helpers;

import java.util.HashMap;
import java.util.Map;

public final class MessageFormatter {
  static final char DELIM_START = '{';
  
  static final char DELIM_STOP = '}';
  
  static final String DELIM_STR = "{}";
  
  private static final char ESCAPE_CHAR = '\\';
  
  public static final FormattingTuple arrayFormat(String paramString, Object[] paramArrayOfObject) {
    Throwable throwable = getThrowableCandidate(paramArrayOfObject);
    Object[] arrayOfObject = paramArrayOfObject;
    if (throwable != null)
      arrayOfObject = trimmedCopy(paramArrayOfObject); 
    return arrayFormat(paramString, arrayOfObject, throwable);
  }
  
  public static final FormattingTuple arrayFormat(String paramString, Object[] paramArrayOfObject, Throwable paramThrowable) {
    if (paramString == null)
      return new FormattingTuple(null, paramArrayOfObject, paramThrowable); 
    if (paramArrayOfObject == null)
      return new FormattingTuple(paramString); 
    StringBuilder stringBuilder = new StringBuilder(paramString.length() + 50);
    int i = 0;
    int j = 0;
    while (i < paramArrayOfObject.length) {
      int k = paramString.indexOf("{}", j);
      if (k == -1) {
        if (!j)
          return new FormattingTuple(paramString, paramArrayOfObject, paramThrowable); 
        stringBuilder.append(paramString, j, paramString.length());
        return new FormattingTuple(stringBuilder.toString(), paramArrayOfObject, paramThrowable);
      } 
      if (isEscapedDelimeter(paramString, k)) {
        if (!isDoubleEscaped(paramString, k)) {
          i--;
          stringBuilder.append(paramString, j, k - 1);
          stringBuilder.append('{');
          j = k + 1;
        } else {
          stringBuilder.append(paramString, j, k - 1);
          deeplyAppendParameter(stringBuilder, paramArrayOfObject[i], (Map)new HashMap<Object, Object>());
          j = k + 2;
        } 
      } else {
        stringBuilder.append(paramString, j, k);
        deeplyAppendParameter(stringBuilder, paramArrayOfObject[i], (Map)new HashMap<Object, Object>());
        j = k + 2;
      } 
      i++;
    } 
    stringBuilder.append(paramString, j, paramString.length());
    return new FormattingTuple(stringBuilder.toString(), paramArrayOfObject, paramThrowable);
  }
  
  private static void booleanArrayAppend(StringBuilder paramStringBuilder, boolean[] paramArrayOfboolean) {
    paramStringBuilder.append('[');
    int j = paramArrayOfboolean.length;
    for (int i = 0; i < j; i++) {
      paramStringBuilder.append(paramArrayOfboolean[i]);
      if (i != j - 1)
        paramStringBuilder.append(", "); 
    } 
    paramStringBuilder.append(']');
  }
  
  private static void byteArrayAppend(StringBuilder paramStringBuilder, byte[] paramArrayOfbyte) {
    paramStringBuilder.append('[');
    int j = paramArrayOfbyte.length;
    for (int i = 0; i < j; i++) {
      paramStringBuilder.append(paramArrayOfbyte[i]);
      if (i != j - 1)
        paramStringBuilder.append(", "); 
    } 
    paramStringBuilder.append(']');
  }
  
  private static void charArrayAppend(StringBuilder paramStringBuilder, char[] paramArrayOfchar) {
    paramStringBuilder.append('[');
    int j = paramArrayOfchar.length;
    for (int i = 0; i < j; i++) {
      paramStringBuilder.append(paramArrayOfchar[i]);
      if (i != j - 1)
        paramStringBuilder.append(", "); 
    } 
    paramStringBuilder.append(']');
  }
  
  private static void deeplyAppendParameter(StringBuilder paramStringBuilder, Object paramObject, Map<Object[], Object> paramMap) {
    if (paramObject == null) {
      paramStringBuilder.append("null");
      return;
    } 
    if (!paramObject.getClass().isArray()) {
      safeObjectAppend(paramStringBuilder, paramObject);
      return;
    } 
    if (paramObject instanceof boolean[]) {
      booleanArrayAppend(paramStringBuilder, (boolean[])paramObject);
      return;
    } 
    if (paramObject instanceof byte[]) {
      byteArrayAppend(paramStringBuilder, (byte[])paramObject);
      return;
    } 
    if (paramObject instanceof char[]) {
      charArrayAppend(paramStringBuilder, (char[])paramObject);
      return;
    } 
    if (paramObject instanceof short[]) {
      shortArrayAppend(paramStringBuilder, (short[])paramObject);
      return;
    } 
    if (paramObject instanceof int[]) {
      intArrayAppend(paramStringBuilder, (int[])paramObject);
      return;
    } 
    if (paramObject instanceof long[]) {
      longArrayAppend(paramStringBuilder, (long[])paramObject);
      return;
    } 
    if (paramObject instanceof float[]) {
      floatArrayAppend(paramStringBuilder, (float[])paramObject);
      return;
    } 
    if (paramObject instanceof double[]) {
      doubleArrayAppend(paramStringBuilder, (double[])paramObject);
      return;
    } 
    objectArrayAppend(paramStringBuilder, (Object[])paramObject, paramMap);
  }
  
  private static void doubleArrayAppend(StringBuilder paramStringBuilder, double[] paramArrayOfdouble) {
    paramStringBuilder.append('[');
    int j = paramArrayOfdouble.length;
    for (int i = 0; i < j; i++) {
      paramStringBuilder.append(paramArrayOfdouble[i]);
      if (i != j - 1)
        paramStringBuilder.append(", "); 
    } 
    paramStringBuilder.append(']');
  }
  
  private static void floatArrayAppend(StringBuilder paramStringBuilder, float[] paramArrayOffloat) {
    paramStringBuilder.append('[');
    int j = paramArrayOffloat.length;
    for (int i = 0; i < j; i++) {
      paramStringBuilder.append(paramArrayOffloat[i]);
      if (i != j - 1)
        paramStringBuilder.append(", "); 
    } 
    paramStringBuilder.append(']');
  }
  
  public static final FormattingTuple format(String paramString, Object paramObject) {
    return arrayFormat(paramString, new Object[] { paramObject });
  }
  
  public static final FormattingTuple format(String paramString, Object paramObject1, Object paramObject2) {
    return arrayFormat(paramString, new Object[] { paramObject1, paramObject2 });
  }
  
  static final Throwable getThrowableCandidate(Object[] paramArrayOfObject) {
    if (paramArrayOfObject != null) {
      if (paramArrayOfObject.length == 0)
        return null; 
      Object object = paramArrayOfObject[paramArrayOfObject.length - 1];
      if (object instanceof Throwable)
        return (Throwable)object; 
    } 
    return null;
  }
  
  private static void intArrayAppend(StringBuilder paramStringBuilder, int[] paramArrayOfint) {
    paramStringBuilder.append('[');
    int j = paramArrayOfint.length;
    for (int i = 0; i < j; i++) {
      paramStringBuilder.append(paramArrayOfint[i]);
      if (i != j - 1)
        paramStringBuilder.append(", "); 
    } 
    paramStringBuilder.append(']');
  }
  
  static final boolean isDoubleEscaped(String paramString, int paramInt) {
    return (paramInt >= 2 && paramString.charAt(paramInt - 2) == '\\');
  }
  
  static final boolean isEscapedDelimeter(String paramString, int paramInt) {
    return (paramInt == 0) ? false : ((paramString.charAt(paramInt - 1) == '\\'));
  }
  
  private static void longArrayAppend(StringBuilder paramStringBuilder, long[] paramArrayOflong) {
    paramStringBuilder.append('[');
    int j = paramArrayOflong.length;
    for (int i = 0; i < j; i++) {
      paramStringBuilder.append(paramArrayOflong[i]);
      if (i != j - 1)
        paramStringBuilder.append(", "); 
    } 
    paramStringBuilder.append(']');
  }
  
  private static void objectArrayAppend(StringBuilder paramStringBuilder, Object[] paramArrayOfObject, Map<Object[], Object> paramMap) {
    paramStringBuilder.append('[');
    if (!paramMap.containsKey(paramArrayOfObject)) {
      paramMap.put(paramArrayOfObject, null);
      int j = paramArrayOfObject.length;
      for (int i = 0; i < j; i++) {
        deeplyAppendParameter(paramStringBuilder, paramArrayOfObject[i], paramMap);
        if (i != j - 1)
          paramStringBuilder.append(", "); 
      } 
      paramMap.remove(paramArrayOfObject);
    } else {
      paramStringBuilder.append("...");
    } 
    paramStringBuilder.append(']');
  }
  
  private static void safeObjectAppend(StringBuilder paramStringBuilder, Object paramObject) {
    try {
      return;
    } finally {
      Exception exception = null;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("SLF4J: Failed toString() invocation on an object of type [");
      stringBuilder.append(paramObject.getClass().getName());
      stringBuilder.append("]");
      Util.report(stringBuilder.toString(), exception);
      paramStringBuilder.append("[FAILED toString()]");
    } 
  }
  
  private static void shortArrayAppend(StringBuilder paramStringBuilder, short[] paramArrayOfshort) {
    paramStringBuilder.append('[');
    int j = paramArrayOfshort.length;
    for (int i = 0; i < j; i++) {
      paramStringBuilder.append(paramArrayOfshort[i]);
      if (i != j - 1)
        paramStringBuilder.append(", "); 
    } 
    paramStringBuilder.append(']');
  }
  
  private static Object[] trimmedCopy(Object[] paramArrayOfObject) {
    if (paramArrayOfObject != null && paramArrayOfObject.length != 0) {
      int i = paramArrayOfObject.length - 1;
      Object[] arrayOfObject = new Object[i];
      System.arraycopy(paramArrayOfObject, 0, arrayOfObject, 0, i);
      return arrayOfObject;
    } 
    throw new IllegalStateException("non-sensical empty or null argument array");
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\slf4j\helpers\MessageFormatter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
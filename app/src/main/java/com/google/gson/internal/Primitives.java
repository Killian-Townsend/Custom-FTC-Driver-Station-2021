package com.google.gson.internal;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Primitives {
  private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER_TYPE;
  
  private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE_TYPE;
  
  static {
    HashMap<Object, Object> hashMap1 = new HashMap<Object, Object>(16);
    HashMap<Object, Object> hashMap2 = new HashMap<Object, Object>(16);
    add((Map)hashMap1, (Map)hashMap2, boolean.class, Boolean.class);
    add((Map)hashMap1, (Map)hashMap2, byte.class, Byte.class);
    add((Map)hashMap1, (Map)hashMap2, char.class, Character.class);
    add((Map)hashMap1, (Map)hashMap2, double.class, Double.class);
    add((Map)hashMap1, (Map)hashMap2, float.class, Float.class);
    add((Map)hashMap1, (Map)hashMap2, int.class, Integer.class);
    add((Map)hashMap1, (Map)hashMap2, long.class, Long.class);
    add((Map)hashMap1, (Map)hashMap2, short.class, Short.class);
    add((Map)hashMap1, (Map)hashMap2, void.class, Void.class);
    PRIMITIVE_TO_WRAPPER_TYPE = Collections.unmodifiableMap(hashMap1);
    WRAPPER_TO_PRIMITIVE_TYPE = Collections.unmodifiableMap(hashMap2);
  }
  
  private Primitives() {
    throw new UnsupportedOperationException();
  }
  
  private static void add(Map<Class<?>, Class<?>> paramMap1, Map<Class<?>, Class<?>> paramMap2, Class<?> paramClass1, Class<?> paramClass2) {
    paramMap1.put(paramClass1, paramClass2);
    paramMap2.put(paramClass2, paramClass1);
  }
  
  public static boolean isPrimitive(Type paramType) {
    return PRIMITIVE_TO_WRAPPER_TYPE.containsKey(paramType);
  }
  
  public static boolean isWrapperType(Type paramType) {
    return WRAPPER_TO_PRIMITIVE_TYPE.containsKey($Gson$Preconditions.checkNotNull(paramType));
  }
  
  public static <T> Class<T> unwrap(Class<T> paramClass) {
    Class<T> clazz = (Class)WRAPPER_TO_PRIMITIVE_TYPE.get($Gson$Preconditions.checkNotNull(paramClass));
    return (clazz == null) ? paramClass : clazz;
  }
  
  public static <T> Class<T> wrap(Class<T> paramClass) {
    Class<T> clazz = (Class)PRIMITIVE_TO_WRAPPER_TYPE.get($Gson$Preconditions.checkNotNull(paramClass));
    return (clazz == null) ? paramClass : clazz;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\gson\internal\Primitives.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
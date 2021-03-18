package com.google.gson;

import com.google.gson.internal.;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;

public final class JsonPrimitive extends JsonElement {
  private static final Class<?>[] PRIMITIVE_TYPES = new Class[] { 
      int.class, long.class, short.class, float.class, double.class, byte.class, boolean.class, char.class, Integer.class, Long.class, 
      Short.class, Float.class, Double.class, Byte.class, Boolean.class, Character.class };
  
  private Object value;
  
  public JsonPrimitive(Boolean paramBoolean) {
    setValue(paramBoolean);
  }
  
  public JsonPrimitive(Character paramCharacter) {
    setValue(paramCharacter);
  }
  
  public JsonPrimitive(Number paramNumber) {
    setValue(paramNumber);
  }
  
  JsonPrimitive(Object paramObject) {
    setValue(paramObject);
  }
  
  public JsonPrimitive(String paramString) {
    setValue(paramString);
  }
  
  private static boolean isIntegral(JsonPrimitive paramJsonPrimitive) {
    Object object = paramJsonPrimitive.value;
    boolean bool1 = object instanceof Number;
    boolean bool = false;
    null = bool;
    if (bool1) {
      object = object;
      if (!(object instanceof BigInteger) && !(object instanceof Long) && !(object instanceof Integer) && !(object instanceof Short)) {
        null = bool;
        return (object instanceof Byte) ? true : null;
      } 
    } else {
      return null;
    } 
    return true;
  }
  
  private static boolean isPrimitiveOrString(Object<?> paramObject) {
    if (paramObject instanceof String)
      return true; 
    paramObject = (Object<?>)paramObject.getClass();
    Class<?>[] arrayOfClass = PRIMITIVE_TYPES;
    int j = arrayOfClass.length;
    for (int i = 0; i < j; i++) {
      if (arrayOfClass[i].isAssignableFrom((Class<?>)paramObject))
        return true; 
    } 
    return false;
  }
  
  JsonPrimitive deepCopy() {
    return this;
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = true;
    if (this == paramObject)
      return true; 
    if (paramObject != null) {
      if (getClass() != paramObject.getClass())
        return false; 
      paramObject = paramObject;
      if (this.value == null)
        return (((JsonPrimitive)paramObject).value == null); 
      if (isIntegral(this) && isIntegral((JsonPrimitive)paramObject))
        return (getAsNumber().longValue() == paramObject.getAsNumber().longValue()); 
      if (this.value instanceof Number && ((JsonPrimitive)paramObject).value instanceof Number) {
        double d1 = getAsNumber().doubleValue();
        double d2 = paramObject.getAsNumber().doubleValue();
        if (d1 != d2) {
          if (Double.isNaN(d1) && Double.isNaN(d2))
            return true; 
          bool = false;
        } 
        return bool;
      } 
      return this.value.equals(((JsonPrimitive)paramObject).value);
    } 
    return false;
  }
  
  public BigDecimal getAsBigDecimal() {
    Object object = this.value;
    return (object instanceof BigDecimal) ? (BigDecimal)object : new BigDecimal(this.value.toString());
  }
  
  public BigInteger getAsBigInteger() {
    Object object = this.value;
    return (object instanceof BigInteger) ? (BigInteger)object : new BigInteger(this.value.toString());
  }
  
  public boolean getAsBoolean() {
    return isBoolean() ? getAsBooleanWrapper().booleanValue() : Boolean.parseBoolean(getAsString());
  }
  
  Boolean getAsBooleanWrapper() {
    return (Boolean)this.value;
  }
  
  public byte getAsByte() {
    return isNumber() ? getAsNumber().byteValue() : Byte.parseByte(getAsString());
  }
  
  public char getAsCharacter() {
    return getAsString().charAt(0);
  }
  
  public double getAsDouble() {
    return isNumber() ? getAsNumber().doubleValue() : Double.parseDouble(getAsString());
  }
  
  public float getAsFloat() {
    return isNumber() ? getAsNumber().floatValue() : Float.parseFloat(getAsString());
  }
  
  public int getAsInt() {
    return isNumber() ? getAsNumber().intValue() : Integer.parseInt(getAsString());
  }
  
  public long getAsLong() {
    return isNumber() ? getAsNumber().longValue() : Long.parseLong(getAsString());
  }
  
  public Number getAsNumber() {
    Object object = this.value;
    return (Number)((object instanceof String) ? new LazilyParsedNumber((String)this.value) : object);
  }
  
  public short getAsShort() {
    return isNumber() ? getAsNumber().shortValue() : Short.parseShort(getAsString());
  }
  
  public String getAsString() {
    return isNumber() ? getAsNumber().toString() : (isBoolean() ? getAsBooleanWrapper().toString() : (String)this.value);
  }
  
  public int hashCode() {
    if (this.value == null)
      return 31; 
    if (isIntegral(this)) {
      long l = getAsNumber().longValue();
      return (int)(l >>> 32L ^ l);
    } 
    Object object = this.value;
    if (object instanceof Number) {
      long l = Double.doubleToLongBits(getAsNumber().doubleValue());
      return (int)(l >>> 32L ^ l);
    } 
    return object.hashCode();
  }
  
  public boolean isBoolean() {
    return this.value instanceof Boolean;
  }
  
  public boolean isNumber() {
    return this.value instanceof Number;
  }
  
  public boolean isString() {
    return this.value instanceof String;
  }
  
  void setValue(Object paramObject) {
    boolean bool;
    if (paramObject instanceof Character) {
      this.value = String.valueOf(((Character)paramObject).charValue());
      return;
    } 
    if (paramObject instanceof Number || isPrimitiveOrString(paramObject)) {
      bool = true;
    } else {
      bool = false;
    } 
    .Gson.Preconditions.checkArgument(bool);
    this.value = paramObject;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\gson\JsonPrimitive.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
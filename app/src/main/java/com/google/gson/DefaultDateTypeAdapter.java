package com.google.gson;

import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

final class DefaultDateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
  private final DateFormat enUsFormat;
  
  private final DateFormat localFormat;
  
  DefaultDateTypeAdapter() {
    this(DateFormat.getDateTimeInstance(2, 2, Locale.US), DateFormat.getDateTimeInstance(2, 2));
  }
  
  DefaultDateTypeAdapter(int paramInt) {
    this(DateFormat.getDateInstance(paramInt, Locale.US), DateFormat.getDateInstance(paramInt));
  }
  
  public DefaultDateTypeAdapter(int paramInt1, int paramInt2) {
    this(DateFormat.getDateTimeInstance(paramInt1, paramInt2, Locale.US), DateFormat.getDateTimeInstance(paramInt1, paramInt2));
  }
  
  DefaultDateTypeAdapter(String paramString) {
    this(new SimpleDateFormat(paramString, Locale.US), new SimpleDateFormat(paramString));
  }
  
  DefaultDateTypeAdapter(DateFormat paramDateFormat1, DateFormat paramDateFormat2) {
    this.enUsFormat = paramDateFormat1;
    this.localFormat = paramDateFormat2;
  }
  
  private Date deserializeToDate(JsonElement paramJsonElement) {
    synchronized (this.localFormat) {
      return this.localFormat.parse(paramJsonElement.getAsString());
    } 
  }
  
  public Date deserialize(JsonElement paramJsonElement, Type paramType, JsonDeserializationContext paramJsonDeserializationContext) throws JsonParseException {
    if (paramJsonElement instanceof JsonPrimitive) {
      Date date = deserializeToDate(paramJsonElement);
      if (paramType == Date.class)
        return date; 
      if (paramType == Timestamp.class)
        return new Timestamp(date.getTime()); 
      if (paramType == Date.class)
        return new Date(date.getTime()); 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(getClass());
      stringBuilder.append(" cannot deserialize to ");
      stringBuilder.append(paramType);
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    throw new JsonParseException("The date should be a string value");
  }
  
  public JsonElement serialize(Date paramDate, Type paramType, JsonSerializationContext paramJsonSerializationContext) {
    synchronized (this.localFormat) {
      return new JsonPrimitive(this.enUsFormat.format(paramDate));
    } 
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(DefaultDateTypeAdapter.class.getSimpleName());
    stringBuilder.append('(');
    stringBuilder.append(this.localFormat.getClass().getSimpleName());
    stringBuilder.append(')');
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\gson\DefaultDateTypeAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
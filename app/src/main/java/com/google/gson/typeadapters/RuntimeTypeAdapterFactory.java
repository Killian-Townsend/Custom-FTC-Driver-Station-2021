package com.google.gson.typeadapters;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public final class RuntimeTypeAdapterFactory<T> implements TypeAdapterFactory {
  private final Class<?> baseType;
  
  private final Map<String, Class<?>> labelToSubtype = new LinkedHashMap<String, Class<?>>();
  
  private final Map<Class<?>, String> subtypeToLabel = new LinkedHashMap<Class<?>, String>();
  
  private final String typeFieldName;
  
  private RuntimeTypeAdapterFactory(Class<?> paramClass, String paramString) {
    if (paramString != null && paramClass != null) {
      this.baseType = paramClass;
      this.typeFieldName = paramString;
      return;
    } 
    throw null;
  }
  
  public static <T> RuntimeTypeAdapterFactory<T> of(Class<T> paramClass) {
    return new RuntimeTypeAdapterFactory<T>(paramClass, "type");
  }
  
  public static <T> RuntimeTypeAdapterFactory<T> of(Class<T> paramClass, String paramString) {
    return new RuntimeTypeAdapterFactory<T>(paramClass, paramString);
  }
  
  public <R> TypeAdapter<R> create(Gson paramGson, TypeToken<R> paramTypeToken) {
    if (paramTypeToken.getRawType() != this.baseType)
      return null; 
    final LinkedHashMap<Object, Object> labelToDelegate = new LinkedHashMap<Object, Object>();
    final LinkedHashMap<Object, Object> subtypeToDelegate = new LinkedHashMap<Object, Object>();
    for (Map.Entry<String, Class<?>> entry : this.labelToSubtype.entrySet()) {
      TypeAdapter typeAdapter = paramGson.getDelegateAdapter(this, TypeToken.get((Class)entry.getValue()));
      linkedHashMap1.put(entry.getKey(), typeAdapter);
      linkedHashMap2.put(entry.getValue(), typeAdapter);
    } 
    return (new TypeAdapter<R>() {
        public R read(JsonReader param1JsonReader) throws IOException {
          JsonElement jsonElement1 = Streams.parse(param1JsonReader);
          JsonElement jsonElement2 = jsonElement1.getAsJsonObject().remove(RuntimeTypeAdapterFactory.this.typeFieldName);
          if (jsonElement2 != null) {
            String str = jsonElement2.getAsString();
            TypeAdapter typeAdapter = (TypeAdapter)labelToDelegate.get(str);
            if (typeAdapter != null)
              return (R)typeAdapter.fromJsonTree(jsonElement1); 
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("cannot deserialize ");
            stringBuilder1.append(RuntimeTypeAdapterFactory.this.baseType);
            stringBuilder1.append(" subtype named ");
            stringBuilder1.append(str);
            stringBuilder1.append("; did you forget to register a subtype?");
            throw new JsonParseException(stringBuilder1.toString());
          } 
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("cannot deserialize ");
          stringBuilder.append(RuntimeTypeAdapterFactory.this.baseType);
          stringBuilder.append(" because it does not define a field named ");
          stringBuilder.append(RuntimeTypeAdapterFactory.this.typeFieldName);
          throw new JsonParseException(stringBuilder.toString());
        }
        
        public void write(JsonWriter param1JsonWriter, R param1R) throws IOException {
          Class<?> clazz = param1R.getClass();
          String str = (String)RuntimeTypeAdapterFactory.this.subtypeToLabel.get(clazz);
          TypeAdapter typeAdapter = (TypeAdapter)subtypeToDelegate.get(clazz);
          if (typeAdapter != null) {
            JsonObject jsonObject = typeAdapter.toJsonTree(param1R).getAsJsonObject();
            if (!jsonObject.has(RuntimeTypeAdapterFactory.this.typeFieldName)) {
              JsonObject jsonObject1 = new JsonObject();
              jsonObject1.add(RuntimeTypeAdapterFactory.this.typeFieldName, (JsonElement)new JsonPrimitive(str));
              for (Map.Entry entry : jsonObject.entrySet())
                jsonObject1.add((String)entry.getKey(), (JsonElement)entry.getValue()); 
              Streams.write((JsonElement)jsonObject1, param1JsonWriter);
              return;
            } 
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("cannot serialize ");
            stringBuilder1.append(clazz.getName());
            stringBuilder1.append(" because it already defines a field named ");
            stringBuilder1.append(RuntimeTypeAdapterFactory.this.typeFieldName);
            throw new JsonParseException(stringBuilder1.toString());
          } 
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("cannot serialize ");
          stringBuilder.append(clazz.getName());
          stringBuilder.append("; did you forget to register a subtype?");
          throw new JsonParseException(stringBuilder.toString());
        }
      }).nullSafe();
  }
  
  public RuntimeTypeAdapterFactory<T> registerSubtype(Class<? extends T> paramClass) {
    return registerSubtype(paramClass, paramClass.getSimpleName());
  }
  
  public RuntimeTypeAdapterFactory<T> registerSubtype(Class<? extends T> paramClass, String paramString) {
    if (paramClass != null && paramString != null) {
      if (!this.subtypeToLabel.containsKey(paramClass) && !this.labelToSubtype.containsKey(paramString)) {
        this.labelToSubtype.put(paramString, paramClass);
        this.subtypeToLabel.put(paramClass, paramString);
        return this;
      } 
      throw new IllegalArgumentException("types and labels must be unique");
    } 
    throw null;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\gson\typeadapters\RuntimeTypeAdapterFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
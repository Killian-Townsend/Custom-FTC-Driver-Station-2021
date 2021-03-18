package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

public final class JsonAdapterAnnotationTypeAdapterFactory implements TypeAdapterFactory {
  private final ConstructorConstructor constructorConstructor;
  
  public JsonAdapterAnnotationTypeAdapterFactory(ConstructorConstructor paramConstructorConstructor) {
    this.constructorConstructor = paramConstructorConstructor;
  }
  
  public <T> TypeAdapter<T> create(Gson paramGson, TypeToken<T> paramTypeToken) {
    JsonAdapter jsonAdapter = (JsonAdapter)paramTypeToken.getRawType().getAnnotation(JsonAdapter.class);
    return (TypeAdapter)((jsonAdapter == null) ? null : getTypeAdapter(this.constructorConstructor, paramGson, paramTypeToken, jsonAdapter));
  }
  
  TypeAdapter<?> getTypeAdapter(ConstructorConstructor paramConstructorConstructor, Gson paramGson, TypeToken<?> paramTypeToken, JsonAdapter paramJsonAdapter) {
    TypeAdapter<?> typeAdapter1;
    Object object = paramConstructorConstructor.get(TypeToken.get(paramJsonAdapter.value())).construct();
    if (object instanceof TypeAdapter) {
      typeAdapter1 = (TypeAdapter)object;
    } else if (object instanceof TypeAdapterFactory) {
      typeAdapter1 = ((TypeAdapterFactory)object).create(paramGson, paramTypeToken);
    } else {
      boolean bool = object instanceof JsonSerializer;
      if (bool || object instanceof JsonDeserializer) {
        JsonDeserializer<?> jsonDeserializer = null;
        if (bool) {
          JsonSerializer jsonSerializer = (JsonSerializer)object;
        } else {
          paramConstructorConstructor = null;
        } 
        if (object instanceof JsonDeserializer)
          jsonDeserializer = (JsonDeserializer)object; 
        typeAdapter1 = new TreeTypeAdapter((JsonSerializer<?>)paramConstructorConstructor, jsonDeserializer, paramGson, paramTypeToken, null);
      } else {
        throw new IllegalArgumentException("@JsonAdapter value must be TypeAdapter, TypeAdapterFactory, JsonSerializer or JsonDeserializer reference.");
      } 
    } 
    TypeAdapter<?> typeAdapter2 = typeAdapter1;
    if (typeAdapter1 != null) {
      typeAdapter2 = typeAdapter1;
      if (paramJsonAdapter.nullSafe())
        typeAdapter2 = typeAdapter1.nullSafe(); 
    } 
    return typeAdapter2;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\gson\internal\bind\JsonAdapterAnnotationTypeAdapterFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
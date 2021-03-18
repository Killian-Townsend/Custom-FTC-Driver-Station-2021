package com.google.gson.interceptors;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public final class InterceptorFactory implements TypeAdapterFactory {
  public <T> TypeAdapter<T> create(Gson paramGson, TypeToken<T> paramTypeToken) {
    Intercept intercept = (Intercept)paramTypeToken.getRawType().getAnnotation(Intercept.class);
    return (intercept == null) ? null : new InterceptorAdapter<T>(paramGson.getDelegateAdapter(this, paramTypeToken), intercept);
  }
  
  static class InterceptorAdapter<T> extends TypeAdapter<T> {
    private final TypeAdapter<T> delegate;
    
    private final JsonPostDeserializer<T> postDeserializer;
    
    public InterceptorAdapter(TypeAdapter<T> param1TypeAdapter, Intercept param1Intercept) {
      try {
        this.delegate = param1TypeAdapter;
        this.postDeserializer = param1Intercept.postDeserialize().newInstance();
        return;
      } catch (Exception exception) {
        throw new RuntimeException(exception);
      } 
    }
    
    public T read(JsonReader param1JsonReader) throws IOException {
      Object object = this.delegate.read(param1JsonReader);
      this.postDeserializer.postDeserialize((T)object);
      return (T)object;
    }
    
    public void write(JsonWriter param1JsonWriter, T param1T) throws IOException {
      this.delegate.write(param1JsonWriter, param1T);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\gson\interceptors\InterceptorFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.google.gson.typeadapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.annotation.PostConstruct;

public class PostConstructAdapterFactory implements TypeAdapterFactory {
  public <T> TypeAdapter<T> create(Gson paramGson, TypeToken<T> paramTypeToken) {
    for (Class<Object> clazz = paramTypeToken.getRawType(); clazz != Object.class; clazz = (Class)clazz.getSuperclass()) {
      for (Method method : clazz.getDeclaredMethods()) {
        if (method.isAnnotationPresent((Class)PostConstruct.class)) {
          method.setAccessible(true);
          return new PostConstructAdapter<T>(paramGson.getDelegateAdapter(this, paramTypeToken), method);
        } 
      } 
    } 
    return null;
  }
  
  static final class PostConstructAdapter<T> extends TypeAdapter<T> {
    private final TypeAdapter<T> delegate;
    
    private final Method method;
    
    public PostConstructAdapter(TypeAdapter<T> param1TypeAdapter, Method param1Method) {
      this.delegate = param1TypeAdapter;
      this.method = param1Method;
    }
    
    public T read(JsonReader param1JsonReader) throws IOException {
      object = this.delegate.read(param1JsonReader);
      if (object != null)
        try {
          this.method.invoke(object, new Object[0]);
          return (T)object;
        } catch (IllegalAccessException illegalAccessException) {
          throw new AssertionError();
        } catch (InvocationTargetException object) {
          if (object.getCause() instanceof RuntimeException)
            throw (RuntimeException)object.getCause(); 
          throw new RuntimeException(object.getCause());
        }  
      return (T)object;
    }
    
    public void write(JsonWriter param1JsonWriter, T param1T) throws IOException {
      this.delegate.write(param1JsonWriter, param1T);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\gson\typeadapters\PostConstructAdapterFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
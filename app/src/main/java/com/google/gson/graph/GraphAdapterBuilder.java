package com.google.gson.graph;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public final class GraphAdapterBuilder {
  private final ConstructorConstructor constructorConstructor = new ConstructorConstructor(this.instanceCreators);
  
  private final Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<Type, InstanceCreator<?>>();
  
  public GraphAdapterBuilder addType(Type paramType) {
    return addType(paramType, new InstanceCreator<Object>() {
          public Object createInstance(Type param1Type) {
            return objectConstructor.construct();
          }
        });
  }
  
  public GraphAdapterBuilder addType(Type paramType, InstanceCreator<?> paramInstanceCreator) {
    if (paramType != null && paramInstanceCreator != null) {
      this.instanceCreators.put(paramType, paramInstanceCreator);
      return this;
    } 
    throw null;
  }
  
  public void registerOn(GsonBuilder paramGsonBuilder) {
    Factory factory = new Factory(this.instanceCreators);
    paramGsonBuilder.registerTypeAdapterFactory(factory);
    Iterator<Map.Entry> iterator = this.instanceCreators.entrySet().iterator();
    while (iterator.hasNext())
      paramGsonBuilder.registerTypeAdapter((Type)((Map.Entry)iterator.next()).getKey(), factory); 
  }
  
  static class Element<T> {
    private final JsonElement element;
    
    private final String id;
    
    private TypeAdapter<T> typeAdapter;
    
    private T value;
    
    Element(T param1T, String param1String, TypeAdapter<T> param1TypeAdapter, JsonElement param1JsonElement) {
      this.value = param1T;
      this.id = param1String;
      this.typeAdapter = param1TypeAdapter;
      this.element = param1JsonElement;
    }
    
    void read(GraphAdapterBuilder.Graph param1Graph) throws IOException {
      if (param1Graph.nextCreate == null) {
        GraphAdapterBuilder.Graph.access$702(param1Graph, this);
        Object object = this.typeAdapter.fromJsonTree(this.element);
        this.value = (T)object;
        if (object != null)
          return; 
        object = new StringBuilder();
        object.append("non-null value deserialized to null: ");
        object.append(this.element);
        throw new IllegalStateException(object.toString());
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unexpected recursive call to read() for ");
      stringBuilder.append(this.id);
      throw new IllegalStateException(stringBuilder.toString());
    }
    
    void write(JsonWriter param1JsonWriter) throws IOException {
      this.typeAdapter.write(param1JsonWriter, this.value);
    }
  }
  
  static class Factory implements TypeAdapterFactory, InstanceCreator {
    private final ThreadLocal<GraphAdapterBuilder.Graph> graphThreadLocal = new ThreadLocal<GraphAdapterBuilder.Graph>();
    
    private final Map<Type, InstanceCreator<?>> instanceCreators;
    
    Factory(Map<Type, InstanceCreator<?>> param1Map) {
      this.instanceCreators = param1Map;
    }
    
    public <T> TypeAdapter<T> create(Gson param1Gson, TypeToken<T> param1TypeToken) {
      return !this.instanceCreators.containsKey(param1TypeToken.getType()) ? null : new TypeAdapter<T>() {
          public T read(JsonReader param2JsonReader) throws IOException {
            GraphAdapterBuilder.Graph graph1;
            String str1;
            String str2;
            if (param2JsonReader.peek() == JsonToken.NULL) {
              param2JsonReader.nextNull();
              return null;
            } 
            GraphAdapterBuilder.Graph graph2 = GraphAdapterBuilder.Factory.this.graphThreadLocal.get();
            boolean bool = false;
            if (graph2 == null) {
              GraphAdapterBuilder.Graph graph4 = new GraphAdapterBuilder.Graph(new HashMap<Object, Object>());
              bool = true;
              param2JsonReader.beginObject();
              GraphAdapterBuilder.Graph graph3 = null;
              while (param2JsonReader.hasNext()) {
                String str = param2JsonReader.nextName();
                graph2 = graph3;
                if (graph3 == null)
                  str2 = str; 
                JsonElement jsonElement = (JsonElement)elementAdapter.read(param2JsonReader);
                graph4.map.put(str, new GraphAdapterBuilder.Element(null, str, typeAdapter, jsonElement));
                str1 = str2;
              } 
              param2JsonReader.endObject();
              graph1 = graph4;
            } else {
              str1 = graph1.nextString();
              null = str2;
            } 
            if (bool)
              GraphAdapterBuilder.Factory.this.graphThreadLocal.set(null); 
            try {
              GraphAdapterBuilder.Element element = (GraphAdapterBuilder.Element)((GraphAdapterBuilder.Graph)null).map.get(str1);
              if (element.value == null) {
                GraphAdapterBuilder.Element.access$602(element, typeAdapter);
                element.read((GraphAdapterBuilder.Graph)null);
              } 
              return element.value;
            } finally {
              if (bool)
                GraphAdapterBuilder.Factory.this.graphThreadLocal.remove(); 
            } 
          }
          
          public void write(JsonWriter param2JsonWriter, T param2T) throws IOException {
            if (param2T == null) {
              param2JsonWriter.nullValue();
              return;
            } 
            GraphAdapterBuilder.Graph graph2 = GraphAdapterBuilder.Factory.this.graphThreadLocal.get();
            boolean bool = false;
            GraphAdapterBuilder.Graph graph1 = graph2;
            if (graph2 == null) {
              bool = true;
              graph1 = new GraphAdapterBuilder.Graph(new IdentityHashMap<Object, Object>());
            } 
            GraphAdapterBuilder.Element<T> element2 = (GraphAdapterBuilder.Element)graph1.map.get(param2T);
            GraphAdapterBuilder.Element<T> element1 = element2;
            if (element2 == null) {
              element1 = new GraphAdapterBuilder.Element<T>(param2T, graph1.nextName(), typeAdapter, null);
              graph1.map.put(param2T, element1);
              graph1.queue.add(element1);
            } 
            if (bool) {
              GraphAdapterBuilder.Factory.this.graphThreadLocal.set(graph1);
              try {
                param2JsonWriter.beginObject();
                while (true) {
                  GraphAdapterBuilder.Element element = graph1.queue.poll();
                  if (element != null) {
                    param2JsonWriter.name(element.id);
                    element.write(param2JsonWriter);
                    continue;
                  } 
                  param2JsonWriter.endObject();
                  return;
                } 
              } finally {
                GraphAdapterBuilder.Factory.this.graphThreadLocal.remove();
              } 
            } 
            param2JsonWriter.value(element1.id);
          }
        };
    }
    
    public Object createInstance(Type param1Type) {
      Object object;
      GraphAdapterBuilder.Graph graph = this.graphThreadLocal.get();
      if (graph != null && graph.nextCreate != null) {
        object = ((InstanceCreator)this.instanceCreators.get(param1Type)).createInstance(param1Type);
        GraphAdapterBuilder.Element.access$502(graph.nextCreate, object);
        GraphAdapterBuilder.Graph.access$702(graph, null);
        return object;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unexpected call to createInstance() for ");
      stringBuilder.append(object);
      throw new IllegalStateException(stringBuilder.toString());
    }
  }
  
  class null extends TypeAdapter<T> {
    public T read(JsonReader param1JsonReader) throws IOException {
      GraphAdapterBuilder.Graph graph1;
      String str1;
      String str2;
      if (param1JsonReader.peek() == JsonToken.NULL) {
        param1JsonReader.nextNull();
        return null;
      } 
      GraphAdapterBuilder.Graph graph2 = this.this$0.graphThreadLocal.get();
      boolean bool = false;
      if (graph2 == null) {
        GraphAdapterBuilder.Graph graph4 = new GraphAdapterBuilder.Graph(new HashMap<Object, Object>());
        bool = true;
        param1JsonReader.beginObject();
        GraphAdapterBuilder.Graph graph3 = null;
        while (param1JsonReader.hasNext()) {
          String str = param1JsonReader.nextName();
          graph2 = graph3;
          if (graph3 == null)
            str2 = str; 
          JsonElement jsonElement = (JsonElement)elementAdapter.read(param1JsonReader);
          graph4.map.put(str, new GraphAdapterBuilder.Element(null, str, typeAdapter, jsonElement));
          str1 = str2;
        } 
        param1JsonReader.endObject();
        graph1 = graph4;
      } else {
        str1 = graph1.nextString();
        null = str2;
      } 
      if (bool)
        this.this$0.graphThreadLocal.set(null); 
      try {
        GraphAdapterBuilder.Element element = (GraphAdapterBuilder.Element)((GraphAdapterBuilder.Graph)null).map.get(str1);
        if (element.value == null) {
          GraphAdapterBuilder.Element.access$602(element, typeAdapter);
          element.read((GraphAdapterBuilder.Graph)null);
        } 
        return element.value;
      } finally {
        if (bool)
          this.this$0.graphThreadLocal.remove(); 
      } 
    }
    
    public void write(JsonWriter param1JsonWriter, T param1T) throws IOException {
      if (param1T == null) {
        param1JsonWriter.nullValue();
        return;
      } 
      GraphAdapterBuilder.Graph graph2 = this.this$0.graphThreadLocal.get();
      boolean bool = false;
      GraphAdapterBuilder.Graph graph1 = graph2;
      if (graph2 == null) {
        bool = true;
        graph1 = new GraphAdapterBuilder.Graph(new IdentityHashMap<Object, Object>());
      } 
      GraphAdapterBuilder.Element<T> element2 = (GraphAdapterBuilder.Element)graph1.map.get(param1T);
      GraphAdapterBuilder.Element<T> element1 = element2;
      if (element2 == null) {
        element1 = new GraphAdapterBuilder.Element<T>(param1T, graph1.nextName(), typeAdapter, null);
        graph1.map.put(param1T, element1);
        graph1.queue.add(element1);
      } 
      if (bool) {
        this.this$0.graphThreadLocal.set(graph1);
        try {
          param1JsonWriter.beginObject();
          while (true) {
            GraphAdapterBuilder.Element element = graph1.queue.poll();
            if (element != null) {
              param1JsonWriter.name(element.id);
              element.write(param1JsonWriter);
              continue;
            } 
            param1JsonWriter.endObject();
            return;
          } 
        } finally {
          this.this$0.graphThreadLocal.remove();
        } 
      } 
      param1JsonWriter.value(element1.id);
    }
  }
  
  static class Graph {
    private final Map<Object, GraphAdapterBuilder.Element<?>> map;
    
    private GraphAdapterBuilder.Element nextCreate;
    
    private final Queue<GraphAdapterBuilder.Element> queue = new LinkedList<GraphAdapterBuilder.Element>();
    
    private Graph(Map<Object, GraphAdapterBuilder.Element<?>> param1Map) {
      this.map = param1Map;
    }
    
    public String nextName() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("0x");
      stringBuilder.append(Integer.toHexString(this.map.size() + 1));
      return stringBuilder.toString();
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\gson\graph\GraphAdapterBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
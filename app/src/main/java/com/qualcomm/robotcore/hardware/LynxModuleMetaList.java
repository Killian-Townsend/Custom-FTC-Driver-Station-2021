package com.qualcomm.robotcore.hardware;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.qualcomm.robotcore.util.SerialNumber;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson;

public class LynxModuleMetaList implements Iterable<LynxModuleMeta> {
  public LynxModuleMeta[] modules;
  
  public SerialNumber serialNumber;
  
  public LynxModuleMetaList(SerialNumber paramSerialNumber) {
    this(paramSerialNumber, new LynxModuleMeta[0]);
  }
  
  public LynxModuleMetaList(SerialNumber paramSerialNumber, Collection<LynxModuleMeta> paramCollection) {
    this(paramSerialNumber, metaFromModules(paramCollection));
  }
  
  public LynxModuleMetaList(SerialNumber paramSerialNumber, List<RobotCoreLynxModule> paramList) {
    this(paramSerialNumber, metaFromModules(paramList));
  }
  
  private LynxModuleMetaList(SerialNumber paramSerialNumber, LynxModuleMeta[] paramArrayOfLynxModuleMeta) {
    this.serialNumber = paramSerialNumber;
    this.modules = paramArrayOfLynxModuleMeta;
  }
  
  public static LynxModuleMetaList fromSerializationString(String paramString) {
    JsonDeserializer jsonDeserializer = new JsonDeserializer() {
        public Object deserialize(JsonElement param1JsonElement, Type param1Type, JsonDeserializationContext param1JsonDeserializationContext) throws JsonParseException {
          return param1JsonDeserializationContext.deserialize(param1JsonElement, LynxModuleMeta.class);
        }
      };
    return (LynxModuleMetaList)(new GsonBuilder()).registerTypeAdapter(RobotCoreLynxModule.class, jsonDeserializer).create().fromJson(paramString, LynxModuleMetaList.class);
  }
  
  private static LynxModuleMeta[] metaFromModules(Collection<LynxModuleMeta> paramCollection) {
    LynxModuleMeta[] arrayOfLynxModuleMeta = new LynxModuleMeta[paramCollection.size()];
    Iterator<LynxModuleMeta> iterator = paramCollection.iterator();
    for (int i = 0; iterator.hasNext(); i++)
      arrayOfLynxModuleMeta[i] = iterator.next(); 
    return arrayOfLynxModuleMeta;
  }
  
  private static LynxModuleMeta[] metaFromModules(List<RobotCoreLynxModule> paramList) {
    int j = paramList.size();
    LynxModuleMeta[] arrayOfLynxModuleMeta = new LynxModuleMeta[j];
    for (int i = 0; i < j; i++)
      arrayOfLynxModuleMeta[i] = new LynxModuleMeta(paramList.get(i)); 
    return arrayOfLynxModuleMeta;
  }
  
  protected LynxModuleMetaList flatten() {
    LynxModuleMeta[] arrayOfLynxModuleMeta = new LynxModuleMeta[this.modules.length];
    for (int i = 0; i < this.modules.length; i++)
      arrayOfLynxModuleMeta[i] = new LynxModuleMeta(this.modules[i]); 
    return new LynxModuleMetaList(this.serialNumber, arrayOfLynxModuleMeta);
  }
  
  public LynxModuleMeta getParent() {
    int i = 0;
    while (true) {
      LynxModuleMeta[] arrayOfLynxModuleMeta = this.modules;
      if (i < arrayOfLynxModuleMeta.length) {
        LynxModuleMeta lynxModuleMeta = arrayOfLynxModuleMeta[i];
        if (lynxModuleMeta.isParent())
          return lynxModuleMeta; 
        i++;
        continue;
      } 
      return null;
    } 
  }
  
  public Iterator<LynxModuleMeta> iterator() {
    return Arrays.<LynxModuleMeta>asList(this.modules).iterator();
  }
  
  public String toSerializationString() {
    return SimpleGson.getInstance().toJson(flatten());
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[");
    LynxModuleMeta[] arrayOfLynxModuleMeta = this.modules;
    int j = arrayOfLynxModuleMeta.length;
    int i = 0;
    for (boolean bool = true; i < j; bool = false) {
      LynxModuleMeta lynxModuleMeta = arrayOfLynxModuleMeta[i];
      if (!bool)
        stringBuilder.append(" "); 
      stringBuilder.append(String.format(Locale.getDefault(), "%d(%s)", new Object[] { Integer.valueOf(lynxModuleMeta.getModuleAddress()), Boolean.valueOf(lynxModuleMeta.isParent()) }));
      i++;
    } 
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\LynxModuleMetaList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package com.qualcomm.robotcore.hardware.configuration;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class BuiltInConfigurationTypeJsonAdapter extends TypeAdapter<BuiltInConfigurationType> {
  public BuiltInConfigurationType read(JsonReader paramJsonReader) throws IOException {
    JsonToken jsonToken1 = paramJsonReader.peek();
    JsonToken jsonToken2 = JsonToken.NULL;
    String str = null;
    if (jsonToken1 == jsonToken2) {
      paramJsonReader.nextNull();
      return null;
    } 
    paramJsonReader.beginObject();
    while (paramJsonReader.hasNext()) {
      if (paramJsonReader.nextName().equals("xmlTag")) {
        str = paramJsonReader.nextString();
        continue;
      } 
      paramJsonReader.skipValue();
    } 
    paramJsonReader.endObject();
    return BuiltInConfigurationType.fromXmlTag(str);
  }
  
  public void write(JsonWriter paramJsonWriter, BuiltInConfigurationType paramBuiltInConfigurationType) throws IOException {
    if (paramBuiltInConfigurationType == null) {
      paramJsonWriter.nullValue();
      return;
    } 
    paramJsonWriter.beginObject();
    paramJsonWriter.name("xmlTag").value(paramBuiltInConfigurationType.getXmlTag());
    paramJsonWriter.name("name").value(paramBuiltInConfigurationType.getDisplayName(ConfigurationType.DisplayNameFlavor.Normal));
    paramJsonWriter.name("flavor").value(ConfigurationType.DeviceFlavor.BUILT_IN.toString());
    paramJsonWriter.endObject();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\BuiltInConfigurationTypeJsonAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
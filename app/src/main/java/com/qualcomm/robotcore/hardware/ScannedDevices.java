package com.qualcomm.robotcore.hardware;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ScannedDevices {
  public static final String TAG = "ScannedDevices";
  
  @Expose
  protected String errorMessage = null;
  
  protected final Object lock = new Object();
  
  @Expose
  @JsonAdapter(MapAdapter.class)
  protected Map<SerialNumber, DeviceManager.UsbDeviceType> map;
  
  public ScannedDevices() {
    this.map = new HashMap<SerialNumber, DeviceManager.UsbDeviceType>();
  }
  
  public ScannedDevices(ScannedDevices paramScannedDevices) {
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    this.map = (Map)hashMap;
    hashMap.clear();
    for (Map.Entry<SerialNumber, DeviceManager.UsbDeviceType> entry : paramScannedDevices.entrySet())
      this.map.put((SerialNumber)entry.getKey(), (DeviceManager.UsbDeviceType)entry.getValue()); 
    this.errorMessage = paramScannedDevices.errorMessage;
  }
  
  public static ScannedDevices fromSerializationString(String paramString) {
    ScannedDevices scannedDevices2 = new ScannedDevices();
    String str = paramString.trim();
    ScannedDevices scannedDevices1 = scannedDevices2;
    if (str.length() > 0)
      scannedDevices1 = (ScannedDevices)newGson().fromJson(str, ScannedDevices.class); 
    return scannedDevices1;
  }
  
  protected static Gson newGson() {
    return (new GsonBuilder()).excludeFieldsWithoutExposeAnnotation().create();
  }
  
  public boolean containsKey(SerialNumber paramSerialNumber) {
    synchronized (this.lock) {
      return this.map.containsKey(paramSerialNumber);
    } 
  }
  
  public Set<Map.Entry<SerialNumber, DeviceManager.UsbDeviceType>> entrySet() {
    synchronized (this.lock) {
      return this.map.entrySet();
    } 
  }
  
  public DeviceManager.UsbDeviceType get(SerialNumber paramSerialNumber) {
    synchronized (this.lock) {
      return this.map.get(paramSerialNumber);
    } 
  }
  
  public String getErrorMessage() {
    return this.errorMessage;
  }
  
  public Set<SerialNumber> keySet() {
    synchronized (this.lock) {
      return this.map.keySet();
    } 
  }
  
  public DeviceManager.UsbDeviceType put(SerialNumber paramSerialNumber, DeviceManager.UsbDeviceType paramUsbDeviceType) {
    synchronized (this.lock) {
      return this.map.put(paramSerialNumber, paramUsbDeviceType);
    } 
  }
  
  public DeviceManager.UsbDeviceType remove(SerialNumber paramSerialNumber) {
    synchronized (this.lock) {
      return this.map.remove(paramSerialNumber);
    } 
  }
  
  public void setErrorMessage(String paramString) {
    synchronized (this.lock) {
      if (TextUtils.isEmpty(this.errorMessage))
        this.errorMessage = paramString; 
      return;
    } 
  }
  
  public int size() {
    synchronized (this.lock) {
      return this.map.size();
    } 
  }
  
  public String toSerializationString() {
    return newGson().toJson(this, ScannedDevices.class);
  }
  
  protected static class MapAdapter extends TypeAdapter<Map<SerialNumber, DeviceManager.UsbDeviceType>> {
    public Map<SerialNumber, DeviceManager.UsbDeviceType> read(JsonReader param1JsonReader) throws IOException {
      HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
      param1JsonReader.beginArray();
      while (param1JsonReader.hasNext()) {
        param1JsonReader.beginObject();
        SerialNumber serialNumber = null;
        DeviceManager.UsbDeviceType usbDeviceType = null;
        while (param1JsonReader.hasNext()) {
          String str = param1JsonReader.nextName();
          byte b = -1;
          int i = str.hashCode();
          if (i != 106079) {
            if (i == 111972721 && str.equals("value"))
              b = 1; 
          } else if (str.equals("key")) {
            b = 0;
          } 
          if (b != 0) {
            if (b != 1) {
              param1JsonReader.skipValue();
              continue;
            } 
            usbDeviceType = DeviceManager.UsbDeviceType.from(param1JsonReader.nextString());
            continue;
          } 
          serialNumber = SerialNumber.fromString(param1JsonReader.nextString());
        } 
        param1JsonReader.endObject();
        if (serialNumber != null && usbDeviceType != null)
          hashMap.put(serialNumber, usbDeviceType); 
      } 
      param1JsonReader.endArray();
      return (Map)hashMap;
    }
    
    public void write(JsonWriter param1JsonWriter, Map<SerialNumber, DeviceManager.UsbDeviceType> param1Map) throws IOException {
      param1JsonWriter.beginArray();
      for (Map.Entry<SerialNumber, DeviceManager.UsbDeviceType> entry : param1Map.entrySet()) {
        param1JsonWriter.beginObject();
        param1JsonWriter.name("key").value(((SerialNumber)entry.getKey()).getString());
        param1JsonWriter.name("value").value(((DeviceManager.UsbDeviceType)entry.getValue()).toString());
        param1JsonWriter.endObject();
      } 
      param1JsonWriter.endArray();
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\ScannedDevices.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
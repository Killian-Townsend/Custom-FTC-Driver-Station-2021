package com.qualcomm.robotcore.util;

import android.text.TextUtils;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.qualcomm.robotcore.R;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.robotcore.internal.usb.EmbeddedSerialNumber;
import org.firstinspires.ftc.robotcore.internal.usb.FakeSerialNumber;
import org.firstinspires.ftc.robotcore.internal.usb.LynxModuleSerialNumber;
import org.firstinspires.ftc.robotcore.internal.usb.UsbSerialNumber;
import org.firstinspires.ftc.robotcore.internal.usb.VendorProductSerialNumber;

@JsonAdapter(SerialNumber.GsonTypeAdapter.class)
public abstract class SerialNumber implements Serializable {
  protected static final HashMap<String, String> deviceDisplayNames = new HashMap<String, String>();
  
  protected static final String embedded = "(embedded)";
  
  protected static final String fakePrefix = "FakeUSB:";
  
  protected static final String lynxModulePrefix = "ExpHub:";
  
  protected static final String vendorProductPrefix = "VendorProduct:";
  
  protected final String serialNumberString;
  
  protected SerialNumber(String paramString) {
    this.serialNumberString = paramString;
  }
  
  public static SerialNumber createEmbedded() {
    return (SerialNumber)new EmbeddedSerialNumber();
  }
  
  public static SerialNumber createFake() {
    return (SerialNumber)new FakeSerialNumber();
  }
  
  public static SerialNumber fromString(String paramString) {
    return (SerialNumber)(FakeSerialNumber.isLegacyFake(paramString) ? createFake() : (paramString.startsWith("FakeUSB:") ? new FakeSerialNumber(paramString) : (paramString.startsWith("VendorProduct:") ? new VendorProductSerialNumber(paramString) : (paramString.startsWith("ExpHub:") ? new LynxModuleSerialNumber(paramString) : (paramString.equals("(embedded)") ? createEmbedded() : new UsbSerialNumber(paramString))))));
  }
  
  public static SerialNumber fromStringOrNull(String paramString) {
    return !TextUtils.isEmpty(paramString) ? fromString(paramString) : null;
  }
  
  public static SerialNumber fromUsbOrNull(String paramString) {
    return UsbSerialNumber.isValidUsbSerialNumber(paramString) ? fromString(paramString) : null;
  }
  
  public static SerialNumber fromVidPid(int paramInt1, int paramInt2, String paramString) {
    return (SerialNumber)new VendorProductSerialNumber(paramInt1, paramInt2, paramString);
  }
  
  public static String getDeviceDisplayName(SerialNumber paramSerialNumber) {
    synchronized (deviceDisplayNames) {
      String str2 = deviceDisplayNames.get(paramSerialNumber.getString());
      String str1 = str2;
      if (str2 == null)
        str1 = Misc.formatForUser(R.string.deviceDisplayNameUnknownUSBDevice, new Object[] { paramSerialNumber }); 
      return str1;
    } 
  }
  
  public static void noteSerialNumberType(SerialNumber paramSerialNumber, String paramString) {
    synchronized (deviceDisplayNames) {
      deviceDisplayNames.put(paramSerialNumber.getString(), Misc.formatForUser("%s [%s]", new Object[] { paramString, paramSerialNumber }));
      return;
    } 
  }
  
  public boolean equals(Object paramObject) {
    return (paramObject == null) ? false : ((paramObject == this) ? true : ((paramObject instanceof SerialNumber) ? this.serialNumberString.equals(((SerialNumber)paramObject).serialNumberString) : ((paramObject instanceof String) ? equals((String)paramObject) : false)));
  }
  
  public boolean equals(String paramString) {
    return this.serialNumberString.equals(paramString);
  }
  
  public SerialNumber getScannableDeviceSerialNumber() {
    return this;
  }
  
  public String getString() {
    return this.serialNumberString;
  }
  
  public int hashCode() {
    return this.serialNumberString.hashCode() ^ 0xABCD9873;
  }
  
  public boolean isEmbedded() {
    return false;
  }
  
  public boolean isFake() {
    return false;
  }
  
  public boolean isUsb() {
    return false;
  }
  
  public boolean isVendorProduct() {
    return false;
  }
  
  public boolean matches(Object paramObject) {
    return equals(paramObject);
  }
  
  static class GsonTypeAdapter extends TypeAdapter<SerialNumber> {
    public SerialNumber read(JsonReader param1JsonReader) throws IOException {
      return SerialNumber.fromStringOrNull(param1JsonReader.nextString());
    }
    
    public void write(JsonWriter param1JsonWriter, SerialNumber param1SerialNumber) throws IOException {
      if (param1SerialNumber == null) {
        param1JsonWriter.nullValue();
        return;
      } 
      param1JsonWriter.value(param1SerialNumber.getString());
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcor\\util\SerialNumber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
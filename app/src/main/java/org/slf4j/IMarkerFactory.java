package org.slf4j;

public interface IMarkerFactory {
  boolean detachMarker(String paramString);
  
  boolean exists(String paramString);
  
  Marker getDetachedMarker(String paramString);
  
  Marker getMarker(String paramString);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\slf4j\IMarkerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
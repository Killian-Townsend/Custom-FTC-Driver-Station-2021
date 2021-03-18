package org.slf4j.helpers;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.IMarkerFactory;
import org.slf4j.Marker;

public class BasicMarkerFactory implements IMarkerFactory {
  private final ConcurrentMap<String, Marker> markerMap = new ConcurrentHashMap<String, Marker>();
  
  public boolean detachMarker(String paramString) {
    boolean bool = false;
    if (paramString == null)
      return false; 
    if (this.markerMap.remove(paramString) != null)
      bool = true; 
    return bool;
  }
  
  public boolean exists(String paramString) {
    return (paramString == null) ? false : this.markerMap.containsKey(paramString);
  }
  
  public Marker getDetachedMarker(String paramString) {
    return new BasicMarker(paramString);
  }
  
  public Marker getMarker(String paramString) {
    if (paramString != null) {
      Marker marker2 = this.markerMap.get(paramString);
      Marker marker1 = marker2;
      if (marker2 == null) {
        marker1 = new BasicMarker(paramString);
        Marker marker = this.markerMap.putIfAbsent(paramString, marker1);
        if (marker != null)
          marker1 = marker; 
      } 
      return marker1;
    } 
    throw new IllegalArgumentException("Marker name cannot be null");
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\slf4j\helpers\BasicMarkerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.slf4j;

import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.helpers.Util;
import org.slf4j.impl.StaticMarkerBinder;

public class MarkerFactory {
  static IMarkerFactory MARKER_FACTORY;
  
  static {
    try {
      MARKER_FACTORY = bwCompatibleGetMarkerFactoryFromBinder();
      return;
    } catch (NoClassDefFoundError noClassDefFoundError) {
      MARKER_FACTORY = (IMarkerFactory)new BasicMarkerFactory();
      return;
    } catch (Exception exception) {
      Util.report("Unexpected failure while binding MarkerFactory", exception);
      return;
    } 
  }
  
  private static IMarkerFactory bwCompatibleGetMarkerFactoryFromBinder() throws NoClassDefFoundError {
    try {
      return StaticMarkerBinder.getSingleton().getMarkerFactory();
    } catch (NoSuchMethodError noSuchMethodError) {
      return StaticMarkerBinder.SINGLETON.getMarkerFactory();
    } 
  }
  
  public static Marker getDetachedMarker(String paramString) {
    return MARKER_FACTORY.getDetachedMarker(paramString);
  }
  
  public static IMarkerFactory getIMarkerFactory() {
    return MARKER_FACTORY;
  }
  
  public static Marker getMarker(String paramString) {
    return MARKER_FACTORY.getMarker(paramString);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\slf4j\MarkerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
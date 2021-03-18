package org.slf4j.impl;

import org.slf4j.IMarkerFactory;
import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.spi.MarkerFactoryBinder;

public class StaticMarkerBinder implements MarkerFactoryBinder {
  public static final StaticMarkerBinder SINGLETON = new StaticMarkerBinder();
  
  final IMarkerFactory markerFactory = (IMarkerFactory)new BasicMarkerFactory();
  
  public static StaticMarkerBinder getSingleton() {
    return SINGLETON;
  }
  
  public IMarkerFactory getMarkerFactory() {
    return this.markerFactory;
  }
  
  public String getMarkerFactoryClassStr() {
    return BasicMarkerFactory.class.getName();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\slf4j\impl\StaticMarkerBinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
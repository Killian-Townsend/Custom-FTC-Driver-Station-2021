package org.slf4j;

import java.io.Serializable;
import java.util.Iterator;

public interface Marker extends Serializable {
  public static final String ANY_MARKER = "*";
  
  public static final String ANY_NON_NULL_MARKER = "+";
  
  void add(Marker paramMarker);
  
  boolean contains(String paramString);
  
  boolean contains(Marker paramMarker);
  
  boolean equals(Object paramObject);
  
  String getName();
  
  boolean hasChildren();
  
  boolean hasReferences();
  
  int hashCode();
  
  Iterator<Marker> iterator();
  
  boolean remove(Marker paramMarker);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\slf4j\Marker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
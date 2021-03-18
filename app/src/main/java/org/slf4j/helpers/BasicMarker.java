package org.slf4j.helpers;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Marker;

public class BasicMarker implements Marker {
  private static String CLOSE = " ]";
  
  private static String OPEN = "[ ";
  
  private static String SEP = ", ";
  
  private static final long serialVersionUID = -2849567615646933777L;
  
  private final String name;
  
  private List<Marker> referenceList = new CopyOnWriteArrayList<Marker>();
  
  BasicMarker(String paramString) {
    if (paramString != null) {
      this.name = paramString;
      return;
    } 
    throw new IllegalArgumentException("A marker name cannot be null");
  }
  
  public void add(Marker paramMarker) {
    if (paramMarker != null) {
      if (contains(paramMarker))
        return; 
      if (paramMarker.contains(this))
        return; 
      this.referenceList.add(paramMarker);
      return;
    } 
    throw new IllegalArgumentException("A null value cannot be added to a Marker as reference.");
  }
  
  public boolean contains(String paramString) {
    if (paramString != null) {
      if (this.name.equals(paramString))
        return true; 
      if (hasReferences()) {
        Iterator<Marker> iterator = this.referenceList.iterator();
        while (iterator.hasNext()) {
          if (((Marker)iterator.next()).contains(paramString))
            return true; 
        } 
      } 
      return false;
    } 
    throw new IllegalArgumentException("Other cannot be null");
  }
  
  public boolean contains(Marker paramMarker) {
    if (paramMarker != null) {
      if (equals(paramMarker))
        return true; 
      if (hasReferences()) {
        Iterator<Marker> iterator = this.referenceList.iterator();
        while (iterator.hasNext()) {
          if (((Marker)iterator.next()).contains(paramMarker))
            return true; 
        } 
      } 
      return false;
    } 
    throw new IllegalArgumentException("Other cannot be null");
  }
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (paramObject == null)
      return false; 
    if (!(paramObject instanceof Marker))
      return false; 
    paramObject = paramObject;
    return this.name.equals(paramObject.getName());
  }
  
  public String getName() {
    return this.name;
  }
  
  public boolean hasChildren() {
    return hasReferences();
  }
  
  public boolean hasReferences() {
    return (this.referenceList.size() > 0);
  }
  
  public int hashCode() {
    return this.name.hashCode();
  }
  
  public Iterator<Marker> iterator() {
    return this.referenceList.iterator();
  }
  
  public boolean remove(Marker paramMarker) {
    return this.referenceList.remove(paramMarker);
  }
  
  public String toString() {
    if (!hasReferences())
      return getName(); 
    Iterator<Marker> iterator = iterator();
    StringBuilder stringBuilder = new StringBuilder(getName());
    stringBuilder.append(' ');
    stringBuilder.append(OPEN);
    while (iterator.hasNext()) {
      stringBuilder.append(((Marker)iterator.next()).getName());
      if (iterator.hasNext())
        stringBuilder.append(SEP); 
    } 
    stringBuilder.append(CLOSE);
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\slf4j\helpers\BasicMarker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
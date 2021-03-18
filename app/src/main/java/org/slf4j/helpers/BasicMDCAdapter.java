package org.slf4j.helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.spi.MDCAdapter;

public class BasicMDCAdapter implements MDCAdapter {
  private InheritableThreadLocal<Map<String, String>> inheritableThreadLocal = new InheritableThreadLocal<Map<String, String>>() {
      protected Map<String, String> childValue(Map<String, String> param1Map) {
        return (param1Map == null) ? null : new HashMap<String, String>(param1Map);
      }
    };
  
  public void clear() {
    Map map = this.inheritableThreadLocal.get();
    if (map != null) {
      map.clear();
      this.inheritableThreadLocal.remove();
    } 
  }
  
  public String get(String paramString) {
    Map map = this.inheritableThreadLocal.get();
    return (map != null && paramString != null) ? (String)map.get(paramString) : null;
  }
  
  public Map<String, String> getCopyOfContextMap() {
    Map<? extends String, ? extends String> map = this.inheritableThreadLocal.get();
    return (map != null) ? new HashMap<String, String>(map) : null;
  }
  
  public Set<String> getKeys() {
    Map map = this.inheritableThreadLocal.get();
    return (map != null) ? map.keySet() : null;
  }
  
  public void put(String paramString1, String paramString2) {
    if (paramString1 != null) {
      Map<Object, Object> map2 = (Map)this.inheritableThreadLocal.get();
      Map<Object, Object> map1 = map2;
      if (map2 == null) {
        map1 = new HashMap<Object, Object>();
        this.inheritableThreadLocal.set(map1);
      } 
      map1.put(paramString1, paramString2);
      return;
    } 
    throw new IllegalArgumentException("key cannot be null");
  }
  
  public void remove(String paramString) {
    Map map = this.inheritableThreadLocal.get();
    if (map != null)
      map.remove(paramString); 
  }
  
  public void setContextMap(Map<String, String> paramMap) {
    this.inheritableThreadLocal.set(new HashMap<String, String>(paramMap));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\slf4j\helpers\BasicMDCAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
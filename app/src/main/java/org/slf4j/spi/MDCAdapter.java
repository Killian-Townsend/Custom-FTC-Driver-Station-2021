package org.slf4j.spi;

import java.util.Map;

public interface MDCAdapter {
  void clear();
  
  String get(String paramString);
  
  Map<String, String> getCopyOfContextMap();
  
  void put(String paramString1, String paramString2);
  
  void remove(String paramString);
  
  void setContextMap(Map<String, String> paramMap);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\slf4j\spi\MDCAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
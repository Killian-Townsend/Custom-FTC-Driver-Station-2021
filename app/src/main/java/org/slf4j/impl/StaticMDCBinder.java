package org.slf4j.impl;

import org.slf4j.helpers.NOPMDCAdapter;
import org.slf4j.spi.MDCAdapter;

public class StaticMDCBinder {
  public static final StaticMDCBinder SINGLETON = new StaticMDCBinder();
  
  public static final StaticMDCBinder getSingleton() {
    return SINGLETON;
  }
  
  public MDCAdapter getMDCA() {
    return (MDCAdapter)new NOPMDCAdapter();
  }
  
  public String getMDCAdapterClassStr() {
    return NOPMDCAdapter.class.getName();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\slf4j\impl\StaticMDCBinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
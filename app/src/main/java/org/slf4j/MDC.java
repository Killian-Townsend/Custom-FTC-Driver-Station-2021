package org.slf4j;

import java.io.Closeable;
import java.util.Map;
import org.slf4j.helpers.NOPMDCAdapter;
import org.slf4j.helpers.Util;
import org.slf4j.impl.StaticMDCBinder;
import org.slf4j.spi.MDCAdapter;

public class MDC {
  static final String NO_STATIC_MDC_BINDER_URL = "http://www.slf4j.org/codes.html#no_static_mdc_binder";
  
  static final String NULL_MDCA_URL = "http://www.slf4j.org/codes.html#null_MDCA";
  
  static MDCAdapter mdcAdapter;
  
  static {
    try {
      mdcAdapter = bwCompatibleGetMDCAdapterFromBinder();
      return;
    } catch (NoClassDefFoundError noClassDefFoundError) {
      mdcAdapter = (MDCAdapter)new NOPMDCAdapter();
      String str = noClassDefFoundError.getMessage();
      if (str != null && str.contains("StaticMDCBinder")) {
        Util.report("Failed to load class \"org.slf4j.impl.StaticMDCBinder\".");
        Util.report("Defaulting to no-operation MDCAdapter implementation.");
        Util.report("See http://www.slf4j.org/codes.html#no_static_mdc_binder for further details.");
        return;
      } 
      throw noClassDefFoundError;
    } catch (Exception exception) {
      Util.report("MDC binding unsuccessful.", exception);
      return;
    } 
  }
  
  private static MDCAdapter bwCompatibleGetMDCAdapterFromBinder() throws NoClassDefFoundError {
    try {
      return StaticMDCBinder.getSingleton().getMDCA();
    } catch (NoSuchMethodError noSuchMethodError) {
      return StaticMDCBinder.SINGLETON.getMDCA();
    } 
  }
  
  public static void clear() {
    MDCAdapter mDCAdapter = mdcAdapter;
    if (mDCAdapter != null) {
      mDCAdapter.clear();
      return;
    } 
    throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
  }
  
  public static String get(String paramString) throws IllegalArgumentException {
    if (paramString != null) {
      MDCAdapter mDCAdapter = mdcAdapter;
      if (mDCAdapter != null)
        return mDCAdapter.get(paramString); 
      throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
    } 
    throw new IllegalArgumentException("key parameter cannot be null");
  }
  
  public static Map<String, String> getCopyOfContextMap() {
    MDCAdapter mDCAdapter = mdcAdapter;
    if (mDCAdapter != null)
      return mDCAdapter.getCopyOfContextMap(); 
    throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
  }
  
  public static MDCAdapter getMDCAdapter() {
    return mdcAdapter;
  }
  
  public static void put(String paramString1, String paramString2) throws IllegalArgumentException {
    if (paramString1 != null) {
      MDCAdapter mDCAdapter = mdcAdapter;
      if (mDCAdapter != null) {
        mDCAdapter.put(paramString1, paramString2);
        return;
      } 
      throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
    } 
    throw new IllegalArgumentException("key parameter cannot be null");
  }
  
  public static MDCCloseable putCloseable(String paramString1, String paramString2) throws IllegalArgumentException {
    put(paramString1, paramString2);
    return new MDCCloseable(paramString1);
  }
  
  public static void remove(String paramString) throws IllegalArgumentException {
    if (paramString != null) {
      MDCAdapter mDCAdapter = mdcAdapter;
      if (mDCAdapter != null) {
        mDCAdapter.remove(paramString);
        return;
      } 
      throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
    } 
    throw new IllegalArgumentException("key parameter cannot be null");
  }
  
  public static void setContextMap(Map<String, String> paramMap) {
    MDCAdapter mDCAdapter = mdcAdapter;
    if (mDCAdapter != null) {
      mDCAdapter.setContextMap(paramMap);
      return;
    } 
    throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
  }
  
  public static class MDCCloseable implements Closeable {
    private final String key;
    
    private MDCCloseable(String param1String) {
      this.key = param1String;
    }
    
    public void close() {
      MDC.remove(this.key);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\slf4j\MDC.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
package org.firstinspires.ftc.robotserver.internal.webserver;

import java.util.HashMap;
import java.util.Map;

public class MimeTypesUtil {
  public static final String MIME_CSS = "text/css";
  
  public static final String MIME_JAVASCRIPT = "application/javascript";
  
  public static final String MIME_JSON = "application/json";
  
  public static final String MIME_TEXT = "text/plain";
  
  private static final Map<String, String> MIME_TYPES_BY_EXT;
  
  static {
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    MIME_TYPES_BY_EXT = (Map)hashMap;
    hashMap.put("asc", "text/plain");
    MIME_TYPES_BY_EXT.put("bin", "application/octet-stream");
    MIME_TYPES_BY_EXT.put("class", "application/octet-stream");
    MIME_TYPES_BY_EXT.put("css", "text/css");
    MIME_TYPES_BY_EXT.put("cur", "image/x-win-bitmap");
    MIME_TYPES_BY_EXT.put("doc", "application/msword");
    MIME_TYPES_BY_EXT.put("exe", "application/octet-stream");
    MIME_TYPES_BY_EXT.put("flv", "video/x-flv");
    MIME_TYPES_BY_EXT.put("gif", "image/gif");
    MIME_TYPES_BY_EXT.put("gz", "application/octet-stream");
    MIME_TYPES_BY_EXT.put("gzip", "application/octet-stream");
    MIME_TYPES_BY_EXT.put("html", "text/html");
    MIME_TYPES_BY_EXT.put("htm", "text/html");
    MIME_TYPES_BY_EXT.put("ico", "image/x-icon");
    MIME_TYPES_BY_EXT.put("java", "text/x-java-source, text/java");
    MIME_TYPES_BY_EXT.put("jpeg", "image/jpeg");
    MIME_TYPES_BY_EXT.put("jpg", "image/jpeg");
    MIME_TYPES_BY_EXT.put("js", "application/javascript");
    MIME_TYPES_BY_EXT.put("json", "application/json");
    MIME_TYPES_BY_EXT.put("less", "text/css");
    MIME_TYPES_BY_EXT.put("logcat", "text/plain");
    MIME_TYPES_BY_EXT.put("m3u8", "application/vnd.apple.mpegurl");
    MIME_TYPES_BY_EXT.put("m3u", "audio/mpeg-url");
    MIME_TYPES_BY_EXT.put("md", "text/plain");
    MIME_TYPES_BY_EXT.put("mov", "video/quicktime");
    MIME_TYPES_BY_EXT.put("mp3", "audio/mpeg");
    MIME_TYPES_BY_EXT.put("mp4", "video/mp4");
    MIME_TYPES_BY_EXT.put("ogg", "application/x-ogg");
    MIME_TYPES_BY_EXT.put("ogv", "video/ogg");
    MIME_TYPES_BY_EXT.put("pdf", "application/pdf");
    MIME_TYPES_BY_EXT.put("png", "image/png");
    MIME_TYPES_BY_EXT.put("svg", "image/svg+xml");
    MIME_TYPES_BY_EXT.put("swf", "application/x-shockwave-flash");
    MIME_TYPES_BY_EXT.put("ts", "video/mp2t");
    MIME_TYPES_BY_EXT.put("txt", "text/plain");
    MIME_TYPES_BY_EXT.put("wav", "audio/wav");
    MIME_TYPES_BY_EXT.put("xml", "text/xml");
    MIME_TYPES_BY_EXT.put("zip", "application/octet-stream");
    MIME_TYPES_BY_EXT.put("map", "application/json map");
    MIME_TYPES_BY_EXT.put("jar", "application/octet-stream");
    MIME_TYPES_BY_EXT.put("log", "text/plain");
    MIME_TYPES_BY_EXT.put("ttf", "application/x-font-truetype");
    MIME_TYPES_BY_EXT.put("otf", "application/x-font-opentype");
    MIME_TYPES_BY_EXT.put("woff", "application/font-woff");
    MIME_TYPES_BY_EXT.put("woff2", "application/font-woff2");
    MIME_TYPES_BY_EXT.put("eot", "application/vnd.ms-fontobject");
    MIME_TYPES_BY_EXT.put("sfnt", "application/font-sfnt");
  }
  
  public static String determineMimeType(String paramString) {
    int i = paramString.lastIndexOf(".");
    return (i != -1) ? getMimeType(paramString.substring(i + 1)) : null;
  }
  
  public static String getMimeType(String paramString) {
    String str = paramString;
    if (paramString.startsWith("."))
      str = paramString.substring(1); 
    return MIME_TYPES_BY_EXT.get(str);
  }
  
  public static class TypedPaths {
    private static final Map<String, String> mimeTypesByPath = new HashMap<String, String>();
    
    String determineMimeType(String param1String) {
      String str2 = mimeTypesByPath.get(param1String);
      String str1 = str2;
      if (str2 == null)
        str1 = MimeTypesUtil.determineMimeType(param1String); 
      return str1;
    }
    
    public void setMimeType(String param1String1, String param1String2) {
      mimeTypesByPath.put(param1String1, param1String2);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\webserver\MimeTypesUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
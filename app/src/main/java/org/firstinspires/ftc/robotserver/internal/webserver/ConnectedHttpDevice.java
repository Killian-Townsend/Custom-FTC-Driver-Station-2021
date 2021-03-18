package org.firstinspires.ftc.robotserver.internal.webserver;

import com.qualcomm.robotcore.util.RobotLog;
import fi.iki.elonen.NanoHTTPD;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson;
import org.firstinspires.ftc.robotcore.internal.webserver.FtcUserAgentCategory;

public final class ConnectedHttpDevice {
  public static final String TAG = ConnectedHttpDevice.class.getSimpleName();
  
  private static final Map<String, String> identityToMachineName = new HashMap<String, String>();
  
  public final String currentPage;
  
  public final String identity;
  
  public final String machineName;
  
  private ConnectedHttpDevice(String paramString1, String paramString2, String paramString3) {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial <init> : ()V
    //   4: aload_0
    //   5: aload_1
    //   6: putfield identity : Ljava/lang/String;
    //   9: aload_0
    //   10: aload_2
    //   11: putfield currentPage : Ljava/lang/String;
    //   14: aload_3
    //   15: invokestatic getMachineType : (Ljava/lang/String;)Ljava/lang/String;
    //   18: astore #5
    //   20: getstatic org/firstinspires/ftc/robotserver/internal/webserver/ConnectedHttpDevice.identityToMachineName : Ljava/util/Map;
    //   23: aload_1
    //   24: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   29: checkcast java/lang/String
    //   32: astore_2
    //   33: aload_2
    //   34: ifnull -> 48
    //   37: aload_2
    //   38: astore_3
    //   39: aload_2
    //   40: aload #5
    //   42: invokevirtual startsWith : (Ljava/lang/String;)Z
    //   45: ifne -> 129
    //   48: iconst_1
    //   49: istore #4
    //   51: iload #4
    //   53: ldc 2147483647
    //   55: if_icmpge -> 116
    //   58: new java/lang/StringBuilder
    //   61: dup
    //   62: invokespecial <init> : ()V
    //   65: astore_2
    //   66: aload_2
    //   67: aload #5
    //   69: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   72: pop
    //   73: aload_2
    //   74: ldc ' #'
    //   76: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   79: pop
    //   80: aload_2
    //   81: iload #4
    //   83: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   86: pop
    //   87: aload_2
    //   88: invokevirtual toString : ()Ljava/lang/String;
    //   91: astore_2
    //   92: getstatic org/firstinspires/ftc/robotserver/internal/webserver/ConnectedHttpDevice.identityToMachineName : Ljava/util/Map;
    //   95: aload_2
    //   96: invokeinterface containsValue : (Ljava/lang/Object;)Z
    //   101: ifne -> 107
    //   104: goto -> 116
    //   107: iload #4
    //   109: iconst_1
    //   110: iadd
    //   111: istore #4
    //   113: goto -> 51
    //   116: getstatic org/firstinspires/ftc/robotserver/internal/webserver/ConnectedHttpDevice.identityToMachineName : Ljava/util/Map;
    //   119: aload_1
    //   120: aload_2
    //   121: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   126: pop
    //   127: aload_2
    //   128: astore_3
    //   129: aload_0
    //   130: aload_3
    //   131: putfield machineName : Ljava/lang/String;
    //   134: return
  }
  
  public static ConnectedHttpDevice from(NanoHTTPD.IHTTPSession paramIHTTPSession) {
    Map map1 = paramIHTTPSession.getParameters();
    Map map2 = paramIHTTPSession.getHeaders();
    String str3 = ((List<String>)map1.get("name")).get(0);
    String str2 = (String)map2.get("remote-addr");
    String str4 = (String)map2.get("user-agent");
    String str1 = SessionCookie.fromSession(paramIHTTPSession);
    if (str1 == null) {
      RobotLog.vv(TAG, "cookie absent: using ip=%s", new Object[] { str2 });
      str1 = str2;
    } 
    return new ConnectedHttpDevice(str1, str3, str4);
  }
  
  public static ConnectedHttpDevice fromJson(String paramString) {
    return (ConnectedHttpDevice)SimpleGson.getInstance().fromJson(paramString, ConnectedHttpDevice.class);
  }
  
  private static String getMachineType(String paramString) {
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$internal$webserver$FtcUserAgentCategory[FtcUserAgentCategory.fromUserAgent(paramString).ordinal()];
    if (i != 1) {
      if (i != 2) {
        if (i == 3) {
          paramString = paramString.toLowerCase();
          if (testAgent(paramString, "Windows Phone"))
            return "WindowsPhone"; 
          if (testAgent(paramString, "Windows"))
            return "Windows"; 
          if (testAgent(paramString, "Macintosh"))
            return "Mac"; 
          if (testAgent(paramString, "CrOS"))
            return "ChromeBook"; 
          if (testAgent(paramString, "android"))
            return "Android"; 
          if (testAgent(paramString, "iPhone"))
            return "iPhone"; 
          if (testAgent(paramString, "iPad"))
            return "iPad"; 
          if (testAgent(paramString, "X11"))
            return "Unix"; 
          if (testAgent(paramString, "REV-UI"))
            return "REV UI"; 
        } 
        return "(unknown)";
      } 
      return "RobotController";
    } 
    return "DriverStation";
  }
  
  private static boolean testAgent(String paramString1, String paramString2) {
    return paramString1.contains(paramString2.toLowerCase());
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject instanceof ConnectedHttpDevice) {
      paramObject = paramObject;
      return Objects.equals(this.identity, ((ConnectedHttpDevice)paramObject).identity);
    } 
    return false;
  }
  
  public int hashCode() {
    return this.identity.hashCode();
  }
  
  public String toJson() {
    return SimpleGson.getInstance().toJson(this);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\webserver\ConnectedHttpDevice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
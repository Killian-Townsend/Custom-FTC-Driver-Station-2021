package org.firstinspires.ftc.robotserver.internal.webserver;

import com.qualcomm.robotcore.util.RobotLog;
import fi.iki.elonen.NanoHTTPD;
import java.util.Locale;
import java.util.UUID;

public class SessionCookie extends NanoHTTPD.Cookie {
  public static final String TAG = SessionCookie.class.getSimpleName();
  
  protected static final String sessionCookieName = "consoleSession";
  
  public SessionCookie(String paramString1, String paramString2) {
    super(paramString1, paramString2, "");
  }
  
  public static void ensureInSession(NanoHTTPD.IHTTPSession paramIHTTPSession) {
    if (fromSessionInternal(paramIHTTPSession) == null) {
      String str = UUID.randomUUID().toString();
      paramIHTTPSession.getCookies().set(new SessionCookie("consoleSession", str));
      RobotLog.vv(TAG, "added SessionCookie: cookie=%s uri='%s'", new Object[] { str, paramIHTTPSession.getUri() });
    } 
  }
  
  public static String fromSession(NanoHTTPD.IHTTPSession paramIHTTPSession) {
    String str = fromSessionInternal(paramIHTTPSession);
    if (str == null)
      RobotLog.ee(TAG, "session cookie unexpectedly null uri=%s", new Object[] { paramIHTTPSession.getUri() }); 
    return str;
  }
  
  protected static String fromSessionInternal(NanoHTTPD.IHTTPSession paramIHTTPSession) {
    return paramIHTTPSession.getCookies().read("consoleSession");
  }
  
  public String getHTTPHeader() {
    return (this.e == null || this.e.length() == 0) ? String.format(Locale.US, "%s=%s", new Object[] { this.n, this.v }) : super.getHTTPHeader();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\webserver\SessionCookie.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
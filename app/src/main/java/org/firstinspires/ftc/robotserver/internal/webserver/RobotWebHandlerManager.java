package org.firstinspires.ftc.robotserver.internal.webserver;

import android.content.res.AssetManager;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.WebHandlerManager;
import com.qualcomm.robotcore.util.WebServer;
import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.webserver.WebHandler;
import org.firstinspires.ftc.robotcore.internal.webserver.WebObserver;

public final class RobotWebHandlerManager implements WebHandlerManager {
  public static final NanoHTTPD.Response INTERNAL_ERROR_RESPONSE;
  
  public static final NanoHTTPD.Response OK_RESPONSE;
  
  public static final String TAG = RobotWebHandlerManager.class.getSimpleName();
  
  private final Map<String, WebHandler> handlerMap = new ConcurrentHashMap<String, WebHandler>(37);
  
  private final Map<String, WebObserver> observersMap = new ConcurrentHashMap<String, WebObserver>();
  
  private final WebHandler serveAsset = new ServeAsset();
  
  private final WebServer webServer;
  
  static {
    OK_RESPONSE = NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "text/plain", "");
    INTERNAL_ERROR_RESPONSE = NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "Internal Error");
  }
  
  public RobotWebHandlerManager(WebServer paramWebServer) {
    this.webServer = paramWebServer;
    RobotControllerWebHandlers.initialize(this);
  }
  
  public static NanoHTTPD.Response internalErrorResponse(String paramString1, String paramString2) {
    RobotLog.ee(paramString1, paramString2);
    return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", paramString2);
  }
  
  public static NanoHTTPD.Response internalErrorResponse(String paramString1, String paramString2, Object... paramVarArgs) {
    return internalErrorResponse(paramString1, String.format(paramString2, paramVarArgs));
  }
  
  public static NanoHTTPD.Response internalErrorResponse(String paramString, Throwable paramThrowable) {
    RobotLog.ee(paramString, paramThrowable, paramThrowable.getMessage());
    return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", paramThrowable.getMessage());
  }
  
  public WebHandler getRegistered(String paramString) {
    return this.handlerMap.get(paramString);
  }
  
  public WebServer getWebServer() {
    return this.webServer;
  }
  
  public void register(String paramString, WebHandler paramWebHandler) {
    this.handlerMap.put(paramString, paramWebHandler);
  }
  
  public void registerObserver(String paramString, WebObserver paramWebObserver) {
    this.observersMap.put(paramString, paramWebObserver);
  }
  
  NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession paramIHTTPSession) {
    SessionCookie.ensureInSession(paramIHTTPSession);
    Iterator<WebObserver> iterator = this.observersMap.values().iterator();
    while (iterator.hasNext())
      ((WebObserver)iterator.next()).observe(paramIHTTPSession); 
    String str = paramIHTTPSession.getUri();
    WebHandler webHandler = this.handlerMap.get(str);
    if (webHandler == null)
      try {
        return this.serveAsset.getResponse(paramIHTTPSession);
      } catch (IOException iOException) {
        RobotLog.logStackTrace(iOException);
        return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "Internal Error");
      } catch (fi.iki.elonen.NanoHTTPD.ResponseException responseException) {
        RobotLog.logStackTrace((Throwable)responseException);
        return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)responseException.getStatus(), "text/plain", responseException.getMessage());
      }  
    return webHandler.getResponse((NanoHTTPD.IHTTPSession)responseException);
  }
  
  private static class ServeAsset implements WebHandler {
    public static final String TAG = ServeAsset.class.getSimpleName();
    
    public static SimpleDateFormat gmtFrmt;
    
    public static String staticDateStamp = gmtFrmt.format(new Date());
    
    private final AssetManager assetManager = AppUtil.getInstance().getRootActivity().getAssets();
    
    private final MimeTypesUtil.TypedPaths typedPaths = new MimeTypesUtil.TypedPaths();
    
    public ServeAsset() {
      String str = MimeTypesUtil.getMimeType("json");
      this.typedPaths.setMimeType("css/bootstrap.min.css.map", str);
      this.typedPaths.setMimeType("css/bootstrap-theme.css.map", str);
      this.typedPaths.setMimeType("css/bootstrap-theme.min.css.map", str);
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException {
      String str1;
      String str2 = param1IHTTPSession.getUri();
      if (str2.startsWith("/")) {
        str1 = str2.substring(1);
      } else {
        str1 = str2;
      } 
      String str3 = this.typedPaths.determineMimeType(str1);
      if (str3 == null)
        return RobotWebHandlerManager.internalErrorResponse(TAG, "Mime type unknown: uri='%s' path='%s'", new Object[] { str2, str1 }); 
      try {
        InputStream inputStream = this.assetManager.open(str1);
        return NanoHTTPD.newChunkedResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, str3, inputStream);
      } catch (IOException iOException) {
        NanoHTTPD.Response response = NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.NOT_FOUND, "text/plain", "");
        response.addHeader("Date", staticDateStamp);
        return response;
      } 
    }
    
    static {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
      gmtFrmt = simpleDateFormat;
      simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\webserver\RobotWebHandlerManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
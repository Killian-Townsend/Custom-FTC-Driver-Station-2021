package org.firstinspires.ftc.robotserver.internal.webserver;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.util.Device;
import com.qualcomm.robotcore.util.IncludedFirmwareFileInfo;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.util.ThreadPool;
import com.qualcomm.robotcore.util.WebHandlerManager;
import com.qualcomm.robotcore.util.WebServer;
import fi.iki.elonen.NanoHTTPD;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import org.firstinspires.ftc.robotcore.internal.hardware.android.AndroidBoard;
import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.network.RecvLoopRunnable;
import org.firstinspires.ftc.robotcore.internal.network.RobotCoreCommandList;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.ui.UILocation;
import org.firstinspires.ftc.robotcore.internal.webserver.RobotControllerWebInfo;
import org.firstinspires.ftc.robotcore.internal.webserver.WebHandler;
import org.firstinspires.ftc.robotcore.internal.webserver.WebObserver;
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.WebSocketManager;
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.WebSocketNamespaceHandler;
import org.firstinspires.ftc.robotserver.internal.webserver.controlhubupdater.ChUpdaterCommManager;
import org.firstinspires.ftc.robotserver.internal.webserver.controlhubupdater.ChUpdaterUploadResponse;

public class RobotControllerWebHandlers {
  public static boolean DEBUG = false;
  
  public static final String INDEX_FILE = "frame.html";
  
  public static final String PARAM_AP_PASSWORD = "password";
  
  public static final String PARAM_CHANNEL_NAME = "channelName";
  
  public static final String PARAM_FILENAME = "filename";
  
  public static final String PARAM_MESSAGE = "message";
  
  public static final String PARAM_NAME = "name";
  
  public static final String PARAM_NEW_NAME = "new_name";
  
  public static final String PARAM_SERIAL_NUMBER = "serialNumber";
  
  public static final String TAG = RobotControllerWebHandlers.class.getSimpleName();
  
  public static final String URI_ANON_PING = "/anonymousPing";
  
  public static final String URI_CHANGE_NETWORK_SETTINGS = "/changeNetworkSettings";
  
  public static final String URI_COLORS = "/css/colors.less";
  
  public static final String URI_DOWNLOAD_FILE = "/downloadFile";
  
  public static final String URI_EXIT_PROGRAM_AND_MANAGE = "/exitProgramAndManage";
  
  public static final String URI_LIST_LOG_FILES = "/listLogs";
  
  public static final String URI_NAV_HELP = "/help.html";
  
  public static final String URI_NAV_HOME = "/connection.html";
  
  public static final String URI_NAV_MANAGE = "/manage.html";
  
  public static final String URI_PERFORM_REV_FIRMWARE_UPDATE = "/performRevFirmwareUpdate";
  
  public static final String URI_PING = "/ping";
  
  public static final String URI_RC_CONFIG = "/js/rc_config.js";
  
  public static final String URI_RC_INFO = "/js/rcInfo.json";
  
  public static final String URI_REBOOT = "/reboot";
  
  public static final String URI_REV_HUBS_AVAILABLE_FOR_UPDATE = "/revHubsAvailableForUpdate";
  
  public static final String URI_TOAST = "/toast";
  
  public static final String URI_UPDATE_CONTROL_HUB_APK = "/updateControlHubAPK";
  
  public static final String URI_UPLOAD_CONTROL_HUB_OTA = "/uploadControlHubOta";
  
  public static final String URI_UPLOAD_EXPANSION_HUB_FIRMWARE = "/uploadExpansionHubFirmware";
  
  public static final String URI_UPLOAD_TFLITE_MODEL_FILE = "/uploadTfliteModelFile";
  
  public static final String URI_UPLOAD_WEBCAM_CALIBRATION_FILE = "/uploadWebcamCalibrationFile";
  
  static {
    DEBUG = false;
  }
  
  public static WebHandler decorateWithParms(WebHandler paramWebHandler) {
    return new SessionParametersGenerator(paramWebHandler);
  }
  
  public static void initialize(WebHandlerManager paramWebHandlerManager) {
    DragonboardAPKUpdate dragonboardAPKUpdate;
    ChUpdaterCommManager chUpdaterCommManager;
    WebSocketManager webSocketManager = paramWebHandlerManager.getWebServer().getWebSocketManager();
    if (AndroidBoard.getInstance().hasControlHubUpdater()) {
      chUpdaterCommManager = new ChUpdaterCommManager(webSocketManager);
      webSocketManager.registerNamespaceHandler((WebSocketNamespaceHandler)chUpdaterCommManager);
      ControlHubApkUpdate controlHubApkUpdate = new ControlHubApkUpdate(chUpdaterCommManager);
    } else {
      dragonboardAPKUpdate = new DragonboardAPKUpdate();
      webSocketManager.registerNamespaceAsBroadcastOnly("ControlHubUpdater");
      chUpdaterCommManager = null;
    } 
    paramWebHandlerManager.register("/", new ServerRootIndex("frame.html"));
    paramWebHandlerManager.register("/anonymousPing", new AnonymousPing());
    paramWebHandlerManager.register("/ping", decorateWithParms(new ClientPing()));
    paramWebHandlerManager.register("/listLogs", new ListLogFiles());
    paramWebHandlerManager.register("/downloadFile", new FileDownload());
    paramWebHandlerManager.register("/changeNetworkSettings", decorateWithParms(new ChangeNetworkSettings()));
    paramWebHandlerManager.register("/updateControlHubAPK", dragonboardAPKUpdate);
    paramWebHandlerManager.register("/uploadExpansionHubFirmware", new StandardUpload(AppUtil.LYNX_FIRMWARE_UPDATE_DIR.getAbsolutePath()));
    paramWebHandlerManager.register("/revHubsAvailableForUpdate", new RevHubsAvailableForUpdate());
    paramWebHandlerManager.register("/performRevFirmwareUpdate", decorateWithParms(new PerformRevFirmwareUpdate()));
    paramWebHandlerManager.register("/uploadWebcamCalibrationFile", new StandardUpload(AppUtil.WEBCAM_CALIBRATIONS_DIR.getAbsolutePath()));
    paramWebHandlerManager.register("/uploadTfliteModelFile", new StandardUpload(AppUtil.TFLITE_MODELS_DIR.getAbsolutePath()));
    paramWebHandlerManager.register("/uploadControlHubOta", new OtaUpdate(chUpdaterCommManager));
    paramWebHandlerManager.register("/js/rc_config.js", new RobotControllerConfiguration());
    paramWebHandlerManager.register("/js/rcInfo.json", new RobotControllerInfoHandler(paramWebHandlerManager.getWebServer()));
    paramWebHandlerManager.register("/reboot", new Reboot());
    paramWebHandlerManager.register("/toast", new SimpleSuccess());
    paramWebHandlerManager.register("/exitProgramAndManage", new SimpleSuccess());
    AppThemeColorsHandler appThemeColorsHandler = new AppThemeColorsHandler();
    paramWebHandlerManager.register("/css/colors.less", appThemeColorsHandler);
    paramWebHandlerManager.registerObserver("/css/colors.less", appThemeColorsHandler);
  }
  
  public static class AnonymousPing implements WebHandler {
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) {
      if (RobotControllerWebHandlers.DEBUG)
        RobotLog.dd(RobotControllerWebHandlers.TAG, "In AnonymousPing"); 
      return RobotWebHandlerManager.OK_RESPONSE;
    }
  }
  
  public static class AppThemeColorsHandler implements WebHandler, WebObserver {
    public static final String TAG = AppThemeColorsHandler.class.getSimpleName();
    
    protected final Map<String, String> sessionColors = new ConcurrentHashMap<String, String>();
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      String str1 = SessionCookie.fromSession(param1IHTTPSession);
      if (str1 == null) {
        str1 = null;
      } else {
        str1 = this.sessionColors.get(str1);
      } 
      String str2 = str1;
      if (str1 == null)
        str2 = InstanceHolder.ourColors; 
      return NoCachingWebHandler.setNoCache(param1IHTTPSession, NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "text/css", str2));
    }
    
    public void observe(NanoHTTPD.IHTTPSession param1IHTTPSession) {
      String str = (String)param1IHTTPSession.getHeaders().get(AppThemeColors.htppHeaderNameLower);
      if (str != null) {
        String str1 = SessionCookie.fromSession(param1IHTTPSession);
        if (str1 != null)
          this.sessionColors.put(str1, AppThemeColors.fromHeader(str)); 
      } 
    }
    
    protected static class InstanceHolder {
      public static final String ourColors = AppThemeColors.fromTheme().toLess();
    }
  }
  
  protected static class InstanceHolder {
    public static final String ourColors = AppThemeColors.fromTheme().toLess();
  }
  
  public static class ChangeNetworkSettings implements WebHandler {
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: aload_1
      //   3: invokeinterface getParameters : ()Ljava/util/Map;
      //   8: ldc 'name'
      //   10: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
      //   15: checkcast java/util/List
      //   18: astore #5
      //   20: aload_1
      //   21: invokeinterface getParameters : ()Ljava/util/Map;
      //   26: ldc 'password'
      //   28: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
      //   33: checkcast java/util/List
      //   36: astore_2
      //   37: aload_1
      //   38: invokeinterface getParameters : ()Ljava/util/Map;
      //   43: ldc 'channelName'
      //   45: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
      //   50: checkcast java/util/List
      //   53: astore #4
      //   55: aconst_null
      //   56: astore_3
      //   57: aload #5
      //   59: ifnull -> 153
      //   62: aload #5
      //   64: iconst_0
      //   65: invokeinterface get : (I)Ljava/lang/Object;
      //   70: checkcast java/lang/String
      //   73: astore_1
      //   74: goto -> 77
      //   77: aload_2
      //   78: ifnull -> 158
      //   81: aload_2
      //   82: iconst_0
      //   83: invokeinterface get : (I)Ljava/lang/Object;
      //   88: checkcast java/lang/String
      //   91: astore_2
      //   92: goto -> 95
      //   95: aload #4
      //   97: ifnull -> 115
      //   100: aload #4
      //   102: iconst_0
      //   103: invokeinterface get : (I)Ljava/lang/Object;
      //   108: checkcast java/lang/String
      //   111: invokestatic fromName : (Ljava/lang/String;)Lorg/firstinspires/ftc/robotcore/internal/network/ApChannel;
      //   114: astore_3
      //   115: invokestatic getInstance : ()Lorg/firstinspires/ftc/robotcore/internal/network/NetworkConnectionHandler;
      //   118: invokevirtual getNetworkConnection : ()Lcom/qualcomm/robotcore/wifi/NetworkConnection;
      //   121: aload_1
      //   122: aload_2
      //   123: aload_3
      //   124: invokevirtual setNetworkSettings : (Ljava/lang/String;Ljava/lang/String;Lorg/firstinspires/ftc/robotcore/internal/network/ApChannel;)V
      //   127: getstatic org/firstinspires/ftc/robotserver/internal/webserver/RobotWebHandlerManager.OK_RESPONSE : Lfi/iki/elonen/NanoHTTPD$Response;
      //   130: astore_1
      //   131: aload_0
      //   132: monitorexit
      //   133: aload_1
      //   134: areturn
      //   135: astore_1
      //   136: getstatic org/firstinspires/ftc/robotserver/internal/webserver/RobotControllerWebHandlers.TAG : Ljava/lang/String;
      //   139: aload_1
      //   140: invokestatic internalErrorResponse : (Ljava/lang/String;Ljava/lang/Throwable;)Lfi/iki/elonen/NanoHTTPD$Response;
      //   143: astore_1
      //   144: aload_0
      //   145: monitorexit
      //   146: aload_1
      //   147: areturn
      //   148: astore_1
      //   149: aload_0
      //   150: monitorexit
      //   151: aload_1
      //   152: athrow
      //   153: aconst_null
      //   154: astore_1
      //   155: goto -> 77
      //   158: aconst_null
      //   159: astore_2
      //   160: goto -> 95
      // Exception table:
      //   from	to	target	type
      //   2	55	148	finally
      //   62	74	148	finally
      //   81	92	148	finally
      //   100	115	148	finally
      //   115	127	135	org/firstinspires/ftc/robotcore/internal/network/InvalidNetworkSettingException
      //   115	127	148	finally
      //   127	131	148	finally
      //   136	144	148	finally
    }
  }
  
  public static class ClientPing extends RequireNameHandler {
    final PingResponse pingResponse = new PingResponse();
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession, String param1String) throws IOException, NanoHTTPD.ResponseException {
      this.pingResponse.noteDevicePing(ConnectedHttpDevice.from(param1IHTTPSession));
      this.pingResponse.removeOldPings();
      return NoCachingWebHandler.setNoCache(param1IHTTPSession, NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, MimeTypesUtil.getMimeType("json"), this.pingResponse.toJson()));
    }
  }
  
  private static class ControlHubApkUpdate extends ControlHubUpdaterUpload {
    protected ControlHubApkUpdate(ChUpdaterCommManager param1ChUpdaterCommManager) {
      super(ChUpdaterCommManager.UpdateType.APP, param1ChUpdaterCommManager);
    }
    
    public File provideDestinationDirectory(String param1String, File param1File) {
      return AppUtil.RC_APP_UPDATE_DIR;
    }
  }
  
  private static abstract class ControlHubUpdaterUpload extends FileUpload {
    private final ChUpdaterCommManager chUpdaterCommManager;
    
    private final ChUpdaterCommManager.UpdateType updateType;
    
    protected ControlHubUpdaterUpload(ChUpdaterCommManager.UpdateType param1UpdateType, ChUpdaterCommManager param1ChUpdaterCommManager) {
      this.updateType = param1UpdateType;
      this.chUpdaterCommManager = param1ChUpdaterCommManager;
    }
    
    public final NanoHTTPD.Response hook(File param1File) {
      if (AndroidBoard.getInstance().hasControlHubUpdater()) {
        ChUpdaterCommManager chUpdaterCommManager = this.chUpdaterCommManager;
        if (chUpdaterCommManager != null)
          return ChUpdaterUploadResponse.create(chUpdaterCommManager.startUpdate(this.updateType, param1File)); 
      } 
      if (AndroidBoard.getInstance().hasControlHubUpdater())
        RobotLog.ww(RobotControllerWebHandlers.TAG, "This device should include the Control Hub Updater, yet chUpdaterCommManager is null."); 
      return RobotWebHandlerManager.internalErrorResponse(RobotControllerWebHandlers.TAG, AppUtil.getDefContext().getString(R.string.ch_updater_not_supported));
    }
  }
  
  private static final class DragonboardAPKUpdate extends FileUpload {
    private static final File DESTINATION_DIR = AppUtil.RC_APP_UPDATE_DIR;
    
    private static final String DRAGONBOARD_APK_METADATA_TAG = "org.firstinspires.main.entry";
    
    private DragonboardAPKUpdate() {}
    
    private String getDescription(String param1String, PackageInfo param1PackageInfo) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(param1String);
      stringBuilder.append(System.lineSeparator());
      stringBuilder.append(param1PackageInfo.packageName);
      stringBuilder.append(System.lineSeparator());
      ActivityInfo[] arrayOfActivityInfo = param1PackageInfo.activities;
      int k = arrayOfActivityInfo.length;
      int i = 0;
      int j;
      for (j = i; i < k; j = m) {
        ActivityInfo activityInfo = arrayOfActivityInfo[i];
        int m = j;
        if (activityInfo.metaData != null) {
          m = j;
          if (Device.isRevControlHub()) {
            m = j;
            if (activityInfo.metaData.getBoolean("org.firstinspires.main.entry", false)) {
              stringBuilder.append(param1PackageInfo.packageName);
              stringBuilder.append("/");
              stringBuilder.append(activityInfo.name);
              stringBuilder.append(System.lineSeparator());
              m = 1;
            } 
          } 
        } 
        i++;
      } 
      return (j != 0) ? stringBuilder.toString() : "";
    }
    
    private NanoHTTPD.Response invalidApk(File param1File, String param1String) {
      RobotLog.ii(RobotControllerWebHandlers.TAG, "Invalid APK. Removing from file system");
      if (!param1File.delete())
        RobotLog.ii(RobotControllerWebHandlers.TAG, "Invalid APK cannot be removed"); 
      String str = "Invalid APK";
      if (param1String != null) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid APK");
        stringBuilder.append(": ");
        stringBuilder.append(param1String);
        str = stringBuilder.toString();
      } 
      return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", str);
    }
    
    private NanoHTTPD.Response writeDescription(File param1File) {
      PackageInfo packageInfo = AppUtil.getInstance().getActivity().getPackageManager().getPackageArchiveInfo(param1File.getAbsolutePath(), 129);
      if (packageInfo == null)
        return invalidApk(param1File, null); 
      String str = getDescription(param1File.getAbsolutePath(), packageInfo);
      return str.isEmpty() ? invalidApk(param1File, "This APK does not support the Control Hub.") : writeDescriptionFile(param1File.getName(), str);
    }
    
    private NanoHTTPD.Response writeDescriptionFile(String param1String1, String param1String2) {
      int i = param1String1.lastIndexOf('.');
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(param1String1.substring(0, i));
      stringBuilder.append(".des");
      param1String1 = stringBuilder.toString();
      File file = new File(DESTINATION_DIR, param1String1);
      try {
        FileWriter fileWriter = new FileWriter(file);
        try {
          fileWriter.write(param1String2);
          return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "application/json", "{}");
        } finally {
          param1String2 = null;
        } 
      } catch (IOException iOException) {
        RobotLog.ii(RobotControllerWebHandlers.TAG, iOException.getMessage());
        return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", iOException.getMessage());
      } 
    }
    
    public NanoHTTPD.Response hook(File param1File) {
      return writeDescription(param1File);
    }
    
    public File provideDestinationDirectory(String param1String, File param1File) {
      return DESTINATION_DIR;
    }
  }
  
  public static class FileDownload extends RequireNameHandler {
    public static File fileRoot = AppUtil.ROOT_FOLDER;
    
    private NanoHTTPD.Response fetchFileContent(File param1File) throws IOException {
      try {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(param1File));
        NanoHTTPD.Response response = NanoHTTPD.newChunkedResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, MimeTypesUtil.determineMimeType(param1File.getName()), bufferedInputStream);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("attachment; filename=\"");
        stringBuilder.append(param1File.getName());
        stringBuilder.append("\"");
        response.addHeader("Content-Disposition", stringBuilder.toString());
        return response;
      } catch (IOException iOException) {
        return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.NOT_FOUND, "text/plain", "");
      } 
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession, String param1String) throws IOException {
      if (RobotControllerWebHandlers.DEBUG)
        RobotLog.dd(RobotControllerWebHandlers.TAG, "FileDownload name=%s", new Object[] { param1String }); 
      return fetchFileContent(new File(fileRoot, param1String));
    }
  }
  
  public static abstract class FileUpload implements WebHandler {
    private static final String FILE_FORM_ID = "file";
    
    private boolean checkDir(File param1File) {
      return !param1File.exists() ? param1File.mkdirs() : true;
    }
    
    private String getUploadedFileName(NanoHTTPD.IHTTPSession param1IHTTPSession) throws FilenameNotProvidedException {
      if (param1IHTTPSession.getParameters().containsKey("file"))
        return ((List<String>)param1IHTTPSession.getParameters().get("file")).get(0); 
      throw new FilenameNotProvidedException();
    }
    
    protected String getFileName(String param1String) {
      return param1String;
    }
    
    public final NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: getstatic org/firstinspires/ftc/robotserver/internal/webserver/RobotControllerWebHandlers.DEBUG : Z
      //   5: ifeq -> 16
      //   8: getstatic org/firstinspires/ftc/robotserver/internal/webserver/RobotControllerWebHandlers.TAG : Ljava/lang/String;
      //   11: ldc 'In FileUpload'
      //   13: invokestatic dd : (Ljava/lang/String;Ljava/lang/String;)V
      //   16: new java/util/HashMap
      //   19: dup
      //   20: invokespecial <init> : ()V
      //   23: astore_3
      //   24: aload_1
      //   25: aload_3
      //   26: invokeinterface parseBody : (Ljava/util/Map;)V
      //   31: aload_0
      //   32: aload_0
      //   33: aload_1
      //   34: invokespecial getUploadedFileName : (Lfi/iki/elonen/NanoHTTPD$IHTTPSession;)Ljava/lang/String;
      //   37: invokevirtual getFileName : (Ljava/lang/String;)Ljava/lang/String;
      //   40: astore_2
      //   41: new java/io/File
      //   44: dup
      //   45: aload_3
      //   46: ldc 'file'
      //   48: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
      //   53: checkcast java/lang/String
      //   56: invokespecial <init> : (Ljava/lang/String;)V
      //   59: astore_3
      //   60: aload_0
      //   61: aload_2
      //   62: aload_3
      //   63: invokevirtual provideDestinationDirectory : (Ljava/lang/String;Ljava/io/File;)Ljava/io/File;
      //   66: astore_1
      //   67: new java/io/File
      //   70: dup
      //   71: aload_1
      //   72: aload_2
      //   73: invokespecial <init> : (Ljava/io/File;Ljava/lang/String;)V
      //   76: astore_2
      //   77: aload_0
      //   78: aload_1
      //   79: invokespecial checkDir : (Ljava/io/File;)Z
      //   82: ifne -> 126
      //   85: getstatic org/firstinspires/ftc/robotserver/internal/webserver/RobotControllerWebHandlers.TAG : Ljava/lang/String;
      //   88: astore_2
      //   89: new java/lang/StringBuilder
      //   92: dup
      //   93: invokespecial <init> : ()V
      //   96: astore_3
      //   97: aload_3
      //   98: ldc 'Could not access upload location '
      //   100: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   103: pop
      //   104: aload_3
      //   105: aload_1
      //   106: invokevirtual getAbsolutePath : ()Ljava/lang/String;
      //   109: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   112: pop
      //   113: aload_2
      //   114: aload_3
      //   115: invokevirtual toString : ()Ljava/lang/String;
      //   118: invokestatic internalErrorResponse : (Ljava/lang/String;Ljava/lang/String;)Lfi/iki/elonen/NanoHTTPD$Response;
      //   121: astore_1
      //   122: aload_0
      //   123: monitorexit
      //   124: aload_1
      //   125: areturn
      //   126: aload_3
      //   127: aload_2
      //   128: invokevirtual renameTo : (Ljava/io/File;)Z
      //   131: ifne -> 147
      //   134: getstatic org/firstinspires/ftc/robotserver/internal/webserver/RobotControllerWebHandlers.TAG : Ljava/lang/String;
      //   137: ldc 'Failed to move file to correct location'
      //   139: invokestatic internalErrorResponse : (Ljava/lang/String;Ljava/lang/String;)Lfi/iki/elonen/NanoHTTPD$Response;
      //   142: astore_1
      //   143: aload_0
      //   144: monitorexit
      //   145: aload_1
      //   146: areturn
      //   147: aload_0
      //   148: aload_2
      //   149: invokevirtual hook : (Ljava/io/File;)Lfi/iki/elonen/NanoHTTPD$Response;
      //   152: astore_1
      //   153: aload_0
      //   154: monitorexit
      //   155: aload_1
      //   156: areturn
      //   157: getstatic org/firstinspires/ftc/robotserver/internal/webserver/RobotControllerWebHandlers.TAG : Ljava/lang/String;
      //   160: aload_1
      //   161: ldc 'Failed to retrieve uploaded file'
      //   163: invokestatic ee : (Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;)V
      //   166: getstatic org/firstinspires/ftc/robotserver/internal/webserver/RobotControllerWebHandlers.TAG : Ljava/lang/String;
      //   169: ldc 'Upload failed, try again'
      //   171: invokestatic internalErrorResponse : (Ljava/lang/String;Ljava/lang/String;)Lfi/iki/elonen/NanoHTTPD$Response;
      //   174: astore_1
      //   175: aload_0
      //   176: monitorexit
      //   177: aload_1
      //   178: areturn
      //   179: astore_1
      //   180: aload_0
      //   181: monitorexit
      //   182: aload_1
      //   183: athrow
      //   184: astore_1
      //   185: goto -> 157
      //   188: astore_1
      //   189: goto -> 157
      //   192: astore_1
      //   193: goto -> 157
      // Exception table:
      //   from	to	target	type
      //   2	16	179	finally
      //   16	24	179	finally
      //   24	122	192	java/io/IOException
      //   24	122	188	fi/iki/elonen/NanoHTTPD$ResponseException
      //   24	122	184	org/firstinspires/ftc/robotserver/internal/webserver/FilenameNotProvidedException
      //   24	122	179	finally
      //   122	124	179	finally
      //   126	143	192	java/io/IOException
      //   126	143	188	fi/iki/elonen/NanoHTTPD$ResponseException
      //   126	143	184	org/firstinspires/ftc/robotserver/internal/webserver/FilenameNotProvidedException
      //   126	143	179	finally
      //   143	145	179	finally
      //   147	153	192	java/io/IOException
      //   147	153	188	fi/iki/elonen/NanoHTTPD$ResponseException
      //   147	153	184	org/firstinspires/ftc/robotserver/internal/webserver/FilenameNotProvidedException
      //   147	153	179	finally
      //   153	155	179	finally
      //   157	177	179	finally
      //   180	182	179	finally
    }
    
    public abstract NanoHTTPD.Response hook(File param1File);
    
    public abstract File provideDestinationDirectory(String param1String, File param1File);
  }
  
  public static class ListLogFiles implements WebHandler {
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      // Byte code:
      //   0: aload_0
      //   1: monitorenter
      //   2: new java/util/ArrayList
      //   5: dup
      //   6: invokespecial <init> : ()V
      //   9: astore_1
      //   10: invokestatic getDefContext : ()Landroid/content/Context;
      //   13: invokestatic getExtantLogFiles : (Landroid/content/Context;)Ljava/util/List;
      //   16: invokeinterface iterator : ()Ljava/util/Iterator;
      //   21: astore_2
      //   22: aload_2
      //   23: invokeinterface hasNext : ()Z
      //   28: ifeq -> 104
      //   31: aload_2
      //   32: invokeinterface next : ()Ljava/lang/Object;
      //   37: checkcast java/io/File
      //   40: astore_3
      //   41: invokestatic getInstance : ()Lorg/firstinspires/ftc/robotcore/internal/system/AppUtil;
      //   44: getstatic org/firstinspires/ftc/robotserver/internal/webserver/RobotControllerWebHandlers$FileDownload.fileRoot : Ljava/io/File;
      //   47: aload_3
      //   48: invokevirtual getAbsoluteFile : ()Ljava/io/File;
      //   51: invokevirtual getRelativePath : (Ljava/io/File;Ljava/io/File;)Ljava/io/File;
      //   54: astore #4
      //   56: aload #4
      //   58: invokevirtual isAbsolute : ()Z
      //   61: ifeq -> 89
      //   64: getstatic org/firstinspires/ftc/robotserver/internal/webserver/RobotControllerWebHandlers.TAG : Ljava/lang/String;
      //   67: ldc 'internal error: %s not under %s'
      //   69: iconst_2
      //   70: anewarray java/lang/Object
      //   73: dup
      //   74: iconst_0
      //   75: aload_3
      //   76: aastore
      //   77: dup
      //   78: iconst_1
      //   79: getstatic org/firstinspires/ftc/robotserver/internal/webserver/RobotControllerWebHandlers$FileDownload.fileRoot : Ljava/io/File;
      //   82: aastore
      //   83: invokestatic ee : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
      //   86: goto -> 22
      //   89: aload_1
      //   90: aload #4
      //   92: invokevirtual getPath : ()Ljava/lang/String;
      //   95: invokeinterface add : (Ljava/lang/Object;)Z
      //   100: pop
      //   101: goto -> 22
      //   104: invokestatic getInstance : ()Lcom/google/gson/Gson;
      //   107: aload_1
      //   108: invokevirtual toJson : (Ljava/lang/Object;)Ljava/lang/String;
      //   111: astore_1
      //   112: getstatic fi/iki/elonen/NanoHTTPD$Response$Status.OK : Lfi/iki/elonen/NanoHTTPD$Response$Status;
      //   115: ldc 'json'
      //   117: invokestatic getMimeType : (Ljava/lang/String;)Ljava/lang/String;
      //   120: aload_1
      //   121: invokestatic newFixedLengthResponse : (Lfi/iki/elonen/NanoHTTPD$Response$IStatus;Ljava/lang/String;Ljava/lang/String;)Lfi/iki/elonen/NanoHTTPD$Response;
      //   124: astore_1
      //   125: aload_0
      //   126: monitorexit
      //   127: aload_1
      //   128: areturn
      //   129: astore_1
      //   130: aload_0
      //   131: monitorexit
      //   132: aload_1
      //   133: athrow
      // Exception table:
      //   from	to	target	type
      //   2	22	129	finally
      //   22	86	129	finally
      //   89	101	129	finally
      //   104	127	129	finally
      //   130	132	129	finally
    }
  }
  
  private static class OtaUpdate extends ControlHubUpdaterUpload {
    protected OtaUpdate(ChUpdaterCommManager param1ChUpdaterCommManager) {
      super(ChUpdaterCommManager.UpdateType.OTA, param1ChUpdaterCommManager);
    }
    
    public File provideDestinationDirectory(String param1String, File param1File) {
      return AppUtil.OTA_UPDATE_DIR;
    }
  }
  
  private static final class PerformRevFirmwareUpdate implements WebHandler {
    private PerformRevFirmwareUpdate() {}
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) {
      RobotCoreCommandList.FWImage fWImage;
      if (param1IHTTPSession.getMethod() != NanoHTTPD.Method.POST)
        return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "Only POST method is supported"); 
      List<String> list = (List)param1IHTTPSession.getParameters().get("serialNumber");
      if (list == null)
        return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "serialNumber parameter not provided"); 
      SerialNumber serialNumber = SerialNumber.fromString(list.get(0));
      if (param1IHTTPSession.getParameters().containsKey("filename")) {
        fWImage = new RobotCoreCommandList.FWImage(new File(AppUtil.LYNX_FIRMWARE_UPDATE_DIR, ((List<String>)param1IHTTPSession.getParameters().get("filename")).get(0)), false);
      } else {
        fWImage = IncludedFirmwareFileInfo.FW_IMAGE;
      } 
      RobotCoreCommandList.LynxFirmwareUpdate lynxFirmwareUpdate = new RobotCoreCommandList.LynxFirmwareUpdate();
      lynxFirmwareUpdate.firmwareImageFile = fWImage;
      lynxFirmwareUpdate.serialNumber = serialNumber;
      lynxFirmwareUpdate.originatorId = UUID.randomUUID().toString();
      null = new Command("CMD_LYNX_FIRMWARE_UPDATE", lynxFirmwareUpdate.serialize());
      FirmwareUpdateResultReceiver firmwareUpdateResultReceiver = new FirmwareUpdateResultReceiver(lynxFirmwareUpdate.originatorId);
      NetworkConnectionHandler.getInstance().pushReceiveLoopCallback((RecvLoopRunnable.RecvLoopCallback)firmwareUpdateResultReceiver);
      NetworkConnectionHandler.getInstance().injectReceivedCommand(null);
      try {
        String str = firmwareUpdateResultReceiver.didUpdateSucceed();
        return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "application/json", str);
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
        return RobotWebHandlerManager.INTERNAL_ERROR_RESPONSE;
      } finally {
        NetworkConnectionHandler.getInstance().removeReceiveLoopCallback((RecvLoopRunnable.RecvLoopCallback)firmwareUpdateResultReceiver);
      } 
    }
    
    private static final class FirmwareUpdateResultReceiver extends RecvLoopRunnable.DegenerateCallback {
      private final CountDownLatch latch = new CountDownLatch(1);
      
      private final String originatorId;
      
      volatile String result = null;
      
      public FirmwareUpdateResultReceiver(String param2String) {
        this.originatorId = param2String;
      }
      
      public CallbackResult commandEvent(Command param2Command) {
        if ("CMD_LYNX_FIRMWARE_UPDATE_RESP".equals(param2Command.getName())) {
          RobotCoreCommandList.LynxFirmwareUpdateResp lynxFirmwareUpdateResp = RobotCoreCommandList.LynxFirmwareUpdateResp.deserialize(param2Command.getExtra());
          if (this.originatorId.equals(lynxFirmwareUpdateResp.originatorId)) {
            lynxFirmwareUpdateResp.originatorId = null;
            this.result = lynxFirmwareUpdateResp.serialize();
            this.latch.countDown();
            return CallbackResult.HANDLED;
          } 
        } 
        return CallbackResult.NOT_HANDLED;
      }
      
      public String didUpdateSucceed() throws InterruptedException {
        this.latch.await();
        return this.result;
      }
    }
  }
  
  private static final class FirmwareUpdateResultReceiver extends RecvLoopRunnable.DegenerateCallback {
    private final CountDownLatch latch = new CountDownLatch(1);
    
    private final String originatorId;
    
    volatile String result = null;
    
    public FirmwareUpdateResultReceiver(String param1String) {
      this.originatorId = param1String;
    }
    
    public CallbackResult commandEvent(Command param1Command) {
      if ("CMD_LYNX_FIRMWARE_UPDATE_RESP".equals(param1Command.getName())) {
        RobotCoreCommandList.LynxFirmwareUpdateResp lynxFirmwareUpdateResp = RobotCoreCommandList.LynxFirmwareUpdateResp.deserialize(param1Command.getExtra());
        if (this.originatorId.equals(lynxFirmwareUpdateResp.originatorId)) {
          lynxFirmwareUpdateResp.originatorId = null;
          this.result = lynxFirmwareUpdateResp.serialize();
          this.latch.countDown();
          return CallbackResult.HANDLED;
        } 
      } 
      return CallbackResult.NOT_HANDLED;
    }
    
    public String didUpdateSucceed() throws InterruptedException {
      this.latch.await();
      return this.result;
    }
  }
  
  public static class Reboot implements WebHandler {
    private static final boolean ENABLE_REBOOT = true;
    
    public static final String TAG = Reboot.class.getSimpleName();
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) {
      if (LynxConstants.isRevControlHub()) {
        ThreadPool.getDefault().submit(new Runnable() {
              public void run() {
                try {
                  AppUtil.getInstance().showToast(UILocation.BOTH, AppUtil.getDefContext().getString(R.string.toastRebootRC));
                  Thread.sleep(1000L);
                  (new ProcessBuilder(new String[] { "reboot" })).start();
                  return;
                } catch (IOException iOException) {
                  RobotLog.ee(RobotControllerWebHandlers.Reboot.TAG, iOException, "unable to process reboot request");
                  return;
                } catch (InterruptedException interruptedException) {
                  Thread.currentThread().interrupt();
                  return;
                } 
              }
            });
        return RobotWebHandlerManager.OK_RESPONSE;
      } 
      return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.UNAUTHORIZED, "text/plain", "Rebooting supported only on REV Control Hub");
    }
  }
  
  class null implements Runnable {
    public void run() {
      try {
        AppUtil.getInstance().showToast(UILocation.BOTH, AppUtil.getDefContext().getString(R.string.toastRebootRC));
        Thread.sleep(1000L);
        (new ProcessBuilder(new String[] { "reboot" })).start();
        return;
      } catch (IOException iOException) {
        RobotLog.ee(RobotControllerWebHandlers.Reboot.TAG, iOException, "unable to process reboot request");
        return;
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
        return;
      } 
    }
  }
  
  public static class Redirection implements WebHandler {
    private final QueryParameters queryParameters;
    
    private final String targetURI;
    
    public Redirection(String param1String) {
      this(param1String, QueryParameters.PRESERVE);
    }
    
    public Redirection(String param1String, QueryParameters param1QueryParameters) {
      this.targetURI = param1String;
      this.queryParameters = param1QueryParameters;
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) {
      String str1;
      String str3 = this.targetURI;
      if (param1IHTTPSession.getQueryParameterString() != null && param1IHTTPSession.getQueryParameterString().length() > 0) {
        str1 = param1IHTTPSession.getQueryParameterString();
      } else {
        str1 = null;
      } 
      String str2 = str3;
      if (this.queryParameters == QueryParameters.PRESERVE) {
        str2 = str3;
        if (str1 != null) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(str3);
          stringBuilder.append("?");
          stringBuilder.append(str1);
          str2 = stringBuilder.toString();
        } 
      } 
      if (RobotControllerWebHandlers.DEBUG) {
        str3 = param1IHTTPSession.getUri();
        String str = str3;
        if (str1 != null) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append(str3);
          stringBuilder.append("?");
          stringBuilder.append(str1);
          str = stringBuilder.toString();
        } 
        RobotLog.dd(RobotControllerWebHandlers.TAG, "In Redirect from='%s' to='%s'", new Object[] { str, str2 });
      } 
      NanoHTTPD.Response response = NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.TEMPORARY_REDIRECT, "text/plain", "");
      response.addHeader("Location", str2);
      return response;
    }
    
    public enum QueryParameters {
      DISCARD, PRESERVE;
      
      static {
        QueryParameters queryParameters = new QueryParameters("DISCARD", 1);
        DISCARD = queryParameters;
        $VALUES = new QueryParameters[] { PRESERVE, queryParameters };
      }
    }
  }
  
  public enum QueryParameters {
    DISCARD, PRESERVE;
    
    static {
      QueryParameters queryParameters = new QueryParameters("DISCARD", 1);
      DISCARD = queryParameters;
      $VALUES = new QueryParameters[] { PRESERVE, queryParameters };
    }
  }
  
  public static abstract class RequireNameHandler implements WebHandler {
    public final NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      Map map = param1IHTTPSession.getParameters();
      if (map.containsKey("name")) {
        String str = ((List<String>)map.get("name")).get(0);
      } else {
        map = null;
      } 
      return (map == null) ? NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "Bad Request: name parameter is required") : ((map.length() == 0) ? NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.BAD_REQUEST, "text/plain", "Bad Request: name must be non-empty") : getResponse(param1IHTTPSession, (String)map));
    }
    
    protected abstract NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession, String param1String) throws IOException, NanoHTTPD.ResponseException;
  }
  
  private static final class RevHubsAvailableForUpdate implements WebHandler {
    private RevHubsAvailableForUpdate() {}
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) {
      if (!NetworkConnectionHandler.getInstance().readyForCommandProcessing()) {
        NanoHTTPD.Response response = NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.SERVICE_UNAVAILABLE, "text/plain", "Currently unable to scan for REV Hubs");
        response.addHeader("Retry-After", "5");
        return response;
      } 
      UsbAccessibleLynxModulesReceiver usbAccessibleLynxModulesReceiver = new UsbAccessibleLynxModulesReceiver();
      RobotCoreCommandList.USBAccessibleLynxModulesRequest uSBAccessibleLynxModulesRequest = new RobotCoreCommandList.USBAccessibleLynxModulesRequest();
      uSBAccessibleLynxModulesRequest.forFirmwareUpdate = true;
      null = new Command("CMD_GET_USB_ACCESSIBLE_LYNX_MODULES", uSBAccessibleLynxModulesRequest.serialize());
      NetworkConnectionHandler.getInstance().pushReceiveLoopCallback((RecvLoopRunnable.RecvLoopCallback)usbAccessibleLynxModulesReceiver);
      NetworkConnectionHandler.getInstance().injectReceivedCommand(null);
      try {
        String str = usbAccessibleLynxModulesReceiver.waitForUsbAccessibleLynxModules();
        return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "application/json", str);
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
        return RobotWebHandlerManager.INTERNAL_ERROR_RESPONSE;
      } finally {
        NetworkConnectionHandler.getInstance().removeReceiveLoopCallback((RecvLoopRunnable.RecvLoopCallback)usbAccessibleLynxModulesReceiver);
      } 
    }
    
    private static final class UsbAccessibleLynxModulesReceiver extends RecvLoopRunnable.DegenerateCallback {
      private final CountDownLatch latch = new CountDownLatch(1);
      
      volatile String usbAccessibleLynxModules;
      
      private UsbAccessibleLynxModulesReceiver() {}
      
      public CallbackResult commandEvent(Command param2Command) {
        if ("CMD_GET_USB_ACCESSIBLE_LYNX_MODULES_RESP".equals(param2Command.getName())) {
          this.usbAccessibleLynxModules = param2Command.getExtra();
          this.latch.countDown();
          return CallbackResult.HANDLED_CONTINUE;
        } 
        return CallbackResult.NOT_HANDLED;
      }
      
      public String waitForUsbAccessibleLynxModules() throws InterruptedException {
        this.latch.await();
        return this.usbAccessibleLynxModules;
      }
    }
  }
  
  private static final class UsbAccessibleLynxModulesReceiver extends RecvLoopRunnable.DegenerateCallback {
    private final CountDownLatch latch = new CountDownLatch(1);
    
    volatile String usbAccessibleLynxModules;
    
    private UsbAccessibleLynxModulesReceiver() {}
    
    public CallbackResult commandEvent(Command param1Command) {
      if ("CMD_GET_USB_ACCESSIBLE_LYNX_MODULES_RESP".equals(param1Command.getName())) {
        this.usbAccessibleLynxModules = param1Command.getExtra();
        this.latch.countDown();
        return CallbackResult.HANDLED_CONTINUE;
      } 
      return CallbackResult.NOT_HANDLED;
    }
    
    public String waitForUsbAccessibleLynxModules() throws InterruptedException {
      this.latch.await();
      return this.usbAccessibleLynxModules;
    }
  }
  
  public static class RobotControllerConfiguration implements WebHandler {
    public static void appendVariable(StringBuilder param1StringBuilder, String param1String1, String param1String2) {
      param1StringBuilder.append("var ");
      param1StringBuilder.append(param1String1);
      param1StringBuilder.append(" = '");
      param1StringBuilder.append(param1String2);
      param1StringBuilder.append("';\n");
    }
    
    protected void appendVariables(StringBuilder param1StringBuilder) {
      appendVariable(param1StringBuilder, "URI_ANON_PING", "/anonymousPing");
      appendVariable(param1StringBuilder, "URI_PING", "/ping");
      appendVariable(param1StringBuilder, "URI_LIST_LOG_FILES", "/listLogs");
      appendVariable(param1StringBuilder, "URI_DOWNLOAD_FILE", "/downloadFile");
      appendVariable(param1StringBuilder, "URI_CHANGE_NETWORK_SETTINGS", "/changeNetworkSettings");
      appendVariable(param1StringBuilder, "URI_UPLOAD_EXPANSION_HUB_FIRMWARE", "/uploadExpansionHubFirmware");
      appendVariable(param1StringBuilder, "URI_REV_HUBS_AVAILABLE_FOR_UPDATE", "/revHubsAvailableForUpdate");
      appendVariable(param1StringBuilder, "URI_PERFORM_REV_FIRMWARE_UPDATE", "/performRevFirmwareUpdate");
      appendVariable(param1StringBuilder, "URI_UPDATE_CONTROL_HUB_APK", "/updateControlHubAPK");
      appendVariable(param1StringBuilder, "URI_UPLOAD_WEBCAM_CALIBRATION_FILE", "/uploadWebcamCalibrationFile");
      appendVariable(param1StringBuilder, "URI_UPLOAD_TFLITE_MODEL_FILE", "/uploadTfliteModelFile");
      appendVariable(param1StringBuilder, "URI_UPLOAD_CONTROL_HUB_OTA", "/uploadControlHubOta");
      appendVariable(param1StringBuilder, "URI_NAV_HOME", "/connection.html");
      appendVariable(param1StringBuilder, "URI_NAV_MANAGE", "/manage.html");
      appendVariable(param1StringBuilder, "URI_NAV_HELP", "/help.html");
      appendVariable(param1StringBuilder, "URI_RC_INFO", "/js/rcInfo.json");
      appendVariable(param1StringBuilder, "URI_REBOOT", "/reboot");
      appendVariable(param1StringBuilder, "URI_COLORS", "/css/colors.less");
      appendVariable(param1StringBuilder, "PARAM_NAME", "name");
      appendVariable(param1StringBuilder, "PARAM_AP_PASSWORD", "password");
      appendVariable(param1StringBuilder, "PARAM_CHANNEL_NAME", "channelName");
      appendVariable(param1StringBuilder, "PARAM_NEW_NAME", "new_name");
      appendVariable(param1StringBuilder, "PARAM_MESSAGE", "message");
      appendVariable(param1StringBuilder, "PARAM_SERIAL_NUMBER", "serialNumber");
      appendVariable(param1StringBuilder, "PARAM_FILENAME", "filename");
      appendVariable(param1StringBuilder, "URI_EXIT_PROGRAM_AND_MANAGE", "/exitProgramAndManage");
      appendVariable(param1StringBuilder, "URI_TOAST", "/toast");
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      StringBuilder stringBuilder = new StringBuilder();
      appendVariables(stringBuilder);
      return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, "application/javascript", stringBuilder.toString());
    }
  }
  
  public static class RobotControllerInfoHandler implements WebHandler {
    private final WebServer webServer;
    
    public RobotControllerInfoHandler(WebServer param1WebServer) {
      this.webServer = param1WebServer;
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      if (RobotControllerWebHandlers.DEBUG)
        RobotLog.dd(RobotControllerWebHandlers.TAG, "RCInfoHandler"); 
      RobotControllerWebInfo robotControllerWebInfo = this.webServer.getConnectionInformation();
      robotControllerWebInfo.setFtcUserAgentCategory(param1IHTTPSession.getHeaders());
      String str = robotControllerWebInfo.toJson();
      return NoCachingWebHandler.setNoCache(param1IHTTPSession, NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, MimeTypesUtil.getMimeType("json"), str));
    }
  }
  
  private static class ServerRootIndex implements WebHandler {
    private final AssetManager assets;
    
    private final String index;
    
    public ServerRootIndex(String param1String) {
      this.index = param1String;
      this.assets = AppUtil.getInstance().getApplication().getAssets();
    }
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) throws IOException, NanoHTTPD.ResponseException {
      try {
        InputStream inputStream = this.assets.open(this.index);
        String str = MimeTypesUtil.determineMimeType(this.index);
        return NanoHTTPD.newChunkedResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.OK, str, inputStream);
      } catch (Exception exception) {
        return NanoHTTPD.newFixedLengthResponse((NanoHTTPD.Response.IStatus)NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "Internal Error");
      } 
    }
  }
  
  public static class SimpleSuccess implements WebHandler {
    public static final String TAG = SimpleSuccess.class.getSimpleName();
    
    public NanoHTTPD.Response getResponse(NanoHTTPD.IHTTPSession param1IHTTPSession) {
      return RobotWebHandlerManager.OK_RESPONSE;
    }
  }
  
  private static final class StandardUpload extends FileUpload {
    private final File destinationDir;
    
    StandardUpload(String param1String) {
      this.destinationDir = new File(param1String);
    }
    
    public NanoHTTPD.Response hook(File param1File) {
      return NanoHTTPD.newFixedLengthResponse(param1File.getName());
    }
    
    public File provideDestinationDirectory(String param1String, File param1File) {
      return this.destinationDir;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotserver\internal\webserver\RobotControllerWebHandlers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
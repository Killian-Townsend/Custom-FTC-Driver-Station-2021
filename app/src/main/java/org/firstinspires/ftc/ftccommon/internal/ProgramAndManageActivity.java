package org.firstinspires.ftc.ftccommon.internal;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.webkit.ConsoleMessage;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import com.qualcomm.ftccommon.R;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.File;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.ui.LocalByRefRequestCodeHolder;
import org.firstinspires.ftc.robotcore.internal.ui.ThemedActivity;
import org.firstinspires.ftc.robotcore.internal.ui.UILocation;
import org.firstinspires.ftc.robotcore.internal.webserver.FtcUserAgentCategory;
import org.firstinspires.ftc.robotcore.internal.webserver.RobotControllerWebInfo;
import org.firstinspires.ftc.robotserver.internal.webserver.AppThemeColors;
import org.firstinspires.ftc.robotserver.internal.webserver.MimeTypesUtil;
import org.firstinspires.inspection.R;

public class ProgramAndManageActivity extends ThemedActivity {
  public static final String TAG = "Console";
  
  final ProgramAndManageDownloadListener downloadListener = new ProgramAndManageDownloadListener();
  
  final ProgramAndManageWebChromeClient webChromeClient = new ProgramAndManageWebChromeClient();
  
  protected RobotControllerWebInfo webInfo;
  
  protected WebView webView;
  
  final ProgramAndManageWebViewClient webViewClient = new ProgramAndManageWebViewClient();
  
  protected FrameLayout getBackBar() {
    return (FrameLayout)findViewById(R.id.backbar);
  }
  
  public String getTag() {
    return "Console";
  }
  
  protected String getUrlParam(String paramString1, String paramString2) {
    UrlQuerySanitizer urlQuerySanitizer = new UrlQuerySanitizer();
    urlQuerySanitizer.setAllowUnregisteredParamaters(true);
    urlQuerySanitizer.registerParameter(paramString2, UrlQuerySanitizer.getAllButNulLegal());
    urlQuerySanitizer.parseUrl(paramString1);
    return urlQuerySanitizer.getValue(paramString2);
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    RobotLog.vv("Console", "onActivityResult() requestCode=%d resultCode=%d data=%s", new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), paramIntent });
    LocalByRefRequestCodeHolder localByRefRequestCodeHolder = LocalByRefRequestCodeHolder.from(paramInt1);
    if (localByRefRequestCodeHolder != null) {
      paramInt1 = localByRefRequestCodeHolder.getUserRequestCode();
      if (paramInt1 == RequestCode.CHOOSE_FILE.ordinal()) {
        Uri[] arrayOfUri;
        Uri uri = null;
        if (paramIntent == null || paramInt2 != -1) {
          paramIntent = null;
        } else {
          Uri[] arrayOfUri1 = new Uri[1];
          arrayOfUri1[0] = paramIntent.getData();
          arrayOfUri = arrayOfUri1;
        } 
        if (arrayOfUri != null)
          uri = arrayOfUri[0]; 
        RobotLog.vv("Console", "CHOOSE_FILE result=%s", new Object[] { uri });
        ((ValueCallback)localByRefRequestCodeHolder.getTargetAndForget()).onReceiveValue(arrayOfUri);
        return;
      } 
      RobotLog.ee("Console", "onActivityResult() user requestCode=%d: unexpected", new Object[] { Integer.valueOf(paramInt1) });
      return;
    } 
    RobotLog.ee("Console", "onActivityResult() actual requestCode=%d: unexpected", new Object[] { Integer.valueOf(paramInt1) });
  }
  
  public void onCreate(Bundle paramBundle) {
    RobotLog.vv("Console", "onCreate()");
    super.onCreate(paramBundle);
    this.downloadListener.register();
    setContentView(R.layout.activity_program_and_manage);
    this.webInfo = RobotControllerWebInfo.fromJson(getIntent().getStringExtra("RC_WEB_INFO"));
    WebView webView = (WebView)findViewById(R.id.webView);
    this.webView = webView;
    WebSettings webSettings = webView.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webSettings.setDomStorageEnabled(true);
    webSettings.setUserAgentString(FtcUserAgentCategory.addToUserAgent(WebSettings.getDefaultUserAgent((Context)this)));
    this.webView.setWebChromeClient(this.webChromeClient);
    this.webView.setWebViewClient(this.webViewClient);
    this.webView.setDownloadListener(this.downloadListener);
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    hashMap.put("Ftc-RCConsole-Theme", AppThemeColors.toHeader(AppThemeColors.fromTheme().toLess()));
    this.webView.loadUrl(this.webInfo.getServerUrl(), hashMap);
  }
  
  protected void onDestroy() {
    RobotLog.vv("Console", "onDestroy()");
    super.onDestroy();
    this.downloadListener.unregister();
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    if (paramKeyEvent.getAction() == 0 && paramInt == 4 && this.webView.canGoBack()) {
      this.webView.goBack();
      return true;
    } 
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  protected void onPause() {
    RobotLog.vv("Console", "onPause()");
    super.onPause();
  }
  
  protected void onResume() {
    RobotLog.vv("Console", "onResume()");
    super.onResume();
  }
  
  protected AppUtil.DialogContext showAlert(String paramString, Consumer<AppUtil.DialogContext> paramConsumer) {
    AppUtil.DialogParams dialogParams = new AppUtil.DialogParams(UILocation.ONLY_LOCAL, getString(R.string.alertTitleRobotControllerConsole), paramString);
    dialogParams.activity = (Activity)this;
    dialogParams.flavor = AppUtil.DialogFlavor.ALERT;
    return AppUtil.getInstance().showDialog(dialogParams, paramConsumer);
  }
  
  protected AppUtil.DialogContext showConfirm(String paramString, Consumer<AppUtil.DialogContext> paramConsumer) {
    AppUtil.DialogParams dialogParams = new AppUtil.DialogParams(UILocation.ONLY_LOCAL, getString(R.string.alertTitleRobotControllerConsole), paramString);
    dialogParams.activity = (Activity)this;
    dialogParams.flavor = AppUtil.DialogFlavor.CONFIRM;
    return AppUtil.getInstance().showDialog(dialogParams, paramConsumer);
  }
  
  protected AppUtil.DialogContext showPrompt(String paramString1, String paramString2, Consumer<AppUtil.DialogContext> paramConsumer) {
    AppUtil.DialogParams dialogParams = new AppUtil.DialogParams(UILocation.ONLY_LOCAL, getString(R.string.alertTitleRobotControllerConsole), paramString1);
    dialogParams.activity = (Activity)this;
    dialogParams.flavor = AppUtil.DialogFlavor.PROMPT;
    dialogParams.defaultValue = paramString2;
    return AppUtil.getInstance().showDialog(dialogParams, paramConsumer);
  }
  
  protected class ProgramAndManageDownloadListener extends BroadcastReceiver implements DownloadListener {
    protected Set<Long> outstandingDownloadIds = Collections.newSetFromMap(new ConcurrentHashMap<Long, Boolean>());
    
    public void onDownloadStart(String param1String1, String param1String2, String param1String3, String param1String4, long param1Long) {
      boolean bool;
      if ("/downloadFile".equals(URI.create(param1String1).getPath())) {
        String str1 = ProgramAndManageActivity.this.getUrlParam(param1String1, "name");
        param1String2 = str1;
        if (str1 != null)
          param1String2 = (new File(str1)).getName(); 
      } else {
        param1String2 = null;
      } 
      String str = param1String2;
      if (param1String2 == null)
        str = URLUtil.guessFileName(param1String1, param1String3, param1String4); 
      RobotLog.vv("Console", "onDownloadStart(url=%s disp='%s' mime='%s' length=%d) filename=%s", new Object[] { param1String1, param1String3, param1String4, Long.valueOf(param1Long), str });
      DownloadManager.Request request = new DownloadManager.Request(Uri.parse(param1String1));
      request.setDescription(ProgramAndManageActivity.this.getString(R.string.webViewDownloadRequestDescription));
      request.setTitle(str);
      request.allowScanningByMediaScanner();
      request.setNotificationVisibility(1);
      request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, str);
      param1Long = ((DownloadManager)ProgramAndManageActivity.this.getSystemService("download")).enqueue(request);
      if (param1Long == 0L) {
        bool = true;
      } else {
        bool = false;
      } 
      Assert.assertFalse(bool);
      this.outstandingDownloadIds.add(Long.valueOf(param1Long));
      AppUtil.getInstance().showToast(UILocation.ONLY_LOCAL, ProgramAndManageActivity.this.getString(R.string.toastWebViewDownloadFile, new Object[] { str }));
    }
    
    public void onReceive(Context param1Context, Intent param1Intent) {
      if (param1Intent.getAction().equals("android.intent.action.DOWNLOAD_COMPLETE")) {
        long l = param1Intent.getLongExtra("extra_download_id", 0L);
        if (this.outstandingDownloadIds.remove(Long.valueOf(l)) && this.outstandingDownloadIds.size() == 0)
          AppUtil.getInstance().showToast(UILocation.ONLY_LOCAL, ProgramAndManageActivity.this.getString(R.string.toastWebViewDownloadsComplete)); 
      } 
    }
    
    public void register() {
      ProgramAndManageActivity.this.registerReceiver(this, new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE"));
    }
    
    public void unregister() {
      ProgramAndManageActivity.this.unregisterReceiver(this);
    }
  }
  
  protected class ProgramAndManageWebChromeClient extends WebChromeClient {
    public boolean onConsoleMessage(ConsoleMessage param1ConsoleMessage) {
      URI uRI = URI.create(param1ConsoleMessage.sourceId());
      RobotLog.dd("Console", "%s(%s,%d): %s", new Object[] { param1ConsoleMessage.messageLevel().toString().toLowerCase(Locale.getDefault()), uRI.getPath(), Integer.valueOf(param1ConsoleMessage.lineNumber()), param1ConsoleMessage.message() });
      return true;
    }
    
    public boolean onJsAlert(WebView param1WebView, String param1String1, String param1String2, JsResult param1JsResult) {
      RobotLog.vv("Console", "onJsAlert() message=%s", new Object[] { param1String2 });
      return showJsAlert(param1WebView, param1String1, param1String2, param1JsResult);
    }
    
    public boolean onJsBeforeUnload(WebView param1WebView, String param1String1, String param1String2, JsResult param1JsResult) {
      RobotLog.vv("Console", "onJsBeforeUnload() url=%s message=%s", new Object[] { param1String1, param1String2 });
      return showJsAlert(param1WebView, param1String1, param1String2, param1JsResult);
    }
    
    public boolean onJsConfirm(WebView param1WebView, String param1String1, String param1String2, final JsResult result) {
      RobotLog.vv("Console", "onJsConfirm() url=%s message=%s", new Object[] { param1String1, param1String2 });
      ProgramAndManageActivity.this.showConfirm(param1String2, new Consumer<AppUtil.DialogContext>() {
            public void accept(AppUtil.DialogContext param2DialogContext) {
              boolean bool;
              if (param2DialogContext.getOutcome() == AppUtil.DialogContext.Outcome.UNKNOWN) {
                bool = true;
              } else {
                bool = false;
              } 
              Assert.assertFalse(bool);
              if (param2DialogContext.getOutcome() == AppUtil.DialogContext.Outcome.CONFIRMED) {
                result.confirm();
                return;
              } 
              result.cancel();
            }
          });
      return true;
    }
    
    public boolean onJsPrompt(WebView param1WebView, String param1String1, String param1String2, String param1String3, final JsPromptResult result) {
      RobotLog.vv("Console", "onJsPrompt() url=%s message=%s default=%s", new Object[] { param1String1, param1String2, param1String3 });
      ProgramAndManageActivity.this.showPrompt(param1String2, param1String3, new Consumer<AppUtil.DialogContext>() {
            public void accept(AppUtil.DialogContext param2DialogContext) {
              boolean bool;
              if (param2DialogContext.getOutcome() == AppUtil.DialogContext.Outcome.UNKNOWN) {
                bool = true;
              } else {
                bool = false;
              } 
              Assert.assertFalse(bool);
              if (param2DialogContext.getOutcome() == AppUtil.DialogContext.Outcome.CONFIRMED) {
                result.confirm(param2DialogContext.getText().toString());
                return;
              } 
              result.cancel();
            }
          });
      return true;
    }
    
    public boolean onShowFileChooser(WebView param1WebView, ValueCallback<Uri[]> param1ValueCallback, WebChromeClient.FileChooserParams param1FileChooserParams) {
      String[] arrayOfString = param1FileChooserParams.getAcceptTypes();
      if (arrayOfString.length > 0) {
        String str = arrayOfString[0];
      } else {
        arrayOfString = null;
      } 
      showFileChooser(param1ValueCallback, (String)arrayOfString);
      return true;
    }
    
    public void openFileChooser(final ValueCallback<Uri> uploadFile, String param1String1, String param1String2) {
      RobotLog.vv("Console", "openFileChooser(): acceptType=%s capture=%s", new Object[] { param1String1, param1String2 });
      Assert.assertNotNull(uploadFile);
      showFileChooser(new ValueCallback<Uri[]>() {
            public void onReceiveValue(Uri[] param2ArrayOfUri) {
              ValueCallback valueCallback = uploadFile;
              if (param2ArrayOfUri != null && param2ArrayOfUri.length > 0) {
                Uri uri = param2ArrayOfUri[0];
              } else {
                param2ArrayOfUri = null;
              } 
              valueCallback.onReceiveValue(param2ArrayOfUri);
            }
          },  param1String1);
    }
    
    protected void showFileChooser(ValueCallback<Uri[]> param1ValueCallback, String param1String) {
      String str1;
      Intent intent;
      boolean bool = true;
      RobotLog.vv("Console", "showFileChooser(): type=%s", new Object[] { param1String });
      try {
        intent = new Intent("android.intent.action.GET_CONTENT");
        intent.addCategory("android.intent.category.OPENABLE");
        intent.putExtra("android.intent.extra.LOCAL_ONLY", true);
      } finally {
        param1ValueCallback.onReceiveValue(null);
      } 
      String str2 = param1String;
      if (param1String == null)
        str2 = "*/*"; 
      intent.setType(str2);
      if (intent.resolveActivity(ProgramAndManageActivity.this.getPackageManager()) == null && str1 != null) {
        param1String = MimeTypeMap.getSingleton().getMimeTypeFromExtension(str1);
        if (param1String != null)
          intent.setType(param1String); 
      } 
      if (intent.resolveActivity(ProgramAndManageActivity.this.getPackageManager()) == null && str1 != null) {
        param1String = MimeTypesUtil.getMimeType(str1);
        if (param1String != null)
          intent.setType(param1String); 
      } 
      if (intent.resolveActivity(ProgramAndManageActivity.this.getPackageManager()) == null)
        intent.setType("image/*"); 
      if (intent.resolveActivity(ProgramAndManageActivity.this.getPackageManager()) == null) {
        ProgramAndManageActivity.this.showAlert(ProgramAndManageActivity.this.getString(R.string.alertMessageNoActionGetContent), (Consumer<AppUtil.DialogContext>)null);
        param1ValueCallback.onReceiveValue(null);
        return;
      } 
      LocalByRefRequestCodeHolder localByRefRequestCodeHolder = new LocalByRefRequestCodeHolder(ProgramAndManageActivity.RequestCode.CHOOSE_FILE.ordinal(), param1ValueCallback);
      RobotLog.vv("Console", "showFileChooser(): launching get=%s", new Object[] { intent });
      try {
        ProgramAndManageActivity.this.startActivityForResult(intent, localByRefRequestCodeHolder.getActualRequestCode());
      } catch (ActivityNotFoundException activityNotFoundException) {
        RobotLog.ee("Console", (Throwable)activityNotFoundException, "internal error");
        bool = false;
      } 
      if (!bool)
        param1ValueCallback.onReceiveValue(null); 
    }
    
    protected boolean showJsAlert(WebView param1WebView, String param1String1, String param1String2, final JsResult result) {
      ProgramAndManageActivity.this.showAlert(param1String2, new Consumer<AppUtil.DialogContext>() {
            public void accept(AppUtil.DialogContext param2DialogContext) {
              result.confirm();
            }
          });
      return true;
    }
  }
  
  class null implements Consumer<AppUtil.DialogContext> {
    public void accept(AppUtil.DialogContext param1DialogContext) {
      result.confirm();
    }
  }
  
  class null implements Consumer<AppUtil.DialogContext> {
    public void accept(AppUtil.DialogContext param1DialogContext) {
      boolean bool;
      if (param1DialogContext.getOutcome() == AppUtil.DialogContext.Outcome.UNKNOWN) {
        bool = true;
      } else {
        bool = false;
      } 
      Assert.assertFalse(bool);
      if (param1DialogContext.getOutcome() == AppUtil.DialogContext.Outcome.CONFIRMED) {
        result.confirm();
        return;
      } 
      result.cancel();
    }
  }
  
  class null implements Consumer<AppUtil.DialogContext> {
    public void accept(AppUtil.DialogContext param1DialogContext) {
      boolean bool;
      if (param1DialogContext.getOutcome() == AppUtil.DialogContext.Outcome.UNKNOWN) {
        bool = true;
      } else {
        bool = false;
      } 
      Assert.assertFalse(bool);
      if (param1DialogContext.getOutcome() == AppUtil.DialogContext.Outcome.CONFIRMED) {
        result.confirm(param1DialogContext.getText().toString());
        return;
      } 
      result.cancel();
    }
  }
  
  class null implements ValueCallback<Uri[]> {
    public void onReceiveValue(Uri[] param1ArrayOfUri) {
      ValueCallback valueCallback = uploadFile;
      if (param1ArrayOfUri != null && param1ArrayOfUri.length > 0) {
        Uri uri = param1ArrayOfUri[0];
      } else {
        param1ArrayOfUri = null;
      } 
      valueCallback.onReceiveValue(param1ArrayOfUri);
    }
  }
  
  protected class ProgramAndManageWebViewClient extends WebViewClient {
    public WebResourceResponse shouldInterceptRequest(WebView param1WebView, String param1String) {
      URI uRI = URI.create(param1String);
      if ("/exitProgramAndManage".equals(uRI.getPath())) {
        RobotLog.vv("Console", "exiting Program & Manage");
        ProgramAndManageActivity.this.finish();
        return null;
      } 
      if ("/toast".equals(uRI.getPath())) {
        RobotLog.vv("Console", "toast url=%s", new Object[] { param1String });
        String str = ProgramAndManageActivity.this.getUrlParam(param1String, "message");
        if (str != null)
          AppUtil.getInstance().showToast(UILocation.ONLY_LOCAL, str); 
      } 
      return null;
    }
    
    public boolean shouldOverrideUrlLoading(WebView param1WebView, String param1String) {
      RobotLog.vv("Console", "shouldOverrideUrlLoading() url=%s", new Object[] { param1String });
      return false;
    }
  }
  
  protected enum RequestCode {
    CHOOSE_FILE, NONE;
    
    static {
      RequestCode requestCode = new RequestCode("CHOOSE_FILE", 1);
      CHOOSE_FILE = requestCode;
      $VALUES = new RequestCode[] { NONE, requestCode };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\ftccommon\internal\ProgramAndManageActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */